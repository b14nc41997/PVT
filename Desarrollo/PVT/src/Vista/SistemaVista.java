
package Vista;
import java.text.Normalizer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import Modelo.Empleado;
import Modelo.EmpleadoDao;
import Modelo.ExportarInventario;
import Modelo.Producto;
import Modelo.ProductoDao;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.System.Logger.Level;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageOutputStream;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableRowSorter;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

public class SistemaVista extends javax.swing.JFrame {

    Empleado emp = new Empleado();
    EmpleadoDao empDao = new EmpleadoDao();
    Producto pro = new Producto();
    ProductoDao proDao = new ProductoDao();
    DefaultTableModel modelo = new DefaultTableModel();    
    
    public SistemaVista() {
        initComponents();
        txtIdEmpleado.setVisible(false);
        txtIdInventario.setVisible(false);
        proDao.seleccionarCategoriaVenta(cbxCategoriaProducto);
        proDao.seleccionarCategoriaInventario(cbxCategoriaInventario);
        setLocationRelativeTo(null); //centrado
        setResizable(false); //para no maximizar interfaz
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
        modelo = (DefaultTableModel) tablaInventario.getModel();
        
        Object[] obj = new Object[8];      
        JTableHeader header = tablaInventario.getTableHeader();
        header.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        
        for (int i = 0; i < listPro.size(); i++) {
            obj[0] = listPro.get(i).getId_producto();
            obj[2] = listPro.get(i).getNombre_producto();
            obj[3] = listPro.get(i).getCategoria_producto();
            obj[4] = listPro.get(i).getDescripcion_producto();
            obj[5] = listPro.get(i).getCosto_producto();
            obj[6] = listPro.get(i).getVenta_producto();
            obj[7] = listPro.get(i).getStock_producto();
            obj[1] = listPro.get(i).getCodigo_producto();
            modelo.addRow((obj));
        }
        tablaInventario.setModel(modelo);
        
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(modelo);
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
        
        
        
//        cbxOrdenarInventario.addActionListener(new ActionListener() {
//			@Override
//			public void actionPerformed(ActionEvent e) {
//                            limpiarTabla();
//                            String atributo = Normalizer.normalize((String) cbxOrdenarInventario.getSelectedItem(), Normalizer.Form.NFD)
//                            .replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
//				atributo = atributo + "_producto";
//                                atributo = atributo.toLowerCase();        
//        
//        //proDao.listarProductosPorAtributo(atributo);
//        List<Producto> listPro = proDao.listarProductosPorAtributo(atributo);
//        //List<Producto> listPro = proDao.listarProductos();
//        modelo = (DefaultTableModel) tablaInventario.getModel();
//        Object[] obj = new Object[8];
//        
//        
//        for (int i = 0; i < listPro.size(); i++) {
//            obj[0] = listPro.get(i).getId_producto();
//            obj[2] = listPro.get(i).getNombre_producto();
//            obj[3] = listPro.get(i).getCategoria_producto();
//            obj[4] = listPro.get(i).getDescripcion_producto();
//            obj[5] = listPro.get(i).getCosto_producto();
//            obj[6] = listPro.get(i).getVenta_producto();
//            obj[7] = listPro.get(i).getStock_producto();
//            obj[1] = listPro.get(i).getCodigo_producto();
//            modelo.addRow((obj));
//        }
//        tablaInventario.setModel(modelo);
//			}
//		});
        
    }
    
