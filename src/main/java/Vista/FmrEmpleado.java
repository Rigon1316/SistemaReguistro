package Vista;

import Model.DAO.RolServicio;
import Model.Empleado;
import Model.Persona;
import Model.Rol;
import Servicios.EmpleadoServicio;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;

public class FmrEmpleado extends javax.swing.JInternalFrame {

    private final EmpleadoServicio servicio;
    private final RolServicio rolServicio;
    private DefaultTableModel tabla;
    private String cedulaSeleccionada = null;
    private static FmrEmpleado instance;

    public static FmrEmpleado getInstance() {
        if (instance == null) {
            instance = new FmrEmpleado();
        }
        return instance;
    }

    public FmrEmpleado() {
        initComponents();
        setClosable(true);
        setMaximizable(true);
        setIconifiable(true);
        setResizable(true);
        servicio = new EmpleadoServicio();
        rolServicio = new RolServicio();
        inicializarTabla();
        tabla = (DefaultTableModel) tbl_Empleados.getModel();
        cargarDatos();
        cargarRoles();
        controlarBotones(true);

        tbl_Empleados.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int fila = tbl_Empleados.getSelectedRow();
                if (fila >= 0) {
                    cargarDatosDesdeFila(fila);
                    controlarBotones(false);
                }
            }
        });

    }

    public void cargarRoles() {
        List<String> roles = rolServicio.listarRoles();
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();

        for (String rol : roles) {
            model.addElement(rol);
        }

        if (model.getSize() == 0) {
            model.addElement("Sin roles");
        }

        cmb_Roles.setModel(model);
    }

    public void actualizarComboRoles() {
        cargarRoles();
    }

    private void inicializarTabla() {
        DefaultTableModel model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        model.addColumn("Cédula");
        model.addColumn("Nombre");
        model.addColumn("Apellido");
        model.addColumn("Rol");
        model.addColumn("Dirección");
        model.addColumn("Salario");
        model.addColumn("Turno");
        model.addColumn("Fecha Contratación");

        tbl_Empleados.setModel(model);
    }

    public void cargarDatos() {
        DefaultTableModel model = (DefaultTableModel) tbl_Empleados.getModel();
        model.setRowCount(0);

        List<Empleado> empleados = servicio.listarEmpleados();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        for (Empleado emp : empleados) {
            String roles = "";
            if (emp.getRoles() != null && !emp.getRoles().isEmpty()) {
                StringBuilder sb = new StringBuilder();
                for (Rol rol : emp.getRoles()) {
                    if (sb.length() > 0) {
                        sb.append(", ");
                    }
                    sb.append(rol.getNombre());
                }
                roles = sb.toString();
            }

            Object[] fila = {
                emp.getCedula(),
                emp.getNombre(),
                emp.getApellido(),
                roles,
                emp.getDireccion(),
                emp.getSalario(),
                emp.getTurno(),
                emp.getFechaContratacion().format(formatter)
            };
            model.addRow(fila);
        }

        limpiarCampos();
    }

    private void buscarEmpleado() {
        String criterio = txt_Cedula.getText().trim();

        if (!criterio.isEmpty()) {

            Empleado empleadoEncontrado = servicio.buscarPorCedula(criterio);

            if (empleadoEncontrado != null) {

                DefaultTableModel model = (DefaultTableModel) tbl_Empleados.getModel();
                model.setRowCount(0);

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

                String roles = "";
                if (empleadoEncontrado.getRoles() != null && !empleadoEncontrado.getRoles().isEmpty()) {
                    StringBuilder sb = new StringBuilder();
                    for (Rol rol : empleadoEncontrado.getRoles()) {
                        if (sb.length() > 0) {
                            sb.append(", ");
                        }
                        sb.append(rol.getNombre());
                    }
                    roles = sb.toString();
                }

                Object[] fila = {
                    empleadoEncontrado.getCedula(),
                    empleadoEncontrado.getNombre(),
                    empleadoEncontrado.getApellido(),
                    roles,
                    empleadoEncontrado.getDireccion(),
                    empleadoEncontrado.getSalario(),
                    empleadoEncontrado.getTurno(),
                    empleadoEncontrado.getFechaContratacion().format(formatter)
                };
                model.addRow(fila);

                cedulaSeleccionada = empleadoEncontrado.getCedula();
                txt_Cedula.setText(empleadoEncontrado.getCedula());
                txt_Nombre.setText(empleadoEncontrado.getNombre());
                txt_Apellido.setText(empleadoEncontrado.getApellido());

                if (empleadoEncontrado.getRoles() != null && !empleadoEncontrado.getRoles().isEmpty()) {
                    cmb_Roles.setSelectedItem(empleadoEncontrado.getRoles().get(0).getNombre());
                }

                txt_Direccion.setText(empleadoEncontrado.getDireccion());
                txt_Salario.setText(String.valueOf(empleadoEncontrado.getSalario()));
                cmb_Turno.setSelectedItem(empleadoEncontrado.getTurno());

                Date fechaConvertida = Date.from(
                        empleadoEncontrado.getFechaContratacion().atStartOfDay(ZoneId.systemDefault()).toInstant()
                );
                jdc_Fecha_Contratacion.setDate(fechaConvertida);

                controlarBotones(false);
            } else {

                Persona personaEncontrada = servicio.buscarPersonaPorCedula(criterio);

                if (personaEncontrada != null) {

                    txt_Cedula.setText(personaEncontrada.getCedula());
                    txt_Nombre.setText(personaEncontrada.getNombre());
                    txt_Apellido.setText(personaEncontrada.getApellido());

                    DefaultTableModel model = (DefaultTableModel) tbl_Empleados.getModel();
                    model.setRowCount(0);

                    txt_Direccion.setText("");
                    txt_Salario.setText("");
                    cmb_Turno.setSelectedIndex(0);
                    jdc_Fecha_Contratacion.setDate(null);

                    JOptionPane.showMessageDialog(this, "Persona encontrada. Complete los demás datos para registrarla como empleado.");
                    controlarBotones(true);
                } else {
                    JOptionPane.showMessageDialog(this, "No se encontró ninguna persona ni empleado con la cédula: " + criterio);
                    limpiarCampos();
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Por favor ingrese una cédula antes de buscar.");
        }
    }

    public void agregarEmpleado() {
        if (validarFormulario()) {
            try {
                String cedula = txt_Cedula.getText().trim();
                String nombre = txt_Nombre.getText().trim();
                String apellido = txt_Apellido.getText().trim();
                String rolSeleccionado = cmb_Roles.getSelectedItem().toString();
                String direccion = txt_Direccion.getText().trim();
                double salario = Double.parseDouble(txt_Salario.getText().trim());
                String turno = cmb_Turno.getSelectedItem().toString();
                String correo = txt_Correo.getText().trim();

                if (correo.isEmpty()) {
                    JOptionPane.showMessageDialog(this,
                            "El correo electrónico es obligatorio.",
                            "Error de validación", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Date fechaSeleccionada = jdc_Fecha_Contratacion.getDate();
                if (fechaSeleccionada == null) {
                    JOptionPane.showMessageDialog(this, "Por favor, seleccione una fecha de contratación válida.",
                            "Error de validación", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Date fechaNacimientoSeleccionada = jdc_fecha.getDate();
                if (fechaNacimientoSeleccionada == null) {
                    JOptionPane.showMessageDialog(this, "Por favor, seleccione una fecha de nacimiento válida.",
                            "Error de validación", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                LocalDate fechaContratacion = fechaSeleccionada.toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate();

                LocalDate fechaNacimiento = fechaNacimientoSeleccionada.toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate();

                Empleado empleado = new Empleado();
                empleado.setCedula(cedula);
                empleado.setNombre(nombre);
                empleado.setApellido(apellido);
                empleado.setCorreo(correo);

                List<Rol> listaRoles = new ArrayList<>();
                listaRoles.add(new Rol(rolSeleccionado));
                empleado.setRoles(listaRoles);

                empleado.setDireccion(direccion);
                empleado.setSalario(salario);
                empleado.setTurno(turno);
                empleado.setFechaContratacion(fechaContratacion);
                // Establecer la fecha de nacimiento
                empleado.setFecha_de_nacimiento(fechaNacimiento);

                EmpleadoServicio empleadoServicio = new EmpleadoServicio();
                Empleado empleadoExistente = empleadoServicio.buscarPorCedula(cedula);

                if (empleadoExistente != null) {
                    JOptionPane.showMessageDialog(this, "Ya existe un empleado con esta cédula.",
                            "Error de registro", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                boolean registrado = empleadoServicio.agregarEmpleado(empleado) == 1;

                if (registrado) {
                    JOptionPane.showMessageDialog(this, "Empleado registrado correctamente.");
                    cargarDatos();
                    limpiarCampos();
                } else {
                    JOptionPane.showMessageDialog(this, "No se pudo registrar el empleado. Intente nuevamente.",
                            "Error al registrar", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "El salario debe ser un valor numérico válido.",
                        "Error de formato", JOptionPane.ERROR_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error al registrar: " + e.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }

    private void cargarDatosDesdeFila(int fila) {
    // El código existente
    cedulaSeleccionada = tbl_Empleados.getValueAt(fila, 0).toString();
    txt_Cedula.setText(cedulaSeleccionada);
    txt_Nombre.setText(tbl_Empleados.getValueAt(fila, 1).toString());
    txt_Apellido.setText(tbl_Empleados.getValueAt(fila, 2).toString());

    String roles = tbl_Empleados.getValueAt(fila, 3).toString();
    if (!roles.isEmpty()) {
        String primerRol = roles.split(",")[0].trim();
        cmb_Roles.setSelectedItem(primerRol);
    }

    txt_Direccion.setText(tbl_Empleados.getValueAt(fila, 4).toString());
    txt_Salario.setText(tbl_Empleados.getValueAt(fila, 5).toString());
    cmb_Turno.setSelectedItem(tbl_Empleados.getValueAt(fila, 6).toString());

    Empleado empleado = servicio.buscarPorCedula(cedulaSeleccionada);
    if (empleado != null) {
        if (empleado.getCorreo() != null) {
            txt_Correo.setText(empleado.getCorreo());
        } else {
            txt_Correo.setText("");
        }
        
        // Cargar fecha de nacimiento si existe
        if (empleado.getFecha_de_nacimiento() != null) {
            Date fechaNacimiento = Date.from(
                empleado.getFecha_de_nacimiento().atStartOfDay(ZoneId.systemDefault()).toInstant()
            );
            jdc_fecha.setDate(fechaNacimiento);
        } else {
            jdc_fecha.setDate(null);
        }
    }

    String fechaTexto = tbl_Empleados.getValueAt(fila, 7).toString();
    try {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date fechaContratacion = formatter.parse(fechaTexto);
        jdc_Fecha_Contratacion.setDate(fechaContratacion);
    } catch (Exception e) {
        System.err.println("Error al parsear la fecha: " + e.getMessage());
        jdc_Fecha_Contratacion.setDate(null);
    }

    controlarBotones(false);
}

    private void actualizarEmpleado() {
        try {
            if (cedulaSeleccionada == null) {
                JOptionPane.showMessageDialog(this, "Por favor, selecciona un empleado de la tabla para actualizar.");
                return;
            }

            if (!validarFormulario()) {
                return;
            }

            String cedula = txt_Cedula.getText().trim();
            String nombre = txt_Nombre.getText().trim();
            String apellido = txt_Apellido.getText().trim();
            String rolSeleccionado = cmb_Roles.getSelectedItem().toString();
            String direccion = txt_Direccion.getText().trim();
            double salario = Double.parseDouble(txt_Salario.getText().trim());
            String turno = cmb_Turno.getSelectedItem().toString();
            String correo = txt_Correo.getText().trim();
            if (correo.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "El correo electrónico es obligatorio.",
                        "Error de validación", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Date fechaSeleccionada = jdc_Fecha_Contratacion.getDate();
            if (fechaSeleccionada == null) {
                JOptionPane.showMessageDialog(this, "Por favor, seleccione una fecha de contratación válida.",
                        "Error de validación", JOptionPane.ERROR_MESSAGE);
                return;
            }

            
            Date fechaNacimientoSeleccionada = jdc_fecha.getDate();
            if (fechaNacimientoSeleccionada == null) {
                JOptionPane.showMessageDialog(this, "Por favor, seleccione una fecha de nacimiento válida.",
                        "Error de validación", JOptionPane.ERROR_MESSAGE);
                return;
            }

            LocalDate fechaContratacion = fechaSeleccionada.toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();

            
            LocalDate fechaNacimiento = fechaNacimientoSeleccionada.toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();

            Empleado empleado = servicio.buscarPorCedula(cedulaSeleccionada);
            if (empleado != null) {
                empleado.setCedula(cedula);
                empleado.setNombre(nombre);
                empleado.setApellido(apellido);
                empleado.setCorreo(correo);

                List<Rol> listaRoles = new ArrayList<>();
                listaRoles.add(new Rol(rolSeleccionado));
                empleado.setRoles(listaRoles);

                empleado.setDireccion(direccion);
                empleado.setSalario(salario);
                empleado.setTurno(turno);
                empleado.setFechaContratacion(fechaContratacion);
               
                empleado.setFecha_de_nacimiento(fechaNacimiento);

                boolean actualizado = servicio.actualizarEmpleado(empleado);

                if (actualizado) {
                    JOptionPane.showMessageDialog(this, "Empleado actualizado correctamente.");
                    cargarDatos();
                    limpiarCampos();
                    controlarBotones(true);
                } else {
                    JOptionPane.showMessageDialog(this, "No se pudo actualizar el empleado.",
                            "Error de actualización", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al actualizar: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void eliminarEmpleado() {
        try {
            if (cedulaSeleccionada == null) {
                JOptionPane.showMessageDialog(this, "Por favor, selecciona un empleado de la tabla para eliminar.");
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(this,
                    "¿Está seguro de eliminar este empleado?", "Confirmación",
                    JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                boolean eliminado = servicio.eliminarEmpleado(cedulaSeleccionada);

                if (eliminado) {
                    JOptionPane.showMessageDialog(this, "Empleado eliminado correctamente.");
                    cargarDatos();
                    limpiarCampos();
                    controlarBotones(true);
                } else {
                    JOptionPane.showMessageDialog(this, "No se pudo eliminar el empleado.",
                            "Error de eliminación", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al eliminar: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean validarFormulario() {
    // Ejemplo básico de validación
    if (txt_Cedula.getText().trim().isEmpty() ||
            txt_Nombre.getText().trim().isEmpty() ||
            txt_Apellido.getText().trim().isEmpty() ||
            txt_Direccion.getText().trim().isEmpty() ||
            txt_Salario.getText().trim().isEmpty() ||
            txt_Correo.getText().trim().isEmpty() ||
            jdc_Fecha_Contratacion.getDate() == null ||
            jdc_fecha.getDate() == null) { // Validar fecha de nacimiento
        
        JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.",
                "Error de validación", JOptionPane.ERROR_MESSAGE);
        return false;
    }
    
    try {
        Double.parseDouble(txt_Salario.getText().trim());
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this, "El salario debe ser un valor numérico válido.",
                "Error de formato", JOptionPane.ERROR_MESSAGE);
        return false;
    }
    
    return true;
}

    private void controlarBotones(boolean enModoAgregar) {
        btn_Guardar.setEnabled(enModoAgregar);
        btn_Actualizar.setEnabled(!enModoAgregar);
        btn_Eliminar.setEnabled(!enModoAgregar);
        btn_Buscar.setEnabled(true);
    }

    private void limpiarCampos() {
    txt_Cedula.setText("");
    txt_Nombre.setText("");
    txt_Apellido.setText("");
    txt_Direccion.setText("");
    txt_Salario.setText("");
    txt_Correo.setText("");
    cmb_Turno.setSelectedIndex(0);
    jdc_Fecha_Contratacion.setDate(null);
    jdc_fecha.setDate(null); // Limpiar la fecha de nacimiento
    cedulaSeleccionada = null;
    controlarBotones(true);
}

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txt_Apellido = new javax.swing.JTextField();
        txt_Cedula = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbl_Empleados = new javax.swing.JTable();
        btn_Guardar = new javax.swing.JButton();
        btn_Buscar = new javax.swing.JButton();
        btn_Eliminar = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        cmb_Turno = new javax.swing.JComboBox<>();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        txt_Direccion = new javax.swing.JTextField();
        txt_Salario = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jdc_Fecha_Contratacion = new org.jdesktop.swingx.JXDatePicker();
        txt_Nombre = new javax.swing.JTextField();
        btn_Actualizar = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();
        cmb_Roles = new javax.swing.JComboBox<>();
        jLabel4 = new javax.swing.JLabel();
        txt_Correo = new javax.swing.JTextField();
        jdc_fecha = new org.jdesktop.swingx.JXDatePicker();
        jLabel5 = new javax.swing.JLabel();

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setBackground(new java.awt.Color(0, 0, 0));
        jLabel1.setForeground(new java.awt.Color(0, 0, 0));
        jLabel1.setText("Nombre:");

        jLabel2.setForeground(new java.awt.Color(0, 0, 0));
        jLabel2.setText("Apellido:");
        jLabel2.setToolTipText("");

        jLabel3.setForeground(new java.awt.Color(0, 0, 0));
        jLabel3.setText("Cedula:");

        tbl_Empleados.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(tbl_Empleados);

        btn_Guardar.setBackground(new java.awt.Color(153, 255, 0));
        btn_Guardar.setForeground(new java.awt.Color(0, 0, 0));
        btn_Guardar.setText("Guardar");
        btn_Guardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_GuardarActionPerformed(evt);
            }
        });

        btn_Buscar.setBackground(new java.awt.Color(153, 255, 0));
        btn_Buscar.setForeground(new java.awt.Color(0, 0, 0));
        btn_Buscar.setText("Buscar");
        btn_Buscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_BuscarActionPerformed(evt);
            }
        });

        btn_Eliminar.setBackground(new java.awt.Color(255, 0, 102));
        btn_Eliminar.setForeground(new java.awt.Color(0, 0, 0));
        btn_Eliminar.setText("Eliminar");
        btn_Eliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_EliminarActionPerformed(evt);
            }
        });

        jLabel6.setForeground(new java.awt.Color(0, 0, 0));
        jLabel6.setText("Direccion:");

        cmb_Turno.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Mañana", "Noche" }));

        jLabel7.setForeground(new java.awt.Color(0, 0, 0));
        jLabel7.setText("Salario");

        jLabel8.setForeground(new java.awt.Color(0, 0, 0));
        jLabel8.setText("Turno");

        jLabel9.setForeground(new java.awt.Color(0, 0, 0));
        jLabel9.setText("Fecha de Contratacion");

        btn_Actualizar.setBackground(new java.awt.Color(153, 255, 0));
        btn_Actualizar.setForeground(new java.awt.Color(0, 0, 0));
        btn_Actualizar.setText("Actualizar");
        btn_Actualizar.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        btn_Actualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_ActualizarActionPerformed(evt);
            }
        });

        jLabel10.setForeground(new java.awt.Color(0, 0, 0));
        jLabel10.setText("Roles:");

        cmb_Roles.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel4.setForeground(new java.awt.Color(0, 0, 0));
        jLabel4.setText("Correo:");

        jLabel5.setForeground(new java.awt.Color(0, 0, 0));
        jLabel5.setText("Fecha de nacimiento:");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jScrollPane1)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(94, 94, 94)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(btn_Guardar)
                                .addGap(76, 76, 76)
                                .addComponent(btn_Buscar)
                                .addGap(90, 90, 90)
                                .addComponent(btn_Eliminar))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGap(38, 38, 38))
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(18, 18, 18)))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(38, 38, 38)))
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(txt_Nombre, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE)
                                    .addComponent(txt_Apellido, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txt_Direccion, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txt_Correo, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txt_Cedula))
                                .addGap(68, 68, 68)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel7)
                                    .addComponent(jLabel8)
                                    .addComponent(jLabel9)
                                    .addComponent(jLabel10)
                                    .addComponent(jLabel5))))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(9, 9, 9)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(txt_Salario, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(cmb_Roles, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(cmb_Turno, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jdc_Fecha_Contratacion, javax.swing.GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE)
                                    .addComponent(jdc_fecha, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btn_Actualizar, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(86, 86, 86))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txt_Salario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cmb_Roles, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cmb_Turno, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jdc_Fecha_Contratacion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel4)
                                .addComponent(txt_Correo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jdc_fecha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btn_Eliminar)
                            .addComponent(btn_Guardar)
                            .addComponent(btn_Actualizar, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btn_Buscar)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(91, 91, 91)
                                .addComponent(jLabel7)
                                .addGap(16, 16, 16)
                                .addComponent(jLabel10)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel8)
                                .addGap(28, 28, 28)
                                .addComponent(jLabel9))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(79, 79, 79)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(12, 12, 12)
                                        .addComponent(jLabel3))
                                    .addComponent(txt_Cedula, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel1)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(txt_Nombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(txt_Apellido, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel2))))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(txt_Direccion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel6))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel5)
                        .addGap(0, 119, Short.MAX_VALUE)))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(16, 16, 16))
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

    private void btn_BuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_BuscarActionPerformed
        buscarEmpleado();
    }//GEN-LAST:event_btn_BuscarActionPerformed

    private void btn_GuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_GuardarActionPerformed
        agregarEmpleado();
    }//GEN-LAST:event_btn_GuardarActionPerformed

    private void btn_EliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_EliminarActionPerformed
        eliminarEmpleado();
    }//GEN-LAST:event_btn_EliminarActionPerformed

    private void btn_ActualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_ActualizarActionPerformed
        actualizarEmpleado();
    }//GEN-LAST:event_btn_ActualizarActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_Actualizar;
    private javax.swing.JButton btn_Buscar;
    private javax.swing.JButton btn_Eliminar;
    private javax.swing.JButton btn_Guardar;
    private javax.swing.JComboBox<String> cmb_Roles;
    private javax.swing.JComboBox<String> cmb_Turno;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private org.jdesktop.swingx.JXDatePicker jdc_Fecha_Contratacion;
    private org.jdesktop.swingx.JXDatePicker jdc_fecha;
    private javax.swing.JTable tbl_Empleados;
    private javax.swing.JTextField txt_Apellido;
    private javax.swing.JTextField txt_Cedula;
    private javax.swing.JTextField txt_Correo;
    private javax.swing.JTextField txt_Direccion;
    private javax.swing.JTextField txt_Nombre;
    private javax.swing.JTextField txt_Salario;
    // End of variables declaration//GEN-END:variables
}
