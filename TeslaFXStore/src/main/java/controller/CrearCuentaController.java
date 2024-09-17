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
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import model.Customer;

/**
 * FXML Controller class
 *
 * @author jhova
 */
public class CrearCuentaController implements Initializable {

    // Permite mover la ventana manteniendo pulsado el cursor
    private double xOffset = 0;
    private double yOffset = 0;

    // Crud a utilizar
    private CrudCustomer crudCustomer;
    // Customer
    private Customer customer;

    @FXML
    private AnchorPane mainPane;

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
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
    private Label label_edadIncorrecta;
    @FXML
    private Label label_camposVacios;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Conexión crud
        crudCustomer = new CrudCustomer();
        
        // Radius de esquinas
        recorteConEsquinas();

    }

    /**
     * Crear un recorte con esquinas redondeadas y aplicarlo al AnchorPane
     */
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
    private void abrirHomeSinSesion(MouseEvent event) {
        new AbrirVentanas().abrirHomeSinSesion(event);
    }

    // Abrir HomeConSesion
    @FXML
    private void abrirHomeConSesion(MouseEvent event) {
        String nombre = textfield_campoNombre.getText();
        String pais = textfield_campoPais.getText();
        String edad = textfield_campoEdad.getText();
        String email = textfield_campoCorreo.getText();
        String password = textfield_campoPassword.getText();
        boolean rellenaCampo = false;
        int edadT;

        // Verificar que los campos estén llenos
        if (!nombre.trim().equalsIgnoreCase("")
                && !pais.trim().equalsIgnoreCase("")
                && !edad.trim().equalsIgnoreCase("")
                && !email.trim().equalsIgnoreCase("")
                && !password.trim().equalsIgnoreCase("")) {
            rellenaCampo = true;
            label_camposVacios.setVisible(false);
        } else{
            label_camposVacios.setVisible(true);
        }
        
        // Comprobación de errores
        if (rellenaCampo) {
            // Comprobar que la edad esté en formato int
            try {
                edadT = Integer.parseInt(edad);
                label_edadIncorrecta.setVisible(false);
                // Comprobar que la edad esté entre 1 y 150
                if (edadT >= 1 && edadT <= 150) {
                    label_edadIncorrecta.setVisible(false);
                    // Acceder a HomeConSesion
                    var customer = new Customer(nombre, pais, edad, email, password);
                    var crud = new CrudCustomer();
                    crud.saveCustomer(customer);
                    new AbrirVentanas().abrirHomeConSesion(event, customer);
                } else{
                    label_edadIncorrecta.setVisible(true);
                }
            } catch (Exception e) {
                label_edadIncorrecta.setVisible(true);
            }
        }
    }

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

}
