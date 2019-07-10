/*
 * (C) Copyright 2013-2018, by Barak Naveh and Contributors.
 *
 * JGraphT : a free Java graph-theory library
 *
 * See the CONTRIBUTORS.md file distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0, or the
 * GNU Lesser General Public License v2.1 or later
 * which is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1-standalone.html.
 *
 * SPDX-License-Identifier: EPL-2.0 OR LGPL-2.1-or-later
 */
package ar.com.utn.frre.grupo2.arboldecision.view;

import ar.com.utn.frre.grupo2.arboldecision.dto.NodoDTO;
import com.mxgraph.layout.mxCompactTreeLayout;
import com.mxgraph.swing.mxGraphComponent;
import java.awt.Dimension;
import java.text.NumberFormat;
import java.util.LinkedList;
import javafx.embed.swing.SwingNode;
import javax.swing.JPanel;
import org.jgrapht.Graph;
import org.jgrapht.ListenableGraph;
import org.jgrapht.ext.JGraphXAdapter;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultListenableGraph;

public class ArbolPane extends SwingNode {

    private static final long serialVersionUID = 2202072534703043194L;

    private static final Dimension DEFAULT_SIZE = new Dimension(530, 320);
    private final JPanel jpanel = new JPanel();

    private final ListenableGraph<NodoDTO, DefaultEdge> g
            = new DefaultListenableGraph<>(new DefaultDirectedGraph<>(DefaultEdge.class));

    private final JGraphXAdapter<NodoDTO, DefaultEdge> jgxAdapter = new JGraphXAdapter<>(g);

    public ArbolPane() {
        super();
        jpanel.setPreferredSize(DEFAULT_SIZE);
        mxGraphComponent graphComponent = new mxGraphComponent(jgxAdapter);

        graphComponent.setConnectable(false);
        graphComponent.getGraph().setAllowDanglingEdges(false);
        jpanel.resize(DEFAULT_SIZE);
        setContent(graphComponent);

    }

    public void clear() {
        clearGraph(g);
    }

    public void drawArbol(NodoDTO nodoRaiz) {

        clearGraph(g);
        drawNodo(nodoRaiz, null, g);

        mxCompactTreeLayout layout = new mxCompactTreeLayout(jgxAdapter, false);
        layout.setEdgeRouting(false);
        layout.execute(jgxAdapter.getDefaultParent());
    }

    private void drawNodo(NodoDTO nodo, NodoDTO nodoPadre, ListenableGraph g) {

        g.addVertex(nodo);
        if (nodoPadre != null) {
            g.addEdge(nodoPadre, nodo, new AristaEtiquetada(nodo));
        }

        if (nodo.getHijos() != null) {
            for (NodoDTO nodoHijo : nodo.getHijos()) {
                drawNodo(nodoHijo, nodo, g);
            }
        }

    }

    private class AristaEtiquetada extends DefaultEdge {

        private final String etiqueta;

        public AristaEtiquetada(NodoDTO nodo) {
            this.etiqueta = armarTextoRama(nodo);
        }

        private String armarTextoRama(NodoDTO nodo) {
            if (nodo.getEjeParticion() != null) {
                String eje = nodo.getEjeParticion() == 1 ? "x" : "y";
                String signo = nodo.getEsRamaMenor() ? "<" : ">";
                String valor = NumberFormat.getNumberInstance().format(nodo.getValorParticion());
                return String.format("%s %s %s", eje, signo, valor);
            }
            return "";
        }

        @Override
        public String toString() {
            return etiqueta;
        }
    }

    public JGraphXAdapter<NodoDTO, DefaultEdge> getJgxAdapter() {
        return jgxAdapter;
    }

    public static <V, E> void removeAllEdges(Graph<V, E> graph) {
        LinkedList<E> copy = new LinkedList<>();
        for (E e : graph.edgeSet()) {
            copy.add(e);
        }
        graph.removeAllEdges(copy);
    }

    public static <V, E> void clearGraph(Graph<V, E> graph) {
        removeAllEdges(graph);
        removeAllVertices(graph);
    }

    public static <V, E> void removeAllVertices(Graph<V, E> graph) {
        LinkedList<V> copy = new LinkedList<>();
        for (V v : graph.vertexSet()) {
            copy.add(v);
        }
        graph.removeAllVertices(copy);
    }

}
