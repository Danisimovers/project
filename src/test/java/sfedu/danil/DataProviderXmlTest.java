package sfedu.danil;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sfedu.danil.api.DataProviderXml;
import sfedu.danil.models.User;
import sfedu.danil.models.Role;
import java.io.File;
import java.io.IOException;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static sfedu.danil.Constants.TEST_XML_FILE;

public class DataProviderXmlTest {

    private DataProviderXml dataProvider;

    @BeforeEach
    void setUp() throws IOException {
        dataProvider = new DataProviderXml(TEST_XML_FILE);
        File file = new File(TEST_XML_FILE);
        if (file.exists()) {
            file.delete();
        }
        file.createNewFile();
    }

    @Test
    void testSaveRecord() {
        User user = new User("John Doe", "john.doe@example.com", "123-456-7890", Role.ORGANIZER, "A");
        dataProvider.saveRecord(user);
        User savedUser = dataProvider.getRecordById(user.getId());
        assertEquals("John Doe", savedUser.getName());
        assertEquals("john.doe@example.com", savedUser.getEmail());
        assertEquals("123-456-7890", savedUser.getPhoneNumber());
    }

    @Test
    void testDeleteRecord() {
        User user = new User("Jane Doe", "jane.doe@example.com", "987-654-3210", Role.PARTICIPANT, "B");
        dataProvider.saveRecord(user);
        dataProvider.deleteRecord(user.getId());
        assertThrows(NoSuchElementException.class, () -> dataProvider.getRecordById(user.getId()));
    }

    @Test
    void testGetRecordById() {
        User user = new User("Alice", "alice@example.com", "111-222-3333", Role.PARTICIPANT, "C");
        dataProvider.saveRecord(user);
        User fetchedUser = dataProvider.getRecordById(user.getId());
        assertNotNull(fetchedUser);
        assertEquals(user.getId(), fetchedUser.getId());
        assertEquals(user.getName(), fetchedUser.getName());
    }

    @Test
    void testUpdateRecord() {
        User user = new User("Bob", "bob@example.com", "555-666-7777", Role.PARTICIPANT, "D");
        dataProvider.saveRecord(user);
        user.setName("Robert");
        user.setEmail("robert@example.com");
        dataProvider.updateRecord(user);
        User updatedUser = dataProvider.getRecordById(user.getId());
        assertEquals("Robert", updatedUser.getName());
        assertEquals("robert@example.com", updatedUser.getEmail());
    }

    @Test
    void testInitDataSource() {
        User user = new User("Charlie", "charlie@example.com", "333-444-5555", Role.PARTICIPANT, "A");
        dataProvider.saveRecord(user);
        DataProviderXml newProvider = new DataProviderXml(TEST_XML_FILE);
        User fetchedUser = newProvider.getRecordById(user.getId());
        assertEquals("Charlie", fetchedUser.getName());
    }
}

