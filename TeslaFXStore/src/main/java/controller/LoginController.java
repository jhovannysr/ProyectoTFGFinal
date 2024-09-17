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
import javafx.scene.control.Button;
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
public class LoginController implements Initializable {

    // Permite mover la ventana manteniendo pulsado el cursor
    private double xOffset = 0;
    private double yOffset = 0;

    // Customer
    private CrudCustomer crudCustomer;
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
    private TextField textfield_campoCorreo;
    @FXML
    private TextField textfield_campoPassword;
    @FXML
    private Button btn_Siguiente;
    @FXML
    private Label label_emailIncorrecto;
    @FXML
    private Label label_passwordIncorrecto;
    @FXML
    private Label label_camposVacios;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Abrir conexión
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
    @FXML
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

    @FXML
    private void abrirHomeConSesion(MouseEvent event) {
        String email = textfield_campoCorreo.getText();
        String password = textfield_campoPassword.getText();

        Customer customer = crudCustomer.findCustomer(email);

        // Abrir modo administrador
        if (email.equalsIgnoreCase("administradortesla001@gmail.com") && password.equalsIgnoreCase("123456789")) {
            new AbrirVentanas().abrirAdministratorMode(event);
        }
        
        //Los 2 campos con datos
        if (!email.trim().equalsIgnoreCase("") && !password.trim().equalsIgnoreCase("")) {
            label_camposVacios.setVisible(false);
            // Verificar si el customer existe
            if (customer != null) {
                label_emailIncorrecto.setVisible(false);
                // Verificar que la contraseña coincida con el correo
                if (customer.getPassword().equals(password)) {
                    label_passwordIncorrecto.setVisible(false);
                    // Abrir HomeConSesion
                    new AbrirVentanas().abrirHomeConSesion(event, customer);
                } else {
                    System.err.println("Login: Contraseña Incorrecta");
                    label_passwordIncorrecto.setVisible(true);
                }
            } else {
                System.err.println("Login: Email Incorrecto");
                label_emailIncorrecto.setVisible(true);
            }
        } else {
            label_camposVacios.setVisible(true);
        }
    }

    @FXML
    private void webProblemasIniciarSesion(MouseEvent event) {
        openWebProblemasIniciarSesion("https://www.tesla.com/es_ES/support/account-support?redirect=no");
    }

    // Método para abrir el enlace en el navegador
    private void openWebProblemasIniciarSesion(String url) {
        try {
            Desktop.getDesktop().browse(new URI(url));
        } catch (URISyntaxException ex) {
        } catch (IOException ex) {
        }
    }

    @FXML
    private void abrirCrearCuenta(MouseEvent event) {
        new AbrirVentanas().abrirCrearCuenta(event);
    }
}
