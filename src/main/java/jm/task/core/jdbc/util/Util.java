package jm.task.core.jdbc.util;

import jm.task.core.jdbc.model.User;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.service.ServiceRegistry;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public final class Util {
    public static final String URL = "jdbc:mysql://localhost:3306/DBPP";
    public static final String USER = "root";
    public static final String PASSWORD = "root";
    public static Connection connection;

    private static SessionFactory sessionFactory;

    private Util() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    public static Connection getConnection() {
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return connection;

    }

    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

    }

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try {
                Configuration configuration = new Configuration();
                Properties settings = new Properties();
                settings.put(Environment.DRIVER, "com.mysql.cj.jdbc.Driver");
                settings.put(Environment.URL, URL + "?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true");
                settings.put(Environment.USER, USER);
                settings.put(Environment.PASS, PASSWORD);
                settings.put(Environment.DIALECT, "org.hibernate.dialect.MySQL8Dialect");
                settings.put(Environment.SHOW_SQL, "true");
                settings.put(Environment.FORMAT_SQL, "true");
                settings.put(Environment.HBM2DDL_AUTO, "none"); // Управление таблицами вручную через DAO
                settings.put(Environment.CURRENT_SESSION_CONTEXT_CLASS, "thread");

                configuration.setProperties(settings);
                configuration.addAnnotatedClass(User.class); // Регистрация сущности

                ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                        .applySettings(configuration.getProperties())
                        .build();

                sessionFactory = configuration.buildSessionFactory(serviceRegistry);
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("Failed to create Hibernate SessionFactory", e);
            }
        }
        return sessionFactory;
    }
}
