package sfedu.danil;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.bson.Document;
import sfedu.danil.models.HistoryContent;

public class JsonConverter {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.registerModule(new JavaTimeModule());
    }

    // Конвертация объекта HistoryContent в Document для MongoDB
    public Document toDocument(HistoryContent content) {
        try {
            // Преобразуем объект в строку JSON и парсим её в Document
            return Document.parse(objectMapper.writeValueAsString(content));
        } catch (Exception e) {
            throw new RuntimeException("Error converting object to Document", e);
        }
    }

    // Конвертация Document обратно в объект HistoryContent
    public HistoryContent toHistoryContent(Document document) {
        try {
            return objectMapper.readValue(document.toJson(), HistoryContent.class);
        } catch (Exception e) {
            throw new RuntimeException("Error converting Document to object", e);
        }
    }
}
