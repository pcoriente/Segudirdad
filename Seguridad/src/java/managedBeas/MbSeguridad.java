/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBeas;

import daoPermisos.DaoPer;
import dominios.Accion;
import dominios.BaseDato;
import dominios.Modulo;
import dominios.ModuloSubMenu;
import dominios.Moneda;
import dominios.Nivel;
import dominios.Pais;
import dominios.UsuarioPerfil;
import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
//import net.sf.jasperreports.engine.JRException;
//import net.sf.jasperreports.engine.JRExporterParameter;
//import net.sf.jasperreports.engine.JasperExportManager;
//import net.sf.jasperreports.engine.JasperFillManager;
//import net.sf.jasperreports.engine.JasperPrint;
//import net.sf.jasperreports.engine.JasperReport;
//import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
//import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
//import net.sf.jasperreports.engine.util.JRLoader;
import org.primefaces.context.RequestContext;
import org.primefaces.event.RowEditEvent;
import org.primefaces.model.TreeNode;
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
    @ManagedProperty(value = "#{mbTreeTable}")
    private MbTreeTable mbTreTable = new MbTreeTable();
    @ManagedProperty(value = "#{mbAcciones}")
    private MbAcciones mbAcciones = new MbAcciones();
    @ManagedProperty(value = "#{mbModulos}")
    private MbModulos mbModulos = new MbModulos();
    @ManagedProperty(value = "#{mbPerfiles}")
    private MbPerfiles mbPerfiles = new MbPerfiles();
    @ManagedProperty(value = "#{mbPaises}")
    private MbPaises mbPais;
    private ModuloSubMenu sub = new ModuloSubMenu();
    private int aparecer;
    private int aparecerSubMenu;
    private Moneda mone = new Moneda();

    public MbSeguridad() {
        mbBasesDatos = new MbBasesDatos();
        mbUsuarios = new MbUsuarios();
        mbAcciones = new MbAcciones();
//     mbModulos = new MbModulos();
        mbPerfiles = new MbPerfiles();
        mbPais = new MbPaises();

    }

    public Moneda getMone() {
        return mone;
    }

    public void setMone(Moneda mone) {
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

//    public void limpiarModulos() {
//        ArrayList<SelectItem> selectItem = new ArrayList<>();
//        ModuloSubMenu m = new ModuloSubMenu();
//        m.setIdSubMenu(0);
//        m.setSubMenu("Seleccione un SubMenus");
//        selectItem.add(new SelectItem(m, m.getSubMenu()));
//        mbModulos.setModuloSubMenuCmb2(selectItem);
//    }
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
        } else if (mbPerfiles.getPerfilCmb().getIdPerfil() == 0) {
//        else if (mbPerfiles.getPerfil().getIdPerfil() == 0) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error:", "Seleccione un perfil"));
        } else {
            ArrayList<Nivel> listaNiveles = new ArrayList<>();
            for (TreeNode n : mbTreTable.getNodosSeleccionados()) {
                try {
                    Nivel nivel = (Nivel) n.getData();
                    listaNiveles.add(nivel);
                } catch (Exception e) {
                    System.err.println(e);
                }
            }
            mbPerfiles.getPerfilCmb().getIdUsuario();
            UsuarioPerfil usuaPerfil = new UsuarioPerfil();
            String jndi = mbBasesDatos.getBaseDatos().getJndi();
            DaoPer daoPermisos = new DaoPer(jndi);
            usuaPerfil.setIdPerfil(mbPerfiles.getPerfilCmb().getIdPerfil());
            daoPermisos.insertarUsuarioPerfil(usuaPerfil, listaNiveles);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Exito", "Se insertaron los datos Correctamente"));
        }
    }

    public void cancelarUsuarioPerfil() {
        mbBasesDatos = new MbBasesDatos();
        mbModulos = new MbModulos();
        mbAcciones = new MbAcciones();
        mbPerfiles = new MbPerfiles();
        mbTreTable = new MbTreeTable();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Cancelado", "Datos Cancelados"));
    }

    public void guardarModulo() throws SQLException {
        String strSubMenu = mbModulos.getModuloSubMenuCmb().getSubMenu();
        DaoPer daoPer = new DaoPer();
        RequestContext context = RequestContext.getCurrentInstance();
        FacesMessage msg = null;
        boolean loggedIn = false;
        if (mbModulos.getModulo().getModulo().equals("")) {
            loggedIn = false;
            msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Error", "Ingrese un Nombre de Modulo");
        } else {
            int identity = daoPer.guardarModulo(mbModulos.getModulo());
            loggedIn = true;
            msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Exito", "Nuevo Modulo Disponible");
            mbModulos.getModulo().setIdModulo(identity);
            mbModulos.getModulo().setModulo(mbModulos.getModulo().getModulo());
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
                mbPerfiles.getPerfilCmb().setIdPerfil(identity);
                mbPerfiles.getPerfil().setPerfil(perfil);
                loggedIn = true;
                msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Exito", "Nuevo Perfil Disponible");
//            ------------------------
                String nomBd = mbBasesDatos.getBaseDatos().getBaseDatos();
                if (identity != 0 && nomBd != null) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Exito", "Entro al change"));
                    String jndi = mbBasesDatos.getBaseDatos().getJndi();
                    mbTreTable = new MbTreeTable(identity, jndi);
                }

            }
        }
        FacesContext.getCurrentInstance().addMessage(null, msg);
        context.addCallbackParam("loggedIn", loggedIn);
    }

    public void guardarValoresModulo() {
        RequestContext context = RequestContext.getCurrentInstance();
        FacesMessage msg = null;
        boolean loggedIn = false;
        if (mbModulos.getModulo().getIdModulo() > 0) {
            int idSubMenu = 0;
            try {
                idSubMenu = mbModulos.getModuloSubMenuCmb().getIdSubMenu();

            } catch (Exception e) {
                idSubMenu = 0;
            }
            mbModulos.getModulo().setIdSubMenu(idSubMenu);
            mbModulos.getModulo().setIdMenu(mbModulos.getModuloMenucmb23().getIdMenu());
            DaoPer daoPermisos = new DaoPer();
            try {
                daoPermisos.actualizarModulos(mbModulos.getModulo());
                loggedIn = true;
                msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Exito", "Modulo Actualizado exitosamente!");
            } catch (SQLException ex) {
                Logger.getLogger(MbSeguridad.class.getName()).log(Level.SEVERE, null, ex);
            }

        } else {
            DaoPer daoPermisos = new DaoPer();
            mbModulos.getModulo().getModulo();
            mbModulos.getModulo().getUrl();
            mbModulos.getModulo().setIdMenu(mbModulos.getModuloMenucmb23().getIdMenu());
            try {
                mbModulos.getModulo().setIdSubMenu(mbModulos.getModuloSubMenuCmb().getIdSubMenu());
            } catch (Exception e) {
                mbModulos.getModulo().setIdSubMenu(0);
            }

            try {
                if (mbModulos.getModulo().getModulo().equals("") && mbModulos.getModulo().getUrl().equals("") && mbModulos.getModulo().getIdMenu() == 0) {
                    loggedIn = false;
                    msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Error", "Ingrese todas las Opciones");
                } else if (mbModulos.getModulo().equals("")) {
                    mbModulos.getModulo().setModulo("");
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
                    mbModulos = new MbModulos();
                    int idPerfil = mbPerfiles.getPerfilCmb().getIdPerfil();
                    String jndi = mbBasesDatos.getBaseDatos().getJndi();
                    if (idPerfil != 0 && jndi != null) {
                        mbTreTable = new MbTreeTable(idPerfil, jndi);
                    }
                }
            } catch (SQLException ex) {
                Logger.getLogger(MbModulos.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        FacesContext.getCurrentInstance().addMessage(null, msg);
        context.addCallbackParam("loggedIn", loggedIn);
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
            msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Exito", "Nuevas Acciones Disponibles");
            int idPerfil = mbPerfiles.getPerfilCmb().getIdPerfil();
            String jndi = mbBasesDatos.getBaseDatos().getJndi();
            if (mbBasesDatos.getBaseDatos().getIdBaseDatos() != 0 && mbPerfiles.getPerfil().getIdPerfil() != 0) {
                mbTreTable = new MbTreeTable(idPerfil, jndi);
            }
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

    public void dameModulosAcciones(int id) {
        if (mbBasesDatos.getBaseDatos().getIdBaseDatos() == 0) {
            mbTreTable = new MbTreeTable();
            mbBasesDatos = new MbBasesDatos();
        }
        String nomBd = mbBasesDatos.getBaseDatos().getBaseDatos();
        int idPerfil = 0;
        idPerfil = mbPerfiles.getPerfilCmb().getIdPerfil();
        if (idPerfil != 0 && nomBd != null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Exito", "Entro al change"));
            String jndi = mbBasesDatos.getBaseDatos().getJndi();
            mbTreTable = new MbTreeTable(idPerfil, jndi);

        }
    }

    public void dameModulosAcciones2(int id) {
        if (mbPerfiles.getPerfilCmb().getIdPerfil() == 0) {
            mbTreTable = new MbTreeTable();
            mbPerfiles = new MbPerfiles();
        }
        String nomBd = mbBasesDatos.getBaseDatos().getBaseDatos();
        int idPerfil = 0;
        idPerfil = mbPerfiles.getPerfilCmb().getIdPerfil();
        if (idPerfil != 0 && nomBd != null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Exito", "Entro al change"));
            String jndi = mbBasesDatos.getBaseDatos().getJndi();
            mbTreTable = new MbTreeTable(idPerfil, jndi);
        }
    }

    public void cargarListaSubMenus(int id) {
        DaoPer daoPermisos = new DaoPer();
        try {
            ArrayList<SelectItem> vacio = new ArrayList<>();
            mbModulos.setModuloSubMenuCmb2(vacio);
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
        mbModulos = new MbModulos();
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

    public MbTreeTable getMbTreTable() {
        return mbTreTable;
    }

    public void setMbTreTable(MbTreeTable mbTreTable) {
        this.mbTreTable = mbTreTable;
    }

    public void dameValoresTablaMonedas(RowEditEvent event) {
        try {
            DaoPer daoPermisos = new DaoPer();
            Moneda m = (Moneda) event.getObject();
            daoPermisos.ActualizarMonedas(m.getIdMoneda(), m);
        } catch (SQLException ex) {
            Logger.getLogger(MbSeguridad.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

//    public void reporteMonedas() throws JRException, IOException {
//        String ubicacion = "C:\\Reportes\\monedas.jasper";
//        JasperReport report;
//        report = (JasperReport) JRLoader.loadObjectFromFile(ubicacion); //DEPRECADO
//        JasperPrint jasperPrint = JasperFillManager.fillReport(report, null, new JRBeanCollectionDataSource(mbMonedas.getTablaMonedas()));
//        HttpServletResponse httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
//        httpServletResponse.addHeader("Content-disposition", "attachment; filename=report.pdf");
//        ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream();
//        JasperExportManager.exportReportToPdfStream(jasperPrint, servletOutputStream);
//        FacesContext.getCurrentInstance().responseComplete();
//
//    }
    public void actualizarModulos() {
        int id = mbModulos.getModulo().getIdMenu();

        DaoPer daoPermisos = new DaoPer();
        mbModulos.getModuloMenucmb23().setIdMenu(id);
        ArrayList<SelectItem> selectItem = new ArrayList<>();
        ArrayList<ModuloSubMenu> moduloSubMenu = new ArrayList<ModuloSubMenu>();
        ModuloSubMenu subMenu = new ModuloSubMenu();
        subMenu.setSubMenu("Seleccione un SubMenu");
        SelectItem st = new SelectItem(subMenu, subMenu.getSubMenu());
        selectItem.add(st);
        try {
            moduloSubMenu = daoPermisos.dameSubMenus();
            for (ModuloSubMenu s : moduloSubMenu) {
                selectItem.add(new SelectItem(s, s.getSubMenu()));
            }
        } catch (SQLException ex) {
            Logger.getLogger(MbSeguridad.class.getName()).log(Level.SEVERE, null, ex);
        }
        mbModulos.setModuloSubMenuCmb2(selectItem);
        mbModulos.getModuloSubMenuCmb().setIdSubMenu(mbModulos.getModulo().getIdSubMenu());
    }

    public void limpiarModulos() {
        mbModulos = new MbModulos();
        mbModulos.getModulo().setIdModulo(0);
    }

    public void limpiarAcciones() {
        mbAcciones = new MbAcciones();
        mbModulos.getModuloCmb().setIdModulo(0);
    }

    public void dameValoresTablaPaises(RowEditEvent event) {
        Pais pais = (Pais) event.getObject();
        DaoPer daoPermisos = new DaoPer();
        RequestContext context = RequestContext.getCurrentInstance();
        FacesMessage msg = null;
        boolean loggedIn = false;

        String codigoPais = pais.getCodigoPais().trim();
        int longitud = codigoPais.length();
        if (longitud > 3) {
            loggedIn = false;
            msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Denegado", "El Codigo Pais no debe de ser mayor de 3!");
            mbPais = new MbPaises();
        } else {
            try {
                daoPermisos.actualizarPais(pais);
                loggedIn = true;
                msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Actualizado", "Actualizacion Exitosa...");
            } catch (Exception e) {
                System.err.println(e);
            }
        }

        FacesContext.getCurrentInstance().addMessage(null, msg);
        context.addCallbackParam("loggedIn", loggedIn);
    }

    public void limpiarAplicacion() {
        MbSeguridad mbSeguridad = new MbSeguridad();
    }

    public MbPaises getMbPais() {
        return mbPais;
    }

    public void setMbPais(MbPaises mbPais) {
        this.mbPais = mbPais;
    }

    public void dameValores() {
    }
}
