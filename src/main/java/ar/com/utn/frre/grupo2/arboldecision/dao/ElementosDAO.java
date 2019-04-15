/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.com.utn.frre.grupo2.arboldecision.dao;

import ar.com.utn.frre.grupo2.arboldecision.dto.ElementoDTO;

/**
 *
 * @author ulises
 */
public class ElementosDAO extends AbstractDAO<ElementoDTO> {

    public ElementosDAO() {
        super(ElementoDTO.class, ElementoDTO.RECORD_NAME);
    }

}
