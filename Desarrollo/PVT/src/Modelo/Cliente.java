
package Modelo;

public class Cliente {
    
    private int id_cliente;
    private long dni;
    private String nombre;
    
    public Cliente() {}

    public Cliente(int id_cliente, int dni, String nombre) {
        this.id_cliente = id_cliente;
        this.dni = dni;
        this.nombre = nombre;
    }

    public int getId_cliente() {
        return id_cliente;
    }

    public void setId_cliente(int id_cliente) {
        this.id_cliente = id_cliente;
    }

    public long getDni() {
        return dni;
    }

    public void setDni(long dni) {
        this.dni = dni;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
