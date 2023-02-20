
package Modelo;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class VentaDao {
    
    private static Connection conexion;
    private static final Conexion cn = new Conexion();
    private static PreparedStatement ps;
    private static ResultSet rs;
    
    
    public int idVenta(){
        int id = 0;
        String sql = "SELECT MAX(id_venta) FROM ventas";
        try{
            conexion = cn.getConnection();
            ps = conexion.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                id = rs.getInt(1);
            }
        }catch(SQLException e){
            System.out.println(e.toString());
        }
        return id;
    }
    
    private String fechaActual(){
        String fecha = String.valueOf(LocalDate.now());
        return fecha;
    }
    
    public boolean RegistrarVenta(Venta v) {
        String sql = "INSERT INTO ventas (fecha, dni_cliente, nombre_cliente, "
                + "nombre_empleado, descripcion_venta, "
                + "monto_total) VALUES (?,?,?,?,?,?)";
        try {
            conexion = cn.getConnection();
            ps = conexion.prepareStatement(sql);
            ps.setString(1, fechaActual());
            ps.setInt(2, v.getDni());
            ps.setString(3, v.getNombre());
            ps.setString(4, v.getEmpleado());
            ps.setString(5, v.getDescripcion());
            ps.setDouble(6, v.getTotal());
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
    
    public boolean RegistrarDetalleVenta(DetalleVenta dv) {
        String sql = "INSERT INTO detalle_venta (id_producto, cantidad, precio,"
                + "id_venta) VALUES (?,?,?,?)";
        try {
            conexion = cn.getConnection();
            ps = conexion.prepareStatement(sql);
            ps.setString(1, dv.getId_producto());
            ps.setInt(2, dv.getCantidad());
            ps.setFloat(3, dv.getPrecio());
            ps.setInt(4, dv.getId_venta());            
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
    
    public boolean ActualizarStock(int cant,String cod){
        String sql="UPDATE productos SET stock_producto =? WHERE codigo_producto=?"; 
        try{
            conexion=cn.getConnection();
            ps=conexion.prepareStatement(sql);
            ps.setInt(1,cant);
            ps.setString(2, cod);
            ps.execute();
            return true;
        }catch(SQLException e){
            System.out.println(e.toString());
            return false;
        }
    }
      
    public boolean eliminarVenta(int id_venta) {
        String sql = "DELETE "
                + "FROM ventas "
                + "WHERE id_venta = ?";
        try {
            ps = conexion.prepareStatement(sql);
            ps.setInt(1, id_venta);
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
    
    //Mostrar consulta según FECHA (Vista Reporte)
    public List listarReportes(String fecha) throws ParseException{
        List<Venta> listRepo = new ArrayList();
        
        String consulta = "SELECT * FROM ventas WHERE fecha = '"+fecha+"'";    
        
        try {
            conexion = cn.getConnection();
            ps = conexion.prepareStatement(consulta);            
            rs = ps.executeQuery();
            while (rs.next()) {
                Venta rep = new Venta();                
                rep.setId(rs.getInt("id_venta"));  
                rep.setFecha(Date.valueOf(rs.getString("fecha")));                
                rep.setDni(rs.getInt("dni_cliente"));
                rep.setNombre(rs.getString("nombre_cliente"));
                rep.setEmpleado(rs.getString("nombre_empleado"));
                rep.setDescripcion(rs.getString("descripcion_venta"));
                rep.setTotal(rs.getFloat("monto_total"));               
                
                listRepo.add(rep); 
            }
        } catch (SQLException e) {
            System.out.println(e.toString());
        }
        
        return listRepo;
    }
    
}
