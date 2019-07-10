/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.com.utn.frre.grupo2.arboldecision.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 *
 * @author ulises
 */
public class NodoDTO {

    private final String uuid = UUID.randomUUID().toString();

    private NodoDTO padre;
    private List<NodoDTO> hijos;
    private Boolean esHoja;
    private Boolean esHojaPura;
    private Integer claseHoja;
    private String claseHojaString;
    private BigDecimal valorParticion;
    private Integer ejeParticion;
    private Boolean esRamaMenor;
    private List<ElementoDTO> elementos;
    private RangosDTO rangosDTO;
    private Integer nivel;
    private BigDecimal entropia;
    private List<Integer> camino = new ArrayList<>();

    public NodoDTO() {
    }

    public NodoDTO(NodoDTO padre, BigDecimal valorParticion, Integer ejeParticion, Boolean esRamaMenor, List<ElementoDTO> elementos, RangosDTO rangosDTO) {
        this.padre = padre;
        this.valorParticion = valorParticion;
        this.ejeParticion = ejeParticion;
        this.esRamaMenor = esRamaMenor;
        this.elementos = elementos;
        this.rangosDTO = rangosDTO;

        //Los siguientes atributos son utilizados para el diagrama
        this.nivel = padre.getNivel() + 1;
        this.camino.addAll(padre.getCamino());
        this.camino.add(esRamaMenor ? -1 : 1);
    }

    public NodoDTO getPadre() {
        return padre;
    }

    public void setPadre(NodoDTO padre) {
        this.padre = padre;
    }

    public List<NodoDTO> getHijos() {
        return hijos;
    }

    public void setHijos(List<NodoDTO> hijos) {
        this.hijos = hijos;
    }

    public Boolean getEsHoja() {
        return esHoja;
    }

    public void setEsHoja(Boolean esHoja) {
        this.esHoja = esHoja;
    }

    public Boolean getEsHojaPura() {
        return esHojaPura;
    }

    public void setEsHojaPura(Boolean esHojaPura) {
        this.esHojaPura = esHojaPura;
    }

    public Integer getClaseHoja() {
        return claseHoja;
    }

    public void setClaseHoja(Integer claseHoja) {
        this.claseHoja = claseHoja;
    }

    public BigDecimal getValorParticion() {
        return valorParticion;
    }

    public void setValorParticion(BigDecimal valorParticion) {
        this.valorParticion = valorParticion;
    }

    public Integer getEjeParticion() {
        return ejeParticion;
    }

    public void setEjeParticion(Integer ejeParticion) {
        this.ejeParticion = ejeParticion;
    }

    public Boolean getEsRamaMenor() {
        return esRamaMenor;
    }

    public void setEsRamaMenor(Boolean esRamaMenor) {
        this.esRamaMenor = esRamaMenor;
    }

    public List<ElementoDTO> getElementos() {
        return elementos;
    }

    public void setElementos(List<ElementoDTO> elementos) {
        this.elementos = elementos;
    }

    public RangosDTO getRangosDTO() {
        return rangosDTO;
    }

    public void setRangosDTO(RangosDTO rangosDTO) {
        this.rangosDTO = rangosDTO;
    }

    public Integer getNivel() {
        return nivel;
    }

    public void setNivel(Integer nivel) {
        this.nivel = nivel;
    }

    public List<Integer> getCamino() {
        return camino;
    }

    public void setCamino(List<Integer> camino) {
        this.camino = camino;
    }

    public BigDecimal getEntropia() {
        return entropia;
    }

    public void setEntropia(BigDecimal entropia) {
        this.entropia = entropia;
    }

    public String getClaseHojaString() {
        return claseHojaString;
    }

    public void setClaseHojaString(String claseHojaString) {
        this.claseHojaString = claseHojaString;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 59 * hash + Objects.hashCode(this.uuid);
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
        final NodoDTO other = (NodoDTO) obj;
        if (!Objects.equals(this.uuid, other.uuid)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return esHoja ? (claseHoja == null ? "?" : claseHojaString) : "   ";
    }

}
