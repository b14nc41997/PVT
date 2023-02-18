
package Modelo;

public class DetalleVenta {
    
    private int id;
    private String id_producto;
    private int cantidad;
    private float precio;
    private int id_venta;
    
    public DetalleVenta(){}

    public DetalleVenta(int id, String id_producto, int cantidad, float precio, int id_venta) {
        this.id = id;
        this.id_producto = id_producto;
        this.cantidad = cantidad;
        this.precio = precio;
        this.id_venta = id_venta;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getId_producto() {
        return id_producto;
    }

    public void setId_producto(String id_producto) {
        this.id_producto = id_producto;
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

    public int getId_venta() {
        return id_venta;
    }

    public void setId_venta(int id_venta) {
        this.id_venta = id_venta;
    }
    
    
    
}
