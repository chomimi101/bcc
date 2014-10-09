package jsf;
import entity.AppAbstract;
import entity.BureauClerk;
import entity.GuotuAppDetail;
import entity.TownOfficer;
import entity.VillageAgent;
import jsf.util.JsfUtil;
import jsf.util.JsfUtil.PersistAction;
import session.AppAbstractFacade;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import session.GuotuAppDetailFacade;

@ManagedBean(name = "appAbstractController")
@ApplicationScoped
public class AppAbstractController implements Serializable {
    @PersistenceContext(unitName = "BCC_BLOB_PU")
    private EntityManager   em;
    @EJB
    AppAbstractFacade       appAbstractFacade;
    private VillageAgent    agent;
    private TownOfficer     officer;
    private BureauClerk     clerk;
    @EJB
    private GuotuAppDetailFacade guotuAppDetailFacade;
    private AppAbstract selected = null;
    private List<AppAbstract>  appAbstractList;    // 行政申请列表
    private String appAbstractCategory;
    
    private boolean villiageOfficeEditable;
    private boolean townOfficeEditable;
    private boolean bureauOfficeEditable;
    private boolean detetableApplication;
    
    private String appProcessingState = null;
    
    public AppAbstractController() {
        FacesContext context = FacesContext.getCurrentInstance();
        Object u = context.getExternalContext().getSessionMap().get("loginUser");
        if(u instanceof VillageAgent)
            agent = (VillageAgent) u;
        else if(u instanceof TownOfficer)
            officer = (TownOfficer) u;
        else
            clerk  = (BureauClerk) u;

        appAbstractList = new ArrayList<>(); // 无参构建函数创建一个空白的申请列表
    }

    public String returnToWorkflowPage() {
        FacesContext context = FacesContext.getCurrentInstance();
        Object user = context.getExternalContext().getSessionMap().get("loginUser");
        if (user instanceof VillageAgent) {
            return "/appAbstract/V_List";
        } else if (user instanceof TownOfficer) {
            return "/appAbstract/V_List";
        } else {
            return "/appAbstract/V_List";
        }
    }

    // 查询登录行政村‘已提交申请’状态的行政审批数据
    public void querySubmittedAppAbstract() {
        // 对于‘已提交申请’的申请数据隐藏相应的提交申请、取消申请按钮
        villiageOfficeEditable = false;
        // this query is only used by villiage agent, not appliable to town and bureau officer
//        townOfficeEditable = true;
//        bureauOfficeEditable = false;
        
        detetableApplication = false;
        appAbstractCategory = "审批中的-行政审批申请";
        appAbstractList = 
                em.createQuery( "SELECT a FROM AppAbstract a where a.state LIKE '等待%' AND a.villageAgentId.villageAgentId =:agentId" )
                    .setParameter("agentId", agent.getVillageAgentId()).getResultList();
        selected = null;
    }

    public void queryDisapprovedAppAbstract() {
        // 村、乡或县发现申请不合法，无法继续办理。申请过程终止。
        // 对于‘未获批准’的申请数据禁止提交申请按钮、显示取消申请按钮
        villiageOfficeEditable = false;
        townOfficeEditable = false;
        bureauOfficeEditable = false;
        detetableApplication = true; // 未获批准的申请是否需要保留？
        appAbstractCategory = "未批准的-行政审批申请";
        appAbstractList = 
            em.createQuery( "SELECT a FROM AppAbstract a where a.state = '申请未获批准' AND a.villageAgentId.villageAgentId =:agentId" )
                .setParameter("agentId", agent.getVillageAgentId()).getResultList();
        selected = null;
    }
    
    public void queryDraftAppAbstract() {
        // 对于‘尚未提交’的申请数据显示提交申请按钮和取消申请按钮
        villiageOfficeEditable = true;
        townOfficeEditable = false;
        bureauOfficeEditable = false;
        detetableApplication = true;
        appAbstractCategory = "等待村委会批准的-行政审批申请";
        
        // 查询处于‘待提交申请’的行政审批数据。包括从未提交过的和提交过但因信息不全等退回来的申请。
        appAbstractList = 
            em.createQuery( "SELECT a FROM AppAbstract a where (a.state = '起草阶段' OR a.state = '退回村里的申请') AND a.villageAgentId.villageAgentId =:agentId")
                .setParameter("agentId", agent.getVillageAgentId()).getResultList();
        
        selected = null;
    }
    