    public void limpiarTabla(){
        for (int i = 0; i < modelo.getRowCount(); i++) {
            modelo.removeRow(i);
            i = i-1;
        }
    }
    
//    public void exportarExcel(JTable tabla) throws IOException{
//        JFileChooser chooser = new JFileChooser();
//        FileNameExtensionFilter filter =  new FileNameExtensionFilter("Inventario", "xls");
//        chooser.setFileFilter(filter);
//        chooser.setDialogTitle("Guardar Archivo");
//        chooser.setAcceptAllFileFilterUsed(false);
//        if(chooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION){
//            String ruta = chooser.getSelectedFile().toString().concat(".xls");
//            try{
//                File archivoXLS = new File(ruta);
//                if(archivoXLS.exists()){
//                    archivoXLS.delete();
//                }
//                archivoXLS.createNewFile();
//                Workbook libro = new HSSFWorkbook();
//                FileOutputStream archivo = new FileOutputStream(archivoXLS);
//                Sheet hoja = libro.createSheet("Inventario");
//                hoja.setDisplayGridlines(false);
//                for(int i = 0 ; i< tabla.getRowCount(); i++){
//                    Row fila = hoja.createRow(i);
//                    for( int j = 0; j< tabla.getColumnCount(); j++){
//                        Cell celda = fila.createCell(j);
//                        if(i == 0){
//                            celda.setCellValue(tabla.getColumnName(j));
//                        }
//                    }
//                }
//                int filaInicio = 1;
//                for(int i = 0; i<tabla.getRowCount(); i++){
//                    Row fila = hoja.createRow(filaInicio);
//                    filaInicio++;
//                    for( int j = 0; j< tabla.getColumnCount(); j++){
//                        Cell celda = fila.createCell(j);
//                        if(tabla.getValueAt(i, j) instanceof Integer){
//                            celda.setCellValue(Integer.parseInt(tabla.getValueAt(i, j).toString()));
//                        }else if(tabla.getValueAt(i, j) instanceof Float){
//                            celda.setCellValue(Float.parseFloat(tabla.getValueAt(i, j).toString()));
//                        }else{
//                            celda.setCellValue(String.valueOf(tabla.getValueAt(i, j)));
//                        }
//                        
//                    }
//                }
//                libro.write(archivo);
//                archivo.close();     
//                Desktop.getDesktop().open(archivoXLS);
//            }catch( IOException | NumberFormatException e){
//                throw e;                
//            }
//        }
//    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        btnSalir = new javax.swing.JButton();
        tabbedPane = new javax.swing.JTabbedPane();
        pnlVenta = new javax.swing.JPanel();
        pnlFondoVenta = new javax.swing.JPanel();
        pnlFondoTituloVenta = new javax.swing.JPanel();
        lblTituloVenta = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tablaCarrito = new javax.swing.JTable();
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
        txtStockCarrito = new javax.swing.JTextField();
        btnActualizarVenta = new javax.swing.JButton();
        btnEliminarVenta = new javax.swing.JButton();
        btnLimpiarVenta = new javax.swing.JButton();
        btnAgregarVenta = new javax.swing.JButton();
        btnAgregarVenta1 = new javax.swing.JButton();
        lblPrecioCarrito1 = new javax.swing.JLabel();
        jPanel19 = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        txtPrecioCarrito1 = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        lblNombreCarrito1 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jPanel6 = new javax.swing.JPanel();
        cbxDocumentoEmpleado2 = new javax.swing.JComboBox<>();
        txtCodigoCarrito2 = new javax.swing.JTextField();
        jPanel21 = new javax.swing.JPanel();
        jLabel37 = new javax.swing.JLabel();
        lblNombreCarrito2 = new javax.swing.JLabel();
        txtNombreCarrito2 = new javax.swing.JTextField();
        lblCodigoCarrito3 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        pnlInventario = new javax.swing.JPanel();
        pnlFondoInventario = new javax.swing.JPanel();
        pnlFondoTituloInventario = new javax.swing.JPanel();
        lblTituloInventario = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        btnBuscarInventario = new javax.swing.JButton();
        lblBuscarInventario = new javax.swing.JLabel();
        cbxCriterioInventario = new javax.swing.JComboBox<>();
        txtCriterioInventario = new javax.swing.JTextField();
        btnDescargarTabla = new javax.swing.JButton();
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
        jButton1 = new javax.swing.JButton();
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
        jLabel4 = new javax.swing.JLabel();
        lblSelFechaReporte = new javax.swing.JLabel();
        jScrollPane7 = new javax.swing.JScrollPane();
        tablaReporte = new javax.swing.JTable();
        lblPrecioReporte = new javax.swing.JLabel();
        pnlIconSolReporte = new javax.swing.JPanel();
        lblIconSolReporte = new javax.swing.JLabel();
        txtTotalReporte = new javax.swing.JTextField();
        lblFondoReporte = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(67, 102, 129));
        jPanel1.setMinimumSize(new java.awt.Dimension(1250, 750));
        jPanel1.setPreferredSize(new java.awt.Dimension(1435, 750));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel3.setBackground(new java.awt.Color(67, 102, 129));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/logo2.png"))); // NOI18N

        btnSalir.setBackground(new java.awt.Color(18, 23, 28));
        btnSalir.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        btnSalir.setForeground(new java.awt.Color(255, 255, 255));
        btnSalir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/iconSalir.png"))); // NOI18N
        btnSalir.setText("Salir");
        btnSalir.setBorder(null);
        btnSalir.setBorderPainted(false);
        btnSalir.setDefaultCapable(false);
        btnSalir.setSelected(true);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnSalir, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnSalir, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
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

        tablaCarrito.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "#", "Categoría", "Código", "Producto", "Cantidad", "Precio Unitario", "Total"
            }
        ));
        jScrollPane4.setViewportView(tablaCarrito);
        if (tablaCarrito.getColumnModel().getColumnCount() > 0) {
            tablaCarrito.getColumnModel().getColumn(0).setPreferredWidth(3);
            tablaCarrito.getColumnModel().getColumn(1).setPreferredWidth(20);
            tablaCarrito.getColumnModel().getColumn(2).setPreferredWidth(5);
            tablaCarrito.getColumnModel().getColumn(3).setPreferredWidth(20);
            tablaCarrito.getColumnModel().getColumn(4).setPreferredWidth(5);
            tablaCarrito.getColumnModel().getColumn(5).setPreferredWidth(10);
            tablaCarrito.getColumnModel().getColumn(6).setPreferredWidth(10);
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

        lblCodigoCarrito.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblCodigoCarrito.setForeground(new java.awt.Color(255, 255, 255));
        lblCodigoCarrito.setText("Codigo");

        txtCodigoCarrito.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtCodigoCarrito.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 5));

        jPanel13.setBackground(new java.awt.Color(67, 102, 129));
        jPanel13.setAlignmentX(0.0F);

        jLabel35.setBackground(new java.awt.Color(67, 102, 129));
        jLabel35.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel35.setForeground(new java.awt.Color(255, 255, 255));
        jLabel35.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel35.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/iconBuscar.png"))); // NOI18N
        jLabel35.setToolTipText("");
        jLabel35.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

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

        txtStockCarrito.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtStockCarrito.setToolTipText("Ingresar nombre(s)");
        txtStockCarrito.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(240, 240, 240), 5));
        txtStockCarrito.setEnabled(false);

        btnActualizarVenta.setBackground(new java.awt.Color(33, 50, 60));
        btnActualizarVenta.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnActualizarVenta.setForeground(new java.awt.Color(255, 255, 255));
        btnActualizarVenta.setText("Actualizar item");
        btnActualizarVenta.setBorder(null);
        btnActualizarVenta.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        btnEliminarVenta.setBackground(new java.awt.Color(51, 0, 0));
        btnEliminarVenta.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnEliminarVenta.setForeground(new java.awt.Color(255, 255, 255));
        btnEliminarVenta.setText("Eliminar item");
        btnEliminarVenta.setBorder(null);
        btnEliminarVenta.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        btnLimpiarVenta.setBackground(new java.awt.Color(33, 50, 60));
        btnLimpiarVenta.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnLimpiarVenta.setForeground(new java.awt.Color(255, 255, 255));
        btnLimpiarVenta.setText("Limpiar carrito");
        btnLimpiarVenta.setBorder(null);
        btnLimpiarVenta.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        btnAgregarVenta.setBackground(new java.awt.Color(33, 50, 60));
        btnAgregarVenta.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnAgregarVenta.setForeground(new java.awt.Color(255, 255, 255));
        btnAgregarVenta.setText("Agregar a carrito");
        btnAgregarVenta.setBorder(null);
        btnAgregarVenta.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
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
                            .addComponent(lblCodigoCarrito, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(58, 58, 58)
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
                    .addGroup(jPanel16Layout.createSequentialGroup()
                        .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel16Layout.createSequentialGroup()
                                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblStockCarrito, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtStockCarrito, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(51, 51, 51)
                                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtCantidadCarrito, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblCantidadCarrito, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel16Layout.createSequentialGroup()
                                .addGap(24, 24, 24)
                                .addComponent(btnEliminarVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(7, 7, 7)
                        .addComponent(btnActualizarVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(37, 37, 37)
                        .addComponent(btnLimpiarVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(43, 43, 43)
                        .addComponent(btnAgregarVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
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
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
                                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(btnLimpiarVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(btnAgregarVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(btnEliminarVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(btnActualizarVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(13, 13, 13))))))
        );

        pnlFondoVenta.add(jPanel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(31, 168, -1, -1));

        btnAgregarVenta1.setBackground(new java.awt.Color(33, 50, 60));
        btnAgregarVenta1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnAgregarVenta1.setForeground(new java.awt.Color(255, 255, 255));
        btnAgregarVenta1.setText("Generar venta");
        btnAgregarVenta1.setBorder(null);
        btnAgregarVenta1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        pnlFondoVenta.add(btnAgregarVenta1, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 520, 150, 40));

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

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblNombreCarrito1, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblNombreCarrito1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pnlFondoVenta.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(614, 62, -1, -1));

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)), "Cliente", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Tahoma", 0, 12), new java.awt.Color(153, 153, 153))); // NOI18N
        jPanel6.setOpaque(false);

        cbxDocumentoEmpleado2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "DNI", "CE" }));

        txtCodigoCarrito2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtCodigoCarrito2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 5));

        jPanel21.setBackground(new java.awt.Color(67, 102, 129));
        jPanel21.setAlignmentX(0.0F);

        jLabel37.setBackground(new java.awt.Color(67, 102, 129));
        jLabel37.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel37.setForeground(new java.awt.Color(255, 255, 255));
        jLabel37.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel37.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/iconBuscar.png"))); // NOI18N
        jLabel37.setToolTipText("");
        jLabel37.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

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

        txtNombreCarrito2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 5));

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
                        .addComponent(cbxDocumentoEmpleado2, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txtCodigoCarrito2, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jPanel21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(36, 36, 36))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addComponent(lblCodigoCarrito3, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtNombreCarrito2, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
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
                        .addComponent(txtNombreCarrito2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(5, 5, 5))
                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(cbxDocumentoEmpleado2, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(txtCodigoCarrito2, javax.swing.GroupLayout.Alignment.LEADING)
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

        btnBuscarInventario.setBackground(new java.awt.Color(33, 50, 60));
        btnBuscarInventario.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnBuscarInventario.setForeground(new java.awt.Color(255, 255, 255));
        btnBuscarInventario.setText("Buscar");
        btnBuscarInventario.setBorder(null);
        btnBuscarInventario.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnBuscarInventario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarInventarioActionPerformed(evt);
            }
        });
        pnlFondoInventario.add(btnBuscarInventario, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 80, 120, 40));

        lblBuscarInventario.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblBuscarInventario.setForeground(new java.awt.Color(255, 255, 255));
        lblBuscarInventario.setText("Buscar por");
        pnlFondoInventario.add(lblBuscarInventario, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 70, -1, -1));

        cbxCriterioInventario.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        cbxCriterioInventario.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "ID", "Nombre", "Categoría", "Código" }));
        pnlFondoInventario.add(cbxCriterioInventario, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 90, 120, 35));

        txtCriterioInventario.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtCriterioInventario.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 5));
        pnlFondoInventario.add(txtCriterioInventario, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 90, 180, 30));

        btnDescargarTabla.setBackground(new java.awt.Color(33, 50, 60));
        btnDescargarTabla.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnDescargarTabla.setForeground(new java.awt.Color(255, 255, 255));
        btnDescargarTabla.setText("Descargar Tabla");
        btnDescargarTabla.setBorder(null);
        btnDescargarTabla.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnDescargarTabla.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDescargarTablaActionPerformed(evt);
            }
        });
        pnlFondoInventario.add(btnDescargarTabla, new org.netbeans.lib.awtextra.AbsoluteConstraints(840, 80, 180, 40));

        tablaInventario.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Código", "Nombre", "Categoría", "Descripción", "Costo", "Venta", "Stock"
            }
        ));
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

        jLabel25.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel25.setForeground(new java.awt.Color(255, 255, 255));
        jLabel25.setText("Categoría");

        cbxCategoriaInventario.setToolTipText("");

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

        btnEliminarInventario.setBackground(new java.awt.Color(51, 0, 0));
        btnEliminarInventario.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnEliminarInventario.setForeground(new java.awt.Color(255, 255, 255));
        btnEliminarInventario.setText("Eliminar");
        btnEliminarInventario.setBorder(null);
        btnEliminarInventario.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
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
        btnActualizarInventario.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
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
        lblImagenInventario.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
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

        jButton1.setBackground(new java.awt.Color(33, 50, 60));
        jButton1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("Descargar Inventario");
        jButton1.setBorder(null);
        jButton1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        pnlFondoInventario.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 80, 180, 40));

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

        txtFotoProducto.setEditable(false);
        txtFotoProducto.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtFotoProducto.setToolTipText("Ingresar nombre(s)");
        txtFotoProducto.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(245, 241, 241), 5));
        pnlFondoProducto.add(txtFotoProducto, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 370, 80, 30));

        jPanel12.setBackground(new java.awt.Color(67, 102, 129));
        jPanel12.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        btnFotoProducto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/iconUpload.png"))); // NOI18N
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
        btnGuardarProducto.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
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
        btnLimpiarProducto.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
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

        dchFecNacimientoEmpleado.setDateFormatString("dd-MM-yyyy");
        dchFecNacimientoEmpleado.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        dchFecNacimientoEmpleado.setOpaque(false);
        pnlFondoEmpleado.add(dchFecNacimientoEmpleado, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 180, 180, 35));

        lblFecIngresoEmpleado.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblFecIngresoEmpleado.setForeground(new java.awt.Color(255, 255, 255));
        lblFecIngresoEmpleado.setText("Fecha de Ingreso");
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
        btnActualizarEmpleado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnActualizarEmpleadoActionPerformed(evt);
            }
        });
        pnlFondoEmpleado.add(btnActualizarEmpleado, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 250, 120, 40));

        btnEliminarEmpleado.setBackground(new java.awt.Color(33, 50, 60));
        btnEliminarEmpleado.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnEliminarEmpleado.setForeground(new java.awt.Color(255, 255, 255));
        btnEliminarEmpleado.setText("Eliminar");
        btnEliminarEmpleado.setBorder(null);
        btnEliminarEmpleado.setBorderPainted(false);
        btnEliminarEmpleado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarEmpleadoActionPerformed(evt);
            }
        });
        pnlFondoEmpleado.add(btnEliminarEmpleado, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 250, 120, 40));

        btnLimpiarEmpleado.setBackground(new java.awt.Color(33, 50, 60));
        btnLimpiarEmpleado.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnLimpiarEmpleado.setForeground(new java.awt.Color(255, 255, 255));
        btnLimpiarEmpleado.setText("Limpiar");
        btnLimpiarEmpleado.setBorder(null);
        btnLimpiarEmpleado.setBorderPainted(false);
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

        btnDescargaReporte.setBackground(new java.awt.Color(33, 50, 60));
        btnDescargaReporte.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnDescargaReporte.setForeground(new java.awt.Color(255, 255, 255));
        btnDescargaReporte.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/iconDescarga.png"))); // NOI18N
        btnDescargaReporte.setText("Descargar progreso del día");
        btnDescargaReporte.setBorder(null);
        btnDescargaReporte.setBorderPainted(false);
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

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/iconBuscar.png"))); // NOI18N
        jLabel4.setToolTipText("");
        jLabel4.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
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
                "#", "ID", "Fecha", "DNI/CE Cliente", "Nombre Cliente", "Empleado", "Descripción", "Total"
            }
        ));
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
        txtNombreInventario.setText(tablaInventario.getValueAt(fila, 2).toString());        
        txtDescripcionInventario.setText(tablaInventario.getValueAt(fila, 4).toString());
        txtPrecioCostoInventario.setText(tablaInventario.getValueAt(fila, 5).toString());
        txtPrecioVentaInventario.setText(tablaInventario.getValueAt(fila, 6).toString());
        txtStockInventario.setValue(tablaInventario.getValueAt(fila, 7));
        txtCodigoInventario.setText(tablaInventario.getValueAt(fila, 1).toString());        
        if (tablaInventario.getValueAt(fila, 3).toString().contains("Joya")){
            cbxCategoriaInventario.setSelectedIndex(0);
        }else{
            cbxCategoriaInventario.setSelectedIndex(1);
        }
    }//GEN-LAST:event_tablaInventarioMouseClicked

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

    private void btnEliminarInventarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarInventarioActionPerformed
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
    }//GEN-LAST:event_btnEliminarInventarioActionPerformed

    private void btnActualizarInventarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnActualizarInventarioActionPerformed
        if ("".equals(txtIdInventario.getText())) {
            JOptionPane.showMessageDialog(null, "Seleccione un producto");
        } else{            
            if (!"".equals(txtNombreInventario.getText()) && !"".equals(cbxCategoriaInventario.getSelectedItem())
                && !"".equals(txtCodigoInventario.getText()) && !"".equals(txtDescripcionInventario.getText())
                && !"".equals(txtPrecioCostoInventario.getText()) && !"".equals(txtPrecioVentaInventario.getText())
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
                    pro.setVenta_producto(Float.parseFloat(txtPrecioVentaInventario.getText()));                
                    
                    
                    if(proDao.modificarProducto(pro)){
                        limpiarTabla();
                        limpiarInventario();
                        listarProductos();
                        JOptionPane.showMessageDialog(null, "ACTUALIZADO CON EXITO");                        
                    }                   
                }
            } else{
                JOptionPane.showMessageDialog(null, "Existe algunos campos vacios");
            }
        }
    }//GEN-LAST:event_btnActualizarInventarioActionPerformed

    private void btnGuardarProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarProductoActionPerformed
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
            pro.setFoto_producto(getImagen(txtFotoProducto.getText()));//colocar aquí el objeto bytes de la foto
            proDao.registrarProducto(pro);

            limpiarProducto();
            /*limpiarTabla();
            limpiarEmpleado();
            listarEmpleados();*/
            JOptionPane.showMessageDialog(null,"Producto Registrado con éxito");
        } else{
            JOptionPane.showMessageDialog(null,"Existen campos vacios");
        }
    }//GEN-LAST:event_btnGuardarProductoActionPerformed

    private void btnLimpiarProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimpiarProductoActionPerformed
        limpiarProducto();
    }//GEN-LAST:event_btnLimpiarProductoActionPerformed

    private void btnGuardarEmpleadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarEmpleadoActionPerformed
        if(!"".equals(txtNombreEmpleado.getText()) && !"".equals(txtApePaternoEmpleado.getText()) && !"".equals(txtApeMaternoEmpleado.getText())
                && !"".equals(txtDocumentoEmpleado.getText()) && !"".equals(txtCelularEmpleado.getText())
                && !"".equals(dchFecNacimientoEmpleado.getDate().toString()) && !"".equals(dchFecIngresoEmpleado.getDate().toString())){
            
            emp.setNombre_empleado(txtNombreEmpleado.getText());
            emp.setApe_paterno_empleado(txtApePaternoEmpleado.getText());
            emp.setApe_materno_empleado(txtApeMaternoEmpleado.getText());
            emp.setDocumento_empleado(Integer.parseInt(txtDocumentoEmpleado.getText()));
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
            
            limpiarTabla();
            limpiarEmpleado();
            listarEmpleados();
            JOptionPane.showMessageDialog(null,"Empleado Registrado con éxito");
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
    }//GEN-LAST:event_lblImagenInventarioMouseClicked

    private void btnActualizarEmpleadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnActualizarEmpleadoActionPerformed
        if ("".equals(txtIdEmpleado.getText())) {
            JOptionPane.showMessageDialog(null, "Seleccione un empleado");
        } else{
            if (!"".equals(txtNombreEmpleado.getText()) && !"".equals(txtApePaternoEmpleado.getText()) && !"".equals(txtApeMaternoEmpleado.getText())
                && !"".equals(txtDocumentoEmpleado.getText()) && !"".equals(txtCelularEmpleado.getText())
                && !"".equals(dchFecNacimientoEmpleado.getDate().toString()) && !"".equals(dchFecIngresoEmpleado.getDate().toString())){

                int pregunta = JOptionPane.showConfirmDialog(null,"¿Seguro que quieres modificar este empleado?");
                if (pregunta == 0) {
                    emp.setId_empleado(Integer.parseInt(txtIdEmpleado.getText()));
                    emp.setNombre_empleado(txtNombreEmpleado.getText());
                    emp.setApe_paterno_empleado(txtApePaternoEmpleado.getText());
                    emp.setApe_materno_empleado(txtApeMaternoEmpleado.getText());
                    emp.setDocumento_empleado(Integer.parseInt(txtDocumentoEmpleado.getText()));
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

                    limpiarTabla();
                    limpiarEmpleado();
                    listarEmpleados();
                    JOptionPane.showMessageDialog(null, "ACTUALIZADO CON EXITO");
                }
            } else{
                JOptionPane.showMessageDialog(null, "Existe algunos campos vacios");
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
                limpiarTabla();
                limpiarEmpleado();
                listarEmpleados();
            }
        } else{
            JOptionPane.showMessageDialog(null, "Seleccione un empleado");
        }
    }//GEN-LAST:event_btnEliminarEmpleadoActionPerformed

    private void btnBuscarInventarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarInventarioActionPerformed
        // TODO add your handling code here:
        limpiarTabla();
        String atributo = Normalizer.normalize((String) cbxCriterioInventario.getSelectedItem(), Normalizer.Form.NFD)
                            .replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
        atributo = atributo + "_producto";
        atributo = atributo.toLowerCase(); 
        
        String valor = txtCriterioInventario.getText();
        List<Producto> listPro = proDao.filtrarProductos(atributo, valor);
        modelo = (DefaultTableModel) tablaInventario.getModel();
        Object[] obj = new Object[8];        
        
        for (int i = 0; i < listPro.size(); i++) {
            obj[0] = listPro.get(i).getId_producto();
            obj[2] = listPro.get(i).getNombre_producto();
            obj[3] = listPro.get(i).getCategoria_producto();
            obj[4] = listPro.get(i).getDescripcion_producto();
            obj[5] = listPro.get(i).getCosto_producto();
            obj[6] = listPro.get(i).getVenta_producto();
            obj[7] = listPro.get(i).getStock_producto();
            obj[1] = listPro.get(i).getCodigo_producto();
            modelo.addRow((obj));
        }
        tablaInventario.setModel(modelo);

        
