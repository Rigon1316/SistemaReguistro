package Vista;

import Negocio.RolServicio;

import java.util.List;
import javax.swing.*;

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
        javax.swing.GroupLayout pnlLayout = new javax.swing.GroupLayout(pnl_Roles);
        pnl_Roles.setLayout(pnlLayout);
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

    private void eliminarRol(String nombreRol) {
        if (nombreRol.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese un nombre de rol para eliminar",
                    "Campo vacío", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(this,
                "¿Está seguro que desea eliminar el rol '" + nombreRol + "'?",
                "Confirmar eliminación", JOptionPane.YES_NO_OPTION);

        if (confirmacion == JOptionPane.YES_OPTION) {
            if (rolServicio.eliminarRol(nombreRol)) {
                JOptionPane.showMessageDialog(this, "Rol eliminado correctamente");
                txt_Rol.setText("");
                cargarRoles();

                if (fmrEmpleado != null) {
                    fmrEmpleado.cargarRoles();
                }
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo eliminar el rol. Puede que no exista o esté en uso.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void actualizarFormularioEmpleado() {
        if (fmrEmpleado != null) {
            fmrEmpleado.cargarRoles();
        }
    }


    public void setFormularioEmpleado(FmrEmpleado fmrEmpleado) {
        this.fmrEmpleado = fmrEmpleado;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txt_Rol = new javax.swing.JTextField();
        btn_Guardar = new javax.swing.JButton();
        btn_Eliminar = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        pnl_Roles = new javax.swing.JPanel();

        jPanel1.setBackground(new java.awt.Color(215, 222, 233));

        jLabel1.setBackground(new java.awt.Color(0, 0, 0));
        jLabel1.setForeground(new java.awt.Color(0, 0, 0));
        jLabel1.setText("Nuevo rol:");

        txt_Rol.setBackground(new java.awt.Color(202, 210, 215));

        btn_Guardar.setBackground(new java.awt.Color(178, 235, 242));
        btn_Guardar.setForeground(new java.awt.Color(0, 0, 0));
        btn_Guardar.setText("Guardar");
        btn_Guardar.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_Guardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_GuardarActionPerformed(evt);
            }
        });

        btn_Eliminar.setBackground(new java.awt.Color(255, 182, 193));
        btn_Eliminar.setForeground(new java.awt.Color(0, 0, 0));
        btn_Eliminar.setText("Eliminar");
        btn_Eliminar.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_Eliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_EliminarActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 0, 0));
        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/equipo.png"))); // NOI18N
        jLabel2.setText("Roles");

        javax.swing.GroupLayout pnl_RolesLayout = new javax.swing.GroupLayout(pnl_Roles);
        pnl_Roles.setLayout(pnl_RolesLayout);
        pnl_RolesLayout.setHorizontalGroup(
            pnl_RolesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 392, Short.MAX_VALUE)
        );
        pnl_RolesLayout.setVerticalGroup(
            pnl_RolesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 155, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(138, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(64, 64, 64)
                        .addComponent(jLabel2))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btn_Guardar, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(80, 80, 80)
                        .addComponent(txt_Rol, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_Eliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(103, 103, 103))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(85, 85, 85)
                .addComponent(pnl_Roles, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addGap(26, 26, 26)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txt_Rol, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(40, 40, 40)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_Eliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_Guardar, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(pnl_Roles, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(28, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
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
    private javax.swing.JButton btn_Eliminar;
    private javax.swing.JButton btn_Guardar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel pnl_Roles;
    private javax.swing.JTextField txt_Rol;
    // End of variables declaration//GEN-END:variables
}
