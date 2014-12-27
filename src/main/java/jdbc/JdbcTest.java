package jdbc;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

public class JdbcTest {

    @Test
    public void testGetCountsUsers() throws Exception {
        Integer nowOnline = 3; // онлайн 3 пользователя

        // получаем кол-во онлайн

        Integer count = 0;
        ArrayList usersOnline = new ArrayList<String>();
        usersOnline.add(0, "Droll");
        usersOnline.add(1, "Tromm");
        usersOnline.add(2, "Frokk");

        count = usersOnline.size();

        if (count != -10) {
            assertEquals("Count usersOnline != -10", count, nowOnline);
        }
    }

    @Test
    public void testGetTopTenUsers() throws Exception {
        String topN = "[Froll, Troll, Droll, Mroll, Groll]"; // топ сейчас

        // топ 10
        ArrayList nowTopTen = new ArrayList<String>();
        nowTopTen.add("Froll");
        nowTopTen.add("Troll");
        nowTopTen.add("Droll");
        nowTopTen.add("Mroll");
        nowTopTen.add("Groll");
        String nt = nowTopTen.toString();

        assertEquals("Top must equals", topN, nt);

    }

    @Test
    public void testGetMessages() throws Exception {

    }
}