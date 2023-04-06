
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
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
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

public class ExportarReporteExcel {
    
    public void reporte(DefaultTableModel modelo, String fecha) {
 
        Workbook book = new XSSFWorkbook();
        Sheet sheet = book.createSheet("Reporte-de-ventas");
        
        try {
            //Imagen logo de la empresa
            InputStream is = new FileInputStream("PVT/src/Imagenes/logo.png");
            byte[] bytes = IOUtils.toByteArray(is);
            int imgIndex = book.addPicture(bytes, Workbook.PICTURE_TYPE_PNG);
            is.close();
 
            CreationHelper help = book.getCreationHelper();
            Drawing draw = sheet.createDrawingPatriarch();
 
            ClientAnchor anchor = help.createClientAnchor();
            anchor.setCol1(0);
            anchor.setRow1(1);
            Picture pict = draw.createPicture(anchor, imgIndex);
            pict.resize(1, 3);
 
            CellStyle tituloEstilo = book.createCellStyle();
            tituloEstilo.setAlignment(HorizontalAlignment.CENTER);
            tituloEstilo.setVerticalAlignment(VerticalAlignment.CENTER);
            Font fuenteTitulo = book.createFont();
            fuenteTitulo.setFontName("Arial");
            fuenteTitulo.setBold(true);
            fuenteTitulo.setFontHeightInPoints((short) 14);
            tituloEstilo.setFont(fuenteTitulo);
 
            Row filaTitulo = sheet.createRow(1);
            Cell celdaTitulo = filaTitulo.createCell(1);
            celdaTitulo.setCellStyle(tituloEstilo);
            celdaTitulo.setCellValue("Reporte de Ventas");
 
            sheet.addMergedRegion(new CellRangeAddress(1, 2, 1, 3));
            
            //Encabezado
            String[] cabecera = new String[]{"#", "ID_Venta", "Fecha", "DNI/ICE Cliente", "Nombre Cliente", "Empleado", "Descripción {codigo(cantidad x precio unit.)}", "Total", "Progreso del día"};
 
            CellStyle headerStyle = book.createCellStyle();
            headerStyle.setFillForegroundColor(IndexedColors.BLACK.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerStyle.setBorderBottom(BorderStyle.THIN);
            headerStyle.setBorderLeft(BorderStyle.THIN);
            headerStyle.setBorderRight(BorderStyle.THIN);
            headerStyle.setBorderBottom(BorderStyle.THIN);
 
            Font font = book.createFont();
            font.setFontName("Arial");
            font.setBold(true);
            font.setColor(IndexedColors.WHITE.getIndex());
            font.setFontHeightInPoints((short) 12);
            headerStyle.setFont(font);
 
            Row filaEncabezados = sheet.createRow(4);
 
            for (int i = 0; i < cabecera.length; i++) {
                Cell celdaEnzabezado = filaEncabezados.createCell(i);
                celdaEnzabezado.setCellStyle(headerStyle);
                celdaEnzabezado.setCellValue(cabecera[i]);
            }
 
            Conexion con = new Conexion();
            PreparedStatement ps;
            ResultSet rs;
            Connection conn = con.getConnection();
            
            //Cuantos filas hacia abajo debe llenar en excel
            int numFilaDatos = 5;
 
            CellStyle datosEstilo = book.createCellStyle();
            datosEstilo.setBorderBottom(BorderStyle.THIN);
            datosEstilo.setBorderLeft(BorderStyle.THIN);
            datosEstilo.setBorderRight(BorderStyle.THIN);
            datosEstilo.setBorderBottom(BorderStyle.THIN);
 
            
            int numCol = modelo.getColumnCount();
            int filaCada = 0;
            
            //Acumular suma de monto_venta
            float suma = 0;
            
            //Qué filas colorear
            int filaColorear;
            
            //Estilo de celda para colorear filas
            CellStyle estiloCelda = book.createCellStyle();
            estiloCelda.setFillForegroundColor(IndexedColors.GREEN.getIndex());
            estiloCelda.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            estiloCelda.setBorderBottom(BorderStyle.THIN);
            estiloCelda.setBorderLeft(BorderStyle.THIN);
            estiloCelda.setBorderRight(BorderStyle.THIN);
            estiloCelda.setBorderBottom(BorderStyle.THIN);   
            estiloCelda.setFont(font);
 
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
                    if ((a == numCol - 1 || a == numCol - 2) && suma < 330) {
                        CeldaDatos.setCellStyle(estiloCelda);
                    }

                    //En las dos últimas columnas retornan decimal
                    if (a == numCol - 2 || a == numCol - 1) {
                        
                        double numero = Float.valueOf(String.valueOf(modelo.getValueAt(filaCada, a)));
                        double numeroRedondeado = Math.round(numero * 10.0) / 10.0; // Redondear a un decimal
                        //String numeroFormateado = String.format("%.1f0", numeroRedondeado).replace(",","."); // Agregar cero adicional
                        //System.out.println(numeroFormateado);
                        CeldaDatos.setCellValue(numeroRedondeado);
                        //CeldaDatos.setCellValue(Float.valueOf(String.valueOf(modelo.getValueAt(filaCada, a))));
                    } else {
                        CeldaDatos.setCellValue(String.valueOf(modelo.getValueAt(filaCada, a)));
                    }
                }
                numFilaDatos++;
                filaCada++;
            }
            
            //sheet.autoSizeColumn(0);
            sheet.autoSizeColumn(1);
            sheet.autoSizeColumn(2);
            sheet.autoSizeColumn(3);
            sheet.autoSizeColumn(4);
            sheet.autoSizeColumn(5);
            sheet.autoSizeColumn(6);
            sheet.autoSizeColumn(7);
            sheet.autoSizeColumn(8);
                                   
            sheet.setZoom(100);
            String fileName = "reporte_ventas";
            String home = System.getProperty("user.home");
            File file = new File("D:/" + fileName +" - "+fecha+ ".xlsx");
            FileOutputStream fileOut = new FileOutputStream(file);
            book.write(fileOut);
            fileOut.close();
            Desktop.getDesktop().open(file);
            JOptionPane.showMessageDialog(null, "Reporte Generado");
 
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ExportarReporteExcel.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ExportarReporteExcel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
