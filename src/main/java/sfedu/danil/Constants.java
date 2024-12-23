package sfedu.danil;

public class Constants {
    public static final String CONFIG_FILE_PATH = "./src/main/resources/environment.properties";
    public static final String LOG_FILE_PATH = "logs/app.log";
    public static final String XML_CONFIG_PATH = "./src/main/resources/environment.xml";
    public static final String YAML_CONFIG_PATH = "./src/main/resources/environment.yaml";
    public static final String CONNECTION_STRING = "mongodb://localhost:27017";
    public static final String DATABASE_NAME = "historyDB";
    public static final String COLLECTION_NAME = "historyContent";
    public static final String TEST_CSV_FILE = "test_users.csv";
    public static final String TEST_XML_FILE = "test_users.xml";
    public static final String DB_PROPERTIES_PATH = "./src/main/resources/database.properties";


    public static final String CREATE_USER_QUERY = "INSERT INTO users (id, name, email, phone_number, role, rating, competition_id, created_at, updated_at) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)";
    public static final String READ_USER_QUERY = "SELECT * FROM users WHERE id = ?";
    public static final String UPD_USER_QUERY = "UPDATE users SET name = ?, email = ?, phone_number = ?, role = ?, rating = ?, competition_id = ?, updated_at = CURRENT_TIMESTAMP " +
            "WHERE id = ?";
    public static final String DEL_USER_QUERY = "DELETE FROM users WHERE id = ?";
    public static final String RDALL_USER_QUERY = "SELECT * FROM users";


    public static final String CREATE_COMP_QUERY = "INSERT INTO competitions (id, name, date) VALUES (?, ?, ?)";
    public static final String READ_COMP_QUERY = "SELECT * FROM competitions WHERE id = ?";
    public static final String UPD_COMP_QUERY = "UPDATE competitions SET name = ?, date = ? WHERE id = ?";
    public static final String DEL_COMP_QUERY = "DELETE FROM competitions WHERE id = ?";
    public static final String RDALL_COMP_QUERY = "SELECT * FROM competitions";


    public static final String CREATE_CATCH_QUERY = "INSERT INTO catches (id, weight, points, user_id, competition_id, fish_type, created_at, updated_at) " +
            "VALUES (?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)";
    public static final String READ_CATCH_QUERY = "SELECT * FROM catches WHERE id = ?";
    public static final String UPD_CATCH_QUERY = "UPDATE catches SET weight = ?, points = ?, user_id = ?, competition_id = ?, fish_type = ?, updated_at = CURRENT_TIMESTAMP " +
            "WHERE id = ?";
    public static final String DEL_CATCH_QUERY = "DELETE FROM catches WHERE id = ?";
    public static final String RDALL_CATCH_QUERY = "SELECT * FROM catches";
}
