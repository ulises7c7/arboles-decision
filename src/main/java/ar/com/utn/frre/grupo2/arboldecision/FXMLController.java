package ar.com.utn.frre.grupo2.arboldecision;

import ar.com.utn.frre.grupo2.arboldecision.dao.ElementosDAO;
import ar.com.utn.frre.grupo2.arboldecision.dto.ElementoDTO;
import ar.com.utn.frre.grupo2.arboldecision.dto.NodoDTO;
import ar.com.utn.frre.grupo2.arboldecision.dto.RangosDTO;
import ar.com.utn.frre.grupo2.arboldecision.service.ArbolDecisionService;
import ar.com.utn.frre.grupo2.arboldecision.view.ArbolPane;
import ar.com.utn.frre.grupo2.arboldecision.view.CanvasGrafico;
import ar.com.utn.frre.grupo2.arboldecision.view.SelectionListener;
import com.mxgraph.model.mxCell;
import com.mxgraph.util.mxEvent;
import com.mxgraph.util.mxEventObject;
import com.mxgraph.util.mxEventSource;
import com.mxgraph.view.mxGraphSelectionModel;
import eu.hansolo.enzo.notification.Notification;
import eu.hansolo.enzo.notification.NotificationBuilder;
import eu.hansolo.enzo.notification.NotifierBuilder;
import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
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
    private ArbolPane arbolPane;
    @FXML
    private TabPane tabPane;
    @FXML
    private ComboBox<String> criterioInformacion;
    private final Map<String, Label> labels = new HashMap<>();
    private File initialDirectory = null;

    @FXML
    private void importarElementos() {

        FileChooser fileChooser = new FileChooser();
        if (initialDirectory != null) {
            fileChooser.setInitialDirectory(initialDirectory);
        }

        fileChooser.setTitle(mensajes.getString("titulo_file_chooser"));
        File archivo = fileChooser.showOpenDialog(this.getStage());
        if (archivo != null) {
            initialDirectory = archivo.getParentFile();
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

        Set<String> clases = new HashSet<>();

        for (ElementoDTO elemento : elementos) {
            clases.add(elemento.getClaseString());
        }
        ClaseHandler.getInstancia().definirClases(clases);

        for (ElementoDTO elemento : elementos) {
            elemento.setClase(
                    ClaseHandler.getInstancia().getClaseNumero(
                            elemento.getClaseString()));
        }

        recargarTabla();
        nodoRaiz = null;
        canvas.setNodoRaiz(nodoRaiz);
        canvas.setElementos(elementos);
        canvas.redraw();

        canvas.getElementosPrueba().clear();
        elementosTestTable.getItems().clear();
        elementosPrueba.clear();
        arbolPane.clear();

    }

    @FXML
    private void procesarElementos() {

        if (elementos == null || elementos.isEmpty()) {
            notifier.notify(NotificationBuilder.create()
                    .title(mensajes.getString("dataset_vacio_error_titulo"))
                    .message(mensajes.getString("dataset_vacio_error_mensaje"))
                    .image(Notification.ERROR_ICON).build());
        } else {

            RangosDTO rangos = arbolesService.generarRangoInicial(elementos);
            nodoRaiz = new NodoDTO();
            nodoRaiz.setElementos(elementos);
            nodoRaiz.setNivel(1);
            nodoRaiz.setRangosDTO(rangos);

            BigDecimal umbral = new BigDecimal(umbralTextField.getText());

            if (criterioInformacion.getSelectionModel().getSelectedIndex() == 0) {
                arbolesService.decisionTree(elementos, rangos, nodoRaiz, umbral);
            } else {
                arbolesService.decisionTreeGainRatio(elementos, rangos, nodoRaiz, umbral);
            }

            canvas.setNodoRaiz(nodoRaiz);
            canvas.setElementos(elementos);
            canvas.redraw();

            arbolPane.drawArbol(nodoRaiz);
        }
    }

    @FXML
    private void addTestPoint() {
        ElementoDTO elementoTest = new ElementoDTO();

        elementoTest.setCoordX(new BigDecimal(coordXTestPoint.getText()));
        elementoTest.setCoordY(new BigDecimal(coordYTestPoint.getText()));

        elementosPrueba.add(elementoTest);
        elementosTestTable.getItems().clear();
        elementosTestTable.getItems().addAll(elementosPrueba);
        canvas.getElementosPrueba().add(elementoTest);
        canvas.redraw();
    }

    @FXML
    private void borrarTestData() {
        ObservableList<ElementoDTO> elementosSeleccionados = elementosTestTable.getSelectionModel().getSelectedItems();
        elementosPrueba.removeAll(elementosSeleccionados);
        elementosTestTable.getItems().clear();
        elementosTestTable.getItems().addAll(elementosPrueba);
        canvas.getElementosPrueba().removeAll(elementosSeleccionados);
        canvas.redraw();
    }

    @FXML
    private void cerrarAplicacion() {
        getStage().close();
    }

    @FXML
    private void clasificarDatosPrueba() {

        if (nodoRaiz == null) {

            notifier.notify(NotificationBuilder.create()
                    .title(mensajes.getString("clasificacion_error_titulo"))
                    .message(mensajes.getString("clasificacion_error_mensaje"))
                    .image(Notification.ERROR_ICON).build());

        } else {

            for (ElementoDTO elemento : elementosPrueba) {
                arbolesService.clasificar(elemento, nodoRaiz);
            }
            elementosTestTable.getItems().clear();
            elementosTestTable.getItems().addAll(elementosPrueba);
            canvas.redraw();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        umbralTextField.setText("0");

        criterioInformacion.getSelectionModel().select(0);
        labels.put("tipoNodoLbl", tipoNodoLbl);
        labels.put("tipoHojaLbl", tipoHojaLbl);
        labels.put("claseHojaLbl", claseHojaLbl);
        labels.put("entropiaLbl", entropiaLbl);

        elementosTestTable.getSelectionModel().setSelectionMode(javafx.scene.control.SelectionMode.MULTIPLE);

        arbolPane.getJgxAdapter().getSelectionModel().addListener(mxEvent.CHANGE, new SelectionListener(labels));
        arbolPane.getJgxAdapter().getSelectionModel().addListener(mxEvent.CHANGE, new mxEventSource.mxIEventListener() {

            @Override
            public void invoke(Object sender, mxEventObject evt) {

                mxGraphSelectionModel sm = (mxGraphSelectionModel) sender;
                mxCell cell = (mxCell) sm.getCell();
                if (cell != null && cell.isVertex()) {
                    tabPane.getSelectionModel().selectLast();
                }
            }

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
