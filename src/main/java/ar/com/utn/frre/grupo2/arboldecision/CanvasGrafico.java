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
import java.util.List;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

/**
 *
 * @author ulises
 */
public class CanvasGrafico extends Canvas {

    private BigDecimal factorScale = new BigDecimal(80);
    private GraphicsContext gc;
    private Integer margen = 300;


    private NodoDTO nodoRaiz;
    private List<ElementoDTO> elementos;

    public CanvasGrafico() {
        super();

        this.setHeight(400);
        this.setWidth(300);
        this.setOnMouseExited((event) -> {
            redraw();
        });

        this.setOnScroll((event) -> {
            double zoomFactor = 1.05;
            double deltaY = event.getDeltaY();
            if (deltaY > 0) {
                zoomFactor = 2.0 - zoomFactor;
            }
            factorScale = factorScale.multiply(new BigDecimal(2 - zoomFactor));

            redraw();
        });

        this.setOnMouseMoved((event) -> {
            redraw();
            gc.setStroke(Color.color(SolarizedColors.BASE3.getRed(), SolarizedColors.BASE3.getGreen(), SolarizedColors.BASE3.getBlue(), 0.5));
            gc.setLineWidth(0.5);
            gc.strokeLine(event.getSceneX() - this.getLayoutX(), 0, event.getSceneX() - this.getLayoutX(), this.getHeight());
            gc.strokeLine(0, event.getSceneY() - this.getLayoutY(), this.getWidth(), event.getSceneY() - this.getLayoutY());

        });
        gc = this.getGraphicsContext2D();
        pintarFondo();
        dibujarEjes();

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

    private Long corregirX(BigDecimal coordX) {
        return coordX.multiply(factorScale).setScale(0, RoundingMode.HALF_UP).longValue();
    }

    private Long corregirY(BigDecimal coordY) {
        return -coordY.multiply(factorScale).setScale(0, RoundingMode.HALF_UP).longValue() + margen;
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

}
