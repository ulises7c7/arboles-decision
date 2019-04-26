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

    private BigDecimal cotaInferiorX;
    private BigDecimal cotaSuperiorX;

    private BigDecimal cotaInferiorY;
    private BigDecimal cotaSuperiorY;

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

    public BigDecimal getCotaInferiorX() {
        return cotaInferiorX;
    }

    public void setCotaInferiorX(BigDecimal cotaInferiorX) {
        this.cotaInferiorX = cotaInferiorX;
    }

    public BigDecimal getCotaSuperiorX() {
        return cotaSuperiorX;
    }

    public void setCotaSuperiorX(BigDecimal cotaSuperiorX) {
        this.cotaSuperiorX = cotaSuperiorX;
    }

    public BigDecimal getCotaInferiorY() {
        return cotaInferiorY;
    }

    public void setCotaInferiorY(BigDecimal cotaInferiorY) {
        this.cotaInferiorY = cotaInferiorY;
    }

    public BigDecimal getCotaSuperiorY() {
        return cotaSuperiorY;
    }

    public void setCotaSuperiorY(BigDecimal cotaSuperiorY) {
        this.cotaSuperiorY = cotaSuperiorY;
    }



}
