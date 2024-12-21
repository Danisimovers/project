package sfedu.danil.api;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.simpleframework.xml.*;
import org.simpleframework.xml.core.Persister;
import sfedu.danil.models.User;

import java.io.*;
import java.util.*;

@Root
@ElementList(inline = true)
public class DataProviderXml implements IDataProvider<User> {

    private static final Logger logger = LogManager.getLogger(DataProviderXml.class);

    private String xmlFilePath;
    private List<User> userList;

    public DataProviderXml(String filePath) {
        this.xmlFilePath = filePath;
        this.userList = new ArrayList<>();
        initDataSource();
    }

    @Override
    public void initDataSource() {
        try {
            File file = new File(xmlFilePath);
            if (!file.exists()) {
                logger.warn("Файл XML не существует, создаем новый файл.");
                file.createNewFile();
            }
            Serializer serializer = new Persister();
            Users users = serializer.read(Users.class, file);
            if (users != null && users.getUserList() != null) {
                userList = users.getUserList();
                logger.info("Успешно инициализированы данные из XML: {} записей", userList.size());
            }
        } catch (Exception e) {
            logger.error("Ошибка при инициализации данных из XML", e);
        }
    }

    @Override
    public void saveRecord(User record) {
        try {
            userList.add(record);
            Serializer serializer = new Persister();
            Users users = new Users();
            users.setUserList(userList);
            File file = new File(xmlFilePath);
            serializer.write(users, file);
            logger.info("Успешно сохранена запись: {}", record);
        } catch (Exception e) {
            logger.error("Ошибка при сохранении записи в XML", e);
        }
    }

    @Override
    public void deleteRecord(String id) {
        boolean recordRemoved = userList.removeIf(record -> record.getId().equals(id));
        if (recordRemoved) {
            try {
                Serializer serializer = new Persister();
                Users users = new Users();
                users.setUserList(userList);
                File file = new File(xmlFilePath);
                serializer.write(users, file);
                logger.info("Запись с ID {} успешно удалена.", id);
            } catch (Exception e) {
                logger.error("Ошибка при удалении записи из XML", e);
            }
        } else {
            logger.warn("Запись с ID {} не найдена для удаления.", id);
        }
    }

    @Override
    public User getRecordById(String id) {
        for (User user : userList) {
            if (user.getId().equals(id)) {
                logger.info("Запись с ID {} найдена: {}", id, user);
                return user;
            }
        }
        logger.warn("Запись с ID {} не найдена.", id);
        throw new NoSuchElementException("Запись с ID " + id + " не найдена.");
    }

    @Override
    public void updateRecord(User record) {
        boolean isUpdated = false;
        for (int i = 0; i < userList.size(); i++) {
            if (userList.get(i).getId().equals(record.getId())) {
                userList.set(i, record);
                isUpdated = true;
                break;
            }
        }

        if (!isUpdated) {
            logger.error("Запись с ID {} не найдена для обновления.", record.getId());
            throw new NoSuchElementException("Запись с ID " + record.getId() + " не найдена для обновления.");
        }

        try {
            Serializer serializer = new Persister();
            Users users = new Users();
            users.setUserList(userList);

            File file = new File(xmlFilePath);
            serializer.write(users, file);
            logger.info("Запись с ID {} успешно обновлена.", record.getId());
        } catch (Exception e) {
            logger.error("Ошибка при обновлении записи в XML", e);
        }
    }
}

@Root
class Users {
    @ElementList(inline = true)
    private List<User> userList;

    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }


}
