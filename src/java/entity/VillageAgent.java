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
@Table(name = "village_agent")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "VillageAgent.findAll", query = "SELECT v FROM VillageAgent v"),
    @NamedQuery(name = "VillageAgent.findByVillageAgentId", query = "SELECT v FROM VillageAgent v WHERE v.villageAgentId = :villageAgentId"),
    @NamedQuery(name = "VillageAgent.findByName", query = "SELECT v FROM VillageAgent v WHERE v.name = :name"),
    @NamedQuery(name = "VillageAgent.findByPassword", query = "SELECT v FROM VillageAgent v WHERE v.password = :password")})
public class VillageAgent implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "village_agent_id")
    private Integer villageAgentId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "name")
    private String name;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "password")
    private String password;
    @JoinColumn(name = "village_office_id", referencedColumnName = "village_office_id")
    @ManyToOne(optional = false)
    private VillageOffice villageOfficeId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "villageAgentId")
    private Collection<AppAbstract> appAbstractCollection;

    public VillageAgent() {
    }

    public VillageAgent(Integer villageAgentId) {
        this.villageAgentId = villageAgentId;
    }

    public VillageAgent(Integer villageAgentId, String name, String password) {
        this.villageAgentId = villageAgentId;
        this.name = name;
        this.password = password;
    }

    public Integer getVillageAgentId() {
        return villageAgentId;
    }

    public void setVillageAgentId(Integer villageAgentId) {
        this.villageAgentId = villageAgentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public VillageOffice getVillageOfficeId() {
        return villageOfficeId;
    }

    public void setVillageOfficeId(VillageOffice villageOfficeId) {
        this.villageOfficeId = villageOfficeId;
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
        hash += (villageAgentId != null ? villageAgentId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof VillageAgent)) {
            return false;
        }
        VillageAgent other = (VillageAgent) object;
        if ((this.villageAgentId == null && other.villageAgentId != null) || (this.villageAgentId != null && !this.villageAgentId.equals(other.villageAgentId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.VillageAgent[ villageAgentId=" + villageAgentId + " ]";
    }
    
}
