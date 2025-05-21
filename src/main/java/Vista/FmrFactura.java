package Vista;

import Model.DetalleFactura;
import Model.Factura;
import Model.Persona;
import Model.Producto;
import Negocio.FacturaServicio;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class FmrFactura extends javax.swing.JInternalFrame {

    private List<DetalleFactura> detallesFactura = new ArrayList();
    private final FacturaServicio servicio;
    private Persona personaEncontrada;
    private Producto productoEncontrado;
    private final DefaultTableModel modelo;

    public FmrFactura() {
        this.servicio = new FacturaServicio();
        initComponents();
        setClosable(true);
        setMaximizable(true);
        setIconifiable(true);
        setResizable(true);

        this.modelo = new DefaultTableModel(
                new String[]{"Cantidad", "Descripción", "Precio Unitario", "Total"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        this.tbl_Factura.setModel(modelo);
    }

    private void BuscarPersona() {
        String cedula = this.txt_Cedula.getText();
        this.personaEncontrada = this.servicio.BuscarPersonaPorCedula(cedula);
        if (this.personaEncontrada != null) {
            this.txt_Nombre.setText(this.personaEncontrada.getNombre());
            this.txt_Apellido.setText(this.personaEncontrada.getApellido());
            this.txt_Cedula.setText(this.personaEncontrada.getNumIdentificacion());
            this.txt_Correo.setText(this.personaEncontrada.getCorreo());

        } else {

        }
    }

    private void BuscarProducto() {
        String codigo = this.txt_Codigo.getText();
        this.productoEncontrado = this.servicio.BuscarProductoPorCodigo(codigo);
        if (this.productoEncontrado != null) {
            this.txt_Precio.setText(String.valueOf(this.productoEncontrado.getPrecio()));
            this.txt_Producto.setText(this.productoEncontrado.getNombre());

        } else {

        }
    }

    private void AgregarDetalleFactura() {
        int cantidad = Integer.parseInt(this.txt_Cantidad.getText());
        if (this.productoEncontrado != null) {
            // int cantidad, float precioUnitario, Producto producto
            DetalleFactura nuevoDetalle = new DetalleFactura(cantidad,
                    this.productoEncontrado.getPrecio(), this.productoEncontrado);
            this.detallesFactura.add(nuevoDetalle);
            ActualizarTablaRegistro();
        }
    }

    private void EliminarDetalle() {
        int filaSeleccionada = this.tbl_Factura.getSelectedRow();

        if (filaSeleccionada >= 0) {
            int confirmacion = JOptionPane.showConfirmDialog(null,
                    "¿Estás seguro de eliminar el detalle: "
                    + this.detallesFactura.get(filaSeleccionada).getProducto().getNombre(),
                    "Confirmar eliminación",
                    JOptionPane.YES_NO_OPTION);

            if (confirmacion == JOptionPane.YES_OPTION) {
                this.detallesFactura.remove(filaSeleccionada);
                ActualizarTablaRegistro();
            }
        }
    }

    private void ActualizarTablaRegistro() {
        float subTotal = 0;
        this.modelo.setRowCount(0);
        for (DetalleFactura detalleActual : this.detallesFactura) {
            float total = detalleActual.getCantidad() * detalleActual.getProducto().getPrecio();
            subTotal += total;

            Object[] fila = new Object[]{
                detalleActual.getCantidad(),
                detalleActual.getProducto().getNombre(),
                detalleActual.getProducto().getPrecio(),
                total
            };
            this.modelo.addRow(fila);
        }
        float iva = subTotal * 0.15f;
        float total = iva + subTotal;
        this.Subtotal.setText(String.format("%.2f", subTotal));
        this.ElIVA.setText(String.format("%.2f", iva));
        this.Total.setText(String.format("%.2f", total));
    }

    private void RegistrarFactura() {
        if (this.personaEncontrada != null && this.detallesFactura.size() > 0) {
            Factura nuevaFactura = new Factura(this.personaEncontrada, this.detallesFactura);
            this.servicio.RegistrarNuevaFactura(nuevaFactura);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbl_Factura = new javax.swing.JTable();
        btn_Agregar = new javax.swing.JButton();
        btn_CrearFactura = new javax.swing.JButton();
        btn_EliminarDetalle = new javax.swing.JButton();
        Subtotal = new javax.swing.JLabel();
        ElIVA = new javax.swing.JLabel();
        Total = new javax.swing.JLabel();
        lbl_Subtotal = new javax.swing.JLabel();
        lbl_IVA = new javax.swing.JLabel();
        lbl_Total = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        txt_Apellido = new javax.swing.JTextField();
        txt_Nombre = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        txt_NumeroCedula = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        txt_Correo = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        btn_BuscarPersona = new javax.swing.JButton();
        txt_Cedula = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txt_Codigo = new javax.swing.JTextField();
        btn_BuscarProducto = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();
        txt_Producto = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txt_Cantidad = new javax.swing.JTextField();
        txt_Precio = new javax.swing.JTextField();

        setBackground(new java.awt.Color(0, 204, 255));
        setClosable(true);

        jPanel3.setBackground(new java.awt.Color(215, 222, 233));

        tbl_Factura.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane1.setViewportView(tbl_Factura);

        btn_Agregar.setBackground(new java.awt.Color(186, 245, 162));
        btn_Agregar.setForeground(new java.awt.Color(0, 0, 0));
        btn_Agregar.setText("Agregar Producto");
        btn_Agregar.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_Agregar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_AgregarActionPerformed(evt);
            }
        });

        btn_CrearFactura.setBackground(new java.awt.Color(178, 235, 242));
        btn_CrearFactura.setForeground(new java.awt.Color(0, 0, 0));
        btn_CrearFactura.setText("Crear Factura ");
        btn_CrearFactura.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_CrearFactura.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_CrearFacturaActionPerformed(evt);
            }
        });

        btn_EliminarDetalle.setBackground(new java.awt.Color(255, 182, 193));
        btn_EliminarDetalle.setForeground(new java.awt.Color(0, 0, 0));
        btn_EliminarDetalle.setText("Eliminar Detalle");
        btn_EliminarDetalle.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_EliminarDetalle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_EliminarDetalleActionPerformed(evt);
            }
        });

        Subtotal.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        Subtotal.setForeground(new java.awt.Color(44, 62, 80));
        Subtotal.setText("0");

        ElIVA.setForeground(new java.awt.Color(255, 160, 160));
        ElIVA.setText("0");

        Total.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        Total.setForeground(new java.awt.Color(30, 30, 30));
        Total.setText("0");

        lbl_Subtotal.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        lbl_Subtotal.setForeground(new java.awt.Color(44, 62, 80));
        lbl_Subtotal.setText("SUBTOTAL:");

        lbl_IVA.setForeground(new java.awt.Color(255, 160, 160));
        lbl_IVA.setText("IVA 15%");

        lbl_Total.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lbl_Total.setForeground(new java.awt.Color(30, 30, 30));
        lbl_Total.setText("TOTAL:");

        jPanel1.setBackground(new java.awt.Color(250, 250, 250));

        jLabel7.setForeground(new java.awt.Color(0, 0, 0));
        jLabel7.setText("Apellido:");

        txt_Apellido.setBackground(new java.awt.Color(202, 210, 215));

        txt_Nombre.setBackground(new java.awt.Color(202, 210, 215));

        jLabel8.setForeground(new java.awt.Color(0, 0, 0));
        jLabel8.setText("Numero de cedula:");

        txt_NumeroCedula.setBackground(new java.awt.Color(202, 210, 215));

        jLabel9.setForeground(new java.awt.Color(0, 0, 0));
        jLabel9.setText("Correo electronico:");

        txt_Correo.setBackground(new java.awt.Color(202, 210, 215));

        jLabel1.setBackground(new java.awt.Color(0, 0, 0));
        jLabel1.setForeground(new java.awt.Color(0, 0, 0));
        jLabel1.setText("Numero de cedula:");

        btn_BuscarPersona.setBackground(new java.awt.Color(197, 255, 197));
        btn_BuscarPersona.setForeground(new java.awt.Color(0, 0, 0));
        btn_BuscarPersona.setText("Buscar");
        btn_BuscarPersona.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_BuscarPersona.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_BuscarPersonaActionPerformed(evt);
            }
        });

        txt_Cedula.setBackground(new java.awt.Color(202, 210, 215));
        txt_Cedula.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_CedulaKeyTyped(evt);
            }
        });

        jLabel5.setForeground(new java.awt.Color(0, 0, 0));
        jLabel5.setText("Informacion del cliente");

        jLabel6.setForeground(new java.awt.Color(0, 0, 0));
        jLabel6.setText("Nombre:");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(txt_Nombre, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(34, 34, 34)
                            .addComponent(txt_Apellido, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txt_Correo)
                        .addComponent(txt_NumeroCedula, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txt_Cedula, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(49, 49, 49)
                                .addComponent(btn_BuscarPersona, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(35, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(6, 6, 6)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_Cedula, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_BuscarPersona, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(16, 16, 16)
                .addComponent(jLabel5)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_Apellido, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_Nombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(22, 22, 22)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txt_NumeroCedula, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txt_Correo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(10, Short.MAX_VALUE))
        );

        jPanel2.setBackground(new java.awt.Color(250, 250, 250));

        jLabel2.setForeground(new java.awt.Color(0, 0, 0));
        jLabel2.setText("Codigo:");

        txt_Codigo.setBackground(new java.awt.Color(202, 210, 215));
        txt_Codigo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_CodigoKeyTyped(evt);
            }
        });

        btn_BuscarProducto.setBackground(new java.awt.Color(197, 255, 197));
        btn_BuscarProducto.setForeground(new java.awt.Color(0, 0, 0));
        btn_BuscarProducto.setText("Buscar");
        btn_BuscarProducto.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_BuscarProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_BuscarProductoActionPerformed(evt);
            }
        });

        jLabel10.setForeground(new java.awt.Color(0, 0, 0));
        jLabel10.setText("Producto:");

        txt_Producto.setBackground(new java.awt.Color(202, 210, 215));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel10)
                        .addGap(29, 29, 29)
                        .addComponent(txt_Producto, javax.swing.GroupLayout.PREFERRED_SIZE, 213, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_Codigo, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 52, Short.MAX_VALUE)
                        .addComponent(btn_BuscarProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(37, 37, 37))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txt_Codigo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_BuscarProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(txt_Producto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(38, Short.MAX_VALUE))
        );

        jPanel4.setBackground(new java.awt.Color(250, 250, 250));
        jPanel4.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        jLabel3.setForeground(new java.awt.Color(0, 0, 0));
        jLabel3.setText("Cantidad:");

        jLabel4.setForeground(new java.awt.Color(0, 0, 0));
        jLabel4.setText("Precio:");

        txt_Cantidad.setBackground(new java.awt.Color(202, 210, 215));
        txt_Cantidad.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_CantidadKeyTyped(evt);
            }
        });

        txt_Precio.setBackground(new java.awt.Color(202, 210, 215));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(81, 81, 81)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txt_Cantidad, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(9, 9, 9)))
                .addGap(47, 47, 47)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txt_Precio, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(16, 16, 16)
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txt_Precio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_Cantidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(22, 22, 22))
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btn_EliminarDetalle, javax.swing.GroupLayout.PREFERRED_SIZE, 242, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(58, 58, 58)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btn_CrearFactura, javax.swing.GroupLayout.PREFERRED_SIZE, 242, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(lbl_Subtotal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(lbl_IVA, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lbl_Total, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(97, 97, 97)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(Subtotal, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(ElIVA, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(Total)))))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(38, 38, 38)
                                .addComponent(btn_Agregar, javax.swing.GroupLayout.PREFERRED_SIZE, 242, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(496, 496, 496))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(45, 45, 45)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGap(29, 29, 29))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 6, Short.MAX_VALUE))
                    .addComponent(jScrollPane1))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(Subtotal)
                            .addComponent(lbl_Subtotal))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(ElIVA)
                            .addComponent(lbl_IVA))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(Total)
                            .addComponent(lbl_Total))
                        .addGap(34, 34, 34))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(11, 11, 11)))
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_EliminarDetalle, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_CrearFactura, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_Agregar, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txt_CantidadKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_CantidadKeyTyped
        char c = evt.getKeyChar();
        if (!Character.isDigit(c)) {
            evt.consume();
        }
    }//GEN-LAST:event_txt_CantidadKeyTyped

    private void btn_BuscarProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_BuscarProductoActionPerformed
        BuscarPersona();
    }//GEN-LAST:event_btn_BuscarProductoActionPerformed

    private void txt_CodigoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_CodigoKeyTyped

    }//GEN-LAST:event_txt_CodigoKeyTyped

    private void txt_CedulaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_CedulaKeyTyped
        char c = evt.getKeyChar();
        if (!Character.isDigit(c)) {
            evt.consume();
        }
    }//GEN-LAST:event_txt_CedulaKeyTyped

    private void btn_BuscarPersonaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_BuscarPersonaActionPerformed
        BuscarPersona();

    }//GEN-LAST:event_btn_BuscarPersonaActionPerformed

    private void btn_EliminarDetalleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_EliminarDetalleActionPerformed
        EliminarDetalle();
    }//GEN-LAST:event_btn_EliminarDetalleActionPerformed

    private void btn_CrearFacturaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_CrearFacturaActionPerformed
        RegistrarFactura();
    }//GEN-LAST:event_btn_CrearFacturaActionPerformed

    private void btn_AgregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_AgregarActionPerformed
        AgregarDetalleFactura();
    }//GEN-LAST:event_btn_AgregarActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel ElIVA;
    private javax.swing.JLabel Subtotal;
    private javax.swing.JLabel Total;
    private javax.swing.JButton btn_Agregar;
    private javax.swing.JButton btn_BuscarPersona;
    private javax.swing.JButton btn_BuscarProducto;
    private javax.swing.JButton btn_CrearFactura;
    private javax.swing.JButton btn_EliminarDetalle;
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
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lbl_IVA;
    private javax.swing.JLabel lbl_Subtotal;
    private javax.swing.JLabel lbl_Total;
    private javax.swing.JTable tbl_Factura;
    private javax.swing.JTextField txt_Apellido;
    private javax.swing.JTextField txt_Cantidad;
    private javax.swing.JTextField txt_Cedula;
    private javax.swing.JTextField txt_Codigo;
    private javax.swing.JTextField txt_Correo;
    private javax.swing.JTextField txt_Nombre;
    private javax.swing.JTextField txt_NumeroCedula;
    private javax.swing.JTextField txt_Precio;
    private javax.swing.JTextField txt_Producto;
    // End of variables declaration//GEN-END:variables
}
