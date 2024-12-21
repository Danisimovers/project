package sfedu.danil.utils;


import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.io.*;
import java.util.*;

public class YamlUtil {

    public static Map<String, Object> loadYaml(String filePath) throws IOException {
        Map<String, Object> yamlMap = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            String currentKey = null;
            Map<String, String> currentNestedMap = null;
            List<String> currentList = null;

            while ((line = reader.readLine()) != null) {
                line = line.trim();

                if (line.startsWith("#") || line.isEmpty()) {
                    continue; // Пропускаем комментарии и пустые строки
                }

                if (line.contains(":")) {
                    String[] parts = line.split(":", 2);
                    String key = parts[0].trim();
                    String value = parts[1].trim();

                    if (value.isEmpty()) {
                        // Начало вложенной структуры (вложенной карты или списка)
                        currentKey = key;
                        if (yamlMap.containsKey(currentKey) && yamlMap.get(currentKey) instanceof List) {
                            currentList = (List<String>) yamlMap.get(currentKey);
                        } else {
                            currentNestedMap = new HashMap<>();
                            yamlMap.put(currentKey, currentNestedMap);
                        }
                    } else {
                        // Простая пара ключ-значение
                        if (currentKey != null && yamlMap.get(currentKey) instanceof Map) {
                            ((Map<String, String>) yamlMap.get(currentKey)).put(key, value);
                        } else {
                            yamlMap.put(key, value);
                        }
                    }
                } else if (line.startsWith("-") && currentKey != null) {
                    // Элемент списка
                    if (yamlMap.get(currentKey) instanceof List) {
                        ((List<String>) yamlMap.get(currentKey)).add(line.substring(1).trim());
                    } else {
                        currentList = new ArrayList<>();
                        currentList.add(line.substring(1).trim());
                        yamlMap.put(currentKey, currentList);
                    }
                }
            }
        }
        return yamlMap;
    }

    public static void saveYaml(String filePath, Map<String, Object> yamlMap) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Map.Entry<String, Object> entry : yamlMap.entrySet()) {
                writer.write(entry.getKey() + ":");
                if (entry.getValue() instanceof Map) {
                    writer.newLine();
                    for (Map.Entry<String, String> subEntry : ((Map<String, String>) entry.getValue()).entrySet()) {
                        writer.write("  " + subEntry.getKey() + ": " + subEntry.getValue());
                        writer.newLine();
                    }
                } else if (entry.getValue() instanceof List) {
                    writer.newLine();
                    for (String item : (List<String>) entry.getValue()) {
                        writer.write("  - " + item);
                        writer.newLine();
                    }
                } else {
                    writer.write(" " + entry.getValue());
                    writer.newLine();
                }
            }
        }
    }
}