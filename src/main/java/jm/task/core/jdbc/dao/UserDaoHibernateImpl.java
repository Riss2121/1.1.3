package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;

import java.util.List;

public class UserDaoHibernateImpl implements UserDao {
    public UserDaoHibernateImpl() {

    }


    @Override
    public void createUsersTable() {
        String sql = "CREATE TABLE IF NOT EXISTS users (" +
                "id BIGINT AUTO_INCREMENT PRIMARY KEY, " +
                "name VARCHAR(255), " +
                "lastName VARCHAR(255), " +
                "age TINYINT" +
                ")";
        try (Session session = Util.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            NativeQuery<?> query = session.createNativeQuery(sql);
            query.executeUpdate();
            tx.commit();
        } catch (Exception e) {
            throw new RuntimeException("Не удалось создать таблицу", e);
        }
    }

    @Override
    public void dropUsersTable() {
        String sql = "DROP TABLE IF EXISTS users";
        try (Session session = Util.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            NativeQuery<?> query = session.createNativeQuery(sql);
            query.executeUpdate();
            tx.commit();
        } catch (Exception e) {
            throw new RuntimeException("Не удалось удалить таблицу", e);
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        User user = new User(name, lastName, age);
        try (Session session = Util.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.save(user);
            tx.commit();
        } catch (Exception e) {
            throw new RuntimeException("Не удалось сохранить пользователя", e);
        }
    }

    @Override
    public void removeUserById(long id) {
        try (Session session = Util.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            User user = session.get(User.class, id);
            if (user != null) {
                session.delete(user);
            }
            tx.commit();
        } catch (Exception e) {
            throw new RuntimeException("Не удалось удалить пользователя по ID", e);
        }
    }

    @Override
    public List<User> getAllUsers() {
        try (Session session = Util.getSessionFactory().openSession()) {
            Query<User> query = session.createQuery("FROM User", User.class);
            return query.list();
        } catch (Exception e) {
            throw new RuntimeException("Не удалось получить список пользователей", e);
        }
    }

    @Override
    public void cleanUsersTable() {
        String sql = "TRUNCATE TABLE users";
        try (Session session = Util.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            NativeQuery<?> query = session.createNativeQuery(sql);
            query.executeUpdate();
            tx.commit();
        } catch (Exception e) {
            throw new RuntimeException("Не удалось очистить таблицу", e);
        }
    }
}
