package sfedu.danil.cli;

import sfedu.danil.dao.CatchDao;
import sfedu.danil.dao.CompetitionDao;
import sfedu.danil.dao.UserDao;
import sfedu.danil.models.Catch;
import sfedu.danil.models.Competition;
import sfedu.danil.models.Role;
import sfedu.danil.models.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class FishMatch {

    private static final CompetitionDao competitionDao = new CompetitionDao();
    private static final UserDao userDao = new UserDao();
    private static final CatchDao catchDao = new CatchDao();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        // Приветствие и выбор типа данных
        System.out.println("Добро пожаловать в систему управления рыболовными соревнованиями!");
        System.out.println("Выберите тип данных, с которым хотите работать:");
        System.out.println("1. PostgreSQL");
        System.out.println("2. MongoDB");
        System.out.println("3. XML (Заглушка)");
        System.out.println("4. CSV (Заглушка)");
        System.out.print("Введите номер выбора: ");

        String dataChoice = scanner.nextLine();

        switch (dataChoice) {
            case "1":
                System.out.println("Вы выбрали работу с PostgreSQL.");
                break;
            case "2":
                System.out.println("Вы выбрали работу с MongoDB.");
                break;
            case "3":
                System.out.println("XML поддержка пока не реализована.");
                return; // Завершаем программу, если выбран XML
            case "4":
                System.out.println("CSV поддержка пока не реализована.");
                return; // Завершаем программу, если выбран CSV
            default:
                System.out.println("Неверный выбор. Программа завершена.");
                return; // Завершаем программу при неверном выборе
        }

        while (running) {
            System.out.println("\n=== Главное меню ===");
            System.out.println("1. Управление соревнованиями");
            System.out.println("2. Управление пользователями");
            System.out.println("3. Управление уловами");
            System.out.println("4. Выйти");
            System.out.print("Выберите опцию: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    manageCompetitions(scanner);
                    break;
                case "2":
                    manageUsers(scanner);
                    break;
                case "3":
                    manageCatches(scanner);
                    break;
                case "4":
                    running = false;
                    break;
                default:
                    System.out.println("Неверный выбор. Попробуйте снова.");
            }
        }

        scanner.close();
    }

    private static void manageCatches(Scanner scanner) {
        boolean catchRunning = true;

        while (catchRunning) {
            System.out.println("\n=== Меню Улов ===");
            System.out.println("1. Создать улов");
            System.out.println("2. Просмотреть улов");
            System.out.println("3. Обновить улов");
            System.out.println("4. Удалить улов");
            System.out.println("5. Посмотреть все уловы");
            System.out.println("6. Вернуться в главное меню");
            System.out.print("Выберите опцию: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    createCatch(scanner);
                    break;
                case "2":
                    readCatch(scanner);
                    break;
                case "3":
                    updateCatch(scanner);
                    break;
                case "4":
                    deleteCatch(scanner);
                    break;
                case "5":
                    getAllCatches();
                    break;
                case "6":
                    catchRunning = false;
                    break;
                default:
                    System.out.println("Неверный выбор. Попробуйте снова.");
            }
        }
    }


    private static void manageUsers(Scanner scanner) {
        boolean userRunning = true;

        while (userRunning) {
            System.out.println("\n=== Меню пользователей ===");
            System.out.println("1. Создать пользователя");
            System.out.println("2. Просмотреть пользователя");
            System.out.println("3. Обновить пользователя");
            System.out.println("4. Удалить пользователя");
            System.out.println("5. Посмотреть всех пользователей");
            System.out.println("6. Вернуться в главное меню");
            System.out.print("Выберите опцию: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    createUser(scanner);
                    break;
                case "2":
                    readUser(scanner);
                    break;
                case "3":
                    updateUser(scanner);
                    break;
                case "4":
                    deleteUser(scanner);
                    break;
                case "5":
                    getAllUsers();
                    break;
                case "6":
                    userRunning = false;
                    break;
                default:
                    System.out.println("Неверный выбор. Попробуйте снова.");
            }
        }
    }

    private static void manageCompetitions(Scanner scanner) {
        boolean competitionRunning = true;

        while (competitionRunning) {
            System.out.println("\n=== Меню соревнований ===");
            System.out.println("1. Создать соревнование");
            System.out.println("2. Просмотреть соревнование");
            System.out.println("3. Обновить соревнование");
            System.out.println("4. Удалить соревнование");
            System.out.println("5. Посмотреть все соревнования");
            System.out.println("6. Вернуться в главное меню");
            System.out.print("Выберите опцию: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    createCompetition(scanner);
                    break;
                case "2":
                    readCompetition(scanner);
                    break;
                case "3":
                    updateCompetition(scanner);
                    break;
                case "4":
                    deleteCompetition(scanner);
                    break;
                case "5":
                    getAllCompetitions();
                    break;
                case "6":
                    competitionRunning = false;
                    break;
                default:
                    System.out.println("Неверный выбор. Попробуйте снова.");
            }
        }
    }

    private static void createUser(Scanner scanner) {
        try {
            System.out.println("\n--- Создание пользователя ---");
            System.out.print("Имя: ");
            String name = scanner.nextLine();
            System.out.print("Электронная почта: ");
            String email = scanner.nextLine();
            System.out.print("Номер телефона: ");
            String phoneNumber = scanner.nextLine();
            System.out.print("Роль (ORGANIZER/PARTICIPANT): ");
            Role role = Role.valueOf(scanner.nextLine().toUpperCase());
            System.out.print("Рейтинг (0.00 - 9.99): ");
            String rating = scanner.nextLine();
            System.out.print("ID соревнования: ");
            String competitionId = scanner.nextLine();
            if (competitionId.isEmpty()) competitionId = null;

            User user = new User(name, email, phoneNumber, role, rating, competitionId);
            userDao.create(user);
            System.out.println("Пользователь успешно создан!");
        } catch (Exception e) {
            System.out.println("Ошибка при создании пользователя: " + e.getMessage());
        }
    }

    private static void readUser(Scanner scanner) {
        try {
            System.out.println("\n--- Просмотр данных пользователя ---");
            System.out.print("Введите ID пользователя: ");
            String id = scanner.nextLine();

            userDao.read(id).ifPresentOrElse(
                    user -> System.out.println("Пользователь найден: " + user),
                    () -> System.out.println("Пользователь не найден.")
            );
        } catch (Exception e) {
            System.out.println("Ошибка при чтении данных пользователя: " + e.getMessage());
        }
    }

    private static void updateUser(Scanner scanner) {
        try {
            System.out.println("\n--- Обновление данных пользователя ---");
            System.out.print("Введите ID пользователя: ");
            String id = scanner.nextLine();

            userDao.read(id).ifPresentOrElse(user -> {
                try {
                    System.out.print("Новое имя (текущее: " + user.getName() + "): ");
                    String name = scanner.nextLine();
                    if (!name.isEmpty()) user.setName(name);

                    System.out.print("Новый Email (текущий: " + user.getEmail() + "): ");
                    String email = scanner.nextLine();
                    if (!email.isEmpty()) user.setEmail(email);

                    System.out.print("Новый номер телефона (текущий: " + user.getPhoneNumber() + "): ");
                    String phoneNumber = scanner.nextLine();
                    if (!phoneNumber.isEmpty()) user.setPhoneNumber(phoneNumber);

                    System.out.print("Новая роль (текущая: " + user.getRole() + "): ");
                    String role = scanner.nextLine();
                    if (!role.isEmpty()) user.setRole(Role.valueOf(role.toUpperCase()));

                    System.out.print("Новый рейтинг (текущий: " + user.getRating() + "): ");
                    String rating = scanner.nextLine();
                    if (!rating.isEmpty()) user.setRating(rating);

                    System.out.print("Новый ID соревнования (текущий: " + user.getCompetitionId() + "): ");
                    String competitionId = scanner.nextLine();
                    user.setCompetitionId(competitionId.isEmpty() ? null : competitionId);

                    userDao.update(user);
                    System.out.println("Данные пользователя успешно обновлены!");
                } catch (Exception e) {
                    System.out.println("Ошибка при обновлении данных пользователя: " + e.getMessage());
                }
            }, () -> System.out.println("Пользователь не найден."));
        } catch (Exception e) {
            System.out.println("Ошибка при чтении данных пользователя: " + e.getMessage());
        }
    }

    private static void deleteUser(Scanner scanner) {
        try {
            System.out.println("\n--- Удаление пользователя ---");
            System.out.print("Введите ID пользователя: ");
            String id = scanner.nextLine();
            userDao.delete(id);
            System.out.println("Пользователь успешно удалён!");
        } catch (Exception e) {
            System.out.println("Ошибка при удалении пользователя: " + e.getMessage());
        }
    }

    private static void getAllUsers() {
        try {
            System.out.println("\n--- Все пользователи ---");
            userDao.getAll().forEach(System.out::println);
        } catch (Exception e) {
            System.out.println("Ошибка при получении списка пользователей: " + e.getMessage());
        }
    }

    private static void createCompetition(Scanner scanner) {
        try {
            System.out.println("\n--- Создание соревнования ---");
            System.out.print("Название соревнования: ");
            String name = scanner.nextLine();
            System.out.print("Дата соревнования (гггг-мм-дд чч:мм): ");
            String dateInput = scanner.nextLine();
            LocalDateTime date = LocalDateTime.parse(dateInput, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
            Competition competition = new Competition(name, date);
            competitionDao.create(competition);
            System.out.println("Соревнование успешно создано!");
        } catch (Exception e) {
            System.out.println("Ошибка при создании соревнования: " + e.getMessage());
        }
    }


    private static void readCompetition(Scanner scanner) {
        try {
            System.out.println("\n--- Просмотр данных соревнования ---");
            System.out.print("Введите ID соревнования: ");
            String id = scanner.nextLine();

            competitionDao.read(id).ifPresentOrElse(
                    competition -> System.out.println("Соревнование найдено: " + competition),
                    () -> System.out.println("Соревнование не найдено.")
            );
        } catch (Exception e) {
            System.out.println("Ошибка при чтении данных соревнования: " + e.getMessage());
        }
    }


    private static void updateCompetition(Scanner scanner) {
        try {
            System.out.println("\n--- Обновить данные соревнования ---");
            System.out.print("Введите ID соревнования: ");
            String id = scanner.nextLine();

            competitionDao.read(id).ifPresentOrElse(competition -> {
                try {
                    System.out.print("Новое название (текущее: " + competition.getName() + "): ");
                    String name = scanner.nextLine();
                    if (!name.isEmpty()) competition.setName(name);

                    System.out.print("Новая дата (текущая: " + competition.getDate() + "): ");
                    String dateStr = scanner.nextLine();
                    if (!dateStr.isEmpty()) competition.setDate(LocalDateTime.parse(dateStr));

                    competitionDao.update(competition);
                    System.out.println("Данные соревнования обновлены успешно!");
                } catch (Exception e) {
                    System.out.println("Ошибка при обновлении данных соревнования: " + e.getMessage());
                }
            }, () -> System.out.println("Соревнование не найдено."));
        } catch (Exception e) {
            System.out.println("Ошибка при чтении данных соревнования: " + e.getMessage());
        }
    }


    private static void deleteCompetition(Scanner scanner) {
        try {
            System.out.println("\n--- Удалить соревнование ---");
            System.out.print("Введите ID соревнования: ");
            String id = scanner.nextLine();
            competitionDao.delete(id);
            System.out.println("Соревнование удалено успешно!");
        } catch (Exception e) {
            System.out.println("Ошибка при удалении соревнования: " + e.getMessage());
        }
    }


    private static void getAllCompetitions() {
        try {
            System.out.println("\n--- Все соревнования ---");
            competitionDao.getAll().forEach(System.out::println);
        } catch (Exception e) {
            System.out.println("Ошибка при получении списка соревнований: " + e.getMessage());
        }
    }


    private static void createCatch(Scanner scanner) {
        try {
            System.out.println("\n--- Создать улов ---");
            System.out.print("Вид рыбы: ");
            String fishType = scanner.nextLine();
            System.out.print("Вес: ");
            double weight = Double.parseDouble(scanner.nextLine());
            System.out.print("Очки: ");
            double points = Double.parseDouble(scanner.nextLine());
            System.out.print("ID пользователя: ");
            String userId = scanner.nextLine();
            System.out.print("ID соревнования: ");
            String competitionId = scanner.nextLine();

            Catch catchEntry = new Catch(fishType, weight, points, userId, competitionId);
            catchDao.create(catchEntry);
            System.out.println("Улов успешно создан!");
        } catch (Exception e) {
            System.out.println("Ошибка при создании улова: " + e.getMessage());
        }
    }


    private static void readCatch(Scanner scanner) {
        try {
            System.out.println("\n--- Просмотр данных улова ---");
            System.out.print("Введите ID улова: ");
            String id = scanner.nextLine();

            catchDao.read(id).ifPresentOrElse(
                    catchEntry -> System.out.println("Улов найден: " + catchEntry),
                    () -> System.out.println("Улов не найден.")
            );
        } catch (Exception e) {
            System.out.println("Ошибка при просмотре данных улова: " + e.getMessage());
        }
    }


    private static void updateCatch(Scanner scanner) {
        try {
            System.out.println("\n--- Обновить данные улова ---");
            System.out.print("Введите ID улова: ");
            String id = scanner.nextLine();

            catchDao.read(id).ifPresentOrElse(catchEntry -> {
                try {
                    System.out.print("Новый вид рыбы (текущий: " + catchEntry.getFishType() + "): ");
                    String fishType = scanner.nextLine();
                    if (!fishType.isEmpty()) catchEntry.setFishType(fishType);

                    System.out.print("Новый вес (текущий: " + catchEntry.getWeight() + "): ");
                    double weight = Double.parseDouble(scanner.nextLine());
                    if (weight > 0) catchEntry.setWeight(weight);

                    System.out.print("Новые очки (текущие: " + catchEntry.getPoints() + "): ");
                    double points = Double.parseDouble(scanner.nextLine());
                    if (points > 0) catchEntry.setPoints(points);

                    System.out.print("Новый ID пользователя (текущий: " + catchEntry.getUserId() + "): ");
                    String userId = scanner.nextLine();
                    if (!userId.isEmpty()) catchEntry.setUserId(userId);

                    System.out.print("Новый ID соревнования (текущий: " + catchEntry.getCompetitionId() + "): ");
                    String competitionId = scanner.nextLine();
                    catchEntry.setCompetitionId(competitionId.isEmpty() ? null : competitionId);

                    catchDao.update(catchEntry);
                    System.out.println("Данные улова успешно обновлены!");
                } catch (Exception e) {
                    System.out.println("Ошибка при обновлении данных улова: " + e.getMessage());
                }
            }, () -> System.out.println("Улов не найден."));
        } catch (Exception e) {
            System.out.println("Ошибка при чтении улова: " + e.getMessage());
        }
    }


    private static void deleteCatch(Scanner scanner) {
        try {
            System.out.println("\n--- Удалить улов ---");
            System.out.print("Введите ID улова: ");
            String id = scanner.nextLine();
            catchDao.delete(id);
            System.out.println("Улов успешно удален!");
        } catch (Exception e) {
            System.out.println("Ошибка при удалении улова: " + e.getMessage());
        }
    }

    private static void getAllCatches() {
        try {
            System.out.println("\n--- Все уловы ---");
            catchDao.getAll().forEach(System.out::println);
        } catch (Exception e) {
            System.out.println("Ошибка при получении списка уловов: " + e.getMessage());
        }
    }
}