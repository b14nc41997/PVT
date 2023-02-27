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

    public String clienteEscogido(long dni) {
        String sql = "SELECT * "
                + "FROM clientes WHERE dni_cliente = '" + dni + "'";
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
        } finally {
            try {
                conexion.close();
            } catch (SQLException e) {
                System.out.println(e.toString());
            }
        }
        return null;
    }

    public boolean registrarCliente(Cliente cli) {
        String sql = "INSERT INTO clientes (dni_cliente, nombre_cliente)"
                + "VALUES (?,?)";
        try {
            conexion = cn.getConnection();
            ps = conexion.prepareStatement(sql);
            ps.setLong(1, cli.getDni());
            ps.setString(2, cli.getNombre());

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

    public List listarClientes() {
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
        } finally {
            try {
                conexion.close();
            } catch (SQLException e) {
                System.out.println(e.toString());
            }
        }
        return listCli;
    }

    public boolean modificarCliente(Cliente cli) {
        String sql = "UPDATE clientes "
                + "SET dni_cliente=?, nombre_cliente=?"
                + "WHERE id_cliente=?";
        try {
            ps = conexion.prepareStatement(sql);
            ps.setLong(1, cli.getDni());
            ps.setString(2, cli.getNombre());
            ps.setInt(3, cli.getId_cliente());

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
