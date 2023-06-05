package com.denisfesenko.service;

import com.denisfesenko.util.Constants;
import com.denisfesenko.util.Utils;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class TelegramSenderService {

    private static final Logger logger = LoggerFactory.getLogger(TelegramSenderService.class);

    private final TelegramBot bot;

    public TelegramSenderService(TelegramBot bot) {
        this.bot = bot;
    }

    public void sendNewsBlock(Map<String, String> newsBlock, String blockTitle, String formattedDate) {
        if (!newsBlock.isEmpty()) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(Constants.SERBIAN_NEWS_DIGEST);
            stringBuilder.append(formattedDate);
            stringBuilder.append("\n\n");
            stringBuilder.append(blockTitle);
            stringBuilder.append("\n\n");

            int currentMessageIndex = 1; // Keep track of current message index.
            for (Map.Entry<String, String> entry : newsBlock.entrySet()) {
                String currentEntry = "\uD83D\uDD38 " + "[" + Utils.escapeTgString(entry.getValue()) + "]" + "("
                        + entry.getKey() + ")" + "\n\n";
                if (stringBuilder.length() + currentEntry.length() > 4096) {
                    // If the current entry would exceed the limit, send the message and start a new one.
                    this.sendMessageToChannel(stringBuilder.toString());
                    stringBuilder = new StringBuilder(); // Reset the stringBuilder
                    stringBuilder.append(Constants.SERBIAN_NEWS_DIGEST);
                    stringBuilder.append(formattedDate);
                    stringBuilder.append("\n\n");
                    stringBuilder.append(blockTitle);
                    stringBuilder.append(Constants.CONTINUATION);
                    stringBuilder.append(currentMessageIndex);
                    stringBuilder.append(")");
                    stringBuilder.append("\n\n");
                    currentMessageIndex++;
                }
                stringBuilder.append(currentEntry); // Add current entry to the stringBuilder.
            }
            // If there is remaining content to send.
            if (stringBuilder.length() > 0) {
                this.sendMessageToChannel(stringBuilder.toString());
            }
        }
    }

    private void sendMessageToChannel(String message) {
        SendMessage request = new SendMessage(Constants.TG_BOT_CHANNEL_ID, message).parseMode(ParseMode.MarkdownV2)
                .disableWebPagePreview(true);
        SendResponse response = bot.execute(request);
        if (response.message() != null) {
            logger.info("Telegram API response: {}", response);
        } else {
            String responseDescription = response.description();
            logger.warn("Telegram API response: {}", responseDescription);
        }
    }
}
