
package Modelo;

import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
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
import java.time.LocalDateTime;
import javax.swing.table.DefaultTableModel;
import java.time.format.DateTimeFormatter;

public class ExportarVentaPDF {

    public void reporteVenta(DefaultTableModel modelo, Venta vent, int idVenta){
        
        String dateTime = DateTimeFormatter.ofPattern("hh:mm:ss a")
                        .format(LocalDateTime.now());    
        try{
            String home = System.getProperty("user.home");
            File file = new File(home + "/Downloads/"+"venta"+idVenta+".pdf");
            
            FileOutputStream archivo;
            
            archivo=new FileOutputStream(file);
            Document doc= new Document();
            PdfWriter.getInstance(doc, archivo);
            
            doc.open();
            
            //FORMATO
            Image img=Image.getInstance("src/Imagenes/logo.png");
            
            Paragraph fecha=new Paragraph();
            Font negrita=new Font(Font.FontFamily.HELVETICA,12,Font.BOLD,BaseColor.WHITE);
            fecha.add(Chunk.NEWLINE);
            Date date=new Date();
            fecha.add("Boleta: "+idVenta+"\n"+new SimpleDateFormat("dd-MM-yyyy").format(date)
                    +"\n"+dateTime+"\n\n");
            
            
            PdfPTable Encabezado=new PdfPTable(4);
            Encabezado.setWidthPercentage(100);
            Encabezado.getDefaultCell().setBorder(0);
            float[] ColumnaEncabezado = new float []{20f,30f,70f,40f};
            Encabezado.setWidths(ColumnaEncabezado);
            Encabezado.setHorizontalAlignment(Element.ALIGN_LEFT);
            
            Encabezado.addCell(img);
            
            String ruc="123567429";
            String nom="Tattoos";
            String dir="Av Palmeras 342";
            String ra="Tatus SAC";
            
            Encabezado.addCell("");
            Encabezado.addCell(nom+"\n"+ra+"\nRUC: "+ruc+"\n"+dir);
            Encabezado.addCell(fecha);
            doc.add(Encabezado);
            
            Paragraph cli=new Paragraph();
            cli.add(Chunk.NEWLINE);
            
            Chunk c1 = new Chunk("DNI/CE: ", new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD));
            Chunk c2 = new Chunk("Nombre: ", new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD));
            Chunk c3 = new Chunk("Datos del cliente: ", new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD));
            Chunk c4 = new Chunk("Empleado: ", new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD));
            
            cli.add(c4);
            cli.add(vent.getEmpleado()+"\n\n");
            cli.add(c3);  
            cli.add("\n");
            cli.add(c1);
            cli.add("Documento\n");
            cli.add(c2);            
            cli.add(vent.getNombre()+"\n\n");
            
            doc.add(cli);
            
            //productos
            PdfPTable tablapro=new PdfPTable(6);
            tablapro.setWidthPercentage(100);
            tablapro.getDefaultCell().setBorder(0);
            float[] Columnapro = new float []{8f,10f,10f,35f,10f,10f};
            tablapro.setWidths(Columnapro);
            tablapro.setHorizontalAlignment(Element.ALIGN_LEFT);
            PdfPCell pro1=new PdfPCell(new Phrase("Cant.",negrita));
            PdfPCell pro2=new PdfPCell(new Phrase("Categoría",negrita));
            PdfPCell pro3=new PdfPCell(new Phrase("Código",negrita));
            PdfPCell pro4=new PdfPCell(new Phrase("Descripción",negrita));
            PdfPCell pro5=new PdfPCell(new Phrase("P. Unit.",negrita));
            PdfPCell pro6=new PdfPCell(new Phrase("Total.",negrita));
            pro1.setBorder(0);
            pro2.setBorder(0);
            pro3.setBorder(0);
            pro4.setBorder(0);
            pro5.setBorder(0);
            pro6.setBorder(0);
            pro1.setBackgroundColor(BaseColor.BLACK);
            pro2.setBackgroundColor(BaseColor.BLACK);
            pro3.setBackgroundColor(BaseColor.BLACK);
            pro4.setBackgroundColor(BaseColor.BLACK);
            pro5.setBackgroundColor(BaseColor.BLACK);
            pro6.setBackgroundColor(BaseColor.BLACK);
            tablapro.addCell(pro1);
            tablapro.addCell(pro2);
            tablapro.addCell(pro3);
            tablapro.addCell(pro4);
            tablapro.addCell(pro5);
            tablapro.addCell(pro6);
            
            //VER AQUÍ
            for (int i = 0; i < modelo.getRowCount(); i++) {
                String cantidad = modelo.getValueAt(i, 5).toString();
                String categoria = modelo.getValueAt(i, 1).toString();
                String codigo = modelo.getValueAt(i, 2).toString();
                String descripcion = modelo.getValueAt(i, 4).toString();
                
                
                String numeroFormateado = String.format("%.1f0", Math.round(Float.valueOf(modelo.getValueAt(i, 6).toString()) * 10.0) / 10.0);
                String precio = numeroFormateado;
                
                String numeroFormateado2 = String.format("%.1f0", Math.round(Float.valueOf(modelo.getValueAt(i, 7).toString()) * 10.0) / 10.0);
                String total = numeroFormateado2;
                tablapro.addCell(cantidad);
                tablapro.addCell(categoria);
                tablapro.addCell(codigo);
                tablapro.addCell(descripcion);
                tablapro.addCell(precio);
                tablapro.addCell(total);
                
            }
            doc.add(tablapro);
            
            Paragraph info = new Paragraph();
            info.add(Chunk.NEWLINE);
            
            String numeroFormateado2 = String.format("%.1f0", Math.round(vent.getTotal() * 10.0) / 10.0);
            info.add("Total a Pagar: S/."+ numeroFormateado2);
            info.setAlignment(Element.ALIGN_RIGHT);
            doc.add(info);
            
            Paragraph firma = new Paragraph();
            firma.add(Chunk.NEWLINE);
            firma.add("Cancelación y Firma\n\n");
            firma.add("------------------------");
            firma.setAlignment(Element.ALIGN_CENTER);
            doc.add(firma);
            
            Paragraph mensaje = new Paragraph();
            mensaje.add(Chunk.NEWLINE);
            mensaje.add("Gracias por su visita");
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
