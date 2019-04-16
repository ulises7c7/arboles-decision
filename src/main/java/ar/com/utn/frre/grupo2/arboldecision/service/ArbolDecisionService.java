/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.com.utn.frre.grupo2.arboldecision.service;

import ar.com.utn.frre.grupo2.arboldecision.dto.ElementoDTO;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 *
 * @author ulises
 */
public class ArbolDecisionService {

    /**
     * A partir de la lista de elementos pasados por parametro, devuelve la
     * lista de todos los valores posibles de X ordenados de forma creciente
     *
     * @param elementos
     * @return
     */
    public List<BigDecimal> obtenerValoresX(List<ElementoDTO> elementos) {
        Set<BigDecimal> valoresX = new HashSet<>();

        for (ElementoDTO elemento : elementos) {
            valoresX.add(elemento.getCoordX());
        }

        List<BigDecimal> list = new ArrayList<>(valoresX);


        Collections.sort(list, (a, b) -> a.compareTo(b));
        return list;
    }

    /**
     * A partir de la lista de elementos pasados por parametro, devuelve la
     * lista de todos los valores posibles de X ordenados de forma creciente
     *
     * @param elementos
     * @return
     */
    public List<BigDecimal> obtenerValoresY(List<ElementoDTO> elementos) {
        Set<BigDecimal> valoresX = new HashSet<>();

        for (ElementoDTO elemento : elementos) {
            valoresX.add(elemento.getCoordY());
        }

        List<BigDecimal> list = new ArrayList<>(valoresX);

        Collections.sort(list, (a, b) -> a.compareTo(b));
        return list;
    }

    /**
     * Obtiene los valores intermedios de la lista de numeros pasados como
     * parametro
     *
     * @param valores
     * @return
     */
    public List<BigDecimal> obtenerValoresIntermedios(List<BigDecimal> valores) {
        List<BigDecimal> intermedios = new ArrayList<>();
        intermedios.add(valores.get(0).subtract(new BigDecimal(0.01)));
        for (int i = 0; i < valores.size() - 1; i++) {
            intermedios.add(valores.get(i).add(valores.get(i + 1).divide(new BigDecimal(2), valores.get(i).scale() + 1, RoundingMode.HALF_UP)));
        }
        intermedios.add(valores.get(valores.size() - 1).add(new BigDecimal(0.01)));
        return intermedios;
    }

    private Integer claseTodosElementos(List<ElementoDTO> elementos) {

        if (!elementos.isEmpty()) {
            Integer claseTest = elementos.get(0).getClase();
            for (ElementoDTO elemento : elementos) {
                if (!Objects.equals(elemento.getClase(), claseTest)) {
                    return null;
                }
            }
            return claseTest;
        }
        return null;
    }

}
