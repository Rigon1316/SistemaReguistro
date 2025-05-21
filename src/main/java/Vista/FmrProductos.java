package Vista;

import Model.Producto;
import Negocio.ProductoServicio;
import java.awt.Color;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;

public class FmrProductos extends javax.swing.JInternalFrame {

    private ProductoServicio servicio;
    private final DefaultTableModel modelo;
    private List<Producto> listadoProductos;

    private final Border bordeRojo = BorderFactory.createLineBorder(Color.RED, 2);
    private final Border bordeNegro = BorderFactory.createLineBorder(Color.BLACK, 1);

    public FmrProductos() {
        initComponents();
        setClosable(true);
        setMaximizable(true);
        setIconifiable(true);
        setResizable(true);
        this.servicio = new ProductoServicio();
        this.modelo = new DefaultTableModel(
                new String[]{"Nombre", "Código", "Precio"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {

                return false;
            }
        };
        this.tbl_producto.setModel(modelo);
        ActualizarTablaRegistro();
        LimpiarFormulario();
    }

    private void RegistrarProducto() {
        if (ValidarFormulario()) {
            Producto nuevoProducto = GenerarDatosProducto();
            if (nuevoProducto == null) {
                return;
            }

            int registro = servicio.AgregarNuevoProducto(nuevoProducto);
            switch (registro) {
                case 0:
                    MostrarMensaje("Ya existe el producto con ese número de código.",
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
            }
        } else {
            MostrarMensaje("Debe completar todos los campos obligatorios.",
                    "Advertencia", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void ActualizarProducto() {
        int filaSeleccionada = this.tbl_producto.getSelectedRow();
        if (filaSeleccionada >= 0) {
            if (ValidarFormulario()) {
                Producto actualizarProducto = GenerarDatosProducto();
                if (actualizarProducto == null) {
                    return;
                }

                int idProducto = this.listadoProductos.get(filaSeleccionada).getId();
                boolean actualizado = this.servicio.ActualizarProducto(idProducto, actualizarProducto);

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

    private void EliminarProducto() {
        int filaSeleccionada = this.tbl_producto.getSelectedRow();

        if (filaSeleccionada >= 0) {
            int confirmacion = JOptionPane.showConfirmDialog(null,
                    "¿Estás seguro de eliminar este producto?",
                    "Confirmar eliminación",
                    JOptionPane.YES_NO_OPTION);

            if (confirmacion == JOptionPane.YES_OPTION) {
                int idProducto = this.listadoProductos.get(filaSeleccionada).getId();
                boolean eliminado = this.servicio.EliminarProductoPorId(idProducto);
                if (eliminado) {
                    MostrarMensaje("Producto eliminado correctamente.",
                            "", JOptionPane.INFORMATION_MESSAGE);
                    ActualizarTablaRegistro();
                    LimpiarFormulario();
                } else {
                    MostrarMensaje("No se pudo eliminar el producto.",
                            "", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        }
    }

    private void MostrarMensaje(String msm, String cabezera, int tipoPanel) {
        JOptionPane.showMessageDialog(null, msm, cabezera, tipoPanel);
    }

    private Producto GenerarDatosProducto() {
        String nombre = this.txt_Nombre.getText();
        String codigo = this.txt_Codigo.getText();
        float precio = Float.parseFloat(this.txt_Precio.getText());
        int stock = Integer.parseInt(this.txt_Stock.getText());
        Producto nuevoProducto = new Producto(precio, codigo, nombre, stock);
        return nuevoProducto;
    }

    private boolean ValidarFormulario() {
        this.txt_Nombre.setBorder(this.txt_Nombre.getText().isEmpty() ? this.bordeRojo : this.bordeNegro);
        this.txt_Codigo.setBorder(this.txt_Codigo.getText().isEmpty() ? this.bordeRojo : this.bordeNegro);
        this.txt_Precio.setBorder(this.txt_Precio.getText().isEmpty() ? this.bordeRojo : this.bordeNegro);
        this.txt_Stock.setBorder(this.txt_Stock.getText().isEmpty() ? this.bordeRojo : this.bordeNegro);

        return !(this.txt_Nombre.getText().isEmpty()
                || this.txt_Codigo.getText().isEmpty()
                || this.txt_Precio.getText().isEmpty()
                || this.txt_Stock.getText().isEmpty());
    }

    private void ActualizarTablaRegistro() {
        this.modelo.setRowCount(0);

        this.listadoProductos = this.servicio.ListarProductos();
        for (Producto productoActual : this.listadoProductos) {
            Object[] fila = new Object[]{
                productoActual.getNombre(),
                productoActual.getCodigo(),
                productoActual.getPrecio(),
                productoActual.getStock()
            };
            this.modelo.addRow(fila);
        }
    }

    private void LlenarDesdeFila() {
        int filaSeleccionada = this.tbl_producto.getSelectedRow();

        if (filaSeleccionada >= 0) {
            this.btn_Eliminar.setEnabled(true);
            this.btn_Actualizar.setEnabled(true);
            this.btn_Guardar.setEnabled(false);

            String codigo = this.tbl_producto.getValueAt(filaSeleccionada, 1).toString();
            String nombre = this.tbl_producto.getValueAt(filaSeleccionada, 0).toString();
            String precio = this.tbl_producto.getValueAt(filaSeleccionada, 2).toString();
            String stock = this.tbl_producto.getValueAt(filaSeleccionada, 3).toString();

            this.txt_Codigo.setText(nombre);
            this.txt_Nombre.setText(codigo);
            this.txt_Precio.setText(precio);
            this.txt_Stock.setText(stock);
        }
    }

    private void LimpiarFormulario() {
        txt_Codigo.setText("");
        txt_Nombre.setText("");
        txt_Precio.setText("");
        txt_Stock.setText("");
        tbl_producto.clearSelection();
        btn_Guardar.setEnabled(true);
        btn_Eliminar.setEnabled(false);
        btn_Actualizar.setEnabled(false);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        btn_Guardar = new javax.swing.JButton();
        btn_Eliminar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbl_producto = new javax.swing.JTable();
        txt_Codigo = new javax.swing.JTextField();
        txt_Nombre = new javax.swing.JTextField();
        txt_Precio = new javax.swing.JTextField();
        txt_Stock = new javax.swing.JTextField();
        btn_Actualizar = new javax.swing.JButton();

        jPanel1.setBackground(new java.awt.Color(215, 222, 233));
        jPanel1.setName(""); // NOI18N

        jLabel1.setBackground(new java.awt.Color(204, 204, 255));
        jLabel1.setFont(new java.awt.Font("Segoe UI", 2, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(51, 51, 51));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nuevo-producto (1).png"))); // NOI18N
        jLabel1.setText("Producto");

        jLabel3.setForeground(new java.awt.Color(0, 0, 0));
        jLabel3.setText("Codigo");

        jLabel4.setForeground(new java.awt.Color(0, 0, 0));
        jLabel4.setText("Nombre:");

        jLabel6.setForeground(new java.awt.Color(0, 0, 0));
        jLabel6.setText("Precio:");

        jLabel7.setForeground(new java.awt.Color(0, 0, 0));
        jLabel7.setText("Stock:");

        btn_Guardar.setBackground(new java.awt.Color(178, 235, 242));
        btn_Guardar.setForeground(new java.awt.Color(0, 0, 0));
        btn_Guardar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/base-de-datos.png"))); // NOI18N
        btn_Guardar.setText("Guardar");
        btn_Guardar.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_Guardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_GuardarActionPerformed(evt);
            }
        });

        btn_Eliminar.setBackground(new java.awt.Color(255, 182, 193));
        btn_Eliminar.setForeground(new java.awt.Color(0, 0, 0));
        btn_Eliminar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/eliminar-producto.png"))); // NOI18N
        btn_Eliminar.setText("Eliminar");
        btn_Eliminar.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_Eliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_EliminarActionPerformed(evt);
            }
        });

        tbl_producto.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Codigo", "Nombre", "Descripcion", "Precio", "Stock", "Categoria"
            }
        ));
        tbl_producto.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                tbl_productoAncestorAdded(evt);
            }
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });
        tbl_producto.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbl_productoMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tbl_producto);

