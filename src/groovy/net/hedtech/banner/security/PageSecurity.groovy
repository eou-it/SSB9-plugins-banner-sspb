/*******************************************************************************
 Copyright 2019 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.security


import javax.persistence.*
import java.sql.Date

@Entity
@Table(name = "GBRPSEC")
@NamedQueries(value = [
        @NamedQuery(name = "PageSecurity.fetchAllByPageId",
                query = """FROM   PageSecurity a
		   WHERE  a.id.pageId = :pageId
		  """
        )
])
class PageSecurity implements Serializable{

    public static final long serialVersionUID = 2102468661415084360L

    @EmbeddedId
    PageSecurityId id

    @Column(name="GBRPSEC_SURROGATE_ID")
    @SequenceGenerator(name = 'GBRPSEC_SEQ_GENERATOR', sequenceName = 'SSPBMGR.GBRPSEC_SURROGATE_ID_SEQUENCE')
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = 'GBRPSEC_SEQ_GENERATOR')
    Long surrogateId

    @Column(name="GBRPSEC_TYPE", nullable= false ,length = 30)
    String type

    @Column(name="GBRPSEC_ALLOW_MODIFY_IND", nullable= false ,length = 1)
    String allowModifyInd

    @Column(name="GBRPSEC_ACTIVITY_DATE", nullable= false )
    Date acitivityDate

    @Column(name="GBRPSEC_USER_ID", nullable= false ,length = 30)
    String userId

    @Column(name="GBRPSEC_DATA_ORIGIN" ,length = 30)
    String dataOrigin

    @Column(name="GBRPSEC_VERSION" ,length = 19)
    Long version

    @Column( name="GBRPSEC_VPDI_CODE", length = 19)
    Long vpdiCode



    public static def fetchAllByPageId(Long id) {
       return  PageSecurity.withSession {session -> session.getNamedQuery('PageSecurity.fetchAllByPageId').setLong('pageId', id).list()}
    }

    boolean equals(o) {
        if (this.is(o)) return true
        if (getClass() != o.class) return false

        PageSecurity that = (PageSecurity) o

        if (acitivityDate != that.acitivityDate) return false
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
        result = 31 * result + (acitivityDate != null ? acitivityDate.hashCode() : 0)
        result = 31 * result + (userId != null ? userId.hashCode() : 0)
        result = 31 * result + (dataOrigin != null ? dataOrigin.hashCode() : 0)
        result = 31 * result + (version != null ? version.hashCode() : 0)
        result = 31 * result + (vpdiCode != null ? vpdiCode.hashCode() : 0)
        return result
    }

    @Override
    public String toString() {
        return "PageSecurity{" +
                "id=" + id +
                ", surrogateId=" + surrogateId +
                ", type='" + type + '\'' +
                ", allowModifyInd='" + allowModifyInd + '\'' +
                ", acitivityDate=" + acitivityDate +
                ", userId='" + userId + '\'' +
                ", dataOrigin='" + dataOrigin + '\'' +
                ", version=" + version +
                ", vpdiCode=" + vpdiCode +
                '}';
    }
}