    // application status: 起草阶段, 等待乡里审批, 等待局里审批, 已获局里批准, 申请未获批准，退回村里的申请   
    public void querySubmittedToOfficeAbstract() { // 查询登录用户管辖范围的被乡政府和县政府‘已批准申请’的行政审批数据
        // 查询登录村代办员经手的、状态为‘已批准申请’的行政审批数据
        appAbstractList = 
            em.createQuery("SELECT a FROM AppAbstract a where a.state = '等待乡里审批' AND a.villageAgentId.villageOfficeId.townOfficeId.townOfficeId =:officerId" )
                    .setParameter("officerId", officer.getTownOfficeId().getTownOfficeId()).getResultList();
        
        villiageOfficeEditable = false;
        townOfficeEditable = true;
        bureauOfficeEditable = false; // 对于‘已批准申请’的申请数据隐藏相应的修改按钮
        detetableApplication = false;
        appAbstractCategory = "等待乡政府批准的-行政审批申请";

        selected = null;
    }

    public void querySubmittedToBureauAbstract() {
        appAbstractList = 
            em.createQuery("SELECT a FROM AppAbstract a where a.state = '等待局里审批' AND a.villageAgentId.villageOfficeId.townOfficeId.countyBureauId.countyBureauId = :officerId" )
                    .setParameter("officerId", clerk.getCountyBureauId().getCountyBureauId()).getResultList();
        
        villiageOfficeEditable = false;
        townOfficeEditable = false;
        bureauOfficeEditable = true;
        detetableApplication = false;
        appAbstractCategory = "等待县政府审批的-行政审批申请";
        
        selected = null;
    }

    public void queryApprovedAppByVillageOffice() {
        appAbstractList =
            em.createQuery("SELECT a FROM AppAbstract a where a.state = '已获局里批准' AND a.villageAgentId.villageOfficeId.villageOfficeId =:villageOfficeId" )
                    .setParameter("villageOfficeId", agent.getVillageOfficeId().getVillageOfficeId()).getResultList();
        
        villiageOfficeEditable = false;
        townOfficeEditable = false;
        bureauOfficeEditable = false; // 对于‘已获乡里批准’的申请数据隐藏乡政府审批操作按钮
        detetableApplication = false;
        appAbstractCategory = "县局已批准的-行政审批申请";
    }

    public void queryApprovedAppByTownOffice() {
        appAbstractList =
            em.createQuery("SELECT a FROM AppAbstract a where a.state = '已获局里批准' AND a.villageAgentId.villageOfficeId.villageOfficeId = :villageOfficeId" )
                    .setParameter("villageOfficeId", officer.getTownOfficeId().getTownOfficeId()).getResultList();
        
        villiageOfficeEditable = false;
        townOfficeEditable = false;
        bureauOfficeEditable = false; // 对于‘已获乡里批准’的申请数据隐藏乡政府审批操作按钮
        detetableApplication = false;
        appAbstractCategory = "县局已批准的-行政审批申请";
}

    public void queryApprovedAppByBureauOffice() {
        appAbstractList =
            em.createQuery("SELECT a FROM AppAbstract a where a.state = '已获局里批准' AND a.businessId.countyBureauId.countyBureauId =:countyBureauId" )
                    .setParameter("countyBureauId", clerk.getCountyBureauId().getCountyBureauId()).getResultList();
        
        villiageOfficeEditable = false;
        townOfficeEditable = false;
        bureauOfficeEditable = false; // 对于‘已获局里批准’的申请数据隐藏局政府审批操作按钮
        detetableApplication = false;
        appAbstractCategory = "县局已批准的-行政审批申请";
    }

