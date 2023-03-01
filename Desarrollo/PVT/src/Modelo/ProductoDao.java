
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
                pro.setFoto_producto(rs.getBytes("foto_producto"));
                
                listPro.add(pro);
            }
        } catch (SQLException e) {
            System.out.println(e.toString());
        }
        return listPro;
    }
    
//    public List listarProductosPorAtributo(String atributo){
//        List<Producto> listPro = new ArrayList();
//        String sql = "SELECT * "
//                    + "FROM productos ORDER BY " + atributo;
//        try {
//            conexion = cn.getConnection();
//            ps = conexion.prepareStatement(sql);
//            rs = ps.executeQuery();
//            while (rs.next()) {
//                Producto pro = new Producto();
//                pro.setId_producto(rs.getInt("id_producto"));
//                pro.setNombre_producto(rs.getString("nombre_producto"));
//                pro.setCategoria_producto(rs.getString("categoria_producto"));
//                pro.setDescripcion_producto(rs.getString("descripcion_producto"));
//                pro.setCosto_producto(rs.getFloat("costo_producto"));
//                pro.setVenta_producto(rs.getFloat("venta_producto"));
//                pro.setStock_producto(rs.getInt("stock_producto"));
//                pro.setCodigo_producto(rs.getString("codigo_producto"));
//                pro.setFoto_producto(rs.getBytes("foto_producto"));
//                
//                listPro.add(pro);
//            }
//        } catch (SQLException e) {
//            System.out.println(e.toString());
//        }
//        return listPro;
//    }
        
    
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
        
        try {
            String sql = "SELECT * FROM productos WHERE id_producto = ?";
            ps = conexion.prepareStatement(sql);
            ps.setInt(1, pro.getId_producto());
            rs = ps.executeQuery();
            rs.next();
            Producto proActual = new Producto(
                    rs.getInt("id_producto"), rs.getString("nombre_producto"), 
                    rs.getString("categoria_producto"), rs.getString("descripcion_producto"),
                    rs.getFloat("costo_producto"), rs.getFloat("venta_producto"),
                    rs.getInt("stock_producto"), rs.getString("codigo_producto"),
                    rs.getBytes("foto_producto"));
            
            sql = "SELECT COUNT(IF(codigo_producto = ? , 1 ,null)) AS countCod, COUNT(IF(nombre_producto = ? , 1 ,null)) AS countNombre from productos";
            
            ps = conexion.prepareStatement(sql);
            ps.setString(1, pro.getCodigo_producto() );
            ps.setString(2, pro.getNombre_producto() );
            
            rs = ps.executeQuery();
            rs.next();
            int countCod = rs.getInt("countCod");
            int countNombre = rs.getInt("countNombre");
            
            if(!proActual.getCodigo_producto().equals(pro.getCodigo_producto())){
                if(countCod>0){
                    JOptionPane.showMessageDialog(null, "Codigo de producto ya existe");
                    return false;
                }
            }
            
            if(!proActual.getNombre_producto().equals(pro.getNombre_producto())){
                if(countNombre>0){
                    JOptionPane.showMessageDialog(null, "Nombre de producto ya existe");
                    return false;
                }
            }          
            
            sql = "UPDATE productos "
                    + "SET nombre_producto=?,categoria_producto=?,codigo_producto=?,"
                    + "descripcion_producto=?,"
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
            return true;
        } catch (SQLException e) {
            System.out.println(e.toString());
            return false;
        } 
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
    
    
//    public Producto buscarProducto(int id) throws SQLException {
//        String sql = "Select * "
//                    + "FROM productos "
//                    + "WHERE id_producto = ?";
//        ps = conexion.prepareStatement(sql); 
//        ps.setInt(1, id);        
//        
//        rs = ps.executeQuery();
//        Producto producto = null;
//        if (rs.next()) {
//            producto = new Producto();
//            producto.setId_producto(rs.getInt("id_producto"));
//            producto.setNombre_producto(rs.getString("nombre_producto"));
//            producto.setCategoria_producto(rs.getString("categoria_producto"));
//            producto.setDescripcion_producto(rs.getString("descripcion_producto"));
//            producto.setCosto_producto(rs.getFloat("costo_producto"));
//            producto.setVenta_producto(rs.getFloat("venta_producto"));
//            producto.setStock_producto(rs.getInt("stock_producto"));
//            producto.setCodigo_producto(rs.getString("codigo_producto"));
//            producto.setFoto_producto(rs.getBytes("foto_producto"));
//            
//        }              
//        return producto;
//    }  
    
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
    
    
}
