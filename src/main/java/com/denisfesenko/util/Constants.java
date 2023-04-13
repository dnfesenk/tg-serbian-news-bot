package com.denisfesenko.util;

import java.util.List;

public class Constants {

    public static final String MONGO_DATABASE_NAME = "SerbianNews";
    public static final String MONGO_COLLECTION_NAME = "UrlsCollection";
    public static final List<String> DANAS_URLS = List.of("https://www.danas.rs/najnovije-vesti/",
            "https://www.danas.rs/najnovije-vesti/page/2/", "https://www.danas.rs/najnovije-vesti/page/3/");
    public static final String OPENAI_URL = "https://api.openai.com/v1/chat/completions";
    public static final String HREF = "href";
    public static final String URL = "url";

    private Constants() {
    }
}
