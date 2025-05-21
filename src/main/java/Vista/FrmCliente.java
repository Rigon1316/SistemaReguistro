package Vista;

import Model.Cliente;
import Model.Fidelidad;
import Model.Persona;
import Negocio.PersonaServicio;
import java.awt.Color;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;

public class FrmCliente extends javax.swing.JInternalFrame {

    private PersonaServicio servicio;
    private final DefaultTableModel modelo;
    private List<Persona> listadoPersonas;

    private final Border bordeRojo = BorderFactory.createLineBorder(Color.RED, 2);
    private final Border bordeNegro = BorderFactory.createLineBorder(Color.BLACK, 1);

    public FrmCliente() {
        initComponents();
        setClosable(true);
        setMaximizable(true);
        setIconifiable(true);
        setResizable(true);
        this.servicio = new PersonaServicio();

        this.modelo = new DefaultTableModel(
                new String[]{"Nombre", "Apellido", "Cédula", "Correo electrónico",
                    "Fecha de nacimiento", "Edad"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        this.tbl_Cliente.setModel(modelo);
        ActualizarTablaRegistro();
        LimpiarFormulario();
    }

    private void RegistrarNuevoCliente() {

        if (ValidarFormulario()) {
            Cliente nuevoCliente = GenerarDatosCliente();

            if (nuevoCliente == null) {
                return;
            }
            if (this.checkAfiliacion.isSelected()) {
                Fidelidad fidelidad = new Fidelidad(nuevoCliente, 0, 0, LocalDate.now());
                nuevoCliente.setFidelidad(fidelidad);
            }
            int registro = servicio.AgregarPersona(nuevoCliente);
            switch (registro) {
                case 0:
                    MostrarMensaje("Ya existe la persona con ese número de cédula.",
                            "Advertencia", JOptionPane.WARNING_MESSAGE);
                    break;

                case 1:
                    MostrarMensaje("Registro exitoso.",
                            "Información", JOptionPane.INFORMATION_MESSAGE);
                    ActualizarTablaRegistro();
                    LimpiarFormulario();
                    break;

                case 2:
                    MostrarMensaje("Error interno, intentelo más tarde.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    break;

                case 3:
                    MostrarMensaje("El sistema solo permite registrar a mayores de edad.",
                            "Advertencia", JOptionPane.QUESTION_MESSAGE);
                    break;
            }
        } else {
            MostrarMensaje("Debe completar todos los campos obligatorios.",
                    "Advertencia", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void ActualizarDatosPersona() {
        int filaSeleccionada = this.tbl_Cliente.getSelectedRow();

        if (filaSeleccionada >= 0) {
            if (ValidarFormulario()) {
                Cliente actualizarPersona = GenerarDatosCliente();
                if (actualizarPersona == null) {
                    return;
                }
                int idPersona = this.listadoPersonas.get(filaSeleccionada).getId();
                boolean actualizado = this.servicio.ActualizarPersona(idPersona, actualizarPersona);

                if (actualizado) {
                    MostrarMensaje("Registro actualizado.",
                            "Información", JOptionPane.INFORMATION_MESSAGE);
                    ActualizarTablaRegistro();
                    LimpiarFormulario();
                } else {
                    MostrarMensaje("No se pudo actualizar el registro.",
                            "Advertencia", JOptionPane.WARNING_MESSAGE);
                }
            }
        }
    }

    private void EliminarRegistroPersona() {
        int filaSeleccionada = this.tbl_Cliente.getSelectedRow();

        if (filaSeleccionada >= 0) {
            int confirmacion = JOptionPane.showConfirmDialog(null,
                    "¿Estás seguro de eliminar esta persona?",
                    "Confirmar eliminación",
                    JOptionPane.YES_NO_OPTION);

            if (confirmacion == JOptionPane.YES_OPTION) {
                int idPersona = this.listadoPersonas.get(filaSeleccionada).getId();
                boolean eliminado = this.servicio.EliminarPersonaPorId(idPersona);
                if (eliminado) {
                    MostrarMensaje("Persona eliminada correctamente.",
                            "", JOptionPane.INFORMATION_MESSAGE);
                    ActualizarTablaRegistro();
                    LimpiarFormulario();
                } else {
                    MostrarMensaje("No se pudo eliminar la persona.",
                            "", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        }
    }

    private void ActualizarTablaRegistro() {
        this.modelo.setRowCount(0);

        this.listadoPersonas = this.servicio.ListarPersonas();

        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        for (Persona personaActual : this.listadoPersonas) {
            Object[] fila = new Object[]{
                personaActual.getNombre(),
                personaActual.getApellido(),
                personaActual.getNumIdentificacion(),
                personaActual.getCorreo(),
                personaActual.getFechaNacimiento().format(formato), 
                personaActual.getEdad()
            };
            this.modelo.addRow(fila);
        }
    }

    private void MostrarMensaje(String msm, String cabezera, int tipoPanel) {
        JOptionPane.showMessageDialog(null, msm, cabezera, tipoPanel);
    }

    private Cliente GenerarDatosCliente() {
        Cliente nuevoCliente = null;
        String nombre = this.txt_nOMBRE.getText();
        String apellido = this.txt_Apellido.getText();
        String numId = this.txt_Cedula.getText();
        String correo = this.txt_Correo.getText();

        try {
            Date fechaSeleccionada = this.jxd_FechaNacimiento.getDate();
            if (fechaSeleccionada == null) {
                throw new DateTimeParseException("Fecha no seleccionada", "", 0);
            }

            Instant instant = fechaSeleccionada.toInstant();
            ZoneId zona = ZoneId.systemDefault();
            LocalDate fechaNacimiento = instant.atZone(zona).toLocalDate();

            String direccion = this.txt_Direccion.getText();

            nuevoCliente = new Cliente(nombre, apellido, numId, correo, fechaNacimiento, direccion);

        } catch (DateTimeParseException | NullPointerException ex) {
            MostrarMensaje("Fecha de nacimiento inválida o no seleccionada.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }

        return nuevoCliente;
    }

    private boolean ValidarFormulario() {
        this.txt_nOMBRE.setBorder(this.txt_nOMBRE.getText().isEmpty() ? this.bordeRojo : this.bordeNegro);
        this.txt_Apellido.setBorder(this.txt_Apellido.getText().isEmpty() ? this.bordeRojo : this.bordeNegro);
        this.txt_Cedula.setBorder(this.txt_Cedula.getText().isEmpty() ? this.bordeRojo : this.bordeNegro);
        this.txt_Correo.setBorder(this.txt_Correo.getText().isEmpty() ? this.bordeRojo : this.bordeNegro);

        boolean fechaInvalida = this.jxd_FechaNacimiento.getDate() == null;
        this.jxd_FechaNacimiento.setBorder(fechaInvalida ? this.bordeRojo : this.bordeNegro);

        return !(this.txt_nOMBRE.getText().isEmpty()
                || this.txt_Apellido.getText().isEmpty()
                || this.txt_Cedula.getText().isEmpty()
                || this.txt_Correo.getText().isEmpty()
                || fechaInvalida);
    }

    private void LlenarDesdeFila() {
        int filaSeleccionada = this.tbl_Cliente.getSelectedRow();

        if (filaSeleccionada >= 0) {
            this.btn_Eliminar.setEnabled(true);
            this.btn_Actualizar.setEnabled(true);
            this.btn_Agregar.setEnabled(false);

            String nombre = this.tbl_Cliente.getValueAt(filaSeleccionada, 0).toString();
            String apellido = this.tbl_Cliente.getValueAt(filaSeleccionada, 1).toString();
            String cedula = this.tbl_Cliente.getValueAt(filaSeleccionada, 2).toString();
            String correo = this.tbl_Cliente.getValueAt(filaSeleccionada, 3).toString();
            String fechaNacimientoStr = this.tbl_Cliente.getValueAt(filaSeleccionada, 4).toString();

            this.txt_nOMBRE.setText(nombre);
            this.txt_Apellido.setText(apellido);
            this.txt_Cedula.setText(cedula);
            this.txt_Correo.setText(correo);

            try {

                LocalDate fecha = LocalDate.parse(fechaNacimientoStr);
                this.jxd_FechaNacimiento.setDate(java.sql.Date.valueOf(fecha));
            } catch (Exception e) {
                MostrarMensaje("Error al convertir fecha: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void LimpiarFormulario() {
        txt_nOMBRE.setText("");
        txt_Apellido.setText("");
        txt_Cedula.setText("");
        txt_Correo.setText("");
        jxd_FechaNacimiento.setDate(null);

        tbl_Cliente.clearSelection();
        btn_Agregar.setEnabled(true);
        btn_Eliminar.setEnabled(false);
        btn_Actualizar.setEnabled(false);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel4 = new javax.swing.JPanel();
        lblDateUser1 = new javax.swing.JLabel();
        txt_Correo = new javax.swing.JTextField();
        txt_Direccion = new javax.swing.JTextField();
        lblDateUser = new javax.swing.JLabel();
        checkAfiliacion = new javax.swing.JCheckBox();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbl_Cliente = new javax.swing.JTable();
        lblLastnameUser = new javax.swing.JLabel();
        txt_Apellido = new javax.swing.JTextField();
        lblNameUser = new javax.swing.JLabel();
        txt_nOMBRE = new javax.swing.JTextField();
        lblNumIdUser = new javax.swing.JLabel();
        txt_Cedula = new javax.swing.JTextField();
        lblEmailUser = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        btn_Agregar = new javax.swing.JButton();
        btn_limpiar = new javax.swing.JButton();
        btn_Eliminar = new javax.swing.JButton();
        btn_Actualizar = new javax.swing.JButton();
        jxd_FechaNacimiento = new org.jdesktop.swingx.JXDatePicker();

        jPanel4.setBackground(new java.awt.Color(215, 222, 233));

        lblDateUser1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblDateUser1.setForeground(new java.awt.Color(0, 0, 0));
        lblDateUser1.setText("Dirección:");

        txt_Correo.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        txt_Correo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_CorreoActionPerformed(evt);
            }
        });

        txt_Direccion.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        txt_Direccion.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_DireccionKeyPressed(evt);
            }
        });

        lblDateUser.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblDateUser.setForeground(new java.awt.Color(0, 0, 0));
        lblDateUser.setText("Fecha de nacimiento:");

        checkAfiliacion.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        checkAfiliacion.setForeground(new java.awt.Color(0, 0, 0));
        checkAfiliacion.setText("Desea afiliarse");
        checkAfiliacion.setIconTextGap(5);
        checkAfiliacion.setMaximumSize(new java.awt.Dimension(140, 40));
        checkAfiliacion.setMinimumSize(new java.awt.Dimension(140, 40));
        checkAfiliacion.setPreferredSize(new java.awt.Dimension(140, 40));

        tbl_Cliente.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        tbl_Cliente.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Nombre", "Apellido", "Cédula", "Correo electrónico", "Fecha de nacimiento", "Edad"
            }
        ));
        tbl_Cliente.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbl_ClienteMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tbl_Cliente);

        lblLastnameUser.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblLastnameUser.setForeground(new java.awt.Color(0, 0, 0));
        lblLastnameUser.setText("Apellido:");

        txt_Apellido.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        txt_Apellido.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_ApellidoActionPerformed(evt);
            }
        });

        lblNameUser.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblNameUser.setForeground(new java.awt.Color(0, 0, 0));
        lblNameUser.setText("Nombre:");

        txt_nOMBRE.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        txt_nOMBRE.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_nOMBREActionPerformed(evt);
            }
        });

        lblNumIdUser.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblNumIdUser.setForeground(new java.awt.Color(0, 0, 0));
        lblNumIdUser.setText("Cédula:");

        txt_Cedula.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        txt_Cedula.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_CedulaKeyTyped(evt);
            }
        });

        lblEmailUser.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblEmailUser.setForeground(new java.awt.Color(0, 0, 0));
        lblEmailUser.setText("Correo:");

        jLabel19.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(0, 0, 0));
        jLabel19.setIcon(new javax.swing.ImageIcon(getClass().getResource("/chico.png"))); // NOI18N
        jLabel19.setText("Cliente");

        btn_Agregar.setText("Agregar");
        btn_Agregar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_AgregarActionPerformed(evt);
            }
        });

        btn_limpiar.setText("Limpiar");
        btn_limpiar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_limpiarActionPerformed(evt);
            }
        });

        btn_Eliminar.setText("Eliminar");
        btn_Eliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_EliminarActionPerformed(evt);
            }
        });

        btn_Actualizar.setText("Actualizar");
        btn_Actualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_ActualizarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel19)
                .addGap(318, 318, 318))
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 774, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(49, 49, 49)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblNameUser, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblLastnameUser, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblNumIdUser, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(70, 70, 70)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(txt_nOMBRE, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txt_Cedula, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txt_Apellido, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblDateUser, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblDateUser1, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblEmailUser, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(32, 32, 32)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txt_Correo)
                                    .addComponent(txt_Direccion)
                                    .addComponent(jxd_FechaNacimiento, javax.swing.GroupLayout.DEFAULT_SIZE, 190, Short.MAX_VALUE)))
                            .addComponent(checkAfiliacion, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(124, 124, 124)
                        .addComponent(btn_Agregar)
                        .addGap(93, 93, 93)
                        .addComponent(btn_limpiar)
                        .addGap(83, 83, 83)
                        .addComponent(btn_Eliminar)
                        .addGap(82, 82, 82)
                        .addComponent(btn_Actualizar)))
                .addContainerGap(18, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap(19, Short.MAX_VALUE)
                .addComponent(jLabel19)
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(txt_Correo, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(14, 14, 14)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblDateUser, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jxd_FechaNacimiento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txt_Direccion, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblDateUser1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txt_Cedula, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txt_nOMBRE, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblNameUser, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblEmailUser, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblLastnameUser, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txt_Apellido, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 16, Short.MAX_VALUE)
                        .addComponent(lblNumIdUser, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(checkAfiliacion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(47, 47, 47)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_Agregar)
                    .addComponent(btn_limpiar)
                    .addComponent(btn_Eliminar)
                    .addComponent(btn_Actualizar))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 237, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(14, 14, 14))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(16, 16, 16))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txt_CorreoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_CorreoActionPerformed

    }//GEN-LAST:event_txt_CorreoActionPerformed

    private void txt_DireccionKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_DireccionKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_DireccionKeyPressed

    private void tbl_ClienteMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl_ClienteMouseClicked
        LlenarDesdeFila();
    }//GEN-LAST:event_tbl_ClienteMouseClicked

    private void txt_ApellidoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_ApellidoActionPerformed

    }//GEN-LAST:event_txt_ApellidoActionPerformed

    private void txt_CedulaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_CedulaKeyTyped
        char c = evt.getKeyChar();
        // Si es un caracter se consume solo acepta de 0-9
        if (!Character.isDigit(c)) {
            evt.consume();
        }
    }//GEN-LAST:event_txt_CedulaKeyTyped

    private void txt_nOMBREActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_nOMBREActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_nOMBREActionPerformed

    private void btn_AgregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_AgregarActionPerformed
        RegistrarNuevoCliente();
    }//GEN-LAST:event_btn_AgregarActionPerformed

    private void btn_ActualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_ActualizarActionPerformed
        ActualizarDatosPersona();
    }//GEN-LAST:event_btn_ActualizarActionPerformed

    private void btn_EliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_EliminarActionPerformed
        EliminarRegistroPersona();
    }//GEN-LAST:event_btn_EliminarActionPerformed

    private void btn_limpiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_limpiarActionPerformed
        LimpiarFormulario();
    }//GEN-LAST:event_btn_limpiarActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_Actualizar;
    private javax.swing.JButton btn_Agregar;
    private javax.swing.JButton btn_Eliminar;
    private javax.swing.JButton btn_limpiar;
    private javax.swing.JCheckBox checkAfiliacion;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private org.jdesktop.swingx.JXDatePicker jxd_FechaNacimiento;
    private javax.swing.JLabel lblDateUser;
    private javax.swing.JLabel lblDateUser1;
    private javax.swing.JLabel lblEmailUser;
    private javax.swing.JLabel lblLastnameUser;
    private javax.swing.JLabel lblNameUser;
    private javax.swing.JLabel lblNumIdUser;
    private javax.swing.JTable tbl_Cliente;
    private javax.swing.JTextField txt_Apellido;
    private javax.swing.JTextField txt_Cedula;
    private javax.swing.JTextField txt_Correo;
    private javax.swing.JTextField txt_Direccion;
    private javax.swing.JTextField txt_nOMBRE;
    // End of variables declaration//GEN-END:variables
}
