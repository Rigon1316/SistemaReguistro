package Vista;

import Model.DetalleFactura;
import java.util.ArrayList;
import java.util.List;
import Model.Factura;
import Model.Persona;
import Model.Producto;
import Negocio.FacturaServicio;
import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;

public class FormularioFactura extends javax.swing.JFrame {

    private List<DetalleFactura> detallesFactura = new ArrayList();

    private List<Factura> facturasCreadas = new ArrayList();

    private Persona personaEncontrada;

    private final FacturaServicio servicio;

    private Producto productoEncontrado;

    private DefaultTableModel modeloFactura;

    public FormularioFactura() {
        servicio = new FacturaServicio();
        initComponents();
        modeloFactura = new DefaultTableModel(
                new Object[]{"Cliente", "Total"}, 0);
        tbl_Factura.setModel(modeloFactura);

        controlarBotones(false);

        Factura mifactura = servicio.obtenerFacturaCompletaPorId(5);
        if (mifactura != null) {
            System.out.println("La factura del cliente es: " + mifactura.getPersona().getNombre());
            for (DetalleFactura detalle : mifactura.getDetalles()) {
                System.out.println("Producto: " + detalle.getProducto().getNombre()
                        + " - " + detalle.getProducto().getPrecio() + "," + detalle.getCantidad());
            }
        } else {
            System.out.println("No se encontró la factura con id 5");
        }
    }

    private void registrarFactura() {
        if (this.personaEncontrada != null && this.detallesFactura.size() > 0) {
            Factura nuevaFactura = new Factura(this.personaEncontrada, this.detallesFactura);
            this.servicio.RegistrarNuevaFactura(nuevaFactura);
            facturasCreadas.add(nuevaFactura);
            agregarFacturaATabla(nuevaFactura);
        }
    }

    private void agregarDetalleFactura() {
        int cantidad = Integer.parseInt(this.txt_Cantidad.getText());
        if (this.productoEncontrado != null) {
            DetalleFactura nuevoDetalle = new DetalleFactura(cantidad,
                    this.productoEncontrado.getPrecio(), this.productoEncontrado);
            this.detallesFactura.add(nuevoDetalle);

            for (DetalleFactura actualDetalle : this.detallesFactura) {
                System.out.println("Detalle: " + actualDetalle.getProducto().getNombre()
                        + ", " + actualDetalle.getProducto().getPrecio());
            }

        }
    }

    private double calcularTotalFactura() {
        double total = 0;
        for (DetalleFactura detalle : detallesFactura) {
            total += detalle.getCantidad() * detalle.getPrecioUnitario();
        }
        return total;
    }

    private void buscarPersona() {
        String cedula = this.txt_Cedula.getText();
        this.personaEncontrada = this.servicio.BuscarPersonaPorCedula(cedula);
        if (personaEncontrada != null) {
            System.out.println("La Persona es: " + this.personaEncontrada.getNombre());
        }
    }

    private void buscarProducto() {
        String codigo = this.txt_Codigo.getText();
        this.productoEncontrado = this.servicio.BuscarProductoPorCodigo(codigo);
        if (this.productoEncontrado != null) {
            System.out.println("El producto encontrado es: " + productoEncontrado.getNombre());
            this.txt_Precio.setText(String.valueOf(this.productoEncontrado.getPrecio()));
        }
    }

    private boolean validarFormularioDetalle() {
        Border bordeRojo = BorderFactory.createLineBorder(Color.RED, 2);
        Border bordeNegro = BorderFactory.createLineBorder(Color.BLACK, 1);

        boolean codigoValido = !txt_Codigo.getText().trim().isEmpty();
        boolean cantidadValida = !txt_Cantidad.getText().trim().isEmpty();
        boolean precioValido = !txt_Precio.getText().trim().isEmpty();

        txt_Codigo.setBorder(codigoValido ? bordeNegro : bordeRojo);
        txt_Cantidad.setBorder(cantidadValida ? bordeNegro : bordeRojo);
        txt_Precio.setBorder(precioValido ? bordeNegro : bordeRojo);

        if (!codigoValido || !cantidadValida || !precioValido) {
            JOptionPane.showMessageDialog(this, "Por favor complete todos los campos requeridos.");
            return false;
        }

        try {
            Integer.parseInt(txt_Cantidad.getText().trim());
            Double.parseDouble(txt_Precio.getText().trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "La cantidad debe ser un número entero y el precio debe ser un número decimal.");
            return false;
        }

        return true;
    }

