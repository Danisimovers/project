package sfedu.danil;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sfedu.danil.api.DataProviderXml;
import sfedu.danil.models.User;
import sfedu.danil.models.Role;
import sfedu.danil.models.Competition;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
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

        Competition competition = new Competition("Coding Challenge", LocalDateTime.now());
        String competitionId = competition.getId();


        User user = new User("John Doe", "john.doe@example.com", "123-456-7890", Role.ORGANIZER, "A", competitionId);
        dataProvider.saveRecord(user);


        User savedUser = dataProvider.getRecordById(user.getId());
        assertEquals("John Doe", savedUser.getName());
        assertEquals("john.doe@example.com", savedUser.getEmail());
        assertEquals("123-456-7890", savedUser.getPhoneNumber());
        assertEquals("A", savedUser.getRating());
        assertEquals(competitionId, savedUser.getCompetitionId());
    }

    @Test
    void testDeleteRecord() {
        Competition competition = new Competition("Math Contest", LocalDateTime.now());
        String competitionId = competition.getId();


        User user = new User("Jane Doe", "jane.doe@example.com", "987-654-3210", Role.PARTICIPANT, "B", competitionId);
        dataProvider.saveRecord(user);


        dataProvider.deleteRecord(user.getId());


        assertThrows(NoSuchElementException.class, () -> dataProvider.getRecordById(user.getId()));
    }

    @Test
    void testGetRecordById() {
        Competition competition = new Competition("Science Fair", LocalDateTime.now());
        String competitionId = competition.getId();


        User user = new User("Alice", "alice@example.com", "111-222-3333", Role.PARTICIPANT, "C", competitionId);
        dataProvider.saveRecord(user);


        User fetchedUser = dataProvider.getRecordById(user.getId());
        assertNotNull(fetchedUser);
        assertEquals(user.getId(), fetchedUser.getId());
        assertEquals(user.getName(), fetchedUser.getName());
        assertEquals(user.getRating(), fetchedUser.getRating());
        assertEquals(user.getCompetitionId(), fetchedUser.getCompetitionId());
    }

    @Test
    void testUpdateRecord() {
        Competition competition = new Competition("Art Contest", LocalDateTime.now());
        String competitionId = competition.getId();


        User user = new User("Bob", "bob@example.com", "555-666-7777", Role.PARTICIPANT, "D", competitionId);
        dataProvider.saveRecord(user);


        user.setName("Robert");
        user.setEmail("robert@example.com");
        user.setRating("E");
        String newCompetitionId = new Competition("New Art Contest", LocalDateTime.now()).getId(); // Генерация нового competitionId
        user.setCompetitionId(newCompetitionId);
        dataProvider.updateRecord(user);


        User updatedUser = dataProvider.getRecordById(user.getId());
        assertEquals("Robert", updatedUser.getName());
        assertEquals("robert@example.com", updatedUser.getEmail());
        assertEquals("E", updatedUser.getRating());
        assertEquals(newCompetitionId, updatedUser.getCompetitionId());
    }

    @Test
    void testInitDataSource() {
        Competition competition = new Competition("Tech Challenge", LocalDateTime.now());
        String competitionId = competition.getId();


        User user = new User("Charlie", "charlie@example.com", "333-444-5555", Role.PARTICIPANT, "F", competitionId);
        dataProvider.saveRecord(user);


        DataProviderXml newProvider = new DataProviderXml(TEST_XML_FILE);
        User fetchedUser = newProvider.getRecordById(user.getId());
        assertEquals("Charlie", fetchedUser.getName());
        assertEquals("F", fetchedUser.getRating());
        assertEquals(competitionId, fetchedUser.getCompetitionId());
    }
}
