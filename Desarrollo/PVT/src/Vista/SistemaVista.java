
package Vista;

import Modelo.Cliente;
import Modelo.ClienteDao;
import Modelo.DetalleVenta;
import Modelo.Empleado;
import Modelo.EmpleadoDao;
import Modelo.Producto;
import Modelo.ProductoDao;
import Modelo.Venta;
import Modelo.VentaDao;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class SistemaVista extends javax.swing.JFrame {

    Empleado emp = new Empleado();
    EmpleadoDao empDao = new EmpleadoDao();
    Producto pro = new Producto();
    ProductoDao proDao = new ProductoDao();
    Cliente cli = new Cliente();
    ClienteDao cliDao = new ClienteDao();
    Venta vent = new Venta();
    VentaDao ventDao = new VentaDao();
    DetalleVenta detalleVenta = new DetalleVenta();
    
    DefaultTableModel modelo = new DefaultTableModel();
    
    public SistemaVista() {
        System.out.println(getClass().getResource(""));
        initComponents();
        llenarEmpleados(); //Usar método al inicializar programa
        txtIdEmpleado.setVisible(false);
        txtIdInventario.setVisible(false);
        proDao.seleccionarCategoriaVenta(cbxCategoriaProducto);
        proDao.seleccionarCategoriaInventario(cbxCategoriaInventario);
        setLocationRelativeTo(null); //centrado
        //setResizable(false); //para no maximizar interfaz
        this.setTitle("PUNTO DE VENTA TATOOS");
    }
    public void listarEmpleados(){
        List<Empleado> listEmp = empDao.listarEmpleados();
        modelo = (DefaultTableModel) tablaEmpleado.getModel();
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
            
            modelo.addRow((obj));
        }
        tablaEmpleado.setModel(modelo);
    }
    public void listarProductos(){
        List<Producto> listPro = proDao.listarProductos();
        modelo = (DefaultTableModel) tablaProducto.getModel();
        Object[] obj = new Object[8];
        for (int i = 0; i < listPro.size(); i++) {
            obj[0] = listPro.get(i).getId_producto();
            obj[1] = listPro.get(i).getNombre_producto();
            obj[2] = listPro.get(i).getCategoria_producto();
            obj[3] = listPro.get(i).getDescripcion_producto();
            obj[4] = listPro.get(i).getCosto_producto();
            obj[5] = listPro.get(i).getVenta_producto();
            obj[6] = listPro.get(i).getStock_producto();
            obj[7] = listPro.get(i).getCodigo_producto();
            modelo.addRow((obj));
        }
        tablaProducto.setModel(modelo);
    }
    
    
    
    public void limpiarTabla(){
        for (int i = 0; i < modelo.getRowCount(); i++) {
            modelo.removeRow(i);
            i = i-1;
        }
    }
    
    //Generar Venta
    //Llenar el combobux Empleados
    private void llenarEmpleados(){
        List<Empleado> listaEmpleados = empDao.listarEmpleados();
        cboEmpleados.removeAllItems();
        for (int i = 0; i < listaEmpleados.size(); i++) {
            cboEmpleados.addItem(listaEmpleados.get(i).getNombre_empleado());
        }
    }
    
    //Llenar datos del cliente
    private void llenarClientes(int dni){
        
        String nombre = cliDao.clienteEscogido(dni);
        
        if ((nombre == null) || (nombre == "")) {
            
            JOptionPane.showMessageDialog(null, "Cliente no encontrado. "
                    + "Se procederá a ingresar nuevo cliente.", "Advertencia",
                    JOptionPane.WARNING_MESSAGE);
            
            int dniCli = Integer.parseInt(JOptionPane.showInputDialog(null, 
                    "Ingrese DNI del "
                    + "nuevo cliente: ", "NUEVO INGRESO", 
                    JOptionPane.INFORMATION_MESSAGE));
            
            String nombreCli = JOptionPane.showInputDialog(null, "Ingrese "
                    + "nombre:", "NUEVO INGRESO", 
                    JOptionPane.INFORMATION_MESSAGE);
            
            cli.setDni(dniCli);
            cli.setNombre(nombreCli);
            
            cliDao.registrarCliente(cli);
            
            txtNombreCliente2.setText(String.valueOf(dniCli));
            
            JOptionPane.showMessageDialog(null, "¡Cliente "+nombreCli
                    +" ingresado con éxito!","ÉXITO", 
                    JOptionPane.INFORMATION_MESSAGE);
            
            
        }else{
            txtNombreCliente.setText(nombre);
        }
        
        
        
    }
    
    private void registrarVenta(){
        int dni = Integer.parseInt(txtNombreCliente2.getText());
        String cliente = txtNombreCliente.getText();
        String empleado = cboEmpleados.getSelectedItem().toString();
        String descripcion = txtDescripcionCarrito.getText();
        
        /*float precioUnitario = Float.valueOf(txtPrecioCarrito.getText());
        float cantidad = (Float) txtCantidadCarrito.getValue();*/
        
        float monto = Float.valueOf(txtPrecioCarrito1.getText());
        
        
        
        vent.setDni(dni);
        vent.setNombre(cliente);
        vent.setEmpleado(empleado); 
        vent.setDescripcion(registrarDetalle());
        
        
        vent.setTotal(monto);
        
        ventDao.RegistrarVenta(vent);
    }
    
    private String registrarDetalle(){
        
        DecimalFormat df = new DecimalFormat("#.00");
        
        String detalleF = "";
        //String detalleT;
        //int id = ventDao.IdVenta();
        for (int i = 0; i < tablaProducto1.getRowCount(); i++) {            
            String codigoProducto = tablaProducto1.getValueAt(i, 2).toString();            
            int cantidadProducto = Integer.parseInt(tablaProducto1.getValueAt(i, 4).toString());
            String nombreProd = tablaProducto1.getValueAt(i, 1).toString();
            float precioU = Float.valueOf(tablaProducto1.getValueAt(i, 5).toString());
            
            detalleVenta.setNombre(nombreProd);
            detalleVenta.setCantidad(cantidadProducto);
            detalleVenta.setPrecio(precioU);
            
            if (i>0 || i==tablaProducto1.getRowCount()-1) {
                detalleF+="; ";
            }
            
            detalleF += cantidadProducto+"-"+nombreProd+"-"+df.format(precioU);
            
            
            
            //ventDao.RegistrarDetalleVenta(detalleVenta);
        }
        return detalleF;
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        buttonCustom1 = new button.ButtonCustom();
        tabbedPane = new javax.swing.JTabbedPane();
        pnlVenta = new javax.swing.JPanel();
        pnlFondoVenta = new javax.swing.JPanel();
        pnlFondoTituloVenta = new javax.swing.JPanel();
        lblTituloVenta = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tablaProducto1 = new javax.swing.JTable();
        jPanel16 = new javax.swing.JPanel();
        lblCategoriaCarrito = new javax.swing.JLabel();
        cbxCategoriaVenta = new javax.swing.JComboBox<>();
        lblCodigoCarrito = new javax.swing.JLabel();
        txtCodigoCarrito = new javax.swing.JTextField();
        jPanel13 = new javax.swing.JPanel();
        jLabel35 = new javax.swing.JLabel();
        lblNombreCarrito = new javax.swing.JLabel();
        txtNombreCarrito = new javax.swing.JTextField();
        lblStockCarrito = new javax.swing.JLabel();
        lblCantidadCarrito = new javax.swing.JLabel();
        txtCantidadCarrito = new javax.swing.JSpinner();
        lblPrecioCarrito = new javax.swing.JLabel();
        jPanel18 = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        txtPrecioCarrito = new javax.swing.JTextField();
        lblDescripcionCarrito = new javax.swing.JLabel();
        jScrollPane6 = new javax.swing.JScrollPane();
        txtDescripcionCarrito = new javax.swing.JTextArea();
        botonAgregarCarrito = new button.ButtonCustom();
        botonActualizarCarrito = new button.ButtonCustom();
        buttonEliminarCarrito = new button.ButtonCustom();
        botonLimpiarCarrito1 = new button.ButtonCustom();
        txtStockCarrito = new javax.swing.JTextField();
        botonGenerarVenta = new button.ButtonCustom();
        lblPrecioCarrito1 = new javax.swing.JLabel();
        jPanel19 = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        txtPrecioCarrito1 = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        lblNombreCarrito1 = new javax.swing.JLabel();
        cboEmpleados = new javax.swing.JComboBox<>();
        jPanel6 = new javax.swing.JPanel();
        cbxDocumentoCliente2 = new javax.swing.JComboBox<>();
        txtNombreCliente2 = new javax.swing.JTextField();
        jPanel21 = new javax.swing.JPanel();
        jLabel37 = new javax.swing.JLabel();
        lblNombreCarrito2 = new javax.swing.JLabel();
        txtNombreCliente = new javax.swing.JTextField();
        lblCodigoCarrito3 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        pnlInventario = new javax.swing.JPanel();
        pnlFondoInventario = new javax.swing.JPanel();
        pnlFondoTituloInventario = new javax.swing.JPanel();
        lblTituloInventario = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        botonDescargarInventario = new button.ButtonCustom();
        lblBuscarInventario = new javax.swing.JLabel();
        cbxCriterioInventario = new javax.swing.JComboBox<>();
        txtCriterioInventario = new javax.swing.JTextField();
        buscarInventario = new button.ButtonCustom();
        lblOrdenarInventario = new javax.swing.JLabel();
        cbxOrdenarInventario = new javax.swing.JComboBox<>();
        jScrollPane3 = new javax.swing.JScrollPane();
        tablaProducto = new javax.swing.JTable();
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
        botonEliminarProductoInventario = new button.ButtonCustom();
        botonActualizarProductoInventario = new button.ButtonCustom();
        lblStockInventario1 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
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
        jLabel32 = new javax.swing.JLabel();
        botonLimpiarProducto = new button.ButtonCustom();
        botonGuardarProducto = new button.ButtonCustom();
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
        txtFecNacimientoEmpleado = new javax.swing.JTextField();
        jPanel11 = new javax.swing.JPanel();
        jLabel33 = new javax.swing.JLabel();
        lblFecIngresoEmpleado = new javax.swing.JLabel();
        txtFecIngresoEmpleado = new javax.swing.JTextField();
        jPanel15 = new javax.swing.JPanel();
        lblIconCalendarioEmpleado = new javax.swing.JLabel();
        botonActualizarEmpleado = new button.ButtonCustom();
        botonEliminarEmpleado = new button.ButtonCustom();
        botonLimpiarEmpleado = new button.ButtonCustom();
        botonGuardarEmpleado = new button.ButtonCustom();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaEmpleado = new javax.swing.JTable();
        txtIdEmpleado = new javax.swing.JTextField();
        lblFondoEmpleado = new javax.swing.JLabel();
        pnlReporte = new javax.swing.JPanel();
        pnlFondoReporte = new javax.swing.JPanel();
        pnlTituloReporte = new javax.swing.JPanel();
        lblTituloReporte = new javax.swing.JLabel();
        lblFondoTitulo = new javax.swing.JLabel();
        botonGuardarEmpleado1 = new button.ButtonCustom();
        lblSelFechaReporte = new javax.swing.JLabel();
        txtFecReporte = new javax.swing.JTextField();
        pnlIconCalendarioReporte = new javax.swing.JPanel();
        lblIconCalendarioReporte = new javax.swing.JLabel();
        jScrollPane7 = new javax.swing.JScrollPane();
        tablaReporte = new javax.swing.JTable();
        lblPrecioReporte = new javax.swing.JLabel();
        pnlIconSolReporte = new javax.swing.JPanel();
        lblIconSolReporte = new javax.swing.JLabel();
        txtTotalReporte = new javax.swing.JTextField();
        lblFondoReporte = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(1270, 750));

        jPanel1.setBackground(new java.awt.Color(67, 102, 129));
        jPanel1.setMinimumSize(new java.awt.Dimension(1250, 750));
        jPanel1.setPreferredSize(new java.awt.Dimension(1435, 750));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel3.setBackground(new java.awt.Color(67, 102, 129));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/logo2.png"))); // NOI18N

        buttonCustom1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/iconSalir.png"))); // NOI18N
        buttonCustom1.setText("Salir");
        buttonCustom1.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        buttonCustom1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        buttonCustom1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonCustom1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(buttonCustom1, javax.swing.GroupLayout.DEFAULT_SIZE, 96, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(buttonCustom1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jPanel1.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(1050, 10, 190, 70));

        tabbedPane.setBackground(new java.awt.Color(67, 102, 129));
        tabbedPane.setTabPlacement(javax.swing.JTabbedPane.LEFT);
        tabbedPane.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        tabbedPane.setName(""); // NOI18N
        tabbedPane.setOpaque(true);
        tabbedPane.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabbedPaneMouseClicked(evt);
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
        lblTituloVenta.setText("Venta");
        pnlFondoTituloVenta.add(lblTituloVenta, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, 160, 43));

        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/fondoTitulos.png"))); // NOI18N
        pnlFondoTituloVenta.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 180, -1));

        pnlFondoVenta.add(pnlFondoTituloVenta, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 11, -1, -1));

        tablaProducto1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "#", "Categoría", "Código", "Producto", "Cantidad", "Precio Unitario", "Total"
            }
        ));
        tablaProducto1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablaProducto1MouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(tablaProducto1);
        if (tablaProducto1.getColumnModel().getColumnCount() > 0) {
            tablaProducto1.getColumnModel().getColumn(0).setPreferredWidth(3);
            tablaProducto1.getColumnModel().getColumn(1).setPreferredWidth(20);
            tablaProducto1.getColumnModel().getColumn(2).setPreferredWidth(5);
            tablaProducto1.getColumnModel().getColumn(3).setPreferredWidth(20);
            tablaProducto1.getColumnModel().getColumn(4).setPreferredWidth(5);
            tablaProducto1.getColumnModel().getColumn(5).setPreferredWidth(10);
            tablaProducto1.getColumnModel().getColumn(6).setPreferredWidth(10);
        }

        pnlFondoVenta.add(jScrollPane4, new org.netbeans.lib.awtextra.AbsoluteConstraints(31, 394, 813, 119));

        jPanel16.setBackground(new java.awt.Color(18, 23, 28));
        jPanel16.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)), "Carrito", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Tahoma", 0, 12), new java.awt.Color(153, 153, 153))); // NOI18N
        jPanel16.setOpaque(false);

        lblCategoriaCarrito.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblCategoriaCarrito.setForeground(new java.awt.Color(255, 255, 255));
        lblCategoriaCarrito.setText("Categoria");

        cbxCategoriaVenta.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Joya", "Tatuaje" }));
        cbxCategoriaVenta.setToolTipText("");
        cbxCategoriaVenta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxCategoriaVentaActionPerformed(evt);
            }
        });

        lblCodigoCarrito.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblCodigoCarrito.setForeground(new java.awt.Color(255, 255, 255));
        lblCodigoCarrito.setText("Codigo");

        txtCodigoCarrito.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtCodigoCarrito.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 5));
        txtCodigoCarrito.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCodigoCarritoActionPerformed(evt);
            }
        });

        jPanel13.setBackground(new java.awt.Color(67, 102, 129));
        jPanel13.setAlignmentX(0.0F);

        jLabel35.setBackground(new java.awt.Color(67, 102, 129));
        jLabel35.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel35.setForeground(new java.awt.Color(255, 255, 255));
        jLabel35.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel35.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/iconBuscar.png"))); // NOI18N
        jLabel35.setToolTipText("");
        jLabel35.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jLabel35.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jLabel35MousePressed(evt);
            }
        });

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel13Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel35))
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel13Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel35))
        );

        lblNombreCarrito.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblNombreCarrito.setForeground(new java.awt.Color(255, 255, 255));
        lblNombreCarrito.setText("Nombre");

        txtNombreCarrito.setEditable(false);
        txtNombreCarrito.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 5));

        lblStockCarrito.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblStockCarrito.setForeground(new java.awt.Color(255, 255, 255));
        lblStockCarrito.setText("Stock");

        lblCantidadCarrito.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblCantidadCarrito.setForeground(new java.awt.Color(255, 255, 255));
        lblCantidadCarrito.setText("Cantidad");

        txtCantidadCarrito.setModel(new javax.swing.SpinnerNumberModel(1, 0, null, 1));
        txtCantidadCarrito.setAutoscrolls(true);
        txtCantidadCarrito.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        txtCantidadCarrito.setEditor(new javax.swing.JSpinner.NumberEditor(txtCantidadCarrito, ""));
        txtCantidadCarrito.setFocusable(false);

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

        txtPrecioCarrito.setEditable(false);
        txtPrecioCarrito.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtPrecioCarrito.setToolTipText("Ingresar nombre(s)");
        txtPrecioCarrito.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 5));

        lblDescripcionCarrito.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblDescripcionCarrito.setForeground(new java.awt.Color(255, 255, 255));
        lblDescripcionCarrito.setText("Descripción");

        jScrollPane6.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 5));

        txtDescripcionCarrito.setColumns(20);
        txtDescripcionCarrito.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtDescripcionCarrito.setRows(5);
        txtDescripcionCarrito.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 5));
        jScrollPane6.setViewportView(txtDescripcionCarrito);

        botonAgregarCarrito.setText("Agregar a carrito");
        botonAgregarCarrito.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        botonAgregarCarrito.setStyle(button.ButtonCustom.ButtonStyle.SECONDARY);
        botonAgregarCarrito.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonAgregarCarritoActionPerformed(evt);
            }
        });

        botonActualizarCarrito.setText("Actualizar");
        botonActualizarCarrito.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        botonActualizarCarrito.setStyle(button.ButtonCustom.ButtonStyle.SECONDARY);
        botonActualizarCarrito.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                botonActualizarCarritoMousePressed(evt);
            }
        });
        botonActualizarCarrito.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonActualizarCarritoActionPerformed(evt);
            }
        });

        buttonEliminarCarrito.setText("Eliminar");
        buttonEliminarCarrito.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        buttonEliminarCarrito.setStyle(button.ButtonCustom.ButtonStyle.SECONDARY);
        buttonEliminarCarrito.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonEliminarCarritoActionPerformed(evt);
            }
        });

        botonLimpiarCarrito1.setText("Limpiar");
        botonLimpiarCarrito1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        botonLimpiarCarrito1.setStyle(button.ButtonCustom.ButtonStyle.SECONDARY);
        botonLimpiarCarrito1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonLimpiarCarrito1ActionPerformed(evt);
            }
        });

        txtStockCarrito.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtStockCarrito.setToolTipText("Ingresar nombre(s)");
        txtStockCarrito.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(240, 240, 240), 5));
        txtStockCarrito.setEnabled(false);
        txtStockCarrito.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtStockCarritoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(jPanel16Layout.createSequentialGroup()
                        .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(cbxCategoriaVenta, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblCategoriaCarrito, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(24, 24, 24)
                        .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel16Layout.createSequentialGroup()
                                .addComponent(txtCodigoCarrito, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, 0)
                                .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(lblCodigoCarrito, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel16Layout.createSequentialGroup()
                        .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblStockCarrito, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtStockCarrito, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(51, 51, 51)
                        .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtCantidadCarrito, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblCantidadCarrito, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)))
                .addGap(28, 28, 28)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtNombreCarrito, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblNombreCarrito, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel16Layout.createSequentialGroup()
                        .addComponent(jPanel18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(txtPrecioCarrito, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(lblPrecioCarrito, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblDescripcionCarrito, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 318, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(19, 19, 19))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel16Layout.createSequentialGroup()
                .addGap(60, 60, 60)
                .addComponent(botonActualizarCarrito, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26)
                .addComponent(buttonEliminarCarrito, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 31, Short.MAX_VALUE)
                .addComponent(botonLimpiarCarrito1, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26)
                .addComponent(botonAgregarCarrito, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(60, 60, 60))
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
                                .addComponent(cbxCategoriaVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(jPanel16Layout.createSequentialGroup()
                                    .addComponent(lblNombreCarrito)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(txtNombreCarrito, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel16Layout.createSequentialGroup()
                                    .addComponent(lblCodigoCarrito)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(txtCodigoCarrito, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                        .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel16Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(lblStockCarrito)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(jPanel16Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 9, Short.MAX_VALUE)
                                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtStockCarrito, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel16Layout.createSequentialGroup()
                                        .addComponent(lblPrecioCarrito)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jPanel18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(txtPrecioCarrito, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(lblCantidadCarrito)
                                        .addGroup(jPanel16Layout.createSequentialGroup()
                                            .addGap(21, 21, 21)
                                            .addComponent(txtCantidadCarrito, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(botonAgregarCarrito, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(botonActualizarCarrito, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(buttonEliminarCarrito, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(botonLimpiarCarrito1, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(14, 14, 14))))))
        );

        pnlFondoVenta.add(jPanel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(31, 168, -1, -1));

        botonGenerarVenta.setText("Generar venta");
        botonGenerarVenta.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        botonGenerarVenta.setStyle(button.ButtonCustom.ButtonStyle.SECONDARY);
        botonGenerarVenta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonGenerarVentaActionPerformed(evt);
            }
        });
        pnlFondoVenta.add(botonGenerarVenta, new org.netbeans.lib.awtextra.AbsoluteConstraints(351, 524, 150, 37));

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

        txtPrecioCarrito1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtPrecioCarrito1.setToolTipText("Ingresar nombre(s)");
        txtPrecioCarrito1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(240, 240, 240), 5));
        txtPrecioCarrito1.setEnabled(false);
        pnlFondoVenta.add(txtPrecioCarrito1, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 530, 110, 30));

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)), "Empleado", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Tahoma", 0, 12), new java.awt.Color(153, 153, 153))); // NOI18N
        jPanel4.setOpaque(false);

        lblNombreCarrito1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblNombreCarrito1.setForeground(new java.awt.Color(255, 255, 255));
        lblNombreCarrito1.setText("Nombre");

        cboEmpleados.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cboEmpleados.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboEmpleadosActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblNombreCarrito1, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboEmpleados, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(21, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblNombreCarrito1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cboEmpleados, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pnlFondoVenta.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(614, 62, -1, -1));

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)), "Cliente", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Tahoma", 0, 12), new java.awt.Color(153, 153, 153))); // NOI18N
        jPanel6.setOpaque(false);

        cbxDocumentoCliente2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "DNI", "CE" }));
        cbxDocumentoCliente2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxDocumentoCliente2ActionPerformed(evt);
            }
        });

        txtNombreCliente2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtNombreCliente2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 5));
        txtNombreCliente2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNombreCliente2ActionPerformed(evt);
            }
        });
        txtNombreCliente2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtNombreCliente2KeyTyped(evt);
            }
        });

        jPanel21.setBackground(new java.awt.Color(67, 102, 129));
        jPanel21.setAlignmentX(0.0F);

        jLabel37.setBackground(new java.awt.Color(67, 102, 129));
        jLabel37.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel37.setForeground(new java.awt.Color(255, 255, 255));
        jLabel37.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel37.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/iconBuscar.png"))); // NOI18N
        jLabel37.setToolTipText("");
        jLabel37.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jLabel37.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jLabel37MousePressed(evt);
            }
        });

        javax.swing.GroupLayout jPanel21Layout = new javax.swing.GroupLayout(jPanel21);
        jPanel21.setLayout(jPanel21Layout);
        jPanel21Layout.setHorizontalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel21Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel37))
        );
        jPanel21Layout.setVerticalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel21Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel37))
        );

        lblNombreCarrito2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblNombreCarrito2.setForeground(new java.awt.Color(255, 255, 255));
        lblNombreCarrito2.setText("Nombre");

        txtNombreCliente.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 5));
        txtNombreCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNombreClienteActionPerformed(evt);
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
                        .addComponent(cbxDocumentoCliente2, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txtNombreCliente2, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jPanel21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(36, 36, 36))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addComponent(lblCodigoCarrito3, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtNombreCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
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
                        .addComponent(txtNombreCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(5, 5, 5))
                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(cbxDocumentoCliente2, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(txtNombreCliente2, javax.swing.GroupLayout.Alignment.LEADING)
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
                .addContainerGap(82, Short.MAX_VALUE))
        );
        pnlVentaLayout.setVerticalGroup(
            pnlVentaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlVentaLayout.createSequentialGroup()
                .addGap(13, 13, 13)
                .addComponent(pnlFondoVenta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tabbedPane.addTab("<html><center> Generar <p> venta </center></html>", new javax.swing.ImageIcon(getClass().getResource("/Imagenes/iconCarrito.png")), pnlVenta, ""); // NOI18N
        pnlVenta.getAccessibleContext().setAccessibleDescription("");

        pnlInventario.setBackground(new java.awt.Color(33, 50, 60));
        pnlInventario.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                pnlInventarioMouseClicked(evt);
            }
        });

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

        botonDescargarInventario.setText("Descargar Inventario");
        botonDescargarInventario.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        botonDescargarInventario.setStyle(button.ButtonCustom.ButtonStyle.SECONDARY);
        pnlFondoInventario.add(botonDescargarInventario, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 90, 180, 37));

        lblBuscarInventario.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblBuscarInventario.setForeground(new java.awt.Color(255, 255, 255));
        lblBuscarInventario.setText("Buscar por");
        pnlFondoInventario.add(lblBuscarInventario, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 70, -1, -1));

        cbxCriterioInventario.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        cbxCriterioInventario.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "ID", "Nombre", "Categoría", "Código" }));
        cbxCriterioInventario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxCriterioInventarioActionPerformed(evt);
            }
        });
        pnlFondoInventario.add(cbxCriterioInventario, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 90, 120, 35));

        txtCriterioInventario.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtCriterioInventario.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 5));
        txtCriterioInventario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCriterioInventarioActionPerformed(evt);
            }
        });
        pnlFondoInventario.add(txtCriterioInventario, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 90, 180, 30));

        buscarInventario.setText("Buscar");
        buscarInventario.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        buscarInventario.setStyle(button.ButtonCustom.ButtonStyle.SECONDARY);
        pnlFondoInventario.add(buscarInventario, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 90, 120, 37));

        lblOrdenarInventario.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblOrdenarInventario.setForeground(new java.awt.Color(255, 255, 255));
        lblOrdenarInventario.setText("Ordenar por");
        pnlFondoInventario.add(lblOrdenarInventario, new org.netbeans.lib.awtextra.AbsoluteConstraints(900, 70, -1, -1));

        cbxOrdenarInventario.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        cbxOrdenarInventario.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "ID", "Nombre", "Categoría", "Código" }));
        pnlFondoInventario.add(cbxOrdenarInventario, new org.netbeans.lib.awtextra.AbsoluteConstraints(900, 90, 120, 35));

        tablaProducto.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Código", "Nombre", "Categoría", "Descripción", "Costo", "Venta", "Stock"
            }
        ));
        tablaProducto.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablaProductoMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(tablaProducto);
        if (tablaProducto.getColumnModel().getColumnCount() > 0) {
            tablaProducto.getColumnModel().getColumn(0).setPreferredWidth(50);
            tablaProducto.getColumnModel().getColumn(2).setPreferredWidth(100);
            tablaProducto.getColumnModel().getColumn(3).setPreferredWidth(100);
            tablaProducto.getColumnModel().getColumn(4).setPreferredWidth(200);
        }

        pnlFondoInventario.add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 150, 520, 330));

        jPanel14.setBackground(new java.awt.Color(18, 23, 28));
        jPanel14.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)), "Producto", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 12), new java.awt.Color(153, 153, 153))); // NOI18N

        jLabel24.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel24.setForeground(new java.awt.Color(255, 255, 255));
        jLabel24.setText("Nombre");

        txtNombreInventario.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 5));

        jLabel25.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel25.setForeground(new java.awt.Color(255, 255, 255));
        jLabel25.setText("Categoría");

        cbxCategoriaInventario.setToolTipText("");
        cbxCategoriaInventario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxCategoriaInventarioActionPerformed(evt);
            }
        });

        jLabel26.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel26.setForeground(new java.awt.Color(255, 255, 255));
        jLabel26.setText("Codigo");

        txtCodigoInventario.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 5));

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
        txtPrecioCostoInventario.setToolTipText("Ingresar nombre(s)");
        txtPrecioCostoInventario.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 5));

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
        txtPrecioVentaInventario.setToolTipText("Ingresar nombre(s)");
        txtPrecioVentaInventario.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 5));

        lblStockInventario.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblStockInventario.setForeground(new java.awt.Color(255, 255, 255));
        lblStockInventario.setText("Stock");

        txtStockInventario.setModel(new javax.swing.SpinnerNumberModel(1, 0, null, 1));
        txtStockInventario.setAutoscrolls(true);
        txtStockInventario.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        txtStockInventario.setEditor(new javax.swing.JSpinner.NumberEditor(txtStockInventario, ""));
        txtStockInventario.setFocusable(false);

        botonEliminarProductoInventario.setText("Eliminar");
        botonEliminarProductoInventario.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        botonEliminarProductoInventario.setStyle(button.ButtonCustom.ButtonStyle.SECONDARY);
        botonEliminarProductoInventario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonEliminarProductoInventarioActionPerformed(evt);
            }
        });

        botonActualizarProductoInventario.setText("Actualizar");
        botonActualizarProductoInventario.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        botonActualizarProductoInventario.setStyle(button.ButtonCustom.ButtonStyle.SECONDARY);
        botonActualizarProductoInventario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonActualizarProductoInventarioActionPerformed(evt);
            }
        });

        lblStockInventario1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblStockInventario1.setForeground(new java.awt.Color(255, 255, 255));
        lblStockInventario1.setText("Ver imagen");

        jPanel5.setBackground(new java.awt.Color(67, 102, 129));

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/iconImagen.png"))); // NOI18N
        jLabel2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(jLabel2)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jLabel2)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        txtDescripcionInventario.setColumns(20);
        txtDescripcionInventario.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtDescripcionInventario.setRows(5);
        txtDescripcionInventario.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 5));
        jScrollPane8.setViewportView(txtDescripcionInventario);

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel14Layout.createSequentialGroup()
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
                                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel14Layout.createSequentialGroup()
                                .addComponent(botonEliminarProductoInventario, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(botonActualizarProductoInventario, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)))
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
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(botonActualizarProductoInventario, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(botonEliminarProductoInventario, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                .addContainerGap(49, Short.MAX_VALUE))
        );

        tabbedPane.addTab("<html><center>Ver<p>inventario</center></html>", new javax.swing.ImageIcon(getClass().getResource("/Imagenes/iconInventario.png")), pnlInventario); // NOI18N

        pnlProducto.setBackground(new java.awt.Color(33, 50, 60));
        pnlProducto.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                pnlProductoMouseClicked(evt);
            }
        });

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
        lblTituloProducto.setText("Agregar Producto");
        pnlTituloProducto.add(lblTituloProducto, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 180, 43));

        lblFondoTituloProducto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/fondoTitulos.png"))); // NOI18N
        pnlTituloProducto.add(lblFondoTituloProducto, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        pnlFondoProducto.add(pnlTituloProducto, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 20, 180, 43));

        lblNombreProducto.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblNombreProducto.setForeground(new java.awt.Color(255, 255, 255));
        lblNombreProducto.setText("Nombre*");
        pnlFondoProducto.add(lblNombreProducto, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 90, -1, -1));

        txtNombreProducto.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtNombreProducto.setToolTipText("Ingresar nombre(s)");
        txtNombreProducto.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 5));
        pnlFondoProducto.add(txtNombreProducto, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 110, 180, 30));

        lblCategoriaProducto.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblCategoriaProducto.setForeground(new java.awt.Color(255, 255, 255));
        lblCategoriaProducto.setText("Categoría*");
        pnlFondoProducto.add(lblCategoriaProducto, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 90, -1, -1));

        cbxCategoriaProducto.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        cbxCategoriaProducto.setBorder(null);
        cbxCategoriaProducto.setLightWeightPopupEnabled(false);
        cbxCategoriaProducto.setRequestFocusEnabled(false);
        cbxCategoriaProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxCategoriaProductoActionPerformed(evt);
            }
        });
        pnlFondoProducto.add(cbxCategoriaProducto, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 110, 180, 35));

        lblDescripcionProducto.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblDescripcionProducto.setForeground(new java.awt.Color(255, 255, 255));
        lblDescripcionProducto.setText("Descripción");
        pnlFondoProducto.add(lblDescripcionProducto, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 160, -1, -1));

        txtDescripcionProducto.setColumns(20);
        txtDescripcionProducto.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtDescripcionProducto.setRows(5);
        txtDescripcionProducto.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 5));
        jScrollPane2.setViewportView(txtDescripcionProducto);

        pnlFondoProducto.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 180, 400, 80));

        lblPrecioCompraProducto.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblPrecioCompraProducto.setForeground(new java.awt.Color(255, 255, 255));
        lblPrecioCompraProducto.setText("Precio de compra");
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
        txtPrecioCompraProducto.setToolTipText("Ingresar nombre(s)");
        txtPrecioCompraProducto.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 5));
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
        txtPrecioVentaProducto.setToolTipText("Ingresar nombre(s)");
        txtPrecioVentaProducto.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 5));
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
        lblCodigoProducto.setText("Código");
        pnlFondoProducto.add(lblCodigoProducto, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 350, -1, -1));

        txtCodigoProducto.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtCodigoProducto.setToolTipText("Ingresar nombre(s)");
        txtCodigoProducto.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 5));
        pnlFondoProducto.add(txtCodigoProducto, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 370, 180, 30));

        lblFotoProducto.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblFotoProducto.setForeground(new java.awt.Color(255, 255, 255));
        lblFotoProducto.setText("Foto");
        pnlFondoProducto.add(lblFotoProducto, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 350, -1, -1));

        txtFotoProducto.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtFotoProducto.setToolTipText("Ingresar nombre(s)");
        txtFotoProducto.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 5));
        pnlFondoProducto.add(txtFotoProducto, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 370, 80, 30));

        jPanel12.setBackground(new java.awt.Color(67, 102, 129));
        jPanel12.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        jLabel32.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/iconUpload.png"))); // NOI18N

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel12Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel32, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addComponent(jLabel32, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pnlFondoProducto.add(jPanel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 370, 30, 30));

        botonLimpiarProducto.setText("Limpiar");
        botonLimpiarProducto.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        botonLimpiarProducto.setStyle(button.ButtonCustom.ButtonStyle.SECONDARY);
        botonLimpiarProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonLimpiarProductoActionPerformed(evt);
            }
        });
        pnlFondoProducto.add(botonLimpiarProducto, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 430, 180, 37));

        botonGuardarProducto.setText("Guardar");
        botonGuardarProducto.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        botonGuardarProducto.setStyle(button.ButtonCustom.ButtonStyle.SECONDARY);
        botonGuardarProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonGuardarProductoActionPerformed(evt);
            }
        });
        pnlFondoProducto.add(botonGuardarProducto, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 430, 180, 37));

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
                .addContainerGap(49, Short.MAX_VALUE))
        );

        tabbedPane.addTab("<html><center>Agregar<p>producto</center></html>", new javax.swing.ImageIcon(getClass().getResource("/Imagenes/iconProducto.png")), pnlProducto); // NOI18N

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
        lblTituloEmpleado.setText("Agregar Empleado");
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
        txtNombreEmpleado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNombreEmpleadoActionPerformed(evt);
            }
        });
        pnlFondoEmpleado.add(txtNombreEmpleado, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 110, 180, 30));

        lblApePaternoEmpleado.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblApePaternoEmpleado.setForeground(new java.awt.Color(255, 255, 255));
        lblApePaternoEmpleado.setText("Apellido Paterno*");
        pnlFondoEmpleado.add(lblApePaternoEmpleado, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 90, -1, -1));

        txtApePaternoEmpleado.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtApePaternoEmpleado.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 5));
        pnlFondoEmpleado.add(txtApePaternoEmpleado, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 110, 180, 30));

        lblApeMaternoEmpleado.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblApeMaternoEmpleado.setForeground(new java.awt.Color(255, 255, 255));
        lblApeMaternoEmpleado.setText("Apellido Materno");
        pnlFondoEmpleado.add(lblApeMaternoEmpleado, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 90, -1, -1));

        txtApeMaternoEmpleado.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtApeMaternoEmpleado.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 5));
        pnlFondoEmpleado.add(txtApeMaternoEmpleado, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 110, 180, 30));

        lblDocumentoEmpleado.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblDocumentoEmpleado.setForeground(new java.awt.Color(255, 255, 255));
        lblDocumentoEmpleado.setText("Documento de Identidad*");
        pnlFondoEmpleado.add(lblDocumentoEmpleado, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 160, -1, -1));

        cbxDocumentoEmpleado.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "DNI", "CE" }));
        pnlFondoEmpleado.add(cbxDocumentoEmpleado, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 180, 55, 35));

        txtDocumentoEmpleado.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtDocumentoEmpleado.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 5));
        txtDocumentoEmpleado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDocumentoEmpleadoActionPerformed(evt);
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
        pnlFondoEmpleado.add(txtCelularEmpleado, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 180, 150, 30));

        lblFecNacimientoEmpleado.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblFecNacimientoEmpleado.setForeground(new java.awt.Color(255, 255, 255));
        lblFecNacimientoEmpleado.setText("Fecha de Nacimiento");
        pnlFondoEmpleado.add(lblFecNacimientoEmpleado, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 160, -1, -1));

        txtFecNacimientoEmpleado.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtFecNacimientoEmpleado.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 5));
        pnlFondoEmpleado.add(txtFecNacimientoEmpleado, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 180, 150, 30));

        jPanel11.setBackground(new java.awt.Color(67, 102, 129));
        jPanel11.setAlignmentX(0.0F);

        jLabel33.setBackground(new java.awt.Color(67, 102, 129));
        jLabel33.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel33.setForeground(new java.awt.Color(255, 255, 255));
        jLabel33.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel33.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/iconCalendar.png"))); // NOI18N
        jLabel33.setToolTipText("");
        jLabel33.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel33))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel33))
        );

        pnlFondoEmpleado.add(jPanel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(810, 180, 30, 30));

        lblFecIngresoEmpleado.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblFecIngresoEmpleado.setForeground(new java.awt.Color(255, 255, 255));
        lblFecIngresoEmpleado.setText("Fecha de Ingreso");
        pnlFondoEmpleado.add(lblFecIngresoEmpleado, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 230, -1, -1));

        txtFecIngresoEmpleado.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtFecIngresoEmpleado.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 5));
        txtFecIngresoEmpleado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtFecIngresoEmpleadoActionPerformed(evt);
            }
        });
        pnlFondoEmpleado.add(txtFecIngresoEmpleado, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 250, 150, 30));

        jPanel15.setBackground(new java.awt.Color(67, 102, 129));
        jPanel15.setAlignmentX(0.0F);

        lblIconCalendarioEmpleado.setBackground(new java.awt.Color(67, 102, 129));
        lblIconCalendarioEmpleado.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblIconCalendarioEmpleado.setForeground(new java.awt.Color(255, 255, 255));
        lblIconCalendarioEmpleado.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblIconCalendarioEmpleado.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/iconCalendar.png"))); // NOI18N
        lblIconCalendarioEmpleado.setToolTipText("");
        lblIconCalendarioEmpleado.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel15Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(lblIconCalendarioEmpleado))
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel15Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(lblIconCalendarioEmpleado))
        );

        pnlFondoEmpleado.add(jPanel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 250, 30, 30));

        botonActualizarEmpleado.setText("Actualizar");
        botonActualizarEmpleado.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        botonActualizarEmpleado.setStyle(button.ButtonCustom.ButtonStyle.SECONDARY);
        botonActualizarEmpleado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonActualizarEmpleadoActionPerformed(evt);
            }
        });
        pnlFondoEmpleado.add(botonActualizarEmpleado, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 250, 120, 37));

        botonEliminarEmpleado.setText("Eliminar");
        botonEliminarEmpleado.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        botonEliminarEmpleado.setStyle(button.ButtonCustom.ButtonStyle.SECONDARY);
        botonEliminarEmpleado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonEliminarEmpleadoActionPerformed(evt);
            }
        });
        pnlFondoEmpleado.add(botonEliminarEmpleado, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 250, 120, 37));

        botonLimpiarEmpleado.setText("Limpiar");
        botonLimpiarEmpleado.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        botonLimpiarEmpleado.setStyle(button.ButtonCustom.ButtonStyle.SECONDARY);
        botonLimpiarEmpleado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonLimpiarEmpleadoActionPerformed(evt);
            }
        });
        pnlFondoEmpleado.add(botonLimpiarEmpleado, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 250, 120, 37));

        botonGuardarEmpleado.setText("Guardar");
        botonGuardarEmpleado.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        botonGuardarEmpleado.setStyle(button.ButtonCustom.ButtonStyle.SECONDARY);
        botonGuardarEmpleado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonGuardarEmpleadoActionPerformed(evt);
            }
        });
        pnlFondoEmpleado.add(botonGuardarEmpleado, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 250, 120, 37));

        tablaEmpleado.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Nombre", "A. Paterno", "A. Materno", "DNI/CE", "Celular", "F. Nacimiento", "F. Ingreso"
            }
        ));
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
            tablaEmpleado.getColumnModel().getColumn(4).setPreferredWidth(15);
            tablaEmpleado.getColumnModel().getColumn(5).setPreferredWidth(8);
            tablaEmpleado.getColumnModel().getColumn(6).setPreferredWidth(10);
            tablaEmpleado.getColumnModel().getColumn(7).setPreferredWidth(3);
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
                .addContainerGap(104, Short.MAX_VALUE))
        );
        pnlEmpleadoLayout.setVerticalGroup(
            pnlEmpleadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlEmpleadoLayout.createSequentialGroup()
                .addGap(42, 42, 42)
                .addComponent(pnlFondoEmpleado, javax.swing.GroupLayout.PREFERRED_SIZE, 511, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(42, Short.MAX_VALUE))
        );

        tabbedPane.addTab("<html><center>Agregar<p>empleado</center></html>", new javax.swing.ImageIcon(getClass().getResource("/Imagenes/iconEmpleado.png")), pnlEmpleado); // NOI18N

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
        lblTituloReporte.setText("Reporte de ventas");
        pnlTituloReporte.add(lblTituloReporte, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 180, 43));

        lblFondoTitulo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/fondoTitulos.png"))); // NOI18N
        pnlTituloReporte.add(lblFondoTitulo, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        pnlFondoReporte.add(pnlTituloReporte, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 20, 180, -1));

        botonGuardarEmpleado1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/iconDescarga.png"))); // NOI18N
        botonGuardarEmpleado1.setText("Descargar progreso del día");
        botonGuardarEmpleado1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        botonGuardarEmpleado1.setStyle(button.ButtonCustom.ButtonStyle.SECONDARY);
        botonGuardarEmpleado1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonGuardarEmpleado1ActionPerformed(evt);
            }
        });
        pnlFondoReporte.add(botonGuardarEmpleado1, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 80, 220, 50));

        lblSelFechaReporte.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblSelFechaReporte.setForeground(new java.awt.Color(255, 255, 255));
        lblSelFechaReporte.setText("Seleccionar fecha");
        pnlFondoReporte.add(lblSelFechaReporte, new org.netbeans.lib.awtextra.AbsoluteConstraints(710, 80, -1, -1));

        txtFecReporte.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtFecReporte.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 5));
        txtFecReporte.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtFecReporteActionPerformed(evt);
            }
        });
        pnlFondoReporte.add(txtFecReporte, new org.netbeans.lib.awtextra.AbsoluteConstraints(710, 100, 150, 30));

        pnlIconCalendarioReporte.setBackground(new java.awt.Color(67, 102, 129));
        pnlIconCalendarioReporte.setAlignmentX(0.0F);

        lblIconCalendarioReporte.setBackground(new java.awt.Color(67, 102, 129));
        lblIconCalendarioReporte.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblIconCalendarioReporte.setForeground(new java.awt.Color(255, 255, 255));
        lblIconCalendarioReporte.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblIconCalendarioReporte.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/iconCalendar.png"))); // NOI18N
        lblIconCalendarioReporte.setToolTipText("");
        lblIconCalendarioReporte.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        javax.swing.GroupLayout pnlIconCalendarioReporteLayout = new javax.swing.GroupLayout(pnlIconCalendarioReporte);
        pnlIconCalendarioReporte.setLayout(pnlIconCalendarioReporteLayout);
        pnlIconCalendarioReporteLayout.setHorizontalGroup(
            pnlIconCalendarioReporteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlIconCalendarioReporteLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(lblIconCalendarioReporte))
        );
        pnlIconCalendarioReporteLayout.setVerticalGroup(
            pnlIconCalendarioReporteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlIconCalendarioReporteLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(lblIconCalendarioReporte))
        );

        pnlFondoReporte.add(pnlIconCalendarioReporte, new org.netbeans.lib.awtextra.AbsoluteConstraints(860, 100, 30, 30));

        tablaReporte.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "#", "ID", "Fecha", "DNI/CE Cliente", "Nombre Cliente", "Empleado", "Descripción", "Total"
            }
        ));
        tablaReporte.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablaReporteMouseClicked(evt);
            }
        });
        jScrollPane7.setViewportView(tablaReporte);
        if (tablaReporte.getColumnModel().getColumnCount() > 0) {
            tablaReporte.getColumnModel().getColumn(0).setPreferredWidth(3);
            tablaReporte.getColumnModel().getColumn(1).setPreferredWidth(5);
            tablaReporte.getColumnModel().getColumn(2).setPreferredWidth(10);
            tablaReporte.getColumnModel().getColumn(3).setPreferredWidth(15);
            tablaReporte.getColumnModel().getColumn(4).setPreferredWidth(20);
            tablaReporte.getColumnModel().getColumn(5).setPreferredWidth(20);
            tablaReporte.getColumnModel().getColumn(6).setPreferredWidth(40);
            tablaReporte.getColumnModel().getColumn(7).setPreferredWidth(8);
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
        txtTotalReporte.setToolTipText("Ingresar nombre(s)");
        txtTotalReporte.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(240, 240, 240), 5));
        txtTotalReporte.setEnabled(false);
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
                .addContainerGap(19, Short.MAX_VALUE))
        );
        pnlReporteLayout.setVerticalGroup(
            pnlReporteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlReporteLayout.createSequentialGroup()
                .addGap(100, 100, 100)
                .addComponent(pnlFondoReporte, javax.swing.GroupLayout.PREFERRED_SIZE, 396, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(99, Short.MAX_VALUE))
        );

        tabbedPane.addTab("<html><center>Ver<p>reporte</center></html>", new javax.swing.ImageIcon(getClass().getResource("/Imagenes/iconReporte.png")), pnlReporte); // NOI18N

        jPanel1.add(tabbedPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 90, 1210, 600));

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
        // TODO add your handling code here:
        int fila = tablaEmpleado.rowAtPoint(evt.getPoint());
        txtIdEmpleado.setText(tablaEmpleado.getValueAt(fila, 0).toString());
        txtNombreEmpleado.setText(tablaEmpleado.getValueAt(fila,1).toString());
        txtApePaternoEmpleado.setText(tablaEmpleado.getValueAt(fila,2).toString());
        txtApeMaternoEmpleado.setText(tablaEmpleado.getValueAt(fila,3).toString());
        txtDocumentoEmpleado.setText(tablaEmpleado.getValueAt(fila,4).toString());
        txtCelularEmpleado.setText(tablaEmpleado.getValueAt(fila,5).toString());
        txtFecNacimientoEmpleado.setText(tablaEmpleado.getValueAt(fila,6).toString());
        txtFecIngresoEmpleado.setText(tablaEmpleado.getValueAt(fila,7).toString());
    }//GEN-LAST:event_tablaEmpleadoMouseClicked

    private void botonGuardarEmpleadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonGuardarEmpleadoActionPerformed
        if(!"".equals(txtNombreProducto.getText()) && !"".equals(cbxCategoriaProducto.getSelectedItem())
            && !"".equals(txtDescripcionProducto.getText())
            && !"".equals(txtPrecioCompraProducto.getText()) && !"".equals(txtPrecioVentaProducto.getText())
            && !"".equals((String)txtStockProducto.getValue()) && !"".equals(txtCodigoProducto.getText())
            && !"".equals(txtFotoProducto.getText())){

            pro.setNombre_producto(txtNombreProducto.getText());
            pro.setCategoria_producto(cbxCategoriaProducto.getSelectedItem().toString());
            pro.setDescripcion_producto(txtDescripcionProducto.getText());
            pro.setCosto_producto(Float.parseFloat(txtPrecioCompraProducto.getText()));
            pro.setVenta_producto(Float.parseFloat(txtPrecioVentaProducto.getText()));
            pro.setStock_producto((Integer)txtStockProducto.getValue());
            pro.setCodigo_producto(txtCodigoProducto.getText());
            pro.setFoto_producto(txtFotoProducto.getText());
            proDao.registrarProducto(pro);

            limpiarProducto();
            /*limpiarTabla();
            limpiarEmpleado();
            listarEmpleados();*/
            JOptionPane.showMessageDialog(null,"Producto Registrado con éxito");
        } else{
            JOptionPane.showMessageDialog(null,"Existen campos vacios");
        }
    }//GEN-LAST:event_botonGuardarEmpleadoActionPerformed

    private void botonLimpiarEmpleadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonLimpiarEmpleadoActionPerformed
        limpiarEmpleado();
    }//GEN-LAST:event_botonLimpiarEmpleadoActionPerformed

    private void botonEliminarEmpleadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonEliminarEmpleadoActionPerformed
        if (!"".equals(txtIdEmpleado.getText())){
            int pregunta = JOptionPane.showConfirmDialog(null,"¿Seguro que quieres eliminar este empleado?");
            if (pregunta == 0) {
                int id = Integer.parseInt(txtIdEmpleado.getText());
                empDao.eliminarEmpleado(id);
                limpiarTabla();
                limpiarEmpleado();
                listarEmpleados();
            }
        } else{
            JOptionPane.showMessageDialog(null, "Seleccione un empleado");
        }
    }//GEN-LAST:event_botonEliminarEmpleadoActionPerformed

    private void botonActualizarEmpleadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonActualizarEmpleadoActionPerformed
        if ("".equals(txtIdEmpleado.getText())) {
            JOptionPane.showMessageDialog(null, "Seleccione un empleado");
        } else{
            if (!"".equals(txtNombreEmpleado.getText()) && !"".equals(txtApePaternoEmpleado.getText()) && !"".equals(txtApeMaternoEmpleado.getText())
                && !"".equals(txtDocumentoEmpleado.getText()) && !"".equals(txtCelularEmpleado.getText())
                && !"".equals(txtFecNacimientoEmpleado.getText()) && !"".equals(txtFecIngresoEmpleado.getText())){

                int pregunta = JOptionPane.showConfirmDialog(null,"¿Seguro que quieres modificar este empleado?");
                if (pregunta == 0) {
                    emp.setId_empleado(Integer.parseInt(txtIdEmpleado.getText()));
                    emp.setNombre_empleado(txtNombreEmpleado.getText());
                    emp.setApe_paterno_empleado(txtApePaternoEmpleado.getText());
                    emp.setApe_materno_empleado(txtApeMaternoEmpleado.getText());
                    emp.setDocumento_empleado(Integer.parseInt(txtDocumentoEmpleado.getText()));
                    emp.setCelular_empleado(Integer.parseInt(txtCelularEmpleado.getText()));
                    emp.setFec_naciminto_empleado(txtFecNacimientoEmpleado.getText());
                    emp.setFec_ingreso_empleado(txtFecIngresoEmpleado.getText());
                    empDao.modificarEmpleado(emp);

                    limpiarTabla();
                    limpiarEmpleado();
                    listarEmpleados();
                    JOptionPane.showMessageDialog(null, "ACTUALIZADO CON EXITO");
                }
            } else{
                JOptionPane.showMessageDialog(null, "Existe algunos campos vacios");
            }
        }
    }//GEN-LAST:event_botonActualizarEmpleadoActionPerformed

    private void txtFecIngresoEmpleadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtFecIngresoEmpleadoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtFecIngresoEmpleadoActionPerformed

    private void txtDocumentoEmpleadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDocumentoEmpleadoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDocumentoEmpleadoActionPerformed

    private void txtNombreEmpleadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNombreEmpleadoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNombreEmpleadoActionPerformed

    private void botonActualizarProductoInventarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonActualizarProductoInventarioActionPerformed
        if ("".equals(txtIdInventario.getText())) {
            JOptionPane.showMessageDialog(null, "Seleccione un producto");
        } else{
            if (!"".equals(txtNombreInventario.getText()) && !"".equals(cbxCategoriaInventario.getSelectedItem())
                && !"".equals(txtCodigoInventario.getText()) && !"".equals(txtDescripcionInventario.getText())
                && !"".equals(txtPrecioCostoInventario.getText()) && !"".equals(txtPrecioVentaProducto.getText())
                && (Integer)txtStockInventario.getValue()!= 0 ){
                int pregunta = JOptionPane.showConfirmDialog(null,"¿Seguro que quieres modificar este producto");
                if (pregunta == 0) {
                    pro.setId_producto(Integer.parseInt(txtIdInventario.getText()));
                    pro.setNombre_producto(txtNombreInventario.getText());
                    pro.setCategoria_producto(cbxCategoriaInventario.getSelectedItem().toString());
                    pro.setCodigo_producto(txtCodigoInventario.getText());
                    pro.setDescripcion_producto(txtDescripcionInventario.getText());
                    pro.setStock_producto((Integer)txtStockInventario.getValue());
                    pro.setCosto_producto(Float.parseFloat(txtPrecioCostoInventario.getText()));
                    pro.setVenta_producto(Float.parseFloat(txtPrecioVentaProducto.getText()));
                    proDao.modificarProducto(pro);

                    limpiarTabla();
                    limpiarInventario();
                    listarProductos();
                    JOptionPane.showMessageDialog(null, "ACTUALIZADO CON EXITO");
                }
            } else{
                JOptionPane.showMessageDialog(null, "Existe algunos campos vacios");
            }
        }
    }//GEN-LAST:event_botonActualizarProductoInventarioActionPerformed

    private void botonEliminarProductoInventarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonEliminarProductoInventarioActionPerformed
        if (!"".equals(txtIdInventario.getText())){
            int pregunta = JOptionPane.showConfirmDialog(null,"¿Seguro que quieres eliminar este producto?");
            if (pregunta == 0) {
                int id = Integer.parseInt(txtIdInventario.getText());
                proDao.eliminarProducto(id);
                limpiarTabla();
                limpiarInventario();
                listarProductos();
            }
        } else{
            JOptionPane.showMessageDialog(null, "Seleccione un producto");
        }
    }//GEN-LAST:event_botonEliminarProductoInventarioActionPerformed

    private void tablaProductoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaProductoMouseClicked
        // TODO add your handling code here:
        int fila = tablaProducto.rowAtPoint(evt.getPoint());
        //;
        txtIdInventario.setText(tablaProducto.getValueAt(fila, 0).toString());
        txtNombreInventario.setText(tablaProducto.getValueAt(fila, 1).toString());
        cbxCategoriaInventario.setSelectedItem(tablaProducto.getValueAt(fila, 2).toString());
        txtDescripcionInventario.setText(tablaProducto.getValueAt(fila, 3).toString());
        txtPrecioCostoInventario.setText(tablaProducto.getValueAt(fila, 4).toString());
        txtPrecioVentaProducto.setText(tablaProducto.getValueAt(fila, 5).toString());
        txtStockInventario.setValue(tablaProducto.getValueAt(fila, 6).toString());
        txtCodigoInventario.setText(tablaProducto.getValueAt(fila, 7).toString());
    }//GEN-LAST:event_tablaProductoMouseClicked

    private void txtCriterioInventarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCriterioInventarioActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCriterioInventarioActionPerformed

    private void cbxCriterioInventarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxCriterioInventarioActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbxCriterioInventarioActionPerformed

    private void botonLimpiarProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonLimpiarProductoActionPerformed
        limpiarProducto();
    }//GEN-LAST:event_botonLimpiarProductoActionPerformed

    private void botonGuardarProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonGuardarProductoActionPerformed
        if(!"".equals(txtNombreProducto.getText()) && !"".equals(cbxCategoriaProducto.getSelectedItem())
            && !"".equals(txtDescripcionProducto.getText())
            && !"".equals(txtPrecioCompraProducto.getText()) && !"".equals(txtPrecioVentaProducto.getText())
            && !"0".equals(txtStockProducto.getValue()) && !"".equals(txtCodigoProducto.getText())
            && !"".equals(txtFotoProducto.getText())){

            pro.setNombre_producto(txtNombreProducto.getText());
            pro.setCategoria_producto(cbxCategoriaProducto.getSelectedItem().toString());
            pro.setDescripcion_producto(txtDescripcionProducto.getText());
            pro.setCosto_producto(Float.parseFloat(txtPrecioCompraProducto.getText()));
            pro.setVenta_producto(Float.parseFloat(txtPrecioVentaProducto.getText()));
            pro.setStock_producto((Integer)txtStockProducto.getValue());
            pro.setCodigo_producto(txtCodigoProducto.getText());
            pro.setFoto_producto(txtFotoProducto.getText());
            proDao.registrarProducto(pro);

            limpiarProducto();
            /*limpiarTabla();
            limpiarEmpleado();
            listarEmpleados();*/
            JOptionPane.showMessageDialog(null,"Producto Registrado con éxito");
        } else{
            JOptionPane.showMessageDialog(null,"Existen campos vacios");
        }
    }//GEN-LAST:event_botonGuardarProductoActionPerformed

    private void tablaProducto1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaProducto1MouseClicked
        int fila = tablaProducto1.rowAtPoint(evt.getPoint());
        cbxCategoriaVenta.setSelectedItem(tablaProducto1.getValueAt(fila, 1));
        txtCodigoCarrito.setText(String.valueOf(tablaProducto1.getValueAt(fila, 2)));
        txtNombreCarrito.setText(String.valueOf(tablaProducto1.getValueAt(fila, 3)));
        txtCantidadCarrito.setValue(tablaProducto1.getValueAt(fila, 4));
        txtPrecioCarrito.setText(String.valueOf(tablaProducto1.getValueAt(fila, 5)));
        
    }//GEN-LAST:event_tablaProducto1MouseClicked

    private void cbxCategoriaInventarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxCategoriaInventarioActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbxCategoriaInventarioActionPerformed

    private void botonAgregarCarritoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonAgregarCarritoActionPerformed

        DecimalFormat df = new DecimalFormat("#.00");

        String codigo = txtCodigoCarrito.getText();
        String cliente = txtNombreCliente.getText();
        //String empleado = cboEmpleados.getSelectedItem().toString();
        String nombreProd = txtNombreCarrito.getText();
        String categoria = cbxCategoriaVenta.getSelectedItem().toString();
        int cantidad = (Integer) txtCantidadCarrito.getValue();
        float precio = Float.valueOf(txtPrecioCarrito.getText());   
        int contador = 1;
        
        
        float sumaTotal = 0;
        
        if (categoria.equals("Tatuaje")) {
            cantidad = 1;
            if (txtDescripcionCarrito.getText().equals("") ||
                    txtDescripcionCarrito.getText().equals(null)) {
                JOptionPane.showMessageDialog(null, "Debe ingresar "
                        + "una descripción para el tatuaje");
                return;
            }
        }
        
        float total = cantidad*precio;
        
        /*
        if (cliente.equals("") || txtNombreCliente.getText().equals(null)) {
            JOptionPane.showMessageDialog(null, "El campo cliente debe estar lleno");
            return;
        } */      
        
        
        modelo = (DefaultTableModel) tablaProducto1.getModel();
        Object[] obj = new Object[7];        
        obj[0] = contador;
        obj[1] = categoria;
        obj[2] = codigo;
        obj[3] = nombreProd;
        obj[4] = cantidad;        
        obj[5] = precio;
        obj[6] = total;        
        modelo.addRow((obj));

        contador++;
        tablaProducto.setModel(modelo);
        
        for (int i = 0; i < modelo.getRowCount(); i++) {
            sumaTotal += Float.valueOf(tablaProducto1.getValueAt(i, 6).toString());
        }
        
        txtPrecioCarrito1.setText(String.valueOf(sumaTotal));
        
        limpiarVenta();
        
    }//GEN-LAST:event_botonAgregarCarritoActionPerformed

    private void botonGenerarVentaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonGenerarVentaActionPerformed

        if (txtNombreCliente2.getText().equals(null) || txtNombreCliente2.getText().equals("")
                || txtNombreCliente.getText().equals(null) || txtNombreCliente.equals("")) {
            JOptionPane.showMessageDialog(null, "Debe ingresar un cliente");
            txtNombreCliente2.grabFocus();
            return;
        }

        pdf();
        registrarVenta();
        //registrarDetalle();
        actualizarStock();
        limpiarTabla();
        limpiarVenta();
        txtNombreCliente2.setText("");
        txtNombreCliente.setText("");
        txtDescripcionCarrito.setText("");
        txtPrecioCarrito1.setText("");
        
        JOptionPane.showMessageDialog(null, "¡Venta realizada con éxito!");
    }//GEN-LAST:event_botonGenerarVentaActionPerformed

    private void actualizarStock(){
        for (int i = 0; i < tablaProducto1.getRowCount(); i++) {
            String cod = tablaProducto1.getValueAt(i, 2).toString();
            int cant = Integer.parseInt(tablaProducto1.getValueAt(i, 4).toString());
            pro = proDao.productoEscogido(cod);
            int stockActual = pro.getStock_producto() - cant;
            ventDao.ActualizarStock(stockActual, cod);
        }
    }
    
    private void cbxCategoriaVentaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxCategoriaVentaActionPerformed
        String categoria = cbxCategoriaVenta.getSelectedItem().toString();
        
        if (categoria.equals("Joya")) {
            txtPrecioCarrito.setEnabled(false);
            txtPrecioCarrito.setEditable(false);
            
            txtCodigoCarrito.setEnabled(true);
            txtCodigoCarrito.setEditable(true);
            
            txtCantidadCarrito.setEnabled(true);
            txtCantidadCarrito.setValue(1);
        }else{
            txtStockCarrito.setText("");
            limpiarVenta();
            txtPrecioCarrito.setEnabled(true);
            txtPrecioCarrito.setEditable(true);
            txtCodigoCarrito.setEnabled(false);
            txtCodigoCarrito.setEditable(false);
            txtStockCarrito.setEnabled(false);
            txtStockCarrito.setEditable(false);
            txtCantidadCarrito.setEnabled(false);
            txtCantidadCarrito.setValue(0);
            
            txtNombreCarrito.setEditable(true);
            txtNombreCarrito.setEnabled(true);
            
            txtNombreCarrito.grabFocus();
        }
        
    }//GEN-LAST:event_cbxCategoriaVentaActionPerformed
        
    private void txtCodigoCarritoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCodigoCarritoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCodigoCarritoActionPerformed

    private void botonActualizarCarritoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonActualizarCarritoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_botonActualizarCarritoActionPerformed

    private void botonLimpiarCarrito1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonLimpiarCarrito1ActionPerformed
        limpiarVenta();
    }//GEN-LAST:event_botonLimpiarCarrito1ActionPerformed

    private void txtNombreCliente2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNombreCliente2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNombreCliente2ActionPerformed

    private void tabbedPaneMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabbedPaneMouseClicked
        if(tabbedPane.getSelectedIndex()==1){
            limpiarTabla();
            limpiarInventario();
            listarProductos();
        }
        if(tabbedPane.getSelectedIndex()==3){
            limpiarTabla();
            listarEmpleados();
        }
    }//GEN-LAST:event_tabbedPaneMouseClicked

    private void pnlProductoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pnlProductoMouseClicked
        
    }//GEN-LAST:event_pnlProductoMouseClicked

    private void pnlInventarioMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pnlInventarioMouseClicked
    }//GEN-LAST:event_pnlInventarioMouseClicked

    private void txtFecReporteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtFecReporteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtFecReporteActionPerformed

    private void botonGuardarEmpleado1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonGuardarEmpleado1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_botonGuardarEmpleado1ActionPerformed

    private void tablaReporteMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaReporteMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tablaReporteMouseClicked

    private void cboEmpleadosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboEmpleadosActionPerformed
        
    }//GEN-LAST:event_cboEmpleadosActionPerformed

    private void cbxCategoriaProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxCategoriaProductoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbxCategoriaProductoActionPerformed

    private void jLabel37MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel37MousePressed
        String dni = txtNombreCliente2.getText();
        
        if (dni.equals(null) || dni.equals("")) {
            JOptionPane.showMessageDialog(null, "Debe llenar el campo 'DNI'", "Advertencia", JOptionPane.WARNING_MESSAGE);
        }else{
            int dni2 = Integer.parseInt(dni);
            llenarClientes(dni2);
        }
        
        
    }//GEN-LAST:event_jLabel37MousePressed

    private void jLabel35MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel35MousePressed
        
        DecimalFormat df = new DecimalFormat("#.00");
        
        String codigoProd = txtCodigoCarrito.getText();
        String categoriaA = cbxCategoriaVenta.getSelectedItem().toString();
        
        if (txtCodigoCarrito.getText().equals(null) ||
                txtCodigoCarrito.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "Debe ingresar un codigo de"
                    + " producto a buscar","ADVERTENCIA",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (categoriaA.equals("Joya")) {
            txtPrecioCarrito.setEnabled(false);
            txtPrecioCarrito.setEditable(false);
        }else{
            txtPrecioCarrito.setEnabled(true);
            txtPrecioCarrito.setEditable(true);
        }
        
        Producto prodEscogido = proDao.productoEscogido(codigoProd);
        
        String categoria = prodEscogido.getCategoria_producto().toString();
        String nombre = prodEscogido.getNombre_producto();
        String descripcion = prodEscogido.getDescripcion_producto();
        String stock = String.valueOf(prodEscogido.getStock_producto());
        String precioU = String.valueOf(prodEscogido.getVenta_producto());
        
        if (categoria.equals("Joyas")) {
            cbxCategoriaVenta.setSelectedIndex(0);
        }else if(categoria.equals("Tatuajes")){
            cbxCategoriaVenta.setSelectedIndex(1);
        }
        
        //cbxCategoriaVenta.setSelectedItem(categoria.toString());
        txtNombreCarrito.setText(nombre);
        txtStockCarrito.setText(stock);
        txtPrecioCarrito.setText(precioU);
        //txtDescripcionCarrito.setText(descripcion);
        
    }//GEN-LAST:event_jLabel35MousePressed

    private void txtNombreClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNombreClienteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNombreClienteActionPerformed

    private void txtStockCarritoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtStockCarritoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtStockCarritoActionPerformed

    private void buttonCustom1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonCustom1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_buttonCustom1ActionPerformed

    private void buttonEliminarCarritoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonEliminarCarritoActionPerformed
        eliminarFila();
    }//GEN-LAST:event_buttonEliminarCarritoActionPerformed

    private void botonActualizarCarritoMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_botonActualizarCarritoMousePressed
        
        DecimalFormat df = new DecimalFormat();
        
        String codigoP = txtCodigoCarrito.getText();
        String nombreP =txtNombreCarrito.getText();
        String cate = cbxCategoriaVenta.getSelectedItem().toString();
        String canti = txtCantidadCarrito.getValue().toString();
        
        
        if (codigoP.equals("") || nombreP.equals("")) {
            JOptionPane.showMessageDialog(null, "Ingrese un producto por código primero antes de actualizar", "Advertencia", JOptionPane.WARNING_MESSAGE);
            txtCodigoCarrito.grabFocus();
        }else{
            int fila = tablaProducto1.getSelectedRow();
            if (fila>=0) {
                
                tablaProducto1.setValueAt(cate, fila, 1);
                tablaProducto1.setValueAt(canti, fila, 4);
                
                
                float cantidad1 = Float.parseFloat(tablaProducto1.getValueAt(fila, 4).toString());
                float precioUnitario = Float.parseFloat(tablaProducto1.getValueAt(fila, 5).toString());
                float nuevoTotal = cantidad1*precioUnitario;
                float nuevoTotalFinal = 0;
                
                tablaProducto1.setValueAt(df.format(nuevoTotal), fila, 6);
                
                for (int i = 0; i < tablaProducto1.getRowCount(); i++) {
                    nuevoTotalFinal += Float.parseFloat(tablaProducto1.getValueAt(i, 6).toString());
                }
                
                txtPrecioCarrito1.setText(String.valueOf(df.format(nuevoTotalFinal)));
                
                JOptionPane.showMessageDialog(null, "Datos actualizados con éxito", "Confirmación", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }//GEN-LAST:event_botonActualizarCarritoMousePressed

    private void txtNombreCliente2KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNombreCliente2KeyTyped
        if (cbxDocumentoCliente2.getSelectedItem().toString().equals("DNI")) {
            if (txtNombreCliente2.getText().length() >= 8) {
                evt.consume();
            }
        } else {
            if (txtNombreCliente2.getText().length() >= 12) {
                evt.consume();
            }
        }
         
        
    }//GEN-LAST:event_txtNombreCliente2KeyTyped

    private void cbxDocumentoCliente2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxDocumentoCliente2ActionPerformed
        if (cbxDocumentoCliente2.getSelectedItem().toString().equals("CE")) {
            txtNombreCliente2.setText("");
            txtNombreCliente.setText("");
            txtNombreCliente2.grabFocus();
        }else{
            txtNombreCliente2.setText("");
            txtNombreCliente.setText("");
            txtNombreCliente2.grabFocus();
        }
    }//GEN-LAST:event_cbxDocumentoCliente2ActionPerformed

    private void eliminarFila(){        
        float totalFinal = 0;
        float totalFila = 0;
        int cantidad = 0;
        
        DecimalFormat df = new DecimalFormat();
        
        int fila = tablaProducto1.getSelectedRow();
        if (fila>=0) {
            
            //Pregunta con un JOption Pane
            int respuesta = JOptionPane.showConfirmDialog(null, "¿Está seguro de eliminar ese producto del carrito?", "Confirmar", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
            
            //Si confirma que desea eliminar la fila
            if (respuesta == JOptionPane.YES_OPTION) {
                cantidad = Integer.parseInt(tablaProducto1.getValueAt(fila, 4).toString());
                totalFila = Float.parseFloat(tablaProducto1.getValueAt(fila, 6).toString());
                totalFinal = Float.parseFloat(txtPrecioCarrito1.getText()) - totalFila;
                
                txtPrecioCarrito1.setText(String.valueOf(df.format(totalFinal)));
                
                modelo.removeRow(fila);
                
            }            
            
        }else{
            JOptionPane.showMessageDialog(null, "Debe seleccionar una venta del carrito primero");
        }
    }
    
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
    private button.ButtonCustom botonActualizarCarrito;
    private button.ButtonCustom botonActualizarEmpleado;
    private button.ButtonCustom botonActualizarProductoInventario;
    private button.ButtonCustom botonAgregarCarrito;
    private button.ButtonCustom botonDescargarInventario;
    private button.ButtonCustom botonEliminarEmpleado;
    private button.ButtonCustom botonEliminarProductoInventario;
    private button.ButtonCustom botonGenerarVenta;
    private button.ButtonCustom botonGuardarEmpleado;
    private button.ButtonCustom botonGuardarEmpleado1;
    private button.ButtonCustom botonGuardarProducto;
    private button.ButtonCustom botonLimpiarCarrito1;
    private button.ButtonCustom botonLimpiarEmpleado;
    private button.ButtonCustom botonLimpiarProducto;
    private button.ButtonCustom buscarInventario;
    private button.ButtonCustom buttonCustom1;
    private button.ButtonCustom buttonEliminarCarrito;
    private javax.swing.JComboBox<String> cboEmpleados;
    private javax.swing.JComboBox<String> cbxCategoriaInventario;
    private javax.swing.JComboBox<String> cbxCategoriaProducto;
    private javax.swing.JComboBox<String> cbxCategoriaVenta;
    private javax.swing.JComboBox<String> cbxCriterioInventario;
    private javax.swing.JComboBox<String> cbxDocumentoCliente2;
    private javax.swing.JComboBox<String> cbxDocumentoEmpleado;
    private javax.swing.JComboBox<String> cbxOrdenarInventario;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel37;
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
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JLabel lblApeMaternoEmpleado;
    private javax.swing.JLabel lblApePaternoEmpleado;
    private javax.swing.JLabel lblBuscarInventario;
    private javax.swing.JLabel lblCantidadCarrito;
    private javax.swing.JLabel lblCategoriaCarrito;
    private javax.swing.JLabel lblCategoriaProducto;
    private javax.swing.JLabel lblCelularEmpleado;
    private javax.swing.JLabel lblCodigoCarrito;
    private javax.swing.JLabel lblCodigoCarrito3;
    private javax.swing.JLabel lblCodigoProducto;
    private javax.swing.JLabel lblDescripcionCarrito;
    private javax.swing.JLabel lblDescripcionInventario;
    private javax.swing.JLabel lblDescripcionProducto;
    private javax.swing.JLabel lblDocumentoEmpleado;
    private javax.swing.JLabel lblFecIngresoEmpleado;
    private javax.swing.JLabel lblFecNacimientoEmpleado;
    private javax.swing.JLabel lblFondoEmpleado;
    private javax.swing.JLabel lblFondoProducto;
    private javax.swing.JLabel lblFondoReporte;
    private javax.swing.JLabel lblFondoTitulo;
    private javax.swing.JLabel lblFondoTituloProducto;
    private javax.swing.JLabel lblFotoProducto;
    private javax.swing.JLabel lblIconCalendarioEmpleado;
    private javax.swing.JLabel lblIconCalendarioReporte;
    private javax.swing.JLabel lblIconSolReporte;
    private javax.swing.JLabel lblNombreCarrito;
    private javax.swing.JLabel lblNombreCarrito1;
    private javax.swing.JLabel lblNombreCarrito2;
    private javax.swing.JLabel lblNombreEmpleado;
    private javax.swing.JLabel lblNombreProducto;
    private javax.swing.JLabel lblOrdenarInventario;
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
    private javax.swing.JLabel lblTituloEmpleado;
    private javax.swing.JLabel lblTituloInventario;
    private javax.swing.JLabel lblTituloProducto;
    private javax.swing.JLabel lblTituloReporte;
    private javax.swing.JLabel lblTituloVenta;
    private javax.swing.JPanel pnlEmpleado;
    private javax.swing.JPanel pnlFondoEmpleado;
    private javax.swing.JPanel pnlFondoInventario;
    private javax.swing.JPanel pnlFondoProducto;
    private javax.swing.JPanel pnlFondoReporte;
    private javax.swing.JPanel pnlFondoTituloInventario;
    private javax.swing.JPanel pnlFondoTituloVenta;
    private javax.swing.JPanel pnlFondoVenta;
    private javax.swing.JPanel pnlIconCalendarioReporte;
    private javax.swing.JPanel pnlIconSolReporte;
    private javax.swing.JPanel pnlInventario;
    private javax.swing.JPanel pnlProducto;
    private javax.swing.JPanel pnlReporte;
    private javax.swing.JPanel pnlSOlPrecioCostoInventario;
    private javax.swing.JPanel pnlTituloEmpleado;
    private javax.swing.JPanel pnlTituloProducto;
    private javax.swing.JPanel pnlTituloReporte;
    private javax.swing.JPanel pnlVenta;
    private javax.swing.JTabbedPane tabbedPane;
    private javax.swing.JTable tablaEmpleado;
    private javax.swing.JTable tablaProducto;
    private javax.swing.JTable tablaProducto1;
    private javax.swing.JTable tablaReporte;
    private javax.swing.JTextField txtApeMaternoEmpleado;
    private javax.swing.JTextField txtApePaternoEmpleado;
    private javax.swing.JSpinner txtCantidadCarrito;
    private javax.swing.JTextField txtCelularEmpleado;
    private javax.swing.JTextField txtCodigoCarrito;
    private javax.swing.JTextField txtCodigoInventario;
    private javax.swing.JTextField txtCodigoProducto;
    private javax.swing.JTextField txtCriterioInventario;
    private javax.swing.JTextArea txtDescripcionCarrito;
    private javax.swing.JTextArea txtDescripcionInventario;
    private javax.swing.JTextArea txtDescripcionProducto;
    private javax.swing.JTextField txtDocumentoEmpleado;
    private javax.swing.JTextField txtFecIngresoEmpleado;
    private javax.swing.JTextField txtFecNacimientoEmpleado;
    private javax.swing.JTextField txtFecReporte;
    private javax.swing.JTextField txtFotoProducto;
    private javax.swing.JTextField txtIdEmpleado;
    private javax.swing.JTextField txtIdInventario;
    private javax.swing.JTextField txtNombreCarrito;
    private javax.swing.JTextField txtNombreCliente;
    private javax.swing.JTextField txtNombreCliente2;
    private javax.swing.JTextField txtNombreEmpleado;
    private javax.swing.JTextField txtNombreInventario;
    private javax.swing.JTextField txtNombreProducto;
    private javax.swing.JTextField txtPrecioCarrito;
    private javax.swing.JTextField txtPrecioCarrito1;
    private javax.swing.JTextField txtPrecioCompraProducto;
    private javax.swing.JTextField txtPrecioCostoInventario;
    private javax.swing.JTextField txtPrecioVentaInventario;
    private javax.swing.JTextField txtPrecioVentaProducto;
    private javax.swing.JTextField txtStockCarrito;
    private javax.swing.JSpinner txtStockInventario;
    private javax.swing.JSpinner txtStockProducto;
    private javax.swing.JTextField txtTotalReporte;
    // End of variables declaration//GEN-END:variables
    
    private void limpiarEmpleado(){
        txtIdEmpleado.setText("");
        txtNombreEmpleado.setText("");
        txtApePaternoEmpleado.setText("");
        txtApeMaternoEmpleado.setText("");
        txtDocumentoEmpleado.setText("");
        txtCelularEmpleado.setText("");
        txtFecNacimientoEmpleado.setText("");
        txtFecIngresoEmpleado.setText("");
    }
    
    private void limpiarProducto(){
        txtNombreProducto.setText("");
        cbxCategoriaProducto.setSelectedIndex(-1);
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
        cbxCategoriaInventario.setSelectedIndex(-1);
        txtDescripcionInventario.setText("");
        txtPrecioCostoInventario.setText("");
        txtPrecioVentaProducto.setText("");
        txtStockInventario.setValue(1);
        txtCodigoInventario.setText("");
    }
    
    private void limpiarVenta(){
        //txtNombreCliente2.setText("");
        //txtNombreCliente.setText("");
        txtCodigoCarrito.setText("");
        txtNombreCarrito.setText("");
        txtStockCarrito.setText("");
        txtCantidadCarrito.setValue(1);
        txtPrecioCarrito.setText("");
        //txtDescripcionCarrito.setText("");
    }
    
    private void pdf(){
        DecimalFormat df = new DecimalFormat("#.00");
        String dateTime = DateTimeFormatter.ofPattern("hh:mm:ss a")
                    .format(LocalDateTime.now());
        
        try{
            int id=ventDao.IdVenta();
            FileOutputStream archivo;
            File file = new File("src/pdf/venta"+id+".pdf");
            archivo=new FileOutputStream(file);
            Document doc= new Document();
            PdfWriter.getInstance(doc, archivo);
            doc.open();
            Image img=Image.getInstance("src/Imagenes/logo2.png");
            
            Paragraph fecha=new Paragraph();
            Font negrita=new Font(Font.FontFamily.TIMES_ROMAN,12,Font.BOLD,BaseColor.CYAN);
            fecha.add(Chunk.NEWLINE);
            Date date=new Date();
            fecha.add("Boleta: "+id+"\n"+ "Fecha: "+new SimpleDateFormat("dd-MM-yyyy").format(date)+"\nHora: "+dateTime);
                        
            PdfPTable Encabezado=new PdfPTable(4);
            Encabezado.setWidthPercentage(100);
            Encabezado.getDefaultCell().setBorder(0);
            float[] ColumnaEncabezado = new float []{20f,30f,70f,40f};
            Encabezado.setWidths(ColumnaEncabezado);
            Encabezado.setHorizontalAlignment(Element.ALIGN_LEFT);
            
            Encabezado.addCell(img);
            
            String ruc="123567429";
            String nom="Tattoos";
            String tel="999-999-999";
            String dir="Av Palmeras 342";
            String ra="Tatus SAC";
            
            Encabezado.addCell("");
            Encabezado.addCell("Ruc:"+ruc+ "\nNombre: "+nom+ "\nTelefono: "+tel+ "\nDireccion: "+dir+"\nRazon: "+ra);
            Encabezado.addCell(fecha);
            doc.add(Encabezado);
           
            Paragraph cli=new Paragraph();
            cli.add(Chunk.NEWLINE);
                                   
            Chunk c1 = new Chunk("DNI/ICE: ", new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD));
            Chunk c2 = new Chunk("Nombre: ", new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD));
            Chunk c3 = new Chunk("Datos del cliente: ", new Font(Font.FontFamily.TIMES_ROMAN, 14, Font.BOLD));
            Chunk c4 = new Chunk("Empleado: ", new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD));
            
            cli.add(c4);
            cli.add(cboEmpleados.getSelectedItem().toString()+"\n\n");
            cli.add(c3);  
            cli.add("\n");
            cli.add(c1);
            cli.add(txtNombreCliente2.getText()+"\n");
            cli.add(c2);            
            cli.add(txtNombreCliente.getText()+"\n\n");            
            
            /*
            PdfPTable tablacli=new PdfPTable(4);
            tablacli.setWidthPercentage(100);
            tablacli.getDefaultCell().setBorder(0);
            float[] Columnacli = new float []{20f,50f,30f,40f};
            tablacli.setWidths(Columnacli);
            tablacli.setHorizontalAlignment(Element.ALIGN_LEFT);
            PdfPCell cl1=new PdfPCell(new Phrase("Dni/ICE",negrita));
            PdfPCell cl2=new PdfPCell(new Phrase("Nombre",negrita));*/
            //PdfPCell cl3=new PdfPCell(new Phrase("Telefono",negrita));
            //PdfPCell cl4=new PdfPCell(new Phrase("Direccion",negrita));
            //cl1.setBorder(0);
            //cl2.setBorder(0);
            //cl3.setBorder(0);
            //cl4.setBorder(0);
            //
            //cli.add("");
            //cli.add("DNI/ICE: "+txtNombreCliente2.getText()+"\nCliente: "+txtNombreCliente.getText()+"\n\n");            
            //tablacli.addCell(cl1);
            //tablacli.addCell(cl2);
            //tablacli.addCell(cl3);
            //tablacli.addCell(cl4);
            //tablacli.addCell(txtNombreCliente2.getText());
            //tablacli.addCell(txtNombreCliente.getText());
            //tablacli.addCell(txtTelefonoCV.getText());
            //tablacli.addCell(txtDireccionCV.getText());
            
            doc.add(cli);
            //doc.add(tablacli);
            
            //productos
            PdfPTable tablapro=new PdfPTable(4);
            tablapro.setWidthPercentage(100);
            tablapro.getDefaultCell().setBorder(0);
            float[] Columnapro = new float []{10f,15f,30f,20f};
            tablapro.setWidths(Columnapro);
            tablapro.setHorizontalAlignment(Element.ALIGN_LEFT);
            PdfPCell pro1=new PdfPCell(new Phrase("Cant.",negrita));
            PdfPCell pro2=new PdfPCell(new Phrase("Descripción",negrita));
            PdfPCell pro3=new PdfPCell(new Phrase("Precio U.",negrita));
            PdfPCell pro4=new PdfPCell(new Phrase("Precio T.",negrita));
            pro1.setBorder(0);
            pro2.setBorder(0);
            pro3.setBorder(0);
            pro4.setBorder(0);
            pro1.setBackgroundColor(BaseColor.BLACK);
            pro2.setBackgroundColor(BaseColor.BLACK);
            pro3.setBackgroundColor(BaseColor.BLACK);
            pro4.setBackgroundColor(BaseColor.BLACK);
            tablapro.addCell(pro1);
            tablapro.addCell(pro2);
            tablapro.addCell(pro3);
            tablapro.addCell(pro4);
            for (int i = 0; i < tablaProducto1.getRowCount(); i++) {
                String producto = tablaProducto1.getValueAt(i, 3).toString();
                String cantidad = tablaProducto1.getValueAt(i, 4).toString();
                String precio = tablaProducto1.getValueAt(i, 5).toString();
                String total = df.format(Float.valueOf(tablaProducto1.getValueAt(i, 6).toString()));
                tablapro.addCell(cantidad);
                tablapro.addCell(producto);
                tablapro.addCell(precio);
                tablapro.addCell(total);
                
            }
            doc.add(tablapro);
            
            Paragraph info = new Paragraph();
            info.add(Chunk.NEWLINE);
            info.add("Total a Pagar: S/."+ df.format(Float.parseFloat(txtPrecioCarrito1.getText())));
            info.setAlignment(Element.ALIGN_RIGHT);
            doc.add(info);
            
            Paragraph firma = new Paragraph();
            firma.add(Chunk.NEWLINE);
            firma.add("Cancelacion y Firma\n\n");
            firma.add("------------------------");
            firma.setAlignment(Element.ALIGN_CENTER);
            doc.add(firma);
            
            Paragraph mensaje = new Paragraph();
            mensaje.add(Chunk.NEWLINE);
            mensaje.add("Gracias por su Compra");
            mensaje.setAlignment(Element.ALIGN_CENTER);
            doc.add(mensaje);
            doc.close();
            archivo.close();
            Desktop.getDesktop().open(file);
        }catch(DocumentException | IOException e){
            System.out.println(e.toString());
        }
    }

}
