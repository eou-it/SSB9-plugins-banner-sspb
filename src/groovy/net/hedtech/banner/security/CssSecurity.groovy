/*******************************************************************************
 Copyright 2019 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.security


import javax.persistence.*
import java.sql.Date

@Entity
@Table(name = "GBRCSEC")
@NamedQueries(value = [
        @NamedQuery(name = "Gbrcsec.fetchByCssId",
                query = """FROM   CssSecurity a
		   WHERE  a.cssSecKey.cssId = :cssId
		  """
        )
])

class CssSecurity implements Serializable{

    @EmbeddedId
    CssSecKey cssSecKey

    @Column(name="GBRCSEC_SURROGATE_ID")
    @SequenceGenerator(name = 'GBRCSEC_SEQ_GENERATOR', sequenceName = 'SSPBMGR.GBRCSEC_SURROGATE_ID_SEQUENCE')
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = 'GBRCSEC_SEQ_GENERATOR')
    Long surrogateId

    @Column(name="GBRCSEC_TYPE", nullable= false ,length = 30)
    String type

    @Column(name="GBRCSEC_ALLOW_MODIFY_IND", nullable= false ,length = 1)
    String allowModifyInd

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

    boolean equals(o) {
        if (this.is(o)) return true
        if (getClass() != o.class) return false

        CssSecurity that = (CssSecurity) o

        if (activityDate != that.activityDate) return false
        if (allowModifyInd != that.allowModifyInd) return false
        if (cssSecKey != that.cssSecKey) return false
        if (dataOrigin != that.dataOrigin) return false
        if (surrogateId != that.surrogateId) return false
        if (type != that.type) return false
        if (userId != that.userId) return false
        if (version != that.version) return false
        if (vpdiCode != that.vpdiCode) return false

        return true
    }

    int hashCode() {
        int result
        result = (cssSecKey != null ? cssSecKey.hashCode() : 0)
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
        return "CssSecurity{" +
                "cssSecKey=" + cssSecKey +
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
        List cssSecurity = []
        cssSecurity = CssSecurity.withSession {session ->
            cssSecurity = session.getNamedQuery('Gbrcsec.fetchByCssId').setString('cssId', id).list()}
        def result = cssSecurity?.size()>0?cssSecurity:null
        return result
    }
}