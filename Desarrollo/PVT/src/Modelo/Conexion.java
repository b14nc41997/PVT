
package Modelo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {
    Connection conexion;
    public Connection getConnection(){
        try {
            String myBD = "jdbc:mysql://localhost:3307/puntoventa?serverTimezone=UTC";
            conexion = DriverManager.getConnection(myBD,"root","");
            return conexion;
        } catch (SQLException e) {
            System.out.println(e.toString());
        }
        return null;
    }
}
