package com.denisfesenko.service;

import com.denisfesenko.util.Constants;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class DanasService {

    private static final Logger logger = LoggerFactory.getLogger(DanasService.class);

    public static Map<String, String> getNews() {
        logger.info("Fetching news...");
        List<Future<Map<String, String>>> futures = new ArrayList<>();
        ExecutorService executorService = Executors.newFixedThreadPool(Constants.DANAS_URLS.size());

        for (String danasUrl : Constants.DANAS_URLS) {
            futures.add(executorService.submit(new NewsFetcher(danasUrl)));
        }

        Map<String, String> result = new TreeMap<>();
        for (Future<Map<String, String>> future : futures) {
            try {
                result.putAll(future.get());
            } catch (InterruptedException | ExecutionException e) {
                logger.error("Error fetching news", e);
                Thread.currentThread().interrupt();
            }
        }

        executorService.shutdown();
        logger.info("Finished fetching news");
        return result;
    }

    public static class NewsFetcher implements Callable<Map<String, String>> {
        private final String url;

        public NewsFetcher(String url) {
            this.url = url;
        }

        @Override
        public Map<String, String> call() {
            logger.info("Fetching news from URL: {}", url);
            Map<String, String> result = new TreeMap<>();
            Document document;
            try {
                document = Jsoup.connect(url).get();
                Elements elements = document.select(".article-post-title").select("a");
                for (Element element : elements) {
                    String link = element.attributes().get(Constants.HREF);
                    if (link.contains("vesti")) {
                        result.put(element.attributes().get(Constants.HREF), element.text());
                    }
                }
                logger.info("Fetched news from URL: {}", url);
            } catch (IOException e) {
                logger.error("Error connecting to URL: {}", url, e);
            }
            return result;
        }
    }

    private DanasService() {
    }
}