    public String submitToBureauOffice(int app_id) {
        // 需要 transaction 的支持，把修改数据库的代码移到了 session bean 中去使用 container managed JTA
        appAbstractFacade.submitToBureauOffice(app_id, officer);
        return "/appAbstract/T_List";
    }
    
    public boolean isDetetableApplication() {
        return detetableApplication;
    }

    public void setDetetableApplication(boolean detetableApplication) {
        this.detetableApplication = detetableApplication;
    }
    
    public String viewAppDetail() {
        FacesContext context = FacesContext.getCurrentInstance();

        switch (selected.getBusinessId().getBusinessId()) { // the first getBusinessId actually return AppAbstract object
            case 1: // 农民个人占集体土地建房(原地改建、扩建、新建)
                // eclipse JPA entity wizard generated an one-to-many association instead,shuold be one-to-one
                Iterator<GuotuAppDetail> i = selected.getGuotuAppDetailCollection().iterator();
                GuotuAppDetail selectedAppDetail = i.next();
                context.getExternalContext().getSessionMap().put("selectedAppDetails", selectedAppDetail);
                return "/guotuAppDetail/" + selected.getDispAppUrl() + ".xhtml";
            case 2: // 林木采伐许可证
                return null;
            case 3: // 木材运输证
                return null;
            case 4: // 城乡居民养老保险参保登记
                return null;
            case 5: // 劳动就业咨询
                return null;
            case 6: // 城乡特困群众大病救助申请办理
                return null;
            case 7: // 农村低保办理
                return null;
            case 8: // 出具无婚姻登记记录查询证明
                return null;
            case 9: // 城市低保申请办理
                return null;
            case 10: // 发放百岁及以上老人长寿补贴申请办理
                return null;
            case 11: // 自然灾害生活救助申请办理
                return null;
            case 12: // 出具婚姻登记记录查询证明
                return null;
            case 13: // 孤儿救助办理
                return null;
            case 14: // 农村五保申请
                return null;
            case 15: // 优抚对象优待金发放办理
                return null;
            case 16: // 生育证办理
                return null;
            case 17: // 县内党组织关系转接办理
                return null;
            case 18: // 县外党组织关系转接办理
                return null;
        }
        return null;
    }

    public String editAppDetail() {
        FacesContext context = FacesContext.getCurrentInstance();
        String prefix;

        Object user = context.getExternalContext().getSessionMap().get("loginUser");
        if (user instanceof VillageAgent) {
            prefix = "V_";
        } else if (user instanceof TownOfficer) {
            prefix = "T_";
        } else {
            prefix = "B_";
        }

        switch (selected.getBusinessId().getBusinessId()) { // the first getBusinessId actually return AppAbstract object
            case 1: // 农民个人占集体土地建房(原地改建、扩建、新建)
                // eclipse JPA entity wizard generated an one-to-many association instead,shuold be one-to-one
                Iterator<GuotuAppDetail> i = selected.getGuotuAppDetailCollection().iterator();
                GuotuAppDetail selectedAppDetail = i.next();
                context.getExternalContext().getSessionMap().put("selectedAppDetails", selectedAppDetail);
                return "/guotuAppDetail/" + prefix + selected.getEditAppUrl() + ".xhtml";
                
            case 2: // 林木采伐许可证
                return null;
            case 3: // 木材运输证
                return null;
            case 4: // 城乡居民养老保险参保登记
                return null;
            case 5: // 劳动就业咨询
                return null;
            case 6: // 城乡特困群众大病救助申请办理
                return null;
            case 7: // 农村低保办理
                return null;
            case 8: // 出具无婚姻登记记录查询证明
                return null;
            case 9: // 城市低保申请办理
                return null;
            case 10: // 发放百岁及以上老人长寿补贴申请办理
                return null;
            case 11: // 自然灾害生活救助申请办理
                return null;
            case 12: // 出具婚姻登记记录查询证明
                return null;
            case 13: // 孤儿救助办理
                return null;
            case 14: // 农村五保申请
                return null;
            case 15: // 优抚对象优待金发放办理
                return null;
            case 16: // 生育证办理
                return null;
            case 17: // 县内党组织关系转接办理
                return null;
            case 18: // 县外党组织关系转接办理
                return null;
        }
        return null;
    }

