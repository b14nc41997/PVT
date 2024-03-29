
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
            ps.setBytes(8, pro.getFoto_producto());
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
        categoria.addItem("Joya");
        categoria.addItem("Tatuaje");
        categoria.addItem("Otro"); //nuevo
    }
    
    public List listarProductos(){
        List<Producto> listPro = new ArrayList();
        String sql = "SELECT * "
                    + "FROM productos ORDER BY stock_producto";
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
                pro.setFoto_producto(rs.getBytes("foto_producto"));
                
                listPro.add(pro);
            }
        } catch (SQLException e) {
            System.out.println(e.toString());
        }
        return listPro;
    }
    
    public int obtenerStockProducto(String codigo){
        String sql = "SELECT stock_producto "
                    + "FROM productos "
                    + "WHERE codigo_producto = '"+codigo+"'";
        try {
            conexion = cn.getConnection();
            ps = conexion.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                int stock = rs.getInt("stock_producto");
                return stock;
            }
        } catch (SQLException e) {
            System.out.println(e.toString());
        }
        return 0;
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
    
    public boolean verificarCodNOmProducto(int id, String nombre,String codigo){
        try {
            String sql = "SELECT nombre_producto, codigo_producto "
                        +"FROM productos "
                        +"WHERE id_producto = ?";
            ps = conexion.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            rs.next();
            
            String nombreB = rs.getString("nombre_producto");
            String codigoB = rs.getString("codigo_producto");
            
            sql = "SELECT "
                        +"COUNT(IF(codigo_producto = ? , 1 ,null)) AS countCod, "
                        +"COUNT(IF(nombre_producto = ? , 1 ,null)) AS countNombre "
                    +"from productos";
            
            ps = conexion.prepareStatement(sql);
            ps.setString(1, codigo);
            ps.setString(2, nombre);
            
            rs = ps.executeQuery();
            rs.next();
            int countCod = rs.getInt("countCod");
            int countNombre = rs.getInt("countNombre");
            
            if(!codigoB.equals(codigo)){
                if(countCod>0){
                    JOptionPane.showMessageDialog(null, "El código del producto ya existe.");
                    return false;
                }
            }
            
            if(!nombreB.equals(nombre)){
                if(countNombre>0){
                    JOptionPane.showMessageDialog(null, "El nombre del producto ya existe");
                    return false;
                }
            }
            return true; 
        }catch (SQLException e) {
            System.out.println(e.toString());
            return false;
        }
    }

    public void modificarProducto(Producto pro){
        
        try {
            String sql = "UPDATE productos "
                + "SET nombre_producto=?,categoria_producto=?,"
                    + "codigo_producto=?,descripcion_producto=?,"
                    + "costo_producto=?,venta_producto=?,"
                    + "stock_producto=? "
                + "WHERE id_producto=?";
            
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
        } catch (SQLException e) {
            System.out.println(e.toString());
        } 
    }
    
    public List filtrarProductos(String atributo, String valor){
        List<Producto> listPro = new ArrayList();
        String sql = "SELECT * "
                    + "FROM productos WHERE " + atributo + " like ? ORDER BY " + atributo;
        try {            
            conexion = cn.getConnection();
            ps = conexion.prepareStatement(sql);
            ps.setString(1, "%" + valor + "%");  
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
                pro.setFoto_producto(rs.getBytes("foto_producto"));
                
                listPro.add(pro);
            }
        } catch (SQLException e) {
            System.out.println(e.toString());
        }
        return listPro;
    }
    
    public Producto productoEscogido(String codigo, String categoria){        
        String sql = "SELECT * "
                    + "FROM productos "
                    + "WHERE categoria_producto = '"+categoria+"' AND codigo_producto = '"+codigo+"'";
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
                pro.setFoto_producto(rs.getBytes("foto_producto"));
                
                return pro;
            }
        } catch (SQLException e) {
            System.out.println(e.toString());
        }
        return null;
    }
    
    public byte[] getImagenProducto(String codigo){        
        String sql = "SELECT foto_producto "
                    + "FROM productos WHERE codigo_producto = '"+codigo+"'";
        byte[] bytes;
        try {
            conexion = cn.getConnection();
            ps = conexion.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                bytes = rs.getBytes("foto_producto");
                return bytes;
            }
        } catch (SQLException e) {
            System.out.println(e.toString());
        }
        return null;
    }

    public boolean verificarCodigoUnico(String codigo){
        boolean countCod = false;
        String sql = "SELECT COUNT(codigo_producto) > 0 AS resultado "
                    +"FROM productos "
                    +"WHERE codigo_producto LIKE '"+codigo+"';";
        try {
            conexion = cn.getConnection();
            ps = conexion.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                countCod = rs.getBoolean("resultado");
                return countCod;
            }
        } catch (SQLException e) {
            System.out.println(e.toString());
        }
        return countCod;
    }
    
    public boolean verificarExisteIdCodigo(String id, String codigo){
        boolean countCod = false;
        String sql = "SELECT COUNT(codigo_producto) > 0 AS resultado "
                    +"FROM productos "
                    +"WHERE codigo_producto = '"+codigo+"' "
                        + "AND id_producto = "+id;
        try {
            conexion = cn.getConnection();
            ps = conexion.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                countCod = rs.getBoolean("resultado");
                return countCod;
            }
        } catch (SQLException e) {
            System.out.println(e.toString());
        }
        return countCod;
    }
    
    public boolean modificarFotoProducto(Producto pro){
        String sql = "UPDATE productos "
                    + "SET foto_producto = ? WHERE id_producto=?";
        try{
            ps = conexion.prepareStatement(sql);
            ps.setBytes(1, pro.getFoto_producto());
            ps.setInt(2, pro.getId_producto());
            ps.execute();
            return true;            
        }catch (SQLException e) {
            System.out.println(e.toString());
            return false;
        }
        
    }
}
