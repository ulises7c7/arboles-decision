/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.com.utn.frre.grupo2.arboldecision.dto;

import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author ulises
 */
public class RangosDTO {

    private List<BigDecimal> particionesX;
    private List<BigDecimal> particionesY;


    public List<BigDecimal> getParticionesX() {
        return particionesX;
    }

    public void setParticionesX(List<BigDecimal> particionesX) {
        this.particionesX = particionesX;
    }

    public List<BigDecimal> getParticionesY() {
        return particionesY;
    }

    public void setParticionesY(List<BigDecimal> particionesY) {
        this.particionesY = particionesY;
    }

    public boolean esPosibleParticionarEnX() {
        return particionesX.size() > 2;
    }
    public boolean esPosibleParticionarEnY() {
        return particionesY.size() > 2;
    }



}
