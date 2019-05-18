/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.com.utn.frre.grupo2.arboldecision;

import ar.com.utn.frre.grupo2.arboldecision.dto.NodoDTO;
import java.text.NumberFormat;
import java.util.List;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

/**
 *
 * @author ulises
 */
public class CanvasArbol extends Pane {

    private GraphicsContext gc;
    private NodoDTO nodoRaiz;
    private final double diametroNodo = 30;

    private Integer nivelesCount = 1;
    private final Canvas canvas = new Canvas();

    public CanvasArbol() {
        super();

        getChildren().add(canvas);

        gc = canvas.getGraphicsContext2D();
        pintarFondo();

    }

    private void pintarFondo() {
        gc.setFill(SolarizedColors.BASE3);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }

    public void redraw() {
        pintarFondo();
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
            double x1 = calcularNodoCoordX(nodo.getPadre().getCamino());
            double y1 = calcularNodoCoordY(nodo.getPadre().getNivel(), nivelesCount);

            //centro nodo actual
            double x2 = calcularNodoCoordX(nodo.getCamino());
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
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setTextBaseline(VPos.CENTER);
        gc.setFill(SolarizedColors.BASE03);
        gc.setStroke(SolarizedColors.BASE03);
        gc.setFont(new Font(gc.getFont().getName(), 9));

        gc.strokeText(texto, x, y);
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
                nodo.getCamino()),
                calcularNodoCoordY(nodo.getNivel(), nivelesCount),
                nodo.getEsHoja() ? nodo.getClaseHoja() == null ? "?" : nodo.getClaseHoja().toString() : null);
        if (nodo.getHijos() != null && !nodo.getHijos().isEmpty()) {
            for (NodoDTO hijo : nodo.getHijos()) {
                dibujarNodos(hijo);
            }
        }
    }

    private void dibujarArista(double startX, double startY, double endX, double endY) {
        gc.setStroke(SolarizedColors.BASE03);
        gc.setLineWidth(1);
        gc.strokeLine(startX, startY, endX, endY);
    }

    private void dibujarNodo(double xCentro, double yCentro, String label) {
        gc.setStroke(SolarizedColors.BASE03);
        gc.setFill(SolarizedColors.BASE2);
        gc.setLineWidth(1);
        double radioNodo = diametroNodo / 2;
        gc.fillOval(xCentro - radioNodo, yCentro - radioNodo, diametroNodo, diametroNodo);
        gc.strokeOval(xCentro - radioNodo, yCentro - radioNodo, diametroNodo, diametroNodo);

        if (label != null) {
            gc.setTextAlign(TextAlignment.CENTER);
            gc.setTextBaseline(VPos.CENTER);

            if ("?".equals(label)) {
                gc.setStroke(SolarizedColors.getColorByClase(0));
            } else {
                gc.setStroke(SolarizedColors.getColorByClase(Integer.valueOf(label)));
            }
            gc.setFont(new Font(gc.getFont().getName(), 10));

            gc.strokeText(label, xCentro, yCentro);
        }

    }



    public NodoDTO getNodoRaiz() {
        return nodoRaiz;
    }

    public void setNodoRaiz(NodoDTO nodoRaiz) {
        this.nodoRaiz = nodoRaiz;
    }

    private double calcularNodoCoordY(Integer nivel, Integer nivelesCount) {
        return (getInnerHeightPane() / nivelesCount) * (nivel - 0.5);
    }

    private double calcularNodoCoordX(List<Integer> desplazamientos) {
        double coordX = getInnerWidthPane() / 2;
        double offsetX = getInnerWidthPane() / 2;
        for (Integer desp : desplazamientos) {
            offsetX = offsetX / 2;
            coordX = coordX + (desp * offsetX);
        }
        return coordX;
    }

    @Override
    protected void layoutChildren() {
        double top = snappedTopInset();
        double left = snappedLeftInset();
        double w = getInnerWidthPane();
        double h = getInnerHeightPane();
        canvas.setLayoutX(left);
        canvas.setLayoutY(top);
        if (w != canvas.getWidth() || h != canvas.getHeight()) {
            canvas.setWidth(w);
            canvas.setHeight(h);
            redraw();
        }
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


}
