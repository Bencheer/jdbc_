package mainpackage;

import jdbc.Jdbc;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by CM on 25.10.2014.
 */
public class Main {
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        /**
         * Авторизация пользователя
         */
        Jdbc ff = new Jdbc("1", "1");

        /**
         * Вывод имен пользователей и их сообщений.
         */
        System.out.println("Имена пользователей и их сообщения:");
        System.out.println(ff.getAllMessages().toString());
        System.out.println("________________________________________________________________");
        /**
         * Вывод кол-ва пользователей.
         */
        System.out.println("Количество пользователей: ");
        System.out.println(ff.getCountsUsers());
        System.out.println("________________________________________________________________");
        /**
         * Вывод топ 10
         */
        System.out.println("Топ 10 пользователей: ");
        String top[] = ff.getTopTenUsers();
                int j = 1;
                for (int i = 0; i < top.length; i++) {
                    if (top[i] != null) {
                        System.out.println(j + ": " + top[i]);
                        j++;
                    }
                }
        System.out.println("________________________________________________________________");
        /**
         * Вывод последних топ 10
         */
        System.out.println("Топ последних 10 пользователей: ");
        String top1[] = ff.getLastTopTenUsers();
                int k = 1;
                for (int i = 0; i < top1.length; i++) {
                    if (top1[i] != null) {
                        System.out.println(k + ": " + top1[i]);
                        k++;
                    }
                }
                if (k == 1) {
                    System.out.println("Топ последних не заходивших пуст!");
                }
        System.out.println("________________________________________________________________");
        /**
         * Вывод сообщений авторизованного пользователя
         */
        System.out.println("Мои сообщения: ");
        System.out.println(ff.getMessages(ff.getId()).toString());
        System.out.println("________________________________________________________________");
        /**
         * Отправка сообщения.
         */
        ff.addNewMessage("Привет!!!");
        System.out.println("Сообщения после отправки нового: ");
        System.out.println(ff.getMessages(ff.getId()).toString());
        System.out.println("________________________________________________________________");
        /**
         * Деавторизация.
         */
        ff.clearSession();
    }
}
