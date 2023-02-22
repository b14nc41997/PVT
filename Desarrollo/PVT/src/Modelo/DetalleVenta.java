
package Modelo;

public class DetalleVenta {
    
    //private int id;
    //private String id_producto;
    private String nombre;
    private int cantidad;
    private float precio;    
    
    public DetalleVenta(){}

    public DetalleVenta(String nombre, int cantidad, float precio) {
        this.nombre = nombre;
        this.cantidad = cantidad;
        this.precio = precio;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public float getPrecio() {
        return precio;
    }

    public void setPrecio(float precio) {
        this.precio = precio;
    }

    
    
}
