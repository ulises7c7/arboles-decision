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
import java.util.Arrays;
import java.util.List;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

/**
 *
 * @author ulises
 */
public class CanvasGraficoController {

    private Canvas canvas;
    private BigDecimal factorScale = new BigDecimal(80);
    private GraphicsContext gc;
    private Integer margen = 300;
    private List<Paint> colores = Arrays.asList(SolarizedColors.BASE3, SolarizedColors.BLUE, SolarizedColors.RED); //TODO: generalizar esto para n clases

    private NodoDTO nodoRaiz;
    private List<ElementoDTO> elementos;

    public CanvasGraficoController(Canvas canvas, List<ElementoDTO> elementos, NodoDTO nodoRaiz) {

        this.canvas = canvas;
        this.elementos = elementos;
        this.nodoRaiz = nodoRaiz;

        canvas.setHeight(400);
        canvas.setWidth(300);
        canvas.setOnMouseExited((event) -> {
            redraw();
        });

        canvas.setOnScroll((event) -> {
            double zoomFactor = 1.05;
            double deltaY = event.getDeltaY();
            System.out.println("Delta Y: " + deltaY);
            if (deltaY > 0) {
                zoomFactor = 2.0 - zoomFactor;
            }
            factorScale = factorScale.multiply(new BigDecimal(2 - zoomFactor));

            redraw();
        });

        canvas.setOnMouseMoved((event) -> {
            redraw();
            gc.setStroke(Color.color(SolarizedColors.BASE3.getRed(), SolarizedColors.BASE3.getGreen(), SolarizedColors.BASE3.getBlue(), 0.5));
            gc.setLineWidth(0.5);
            gc.strokeLine(event.getSceneX() - canvas.getLayoutX(), 0, event.getSceneX() - canvas.getLayoutX(), canvas.getHeight());
            gc.strokeLine(0, event.getSceneY() - canvas.getLayoutY(), canvas.getWidth(), event.getSceneY() - canvas.getLayoutY());

        });
        gc = canvas.getGraphicsContext2D();

    }

    private void dibujarParticiones(NodoDTO nodo, GraphicsContext gc) {
        if (nodo != null && nodo.getHijos() != null && !nodo.getHijos().isEmpty()) {

            for (NodoDTO hijo : nodo.getHijos()) {
                BigDecimal coordenadaParticion = hijo.getValorParticion();
                BigDecimal inicio = hijo.getEjeParticion() == ArbolDecisionService.EJE_X ? hijo.getRangosDTO().getCotaInferiorY() : hijo.getRangosDTO().getCotaInferiorX();
                BigDecimal fin = hijo.getEjeParticion() == ArbolDecisionService.EJE_X ? hijo.getRangosDTO().getCotaSuperiorY() : hijo.getRangosDTO().getCotaSuperiorX();

                if (hijo.getEjeParticion() == ArbolDecisionService.EJE_X) {
                    dibujarRecta(coordenadaParticion, inicio, coordenadaParticion, fin, gc);
                } else {
                    dibujarRecta(inicio, coordenadaParticion, fin, coordenadaParticion, gc);
                }

                dibujarParticiones(hijo, gc);
            }
        }
    }

    private void dibujarRecta(BigDecimal coordX1, BigDecimal coordY1, BigDecimal coordX2, BigDecimal coordY2, GraphicsContext gc) {
        gc.setStroke(SolarizedColors.BASE3);
        gc.setLineWidth(1);
        gc.strokeLine(corregirX(coordX1), corregirY(coordY1), corregirX(coordX2), corregirY(coordY2));
    }

    private void dibujarEjes(GraphicsContext gc) {
        gc.setStroke(Color.color(SolarizedColors.BASE3.getRed(), SolarizedColors.BASE3.getGreen(), SolarizedColors.BASE3.getBlue(), 0.8));
        gc.setLineWidth(0.5);
        //EJE X
        gc.strokeLine(corregirX(BigDecimal.ZERO), 0, corregirX(BigDecimal.ZERO), canvas.getHeight());
        //EJE Y
        gc.strokeLine(0, corregirY(BigDecimal.ZERO), canvas.getWidth(), corregirY(BigDecimal.ZERO));
    }

    private void dibujarPunto(BigDecimal coordX, BigDecimal coordY, Paint color, GraphicsContext gc) {
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
        redraw(nodoRaiz, elementos);
    }

    public void redraw(NodoDTO nodoRaiz, List<ElementoDTO> elementos) {

        this.nodoRaiz = nodoRaiz;
        this.elementos = elementos;

        //Pinto el fondo
        gc.setFill(SolarizedColors.BASE03);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        //Pintar ejes
        dibujarEjes(gc);

        //Dibujar divisiones (Si el algoritmo ya fue ejecutado)
        dibujarParticiones(nodoRaiz, gc);

        //Dibujar puntos
        for (ElementoDTO elemento : elementos) {
            dibujarPunto(elemento.getCoordX(), elemento.getCoordY(), colores.get(elemento.getClase()), gc);
        }

    }


}