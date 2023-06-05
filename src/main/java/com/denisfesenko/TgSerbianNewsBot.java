package com.denisfesenko;

import com.denisfesenko.service.DanasService;
import com.denisfesenko.service.GoogleTranslateService;
import com.denisfesenko.service.MongoService;
import com.denisfesenko.service.TelegramSenderService;
import com.denisfesenko.util.Constants;
import com.denisfesenko.util.GoogleCredentialsHelper;
import com.denisfesenko.util.Utils;
import com.google.auth.oauth2.GoogleCredentials;
import com.pengrad.telegrambot.TelegramBot;
import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class TgSerbianNewsBot {

    private static final Logger logger = LoggerFactory.getLogger(TgSerbianNewsBot.class);

    public static void main(String[] args) {
        String formattedDate = Utils.getCurrentBelgradeDateTimeAsString();

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        Map<String, String> news = DanasService.getNews();
        Map<String, String> filteredNews = MongoService.filterAndAddRecords(news);
        if (!filteredNews.isEmpty()) {
            GoogleCredentials googleCredentials = null;
            try {
                googleCredentials = GoogleCredentialsHelper.createFromBase64EvnVar();
            } catch (IOException e) {
                logger.error("Error fetching {}", Constants.GOOGLE_APPLICATION_CREDENTIALS, e);
                System.exit(1);
            }

            Map<String, String> translatedNews = new GoogleTranslateService(googleCredentials).translate(filteredNews);
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

            TelegramBot bot = new TelegramBot(Constants.TG_BOT_TOKEN);
            TelegramSenderService telegramSenderService = new TelegramSenderService(bot);
            telegramSenderService.sendNewsBlock(politics, Constants.POLITICS_BLOCK_TITLE, formattedDate);
            telegramSenderService.sendNewsBlock(economics, Constants.ECONOMICS_BLOCK_TITLE, formattedDate);
            telegramSenderService.sendNewsBlock(society, Constants.SOCIETY_BLOCK_TITLE, formattedDate);
            telegramSenderService.sendNewsBlock(other, Constants.OTHER_BLOCK_TITLE, formattedDate);
        }
        stopWatch.stop();

        logger.info("Elapsed Time in seconds: {}", stopWatch.getTime(TimeUnit.SECONDS));
    }
}
