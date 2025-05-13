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
    public boolean initDataSource() {
        boolean isSuccess = true;

        try (CSVReader reader = new CSVReader(new FileReader(csvFilePath))) {
            List<String[]> csvData = reader.readAll();
            userList = Optional.ofNullable(userList).orElseGet(ArrayList::new);
            for (String[] row : csvData) {
                String id = row[0];// есть цикл, но все равно 0..5
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

            //есть catch, но логики никакой нет, кроме написание логов. добавить логику.(сделал)
        } catch (IOException e) {
            logger.error("Ошибка при чтении CSV", e);
            isSuccess = false;
        } catch (IllegalArgumentException e) {
            logger.error("Ошибка при преобразовании роли", e);
            isSuccess = false;
        } catch (CsvException e) {
            logger.error("Ошибка валидации CSV", e);
            isSuccess = false;
        }
        return isSuccess;
    }

    @Override
    public boolean saveRecord(User record) {
        boolean isSuccess = false;

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
            //да, окей, ты сохранил запись, но будет тогда правильно вернуть хотя true чтобы программа знала о статусе записи
            isSuccess = true;
        } catch (IOException e) {
            logger.error("Ошибка при сохранении записи в CSV", e);
            isSuccess = false;
        }
        return isSuccess;
    }

    @Override
    public boolean deleteRecord(String id) {
        boolean isSuccess = false;
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
                //да, окей, ты сохранил запись, но будет тогда правильно вернуть хотя true чтобы программа знала о статусе записи
                isSuccess = true;
            } catch (IOException e) {
                logger.error("Ошибка при удалении записи из CSV", e);
                isSuccess = false;
            }
        } else {
            logger.warn("Запись с ID {} не найдена для удаления", id);
            isSuccess = false;
        }
        return isSuccess;
    }

    @Override
    public User getRecordById(String id) {
        try {
            for (User user : userList) {
                if (user.getId().equals(id)) {
                    logger.info("Запись с ID {} найдена: {}", id, user);
                    return user;
                }
            }
            logger.warn("Запись с ID {} не найдена.", id);
            throw new NoSuchElementException("Запись с ID " + id + " не найдена."); //вот так не делается, использовать try catch
        } catch (NoSuchElementException e) {
            logger.error("Ошибка: {}", e.getMessage());
            throw e;
        }
    }


    @Override
    public boolean updateRecord(User record) {
        boolean isUpdated = false;
        boolean isSuccess = false;

        for (int i = 0; i < userList.size(); i++) {
            if (userList.get(i).getId().equals(record.getId())) {
                userList.set(i, record);
                isUpdated = true;
                break;
            }
        }
        if (!isUpdated) {
            logger.error("Запись с ID {} не найдена для обновления.", record.getId());
            throw new NoSuchElementException("Запись с ID " + record.getId() + " не найдена для обновления.");//вот так не делается, использовать try catch
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
            isSuccess = true;
        } catch (IOException e) { // добавить логику
            logger.error("Ошибка при обновлении записи в CSV", e);
            isSuccess = false;
        }
        return isSuccess;
    }
}
