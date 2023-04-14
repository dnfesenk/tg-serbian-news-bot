package com.denisfesenko;

import com.denisfesenko.service.DanasService;
import com.denisfesenko.service.MongoService;
import com.denisfesenko.service.OpenAiService;
import com.denisfesenko.service.TelegramSenderService;
import com.denisfesenko.util.StringUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class TgSerbianNewsBot {

    private static final Logger logger = LoggerFactory.getLogger(TgSerbianNewsBot.class);

    public static void main(String[] args) {
        LocalDateTime now = LocalDateTime.now();
        ZoneId belgradeZone = ZoneId.of("Europe/Belgrade");
        ZonedDateTime belgradeDateTime = now.atZone(belgradeZone);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd\\.MM\\.yyyy HH:mm");
        String formattedDate = belgradeDateTime.format(formatter);

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        Map<String, String> news = DanasService.getNews();
        Map<String, String> filteredNews = MongoService.filterAndAddRecords(news);
        if (!filteredNews.isEmpty()) {
            Map<String, String> translatedNews = new OpenAiService().translate(filteredNews);
            Map<String, String> politics = new HashMap<>();
            Map<String, String> economics = new HashMap<>();
            Map<String, String> society = new HashMap<>();
            Map<String, String> other = new HashMap<>();

            for (Map.Entry<String, String> entry : translatedNews.entrySet()) {
                if (entry.getKey().startsWith("https://www.danas.rs/vesti/politika/")) {
                    politics.put(entry.getKey(), entry.getValue());
                } else if (entry.getKey().contains("https://www.danas.rs/vesti/ekonomija/")) {
                    economics.put(entry.getKey(), entry.getValue());
                } else if (entry.getKey().contains("https://www.danas.rs/vesti/drustvo/")) {
                    society.put(entry.getKey(), entry.getValue());
                } else {
                    other.put(entry.getKey(), entry.getValue());
                }
            }

            sendNewsBlock(politics, "\uD83C\uDFDB️️️ Политика", formattedDate);
            sendNewsBlock(economics, "\uD83D\uDCB0 Экономика", formattedDate);
            sendNewsBlock(society, "\uD83D\uDC65️ Общество", formattedDate);
            sendNewsBlock(other, "\uD83D\uDD39 Прочее", formattedDate);
        }
        stopWatch.stop();

        logger.info("Elapsed Time in seconds: {}", stopWatch.getTime(TimeUnit.SECONDS));
    }

    private static void sendNewsBlock(Map<String, String> newsBlock, String blockTitle, String formattedDate) {
        if (!newsBlock.isEmpty()) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("\uD83D\uDCE3 Дайджест новостей Сербии на ");
            stringBuilder.append(formattedDate);
            stringBuilder.append("\n\n");
            stringBuilder.append(blockTitle);
            stringBuilder.append("\n\n");
            for (Map.Entry<String, String> entry : newsBlock.entrySet()) {
                stringBuilder.append("\uD83D\uDD38 ");
                stringBuilder.append("[").append(StringUtils.escapeString(entry.getValue())).append("]");
                stringBuilder.append("(").append(entry.getKey()).append(")");
                stringBuilder.append("\n\n");
            }
            TelegramSenderService telegramSenderService = new TelegramSenderService(System.getenv("TG_BOT_TOKEN"));
            telegramSenderService.sendMessageToChannel("-1001917579438", stringBuilder.toString());
        }
    }
}
