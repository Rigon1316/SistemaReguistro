package Vista;

import Model.DAO.RolServicio;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;


public class FmrRol extends javax.swing.JInternalFrame {

    private final RolServicio rolServicio;
    private final DefaultListModel<String> modeloLista;
    private final JList<String> listaRoles;
    private FmrEmpleado fmrEmpleado; 

    public FmrRol() {
        
        setClosable(true);
        setMaximizable(true);
        setIconifiable(true);
        setResizable(true);
        this.rolServicio = new RolServicio();
        this.modeloLista = new DefaultListModel<>();
        this.listaRoles = new JList<>(modeloLista);

        initComponents();

        
        JScrollPane scrollPane = new JScrollPane(listaRoles);
        javax.swing.GroupLayout pnlLayout = new javax.swing.GroupLayout(Pnl_Roles);
        Pnl_Roles.setLayout(pnlLayout);
        pnlLayout.setHorizontalGroup(
                pnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(scrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 414, Short.MAX_VALUE)
        );
        pnlLayout.setVerticalGroup(
                pnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(scrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
        );

        cargarRoles();
    }

    
    public FmrRol(FmrEmpleado fmrEmpleado) {
        this();
        this.fmrEmpleado = fmrEmpleado;
    }

    private void cargarRoles() {
        modeloLista.clear();
        List<String> roles = rolServicio.listarRoles();
        for (String rol : roles) {
            modeloLista.addElement(rol);
        }
    }

    private void guardarRol() {
        String nuevoRol = txt_Rol.getText().trim(); 
        if (nuevoRol.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese un nombre para el rol",
                    "Campo vacío", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (rolServicio.agregarRol(nuevoRol)) {
            JOptionPane.showMessageDialog(this, "Rol guardado correctamente");
            txt_Rol.setText("");
            cargarRoles();

            if (fmrEmpleado != null) {
                fmrEmpleado.cargarRoles();
            }
        } else {
            JOptionPane.showMessageDialog(this, "No se pudo guardar el rol. Puede que ya exista.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarRol() {
        String rolSeleccionado = listaRoles.getSelectedValue();
        if (rolSeleccionado == null) {
            JOptionPane.showMessageDialog(this, "Seleccione un rol para eliminar",
                    "Selección vacía", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(this,
                "¿Está seguro que desea eliminar el rol '" + rolSeleccionado + "'?",
                "Confirmar eliminación", JOptionPane.YES_NO_OPTION);

        if (confirmacion == JOptionPane.YES_OPTION) {
            if (rolServicio.eliminarRol(rolSeleccionado)) {
                JOptionPane.showMessageDialog(this, "Rol eliminado correctamente");
                cargarRoles();

                if (fmrEmpleado != null) {
                    fmrEmpleado.cargarRoles();
                }
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo eliminar el rol. Puede que esté en uso.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txt_Rol = new javax.swing.JTextField();
        btn_Guardar = new javax.swing.JButton();
        btn_Eliminar = new javax.swing.JButton();
        Pnl_Roles = new javax.swing.JPanel();

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setBackground(new java.awt.Color(0, 0, 0));
        jLabel1.setForeground(new java.awt.Color(0, 0, 0));
        jLabel1.setText("Nuevo rol:");

        btn_Guardar.setText("Guardar");
        btn_Guardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_GuardarActionPerformed(evt);
            }
        });

        btn_Eliminar.setText("Eliminar");
        btn_Eliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_EliminarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout Pnl_RolesLayout = new javax.swing.GroupLayout(Pnl_Roles);
        Pnl_Roles.setLayout(Pnl_RolesLayout);
        Pnl_RolesLayout.setHorizontalGroup(
            Pnl_RolesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 414, Short.MAX_VALUE)
        );
        Pnl_RolesLayout.setVerticalGroup(
            Pnl_RolesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(105, 105, 105)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btn_Guardar)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(80, 80, 80)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btn_Eliminar)
                    .addComponent(txt_Rol, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(17, Short.MAX_VALUE)
                .addComponent(Pnl_Roles, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(14, 14, 14))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(51, 51, 51)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txt_Rol, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(58, 58, 58)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_Guardar)
                    .addComponent(btn_Eliminar))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(Pnl_Roles, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(34, 34, 34))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn_GuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_GuardarActionPerformed
        guardarRol();
    }//GEN-LAST:event_btn_GuardarActionPerformed

    private void btn_EliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_EliminarActionPerformed
        eliminarRol();
    }//GEN-LAST:event_btn_EliminarActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel Pnl_Roles;
    private javax.swing.JButton btn_Eliminar;
    private javax.swing.JButton btn_Guardar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField txt_Rol;
    // End of variables declaration//GEN-END:variables
}
