package com.denisfesenko.service;

import com.denisfesenko.util.Constants;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class MongoService {

    private static final Logger logger = LoggerFactory.getLogger(MongoService.class);

    public static Map<String, String> filterAndAddRecords(Map<String, String> news) {
        logger.info("Filtering and adding news records...");
        Map<String, String> filteredNews = new HashMap<>();
        try (MongoClient mongoClient = MongoClients.create(System.getenv("MONGO_CONNECTION_STRING"))) {
            MongoDatabase database = mongoClient.getDatabase(Constants.MONGO_DATABASE_NAME);
            MongoCollection<Document> collection = database.getCollection(Constants.MONGO_COLLECTION_NAME);

            for (Map.Entry<String, String> entry : news.entrySet()) {
                String url = entry.getKey();
                if (!existsInMongoDB(collection, url)) {
                    addToMongoDB(collection, url);
                    filteredNews.put(url, entry.getValue());
                }
            }
        }
        logger.info("Finished filtering and adding news records");
        return filteredNews;
    }

    public static boolean existsInMongoDB(MongoCollection<Document> collection, String url) {
        boolean exists = collection.countDocuments(Filters.eq(Constants.URL, url)) > 0;
        if (exists) {
            logger.debug("URL exists in MongoDB: {}", url);
        } else {
            logger.debug("URL does not exist in MongoDB: {}", url);
        }
        return exists;
    }

    public static void addToMongoDB(MongoCollection<Document> collection, String url) {
        logger.info("Adding URL to MongoDB: {}", url);
        Document document = new Document(Constants.URL, url);
        collection.insertOne(document);
        logger.info("URL added to MongoDB: {}", url);
    }

    private MongoService() {
    }
}
