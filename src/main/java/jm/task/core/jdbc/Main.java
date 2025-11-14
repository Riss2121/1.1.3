package jm.task.core.jdbc;

import jm.task.core.jdbc.service.UserService;
import jm.task.core.jdbc.service.UserServiceImpl;
import jm.task.core.jdbc.util.Util;

public class Main {

    public static void main(String[] args) {
        UserService userService = new UserServiceImpl();
        try {
            userService.createUsersTable();
            userService.saveUser("Vage", "kata", (byte) 23);
            userService.saveUser("Kostia", "kata", (byte) 30);
            userService.saveUser("Kata", "kata", (byte) 23);
            userService.saveUser("Canya", "kata", (byte) 50);
            userService.getAllUsers().forEach(System.out::println);
            userService.cleanUsersTable();
            userService.dropUsersTable();
        } finally {
            Util.closeConnection();
        }
    }
}
