
package Modelo;

public class Empleado {
    private int id_empleado;
    private String nombre_empleado;
    private String ape_paterno_empleado;
    private String ape_materno_empleado;
    private int documento_empleado;
    private int celular_empleado;
    private String fec_naciminto_empleado;
    private String fec_ingreso_empleado;

    public Empleado() {
    }

    public Empleado(int id_empleado, String nombre_empleado, String ape_paterno_empleado, String ape_materno_empleado, int documento_empleado, int celular_empleado, String fec_naciminto_empleado, String fec_ingreso_empleado) {
        this.id_empleado = id_empleado;
        this.nombre_empleado = nombre_empleado;
        this.ape_paterno_empleado = ape_paterno_empleado;
        this.ape_materno_empleado = ape_materno_empleado;
        this.documento_empleado = documento_empleado;
        this.celular_empleado = celular_empleado;
        this.fec_naciminto_empleado = fec_naciminto_empleado;
        this.fec_ingreso_empleado = fec_ingreso_empleado;
    }

    public int getId_empleado() {
        return id_empleado;
    }

    public void setId_empleado(int id_empleado) {
        this.id_empleado = id_empleado;
    }

    public String getNombre_empleado() {
        return nombre_empleado;
    }

    public void setNombre_empleado(String nombre_empleado) {
        this.nombre_empleado = nombre_empleado;
    }

    public String getApe_paterno_empleado() {
        return ape_paterno_empleado;
    }

    public void setApe_paterno_empleado(String ape_paterno_empleado) {
        this.ape_paterno_empleado = ape_paterno_empleado;
    }

    public String getApe_materno_empleado() {
        return ape_materno_empleado;
    }

    public void setApe_materno_empleado(String ape_materno_empleado) {
        this.ape_materno_empleado = ape_materno_empleado;
    }

    public int getDocumento_empleado() {
        return documento_empleado;
    }

    public void setDocumento_empleado(int documento_empleado) {
        this.documento_empleado = documento_empleado;
    }

    public int getCelular_empleado() {
        return celular_empleado;
    }

    public void setCelular_empleado(int celular_empleado) {
        this.celular_empleado = celular_empleado;
    }

    public String getFec_naciminto_empleado() {
        return fec_naciminto_empleado;
    }

    public void setFec_naciminto_empleado(String fec_naciminto_empleado) {
        this.fec_naciminto_empleado = fec_naciminto_empleado;
    }

    public String getFec_ingreso_empleado() {
        return fec_ingreso_empleado;
    }

    public void setFec_ingreso_empleado(String fec_ingreso_empleado) {
        this.fec_ingreso_empleado = fec_ingreso_empleado;
    }
    
}


