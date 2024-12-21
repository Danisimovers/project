package sfedu.danil;

import org.junit.jupiter.api.Test;
import sfedu.danil.utils.XmlUtil;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class XmlUtilTest {

    @Test
    void testGetList() throws Exception {
        List<String> planets = XmlUtil.getList("item");
        assertEquals(List.of("Earth", "Saturn", "Mars", "Venus"), planets);
    }

    @Test
    void testGetMap() throws Exception {
        Map<Integer, String> months = XmlUtil.getMap("entry");
        assertEquals(Map.of(1, "January", 2, "February", 3, "March", 4, "April"), months);
    }
}
