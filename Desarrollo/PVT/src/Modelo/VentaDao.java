
package Modelo;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;

/**
 *
 * @author sebas
 */
public class VentaDao {
    Connection conexion;
    Conexion cn = new Conexion();
    PreparedStatement ps;
    ResultSet rs;
        
    public int IdVenta(){
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
        } catch (Exception e) {
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
        } catch (Exception e) {
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
    
    /*
    public List Listarventas(){
         List<Venta> ListaVenta = new ArrayList();
        String sql = "SELECT * FROM ventas";
        try{
            conexion=cn.getConnection();
            ps=conexion.prepareStatement(sql);
            rs=ps.executeQuery();
            while(rs.next()){
                Venta vent=new Venta();
                vent.setId(rs.getInt("id_venta"));
                vent.setFecha(rs.getDate("fecha"));
                vent.setDni(rs.getInt("dni_venta"));
                vent.setNombre(rs.getString("nombre_cliente"));
                vent.setEmpleado(rs.getString("nombre_empleado"));
                vent.setDescripcion(rs.getString("descripcion_venta"));
                vent.setTotal(rs.getFloat("monto_total"));
                ListaVenta.add(vent);
            }
        }catch(SQLException e){
            System.out.println(e.toString());   
        }
        return ListaVenta;
    }*/
    
    /*
    public int RegistrarDetalle(Detalle Dv){
        String sql= "INSERT INTO detalle (cod_pro,cantidad,precio,id_venta) VALUES (?,?,?,?)";
        try{
            conexion=cn.getConnection();
            ps=conexion.prepareStatement(sql);
            ps.setString(1,Dv.getCod_pro());
            ps.setInt(2, Dv.getCantidad());
            ps.setDouble(3, Dv.getPrecio());
            ps.setInt(4, Dv.getId());
            ps.execute();
        }catch(SQLException e){
            System.out.println(e.toString());
        }finally{
            try{
                conexion.close();
            }catch(SQLException e){
                System.out.println(e.toString());
                
            }
        
    }
        return r;
    }*/
    
    public boolean ActualizarStock(int cant,String cod){
        String sql="UPDATE productos SET stock =? WHERE codigo=?"; 
        try{
            conexion=cn.getConnection();
            ps=conexion.prepareStatement(sql);
            ps.setInt(1,cant);
            ps.setString(2, cod);
            ps.execute();
            return true;
        }catch(Exception e){
            System.out.println(e.toString());
            return false;
        }
    }
    
      
      
      public List listarEmpleados(){
        List<Empleado> listEmp = new ArrayList();
        String sql = "SELECT * "
                    + "FROM empleados";
        try {
            conexion = cn.getConnection();
            ps = conexion.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Empleado emp = new Empleado();
                emp.setId_empleado(rs.getInt("id_empleado"));
                emp.setNombre_empleado(rs.getString("nombre_empleado"));
                emp.setApe_paterno_empleado(rs.getString("ape_paterno_empleado"));
                emp.setApe_materno_empleado(rs.getString("ape_materno_empleado"));
                emp.setDocumento_empleado(rs.getInt("documento_empleado"));
                emp.setCelular_empleado(rs.getInt("celular_empleado"));
                emp.setFec_naciminto_empleado(rs.getString("fec_naciminto_empleado"));
                emp.setFec_ingreso_empleado(rs.getString("fec_ingreso_empleado"));
                
                listEmp.add(emp);
            }
        } catch (SQLException e) {
            System.out.println(e.toString());
        }
        return listEmp;
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
    
}
    
   


