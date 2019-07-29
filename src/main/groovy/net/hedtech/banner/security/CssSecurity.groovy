/*******************************************************************************
 Copyright 2019 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.security

import org.hibernate.annotations.Generated
import org.hibernate.annotations.GenerationTime

import javax.persistence.*

@Entity
@Table(name = "GBRCSEC")
@NamedQueries(value = [
        @NamedQuery(name = "CssSecurity.fetchAllByCssId",
                query = """FROM   CssSecurity a
		   WHERE  a.id.cssId = :cssId
		  """
        )
])

class CssSecurity implements Serializable{

    public static final long serialVersionUID = 7474581258174177299L

    @EmbeddedId
    CssSecurityId id

    @Column(name="GBRCSEC_SURROGATE_ID")
    @Generated( value = GenerationTime.ALWAYS)
    @SequenceGenerator(name = 'GBRCSEC_SEQ_GENERATOR', sequenceName = 'SSPBMGR.GBRCSEC_SURROGATE_ID_SEQUENCE')
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = 'GBRCSEC_SEQ_GENERATOR')
    Long surrogateId

    @Column(name="GBRCSEC_TYPE", nullable= false ,length = 30)
    String type

    @Column(name="GBRCSEC_ALLOW_MODIFY_IND", nullable= false ,length = 1)
    String allowModifyInd

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="GBRCSEC_ACTIVITY_DATE", nullable= false )
    Date activityDate

    @Column(name="GBRCSEC_USER_ID", nullable= false ,length = 30)
    String userId

    @Column(name="GBRCSEC_DATA_ORIGIN" ,length = 30)
    String dataOrigin

    @Column(name="GBRCSEC_VERSION" ,length = 19)
    Long version

    @Column( name="GBRCSEC_VPDI_CODE", length = 19)
    Long vpdiCode


    static constraints = {
        dataOrigin(nullable:true, maxSize:30)
        vpdiCode(nullable:true, maxSize:19)
    }

    boolean equals(o) {
        if (this.is(o)) return true
        if (getClass() != o.class) return false

        CssSecurity that = (CssSecurity) o

        if (activityDate != that.activityDate) return false
        if (allowModifyInd != that.allowModifyInd) return false
        if (dataOrigin != that.dataOrigin) return false
        if (id != that.id) return false
        if (surrogateId != that.surrogateId) return false
        if (type != that.type) return false
        if (userId != that.userId) return false
        if (version != that.version) return false
        if (vpdiCode != that.vpdiCode) return false

        return true
    }

    int hashCode() {
        int result
        result = (id != null ? id.hashCode() : 0)
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

    public static def fetchAllByCssId(Long id) {
        if (id) {
            return CssSecurity.withSession { session -> session.getNamedQuery('CssSecurity.fetchAllByCssId').setLong('cssId', id).list() }
        } else {
            return []
        }
    }

    @Override
    public String toString() {
        return "CssSecurity{" +
                "id=" + id +
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
}