//        try {
//            pro = proDao.buscarProducto(atributo, valor);  
//            txtNombreInventario.setText(pro.getNombre_producto());
//            txtCodigoInventario.setText(pro.getCodigo_producto());
//            txtDescripcionProducto.setText(pro.getDescripcion_producto());
//            txtPrecioCostoInventario.setText(String.valueOf(pro.getCosto_producto()));
//            txtPrecioVentaInventario.setText(String.valueOf(pro.getVenta_producto()));
//            txtStockInventario.setValue(pro.getStock_producto());
//            txtDescripcionInventario.setText(String.valueOf(pro.getFoto_producto()));
//            //txtDescripcionInventario.setText(pro.getDescripcion_producto());
//        } catch (SQLException ex) {
//            System.out.println(ex.toString());
//        }
        
    }//GEN-LAST:event_btnBuscarInventarioActionPerformed

    private void btnDescargarTablaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDescargarTablaActionPerformed
        // TODO add your handling code here:            
        
        modelo = (DefaultTableModel) tablaInventario.getModel();
        ExportarInventario excel = new ExportarInventario();
        excel.exportarTablaExcel(modelo);
    }//GEN-LAST:event_btnDescargarTablaActionPerformed

    private void btnDescargaReporteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDescargaReporteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnDescargaReporteActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        ExportarInventario excel = new ExportarInventario();
        try {
            excel.exportarInventarioCompleto();
        } catch (SQLException ex) {
            Logger.getLogger(SistemaVista.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton1ActionPerformed

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
    private javax.swing.JButton btnActualizarEmpleado;
    private javax.swing.JButton btnActualizarInventario;
    private javax.swing.JButton btnActualizarVenta;
    private javax.swing.JButton btnAgregarVenta;
    private javax.swing.JButton btnAgregarVenta1;
    private javax.swing.JButton btnBuscarInventario;
    private javax.swing.JButton btnDescargaReporte;
    private javax.swing.JButton btnDescargarTabla;
    private javax.swing.JButton btnEliminarEmpleado;
    private javax.swing.JButton btnEliminarInventario;
    private javax.swing.JButton btnEliminarVenta;
    private javax.swing.JLabel btnFotoProducto;
    private javax.swing.JButton btnGuardarEmpleado;
    private javax.swing.JButton btnGuardarProducto;
    private javax.swing.JButton btnLimpiarEmpleado;
    private javax.swing.JButton btnLimpiarProducto;
    private javax.swing.JButton btnLimpiarVenta;
    private javax.swing.JButton btnSalir;
    private javax.swing.JComboBox<String> cbxCategoriaInventario;
    private javax.swing.JComboBox<String> cbxCategoriaProducto;
    private javax.swing.JComboBox<String> cbxCategoriaVenta;
    private javax.swing.JComboBox<String> cbxCriterioInventario;
    private javax.swing.JComboBox<String> cbxDocumentoEmpleado;
    private javax.swing.JComboBox<String> cbxDocumentoEmpleado2;
    private com.toedter.calendar.JDateChooser dchFecIngresoEmpleado;
    private com.toedter.calendar.JDateChooser dchFecNacimientoEmpleado;
    private com.toedter.calendar.JDateChooser dchFechaReporte;
    private javax.swing.JButton jButton1;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
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
    private javax.swing.JLabel lblIconSolReporte;
    private javax.swing.JLabel lblImagenInventario;
    private javax.swing.JLabel lblNombreCarrito;
    private javax.swing.JLabel lblNombreCarrito1;
    private javax.swing.JLabel lblNombreCarrito2;
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
    private javax.swing.JPanel pnlIconSolReporte;
    private javax.swing.JPanel pnlImagenInventario;
    private javax.swing.JPanel pnlInventario;
    private javax.swing.JPanel pnlProducto;
    private javax.swing.JPanel pnlReporte;
    private javax.swing.JPanel pnlSOlPrecioCostoInventario;
    private javax.swing.JPanel pnlTituloEmpleado;
    private javax.swing.JPanel pnlTituloProducto;
    private javax.swing.JPanel pnlTituloReporte;
    private javax.swing.JPanel pnlVenta;
    private javax.swing.JTabbedPane tabbedPane;
    private javax.swing.JTable tablaCarrito;
    private javax.swing.JTable tablaEmpleado;
    private javax.swing.JTable tablaInventario;
    private javax.swing.JTable tablaReporte;
    private javax.swing.JTextField txtApeMaternoEmpleado;
    private javax.swing.JTextField txtApePaternoEmpleado;
    private javax.swing.JSpinner txtCantidadCarrito;
    private javax.swing.JTextField txtCelularEmpleado;
    private javax.swing.JTextField txtCodigoCarrito;
    private javax.swing.JTextField txtCodigoCarrito2;
    private javax.swing.JTextField txtCodigoInventario;
    private javax.swing.JTextField txtCodigoProducto;
    private javax.swing.JTextField txtCriterioInventario;
    private javax.swing.JTextArea txtDescripcionCarrito;
    private javax.swing.JTextArea txtDescripcionInventario;
    private javax.swing.JTextArea txtDescripcionProducto;
    private javax.swing.JTextField txtDocumentoEmpleado;
    private javax.swing.JTextField txtFotoProducto;
    private javax.swing.JTextField txtIdEmpleado;
    private javax.swing.JTextField txtIdInventario;
    private javax.swing.JTextField txtNombreCarrito;
    private javax.swing.JTextField txtNombreCarrito2;
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

}
