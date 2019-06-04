/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.com.utn.frre.grupo2.arboldecision;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Singleton que maneja el mapeo de ID de clases y sus nombres
 *
 * @author ulises
 */
public class ClaseHandler {

    private static final ClaseHandler INSTANCIA = new ClaseHandler();
    private final List<String> listClases = new ArrayList<>();

    private ClaseHandler() {
    }

    public static ClaseHandler getInstancia() {
        return INSTANCIA;
    }

    public void definirClases(Collection<String> clases) {
        listClases.clear();
        listClases.addAll(clases);
        Collections.sort(listClases);
    }

    public Integer getClaseNumero(String claseString) {
        int index = listClases.indexOf(claseString);
        return index == -1 ? null : index + 1;
    }

    public String getClaseNombre(Integer claseInteger) {
        if (claseInteger != null && claseInteger > 0 && claseInteger <= listClases.size()) {
            return listClases.get(claseInteger - 1);
        }
        return null;

    }
}
