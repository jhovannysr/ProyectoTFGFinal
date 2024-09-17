/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controller;

import dao.Conexion;
import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import model.Customer;

/**
 * FXML Controller class
 *
 * @author jhova
 */
public class HomeSinSesionController implements Initializable {

    // Permite mover la ventana manteniendo pulsado el cursor
    private double xOffset = 0;
    private double yOffset = 0;

    // Customer con el que hemos iniciado sesión
    private Customer customer;

    @FXML
    private AnchorPane mainPane;
    @FXML
    private AnchorPane mediaContainerVideo;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
//        btn_miSesion.setText(string);

        // Video de la portada
        videoPortada();

        // Esquinas redondeadas
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

    // Video de la portada
    private void videoPortada() {
        // Ruta al archivo de video
        String videoPath = getClass().getResource("/images/vidtesla.mp4").toExternalForm();

        // Crear objeto Media y MediaPlayer
        Media media = new Media(videoPath);
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        MediaView mediaView = new MediaView(mediaPlayer);

        // Configurar el MediaView
        mediaView.setFitWidth(1186); // Ajusta el tamaño según tus necesidades
        mediaView.setFitHeight(680);

        // Añadir MediaView al AnchorPane
        mediaContainerVideo.getChildren().add(mediaView);

        // Reproducir el video
        mediaPlayer.play();

        // Reiniciar el video al finalizar
        mediaPlayer.setOnEndOfMedia(() -> {
            mediaPlayer.seek(mediaPlayer.getStartTime()); // Volver al inicio
            mediaPlayer.play(); // Reanudar la reproducción
        });
    }

    // Almacena la posición de la escena
    @FXML
    private void handleMousePressed(MouseEvent event) {
        // Captura la posición inicial del mouse en la ventana
        xOffset = event.getSceneX();
        yOffset = event.getSceneY();
    }

    // Desplaza la ventana según la posición que tenga la escena 
    @FXML
    private void handleMouseDragged(MouseEvent event) {
        // Obtiene la ventana actual (Stage) y la mueve a la nueva posición
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.setX(event.getScreenX() - xOffset);
        stage.setY(event.getScreenY() - yOffset);
    }

    // Minimiza la ventana
    @FXML
    void minimizarVentana(MouseEvent event) {
        // Obtener la Stage actual a partir del evento
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        // Minimizar la ventana
        stage.setIconified(true);
    }

    // Cierra la ventana
    @FXML
    void cerrarVentana(MouseEvent event) {
        // Obtener la Stage actual a partir del evento
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        // Cerrar la ventana
        stage.close();
        // Cerrar conexión
        Conexion.close();
    }

    // Abrir Login y cerrar actual
    @FXML
    private void abrirLogin(MouseEvent event) {
        new AbrirVentanas().abrirLogin(event);
    }

    // Abre enlace de la página web oficial de Tesla
    @FXML
    private void webOficialTesla(MouseEvent event) {
        openWebTesla("https://www.tesla.com/es_es");
    }

    // Método para abrir el enlace en el navegador
    private void openWebTesla(String url) {
        try {
            Desktop.getDesktop().browse(new URI(url));
        } catch (URISyntaxException ex) {
        } catch (IOException ex) {
        }
    }

    @FXML
    private void webPruebaConduccionY(MouseEvent event) {
        openWebTeslaConduccion("https://www.tesla.com/es_es/drive?selectedModel=ModelY");
    }

    @FXML
    private void webPruebaConduccion3(MouseEvent event) {
        openWebTeslaConduccion("https://www.tesla.com/es_es/drive?selectedModel=Model3");
    }

    @FXML
    private void webPruebaConduccionS(MouseEvent event) {
        openWebTeslaConduccion("https://www.tesla.com/es_es/drive?selectedModel=ModelS");
    }

    @FXML
    private void webPruebaConduccionX(MouseEvent event) {
        openWebTeslaConduccion("https://www.tesla.com/es_es/drive?selectedModel=ModelX");
    }

    // Método para abrir el enlace en el navegador
    private void openWebTeslaConduccion(String url) {
        try {
            Desktop.getDesktop().browse(new URI(url));
        } catch (URISyntaxException ex) {
        } catch (IOException ex) {
        }
    }

    // Abre HomeSinSesion
    @FXML
    private void abrirHomeSinSesion(MouseEvent event) {
        new AbrirVentanas().abrirHomeSinSesion(event);
    }

    @FXML
    private void abrirLoginDesdeEncargar(MouseEvent event) {
        abrirLogin(event);
    }

}
