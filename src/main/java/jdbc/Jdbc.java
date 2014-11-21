package jdbc;

import javax.xml.transform.Result;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by CM on 25.10.2014.
 */
public class Jdbc {

    private String login;
    private String password;
    private String name;
    private Integer id;
    private String family;
    private Connection con;

    public Jdbc (String login, String password) {
        this.login = login;
        this.password = password;

        this.con = connectToDb();
        if (con != null) {
            boolean lg = loginInProjec(login, password);
            if (lg) {
//                System.out.println("User isset!");
//                System.out.println(getCountsUsers());
//                String top[] = getTopTenUsers();
//
//                int j = 1;
//                for (int i = 0; i < top.length; i++) {
//                    if (top[i] != null) {
//                        System.out.println(j + ": " + top[i]);
//                        j++;
//                    }
//                }
//
//                String top1[] = getLastTopTenUsers();
//                int k = 1;
//                for (int i = 0; i < top1.length; i++) {
//                    if (top1[i] != null) {
//                        System.out.println(k + ": " + top1[i]);
//                        k++;
//                    }
//                }
//                if (k == 1) {
//                    System.out.println("Топ последних не заходивших пуст!");
//                }
//
//                getMessages(getId());
//
//                //addNewMessage("fgnfdlgdsfgljdfskjglkdfs");
//
//                System.out.println(getAllMessages().toString());
//
//                clearSession();
//
//                System.out.println(getLogin() + " " + getName() + " " + getId());
                System.out.println("Пользователь - " + getLogin() + " авторизован!");
            } else {
                System.out.println("Неверный логин или пароль, или нет соединения с БД! :(");
            }
        }
    }

    /**
     * Регистрация нового пользователя.
     * @param fam
     * @param name
     * @param login
     * @param password
     */
    public Jdbc (String fam, String name, String login, String password) {
        this.login = login;
        this.password = password;

        this.con = connectToDb();
        if (con != null) {
            boolean lg = checkLogin(login);
            if (lg) {
                System.out.println("Пользователь с таким логином уже сущесвует! Выберите другой логин!");
            } else {
                try {
                    boolean nwus = createNewUser(fam, name, login, password);
                    if (nwus) {
                        System.out.println("Поздравляем! Вы успешно зарегистрированы в системе!");
                    } else {
                        System.out.println("Возможно нет подключения к базе данных, или вы незаполнили все поля!");
                    }
                } catch (SQLException e) {
                    System.out.println("Нет подключения к БД!");
                }
            }
        }
    }

    /**
     * Получить логин.
     * @return
     */
    public String getLogin() {
        return this.login;
    }

    /**
     * Получить имя.
     * @return
     */
    public String getName() {
        return this.name;
    }

    /**
     * Получить пароль.
     * @return
     */
    private String getPassword() {
        return this.password;
    }

    /**
     * Получить ИД.
     * @return
     */
    public Integer getId() {
        return id;
    }

    /**
     * Установка пользовательских данных
     * @param login_
     * @param password_
     * @param id_
     * @param family_
     * @param name_
     */
    private void setUserData (String login_, String password_, Integer id_, String family_, String name_) {
        this.login = login_;
        this.password = password_;
        this.id = id_;
        this.family = family_;
        this.name = name_;
    }

    /**
     * Очистка сохраненных пользовательских данных.
     */
    private  void setNullUserData() {
        this.login = null;
        this.password = null;
        this.id = null;
        this.family = null;
        this.name = null;
    }

