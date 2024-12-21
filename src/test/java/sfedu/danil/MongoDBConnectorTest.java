package sfedu.danil;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sfedu.danil.api.MongoDBConnector;
import sfedu.danil.models.HistoryContent;
import sfedu.danil.models.Status;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class MongoDBConnectorTest {
    private static MongoDBConnector connector;
    private static final Logger logger = LogManager.getLogger(MongoDBConnectorTest.class);

    @BeforeAll
    static void setUp() {
        logger.info("Запуск тестов. Инициализация MongoDBConnector...");
        connector = new MongoDBConnector();
    }

    @AfterAll
    static void tearDown() {
        logger.info("Завершение тестов. Закрытие соединения с MongoDB...");
        connector.close();
    }

    @Test
    void testSave() {
        logger.info("Тестирование сохранения объекта в MongoDB...");

        HistoryContent historyContent = new HistoryContent();
        historyContent.setClassName("TestSaveClass");
        historyContent.setMethodName("testMethod");

        Map<String, Object> objectState = new HashMap<>();
        objectState.put("key1", "value1");
        historyContent.setObject(objectState);
        historyContent.setStatus(Status.SUCCESS);

        assertDoesNotThrow(() -> {
            ObjectId generatedId = connector.save(historyContent);
            logger.info("Объект успешно сохранён с ID: " + generatedId);
            historyContent.setId(generatedId);
        });

        assertNotNull(historyContent.getId());
    }

    @Test
    void testFindById() {
        logger.info("Тестирование поиска объекта по ID...");

        HistoryContent historyContent = new HistoryContent();
        historyContent.setClassName("TestFindClass");
        historyContent.setMethodName("findMethod");
        historyContent.setStatus(Status.SUCCESS);

        ObjectId savedId = connector.save(historyContent);
        HistoryContent foundContent = connector.findById(savedId.toHexString());

        assertNotNull(foundContent);
        assertEquals(savedId, foundContent.getId());
        logger.info("Объект успешно сохранён с ID: " + savedId);
        logger.info("Объект найден с ID: " + savedId);
    }

    @Test
    void testUpdateById() {
        logger.info("Тестирование обновления объекта по ID...");

        HistoryContent historyContent = new HistoryContent();
        historyContent.setClassName("OldClass");
        historyContent.setMethodName("oldMethod");
        historyContent.setStatus(Status.SUCCESS);

        ObjectId savedId = connector.save(historyContent);
        logger.info("Сохраненный объект с ID: {}", savedId.toHexString());

        historyContent.setClassName("UpdatedClass");
        historyContent.setMethodName("updateMethod");
        logger.info("Обновление объекта с ID: {}", savedId.toHexString());  // Логируем ID обновляемого объекта
        connector.updateById(savedId.toHexString(), historyContent);


        HistoryContent updatedContent = connector.findById(savedId.toHexString());
        assertNotNull(updatedContent);
        assertEquals("UpdatedClass", updatedContent.getClassName());
    }



    @Test
    void testDeleteById() {
        logger.info("Тестирование удаления объекта по ID...");


        HistoryContent historyContent = new HistoryContent();
        historyContent.setClassName("ToDelete");
        historyContent.setMethodName("deleteMethod");
        historyContent.setStatus(Status.SUCCESS);  // Убедитесь, что status инициализирован


        ObjectId savedId = connector.save(historyContent);
        assertNotNull(savedId);

        logger.info("Сохраненный объект с ID: {}", savedId.toHexString());


        connector.deleteById(savedId.toHexString());


        HistoryContent deletedContent = connector.findById(savedId.toHexString());
        assertNull(deletedContent);

        logger.info("Объект с ID: {} был удален.", savedId.toHexString());
    }


}
