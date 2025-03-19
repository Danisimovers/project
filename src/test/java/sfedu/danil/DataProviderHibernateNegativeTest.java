package sfedu.danil;

import org.junit.jupiter.api.Test;
import sfedu.danil.api.DataProviderHibernate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class DataProviderHibernateNegativeTest {

    @Test
    public void testGetDatabaseSizeOnEmptyDB() {
        long dbSize = DataProviderHibernate.getDatabaseSize();
        assertEquals(0, dbSize, "Размер базы данных должен быть 0, если в ней нет данных");
    }

    @Test
    public void testGetTableNamesOnEmptyDB() {
        List<String> tableNames = DataProviderHibernate.getTableNames();
        assertNotNull(tableNames, "Список таблиц не должен быть null");
        assertTrue(tableNames.isEmpty(), "Список таблиц должен быть пустым, если в БД нет таблиц");
    }

    @Test
    public void testGetUserListOnEmptyDB() {
        List<String> users = DataProviderHibernate.getUserList();
        assertNotNull(users, "Список пользователей не должен быть null");
        assertTrue(users.isEmpty(), "Список пользователей должен быть пустым, если нет записей в таблице");
    }

    @Test
    public void testGetColumnTypesOnEmptyDB() {
        List<Object[]> columnTypes = DataProviderHibernate.getColumnTypes();
        assertNotNull(columnTypes, "Список типов колонок не должен быть null");
        assertTrue(columnTypes.isEmpty(), "Список типов колонок должен быть пустым, если таблиц нет");
    }
}
