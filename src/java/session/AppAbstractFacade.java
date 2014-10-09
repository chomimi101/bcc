package session;
import entity.AppAbstract;
import entity.TownOfficer;
import entity.VillageAgent;
import java.util.Date;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class AppAbstractFacade extends AbstractFacade<AppAbstract> {
    @PersistenceContext(unitName = "BCC_BLOB_PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public AppAbstractFacade() {
        super(AppAbstract.class);
    }

    public AppAbstract updateAppModifierNameAndDate(int app_id, String name) {
        AppAbstract app = em.find(AppAbstract.class, app_id);
        app.setLastModifyDate(new Date());
        app.setLastModifierName(name);
        
        return app;
    }
    
    // 村代办员通过按动‘提交’按钮启动行政申请上报到乡里。申请的状态由 “起草阶段” 变为 “等待乡里审批”
    // database modification operation has to be placed in a session bean to be transaction protected
    public void submitToTownOffice(int app_id, VillageAgent agent) {
        AppAbstract app = updateAppModifierNameAndDate(app_id,agent.getName());
        app.setState("等待乡里审批");
        edit(app);
    }
    
    // 乡办事员通过按动‘提交’按钮启动行政申请上报到局里。申请的状态由 “等待乡里审批” 变为 “等待局里审批”
    public void submitToBureauOffice(int app_id, TownOfficer officer) {
        AppAbstract app = updateAppModifierNameAndDate(app_id,officer.getName());
        app.setState("等待局里审批");
        edit(app);
    }
    
    public void returnAppToVillage(String name, AppAbstract a){
        // application 因材料不全等原因没有通过乡办事员审批，被退回村里重新申请。
        AppAbstract app = updateAppModifierNameAndDate(a.getAppAbstractId(),name);
        app.setState("退回村里的申请");
        edit(app);
    }

    public void submitAppToBureau(String name, AppAbstract a){
        // application 乡政府审批通过，提交到县局
        AppAbstract app = updateAppModifierNameAndDate(a.getAppAbstractId(),name);
        app.setState("等待局里审批");
        edit(app);
    }
    
    public void bureauApproved(String name, AppAbstract a){
        // application 乡政府审批通过，提交到县局
        AppAbstract app = updateAppModifierNameAndDate(a.getAppAbstractId(),name);
        app.setState("已获局里批准");
        edit(app);
    }
    
    public void disapprovedAppReturnToVillage(String name, AppAbstract a){
        // application 因材料不全等原因没有通过乡办事员或局办事员的审批，被退回村里重新申请。
        AppAbstract app = updateAppModifierNameAndDate(a.getAppAbstractId(),name);
        app.setState("申请未获批准");
        edit(app);
    }
}
