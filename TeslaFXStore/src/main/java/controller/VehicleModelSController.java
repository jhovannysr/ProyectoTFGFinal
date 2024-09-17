/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controller;

//import java.awt.Image;
import dao.Conexion;
import dao.CrudVehicle;
import dao.CrudVehicleStock;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import model.Customer;
import model.VehicleStock;
import model.vehicle.*;
import vehiclePrices.VehiclesPrices;

/**
 * FXML Controller class
 *
 * @author jhova
 */
public class VehicleModelSController implements Initializable {

    // Permite mover la ventana manteniendo pulsado el cursor
    private double xOffset = 0;
    private double yOffset = 0;
    @FXML
    private ImageView imageViewVehicle;

    // Lista de imagenes para carrusel
    private List<Image> images = new ArrayList<>();
    private int currentIndex = 0;

    //Tarifa Dual Motor or Plaid
    boolean bDualMotor = true;
    boolean bPlaid = false;

    // Wall Connector
    private boolean bWallConnector = false;

    // SlelfDrivingCapability EnhancedAutopilot
    private boolean bSelfDrivingCapability = false, bEnhancedAutopilot = false;

    // Paint
    boolean bPWhite = true, bPBlack = false, bPRed = false, bPBlue = false;

    // Formato miles, crear un DecimalFormat con el patrón de formato deseado
    DecimalFormat formatter = new DecimalFormat("#,###");

    // Price
    VehiclesPrices vehicleModelSPrices;
    private double price = 0;
    private double priceMensual;

    // Customer
    private Customer customer;

    @FXML
    private HBox hboxDualMotor;

    @FXML
    private HBox hboxPlaid;
    @FXML
    private Pane imgPaintRed;
    @FXML
    private Pane imgPaintBlue;
    @FXML
    private Pane imgPaintBlack;
    @FXML
    private Pane imgPaintWhite;
    @FXML
    private Button btn_enhancedAutopilot;
    @FXML
    private Button btn_selfDrivingCapability;
    @FXML
    private CheckBox wallConnector;
    @FXML
    private Label label_TotalPrice;
    @FXML
    private Label label_MensualPrice;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Prices Model S
        vehicleModelSPrices = new VehiclesPrices();
        // Actualizar los precios acorde al modelo
        vehicleModelSPrices.updatePriceModelS();

        // Cargar las 5 imágenes en la lista
        String car1 = "/images/vehiclesModelS/vehicleModelSCar.jpg";
        String car2 = "/images/vehiclesModelS/vehicleModelSCar2.jpg";
        String car3 = "/images/vehiclesModelS/vehicleModelSCar3.jpg";
        String car4 = "/images/vehiclesModelS/vehicleModelSCar4.jpg";
        cargarImagenesCarrusel(car1, car2, car3, car4);

