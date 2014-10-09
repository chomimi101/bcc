package jsf;
import entity.AppAbstract;
import entity.BureauClerk;
import session.AppAbstractFacade;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@ManagedBean(name="bureauWorkflowController")
@SessionScoped
// 乡、局审批处理页面对应的 Managed bean
public class BureauWorkflowController implements Serializable {
    @PersistenceContext(unitName = "BCC_BLOB_PU")
    private EntityManager em;
    @EJB
    AppAbstractFacade appAbstractFacade;

    private List<AppAbstract>   appAbstractList;    // 行政申请列表
    private int                 appAbstractId;      // 从当前处理的 ApplyState 中获取的
    private BureauClerk         clerk;
    private String              appProcessingState; // 等待审核;已经提交；已经批准;未获批准
    private boolean             submittedToBureau;

    public BureauWorkflowController() {
        appAbstractList = new ArrayList<>(); // 无参构建函数创建一个空白的申请列表
        FacesContext context = FacesContext.getCurrentInstance();
        clerk = (BureauClerk) context.getExternalContext().getSessionMap().get("loginUser"); // 从 context 对象中取回 officer - 村代理员对象
    }

    // application status: 起草阶段, 等待乡里审批, 等待局里审批, 已获局里批准, 申请未获批准，退回村里的申请       
    // 查询行政乡提交到主管局的行政审批业务
    public void querySubmittedToBureauAbstract() {
        // 查询提交到局办事员所在局主管的、处于‘等待局里审批’状态的行政审批数据
        appAbstractList =
            em.createQuery("SELECT a FROM AppAbstract a where a.state = '等待局里审批' AND a.businessId.countyBureauId.countyBureauId =:countyBureauId" )
                    .setParameter("countyBureauId", clerk.getCountyBureauId().getCountyBureauId()).getResultList();
        
        submittedToBureau = true;
    }

    // getters & setters
    public List<AppAbstract> getAppAbstractList() {
        return appAbstractList;
    }

    public void setAppAbstractList(List<AppAbstract> appAbstractList) {
        this.appAbstractList = appAbstractList;
    }
        
    public int getAppAbstractId() {
        return appAbstractId;
    }

    public void setAppAbstractId(int appAbstractId) {
        this.appAbstractId = appAbstractId;
    }

    public boolean isSubmittedToBureau() {
        return submittedToBureau;
    }

    public void setSubmittedToBureau(boolean submittedToBureau) {
        this.submittedToBureau = submittedToBureau;
    }    
}
