
package pvt;

import Vista.LoginVista;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class PVT {

    public static void main(String[] args) {
//        LoginVista lv = new LoginVista();
//        lv.setVisible(true);
        
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
            java.util.logging.Logger.getLogger(LoginVista.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(LoginVista.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(LoginVista.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(LoginVista.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        try{
            Process p = Runtime.getRuntime().exec ("C:\\xampp\\xampp_start.exe");
        }catch (IOException e){ 
            System.out.println("No se encontro " + e); 
        }
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                ImageIcon icon = new ImageIcon(getClass().getResource("/Imagenes/iconJFrame.png"));
                //System.out.println("La imagen se cargó correctamente: " + icon.getImageLoadStatus());
                LoginVista lv = new LoginVista();
                lv.setIconImage(icon.getImage());
                
                lv.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                lv.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosing(WindowEvent e) {
                        try{
                                Process p = Runtime.getRuntime().exec ("C:\\xampp\\xampp_stop.exe");
                            }catch (IOException ex){ 
                                System.out.println("No se encontro " + ex); 
                            }
                            System.exit(0);
                    }
                });
                
                lv.setVisible(true);
            }
        });
    }
}
