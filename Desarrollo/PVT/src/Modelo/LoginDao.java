
package Modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class LoginDao {
    Connection conexion;
    PreparedStatement ps;
    ResultSet rs;
    Conexion cn = new Conexion();
    
    public Login login(String correo_usuario,String contrasena_usuario){
        Login log = new Login();
        String sql = "SELECT * FROM usuarios WHERE correo_usuario = ? AND contrasena_usuario = ?";
        try {
            conexion = cn.getConnection();
            ps = conexion.prepareStatement(sql);
            ps.setString(1, correo_usuario);
            ps.setString(2, contrasena_usuario);
            rs = ps.executeQuery();
            if (rs.next()) {
                log.setId_usuario(rs.getInt("id_usuario"));
                log.setNombre_usuario(rs.getString("nombre_usuario"));
                log.setCorreo_usuario(rs.getString("correo_usuario"));
                log.setContrasena_usuario(rs.getString("contrasena_usuario"));
            }
        } catch (SQLException e) {
            System.out.println(e.toString());
        }
        return log;
    }
    
    
}
