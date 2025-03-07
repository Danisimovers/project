package sfedu.danil.api;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.simpleframework.xml.*;
import org.simpleframework.xml.core.Persister;
import sfedu.danil.models.*;

import java.io.*;
import java.util.*;

@Root
@ElementList(inline = true)
public class DataProviderXml<T> implements IDataProvider<T> {

    private static final Logger logger = LogManager.getLogger(DataProviderXml.class);

    private String xmlFilePath;
    private List<T> dataList;
    private Class<T> type;

    public DataProviderXml(String filePath, Class<T> type) {
        this.xmlFilePath = filePath;
        this.type = type;
        this.dataList = new ArrayList<>();
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
            if (type == User.class) {
                Users users = serializer.read(Users.class, file);
                if (users != null && users.getUserList() != null) {
                    dataList = (List<T>) users.getUserList();
                    logger.info("Успешно инициализированы данные из XML: {} записей", dataList.size());
                }
            } else if (type == Competition.class) {
                Competitions competitions = serializer.read(Competitions.class, file);
                if (competitions != null && competitions.getCompetitionList() != null) {
                    dataList = (List<T>) competitions.getCompetitionList();
                    logger.info("Успешно инициализированы данные из XML: {} записей", dataList.size());
                    for (Competition competition : (List<Competition>) dataList) {
                        logger.info("Десериализованное соревнование: {}", competition);
                    }
                }
            } else if (type == Catch.class) {
                Catches catches = serializer.read(Catches.class, file);
                if (catches != null && catches.getCatchList() != null) {
                    dataList = (List<T>) catches.getCatchList();
                    logger.info("Успешно инициализированы данные из XML: {} записей", dataList.size());
                }
            }
        } catch (Exception e) {
            logger.error("Ошибка при инициализации данных из XML");
        }
    }

    @Override
    public void saveRecord(T record) {
        try {
            dataList.add(record);
            Serializer serializer = new Persister();
            if (type == User.class) {
                Users users = new Users();
                users.setUserList((List<User>) dataList);
                File file = new File(xmlFilePath);
                serializer.write(users, file);
            } else if (type == Competition.class) {
                Competitions competitions = new Competitions();
                competitions.setCompetitionList((List<Competition>) dataList);
                File file = new File(xmlFilePath);
                serializer.write(competitions, file);
                logger.info("Сохранено соревнование: {}", record);
            } else if (type == Catch.class) {
                Catches catches = new Catches();
                catches.setCatchList((List<Catch>) dataList);
                File file = new File(xmlFilePath);
                serializer.write(catches, file);
            }
            logger.info("Успешно сохранена запись: {}", record);
        } catch (Exception e) {
            logger.error("Ошибка при сохранении записи в XML");
        }
    }

    @Override
    public void deleteRecord(String id) {
        boolean recordRemoved = false;
        for (Iterator<T> iterator = dataList.iterator(); iterator.hasNext(); ) {
            T record = iterator.next();
            if (record instanceof User && ((User) record).getId().equals(id)) {
                iterator.remove();
                recordRemoved = true;
                break;
            }
            if (record instanceof Competition && ((Competition) record).getId().equals(id)) {
                iterator.remove();
                recordRemoved = true;
                break;
            }
            if (record instanceof Catch && ((Catch) record).getId().equals(id)) {
                iterator.remove();
                recordRemoved = true;
                break;
            }
        }
        if (recordRemoved) {
            try {
                Serializer serializer = new Persister();
                if (type == User.class) {
                    Users users = new Users();
                    users.setUserList((List<User>) dataList);
                    File file = new File(xmlFilePath);
                    serializer.write(users, file);
                } else if (type == Competition.class) {
                    Competitions competitions = new Competitions();
                    competitions.setCompetitionList((List<Competition>) dataList);
                    File file = new File(xmlFilePath);
                    serializer.write(competitions, file);
                } else if (type == Catch.class) {
                    Catches catches = new Catches();
                    catches.setCatchList((List<Catch>) dataList);
                    File file = new File(xmlFilePath);
                    serializer.write(catches, file);
                }
                logger.info("Запись с ID {} успешно удалена.", id);
            } catch (Exception e) {
                logger.error("Ошибка при удалении записи из XML");
            }
        } else {
            logger.warn("Запись с ID {} не найдена для удаления.", id);
        }
    }

    @Override
    public T getRecordById(String id) {
        for (T record : dataList) {
            if (record instanceof User && ((User) record).getId().equals(id)) {
                return record;
            }
            if (record instanceof Competition && ((Competition) record).getId().equals(id)) {
                return record;
            }
            if (record instanceof Catch && ((Catch) record).getId().equals(id)) {
                return record;
            }
        }
        logger.warn("Запись с ID {} не найдена.", id);
        throw new NoSuchElementException("Запись с ID " + id + " не найдена.");
    }

    @Override
    public void updateRecord(T record) {
        boolean isUpdated = false;
        for (int i = 0; i < dataList.size(); i++) {
            T currentRecord = dataList.get(i);
            if (currentRecord instanceof User && ((User) currentRecord).getId().equals(((User) record).getId())) {
                dataList.set(i, record);
                isUpdated = true;
                break;
            }
            if (currentRecord instanceof Competition && ((Competition) currentRecord).getId().equals(((Competition) record).getId())) {
                dataList.set(i, record);
                isUpdated = true;
                break;
            }
            if (currentRecord instanceof Catch && ((Catch) currentRecord).getId().equals(((Catch) record).getId())) {
                dataList.set(i, record);
                isUpdated = true;
                break;
            }
        }

        if (!isUpdated) {
            logger.error("Запись с ID {} не найдена для обновления.", ((User) record).getId());
            throw new NoSuchElementException("Запись с ID " + ((User) record).getId() + " не найдена для обновления.");
        }

        try {
            Serializer serializer = new Persister();
            if (type == User.class) {
                Users users = new Users();
                users.setUserList((List<User>) dataList);
                File file = new File(xmlFilePath);
                serializer.write(users, file);
            } else if (type == Competition.class) {
                Competitions competitions = new Competitions();
                competitions.setCompetitionList((List<Competition>) dataList);
                File file = new File(xmlFilePath);
                serializer.write(competitions, file);
                logger.info("Обновлено соревнование: {}", record);
            } else if (type == Catch.class) {
                Catches catches = new Catches();
                catches.setCatchList((List<Catch>) dataList);
                File file = new File(xmlFilePath);
                serializer.write(catches, file);
            }
            logger.info("Запись с ID {} успешно обновлена.", ((User) record).getId());
        } catch (Exception e) {
            logger.error("Ошибка при обновлении записи в XML");
        }
    }
}
