package sfedu.danil;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sfedu.danil.api.DataProviderXml;
import sfedu.danil.models.User;
import sfedu.danil.models.Role;
import sfedu.danil.models.Competition;
import sfedu.danil.models.Catch;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static sfedu.danil.Constants.*;

public class DataProviderXmlTest {

    private DataProviderXml<User> userDataProvider;
    private DataProviderXml<Competition> competitionDataProvider;
    private DataProviderXml<Catch> catchDataProvider;

    @BeforeEach
    void setUp() throws IOException {
        userDataProvider = new DataProviderXml<>(USERS_XML_FILE, User.class);
        competitionDataProvider = new DataProviderXml<>(COMPETITIONS_XML_FILE, Competition.class);
        catchDataProvider = new DataProviderXml<>(CATCHES_XML_FILE, Catch.class);

        File userFile = new File(USERS_XML_FILE);
        if (userFile.exists()) {
            userFile.delete();
        }
        userFile.createNewFile();

        File competitionFile = new File(COMPETITIONS_XML_FILE);
        if (competitionFile.exists()) {
            competitionFile.delete();
        }
        competitionFile.createNewFile();

        File catchFile = new File(CATCHES_XML_FILE);
        if (catchFile.exists()) {
            catchFile.delete();
        }
        catchFile.createNewFile();
    }

    @Test
    void testSaveRecord() {
        // Создание соревнования с полными параметрами
        Competition competition = new Competition("Lesh Tournament", LocalDateTime.of(2024, 12, 11, 18, 30));
        String competitionId = competition.getId();
        competitionDataProvider.saveRecord(competition);

        // Создание пользователя с полными параметрами
        User user = new User("Sergey Egorov", "sergey.egorov@example.com", "5555555555", Role.PARTICIPANT, "7.2", competitionId);
        userDataProvider.saveRecord(user);

        // Создание записи о поимке с полными параметрами
        Catch catchRecord = new Catch("Trofeyny Lesch", 4.5, 8.0, user.getId(), competitionId);
        catchDataProvider.saveRecord(catchRecord);

        // Проверка сохранения пользователя
        User savedUser = userDataProvider.getRecordById(user.getId());
        assertEquals("Sergey Egorov", savedUser.getName());
        assertEquals("sergey.egorov@example.com", savedUser.getEmail());
        assertEquals("5555555555", savedUser.getPhoneNumber());
        assertEquals("7.2", savedUser.getRating());
        assertEquals(competitionId, savedUser.getCompetitionId());

        // Проверка сохранения записи о поимке
        Catch savedCatch = catchDataProvider.getRecordById(catchRecord.getId());
        assertEquals(user.getId(), savedCatch.getUserId());
        assertEquals("Trofeyny Lesch", savedCatch.getFishType());
        assertEquals(4.5, savedCatch.getWeight());
    }

    @Test
    void testDeleteRecord() {
        // Создание соревнования
        Competition competition = new Competition("Karas Tournament", LocalDateTime.of(2023, 6, 13, 6, 30));
        String competitionId = competition.getId();
        competitionDataProvider.saveRecord(competition);

        // Создание организатора
        User user = new User("Ivan Petrov", "ivan.petrov@example.com", "1234567890", Role.ORGANIZER, "2.2", competitionId);
        userDataProvider.saveRecord(user);

        // Создание записи о поимке
        Catch catchRecord = new Catch("Sudak", 5.0, 10.0, user.getId(), competitionId);
        catchDataProvider.saveRecord(catchRecord);

        // Удаление пользователя и записи о поимке
        userDataProvider.deleteRecord(user.getId());
        catchDataProvider.deleteRecord(catchRecord.getId());

        // Проверка исключений при попытке получить удаленные записи
        assertThrows(NoSuchElementException.class, () -> userDataProvider.getRecordById(user.getId()));
        assertThrows(NoSuchElementException.class, () -> catchDataProvider.getRecordById(catchRecord.getId()));
    }

