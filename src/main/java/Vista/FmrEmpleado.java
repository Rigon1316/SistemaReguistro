package Vista;

import Model.Empleado;
import Model.Persona;
import Model.Rol;
import Negocio.PersonaServicio;
import Negocio.RolServicio;
import java.awt.Color;
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

public class FmrEmpleado extends javax.swing.JInternalFrame {

    private PersonaServicio servicio;
    private RolServicio rolservicio;
    private final DefaultTableModel modelo;
    private List<Persona> listadoPersonas;

    private final Border bordeRojo = BorderFactory.createLineBorder(Color.RED, 2);
    private final Border bordeNegro = BorderFactory.createLineBorder(Color.BLACK, 1);

    public FmrEmpleado() {
        initComponents();
        setClosable(true);
        setMaximizable(true);
        setIconifiable(true);
        setResizable(true);
        this.servicio = new PersonaServicio();
        this.rolservicio = new RolServicio();
        this.modelo = new DefaultTableModel(
                new String[]{"Nombre", "Apellido", "Cédula", "Correo electrónico",
                    "Fecha de nacimiento", "Edad", "Rol", "Activo"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // ninguna celda será editable
                return false;
            }
        };

        this.tbl_Empleado.setModel(modelo);
        cargarRoles();
        LimpiarFormulario();
    }

