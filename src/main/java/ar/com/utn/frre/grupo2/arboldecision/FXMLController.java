package ar.com.utn.frre.grupo2.arboldecision;

import ar.com.utn.frre.grupo2.arboldecision.dao.ElementosDAO;
import ar.com.utn.frre.grupo2.arboldecision.dto.ElementoDTO;
import ar.com.utn.frre.grupo2.arboldecision.dto.NodoDTO;
import ar.com.utn.frre.grupo2.arboldecision.dto.RangosDTO;
import ar.com.utn.frre.grupo2.arboldecision.service.ArbolDecisionService;
import eu.hansolo.enzo.notification.Notification;
import eu.hansolo.enzo.notification.NotificationBuilder;
import eu.hansolo.enzo.notification.NotifierBuilder;
import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.beanio.InvalidRecordException;

public class FXMLController implements Initializable {

    private Stage stage;

    private final ArbolDecisionService arbolesService = new ArbolDecisionService();
    private Notification.Notifier notifier;

    private final ElementosDAO elementosDAO = new ElementosDAO();
    private final List<ElementoDTO> elementos = new ArrayList<>();
    private final List<ElementoDTO> elementosPrueba = new ArrayList<>();
    private NodoDTO nodoRaiz = null;

    private final Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);

    private final ResourceBundle mensajes = ResourceBundle.getBundle("strings/mensajes");

    @FXML
    private TextField umbralTextField;

    @FXML
    private TableView<ElementoDTO> elementosTable;
    @FXML
    private TableView<ElementoDTO> elementosTestTable;

    @FXML
    private TextField coordXTestPoint;
    @FXML
    private TextField coordYTestPoint;
    @FXML
    private TextField claseTestPoint;
    @FXML
    private CanvasGrafico canvas;
    @FXML
    private Label claseHojaLbl;
    @FXML
    private Label entropiaLbl;
    @FXML
    private Label tipoHojaLbl;
    @FXML
    private Label tipoNodoLbl;
    @FXML
    private PanelArbol canvasArbol;
    @FXML
    private TabPane tabPane;
    private final Map<String, Label> labels = new HashMap<>();

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
                notifier.notify(NotificationBuilder.create()
                        .title(mensajes.getString("importacion_elementos_error_titulo"))
                        .message(mensajes.getString("importacion_elementos_error_mensaje"))
                        .image(Notification.ERROR_ICON).build());
            }

            if (!elementos.isEmpty()) {
                notifier.notify(NotificationBuilder.create()
                        .title(mensajes.getString("importacion_elementos_titulo"))
                        .message(mensajes.getString("importacion_elementos_mensaje"))
                        .image(Notification.INFO_ICON).build());

            }
        }

        recargarTabla();
        nodoRaiz = null;
        canvas.setNodoRaiz(nodoRaiz);
        canvas.setElementos(elementos);
        canvas.redraw();
    }

    @FXML
    private void procesarElementos() {
        RangosDTO rangos = arbolesService.generarRangoInicial(elementos);
        nodoRaiz = new NodoDTO();
        nodoRaiz.setElementos(elementos);
        nodoRaiz.setNivel(1);
        nodoRaiz.setRangosDTO(rangos);

        BigDecimal umbral = new BigDecimal(umbralTextField.getText());

        arbolesService.decisionTree(elementos, rangos, nodoRaiz, umbral);

        canvas.setNodoRaiz(nodoRaiz);
        canvas.setElementos(elementos);
        canvas.redraw();

        canvasArbol.setNodoRaiz(nodoRaiz);
        canvasArbol.redraw();
        System.out.println("Proceso finalizado!");
    }

    @FXML
    private void addTestPoint() {
        ElementoDTO elementoTest = new ElementoDTO();

        elementoTest.setCoordX(new BigDecimal(coordXTestPoint.getText()));
        elementoTest.setCoordY(new BigDecimal(coordYTestPoint.getText()));
        elementoTest.setClase(Integer.valueOf(claseTestPoint.getText()));

        elementosPrueba.add(elementoTest);
        elementosTestTable.getItems().clear();
        elementosTestTable.getItems().addAll(elementosPrueba);
    }

    @FXML
    private void borrarTestData() {
        ObservableList<ElementoDTO> elementosSeleccionados = elementosTestTable.getSelectionModel().getSelectedItems();
        elementosPrueba.removeAll(elementosSeleccionados);
        elementosTestTable.getItems().clear();
        elementosTestTable.getItems().addAll(elementosPrueba);
    }

    @FXML
    private void clasificarDatosPrueba() {
        throw new UnsupportedOperationException("Metodo no implementado aun");
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        umbralTextField.setText("0");
        labels.put("tipoNodoLbl", tipoNodoLbl);
        labels.put("tipoHojaLbl", tipoHojaLbl);
        labels.put("claseHojaLbl", claseHojaLbl);
        labels.put("entropiaLbl", entropiaLbl);

        elementosTestTable.getSelectionModel().setSelectionMode(javafx.scene.control.SelectionMode.MULTIPLE);

        canvasArbol.getSelectionHandler().setLabels(labels);
        canvasArbol.setOnMouseClicked((event) -> {
            tabPane.getSelectionModel().selectLast();
        });

        canvas.setOnMouseClicked((event) -> {
            coordXTestPoint.setText(canvas.traducirX(event.getX()).setScale(4, RoundingMode.HALF_UP).toString());
            coordYTestPoint.setText(canvas.traducirY(event.getY()).setScale(4, RoundingMode.HALF_UP).toString());
        });

    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
        notifier = NotifierBuilder.create()
                .popupLocation(Pos.TOP_RIGHT)
                .popupLifeTime(Duration.millis(10000))
                //.styleSheet(getClass().getResource("mynotification.css").toExternalForm())
                .build();
        stage.setOnCloseRequest(observable -> notifier.stop());
    }

    private void recargarTabla() {
        elementosTable.getItems().clear();
        elementosTable.getItems().addAll(elementos);
    }

}