    private void controlarBotones(boolean enModoAgregar) {
        btn_BuscarPersona.setEnabled(!enModoAgregar);
        btn_BuscarProducto.setEnabled(enModoAgregar);
        btn_Agregar.setEnabled(enModoAgregar);
        btn_CrearFactura.setEnabled(!enModoAgregar && !detallesFactura.isEmpty());

        txt_Codigo.setEditable(enModoAgregar);
        txt_Cantidad.setEditable(enModoAgregar);
        txt_Precio.setEditable(false);
    }

    private void limpiarCamposProducto() {
        txt_Codigo.setText("");
        txt_Cantidad.setText("");
        txt_Precio.setText("");
        this.productoEncontrado = null;
    }

    private void limpiarTodoFormulario() {
        txt_Cedula.setText("");
        limpiarCamposProducto();
        detallesFactura.clear();
        personaEncontrada = null;
        modeloFactura.setRowCount(0);
        controlarBotones(false);
    }

    private void mostrarResumenFactura() {
        JDialog resumenDialog = new JDialog(this, "Resumen de Factura", true);
        resumenDialog.setSize(500, 400);
        resumenDialog.setLocationRelativeTo(this);
        resumenDialog.setLayout(new BorderLayout());

        JTextArea resumenArea = new JTextArea();
        resumenArea.setEditable(false);

        StringBuilder resumen = new StringBuilder();
        resumen.append("Cliente: ").append(personaEncontrada.getNombre()).append("\n");
        resumen.append("Cédula: ").append(personaEncontrada.getCedula()).append("\n\n");
        resumen.append("Productos:\n");

        double total = 0;
        for (DetalleFactura detalle : detallesFactura) {
            resumen.append("- ").append(detalle.getProducto().getNombre())
                    .append(" | Cantidad: ").append(detalle.getCantidad())
                    .append(" | Precio Unitario: $").append(detalle.getPrecioUnitario())
                    .append("\n");
            total += detalle.getCantidad() * detalle.getPrecioUnitario();
        }

        resumen.append("\nTotal a pagar: $").append(String.format("%.2f", total));

        resumenArea.setText(resumen.toString());
        JScrollPane scroll = new JScrollPane(resumenArea);
        resumenDialog.add(scroll, BorderLayout.CENTER);

        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.addActionListener(e -> resumenDialog.dispose());
        JPanel panelBoton = new JPanel();
        panelBoton.add(btnCerrar);

        resumenDialog.add(panelBoton, BorderLayout.SOUTH);

        resumenDialog.setVisible(true);
    }

    private void actualizarTablaDetalles() {

        if (!(tbl_Factura.getModel() instanceof DefaultTableModel)) {
            modeloFactura = new DefaultTableModel(
                    new Object[]{"Código", "Producto", "Cantidad", "Precio Unit."}, 0);
            tbl_Factura.setModel(modeloFactura);
        } else {
            modeloFactura = (DefaultTableModel) tbl_Factura.getModel();
        }
        modeloFactura.setRowCount(0);

        for (DetalleFactura detalle : detallesFactura) {
            Object[] fila = {
                detalle.getProducto().getCodigo(),
                detalle.getProducto().getNombre(),
                detalle.getCantidad(),
                detalle.getPrecioUnitario()
            };
            modeloFactura.addRow(fila);
        }
    }

    private void agregarFacturaATabla(Factura factura) {

        if (!(tbl_Factura.getModel() instanceof DefaultTableModel)) {
            modeloFactura = new DefaultTableModel(
                    new Object[]{"Cliente", "Total"}, 0);
            tbl_Factura.setModel(modeloFactura);
        } else {
            modeloFactura = (DefaultTableModel) tbl_Factura.getModel();
        }

        double total = 0;
        for (DetalleFactura detalle : factura.getDetalles()) {
            total += detalle.getCantidad() * detalle.getPrecioUnitario();
        }

        modeloFactura.addRow(new Object[]{
            factura.getPersona().getNombre(),
            String.format("$%.2f", total)
        });
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        btn_BuscarPersona = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        txt_Cedula = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbl_Factura = new javax.swing.JTable();
        txt_Codigo = new javax.swing.JTextField();
        btn_BuscarProducto = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txt_Cantidad = new javax.swing.JTextField();
        txt_Precio = new javax.swing.JTextField();
        btn_Agregar = new javax.swing.JButton();
        btn_CrearFactura = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(204, 204, 255));

        jLabel1.setBackground(new java.awt.Color(0, 0, 0));
        jLabel1.setForeground(new java.awt.Color(0, 0, 0));
        jLabel1.setText("Numero de cedula");

