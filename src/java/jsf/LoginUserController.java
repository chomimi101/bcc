package jsf;
import entity.BureauClerk;
import entity.TownOfficer;
import entity.VillageAgent;
import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.servlet.http.HttpSession;

@ManagedBean
@SessionScoped
public class LoginUserController implements Serializable {
    private String userAccountName;
    private String userPassword;
    private String loginUserType;
    private String userUnitName;

    @PersistenceContext(unitName = "BCC_BLOB_PU")
    private EntityManager em;

    // login 处理：获取登录用户对象，核查口令字，转向与用户身份对应的业务处理流程
    public String logIn() {
        // 把登录用户对象保存在 login 对象属性变量中，供后面业务处理流程使用
        if (loginUserType.equals("village_agent")) {
            // 村代办员开户账号数据保存在 VillageAgent 表中
            Query query = em.createNamedQuery("VillageAgent.findByName");
            query.setParameter("name", userAccountName);
            VillageAgent agent = (VillageAgent)query.getSingleResult();
            
            if (agent.getPassword().equals(userPassword)) {
                FacesContext context = FacesContext.getCurrentInstance();
                context.getExternalContext().getSessionMap().put("loginUser", agent); // 把村代办员对象保存到 context 对象中
                userAccountName = agent.getName();
                userUnitName = agent.getVillageOfficeId().getName();
                return "/appAbstract/V_List";
            }
        }

        if (loginUserType.equals("town_officer")) {
            // 乡（主管处）办事员开户账号数据保存在 TownOfficer 表中
            Query query = em.createNamedQuery("TownOfficer.findByName");
            query.setParameter("name", userAccountName);
            TownOfficer officer = (TownOfficer)query.getSingleResult(); // 从 Officer 表中取回具有用户（乡办事员）账号的记录

            if (officer.getPassword().equals(userPassword)) {
                FacesContext context = FacesContext.getCurrentInstance();
                context.getExternalContext().getSessionMap().put("loginUser", officer); // 把乡办事员对象保存到 FacesContext 对象中
                userUnitName = officer.getTownOfficeId().getName();
                return "/appAbstract/T_List";
            }
        }

        if (loginUserType.equals("bureau_clerk")) {
            // 县主管局办事员开户账号数据保存在 BureauClerk 表中
            Query query = em.createNamedQuery("BureauClerk.findByName");
            query.setParameter("name", userAccountName);
            BureauClerk clerk = (BureauClerk)query.getSingleResult(); // 从 Clerk 表中取回具有用户（局办事员）账号的记录

            if (clerk.getPassword().equals(userPassword)) {
                FacesContext context = FacesContext.getCurrentInstance();
                context.getExternalContext().getSessionMap().put("loginUser", clerk); // 把局办事员对象保存到 FacesContext 对象中
                userUnitName = clerk.getCountyBureauId().getName();
               return "/appAbstract/B_List";
            }
        }

        return "/login.xhtml";
    }
    
    public String logoff() {
        FacesContext fc = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) fc.getExternalContext().getSession(false);
        session.invalidate();
        return "/login.xhtml";
    }
    
    public String getLoginUserType() {
        return loginUserType;
    }

    public void setLoginUserType(String loginUserType) {
        this.loginUserType = loginUserType;
    }
    
    public String getUserAccountName() {
        return userAccountName;
    }

    public void setUserAccountName(String userName) {
        this.userAccountName = userName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }
    
    public String getUserUnitName() {
        return userUnitName;
    }

    public void setUserUnitName(String userUnitName) {
        this.userUnitName = userUnitName;
    }
}
