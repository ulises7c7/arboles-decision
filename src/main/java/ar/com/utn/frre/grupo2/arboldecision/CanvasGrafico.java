/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.com.utn.frre.grupo2.arboldecision;

import ar.com.utn.frre.grupo2.arboldecision.dto.ElementoDTO;
import ar.com.utn.frre.grupo2.arboldecision.dto.NodoDTO;
import ar.com.utn.frre.grupo2.arboldecision.service.ArbolDecisionService;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.List;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

/**
 *
 * @author ulises
 */
public class CanvasGrafico extends Pane {

    private double factorScale = 80;
    private GraphicsContext gc;
    private double offsetY = 300;
    private double offsetX = 50;
    private final Canvas canvas = new Canvas();
    DecimalFormat df = new DecimalFormat();

    private NodoDTO nodoRaiz;
    private List<ElementoDTO> elementos;

    public CanvasGrafico() {
        super();

        getChildren().add(canvas);

        df.setMaximumFractionDigits(3);
        df.setMinimumFractionDigits(3);
        df.setGroupingUsed(false);

        this.setOnMouseExited((event) -> {
            redraw();
        });

        this.setOnScroll((event) -> {

            scrollZoom(event.getDeltaY(), event.getX(), event.getY());

            redraw();
        });

        this.setOnMouseMoved((event) -> {
            redraw();
            dibujarCoordenadas(event.getX(), event.getY());
            gc.setStroke(Color.color(SolarizedColors.BASE3.getRed(), SolarizedColors.BASE3.getGreen(), SolarizedColors.BASE3.getBlue(), 0.5));
            gc.setLineWidth(0.5);
            gc.strokeLine(event.getX(), 0, event.getX(), this.getHeight());
            gc.strokeLine(0, event.getY(), this.getWidth(), event.getY());

        });
        gc = canvas.getGraphicsContext2D();
        pintarFondo();
        dibujarEjes();

    }

    private void scrollZoom(double deltaY, double xCursor, double yCursor) {
        double zoomFactor = 1.05;
        if (deltaY > 0) {
            zoomFactor = 2.0 - zoomFactor;
        }

        BigDecimal xReal = traducirX(xCursor);
        BigDecimal yReal = traducirY(yCursor);

        factorScale = factorScale * (2 - zoomFactor);

        double corregirX = corregirX(xReal);
        double corregirY = corregirY(yReal);

        offsetX = offsetX + xCursor - corregirX;
        offsetY = offsetY + yCursor - corregirY;
    }

    private void dibujarCoordenadas(double canvasCoordX, double canvasCoordY) {

        gc.setTextAlign(TextAlignment.RIGHT);
        gc.setTextBaseline(VPos.CENTER);

        gc.setStroke(SolarizedColors.BASE3);
        gc.setLineWidth(0.5);
        gc.setFont(new Font(gc.getFont().getName(), 10));

        String texto = String.format("(%s;%s)",
                df.format(traducirX(canvasCoordX)),
                df.format(traducirY(canvasCoordY)));

        gc.strokeText(texto, prefWidth(offsetY) - 10, 10);

    }

    private void dibujarParticiones(NodoDTO nodo) {
        if (nodo != null && nodo.getHijos() != null && !nodo.getHijos().isEmpty()) {

            for (NodoDTO hijo : nodo.getHijos()) {
                BigDecimal coordenadaParticion = hijo.getValorParticion();
                BigDecimal inicio = hijo.getEjeParticion() == ArbolDecisionService.EJE_X ? hijo.getRangosDTO().getCotaInferiorY() : hijo.getRangosDTO().getCotaInferiorX();
                BigDecimal fin = hijo.getEjeParticion() == ArbolDecisionService.EJE_X ? hijo.getRangosDTO().getCotaSuperiorY() : hijo.getRangosDTO().getCotaSuperiorX();

                if (hijo.getEjeParticion() == ArbolDecisionService.EJE_X) {
                    dibujarRecta(coordenadaParticion, inicio, coordenadaParticion, fin);
                } else {
                    dibujarRecta(inicio, coordenadaParticion, fin, coordenadaParticion);
                }

                dibujarParticiones(hijo);
            }
        }
    }

    private void dibujarRecta(BigDecimal coordX1, BigDecimal coordY1, BigDecimal coordX2, BigDecimal coordY2) {
        gc.setStroke(SolarizedColors.BASE3);
        gc.setLineWidth(1);
        gc.strokeLine(corregirX(coordX1), corregirY(coordY1), corregirX(coordX2), corregirY(coordY2));
    }

    private void dibujarEjes() {
        gc.setStroke(Color.color(SolarizedColors.BASE3.getRed(), SolarizedColors.BASE3.getGreen(), SolarizedColors.BASE3.getBlue(), 0.8));
        gc.setLineWidth(0.5);
        //EJE X
        gc.strokeLine(corregirX(BigDecimal.ZERO), 0, corregirX(BigDecimal.ZERO), this.getHeight());
        //EJE Y
        gc.strokeLine(0, corregirY(BigDecimal.ZERO), this.getWidth(), corregirY(BigDecimal.ZERO));
    }

    private void dibujarPunto(BigDecimal coordX, BigDecimal coordY, Paint color) {
        gc.setStroke(color);
        gc.setLineWidth(3);
        gc.strokeLine(corregirX(coordX), corregirY(coordY), corregirX(coordX), corregirY(coordY));

    }

    private double corregirX(BigDecimal coordX) {
        return coordX.multiply(new BigDecimal(factorScale)).setScale(0, RoundingMode.HALF_UP).longValue() + offsetX;
    }

    private double corregirY(BigDecimal coordY) {
        return -coordY.multiply(new BigDecimal(factorScale)).setScale(0, RoundingMode.HALF_UP).longValue() + offsetY;
    }

    private BigDecimal traducirX(double x) {
        return new BigDecimal((x - offsetX) / factorScale);
    }

    private BigDecimal traducirY(double y) {
        return new BigDecimal((offsetY - y) / factorScale);
    }

    public void redraw() {
        pintarFondo();
        dibujarEjes();

        //Dibujar divisiones (Si el algoritmo ya fue ejecutado)
        dibujarParticiones(nodoRaiz);

        dibujarPuntos();

    }

    private void dibujarPuntos() {
        if (elementos != null) {
            for (ElementoDTO elemento : elementos) {
                dibujarPunto(elemento.getCoordX(), elemento.getCoordY(),
                        SolarizedColors.getColorByClase(elemento.getClase()));
            }
        }
    }

    private void pintarFondo() {
        gc.setFill(SolarizedColors.BASE03);
        gc.fillRect(0, 0, this.getWidth(), this.getHeight());
    }

    public NodoDTO getNodoRaiz() {
        return nodoRaiz;
    }

    public void setNodoRaiz(NodoDTO nodoRaiz) {
        this.nodoRaiz = nodoRaiz;
    }

    public List<ElementoDTO> getElementos() {
        return elementos;
    }

    public void setElementos(List<ElementoDTO> elementos) {
        this.elementos = elementos;
    }

    @Override
    protected void layoutChildren() {
        double top = snappedTopInset();
        double right = snappedRightInset();
        double bottom = snappedBottomInset();
        double left = snappedLeftInset();
        double w = getWidth() - left - right;
        double h = getHeight() - top - bottom;
        canvas.setLayoutX(left);
        canvas.setLayoutY(top);
        if (w != canvas.getWidth() || h != canvas.getHeight()) {
            canvas.setWidth(w);
            canvas.setHeight(h);
            redraw();
        }
    }

}
