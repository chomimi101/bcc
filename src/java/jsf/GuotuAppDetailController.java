package jsf;

import entity.AppAbstract;
import entity.BureauClerk;
import entity.Business;
import entity.GuotuAppDetail;
import entity.TownOfficer;
import entity.VillageAgent;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import jsf.util.JsfUtil;
import jsf.util.JsfUtil.PersistAction;
import session.AppAbstractFacade;
import session.GuotuAppDetailFacade;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
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
import javax.faces.event.ValueChangeEvent;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.servlet.http.Part;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

@ManagedBean(name = "guotuAppDetailController")
@SessionScoped
public class GuotuAppDetailController implements Serializable {
    @EJB
    private GuotuAppDetailFacade guotuAppDetailFacade;
    @EJB
    private AppAbstractFacade appAbstractFacade;

    @PersistenceContext(unitName = "BCC_BLOB_PU")
    private EntityManager em;

    private Part shenQing_File;
    private Part shenPi_File;
    private Part songYue_File;
    private Part shenCha_File;
    private Part qiTa_File;

    private List<GuotuAppDetail> items = null;
    private GuotuAppDetail selected;
    private String newOptionSignature;

    public String returnToWorkflowPage() {
        FacesContext context = FacesContext.getCurrentInstance();
        Object user = context.getExternalContext().getSessionMap().get("loginUser");
        // 为了单个功能的开发，这里做了变动
        if (user instanceof VillageAgent) {
            return "/appAbstract/V_List";
        } else if (user instanceof TownOfficer) {
            return "/appAbstract/T_List";
        } else {
            return "/appAbstract/B_List";
        }
    }

    public String forVilliageAgentToEdit() {
        String state = selected.getAppAbstractId().getState();
        if (state.equals("起草阶段")||state.equals("退回村里的申请")) {
            return "false";
        } else {
            return "true";
        }
    }

    public String forTownOfficerToEdit() {
        String state = selected.getAppAbstractId().getState();
        if (state.equals("等待乡里审批")) {
            return "false";
        } else {
            return "true";
        }
    }

    public String forBureauClerkToEdit() {
        String state = selected.getAppAbstractId().getState();
        if (state.equals("等待局里审批")) {
            return "false";
        } else {
            return "true";
        }
    }

    public String submitAppToTownOffice() {
        VillageAgent user = (VillageAgent) getLoginUser();
        appAbstractFacade.submitToTownOffice(selected.getAppAbstractId().getAppAbstractId(), user);
        selected.setVillageOpinion(selected.getVillageOpinion() + newOptionSignature);
        guotuAppDetailFacade.edit(selected);

        return returnToWorkflowPage();
    }

    public String disapproveByVillage() {
        VillageAgent user = (VillageAgent) getLoginUser();
        appAbstractFacade.disapprovedAppReturnToVillage(user.getName(), selected.getAppAbstractId());
        selected.setVillageOpinion(selected.getVillageOpinion() + newOptionSignature);
        guotuAppDetailFacade.edit(selected); // merge update into original receord

        return returnToWorkflowPage();
    }

    public String townReturnAppToVilliage() {
        FacesContext context = FacesContext.getCurrentInstance();
        TownOfficer user = (TownOfficer) context.getExternalContext().getSessionMap().get("loginUser");
        appAbstractFacade.returnAppToVillage(user.getName(), selected.getAppAbstractId());
        selected.setTownOpinion(selected.getTownOpinion() + newOptionSignature);
        guotuAppDetailFacade.edit(selected); // merge update into original receord

        return returnToWorkflowPage();
    }

    public String submitAppToBureau() {
        TownOfficer user = (TownOfficer) getLoginUser();
        appAbstractFacade.submitAppToBureau(user.getName(), selected.getAppAbstractId());
        selected.setTownOpinion(selected.getTownOpinion() + newOptionSignature);
        guotuAppDetailFacade.edit(selected); // merge town office option

        return returnToWorkflowPage();
    }

    public String townDisapproveReturnToVillage() {
        TownOfficer user = (TownOfficer) getLoginUser();
        appAbstractFacade.disapprovedAppReturnToVillage(user.getName(), selected.getAppAbstractId());
        selected.setTownOpinion(selected.getTownOpinion() + newOptionSignature);
        guotuAppDetailFacade.edit(selected); // merge update into original receord

        return returnToWorkflowPage();
    }

