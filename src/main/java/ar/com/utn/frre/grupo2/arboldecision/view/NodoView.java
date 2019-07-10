/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.com.utn.frre.grupo2.arboldecision.view;

import ar.com.utn.frre.grupo2.arboldecision.dto.NodoDTO;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

/**
 *
 * @author ulises
 */
public class NodoView extends Circle {

    private final Paint colorBase;
    private final NodoDTO nodoDTO;

    public NodoView(double centerX, double centerY, double radius, NodoDTO nodoDTO) {
        super(centerX, centerY, radius);

        this.nodoDTO = nodoDTO;
        this.colorBase = nodoDTO.getEsHoja() && !nodoDTO.getEsHojaPura() ? SolarizedColors.BASE1 : SolarizedColors.BASE2;
        this.setStroke(SolarizedColors.BASE03);
        this.setFill(colorBase);
        this.setStrokeWidth(1);
    }

    public boolean requestSelection(boolean select) {
        return true;
    }

    public void notifySelection(boolean select) {
        if (select) {
            this.setStrokeWidth(2);
            this.setStroke(SolarizedColors.BLUE);
        } else {
            this.setStrokeWidth(1);
            this.setStroke(SolarizedColors.BASE03);
        }
    }

    public NodoDTO getNodoDTO() {
        return nodoDTO;
    }

}
