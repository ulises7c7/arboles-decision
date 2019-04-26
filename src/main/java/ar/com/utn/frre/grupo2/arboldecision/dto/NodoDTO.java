/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.com.utn.frre.grupo2.arboldecision.dto;

import java.util.List;

/**
 *
 * @author ulises
 */
public class NodoDTO {

    private NodoDTO padre;
    private List<NodoDTO> hijos;
    private Boolean esHoja;
    private Boolean esHojaPura;
    private Integer claseHoja;

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




}