    @Test
    void testGetRecordById() {
        // Создание соревнования
        Competition competition = new Competition("Sudak Tournament", LocalDateTime.of(2024, 8, 22, 20, 30));
        String competitionId = competition.getId();
        competitionDataProvider.saveRecord(competition);

        // Создание участника
        User user = new User("Oleg Plotvich", "oleg.plotvisch@example.com", "8888888888", Role.PARTICIPANT, "9.9", competitionId);
        userDataProvider.saveRecord(user);

        // Создание записи о поимке
        Catch catchRecord = new Catch("Okun", 3.5, 7.0, user.getId(), competitionId);
        catchDataProvider.saveRecord(catchRecord);

        // Получение и проверка пользователя по ID
        User fetchedUser = userDataProvider.getRecordById(user.getId());
        assertNotNull(fetchedUser);
        assertEquals(user.getId(), fetchedUser.getId());
        assertEquals(user.getName(), fetchedUser.getName());
        assertEquals(user.getRating(), fetchedUser.getRating());
        assertEquals(user.getCompetitionId(), fetchedUser.getCompetitionId());

        // Получение и проверка записи о поимке по ID
        Catch fetchedCatch = catchDataProvider.getRecordById(catchRecord.getId());
        assertNotNull(fetchedCatch);
        assertEquals(catchRecord.getId(), fetchedCatch.getId());
        assertEquals(catchRecord.getFishType(), fetchedCatch.getFishType());
    }

    @Test
    void testUpdateRecord() {
        // Создание соревнования
        Competition competition = new Competition("Som Tournament", LocalDateTime.of(2024, 2, 3, 11, 30));
        String competitionId = competition.getId();
        competitionDataProvider.saveRecord(competition);

        // Создание организатора
        User user = new User("Viktor Volkov", "viktor.volkov@example.com", "7777777777", Role.ORGANIZER, "4.9", competitionId);
        userDataProvider.saveRecord(user);

        // Создание записи о поимке
        Catch catchRecord = new Catch("Karp", 2.0, 5.0, user.getId(), competitionId);
        catchDataProvider.saveRecord(catchRecord);

        // Обновление данных пользователя и записи о поимке
        user.setName("Viktor Volkov Updated");
        user.setEmail("viktor.volkov.updated@example.com");
        user.setRating("5.0");
        String newCompetitionId = new Competition("New Som Tournament", LocalDateTime.now()).getId();
        user.setCompetitionId(newCompetitionId);
        userDataProvider.updateRecord(user);

        catchRecord.setFishType("Big Karp");
        catchDataProvider.updateRecord(catchRecord);

        // Проверка обновленных данных пользователя
        User updatedUser = userDataProvider.getRecordById(user.getId());
        assertEquals("Viktor Volkov Updated", updatedUser.getName());
        assertEquals("viktor.volkov.updated@example.com", updatedUser.getEmail());
        assertEquals("5.0", updatedUser.getRating());
        assertEquals(newCompetitionId, updatedUser.getCompetitionId());

        // Проверка обновленных данных записи о поимке
        Catch updatedCatch = catchDataProvider.getRecordById(catchRecord.getId());
        assertEquals("Big Karp", updatedCatch.getFishType());
    }

    @Test
    void testInitDataSource() {
        // Создание соревнования
        Competition competition = new Competition("Karas Tournament", LocalDateTime.of(2023, 6, 13, 6, 30));
        String competitionId = competition.getId();
        competitionDataProvider.saveRecord(competition);

        // Создание организатора
        User user = new User("Ivan Petrov", "ivan.petrov@example.com", "1234567890", Role.ORGANIZER, "2.2", competitionId);
        userDataProvider.saveRecord(user);

        // Создание записи о поимке
        Catch catchRecord = new Catch("Sudak", 5.0, 10.0, user.getId(), competitionId);
        catchDataProvider.saveRecord(catchRecord);

        // Инициализация нового провайдера и получение данных
        DataProviderXml<User> newUserProvider = new DataProviderXml<>(USERS_XML_FILE, User.class);
        DataProviderXml<Catch> newCatchProvider = new DataProviderXml<>(CATCHES_XML_FILE, Catch.class);

        User fetchedUser = newUserProvider.getRecordById(user.getId());
        assertEquals("Ivan Petrov", fetchedUser.getName());
        assertEquals("2.2", fetchedUser.getRating());
        assertEquals(competitionId, fetchedUser.getCompetitionId());

        Catch fetchedCatch = newCatchProvider.getRecordById(catchRecord.getId());
        assertEquals("Sudak", fetchedCatch.getFishType());
    }
}
