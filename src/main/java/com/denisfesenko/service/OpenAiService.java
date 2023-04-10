package com.denisfesenko.service;

import com.denisfesenko.exception.OpenAiException;
import com.denisfesenko.model.internal.OpenAiRequest;
import com.denisfesenko.model.internal.OpenAiResponse;
import com.denisfesenko.util.Constants;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class OpenAiService {
    private final OkHttpClient httpClient = new OkHttpClient.Builder().readTimeout(60, TimeUnit.SECONDS).build();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public List<Pair<String, String>> translate(List<Pair<String, String>> news) {
        List<Pair<String, String>> result = new ArrayList<>();
        int numThreads = Math.min(news.size(), 10); // Limit the maximum number of threads if needed
        ExecutorService executorService = Executors.newFixedThreadPool(numThreads);

        List<Future<Pair<String, String>>> futures = new ArrayList<>();

        for (Pair<String, String> event : news) {
            futures.add(executorService.submit(() -> {
                OpenAiResponse responseMessage = null;
                try {
                    OpenAiRequest openAiRequest = new OpenAiRequest().setModel("text-davinci-003").setMaxTokens(500).setTemperature(0.8f)
                            .setPrompt("Translate from Serbian to Russian: " + event.getRight());
                    RequestBody requestBody = RequestBody.create(objectMapper.writeValueAsString(openAiRequest), MediaType.parse("application/json"));
                    Request request = new Request.Builder()
                            .url(Constants.OPENAI_URL)
                            .addHeader("Authorization", "Bearer " + Constants.OPENAI_TOKEN)
                            .post(requestBody)
                            .build();
                    try (var response = httpClient.newCall(request).execute()) {
                        if (response.code() == 200 && response.body() != null) {
                            responseMessage = objectMapper.readValue(response.body().string(), OpenAiResponse.class);
                        }
                    }
                } catch (IOException e) {
                    throw new OpenAiException(e);
                }
                return Pair.of(
                        event.getLeft(),
                        responseMessage != null && responseMessage.getChoices() != null && !responseMessage.getChoices().isEmpty()
                                ? StringUtils.normalizeSpace(responseMessage.getChoices().get(0).getText())
                                : event.getRight()
                );
            }));
        }

        for (Future<Pair<String, String>> future : futures) {
            try {
                result.add(future.get());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }
        }

        executorService.shutdown();

        return result;
    }
}
