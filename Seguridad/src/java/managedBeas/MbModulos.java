/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBeas;

import daoPermisos.DaoPer;
import dominios.Modulo;
import dominios.ModuloMenu;
import dominios.ModuloSubMenu;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import org.primefaces.context.RequestContext;

/**
 *
 * @author Comodoro
 */
@ManagedBean
@SessionScoped
public class MbModulos implements Serializable {

    private ArrayList<SelectItem> listaModulosMenu;
    private ArrayList<SelectItem> listaModulosSubMenu;
    private List<SelectItem> listaModulos = new ArrayList<SelectItem>();
    private Modulo modulo = new Modulo();
    private Modulo moduloCmb = new Modulo();
    private ModuloMenu moduloMenucmb = new ModuloMenu();
    private ModuloMenu moduloMenucmb23 = new ModuloMenu();
    private ModuloSubMenu moduloSubMenuCmb = new ModuloSubMenu();
    private List<SelectItem> moduloMenuCmb2 = new ArrayList<SelectItem>();
//    este objeto nuevo es para el seleconemenu de las altas de SubMenus
//    Este objeto nos siver para dar de altas de subMenu
    private ModuloSubMenu AltasSubMenu = new ModuloSubMenu();
    private List<SelectItem> moduloSubMenuCmb2 = new ArrayList<SelectItem>();
    private ModuloMenu moduloMenu = new ModuloMenu();
    private ModuloSubMenu moduloSubMenu = new ModuloSubMenu();
    private ModuloMenu m = new ModuloMenu();
    ModuloMenu modu = new ModuloMenu();
    private int updateSubMenu;

    public MbModulos() {
        this.inicializar();
    }

    private void inicializar() {
        this.dameModuloMenu();

    }

    public int getUpdateSubMenu() {
        return updateSubMenu;
    }

    public void setUpdateSubMenu(int updateSubMenu) {
        this.updateSubMenu = updateSubMenu;
    }

    public ModuloMenu getM() {
        return m;
    }

    public ModuloMenu getModuloMenucmb23() {
        System.out.println("--------------------------");
        System.out.println(moduloMenucmb23.getIdMenu());
        System.out.println("--------------------------");
        return moduloMenucmb23;
    }

    public void setModuloMenucmb23(ModuloMenu moduloMenucmb2) {
        this.moduloMenucmb23 = moduloMenucmb2;
    }

    public void setM(ModuloMenu m) {
        this.m = m;
    }

    public ModuloSubMenu getAltasSubMenu() {
        return AltasSubMenu;
    }

    public void setAltasSubMenu(ModuloSubMenu AltasSubMenu) {
        this.AltasSubMenu = AltasSubMenu;
    }

    public ModuloMenu getModuloMenu() {
        return moduloMenu;
    }

    public void setModuloMenu(ModuloMenu moduloMenu) {

        this.moduloMenu = moduloMenu;
    }

    public ModuloSubMenu getModuloSubMenu() {
        return moduloSubMenu;
    }

    public void setModuloSubMenu(ModuloSubMenu moduloSubMenu) {
        this.moduloSubMenu = moduloSubMenu;
    }

    public List<SelectItem> getModuloSubMenuCmb2() throws SQLException {
        return moduloSubMenuCmb2;
    }

