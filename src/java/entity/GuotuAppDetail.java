/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author bob-macbook
 */
@Entity
@Table(name = "guotu_app_detail")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GuotuAppDetail.findAll", query = "SELECT g FROM GuotuAppDetail g"),
    @NamedQuery(name = "GuotuAppDetail.findByGuotuAppId", query = "SELECT g FROM GuotuAppDetail g WHERE g.guotuAppId = :guotuAppId"),
    @NamedQuery(name = "GuotuAppDetail.findByBusinessName", query = "SELECT g FROM GuotuAppDetail g WHERE g.businessName = :businessName"),
    @NamedQuery(name = "GuotuAppDetail.findByApplicantName", query = "SELECT g FROM GuotuAppDetail g WHERE g.applicantName = :applicantName"),
    @NamedQuery(name = "GuotuAppDetail.findByAge", query = "SELECT g FROM GuotuAppDetail g WHERE g.age = :age"),
    @NamedQuery(name = "GuotuAppDetail.findBySex", query = "SELECT g FROM GuotuAppDetail g WHERE g.sex = :sex"),
    @NamedQuery(name = "GuotuAppDetail.findByHousehold", query = "SELECT g FROM GuotuAppDetail g WHERE g.household = :household"),
    @NamedQuery(name = "GuotuAppDetail.findByAddress", query = "SELECT g FROM GuotuAppDetail g WHERE g.address = :address"),
    @NamedQuery(name = "GuotuAppDetail.findByPhone", query = "SELECT g FROM GuotuAppDetail g WHERE g.phone = :phone"),
    @NamedQuery(name = "GuotuAppDetail.findByVillageOpinion", query = "SELECT g FROM GuotuAppDetail g WHERE g.villageOpinion = :villageOpinion"),
    @NamedQuery(name = "GuotuAppDetail.findByTownOpinion", query = "SELECT g FROM GuotuAppDetail g WHERE g.townOpinion = :townOpinion"),
    @NamedQuery(name = "GuotuAppDetail.findByBureauOpinion", query = "SELECT g FROM GuotuAppDetail g WHERE g.bureauOpinion = :bureauOpinion")})
public class GuotuAppDetail implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "guotu_app_id")
    private Integer guotuAppId;
    @Size(max = 80)
    @Column(name = "business_name")
    private String businessName;
    @Size(max = 20)
    @Column(name = "applicant_name")
    private String applicantName;
    @Column(name = "age")
    private Integer age;
    @Size(max = 6)
    @Column(name = "sex")
    private String sex;
    @Size(max = 6)
    @Column(name = "household")
    private String household;
    @Size(max = 200)
    @Column(name = "address")
    private String address;
    // @Pattern(regexp="^\\(?(\\d{3})\\)?[- ]?(\\d{3})[- ]?(\\d{4})$", message="电话/传真格式无效, 应为 xxx-xxx-xxxx")//if the field contains phone or fax number consider using this annotation to enforce field validation
    @Size(max = 30)
    @Column(name = "phone")
    private String phone;
    @Lob
    @Column(name = "shen_qing_file")
    private byte[] shenQingFile;
    @Lob
    @Column(name = "shen_pi_file")
    private byte[] shenPiFile;
    @Lob
    @Column(name = "song_yue_file")
    private byte[] songYueFile;
    @Lob
    @Column(name = "shen_cha_file")
    private byte[] shenChaFile;
    @Lob
    @Column(name = "qi_ta_file")
    private byte[] qiTaFile;
    @Size(max = 800)
    @Column(name = "village_opinion")
    private String villageOpinion;
    @Size(max = 800)
    @Column(name = "town_opinion")
    private String townOpinion;
    @Size(max = 800)
    @Column(name = "bureau_opinion")
    private String bureauOpinion;
    
    @JoinColumn(name = "app_abstract_id", referencedColumnName = "app_abstract_id")
    @ManyToOne(optional = false)
    private AppAbstract appAbstractId;

    public GuotuAppDetail() {
    }

    public GuotuAppDetail(Integer guotuAppId) {
        this.guotuAppId = guotuAppId;
    }

    public Integer getGuotuAppId() {
        return guotuAppId;
    }

    public void setGuotuAppId(Integer guotuAppId) {
        this.guotuAppId = guotuAppId;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getApplicantName() {
        return applicantName;
    }

    public void setApplicantName(String applicantName) {
        this.applicantName = applicantName;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getHousehold() {
        return household;
    }

    public void setHousehold(String household) {
        this.household = household;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public byte[] getShenQingFile() {
        return shenQingFile;
    }

    public void setShenQingFile(byte[] shenQingFile) {
        this.shenQingFile = shenQingFile;
    }

    public byte[] getShenPiFile() {
        return shenPiFile;
    }

    public void setShenPiFile(byte[] shenPiFile) {
        this.shenPiFile = shenPiFile;
    }

    public byte[] getSongYueFile() {
        return songYueFile;
    }

    public void setSongYueFile(byte[] songYueFile) {
        this.songYueFile = songYueFile;
    }

    public byte[] getShenChaFile() {
        return shenChaFile;
    }

    public void setShenChaFile(byte[] shenChaFile) {
        this.shenChaFile = shenChaFile;
    }

    public byte[] getQiTaFile() {
        return qiTaFile;
    }

    public void setQiTaFile(byte[] qiTaFile) {
        this.qiTaFile = qiTaFile;
    }

    public String getVillageOpinion() {
        return villageOpinion;
    }

    public void setVillageOpinion(String villageOpinion) {
        this.villageOpinion = villageOpinion;
    }

    public String getTownOpinion() {
        return townOpinion;
    }

    public void setTownOpinion(String townOpinion) {
        this.townOpinion = townOpinion;
    }

    public String getBureauOpinion() {
        return bureauOpinion;
    }

    public void setBureauOpinion(String bureauOpinion) {
        this.bureauOpinion = bureauOpinion;
    }

    public AppAbstract getAppAbstractId() {
        return appAbstractId;
    }

    public void setAppAbstractId(AppAbstract appAbstractId) {
        this.appAbstractId = appAbstractId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (guotuAppId != null ? guotuAppId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GuotuAppDetail)) {
            return false;
        }
        GuotuAppDetail other = (GuotuAppDetail) object;
        if ((this.guotuAppId == null && other.guotuAppId != null) || (this.guotuAppId != null && !this.guotuAppId.equals(other.guotuAppId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.GuotuAppDetail[ guotuAppId=" + guotuAppId + " ]";
    }
    
}
