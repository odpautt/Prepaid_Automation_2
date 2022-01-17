package com.indra.actions;

import javax.xml.bind.SchemaOutputResolver;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {
    public DatabaseConnection() throws SQLException {
        Connection con= DriverManager.getConnection("jdbc:oracle:thin:10.69.60.89:1521/DEV11G","ACTIVATOR","ACTIVATOR");

        Statement stmt=con.createStatement();

        String s="SET serveroutput ON;\n" +
                "BEGIN\n" +
                "AL_RE_ACTIVADOR ('3046010569');\n" +
                "END;";
        stmt.executeQuery(s);
        String compare = "dads";

        con.close();
        System.out.println("program is exited");
    }
}

