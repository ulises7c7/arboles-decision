package ar.com.utn.frre.grupo2.arboldecision;

import ar.com.utn.frre.grupo2.arboldecision.dao.ElementosDAO;
import ar.com.utn.frre.grupo2.arboldecision.dto.ElementoDTO;
import ar.com.utn.frre.grupo2.arboldecision.dto.NodoDTO;
import ar.com.utn.frre.grupo2.arboldecision.dto.RangosDTO;
import ar.com.utn.frre.grupo2.arboldecision.service.ArbolDecisionService;
import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.TableView;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.beanio.InvalidRecordException;

public class FXMLController implements Initializable {

    private Stage stage;

    private ArbolDecisionService arbolesService = new ArbolDecisionService();

    private final ElementosDAO elementosDAO = new ElementosDAO();
    private final List<ElementoDTO> elementos = new ArrayList<>();

    private final Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
    private final Alert infoAlert = new Alert(Alert.AlertType.INFORMATION);

    private final ResourceBundle mensajes = ResourceBundle.getBundle("strings/mensajes");

    private BigDecimal factorScale = new BigDecimal(80);

    @FXML
    private TableView<ElementoDTO> elementosTable;

    @FXML
    private Canvas canvas;

    @FXML
    private void importarElementos() {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(mensajes.getString("titulo_file_chooser"));
        File archivo = fileChooser.showOpenDialog(this.getStage());
        if (archivo != null) {
            elementos.clear();
            try {
                elementos.addAll(elementosDAO.obtenerDatosCSV(archivo));
            } catch (InvalidRecordException e) {
                informar(mensajes.getString("importacion_elementos_error_titulo"),
                        mensajes.getString("importacion_elementos_error_header"),
                        mensajes.getString("importacion_elementos_error_mensaje"));
            }

            if (!elementos.isEmpty()) {

                informar(mensajes.getString("importacion_elementos_titulo"),
                        mensajes.getString("importacion_elementos_header"),
                        mensajes.getString("importacion_elementos_mensaje"));
            }
        }

        recargarTablaPeriodos();
        redraw();
    }

    @FXML
    private void procesarElementos() {
        RangosDTO rangos = arbolesService.generarRangoInicial(elementos);
        NodoDTO nodoRaiz = new NodoDTO();
        nodoRaiz.setElementos(elementos);
        nodoRaiz.setRangosDTO(rangos);

        //TODO: parametrizar
        BigDecimal umbral = BigDecimal.ZERO;

        arbolesService.decisionTree(elementos, rangos, nodoRaiz, umbral);
    }

    private void informar(String titulo, String encabezado, String pregunta) {
        infoAlert.setTitle(titulo);
        infoAlert.setHeaderText(encabezado);
        infoAlert.setContentText(pregunta);
        infoAlert.showAndWait();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        canvas.setHeight(500);
        canvas.setWidth(500);
    }

    private void redraw() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        for (ElementoDTO elemento : elementos) {
            if (elemento.getClase() == 1) {
                gc.setFill(Color.BLUE);
                gc.setStroke(null);
                gc.fillRect(
                        elemento.getCoordX().multiply(factorScale).setScale(0, RoundingMode.HALF_UP).longValue() - 2,
                        -elemento.getCoordY().multiply(factorScale).setScale(0, RoundingMode.HALF_UP).longValue() - 2 + 500,
                        5, 5);
            } else {
                gc.setFill(null);
                gc.setStroke(Color.RED);
                gc.strokeOval(
                        elemento.getCoordX().multiply(factorScale).setScale(0, RoundingMode.HALF_UP).longValue() - 2,
                        -elemento.getCoordY().multiply(factorScale).setScale(0, RoundingMode.HALF_UP).longValue() - 2 + 500,
                        5, 5);
            }
        }
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    private void recargarTablaPeriodos() {
        elementosTable.getItems().clear();
        elementosTable.getItems().addAll(elementos);
    }

}
