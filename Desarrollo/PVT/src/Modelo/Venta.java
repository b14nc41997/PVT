
package Modelo;

import java.util.Date;

public class Venta {
        
    private int id;
    private Date fecha;
    private int dni;
    private String nombre;
    private String empleado;
    private String descripcion;
    private float total;
    
    public Venta(){
    }

    public Venta(int id, Date fecha, int dni, String nombre, String empleado, String descripcion, float total) {        
        this.id = id;
        this.fecha = fecha;
        this.dni = dni;
        this.nombre = nombre;
        this.empleado = empleado;
        this.descripcion = descripcion;
        this.total = total;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public int getDni() {
        return dni;
    }

    public void setDni(int dni) {
        this.dni = dni;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmpleado() {
        return empleado;
    }

    public void setEmpleado(String empleado) {
        this.empleado = empleado;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }
    
}
