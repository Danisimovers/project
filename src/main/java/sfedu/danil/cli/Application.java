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

public class Application {

    private static final CompetitionDao competitionDao = new CompetitionDao();
    private static final UserDao userDao = new UserDao();
    private static final CatchDao catchDao = new CatchDao();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("\n=== Main Menu ===");
            System.out.println("1. Manage Competitions");
            System.out.println("2. Manage Users");
            System.out.println("3. Manage Catches");
            System.out.println("4. Exit");
            System.out.print("Choose an option: ");

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
                    System.out.println("Invalid option. Try again.");
            }
        }

        scanner.close();
    }

    private static void manageCatches(Scanner scanner) {
        boolean catchRunning = true;

        while (catchRunning) {
            System.out.println("\n=== Catch Menu ===");
            System.out.println("1. Create Catch");
            System.out.println("2. Read Catch");
            System.out.println("3. Update Catch");
            System.out.println("4. Delete Catch");
            System.out.println("5. Get All Catches");
            System.out.println("6. Back to Main Menu");
            System.out.print("Choose an option: ");

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
                    System.out.println("Invalid option. Try again.");
            }
        }
    }


    private static void manageUsers(Scanner scanner) {
        boolean userRunning = true;

        while (userRunning) {
            System.out.println("\n=== User Menu ===");
            System.out.println("1. Create User");
            System.out.println("2. Read User");
            System.out.println("3. Update User");
            System.out.println("4. Delete User");
            System.out.println("5. Get All Users");
            System.out.println("6. Back to Main Menu");
            System.out.print("Choose an option: ");

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
                    System.out.println("Invalid option. Try again.");
            }
        }
    }

    private static void manageCompetitions(Scanner scanner) {
        boolean competitionRunning = true;

        while (competitionRunning) {
            System.out.println("\n=== Competition Menu ===");
            System.out.println("1. Create Competition");
            System.out.println("2. Read Competition");
            System.out.println("3. Update Competition");
            System.out.println("4. Delete Competition");
            System.out.println("5. Get All Competitions");
            System.out.println("6. Back to Main Menu");
            System.out.print("Choose an option: ");

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
                    System.out.println("Invalid option. Try again.");
            }
        }
    }

    private static void createUser(Scanner scanner) {
        try {
            System.out.println("\n--- Create User ---");
            System.out.print("Name: ");
            String name = scanner.nextLine();
            System.out.print("Email: ");
            String email = scanner.nextLine();
            System.out.print("Phone Number: ");
            String phoneNumber = scanner.nextLine();
            System.out.print("Role (ORGANIZER/PARTICIPANT): ");
            Role role = Role.valueOf(scanner.nextLine().toUpperCase());
            System.out.print("Rating (0.00 - 9.99): ");
            String rating = scanner.nextLine();
            System.out.print("Competition ID: ");
            String competitionId = scanner.nextLine();
            if (competitionId.isEmpty()) competitionId = null;

            User user = new User(name, email, phoneNumber, role, rating, competitionId);
            userDao.create(user);
            System.out.println("User created successfully!");
        } catch (Exception e) {
            System.out.println("Error creating user: " + e.getMessage());
        }
    }

    private static void readUser(Scanner scanner) {
        try {
            System.out.println("\n--- Read User ---");
            System.out.print("Enter User ID: ");
            String id = scanner.nextLine();

            userDao.read(id).ifPresentOrElse(
                    user -> System.out.println("User found: " + user),
                    () -> System.out.println("User not found.")
            );
        } catch (Exception e) {
            System.out.println("Error reading user: " + e.getMessage());
        }
    }

    private static void updateUser(Scanner scanner) {
        try {
            System.out.println("\n--- Update User ---");
            System.out.print("Enter User ID: ");
            String id = scanner.nextLine();

            userDao.read(id).ifPresentOrElse(user -> {
                try {
                    System.out.print("New Name (current: " + user.getName() + "): ");
                    String name = scanner.nextLine();
                    if (!name.isEmpty()) user.setName(name);

                    System.out.print("New Email (current: " + user.getEmail() + "): ");
                    String email = scanner.nextLine();
                    if (!email.isEmpty()) user.setEmail(email);

                    System.out.print("New Phone Number (current: " + user.getPhoneNumber() + "): ");
                    String phoneNumber = scanner.nextLine();
                    if (!phoneNumber.isEmpty()) user.setPhoneNumber(phoneNumber);

                    System.out.print("New Role (current: " + user.getRole() + "): ");
                    String role = scanner.nextLine();
                    if (!role.isEmpty()) user.setRole(Role.valueOf(role.toUpperCase()));

                    System.out.print("New Rating (current: " + user.getRating() + "): ");
                    String rating = scanner.nextLine();
                    if (!rating.isEmpty()) user.setRating(rating);

                    System.out.print("New Competition ID (current: " + user.getCompetitionId() + "): ");
                    String competitionId = scanner.nextLine();
                    user.setCompetitionId(competitionId.isEmpty() ? null : competitionId);

                    userDao.update(user);
                    System.out.println("User updated successfully!");
                } catch (Exception e) {
                    System.out.println("Error updating user: " + e.getMessage());
                }
            }, () -> System.out.println("User not found."));
        } catch (Exception e) {
            System.out.println("Error reading user: " + e.getMessage());
        }
    }

    private static void deleteUser(Scanner scanner) {
        try {
            System.out.println("\n--- Delete User ---");
            System.out.print("Enter User ID: ");
            String id = scanner.nextLine();
            userDao.delete(id);
            System.out.println("User deleted successfully!");
        } catch (Exception e) {
            System.out.println("Error deleting user: " + e.getMessage());
        }
    }

    private static void getAllUsers() {
        try {
            System.out.println("\n--- All Users ---");
            userDao.getAll().forEach(System.out::println);
        } catch (Exception e) {
            System.out.println("Error fetching users: " + e.getMessage());
        }
    }

    private static void createCompetition(Scanner scanner) {
        try {
            System.out.println("\n--- Create Competition ---");
            System.out.print("Competition Name: ");
            String name = scanner.nextLine();
            System.out.print("Competition Date (yyyy-MM-dd HH:mm): ");
            String dateInput = scanner.nextLine();

            // Парсинг строки в LocalDateTime
            LocalDateTime date = LocalDateTime.parse(dateInput, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

            Competition competition = new Competition(name, date);
            competitionDao.create(competition);
            System.out.println("Competition created successfully!");
        } catch (Exception e) {
            System.out.println("Error creating competition: " + e.getMessage());
        }
    }


    private static void readCompetition(Scanner scanner) {
        try {
            System.out.println("\n--- Read Competition ---");
            System.out.print("Enter Competition ID: ");
            String id = scanner.nextLine();

            competitionDao.read(id).ifPresentOrElse(
                    competition -> System.out.println("Competition found: " + competition),
                    () -> System.out.println("Competition not found.")
            );
        } catch (Exception e) {
            System.out.println("Error reading competition: " + e.getMessage());
        }
    }

    private static void updateCompetition(Scanner scanner) {
        try {
            System.out.println("\n--- Update Competition ---");
            System.out.print("Enter Competition ID: ");
            String id = scanner.nextLine();

            competitionDao.read(id).ifPresentOrElse(competition -> {
                try {
                    System.out.print("New Name (current: " + competition.getName() + "): ");
                    String name = scanner.nextLine();
                    if (!name.isEmpty()) competition.setName(name);

                    System.out.print("New Date (current: " + competition.getDate() + "): ");
                    String dateStr = scanner.nextLine();
                    if (!dateStr.isEmpty()) competition.setDate(LocalDateTime.parse(dateStr));

                    competitionDao.update(competition);
                    System.out.println("Competition updated successfully!");
                } catch (Exception e) {
                    System.out.println("Error updating competition: " + e.getMessage());
                }
            }, () -> System.out.println("Competition not found."));
        } catch (Exception e) {
            System.out.println("Error reading competition: " + e.getMessage());
        }
    }

    private static void deleteCompetition(Scanner scanner) {
        try {
            System.out.println("\n--- Delete Competition ---");
            System.out.print("Enter Competition ID: ");
            String id = scanner.nextLine();
            competitionDao.delete(id);
            System.out.println("Competition deleted successfully!");
        } catch (Exception e) {
            System.out.println("Error deleting competition: " + e.getMessage());
        }
    }

    private static void getAllCompetitions() {
        try {
            System.out.println("\n--- All Competitions ---");
            competitionDao.getAll().forEach(System.out::println);
        } catch (Exception e) {
            System.out.println("Error fetching competitions: " + e.getMessage());
        }
    }

    private static void createCatch(Scanner scanner) {
        try {
            System.out.println("\n--- Create Catch ---");
            System.out.print("Fish Type: ");
            String fishType = scanner.nextLine();
            System.out.print("Weight: ");
            double weight = Double.parseDouble(scanner.nextLine());
            System.out.print("Points: ");
            double points = Double.parseDouble(scanner.nextLine());
            System.out.print("User ID: ");
            String userId = scanner.nextLine();
            System.out.print("Competition ID: ");
            String competitionId = scanner.nextLine();

            Catch catchEntry = new Catch(fishType, weight, points, userId, competitionId);
            catchDao.create(catchEntry);
            System.out.println("Catch created successfully!");
        } catch (Exception e) {
            System.out.println("Error creating catch: " + e.getMessage());
        }
    }

    private static void readCatch(Scanner scanner) {
        try {
            System.out.println("\n--- Read Catch ---");
            System.out.print("Enter Catch ID: ");
            String id = scanner.nextLine();

            catchDao.read(id).ifPresentOrElse(
                    catchEntry -> System.out.println("Catch found: " + catchEntry),
                    () -> System.out.println("Catch not found.")
            );
        } catch (Exception e) {
            System.out.println("Error reading catch: " + e.getMessage());
        }
    }

    private static void updateCatch(Scanner scanner) {
        try {
            System.out.println("\n--- Update Catch ---");
            System.out.print("Enter Catch ID: ");
            String id = scanner.nextLine();

            catchDao.read(id).ifPresentOrElse(catchEntry -> {
                try {
                    System.out.print("New Fish Type (current: " + catchEntry.getFishType() + "): ");
                    String fishType = scanner.nextLine();
                    if (!fishType.isEmpty()) catchEntry.setFishType(fishType);

                    System.out.print("New Weight (current: " + catchEntry.getWeight() + "): ");
                    double weight = Double.parseDouble(scanner.nextLine());
                    if (weight > 0) catchEntry.setWeight(weight);

                    System.out.print("New Points (current: " + catchEntry.getPoints() + "): ");
                    double points = Double.parseDouble(scanner.nextLine());
                    if (points > 0) catchEntry.setPoints(points);

                    System.out.print("New User ID (current: " + catchEntry.getUserId() + "): ");
                    String userId = scanner.nextLine();
                    if (!userId.isEmpty()) catchEntry.setUserId(userId);

                    System.out.print("New Competition ID (current: " + catchEntry.getCompetitionId() + "): ");
                    String competitionId = scanner.nextLine();
                    catchEntry.setCompetitionId(competitionId.isEmpty() ? null : competitionId);

                    catchDao.update(catchEntry);
                    System.out.println("Catch updated successfully!");
                } catch (Exception e) {
                    System.out.println("Error updating catch: " + e.getMessage());
                }
            }, () -> System.out.println("Catch not found."));
        } catch (Exception e) {
            System.out.println("Error reading catch: " + e.getMessage());
        }
    }

    private static void deleteCatch(Scanner scanner) {
        try {
            System.out.println("\n--- Delete Catch ---");
            System.out.print("Enter Catch ID: ");
            String id = scanner.nextLine();
            catchDao.delete(id);
            System.out.println("Catch deleted successfully!");
        } catch (Exception e) {
            System.out.println("Error deleting catch: " + e.getMessage());
        }
    }

    private static void getAllCatches() {
        try {
            System.out.println("\n--- All Catches ---");
            catchDao.getAll().forEach(System.out::println);
        } catch (Exception e) {
            System.out.println("Error fetching catches: " + e.getMessage());
        }
    }
}