    public void cargarRoles() {
        try {
            // Limpiar el ComboBox primero
            cmb_Rol.removeAllItems();

            // Obtener la lista de roles desde el servicio
            List<Rol> roles = rolservicio.listarObjetosRol();

            // Agregar un elemento vacío o por defecto como un String
            cmb_Rol.addItem("-- Seleccione un rol --");

            // Agregar todos los roles al ComboBox como Strings (usando sus nombres)
            if (roles != null) {
                for (Rol rol : roles) {
                    cmb_Rol.addItem(rol.getNombre());
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error al cargar los roles: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void RegistrarNuevoEmpleado() {
        if (ValidarFormulario()) {
            Empleado nuevoEmpleado = GenerarDatosEmpleado();

            if (nuevoEmpleado == null) {
                return;
            }

            int registro = servicio.AgregarPersona(nuevoEmpleado);
            switch (registro) {
                case 0:
                    MostrarMensa("Ya existe la persona con ese número de cédula.",
                            "Advertencia", JOptionPane.WARNING_MESSAGE);
                    break;

                case 1:
                    MostrarMensa("Registro exitoso.",
                            "Información", JOptionPane.INFORMATION_MESSAGE);
                    ActualizarTablaRegistro();
                    LimpiarFormulario();
                    break;

                case 2:
                    MostrarMensa("Error interno, intentelo más tarde.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    break;

                case 3:
                    MostrarMensa("El sistema solo permite registrar a mayores de edad.",
                            "Advertencia", JOptionPane.QUESTION_MESSAGE);
                    break;
            }
        } else {
            MostrarMensa("Debe completar todos los campos obligatorios.",
                    "Advertencia", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void ActualizarDatosEmpleado() {
        int filaSeleccionada = this.tbl_Empleado.getSelectedRow();

        if (filaSeleccionada >= 0) {
            if (ValidarFormulario()) {
                Empleado actualizarEmpleado = GenerarDatosEmpleado();
                if (actualizarEmpleado == null) {
                    return;
                }
                int idPersona = this.listadoPersonas.get(filaSeleccionada).getId();
                boolean actualizado = this.servicio.ActualizarPersona(idPersona, actualizarEmpleado);

                if (actualizado) {
                    MostrarMensa("Registro actualizado.",
                            "Información", JOptionPane.INFORMATION_MESSAGE);
                    ActualizarTablaRegistro();
                    LimpiarFormulario();
                } else {
                    MostrarMensa("No se pudo actualizar el registro.",
                            "Advertencia", JOptionPane.WARNING_MESSAGE);
                }
            }
        }
    }

    private void EliminarRegistroPersona() {
        int filaSeleccionada = this.tbl_Empleado.getSelectedRow();

        if (filaSeleccionada >= 0) {
            int confirmacion = JOptionPane.showConfirmDialog(null,
                    "¿Estás seguro de eliminar esta persona?",
                    "Confirmar eliminación",
                    JOptionPane.YES_NO_OPTION);

            if (confirmacion == JOptionPane.YES_OPTION) {
                int idPersona = this.listadoPersonas.get(filaSeleccionada).getId();
                boolean eliminado = this.servicio.EliminarPersonaPorId(idPersona);
                if (eliminado) {
                    MostrarMensa("Persona eliminada correctamente.",
                            "", JOptionPane.INFORMATION_MESSAGE);
                    ActualizarTablaRegistro();
                    LimpiarFormulario();
                } else {
                    MostrarMensa("No se pudo eliminar la persona.",
                            "", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        }
    }

    private void MostrarMensa(String msm, String cabezera, int tipoPanel) {
        JOptionPane.showMessageDialog(null, msm, cabezera, tipoPanel);
    }

    private Empleado GenerarDatosEmpleado() {
        Empleado nuevoEmpleado = null;
        String nombre = this.txt_Nombre.getText();
        String apellido = this.txt_Apellido.getText();
        String numId = this.txt_Cedula.getText();
        String correo = this.txt_Correo.getText();
        boolean activo = this.cbx_Active.isSelected();

        try {
            // Obtener la fecha de nacimiento
            Date fechaSeleccionada = this.jxd_Fechanacimiento.getDate();
            if (fechaSeleccionada == null) {
                throw new IllegalArgumentException("Debe seleccionar una fecha válida.");
            }
            LocalDate fechaNacimiento = fechaSeleccionada.toInstant()
                    .atZone(ZoneId.systemDefault()).toLocalDate();

            // Obtener el rol seleccionado como String
            String nombreRol = (String) this.cmb_Rol.getSelectedItem();
            if (nombreRol == null || nombreRol.equals("-- Seleccione un rol --")) {
                throw new IllegalArgumentException("Debe seleccionar un rol válido.");
            }
            
            Rol rolSeleccionado = new Rol(nombreRol);

            nuevoEmpleado = new Empleado(nombre, apellido, numId, correo,
                    fechaNacimiento, rolSeleccionado, LocalDate.now(), activo);

        } catch (Exception ex) {
            MostrarMensa(ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        return nuevoEmpleado;
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

    private void LlenarFormularioDesdeTabla() {
        int filaSeleccionada = this.tbl_Empleado.getSelectedRow();

        if (filaSeleccionada >= 0) {
            this.btn_Eliminar.setEnabled(true);
            this.btn_Actualizar.setEnabled(true);
            this.btn_Agregar.setEnabled(false);

            String nombre = this.tbl_Empleado.getValueAt(filaSeleccionada, 0).toString();
            String apellido = this.tbl_Empleado.getValueAt(filaSeleccionada, 1).toString();
            String cedula = this.tbl_Empleado.getValueAt(filaSeleccionada, 2).toString();
            String correo = this.tbl_Empleado.getValueAt(filaSeleccionada, 3).toString();
            String fechaNacimientoStr = this.tbl_Empleado.getValueAt(filaSeleccionada, 4).toString();

            try {
                DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                LocalDate fechaNacimiento = LocalDate.parse(fechaNacimientoStr, formato);
                Date fechaConvertida = Date.from(fechaNacimiento.atStartOfDay(ZoneId.systemDefault()).toInstant());
                this.jxd_Fechanacimiento.setDate(fechaConvertida);
            } catch (DateTimeParseException e) {
                this.jxd_Fechanacimiento.setDate(null);
            }

            this.txt_Nombre.setText(nombre);
            this.txt_Apellido.setText(apellido);
            this.txt_Cedula.setText(cedula);
            this.txt_Correo.setText(correo);
        }
    }

    private void LimpiarFormulario() {
        txt_Nombre.setText("");
        txt_Apellido.setText("");
        txt_Cedula.setText("");
        txt_Correo.setText("");
        jxd_Fechanacimiento.setDate(null);

        tbl_Empleado.clearSelection();
        btn_Agregar.setEnabled(true);
        btn_Eliminar.setEnabled(false);
        btn_Actualizar.setEnabled(false);
    }

    private boolean ValidarFormulario() {
        this.txt_Nombre.setBorder(this.txt_Nombre.getText().isEmpty() ? this.bordeRojo : this.bordeNegro);
        this.txt_Apellido.setBorder(this.txt_Apellido.getText().isEmpty() ? this.bordeRojo : this.bordeNegro);
        this.txt_Cedula.setBorder(this.txt_Cedula.getText().isEmpty() ? this.bordeRojo : this.bordeNegro);
        this.txt_Correo.setBorder(this.txt_Correo.getText().isEmpty() ? this.bordeRojo : this.bordeNegro);
        this.jxd_Fechanacimiento.setBorder(this.jxd_Fechanacimiento.getDate() == null ? this.bordeRojo : this.bordeNegro);
        this.cmb_Rol.setBorder(this.cmb_Rol.getSelectedIndex() == 0 ? this.bordeRojo : this.bordeNegro);

        return !(this.txt_Nombre.getText().isEmpty() || this.txt_Apellido.getText().isEmpty()
                || this.txt_Cedula.getText().isEmpty() || this.txt_Correo.getText().isEmpty()
                || this.jxd_Fechanacimiento.getDate() == null || this.cmb_Rol.getSelectedIndex() == 0);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        lblLastnameUser = new javax.swing.JLabel();
        txt_Apellido = new javax.swing.JTextField();
        lblNameUser = new javax.swing.JLabel();
        txt_Nombre = new javax.swing.JTextField();
        lblNumIdUser = new javax.swing.JLabel();
        txt_Cedula = new javax.swing.JTextField();
        lblEmailUser = new javax.swing.JLabel();
        txt_Correo = new javax.swing.JTextField();
        lblDateUser = new javax.swing.JLabel();
        cmb_Rol = new javax.swing.JComboBox<>();
        lblDateUser1 = new javax.swing.JLabel();
        cbx_Active = new javax.swing.JCheckBox();
        jxd_Fechanacimiento = new org.jdesktop.swingx.JXDatePicker();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbl_Empleado = new javax.swing.JTable();
        btn_Agregar = new javax.swing.JButton();
        btn_Actualizar = new javax.swing.JButton();
        btn_Eliminar = new javax.swing.JButton();
        btn_Limpiar = new javax.swing.JButton();

        jPanel1.setBackground(new java.awt.Color(215, 222, 233));

        lblLastnameUser.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblLastnameUser.setForeground(new java.awt.Color(0, 0, 0));
        lblLastnameUser.setText("Apellido:");

        txt_Apellido.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        txt_Apellido.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_ApellidoActionPerformed(evt);
            }
        });

        lblNameUser.setBackground(new java.awt.Color(0, 0, 0));
        lblNameUser.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblNameUser.setForeground(new java.awt.Color(0, 0, 0));
        lblNameUser.setText("Nombre:");

        txt_Nombre.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        txt_Nombre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_NombreActionPerformed(evt);
            }
        });

        lblNumIdUser.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblNumIdUser.setForeground(new java.awt.Color(0, 0, 0));
        lblNumIdUser.setText("Cédula:");

        txt_Cedula.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        txt_Cedula.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_CedulaActionPerformed(evt);
            }
        });
        txt_Cedula.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_CedulaKeyTyped(evt);
            }
        });

        lblEmailUser.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblEmailUser.setForeground(new java.awt.Color(0, 0, 0));
        lblEmailUser.setText("Correo:");

        txt_Correo.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        txt_Correo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_CorreoActionPerformed(evt);
            }
        });

