/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.com.utn.frre.grupo2.arboldecision;

import ar.com.utn.frre.grupo2.arboldecision.dto.NodoDTO;
import ar.com.utn.frre.grupo2.arboldecision.view.NodoView;
import ar.com.utn.frre.grupo2.arboldecision.view.SelectionHandler;
import java.text.NumberFormat;
import java.util.List;
import javafx.geometry.Insets;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 *
 * @author ulises
 */
public class PanelArbol extends Pane {

    private NodoDTO nodoRaiz;
    private final double diametroNodo = 30;

    private Integer nivelesCount = 1;
    private final SelectionHandler selectionHandler;

    public PanelArbol() {
        super();

        selectionHandler = new SelectionHandler(this);
        this.addEventHandler(MouseEvent.MOUSE_PRESSED, selectionHandler.getMousePressedEventHandler());

        this.setBackground(
                new Background(
                        new BackgroundFill(
                                SolarizedColors.BASE3, CornerRadii.EMPTY, Insets.EMPTY)));
    }

    public void redraw() {
        this.getChildren().clear();
        nivelesCount = 1;
        if (nodoRaiz != null) {
            contarNiveles(nodoRaiz);
            dibujarAristas(nodoRaiz);
            dibujarNodos(nodoRaiz);
        }

    }

    private void contarNiveles(NodoDTO nodo) {
        if (nodo.getNivel() > nivelesCount) {
            nivelesCount = nodo.getNivel();
        }

        if (nodo.getHijos() != null && !nodo.getHijos().isEmpty()) {
            for (NodoDTO hijo : nodo.getHijos()) {
                contarNiveles(hijo);
            }
        }
    }

    private void dibujarAristas(NodoDTO nodo) {
        if (nodo.getPadre() != null) {

            //Centro nodo padre
            double x1 = calcularNodoCoordX(nodo.getPadre().getCamino(), nivelesCount);
            double y1 = calcularNodoCoordY(nodo.getPadre().getNivel(), nivelesCount);

            //centro nodo actual
            double x2 = calcularNodoCoordX(nodo.getCamino(), nivelesCount);
            double y2 = calcularNodoCoordY(nodo.getNivel(), nivelesCount);

            //Posicion del texto, si la rama va a la izquierda o derecha
            //se suman unos pixeles en y para que los textos no se superpongan
            double xMid = (x1 + x2) / 2;
            double yMid = ((y1 + y2) / 2) + (nodo.getEsRamaMenor() ? -5 : 5);

            dibujarTexto(armarTextoRama(nodo), xMid, yMid);
            dibujarArista(x1, y1, x2, y2);
        }
        if (nodo.getHijos() != null && !nodo.getHijos().isEmpty()) {
            for (NodoDTO hijo : nodo.getHijos()) {
                dibujarAristas(hijo);
            }
        }
    }

    private void dibujarTexto(String texto, double x, double y) {
        Text text = new Text(texto);
        text.setX(x - text.getLayoutBounds().getWidth() / 2);
        text.setY(y + text.getLayoutBounds().getHeight() / 4);
        text.setFill(SolarizedColors.BASE03);
        text.setStroke(SolarizedColors.BASE03);
        text.setFont(new Font(text.getFont().getName(), 9));

        getChildren().add(text);
    }

    private String armarTextoRama(NodoDTO nodo) {
        if (nodo.getEjeParticion() != null) {
            String eje = nodo.getEjeParticion() == 1 ? "x" : "y";
            String signo = nodo.getEsRamaMenor() ? "<" : ">";
            String valor = NumberFormat.getNumberInstance().format(nodo.getValorParticion());
            return String.format("%s %s %s", eje, signo, valor);
        }
        return "";
    }

    private void dibujarNodos(NodoDTO nodo) {
        dibujarNodo(calcularNodoCoordX(
                nodo.getCamino(), nivelesCount),
                calcularNodoCoordY(nodo.getNivel(), nivelesCount),
                nodo.getEsHoja() ? nodo.getClaseHoja() == null ? "?" : nodo.getClaseHoja().toString() : null, nodo);
        if (nodo.getHijos() != null && !nodo.getHijos().isEmpty()) {
            for (NodoDTO hijo : nodo.getHijos()) {
                dibujarNodos(hijo);
            }
        }
    }

    private void dibujarArista(double startX, double startY, double endX, double endY) {
        Line linea = new Line(startX, startY, endX, endY);
        linea.setStrokeWidth(1);
        linea.setStroke(SolarizedColors.BASE03);
        getChildren().add(linea);
    }

    private void dibujarNodo(double xCentro, double yCentro, String label, NodoDTO nodoDTO) {

        double radioNodo = diametroNodo / 2;

        NodoView circulo = new NodoView(xCentro, yCentro, radioNodo, nodoDTO);
        getChildren().add(circulo);

        if (label != null) {
            Text text = new Text(label);
            text.setX(xCentro - text.getLayoutBounds().getWidth() / 2);
            text.setY(yCentro + text.getLayoutBounds().getHeight() / 4);
            text.setFont(new Font(text.getFont().getName(), 10));

            if ("?".equals(label)) {
                text.setStroke(SolarizedColors.getColorByClase(0));

            } else {
                text.setStroke(SolarizedColors.getColorByClase(Integer.valueOf(label)));
            }

            getChildren().add(text);
        }

    }

    public NodoDTO getNodoRaiz() {
        return nodoRaiz;
    }

    public void setNodoRaiz(NodoDTO nodoRaiz) {
        this.nodoRaiz = nodoRaiz;
    }

    private double calcularNodoCoordY(Integer nivel, Integer nivelesCount) {

        double distanciaMinima = 20;

        if (getInnerHeightPane() / nivelesCount < distanciaMinima) {
            //distancia fija (minima)
            return 20.0 + nivel * distanciaMinima;
        } else {
            //proporcional al tamaño del panel y la cantidad de niveles
            return (getInnerHeightPane() / nivelesCount) * (nivel - 0.5);
        }

    }

    private double calcularNodoCoordX(List<Integer> desplazamientos, int nivelesCount) {
        double coordX = getInnerWidthPane() / 2;
        double distanciaMinima = 100;
        double offsetX;
        if ((getInnerWidthPane() / (2 ^ (nivelesCount - 1))) > distanciaMinima) {
            offsetX = getInnerWidthPane() / 2;
        } else {
            offsetX = distanciaMinima * (2 ^ (nivelesCount + 1));
        }

        for (Integer desp : desplazamientos) {
            offsetX = offsetX / 2;
            coordX = coordX + (desp * offsetX);
        }
        return coordX;
    }

    @Override
    protected void layoutChildren() {
//        double top = snappedTopInset();
//        double left = snappedLeftInset();
//        double w = getInnerWidthPane();
//        double h = getInnerHeightPane();
//        canvas.setLayoutX(left);
//        canvas.setLayoutY(top);
//        if (w != canvas.getWidth() || h != canvas.getHeight()) {
//            canvas.setWidth(w);
//            canvas.setHeight(h);
//            redraw();
//        }
    }

    private double getInnerHeightPane() {
        double top = snappedTopInset();
        double bottom = snappedBottomInset();
        double h = getHeight() - top - bottom;
        return h;
    }

    private double getInnerWidthPane() {

        double right = snappedRightInset();

        double left = snappedLeftInset();
        double w = getWidth() - left - right;

        return w;
    }

    public SelectionHandler getSelectionHandler() {
        return selectionHandler;
    }

}
