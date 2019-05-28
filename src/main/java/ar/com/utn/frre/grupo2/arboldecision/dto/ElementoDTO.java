/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.com.utn.frre.grupo2.arboldecision.dto;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;
import org.beanio.annotation.Field;
import org.beanio.annotation.Record;

/**
 *
 * @author ulises
 */
@Record(name = ElementoDTO.RECORD_NAME)
public class ElementoDTO {

    public static final String RECORD_NAME = "elemento";
    private final String uuid = UUID.randomUUID().toString();

    @Field(at = 0)
    private BigDecimal coordX;
    @Field(at = 1)
    private BigDecimal coordY;
    @Field(at = 2)
    private Integer clase;

    private String resultadoClasificacion;

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

    public String getResultadoClasificacion() {
        return resultadoClasificacion;
    }

    public void setResultadoClasificacion(String resultadoClasificacion) {
        this.resultadoClasificacion = resultadoClasificacion;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + Objects.hashCode(this.uuid);
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
        if (!Objects.equals(this.uuid, other.uuid)) {
            return false;
        }
        return true;
    }

}
