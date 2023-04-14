package com.denisfesenko.service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GoogleTranslateService {
    private static final Logger logger = LoggerFactory.getLogger(GoogleTranslateService.class);
    private final GoogleCredentials googleCredentials;

    public GoogleTranslateService(GoogleCredentials googleCredentials) {
        this.googleCredentials = googleCredentials;
    }

    public Map<String, String> translate(Map<String, String> news) {
        logger.info("Translating news...");
        ConcurrentHashMap<String, String> result = new ConcurrentHashMap<>();
        Translate translate = TranslateOptions.newBuilder().setCredentials(this.googleCredentials).build().getService();
        String sourceLanguage = "sr"; // Serbian
        String targetLanguage = "ru"; // Russian

        int numThreads = Math.min(news.size(), 10);
        ExecutorService executorService = Executors.newFixedThreadPool(numThreads);

        CompletableFuture.allOf(news.entrySet().stream()
                .map(event -> CompletableFuture.runAsync(() -> {
                    Translation translation = translate.translate(event.getValue(),
                            Translate.TranslateOption.targetLanguage(targetLanguage),
                            Translate.TranslateOption.sourceLanguage(sourceLanguage));
                    String translatedText = translation.getTranslatedText();
                    result.put(event.getKey(), translatedText);
                    logger.info("Translated: {} to {}", event.getValue(), translatedText);
                }, executorService)).toArray(CompletableFuture[]::new)).join();

        executorService.shutdown();

        return result;
    }
}
