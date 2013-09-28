/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBeas;

import daoPermisos.DaoPer;
import dominios.Accion;
import dominios.Modulo;
import dominios.ModuloMenu;
import dominios.ModuloSubMenu;
import dominios.Nivel;
import dominios.UsuarioPerfil;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
//import org.apache.velocity.runtime.directive.Parse;
import org.primefaces.component.tree.Tree;
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
    private TreeNode modulo;
    private TreeNode[] nodosSeleccionados;
    private ArrayList<Modulo> listaModulos = new ArrayList<>();
    private ArrayList<Accion> listaAccion = new ArrayList<>();
    private ArrayList<ModuloMenu> listaMenus = new ArrayList<>();
    private ArrayList<ModuloSubMenu> listaSubMenus = new ArrayList<>();
    private int control;

    public MbTreeTable() {
//        cargarTreeTable(0);
    }

    public MbTreeTable(int idPerfil, String Jndi) {
        cargarTreeTable(idPerfil, Jndi);
    }

    public void cargarTreeTable(int idPerfil, String jndi) {
        ArrayList<Nivel> listaNiveles = new ArrayList<>();
        DaoPer daoPermisos = new DaoPer();
        DaoPer daoP = new DaoPer(jndi);
        ArrayList<Accion> listaAcciones = null;

        try {
            listaAcciones = daoP.dameAccion(idPerfil);
        } catch (SQLException ex) {
            Logger.getLogger(MbTreeTable.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            listaNiveles = daoPermisos.dameNiveles();
            nodos = new DefaultTreeNode("root", nodos);
            int idMenu = 0;
            int idSubmenu = 0;
            int idModulo = 0;
            TreeNode nodoMenu = null;
            TreeNode nodoSubMenu = null;
            TreeNode nodoModulo = null;
            TreeNode nodoAccion = null;
            for (Nivel n : listaNiveles) {
                if (n.getIdMenu() != idMenu) {
                    idMenu = n.getIdMenu();
                    nodoMenu = new DefaultTreeNode(n.getMenu(), nodos);
                }
                if (n.getIdSubMenu() != idSubmenu && n.getIdSubMenu() > 0) {
                    idSubmenu = n.getIdSubMenu();
                    nodoSubMenu = new DefaultTreeNode(n.getSubMenu(), nodoMenu);
                }
                if (n.getIdModulo() != idModulo && n.getIdSubMenu() > 0) {
                    idModulo = n.getIdModulo();
                    nodoModulo = new DefaultTreeNode(n.getModulo(), nodoSubMenu);
                }
                if (n.getIdSubMenu() > 0) {
//                    Aqui agregamos las acciones
                    nodoAccion = new DefaultTreeNode(n, nodoModulo);

                    for (Accion ac : listaAcciones) {
                        if (ac.getIdAccion() == n.getIdAccion()) {
                            nodoAccion.setSelected(true);
                            nodoAccion.setSelectable(true);
                            nodoModulo.setExpanded(true);
                            nodoSubMenu.setExpanded(true);
                            nodoMenu.setExpanded(true);
                        }
                    }
                }
                if (n.getIdModulo() != idModulo && n.getIdSubMenu() == 0) {
                    idModulo = n.getIdModulo();
                    nodoModulo = new DefaultTreeNode(n.getModulo(), nodoMenu);
                }
                if (n.getIdSubMenu() == 0) {
//                    Aqui Agregamos las Acciones
                    nodoAccion = new DefaultTreeNode(n, nodoModulo);

                    for (Accion ac : listaAcciones) {
                        if (ac.getIdAccion() == n.getIdAccion()) {
                            nodoAccion.setSelected(true);
                            nodoAccion.setSelectable(true);
                            nodoModulo.setExpanded(true);
                            nodoMenu.setExpanded(true);
                        }
                    }

                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(MbTreeTable.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void cargarTreeTableJulio(int idPerfil, String jndi) {
        ArrayList<Nivel> listaNiveles = new ArrayList<>();
        try {
            DaoPer daoPermisos = new DaoPer();
            DaoPer daoP = new DaoPer(jndi);
            ArrayList<Accion> listaAcciones = null;

            try {
                listaAcciones = daoP.dameAccion(idPerfil);
            } catch (SQLException ex) {
                Logger.getLogger(MbTreeTable.class.getName()).log(Level.SEVERE, null, ex);
            }
            listaNiveles = daoPermisos.dameNiveles();
            nodos = new DefaultTreeNode("root", nodos);
            TreeNode nodoMenu = null;
            int idMenu = 0;
            int n = listaNiveles.size();
            int respaldoi = 0;
            for (int i = 0; i < n; i++) {
                Nivel nivel = listaNiveles.get(i);
                if (nivel.getIdMenu() != idMenu) {
                    idMenu = nivel.getIdMenu();
                    nodoMenu = new DefaultTreeNode(nivel.getMenu(), nodos);
                }
                int idSubmenu = 0;
                i = respaldoi;
                TreeNode nodoSubMenu = null;
                while (i < n && nivel.getIdMenu() == idMenu) {
                    if (nivel.getIdSubMenu() != 0) {
                        idSubmenu = nivel.getIdSubMenu();
                        nodoSubMenu = new DefaultTreeNode(nivel.getSubMenu(), nodoMenu);
                    }
                    while (i < n && nivel.getIdMenu() == idMenu && nivel.getIdSubMenu() == idSubmenu) {
                        nivel = listaNiveles.get(i);
                        if (idSubmenu == 0) {
                            modulo = new DefaultTreeNode(nivel.getModulo(), nodoMenu);
                        } else {
                            modulo = new DefaultTreeNode(nivel.getModulo(), nodoSubMenu);
                        }
                        new DefaultTreeNode(nivel.getAccion(), modulo);
                        i++;
                    }
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(MbTreeTable.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void cargarTreeTableFallo(int idPerfil, String jndi) {
        DaoPer daoPermisos = new DaoPer();
        DaoPer daoP = new DaoPer(jndi);
        ArrayList<Accion> listaAcciones = null;

        try {
            listaAcciones = daoP.dameAccion(idPerfil);
        } catch (SQLException ex) {
            Logger.getLogger(MbTreeTable.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            listaModulos = daoPermisos.dameModulos();
            listaAccion = daoPermisos.dameAcciones();
            listaMenus = daoPermisos.dameMOdulosMenu();
            listaSubMenus = daoPermisos.dameSubMenus();
            nodos = new DefaultTreeNode("root", nodos);
//            Recorremos los MenusCorrespondientes que son 3
            for (ModuloMenu menu : listaMenus) {
                TreeNode nodoMenu = new DefaultTreeNode("Menu:" + menu, nodos);
//                 Procedemos a recorres los SubMenus
                for (ModuloSubMenu subMenu : listaSubMenus) {
                    for (Modulo m : listaModulos) {
                        if (menu.getIdMenu() == m.getIdMenu()) {
                            if (m.getIdSubMenu() == subMenu.getIdSubMenu()) {
                                TreeNode SubMenu = new DefaultTreeNode("SubMenu:" + subMenu, nodoMenu);
                                modulo = new DefaultTreeNode("Modulo:" + m, SubMenu);
                                for (int y = 0; y < listaAcciones.size(); y++) {
                                    if (listaAcciones.get(y).getIdMOdulo() == m.getIdModulo()) {
                                        modulo.setExpanded(true);
//                                        ---------
                                        nodoMenu.setExpanded(true);
                                        SubMenu.setExpanded(true);
                                        nodos.setExpanded(true);
//                                        break;
                                    }
                                }
                                for (int x = 0; x < listaAccion.size(); x++) {
                                    if (m.getIdModulo() == listaAccion.get(x).getIdMOdulo()) {
                                        TreeNode work = new DefaultTreeNode(listaAccion.get(x), modulo);
                                        for (int w = 0; w < listaAcciones.size(); w++) {
                                            if (listaAccion.get(x).getIdAccion() == listaAcciones.get(w).getIdAccion()) {
                                                work.setSelected(true);
//                                work.setSelectable(true);
                                                break;
                                            } else {
                                                work.setSelected(false);
                                            }
                                        }
                                    }
                                }
                            }
                        } else {
                            modulo = new DefaultTreeNode("Modulo:" + m, nodoMenu);
                            for (int y = 0; y < listaAcciones.size(); y++) {
                                if (listaAcciones.get(y).getIdMOdulo() == m.getIdModulo()) {
                                    modulo.setExpanded(true);
                                    break;
                                } else {
                                    modulo.setSelected(false);
                                }
                            }
                            for (int x = 0; x < listaAccion.size(); x++) {
                                if (m.getIdModulo() == listaAccion.get(x).getIdMOdulo()) {
                                    TreeNode work = new DefaultTreeNode(listaAccion.get(x), modulo);
                                    for (int w = 0; w < listaAcciones.size(); w++) {
                                        if (listaAccion.get(x).getIdAccion() == listaAcciones.get(w).getIdAccion()) {
                                            work.setSelected(true);
//                                                -----
                                            modulo.setExpanded(true);
                                            nodoMenu.setExpanded(true);
                                            nodos.setExpanded(true);
                                            break;
                                        } else {
//                                                work.setSelected(false);
////                                                -------
//                                                modulo.setExpanded(true);
//                                                nodoMenu.setExpanded(true);
//                                                nodos.setExpanded(true);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (SQLException ex) {
            System.err.println(ex);
        }
    }

    public void cargarTreeTableii(int idPerfil, String jndi) {
        DaoPer daoPermisos = new DaoPer();
        DaoPer daoP = new DaoPer(jndi);
        ArrayList<Accion> listaAcciones = null;
        try {
            listaAcciones = daoP.dameAccion(idPerfil);
        } catch (SQLException ex) {
            Logger.getLogger(MbTreeTable.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            listaModulos = daoPermisos.dameModulos();
            listaAccion = daoPermisos.dameAcciones();
            nodos = new DefaultTreeNode("root", nodos);
            for (Modulo m : listaModulos) {
                TreeNode modulo = new DefaultTreeNode(m, nodos);
                for (int y = 0; y < listaAcciones.size(); y++) {
                    if (listaAcciones.get(y).getIdMOdulo() == m.getIdModulo()) {
                        modulo.setExpanded(true);
                        break;
                    } else {
                        modulo.setSelected(false);
                    }
                }
                for (int x = 0; x < listaAccion.size(); x++) {
                    if (m.getIdModulo() == listaAccion.get(x).getIdMOdulo()) {
                        TreeNode work = new DefaultTreeNode(listaAccion.get(x), modulo);
                        for (int w = 0; w < listaAcciones.size(); w++) {
                            if (listaAccion.get(x).getIdAccion() == listaAcciones.get(w).getIdAccion()) {
                                work.setSelected(true);
//                                work.setSelectable(true);
                                break;
                            } else {
                                work.setSelected(false);
//                                work.setSelectable(false);
                            }
                        }
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

    public ArrayList<ModuloMenu> getListaMenus() {
        return listaMenus;
    }

    public void setListaMenus(ArrayList<ModuloMenu> listaMenus) {
        this.listaMenus = listaMenus;
    }

    public ArrayList<ModuloSubMenu> getListaSubMenus() {
        return listaSubMenus;
    }

    public void setListaSubMenus(ArrayList<ModuloSubMenu> listaSubMenus) {
        this.listaSubMenus = listaSubMenus;
    }
}