    public String bureauReturnAppToVilliage() {
        FacesContext context = FacesContext.getCurrentInstance();
        BureauClerk user = (BureauClerk) context.getExternalContext().getSessionMap().get("loginUser");
        appAbstractFacade.returnAppToVillage(user.getName(), selected.getAppAbstractId());
        selected.setBureauOpinion(selected.getBureauOpinion() + newOptionSignature);
        guotuAppDetailFacade.edit(selected); // merge update into original receord

        return returnToWorkflowPage();
    }

    public String bureauDisapproveReturnToVillage() {
        BureauClerk user = (BureauClerk) getLoginUser();
        appAbstractFacade.disapprovedAppReturnToVillage(user.getName(), selected.getAppAbstractId());
        selected.setBureauOpinion(selected.getBureauOpinion() + newOptionSignature);
        guotuAppDetailFacade.edit(selected); // merge update into original receord

        return returnToWorkflowPage();
    }

    public String bureauApproved() {
        BureauClerk user = (BureauClerk) getLoginUser();
        appAbstractFacade.bureauApproved(user.getName(), selected.getAppAbstractId());
        selected.setBureauOpinion(selected.getBureauOpinion() + newOptionSignature);
        guotuAppDetailFacade.edit(selected); // merge bureau office option

        return returnToWorkflowPage();
    }

    public String userSignature() {
        String userName;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日");
        String dateTime = dateFormat.format(new Date());

        FacesContext context = FacesContext.getCurrentInstance();
        Object user = context.getExternalContext().getSessionMap().get("loginUser");
        if (user instanceof VillageAgent) {
            userName = ((VillageAgent) user).getName();
        } else if (user instanceof TownOfficer) {
            userName = ((TownOfficer) user).getName();
        } else {
            userName = ((BureauClerk) user).getName();
        }
        // leave some space between each options
        return "[审批人：" + userName + ", " + dateTime + "]" + "   "; 
    }

    public void optionChanged(ValueChangeEvent e) {
        //postfix timestamp (and also author name) on new option text
        newOptionSignature = userSignature();
    }

    public Object getLoginUser() {
        FacesContext context = FacesContext.getCurrentInstance();
        return context.getExternalContext().getSessionMap().get("loginUser");
    }

    public GuotuAppDetail getSelected() {
        return selected;
    }

    public void setSelected(GuotuAppDetail selected) {
        newOptionSignature = "";
        this.selected = selected;
    }

    public Part getShenQing_File() {
        return shenQing_File;
    }

    public void setShenQing_File(Part shenQing_File) {
        this.shenQing_File = shenQing_File;
    }

    public Part getShenPi_File() {
        return shenPi_File;
    }

    public void setShenPi_File(Part shenPi_File) {
        this.shenPi_File = shenPi_File;
    }

    public Part getSongYue_File() {
        return songYue_File;
    }

    public void setSongYue_File(Part songYue_File) {
        this.songYue_File = songYue_File;
    }

    public Part getShenCha_File() {
        return shenCha_File;
    }

    public void setShenCha_File(Part shenCha_File) {
        this.shenCha_File = shenCha_File;
    }

    public Part getQiTa_File() {
        return qiTa_File;
    }

