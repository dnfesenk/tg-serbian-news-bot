package com.denisfesenko.service;

import com.denisfesenko.exception.DanasConnectException;
import com.denisfesenko.util.Constants;
import org.apache.commons.lang3.tuple.Pair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DanasService {
    public static List<Pair<String, String>> getNews() {
        List<Pair<String, String>> result = new ArrayList<>();
        Document document;
        try {
            document = Jsoup.connect(Constants.DANAS_URL).get();
        } catch (IOException e) {
            throw new DanasConnectException(e);
        }
        Elements elements = document.select(".article-post-title").select("a");
        for (Element element : elements) {
            String link = element.attributes().get(Constants.HREF);
            if (link.contains("vesti")) {
                result.add(Pair.of(element.attributes().get(Constants.HREF), element.text()));
            }
        }
        return result;
    }

    private DanasService() {
    }
}
