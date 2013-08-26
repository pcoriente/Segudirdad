/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBeas;

import daoPermisos.DaoPer;
import dominios.Accion;
import dominios.BaseDato;
import dominios.Modulo;
import dominios.ModuloMenu;
import dominios.ModuloSubMenu;
import dominios.Monedas;
import dominios.UsuarioPerfil;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import org.primefaces.context.RequestContext;
import org.primefaces.event.RowEditEvent;
import utilerias.Utilerias;

/**
 *
 * @author Comodoro
 */
@ManagedBean
@SessionScoped
//@ApplicationScoped
public class MbSeguridad implements Serializable {

    @ManagedProperty(value = "#{mbBasesDatos}")
    private MbBasesDatos mbBasesDatos = new MbBasesDatos();
    @ManagedProperty(value = "#{mbMonedas}")
    private MbMonedas mbMonedas = new MbMonedas();
    @ManagedProperty(value = "#{mbUsuarios}")
    private MbUsuarios mbUsuarios = new MbUsuarios();
    @ManagedProperty(value = "#{mbAcciones}")
    private MbAcciones mbAcciones = new MbAcciones();
    @ManagedProperty(value = "#{mbModulos}")
    private MbModulos mbModulos = new MbModulos();
    @ManagedProperty(value = "#{mbPerfiles}")
    private MbPerfiles mbPerfiles = new MbPerfiles();
    private ModuloSubMenu sub = new ModuloSubMenu();
    private int aparecer;
    private int aparecerSubMenu;
    private Monedas mone = new Monedas();

    public Monedas getMone() {
        return mone;
    }

    public void setMone(Monedas mone) {
        this.mone = mone;
    }

    public int getAparecerSubMenu() {
        return aparecerSubMenu;
    }

    public void setAparecerSubMenu(int aparecerSubMenu) {
        this.aparecerSubMenu = aparecerSubMenu;
    }

    public int getAparecer() {
        return aparecer;
    }

    public void setAparecer(int aparecer) {
        this.aparecer = aparecer;
    }

    public void control() {
        this.setAparecer(1);
        this.setAparecerSubMenu(0);
    }

    public void desaparecer() {
        this.setAparecer(0);
        this.setAparecerSubMenu(0);
    }

    public void limpiarModulos() {
        ArrayList<SelectItem> selectItem = new ArrayList<>();
        ModuloSubMenu m = new ModuloSubMenu();
        m.setIdSubMenu(0);
        m.setSubMenu("Seleccione un SubMenus");
        selectItem.add(new SelectItem(m, m.getSubMenu()));
        mbModulos.setModuloSubMenuCmb2(selectItem);
    }