        btn_BuscarPersona.setText("Buscar");
        btn_BuscarPersona.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_BuscarPersonaActionPerformed(evt);
            }
        });

        jLabel2.setForeground(new java.awt.Color(0, 0, 0));
        jLabel2.setText("Codigo:");

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

        btn_BuscarProducto.setText("Buscar");
        btn_BuscarProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_BuscarProductoActionPerformed(evt);
            }
        });

        jLabel3.setForeground(new java.awt.Color(0, 0, 0));
        jLabel3.setText("Cantidad:");

        jLabel4.setForeground(new java.awt.Color(0, 0, 0));
        jLabel4.setText("Precio:");

        btn_Agregar.setText("Agregar");
        btn_Agregar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_AgregarActionPerformed(evt);
            }
        });

        btn_CrearFactura.setText("Crear Factura ");
        btn_CrearFactura.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_CrearFacturaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(87, 87, 87)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(83, 83, 83)
                            .addComponent(txt_Codigo, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(jLabel1)
                            .addGap(61, 61, 61)
                            .addComponent(txt_Cedula, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txt_Cantidad, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(49, 49, 49)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txt_Precio, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 8, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btn_BuscarPersona)
                    .addComponent(btn_BuscarProducto)
                    .addComponent(btn_Agregar))
                .addGap(126, 126, 126))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btn_CrearFactura, javax.swing.GroupLayout.PREFERRED_SIZE, 452, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(29, 29, 29))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(55, 55, 55)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(btn_BuscarPersona)
                    .addComponent(txt_Cedula, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(36, 36, 36)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txt_Codigo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_BuscarProducto))
                .addGap(54, 54, 54)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_Cantidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_Precio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_Agregar))
                .addGap(45, 45, 45)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btn_CrearFactura, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27))
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

    private void btn_BuscarPersonaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_BuscarPersonaActionPerformed
        buscarPersona();
        if (personaEncontrada != null) {
            controlarBotones(true);
            JOptionPane.showMessageDialog(this, "Cliente encontrado: " + personaEncontrada.getNombre());
        } else {
            JOptionPane.showMessageDialog(this, "Cliente no encontrado. Verifique la cédula.");
        }
    }//GEN-LAST:event_btn_BuscarPersonaActionPerformed

    private void btn_BuscarProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_BuscarProductoActionPerformed
        buscarProducto();
        if (productoEncontrado != null) {
            JOptionPane.showMessageDialog(this, "Producto encontrado: " + productoEncontrado.getNombre());
        } else {
            JOptionPane.showMessageDialog(this, "Producto no encontrado. Verifique el código.");
        }
    }//GEN-LAST:event_btn_BuscarProductoActionPerformed

    private void btn_AgregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_AgregarActionPerformed
        if (validarFormularioDetalle()) {
            int cantidad = Integer.parseInt(txt_Cantidad.getText().trim());
            if (cantidad > 0) {
                DetalleFactura detalle = new DetalleFactura();
                detalle.setProducto(productoEncontrado);
                detalle.setCantidad(cantidad);
                detalle.setPrecioUnitario(productoEncontrado.getPrecio());
                detallesFactura.add(detalle);

                actualizarTablaDetalles();
                limpiarCamposProducto();
                btn_CrearFactura.setEnabled(!detallesFactura.isEmpty());
                JOptionPane.showMessageDialog(this, "Producto agregado correctamente.");
            } else {
                JOptionPane.showMessageDialog(this, "La cantidad debe ser mayor a cero.");
            }
        }
    }//GEN-LAST:event_btn_AgregarActionPerformed

    private void btn_CrearFacturaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_CrearFacturaActionPerformed
        if (!detallesFactura.isEmpty()) {
            mostrarResumenFactura();
            registrarFactura();

            JOptionPane.showMessageDialog(this, "Factura creada exitosamente.");
            limpiarTodoFormulario();
        } else {
            JOptionPane.showMessageDialog(this, "Debe agregar al menos un producto a la factura.");
        }
    }//GEN-LAST:event_btn_CrearFacturaActionPerformed

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
            java.util.logging.Logger.getLogger(FormularioFactura.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FormularioFactura.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FormularioFactura.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FormularioFactura.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FormularioFactura().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_Agregar;
    private javax.swing.JButton btn_BuscarPersona;
    private javax.swing.JButton btn_BuscarProducto;
    private javax.swing.JButton btn_CrearFactura;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tbl_Factura;
    private javax.swing.JTextField txt_Cantidad;
    private javax.swing.JTextField txt_Cedula;
    private javax.swing.JTextField txt_Codigo;
    private javax.swing.JTextField txt_Precio;
    // End of variables declaration//GEN-END:variables
}
