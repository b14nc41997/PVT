
package Vista;
import Modelo.Login;
import Modelo.LoginDao;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class LoginVista extends javax.swing.JFrame {
    Login log = new Login();
    LoginDao logDao = new LoginDao();
    
    public LoginVista() {
        initComponents();
        setLocationRelativeTo(null); //centrado
        setResizable(false); //para no maximizar interfaz
        this.setTitle("PUNTO DE VENTA ARTESANOS INK TATTOO");
    }
    
    public static String user = "";
    
    public void validar(){
        String correo_usuario = txtUsuario.getText();
        String contrasena_usuario = String.valueOf(txtContrasenia.getPassword());
        if (!"".equals(correo_usuario) || !"".equals(contrasena_usuario)) {
            log = logDao.login(correo_usuario, contrasena_usuario);
            if (log.getCorreo_usuario()!=null && log.getContrasena_usuario()!=null) {
                
                user = log.getNombre_usuario();
                ImageIcon icon = new ImageIcon(getClass().getResource("/Imagenes/iconJFrame.png"));
                SistemaVista sv = new SistemaVista();
                sv.setIconImage(icon.getImage());
                
                sv.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                sv.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosing(WindowEvent e) {
                        int option = JOptionPane.showConfirmDialog(sv, "¿Está seguro de cerrar la aplicación?", "Confirmación de salida", JOptionPane.YES_NO_OPTION);
                        if (option == JOptionPane.YES_OPTION) {
                            try{
                                Process p = Runtime.getRuntime().exec ("C:\\xampp\\xampp_stop.exe");
                            }catch (IOException ex){ 
                                System.out.println("No se encontro " + ex); 
                            }
                            System.exit(0);
                        }
                    }
                });
                
                sv.setVisible(true);
                dispose();
                
            } else{
                JOptionPane.showMessageDialog(null,"Correo o contraseña incorrectos");
            }
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jInternalFrame1 = new javax.swing.JInternalFrame();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        lblLogo = new javax.swing.JLabel();
        lblUsuario = new javax.swing.JLabel();
        pnlIconUsuario = new javax.swing.JPanel();
        lblIconUsuario = new javax.swing.JLabel();
        txtUsuario = new javax.swing.JTextField();
        lblContrasenia = new javax.swing.JLabel();
        pnlIconContrasenia = new javax.swing.JPanel();
        lblIconContrasenia = new javax.swing.JLabel();
        txtContrasenia = new javax.swing.JPasswordField();
        btnIngresar = new javax.swing.JButton();
        lblfondoTarjetaLogin = new javax.swing.JLabel();
        lblFondo = new javax.swing.JLabel();

        jInternalFrame1.setVisible(true);

        javax.swing.GroupLayout jInternalFrame1Layout = new javax.swing.GroupLayout(jInternalFrame1.getContentPane());
        jInternalFrame1.getContentPane().setLayout(jInternalFrame1Layout);
        jInternalFrame1Layout.setHorizontalGroup(
            jInternalFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jInternalFrame1Layout.setVerticalGroup(
            jInternalFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(0, 0, 0));

        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jPanel2.setOpaque(false);
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblLogo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/logo.png"))); // NOI18N
        jPanel2.add(lblLogo, new org.netbeans.lib.awtextra.AbsoluteConstraints(78, 14, -1, 98));

        lblUsuario.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblUsuario.setForeground(new java.awt.Color(255, 255, 255));
        lblUsuario.setText("Usuario");
        jPanel2.add(lblUsuario, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 120, -1, -1));

        pnlIconUsuario.setBackground(new java.awt.Color(67, 102, 129));

        lblIconUsuario.setBackground(new java.awt.Color(67, 102, 129));
        lblIconUsuario.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/iconUsuario.png"))); // NOI18N

        javax.swing.GroupLayout pnlIconUsuarioLayout = new javax.swing.GroupLayout(pnlIconUsuario);
        pnlIconUsuario.setLayout(pnlIconUsuarioLayout);
        pnlIconUsuarioLayout.setHorizontalGroup(
            pnlIconUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlIconUsuarioLayout.createSequentialGroup()
                .addComponent(lblIconUsuario)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        pnlIconUsuarioLayout.setVerticalGroup(
            pnlIconUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlIconUsuarioLayout.createSequentialGroup()
                .addComponent(lblIconUsuario)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jPanel2.add(pnlIconUsuario, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 140, 30, 30));

        txtUsuario.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 5));
        jPanel2.add(txtUsuario, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 140, 150, 30));

        lblContrasenia.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblContrasenia.setForeground(new java.awt.Color(255, 255, 255));
        lblContrasenia.setText("Contraseña");
        jPanel2.add(lblContrasenia, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 190, -1, -1));

        pnlIconContrasenia.setBackground(new java.awt.Color(67, 102, 129));

        lblIconContrasenia.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/iconContrasenia.png"))); // NOI18N

        javax.swing.GroupLayout pnlIconContraseniaLayout = new javax.swing.GroupLayout(pnlIconContrasenia);
        pnlIconContrasenia.setLayout(pnlIconContraseniaLayout);
        pnlIconContraseniaLayout.setHorizontalGroup(
            pnlIconContraseniaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlIconContraseniaLayout.createSequentialGroup()
                .addComponent(lblIconContrasenia)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        pnlIconContraseniaLayout.setVerticalGroup(
            pnlIconContraseniaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlIconContraseniaLayout.createSequentialGroup()
                .addComponent(lblIconContrasenia)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jPanel2.add(pnlIconContrasenia, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 210, 30, 30));

        txtContrasenia.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 5));
        txtContrasenia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtContraseniaActionPerformed(evt);
            }
        });
        jPanel2.add(txtContrasenia, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 210, 150, 30));

        btnIngresar.setBackground(new java.awt.Color(17, 16, 22));
        btnIngresar.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnIngresar.setForeground(new java.awt.Color(255, 255, 255));
        btnIngresar.setText("Ingresar");
        btnIngresar.setBorder(null);
        btnIngresar.setBorderPainted(false);
        btnIngresar.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        btnIngresar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnIngresarActionPerformed(evt);
            }
        });
        jPanel2.add(btnIngresar, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 260, 120, 37));

        lblfondoTarjetaLogin.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/fondoTarjetaLogin.png"))); // NOI18N
        jPanel2.add(lblfondoTarjetaLogin, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 240, 320));

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 70, 240, 320));

        lblFondo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/fondoLogin.png"))); // NOI18N
        lblFondo.setToolTipText("");
        jPanel1.add(lblFondo, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 810, 450));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtContraseniaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtContraseniaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtContraseniaActionPerformed

    private void btnIngresarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIngresarActionPerformed
        validar();
    }//GEN-LAST:event_btnIngresarActionPerformed

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
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new LoginVista().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnIngresar;
    private javax.swing.JInternalFrame jInternalFrame1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JLabel lblContrasenia;
    private javax.swing.JLabel lblFondo;
    private javax.swing.JLabel lblIconContrasenia;
    private javax.swing.JLabel lblIconUsuario;
    private javax.swing.JLabel lblLogo;
    private javax.swing.JLabel lblUsuario;
    private javax.swing.JLabel lblfondoTarjetaLogin;
    private javax.swing.JPanel pnlIconContrasenia;
    private javax.swing.JPanel pnlIconUsuario;
    private javax.swing.JPasswordField txtContrasenia;
    private javax.swing.JTextField txtUsuario;
    // End of variables declaration//GEN-END:variables
}
