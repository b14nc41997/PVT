
package Vista;

import Modelo.Cliente;
import Modelo.ClienteDao;
import Modelo.DetalleVenta;
import Modelo.Empleado;
import Modelo.EmpleadoDao;
import Modelo.ExportarInventario;
import Modelo.ExportarReporteExcel;
import Modelo.ExportarVentaPDF;
import Modelo.Producto;
import Modelo.ProductoDao;
import Modelo.Venta;
import Modelo.VentaDao;
import static Vista.LoginVista.user;
import java.awt.Cursor;
import java.awt.event.WindowAdapter;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.text.Normalizer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableRowSorter;
import pvt.PVT;

public class SistemaVista extends javax.swing.JFrame {
    
    Empleado emp = new Empleado();
    EmpleadoDao empDao = new EmpleadoDao();
    Producto pro = new Producto();
    ProductoDao proDao = new ProductoDao();
    Venta repo = new Venta();
    VentaDao repoDao = new VentaDao();
    Venta vent = new Venta();
    VentaDao ventDao = new VentaDao();
    Cliente cli = new Cliente();
    ClienteDao cliDao = new ClienteDao();
    DefaultTableModel modeloTablaVenta = new DefaultTableModel();
    DefaultTableModel modeloTablaInventario = new DefaultTableModel();
    DefaultTableModel modeloTablaEmpleado = new DefaultTableModel();
    DefaultTableModel modeloTablaCliente = new DefaultTableModel();
    DefaultTableModel modeloTablaReporte = new DefaultTableModel();
    DetalleVenta detalleVenta = new DetalleVenta();
    
    public SistemaVista() {
        initComponents();
        lblUserName.setText(user);
        txtIdEmpleado.setVisible(false);
        txtIdInventario.setVisible(false);
        txtIdProductoCarrito.setVisible(false);
        proDao.seleccionarCategoriaVenta(cbxCategoriaProductoCarrito);
        proDao.seleccionarCategoriaInventario(cbxCategoriaProducto);
        proDao.seleccionarCategoriaInventario(cbxCategoriaInventario);
        cargarComboEmpleados();
        
        dchFecNacimientoEmpleado.setMaxSelectableDate(new Date());
        setLocationRelativeTo(null); //centrado
        setResizable(false); //para no maximizar interfaz
        this.setTitle("PUNTO DE VENTA ARTESANOS INK TATTOO");
        spnCantidadProductoCarrito.setModel(new javax.swing.SpinnerNumberModel(1, 1, null, 1));
        modeloTablaVenta = (DefaultTableModel) tablaCarritoVenta.getModel();
        modeloTablaInventario = (DefaultTableModel) tablaInventario.getModel();
        modeloTablaEmpleado = (DefaultTableModel) tablaEmpleado.getModel();
        modeloTablaCliente = (DefaultTableModel) tablaCliente.getModel();
        modeloTablaReporte = (DefaultTableModel) tablaReporte.getModel();
    }
    
    public void enumerarItemsTabla(DefaultTableModel modelo){
        int cantidadFilas = modelo.getRowCount();
        if (cantidadFilas > 0){
            for (int i=0; i<cantidadFilas;i++){
                modelo.setValueAt(i+1, i, 0);
            }
        }
    }
    
    public void listarEmpleados(){
        List<Empleado> listEmp = empDao.listarEmpleados();
        modeloTablaEmpleado = (DefaultTableModel) tablaEmpleado.getModel();
        Object[] obj = new Object[8];
        for (int i = 0; i < listEmp.size(); i++) {
            obj[0] = listEmp.get(i).getId_empleado();
            obj[1] = listEmp.get(i).getNombre_empleado();
            obj[2] = listEmp.get(i).getApe_paterno_empleado();
            obj[3] = listEmp.get(i).getApe_materno_empleado();
            obj[4] = listEmp.get(i).getDocumento_empleado();
            obj[5] = listEmp.get(i).getCelular_empleado();
            obj[6] = listEmp.get(i).getFec_naciminto_empleado();
            obj[7] = listEmp.get(i).getFec_ingreso_empleado();
            
            modeloTablaEmpleado.addRow((obj));
        }
        tablaEmpleado.setModel(modeloTablaEmpleado);
    }
    
