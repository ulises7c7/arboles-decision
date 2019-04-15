/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.com.utn.frre.grupo2.arboldecision.dto;

import java.math.BigDecimal;
import java.util.Objects;
import org.beanio.annotation.Field;
import org.beanio.annotation.Record;

/**
 *
 * @author ulises
 */
@Record(name = ElementoDTO.RECORD_NAME)
public class ElementoDTO {

    public static final String RECORD_NAME = "elemento";

    @Field(at = 0)
    private BigDecimal coordX;
    @Field(at = 1)
    private BigDecimal coordY;
    @Field(at = 2)
    private Integer clase;

    public BigDecimal getCoordX() {
        return coordX;
    }

    public void setCoordX(BigDecimal coordX) {
        this.coordX = coordX;
    }

    public BigDecimal getCoordY() {
        return coordY;
    }

    public void setCoordY(BigDecimal coordY) {
        this.coordY = coordY;
    }

    public Integer getClase() {
        return clase;
    }

    public void setClase(Integer clase) {
        this.clase = clase;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 23 * hash + Objects.hashCode(this.coordX);
        hash = 23 * hash + Objects.hashCode(this.coordY);
        hash = 23 * hash + Objects.hashCode(this.clase);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ElementoDTO other = (ElementoDTO) obj;
        if (!Objects.equals(this.coordX, other.coordX)) {
            return false;
        }
        if (!Objects.equals(this.coordY, other.coordY)) {
            return false;
        }
        if (!Objects.equals(this.clase, other.clase)) {
            return false;
        }
        return true;
    }

}
