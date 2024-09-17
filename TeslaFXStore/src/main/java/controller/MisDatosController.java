/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controller;

import dao.Conexion;
import dao.CrudCustomer;
import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.scene.control.Alert.AlertType;
import model.Customer;

/**
 * FXML Controller class
 *
 * @author jhova
 */
public class MisDatosController implements Initializable {

    // Permite mover la ventana manteniendo pulsado el cursor
    private double xOffset = 0;
    private double yOffset = 0;

    private CrudCustomer crudCustomer;
    private Customer customer;

    @FXML
    private AnchorPane mainPane;

    @FXML
    private TextField textfield_campoNombre;
    @FXML
    private TextField textfield_campoPais;
    @FXML
    private TextField textfield_campoEdad;
    @FXML
    private TextField textfield_campoCorreo;
    @FXML
    private TextField textfield_campoPassword;
    @FXML
    private Button btn_Modificar;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        // Crud customer
        crudCustomer = new CrudCustomer();
        
        // Radius de esquinas
        recorteConEsquinas();

    }

    // Crear un recorte con esquinas redondeadas y aplicarlo al AnchorPane
    private void recorteConEsquinas() {
        Rectangle clip = new Rectangle();
        clip.setWidth(mainPane.getPrefWidth());
        clip.setHeight(mainPane.getPrefHeight());
        clip.setArcWidth(20); // Radio de las esquinas
        clip.setArcHeight(20);
        mainPane.setClip(clip);
    }

    // Minimiar ventana
    @FXML
    private void minimiarVentana(MouseEvent event) {
        // Obtener la Stage actual a partir del evento
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        // Minimizar la ventana
        stage.setIconified(true);
    }

    // Cerrar ventana
    @FXML
    private void cerrarVentana(MouseEvent event) {
        // Obtener la Stage actual a partir del evento
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        // Cerrar la ventana
        stage.close();
        // Cerrar conexión
        Conexion.close();
    }

    // Desactivar el focus del campo al dar clic fuera
    private void desactivarFocusField(MouseEvent event) {
        textfield_campoPassword.setFocusTraversable(false);
        textfield_campoCorreo.setFocusTraversable(false);
        mainPane.requestFocus();
    }

    // Almacena la posición de la scena
    @FXML
    private void handleMousePressed(MouseEvent event) {
        // Captura la posición inicial del mouse en la ventana
        xOffset = event.getSceneX();
        yOffset = event.getSceneY();
    }

    // Permite mover la ventana según la posición de la scena
    @FXML
    private void handleMouseDragged(MouseEvent event) {
        // Obtiene la ventana actual (Stage) y la mueve a la nueva posición
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.setX(event.getScreenX() - xOffset);
        stage.setY(event.getScreenY() - yOffset);
    }

    // Cambiar de ventana a la principal Home Sin Sesion
    @FXML
    private void abrirHomeConSesion(MouseEvent event) {
        new AbrirVentanas().abrirHomeConSesion(event, customer);
    }

    // Abrir HomeConSesion
    @FXML
    private void updateCustomer(MouseEvent event) {
        if (btn_Modificar.getText().equals("Modificar")) {
            btn_Modificar.setText("Guardar");
            camposDeshabilitados(false);
        } else if (btn_Modificar.getText().equals("Guardar")) {
            btn_Modificar.setText("Modificar");

            customer = new Customer(textfield_campoNombre.getText(),
                     textfield_campoPais.getText(),
                     textfield_campoEdad.getText(),
                     textfield_campoCorreo.getText(),
                     textfield_campoPassword.getText());
            crudCustomer.updateCustomer(customer);
            camposDeshabilitados(true);
            mensajeAlert("Usuario Modificado");
        }
    }

    private void camposDeshabilitados(Boolean habilitar) {
        textfield_campoNombre.setDisable(habilitar);
        textfield_campoPais.setDisable(habilitar);
        textfield_campoEdad.setDisable(habilitar);
//        textfield_campoCorreo.setDisable(habilitar);
        textfield_campoPassword.setDisable(habilitar);
    }

    // Mensaje Alert
    private void mensajeAlert(String mensaje) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Confirmación");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    // WEb Aviso de privacidad
    @FXML
    private void webAvisoDePrivacidad(MouseEvent event) {
        openWebAvisoDePrivacidad("https://www.tesla.com/es_es/legal/privacy");
    }

    // Método para abrir el enlace en el navegador
    private void openWebAvisoDePrivacidad(String url) {
        try {
            Desktop.getDesktop().browse(new URI(url));
        } catch (URISyntaxException ex) {
        } catch (IOException ex) {
        }
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
        actualizarInterfazConCustomer();
    }

    // Método para actualizar la interfaz cuando customer está configurado
    private void actualizarInterfazConCustomer() {
        if (customer != null) {
            textfield_campoNombre.setText(customer.getCustomerName());
            textfield_campoPais.setText(customer.getCountry());
            textfield_campoEdad.setText(customer.getAge());
            textfield_campoCorreo.setText(customer.getEmail());
            textfield_campoPassword.setText(customer.getPassword());

        } else {
            System.err.println("Customer aún no está asignado.");
        }
    }

    @FXML
    private void eliminarCuenta(MouseEvent event) {
        crudCustomer.deleteCustomer(customer.getEmail());
        mensajeAlert("Cuenta eliminada");
        new AbrirVentanas().abrirHomeSinSesion(event);
    }

}
