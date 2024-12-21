package sfedu.danil.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.IOException;
import java.util.*;

import static sfedu.danil.Constants.*;

public class XmlUtil {
    private static final Logger logger = LogManager.getLogger(XmlUtil.class);

    /**
     * Загружает и парсит XML файл.
     */
    private static Document getDocument() throws Exception {
        File xmlFile = new File(XML_CONFIG_PATH);
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(xmlFile);
            return document;
        } catch (Exception e) {
            logger.error("Ошибка при загрузке XML файла", e);
            throw e;
        }
    }

    /**
     * Получает список значений по ключу.
     */
    public static List<String> getList(String key) throws Exception {
        logger.info("Получение списка значений для ключа: {}", key);
        Document document = getDocument();
        NodeList nodeList = document.getElementsByTagName(key);
        List<String> result = new ArrayList<>();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                String value = node.getTextContent().trim();
                result.add(value);
            }
        }
        // Логируем все значения, найденные для ключа
        logger.info("Полученные значения для ключа {}: {}", key, result);
        return result;
    }

    /**
     * Получает карту значений по ключу.
     */
    public static Map<Integer, String> getMap(String key) throws Exception {
        logger.info("Получение карты значений для ключа: {}", key);
        Document document = getDocument();
        NodeList nodeList = document.getElementsByTagName(key);
        Map<Integer, String> result = new HashMap<>();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                String mapKey = element.getElementsByTagName("key").item(0).getTextContent().trim();
                String mapValue = element.getElementsByTagName("value").item(0).getTextContent().trim();
                result.put(Integer.parseInt(mapKey), mapValue);
            }
        }
        // Логируем всю карту значений
        logger.info("Полученная карта значений для ключа {}: {}", key, result);
        return result;
    }
}
