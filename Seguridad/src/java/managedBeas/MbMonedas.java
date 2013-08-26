/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBeas;

import daoPermisos.DaoPer;
import dominios.Monedas;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author Comodoro
 */
@ManagedBean
@SessionScoped
public class MbMonedas implements Serializable {

    Monedas monedas = new Monedas();
    ArrayList<Monedas> tablaMonedas = new ArrayList<Monedas>();

    public MbMonedas() {
    }

    public Monedas getMonedas() {
        return monedas;
    }

    public void setMonedas(Monedas monedas) {
        this.monedas = monedas;
    }

    public ArrayList<Monedas> getTablaMonedas() {
        DaoPer daoPermisos = new DaoPer();
        try {
            tablaMonedas = daoPermisos.dameTablaMOnedas();
        } catch (SQLException ex) {
            Logger.getLogger(MbMonedas.class.getName()).log(Level.SEVERE, null, ex);
        }
        return tablaMonedas;
    }

    public void setTablaMonedas(ArrayList<Monedas> tablaMonedas) {
        this.tablaMonedas = tablaMonedas;
    }

  
}
