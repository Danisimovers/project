package sfedu.danil.api;

import java.util.NoSuchElementException;
import com.opencsv.*;
import com.opencsv.exceptions.CsvException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sfedu.danil.models.Role;
import sfedu.danil.models.User;
import java.io.*;
import java.util.*;

public class DataProviderCsv implements IDataProvider<User> {

    Logger logger = LogManager.getLogger(DataProviderCsv.class);

    private String csvFilePath;
    private List<User> userList;

    public DataProviderCsv(String filePath) {
        this.csvFilePath = filePath;
        this.userList = new ArrayList<>();
        initDataSource();
    }
    //NUll обработка изменить!
    @Override
    public void initDataSource() {
        try (CSVReader reader = new CSVReader(new FileReader(csvFilePath))) {
            List<String[]> csvData = reader.readAll();
            userList = Optional.ofNullable(userList).orElseGet(ArrayList::new);
            for (String[] row : csvData) {
                String id = row[0];
                String name = row[1];
                String email = row[2];
                String phoneNumber = row[3];
                Role role = Role.valueOf(row[4]);
                String rating = row[5];
                Optional<String> competitionId = row.length > 6 ? Optional.ofNullable(row[6]) : Optional.empty();

                User user = new User(name, email, phoneNumber, role, rating, competitionId.orElse(null));
                user.setId(id);
                userList.add(user);

            }
            logger.info("Успешно инициализированы данные из CSV: {} записей", userList.size());
        } catch (IOException e) {
            logger.error("Ошибка при чтении CSV", e);
        } catch (IllegalArgumentException e) {
            logger.error("Ошибка при преобразовании роли", e);
        } catch (CsvException e) {
            logger.error("Ошибка валидации CSV", e);
        }
    }

    @Override
    public void saveRecord(User record) {
        try (CSVWriter writer = new CSVWriter(new FileWriter(csvFilePath, true))) {
            String[] values = {
                    record.getId(),
                    record.getName(),
                    record.getEmail(),
                    record.getPhoneNumber(),
                    record.getRole().toString(),
                    record.getRating(),
                    record.getCompetitionId()
            };
            writer.writeNext(values);
            if (userList == null) {
                userList = new ArrayList<>();
            }
            userList.add(record);
            logger.info("Успешно сохранена запись: {}", record);
        } catch (IOException e) {
            logger.error("Ошибка при сохранении записи в CSV", e);
        }
    }

    @Override
    public void deleteRecord(String id) {
        boolean recordRemoved = userList.removeIf(record -> record.getId().equals(id));
        if (recordRemoved) {
            try (CSVWriter writer = new CSVWriter(new FileWriter(csvFilePath))) {
                for (User user : userList) {
                    String[] values = {
                            user.getId(),
                            user.getName(),
                            user.getEmail(),
                            user.getPhoneNumber(),
                            user.getRole().toString(),
                            user.getRating(),
                            user.getCompetitionId()
                    };
                    writer.writeNext(values);
                }
                logger.info("Успешно удалена запись с ID: {}", id);
            } catch (IOException e) {
                logger.error("Ошибка при удалении записи из CSV", e);
            }
        } else {
            logger.warn("Запись с ID {} не найдена для удаления", id);
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

        try (CSVWriter writer = new CSVWriter(new FileWriter(csvFilePath))) {
            for (User user : userList) {
                String[] values = {
                        user.getId(),
                        user.getName(),
                        user.getEmail(),
                        user.getPhoneNumber(),
                        user.getRole().toString(),
                        user.getRating(),
                        user.getCompetitionId()
                };
                writer.writeNext(values);
            }
            logger.info("Успешно обновлена запись: {}", record);
        } catch (IOException e) {
            logger.error("Ошибка при обновлении записи в CSV", e);
        }
    }
}
