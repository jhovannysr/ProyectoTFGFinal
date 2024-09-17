package controller;

import dao.*;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import model.*;
import model.vehicle.*;
import org.bson.types.ObjectId;
import vehiclePrices.VehiclesPrices;

/**
 * FXML Controller class
 *
 * @author jhova
 */
public class AdministratorModeController implements Initializable {

    // Permite mover la ventana manteniendo pulsado el cursor
    private double xOffset = 0;
    private double yOffset = 0;

    // CrudCustomer
    CrudCustomer crudCustomer;

    // CrudStock
    CrudVehicleStock crudStock;

    // CrudVehicle
    CrudVehicle crudVehicle;

    // Formato miles, crear un DecimalFormat con el patrón de formato deseado
    DecimalFormat formatter = new DecimalFormat("#,###");

    @FXML
    private AnchorPane mainPane;
    @FXML
    private TextField field_BusquedaCustomer;
    @FXML
    private TextField field_NameCustomer;
    @FXML
    private TextField field_CountryCustomer;
    @FXML
    private TextField field_AgeCustomer;
    @FXML
    private TextField field_EmailCustomer;
    @FXML
    private TextField field_PasswordCustomer;
    @FXML
    private TextArea textArea_DatosCustomer;
    @FXML
    private VBox vbox_camposCustomer;
    @FXML
    private HBox hbox_textAreaCustomer;
    @FXML
    private Button button_siguienteCustomer;
    @FXML
    private Button button_buscarCustomer;

    // Stock
    @FXML
    private TextField field_BusquedaStock;
    @FXML
    private TextArea textArea_DatosStock;
    @FXML
    private TextField field_ModelYStock;
    @FXML
    private TextField field_ModelXStock;
    @FXML
    private TextField field_Model3Stock;
    @FXML
    private TextField field_ModelSStock;
    @FXML
    private Button button_siguienteStock;
    @FXML
    private HBox hbox_textAreaStock;
    @FXML
    private Button button_buscarStock;
    @FXML
    private VBox vbox_camposStock;

    // Vehicle
    @FXML
    private TextField field_BusquedaVehicle;
    @FXML
    private TextArea textArea_DatosVehicle;
    @FXML
    private Button button_buscarVehicle;
    @FXML
    private VBox vbox_camposVehicle;
    @FXML
    private Button button_siguienteVehicle;
    @FXML
    private HBox hbox_textAreaVehicle;
    @FXML
    private TextField field_EmailVehicle;
    @FXML
    private TextField field_PaintVehicle;
    @FXML
    private TextField field_PriceVehicle;
    @FXML
    private VBox vbox_Tarifa1Vehicle;
    @FXML
    private VBox vbox_Tarifa2PlaidDualMotorVehicle;
    @FXML
    private RadioButton rButton_EnhancedAutopilot;
    @FXML
    private RadioButton rButton_selfDrivingCapability;
    @FXML
    private CheckBox check_wallConnector;
    @FXML
    private RadioButton rButton_rearWheelDrive;
    @FXML
    private RadioButton rButton_DualMotor;
    @FXML
    private RadioButton rButton_Plaid;
    @FXML
    private RadioButton rButton_highAutonomy;
    @FXML
    private RadioButton rButton_performance;
    @FXML
    private ToggleGroup grupoOpciones1Modely3;
    @FXML
    private ToggleGroup grupoOpciones2Modelsx;
    @FXML
    private ToggleGroup grupoOpciones0AutonomousDriving;
    @FXML
    private Label label_buscadorVehicle;
    @FXML
    private TextField field_ModeloVehicle;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // CrudCustomer
        crudCustomer = new CrudCustomer();
        // CrudVehicle
        crudVehicle = new CrudVehicle();
        // CrudStock
        crudStock = new CrudVehicleStock();

        // Estado de los componentes por defecto
        vaciarTodoCustomer();
        vaciarTodoVehicle();
        vaciarTodoStock();

        // Recorte con esquinas
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

    @FXML
    private void abrirHomeSinSesion(MouseEvent event) {
        new AbrirVentanas().abrirHomeSinSesion(event);
    }

    @FXML
    private void minimiarVentana(MouseEvent event) {
        // Obtener la Stage actual a partir del evento
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        // Minimizar la ventana
        stage.setIconified(true);
    }