    /**
     * Соединение с БД.
     * @return
     */
    private Connection connectToDb() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = null;
            conn = DriverManager.getConnection("jdbc:mysql://localhost/jdbc", "root", "");
            return conn;
        } catch (Exception e) {
            System.out.println("SQLException: " + e.getMessage());
            return null;
        }
    }

    /**
     * Авторизация в проекте.
     * @param login
     * @param password
     * @return
     */
    private boolean loginInProjec(String login, String password) {
        try {
            Statement stmt = this.con.createStatement();

            String sql = "SELECT * FROM users WHERE login = '" + login + "' AND password = '" + password + "'";

            ResultSet querySet = stmt.executeQuery(sql);

            String login_ = "";
            String password_  = "";
            Integer id_ = 0;
            String family_ = "";
            String name_ = "";

            while (querySet.next()) {
                login_ = querySet.getString("login");
                password_ = querySet.getString("password");
                id_ = querySet.getInt("id");
                name_ = querySet.getString("name");
                family_ = querySet.getString("fam");
            }

            if (login_.equals(getLogin()) && password_.equals(getPassword())) {
                setUserData(login_, password_, id_, family_, name_);

                stmt = con.createStatement();
                String query = "UPDATE users SET time = NULL WHERE id = '" + getId() + "'";
                stmt.executeUpdate(query);

                return true;
            } else {
                setNullUserData();

                return false;
            }
        } catch (SQLException e) {
            return false;
        }
    }

    /**
     * Проверка существования логина.
     * @param login
     * @return
     */
    private boolean checkLogin(String login) {
        try {
            Statement stmt = this.con.createStatement();

            String sql = "SELECT id FROM users WHERE login = '" + login + "'";

            ResultSet querySet = stmt.executeQuery(sql);

            Integer id_ = -10;

            while (querySet.next()) {
                id_ = querySet.getInt("id");
            }

            if (id_.equals(-10)) {
               return false;
            } else {
                return true;
            }
        } catch (SQLException e) {
            return false;
        }
    }

    /**
     * Создание нового пользователя.
     * @param family
     * @param name
     * @param login
     * @param password
     * @return
     * @throws SQLException
     */
    private boolean createNewUser(String family, String name, String login, String password) throws SQLException {
        String loginData[] = new String[2];
        try {
            Statement statement = this.con.createStatement();

            String query = "INSERT INTO users(id, fam, name, login, password, time) VALUES(NULL, '" + family + "', '" + name + "', '" + login + "', '" + password + "'" + ", NULL" + ")";
            statement.executeUpdate(query, Statement.RETURN_GENERATED_KEYS);

            ResultSet id = statement.getGeneratedKeys();
            System.out.println(id.getInt(1));
            setUserData(login, password, id.getInt(1), family, name);

            return true;
        } catch (Exception e) {
            setNullUserData();
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Кол-во пользователей в системе.
     * @return
     */
    public Integer getCountsUsers() {
        Integer count = 0;

        try {
            Statement snmt = this.con.createStatement();
            String query = "SELECT COUNT(id) as count FROM users";
            ResultSet querySet = snmt.executeQuery(query);

            while (querySet.next()) {
                count = querySet.getInt("count");
            }

            return count;
        } catch (SQLException e) {
            return -10;
        }
    }

    /**
     * Топ первых 10 пользователей.
     * @return
     */
    public String[] getTopTenUsers() {
        String[] top = new String[10];

        try {
            Statement snmt = this.con.createStatement();
            String query = "SELECT fam, name, login, time FROM users ORDER BY time DESC LIMIT 10 ";
            ResultSet querySet =  snmt.executeQuery(query);

            int i = 0;
            while (querySet.next()) {
                top[i] = querySet.getString("fam") + " -- " + querySet.getString("name") + " -- " + querySet.getString("login") + " -- " + querySet.getTime("time");
                i++;
            }

            return top;
        } catch (SQLException e) {
            return null;
        }
    }

    /**
     * Топ последних 10 пользователей.
     * Если этот топ, совпадает с топом 1-х 10, то он не выводится.
     * @return
     */
    public String[] getLastTopTenUsers() {
        String[] lastTop = new String[10];
        Integer[] topId = new Integer[10];
        Integer count = 0;
        try {
            Statement snmt = this.con.createStatement();
            String query = "SELECT id FROM users ORDER BY time DESC LIMIT 10";
            snmt.executeQuery(query);
            ResultSet querySet =  snmt.executeQuery(query);
            int j = 0;
            while (querySet.next()) {
                topId[j] = querySet.getInt("id");
                j++;
            }

            Integer tmpInString[] = new Integer[10];
            int ck = 0;
            for (int i = 0; i < topId.length; i++) {
                if (topId[i] != null) {
                    tmpInString[i] = topId[i];
                    ck++;
                }
            }

            String tmpStr = "";
            for (int i = 0; i < ck; i++) {
                if (i < ck - 1) {
                    tmpStr = tmpStr + tmpInString[i] + ", ";
                } else {
                    tmpStr = tmpStr + tmpInString[i];
                }
            }

            query = "SELECT id, fam, name, login, time FROM users WHERE id NOT IN (" + tmpStr + ") ORDER BY time ASC LIMIT 10";
            querySet =  snmt.executeQuery(query);
            count = getCountsUsers();
            int i = 0;
            while (querySet.next()) {
                lastTop[i] = querySet.getString("fam") + " -- " + querySet.getString("name") + " -- " + querySet.getString("login") + " -- " + querySet.getTime("time");
                i++;
            }

            return lastTop;
        } catch (SQLException e) {
            return null;
        }
    }

    /**
     * Удаление сессии пользователя.
     */
    public void clearSession() {
        setNullUserData();
    }

    /**
     * Получение списка сообщений пользователя.
     * @param id
     * @return
     */
    public ArrayList getMessages(Integer id) {
        ArrayList<String> messages = new ArrayList<String>();

        try {
            Statement snmt = this.con.createStatement();
            String query = "SELECT id, id_user, text_mess, time FROM messages WHERE id_user = " + id;
            snmt.executeQuery(query);
            ResultSet querySet =  snmt.executeQuery(query);

            while (querySet.next()) {
                messages.add(querySet.getString("text_mess") + " -- " + querySet.getTime("time") + "\n");
            }
        } catch (Exception e) {
            return null;
        }

        return messages;
    }

    /**
     * Добавление нового сообщения.
     * @param text
     */
    public void addNewMessage(String text) {
        try {
            Statement snmt = this.con.createStatement();
            String query = "INSERT INTO messages (id, id_user, text_mess, time) VALUES(NULL," + getId() + ", '" + text + "', NULL)";
            snmt.executeUpdate(query);
        } catch (Exception e) {
            System.out.println("Не удалось вставить данные!");
        }
    }

    /**
     * Получить список всех сообщений.
     * @return
     */
    public ArrayList getAllMessages() {
        ArrayList<String> messages = new ArrayList<String>();

        try {
            Statement snmt = this.con.createStatement();
            String query = "SELECT u.name, m.id, m.id_user, m.text_mess, m.time FROM users as u, messages as m WHERE u.id = m.id_user";
            snmt.executeQuery(query);
            ResultSet querySet =  snmt.executeQuery(query);

            while (querySet.next()) {
                messages.add(querySet.getString("name") + " -- " + querySet.getString("text_mess") + " -- " + querySet.getTime("time") + "\n");
            }
        } catch (Exception e) {
            return null;
        }

        return messages;
    }
}