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
@Table(name = "village_office")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "VillageOffice.findAll", query = "SELECT v FROM VillageOffice v"),
    @NamedQuery(name = "VillageOffice.findByVillageOfficeId", query = "SELECT v FROM VillageOffice v WHERE v.villageOfficeId = :villageOfficeId"),
    @NamedQuery(name = "VillageOffice.findByName", query = "SELECT v FROM VillageOffice v WHERE v.name = :name")})
public class VillageOffice implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "village_office_id")
    private Integer villageOfficeId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "name")
    private String name;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "villageOfficeId")
    private Collection<VillageAgent> villageAgentCollection;
    @JoinColumn(name = "town_office_id", referencedColumnName = "town_office_id")
    @ManyToOne(optional = false)
    private TownOffice townOfficeId;

    public VillageOffice() {
    }

    public VillageOffice(Integer villageOfficeId) {
        this.villageOfficeId = villageOfficeId;
    }

    public VillageOffice(Integer villageOfficeId, String name) {
        this.villageOfficeId = villageOfficeId;
        this.name = name;
    }

    public Integer getVillageOfficeId() {
        return villageOfficeId;
    }

    public void setVillageOfficeId(Integer villageOfficeId) {
        this.villageOfficeId = villageOfficeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlTransient
    public Collection<VillageAgent> getVillageAgentCollection() {
        return villageAgentCollection;
    }

    public void setVillageAgentCollection(Collection<VillageAgent> villageAgentCollection) {
        this.villageAgentCollection = villageAgentCollection;
    }

    public TownOffice getTownOfficeId() {
        return townOfficeId;
    }

    public void setTownOfficeId(TownOffice townOfficeId) {
        this.townOfficeId = townOfficeId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (villageOfficeId != null ? villageOfficeId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof VillageOffice)) {
            return false;
        }
        VillageOffice other = (VillageOffice) object;
        if ((this.villageOfficeId == null && other.villageOfficeId != null) || (this.villageOfficeId != null && !this.villageOfficeId.equals(other.villageOfficeId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.VillageOffice[ villageOfficeId=" + villageOfficeId + " ]";
    }
    
}
