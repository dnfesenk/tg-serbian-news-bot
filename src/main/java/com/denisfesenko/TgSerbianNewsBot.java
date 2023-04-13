package com.denisfesenko;

import com.denisfesenko.service.DanasService;
import com.denisfesenko.service.MongoService;
import com.denisfesenko.service.OpenAiService;
import com.denisfesenko.service.TelegramSenderService;
import com.denisfesenko.util.Constants;
import com.denisfesenko.util.StringUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
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
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("\uD83D\uDCE3 Дайджест новостей Сербии на ");
            stringBuilder.append(formattedDate);
            stringBuilder.append("\n\n");

            for (Map.Entry<String, String> entry : translatedNews.entrySet()) {
                stringBuilder.append("\uD83D\uDD38 ");
                stringBuilder.append("[").append(StringUtils.escapeString(entry.getValue())).append("]");
                stringBuilder.append("(").append(entry.getKey()).append(")");
                stringBuilder.append("\n\n");
            }

            TelegramSenderService telegramSenderService = new TelegramSenderService(System.getenv("TG_BOT_TOKEN"));
            telegramSenderService.sendMessageToChannel("-1001917579438", stringBuilder.toString());
        }
        stopWatch.stop();

        logger.info("Elapsed Time in seconds: {}", stopWatch.getTime(TimeUnit.SECONDS));
    }
}
