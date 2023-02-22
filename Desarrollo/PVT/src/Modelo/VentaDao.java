
package Modelo;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class VentaDao {
    
    private static Connection conexion;
    private static final Conexion cn = new Conexion();
    private static PreparedStatement ps;
    private static ResultSet rs;
    
    //Mostrar consulta con la FECHA
    public List listarReportes(String fecha) throws ParseException{
        List<Venta> listRepo = new ArrayList();
        
        String consulta = "SELECT * FROM ventas WHERE fecha = '"+fecha+"'"
                + "ORDER BY monto_total ASC";    
        
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
        }finally{
            try {
                conexion.close();
            } catch (SQLException ex) {
                System.out.println(ex.toString());
            }
        }
        
        return listRepo;
    }
    
}
