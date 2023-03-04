package Modelo;

import java.awt.Desktop;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExportarInventario {
    
    Workbook book = new XSSFWorkbook();
    Sheet sheet = book.createSheet("Inventario");
    CreationHelper help = book.getCreationHelper();
    Drawing draw = sheet.createDrawingPatriarch();
    ClientAnchor anchor = help.createClientAnchor();
    CellStyle tituloEstilo = book.createCellStyle();
    Font fuenteTitulo = book.createFont();
    Row filaTitulo = sheet.createRow(1);
    Cell celdaTitulo = filaTitulo.createCell(1);
    CellStyle headerStyle =  book.createCellStyle();
    CellStyle datosEstilo =  book.createCellStyle();
    CellStyle estiloCelda =  book.createCellStyle();
    String[] cabecera = new String[]{"ID", "Código", "Nombre", "Categoría", "Descripción", "Costo", "Venta", "Stock"};         
    Font font = book.createFont();
    Row filaEncabezados = sheet.createRow(4);    
    int numFilaDatos = 5;    
    long timeMillis = System.currentTimeMillis();
    
    Connection conexion;
    Conexion cn = new Conexion();
    PreparedStatement ps;
    ResultSet rs;
    String dateTime = DateTimeFormatter.ofPattern("hh.mm.ss").format(LocalDateTime.now());
    Date date=new Date();
    String fecha = new SimpleDateFormat("dd-MM-yyyy").format(date);
    
    public void formatoExcel(){
        try{
            InputStream is = new FileInputStream("PVT/src/Imagenes/logo.png");
            byte[] bytes = IOUtils.toByteArray(is);
            int imgIndex = book.addPicture(bytes, Workbook.PICTURE_TYPE_PNG);
            is.close(); 
            
            anchor.setCol1(0);
            anchor.setRow1(1);
            Picture pict = draw.createPicture(anchor, imgIndex);
            pict.resize(1,3); 
            
            tituloEstilo.setAlignment(HorizontalAlignment.CENTER);
            tituloEstilo.setVerticalAlignment(VerticalAlignment.CENTER);            
            fuenteTitulo.setFontName("Arial");
            fuenteTitulo.setBold(true);
            fuenteTitulo.setFontHeightInPoints((short) 14);
            tituloEstilo.setFont(fuenteTitulo);
 
            //celdaTitulo.setCellStyle(tituloEstilo);
            //celdaTitulo.setCellValue("Inventario");
 
            sheet.addMergedRegion(new CellRangeAddress(1, 2, 1, 3));
            
            //Encabezado            
            headerStyle.setFillForegroundColor(IndexedColors.BLUE.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerStyle.setBorderBottom(BorderStyle.THIN);
            headerStyle.setBorderLeft(BorderStyle.THIN);
            headerStyle.setBorderRight(BorderStyle.THIN);
            headerStyle.setBorderBottom(BorderStyle.THIN);
 
            font.setFontName("Arial");
            font.setBold(true);
            font.setColor(IndexedColors.WHITE.getIndex());
            font.setFontHeightInPoints((short) 12);
            headerStyle.setFont(font);
 
            for (int i = 0; i < cabecera.length; i++) {
                Cell celdaEnzabezado = filaEncabezados.createCell(i);
                celdaEnzabezado.setCellStyle(headerStyle);
                celdaEnzabezado.setCellValue(cabecera[i]);
            }
            
            //Cuantos filas hacia abajo debe llenar en excel                         
            datosEstilo.setBorderBottom(BorderStyle.THIN);
            datosEstilo.setBorderLeft(BorderStyle.THIN);
            datosEstilo.setBorderRight(BorderStyle.THIN);
            datosEstilo.setBorderBottom(BorderStyle.THIN);
            
            //Estilo de celda para colorear filas
            estiloCelda.setFillForegroundColor(IndexedColors.WHITE.getIndex());
            estiloCelda.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            estiloCelda.setBorderBottom(BorderStyle.THIN);
            estiloCelda.setBorderLeft(BorderStyle.THIN);
            estiloCelda.setBorderRight(BorderStyle.THIN);
            estiloCelda.setBorderBottom(BorderStyle.THIN);
            
            
        }catch(FileNotFoundException ex){
            Logger.getLogger(ExportarInventario.class.getName()).log(Level.SEVERE, null, ex);
        } catch(IOException ex){
            Logger.getLogger(ExportarInventario.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
        
    public void exportarTablaExcel(DefaultTableModel modelo){         
        formatoExcel();        
        try{       
            celdaTitulo.setCellStyle(tituloEstilo);
            celdaTitulo.setCellValue("Inventario Filtrado");
            int numCol = modelo.getColumnCount();
            int filaCada = 0;
            
            //Acumular suma de monto_venta
            float suma = 0;
            
            //Qué filas colorear
            int filaColorear;            
            
            while (filaCada < modelo.getRowCount()) {
                Row filaDatos = sheet.createRow(numFilaDatos);

                //Llenar columnas
                for (int a = 0; a < numCol; a++) {

                    Cell CeldaDatos = filaDatos.createCell(a);
                    CeldaDatos.setCellStyle(datosEstilo);
                    
                    //Suma para determinar los colores de cada fila
                    if (a == numCol - 2) {
                        suma += Double.valueOf(String.valueOf(modelo.getValueAt(filaCada, a)));
                    }
                    
                    //Colorear sólo las dos últimas columnas
                    if ((a == numCol - 1 || a == numCol - 3 || a == numCol - 2) && suma < 330) {
                        CeldaDatos.setCellStyle(estiloCelda);
                    }

                    //En las dos últimas columnas retornan decimal
                    if (a == numCol - 2 || a == numCol - 3 || a == numCol - 1 ) {
                        CeldaDatos.setCellValue(Math.round(Float.valueOf(String.valueOf(modelo.getValueAt(filaCada, a)))*10.00)/10.00);
                        
                    } else {
                        CeldaDatos.setCellValue(String.valueOf(modelo.getValueAt(filaCada, a)));
                    }
                }
                numFilaDatos++;
                filaCada++;
            }
            
            for(int i = 1; i<cabecera.length; i++){
                sheet.autoSizeColumn(i);
            }           
                                   
            sheet.setZoom(100);
            String fileName = "InventarioFiltrado";            
            String home = System.getProperty("user.home");
            File file = new File("D:/"+fileName+" - "+fecha+" - "+dateTime+".xlsx");
            FileOutputStream fileOut = new FileOutputStream(file);
            book.write(fileOut);
            fileOut.close();
            Desktop.getDesktop().open(file);
            JOptionPane.showMessageDialog(null, "Tabla de inventario descargada");
            
        }catch(FileNotFoundException ex){
            Logger.getLogger(ExportarInventario.class.getName()).log(Level.SEVERE, null, ex);
        } catch(IOException ex){
            Logger.getLogger(ExportarInventario.class.getName()).log(Level.SEVERE, null, ex);
        }       
    }
    
    public void exportarInventarioCompleto() throws SQLException{
        formatoExcel();
        String sql = "Select * from productos";
        
        try{
            
            celdaTitulo.setCellStyle(tituloEstilo);
            celdaTitulo.setCellValue("Inventario Completo");
            
            conexion = cn.getConnection();
            ps = conexion.prepareStatement(sql);
            rs = ps.executeQuery();
            
            book.createSheet("Inventario Total");
            
            while(rs.next()){
                Row fila = sheet.createRow(numFilaDatos++);
                
                Cell celdaId = fila.createCell(0);
                celdaId.setCellValue(rs.getInt("id_producto"));
                
                Cell celdaCodigo = fila.createCell(1);
                celdaCodigo.setCellValue(rs.getString("codigo_producto"));
                
                Cell celdaNombre = fila.createCell(2);
                celdaNombre.setCellValue(rs.getString("nombre_producto"));
                
                Cell celdaCategoria = fila.createCell(3);
                celdaCategoria.setCellValue(rs.getString("categoria_producto"));
                
                Cell celdaDesc = fila.createCell(4);
                celdaDesc.setCellValue(rs.getString("descripcion_producto"));
                
                Cell celdaCosto = fila.createCell(5);
                celdaCosto.setCellValue(Math.round(rs.getFloat("costo_producto")*10.00)/10.00);
                
                Cell celdaVenta = fila.createCell(6);
                celdaVenta.setCellValue(Math.round(rs.getFloat("venta_producto")*10.0)/10.0);
               
                Cell celdaStock = fila.createCell(7);
                celdaStock.setCellValue(rs.getInt("stock_producto"));                
            }
            for(int i = 1; i<cabecera.length; i++){
                sheet.autoSizeColumn(i);
            }  
                
            sheet.setZoom(100);
            String fileName = "InventarioCompleto";
            File file = new File("D:/"+fileName+" - "+fecha+" - "+dateTime+".xlsx");
            FileOutputStream fileOut = new FileOutputStream(file);
            book.write(fileOut);
            fileOut.close();
            Desktop.getDesktop().open(file);
            JOptionPane.showMessageDialog(null, "Inventario descargado");            
            
        }catch(FileNotFoundException ex){
            Logger.getLogger(ExportarInventario.class.getName()).log(Level.SEVERE, null, ex);
        } catch(IOException ex){
            Logger.getLogger(ExportarInventario.class.getName()).log(Level.SEVERE, null, ex);
        }    
    }
}