    @FXML
    private void fullVentana(MouseEvent event) {
        // Obtener la Stage actual a partir del evento
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        // Pantalla completa / full Ventana
        stage.setFullScreen(true);
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

    // ********** Inicio Customer Métodos **********
    @FXML
    private void handle_eliminarCustomer(MouseEvent event) {
        // Vaciar todo
        vaciarTodoCustomer();
        // Activar componentes, field buscar, button buscar
        activarBuscarComponentesCustomer();
        // Cambiar texto del button buscar
        button_buscarCustomer.setText("Eliminar");
    }

    @FXML
    private void handle_anadirCustomer(MouseEvent event) {
        // Vaciar todo
        vaciarTodoCustomer();
        // Poner visible los campos
        vbox_camposCustomer.setVisible(true);
        // Cambiar texto del button
        button_siguienteCustomer.setText("Añadir");
    }

    @FXML
    private void handle_modificarCustomer(MouseEvent event) {
        // Vaciar todo
        vaciarTodoCustomer();
        // Activar componentes, field buscar, button buscar
        activarBuscarComponentesCustomer();
        // Cambiar texto del button buscar
        button_buscarCustomer.setText("Modificar");
        // Cambiar texto del button
        button_siguienteCustomer.setText("Modificar");
    }

    @FXML
    private void handle_consultaCustomer(MouseEvent event) {
        // Vaciar todo
        vaciarTodoCustomer();
        // Activar componentes, field buscar, button buscar
        activarBuscarComponentesCustomer();
        // Cambiar texto del button buscar
        button_buscarCustomer.setText("Buscar");
    }

    @FXML
    private void handle_mostrarListaCustomer(MouseEvent event) {
        // Vaciar todo
        vaciarTodoCustomer();
        // Mostrar el textArea
        hbox_textAreaCustomer.setVisible(true);
        // Mostrar los customers en el textArea
        textArea_DatosCustomer.setText(crudCustomer.listCustomers());
        textArea_DatosCustomer.setEditable(false);
    }

    @FXML
    private void handle_siguienteCustomer(MouseEvent event) {
        if (button_siguienteCustomer.getText().equalsIgnoreCase("Añadir")) {
            addCustomer();
        } else if (button_siguienteCustomer.getText().equalsIgnoreCase("Modificar")) {
            updateCustomer();
        }
        // Vaciar campos
        vaciarCamposCustomer();
    }

    @FXML
    private void handle_AtrasCustomer(MouseEvent event) {
        vaciarTodoCustomer();
    }

    @FXML
    private void handle_buscarCustomer(MouseEvent event) {
        if (button_buscarCustomer.getText().equalsIgnoreCase("Modificar")) {
            // Recoger datos
            Customer c = crudCustomer.findCustomer(field_BusquedaCustomer.getText());
            if (!(c == null)) {
                // Poner visible los campos
                vbox_camposCustomer.setVisible(true);
                // Mostrar los datos del Customer en los campos
                mostrarDatosEnCamposCustomer(c);
            } else {
                mensajeAlert("Email no registrado");
            }
        } else if (button_buscarCustomer.getText().equalsIgnoreCase("Buscar")) {
            Customer c = crudCustomer.findCustomer(field_BusquedaCustomer.getText());
            if (!(c == null)) {
                hbox_textAreaCustomer.setVisible(true);
                textArea_DatosCustomer.setText(c + "");
            } else {
                mensajeAlert("Email no registrado");
            }
        } else if (button_buscarCustomer.getText().equalsIgnoreCase("Eliminar")) {
            Customer c = crudCustomer.findCustomer(field_BusquedaCustomer.getText());
            if (!(c == null)) {
                crudCustomer.deleteCustomer(field_BusquedaCustomer.getText());
                mensajeAlert("Cliente eliminado");
                vaciarTodoCustomer();
            } else {
                mensajeAlert("Email no registrado");
            }
        }

    }

    // Añadir customer
    private void addCustomer() {
        // Recoger datos
        Customer c = recogerDatosCamposCustomer();

        // Crear objeto Customer
        crudCustomer.saveCustomer(c);

        // Alert, nuevo cliente añadido
        mensajeAlert("Nuevo cliente añadido");

        // Vaciar todo
        vaciarTodoCustomer();
    }

    // Modificar customer
    private void updateCustomer() {
        // Crear objeto Customer con los nuevos datos en los campos
        crudCustomer.updateCustomer(recogerDatosCamposCustomer());

        // Alert, nuevo cliente añadido
        mensajeAlert("Cliente modificado");

        // Desactivar todo
        vaciarTodoCustomer();
    }

    // Activar campos, para modificar y consulta
    private void activarBuscarComponentesCustomer() {
        // Poner visible el Button buscar
        button_buscarCustomer.setVisible(true);

        // Activar field buscar
        field_BusquedaCustomer.setEditable(true);
    }

    // Desactivar campos, para modificar y consulta
    private void desactivarBuscarComponentesCustomer() {
        // Poner invisible el Button buscar
        button_buscarCustomer.setVisible(false);

        // Desactivar field buscar
        field_BusquedaCustomer.setEditable(false);
    }

    // Recoger datos de los campos
    private Customer recogerDatosCamposCustomer() {
        String name = field_NameCustomer.getText();
        String country = field_CountryCustomer.getText();
        String age = field_AgeCustomer.getText();
        String email = field_EmailCustomer.getText();
        String password = field_PasswordCustomer.getText();

        return new Customer(name, country, age, email, password);
    }

    // Mostrar datos en los campos
    private void mostrarDatosEnCamposCustomer(Customer customer) {
        field_NameCustomer.setText(customer.getCustomerName());
        field_CountryCustomer.setText(customer.getCountry());
        field_AgeCustomer.setText(customer.getAge());
        field_EmailCustomer.setText(customer.getEmail());
        field_PasswordCustomer.setText(customer.getPassword());
    }

    // Vaciar campos
    private void vaciarCamposCustomer() {
        field_BusquedaCustomer.setText("");
        field_NameCustomer.setText("");
        field_CountryCustomer.setText("");
        field_AgeCustomer.setText("");
        field_EmailCustomer.setText("");
        field_PasswordCustomer.setText("");
        button_buscarCustomer.setText("Buscar");
    }

    // Mensaje Alert
    private void mensajeAlert(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Confirmación");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    // Vaciar todo
    private void vaciarTodoCustomer() {
        // Vaciar campos
        vaciarCamposCustomer();
        // Desactivar componentes, field buscar, button buscar
        desactivarBuscarComponentesCustomer();
        // Poner invisible campos
        vbox_camposCustomer.setVisible(false);
        // Poner invisible textArea
        hbox_textAreaCustomer.setVisible(false);
        // Desactivar field buscar
        field_BusquedaCustomer.setEditable(false);
        // Cambiar texto del button
        button_siguienteCustomer.setText("Siguiente");
    }
    // ********** Fin Customer Métodos **********

    // ********** Inicio Vehicle Métodos **********
    @FXML
    private void handle_buscarVehicle(MouseEvent event) {
        String model = field_BusquedaVehicle.getText();
        if (button_buscarVehicle.getText().equalsIgnoreCase("Añadir")) {
            // Recoger datos y abrir las tarifas acorde al modelo
            if (model.equalsIgnoreCase("ModelY") || model.equalsIgnoreCase("Model3")) {
                // Poner visible los campos del vehicule y invisible la tarifa no requerida
                activarTarifa1();
                // Introducir el nombre del modelo 
                field_ModeloVehicle.setText((model.equalsIgnoreCase("modely") ? "Model Y" : (model.equalsIgnoreCase("model3") ? "Model 3" : "")));
            } else if (model.equalsIgnoreCase("ModelS") || model.equalsIgnoreCase("ModelX")) {
                // Poner visible los campos del vehicule y invisible la tarifa no requerida
                activarTarifa2PlaidDualMotorVehicle();
                // Introducir el nombre del modelo 
                field_ModeloVehicle.setText((model.equalsIgnoreCase("modelS") ? "Model S" : (model.equalsIgnoreCase("modelx") ? "Model X" : "")));
            } else {
                mensajeAlert("No existe el módelo");
            }
        } else if (button_buscarVehicle.getText().equalsIgnoreCase("Modificar")) {
            // Recoger datos
            ObjectId id = guardarId();
            TeslaVehicle v = crudVehicle.findVehicle(id);
            if (!(v == null) && (id != null)) {
                // Poner visible los campos
                vbox_camposVehicle.setVisible(true);
                // Mostrar los datos del Vehicle en los campos
                mostrarDatosEnCamposVehicle(v);
                // Desactivar campo email
                field_EmailVehicle.setEditable(false);
                // Introducir el nombre del modelo 
                field_ModeloVehicle.setText(
                        (v.getModelo().equalsIgnoreCase("model y") ? "Model Y"
                        : (v.getModelo().equalsIgnoreCase("model 3") ? "Model 3"
                        : (v.getModelo().equalsIgnoreCase("model S") ? "Model S"
                        : (v.getModelo().equalsIgnoreCase("model X") ? "Model X" : "idk"))))
                );
                if (v.getModelo().equalsIgnoreCase("Model Y") || v.getModelo().equalsIgnoreCase("Model 3")) {
                    activarTarifa1();
                } else if (v.getModelo().equalsIgnoreCase("Model S") || v.getModelo().equalsIgnoreCase("Model X")) {
                    activarTarifa2PlaidDualMotorVehicle();
                }
            } else {
                mensajeAlert("Id no registrado");
            }
        } else if (button_buscarVehicle.getText().equalsIgnoreCase("Buscar")) {
            ObjectId id = guardarId();
            TeslaVehicle v = crudVehicle.findVehicle(id);
            if (!(v == null) && (id != null)) {
                hbox_textAreaVehicle.setVisible(true);
                textArea_DatosVehicle.setText("- _id = " + id + "\n" + v + "");
            } else {
                mensajeAlert("Id no registrado");
            }
        } else if (button_buscarVehicle.getText().equalsIgnoreCase("Eliminar")) {
            ObjectId id = guardarId();
            TeslaVehicle v = crudVehicle.findVehicle(id);
            if (!(v == null) && (id != null)) {
                crudVehicle.deleteVehicle(id);
                mensajeAlert("Vehiculo eliminado");
                vaciarTodoCustomer();
            } else {
                mensajeAlert("Id no registrado");
            }
            vaciarTodoVehicle();
        }
    }

    @FXML
    private void handle_anadirVehicle(MouseEvent event) {
        // Vaciar todo
        vaciarTodoVehicle();
        // Activar componentes, field buscar, button buscar
        activarBuscarComponentesVehicle();
        // Cambiar texto del button buscar
        button_buscarVehicle.setText("Añadir");
        // Cambiar texto del button
        button_siguienteVehicle.setText("Añadir");
    }

    @FXML
    private void handle_modificarVehicle(MouseEvent event) {
        // Vaciar todo
        vaciarTodoVehicle();
        // Activar componentes, field buscar, button buscar
        activarBuscarComponentesVehicle();
        // Cambiar texto del button buscar
        button_buscarVehicle.setText("Modificar");
        // Cambiar texto del button
        button_siguienteVehicle.setText("Modificar");
        // Cambiar el texto del label de buscador
        label_buscadorVehicle.setText("Introduce el id del vehiculo");
    }

    @FXML
    private void handle_consultaVehicle(MouseEvent event) {
        // Vaciar todo
        vaciarTodoVehicle();
        // Activar componentes, field buscar, button buscar
        activarBuscarComponentesVehicle();
        // Cambiar label
        label_buscadorVehicle.setText("Introduce el id del vehiculo");
        // Cambiar texto del button buscar
        button_buscarVehicle.setText("Buscar");
    }

    @FXML
    private void handle_mostrarListaVehicle(MouseEvent event) {
        // Vaciar todo
        vaciarTodoVehicle();
        // Mostrar el textArea
        hbox_textAreaVehicle.setVisible(true);
        // Mostrar los customers en el textArea
        textArea_DatosVehicle.setText(crudVehicle.listVehicles());
        textArea_DatosVehicle.setEditable(false);
    }

    @FXML
    private void handle_eliminarVehicle(MouseEvent event) {
        // Vaciar todo
        vaciarTodoVehicle();
        // Activar componentes, field buscar, button buscar
        activarBuscarComponentesVehicle();
        // Cambiar texto del button buscar
        button_buscarVehicle.setText("Eliminar");
        // Cambiar texto del label Buscar
        label_buscadorVehicle.setText("Introduce el id del vehiculo");
    }

    @FXML
    private void handle_AtrasVehicle(MouseEvent event) {
        vaciarTodoVehicle();
    }

    @FXML
    private void handle_siguienteVehicle(MouseEvent event) {
        boolean completado = false;
        if (button_siguienteVehicle.getText().equalsIgnoreCase("Añadir")) {
            if (!(addVehicle(field_BusquedaVehicle.getText()) == null)) {
                completado = true;
            }
        } else if (button_siguienteVehicle.getText().equalsIgnoreCase("Modificar")) {
            updateVehicle();
            completado = true;
        }

        if (completado) {
            // Vaciar campos    
            vaciarCamposVehicle();
        }
    }

    @FXML
    private void handle_actualizarPrecioVehicle(MouseEvent event) {
        // En caso de que sea Añadir
        if (field_BusquedaVehicle.getText().equalsIgnoreCase("ModelY")
                || field_BusquedaVehicle.getText().equalsIgnoreCase("Model3")
                || field_BusquedaVehicle.getText().equalsIgnoreCase("ModelS")
                || field_BusquedaVehicle.getText().equalsIgnoreCase("ModelX")) {
            field_PriceVehicle.setText(formatter.format(updatePrices(field_BusquedaVehicle.getText())) + "€");
        } // En caso de que sea Modificar
        else if (field_ModeloVehicle.getText().equalsIgnoreCase("Model Y")
                || field_ModeloVehicle.getText().equalsIgnoreCase("Model 3")
                || field_ModeloVehicle.getText().equalsIgnoreCase("Model S")
                || field_ModeloVehicle.getText().equalsIgnoreCase("Model X")) {
            field_PriceVehicle.setText(formatter.format(updatePrices(field_ModeloVehicle.getText())) + "€");
        }
    }

    @FXML
    private void handle_desactivarCamposVehicle(MouseEvent event) {
        rButton_EnhancedAutopilot.setSelected(false);
        rButton_selfDrivingCapability.setSelected(false);
        check_wallConnector.setSelected(false);
        // para modelos S y X
        rButton_DualMotor.setSelected(true);
        rButton_Plaid.setSelected(false);
        // para modelos Y y 3
        rButton_rearWheelDrive.setSelected(true);
        rButton_highAutonomy.setSelected(false);
        rButton_performance.setSelected(false);
    }

    // Vaciar todo Vehicle
    private void vaciarTodoVehicle() {
        // Vaciar campos
        vaciarCamposVehicle();
        // Desactivar Buscar componentes
        desactivarBuscarComponentesVehicle();
        // Poner invisible campos
        vbox_camposVehicle.setVisible(false);
        // Poner invisible textArea
        hbox_textAreaVehicle.setVisible(false);
        // Desactivar field buscar
        field_BusquedaVehicle.setEditable(false);
        // Cambiar texto del button
        button_siguienteVehicle.setText("Siguiente");
        // Poner invisible tarifa 1, Model X, S
        vbox_Tarifa1Vehicle.setVisible(false);
        // Poner invisible tarifa 1, Model Y, 3
        vbox_Tarifa2PlaidDualMotorVehicle.setVisible(false);
        // Poner texto del label del buscador por defecto
        label_buscadorVehicle.setText("Introduce el Modelo(ModelY/Model3/ModelS/ModelX)");
        // Quitar texto del modelo
        field_ModeloVehicle.setText("");
        // Activar campo email
        field_EmailVehicle.setEditable(true);
    }

    // Vaciar campos
    private void vaciarCamposVehicle() {
        // Campos por defecto de vehicle
        field_BusquedaVehicle.setText("");
        field_EmailVehicle.setText("");
        field_PaintVehicle.setText("");
        field_PriceVehicle.setText("");
        rButton_EnhancedAutopilot.setSelected(false);
        rButton_selfDrivingCapability.setSelected(false);
        check_wallConnector.setSelected(false);
        // para modelos S y X
        rButton_DualMotor.setSelected(true);
        rButton_Plaid.setSelected(false);
        // para modelos Y y 3
        rButton_rearWheelDrive.setSelected(true);
        rButton_highAutonomy.setSelected(false);
        rButton_performance.setSelected(false);
    }

    // Desactivar campos, para modificar y consulta
    private void desactivarBuscarComponentesVehicle() {
        // Poner invisible el Button buscar
        button_buscarVehicle.setVisible(false);

        // Desactivar field buscar
        field_BusquedaVehicle.setEditable(false);
    }

    // Añadir customer
    private TeslaVehicle addVehicle(String model) {
        if (crudCustomer.findCustomer(field_EmailVehicle.getText()) == null) {
            mensajeAlert("Correo no registrado");
            return null;
        }
        if (model.equalsIgnoreCase("ModelY")) {
            // Recoger datos
            ModelY vehicle = (ModelY) recogerDatosCamposVehicle(model);
            // Crear objeto Customer
            crudVehicle.saveTeslaVehicle(vehicle);
            // Alert, nuevo cliente añadido
            mensajeAlert("Nuevo Vehiculo añadido");
            // Vaciar todo
            vaciarTodoVehicle();
            return vehicle;
        } else if (model.equalsIgnoreCase("Model3")) {
            // Recoger datos
            Model3 vehicle = (Model3) recogerDatosCamposVehicle(model);
            // Crear objeto Customer
            crudVehicle.saveTeslaVehicle(vehicle);
            // Alert, nuevo cliente añadido
            mensajeAlert("Nuevo Vehiculo añadido");
            // Vaciar todo
            vaciarTodoVehicle();
            return vehicle;
        } else if (model.equalsIgnoreCase("ModelS")) {
            // Recoger datos
            ModelS vehicle = (ModelS) recogerDatosCamposVehicle(model);
            // Crear objeto Customer
            crudVehicle.saveTeslaVehicle(vehicle);
            // Alert, nuevo cliente añadido
            mensajeAlert("Nuevo Vehiculo añadido");
            // Vaciar todo
            vaciarTodoVehicle();
            return vehicle;
        } else if (model.equalsIgnoreCase("ModelX")) {
            // Recoger datos
            ModelX vehicle = (ModelX) recogerDatosCamposVehicle(model);
            // Crear objeto Customer
            crudVehicle.saveTeslaVehicle(vehicle);
            // Alert, nuevo cliente añadido
            mensajeAlert("Nuevo Vehiculo añadido");
            // Vaciar todo
            vaciarTodoVehicle();
            return vehicle;
        }

        return null;
    }

    private double updatePrices(String model) {
        if (model.equalsIgnoreCase("ModelY") || model.equalsIgnoreCase("Model Y")) {
            VehiclesPrices vehicleModelYPrices = new VehiclesPrices(
                    rButton_EnhancedAutopilot.isSelected(),
                    rButton_selfDrivingCapability.isSelected(),
                    check_wallConnector.isSelected(),
                    rButton_rearWheelDrive.isSelected(),
                    rButton_highAutonomy.isSelected(),
                    rButton_performance.isSelected()
            );
            vehicleModelYPrices.updatePriceModelY();
            return vehicleModelYPrices.finalPriceModelY3();
        } else if (model.equalsIgnoreCase("Model3") || model.equalsIgnoreCase("Model 3")) {
            VehiclesPrices vehicleModel3Prices = new VehiclesPrices(
                    rButton_EnhancedAutopilot.isSelected(),
                    rButton_selfDrivingCapability.isSelected(),
                    check_wallConnector.isSelected(),
                    rButton_rearWheelDrive.isSelected(),
                    rButton_highAutonomy.isSelected(),
                    rButton_performance.isSelected()
            );
            vehicleModel3Prices.updatePriceModel3();
            return vehicleModel3Prices.finalPriceModelY3();
        } else if (model.equalsIgnoreCase("ModelS") || model.equalsIgnoreCase("Model S")) {
            VehiclesPrices vehicleModelSPrices = new VehiclesPrices(
                    rButton_EnhancedAutopilot.isSelected(),
                    rButton_selfDrivingCapability.isSelected(),
                    check_wallConnector.isSelected(),
                    rButton_DualMotor.isSelected(),
                    rButton_Plaid.isSelected()
            );
            vehicleModelSPrices.updatePriceModelS();
            return vehicleModelSPrices.finalPriceModelSX();
        } else if (model.equalsIgnoreCase("ModelX") || model.equalsIgnoreCase("Model X")) {
            VehiclesPrices vehicleModelXPrices = new VehiclesPrices(
                    rButton_EnhancedAutopilot.isSelected(),
                    rButton_selfDrivingCapability.isSelected(),
                    check_wallConnector.isSelected(),
                    rButton_DualMotor.isSelected(),
                    rButton_Plaid.isSelected()
            );
            vehicleModelXPrices.updatePriceModelX();
            return vehicleModelXPrices.finalPriceModelSX();
        }
        return 0;
    }

    // Recoger datos de los campos
    private TeslaVehicle recogerDatosCamposVehicle(String model) {
        ObjectId id = null;
        boolean bModificar = false;
        // Campo buscador
        if (button_buscarVehicle.getText().equalsIgnoreCase("Modificar")) {
            id = new ObjectId(field_BusquedaVehicle.getText());
            bModificar = true;
        }

        // Campos por defecto
        String email = field_EmailVehicle.getText();
        String paint = field_PaintVehicle.getText();
        boolean enhancedAutopilot = rButton_EnhancedAutopilot.isSelected();
        boolean fullSelfDrivingCapability = rButton_selfDrivingCapability.isSelected();
        boolean wallConnectorCharge = check_wallConnector.isSelected();
        double price = updatePrices(model);

        // Modelos de vehiculo
        if (model.equalsIgnoreCase("ModelY") || model.equalsIgnoreCase("Model Y")) {
            boolean bRearWheelDrive = rButton_rearWheelDrive.isSelected();
            boolean bHighAutonomy = rButton_highAutonomy.isSelected();
            boolean bPerformance = rButton_performance.isSelected();
            if (bModificar) {
                bModificar = false;
                return new ModelY(bRearWheelDrive, bHighAutonomy, bPerformance, id, email, paint, enhancedAutopilot, fullSelfDrivingCapability, wallConnectorCharge, price, "Model Y");
            } else {
                return new ModelY(bRearWheelDrive, bHighAutonomy, bPerformance, email, paint, enhancedAutopilot, fullSelfDrivingCapability, wallConnectorCharge, price, "Model Y");
            }
        } else if (model.equalsIgnoreCase("Model3") || model.equalsIgnoreCase("Model 3")) {
            System.out.println("Recoger datos campos model 3");
            boolean bRearWheelDrive = rButton_rearWheelDrive.isSelected();
            boolean bHighAutonomy = rButton_highAutonomy.isSelected();
            boolean bPerformance = rButton_performance.isSelected();
            if (bModificar) {
                bModificar = false;
                return new Model3(bRearWheelDrive, bHighAutonomy, bPerformance, id, email, paint, enhancedAutopilot, fullSelfDrivingCapability, wallConnectorCharge, price, "Model 3");
            } else {
                return new Model3(bRearWheelDrive, bHighAutonomy, bPerformance, email, paint, enhancedAutopilot, fullSelfDrivingCapability, wallConnectorCharge, price, "Model 3");
            }
        } else if (model.equalsIgnoreCase("ModelS") || model.equalsIgnoreCase("Model S")) {
            boolean bDualMotor = rButton_DualMotor.isSelected();
            boolean bPlaid = rButton_Plaid.isSelected();
            if (bModificar) {
                bModificar = false;
                return new ModelS(bDualMotor, bPlaid, id, email, paint, enhancedAutopilot, fullSelfDrivingCapability, wallConnectorCharge, price, "Model S");
            } else {
                return new ModelS(bDualMotor, bPlaid, email, paint, enhancedAutopilot, fullSelfDrivingCapability, wallConnectorCharge, price, "Model S");
            }
        } else if (model.equalsIgnoreCase("ModelX") || model.equalsIgnoreCase("Model X")) {
            boolean bDualMotor = rButton_DualMotor.isSelected();
            boolean bPlaid = rButton_Plaid.isSelected();
            if (bModificar) {
                bModificar = false;
                return new ModelX(bDualMotor, bPlaid, id, email, paint, enhancedAutopilot, fullSelfDrivingCapability, wallConnectorCharge, price, "Model X");
            } else {
                return new ModelX(bDualMotor, bPlaid, email, paint, enhancedAutopilot, fullSelfDrivingCapability, wallConnectorCharge, price, "Model X");
            }
        }

        return null;
    }

    // Activar campos, para modificar y consulta
    private void activarBuscarComponentesVehicle() {
        // Poner visible el Button buscar
        button_buscarVehicle.setVisible(true);

        // Activar field buscar
        field_BusquedaVehicle.setEditable(true);
    }

    // Mostrar datos en los campos
    private void mostrarDatosEnCamposVehicle(TeslaVehicle vehicle) {
        if (vehicle instanceof ModelY) {
            ModelY modelY = (ModelY) vehicle;
            field_EmailVehicle.setText(modelY.getEmail());
            field_PaintVehicle.setText(modelY.getPaint());
            field_PriceVehicle.setText((formatter.format(modelY.getPrice()) + "€"));
            rButton_EnhancedAutopilot.setSelected(modelY.isEnhancedAutopilot());
            rButton_selfDrivingCapability.setSelected(modelY.isFullSelfDrivingCapability());
            check_wallConnector.setSelected(modelY.isWallConnectorCharge());
            rButton_rearWheelDrive.setSelected(modelY.isRearWheelDrive());
            rButton_highAutonomy.setSelected(modelY.isHighAutonomy());
            rButton_performance.setSelected(modelY.isPerformance());
        } else if (vehicle instanceof Model3) {
            Model3 model3 = (Model3) vehicle;
            field_EmailVehicle.setText(model3.getEmail());
            field_PaintVehicle.setText(model3.getPaint());
            field_PriceVehicle.setText((formatter.format(model3.getPrice()) + "€"));
            rButton_EnhancedAutopilot.setSelected(model3.isEnhancedAutopilot());
            rButton_selfDrivingCapability.setSelected(model3.isFullSelfDrivingCapability());
            check_wallConnector.setSelected(model3.isWallConnectorCharge());
            rButton_rearWheelDrive.setSelected(model3.isRearWheelDrive());
            rButton_highAutonomy.setSelected(model3.isHighAutonomy());
            rButton_performance.setSelected(model3.isPerformance());
        } else if (vehicle instanceof ModelS) {
            ModelS modelS = (ModelS) vehicle;
            field_EmailVehicle.setText(modelS.getEmail());
            field_PaintVehicle.setText(modelS.getPaint());
            field_PriceVehicle.setText((formatter.format(modelS.getPrice()) + "€"));
            rButton_EnhancedAutopilot.setSelected(modelS.isEnhancedAutopilot());
            rButton_selfDrivingCapability.setSelected(modelS.isFullSelfDrivingCapability());
            check_wallConnector.setSelected(modelS.isWallConnectorCharge());
            rButton_DualMotor.setSelected(modelS.isDualMotor());
            rButton_Plaid.setSelected(modelS.isPlaid());
        } else if (vehicle instanceof ModelX) {
            ModelX modelX = (ModelX) vehicle;
            field_EmailVehicle.setText(modelX.getEmail());
            field_PaintVehicle.setText(modelX.getPaint());
            field_PriceVehicle.setText((formatter.format(modelX.getPrice()) + "€"));
            rButton_EnhancedAutopilot.setSelected(modelX.isEnhancedAutopilot());
            rButton_selfDrivingCapability.setSelected(modelX.isFullSelfDrivingCapability());
            check_wallConnector.setSelected(modelX.isWallConnectorCharge());
            rButton_DualMotor.setSelected(modelX.isDualMotor());
            rButton_Plaid.setSelected(modelX.isPlaid());
        }
    }

    // Modificar vehicle
    private void updateVehicle() {
        // Crear objeto Customer con los nuevos datos en los campos
        String model = field_ModeloVehicle.getText();
        crudVehicle.updateVehicle(recogerDatosCamposVehicle(model));

        // Alert, nuevo cliente añadido
        mensajeAlert("Vehiculo modificado");

        // Desactivar todo
        vaciarTodoVehicle();
    }

    private void activarTarifa1() {
        vbox_camposVehicle.setVisible(true);
        vbox_Tarifa1Vehicle.setVisible(true);
        vbox_Tarifa2PlaidDualMotorVehicle.setVisible(false);
    }

    private void activarTarifa2PlaidDualMotorVehicle() {
        vbox_camposVehicle.setVisible(true);
        vbox_Tarifa2PlaidDualMotorVehicle.setVisible(true);
        vbox_Tarifa1Vehicle.setVisible(false);
    }

    private ObjectId guardarId() {
        ObjectId id = null;
        try {
            id = new ObjectId(field_BusquedaVehicle.getText());
        } catch (Exception e) {
            id = null;
        }

        return id;
    }
    // ********** Fin Vehicle Métodos **********

    // ********** Inicio Stock Métodos **********
    @FXML
    private void handle_modificarStock(MouseEvent event) {
        // Vaciar todo
        vaciarTodoStock();
        // Activar componentes, field buscar, button buscar
        activarBuscarComponentesStock();
        // Cambiar texto del button buscar
        button_buscarStock.setText("Modificar");
        // Cambiar texto del button
        button_siguienteStock.setText("Modificar");
    }

    @FXML
    private void handle_mostrarListaStock(MouseEvent event) {
        // Vaciar todo
        vaciarTodoStock();
        // Mostrar el textArea
        hbox_textAreaStock.setVisible(true);
        // Mostrar los customers en el textArea
        textArea_DatosStock.setText(crudStock.listStock());
        textArea_DatosStock.setEditable(false);
    }

    @FXML
    private void handle_siguienteStock(MouseEvent event) {
        if (button_siguienteStock.getText().equalsIgnoreCase("Modificar")) {
            updateStock();
        }
    }

    @FXML
    private void handle_buscarStock(MouseEvent event) {
        desactivarCamposStock();
        String stockField
                = (field_BusquedaStock.getText().equalsIgnoreCase("modely") ? "ModelY"
                : (field_BusquedaStock.getText().equalsIgnoreCase("model3") ? "Model3"
                : (field_BusquedaStock.getText().equalsIgnoreCase("models") ? "ModelS"
                : (field_BusquedaStock.getText().equalsIgnoreCase("modelx") ? "ModelX" : "idk"))));

        VehicleStock stock = crudStock.findStock(stockField);
        if (stock != null) {
            // Poner visible los campos
            vbox_camposStock.setVisible(true);
            // Mostrar los datos del Customer en los campos
            mostrarDatosEnCamposStock(stock);
            if (stock.getModel().equalsIgnoreCase("ModelY")) {
                field_ModelYStock.setEditable(true);
                field_ModelYStock.setText("" + stock.getStock());
            } else if (stock.getModel().equalsIgnoreCase("Model3")) {
                field_Model3Stock.setEditable(true);
                field_Model3Stock.setText("" + stock.getStock());
            } else if (stock.getModel().equalsIgnoreCase("ModelX")) {
                field_ModelXStock.setEditable(true);
                field_ModelXStock.setText("" + stock.getStock());
            } else if (stock.getModel().equalsIgnoreCase("ModelS")) {
                field_ModelSStock.setEditable(true);
                field_ModelSStock.setText("" + stock.getStock());
            }
        } else {
            mensajeAlert("Modelo no registrado");
        }
    }

    @FXML
    private void handle_AtrasStock(MouseEvent event) {
        vaciarTodoStock();
    }

    // Vaciar todo
    private void vaciarTodoStock() {
        // Vaciar campos
        vaciarCamposStock();
        // Poner button de buscar por defecto 
        button_buscarStock.setText("Buscar");
        // Desactivar componentes, field buscar, button buscar
        desactivarBuscarComponentesStock();
        // Poner invisible campos
        vbox_camposStock.setVisible(false);
        // Poner invisible textArea
        hbox_textAreaStock.setVisible(false);
        // Desactivar field buscar
        field_BusquedaStock.setEditable(false);
        // Cambiar texto del button
        button_siguienteStock.setText("Siguiente");
    }

    // Modificar customer
    private void updateStock() {
        String stock = field_BusquedaStock.getText();
        // Crear objeto Stock con los nuevos datos en los campos
        crudStock.updateStock(recogerDatosCamposStock(stock));

        // Alert, nuevo cliente añadido
        mensajeAlert("Stock modificado");

        // Desactivar todo
        vaciarTodoStock();
    }

    // Recoger datos de los campos
    private VehicleStock recogerDatosCamposStock(String stock) {
        String modelY = field_ModelYStock.getText();
        String model3 = field_Model3Stock.getText();
        String modelX = field_ModelXStock.getText();
        String modelS = field_ModelSStock.getText();

        if (stock.equalsIgnoreCase("ModelY")) {
            return new VehicleStock("ModelY", Integer.parseInt(modelY));
        } else if (stock.equalsIgnoreCase("Model3")) {
            return new VehicleStock("Model3", Integer.parseInt(model3));
        } else if (stock.equalsIgnoreCase("ModelX")) {
            return new VehicleStock("ModelX", Integer.parseInt(modelX));
        } else if (stock.equalsIgnoreCase("ModelS")) {
            return new VehicleStock("ModelS", Integer.parseInt(modelS));
        }

        return null;
    }

    // Vaciar campos
    private void vaciarCamposStock() {
        field_BusquedaStock.setText("");
        field_ModelYStock.setText("");
        field_Model3Stock.setText("");
        field_ModelSStock.setText("");
        field_ModelXStock.setText("");
    }

    private void desactivarCamposStock() {
        field_ModelYStock.setEditable(false);
        field_Model3Stock.setEditable(false);
        field_ModelXStock.setEditable(false);
        field_ModelSStock.setEditable(false);
    }

    // Desactivar campos, para modificar y consulta
    private void desactivarBuscarComponentesStock() {
        // Poner invisible el Button buscar
        button_buscarStock.setVisible(false);

        // Desactivar field buscar
        field_BusquedaStock.setEditable(false);
    }

    // Activar campos, para modificar y consulta
    private void activarBuscarComponentesStock() {
        // Poner visible el Button buscar
        button_buscarStock.setVisible(true);

        // Activar field buscar
        field_BusquedaStock.setEditable(true);
    }

    // Mostrar datos en los campos
    private void mostrarDatosEnCamposStock(VehicleStock stock) {
        if (stock.getModel().equalsIgnoreCase("ModelY")) {
            field_ModelYStock.setText(stock.getModel());
        } else if (stock.getModel().equalsIgnoreCase("Model3")) {
            field_Model3Stock.setText(stock.getModel());
        } else if (stock.getModel().equalsIgnoreCase("ModelX")) {
            field_ModelXStock.setText(stock.getModel());
        } else if (stock.getModel().equalsIgnoreCase("ModelS")) {
            field_ModelSStock.setText(stock.getModel());
        }

    }
    // ********** Fin Stock Métodos **********

    
}