        // Tarifa DualMotor or Plaid
        tarifa();
    }

    // Tarifa
    public void tarifa() {
        hboxDualMotor.getStyleClass().add("cuadrosHBox2");
        priceMensual = (int) vehicleModelSPrices.getPriceDualMotor() / 48;
        label_TotalPrice.setText("Total: " + (formatter.format(vehicleModelSPrices.getPriceDualMotor())) + "€");
        label_MensualPrice.setText("Plazo 48meses: " + (formatter.format(priceMensual)) + "€ /mes");
    }

    // Cargar las 5 imágenes en la lista
    private void cargarImagenesCarrusel(String car1, String car2, String car3, String car4) {
        images.add(new Image(getClass().getResource(car1).toExternalForm()));
        images.add(new Image(getClass().getResource(car2).toExternalForm()));
        images.add(new Image(getClass().getResource(car3).toExternalForm()));
        images.add(new Image(getClass().getResource(car4).toExternalForm()));
        images.add(new Image(getClass().getResource("/images/vehiclesModelS/vehicleModelSCar5.jpg").toExternalForm()));

        // Mostrar la primera imagen al inicializar
        if (!images.isEmpty()) {
            imageViewVehicle.setImage(images.get(currentIndex));
        }
    }

    @FXML
    private void abrirHomeConSesion(MouseEvent event) {
        new AbrirVentanas().abrirHomeConSesion(event, customer);
    }

    @FXML
    private void minimiarVentana(MouseEvent event) {
        // Obtener la Stage actual a partir del evento
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        // Minimizar la ventana
        stage.setIconified(true);
    }

    @FXML
    private void cerrarVentana(MouseEvent event) {
        // Obtener la Stage actual a partir del evento
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        // Cerrar la ventana
        stage.close();
        // Cerrar conexión
        Conexion.close();
    }

    @FXML
    private void handleMouseDragged(MouseEvent event) {
        // Obtiene la ventana actual (Stage) y la mueve a la nueva posición
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.setX(event.getScreenX() - xOffset);
        stage.setY(event.getScreenY() - yOffset);
    }

    @FXML
    private void handleMousePressed(MouseEvent event) {
        // Captura la posición inicial del mouse en la ventana
        xOffset = event.getSceneX();
        yOffset = event.getSceneY();
    }

    @FXML
    private void handleNext(MouseEvent event) {
        // Navegar a la siguiente imagen
        if (!images.isEmpty()) {
            currentIndex = (currentIndex + 1) % images.size(); // Ciclo hacia adelante
            imageViewVehicle.setImage(images.get(currentIndex));
        }
    }

    @FXML
    private void handlePrev(MouseEvent event) {
        // Navegar a la imagen anterior
        if (!images.isEmpty()) {
            currentIndex = (currentIndex - 1 + images.size()) % images.size(); // Ciclo hacia atrás
            imageViewVehicle.setImage(images.get(currentIndex));
        }
    }

    // Tarifa DualMotor
    @FXML
    private void handleDualMotor(MouseEvent event) {
        if (bPlaid == true) {
            bDualMotor = true;
            bPlaid = false;
            hboxDualMotor.getStyleClass().add("cuadrosHBox2");
            hboxPlaid.getStyleClass().remove("cuadrosHBox2");
            // Actualizar price
            pricesToPrint();
        }
    }

    // Tarifa Plaid
    @FXML
    private void handlePlaid(MouseEvent event) {
        if (bDualMotor == true) {
            bDualMotor = false;
            bPlaid = true;
            hboxPlaid.getStyleClass().add("cuadrosHBox2");
            hboxDualMotor.getStyleClass().remove("cuadrosHBox2");
            // Actualizar price
            pricesToPrint();
        }
    }

    // Paint controlls
    @FXML
    private void handlePaintRedSelect(MouseEvent event) {
        imgPaintRed.getStyleClass().add("paneImgBorder2");
        bPWhite = false;
        bPBlack = false;
        bPRed = true;
        bPBlue = false;
        selectOnlyPaint();
        String car1 = "/images/vehiclesModelS/vehicleModelSCarRed.jpg";
        String car2 = "/images/vehiclesModelS/vehicleModelSCarRed2.jpg";
        String car3 = "/images/vehiclesModelS/vehicleModelSCarRed3.jpg";
        String car4 = "/images/vehiclesModelS/vehicleModelSCarRed4.jpg";

        images.clear();
        cargarImagenesCarrusel(car1, car2, car3, car4);
    }

    @FXML
    private void handlePaintBlueSelect(MouseEvent event) {
        imgPaintBlue.getStyleClass().add("paneImgBorder2");
        bPWhite = false;
        bPBlack = false;
        bPRed = false;
        bPBlue = true;
        selectOnlyPaint();
        String car1 = "/images/vehiclesModelS/vehicleModelSCarBlue.jpg";
        String car2 = "/images/vehiclesModelS/vehicleModelSCarBlue2.jpg";
        String car3 = "/images/vehiclesModelS/vehicleModelSCarBlue3.jpg";
        String car4 = "/images/vehiclesModelS/vehicleModelSCarBlue4.jpg";

        images.clear();
        cargarImagenesCarrusel(car1, car2, car3, car4);
    }

    @FXML
    private void handlePaintBlackSelect(MouseEvent event) {
        imgPaintBlack.getStyleClass().add("paneImgBorder2");
        bPWhite = false;
        bPBlack = true;
        bPRed = false;
        bPBlue = false;
        selectOnlyPaint();
        String car1 = "/images/vehiclesModelS/vehicleModelSCarBlack.jpg";
        String car2 = "/images/vehiclesModelS/vehicleModelSCarBlack2.jpg";
        String car3 = "/images/vehiclesModelS/vehicleModelSCarBlack3.jpg";
        String car4 = "/images/vehiclesModelS/vehicleModelSCarBlack4.jpg";

        images.clear();
        cargarImagenesCarrusel(car1, car2, car3, car4);
    }

    @FXML
    private void handlePaintWhiteSelect(MouseEvent event) {
        imgPaintWhite.getStyleClass().add("paneImgBorder2");
        bPWhite = true;
        bPBlack = false;
        bPRed = false;
        bPBlue = false;
        selectOnlyPaint();
        String car1 = "/images/vehiclesModelS/vehicleModelSCar.jpg";
        String car2 = "/images/vehiclesModelS/vehicleModelSCar2.jpg";
        String car3 = "/images/vehiclesModelS/vehicleModelSCar3.jpg";
        String car4 = "/images/vehiclesModelS/vehicleModelSCar4.jpg";

        images.clear();
        cargarImagenesCarrusel(car1, car2, car3, car4);
    }

    private void selectOnlyPaint() {
        if (bPWhite == true) {
            imgPaintBlack.getStyleClass().remove("paneImgBorder2");
            imgPaintRed.getStyleClass().remove("paneImgBorder2");
            imgPaintBlue.getStyleClass().remove("paneImgBorder2");
        }
        if (bPBlack == true) {
            imgPaintWhite.getStyleClass().remove("paneImgBorder2");
            imgPaintRed.getStyleClass().remove("paneImgBorder2");
            imgPaintBlue.getStyleClass().remove("paneImgBorder2");
        }
        if (bPRed == true) {
            imgPaintBlack.getStyleClass().remove("paneImgBorder2");
            imgPaintWhite.getStyleClass().remove("paneImgBorder2");
            imgPaintBlue.getStyleClass().remove("paneImgBorder2");
        }
        if (bPBlue == true) {
            imgPaintBlack.getStyleClass().remove("paneImgBorder2");
            imgPaintRed.getStyleClass().remove("paneImgBorder2");
            imgPaintWhite.getStyleClass().remove("paneImgBorder2");
        }
    }

    // Añadir o quitar
    @FXML
    private void handleEnhancedAutopilot(MouseEvent event) {
        bEnhancedAutopilot = true;
        // Añadir tarifa 1
        if (btn_enhancedAutopilot.getText().equalsIgnoreCase("Añadir")) {
            btn_enhancedAutopilot.setText("Quitar");
            btn_enhancedAutopilot.getStyleClass().add("btnQuitar");
            // Eliminar tarifa 2
            if (bSelfDrivingCapability == true) {
                btn_selfDrivingCapability.setText("Añadir");
                btn_selfDrivingCapability.getStyleClass().remove("btnQuitar");
                bSelfDrivingCapability = false;
            }
            // Actualizar price
            pricesToPrint();
            // Eliminar tarifa 1
        } else {
            btn_enhancedAutopilot.setText("Añadir");
            btn_enhancedAutopilot.getStyleClass().remove("btnQuitar");
            bEnhancedAutopilot = false;
            // Actualizar price
            pricesToPrint();
        }
        System.out.println("Button1 Autopilot: " + bEnhancedAutopilot);
        System.out.println("Button1 Driving: " + bSelfDrivingCapability);
    }

    @FXML
    private void handleSelfDrivingCapability(MouseEvent event) {
        bSelfDrivingCapability = true;
        // Añadir Tarifa 2
        if (btn_selfDrivingCapability.getText().equalsIgnoreCase("Añadir")) {
            btn_selfDrivingCapability.setText("Quitar");
            btn_selfDrivingCapability.getStyleClass().add("btnQuitar");
            // Eliminar tarifa 1
            if (bEnhancedAutopilot == true) {
                btn_enhancedAutopilot.setText("Añadir");
                btn_enhancedAutopilot.getStyleClass().remove("btnQuitar");
                bEnhancedAutopilot = false;
            }
            // Actualizar price
            pricesToPrint();
            // Eliminar tarifa 2
        } else {
            btn_selfDrivingCapability.setText("Añadir");
            btn_selfDrivingCapability.getStyleClass().remove("btnQuitar");
            bSelfDrivingCapability = false;
            // Actualizar price
            pricesToPrint();
        }
    }

    @FXML
    private void handleWallConnector(MouseEvent event) {
        if (wallConnector.isSelected()) {
            bWallConnector = true;
            // Actualizar price
            pricesToPrint();
        } else {
            bWallConnector = false;
            // Actualizar price
            pricesToPrint();
        }
    }

    // Precios que se van actualizando 
    private void pricesToPrint() {
        price = 0;
        vehicleModelSPrices = new VehiclesPrices(
                bEnhancedAutopilot,
                bSelfDrivingCapability,
                bWallConnector,
                bDualMotor,
                bPlaid);
        vehicleModelSPrices.updatePriceModelS();
        price = vehicleModelSPrices.finalPriceModelSX();

        label_TotalPrice.setText("Total: " + (formatter.format(price)) + "€");
        priceMensual = (int) price / 48;
        label_MensualPrice.setText("Plazo 48meses: " + (formatter.format(priceMensual)) + "€ /mes");
    }

    private String vehiclePaint() {
        if (bPWhite == true) {
            return "Blanco";
        }
        if (bPBlack == true) {
            return "Black";
        }
        if (bPRed == true) {
            return "Red";
        }
        if (bPBlue == true) {
            return "Blue";
        }
        return "Default paint";
    }

    @FXML
    private void buyVehicle(MouseEvent event) {
        pricesToPrint();
        // Almacenar en MongoDB el vehiculo comprado
        TeslaVehicle vehicleModelS = new ModelS(
                bDualMotor,
                bPlaid,
                customer.getEmail(),
                vehiclePaint(),
                bEnhancedAutopilot,
                bSelfDrivingCapability,
                bWallConnector,
                price,
                "Model S");
        new CrudVehicle().saveTeslaVehicle(vehicleModelS);
        // Reducir Stock
        CrudVehicleStock crudStock = new CrudVehicleStock();
        VehicleStock stock = crudStock.findStock("ModelS");
        crudStock.updateStock(new VehicleStock(stock.getModel(), stock.getStock()-1));
        // Alert Compra realizada con exito
        showSuccessAlert();
        // Abrir Home con Sesion
        new AbrirVentanas().abrirHomeConSesion(event, customer);
    }

    // Método para mostrar el alert de compra realizada con éxito
    private void showSuccessAlert() {
        Alert alert = new Alert(AlertType.INFORMATION);  // Tipo de alert: Información
        alert.setTitle("Compra");                        // Título del alert
        alert.setHeaderText("");                       // Sin encabezado
        alert.setContentText("Compra realizada con éxito");  // Mensaje del alert

        alert.showAndWait();  // Mostrar el alert y esperar a que el usuario lo cierre
    }

    // Getters y Setters
    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

}
