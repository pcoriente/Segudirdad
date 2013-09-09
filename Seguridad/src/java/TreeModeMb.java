/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import daoPermisos.DaoPer;
import dominios.Accion;
import dominios.Modulo;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import org.primefaces.event.NodeSelectEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

/**
 *
 * @author Comodoro
 */
@ManagedBean
@SessionScoped
public class TreeModeMb implements Serializable {

    private TreeNode nodos;
    private TreeNode[] nodosSeleccionados;
    private ArrayList<Modulo> listaModulos = new ArrayList<>();
    private ArrayList<Accion> listaAccion = new ArrayList<>();

    public TreeModeMb() {
        DaoPer daoPermisos = new DaoPer();
        try {
            listaModulos = daoPermisos.dameModulos();
            listaAccion = daoPermisos.dameAcciones();
            nodos = new DefaultTreeNode("root", nodos);
            for (Modulo m : listaModulos) {
                TreeNode modulo = new DefaultTreeNode(m, nodos);
                for (int x = 0; x < listaAccion.size(); x++) {
                    if (m.getIdModulo() == listaAccion.get(x).getIdMOdulo()) {
                        TreeNode work = new DefaultTreeNode(listaAccion.get(x), modulo);
                    }
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(TreeModeMb.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public TreeNode[] getNodosSeleccionados() {
        return nodosSeleccionados;
    }

    public void setNodosSeleccionados(TreeNode[] nodosSeleccionados) {
        this.nodosSeleccionados = nodosSeleccionados;
    }

    public TreeNode getNodos() {
        return nodos;
    }

    public void setNodos(TreeNode nodos) {
        this.nodos = nodos;
    }

    public ArrayList<Modulo> getListaModulos() {
        return listaModulos;
    }

    public void setListaModulos(ArrayList<Modulo> listaModulos) {
        this.listaModulos = listaModulos;
    }

    public ArrayList<Accion> getListaAccion() {
        return listaAccion;
    }

    public void setListaAccion(ArrayList<Accion> listaAccion) {
        this.listaAccion = listaAccion;
    }

    public void dameValoresSeleccionados() {
        ArrayList<Accion> accion = new ArrayList<>();
        for (TreeNode n : nodosSeleccionados) {
            try {
                Modulo m = (Modulo) n.getData();
                m.getIdModulo();
            } catch (Exception e) {
                Accion ac = (Accion) n.getData();
                accion.add(ac);
                ac.getIdAccion();
            }
        }
    }

    public void onNodeSelect(NodeSelectEvent event) {
        String valor = event.getTreeNode().toString();
    }
}
