
package Modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ClienteDao {
    
    Connection conexion;
    PreparedStatement ps;
    ResultSet rs;
    Conexion cn = new Conexion();
    
    public String clienteEscogido(int dni){
        List<Cliente> listCli = new ArrayList();
        String sql = "SELECT * "
                    + "FROM clientes WHERE dni_cliente = '"+dni+"'";
        try {
            conexion = cn.getConnection();
            ps = conexion.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Cliente cli = new Cliente();
                cli.setId_cliente(rs.getInt("id_cliente"));
                cli.setDni(rs.getInt("dni_cliente"));
                cli.setNombre(rs.getString("nombre_cliente")); 
                
                return cli.getNombre();
            }
        } catch (SQLException e) {
            System.out.println(e.toString());
        }
        return null;
    }
    
    public List listarClientes(){
        List<Cliente> listCli = new ArrayList();
        String sql = "SELECT * "
                    + "FROM clientes";
        try {
            conexion = cn.getConnection();
            ps = conexion.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Cliente cli = new Cliente();
                cli.setId_cliente(rs.getInt("id_cliente"));
                cli.setDni(rs.getInt("dni_cliente"));
                cli.setNombre(rs.getString("nombre_cliente"));               
                
                listCli.add(cli);
            }
        } catch (SQLException e) {
            System.out.println(e.toString());
        }
        return listCli;
    }
    
}
