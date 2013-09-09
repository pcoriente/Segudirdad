/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBeas;

import daoPermisos.DaoPer;
import dominios.Accion;
import dominios.Modulo;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

/**
 *
 * @author Comodoro
 */
@ManagedBean
@SessionScoped
public class MbTreeTable implements Serializable {

    /**
     * Creates a new instance of MbTreeTable
     */
    private TreeNode nodos;
    private TreeNode[] nodosSeleccionados;
    private ArrayList<Modulo> listaModulos = new ArrayList<>();
    private ArrayList<Accion> listaAccion = new ArrayList<>();

    public MbTreeTable() {
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
            System.err.println(ex);
        }
    }
    
    

    public TreeNode getNodos() {
        return nodos;
    }

    public void setNodos(TreeNode nodos) {
        this.nodos = nodos;
    }

    public TreeNode[] getNodosSeleccionados() {
        return nodosSeleccionados;
    }

    public void setNodosSeleccionados(TreeNode[] nodosSeleccionados) {
        this.nodosSeleccionados = nodosSeleccionados;
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
}
