package com.denisfesenko.service;

import com.denisfesenko.util.Constants;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TelegramSenderService {
    private static final Logger logger = LoggerFactory.getLogger(TelegramSenderService.class);

    public static void sendMessageToChannel(String channelId, String message) {
        TelegramBot bot = new TelegramBot(Constants.TG_BOT_TOKEN);
        SendMessage request = new SendMessage(channelId, message).parseMode(ParseMode.MarkdownV2).disableWebPagePreview(true);
        SendResponse response = bot.execute(request);
        if (response.message() != null) {
            logger.info("Telegram API response: {}", response);
        } else {
            String responseDescription = response.description();
            logger.warn("Telegram API response: {}", responseDescription);
        }
    }

    private TelegramSenderService() {
    }
}
