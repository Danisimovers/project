package sfedu.danil;

import org.junit.jupiter.api.Test;
import sfedu.danil.utils.YamlUtil;

import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class YamlUtilTest {


    @Test
    void testLoadYaml() throws IOException {
        Map<String, Object> yamlData = YamlUtil.loadYaml(Constants.YAML_CONFIG_PATH);

        assertNotNull(yamlData, "Данные YAML не должны быть null");
        assertTrue(yamlData.containsKey("planets"), "Файл должен содержать ключ 'planets'");
        assertTrue(yamlData.containsKey("months"), "Файл должен содержать ключ 'months'");
    }

    @Test
    void testLoadYamlPlanets() throws IOException {
        Map<String, Object> yamlData = YamlUtil.loadYaml(Constants.YAML_CONFIG_PATH);
        List<String> planets = (List<String>) yamlData.get("planets");

        assertNotNull(planets, "Список 'planets' не должен быть null");
        assertEquals(4, planets.size(), "Список 'planets' должен содержать 4 элемента");
        assertEquals(Arrays.asList("Earth", "Saturn", "Mars", "Venus"), planets,
                "Список 'planets' должен содержать правильные значения");
    }

    @Test
    void testLoadYamlMonths() throws IOException {
        Map<String, Object> yamlData = YamlUtil.loadYaml(Constants.YAML_CONFIG_PATH);
        Map<String, String> months = (Map<String, String>) yamlData.get("months");

        assertNotNull(months, "Карта 'months' не должна быть null");
        assertEquals(4, months.size(), "Карта 'months' должна содержать 4 элемента");
        assertEquals("Январь", months.get("1"), "Первый месяц должен быть 'Январь'");
        assertEquals("Февраль", months.get("2"), "Второй месяц должен быть 'Февраль'");
        assertEquals("Март", months.get("3"), "Третий месяц должен быть 'Март'");
        assertEquals("Апрель", months.get("4"), "Четвертый месяц должен быть 'Апрель'");
    }
}
