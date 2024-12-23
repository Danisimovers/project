package sfedu.danil.api;

import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.types.ObjectId;
import sfedu.danil.models.HistoryContent;
import sfedu.danil.models.Status;

import java.time.LocalDateTime;
import java.util.Map;

import static sfedu.danil.Constants.*;

public class MongoDBConnector {

    private final MongoClient mongoClient;
    private final MongoDatabase database;
    private final MongoCollection<Document> collection;

    public MongoDBConnector() {
        mongoClient = MongoClients.create(CONNECTION_STRING);
        database = mongoClient.getDatabase(DATABASE_NAME);
        collection = database.getCollection(COLLECTION_NAME);
    }

    public ObjectId save(HistoryContent content) {
        try {
            Document document = new Document()
                    .append("className", content.getClassName())
                    .append("createdDate", content.getCreatedDate().toString())
                    .append("actor", content.getActor())
                    .append("methodName", content.getMethodName())
                    .append("object", content.getObject())
                    .append("status", content.getStatus().name());

            collection.insertOne(document);
            return document.getObjectId("_id");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public HistoryContent findById(String id) {
        try {
            Document document = collection.find(Filters.eq("_id", toObjectId(id))).first();
            if (document != null) {
                HistoryContent content = new HistoryContent();
                content.setId(document.getObjectId("_id"));
                content.setClassName(document.getString("className"));
                content.setCreatedDate(LocalDateTime.parse(document.getString("createdDate")));
                content.setActor(document.getString("actor"));
                content.setMethodName(document.getString("methodName"));
                content.setObject(document.get("object", Map.class));
                content.setStatus(Status.valueOf(document.getString("status")));
                return content;
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void updateById(String id, HistoryContent updatedContent) {
        try {
            Document document = new Document()
                    .append("className", updatedContent.getClassName())
                    .append("createdDate", updatedContent.getCreatedDate().toString()) // Конвертируем LocalDateTime в String
                    .append("actor", updatedContent.getActor())
                    .append("methodName", updatedContent.getMethodName())
                    .append("object", updatedContent.getObject())
                    .append("status", updatedContent.getStatus().name());

            collection.updateOne(Filters.eq("_id", toObjectId(id)), new Document("$set", document));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteById(String id) {
        try {
            collection.deleteOne(Filters.eq("_id", toObjectId(id)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private ObjectId toObjectId(String id) {
        try {
            return new ObjectId(id);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void close() {
        try {
            if (mongoClient != null) {
                mongoClient.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