    public void deleteApplication() {
        switch (selected.getBusinessId().getBusinessId()) {
            case 1: // 农民个人占集体土地建房(原地改建、扩建、新建)
                // delete GuotuAppDetail
                Iterator<GuotuAppDetail> i = selected.getGuotuAppDetailCollection().iterator();
                guotuAppDetailFacade.remove(i.next());
                // delte AppAbstract
                appAbstractFacade.remove(selected);
                break;
            case 2: // 林木采伐许可证
                break;
            case 3: // 木材运输证
                break;
            case 4: // 城乡居民养老保险参保登记
                break;
            case 5: // 劳动就业咨询
                break;
            case 6: // 城乡特困群众大病救助申请办理
                break;
            case 7: // 农村低保办理
                break;
            case 8: // 出具无婚姻登记记录查询证明
                break;
            case 9: // 城市低保申请办理
                break;
            case 10: // 发放百岁及以上老人长寿补贴申请办理
                break;
            case 11: // 自然灾害生活救助申请办理
                break;
            case 12: // 出具婚姻登记记录查询证明
                break;
            case 13: // 孤儿救助办理
                break;
            case 14: // 农村五保申请
                break;
            case 15: // 优抚对象优待金发放办理
                break;
            case 16: // 生育证办理
                break;
            case 17: // 县内党组织关系转接办理
                break;
            case 18: // 县外党组织关系转接办理
                break;
        }
    }

    public String getAppAbstractCategory() {
        return appAbstractCategory;
    }

    public void setAppAbstractCategory(String appAbstractCategory) {
        this.appAbstractCategory = appAbstractCategory;
    }

    public AppAbstract getSelected() {
        return selected;
    }

    public void setSelected(AppAbstract selected) {
        this.selected = selected;
    }

    public List<AppAbstract> getAppAbstractList() {
        // 为了 template 调试而增加的一次查询。正式代码查询不在这里做
        appAbstractList = getFacade().findAll();
        
        return appAbstractList;
    }

    public void setAppAbstractList(List<AppAbstract> appAbstractList) {
        this.appAbstractList = appAbstractList;
    }

    public boolean isVilliageOfficeEditable() {
        return villiageOfficeEditable;
    }

    public void setVilliageOfficeEditable(boolean villiageOfficeEditable) {
        this.villiageOfficeEditable = villiageOfficeEditable;
    }
 
    public boolean isTownOfficeEditable() {
        return townOfficeEditable;
    }

    public void setTownOfficeEditable(boolean townOfficeEditable) {
        this.townOfficeEditable = townOfficeEditable;
    }

    public boolean isBureauOfficeEditable() {
        return bureauOfficeEditable;
    }

    public void setBureauOfficeEditable(boolean bureauOfficeEditable) {
        this.bureauOfficeEditable = bureauOfficeEditable;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private AppAbstractFacade getFacade() {
        return appAbstractFacade;
    }

    public AppAbstract prepareCreate() {
        selected = new AppAbstract();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("AppAbstractCreated"));
        if (!JsfUtil.isValidationFailed()) {
            appAbstractList = null;    // Invalidate list of appAbstractList to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("AppAbstractUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("AppAbstractDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            appAbstractList = null;    // Invalidate list of appAbstractList to trigger re-query.
        }
    }

    public List<AppAbstract> getItems() {
        if (appAbstractList == null) {
            appAbstractList = getFacade().findAll();
        }
        return appAbstractList;
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

    public AppAbstract getAppAbstract(java.lang.Integer id) {
        return getFacade().find(id);
    }

    public List<AppAbstract> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<AppAbstract> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = AppAbstract.class)
    public static class AppAbstractControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            AppAbstractController controller = (AppAbstractController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "appAbstractController");
            return controller.getAppAbstract(getKey(value));
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
            if (object instanceof AppAbstract) {
                AppAbstract o = (AppAbstract) object;
                return getStringKey(o.getAppAbstractId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), AppAbstract.class.getName()});
                return null;
            }
        }
    }
}
