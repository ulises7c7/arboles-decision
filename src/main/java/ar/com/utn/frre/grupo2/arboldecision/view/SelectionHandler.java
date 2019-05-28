/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.com.utn.frre.grupo2.arboldecision.view;

/**
 *
 * @author ulises
 */
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

public class SelectionHandler {

    private final Clipboard clipboard;

    private final EventHandler<MouseEvent> mousePressedEventHandler;
    private Map<String, Label> labels = new HashMap<>();

    public SelectionHandler(final Parent root) {
        this.clipboard = new Clipboard();
        this.mousePressedEventHandler = (MouseEvent event) -> {
            SelectionHandler.this.doOnMousePressed(root, event);
            event.consume();
        };
    }

    public EventHandler<MouseEvent> getMousePressedEventHandler() {
        return mousePressedEventHandler;
    }

    private void doOnMousePressed(Parent root, MouseEvent event) {
        Node target = (Node) event.getTarget();
        if (target.equals(root)) {
            clipboard.unselectAll();
        }
        if (root.getChildrenUnmodifiable().contains(target) && target instanceof NodoView) {
            NodoView selectableTarget = (NodoView) target;
            if (!clipboard.getSelectedItems().contains(selectableTarget)) {
                clipboard.unselectAll();
            }
            clipboard.select(selectableTarget, true);
        }
    }

    public Map<String, Label> getLabels() {
        return labels;
    }

    public void setLabels(Map<String, Label> labels) {
        this.labels = labels;
    }

    private class Clipboard {

        private final ObservableList<NodoView> selectedItems = FXCollections.observableArrayList();

        public ObservableList<NodoView> getSelectedItems() {
            return selectedItems;
        }

        public boolean select(NodoView nodoView, boolean selected) {
            if (nodoView.requestSelection(selected)) {
                if (selected) {
                    selectedItems.add(nodoView);
                } else {
                    selectedItems.remove(nodoView);
                }
                nodoView.notifySelection(selected);

                labels.get("tipoNodoLbl").setText(nodoView.getNodoDTO().getEsHoja() ? "Hoja" : "Decision");
                labels.get("tipoHojaLbl").setText(nodoView.getNodoDTO().getEsHoja()
                        ? nodoView.getNodoDTO().getEsHojaPura() ? "Pura" : "Impura"
                        : "-");
                labels.get("claseHojaLbl").setText(nodoView.getNodoDTO().getEsHoja()
                        ? nodoView.getNodoDTO().getClaseHoja().toString()
                        : "-");

                return true;
            } else {
                return false;
            }
        }

        public void unselectAll() {
            List<NodoView> unselectList = new ArrayList<>();
            unselectList.addAll(selectedItems);

            unselectList.forEach((sN) -> {
                select(sN, false);
            });

            Collection<Label> values = labels.values();

            values.forEach((label) -> {
                label.setText("");
            });
        }
    }
}
