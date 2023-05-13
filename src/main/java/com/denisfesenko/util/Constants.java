package com.denisfesenko.util;

import java.util.List;

public class Constants {
    public static final String GOOGLE_APPLICATION_CREDENTIALS = "GOOGLE_APPLICATION_CREDENTIALS";

    public static final String MONGO_CONNECTION_STRING = System.getenv("MONGO_CONNECTION_STRING");
    public static final String OPENAI_TOKEN = System.getenv("OPENAI_TOKEN");
    public static final String TG_BOT_TOKEN = System.getenv("TG_BOT_TOKEN");
    public static final String GOOGLE_APPLICATION_CREDENTIALS_BASE64 = System.getenv("GOOGLE_APPLICATION_CREDENTIALS_BASE64");

    public static final String MONGO_DATABASE_NAME = "SerbianNews";
    public static final String MONGO_COLLECTION_NAME = "UrlsCollection";
    public static final List<String> DANAS_URLS = List.of("https://www.danas.rs/najnovije-vesti/",
            "https://www.danas.rs/najnovije-vesti/page/2/", "https://www.danas.rs/najnovije-vesti/page/3/",
            "https://www.danas.rs/najnovije-vesti/page/4/", "https://www.danas.rs/najnovije-vesti/page/5/",
            "https://www.danas.rs/najnovije-vesti/page/6/", "https://www.danas.rs/najnovije-vesti/page/7/",
            "https://www.danas.rs/najnovije-vesti/page/8/", "https://www.danas.rs/najnovije-vesti/page/9/",
            "https://www.danas.rs/najnovije-vesti/page/10/");
    public static final String OPENAI_URL = "https://api.openai.com/v1/chat/completions";
    public static final String HREF = "href";
    public static final String URL = "url";

    public static final String SERBIAN_NEWS_DIGEST = "\uD83D\uDCE3 Дайджест новостей Сербии на ";
    public static final String POLITICS_BLOCK_TITLE = "\uD83C\uDFDB️️️ Политика";
    public static final String ECONOMICS_BLOCK_TITLE = "\uD83D\uDCB0 Экономика";
    public static final String SOCIETY_BLOCK_TITLE = "\uD83D\uDC65️ Общество";
    public static final String OTHER_BLOCK_TITLE = "\uD83D\uDD39 Прочее";

    private Constants() {
    }
}