    public void listarClientes(){
        List<Cliente> listCli = cliDao.listarClientes();
        modeloTablaCliente = (DefaultTableModel) tablaCliente.getModel();
        Object[] obj = new Object[6];
        for (int i = 0; i < listCli.size(); i++) {
            obj[0] = listCli.get(i).getId_cliente();
            obj[1] = listCli.get(i).getDni();
            obj[2] = listCli.get(i).getNombre();
            obj[3] = listCli.get(i).getApellido();
            obj[4] = listCli.get(i).getCelular();
            obj[5] = listCli.get(i).getInstagram();
            
            modeloTablaCliente.addRow((obj));
        }
        tablaCliente.setModel(modeloTablaCliente);
        
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(modeloTablaCliente);
        tablaCliente.setRowSorter(sorter);
        sorter.setComparator(0, new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o1.compareTo(o2);
            }
        });
        
        sorter.setComparator(2, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareToIgnoreCase(o2);
            }
        });
        
        sorter.setComparator(3, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareToIgnoreCase(o2);
            }
        });
        
        sorter.setComparator(5, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareToIgnoreCase(o2);
            }
        });
    }
    
    public void listarProductos(){
        List<Producto> listPro = proDao.listarProductos();
        modeloTablaInventario = (DefaultTableModel) tablaInventario.getModel();
        
        Object[] obj = new Object[8];      
        JTableHeader header = tablaInventario.getTableHeader();
        header.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        
        for (int i = 0; i < listPro.size(); i++) {
            obj[0] = listPro.get(i).getId_producto();
            obj[1] = listPro.get(i).getCodigo_producto();
            obj[2] = listPro.get(i).getNombre_producto();
            obj[3] = listPro.get(i).getCategoria_producto();
            obj[4] = listPro.get(i).getDescripcion_producto();
            obj[5] = String.format("%.1f0", Math.round(listPro.get(i).getCosto_producto() * 10.0) / 10.0).replace(",",".");
            obj[6] = String.format("%.1f0", Math.round(listPro.get(i).getVenta_producto() * 10.0) / 10.0).replace(",",".");
            obj[7] = listPro.get(i).getStock_producto();
            modeloTablaInventario.addRow((obj));
        }
        tablaInventario.setModel(modeloTablaInventario);
        
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(modeloTablaInventario);
        tablaInventario.setRowSorter(sorter);
        sorter.setComparator(0, new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o1.compareTo(o2);
            }
        });
        
        sorter.setComparator(5, new Comparator<Float>() {
            @Override
            public int compare(Float o1, Float o2) {
                return o1.compareTo(o2);
            }
        });
         
        sorter.setComparator(6, new Comparator<Float>() {
            @Override
            public int compare(Float o1, Float o2) {
                return o1.compareTo(o2);
            }
        });
         
        sorter.setComparator(7, new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o1.compareTo(o2);
            }
        });
    }

    public void listarReportes() throws ParseException{
        
        SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
        String fecha = formato.format(dchFechaReporte.getDate());
        
        List<Venta> listRepo = repoDao.listarReportes(fecha);
        
        if (listRepo.isEmpty()){
            JOptionPane.showMessageDialog(null, "No hubieron ventas registradas en la fecha seleccionada");
            return;
        }
        
        modeloTablaReporte = (DefaultTableModel) tablaReporte.getModel();        

        //Variable donde se almacenará la suma
        float suma = 0;
        float totVent = 0;
        Object[] obj = new Object[9];
        for (int i = 0; i < listRepo.size(); i++) {            
            obj[0] = (i+1);
            obj[1] = listRepo.get(i).getId();
            obj[2] = listRepo.get(i).getFecha();
            obj[3] = listRepo.get(i).getDni();
            obj[4] = listRepo.get(i).getNombre();
            obj[5] = listRepo.get(i).getEmpleado();
            obj[6] = listRepo.get(i).getDescripcion();//.
            totVent = listRepo.get(i).getTotal();
            obj[7] = String.format("%.1f0", Math.round(totVent * 10.0) / 10.0).replace(",",".");
            //Se realiza la suma de la columna "Total"            
            suma += listRepo.get(i).getTotal();
            obj[8] = String.format("%.1f0", Math.round(suma * 10.0) / 10.0).replace(",",".");
            modeloTablaReporte.addRow((obj));
        }          
        tablaReporte.setModel(modeloTablaReporte);        
        
        String numeroFormateado = String.format("%.1f0", Math.round(suma * 10.0) / 10.0).replace(",",".");
        txtTotalReporte.setText(String.valueOf(numeroFormateado)); //Se muestra la suma
    }
    
    public void limpiarTabla(DefaultTableModel modelo){
        for (int i = 0; i < modelo.getRowCount(); i++) {
            modelo.removeRow(i);
            i = i-1;
        }
    }

    private void limpiarEmpleado(){
       
        Date fechatxt1 = null, fechatxt2 = null;
        
        txtIdEmpleado.setText("");
        txtNombreEmpleado.setText("");
        txtApePaternoEmpleado.setText("");
        txtApeMaternoEmpleado.setText("");
        txtDocumentoEmpleado.setText("");
        txtCelularEmpleado.setText("");

        dchFecNacimientoEmpleado.setDate(fechatxt1);
        dchFecIngresoEmpleado.setDate(fechatxt2);
    }
    
    private void limpiarCliente(){
        txtIdCliente.setText("");
        txtDocumentoCliente.setText("");
        txtNombreCliente.setText("");
        txtApeCliente.setText("");
        txtCelularCliente.setText("");
        txtInstagramCliente.setText("");
    }
    
    private void limpiarProducto(){
        txtNombreProducto.setText("");
        cbxCategoriaProducto.setSelectedIndex(0);
        txtDescripcionProducto.setText("");
        txtPrecioCompraProducto.setText("");
        txtPrecioVentaProducto.setText("");
        txtStockProducto.setValue(1);
        txtCodigoProducto.setText("");
        txtFotoProducto.setText("");
    }
    
    private void limpiarInventario(){
        txtIdInventario.setText("");
        txtNombreInventario.setText("");
        cbxCategoriaInventario.setSelectedIndex(0);
        txtDescripcionInventario.setText("");
        txtPrecioCostoInventario.setText("");
        txtPrecioVentaInventario.setText("");
        txtStockInventario.setValue(1);
        txtCodigoInventario.setText("");
    }
    
    private void limpiarCarrito(){
        txtCodigoProductoCarrito.setText("");
        txtNombreProductoCarrito.setText("");
        txtStockProductoCarrito.setText("");
        spnCantidadProductoCarrito.setValue(1);
        txtPrecioProductoCarrito.setText("");
        txtDescripcionProductoCarrito.setText("");
    }
    
    private void limpiarVenta(){
        txtDocumentoClienteVenta.setText("");
        txtNombreClienteVenta.setText("");
    }
    
    private byte[] getImagen(String ruta){
        File imagen = new File(ruta);
        try{
            byte[] icono = new byte[(int) imagen.length()];
            InputStream input = new FileInputStream(imagen);
            input.read(icono);
            return icono;
        }catch(Exception ex){
            return null;
        }
    }       
    
    private void llenarClientes(String numDocumento, String tipoDocumento){
        String nombre = cliDao.clienteEscogido(numDocumento);
        
        txtNombreClienteVenta.setText(cliDao.clienteEscogido(numDocumento));
        
        if (nombre == null || nombre.equals("")){
            
            JOptionPane.showMessageDialog(null, tipoDocumento+" no encontrado. \nAgregar al nuevo cliente en la sección Agregar Clientes.");
        }
    }
    
    private void cargarComboEmpleados(){
        List<Empleado> listaEmpleados = empDao.listarEmpleados();
        String nombre, apellido;
        cbxEmpleadoVenta.removeAllItems();
        for (int i = 0; i < listaEmpleados.size(); i++) {
            nombre = listaEmpleados.get(i).getNombre_empleado();
            apellido = listaEmpleados.get(i).getApe_paterno_empleado();
            cbxEmpleadoVenta.addItem(nombre+" "+apellido);
        }
    }
    
    private void registrarVenta(){
        int dni = Integer.parseInt(txtDocumentoClienteVenta.getText());
        String cliente = txtNombreClienteVenta.getText();
        String empleado = cbxEmpleadoVenta.getSelectedItem().toString();
        String descripcion = txtDescripcionProductoCarrito.getText();

        float monto = Float.valueOf(txtTotalVenta.getText().replace(",","."));

        vent.setDni(dni);
        vent.setNombre(cliente);
        vent.setEmpleado(empleado);
        vent.setDescripcion(registrarDetalle());
        vent.setTotal(monto);
        
        ventDao.RegistrarVenta(vent);
    }
    
    private String registrarDetalle(){
        String detalleF = "";
        
        for (int i = 0; i < tablaCarritoVenta.getRowCount(); i++) {
            String nombreProd = "";
            String categoria = tablaCarritoVenta.getValueAt(i, 1).toString();
            
            if(categoria.equals("Tatuaje")){
                nombreProd = categoria;
            }else{
                nombreProd = tablaCarritoVenta.getValueAt(i, 2).toString();
            }
            int cantidadProducto = Integer.parseInt(tablaCarritoVenta.getValueAt(i, 5).toString());
            Float precioU =  (float)(Math.round(Float.valueOf(tablaCarritoVenta.getValueAt(i, 6).toString()) * 10.0) / 10.0);
            String numeroFormateado = String.format("%.1f0", precioU).replace(",",".");
            
            detalleVenta.setNombre(nombreProd);
            detalleVenta.setCantidad(cantidadProducto);
            detalleVenta.setPrecio(precioU);
            
            detalleF += nombreProd+" ("+cantidadProducto+"x"+numeroFormateado+")";
           
            if (tablaCarritoVenta.getRowCount()>1 && i<= tablaCarritoVenta.getRowCount()-2) {    
                detalleF+="; ";
            }
        }
        return detalleF;
    }
    
    private void actualizarStock(){
        for (int i = 0; i < tablaCarritoVenta.getRowCount(); i++) {
            String categoria = tablaCarritoVenta.getValueAt(i, 1).toString(); 
            String cod = tablaCarritoVenta.getValueAt(i, 2).toString();
            int cant = Integer.parseInt(tablaCarritoVenta.getValueAt(i, 5).toString());
            pro = proDao.productoEscogido(cod, categoria);
            int stockActual = proDao.obtenerStockProducto(cod) - cant;
            ventDao.ActualizarStock(stockActual, cod);
        }
    }
 
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        btnSalir = new javax.swing.JButton();
        tabbedPane = new javax.swing.JTabbedPane();
        pnlVenta = new javax.swing.JPanel();
        pnlFondoVenta = new javax.swing.JPanel();
        pnlFondoTituloVenta = new javax.swing.JPanel();
        lblTituloVenta = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tablaCarritoVenta = new javax.swing.JTable();
        jPanel16 = new javax.swing.JPanel();
        lblCategoriaCarrito = new javax.swing.JLabel();
        cbxCategoriaProductoCarrito = new javax.swing.JComboBox<>();
        lblCodigoCarrito = new javax.swing.JLabel();
        txtCodigoProductoCarrito = new javax.swing.JTextField();
        jPanel13 = new javax.swing.JPanel();
        lblBotonBuscarProductoVenta = new javax.swing.JLabel();
        lblNombreCarrito = new javax.swing.JLabel();
        txtNombreProductoCarrito = new javax.swing.JTextField();
        lblStockCarrito = new javax.swing.JLabel();
        lblCantidadCarrito = new javax.swing.JLabel();
        spnCantidadProductoCarrito = new javax.swing.JSpinner();
        lblPrecioCarrito = new javax.swing.JLabel();
        jPanel18 = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        txtPrecioProductoCarrito = new javax.swing.JTextField();
        lblDescripcionCarrito = new javax.swing.JLabel();
        jScrollPane6 = new javax.swing.JScrollPane();
        txtDescripcionProductoCarrito = new javax.swing.JTextArea();
        txtStockProductoCarrito = new javax.swing.JTextField();
        btnActualizarVenta = new javax.swing.JButton();
        btnEliminarVenta = new javax.swing.JButton();
        btnLimpiarVenta = new javax.swing.JButton();
        btnAgregarItemVenta = new javax.swing.JButton();
        txtIdProductoCarrito = new javax.swing.JTextField();
        btnCancelarVenta = new javax.swing.JButton();
        btnGenerarVenta = new javax.swing.JButton();
        lblPrecioCarrito1 = new javax.swing.JLabel();
        jPanel19 = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        txtTotalVenta = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        lblNombreCarrito1 = new javax.swing.JLabel();
        cbxEmpleadoVenta = new javax.swing.JComboBox<>();
        jPanel6 = new javax.swing.JPanel();
        cbxDocumentoClienteVenta = new javax.swing.JComboBox<>();
        txtDocumentoClienteVenta = new javax.swing.JTextField();
        jPanel21 = new javax.swing.JPanel();
        lblBotonBuscarClienteVenta = new javax.swing.JLabel();
        lblNombreCarrito2 = new javax.swing.JLabel();
        txtNombreClienteVenta = new javax.swing.JTextField();
        lblCodigoCarrito3 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        pnlInventario = new javax.swing.JPanel();
        pnlFondoInventario = new javax.swing.JPanel();
        pnlFondoTituloInventario = new javax.swing.JPanel();
        lblTituloInventario = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        lblBuscarInventario = new javax.swing.JLabel();
        cbxCriterioInventario = new javax.swing.JComboBox<>();
        txtCriterioInventario = new javax.swing.JTextField();
        btnDescargarTablaInventario = new javax.swing.JButton();
        btnDescargarInventario = new javax.swing.JButton();
        jPanel8 = new javax.swing.JPanel();
        lblBotonBuscarInventario = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tablaInventario = new javax.swing.JTable();
        jPanel14 = new javax.swing.JPanel();
        txtIdInventario = new javax.swing.JTextField();
        jLabel24 = new javax.swing.JLabel();
        txtNombreInventario = new javax.swing.JTextField();
        jLabel25 = new javax.swing.JLabel();
        cbxCategoriaInventario = new javax.swing.JComboBox<>();
        jLabel26 = new javax.swing.JLabel();
        txtCodigoInventario = new javax.swing.JTextField();
        lblDescripcionInventario = new javax.swing.JLabel();
        lblPrecioCostoInventario = new javax.swing.JLabel();
        pnlSOlPrecioCostoInventario = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        txtPrecioCostoInventario = new javax.swing.JTextField();
        lblPrecioVentaInventario = new javax.swing.JLabel();
        jPanel17 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        txtPrecioVentaInventario = new javax.swing.JTextField();
        lblStockInventario = new javax.swing.JLabel();
        txtStockInventario = new javax.swing.JSpinner();
        btnEliminarInventario = new javax.swing.JButton();
        btnActualizarInventario = new javax.swing.JButton();
        lblStockInventario1 = new javax.swing.JLabel();
        pnlImagenInventario = new javax.swing.JPanel();
        lblImagenInventario = new javax.swing.JLabel();
        jScrollPane8 = new javax.swing.JScrollPane();
        txtDescripcionInventario = new javax.swing.JTextArea();
        jLabel5 = new javax.swing.JLabel();
        pnlProducto = new javax.swing.JPanel();
        pnlFondoProducto = new javax.swing.JPanel();
        pnlTituloProducto = new javax.swing.JPanel();
        lblTituloProducto = new javax.swing.JLabel();
        lblFondoTituloProducto = new javax.swing.JLabel();
        lblNombreProducto = new javax.swing.JLabel();
        txtNombreProducto = new javax.swing.JTextField();
        lblCategoriaProducto = new javax.swing.JLabel();
        cbxCategoriaProducto = new javax.swing.JComboBox<>();
        lblDescripcionProducto = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtDescripcionProducto = new javax.swing.JTextArea();
        lblPrecioCompraProducto = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        txtPrecioCompraProducto = new javax.swing.JTextField();
        lblPrecioVentaProducto = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        jLabel31 = new javax.swing.JLabel();
        txtPrecioVentaProducto = new javax.swing.JTextField();
        lblStockProducto = new javax.swing.JLabel();
        txtStockProducto = new javax.swing.JSpinner();
        lblCodigoProducto = new javax.swing.JLabel();
        txtCodigoProducto = new javax.swing.JTextField();
        lblFotoProducto = new javax.swing.JLabel();
        txtFotoProducto = new javax.swing.JTextField();
        jPanel12 = new javax.swing.JPanel();
        btnFotoProducto = new javax.swing.JLabel();
        btnGuardarProducto = new javax.swing.JButton();
        btnLimpiarProducto = new javax.swing.JButton();
        lblFondoProducto = new javax.swing.JLabel();
        pnlEmpleado = new javax.swing.JPanel();
        pnlFondoEmpleado = new javax.swing.JPanel();
        pnlTituloEmpleado = new javax.swing.JPanel();
        lblTituloEmpleado = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        lblNombreEmpleado = new javax.swing.JLabel();
        txtNombreEmpleado = new javax.swing.JTextField();
        lblApePaternoEmpleado = new javax.swing.JLabel();
        txtApePaternoEmpleado = new javax.swing.JTextField();
        lblApeMaternoEmpleado = new javax.swing.JLabel();
        txtApeMaternoEmpleado = new javax.swing.JTextField();
        lblDocumentoEmpleado = new javax.swing.JLabel();
        cbxDocumentoEmpleado = new javax.swing.JComboBox<>();
        txtDocumentoEmpleado = new javax.swing.JTextField();
        lblCelularEmpleado = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        txtCelularEmpleado = new javax.swing.JTextField();
        lblFecNacimientoEmpleado = new javax.swing.JLabel();
        dchFecNacimientoEmpleado = new com.toedter.calendar.JDateChooser();
        lblFecIngresoEmpleado = new javax.swing.JLabel();
        dchFecIngresoEmpleado = new com.toedter.calendar.JDateChooser();
        btnGuardarEmpleado = new javax.swing.JButton();
        btnActualizarEmpleado = new javax.swing.JButton();
        btnEliminarEmpleado = new javax.swing.JButton();
        btnLimpiarEmpleado = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaEmpleado = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex){
                return false;
            }
        };
        tablaEmpleado = new javax.swing.JTable();
        txtIdEmpleado = new javax.swing.JTextField();
        lblFondoEmpleado = new javax.swing.JLabel();
        pnlReporte = new javax.swing.JPanel();
        pnlFondoReporte = new javax.swing.JPanel();
        pnlTituloReporte = new javax.swing.JPanel();
        lblTituloReporte = new javax.swing.JLabel();
        lblFondoTitulo = new javax.swing.JLabel();
        btnDescargaReporte = new javax.swing.JButton();
        dchFechaReporte = new com.toedter.calendar.JDateChooser();
        jPanel7 = new javax.swing.JPanel();
        lblBotonBuscarReporte = new javax.swing.JLabel();
        lblSelFechaReporte = new javax.swing.JLabel();
        jScrollPane7 = new javax.swing.JScrollPane();
        tablaReporte = new ColorCelda();
        lblPrecioReporte = new javax.swing.JLabel();
        pnlIconSolReporte = new javax.swing.JPanel();
        lblIconSolReporte = new javax.swing.JLabel();
        txtTotalReporte = new javax.swing.JTextField();
        lblFondoReporte = new javax.swing.JLabel();
        pnlCliente = new javax.swing.JPanel();
        pnlFondoCliente = new javax.swing.JPanel();
        pnlTituloCliente = new javax.swing.JPanel();
        lblTituloCliente = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jPanel20 = new javax.swing.JPanel();
        lblNombreCliente = new javax.swing.JLabel();
        txtNombreCliente = new javax.swing.JTextField();
        lblApeCliente = new javax.swing.JLabel();
        txtApeCliente = new javax.swing.JTextField();
        lblDocumentoCliente = new javax.swing.JLabel();
        cbxDocumentoCliente = new javax.swing.JComboBox<>();
        txtDocumentoCliente = new javax.swing.JTextField();
        lblCelularCliente = new javax.swing.JLabel();
        jPanel11 = new javax.swing.JPanel();
        jLabel19 = new javax.swing.JLabel();
        txtCelularCliente = new javax.swing.JTextField();
        lblInstaCliente = new javax.swing.JLabel();
        txtInstagramCliente = new javax.swing.JTextField();
        lblBuscarCliente = new javax.swing.JLabel();
        cbxCriterioCliente = new javax.swing.JComboBox<>();
        txtCriterioCliente = new javax.swing.JTextField();
        jPanel15 = new javax.swing.JPanel();
        lblBotonBuscarCliente = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        tablaEmpleado = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex){
                return false;
            }
        };
        tablaCliente = new javax.swing.JTable();
        btnGuardarCliente = new javax.swing.JButton();
        btnActualizarCliente = new javax.swing.JButton();
        btnEliminarCliente = new javax.swing.JButton();
        btnLimpiarCliente = new javax.swing.JButton();
        lblFondoCliente = new javax.swing.JLabel();
        txtIdCliente = new javax.swing.JTextField();
        iconLogoTienda = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        iconUsuarioLogueado = new javax.swing.JLabel();
        lblUserName = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(67, 102, 129));
        jPanel1.setMinimumSize(new java.awt.Dimension(1250, 750));
        jPanel1.setPreferredSize(new java.awt.Dimension(1435, 750));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel3.setBackground(new java.awt.Color(67, 102, 129));

        btnSalir.setBackground(new java.awt.Color(18, 23, 28));
        btnSalir.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        btnSalir.setForeground(new java.awt.Color(255, 255, 255));
        btnSalir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/iconSalir.png"))); // NOI18N
        btnSalir.setText("Salir");
        btnSalir.setBorder(null);
        btnSalir.setBorderPainted(false);
        btnSalir.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        btnSalir.setDefaultCapable(false);
        btnSalir.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnSalirMouseClicked(evt);
            }
        });
        btnSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalirActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnSalir, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnSalir, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel1.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(1110, 10, 130, 70));

        tabbedPane.setBackground(new java.awt.Color(67, 102, 129));
        tabbedPane.setTabPlacement(javax.swing.JTabbedPane.LEFT);
        tabbedPane.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        tabbedPane.setName(""); // NOI18N
        tabbedPane.setOpaque(true);
        tabbedPane.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                tabbedPaneStateChanged(evt);
            }
        });

        pnlVenta.setBackground(new java.awt.Color(33, 50, 60));
        pnlVenta.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        pnlFondoVenta.setBackground(new java.awt.Color(18, 23, 28));
        pnlFondoVenta.setOpaque(false);
        pnlFondoVenta.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        pnlFondoTituloVenta.setBackground(new java.awt.Color(67, 102, 129));
        pnlFondoTituloVenta.setOpaque(false);
        pnlFondoTituloVenta.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblTituloVenta.setBackground(new java.awt.Color(255, 255, 153));
        lblTituloVenta.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        lblTituloVenta.setForeground(new java.awt.Color(255, 255, 255));
        lblTituloVenta.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTituloVenta.setText("Nueva Venta");
        pnlFondoTituloVenta.add(lblTituloVenta, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, 160, 43));

        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/fondoTitulos.png"))); // NOI18N
        pnlFondoTituloVenta.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 180, -1));

        pnlFondoVenta.add(pnlFondoTituloVenta, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 11, -1, -1));

        tablaCarritoVenta = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex){
                return false;
            }
        };
        tablaCarritoVenta.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "#", "Categoría", "Código", "Producto", "Descripción", "Cantidad", "Precio Unitario", "Total"
            }
        ));
        tablaCarritoVenta.getTableHeader().setReorderingAllowed(false);
        tablaCarritoVenta.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablaCarritoVentaMouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(tablaCarritoVenta);

        pnlFondoVenta.add(jScrollPane4, new org.netbeans.lib.awtextra.AbsoluteConstraints(31, 394, 813, 119));

        jPanel16.setBackground(new java.awt.Color(18, 23, 28));
        jPanel16.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)), "Carrito", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Tahoma", 0, 12), new java.awt.Color(153, 153, 153))); // NOI18N
        jPanel16.setOpaque(false);

        lblCategoriaCarrito.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblCategoriaCarrito.setForeground(new java.awt.Color(255, 255, 255));
        lblCategoriaCarrito.setText("Categoría");

        cbxCategoriaProductoCarrito.setToolTipText("");
        cbxCategoriaProductoCarrito.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbxCategoriaProductoCarritoItemStateChanged(evt);
            }
        });

        lblCodigoCarrito.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblCodigoCarrito.setForeground(new java.awt.Color(255, 255, 255));
        lblCodigoCarrito.setText("Código");

        txtCodigoProductoCarrito.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtCodigoProductoCarrito.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 5));
        txtCodigoProductoCarrito.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtCodigoProductoCarritoKeyTyped(evt);
            }
        });

        jPanel13.setBackground(new java.awt.Color(67, 102, 129));
        jPanel13.setAlignmentX(0.0F);

        lblBotonBuscarProductoVenta.setBackground(new java.awt.Color(67, 102, 129));
        lblBotonBuscarProductoVenta.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblBotonBuscarProductoVenta.setForeground(new java.awt.Color(255, 255, 255));
        lblBotonBuscarProductoVenta.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblBotonBuscarProductoVenta.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/iconBuscar.png"))); // NOI18N
        lblBotonBuscarProductoVenta.setToolTipText("");
        lblBotonBuscarProductoVenta.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        lblBotonBuscarProductoVenta.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblBotonBuscarProductoVentaMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel13Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(lblBotonBuscarProductoVenta))
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel13Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(lblBotonBuscarProductoVenta))
        );

        lblNombreCarrito.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblNombreCarrito.setForeground(new java.awt.Color(255, 255, 255));
        lblNombreCarrito.setText("Nombre");

        txtNombreProductoCarrito.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 5));
        txtNombreProductoCarrito.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        lblStockCarrito.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblStockCarrito.setForeground(new java.awt.Color(255, 255, 255));
        lblStockCarrito.setText("Stock");

        lblCantidadCarrito.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblCantidadCarrito.setForeground(new java.awt.Color(255, 255, 255));
        lblCantidadCarrito.setText("Cantidad");

        spnCantidadProductoCarrito.setModel(new javax.swing.SpinnerNumberModel(1, 0, null, 1));
        spnCantidadProductoCarrito.setAutoscrolls(true);
        spnCantidadProductoCarrito.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        spnCantidadProductoCarrito.setEditor(new javax.swing.JSpinner.NumberEditor(spnCantidadProductoCarrito, ""));
        spnCantidadProductoCarrito.setFocusable(false);
        spnCantidadProductoCarrito.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                spnCantidadProductoCarritoKeyTyped(evt);
            }
        });

        lblPrecioCarrito.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblPrecioCarrito.setForeground(new java.awt.Color(255, 255, 255));
        lblPrecioCarrito.setText("Precio unitario");

        jPanel18.setBackground(new java.awt.Color(67, 102, 129));

        jLabel16.setBackground(new java.awt.Color(67, 102, 129));
        jLabel16.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(255, 255, 255));
        jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel16.setText("S/");
        jLabel16.setToolTipText("");

        javax.swing.GroupLayout jPanel18Layout = new javax.swing.GroupLayout(jPanel18);
        jPanel18.setLayout(jPanel18Layout);
        jPanel18Layout.setHorizontalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel18Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel18Layout.setVerticalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel18Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        txtPrecioProductoCarrito.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtPrecioProductoCarrito.setToolTipText("");
        txtPrecioProductoCarrito.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 5));
        txtPrecioProductoCarrito.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtPrecioProductoCarritoKeyTyped(evt);
            }
        });

        lblDescripcionCarrito.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblDescripcionCarrito.setForeground(new java.awt.Color(255, 255, 255));
        lblDescripcionCarrito.setText("Descripción");

        jScrollPane6.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 5));

        txtDescripcionProductoCarrito.setColumns(20);
        txtDescripcionProductoCarrito.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtDescripcionProductoCarrito.setLineWrap(true);
        txtDescripcionProductoCarrito.setRows(5);
        txtDescripcionProductoCarrito.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 5));
        jScrollPane6.setViewportView(txtDescripcionProductoCarrito);

        txtStockProductoCarrito.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtStockProductoCarrito.setText("1");
        txtStockProductoCarrito.setToolTipText("");
        txtStockProductoCarrito.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(240, 240, 240), 5));
        txtStockProductoCarrito.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        btnActualizarVenta.setBackground(new java.awt.Color(33, 50, 60));
        btnActualizarVenta.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnActualizarVenta.setForeground(new java.awt.Color(255, 255, 255));
        btnActualizarVenta.setText("Actualizar item");
        btnActualizarVenta.setBorder(null);
        btnActualizarVenta.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        btnActualizarVenta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnActualizarVentaActionPerformed(evt);
            }
        });

        btnEliminarVenta.setBackground(new java.awt.Color(51, 0, 0));
        btnEliminarVenta.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnEliminarVenta.setForeground(new java.awt.Color(255, 255, 255));
        btnEliminarVenta.setText("Eliminar item");
        btnEliminarVenta.setBorder(null);
        btnEliminarVenta.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        btnEliminarVenta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarVentaActionPerformed(evt);
            }
        });

        btnLimpiarVenta.setBackground(new java.awt.Color(33, 50, 60));
        btnLimpiarVenta.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnLimpiarVenta.setForeground(new java.awt.Color(255, 255, 255));
        btnLimpiarVenta.setText("Limpiar carrito");
        btnLimpiarVenta.setBorder(null);
        btnLimpiarVenta.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        btnLimpiarVenta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimpiarVentaActionPerformed(evt);
            }
        });

        btnAgregarItemVenta.setBackground(new java.awt.Color(33, 50, 60));
        btnAgregarItemVenta.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnAgregarItemVenta.setForeground(new java.awt.Color(255, 255, 255));
        btnAgregarItemVenta.setText("Agregar item");
        btnAgregarItemVenta.setBorder(null);
        btnAgregarItemVenta.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        btnAgregarItemVenta.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnAgregarItemVentaMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel16Layout.createSequentialGroup()
                        .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(cbxCategoriaProductoCarrito, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblCategoriaCarrito, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(24, 24, 24)
                        .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel16Layout.createSequentialGroup()
                                .addComponent(txtCodigoProductoCarrito, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, 0)
                                .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(lblCodigoCarrito, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(58, 58, 58)
                        .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtNombreProductoCarrito, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblNombreCarrito, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel16Layout.createSequentialGroup()
                                .addComponent(jPanel18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, 0)
                                .addComponent(txtPrecioProductoCarrito, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(lblPrecioCarrito, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblDescripcionCarrito, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 318, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(19, 19, 19))
                    .addGroup(jPanel16Layout.createSequentialGroup()
                        .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel16Layout.createSequentialGroup()
                                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblStockCarrito, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtStockProductoCarrito, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(51, 51, 51)
                                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(spnCantidadProductoCarrito, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblCantidadCarrito, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel16Layout.createSequentialGroup()
                                .addGap(24, 24, 24)
                                .addComponent(btnEliminarVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(7, 7, 7)
                        .addComponent(btnActualizarVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(37, 37, 37)
                        .addComponent(btnLimpiarVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(43, 43, 43)
                        .addComponent(btnAgregarItemVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(52, 52, 52))))
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel16Layout.createSequentialGroup()
                        .addComponent(lblDescripcionCarrito)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel16Layout.createSequentialGroup()
                        .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel16Layout.createSequentialGroup()
                                .addComponent(lblCategoriaCarrito)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cbxCategoriaProductoCarrito, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(jPanel16Layout.createSequentialGroup()
                                    .addComponent(lblNombreCarrito)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(txtNombreProductoCarrito, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel16Layout.createSequentialGroup()
                                    .addComponent(lblCodigoCarrito)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(txtCodigoProductoCarrito, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                        .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel16Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(lblStockCarrito)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(jPanel16Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtStockProductoCarrito, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel16Layout.createSequentialGroup()
                                        .addComponent(lblPrecioCarrito)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jPanel18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(txtPrecioProductoCarrito, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(lblCantidadCarrito)
                                        .addGroup(jPanel16Layout.createSequentialGroup()
                                            .addGap(21, 21, 21)
                                            .addComponent(spnCantidadProductoCarrito, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(btnLimpiarVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(btnAgregarItemVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(btnEliminarVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(btnActualizarVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(13, 13, 13))))))
        );

        pnlFondoVenta.add(jPanel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(31, 168, -1, -1));

        txtIdProductoCarrito.setBackground(new java.awt.Color(102, 255, 102));
        txtIdProductoCarrito.setText("jTextField1");
        pnlFondoVenta.add(txtIdProductoCarrito, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 90, -1, -1));

        btnCancelarVenta.setBackground(new java.awt.Color(51, 0, 0));
        btnCancelarVenta.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnCancelarVenta.setForeground(new java.awt.Color(255, 255, 255));
        btnCancelarVenta.setText("Cancelar venta");
        btnCancelarVenta.setBorder(null);
        btnCancelarVenta.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        btnCancelarVenta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarVentaActionPerformed(evt);
            }
        });
        pnlFondoVenta.add(btnCancelarVenta, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 520, 150, 40));

        btnGenerarVenta.setBackground(new java.awt.Color(33, 50, 60));
        btnGenerarVenta.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnGenerarVenta.setForeground(new java.awt.Color(255, 255, 255));
        btnGenerarVenta.setText("Generar venta");
        btnGenerarVenta.setBorder(null);
        btnGenerarVenta.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        btnGenerarVenta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGenerarVentaActionPerformed(evt);
            }
        });
        pnlFondoVenta.add(btnGenerarVenta, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 520, 150, 40));

        lblPrecioCarrito1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblPrecioCarrito1.setForeground(new java.awt.Color(255, 255, 255));
        lblPrecioCarrito1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblPrecioCarrito1.setText("Total a pagar");
        pnlFondoVenta.add(lblPrecioCarrito1, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 530, 89, 30));

        jPanel19.setBackground(new java.awt.Color(67, 102, 129));

        jLabel17.setBackground(new java.awt.Color(67, 102, 129));
        jLabel17.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(255, 255, 255));
        jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel17.setText("S/");
        jLabel17.setToolTipText("");

        javax.swing.GroupLayout jPanel19Layout = new javax.swing.GroupLayout(jPanel19);
        jPanel19.setLayout(jPanel19Layout);
        jPanel19Layout.setHorizontalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel19Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel19Layout.setVerticalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel19Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pnlFondoVenta.add(jPanel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 530, -1, -1));

        txtTotalVenta.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtTotalVenta.setToolTipText("");
        txtTotalVenta.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(240, 240, 240), 5));
        txtTotalVenta.setFocusable(false);
        pnlFondoVenta.add(txtTotalVenta, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 530, 110, 30));

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)), "Empleado", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Tahoma", 0, 12), new java.awt.Color(153, 153, 153))); // NOI18N
        jPanel4.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jPanel4.setOpaque(false);

        lblNombreCarrito1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblNombreCarrito1.setForeground(new java.awt.Color(255, 255, 255));
        lblNombreCarrito1.setText("Nombre");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblNombreCarrito1, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbxEmpleadoVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblNombreCarrito1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbxEmpleadoVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pnlFondoVenta.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(614, 62, -1, -1));

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)), "Cliente", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Tahoma", 0, 12), new java.awt.Color(153, 153, 153))); // NOI18N
        jPanel6.setOpaque(false);

        cbxDocumentoClienteVenta.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "DNI", "CE" }));

        txtDocumentoClienteVenta.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtDocumentoClienteVenta.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 5));
        txtDocumentoClienteVenta.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtDocumentoClienteVentaKeyTyped(evt);
            }
        });

        jPanel21.setBackground(new java.awt.Color(67, 102, 129));
        jPanel21.setAlignmentX(0.0F);

        lblBotonBuscarClienteVenta.setBackground(new java.awt.Color(67, 102, 129));
        lblBotonBuscarClienteVenta.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblBotonBuscarClienteVenta.setForeground(new java.awt.Color(255, 255, 255));
        lblBotonBuscarClienteVenta.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblBotonBuscarClienteVenta.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/iconBuscar.png"))); // NOI18N
        lblBotonBuscarClienteVenta.setToolTipText("");
        lblBotonBuscarClienteVenta.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        lblBotonBuscarClienteVenta.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblBotonBuscarClienteVentaMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel21Layout = new javax.swing.GroupLayout(jPanel21);
        jPanel21.setLayout(jPanel21Layout);
        jPanel21Layout.setHorizontalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel21Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(lblBotonBuscarClienteVenta))
        );
        jPanel21Layout.setVerticalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel21Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(lblBotonBuscarClienteVenta))
        );

        lblNombreCarrito2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblNombreCarrito2.setForeground(new java.awt.Color(255, 255, 255));
        lblNombreCarrito2.setText("Nombre");

        txtNombreClienteVenta.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 5));
        txtNombreClienteVenta.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        txtNombreClienteVenta.setFocusable(false);
        txtNombreClienteVenta.setRequestFocusEnabled(false);
        txtNombreClienteVenta.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtNombreClienteVentaKeyTyped(evt);
            }
        });

        lblCodigoCarrito3.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblCodigoCarrito3.setForeground(new java.awt.Color(255, 255, 255));
        lblCodigoCarrito3.setText("Documento de identidad");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addComponent(cbxDocumentoClienteVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txtDocumentoClienteVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jPanel21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(36, 36, 36))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addComponent(lblCodigoCarrito3, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtNombreClienteVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblNombreCarrito2, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(15, 15, 15))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblNombreCarrito2)
                            .addComponent(lblCodigoCarrito3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtNombreClienteVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(5, 5, 5))
                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(cbxDocumentoClienteVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(txtDocumentoClienteVenta, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel21, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pnlFondoVenta.add(jPanel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(31, 62, -1, -1));

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/fondoVenta.png"))); // NOI18N
        pnlFondoVenta.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        javax.swing.GroupLayout pnlVentaLayout = new javax.swing.GroupLayout(pnlVenta);
        pnlVenta.setLayout(pnlVentaLayout);
        pnlVentaLayout.setHorizontalGroup(
            pnlVentaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlVentaLayout.createSequentialGroup()
                .addGap(90, 90, 90)
                .addComponent(pnlFondoVenta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(85, Short.MAX_VALUE))
        );
        pnlVentaLayout.setVerticalGroup(
            pnlVentaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlVentaLayout.createSequentialGroup()
                .addGap(13, 13, 13)
                .addComponent(pnlFondoVenta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tabbedPane.addTab("<html><center>Nueva&nbsp;&nbsp;&nbsp;<p>venta&nbsp;&nbsp;&nbsp;</center></html>", new javax.swing.ImageIcon(getClass().getResource("/Imagenes/iconCarrito.png")), pnlVenta, ""); // NOI18N
        pnlVenta.getAccessibleContext().setAccessibleDescription("");

        pnlInventario.setBackground(new java.awt.Color(33, 50, 60));

        pnlFondoInventario.setBackground(new java.awt.Color(18, 23, 28));
        pnlFondoInventario.setOpaque(false);
        pnlFondoInventario.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        pnlFondoTituloInventario.setBackground(new java.awt.Color(67, 102, 129));
        pnlFondoTituloInventario.setOpaque(false);
        pnlFondoTituloInventario.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblTituloInventario.setBackground(new java.awt.Color(255, 255, 153));
        lblTituloInventario.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        lblTituloInventario.setForeground(new java.awt.Color(255, 255, 255));
        lblTituloInventario.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTituloInventario.setText("Inventario");
        pnlFondoTituloInventario.add(lblTituloInventario, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 180, 43));

        jLabel10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/fondoTitulos.png"))); // NOI18N
        pnlFondoTituloInventario.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        pnlFondoInventario.add(pnlFondoTituloInventario, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 20, -1, -1));

        lblBuscarInventario.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblBuscarInventario.setForeground(new java.awt.Color(255, 255, 255));
        lblBuscarInventario.setText("Buscar por");
        pnlFondoInventario.add(lblBuscarInventario, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 70, -1, -1));

        cbxCriterioInventario.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        cbxCriterioInventario.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "ID", "Nombre", "Categoría", "Código" }));
        pnlFondoInventario.add(cbxCriterioInventario, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 90, 120, 35));

        txtCriterioInventario.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtCriterioInventario.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 5));
        txtCriterioInventario.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        txtCriterioInventario.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtCriterioInventarioKeyTyped(evt);
            }
        });
        pnlFondoInventario.add(txtCriterioInventario, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 90, 180, 30));

        btnDescargarTablaInventario.setBackground(new java.awt.Color(33, 50, 60));
        btnDescargarTablaInventario.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnDescargarTablaInventario.setForeground(new java.awt.Color(255, 255, 255));
        btnDescargarTablaInventario.setText("Descargar Tabla");
        btnDescargarTablaInventario.setBorder(null);
        btnDescargarTablaInventario.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        btnDescargarTablaInventario.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnDescargarTablaInventarioMouseClicked(evt);
            }
        });
        pnlFondoInventario.add(btnDescargarTablaInventario, new org.netbeans.lib.awtextra.AbsoluteConstraints(840, 90, 180, 40));

        btnDescargarInventario.setBackground(new java.awt.Color(33, 50, 60));
        btnDescargarInventario.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnDescargarInventario.setForeground(new java.awt.Color(255, 255, 255));
        btnDescargarInventario.setText("Descargar Inventario");
        btnDescargarInventario.setBorder(null);
        btnDescargarInventario.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        btnDescargarInventario.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnDescargarInventarioMouseClicked(evt);
            }
        });
        pnlFondoInventario.add(btnDescargarInventario, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 80, 180, 40));

        jPanel8.setBackground(new java.awt.Color(67, 102, 129));

        lblBotonBuscarInventario.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/iconBuscar.png"))); // NOI18N
        lblBotonBuscarInventario.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        lblBotonBuscarInventario.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblBotonBuscarInventarioMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addComponent(lblBotonBuscarInventario)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addComponent(lblBotonBuscarInventario)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pnlFondoInventario.add(jPanel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 90, 30, 30));

        tablaInventario = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex){
                return false;
            }
        };
        tablaInventario.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Código", "Nombre", "Categoría", "Descripción", "Costo", "Venta", "Stock"
            }
        ));
        tablaInventario.getTableHeader().setReorderingAllowed(false);
        tablaInventario.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablaInventarioMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(tablaInventario);
        if (tablaInventario.getColumnModel().getColumnCount() > 0) {
            tablaInventario.getColumnModel().getColumn(0).setPreferredWidth(50);
            tablaInventario.getColumnModel().getColumn(2).setPreferredWidth(100);
            tablaInventario.getColumnModel().getColumn(3).setPreferredWidth(100);
            tablaInventario.getColumnModel().getColumn(4).setPreferredWidth(200);
        }

        pnlFondoInventario.add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 150, 520, 330));

        jPanel14.setBackground(new java.awt.Color(18, 23, 28));
        jPanel14.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)), "Producto", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Tahoma", 0, 12), new java.awt.Color(153, 153, 153))); // NOI18N

        jLabel24.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel24.setForeground(new java.awt.Color(255, 255, 255));
        jLabel24.setText("Nombre");

        txtNombreInventario.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 5));
        txtNombreInventario.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        txtNombreInventario.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtNombreInventarioKeyTyped(evt);
            }
        });

        jLabel25.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel25.setForeground(new java.awt.Color(255, 255, 255));
        jLabel25.setText("Categoría");

        cbxCategoriaInventario.setToolTipText("");
        cbxCategoriaInventario.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbxCategoriaInventarioItemStateChanged(evt);
            }
        });

        jLabel26.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel26.setForeground(new java.awt.Color(255, 255, 255));
        jLabel26.setText("Código");

        txtCodigoInventario.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 5));
        txtCodigoInventario.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtCodigoInventarioKeyTyped(evt);
            }
        });

        lblDescripcionInventario.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblDescripcionInventario.setForeground(new java.awt.Color(255, 255, 255));
        lblDescripcionInventario.setText("Descripción");

        lblPrecioCostoInventario.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblPrecioCostoInventario.setForeground(new java.awt.Color(255, 255, 255));
        lblPrecioCostoInventario.setText("Precio Costo");

        pnlSOlPrecioCostoInventario.setBackground(new java.awt.Color(67, 102, 129));

        jLabel13.setBackground(new java.awt.Color(67, 102, 129));
        jLabel13.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel13.setText("S/");
        jLabel13.setToolTipText("");

        javax.swing.GroupLayout pnlSOlPrecioCostoInventarioLayout = new javax.swing.GroupLayout(pnlSOlPrecioCostoInventario);
        pnlSOlPrecioCostoInventario.setLayout(pnlSOlPrecioCostoInventarioLayout);
        pnlSOlPrecioCostoInventarioLayout.setHorizontalGroup(
            pnlSOlPrecioCostoInventarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlSOlPrecioCostoInventarioLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        pnlSOlPrecioCostoInventarioLayout.setVerticalGroup(
            pnlSOlPrecioCostoInventarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlSOlPrecioCostoInventarioLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        txtPrecioCostoInventario.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtPrecioCostoInventario.setToolTipText("");
        txtPrecioCostoInventario.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 5));
        txtPrecioCostoInventario.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtPrecioCostoInventarioKeyTyped(evt);
            }
        });

        lblPrecioVentaInventario.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblPrecioVentaInventario.setForeground(new java.awt.Color(255, 255, 255));
        lblPrecioVentaInventario.setText("Precio Venta");

        jPanel17.setBackground(new java.awt.Color(67, 102, 129));

        jLabel14.setBackground(new java.awt.Color(67, 102, 129));
        jLabel14.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(255, 255, 255));
        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel14.setText("S/");
        jLabel14.setToolTipText("");

        javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
        jPanel17.setLayout(jPanel17Layout);
        jPanel17Layout.setHorizontalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel17Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel17Layout.setVerticalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel17Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        txtPrecioVentaInventario.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtPrecioVentaInventario.setToolTipText("");
        txtPrecioVentaInventario.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 5));
        txtPrecioVentaInventario.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtPrecioVentaInventarioKeyTyped(evt);
            }
        });

        lblStockInventario.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblStockInventario.setForeground(new java.awt.Color(255, 255, 255));
        lblStockInventario.setText("Stock");

        txtStockInventario.setModel(new javax.swing.SpinnerNumberModel(1, 0, null, 1));
        txtStockInventario.setAutoscrolls(true);
        txtStockInventario.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        txtStockInventario.setEditor(new javax.swing.JSpinner.NumberEditor(txtStockInventario, ""));
        txtStockInventario.setFocusable(false);

        btnEliminarInventario.setBackground(new java.awt.Color(51, 0, 0));
        btnEliminarInventario.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnEliminarInventario.setForeground(new java.awt.Color(255, 255, 255));
        btnEliminarInventario.setText("Eliminar");
        btnEliminarInventario.setBorder(null);
        btnEliminarInventario.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        btnEliminarInventario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarInventarioActionPerformed(evt);
            }
        });

        btnActualizarInventario.setBackground(new java.awt.Color(33, 50, 60));
        btnActualizarInventario.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnActualizarInventario.setForeground(new java.awt.Color(255, 255, 255));
        btnActualizarInventario.setText("Actualizar");
        btnActualizarInventario.setBorder(null);
        btnActualizarInventario.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        btnActualizarInventario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnActualizarInventarioActionPerformed(evt);
            }
        });

        lblStockInventario1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblStockInventario1.setForeground(new java.awt.Color(255, 255, 255));
        lblStockInventario1.setText("Ver imagen");

        pnlImagenInventario.setBackground(new java.awt.Color(67, 102, 129));

        lblImagenInventario.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/iconImagen.png"))); // NOI18N
        lblImagenInventario.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        lblImagenInventario.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblImagenInventarioMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout pnlImagenInventarioLayout = new javax.swing.GroupLayout(pnlImagenInventario);
        pnlImagenInventario.setLayout(pnlImagenInventarioLayout);
        pnlImagenInventarioLayout.setHorizontalGroup(
            pnlImagenInventarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlImagenInventarioLayout.createSequentialGroup()
                .addComponent(lblImagenInventario)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        pnlImagenInventarioLayout.setVerticalGroup(
            pnlImagenInventarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlImagenInventarioLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(lblImagenInventario)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        txtDescripcionInventario.setColumns(20);
        txtDescripcionInventario.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtDescripcionInventario.setLineWrap(true);
        txtDescripcionInventario.setRows(5);
        txtDescripcionInventario.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 5));
        jScrollPane8.setViewportView(txtDescripcionInventario);

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addGap(4, 4, 4)
                        .addComponent(lblDescripcionInventario, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel14Layout.createSequentialGroup()
                        .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jScrollPane8, javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel14Layout.createSequentialGroup()
                                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtNombreInventario, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtIdInventario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(cbxCategoriaInventario, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtCodigoInventario, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel14Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addGroup(jPanel14Layout.createSequentialGroup()
                                        .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(lblPrecioCostoInventario, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGroup(jPanel14Layout.createSequentialGroup()
                                                .addComponent(pnlSOlPrecioCostoInventario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(0, 0, 0)
                                                .addComponent(txtPrecioCostoInventario, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addGap(21, 21, 21)
                                        .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(jPanel14Layout.createSequentialGroup()
                                                .addComponent(jPanel17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(0, 0, 0)
                                                .addComponent(txtPrecioVentaInventario, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addComponent(lblPrecioVentaInventario, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(24, 24, 24)
                                        .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(lblStockInventario, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(txtStockInventario, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(23, 23, 23)
                                        .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(lblStockInventario1, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(pnlImagenInventario, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGroup(jPanel14Layout.createSequentialGroup()
                                        .addComponent(btnEliminarInventario, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(btnActualizarInventario, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                        .addGap(40, 40, 40))))
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txtIdInventario, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel14Layout.createSequentialGroup()
                                .addComponent(jLabel26)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtCodigoInventario, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel14Layout.createSequentialGroup()
                                .addComponent(jLabel24)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtNombreInventario, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(5, 5, 5))
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addComponent(jLabel25)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cbxCategoriaInventario, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblDescripcionInventario)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addComponent(lblPrecioCostoInventario)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtStockInventario, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pnlSOlPrecioCostoInventario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtPrecioCostoInventario, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addComponent(lblPrecioVentaInventario)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtPrecioVentaInventario, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblStockInventario)
                            .addComponent(lblStockInventario1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pnlImagenInventario, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnEliminarInventario, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnActualizarInventario, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(12, Short.MAX_VALUE))
        );

        pnlFondoInventario.add(jPanel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 140, 466, 340));

        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/fondoInventario.png"))); // NOI18N
        pnlFondoInventario.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1040, -1));

        javax.swing.GroupLayout pnlInventarioLayout = new javax.swing.GroupLayout(pnlInventario);
        pnlInventario.setLayout(pnlInventarioLayout);
        pnlInventarioLayout.setHorizontalGroup(
            pnlInventarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlInventarioLayout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(pnlFondoInventario, javax.swing.GroupLayout.PREFERRED_SIZE, 1038, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlInventarioLayout.setVerticalGroup(
            pnlInventarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlInventarioLayout.createSequentialGroup()
                .addGap(46, 46, 46)
                .addComponent(pnlFondoInventario, javax.swing.GroupLayout.PREFERRED_SIZE, 500, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(53, Short.MAX_VALUE))
        );

        tabbedPane.addTab("<html><right>Inventario</right></html>", new javax.swing.ImageIcon(getClass().getResource("/Imagenes/iconInventario.png")), pnlInventario); // NOI18N

        pnlProducto.setBackground(new java.awt.Color(33, 50, 60));

        pnlFondoProducto.setBackground(new java.awt.Color(18, 23, 28));
        pnlFondoProducto.setOpaque(false);
        pnlFondoProducto.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        pnlTituloProducto.setBackground(new java.awt.Color(67, 102, 129));
        pnlTituloProducto.setOpaque(false);
        pnlTituloProducto.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblTituloProducto.setBackground(new java.awt.Color(67, 102, 129));
        lblTituloProducto.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        lblTituloProducto.setForeground(new java.awt.Color(255, 255, 255));
        lblTituloProducto.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTituloProducto.setText("Nuevo Producto");
        pnlTituloProducto.add(lblTituloProducto, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 180, 43));

        lblFondoTituloProducto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/fondoTitulos.png"))); // NOI18N
        pnlTituloProducto.add(lblFondoTituloProducto, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        pnlFondoProducto.add(pnlTituloProducto, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 20, 180, 43));

        lblNombreProducto.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblNombreProducto.setForeground(new java.awt.Color(255, 255, 255));
        lblNombreProducto.setText("Nombre*");
        pnlFondoProducto.add(lblNombreProducto, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 90, -1, -1));

        txtNombreProducto.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtNombreProducto.setToolTipText("");
        txtNombreProducto.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 5));
        txtNombreProducto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtNombreProductoKeyTyped(evt);
            }
        });
        pnlFondoProducto.add(txtNombreProducto, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 110, 180, 30));

        lblCategoriaProducto.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblCategoriaProducto.setForeground(new java.awt.Color(255, 255, 255));
        lblCategoriaProducto.setText("Categoría*");
        pnlFondoProducto.add(lblCategoriaProducto, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 90, -1, -1));

        cbxCategoriaProducto.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        cbxCategoriaProducto.setBorder(null);
        cbxCategoriaProducto.setLightWeightPopupEnabled(false);
        cbxCategoriaProducto.setRequestFocusEnabled(false);
        cbxCategoriaProducto.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbxCategoriaProductoItemStateChanged(evt);
            }
        });
        pnlFondoProducto.add(cbxCategoriaProducto, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 110, 180, 35));

        lblDescripcionProducto.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblDescripcionProducto.setForeground(new java.awt.Color(255, 255, 255));
        lblDescripcionProducto.setText("Descripción");
        pnlFondoProducto.add(lblDescripcionProducto, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 160, -1, -1));

        txtDescripcionProducto.setColumns(20);
        txtDescripcionProducto.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtDescripcionProducto.setLineWrap(true);
        txtDescripcionProducto.setRows(4);
        txtDescripcionProducto.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 5));
        jScrollPane2.setViewportView(txtDescripcionProducto);

        pnlFondoProducto.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 180, 400, 80));

        lblPrecioCompraProducto.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblPrecioCompraProducto.setForeground(new java.awt.Color(255, 255, 255));
        lblPrecioCompraProducto.setText("Precio de compra*");
        pnlFondoProducto.add(lblPrecioCompraProducto, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 280, -1, -1));

        jPanel9.setBackground(new java.awt.Color(67, 102, 129));

        jLabel11.setBackground(new java.awt.Color(67, 102, 129));
        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel11.setText("S/");
        jLabel11.setToolTipText("");

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pnlFondoProducto.add(jPanel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 300, 30, 30));

        txtPrecioCompraProducto.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtPrecioCompraProducto.setToolTipText("");
        txtPrecioCompraProducto.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 5));
        txtPrecioCompraProducto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtPrecioCompraProductoKeyTyped(evt);
            }
        });
        pnlFondoProducto.add(txtPrecioCompraProducto, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 300, 150, 30));

        lblPrecioVentaProducto.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblPrecioVentaProducto.setForeground(new java.awt.Color(255, 255, 255));
        lblPrecioVentaProducto.setText("Precio de venta");
        pnlFondoProducto.add(lblPrecioVentaProducto, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 280, -1, -1));

        jPanel10.setBackground(new java.awt.Color(67, 102, 129));

        jLabel31.setBackground(new java.awt.Color(67, 102, 129));
        jLabel31.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel31.setForeground(new java.awt.Color(255, 255, 255));
        jLabel31.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel31.setText("S/");
        jLabel31.setToolTipText("");

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel31, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel31, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pnlFondoProducto.add(jPanel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 300, 30, 30));

        txtPrecioVentaProducto.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtPrecioVentaProducto.setToolTipText("");
        txtPrecioVentaProducto.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 5));
        txtPrecioVentaProducto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtPrecioVentaProductoKeyTyped(evt);
            }
        });
        pnlFondoProducto.add(txtPrecioVentaProducto, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 300, 150, 30));

        lblStockProducto.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblStockProducto.setForeground(new java.awt.Color(255, 255, 255));
        lblStockProducto.setText("Stock*");
        pnlFondoProducto.add(lblStockProducto, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 350, -1, -1));

        txtStockProducto.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtStockProducto.setModel(new javax.swing.SpinnerNumberModel(1, 0, null, 1));
        txtStockProducto.setAutoscrolls(true);
        txtStockProducto.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        txtStockProducto.setEditor(new javax.swing.JSpinner.NumberEditor(txtStockProducto, ""));
        txtStockProducto.setFocusable(false);
        pnlFondoProducto.add(txtStockProducto, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 370, 70, 30));

        lblCodigoProducto.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblCodigoProducto.setForeground(new java.awt.Color(255, 255, 255));
        lblCodigoProducto.setText("Código*");
        pnlFondoProducto.add(lblCodigoProducto, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 350, -1, -1));

        txtCodigoProducto.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtCodigoProducto.setToolTipText("");
        txtCodigoProducto.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 5));
        txtCodigoProducto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtCodigoProductoKeyTyped(evt);
            }
        });
        pnlFondoProducto.add(txtCodigoProducto, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 370, 180, 30));

        lblFotoProducto.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblFotoProducto.setForeground(new java.awt.Color(255, 255, 255));
        lblFotoProducto.setText("Foto*");
        pnlFondoProducto.add(lblFotoProducto, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 350, -1, -1));

        txtFotoProducto.setEditable(false);
        txtFotoProducto.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtFotoProducto.setToolTipText("");
        txtFotoProducto.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(245, 241, 241), 5));
        pnlFondoProducto.add(txtFotoProducto, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 370, 80, 30));

        jPanel12.setBackground(new java.awt.Color(67, 102, 129));
        jPanel12.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        btnFotoProducto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/iconUpload.png"))); // NOI18N
        btnFotoProducto.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnFotoProductoMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel12Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(btnFotoProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addComponent(btnFotoProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pnlFondoProducto.add(jPanel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 370, 30, 30));

        btnGuardarProducto.setBackground(new java.awt.Color(33, 50, 60));
        btnGuardarProducto.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnGuardarProducto.setForeground(new java.awt.Color(255, 255, 255));
        btnGuardarProducto.setText("Guardar");
        btnGuardarProducto.setBorder(null);
        btnGuardarProducto.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        btnGuardarProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarProductoActionPerformed(evt);
            }
        });
        pnlFondoProducto.add(btnGuardarProducto, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 430, 180, 40));

        btnLimpiarProducto.setBackground(new java.awt.Color(33, 50, 60));
        btnLimpiarProducto.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnLimpiarProducto.setForeground(new java.awt.Color(255, 255, 255));
        btnLimpiarProducto.setText("Limpiar");
        btnLimpiarProducto.setBorder(null);
        btnLimpiarProducto.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        btnLimpiarProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimpiarProductoActionPerformed(evt);
            }
        });
        pnlFondoProducto.add(btnLimpiarProducto, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 430, 180, 40));

        lblFondoProducto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/fondoProducto.png"))); // NOI18N
        pnlFondoProducto.add(lblFondoProducto, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        javax.swing.GroupLayout pnlProductoLayout = new javax.swing.GroupLayout(pnlProducto);
        pnlProducto.setLayout(pnlProductoLayout);
        pnlProductoLayout.setHorizontalGroup(
            pnlProductoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlProductoLayout.createSequentialGroup()
                .addGap(304, 304, 304)
                .addComponent(pnlFondoProducto, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(305, 305, 305))
        );
        pnlProductoLayout.setVerticalGroup(
            pnlProductoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlProductoLayout.createSequentialGroup()
                .addGap(49, 49, 49)
                .addComponent(pnlFondoProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 497, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(53, Short.MAX_VALUE))
        );

        tabbedPane.addTab("<html><right>Nuevo<p>producto</right></html>", new javax.swing.ImageIcon(getClass().getResource("/Imagenes/iconProducto.png")), pnlProducto); // NOI18N

        pnlEmpleado.setBackground(new java.awt.Color(33, 50, 60));

        pnlFondoEmpleado.setBackground(new java.awt.Color(18, 23, 28));
        pnlFondoEmpleado.setOpaque(false);
        pnlFondoEmpleado.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        pnlTituloEmpleado.setBackground(new java.awt.Color(67, 102, 129));
        pnlTituloEmpleado.setOpaque(false);
        pnlTituloEmpleado.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblTituloEmpleado.setBackground(new java.awt.Color(255, 255, 153));
        lblTituloEmpleado.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        lblTituloEmpleado.setForeground(new java.awt.Color(255, 255, 255));
        lblTituloEmpleado.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTituloEmpleado.setText("Empleados");
        pnlTituloEmpleado.add(lblTituloEmpleado, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 180, 43));

        jLabel15.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/fondoTitulos.png"))); // NOI18N
        pnlTituloEmpleado.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        pnlFondoEmpleado.add(pnlTituloEmpleado, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 30, 180, -1));

        lblNombreEmpleado.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblNombreEmpleado.setForeground(new java.awt.Color(255, 255, 255));
        lblNombreEmpleado.setText("Nombre*");
        pnlFondoEmpleado.add(lblNombreEmpleado, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 90, -1, -1));

        txtNombreEmpleado.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtNombreEmpleado.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 5));
        txtNombreEmpleado.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtNombreEmpleadoKeyTyped(evt);
            }
        });
        pnlFondoEmpleado.add(txtNombreEmpleado, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 110, 180, 30));

        lblApePaternoEmpleado.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblApePaternoEmpleado.setForeground(new java.awt.Color(255, 255, 255));
        lblApePaternoEmpleado.setText("Apellido Paterno*");
        pnlFondoEmpleado.add(lblApePaternoEmpleado, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 90, -1, -1));

        txtApePaternoEmpleado.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtApePaternoEmpleado.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 5));
        txtApePaternoEmpleado.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtApePaternoEmpleadoKeyTyped(evt);
            }
        });
        pnlFondoEmpleado.add(txtApePaternoEmpleado, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 110, 180, 30));

        lblApeMaternoEmpleado.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblApeMaternoEmpleado.setForeground(new java.awt.Color(255, 255, 255));
        lblApeMaternoEmpleado.setText("Apellido Materno");
        pnlFondoEmpleado.add(lblApeMaternoEmpleado, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 90, -1, -1));

        txtApeMaternoEmpleado.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtApeMaternoEmpleado.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 5));
        txtApeMaternoEmpleado.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtApeMaternoEmpleadoKeyTyped(evt);
            }
        });
        pnlFondoEmpleado.add(txtApeMaternoEmpleado, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 110, 180, 30));

        lblDocumentoEmpleado.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblDocumentoEmpleado.setForeground(new java.awt.Color(255, 255, 255));
        lblDocumentoEmpleado.setText("Documento de Identidad*");
        pnlFondoEmpleado.add(lblDocumentoEmpleado, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 160, -1, -1));

        cbxDocumentoEmpleado.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "DNI", "CE" }));
        pnlFondoEmpleado.add(cbxDocumentoEmpleado, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 180, 55, 35));

        txtDocumentoEmpleado.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtDocumentoEmpleado.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 5));
        txtDocumentoEmpleado.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtDocumentoEmpleadoKeyTyped(evt);
            }
        });
        pnlFondoEmpleado.add(txtDocumentoEmpleado, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 180, 120, 30));

        lblCelularEmpleado.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblCelularEmpleado.setForeground(new java.awt.Color(255, 255, 255));
        lblCelularEmpleado.setText("Celular*");
        pnlFondoEmpleado.add(lblCelularEmpleado, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 160, -1, -1));

        jPanel2.setBackground(new java.awt.Color(67, 102, 129));

        jLabel12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/iconCellphone.png"))); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pnlFondoEmpleado.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 180, 30, 30));

        txtCelularEmpleado.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtCelularEmpleado.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 5));
        txtCelularEmpleado.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtCelularEmpleadoKeyTyped(evt);
            }
        });
        pnlFondoEmpleado.add(txtCelularEmpleado, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 180, 150, 30));

        lblFecNacimientoEmpleado.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblFecNacimientoEmpleado.setForeground(new java.awt.Color(255, 255, 255));
        lblFecNacimientoEmpleado.setText("Fecha de Nacimiento (dd-mm-yyyy)");
        pnlFondoEmpleado.add(lblFecNacimientoEmpleado, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 160, -1, -1));

        dchFecNacimientoEmpleado.setDateFormatString("dd-MM-yyyy");
        dchFecNacimientoEmpleado.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        dchFecNacimientoEmpleado.setOpaque(false);
        dchFecNacimientoEmpleado.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                dchFecNacimientoEmpleadoKeyTyped(evt);
            }
        });
        pnlFondoEmpleado.add(dchFecNacimientoEmpleado, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 180, 180, 35));

        lblFecIngresoEmpleado.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblFecIngresoEmpleado.setForeground(new java.awt.Color(255, 255, 255));
        lblFecIngresoEmpleado.setText("Fecha de Ingreso (dd-mm-yyyy)");
        pnlFondoEmpleado.add(lblFecIngresoEmpleado, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 230, -1, -1));

        dchFecIngresoEmpleado.setDateFormatString("dd-MM-yyyy");
        dchFecIngresoEmpleado.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        dchFecIngresoEmpleado.setOpaque(false);
        pnlFondoEmpleado.add(dchFecIngresoEmpleado, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 250, 180, 35));

        btnGuardarEmpleado.setBackground(new java.awt.Color(33, 50, 60));
        btnGuardarEmpleado.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnGuardarEmpleado.setForeground(new java.awt.Color(255, 255, 255));
        btnGuardarEmpleado.setText("Guardar");
        btnGuardarEmpleado.setBorder(null);
        btnGuardarEmpleado.setBorderPainted(false);
        btnGuardarEmpleado.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        btnGuardarEmpleado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarEmpleadoActionPerformed(evt);
            }
        });
        pnlFondoEmpleado.add(btnGuardarEmpleado, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 250, 120, 40));

        btnActualizarEmpleado.setBackground(new java.awt.Color(33, 50, 60));
        btnActualizarEmpleado.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnActualizarEmpleado.setForeground(new java.awt.Color(255, 255, 255));
        btnActualizarEmpleado.setText("Actualizar");
        btnActualizarEmpleado.setBorder(null);
        btnActualizarEmpleado.setBorderPainted(false);
        btnActualizarEmpleado.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        btnActualizarEmpleado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnActualizarEmpleadoActionPerformed(evt);
            }
        });
        pnlFondoEmpleado.add(btnActualizarEmpleado, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 250, 120, 40));

        btnEliminarEmpleado.setBackground(new java.awt.Color(51, 0, 0));
        btnEliminarEmpleado.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnEliminarEmpleado.setForeground(new java.awt.Color(255, 255, 255));
        btnEliminarEmpleado.setText("Eliminar");
        btnEliminarEmpleado.setBorder(null);
        btnEliminarEmpleado.setBorderPainted(false);
        btnEliminarEmpleado.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        btnEliminarEmpleado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarEmpleadoActionPerformed(evt);
            }
        });
        pnlFondoEmpleado.add(btnEliminarEmpleado, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 250, 120, 40));

        btnLimpiarEmpleado.setBackground(new java.awt.Color(33, 50, 60));
        btnLimpiarEmpleado.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnLimpiarEmpleado.setForeground(new java.awt.Color(255, 255, 255));
        btnLimpiarEmpleado.setText("Limpiar");
        btnLimpiarEmpleado.setBorder(null);
        btnLimpiarEmpleado.setBorderPainted(false);
        btnLimpiarEmpleado.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        btnLimpiarEmpleado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimpiarEmpleadoActionPerformed(evt);
            }
        });
        pnlFondoEmpleado.add(btnLimpiarEmpleado, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 250, 120, 40));

        tablaEmpleado.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Nombre", "A. Paterno", "A. Materno", "DNI/CE", "Celular", "F. Nacimiento", "F. Ingreso"
            }
        ));
        tablaEmpleado.getTableHeader().setReorderingAllowed(false);
        tablaEmpleado.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablaEmpleadoMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tablaEmpleado);
        if (tablaEmpleado.getColumnModel().getColumnCount() > 0) {
            tablaEmpleado.getColumnModel().getColumn(0).setPreferredWidth(5);
            tablaEmpleado.getColumnModel().getColumn(1).setPreferredWidth(20);
            tablaEmpleado.getColumnModel().getColumn(2).setPreferredWidth(20);
            tablaEmpleado.getColumnModel().getColumn(3).setPreferredWidth(40);
            tablaEmpleado.getColumnModel().getColumn(3).setHeaderValue("A. Materno");
            tablaEmpleado.getColumnModel().getColumn(4).setPreferredWidth(15);
            tablaEmpleado.getColumnModel().getColumn(5).setPreferredWidth(8);
            tablaEmpleado.getColumnModel().getColumn(6).setPreferredWidth(10);
            tablaEmpleado.getColumnModel().getColumn(6).setHeaderValue("F. Nacimiento");
            tablaEmpleado.getColumnModel().getColumn(7).setPreferredWidth(3);
            tablaEmpleado.getColumnModel().getColumn(7).setHeaderValue("F. Ingreso");
        }

        pnlFondoEmpleado.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 310, 810, 172));

        txtIdEmpleado.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtIdEmpleado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtIdEmpleadoActionPerformed(evt);
            }
        });
        pnlFondoEmpleado.add(txtIdEmpleado, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 50, 40, 20));

        lblFondoEmpleado.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/fondoEmpleado.png"))); // NOI18N
        pnlFondoEmpleado.add(lblFondoEmpleado, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        javax.swing.GroupLayout pnlEmpleadoLayout = new javax.swing.GroupLayout(pnlEmpleado);
        pnlEmpleado.setLayout(pnlEmpleadoLayout);
        pnlEmpleadoLayout.setHorizontalGroup(
            pnlEmpleadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlEmpleadoLayout.createSequentialGroup()
                .addGap(104, 104, 104)
                .addComponent(pnlFondoEmpleado, javax.swing.GroupLayout.PREFERRED_SIZE, 868, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(107, Short.MAX_VALUE))
        );
        pnlEmpleadoLayout.setVerticalGroup(
            pnlEmpleadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlEmpleadoLayout.createSequentialGroup()
                .addGap(42, 42, 42)
                .addComponent(pnlFondoEmpleado, javax.swing.GroupLayout.PREFERRED_SIZE, 511, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(46, Short.MAX_VALUE))
        );

        tabbedPane.addTab("<html><right>Empleados</right></html>", new javax.swing.ImageIcon(getClass().getResource("/Imagenes/iconEmpleado.png")), pnlEmpleado); // NOI18N

        pnlReporte.setBackground(new java.awt.Color(33, 50, 60));

        pnlFondoReporte.setBackground(new java.awt.Color(18, 23, 28));
        pnlFondoReporte.setOpaque(false);
        pnlFondoReporte.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        pnlTituloReporte.setBackground(new java.awt.Color(67, 102, 129));
        pnlTituloReporte.setOpaque(false);
        pnlTituloReporte.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblTituloReporte.setBackground(new java.awt.Color(255, 255, 153));
        lblTituloReporte.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        lblTituloReporte.setForeground(new java.awt.Color(255, 255, 255));
        lblTituloReporte.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTituloReporte.setText("Reportes de ventas");
        pnlTituloReporte.add(lblTituloReporte, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 180, 43));

        lblFondoTitulo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/fondoTitulos.png"))); // NOI18N
        pnlTituloReporte.add(lblFondoTitulo, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        pnlFondoReporte.add(pnlTituloReporte, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 20, 180, -1));

        btnDescargaReporte.setBackground(new java.awt.Color(33, 50, 60));
        btnDescargaReporte.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnDescargaReporte.setForeground(new java.awt.Color(255, 255, 255));
        btnDescargaReporte.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/iconDescarga.png"))); // NOI18N
        btnDescargaReporte.setText("Descargar progreso del día");
        btnDescargaReporte.setBorder(null);
        btnDescargaReporte.setBorderPainted(false);
        btnDescargaReporte.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        btnDescargaReporte.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDescargaReporteActionPerformed(evt);
            }
        });
        pnlFondoReporte.add(btnDescargaReporte, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 80, 220, 50));

        dchFechaReporte.setDateFormatString("dd-MM-yyyy");
        dchFechaReporte.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        dchFechaReporte.setOpaque(false);
        pnlFondoReporte.add(dchFechaReporte, new org.netbeans.lib.awtextra.AbsoluteConstraints(710, 100, 180, 35));

        jPanel7.setBackground(new java.awt.Color(67, 102, 129));

        lblBotonBuscarReporte.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/iconBuscar.png"))); // NOI18N
        lblBotonBuscarReporte.setToolTipText("");
        lblBotonBuscarReporte.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        lblBotonBuscarReporte.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblBotonBuscarReporteMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addComponent(lblBotonBuscarReporte, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addComponent(lblBotonBuscarReporte, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pnlFondoReporte.add(jPanel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(910, 100, 30, 30));

        lblSelFechaReporte.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblSelFechaReporte.setForeground(new java.awt.Color(255, 255, 255));
        lblSelFechaReporte.setText("Seleccionar fecha");
        pnlFondoReporte.add(lblSelFechaReporte, new org.netbeans.lib.awtextra.AbsoluteConstraints(710, 80, -1, -1));

        tablaReporte.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "#", "ID", "Fecha", "DNI/CE Cliente", "Nombre Cliente", "Empleado", "Descripción", "Total", "Progreso del Día"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tablaReporte.getTableHeader().setReorderingAllowed(false);
        jScrollPane7.setViewportView(tablaReporte);
        if (tablaReporte.getColumnModel().getColumnCount() > 0) {
            tablaReporte.getColumnModel().getColumn(0).setMinWidth(10);
            tablaReporte.getColumnModel().getColumn(0).setPreferredWidth(10);
            tablaReporte.getColumnModel().getColumn(1).setMinWidth(15);
            tablaReporte.getColumnModel().getColumn(1).setPreferredWidth(15);
            tablaReporte.getColumnModel().getColumn(2).setMinWidth(35);
            tablaReporte.getColumnModel().getColumn(2).setPreferredWidth(35);
            tablaReporte.getColumnModel().getColumn(3).setMinWidth(35);
            tablaReporte.getColumnModel().getColumn(3).setPreferredWidth(35);
        }

        pnlFondoReporte.add(jScrollPane7, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 150, 980, 172));

        lblPrecioReporte.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblPrecioReporte.setForeground(new java.awt.Color(255, 255, 255));
        lblPrecioReporte.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblPrecioReporte.setText("Total del día");
        pnlFondoReporte.add(lblPrecioReporte, new org.netbeans.lib.awtextra.AbsoluteConstraints(740, 340, 89, 30));

        pnlIconSolReporte.setBackground(new java.awt.Color(67, 102, 129));

        lblIconSolReporte.setBackground(new java.awt.Color(67, 102, 129));
        lblIconSolReporte.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblIconSolReporte.setForeground(new java.awt.Color(255, 255, 255));
        lblIconSolReporte.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblIconSolReporte.setText("S/");
        lblIconSolReporte.setToolTipText("");

        javax.swing.GroupLayout pnlIconSolReporteLayout = new javax.swing.GroupLayout(pnlIconSolReporte);
        pnlIconSolReporte.setLayout(pnlIconSolReporteLayout);
        pnlIconSolReporteLayout.setHorizontalGroup(
            pnlIconSolReporteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlIconSolReporteLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(lblIconSolReporte, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        pnlIconSolReporteLayout.setVerticalGroup(
            pnlIconSolReporteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlIconSolReporteLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(lblIconSolReporte, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pnlFondoReporte.add(pnlIconSolReporte, new org.netbeans.lib.awtextra.AbsoluteConstraints(850, 340, -1, -1));

        txtTotalReporte.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtTotalReporte.setToolTipText("");
        txtTotalReporte.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(240, 240, 240), 5));
        txtTotalReporte.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        txtTotalReporte.setFocusable(false);
        txtTotalReporte.setRequestFocusEnabled(false);
        pnlFondoReporte.add(txtTotalReporte, new org.netbeans.lib.awtextra.AbsoluteConstraints(880, 340, 130, 30));

        lblFondoReporte.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/fondoReporte.png"))); // NOI18N
        pnlFondoReporte.add(lblFondoReporte, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1040, -1));

        javax.swing.GroupLayout pnlReporteLayout = new javax.swing.GroupLayout(pnlReporte);
        pnlReporte.setLayout(pnlReporteLayout);
        pnlReporteLayout.setHorizontalGroup(
            pnlReporteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlReporteLayout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(pnlFondoReporte, javax.swing.GroupLayout.PREFERRED_SIZE, 1038, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(22, Short.MAX_VALUE))
        );
        pnlReporteLayout.setVerticalGroup(
            pnlReporteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlReporteLayout.createSequentialGroup()
                .addGap(100, 100, 100)
                .addComponent(pnlFondoReporte, javax.swing.GroupLayout.PREFERRED_SIZE, 396, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(103, Short.MAX_VALUE))
        );

        tabbedPane.addTab("<html><right>Reportes&nbsp;<p>de venta&nbsp;</right></html>", new javax.swing.ImageIcon(getClass().getResource("/Imagenes/iconReporte.png")), pnlReporte); // NOI18N

        pnlCliente.setBackground(new java.awt.Color(33, 50, 60));

        pnlFondoCliente.setBackground(new java.awt.Color(18, 23, 28));
        pnlFondoCliente.setOpaque(false);
        pnlFondoCliente.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        pnlTituloCliente.setBackground(new java.awt.Color(67, 102, 129));
        pnlTituloCliente.setOpaque(false);
        pnlTituloCliente.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblTituloCliente.setBackground(new java.awt.Color(255, 255, 153));
        lblTituloCliente.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        lblTituloCliente.setForeground(new java.awt.Color(255, 255, 255));
        lblTituloCliente.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTituloCliente.setText("Clientes");
        pnlTituloCliente.add(lblTituloCliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 180, 43));

        jLabel18.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/fondoTitulos.png"))); // NOI18N
        pnlTituloCliente.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        pnlFondoCliente.add(pnlTituloCliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 30, 180, -1));

        jPanel20.setBackground(new java.awt.Color(18, 23, 28));
        jPanel20.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)), "Cliente", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Tahoma", 0, 12), new java.awt.Color(153, 153, 153))); // NOI18N

        lblNombreCliente.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblNombreCliente.setForeground(new java.awt.Color(255, 255, 255));
        lblNombreCliente.setText("Nombre(s)*");

        txtNombreCliente.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtNombreCliente.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 5));
        txtNombreCliente.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtNombreClienteKeyTyped(evt);
            }
        });

        lblApeCliente.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblApeCliente.setForeground(new java.awt.Color(255, 255, 255));
        lblApeCliente.setText("Apellido(s)");

        txtApeCliente.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtApeCliente.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 5));
        txtApeCliente.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtApeClienteKeyTyped(evt);
            }
        });

        lblDocumentoCliente.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblDocumentoCliente.setForeground(new java.awt.Color(255, 255, 255));
        lblDocumentoCliente.setText("Documento de Identidad*");

        cbxDocumentoCliente.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "DNI", "CE" }));

        txtDocumentoCliente.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtDocumentoCliente.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 5));
        txtDocumentoCliente.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtDocumentoClienteKeyTyped(evt);
            }
        });

        lblCelularCliente.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblCelularCliente.setForeground(new java.awt.Color(255, 255, 255));
        lblCelularCliente.setText("Celular");

        jPanel11.setBackground(new java.awt.Color(67, 102, 129));

        jLabel19.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/iconCellphone.png"))); // NOI18N

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        txtCelularCliente.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtCelularCliente.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 5));
        txtCelularCliente.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtCelularClienteKeyTyped(evt);
            }
        });

        lblInstaCliente.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblInstaCliente.setForeground(new java.awt.Color(255, 255, 255));
        lblInstaCliente.setText("Instagram");

        txtInstagramCliente.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtInstagramCliente.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 5));
        txtInstagramCliente.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtInstagramClienteKeyTyped(evt);
            }
        });

        javax.swing.GroupLayout jPanel20Layout = new javax.swing.GroupLayout(jPanel20);
        jPanel20.setLayout(jPanel20Layout);
        jPanel20Layout.setHorizontalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel20Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(lblInstaCliente)
                        .addComponent(txtInstagramCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(lblCelularCliente)
                        .addGroup(jPanel20Layout.createSequentialGroup()
                            .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(0, 0, 0)
                            .addComponent(txtCelularCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(lblApeCliente)
                        .addComponent(txtApeCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(lblNombreCliente)
                                .addComponent(txtNombreCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(lblDocumentoCliente)
                                .addGroup(jPanel20Layout.createSequentialGroup()
                                    .addComponent(cbxDocumentoCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(5, 5, 5)
                                    .addComponent(txtDocumentoCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                .addContainerGap(20, Short.MAX_VALUE))
        );
        jPanel20Layout.setVerticalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel20Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(lblDocumentoCliente)
                .addGap(5, 5, 5)
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cbxDocumentoCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtDocumentoCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(lblNombreCliente)
                .addGap(5, 5, 5)
                .addComponent(txtNombreCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lblApeCliente)
                .addGap(5, 5, 5)
                .addComponent(txtApeCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lblCelularCliente)
                .addGap(5, 5, 5)
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCelularCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(lblInstaCliente)
                .addGap(5, 5, 5)
                .addComponent(txtInstagramCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(24, Short.MAX_VALUE))
        );

        pnlFondoCliente.add(jPanel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 80, 230, 390));

        lblBuscarCliente.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblBuscarCliente.setForeground(new java.awt.Color(255, 255, 255));
        lblBuscarCliente.setText("Buscar por");
        pnlFondoCliente.add(lblBuscarCliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 110, -1, -1));

        cbxCriterioCliente.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        cbxCriterioCliente.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "#", "DNI/CE", "Nombre(s)", "Apellido(s)", "Celular", "Instagram" }));
        pnlFondoCliente.add(cbxCriterioCliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 100, 120, 35));

        txtCriterioCliente.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtCriterioCliente.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 5));
        txtCriterioCliente.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        txtCriterioCliente.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtCriterioClienteKeyTyped(evt);
            }
        });
        pnlFondoCliente.add(txtCriterioCliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 100, 180, 30));

        jPanel15.setBackground(new java.awt.Color(67, 102, 129));

        lblBotonBuscarCliente.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/iconBuscar.png"))); // NOI18N
        lblBotonBuscarCliente.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        lblBotonBuscarCliente.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblBotonBuscarClienteMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addComponent(lblBotonBuscarCliente)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addComponent(lblBotonBuscarCliente)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pnlFondoCliente.add(jPanel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(800, 100, 30, 30));

        tablaCliente.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "#", "DNI/CE", "Nombre(s)", "Apellido(s)", "Celular", "Instagram"
            }
        ));
        tablaCliente.getTableHeader().setReorderingAllowed(false);
        tablaCliente.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablaClienteMouseClicked(evt);
            }
        });
        jScrollPane5.setViewportView(tablaCliente);
        if (tablaCliente.getColumnModel().getColumnCount() > 0) {
            tablaCliente.getColumnModel().getColumn(0).setPreferredWidth(5);
            tablaCliente.getColumnModel().getColumn(1).setPreferredWidth(15);
            tablaCliente.getColumnModel().getColumn(2).setPreferredWidth(20);
            tablaCliente.getColumnModel().getColumn(3).setPreferredWidth(20);
            tablaCliente.getColumnModel().getColumn(4).setPreferredWidth(8);
        }

        pnlFondoCliente.add(jScrollPane5, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 160, 610, 250));

        btnGuardarCliente.setBackground(new java.awt.Color(33, 50, 60));
        btnGuardarCliente.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnGuardarCliente.setForeground(new java.awt.Color(255, 255, 255));
        btnGuardarCliente.setText("Guardar");
        btnGuardarCliente.setBorder(null);
        btnGuardarCliente.setBorderPainted(false);
        btnGuardarCliente.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        btnGuardarCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarClienteActionPerformed(evt);
            }
        });
        pnlFondoCliente.add(btnGuardarCliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(800, 430, 120, 40));

        btnActualizarCliente.setBackground(new java.awt.Color(33, 50, 60));
        btnActualizarCliente.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnActualizarCliente.setForeground(new java.awt.Color(255, 255, 255));
        btnActualizarCliente.setText("Actualizar");
        btnActualizarCliente.setBorder(null);
        btnActualizarCliente.setBorderPainted(false);
        btnActualizarCliente.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        btnActualizarCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnActualizarClienteActionPerformed(evt);
            }
        });
        pnlFondoCliente.add(btnActualizarCliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 430, 120, 40));

        btnEliminarCliente.setBackground(new java.awt.Color(51, 0, 0));
        btnEliminarCliente.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnEliminarCliente.setForeground(new java.awt.Color(255, 255, 255));
        btnEliminarCliente.setText("Eliminar");
        btnEliminarCliente.setBorder(null);
        btnEliminarCliente.setBorderPainted(false);
        btnEliminarCliente.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        btnEliminarCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarClienteActionPerformed(evt);
            }
        });
        pnlFondoCliente.add(btnEliminarCliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 430, 120, 40));

        btnLimpiarCliente.setBackground(new java.awt.Color(33, 50, 60));
        btnLimpiarCliente.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnLimpiarCliente.setForeground(new java.awt.Color(255, 255, 255));
        btnLimpiarCliente.setText("Limpiar");
        btnLimpiarCliente.setBorder(null);
        btnLimpiarCliente.setBorderPainted(false);
        btnLimpiarCliente.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        btnLimpiarCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimpiarClienteActionPerformed(evt);
            }
        });
        pnlFondoCliente.add(btnLimpiarCliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 430, 120, 40));

        lblFondoCliente.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/fondoCliente.png"))); // NOI18N
        lblFondoCliente.setMaximumSize(new java.awt.Dimension(870, 513));
        pnlFondoCliente.add(lblFondoCliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 960, -1));

        txtIdCliente.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtIdCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtIdClienteActionPerformed(evt);
            }
        });
        pnlFondoCliente.add(txtIdCliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 50, 40, 20));

        javax.swing.GroupLayout pnlClienteLayout = new javax.swing.GroupLayout(pnlCliente);
        pnlCliente.setLayout(pnlClienteLayout);
        pnlClienteLayout.setHorizontalGroup(
            pnlClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlClienteLayout.createSequentialGroup()
                .addGap(65, 65, 65)
                .addComponent(pnlFondoCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(54, Short.MAX_VALUE))
        );
        pnlClienteLayout.setVerticalGroup(
            pnlClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlClienteLayout.createSequentialGroup()
                .addGap(42, 42, 42)
                .addComponent(pnlFondoCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 511, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(46, Short.MAX_VALUE))
        );

        tabbedPane.addTab("<html><right>Clientes&nbsp;&nbsp;&nbsp;</right></html>", new javax.swing.ImageIcon(getClass().getResource("/Imagenes/iconEmpleado.png")), pnlCliente); // NOI18N

        jPanel1.add(tabbedPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 90, 1210, 600));
        tabbedPane.getAccessibleContext().setAccessibleName("<html><right>Clientes</right></html>");
        tabbedPane.getAccessibleContext().setAccessibleDescription("");

        iconLogoTienda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/logo2.png"))); // NOI18N
        jPanel1.add(iconLogoTienda, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 20, -1, 60));

        jPanel5.setOpaque(false);
        jPanel5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel1.add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 20, 170, 50));

        iconUsuarioLogueado.setBackground(new java.awt.Color(255, 255, 255));
        iconUsuarioLogueado.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/iconUserLog.png"))); // NOI18N
        jPanel1.add(iconUsuarioLogueado, new org.netbeans.lib.awtextra.AbsoluteConstraints(930, 20, -1, 50));

        lblUserName.setBackground(new java.awt.Color(18, 23, 28));
        lblUserName.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblUserName.setForeground(new java.awt.Color(255, 255, 255));
        lblUserName.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jPanel1.add(lblUserName, new org.netbeans.lib.awtextra.AbsoluteConstraints(970, 20, 110, 50));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/fondoUser.png"))); // NOI18N
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(920, 20, -1, -1));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 1270, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 710, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtIdEmpleadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtIdEmpleadoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtIdEmpleadoActionPerformed

    private void tablaEmpleadoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaEmpleadoMouseClicked
        
        int fila = tablaEmpleado.rowAtPoint(evt.getPoint());
        
        SimpleDateFormat formatoFecha = new SimpleDateFormat("yyy-MM-dd");
        Date fechatxt1, fechatxt2;
        
        txtIdEmpleado.setText(tablaEmpleado.getValueAt(fila, 0).toString());
        txtNombreEmpleado.setText(tablaEmpleado.getValueAt(fila,1).toString());
        txtApePaternoEmpleado.setText(tablaEmpleado.getValueAt(fila,2).toString());
        txtApeMaternoEmpleado.setText(tablaEmpleado.getValueAt(fila,3).toString());
        txtDocumentoEmpleado.setText(tablaEmpleado.getValueAt(fila,4).toString());
        txtCelularEmpleado.setText(tablaEmpleado.getValueAt(fila,5).toString());
        
        try{
            fechatxt1 = formatoFecha.parse(tablaEmpleado.getValueAt(fila,6).toString());
            dchFecNacimientoEmpleado.setDate(fechatxt1);
        }catch(ParseException ex){
            java.util.logging.Logger.getLogger(SistemaVista.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        
        try{
            fechatxt2 = formatoFecha.parse(tablaEmpleado.getValueAt(fila,7).toString());
            dchFecIngresoEmpleado.setDate(fechatxt2);
        }catch(ParseException ex){
            java.util.logging.Logger.getLogger(SistemaVista.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        
        //txtFecIngresoEmpleado.setText(tablaEmpleado.getValueAt(fila,7).toString());
    }//GEN-LAST:event_tablaEmpleadoMouseClicked

    private void tablaInventarioMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaInventarioMouseClicked

        int fila = tablaInventario.rowAtPoint(evt.getPoint());
       
        txtIdInventario.setText(tablaInventario.getValueAt(fila, 0).toString());
        txtCodigoInventario.setText(tablaInventario.getValueAt(fila, 1).toString());
        txtNombreInventario.setText(tablaInventario.getValueAt(fila, 2).toString());
        if (tablaInventario.getValueAt(fila, 3).toString().equals("Joya")){
            cbxCategoriaInventario.setSelectedIndex(0);
        }else{
            cbxCategoriaInventario.setSelectedIndex(1);
        }
        txtDescripcionInventario.setText(tablaInventario.getValueAt(fila, 4).toString());
        txtPrecioCostoInventario.setText(tablaInventario.getValueAt(fila, 5).toString());
        txtPrecioVentaInventario.setText(tablaInventario.getValueAt(fila, 6).toString());
        txtStockInventario.setValue(tablaInventario.getValueAt(fila, 7));
    }//GEN-LAST:event_tablaInventarioMouseClicked

    private void btnEliminarInventarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarInventarioActionPerformed
        if (!"".equals(txtIdInventario.getText())){
            int pregunta = JOptionPane.showConfirmDialog(null,"¿Seguro que quieres eliminar este producto?");
            if (pregunta == 0) {
                int id = Integer.parseInt(txtIdInventario.getText());
                proDao.eliminarProducto(id);
                limpiarTabla(modeloTablaInventario);
                limpiarInventario();
                listarProductos();
            }
        } else{
            JOptionPane.showMessageDialog(null, "Seleccione un producto");
        }
    }//GEN-LAST:event_btnEliminarInventarioActionPerformed

    private void btnActualizarInventarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnActualizarInventarioActionPerformed
        if ("".equals(txtIdInventario.getText())) {
            JOptionPane.showMessageDialog(null, "Seleccione un producto");
        } else{
            if (!"".equals(txtNombreInventario.getText()) && !"".equals(cbxCategoriaInventario.getSelectedItem())
                && !"".equals(txtCodigoInventario.getText()) && !"".equals(txtDescripcionInventario.getText())
                && !"".equals(txtPrecioCostoInventario.getText())){
                
                //VERIFICAR PRECIO COSTO SI ESTÁ BIEN ESCRITO EL FLOAT 
                try {
                   String input = txtPrecioCostoInventario.getText();
                   Float.parseFloat(input);
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null, "El precio costo del producto del inventario no está escrito correctamente. "
                            + "Recuerda usar '.'");
                    return;
                }
                
                //VERIFICAR PRECIO VENTA SI ESTÁ BIEN ESCRITO EL FLOAT 
                //VERIFICAR PRECIO VENTA SI ESTÁ BIEN ESCRITO EL FLOAT 
            String catProductoElegido=(String)cbxCategoriaInventario.getSelectedItem();
            if (catProductoElegido.equals("Joya")){
                try {
                   String input = txtPrecioVentaInventario.getText();
                   Float.parseFloat(input);
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null, "El precio de venta del producto no está escrito correctamente."
+ "\nRecuerde usar el punto decimal");
                    return;
                }
            }
                
                String nombre = txtNombreInventario.getText();
                String codigo = txtCodigoInventario.getText();
                int id = Integer.parseInt(txtIdInventario.getText());

                if(proDao.verificarCodNOmProducto(id, nombre, codigo)){
                    int pregunta = JOptionPane.showConfirmDialog(null,"¿Seguro que quieres modificar este producto?");
                    if (pregunta == 0) {
                        //VERIFICAR CODIGO UNICO
                        pro.setId_producto(Integer.parseInt(txtIdInventario.getText()));
                        pro.setNombre_producto(txtNombreInventario.getText());
                        pro.setCategoria_producto(cbxCategoriaInventario.getSelectedItem().toString());
                        pro.setCodigo_producto(txtCodigoInventario.getText());
                        pro.setDescripcion_producto(txtDescripcionInventario.getText());
                        pro.setStock_producto((Integer)txtStockInventario.getValue());
                        
                        Float pc = Float.parseFloat(txtPrecioCostoInventario.getText());
                        pro.setCosto_producto((float)(Math.round(pc * 10.0) / 10.0));
                        
                        Float pv = Float.parseFloat(txtPrecioVentaInventario.getText());
                        pro.setVenta_producto((float)(Math.round(pv * 10.0) / 10.0)); 
                        
                        if (cbxCategoriaInventario.getSelectedItem().toString().equals("Otro")){
                            pro.setVenta_producto(0);
                        }

                        proDao.modificarProducto(pro);
                        limpiarTabla(modeloTablaInventario);
                        limpiarInventario();
                        listarProductos();
                        JOptionPane.showMessageDialog(null, "ACTUALIZADO CON ÉXITO");                         
                    }
                }
            } else{
                JOptionPane.showMessageDialog(null, "Existen algunos campos vacíos");
            }
        }
    }//GEN-LAST:event_btnActualizarInventarioActionPerformed

    private void btnGuardarProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarProductoActionPerformed
        if ((Integer)txtStockProducto.getValue()==0) {
            JOptionPane.showMessageDialog(null, "El stock del nuevo producto no puede ser igual a 0");
            return;
        }
        
        if(!"".equals(txtNombreProducto.getText()) && !"".equals(cbxCategoriaProducto.getSelectedItem())
            && !"".equals(txtDescripcionProducto.getText()) && !"".equals(txtPrecioCompraProducto.getText())
            && !"0".equals(txtStockProducto.getValue()) && !"".equals(txtCodigoProducto.getText())
            && !"".equals(txtFotoProducto.getText())){
            
            if(proDao.verificarCodigoUnico(txtCodigoProducto.getText())){
                JOptionPane.showMessageDialog(null, "El código ingresado coincide con otro producto. Elija otro código");
                return;
            }
            try {
               String input = txtPrecioCompraProducto.getText();
               Float.parseFloat(input);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "El precio de compra del producto no está escrito correctamente."
                        + "\nRecuerde usar el punto decimal");
                return;
            }

            //VERIFICAR PRECIO VENTA SI ESTÁ BIEN ESCRITO EL FLOAT 
            String catProductoElegido=(String)cbxCategoriaProducto.getSelectedItem();
            if (catProductoElegido.equals("Joya")){
                try {
                   String input = txtPrecioVentaProducto.getText();
                   Float.parseFloat(input);
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null, "El precio de venta del producto no está escrito correctamente."
+ "\nRecuerde usar el punto decimal");
                    return;
                }
            }
            
            pro.setNombre_producto(txtNombreProducto.getText());
            pro.setCategoria_producto(cbxCategoriaProducto.getSelectedItem().toString());
            pro.setDescripcion_producto(txtDescripcionProducto.getText());
            pro.setCosto_producto(Float.parseFloat(txtPrecioCompraProducto.getText()));
            if (txtPrecioVentaProducto.getText().equals("")){
                pro.setVenta_producto(0);
            }else{
                pro.setVenta_producto(Float.parseFloat(txtPrecioVentaProducto.getText()));
            }
            pro.setStock_producto((Integer)txtStockProducto.getValue());
            pro.setCodigo_producto(txtCodigoProducto.getText());
            pro.setFoto_producto(getImagen(txtFotoProducto.getText()));
            proDao.registrarProducto(pro);

            limpiarProducto();
            JOptionPane.showMessageDialog(null,"Producto Registrado con éxito");
        } else{
            JOptionPane.showMessageDialog(null,"Existen campos vacios");
        }
    }//GEN-LAST:event_btnGuardarProductoActionPerformed

    private void btnLimpiarProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimpiarProductoActionPerformed
        limpiarProducto();
    }//GEN-LAST:event_btnLimpiarProductoActionPerformed

    private void btnGuardarEmpleadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarEmpleadoActionPerformed
        
        
        if(!"".equals(txtNombreEmpleado.getText()) && !"".equals(txtApePaternoEmpleado.getText())
                && !"".equals(txtApeMaternoEmpleado.getText()) && !"".equals(txtDocumentoEmpleado.getText())
                && !"".equals(txtCelularEmpleado.getText())){
            
            String numDocumento = txtDocumentoEmpleado.getText();
            String tipoDocumento = cbxDocumentoEmpleado.getSelectedItem().toString();
            int tamNumDocumento = numDocumento.length();

            if (tamNumDocumento == 0) {
                JOptionPane.showMessageDialog(null, "Debe llenar el número de "+tipoDocumento, "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }else if (tipoDocumento.equals("DNI") && tamNumDocumento < 8){
                JOptionPane.showMessageDialog(null, "El DNI ingresado no tiene 8 dígitos", "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }else if (tipoDocumento.equals("CE") && tamNumDocumento < 9){
                JOptionPane.showMessageDialog(null, "El CE ingresado no tiene 9 dígitos", "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            if(empDao.verificarDocumentoUnico(txtDocumentoEmpleado.getText())){
                JOptionPane.showMessageDialog(null, "El documento ingresado coincide con el de otro empleado."
                        +"\nIngrese otro documento");
                return;
            }
            
            //FORMATO FECHA
            Date inputNac = dchFecNacimientoEmpleado.getDate();
            if(inputNac == null){
                JOptionPane.showMessageDialog(null, "La fecha de nacimiento es un campo obligatorio."
                        +"\nVerificar el campo."
                        , "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }
            Date inputIng = dchFecIngresoEmpleado.getDate();
            if(inputIng == null){
                JOptionPane.showMessageDialog(null, "La fecha de ingreso es un campo obligatorio."
                        +"\nVerificar el campo."
                        , "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            if(dchFecNacimientoEmpleado.getDate().after(dchFecIngresoEmpleado.getDate())){
                JOptionPane.showMessageDialog(null, "La fecha de nacimiento debe ser anterior a la fecha de ingreso."
                        +"\nVerificar el campo."
                        , "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }
                    
            emp.setNombre_empleado(txtNombreEmpleado.getText());
            emp.setApe_paterno_empleado(txtApePaternoEmpleado.getText());
            emp.setApe_materno_empleado(txtApeMaternoEmpleado.getText());
            emp.setDocumento_empleado(txtDocumentoEmpleado.getText());
            emp.setCelular_empleado(Integer.parseInt(txtCelularEmpleado.getText()));
            
            Date fNacimiento = dchFecNacimientoEmpleado.getDate();
            long dn = fNacimiento.getTime();
            java.sql.Date fecNacimiento = new java.sql.Date(dn);  
            emp.setFec_naciminto_empleado(fecNacimiento.toString());
            
            Date fIngreso = dchFecIngresoEmpleado.getDate();
            long di = fIngreso.getTime();
            java.sql.Date fecIngreso = new java.sql.Date(di);
            emp.setFec_ingreso_empleado(fecIngreso.toString());
            
            empDao.registrarEmpleado(emp);
            
            limpiarTabla(modeloTablaEmpleado);
            limpiarEmpleado();
            listarEmpleados();
            JOptionPane.showMessageDialog(null,"Empleado Registrado con éxito");
            cargarComboEmpleados();
        } else{
            JOptionPane.showMessageDialog(null,"Existen campos vacios");
        }
    }//GEN-LAST:event_btnGuardarEmpleadoActionPerformed

    private void btnFotoProductoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnFotoProductoMouseClicked
        JFileChooser jFileChooser = new JFileChooser();
        jFileChooser.setMultiSelectionEnabled(false);
        FileNameExtensionFilter filtrado = new FileNameExtensionFilter("JPG & PNG","jpg","png");
        jFileChooser.setFileFilter(filtrado);
        String ruta = "";
        String nombre = ""; 
        int respuesta = jFileChooser.showOpenDialog(this);
        
        if(respuesta == JFileChooser.APPROVE_OPTION){
            ruta = jFileChooser.getSelectedFile().getAbsolutePath();
            //nombre = jFileChooser.getSelectedFile().getName();
            txtFotoProducto.setText(ruta);
        }
    }//GEN-LAST:event_btnFotoProductoMouseClicked

    private void lblImagenInventarioMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblImagenInventarioMouseClicked
        // TODO add your handling code here:
        String codigo = txtCodigoInventario.getText();

        int fila = tablaInventario.getSelectedRow();
        String codigoTabla = tablaInventario.getValueAt(fila, 1).toString();
        String codigoProducto = txtCodigoInventario.getText();

        if (!(codigoTabla).equals(codigoProducto)){
            JOptionPane.showMessageDialog(null, "De clic nuevamente en el producto");
            return;
        }

        if (!(codigo).equals("")){
            byte[] bytes = proDao.getImagenProducto(codigo);
            BufferedImage img = null;
            try{
                try{
                    img = ImageIO.read(new ByteArrayInputStream(bytes));
                }catch(IOException ex){
                    System.out.println(ex.getMessage());
                }

                ImageIcon icono = new ImageIcon(img);

                //JOptionPane.showMessageDialog(null,"",codigo, JOptionPane.YES_NO_OPTION,icono);
                int seleccion = JOptionPane.showOptionDialog(
                    null,
                    "", 
                    codigo,
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    icono,    // null para icono por defecto.
                    new Object[] { "Cambiar Imagen", "Cerrar"},
                    "Cambiar Imagen"// null para YES, NO y CANCEL
                );

                if(seleccion == 0){
                    JFileChooser jFileChooser = new JFileChooser();
                    jFileChooser.setMultiSelectionEnabled(false);
                    FileNameExtensionFilter filtrado = new FileNameExtensionFilter("JPG & PNG","jpg","png");
                    jFileChooser.setFileFilter(filtrado);
                    String ruta = "";
                    String nombre = ""; 
                    int respuesta = jFileChooser.showOpenDialog(this);

                    if(respuesta == JFileChooser.APPROVE_OPTION){
                        ruta = jFileChooser.getSelectedFile().getAbsolutePath();
                        int pregunta = JOptionPane.showConfirmDialog(null,"¿Deseas reemplazar la imagen?","Aviso",JOptionPane.YES_NO_OPTION);
                        if(pregunta == 0){
                            pro.setId_producto(Integer.parseInt(txtIdInventario.getText()));
                            pro.setFoto_producto(getImagen(ruta));
                            proDao.modificarFotoProducto(pro);
                            JOptionPane.showMessageDialog(null, "Imagen actualizada con exito");
                        }
                        //nombre = jFileChooser.getSelectedFile().getName();
                        //System.out.println(ruta);
                        //pro.setFoto_producto(getImagen(txtFotoProducto.getText()));
                    }

                }               

                if (seleccion != -1)
                System.out.println("seleccionada opcion " + (seleccion + 1));

            }catch(Exception ex){
                JOptionPane.showMessageDialog(null, "No tiene una imagen asignada");
            }
        }else {
            JOptionPane.showMessageDialog(null, "Seleccione un producto del inventario");
        }
    }//GEN-LAST:event_lblImagenInventarioMouseClicked

    private void btnActualizarEmpleadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnActualizarEmpleadoActionPerformed
        if ("".equals(txtIdEmpleado.getText())) {
            JOptionPane.showMessageDialog(null, "Seleccione un empleado");
        } else{
            if(!"".equals(txtNombreEmpleado.getText()) && !"".equals(txtApePaternoEmpleado.getText())
                    && !"".equals(txtApeMaternoEmpleado.getText()) && !"".equals(txtDocumentoEmpleado.getText())
                    && !"".equals(txtCelularEmpleado.getText())){

                String numDocumento = txtDocumentoEmpleado.getText();
                String tipoDocumento = cbxDocumentoEmpleado.getSelectedItem().toString();
                int tamNumDocumento = numDocumento.length();

                if (tamNumDocumento == 0) {
                    JOptionPane.showMessageDialog(null, "Debe llenar el número de "+tipoDocumento, "Advertencia", JOptionPane.WARNING_MESSAGE);
                    return;
                }else if (tipoDocumento.equals("DNI") && tamNumDocumento < 8){
                    JOptionPane.showMessageDialog(null, "El DNI ingresado no tiene 8 dígitos", "Advertencia", JOptionPane.WARNING_MESSAGE);
                    return;
                }else if (tipoDocumento.equals("CE") && tamNumDocumento < 9){
                    JOptionPane.showMessageDialog(null, "El CE ingresado no tiene 9 dígitos", "Advertencia", JOptionPane.WARNING_MESSAGE);
                    return;
                }else if(txtCelularEmpleado.getText().length()<9){
                    JOptionPane.showMessageDialog(null, "Verificar la cantidad de dígitos del celular");
                    return;
                }
                
                //FORMATO FECHA
                Date inputNac = dchFecNacimientoEmpleado.getDate();
                if(inputNac == null){
                    JOptionPane.showMessageDialog(null, "La fecha de nacimiento es un campo obligatorio."
                            +"\nVerificar el campo."
                            , "Advertencia", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                Date inputIng = dchFecIngresoEmpleado.getDate();
                
                if(inputIng == null){
                    JOptionPane.showMessageDialog(null, "La fecha de ingreso es un campo obligatorio."
                            +"\nVerificar el campo."
                            , "Advertencia", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                if(empDao.verificarActDocEmpleado(Integer.parseInt(txtIdEmpleado.getText()), txtDocumentoEmpleado.getText())){
                    JOptionPane.showMessageDialog(null, "El documento ingresado coincide con el de otro empleado."
                            +"\nVerifique el documento");
                    return;
                }
                int pregunta = JOptionPane.showConfirmDialog(null,"¿Seguro que quieres modificar este empleado?");
                if (pregunta == 0) {
                    
                    emp.setId_empleado(Integer.parseInt(txtIdEmpleado.getText()));
                    emp.setNombre_empleado(txtNombreEmpleado.getText());
                    emp.setApe_paterno_empleado(txtApePaternoEmpleado.getText());
                    emp.setApe_materno_empleado(txtApeMaternoEmpleado.getText());
                    emp.setDocumento_empleado(txtDocumentoEmpleado.getText());
                    emp.setCelular_empleado(Integer.parseInt(txtCelularEmpleado.getText()));
                    
                    Date fNacimiento = dchFecNacimientoEmpleado.getDate();
                    long dn = fNacimiento.getTime();
                    java.sql.Date fecNacimiento = new java.sql.Date(dn);  
                    emp.setFec_naciminto_empleado(fecNacimiento.toString());

                    Date fIngreso = dchFecIngresoEmpleado.getDate();
                    long di = fIngreso.getTime();
                    java.sql.Date fecIngreso = new java.sql.Date(di);
                    emp.setFec_ingreso_empleado(fecIngreso.toString());
                    
                    empDao.modificarEmpleado(emp);

                    limpiarTabla(modeloTablaEmpleado);
                    limpiarEmpleado();
                    listarEmpleados();
                    JOptionPane.showMessageDialog(null, "ACTUALIZADO CON EXITO");
                    cargarComboEmpleados();
                }
            } else{
                JOptionPane.showMessageDialog(null, "Existen algunos campos vacios");
            }
        }
    }//GEN-LAST:event_btnActualizarEmpleadoActionPerformed

    private void btnLimpiarEmpleadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimpiarEmpleadoActionPerformed
        limpiarEmpleado();
    }//GEN-LAST:event_btnLimpiarEmpleadoActionPerformed

    private void btnEliminarEmpleadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarEmpleadoActionPerformed
        if (!"".equals(txtIdEmpleado.getText())){
            int pregunta = JOptionPane.showConfirmDialog(null,"¿Seguro que quieres eliminar este empleado?");
            if (pregunta == 0) {
                int id = Integer.parseInt(txtIdEmpleado.getText());
                empDao.eliminarEmpleado(id);
                limpiarTabla(modeloTablaEmpleado);
                limpiarEmpleado();
                listarEmpleados();
                cargarComboEmpleados();
            }
        } else{
            JOptionPane.showMessageDialog(null, "Seleccione un empleado");
        }
    }//GEN-LAST:event_btnEliminarEmpleadoActionPerformed

    private void lblBotonBuscarReporteMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblBotonBuscarReporteMouseClicked
        //Verificación fecha 
        txtTotalReporte.setText("");
        Date inputIng = dchFechaReporte.getDate();
        if(inputIng == null){
            JOptionPane.showMessageDialog(null, "Verificar la fecha ingresada."
                    , "Advertencia", JOptionPane.WARNING_MESSAGE);
        } else {
            limpiarTabla(modeloTablaReporte);
            try {
                listarReportes();
            } catch (ParseException e) {
                System.out.println("Error: " + e);
            }
        }
    }//GEN-LAST:event_lblBotonBuscarReporteMouseClicked

    private void btnDescargaReporteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDescargaReporteActionPerformed
        int tablaC = tablaReporte.getRowCount();
        
        if (tablaC > 0) {
            modeloTablaReporte = (DefaultTableModel) tablaReporte.getModel();

            SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
            String fecha = formato.format(dchFechaReporte.getDate());

            ExportarReporteExcel excel = new ExportarReporteExcel();
            excel.reporte(modeloTablaReporte, fecha);
        
        }else{
            JOptionPane.showMessageDialog(null, "No hay datos en la tabla para "
                    + "exportar", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnDescargaReporteActionPerformed

    private void txtCelularEmpleadoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCelularEmpleadoKeyTyped
        
        //Solo ingreso de números
        int key = evt.getKeyChar();
        boolean numeros = key >= 48 && key <= 57;
        if (!numeros){
            evt.consume();
        }

        if(txtCelularEmpleado.getText().length() >= 9){
            evt.consume();
        }
    }//GEN-LAST:event_txtCelularEmpleadoKeyTyped

    private void txtDocumentoClienteVentaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDocumentoClienteVentaKeyTyped
        //Solo ingreso de números
        int key = evt.getKeyChar();
        boolean numeros = key >= 48 && key <= 57;
        if (!numeros){
            evt.consume();
        }
        
        //Caso DNI
        if(cbxDocumentoClienteVenta.getSelectedIndex()==0){
            if(txtDocumentoClienteVenta.getText().length() >= 8){
                evt.consume();
            }
        }
        //Caso Carnet de Extranjería
        else if(cbxDocumentoClienteVenta.getSelectedIndex()==1){
            if(txtDocumentoClienteVenta.getText().length() >= 9){
                evt.consume();
            }  
        }
    }//GEN-LAST:event_txtDocumentoClienteVentaKeyTyped

    private void txtDocumentoEmpleadoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDocumentoEmpleadoKeyTyped
        //Solo ingreso de números
        int key = evt.getKeyChar();
        boolean numeros = key >= 48 && key <= 57;
        if (!numeros){
            evt.consume();
        }
        
        //Caso DNI, max 8 números
        if(cbxDocumentoEmpleado.getSelectedIndex()==0){
            if(txtDocumentoEmpleado.getText().length() >= 8){
                evt.consume();
            }
        }
        //Caso Carnet de Extranjería
        else if(cbxDocumentoEmpleado.getSelectedIndex()==1){
            if(txtDocumentoEmpleado.getText().length() >= 9){
                evt.consume();
            }  
        }
    }//GEN-LAST:event_txtDocumentoEmpleadoKeyTyped

    private void txtNombreClienteVentaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNombreClienteVentaKeyTyped
        //Solo ingreso de letras
        int key = evt.getKeyChar();
        boolean mayusculas = key >= 65 && key <= 90;
        boolean minusculas = key >= 97 && key <= 122;

        if (!(minusculas || mayusculas)){
            evt.consume();
        }
    }//GEN-LAST:event_txtNombreClienteVentaKeyTyped

    private void txtNombreInventarioKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNombreInventarioKeyTyped
        if(txtNombreInventario.getText().length() >= 25){
            evt.consume();
        }
    }//GEN-LAST:event_txtNombreInventarioKeyTyped

    private void txtNombreProductoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNombreProductoKeyTyped
        if(txtNombreProducto.getText().length() >= 25){
            evt.consume();
        }
    }//GEN-LAST:event_txtNombreProductoKeyTyped

    private void txtNombreEmpleadoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNombreEmpleadoKeyTyped
        //Solo ingreso de letras
        int key = evt.getKeyChar();
        boolean mayusculas = key >= 65 && key <= 90;
        boolean minusculas = key >= 97 && key <= 122;

        if (!(minusculas || mayusculas)){
            evt.consume();
        }
        if(txtNombreEmpleado.getText().length() >= 30){
            evt.consume();
        }
    }//GEN-LAST:event_txtNombreEmpleadoKeyTyped

    private void txtApePaternoEmpleadoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtApePaternoEmpleadoKeyTyped
        //Solo ingreso de letras
        int key = evt.getKeyChar();
        boolean mayusculas = key >= 65 && key <= 90;
        boolean minusculas = key >= 97 && key <= 122;

        if (!(minusculas || mayusculas)){
            evt.consume();
        }
        if(txtApePaternoEmpleado.getText().length() >= 20){
            evt.consume();
        }
    }//GEN-LAST:event_txtApePaternoEmpleadoKeyTyped

    private void txtApeMaternoEmpleadoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtApeMaternoEmpleadoKeyTyped
        //Solo ingreso de letras
        int key = evt.getKeyChar();
        boolean mayusculas = key >= 65 && key <= 90;
        boolean minusculas = key >= 97 && key <= 122;

        if (!(minusculas || mayusculas)){
            evt.consume();
        }
        if(txtApeMaternoEmpleado.getText().length() >= 20){
            evt.consume();
        }
    }//GEN-LAST:event_txtApeMaternoEmpleadoKeyTyped

    private void dchFecNacimientoEmpleadoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_dchFecNacimientoEmpleadoKeyTyped
        int key = evt.getKeyChar();
        boolean numeros = key >= 48 && key <= 57;
        if (!(numeros)){
            evt.consume();
        }
        if(txtDocumentoClienteVenta.getText().length() >= 10){
            evt.consume();
        }
    }//GEN-LAST:event_dchFecNacimientoEmpleadoKeyTyped

    private void lblBotonBuscarClienteVentaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblBotonBuscarClienteVentaMouseClicked
        txtNombreClienteVenta.setText("");
        String numDocumento = txtDocumentoClienteVenta.getText();
        String tipoDocumento = cbxDocumentoClienteVenta.getSelectedItem().toString();
        int tamNumDocumento = numDocumento.length();
        
        if (tamNumDocumento == 0) {
            JOptionPane.showMessageDialog(null, "Debe llenar el número de "+tipoDocumento, "Advertencia", JOptionPane.WARNING_MESSAGE);
            txtNombreClienteVenta.setText("");
        }else if (tipoDocumento.equals("DNI") && tamNumDocumento < 8){
            JOptionPane.showMessageDialog(null, "El DNI ingresado no tiene 8 dígitos", "Advertencia", JOptionPane.WARNING_MESSAGE);
        }else if (tipoDocumento.equals("CE") && tamNumDocumento < 9){
            JOptionPane.showMessageDialog(null, "El CE ingresado no tiene 9 dígitos", "Advertencia", JOptionPane.WARNING_MESSAGE);
            txtNombreClienteVenta.setText("");
        }else{
            String numDocumento2 = numDocumento;
            llenarClientes(numDocumento2, tipoDocumento);
        }
    }//GEN-LAST:event_lblBotonBuscarClienteVentaMouseClicked

    private void btnAgregarItemVentaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnAgregarItemVentaMouseClicked
        
        if ((Integer)spnCantidadProductoCarrito.getValue()==0) {
            JOptionPane.showMessageDialog(null, "La cantidad del producto no uede ser 0");
            return;
        }
        
        String categoria = cbxCategoriaProductoCarrito.getSelectedItem().toString();
        
        if (categoria.equals("Tatuaje")) {
            if (txtDescripcionProductoCarrito.getText().equals("")) {
                JOptionPane.showMessageDialog(null, "Debe ingresar "
                        + "una descripción para el tatuaje");
                return;
            
            }
            if (txtPrecioProductoCarrito.getText().equals("")) {
                JOptionPane.showMessageDialog(null, "Debe ingresar "
                        + "una precio para el tatuaje");
                return;
            }
            //VERIFICAR SI ESTÁ BIEN ESCRITO EL FLOAT 
            try {
               String input = txtPrecioProductoCarrito.getText();
               Float.parseFloat(input);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "El precio del item no está escrito correctamente. "
                        + "Recuerda usar '.'");
                return;
            }
        }else if (categoria.equals("Joya")) {
            
            if ("".equals(txtCodigoProductoCarrito.getText())) {
                JOptionPane.showMessageDialog(null, "Debe ingresar el código de un producto");
                return;
            }else if ("".equals(txtNombreProductoCarrito.getText())) {
                JOptionPane.showMessageDialog(null, "Debe realizar la búsqueda del producto");
                return;
            }else if ((Integer)spnCantidadProductoCarrito.getValue()>Integer.parseInt(txtStockProductoCarrito.getText())) {
                JOptionPane.showMessageDialog(null, "No hay suficiente stock del item para la cantidad solicitada");
                return;
            } else if (!(proDao.verificarExisteIdCodigo(txtIdProductoCarrito.getText(),txtCodigoProductoCarrito.getText()))){
                JOptionPane.showMessageDialog(null, "Realice la búsqueda del producto.");
                return;
            } else{
                for (int i = 0; i < tablaCarritoVenta.getRowCount(); i++) {
                    if (txtCodigoProductoCarrito.getText().equals(tablaCarritoVenta.getValueAt(i,2).toString())) {
                        JOptionPane.showMessageDialog(null,"Ya agregaste este producto"
                                + "\nSe recomienda actualizar el del carrito");
                        return;
                    }
                }
            }
            
        }         
        
        int contador = modeloTablaVenta.getRowCount()+1;
        
        String codigo = txtCodigoProductoCarrito.getText();
        String nombreProd = txtNombreProductoCarrito.getText();
        String descripcion = txtDescripcionProductoCarrito.getText();
        int cantidad = (Integer) spnCantidadProductoCarrito.getValue();
        float precio = Float.valueOf(txtPrecioProductoCarrito.getText()); 
        
        float total = cantidad*precio;   
        
        float sumaTotal = 0;

        modeloTablaVenta = (DefaultTableModel) tablaCarritoVenta.getModel();
        Object[] obj = new Object[8];        
        obj[0] = contador;
        obj[1] = categoria;
        obj[2] = codigo;
        obj[3] = nombreProd;
        obj[4] = descripcion;
        obj[5] = cantidad;
        obj[6] = String.format("%.1f0", Math.round(precio * 10.0) / 10.0).replace(",",".");
        obj[7] = String.format("%.1f0", Math.round(total * 10.0) / 10.0).replace(",",".");        
        modeloTablaVenta.addRow((obj));
        tablaInventario.setModel(modeloTablaVenta);
        
        for (int i = 0; i < modeloTablaVenta.getRowCount(); i++) {
            sumaTotal += Float.valueOf(tablaCarritoVenta.getValueAt(i, 7).toString().replace(",","."));
        }
        String numeroFormateado = String.format("%.1f0", Math.round(sumaTotal * 10.0) / 10.0).replace(",",".");
        txtTotalVenta.setText(numeroFormateado);
        
        limpiarCarrito();
    }//GEN-LAST:event_btnAgregarItemVentaMouseClicked

    private void btnCancelarVentaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarVentaActionPerformed
        limpiarCarrito();
        limpiarVenta();
        limpiarTabla(modeloTablaVenta);
        txtTotalVenta.setText("");
    }//GEN-LAST:event_btnCancelarVentaActionPerformed

    private void cbxCategoriaProductoCarritoItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbxCategoriaProductoCarritoItemStateChanged
        String catProductoElegido=(String)cbxCategoriaProductoCarrito.getSelectedItem();
        if (catProductoElegido.equals("Joya") || catProductoElegido.equals("Otro")){
            
            txtNombreProductoCarrito.setFocusable(false);
            txtStockProductoCarrito.setFocusable(false);
            txtPrecioProductoCarrito.setFocusable(false);
            txtDescripcionProductoCarrito.setFocusable(false);
            spnCantidadProductoCarrito.setFocusable(true);  
            lblBotonBuscarProductoVenta.setFocusable(true);
            txtCodigoProductoCarrito.setFocusable(true);
            limpiarCarrito();
            
        }else if (catProductoElegido.equals("Tatuaje")){
            
            txtCodigoProductoCarrito.setFocusable(false);
            txtNombreProductoCarrito.setFocusable(false);
            txtStockProductoCarrito.setFocusable(false);
            spnCantidadProductoCarrito.setFocusable(false);
            txtPrecioProductoCarrito.setFocusable(true);
            txtDescripcionProductoCarrito.setFocusable(true);
            lblBotonBuscarProductoVenta.setFocusable(false);
            limpiarCarrito();
        }
    }//GEN-LAST:event_cbxCategoriaProductoCarritoItemStateChanged

    private void btnGenerarVentaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGenerarVentaActionPerformed
        
        if (txtDocumentoClienteVenta.getText().equals("") || txtNombreClienteVenta.equals("")) {
            JOptionPane.showMessageDialog(null, "Debe ingresar un cliente");
            txtDocumentoClienteVenta.grabFocus();
            return;
        }
        if (tablaCarritoVenta.getRowCount() == 0) {
            JOptionPane.showMessageDialog(null, "El carrito de compra está vacío");
            txtDocumentoClienteVenta.grabFocus();
            return;
        }
        
        registrarVenta();
        
        modeloTablaVenta = (DefaultTableModel) tablaCarritoVenta.getModel();
        ExportarVentaPDF pdf = new ExportarVentaPDF();
        int idVenta = ventDao.idVenta();
        pdf.reporteVenta(modeloTablaVenta, vent, idVenta);
        
        registrarDetalle();
        actualizarStock();//AQUÍ SE QUEDA, POR ESO NO BORRA TABLA
        limpiarTabla(modeloTablaVenta);
        limpiarVenta();
        txtDocumentoClienteVenta.setText("");
        txtNombreClienteVenta.setText("");
        txtDescripcionProductoCarrito.setText("");
        txtTotalVenta.setText("");
        
        LocalDate fechaActual = LocalDate.now();
        LocalDate fecha1LocalDate = dchFechaReporte.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        if (fecha1LocalDate.isEqual(fechaActual)){
            limpiarTabla(modeloTablaReporte);
            try {
                listarReportes();
            } catch (ParseException ex) {
                Logger.getLogger(SistemaVista.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        JOptionPane.showMessageDialog(null, "¡Venta realizada con éxito!");
        
    }//GEN-LAST:event_btnGenerarVentaActionPerformed

    private void lblBotonBuscarProductoVentaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblBotonBuscarProductoVentaMouseClicked
        String categoria = cbxCategoriaProductoCarrito.getSelectedItem().toString();
        
        if (categoria.equals("Joya") || categoria.equals("Otro")){
            String codigoProd = txtCodigoProductoCarrito.getText();

            if (txtCodigoProductoCarrito.getText().equals("")) {
                JOptionPane.showMessageDialog(null, "Debe ingresar un codigo de"
                        + " producto a buscar","ADVERTENCIA",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            Producto prodEscogido = proDao.productoEscogido(codigoProd,categoria);//try catch
            
            if(prodEscogido != null){
                String id = String.valueOf(prodEscogido.getId_producto());
                txtIdProductoCarrito.setText(id);
                categoria = prodEscogido.getCategoria_producto();
                String nombre = prodEscogido.getNombre_producto();
                String descripcion = prodEscogido.getDescripcion_producto();
                String stock = String.valueOf(prodEscogido.getStock_producto());
                String precioU = String.format("%.1f0", prodEscogido.getVenta_producto()).replace(",",".");

                txtNombreProductoCarrito.setText(nombre);
                txtStockProductoCarrito.setText(stock);
                txtPrecioProductoCarrito.setText(precioU);
                txtDescripcionProductoCarrito.setText(descripcion);
                
                //spnCantidadProductoCarrito.set
                
            }else {
                JOptionPane.showMessageDialog(null, "No se encontró producto");
                return;
            }
        }

        if (categoria.equals("Joyas")) {
            cbxCategoriaProductoCarrito.setSelectedIndex(0);
        }else if(categoria.equals("Tatuajes")){
            cbxCategoriaProductoCarrito.setSelectedIndex(1);
        }else if(categoria.equals("Otro")){
            cbxCategoriaProductoCarrito.setSelectedIndex(2);
        }
    }//GEN-LAST:event_lblBotonBuscarProductoVentaMouseClicked

    private void tablaCarritoVentaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaCarritoVentaMouseClicked
        
        limpiarCarrito();
        
        int fila = tablaCarritoVenta.rowAtPoint(evt.getPoint());
        
//        String categoria = cbxCategoriaProductoCarrito.getSelectedItem().toString();

        cbxCategoriaProductoCarrito.setSelectedItem(tablaCarritoVenta.getValueAt(fila, 1));
        txtCodigoProductoCarrito.setText((String) tablaCarritoVenta.getValueAt(fila, 2));
        txtNombreProductoCarrito.setText(String.valueOf(tablaCarritoVenta.getValueAt(fila, 3)));
        txtDescripcionProductoCarrito.setText(String.valueOf(tablaCarritoVenta.getValueAt(fila, 4)));
        
        //Hacer búsqueda y seteo de stock
        String codigo = tablaCarritoVenta.getValueAt(fila, 2).toString();
        int stock = proDao.obtenerStockProducto(codigo);
        txtStockProductoCarrito.setText(String.valueOf(stock));
               
        spnCantidadProductoCarrito.setValue(Integer.parseInt(tablaCarritoVenta.getValueAt(fila, 5).toString()));        
        txtPrecioProductoCarrito.setText(String.valueOf(tablaCarritoVenta.getValueAt(fila, 6)));
        
    }//GEN-LAST:event_tablaCarritoVentaMouseClicked

    private void btnActualizarVentaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnActualizarVentaActionPerformed
        if ((Integer)spnCantidadProductoCarrito.getValue()==0) {
            JOptionPane.showMessageDialog(null, "La cantidad del producto no puede ser 0");
            return;
        }
        String categoriaNuevo = cbxCategoriaProductoCarrito.getSelectedItem().toString();
        //CONSISTENCIAS TATUAJE
        if (categoriaNuevo.equals("Tatuaje")) {
            if (txtDescripcionProductoCarrito.getText().equals("")) {
                JOptionPane.showMessageDialog(null, "Debe ingresar "
                        + "una descripción para el tatuaje");
                return;
            
            }
             if (txtPrecioProductoCarrito.getText().equals("")) {
                JOptionPane.showMessageDialog(null, "Debe ingresar "
                        + "una precio para el tatuaje");
                return;
            }
             
            //VERIFICAR SI ESTÁ BIEN ESCRITO EL FLOAT 
            try {
               String input = txtPrecioProductoCarrito.getText();
               Float.parseFloat(input);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "El precio del tatuaje no está escrito correctamente. "
                        + "Recuerda usar '.'");
                return;
            }
        }     
        
        String codigoNuevo = txtCodigoProductoCarrito.getText();

        //CONSISTENCIAS JOYA
        if (categoriaNuevo.equals("Joya") || categoriaNuevo.equals("Otro")) {
            if (codigoNuevo.equals("")){
                JOptionPane.showMessageDialog(null, "Primero debe seleccionar un item del carrito", "Advertencia", JOptionPane.WARNING_MESSAGE);
                //txtCodigoProductoCarrito.grabFocus();
                return;
            }
            if ((Integer)spnCantidadProductoCarrito.getValue()>Integer.parseInt(txtStockProductoCarrito.getText())) {
            JOptionPane.showMessageDialog(null, "No hay suficiente stock del item para la cantidad solicitada");
            return;
            }
        }
        
        String cantidadNuevo = spnCantidadProductoCarrito.getValue().toString();
        String descripcionNuevo = txtDescripcionProductoCarrito.getText();
        String precioNuevo = txtPrecioProductoCarrito.getText();
        
        int fila = tablaCarritoVenta.getSelectedRow();
        String codigoItem = tablaCarritoVenta.getValueAt(fila, 2).toString();
        String codigoCarrito = txtCodigoProductoCarrito.getText();
        if (!(codigoItem).equals(codigoCarrito)){
            JOptionPane.showMessageDialog(null, "No es posible cambiar el código del item.");
            txtCodigoProductoCarrito.setText(codigoItem);
            return;
        }
        if (fila>=0) {
            //ESTABLECIENDO NUEVOS VALORES EN LA TABLA 
            tablaCarritoVenta.setValueAt(categoriaNuevo, fila, 1);
            tablaCarritoVenta.setValueAt(descripcionNuevo, fila, 4);
            tablaCarritoVenta.setValueAt(cantidadNuevo, fila, 5);
            tablaCarritoVenta.setValueAt(precioNuevo, fila, 6);

            //CALCULO DEL TOTAL DEL ITEM ACTUALIZADO
            float cantidad = Float.parseFloat(cantidadNuevo);
            float precio = Float.parseFloat(precioNuevo);
            float totalNuevo = cantidad*precio;
            String totalNuevoFormateado = String.format("%.1f0", Math.round(totalNuevo * 10.0) / 10.0).replace(",",".");
            float totalCarritoNuevo = 0;
            tablaCarritoVenta.setValueAt(totalNuevoFormateado, fila, 7);

            //CALCULO DEL TOTAL DEL CARRITO

            modeloTablaVenta = (DefaultTableModel) tablaCarritoVenta.getModel();
            tablaInventario.setModel(modeloTablaVenta);
            for (int i = 0; i < tablaCarritoVenta.getRowCount(); i++) {
                totalCarritoNuevo += Float.parseFloat(tablaCarritoVenta.getValueAt(i, 7).toString().replace(",","."));
            }
            
            String totalCarritoNuevoFormateado = String.format("%.1f0", Math.round(totalCarritoNuevo * 10.0) / 10.0).replace(",",".");
            txtTotalVenta.setText(totalCarritoNuevoFormateado);

            //PANEL CONFIRMACION ACTUALIZACION
            JOptionPane.showMessageDialog(null, "Datos actualizados con éxito", "Confirmación", JOptionPane.INFORMATION_MESSAGE);
            limpiarCarrito();
        }
    }//GEN-LAST:event_btnActualizarVentaActionPerformed

    private void btnLimpiarVentaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimpiarVentaActionPerformed
        limpiarCarrito();
    }//GEN-LAST:event_btnLimpiarVentaActionPerformed

    private void btnEliminarVentaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarVentaActionPerformed
        float totalFinal = 0;
        float totalFila = 0;
        
        int fila = tablaCarritoVenta.getSelectedRow();
        if (fila>=0) {
            
            //Pregunta con un JOption Pane
            int respuesta = JOptionPane.showConfirmDialog(null, "¿Está seguro de eliminar ese producto del carrito?", "Confirmar", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
            
            //Si confirma que desea eliminar la fila
            if (respuesta == JOptionPane.YES_OPTION) {
                totalFila = Float.parseFloat(tablaCarritoVenta.getValueAt(fila, 7).toString().replace(",","."));
                totalFinal = Float.parseFloat(txtTotalVenta.getText().replace(",",".")) - totalFila;
                
                String numeroFormateado = String.format("%.1f0", Math.round(totalFinal * 10.0) / 10.0);
                txtTotalVenta.setText(String.valueOf(numeroFormateado));
                
                modeloTablaVenta.removeRow(fila);
                
                limpiarCarrito();
                
                modeloTablaVenta = (DefaultTableModel) tablaCarritoVenta.getModel();
                enumerarItemsTabla(modeloTablaVenta);
            }            
            
        }else{
            JOptionPane.showMessageDialog(null, "Primero debe seleccionar en un item del carrito");
        }
    }//GEN-LAST:event_btnEliminarVentaActionPerformed

    private void txtPrecioProductoCarritoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPrecioProductoCarritoKeyTyped
        int key = evt.getKeyChar();
        boolean numeros = key >= 48 && key <= 57;
        boolean punto = key == 46;
        if (!(numeros || punto)){
            evt.consume();
        }
        if(txtPrecioProductoCarrito.getText().length() >= 9){
            evt.consume();
        }
    }//GEN-LAST:event_txtPrecioProductoCarritoKeyTyped

    private void tabbedPaneStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_tabbedPaneStateChanged
        switch (tabbedPane.getSelectedIndex()) {
            case 1:
                tablaInventario.setModel(modeloTablaInventario);
                limpiarTabla(modeloTablaInventario);
                limpiarInventario();
                listarProductos();
                break;
            case 2:
                break;
            case 3:
                limpiarTabla(modeloTablaEmpleado);
                listarEmpleados();
                break;
            case 4:
                break;
            case 5:
                limpiarTabla(modeloTablaCliente);
                listarClientes();
                break;
            default:
                break;
        }
    }//GEN-LAST:event_tabbedPaneStateChanged

    private void spnCantidadProductoCarritoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_spnCantidadProductoCarritoKeyTyped
        int key = evt.getKeyChar();
        boolean numeros = key >= 48 && key <= 57;
        if (!numeros){
            evt.consume();
        }
        
        if(cbxDocumentoClienteVenta.getSelectedIndex()==0){
            if(txtDocumentoClienteVenta.getText().length() >= 3){
                evt.consume();
            }
        }
    }//GEN-LAST:event_spnCantidadProductoCarritoKeyTyped

    private void btnDescargarInventarioMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnDescargarInventarioMouseClicked
        ExportarInventario excel = new ExportarInventario();
        try {
            excel.exportarInventarioCompleto();
        } catch (SQLException ex) {
            Logger.getLogger(SistemaVista.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnDescargarInventarioMouseClicked

    private void btnDescargarTablaInventarioMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnDescargarTablaInventarioMouseClicked
        modeloTablaInventario = (DefaultTableModel) tablaInventario.getModel();
        ExportarInventario excel = new ExportarInventario();
        excel.exportarTablaExcel(modeloTablaInventario);
    }//GEN-LAST:event_btnDescargarTablaInventarioMouseClicked

    private void lblBotonBuscarInventarioMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblBotonBuscarInventarioMouseClicked
        limpiarTabla(modeloTablaInventario);
        String atributo = Normalizer.normalize((String) cbxCriterioInventario.getSelectedItem(), Normalizer.Form.NFD)
                            .replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
        atributo = atributo + "_producto";
        atributo = atributo.toLowerCase(); 
        
        String valor = txtCriterioInventario.getText();
        List<Producto> listPro = proDao.filtrarProductos(atributo, valor);
        modeloTablaInventario = (DefaultTableModel) tablaInventario.getModel();
        Object[] obj = new Object[8];        
        
        for (int i = 0; i < listPro.size(); i++) {
            obj[0] = listPro.get(i).getId_producto();
            obj[2] = listPro.get(i).getNombre_producto();
            obj[3] = listPro.get(i).getCategoria_producto();
            obj[4] = listPro.get(i).getDescripcion_producto();
            obj[5] = String.format("%.1f0", Math.round(listPro.get(i).getCosto_producto() * 10.0) / 10.0).replace(",",".");
            obj[6] = String.format("%.1f0", Math.round(listPro.get(i).getVenta_producto() * 10.0) / 10.0).replace(",",".");
            obj[7] = listPro.get(i).getStock_producto();
            obj[1] = listPro.get(i).getCodigo_producto();
            modeloTablaInventario.addRow((obj));
        }
        tablaInventario.setModel(modeloTablaInventario);
        limpiarInventario();
    }//GEN-LAST:event_lblBotonBuscarInventarioMouseClicked

    private void txtPrecioCostoInventarioKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPrecioCostoInventarioKeyTyped
        int key = evt.getKeyChar();
        boolean numeros = key >= 48 && key <= 57;
        boolean punto = key == 46;
        if (!(numeros || punto)){
            evt.consume();
        }
        if(txtPrecioCostoInventario.getText().length() >= 9){
            evt.consume();
        }
    }//GEN-LAST:event_txtPrecioCostoInventarioKeyTyped

    private void txtPrecioVentaInventarioKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPrecioVentaInventarioKeyTyped
        int key = evt.getKeyChar();
        boolean numeros = key >= 48 && key <= 57;
        boolean punto = key == 46;
        if (!(numeros || punto)){
            evt.consume();
        }
        if(txtPrecioVentaInventario.getText().length() >= 9){
            evt.consume();
        }
    }//GEN-LAST:event_txtPrecioVentaInventarioKeyTyped

    private void txtPrecioCompraProductoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPrecioCompraProductoKeyTyped
        int key = evt.getKeyChar();
        boolean numeros = key >= 48 && key <= 57;
        boolean punto = key == 46;
        if (!(numeros || punto)){
            evt.consume();
        }
        if(txtPrecioCompraProducto.getText().length() >= 9){
            evt.consume();
        }
    }//GEN-LAST:event_txtPrecioCompraProductoKeyTyped

    private void txtPrecioVentaProductoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPrecioVentaProductoKeyTyped
        int key = evt.getKeyChar();
        boolean numeros = key >= 48 && key <= 57;
        boolean punto = key == 46;
        if (!(numeros || punto)){
            evt.consume();
        }
        if(txtPrecioVentaProducto.getText().length() >= 9){
            evt.consume();
        }
    }//GEN-LAST:event_txtPrecioVentaProductoKeyTyped

    private void btnSalirMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSalirMouseClicked
        int option = JOptionPane.showConfirmDialog(null, "¿Está seguro de que cerrar sesión?", "Confirmación de salida", JOptionPane.YES_NO_OPTION);
        if (option == JOptionPane.YES_OPTION) {
            PVT pvt = new PVT();
            dispose();
            pvt.main(new String[]{});
        }
        
    }//GEN-LAST:event_btnSalirMouseClicked

    private void btnSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalirActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnSalirActionPerformed

    private void cbxCategoriaProductoItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbxCategoriaProductoItemStateChanged
        // TODO add your handling code here:
        String catProductoElegido=(String)cbxCategoriaProducto.getSelectedItem();
        if (catProductoElegido.equals("Otro")){
            
            txtPrecioVentaProducto.setFocusable(false);
            
        }else if (catProductoElegido.equals("Joya")){
            
            txtPrecioVentaProducto.setFocusable(true);
        }
    }//GEN-LAST:event_cbxCategoriaProductoItemStateChanged

    private void cbxCategoriaInventarioItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbxCategoriaInventarioItemStateChanged
        // TODO add your handling code here:
        String catProductoElegido=(String)cbxCategoriaInventario.getSelectedItem();
        if (catProductoElegido.equals("Otro")){
            txtPrecioVentaInventario.setFocusable(false);
        }else if (catProductoElegido.equals("Joya")){
            txtPrecioVentaInventario.setFocusable(true);
        }
    }//GEN-LAST:event_cbxCategoriaInventarioItemStateChanged

    private void txtCodigoProductoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCodigoProductoKeyTyped
        if(txtCodigoProducto.getText().length() >= 10){
            evt.consume();
        }
    }//GEN-LAST:event_txtCodigoProductoKeyTyped

    private void txtCriterioInventarioKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCriterioInventarioKeyTyped
        // TODO add your handling code here:
        if(txtCriterioInventario.getText().length() >= 10){
            evt.consume();
        }

    }//GEN-LAST:event_txtCriterioInventarioKeyTyped

    private void txtCodigoInventarioKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCodigoInventarioKeyTyped
        if(txtCodigoInventario.getText().length() >= 10){
            evt.consume();
        }
    }//GEN-LAST:event_txtCodigoInventarioKeyTyped

    private void txtCodigoProductoCarritoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCodigoProductoCarritoKeyTyped
        // TODO add your handling code here:
        int key = evt.getKeyChar();
        boolean mayusculas = key >= 65 && key <= 90;
        if (mayusculas){
            evt.consume();
        }
        if(txtCodigoProductoCarrito.getText().length() >= 10){
            evt.consume();
        }
    }//GEN-LAST:event_txtCodigoProductoCarritoKeyTyped

    private void txtNombreClienteKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNombreClienteKeyTyped
        //Solo ingreso de letras
        int key = evt.getKeyChar();
        boolean mayusculas = key >= 65 && key <= 90;
        boolean minusculas = key >= 97 && key <= 122;

        if (!(minusculas || mayusculas)){
            evt.consume();
        }
        if(txtNombreCliente.getText().length() >= 30){
            evt.consume();
        }
    }//GEN-LAST:event_txtNombreClienteKeyTyped

    private void txtApeClienteKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtApeClienteKeyTyped
        //Solo ingreso de letras
        int key = evt.getKeyChar();
        boolean mayusculas = key >= 65 && key <= 90;
        boolean minusculas = key >= 97 && key <= 122;

        if (!(minusculas || mayusculas)){
            evt.consume();
        }
        if(txtApeCliente.getText().length() >= 30){
            evt.consume();
        }
    }//GEN-LAST:event_txtApeClienteKeyTyped

    private void txtDocumentoClienteKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDocumentoClienteKeyTyped
        //Solo ingreso de números
        int key = evt.getKeyChar();
        boolean numeros = key >= 48 && key <= 57;
        if (!numeros){
            evt.consume();
        }
        
        //Caso DNI, max 8 números
        if(cbxDocumentoCliente.getSelectedIndex()==0){
            if(txtDocumentoCliente.getText().length() >= 8){
                evt.consume();
            }
        }
        //Caso Carnet de Extranjería
        else if(cbxDocumentoCliente.getSelectedIndex()==1){
            if(txtDocumentoCliente.getText().length() >= 9){
                evt.consume();
            }  
        }
    }//GEN-LAST:event_txtDocumentoClienteKeyTyped

    private void txtCelularClienteKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCelularClienteKeyTyped
        //Solo ingreso de números
        int key = evt.getKeyChar();
        boolean numeros = key >= 48 && key <= 57;
        if (!numeros){
            evt.consume();
        }

        if(txtCelularCliente.getText().length() >= 9){
            evt.consume();
        }
    }//GEN-LAST:event_txtCelularClienteKeyTyped

    private void btnGuardarClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarClienteActionPerformed
        if(!"".equals(txtNombreCliente.getText()) && !"".equals(txtDocumentoCliente.getText())){
            
            String numDocumento = txtDocumentoCliente.getText();
            String tipoDocumento = cbxDocumentoCliente.getSelectedItem().toString();
            int tamNumDocumento = numDocumento.length();

            if (tamNumDocumento == 0) {
                JOptionPane.showMessageDialog(null, "Debe llenar el número de "+tipoDocumento, "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }else if (tipoDocumento.equals("DNI") && tamNumDocumento < 8){
                JOptionPane.showMessageDialog(null, "El DNI ingresado no tiene 8 dígitos", "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }else if (tipoDocumento.equals("CE") && tamNumDocumento < 9){
                JOptionPane.showMessageDialog(null, "El CE ingresado no tiene 9 dígitos", "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            if(cliDao.verificarDocumentoUnico(txtDocumentoCliente.getText())){
                JOptionPane.showMessageDialog(null, "El documento ingresado coincide con el de otro cliente."
                        +"\nIngrese otro documento");
                return;
            }
            
            cli.setDni(txtDocumentoCliente.getText());
            cli.setNombre(txtNombreCliente.getText());
            cli.setApellido(txtApeCliente.getText());
            if (!"".equals(txtCelularCliente.getText())){
                cli.setCelular(Long.parseLong(txtCelularCliente.getText()));
            } else {
                cli.setCelular(0);
            }
            
            cli.setInstagram(txtInstagramCliente.getText());
            
            cliDao.registrarCliente(cli);
            
            limpiarTabla(modeloTablaCliente);
            limpiarCliente();
            listarClientes();
            JOptionPane.showMessageDialog(null,"Cliente Registrado con éxito");
        
        } else{
            JOptionPane.showMessageDialog(null,"Existen campos vacios");
        }
    }//GEN-LAST:event_btnGuardarClienteActionPerformed

    private void btnActualizarClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnActualizarClienteActionPerformed
        if ("".equals(txtIdCliente.getText())) {
            JOptionPane.showMessageDialog(null, "Seleccione un cliente");
        } else{
            if(!"".equals(txtNombreCliente.getText()) && !"".equals(txtDocumentoCliente.getText())){

                String numDocumento = txtDocumentoCliente.getText();
                String tipoDocumento = cbxDocumentoCliente.getSelectedItem().toString();
                int tamNumDocumento = numDocumento.length();

                if (tamNumDocumento == 0) {
                    JOptionPane.showMessageDialog(null, "Debe llenar el número de "+tipoDocumento, "Advertencia", JOptionPane.WARNING_MESSAGE);
                    return;
                }else if (tipoDocumento.equals("DNI") && tamNumDocumento < 8){
                    JOptionPane.showMessageDialog(null, "El DNI ingresado no tiene 8 dígitos", "Advertencia", JOptionPane.WARNING_MESSAGE);
                    return;
                }else if (tipoDocumento.equals("CE") && tamNumDocumento < 9){
                    JOptionPane.showMessageDialog(null, "El CE ingresado no tiene 9 dígitos", "Advertencia", JOptionPane.WARNING_MESSAGE);
                    return;
                }else if(txtCelularCliente.getText().length()<9 && !txtCelularCliente.getText().equals("0")){
                    JOptionPane.showMessageDialog(null, "Verificar la cantidad de dígitos del celular");
                    return;
                }
                
                if(cliDao.verificarActDocCliente(Integer.parseInt(txtIdCliente.getText()), txtDocumentoCliente.getText())){
                    JOptionPane.showMessageDialog(null, "El documento ingresado coincide con el de otro cliente."
                            +"\nVerifique el documento");
                    return;
                }
                int pregunta = JOptionPane.showConfirmDialog(null,"¿Seguro que quieres modificar este cliente?");
                if (pregunta == 0) {
                    
                    cli.setId_cliente(Integer.parseInt(txtIdCliente.getText()));
                    cli.setDni(txtDocumentoCliente.getText());
                    cli.setNombre(txtNombreCliente.getText());
                    cli.setApellido(txtApeCliente.getText());
                    cli.setCelular(Long.parseLong(txtCelularCliente.getText()));
                    cli.setInstagram(txtInstagramCliente.getText());
                    
                    cliDao.modificarCliente(cli);

                    limpiarTabla(modeloTablaCliente);
                    limpiarCliente();
                    listarClientes();
                    JOptionPane.showMessageDialog(null, "ACTUALIZADO CON EXITO");
                }
            } else{
                JOptionPane.showMessageDialog(null, "Existen algunos campos vacios");
            }
        }
    }//GEN-LAST:event_btnActualizarClienteActionPerformed

    private void btnEliminarClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarClienteActionPerformed
        if (!"".equals(txtIdCliente.getText())){
            int pregunta = JOptionPane.showConfirmDialog(null,"¿Seguro que quieres eliminar este cliente?");
            if (pregunta == 0) {
                int id = Integer.parseInt(txtIdCliente.getText());
                cliDao.eliminarCliente(id);
                limpiarTabla(modeloTablaCliente);
                limpiarCliente();
                listarClientes();
            }
        } else{
            JOptionPane.showMessageDialog(null, "Seleccione un cliente");
        }
    }//GEN-LAST:event_btnEliminarClienteActionPerformed

    private void btnLimpiarClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimpiarClienteActionPerformed
        limpiarCliente();
    }//GEN-LAST:event_btnLimpiarClienteActionPerformed

    private void tablaClienteMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaClienteMouseClicked
        int fila = tablaCliente.rowAtPoint(evt.getPoint());
        
        txtIdCliente.setText(tablaCliente.getValueAt(fila, 0).toString());
        txtDocumentoCliente.setText(tablaCliente.getValueAt(fila,1).toString());
        txtNombreCliente.setText(tablaCliente.getValueAt(fila,2).toString());
        txtApeCliente.setText(tablaCliente.getValueAt(fila,3).toString());
        txtCelularCliente.setText(tablaCliente.getValueAt(fila,4).toString());
        txtInstagramCliente.setText(tablaCliente.getValueAt(fila,5).toString());
    }//GEN-LAST:event_tablaClienteMouseClicked

    private void txtIdClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtIdClienteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtIdClienteActionPerformed

    private void txtInstagramClienteKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtInstagramClienteKeyTyped
        if(txtInstagramCliente.getText().length() >= 30){
            evt.consume();
        }
    }//GEN-LAST:event_txtInstagramClienteKeyTyped

    private void txtCriterioClienteKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCriterioClienteKeyTyped
        if(txtCriterioInventario.getText().length() >= 30){
            evt.consume();
        }
    }//GEN-LAST:event_txtCriterioClienteKeyTyped

    private void lblBotonBuscarClienteMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblBotonBuscarClienteMouseClicked
        limpiarTabla(modeloTablaCliente);
        String atributo = Normalizer.normalize((String) cbxCriterioCliente.getSelectedItem(), Normalizer.Form.NFD);
        if(atributo.equals("#"))            atributo="id";
        if(atributo.equals("DNI/CE"))       atributo="dni";
        if(atributo.equals("Nombre(s)"))    atributo="nombre";
        if(atributo.equals("Apellido(s)"))  atributo="apellido";
        atributo = atributo.toLowerCase();
        atributo = atributo + "_cliente";
        
        String valor = txtCriterioCliente.getText();
        List<Cliente> listCli = cliDao.filtrarClientes(atributo, valor);
        modeloTablaCliente = (DefaultTableModel) tablaCliente.getModel();
        Object[] obj = new Object[6];        
        
        for (int i = 0; i < listCli.size(); i++) {
            obj[0] = listCli.get(i).getId_cliente();
            obj[1] = listCli.get(i).getDni();
            obj[2] = listCli.get(i).getNombre();
            obj[3] = listCli.get(i).getApellido();
            obj[4] = listCli.get(i).getCelular();
            obj[5] = listCli.get(i).getInstagram();
            
            modeloTablaCliente.addRow((obj));
        }
        tablaCliente.setModel(modeloTablaCliente);
        limpiarCliente();
    }//GEN-LAST:event_lblBotonBuscarClienteMouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(SistemaVista.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(SistemaVista.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(SistemaVista.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(SistemaVista.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new SistemaVista().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnActualizarCliente;
    private javax.swing.JButton btnActualizarEmpleado;
    private javax.swing.JButton btnActualizarInventario;
    private javax.swing.JButton btnActualizarVenta;
    private javax.swing.JButton btnAgregarItemVenta;
    private javax.swing.JButton btnCancelarVenta;
    private javax.swing.JButton btnDescargaReporte;
    private javax.swing.JButton btnDescargarInventario;
    private javax.swing.JButton btnDescargarTablaInventario;
    private javax.swing.JButton btnEliminarCliente;
    private javax.swing.JButton btnEliminarEmpleado;
    private javax.swing.JButton btnEliminarInventario;
    private javax.swing.JButton btnEliminarVenta;
    private javax.swing.JLabel btnFotoProducto;
    private javax.swing.JButton btnGenerarVenta;
    private javax.swing.JButton btnGuardarCliente;
    private javax.swing.JButton btnGuardarEmpleado;
    private javax.swing.JButton btnGuardarProducto;
    private javax.swing.JButton btnLimpiarCliente;
    private javax.swing.JButton btnLimpiarEmpleado;
    private javax.swing.JButton btnLimpiarProducto;
    private javax.swing.JButton btnLimpiarVenta;
    private javax.swing.JButton btnSalir;
    private javax.swing.JComboBox<String> cbxCategoriaInventario;
    private javax.swing.JComboBox<String> cbxCategoriaProducto;
    private javax.swing.JComboBox<String> cbxCategoriaProductoCarrito;
    private javax.swing.JComboBox<String> cbxCriterioCliente;
    private javax.swing.JComboBox<String> cbxCriterioInventario;
    private javax.swing.JComboBox<String> cbxDocumentoCliente;
    private javax.swing.JComboBox<String> cbxDocumentoClienteVenta;
    private javax.swing.JComboBox<String> cbxDocumentoEmpleado;
    private javax.swing.JComboBox<String> cbxEmpleadoVenta;
    private com.toedter.calendar.JDateChooser dchFecIngresoEmpleado;
    private com.toedter.calendar.JDateChooser dchFecNacimientoEmpleado;
    private com.toedter.calendar.JDateChooser dchFechaReporte;
    private javax.swing.JLabel iconLogoTienda;
    private javax.swing.JLabel iconUsuarioLogueado;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JLabel lblApeCliente;
    private javax.swing.JLabel lblApeMaternoEmpleado;
    private javax.swing.JLabel lblApePaternoEmpleado;
    private javax.swing.JLabel lblBotonBuscarCliente;
    private javax.swing.JLabel lblBotonBuscarClienteVenta;
    private javax.swing.JLabel lblBotonBuscarInventario;
    private javax.swing.JLabel lblBotonBuscarProductoVenta;
    private javax.swing.JLabel lblBotonBuscarReporte;
    private javax.swing.JLabel lblBuscarCliente;
    private javax.swing.JLabel lblBuscarInventario;
    private javax.swing.JLabel lblCantidadCarrito;
    private javax.swing.JLabel lblCategoriaCarrito;
    private javax.swing.JLabel lblCategoriaProducto;
    private javax.swing.JLabel lblCelularCliente;
    private javax.swing.JLabel lblCelularEmpleado;
    private javax.swing.JLabel lblCodigoCarrito;
    private javax.swing.JLabel lblCodigoCarrito3;
    private javax.swing.JLabel lblCodigoProducto;
    private javax.swing.JLabel lblDescripcionCarrito;
    private javax.swing.JLabel lblDescripcionInventario;
    private javax.swing.JLabel lblDescripcionProducto;
    private javax.swing.JLabel lblDocumentoCliente;
    private javax.swing.JLabel lblDocumentoEmpleado;
    private javax.swing.JLabel lblFecIngresoEmpleado;
    private javax.swing.JLabel lblFecNacimientoEmpleado;
    private javax.swing.JLabel lblFondoCliente;
    private javax.swing.JLabel lblFondoEmpleado;
    private javax.swing.JLabel lblFondoProducto;
    private javax.swing.JLabel lblFondoReporte;
    private javax.swing.JLabel lblFondoTitulo;
    private javax.swing.JLabel lblFondoTituloProducto;
    private javax.swing.JLabel lblFotoProducto;
    private javax.swing.JLabel lblIconSolReporte;
    private javax.swing.JLabel lblImagenInventario;
    private javax.swing.JLabel lblInstaCliente;
    private javax.swing.JLabel lblNombreCarrito;
    private javax.swing.JLabel lblNombreCarrito1;
    private javax.swing.JLabel lblNombreCarrito2;
    private javax.swing.JLabel lblNombreCliente;
    private javax.swing.JLabel lblNombreEmpleado;
    private javax.swing.JLabel lblNombreProducto;
    private javax.swing.JLabel lblPrecioCarrito;
    private javax.swing.JLabel lblPrecioCarrito1;
    private javax.swing.JLabel lblPrecioCompraProducto;
    private javax.swing.JLabel lblPrecioCostoInventario;
    private javax.swing.JLabel lblPrecioReporte;
    private javax.swing.JLabel lblPrecioVentaInventario;
    private javax.swing.JLabel lblPrecioVentaProducto;
    private javax.swing.JLabel lblSelFechaReporte;
    private javax.swing.JLabel lblStockCarrito;
    private javax.swing.JLabel lblStockInventario;
    private javax.swing.JLabel lblStockInventario1;
    private javax.swing.JLabel lblStockProducto;
    private javax.swing.JLabel lblTituloCliente;
    private javax.swing.JLabel lblTituloEmpleado;
    private javax.swing.JLabel lblTituloInventario;
    private javax.swing.JLabel lblTituloProducto;
    private javax.swing.JLabel lblTituloReporte;
    private javax.swing.JLabel lblTituloVenta;
    private javax.swing.JLabel lblUserName;
    private javax.swing.JPanel pnlCliente;
    private javax.swing.JPanel pnlEmpleado;
    private javax.swing.JPanel pnlFondoCliente;
    private javax.swing.JPanel pnlFondoEmpleado;
    private javax.swing.JPanel pnlFondoInventario;
    private javax.swing.JPanel pnlFondoProducto;
    private javax.swing.JPanel pnlFondoReporte;
    private javax.swing.JPanel pnlFondoTituloInventario;
    private javax.swing.JPanel pnlFondoTituloVenta;
    private javax.swing.JPanel pnlFondoVenta;
    private javax.swing.JPanel pnlIconSolReporte;
    private javax.swing.JPanel pnlImagenInventario;
    private javax.swing.JPanel pnlInventario;
    private javax.swing.JPanel pnlProducto;
    private javax.swing.JPanel pnlReporte;
    private javax.swing.JPanel pnlSOlPrecioCostoInventario;
    private javax.swing.JPanel pnlTituloCliente;
    private javax.swing.JPanel pnlTituloEmpleado;
    private javax.swing.JPanel pnlTituloProducto;
    private javax.swing.JPanel pnlTituloReporte;
    private javax.swing.JPanel pnlVenta;
    private javax.swing.JSpinner spnCantidadProductoCarrito;
    private javax.swing.JTabbedPane tabbedPane;
    private javax.swing.JTable tablaCarritoVenta;
    private javax.swing.JTable tablaCliente;
    private javax.swing.JTable tablaEmpleado;
    private javax.swing.JTable tablaInventario;
    private javax.swing.JTable tablaReporte;
    private javax.swing.JTextField txtApeCliente;
    private javax.swing.JTextField txtApeMaternoEmpleado;
    private javax.swing.JTextField txtApePaternoEmpleado;
    private javax.swing.JTextField txtCelularCliente;
    private javax.swing.JTextField txtCelularEmpleado;
    private javax.swing.JTextField txtCodigoInventario;
    private javax.swing.JTextField txtCodigoProducto;
    private javax.swing.JTextField txtCodigoProductoCarrito;
    private javax.swing.JTextField txtCriterioCliente;
    private javax.swing.JTextField txtCriterioInventario;
    private javax.swing.JTextArea txtDescripcionInventario;
    private javax.swing.JTextArea txtDescripcionProducto;
    private javax.swing.JTextArea txtDescripcionProductoCarrito;
    private javax.swing.JTextField txtDocumentoCliente;
    private javax.swing.JTextField txtDocumentoClienteVenta;
    private javax.swing.JTextField txtDocumentoEmpleado;
    private javax.swing.JTextField txtFotoProducto;
    private javax.swing.JTextField txtIdCliente;
    private javax.swing.JTextField txtIdEmpleado;
    private javax.swing.JTextField txtIdInventario;
    private javax.swing.JTextField txtIdProductoCarrito;
    private javax.swing.JTextField txtInstagramCliente;
    private javax.swing.JTextField txtNombreCliente;
    private javax.swing.JTextField txtNombreClienteVenta;
    private javax.swing.JTextField txtNombreEmpleado;
    private javax.swing.JTextField txtNombreInventario;
    private javax.swing.JTextField txtNombreProducto;
    private javax.swing.JTextField txtNombreProductoCarrito;
    private javax.swing.JTextField txtPrecioCompraProducto;
    private javax.swing.JTextField txtPrecioCostoInventario;
    private javax.swing.JTextField txtPrecioProductoCarrito;
    private javax.swing.JTextField txtPrecioVentaInventario;
    private javax.swing.JTextField txtPrecioVentaProducto;
    private javax.swing.JSpinner txtStockInventario;
    private javax.swing.JSpinner txtStockProducto;
    private javax.swing.JTextField txtStockProductoCarrito;
    private javax.swing.JTextField txtTotalReporte;
    private javax.swing.JTextField txtTotalVenta;
    // End of variables declaration//GEN-END:variables
}