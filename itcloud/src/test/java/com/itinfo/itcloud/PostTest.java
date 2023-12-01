package com.itinfo.itcloud;

import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;

public class PostTest {
    private static final String DRIVER="org.postgresql.Driver";
    private static final String URL = "jdbc:postgresql://localhost:5432/ovirt";
    private static final String USER = "postgres";
    private static final String PW = "admin";

    @Test
    public void testConnection() throws Exception {
        Class.forName(DRIVER);

        try(Connection conn = DriverManager.getConnection(URL,USER,PW)){
            System.out.println(conn);

        }catch(Exception e) {
            e.printStackTrace();
        }
    }


}