        txt_Codigo.setBackground(new java.awt.Color(202, 210, 215));

        txt_Nombre.setBackground(new java.awt.Color(202, 210, 215));

        txt_Precio.setBackground(new java.awt.Color(202, 210, 215));

        txt_Stock.setBackground(new java.awt.Color(202, 210, 215));

        btn_Actualizar.setBackground(new java.awt.Color(255, 239, 179));
        btn_Actualizar.setForeground(new java.awt.Color(0, 0, 0));
        btn_Actualizar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/actualizacion-de-la-nube.png"))); // NOI18N
        btn_Actualizar.setText("Actualizar");
        btn_Actualizar.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_Actualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_ActualizarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(53, Short.MAX_VALUE)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 565, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(53, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(153, 153, 153)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(119, 119, 119)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txt_Stock, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txt_Precio, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txt_Nombre, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txt_Codigo, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(132, 132, 132)
                        .addComponent(btn_Guardar, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(56, 56, 56)
                        .addComponent(btn_Eliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(43, 43, 43)
                        .addComponent(btn_Actualizar, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 625, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(22, 22, 22))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(jLabel1)
                .addGap(32, 32, 32)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txt_Codigo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txt_Nombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(txt_Precio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(9, 9, 9)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(txt_Stock, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(41, 41, 41)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_Guardar, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_Eliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_Actualizar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(24, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn_GuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_GuardarActionPerformed
        RegistrarProducto();
    }//GEN-LAST:event_btn_GuardarActionPerformed

    private void btn_EliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_EliminarActionPerformed
        EliminarProducto();
    }//GEN-LAST:event_btn_EliminarActionPerformed

    private void tbl_productoAncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_tbl_productoAncestorAdded

    }//GEN-LAST:event_tbl_productoAncestorAdded

    private void btn_ActualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_ActualizarActionPerformed
        ActualizarProducto();
    }//GEN-LAST:event_btn_ActualizarActionPerformed

    private void tbl_productoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl_productoMouseClicked
        LlenarDesdeFila();
    }//GEN-LAST:event_tbl_productoMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_Actualizar;
    private javax.swing.JButton btn_Eliminar;
    private javax.swing.JButton btn_Guardar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tbl_producto;
    private javax.swing.JTextField txt_Codigo;
    private javax.swing.JTextField txt_Nombre;
    private javax.swing.JTextField txt_Precio;
    private javax.swing.JTextField txt_Stock;
    // End of variables declaration//GEN-END:variables
}
