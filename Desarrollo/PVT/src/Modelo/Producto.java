
package Modelo;

public class Producto {
    private int id_producto;
    private String nombre_producto;
    private String categoria_producto;
    private String descripcion_producto;
    private float costo_producto;
    private float venta_producto;
    private int stock_producto;
    private String codigo_producto;
    private String foto_producto;

    public Producto() {
    }

    public Producto(int id_producto, String nombre_producto, String categoria_producto, String descripcion_producto, float costo_producto, float venta_producto, int stock_producto, String codigo_producto, String foto_producto) {
        this.id_producto = id_producto;
        this.nombre_producto = nombre_producto;
        this.categoria_producto = categoria_producto;
        this.descripcion_producto = descripcion_producto;
        this.costo_producto = costo_producto;
        this.venta_producto = venta_producto;
        this.stock_producto = stock_producto;
        this.codigo_producto = codigo_producto;
        this.foto_producto = foto_producto;
    }

    public int getId_producto() {
        return id_producto;
    }

    public void setId_producto(int id_producto) {
        this.id_producto = id_producto;
    }

    public String getNombre_producto() {
        return nombre_producto;
    }

    public void setNombre_producto(String nombre_producto) {
        this.nombre_producto = nombre_producto;
    }

    public String getCategoria_producto() {
        return categoria_producto;
    }

    public void setCategoria_producto(String categoria_producto) {
        this.categoria_producto = categoria_producto;
    }

    public String getDescripcion_producto() {
        return descripcion_producto;
    }

    public void setDescripcion_producto(String descripcion_producto) {
        this.descripcion_producto = descripcion_producto;
    }

    public float getCosto_producto() {
        return costo_producto;
    }

    public void setCosto_producto(float costo_producto) {
        this.costo_producto = costo_producto;
    }

    public float getVenta_producto() {
        return venta_producto;
    }

    public void setVenta_producto(float venta_producto) {
        this.venta_producto = venta_producto;
    }

    public int getStock_producto() {
        return stock_producto;
    }

    public void setStock_producto(int stock_producto) {
        this.stock_producto = stock_producto;
    }

    public String getCodigo_producto() {
        return codigo_producto;
    }

    public void setCodigo_producto(String codigo_producto) {
        this.codigo_producto = codigo_producto;
    }

    public String getFoto_producto() {
        return foto_producto;
    }

    public void setFoto_producto(String foto_producto) {
        this.foto_producto = foto_producto;
    }
    
    
}
