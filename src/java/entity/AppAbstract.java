package entity;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@Table(name = "app_abstract")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AppAbstract.findAll", query = "SELECT a FROM AppAbstract a"),
    @NamedQuery(name = "AppAbstract.findByAppAbstractId", query = "SELECT a FROM AppAbstract a WHERE a.appAbstractId = :appAbstractId"),
    @NamedQuery(name = "AppAbstract.findByState", query = "SELECT a FROM AppAbstract a WHERE a.state = :state"),
    @NamedQuery(name = "AppAbstract.findByVillageName", query = "SELECT a FROM AppAbstract a WHERE a.villageName = :villageName"),
    @NamedQuery(name = "AppAbstract.findByLastModifyDate", query = "SELECT a FROM AppAbstract a WHERE a.lastModifyDate = :lastModifyDate"),
    @NamedQuery(name = "AppAbstract.findByLastModifierName", query = "SELECT a FROM AppAbstract a WHERE a.lastModifierName = :lastModifierName"),
    @NamedQuery(name = "AppAbstract.findByEditAppUrl", query = "SELECT a FROM AppAbstract a WHERE a.editAppUrl = :editAppUrl"),
    @NamedQuery(name = "AppAbstract.findByDispAppUrl", query = "SELECT a FROM AppAbstract a WHERE a.dispAppUrl = :dispAppUrl")})
public class AppAbstract implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "app_abstract_id")
    private Integer appAbstractId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "state")
    private String state;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "village_name")
    private String villageName;
    @Column(name = "last_modify_date")
    @Temporal(TemporalType.DATE)
    private Date lastModifyDate;
    @Size(max = 45)
    @Column(name = "last_modifier_name")
    private String lastModifierName;
    @Size(max = 45)
    @Column(name = "edit_app_url")
    private String editAppUrl;
    @Size(max = 45)
    @Column(name = "disp_app_url")
    private String dispAppUrl;
    @JoinColumn(name = "business_id", referencedColumnName = "business_id")
    @ManyToOne(optional = false)
    private Business businessId;
    @JoinColumn(name = "village_agent_id", referencedColumnName = "village_agent_id")
    @ManyToOne(optional = false)
    private VillageAgent villageAgentId;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "appAbstractId")
    private Collection<GuotuAppDetail> guotuAppDetailCollection;

    public AppAbstract() {
    }

    public AppAbstract(Integer appAbstractId) {
        this.appAbstractId = appAbstractId;
    }

    public AppAbstract(Integer appAbstractId, String state, String villageName) {
        this.appAbstractId = appAbstractId;
        this.state = state;
        this.villageName = villageName;
    }

    public Integer getAppAbstractId() {
        return appAbstractId;
    }

    public void setAppAbstractId(Integer appAbstractId) {
        this.appAbstractId = appAbstractId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getVillageName() {
        return villageName;
    }

    public void setVillageName(String villageName) {
        this.villageName = villageName;
    }

    public Date getLastModifyDate() {
        return lastModifyDate;
    }

    public void setLastModifyDate(Date lastModifyDate) {
        this.lastModifyDate = lastModifyDate;
    }

    public String getLastModifierName() {
        return lastModifierName;
    }

    public void setLastModifierName(String lastModifierName) {
        this.lastModifierName = lastModifierName;
    }

    public String getEditAppUrl() {
        return editAppUrl;
    }

    public void setEditAppUrl(String editAppUrl) {
        this.editAppUrl = editAppUrl;
    }

    public String getDispAppUrl() {
        return dispAppUrl;
    }

    public void setDispAppUrl(String dispAppUrl) {
        this.dispAppUrl = dispAppUrl;
    }

    public Business getBusinessId() {
        return businessId;
    }

    public void setBusinessId(Business businessId) {
        this.businessId = businessId;
    }

    public VillageAgent getVillageAgentId() {
        return villageAgentId;
    }

    public void setVillageAgentId(VillageAgent villageAgentId) {
        this.villageAgentId = villageAgentId;
    }

    @XmlTransient
    public Collection<GuotuAppDetail> getGuotuAppDetailCollection() {
        return guotuAppDetailCollection;
    }

    public void setGuotuAppDetailCollection(Collection<GuotuAppDetail> guotuAppDetailCollection) {
        this.guotuAppDetailCollection = guotuAppDetailCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (appAbstractId != null ? appAbstractId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AppAbstract)) {
            return false;
        }
        AppAbstract other = (AppAbstract) object;
        if ((this.appAbstractId == null && other.appAbstractId != null) || (this.appAbstractId != null && !this.appAbstractId.equals(other.appAbstractId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.AppAbstract[ appAbstractId=" + appAbstractId + " ]";
    }
    
}