        lblDateUser.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblDateUser.setForeground(new java.awt.Color(0, 0, 0));
        lblDateUser.setText("Fecha de nacimiento:");

        cmb_Rol.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        cmb_Rol.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        lblDateUser1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblDateUser1.setForeground(new java.awt.Color(0, 0, 0));
        lblDateUser1.setText("Cargo:");

        cbx_Active.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        cbx_Active.setForeground(new java.awt.Color(0, 0, 0));
        cbx_Active.setText("Activado");
        cbx_Active.setIconTextGap(5);
        cbx_Active.setMaximumSize(new java.awt.Dimension(140, 40));
        cbx_Active.setMinimumSize(new java.awt.Dimension(140, 40));
        cbx_Active.setPreferredSize(new java.awt.Dimension(140, 40));

        tbl_Empleado.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Nombre", "Apellido", "Cedula", "Correo", "Fecha de nacimiento", "Edad"
            }
        ));
        tbl_Empleado.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbl_EmpleadoMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tbl_Empleado);

        btn_Agregar.setText("Agregar");
        btn_Agregar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_AgregarActionPerformed(evt);
            }
        });

        btn_Actualizar.setText("Actualizar");
        btn_Actualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_ActualizarActionPerformed(evt);
            }
        });

        btn_Eliminar.setText("Eliminar");
        btn_Eliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_EliminarActionPerformed(evt);
            }
        });

        btn_Limpiar.setText("Limpiar");
        btn_Limpiar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_LimpiarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 778, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(120, 120, 120)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(btn_Agregar)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(lblLastnameUser, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(lblNameUser, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(lblNumIdUser, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(lblEmailUser, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(txt_Correo, javax.swing.GroupLayout.DEFAULT_SIZE, 140, Short.MAX_VALUE)
                                .addComponent(txt_Cedula)
                                .addComponent(txt_Nombre)
                                .addComponent(txt_Apellido))
                            .addComponent(btn_Actualizar))
                        .addGap(84, 84, 84)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cbx_Active, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(lblDateUser, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(lblDateUser1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(44, 44, 44)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jxd_Fechanacimiento, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(cmb_Rol, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(btn_Eliminar)
                                .addGap(86, 86, 86)
                                .addComponent(btn_Limpiar)))))
                .addContainerGap(19, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(104, 104, 104)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_Nombre, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblNameUser, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblDateUser, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jxd_Fechanacimiento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_Apellido, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblLastnameUser, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblDateUser1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmb_Rol, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_Cedula, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblNumIdUser, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbx_Active, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_Correo, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblEmailUser, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(95, 95, 95)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_Agregar)
                    .addComponent(btn_Actualizar)
                    .addComponent(btn_Eliminar)
                    .addComponent(btn_Limpiar))
                .addGap(37, 37, 37)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 284, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(23, 23, 23))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txt_ApellidoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_ApellidoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_ApellidoActionPerformed

    private void txt_NombreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_NombreActionPerformed

    }//GEN-LAST:event_txt_NombreActionPerformed

    private void txt_CedulaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_CedulaKeyTyped
        char c = evt.getKeyChar();
        if (!Character.isDigit(c)) {
            evt.consume();
        }
    }//GEN-LAST:event_txt_CedulaKeyTyped

    private void txt_CorreoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_CorreoActionPerformed

    }//GEN-LAST:event_txt_CorreoActionPerformed

    private void txt_CedulaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_CedulaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_CedulaActionPerformed

    private void btn_AgregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_AgregarActionPerformed
        RegistrarNuevoEmpleado();
    }//GEN-LAST:event_btn_AgregarActionPerformed

    private void tbl_EmpleadoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl_EmpleadoMouseClicked
        LlenarFormularioDesdeTabla();
    }//GEN-LAST:event_tbl_EmpleadoMouseClicked

    private void btn_ActualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_ActualizarActionPerformed
        ActualizarDatosEmpleado();
    }//GEN-LAST:event_btn_ActualizarActionPerformed

    private void btn_EliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_EliminarActionPerformed
        EliminarRegistroPersona();
    }//GEN-LAST:event_btn_EliminarActionPerformed

    private void btn_LimpiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_LimpiarActionPerformed
        LimpiarFormulario();
    }//GEN-LAST:event_btn_LimpiarActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_Actualizar;
    private javax.swing.JButton btn_Agregar;
    private javax.swing.JButton btn_Eliminar;
    private javax.swing.JButton btn_Limpiar;
    private javax.swing.JCheckBox cbx_Active;
    private javax.swing.JComboBox<String> cmb_Rol;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private org.jdesktop.swingx.JXDatePicker jxd_Fechanacimiento;
    private javax.swing.JLabel lblDateUser;
    private javax.swing.JLabel lblDateUser1;
    private javax.swing.JLabel lblEmailUser;
    private javax.swing.JLabel lblLastnameUser;
    private javax.swing.JLabel lblNameUser;
    private javax.swing.JLabel lblNumIdUser;
    private javax.swing.JTable tbl_Empleado;
    private javax.swing.JTextField txt_Apellido;
    private javax.swing.JTextField txt_Cedula;
    private javax.swing.JTextField txt_Correo;
    private javax.swing.JTextField txt_Nombre;
    // End of variables declaration//GEN-END:variables
}
