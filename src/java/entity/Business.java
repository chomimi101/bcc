/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.Collection;
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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author bob-macbook
 */
@Entity
@Table(name = "business")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Business.findAll", query = "SELECT b FROM Business b"),
    @NamedQuery(name = "Business.findByBusinessId", query = "SELECT b FROM Business b WHERE b.businessId = :businessId"),
    @NamedQuery(name = "Business.findByName", query = "SELECT b FROM Business b WHERE b.name = :name"),
    @NamedQuery(name = "Business.findByEditAppUrl", query = "SELECT b FROM Business b WHERE b.editAppUrl = :editAppUrl"),
    @NamedQuery(name = "Business.findByDispAppUrl", query = "SELECT b FROM Business b WHERE b.dispAppUrl = :dispAppUrl")})
public class Business implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "business_id")
    private Integer businessId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 60)
    @Column(name = "name")
    private String name;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "edit_app_url")
    private String editAppUrl;
    @Size(max = 45)
    @Column(name = "disp_app_url")
    private String dispAppUrl;
    @JoinColumn(name = "county_bureau_id", referencedColumnName = "county_bureau_id")
    @ManyToOne(optional = false)
    private CountyBureau countyBureauId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "businessId")
    private Collection<AppAbstract> appAbstractCollection;

    public Business() {
    }

    public Business(Integer businessId) {
        this.businessId = businessId;
    }

    public Business(Integer businessId, String name, String editAppUrl) {
        this.businessId = businessId;
        this.name = name;
        this.editAppUrl = editAppUrl;
    }

    public Integer getBusinessId() {
        return businessId;
    }

    public void setBusinessId(Integer businessId) {
        this.businessId = businessId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public CountyBureau getCountyBureauId() {
        return countyBureauId;
    }

    public void setCountyBureauId(CountyBureau countyBureauId) {
        this.countyBureauId = countyBureauId;
    }

    @XmlTransient
    public Collection<AppAbstract> getAppAbstractCollection() {
        return appAbstractCollection;
    }

    public void setAppAbstractCollection(Collection<AppAbstract> appAbstractCollection) {
        this.appAbstractCollection = appAbstractCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (businessId != null ? businessId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Business)) {
            return false;
        }
        Business other = (Business) object;
        if ((this.businessId == null && other.businessId != null) || (this.businessId != null && !this.businessId.equals(other.businessId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Business[ businessId=" + businessId + " ]";
    }
    
}
