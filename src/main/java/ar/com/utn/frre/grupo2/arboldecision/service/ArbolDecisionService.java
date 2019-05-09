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
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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

        //TODO: verificar esto
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
     * parametro. Ademas agrega un valor a la izquierda y uno a la derecha del
     * conjunto como cotas superior e inferior
     *
     * @param valores
     * @return
     */
    public List<BigDecimal> obtenerValoresIntermedios(List<BigDecimal> valores) {
        List<BigDecimal> intermedios = new ArrayList<>();
        intermedios.add(valores.get(0).subtract(new BigDecimal(0.01)));
        for (int i = 0; i < valores.size() - 1; i++) {
            intermedios.add(valores.get(i).add(valores.get(i + 1)).divide(new BigDecimal(2), valores.get(i).scale() + 2, RoundingMode.HALF_UP));
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
        //Obtengo la cantidad de elementos por cada
        Map<Integer, Integer> countPorClase = conteoPorClase(elementos);

        Collection<Integer> values = countPorClase.values();

        BigDecimal entropia = BigDecimal.ZERO;
        Integer cantElementos = elementos.size();

        for (Integer conteoClase : values) {
            BigDecimal probabilidadClase = new BigDecimal(conteoClase).divide(new BigDecimal(cantElementos), 4, RoundingMode.HALF_UP);
            entropia = entropia.add(probabilidadClase.multiply(logartimoBase2(probabilidadClase)));
        }

        return entropia.negate();
    }

    /**
     * Devuelve un Map de dos enteros (clase, clase.count) con el conteo de cada
     * clase de los elementos pasados por parametro
     *
     * @return
     */
    private Map<Integer, Integer> conteoPorClase(List<ElementoDTO> elementos) {

        Map<Integer, Integer> countPorClase = new HashMap<>();

        for (ElementoDTO elemento : elementos) {
            Integer conteo = countPorClase.get(elemento.getClase());
            if (conteo == null) {
                conteo = 0;
            }
            countPorClase.put(elemento.getClase(), conteo + 1);
        }
        return countPorClase;
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

        List<ElementoDTO> elementos1 = particionar(elementos, valorParticion, ejeParticion, Boolean.TRUE);
        List<ElementoDTO> elementos2 = particionar(elementos, valorParticion, ejeParticion, Boolean.FALSE);

        BigDecimal sizeElementos = new BigDecimal(elementos.size());
        BigDecimal sizeElementos1 = new BigDecimal(elementos1.size());
        BigDecimal sizeElementos2 = new BigDecimal(elementos2.size());

        BigDecimal entropia1 = impurityEval1(elementos1);
        BigDecimal entropia2 = impurityEval1(elementos2);

        return sizeElementos1.divide(sizeElementos, 4, RoundingMode.HALF_UP).multiply(entropia1).add(
                sizeElementos2.divide(sizeElementos, 4, RoundingMode.HALF_UP).multiply(entropia2)
        );

    }

    private BigDecimal logartimoBase2(BigDecimal numero) {
        double logaritmo = Math.log10(numero.doubleValue()) / Math.log10(2d);
        return BigDecimal.valueOf(logaritmo).setScale(4, RoundingMode.HALF_UP);
    }

    private Integer claseTodosElementos(List<ElementoDTO> elementos) {

        if (elementos != null && !elementos.isEmpty()) {
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

    /**
     * Obtiene la clase mas frecuente del conjunto pasado como parametro
     * Devuelve null si el conjunto es vacio o si no hay clase mas frecuente
     *
     * @param elementos
     * @return
     */
    private Integer claseMasFrecuente(List<ElementoDTO> elementos) {
        if (elementos != null && !elementos.isEmpty()) {
            //Obtengo los pares ( cj ; cj.count )
            Map<Integer, Integer> conteoClases = conteoPorClase(elementos);

            //Si hay una sola clase, retorno la unica clase
            if (conteoClases.keySet().size() == 1) {
                return elementos.get(0).getClase();
            }

            //Ordeno las clases de mayor a menor segun su frecuencia
            List<Integer> values = new ArrayList(conteoClases.values());
            Collections.sort(values, (a, b) -> b.compareTo(a));

            // Si la primera y la segunda clase, ordenadas por su frecuencia,
            // tienen el mismo valor, devuelvo null
            // (no hay clase mas frecuente)
            if (values.get(0).equals(values.get(1))) {
                return null;
            }

            //Sino devuelvo la clase del elemento con la frcuencia maxima
            for (Map.Entry<Integer, Integer> entry : conteoClases.entrySet()) {
                if (Objects.equals(values.get(0), entry.getValue())) {
                    return entry.getKey();
                }
            }

        }
        return null;
    }

    private List<ElementoDTO> particionar(List<ElementoDTO> elementos,
            BigDecimal valorParticion, Integer ejeParticion, Boolean obtenerMenores) {

        //Valor para usar en el compareTo entre la coordenada
        //del elemento y la coordenada de particion
        Integer valorComparacion = obtenerMenores ? -1 : 1;

        List<ElementoDTO> subconjunto = new ArrayList<>();
        for (ElementoDTO elemento : elementos) {
            BigDecimal coordElemento = ejeParticion == EJE_X
                    ? elemento.getCoordX()
                    : elemento.getCoordY();

            if (coordElemento.compareTo(valorParticion) == valorComparacion) {
                subconjunto.add(elemento);
            }
        }

        return subconjunto;
    }

    public RangosDTO generarRangoInicial(List<ElementoDTO> elementos) {
        RangosDTO rangos = new RangosDTO();

        List<BigDecimal> valoresX = obtenerValoresX(elementos);
        List<BigDecimal> valoresY = obtenerValoresY(elementos);

        rangos.setParticionesX(obtenerValoresIntermedios(valoresX));
        rangos.setParticionesY(obtenerValoresIntermedios(valoresY));

        return rangos;
    }

    private RangosDTO generarRangoParticion(RangosDTO rangosOriginales,
            BigDecimal valorParticion, Integer ejeParticion, Boolean obtenerMenores) {
        RangosDTO nuevosRangos = new RangosDTO();

        Integer valorComparacion = obtenerMenores ? -1 : 1;

        List<BigDecimal> valoresParticionOriginales = ejeParticion == EJE_X
                ? rangosOriginales.getParticionesX()
                : rangosOriginales.getParticionesY();

        List<BigDecimal> valoresParticionNuevos = new ArrayList<>();

        for (BigDecimal valor : valoresParticionOriginales) {
            if (valor.compareTo(valorParticion) == valorComparacion
                    || valor.compareTo(valorParticion) == 0) {
                valoresParticionNuevos.add(valor);
            }
        }

        // A Los valores del eje particionado, le asigno los nuevos valores obtenidos arriba
        // A los valores del otro eje, los dejo como estaban en el rangoOriginal
        if (ejeParticion == EJE_X) {
            nuevosRangos.setParticionesX(valoresParticionNuevos);
            nuevosRangos.setParticionesY(new ArrayList<>());
            nuevosRangos.getParticionesY().addAll(rangosOriginales.getParticionesY());
        } else {
            nuevosRangos.setParticionesY(valoresParticionNuevos);
            nuevosRangos.setParticionesX(new ArrayList<>());
            nuevosRangos.getParticionesX().addAll(rangosOriginales.getParticionesX());
        }

        return nuevosRangos;

    }

    public void decisionTree(List<ElementoDTO> elementos, RangosDTO rangos, NodoDTO nodo, BigDecimal umbral) {
        if (claseTodosElementos(elementos) != null) {
            Integer claseHoja = claseTodosElementos(elementos);
            nodo.setEsHoja(Boolean.TRUE);
            nodo.setEsHojaPura(Boolean.TRUE);
            nodo.setClaseHoja(claseHoja);

        } else if (!rangos.esPosibleParticionarEnX() && !rangos.esPosibleParticionarEnY()) {
            // Caso A = vacio
            // T hoja de clase cj, con cj = clase mas frecuente en D

            // Este caso se va a dar cuando haya varios puntos de
            // distintas clases en la misma coordenada
            nodo.setEsHoja(Boolean.TRUE);
            nodo.setEsHojaPura(Boolean.FALSE);
            nodo.setClaseHoja(claseMasFrecuente(elementos));

        } else {

            BigDecimal valorDivision = null;
            BigDecimal menorEntropia = null;
            Integer ejeDivision = null;

            BigDecimal entropiaTotal = impurityEval1(elementos);

            //Obtengo la mejor particion para el eje X
            for (int i = 1; i < rangos.getParticionesX().size() - 1; i++) {
                BigDecimal particionX = rangos.getParticionesX().get(i);
                BigDecimal entropiaParticion = impurityEval2(elementos, particionX, EJE_X);

                if (menorEntropia == null
                        || entropiaParticion.compareTo(menorEntropia) == -1) {
                    menorEntropia = entropiaParticion;
                    valorDivision = particionX;
                    ejeDivision = EJE_X;
                }
            }

            //Obtengo la menor particion para el eje Y
            for (int i = 1; i < rangos.getParticionesY().size() - 1; i++) {
                BigDecimal particionY = rangos.getParticionesY().get(i);
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
                nodo.setEsHoja(Boolean.FALSE);
                nodo.setHijos(new ArrayList<>());

                //Tratar rama 1
                List<ElementoDTO> particion1 = particionar(elementos, valorDivision, ejeDivision, Boolean.TRUE);
                RangosDTO rangosParticion1 = generarRangoParticion(rangos, valorDivision, ejeDivision, Boolean.TRUE);
                NodoDTO nodoParticion1 = new NodoDTO(nodo, valorDivision, ejeDivision, Boolean.TRUE, particion1, rangosParticion1);
                nodo.getHijos().add(nodoParticion1);
                decisionTree(particion1, rangosParticion1, nodoParticion1, umbral);

                //Tratar rama 2
                List<ElementoDTO> particion2 = particionar(elementos, valorDivision, ejeDivision, Boolean.FALSE);
                RangosDTO rangosParticion2 = generarRangoParticion(rangos, valorDivision, ejeDivision, Boolean.FALSE);
                NodoDTO nodoParticion2 = new NodoDTO(nodo, valorDivision, ejeDivision, Boolean.FALSE, particion2, rangosParticion2);
                nodo.getHijos().add(nodoParticion2);
                decisionTree(particion2, rangosParticion2, nodoParticion2, umbral);
            }

        }
    }

}