    public void CargarModulosActualizar() {
        int idModulo = moduloCmb.getIdMenu();
        int i = this.getModuloCmb().getIdModulo();
        DaoPer daoPermisos = new DaoPer();
        Modulo m = new Modulo();
        try {
            m = daoPermisos.dameModulo(idModulo);
            getModulo().setModulo(m.getModulo());
            getModulo().setUrl(m.getUrl());
            getModuloMenucmb23().setIdMenu(m.getIdMenu());
            ArrayList<ModuloSubMenu> arrayModulosSubMenu = new ArrayList<>();
            arrayModulosSubMenu = daoPermisos.dameSubMenus(idModulo);
            ArrayList<SelectItem> valoressubmenu = new ArrayList<>();
            ModuloSubMenu moduloSubmenu = new ModuloSubMenu();
            moduloSubmenu.setIdSubMenu(0);
            moduloSubmenu.setSubMenu("Seleccione un SubMenu");
            SelectItem selectItem = new SelectItem(moduloSubmenu, moduloSubmenu.getSubMenu());
            valoressubmenu.add(selectItem);
            for (ModuloSubMenu modulo : arrayModulosSubMenu) {
                valoressubmenu.add(new SelectItem(modulo, modulo.getSubMenu()));
            }
            this.setModuloSubMenuCmb2(valoressubmenu);
//          mbModulos.getModuloSubMenuCmb().setIdSubMenu(m.getIdSubMenu());
        } catch (SQLException ex) {
            Logger.getLogger(MbSeguridad.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setModuloSubMenuCmb2(List<SelectItem> moduloSubMenuCmb2) {
        this.moduloSubMenuCmb2 = moduloSubMenuCmb2;
    }

    public List<SelectItem> getModuloMenuCmb2() {
        return moduloMenuCmb2;
    }

    public void setModuloMenuCmb2(ArrayList<SelectItem> moduloMenuCmb) {
        this.moduloMenuCmb2 = moduloMenuCmb;
    }

    public ModuloMenu getModuloMenucmb() {
        return moduloMenucmb;
    }

    public void setModuloMenucmb(ModuloMenu moduloMenucmb) {
        this.moduloMenucmb = moduloMenucmb;
    }

    public ModuloSubMenu getModuloSubMenuCmb() {
        return moduloSubMenuCmb;
    }

    public void setModuloSubMenuCmb(ModuloSubMenu moduloSubMenuCmb) {
        this.moduloSubMenuCmb = moduloSubMenuCmb;
    }

    public ArrayList<SelectItem> getListaModulosMenu() {

        return listaModulosMenu;
    }

    public void setListaModulosMenu(ArrayList<SelectItem> listaModulosMenu) {
        this.listaModulosMenu = listaModulosMenu;
    }

    public ArrayList<SelectItem> getListaModulosSubMenu() {
        return listaModulosSubMenu;
    }

    public void setListaModulosSubMenu(ArrayList<SelectItem> listaModulosSubMenu) {
        this.listaModulosSubMenu = listaModulosSubMenu;
    }

    public Modulo getModuloCmb() {
        return moduloCmb;
    }

    public void setModuloCmb(Modulo moduloCmb) {
        this.moduloCmb = moduloCmb;
    }

    public Modulo getModulo() {
        return modulo;
    }

    public void setModulo(Modulo modulo) {
        this.modulo = modulo;
    }

    public List<SelectItem> getListaModulos() {
        this.dameModulos();
        return listaModulos;
    }

    public void setListaModulos(List<SelectItem> listaModulos) {
        this.listaModulos = listaModulos;
    }

    // Metodo implementado para el evento change en el Jsf
    public void dameValoresModuloChange() {
        moduloSubMenuCmb2 = new ArrayList<>();
        try {
            int id = moduloMenucmb23.getIdMenu();
            DaoPer daoPermisos = new DaoPer();
            ArrayList<ModuloSubMenu> moduloSubMenu = new ArrayList<ModuloSubMenu>();
            moduloSubMenu = daoPermisos.dameSubMenus(id);
            if (moduloSubMenu.size() > 0) {
                ModuloSubMenu subMenu = new ModuloSubMenu();
                subMenu.setIdSubMenu(0);
                subMenu.setSubMenu("Seleccione un Sub Menu");
                SelectItem selecItem = new SelectItem(subMenu, subMenu.getSubMenu());
                moduloSubMenuCmb2.add(selecItem);
                for (ModuloSubMenu mod : moduloSubMenu) {
                    moduloSubMenuCmb2.add(new SelectItem(mod, mod.getSubMenu()));
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(MbModulos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void dameModulos() {
        DaoPer dp = new DaoPer();
        listaModulos = new ArrayList<SelectItem>();
        modulo.setModulo("Seleccione un Modulo");
        modulo.setIdModulo(0);
        SelectItem itemModulo = new SelectItem(modulo, modulo.getModulo());
        listaModulos.add(itemModulo);
        try {
            for (Modulo d : dp.dameModulos()) {
                listaModulos.add(new SelectItem(d, d.getModulo()));
            }
        } catch (SQLException ex) {
            Logger.getLogger(MbModulos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void dameValorId() {
        try {
            int id = getModuloMenucmb().getIdMenu();
            DaoPer daopermisos = new DaoPer();
            ArrayList<ModuloSubMenu> subMenus = new ArrayList<ModuloSubMenu>();
            subMenus = daopermisos.dameSubMenus(id);


            ModuloSubMenu modulosSub = new ModuloSubMenu();
            modulosSub.setIdSubMenu(0);
            modulosSub.setSubMenu("Selecciona un SubModulo");
            SelectItem selectItem = new SelectItem(modulosSub, modulosSub.getSubMenu());

            listaModulosSubMenu.add(selectItem);
            for (ModuloSubMenu m4 : subMenus) {
                SelectItem s = new SelectItem(m4, m4.getSubMenu());
                listaModulosSubMenu.add(s);
            }
        } catch (SQLException ex) {
            Logger.getLogger(MbUsuarios.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private ArrayList<SelectItem> dameModulosMenu() {
        ArrayList<SelectItem> modulosMenu = new ArrayList<SelectItem>();
        try {
            ModuloMenu dModulosMenu = new ModuloMenu();
            dModulosMenu.setIdMenu(0);
            dModulosMenu.setMenu("SELECCIONE UN MODULO");
            SelectItem se = new SelectItem(dModulosMenu, dModulosMenu.getMenu());
            modulosMenu.add(se);

            DaoPer daoPermisos = new DaoPer();
            ArrayList<ModuloMenu> m = daoPermisos.dameMOdulosMenu();
            for (ModuloMenu moduloMenu : m) {
                modulosMenu.add(new SelectItem(moduloMenu, moduloMenu.getMenu()));
            }
        } catch (SQLException ex) {
            Logger.getLogger(MbUsuarios.class.getName()).log(Level.SEVERE, null, ex);
        }
        return modulosMenu;
    }

    public void dameModuloMenu() {
        try {
            DaoPer daoPermiso = new DaoPer();
            ModuloMenu moduloMenu = new ModuloMenu();
            moduloMenu.setIdMenu(0);
            moduloMenu.setMenu("Seleccione un Menu");
            moduloMenuCmb2 = new ArrayList<SelectItem>();
            SelectItem select = new SelectItem(moduloMenu, moduloMenu.getMenu());
            moduloMenuCmb2.add(select);
            for (ModuloMenu m : daoPermiso.dameMOdulosMenu()) {
                moduloMenuCmb2.add(new SelectItem(m, m.getMenu()));
            }
        } catch (SQLException ex) {
            Logger.getLogger(MbModulos.class.getName()).log(Level.SEVERE, null, ex);
        }
//        return m2;
    }

    public void guardarValoresModulo() {
        RequestContext context = RequestContext.getCurrentInstance();
        FacesMessage msg = null;
        boolean loggedIn = false;
        DaoPer daoPermisos = new DaoPer();
        modulo.getModulo();
        modulo.getUrl();
        modulo.setIdMenu(moduloMenucmb.getIdMenu());
        modulo.setIdSubMenu(moduloSubMenuCmb.getIdSubMenu());
        try {
            if (modulo.getModulo().equals("") && modulo.getUrl().equals("") && modulo.getIdMenu() == 0 && modulo.getIdSubMenu() == 0) {
                loggedIn = false;
                msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Error", "Ingrese todas las Opciones");
            } else {
                daoPermisos.guardarModulo(modulo);
            }
        } catch (SQLException ex) {
            Logger.getLogger(MbModulos.class.getName()).log(Level.SEVERE, null, ex);
        }
        FacesContext.getCurrentInstance().addMessage(null, msg);
        context.addCallbackParam("loggedIn", loggedIn);
    }
}
