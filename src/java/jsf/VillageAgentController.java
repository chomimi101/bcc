package jsf;
import entity.BureauClerk;
import entity.TownOfficer;
import entity.VillageAgent;
import jsf.util.JsfUtil;
import jsf.util.JsfUtil.PersistAction;
import session.VillageAgentFacade;
import java.io.Serializable;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.servlet.http.HttpSession;

@ManagedBean(name="villageAgentController")
@SessionScoped
public class VillageAgentController implements Serializable {
    @EJB
    private VillageAgentFacade agentEjbFacade;
    private List<VillageAgent> items = null;
    private VillageAgent selected;
    
    public VillageAgentController() {
    }

    public VillageAgent getSelected() {
        return selected;
    }

    public void setSelected(VillageAgent selected) {
        this.selected = selected;
    }

    public VillageAgentFacade getAgentEjbFacade() {
        return agentEjbFacade;
    }

    public void setAgentEjbFacade(VillageAgentFacade agentEjbFacade) {
        this.agentEjbFacade = agentEjbFacade;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    public VillageAgent prepareCreate() {
        selected = new VillageAgent();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("VillageAgentCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("VillageAgentUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("VillageAgentDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<VillageAgent> getItems(){
        if (items == null) {
            items = agentEjbFacade.findAll();
        }
        return items;
    }

    private void persist(PersistAction persistAction, String successMessage) {
        if (selected != null) {
            setEmbeddableKeys();
            try {
                if (persistAction != PersistAction.DELETE) {
                    agentEjbFacade.edit(selected);
                } else {
                    agentEjbFacade.remove(selected);
                }
                JsfUtil.addSuccessMessage(successMessage);
            } catch (EJBException ex) {
                String msg = "";
                Throwable cause = ex.getCause();
                if (cause != null) {
                    msg = cause.getLocalizedMessage();
                }
                if (msg.length() > 0) {
                    JsfUtil.addErrorMessage(msg);
                } else {
                    JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
                }
            } catch (Exception ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            }
        }
    }

    public VillageAgent getVillageAgent(java.lang.Integer id) {
        return agentEjbFacade.find(id);
    }

    public List<VillageAgent> getItemsAvailableSelectMany() {
        return agentEjbFacade.findAll();
    }

    public List<VillageAgent> getItemsAvailableSelectOne() {
        return agentEjbFacade.findAll();
    }

    @FacesConverter(forClass = VillageAgent.class)
    public static class VillageAgentControllerConverter implements Converter {
        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            VillageAgentController controller = (VillageAgentController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "villageAgentController");
            return controller.getVillageAgent(getKey(value));
        }

        java.lang.Integer getKey(String value) {
            java.lang.Integer key;
            key = Integer.valueOf(value);
            return key;
        }

        String getStringKey(java.lang.Integer value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof VillageAgent) {
                VillageAgent o = (VillageAgent) object;
                return getStringKey(o.getVillageAgentId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), VillageAgent.class.getName()});
                return null;
            }
        }
    }}
