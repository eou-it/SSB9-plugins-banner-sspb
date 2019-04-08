/*******************************************************************************
 Copyright 2019 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.security


import javax.persistence.*
import java.sql.Date

@Entity
@Table(name = "GBRVSEC")
@NamedQueries(value = [
        @NamedQuery(name = "Gbrvsec.fetchById",
                query = """FROM   VirtualDomainSecurity a
		   WHERE  a.domainSecKey.domainId = :domainId
		  """
        )
])
class VirtualDomainSecurity implements Serializable{

    @EmbeddedId
    DomainSecKey domainSecKey

    @Column(name="GBRVSEC_SURROGATE_ID")
    @SequenceGenerator(name = 'GBRVSEC_SEQ_GENERATOR', sequenceName = 'SSPBMGR.GBRVSEC_SURROGATE_ID_SEQUENCE')
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = 'GBRVSEC_SEQ_GENERATOR')
    Long surrogateId

    @Column(name="GBRVSEC_TYPE", nullable= false ,length = 30)
    String type

    @Column(name="GBRVSEC_ALLOW_MODIFY_IND", nullable= false ,length = 1)
    String allowModifyInd

    @Column(name="GBRVSEC_ACTIVITY_DATE", nullable= false )
    Date activityDate

    @Column(name="GBRVSEC_USER_ID", nullable= false ,length = 30)
    String userId

    @Column(name="GBRVSEC_DATA_ORIGIN" ,length = 30)
    String dataOrigin

    @Column(name="GBRVSEC_VERSION" ,length = 19)
    Long version

    @Column( name="GBRVSEC_VPDI_CODE", length = 19)
    Long vpdiCode

    boolean equals(o) {
        if (this.is(o)) return true
        if (getClass() != o.class) return false

        VirtualDomainSecurity that = (VirtualDomainSecurity) o

        if (activityDate != that.activityDate) return false
        if (allowModifyInd != that.allowModifyInd) return false
        if (dataOrigin != that.dataOrigin) return false
        if (domainSecKey != that.domainSecKey) return false
        if (surrogateId != that.surrogateId) return false
        if (type != that.type) return false
        if (userId != that.userId) return false
        if (version != that.version) return false
        if (vpdiCode != that.vpdiCode) return false

        return true
    }

    int hashCode() {
        int result
        result = (domainSecKey != null ? domainSecKey.hashCode() : 0)
        result = 31 * result + (surrogateId != null ? surrogateId.hashCode() : 0)
        result = 31 * result + (type != null ? type.hashCode() : 0)
        result = 31 * result + (allowModifyInd != null ? allowModifyInd.hashCode() : 0)
        result = 31 * result + (activityDate != null ? activityDate.hashCode() : 0)
        result = 31 * result + (userId != null ? userId.hashCode() : 0)
        result = 31 * result + (dataOrigin != null ? dataOrigin.hashCode() : 0)
        result = 31 * result + (version != null ? version.hashCode() : 0)
        result = 31 * result + (vpdiCode != null ? vpdiCode.hashCode() : 0)
        return result
    }

    @Override
    public String toString() {
        return "VirtualDomainSecurity{" +
                "domainSecKey=" + domainSecKey +
                ", surrogateId=" + surrogateId +
                ", type='" + type + '\'' +
                ", allowModifyInd='" + allowModifyInd + '\'' +
                ", activityDate=" + activityDate +
                ", userId='" + userId + '\'' +
                ", dataOrigin='" + dataOrigin + '\'' +
                ", version=" + version +
                ", vpdiCode=" + vpdiCode +
                '}';
    }

    public static def findById(String id) {
        List domainsec = []
        domainsec = VirtualDomainSecurity.withSession {session ->
            domainsec = session.getNamedQuery('Gbrvsec.fetchById').setString('domainId', id).list()}
        def result = domainsec?.size()>0?domainsec:null
        return result
    }
}