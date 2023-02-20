
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
import javax.swing.table.DefaultTableModel;

public class ExportarVentaPDF {
    
    public void reporteVenta(DefaultTableModel modelo, Venta vent, int idVenta){
        
    DecimalFormat df = new DecimalFormat("#.00");
        
        try{
            
            String home = System.getProperty("user.home");
            File file = new File(home + "/Downloads/"+"venta"+idVenta+".pdf");
            
            FileOutputStream archivo;
            archivo=new FileOutputStream(file);
            Document doc= new Document();
            PdfWriter.getInstance(doc, archivo);
            
            doc.open();
            
            //FORMATO
            
            Image img=Image.getInstance("src/Imagenes/logo2.png");
            
            Paragraph fecha=new Paragraph();
            Font negrita=new Font(Font.FontFamily.TIMES_ROMAN,12,Font.BOLD,BaseColor.CYAN);
            fecha.add(Chunk.NEWLINE);
            Date date=new Date();
            fecha.add("Factura: "+idVenta+"\n"+ "Fecha: "+new SimpleDateFormat("dd-MM-yyyy").format(date)+"\n\n");
            
            
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
            cli.add("Datos de los clientes"+"\n\n");
            doc.add(cli);
            
            PdfPTable tablacli=new PdfPTable(4);
            tablacli.setWidthPercentage(100);
            tablacli.getDefaultCell().setBorder(0);
            float[] Columnacli = new float []{20f,50f,30f,40f};
            tablacli.setWidths(Columnacli);
            tablacli.setHorizontalAlignment(Element.ALIGN_LEFT);
            PdfPCell cl1=new PdfPCell(new Phrase("Dni/ICE",negrita));
            PdfPCell cl2=new PdfPCell(new Phrase("Nombre",negrita));
            
            cl1.setBorder(0);
            cl2.setBorder(0);
            tablacli.addCell(cl1);
            tablacli.addCell(cl2);
            tablacli.addCell(Integer.toString(vent.getDni()));
            tablacli.addCell(vent.getNombre());
            
            doc.add(tablacli);
            
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
            for (int i = 0; i < modelo.getRowCount(); i++) {
                String producto = modelo.getValueAt(i, 3).toString();
                String cantidad = modelo.getValueAt(i, 4).toString();
                String precio = modelo.getValueAt(i, 5).toString();
                String total = df.format(Float.valueOf(modelo.getValueAt(i, 6).toString()));
                tablapro.addCell(cantidad);
                tablapro.addCell(producto);
                tablapro.addCell(precio);
                tablapro.addCell(total);
                
            }
            doc.add(tablapro);
            
            Paragraph info = new Paragraph();
            info.add(Chunk.NEWLINE);
            info.add("Total a Pagar: S/."+ df.format(Float.parseFloat(Float.toString(vent.getTotal()))));
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
