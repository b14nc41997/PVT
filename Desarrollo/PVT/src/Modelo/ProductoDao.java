
package Modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;

public class ProductoDao {
    Connection conexion;
    Conexion cn = new Conexion();
    PreparedStatement ps;
    ResultSet rs;
    
    public boolean registrarProducto(Producto pro){
        String sql = "INSERT INTO productos (nombre_producto,categoria_producto,descripcion_producto,"
                + "costo_producto,venta_producto,"
                + "stock_producto,codigo_producto,foto_producto)"
                + "VALUES (?,?,?,?,?,?,?,?)";
        try {
            conexion = cn.getConnection();
            ps = conexion.prepareStatement(sql);
            ps.setString(1, pro.getNombre_producto());
            ps.setString(2, pro.getCategoria_producto());
            ps.setString(3, pro.getDescripcion_producto());
            ps.setFloat(4, pro.getCosto_producto());
            ps.setFloat(5, pro.getVenta_producto());
            ps.setInt(6, pro.getStock_producto());
            ps.setString(7, pro.getCodigo_producto());
            ps.setString(8, pro.getFoto_producto());
            ps.execute();
            return  true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.toString());
            return false;
        } finally{
            try {
                conexion.close();
            } catch (SQLException e) {
                System.out.println(e.toString());
            }
        }
    }
    
    public void seleccionarCategoriaInventario(JComboBox categoria){
        categoria.addItem("Joya");
        categoria.addItem("Otro");
    }
    
    public void seleccionarCategoriaVenta(JComboBox categoria){
        categoria.addItem("Tatuaje");
        categoria.addItem("Joya");
    }
    
    public List listarProductos(){
        List<Producto> listPro = new ArrayList();
        String sql = "SELECT * "
                    + "FROM productos";
        try {
            conexion = cn.getConnection();
            ps = conexion.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Producto pro = new Producto();
                pro.setId_producto(rs.getInt("id_producto"));
                pro.setNombre_producto(rs.getString("nombre_producto"));
                pro.setCategoria_producto(rs.getString("categoria_producto"));
                pro.setDescripcion_producto(rs.getString("descripcion_producto"));
                pro.setCosto_producto(rs.getFloat("costo_producto"));
                pro.setVenta_producto(rs.getFloat("venta_producto"));
                pro.setStock_producto(rs.getInt("stock_producto"));
                pro.setCodigo_producto(rs.getString("codigo_producto"));
                pro.setFoto_producto(rs.getString("foto_producto"));
                
                listPro.add(pro);
            }
        } catch (SQLException e) {
            System.out.println(e.toString());
        }
        return listPro;
    }
    
    public boolean eliminarProducto(int id_producto){
        String sql = "DELETE "
                    + "FROM productos "
                    + "WHERE id_producto = ?";
        try {
            ps = conexion.prepareStatement(sql);
            ps.setInt(1, id_producto);
            ps.execute();
            return true;
        } catch (SQLException e) {
            System.out.println(e.toString());
            return  false;
        } finally{
            try {
                conexion.close();
            } catch (SQLException ex) {
                System.out.println(ex.toString());
            }
        }
    }
    
    public boolean modificarProducto(Producto pro){
        String sql = "UPDATE productos "
                    + "SET nombre_producto=?,categoria_producto=?,codigo_producto=?,"
                    + "descripcion_producto=?,"
                    + "costo_producto=?,venta_producto=?,"
                    + "stock_producto=? "
                    + "WHERE id_producto=?";
        try {
            ps = conexion.prepareStatement(sql);
            ps.setString(1, pro.getNombre_producto());
            ps.setString(2, pro.getCategoria_producto());
            ps.setString(3, pro.getCodigo_producto());
            ps.setString(4, pro.getDescripcion_producto());
            ps.setFloat(5, pro.getCosto_producto());
            ps.setFloat(6, pro.getVenta_producto());
            ps.setInt(7, pro.getStock_producto());
            ps.setInt(8, pro.getId_producto());
            
            ps.execute();
            return true;
        } catch (SQLException e) {
            System.out.println(e.toString());
            return false;
        } finally{
            try {
                conexion.close();
            } catch (SQLException ex) {
                System.out.println(ex.toString());
            }
        }
    }
    
    public Producto productoEscogido(String codigo){        
        String sql = "SELECT * "
                    + "FROM productos WHERE codigo_producto = '"+codigo+"'";
        try {
            conexion = cn.getConnection();
            ps = conexion.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Producto pro = new Producto();
                pro.setId_producto(rs.getInt("id_producto"));
                pro.setNombre_producto(rs.getString("nombre_producto"));
                pro.setCategoria_producto(rs.getString("categoria_producto")); 
                pro.setDescripcion_producto(rs.getString("descripcion_producto")); 
                pro.setCosto_producto(rs.getFloat("costo_producto")); 
                pro.setVenta_producto(rs.getFloat("venta_producto")); 
                pro.setStock_producto(rs.getInt("stock_producto")); 
                pro.setCodigo_producto(rs.getString("codigo_producto")); 
                pro.setFoto_producto(rs.getString("foto_producto")); 
                
                return pro;
            }
        } catch (SQLException e) {
            System.out.println(e.toString());
        }
        return null;
    }
}