    public void setQiTa_File(Part qiTa_File) {
        this.qiTa_File = qiTa_File;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private GuotuAppDetailFacade getFacade() {
        return guotuAppDetailFacade;
    }

    public GuotuAppDetail prepareCreate() {
        selected = new GuotuAppDetail();
        initializeEmbeddableKey();
        return selected;
    }

    public void assignSelected() {
        // 把 village_workflow 中放入 FacesContext 中 GuotoAppDetails 对象赋给 selected 供 disp_guotu 使用
        FacesContext context = FacesContext.getCurrentInstance();
        selected = (GuotuAppDetail) context.getExternalContext().getSessionMap().get("selectedAppDetails");
    }

    public byte[] saveImageFileToEntityBlob(Part filePart) throws IOException {
        String originalFileName = filePart.getSubmittedFileName();

        if (originalFileName != null) {
            FileInputStream in_stream = (FileInputStream) filePart.getInputStream();
            ByteArrayOutputStream out_stream = new ByteArrayOutputStream();
            int ch;
            while ((ch = in_stream.read()) != -1) {
                out_stream.write(ch);
            }

            return out_stream.toByteArray();
        } else {
            return null;
        }
    }

    public String uploadFilesSaveApplication() throws IOException {
        // save scanned files into the BLOB attributes of GuoTuDetailApp object
        selected.setShenQingFile(saveImageFileToEntityBlob(shenQing_File));
        selected.setShenPiFile(saveImageFileToEntityBlob(shenPi_File));
        selected.setSongYueFile(saveImageFileToEntityBlob(songYue_File));
        selected.setShenChaFile(saveImageFileToEntityBlob(shenCha_File));
        selected.setQiTaFile(saveImageFileToEntityBlob(qiTa_File));

        // 创建业务申请摘要对象并持久化
        FacesContext context = FacesContext.getCurrentInstance();
        VillageAgent agent = (VillageAgent) context.getExternalContext().getSessionMap().get("loginUser");
        Query query = em.createNamedQuery("Business.findByName");
        query.setParameter("name", "农民个人占集体土地建房(原地改建、扩建、新建)");
        Business business = (Business) query.getSingleResult();

        String villageName = agent.getVillageOfficeId().getName();

        AppAbstract appAbstract = new AppAbstract(1, "起草阶段", villageName);
        appAbstract.setBusinessId(business);
        appAbstract.setVillageAgentId(agent);
        appAbstract.setLastModifyDate(new Date());
        appAbstract.setLastModifierName(agent.getName());
        appAbstract.setEditAppUrl(business.getEditAppUrl());
        appAbstract.setDispAppUrl(business.getDispAppUrl());
        appAbstractFacade.create((AppAbstract) appAbstract);

        selected.setAppAbstractId(appAbstract);
        selected.setBusinessName(business.getName());

        // AppAbstract entity is persisted by the cascading persist operation of GuoTuDetail entity
        persist(PersistAction.CREATE, "农民个人占集体土地建房业务申请创建好了");
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
        // 把 village_workflow 中放入 FacesContext 中 GuotoAppDetails 对象赋给 selected 供 disp_guotu 使用
        context.getExternalContext().getSessionMap().put("selectedAppDetails", selected);
        
        return "/appAbstract/V_List";
    }

    // methods used for display application contents
    public StreamedContent getImageFileFromEntityBlob(byte[] blob) {
        ByteArrayInputStream stream = new ByteArrayInputStream(blob);
        return new DefaultStreamedContent(stream, "image/jpeg");
    }

    public StreamedContent getShenQingImageFile() {
        return getImageFileFromEntityBlob(selected.getShenQingFile());
    }

    public StreamedContent getShenPiImageFile() throws FileNotFoundException {
        return getImageFileFromEntityBlob(selected.getShenPiFile());

    }

    public StreamedContent getSongYueImageFile() {
        return getImageFileFromEntityBlob(selected.getSongYueFile());
    }

    public StreamedContent getShenChaImageFile() {
        return getImageFileFromEntityBlob(selected.getShenChaFile());
    }

    public StreamedContent getQiTaImageFile() {
        return getImageFileFromEntityBlob(selected.getQiTaFile());
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("GuotuAppDetailUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("GuotuAppDetailDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<GuotuAppDetail> getItems() {
        items = getFacade().findAll();

        return items;
    }

    private void persist(PersistAction persistAction, String successMessage) {
        if (selected != null) {
            setEmbeddableKeys();
            try {
                if (persistAction != PersistAction.DELETE) {
                    getFacade().edit(selected);
                } else {
                    getFacade().remove(selected);
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

    public GuotuAppDetail getGuotuAppDetail(java.lang.Integer id) {
        return getFacade().find(id);
    }

    public List<GuotuAppDetail> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<GuotuAppDetail> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = GuotuAppDetail.class)
    public static class GuotuAppDetailControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            GuotuAppDetailController controller = (GuotuAppDetailController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "guotuAppDetailController");
            return controller.getGuotuAppDetail(getKey(value));
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
            if (object instanceof GuotuAppDetail) {
                GuotuAppDetail o = (GuotuAppDetail) object;
                return getStringKey(o.getGuotuAppId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), GuotuAppDetail.class.getName()});
                return null;
            }
        }
    }
}