    public void controlSubMenu() {
        if (mbModulos.getModuloMenucmb23().getIdMenu() > 0) {
            this.setAparecerSubMenu(1);
            this.setAparecer(0);
        } else {
            FacesMessage msg = null;
            msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Error", "Seleccione un Menu");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }

    }

    public void cancelarDRendereds() {
        this.setAparecerSubMenu(0);
        this.setAparecer(0);
    }

    public ModuloSubMenu getSub() {
        return sub;
    }

    public void setSub(ModuloSubMenu sub) {
        this.sub = sub;
    }

    public MbSeguridad() {
        mbBasesDatos = new MbBasesDatos();
        mbUsuarios = new MbUsuarios();
        mbAcciones = new MbAcciones();
//     mbModulos = new MbModulos();
        mbPerfiles = new MbPerfiles();

    }

    public void actualizarUsuarioPerfil() {
        RequestContext context = RequestContext.getCurrentInstance();
        FacesMessage msg = null;
        boolean loggedIn = false;
        try {
            DaoPer daoPermisos = new DaoPer();
            int idPerfil = mbUsuarios.getP2().getIdPerfil();
            mbUsuarios.getUsuarioCmb().getIdUsuario();
            if (idPerfil == 0) {
                loggedIn = false;
                msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Error", "Seleccione un Perfil");
            } else {
                loggedIn = true;
                daoPermisos.ActualizarUsuario(mbUsuarios);
//                mbUsuarios.getUsuarioCmb().setIdUsuario(0);
//                mbUsuarios.getP2().setIdPerfil(0);
                limpiarModificacionesPerfilUsuario();
                msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Exito", "El perfil del usuario se ha modificado");
            }
        } catch (SQLException ex) {
            Logger.getLogger(MbSeguridad.class.getName()).log(Level.SEVERE, null, ex);
        }
        FacesContext.getCurrentInstance().addMessage(null, msg);
        context.addCallbackParam("loggedIn", loggedIn);
    }

    public void guardarValores() throws SQLException {
        if (mbBasesDatos.getBaseDatos().getIdBaseDatos() == 0 && mbPerfiles.getPerfil().getIdPerfil() == 0 && mbModulos.getModulo().getIdModulo() == 0) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error:", "Seleccione todas las Opciones"));
        } else if (mbBasesDatos.getBaseDatos().getIdBaseDatos() == 0) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error:", "Seleccione una Base de Datos"));
        } else if (mbPerfiles.getPerfil().getIdPerfil() == 0) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error:", "Seleccione un perfil"));
        } else if (mbModulos.getModuloCmb().getIdModulo() == 0) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error:", "Seleccione un Modulo"));
        } else {
            mbPerfiles.getPerfilCmb().getIdUsuario();
            ArrayList<Accion> acciones = new ArrayList<Accion>();
            acciones = (ArrayList<Accion>) mbAcciones.getPickAcciones().getTarget();
            UsuarioPerfil usuaPerfil = new UsuarioPerfil();
            String jndi = mbBasesDatos.getBaseDatos().getJndi();
            DaoPer daoPermisos = new DaoPer(jndi);
            usuaPerfil.setIdPerfil(mbPerfiles.getPerfilCmb().getIdPerfil());
            usuaPerfil.setIdModulo(mbModulos.getModuloCmb().getIdModulo());
            if (usuaPerfil.getIdModulo() != 0 && usuaPerfil.getIdPerfil() != 0) {
                daoPermisos.insertarUsuarioPerfil(usuaPerfil, acciones);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Exito", "Se insertaron los datos Correctamente"));
            }
        }
    }

    public void cancelarUsuarioPerfil() {
        mbBasesDatos = new MbBasesDatos();
        mbModulos = new MbModulos();
        mbAcciones = new MbAcciones();
        mbPerfiles = new MbPerfiles();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Cancelado", "Datos Cancelados"));
    }

    public void guardarModulo() throws SQLException {
        String strSubMenu = mbModulos.getModuloSubMenuCmb().getSubMenu();
        DaoPer daoPer = new DaoPer();
        RequestContext context = RequestContext.getCurrentInstance();
        FacesMessage msg = null;
        boolean loggedIn = false;
//        if (mbModulos.getModulo().getIdModulo() != 0) {
//            mbModulos.getModulo().getModulo();
//            mbModulos.getModulo().setIdModulo(mbModulos.getModuloCmb().getIdModulo());
//            daoPer.ActualizarModulos(mbModulos.getModulo());
//            loggedIn = true;
//            msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Exito", "El Modulo fue Actualizado Correctamente");
//        } else {
        if (mbModulos.getModulo().getModulo().equals("")) {
            loggedIn = false;
            msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Error", "Ingrese un Nombre de Modulo");
        } else {
            int identity = daoPer.guardarModulo(mbModulos.getModulo());
            loggedIn = true;
            msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Exito", "Nuevo Modulo Disponible");
            mbModulos.getModulo().setIdModulo(identity);
            mbModulos.getModulo().setModulo(mbModulos.getModulo().getModulo());
//            }
        }
        FacesContext.getCurrentInstance().addMessage(null, msg);
        context.addCallbackParam("loggedIn", loggedIn);
    }

    public void guardarModuloMenu() {
        RequestContext context = RequestContext.getCurrentInstance();
        FacesMessage msg = null;
        boolean loggedIn = false;
        String moduloMenu = mbModulos.getModuloMenu().getMenu();
        mbModulos.getM().getMenu();
        if (moduloMenu.equals("")) {
            loggedIn = false;
            msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Denegado", "Ingrese un menu");
        } else {
            DaoPer daoPermisos = new DaoPer();
            try {
                daoPermisos.guardarModuloMenu(mbModulos.getModuloMenu());
                mbModulos.getModuloMenu().setMenu("");
                this.setAparecer(0);
            } catch (SQLException ex) {
                Logger.getLogger(MbSeguridad.class.getName()).log(Level.SEVERE, null, ex);
            }
            loggedIn = true;
            msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Exito", "Nuevo Menu Disponible");
        }
        FacesContext.getCurrentInstance().addMessage(null, msg);
        context.addCallbackParam("loggedIn", loggedIn);
    }

    public void guardarModuloSubMenu() {
        RequestContext context = RequestContext.getCurrentInstance();
        FacesMessage msg = null;
        boolean loggedIn = false;
        int x = mbModulos.getModuloMenucmb23().getIdMenu();
        if (sub.getSubMenu().equals("")) {
            loggedIn = false;
            msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Error", "Escriba un SubMenu");
        } else {
            try {
                ModuloSubMenu moduloSubMenu = new ModuloSubMenu();
                DaoPer daoPer = new DaoPer();
                moduloSubMenu.setSubMenu(sub.getSubMenu());
                moduloSubMenu.setIdMenu(x);
                daoPer.insertarSubMenu(moduloSubMenu);
                sub.setSubMenu("");
                loggedIn = true;
                msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Exito", "Nuevos SubModulos Disponibles");
                this.setAparecerSubMenu(0);
                this.getMbModulos().setUpdateSubMenu(1);
                ArrayList<SelectItem> selec = new ArrayList<>();
                ArrayList<ModuloSubMenu> subMenu = new ArrayList<>();
                subMenu = daoPer.dameSubMenus(x);
                for (ModuloSubMenu subMen : subMenu) {
                    selec.add(new SelectItem(subMen, subMen.getSubMenu()));
                }
                mbModulos.setModuloSubMenuCmb2(selec);
            } catch (SQLException ex) {
                Logger.getLogger(MbSeguridad.class.getName()).log(Level.SEVERE, null, ex);
                msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Exito", ex.toString());
            }
        }
        FacesContext.getCurrentInstance().addMessage(null, msg);
        context.addCallbackParam("loggedIn", loggedIn);
    }

    public void guardarPerfil() throws SQLException {
        DaoPer daoPer = new DaoPer();
        RequestContext context = RequestContext.getCurrentInstance();
        FacesMessage msg = null;
        boolean loggedIn = false;
        if (mbPerfiles.getPerfilCmb().getIdPerfil() != 0) {
            mbPerfiles.getPerfil().getPerfil();
            mbPerfiles.getPerfil().setIdPerfil(mbPerfiles.getPerfilCmb().getIdPerfil());
            daoPer.ActualizarPerfiles(mbPerfiles.getPerfil());
            loggedIn = true;
            msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Exito", "Perfil Actualizado");
        } else {
            if (mbPerfiles.getPerfil().getPerfil().equals("")) {
                loggedIn = false;
                msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Error", "Ingrese un Nombre de Perfil");
            } else {
                mbUsuarios.getUsuario().getIdUsuario();
                String perfil = mbPerfiles.getPerfil().getPerfil();
                int identity = daoPer.insertarPerfil(mbPerfiles.getPerfil());



//                dameModulosAcciones(identity);



                mbPerfiles.getPerfilCmb().setIdPerfil(identity);
                mbPerfiles.getPerfil().setPerfil(perfil);
                loggedIn = true;
                msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Exito", "Nuevo Perfil Disponible");
            }
        }
        FacesContext.getCurrentInstance().addMessage(null, msg);
        context.addCallbackParam("loggedIn", loggedIn);
    }

    public void guardarValoresModulo() {
        DaoPer daoPermisos = new DaoPer();
        mbModulos.getModulo().getModulo();
        mbModulos.getModulo().getUrl();
        mbModulos.getModulo().setIdMenu(mbModulos.getModuloMenucmb23().getIdMenu());
        try {
            mbModulos.getModulo().setIdSubMenu(mbModulos.getModuloSubMenuCmb().getIdSubMenu());
        } catch (Exception e) {
            mbModulos.getModulo().setIdSubMenu(0);
        }
        if (mbModulos.getModuloCmb().getIdModulo() > 0) {
            RequestContext context = RequestContext.getCurrentInstance();
            FacesMessage msg = null;
            boolean loggedIn = false;
            int idModulo = mbModulos.getModuloCmb().getIdModulo();
            try {
                daoPermisos.actualizarModulos(mbModulos.getModulo(), idModulo);
                loggedIn = true;
                msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Exito", "Modulo Actualizado");
            } catch (SQLException ex) {
                loggedIn = false;
                Logger.getLogger(MbSeguridad.class.getName()).log(Level.SEVERE, null, ex);
            }
            FacesContext.getCurrentInstance().addMessage(null, msg);
            context.addCallbackParam("loggedIn", loggedIn);
        } else {
            RequestContext context = RequestContext.getCurrentInstance();
            FacesMessage msg = null;
            boolean loggedIn = false;

            try {
                if (mbModulos.getModulo().getModulo().equals("") && mbModulos.getModulo().getUrl().equals("") && mbModulos.getModulo().getIdMenu() == 0) {
                    loggedIn = false;
                    msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Error", "Ingrese todas las Opciones");
                } else if (mbModulos.getModulo().equals("")) {
                    mbModulos.getModulo().setModulo("");
//                    mbSeguridad.mbModulos.modulo.modulo

                    loggedIn = false;
                    msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Error", "Ingrese un Modulo");
                } else if (mbModulos.getModulo().getUrl().equals("")) {
                    loggedIn = false;
                    msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Error", "Ingrese una Url");
                } else if (mbModulos.getModulo().getIdMenu() == 0) {
                    loggedIn = false;
                    msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Error", "Seleccione un Menu Modulo");
                } else {
                    int id = daoPermisos.guardarModulo(mbModulos.getModulo());
                    mbModulos.getModuloCmb().setIdModulo(id);
                    loggedIn = true;
                    msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Exito", "Nuevos modulos Disponibles");
                }
            } catch (SQLException ex) {
                Logger.getLogger(MbModulos.class.getName()).log(Level.SEVERE, null, ex);
            }
            FacesContext.getCurrentInstance().addMessage(null, msg);
            context.addCallbackParam("loggedIn", loggedIn);
        }
    }

    public void guardarAcciones() throws SQLException {
        RequestContext context = RequestContext.getCurrentInstance();
        FacesMessage msg = null;
        boolean loggedIn = false;
        if (mbAcciones.getAcion().getAccion().equals("") && mbAcciones.getAcion().getIdBoton().equals("")) {
            loggedIn = false;
            msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Error", "Ingrese una Accion y un IdButom");
            dameModulosAcciones(0);
        } else if (mbAcciones.getAcion().getIdBoton().equals("")) {
            loggedIn = false;
            msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Error", "Ingrese el idBotom");
            dameModulosAcciones(0);
        } else if (mbAcciones.getAcion().getAccion().equals("")) {
            loggedIn = false;
            msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Error", "Ingrese una Acción");
            dameModulosAcciones(0);
        } else {
            loggedIn = true;
            DaoPer daoPermisos = new DaoPer();
            mbAcciones.getAcion().setIdMOdulo(mbModulos.getModuloCmb().getIdModulo());
            daoPermisos.insertarAcciones(mbAcciones.getAcion());
            dameModulosAcciones(0);
//            mbAcciones = new MbAcciones();
            msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Exito", "Nuevas Acciones Disponibles");
//            RequestContext.getCurrentInstance().execute("dlg.hide();");
        }
        FacesContext.getCurrentInstance().addMessage(null, msg);
        context.addCallbackParam("loggedIn", loggedIn);
    }

    public void dameBdsPickList() throws SQLException {
        RequestContext context = RequestContext.getCurrentInstance();
        FacesMessage msg = null;
        boolean loggedIn;
        ArrayList<BaseDato> bd = new ArrayList<BaseDato>();
        if (bd.size() > 0) {
        } else {
            bd = (ArrayList<BaseDato>) mbBasesDatos.getPickBd().getTarget();
            mbBasesDatos.getPickBd().getSource();
            DaoPer p = new DaoPer();
            p.insertarBd(bd);
            if (mbBasesDatos.getPickBd().getTarget().size() == 0) {
                loggedIn = true;
                msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Removido", "Las bases de Datos fueron removidas");
            } else {
                loggedIn = true;
                msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Agregadas", "Nuevas Bases de Datos disponibles");
            }
            FacesContext.getCurrentInstance().addMessage(null, msg);
            context.addCallbackParam("Bd´s", loggedIn);
        }
    }

    public void dameModulosAcciones(int id) throws SQLException {
//        int idModulo2 = mbModulos.getModuloCmb().getIdModulo();
//        CargarModulosActualizar(idModulo2);
//        cargarListaSubMenus();
        mbAcciones = new MbAcciones();
        if (mbPerfiles.getPerfilCmb().getIdPerfil() != 0) {
            mbPerfiles.getPerfil().setIdPerfil(mbPerfiles.getPerfilCmb().getIdPerfil());
            mbPerfiles.getPerfil().setPerfil(mbPerfiles.getPerfilCmb().getPerfil());
        }
        if (mbModulos.getModulo().getIdModulo() != 0) {
            mbModulos.getModulo().setModulo(mbModulos.getModuloCmb().getModulo());
        }
        if (mbBasesDatos.getBaseDatos().getIdBaseDatos() == 0) {
            mbBasesDatos = new MbBasesDatos();
        }
        if (mbPerfiles.getPerfilCmb().getIdPerfil() == 0) {
            mbPerfiles = new MbPerfiles();
        }
        if (mbModulos.getModuloCmb().getIdModulo() == 0) {
//            mbModulos = new MbModulos();
        } //        codigo colocado Ultimamente
        else {
        }
        String nomBd = mbBasesDatos.getBaseDatos().getBaseDatos();
        int idPerfil = 0;
        if (id > 0) {
            idPerfil = id;
        } else {
            idPerfil = mbPerfiles.getPerfilCmb().getIdPerfil();
        }
        int idModulo = mbModulos.getModuloCmb().getIdModulo();

        if (idPerfil != 0 && idModulo != 0 && nomBd != null) {
            String perfil = mbPerfiles.getPerfilCmb().getPerfil();
            mbPerfiles.getPerfil().setPerfil(perfil);
            DaoPer daoPermisos = new DaoPer();
            ArrayList<Accion> acciones = new ArrayList<Accion>();
            acciones = daoPermisos.dameValores(nomBd, idModulo, idPerfil);

            for (Accion ac : acciones) {
                if (ac.getIdPerfil() == 0) {
                    mbAcciones.accionesOrigen.add(ac);
                } else {
                    mbAcciones.accionesDestino.add(ac);
                }
            }
        }
    }

    public void cargarListaSubMenus(int id) {
        if (id == 0) {
            System.err.println("*************");
            System.err.println("****vacio****");
            System.err.println("*************");
        }
        DaoPer daoPermisos = new DaoPer();
//        Modulo m = new Modulo();
        try {
            ArrayList<SelectItem> vacio = new ArrayList<>();
            mbModulos.setModuloSubMenuCmb2(vacio);
//          m = daoPermisos.dameModulo(idModulo);
            ArrayList<ModuloSubMenu> arrayModulosSubMenu = new ArrayList<>();
            arrayModulosSubMenu = daoPermisos.dameSubMenus(id);
            ArrayList<SelectItem> valoressubmenu = new ArrayList<>();
            ModuloSubMenu moduloSubmenu = new ModuloSubMenu();
            moduloSubmenu.setIdSubMenu(0);
            moduloSubmenu.setSubMenu("Seleccione un SubMenu");
            SelectItem selectItem = new SelectItem(moduloSubmenu, moduloSubmenu.getSubMenu());
            valoressubmenu.add(selectItem);
            for (ModuloSubMenu modulo : arrayModulosSubMenu) {
                valoressubmenu.add(new SelectItem(modulo, modulo.getSubMenu()));
            }
            mbModulos.setModuloSubMenuCmb2(valoressubmenu);
        } catch (SQLException ex) {
            Logger.getLogger(MbSeguridad.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void CargarModulosActualizar(int idModulo) {
        DaoPer daoPermisos = new DaoPer();
        Modulo m = new Modulo();
        try {
            m = daoPermisos.dameModulo(idModulo);
            this.getMbModulos().setModulo(m);
            mbModulos.getModulo().setModulo(m.getModulo());
            mbModulos.getModulo().setUrl(m.getUrl());
//            mbModulos.getModuloMenucmb23().setIdMenu(m.getIdMenu());
//            mbModulos.getModuloSubMenuCmb().setIdSubMenu(m.getIdSubMenu());
        } catch (SQLException ex) {
            Logger.getLogger(MbSeguridad.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void insertarDatos() throws SQLException, Exception {
        DaoPer daoUsuario = new DaoPer();
        RequestContext context = RequestContext.getCurrentInstance();
        FacesMessage msg = null;
        boolean loggedIn = false;
        boolean validarEmail = false;
        Utilerias utilerias = new Utilerias();
        if (mbUsuarios.getUsuario().getUsuario().equals("") && mbUsuarios.getUsuario().getLogin().equals("") && mbUsuarios.getUsuario().getPassword().equals("") && mbUsuarios.getUsuario().getEmail().equals("")) {
            loggedIn = false;
            msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Error", "Llene todos los Campos");
        }
        if (mbUsuarios.getUsuario().getUsuario().equals("")) {
            loggedIn = false;
            msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Error", "Escriba un Usuario");
        } else if (mbUsuarios.getUsuario().getLogin().equals("")) {
            loggedIn = false;
            msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Error", "Escriba un Login");
        } else if (mbUsuarios.getUsuario().getPassword().equals("")) {
            loggedIn = false;
            msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Error", "Escriba un Password");
        } else if (mbUsuarios.getUsuario().getEmail().equals("")) {
            loggedIn = false;
            msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Error", "Escriba un Email");
        } else {
            validarEmail = utilerias.validarEmail(mbUsuarios.getUsuario().getEmail());
            if (validarEmail == true) {
                String fecha = utilerias.dameFecha();
                daoUsuario.insertarUsuario(mbUsuarios.getUsuario(), mbBasesDatos.getBaseDatos().getIdBaseDatos(), mbPerfiles.getPerfilCmb().getIdPerfil());
//                bd.getIdBaseDatos();
//                perfil2.getIdPerfiles();
                mbUsuarios = new MbUsuarios();
                loggedIn = true;
                msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Exito", "Nuevo Usuario Disponible");
            } else {
                loggedIn = false;
                msg = new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error", "Ingrese un Email Valido");
            }
        }
        FacesContext.getCurrentInstance().addMessage(null, msg);
        context.addCallbackParam("loggedIn", loggedIn);
    }

    public void limpiarPerfiles() {
        if (mbPerfiles.getPerfilCmb().getIdPerfil() == 0) {
            mbPerfiles = new MbPerfiles();
        } else {
            String perfil = mbPerfiles.getPerfilCmb().getPerfil();
            mbPerfiles.getPerfil().setPerfil(perfil);
        }
    }

    public void limpiarModificacionesPerfilUsuario() {
        mbUsuarios.getUsuarioCmb().setIdUsuario(0);
        mbUsuarios.getP2().setIdPerfil(0);
    }

    public void limpiarUsuarios() {
        String perfil = mbPerfiles.getPerfilCmb().getPerfil();
        mbPerfiles.getPerfil().setPerfil(null);
        mbUsuarios = new MbUsuarios();
    }

    public void cargarDatos() {
        mbModulos.getModuloMenucmb23().setIdMenu(mbModulos.getModuloCmb().getIdMenu());
        mbModulos.getModulo().setModulo(mbModulos.getModuloCmb().getModulo());
        mbModulos.getModulo().setUrl(mbModulos.getModuloCmb().getUrl());
        mbModulos.getModuloSubMenuCmb().setIdSubMenu(mbModulos.getModuloCmb().getIdSubMenu());
        DaoPer daoPermisos = new DaoPer();
        ArrayList<ModuloSubMenu> moduloSubmenu = new ArrayList<>();
        if (mbModulos.getModuloCmb().getIdMenu() > 0) {
            try {
                ArrayList<SelectItem> arraySelecItem = new ArrayList<>();
                ModuloSubMenu moduloSubMenu = new ModuloSubMenu();
                moduloSubMenu.setIdSubMenu(0);
                moduloSubMenu.setSubMenu("Seleccione un SubMenu");
                SelectItem select = new SelectItem(moduloSubMenu, moduloSubMenu.getSubMenu());
                arraySelecItem.add(select);
                moduloSubmenu = daoPermisos.dameSubMenus(mbModulos.getModuloCmb().getIdMenu());
                for (ModuloSubMenu m : moduloSubmenu) {
                    arraySelecItem.add(new SelectItem(m, m.getSubMenu()));
                }
                mbModulos.setModuloSubMenuCmb2(arraySelecItem);
            } catch (SQLException ex) {
                Logger.getLogger(MbSeguridad.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public String home() {
        String pagina = "index.xhtml";
        return pagina;
    }

    public MbBasesDatos getMbBasesDatos() {
        return mbBasesDatos;
    }

    public void limpiarPerfil() {
        ArrayList<SelectItem> selectItems = new ArrayList<>();
        mbUsuarios.setListaPerfiles(selectItems);
    }

    public void setMbBasesDatos(MbBasesDatos mbBasesDatos) {
        this.mbBasesDatos = mbBasesDatos;
    }

    public MbUsuarios getMbUsuarios() {
        return mbUsuarios;
    }

    public void setMbUsuarios(MbUsuarios mbUsuarios) {
        this.mbUsuarios = mbUsuarios;
    }

    public MbAcciones getMbAcciones() {
        return mbAcciones;
    }

    public void setMbAcciones(MbAcciones mbAcciones) {
        this.mbAcciones = mbAcciones;
    }

    public MbModulos getMbModulos() {
        return mbModulos;
    }

    public void setMbModulos(MbModulos mbModulos) {
        this.mbModulos = mbModulos;
    }

    public MbPerfiles getMbPerfiles() {
        return mbPerfiles;
    }

    public void setMbPerfiles(MbPerfiles mbPerfiles) {
        this.mbPerfiles = mbPerfiles;
    }

    public MbMonedas getMbMonedas() {
        return mbMonedas;
    }

    public void setMbMonedas(MbMonedas mbMonedas) {
        this.mbMonedas = mbMonedas;
    }

    public void dameValoresTablaMonedas(RowEditEvent event) {
        Monedas m = (Monedas) event.getObject();
        mbMonedas.getMonedas().getSimbolo();
    }
}
