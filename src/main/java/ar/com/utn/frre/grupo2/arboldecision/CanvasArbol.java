/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.com.utn.frre.grupo2.arboldecision;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

/**
 *
 * @author ulises
 */
public class CanvasArbol extends Canvas {

    private GraphicsContext gc;

    public CanvasArbol() {
        super();
        this.setHeight(400);
        this.setWidth(300);
        gc = this.getGraphicsContext2D();
    }


}
