package com.denisfesenko.service;

import com.denisfesenko.model.internal.Message;
import com.denisfesenko.model.internal.OpenAiRequest;
import com.denisfesenko.model.internal.OpenAiResponse;
import com.denisfesenko.util.Constants;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class OpenAiService {
    private final OkHttpClient httpClient = new OkHttpClient.Builder().readTimeout(120, TimeUnit.SECONDS).build();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private static final Logger logger = LoggerFactory.getLogger(OpenAiService.class);

    public Map<String, String> translate(Map<String, String> news) {
        logger.info("Translating news...");
        Map<String, String> result = new HashMap<>();
        int numThreads = Math.min(news.size(), 10); // Limit the maximum number of threads if needed
        ExecutorService executorService = Executors.newFixedThreadPool(numThreads);

        List<Future<Map.Entry<String, String>>> futures = new ArrayList<>();

        for (Map.Entry<String, String> event : news.entrySet()) {
            futures.add(executorService.submit(() -> {
                OpenAiResponse responseMessage = null;
                try {
                    OpenAiRequest openAiRequest = new OpenAiRequest().setModel("gpt-3.5-turbo").setTemperature(0.8)
                            .setMessages(List.of(new Message().setRole("user").setContent("Translate the following Serbian text to Russian: " + event.getValue())));
                    RequestBody requestBody = RequestBody.create(objectMapper.writeValueAsString(openAiRequest), MediaType.parse("application/json"));
                    Request request = new Request.Builder()
                            .url(Constants.OPENAI_URL)
                            .addHeader("Authorization", "Bearer " + Constants.OPENAI_TOKEN)
                            .post(requestBody)
                            .build();
                    logger.info("OpenAI request: {}", openAiRequest);
                    try (var response = httpClient.newCall(request).execute()) {
                        if (response.code() == 200 && response.body() != null) {
                            responseMessage = objectMapper.readValue(response.body().string(), OpenAiResponse.class);
                            logger.info("OpenAI response: {}", responseMessage);
                        }
                    }
                } catch (IOException e) {
                    logger.error("Error in translation request", e);
                }
                return Map.entry(
                        event.getKey(),
                        responseMessage != null && responseMessage.getChoices() != null && !responseMessage.getChoices().isEmpty()
                                ? StringUtils.normalizeSpace(responseMessage.getChoices().get(0).getMessage().getContent())
                                : event.getValue()
                );
            }));
        }

        for (Future<Map.Entry<String, String>> future : futures) {
            try {
                Map.Entry<String, String> entry = future.get();
                result.put(entry.getKey(), entry.getValue());
            } catch (InterruptedException | ExecutionException e) {
                logger.error("Error processing translation future", e);
                Thread.currentThread().interrupt();
            }
        }

        executorService.shutdown();
        logger.info("Translation completed");
        return result;
    }
}