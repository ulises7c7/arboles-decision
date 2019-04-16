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
public class ArbolDTO {

    private ArbolDTO padre;
    private List<ArbolDTO> hijos;

    public ArbolDTO getPadre() {
        return padre;
    }

    public void setPadre(ArbolDTO padre) {
        this.padre = padre;
    }

    public List<ArbolDTO> getHijos() {
        return hijos;
    }

    public void setHijos(List<ArbolDTO> hijos) {
        this.hijos = hijos;
    }


}
