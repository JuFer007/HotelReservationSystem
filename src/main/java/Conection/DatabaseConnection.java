package Conection;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static DatabaseConnection instance;
    private Connection connection;
    private static final String URL = "jdbc:mysql://localhost:3306/HotelSystem?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "123456";

    private DatabaseConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Driver MySQL cargado correctamente");

            this.connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Conexión exitosa a la base de datos HotelSystem");

        } catch (ClassNotFoundException e) {
            System.err.println("ERROR: Driver de MySQL no encontrado");
            System.err.println("Asegúrate de tener mysql-connector-j en tu pom.xml");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("ERROR: No se pudo conectar a la base de datos");
            System.err.println("Verifica que:");
            System.err.println("1. MySQL esté corriendo");
            System.err.println("2. La base de datos 'HotelSystem' exista");
            System.err.println("3. Usuario: " + USER + " / Password: " + PASSWORD);
            e.printStackTrace();
        }
    }

    public static synchronized DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                System.out.println("Reconectando a la base de datos...");
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("Reconexión exitosa");
            }
        } catch (SQLException e) {
            System.err.println("ERROR al reconectar: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
        return connection;
    }

    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Conexión cerrada correctamente");
            }
        } catch (SQLException e) {
            System.err.println("Error al cerrar conexión: " + e.getMessage());
        }
    }
}
