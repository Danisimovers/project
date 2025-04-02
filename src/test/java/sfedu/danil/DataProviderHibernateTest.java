package sfedu.danil;


import org.junit.jupiter.api.Test;
import sfedu.danil.api.DataProviderHibernate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class DataProviderHibernateTest {

    @Test
    public void testGetDatabaseSize() {
        long dbSize = DataProviderHibernate.getDatabaseSize();
        assertTrue(dbSize > 0, "Размер базы данных должен быть больше нуля");
    }

    @Test
    public void testGetTableNames() {
        List<String> tableNames = DataProviderHibernate.getTableNames();
        assertNotNull(tableNames, "Список таблиц не должен быть null");
        assertFalse(tableNames.isEmpty(), "Список таблиц не должен быть пустым");
    }

    @Test
    public void testGetUserList() {
        List<String> users = DataProviderHibernate.getUserList();
        assertNotNull(users, "Список пользователей не должен быть null");
        assertFalse(users.isEmpty(), "Список пользователей не должен быть пустым");
    }

    @Test
    public void testGetColumnTypes() {
        List<Object[]> columnTypes = DataProviderHibernate.getColumnTypes();
        assertNotNull(columnTypes, "Список типов колонок не должен быть null");
        assertFalse(columnTypes.isEmpty(), "Список типов колонок не должен быть пустым");

        for (Object[] column : columnTypes) {
            assertTrue(column.length == 3, "Каждая строка должна содержать 3 элемента");
        }
    }
}