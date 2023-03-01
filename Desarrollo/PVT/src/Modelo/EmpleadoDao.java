
package Modelo;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class EmpleadoDao {
    Connection conexion;
    PreparedStatement ps;
    ResultSet rs;
    Conexion cn = new Conexion();
    
    public boolean registrarEmpleado(Empleado emp){
        String sql = "INSERT INTO empleados (nombre_empleado,ape_paterno_empleado,ape_materno_empleado,"
                + "documento_empleado,celular_empleado,"
                + "fec_naciminto_empleado,fec_ingreso_empleado)"
                + "VALUES (?,?,?,?,?,?,?)";
        try {
            conexion = cn.getConnection();
            ps = conexion.prepareStatement(sql);
            ps.setString(1, emp.getNombre_empleado());
            ps.setString(2, emp.getApe_paterno_empleado());
            ps.setString(3, emp.getApe_materno_empleado());
            ps.setInt(4, emp.getDocumento_empleado());
            ps.setInt(5, emp.getCelular_empleado());
            ps.setString(6, emp.getFec_naciminto_empleado());
            ps.setString(7, emp.getFec_ingreso_empleado());
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
    
    public boolean eliminarEmpleado(int id_empleado){
        String sql = "DELETE "
                    + "FROM empleados "
                    + "WHERE id_empleado = ?";
        try {
            ps = conexion.prepareStatement(sql);
            ps.setInt(1, id_empleado);
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
    
    public boolean modificarEmpleado(Empleado emp){
        String sql = "UPDATE empleados "
                    + "SET nombre_empleado=?,ape_paterno_empleado=?,ape_materno_empleado=?,"
                    + "documento_empleado=?,celular_empleado=?,"
                    + "fec_naciminto_empleado=?,fec_ingreso_empleado=? "
                    + "WHERE id_empleado=?";
        try {
            ps = conexion.prepareStatement(sql);
            ps.setString(1, emp.getNombre_empleado());
            ps.setString(2, emp.getApe_paterno_empleado());
            ps.setString(3, emp.getApe_materno_empleado());
            ps.setInt(4, emp.getDocumento_empleado());
            ps.setInt(5, emp.getCelular_empleado());
            ps.setString(6, emp.getFec_naciminto_empleado());
            ps.setString(7, emp.getFec_ingreso_empleado());
            ps.setInt(8, emp.getId_empleado());
            
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
    
    public List listarEmpleadosSimple(){
        List<Empleado> listEmpSimple = new ArrayList();
        String sql = "SELECT nombre_empleado,ape_paterno_empleado "
                    + "FROM empleados";
        try {
            conexion = cn.getConnection();
            ps = conexion.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Empleado emp = new Empleado();
                emp.setNombre_empleado(rs.getString("nombre_empleado"));
                emp.setApe_paterno_empleado(rs.getString("ape_paterno_empleado"));
                
                listEmpSimple.add(emp);
            }
        } catch (SQLException e) {
            System.out.println(e.toString());
        }
        return listEmpSimple;
    }
    
    public boolean verificarDocumentoUnico(String documento){
        int countDoc;
        String sql = "SELECT COUNT(IF(documento_empleado = ? , 1 ,null)) AS countCod from empleados";


        try {
            conexion = cn.getConnection();
            ps = conexion.prepareStatement(sql);
            ps.setString(1, documento );
            rs = ps.executeQuery();
            if (rs.next()) {
                countDoc = rs.getInt("countCod");
                return false;
            }
        } catch (SQLException e) {
            System.out.println(e.toString());
        }
        return true;
    }
    
    public boolean verificarActDocEmpleado(int id, String documento){
        try {
            String sql = "SELECT documento_empleado "
                        +"FROM empleados "
                        +"WHERE id_empleado = ?";
            ps = conexion.prepareStatement(sql);
           
            ps.setInt(1, id);
            rs = ps.executeQuery();
            rs.next();
            
            String documentoB = rs.getString("documento_empleado");
            
            sql = "SELECT "
                        +"COUNT(IF(documento_empleado = ? , 1 ,null)) AS countDocumento "
                    +"from empleados";
            
            ps = conexion.prepareStatement(sql);
            ps.setString(1, documento);
            
            rs = ps.executeQuery();
            rs.next();
            int countDoc = rs.getInt("countDocumento");
            
            if(documentoB.equals(documento)){
                if(countDoc>0){
                    return false;
                }
            }

            return true; 
        }catch (SQLException e) {
            System.out.println(e.toString());
            return false;
        }
    }
}
