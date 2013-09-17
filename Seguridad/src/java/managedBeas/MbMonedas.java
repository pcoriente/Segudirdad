/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBeas;

import daoPermisos.DaoPer;
import dominios.Moneda;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.primefaces.context.RequestContext;

/**
 *
 * @author Comodoro
 */
@ManagedBean
@SessionScoped
public class MbMonedas implements Serializable {

    Moneda monedas = new Moneda();
    ArrayList<Moneda> tablaMonedas = new ArrayList<Moneda>();

    public MbMonedas() {
        DaoPer daoPermisos = new DaoPer();
        try {
            tablaMonedas = daoPermisos.dameTablaMOnedas();
        } catch (SQLException ex) {
            Logger.getLogger(MbMonedas.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void guardarMonedas() {
        RequestContext context = RequestContext.getCurrentInstance();
        FacesMessage msg = null;
        boolean loggedIn = false;
        if (getMonedas().getMoneda().equals("") && getMonedas().getCodigoIso().equals("") && getMonedas().getPrefijoUnidad().equals("") && getMonedas().getPrefijo().equals("") && getMonedas().getSufijo().equals("") && getMonedas().getSimbolo().equals("")) {
            loggedIn = false;
            msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Error", "Llene todos los campos");
        } else if (getMonedas().getMoneda().equals("")) {
            loggedIn = false;
            msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Error", "Escriba una Moneda");
        } else if (getMonedas().getCodigoIso().equals("")) {
            loggedIn = false;
            msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Error", "Escriba un Codigo Iso");
        } else if (getMonedas().getPrefijoUnidad().equals("")) {
            loggedIn = false;
            msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Error", "Escriba un Prefijo Unidad");
        } else if (getMonedas().getPrefijo().equals("")) {
            loggedIn = false;
            msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Error", "Escriba un Prefijo");
        } else if (getMonedas().getSufijo().equals("")) {
            loggedIn = false;
            msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Error", "Escriba un Sufijo");
        } else if (getMonedas().getSimbolo().equals("")) {
            loggedIn = false;
            msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Error", "Escriba un Simbolo");
        } else {
            DaoPer daoPermisos = new DaoPer();
            try {
                daoPermisos.guardarMonedas(this.getMonedas());
                msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Exito", "Nuevas Monedas Disponibles");
                loggedIn = true;
                tablaMonedas = daoPermisos.dameTablaMOnedas();
                Moneda moneda= new Moneda();
                this.setMonedas(moneda);
            } catch (SQLException ex) {
                Logger.getLogger(MbSeguridad.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        FacesContext.getCurrentInstance().addMessage(null, msg);
        context.addCallbackParam("loggedIn", loggedIn);
    }

    public Moneda getMonedas() {
        return monedas;
    }

    public void setMonedas(Moneda monedas) {
        this.monedas = monedas;
    }

    public ArrayList<Moneda> getTablaMonedas() {
        return tablaMonedas;
    }

    public void setTablaMonedas(ArrayList<Moneda> tablaMonedas) {
        this.tablaMonedas = tablaMonedas;
    }
}
