package sfedu.danil;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sfedu.danil.dao.UserMappedSuperclassDao;
import sfedu.danil.models.mappedSuperclass.Participant;
import sfedu.danil.models.mappedSuperclass.Organizer;
import sfedu.danil.models.mappedSuperclass.User;
import sfedu.danil.api.HibernateUtil;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class UserMappedSuperclassDaoTest {

    private UserMappedSuperclassDao userDao;

    @BeforeEach
    public void setUp() {
        userDao = new UserMappedSuperclassDao();
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.createQuery("DELETE FROM Participant").executeUpdate();
            session.createQuery("DELETE FROM Organizer").executeUpdate();
            tx.commit();
        }
    }

    @Test
    public void testCreateAndReadParticipant() {
        Participant participant = new Participant("Иван Иванов", "ivan@example.com", "123456789", "A", "123", "5 лет");
        userDao.create(participant);
        Participant retrieved = userDao.read(Participant.class, participant.getId());
        assertNotNull(retrieved, "Участник не найден в базе данных");
        assertEquals("Иван Иванов", retrieved.getName());
        assertEquals("ivan@example.com", retrieved.getEmail());
        assertEquals("5 лет", retrieved.getFishingExperience());
    }

    @Test
    public void testCreateAndReadOrganizer() {
        Organizer organizer = new Organizer("Мария Смирнова", "maria@example.com", "987654321", "B", "456", "Рыболовная корпорация");
        userDao.create(organizer);
        Organizer retrieved = userDao.read(Organizer.class, organizer.getId());
        assertNotNull(retrieved, "Организатор не найден в базе данных");
        assertEquals("Мария Смирнова", retrieved.getName());
        assertEquals("maria@example.com", retrieved.getEmail());
        assertEquals("Рыболовная корпорация", retrieved.getOrganizationName());
    }

    @Test
    public void testUpdateParticipant() {
        Participant participant = new Participant("Алексей Петров", "aleksey@example.com", "111223344", "A", "789", "2 года");
        userDao.create(participant);
        participant.setFishingExperience("3 года");
        userDao.update(participant);
        Participant updated = userDao.read(Participant.class, participant.getId());
        assertEquals("3 года", updated.getFishingExperience(), "Неверно обновлён опыт рыбалки");
    }

    @Test
    public void testDeleteOrganizer() {
        Organizer organizer = new Organizer("Николай Васильев", "nikolai@example.com", "555666777", "C", "101", "Приключения Рыбака");
        userDao.create(organizer);
        userDao.delete(Organizer.class, organizer.getId());
        Organizer deleted = userDao.read(Organizer.class, organizer.getId());
        assertNull(deleted, "Организатор не был удалён из базы данных");
    }

    @Test
    public void testGetAllUsers() {
        Participant participant1 = new Participant("Том", "tom@example.com", "123", "A", "112", "10 лет");
        Participant participant2 = new Participant("Сара", "sarah@example.com", "456", "B", "113", "8 лет");
        Organizer organizer = new Organizer("Нина", "nina@example.com", "789", "C", "114", "Природа Ltd.");
        userDao.create(participant1);
        userDao.create(participant2);
        userDao.create(organizer);
        List<User> users = userDao.getAll();
        assertEquals(3, users.size(), "Неверное количество пользователей в базе данных");
    }
}
