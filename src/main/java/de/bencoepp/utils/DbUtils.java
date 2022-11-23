package de.bencoepp.utils;

import java.sql.*;

public class DbUtils {

    Connection con;

    public DbUtils() throws ClassNotFoundException {
        Class.forName("org.sqlite.JDBC");
        try {
            String userDir = System.getProperty("user.dir");
            con = DriverManager.getConnection("jdbc:sqlite:" + userDir + "\\sql.db");
            Statement statement = con.createStatement();
            statement.setQueryTimeout(30);
        }catch (Exception e){
            System.out.println(e);
        }
    }

    public Connection getCon() {
        return con;
    }

    public void setCon(Connection con) {
        this.con = con;
    }
}
