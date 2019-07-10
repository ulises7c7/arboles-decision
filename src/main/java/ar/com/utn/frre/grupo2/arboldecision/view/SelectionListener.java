/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.com.utn.frre.grupo2.arboldecision.view;

import ar.com.utn.frre.grupo2.arboldecision.dto.NodoDTO;
import com.mxgraph.model.mxCell;
import com.mxgraph.util.mxEventObject;
import com.mxgraph.util.mxEventSource;
import com.mxgraph.view.mxGraphSelectionModel;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;
import javafx.application.Platform;
import javafx.scene.control.Label;

/**
 *
 * @author ulises
 */
public class SelectionListener implements mxEventSource.mxIEventListener {

    private Map<String, Label> labels = new HashMap<>();

    public SelectionListener() {
    }

    public SelectionListener(Map<String, Label> labels) {
        this.labels = labels;
    }

    @Override
    public void invoke(Object sender, mxEventObject evt) {

        mxGraphSelectionModel sm = (mxGraphSelectionModel) sender;
        mxCell cell = (mxCell) sm.getCell();
        if (cell != null && cell.isVertex()) {

            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    //javaFX operations should go here

                    NodoDTO nodo = (NodoDTO) cell.getValue();

                    labels.get("tipoNodoLbl").setText(nodo.getEsHoja() ? "Hoja" : "Decision");
                    labels.get("tipoHojaLbl").setText(nodo.getEsHoja()
                            ? nodo.getEsHojaPura() ? "Pura" : "Impura"
                            : "-");
                    labels.get("claseHojaLbl").setText(nodo.getEsHoja()
                            ? nodo.getClaseHojaString()
                            : "-");

                    labels.get("entropiaLbl").setText(nodo.getEntropia().setScale(4, RoundingMode.HALF_UP).toString());

                }
            });

        }

    }

}
