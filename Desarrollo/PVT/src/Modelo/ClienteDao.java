package Modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class ClienteDao {
    Connection conexion;
    PreparedStatement ps;
    ResultSet rs;
    Conexion cn = new Conexion();

    public String clienteEscogido(String dni) {
        String sql = "SELECT * FROM clientes2 WHERE dni_cliente = '"+ dni +"';";
        try {
            conexion = cn.getConnection();
            ps = conexion.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Cliente cli = new Cliente();
                cli.setId_cliente(rs.getInt("id_cliente"));
                cli.setDni(rs.getString("dni_cliente"));
                cli.setNombre(rs.getString("nombre_cliente"));
                cli.setApellido(rs.getString("apellido_cliente"));
                
                return cli.getNombre()+" "+cli.getApellido();
            }
        } catch (SQLException e) {
            System.out.println(e.toString());
        } finally {
            try {
                conexion.close();
            } catch (SQLException e) {
                System.out.println(e.toString());
            }
        }
        return null;
    }
    
    public List filtrarClientes(String atributo, String valor){
        List<Cliente> listCli = new ArrayList();
        String sql = "SELECT * "
                    + "FROM clientes2 WHERE " + atributo + " like ? ORDER BY " + atributo;
        try {            
            conexion = cn.getConnection();
            ps = conexion.prepareStatement(sql);
            ps.setString(1, "%" + valor + "%");  
            rs = ps.executeQuery();
            while (rs.next()) {
                Cliente cli = new Cliente();
                cli.setId_cliente(rs.getInt("id_cliente"));
                cli.setDni(rs.getString("dni_cliente"));
                cli.setNombre(rs.getString("nombre_cliente"));
                cli.setApellido(rs.getString("apellido_cliente"));
                cli.setCelular(rs.getLong("celular_cliente"));
                cli.setInstagram(rs.getString("instagram_cliente"));
                
                listCli.add(cli);
            }
        } catch (SQLException e) {
            System.out.println(e.toString());
        }
        return listCli;
    }
    
    public boolean registrarCliente(Cliente cli) {
        String sql = "INSERT INTO clientes2 (dni_cliente, nombre_cliente, apellido_cliente, celular_cliente, instagram_cliente)"
                + "VALUES (?,?,?,?,?)";
        try {
            conexion = cn.getConnection();
            ps = conexion.prepareStatement(sql);
            ps.setString(1, cli.getDni());
            ps.setString(2, cli.getNombre());
            ps.setString(3, cli.getApellido());
            ps.setLong(4, cli.getCelular());
            ps.setString(5, cli.getInstagram());

            ps.execute();
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.toString());
            return false;
        } finally {
            try {
                conexion.close();
            } catch (SQLException e) {
                System.out.println(e.toString());
            }
        }
    }
    
    public boolean verificarDocumentoUnico(String documento){
        boolean countDoc = false;
        String sql = "SELECT COUNT(dni_cliente) > 0 AS resultado "
                    +"FROM clientes2 "
                    +"WHERE dni_cliente LIKE "+documento+";";
        try {
            conexion = cn.getConnection();
            ps = conexion.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                countDoc = rs.getBoolean("resultado");
                return countDoc;
            }
        } catch (SQLException e) {
            System.out.println(e.toString());
        }
        return countDoc;
    }
    
    public boolean verificarActDocCliente(int id, String documento){
        try {
            String sql = "SELECT dni_cliente "
                        +"FROM clientes2 "
                        +"WHERE id_cliente = ?";
            
            conexion = cn.getConnection();
            ps = conexion.prepareStatement(sql);
           
            ps.setInt(1, id);
            rs = ps.executeQuery();
            rs.next();
            
            String documentoB = rs.getString("dni_cliente"); //documento actual, en base a la busqueda sql
            
            if(documentoB.equals(documento)){ //en doc obtenido por consulta con el id y el que esta en el txt son iguales
                return false;
            } else{
                sql = "SELECT COUNT(*) AS countDocumento " 
                    +"FROM clientes2 WHERE dni_cliente = ?";
                
                ps = conexion.prepareStatement(sql);
                ps.setString(1, documento); //documento que esta en el txt
                
                rs = ps.executeQuery();
                rs.next();
                int countDoc = rs.getInt("countDocumento");
                
                return (countDoc>=1);
            }
        }catch (SQLException e) {
            System.out.println(e.toString());
            return false;
        }
    }

    public List listarClientes() {
        List<Cliente> listCli = new ArrayList();
        String sql = "SELECT * "
                + "FROM clientes2";
        try {
            conexion = cn.getConnection();
            ps = conexion.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Cliente cli = new Cliente();
                cli.setId_cliente(rs.getInt("id_cliente"));
                cli.setDni(rs.getString("dni_cliente"));
                cli.setNombre(rs.getString("nombre_cliente"));
                cli.setApellido(rs.getString("apellido_cliente"));
                cli.setCelular(rs.getLong("celular_cliente"));
                cli.setInstagram(rs.getString("instagram_cliente"));

                listCli.add(cli);
            }
        } catch (SQLException e) {
            System.out.println(e.toString());
        } finally {
            try {
                conexion.close();
            } catch (SQLException e) {
                System.out.println(e.toString());
            }
        }
        return listCli;
    }
    
    public boolean eliminarCliente(int id_cliente){
        String sql = "DELETE "
                    + "FROM clientes2 "
                    + "WHERE id_cliente = ?";
        try {
            conexion = cn.getConnection();
            ps = conexion.prepareStatement(sql);
            ps.setInt(1, id_cliente);
            ps.execute();
            return true;
        } catch (SQLException e) {
            return  false;
        } finally{
            try {
                conexion.close();
            } catch (SQLException ex) {
                System.out.println(ex.toString());
            }
        }
    }
    
    public boolean modificarCliente(Cliente cli) {
        String sql = "UPDATE clientes2 "
                + "SET dni_cliente=?, nombre_cliente=?, "
                + "apellido_cliente=?, celular_cliente=?, instagram_cliente=? WHERE id_cliente=?";
        try {
            conexion = cn.getConnection();
            ps = conexion.prepareStatement(sql);
            ps.setString(1, cli.getDni());
            ps.setString(2, cli.getNombre());
            ps.setString(3, cli.getApellido());
            ps.setLong(4, cli.getCelular());
            ps.setString(5, cli.getInstagram());
            ps.setInt(6, cli.getId_cliente());

            ps.execute();
            return true;
        } catch (SQLException e) {
            System.out.println(e.toString());
            return false;
        } finally {
            try {
                conexion.close();
            } catch (SQLException ex) {
                System.out.println(ex.toString());
            }
        }
    }
}
