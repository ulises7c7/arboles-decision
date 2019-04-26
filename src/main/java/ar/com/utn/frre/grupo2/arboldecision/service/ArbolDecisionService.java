/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.com.utn.frre.grupo2.arboldecision.service;

import ar.com.utn.frre.grupo2.arboldecision.dto.ElementoDTO;
import ar.com.utn.frre.grupo2.arboldecision.dto.NodoDTO;
import ar.com.utn.frre.grupo2.arboldecision.dto.RangosDTO;
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

    private static final int EJE_X = 1;
    private static final int EJE_Y = 2;

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

    /**
     * Obtiene la entropia del conjunto de elementos pasados como parametros
     *
     *
     * @param elementos
     * @return
     */
    private BigDecimal impurityEval1(List<ElementoDTO> elementos) {
        throw new UnsupportedOperationException("Impurity eval 1 no implementada");
    }

    /**
     * Obtiene la entropia resultante de particionar al cunjunto pasado como
     * parametro al particionar de acuerdo a los parametros de particion
     *
     * @param elementos
     * @return
     */
    private BigDecimal impurityEval2(List<ElementoDTO> elementos,
            BigDecimal valorParticion, Integer ejeParticion) {
        throw new UnsupportedOperationException("Impurity eval 2 no implementada");
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

    private List<ElementoDTO> particionar(List<ElementoDTO> elementos,
            BigDecimal valorParticion, Integer ejeParticion, Boolean obtenerMenores) {
        throw new UnsupportedOperationException("Particionar conjuntos no implementado aun");
    }

    private RangosDTO generarRangoParticion() {
        throw new UnsupportedOperationException("Generar rango de particion no implementado aun");
    }

    public void decisionTree(List<ElementoDTO> elementos, RangosDTO rangos, NodoDTO nodo, BigDecimal umbral) {
        if (claseTodosElementos(elementos) != null) {
            Integer claseHoja = claseTodosElementos(elementos);
            nodo.setEsHoja(Boolean.TRUE);
            nodo.setEsHojaPura(Boolean.TRUE);
            nodo.setClaseHoja(claseHoja);

            /*
             * } else if ( A = 'vacío') {
             *     //hacer T hoja de clase cj,
             *     //con cj = clase mas frecuente en D
             *
             * el caso A = 'vacío' nunca se va a dar,
             * a lo sumo obtendremos una partición pura
             * con un unico elemento
             */
        } else {

            BigDecimal valorDivision = null;
            BigDecimal menorEntropia = null;
            Integer ejeDivision = null;

            BigDecimal entropiaTotal = impurityEval1(elementos);

            //Obtengo la mejor particion para el eje X
            for (BigDecimal particionX : rangos.getParticionesX()) {
                BigDecimal entropiaParticion = impurityEval2(elementos, particionX, EJE_X);

                if (menorEntropia == null
                        || entropiaParticion.compareTo(menorEntropia) == -1) {
                    menorEntropia = entropiaParticion;
                    valorDivision = particionX;
                    ejeDivision = EJE_X;
                }
            }

            //Obtengo la menor particion para el eje Y
            for (BigDecimal particionY : rangos.getParticionesY()) {
                BigDecimal entropiaParticion = impurityEval2(elementos, particionY, EJE_Y);

                if (menorEntropia == null
                        || entropiaParticion.compareTo(menorEntropia) == -1) {
                    menorEntropia = entropiaParticion;
                    valorDivision = particionY;
                    ejeDivision = EJE_Y;
                }
            }

            if (entropiaTotal.subtract(menorEntropia).compareTo(umbral) == -1) {
                //TODO: tratar este caso, se hace T nodo hoja de clase cj mas frecuente
            } else {
                //TODO: particionar conjunto D en dos y tratar cada "mitad"
                nodo.setEsHoja(Boolean.FALSE);

                List<ElementoDTO> particion1 = particionar(elementos, valorDivision, ejeDivision, Boolean.TRUE);
                NodoDTO nodoParticion1 = new NodoDTO();

                nodo.getHijos().add(nodoParticion1);

                RangosDTO rangosParticion1 = generarRangoParticion();
                decisionTree(particion1, rangosParticion1, nodoParticion1, umbral);

                List<ElementoDTO> particion2 = particionar(elementos, valorDivision, ejeDivision, Boolean.FALSE);
                //TODO: hacer lo mismo que con particion 1

//                decisionTree(particion2, rangosParticion2, nodoParticion2, umbral);

            }



        }
    }



}
