/*******************************************************************************
 Copyright 2019 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.security


import javax.persistence.*
import java.sql.Date

@Entity
@Table(name = "GORFBPR")
@NamedQueries(value=[
        @NamedQuery(
                name = "getAllBusinessProfile",
                query = """ FROM BusinessProfile g 
                        where g.id.profileCode= :profile 
                        and  g.id.profileUserId =:userId
                        """ )
])
class BusinessProfile implements Serializable{
    public static final long serialVersionUID = 8240815446214088510L

    @EmbeddedId
    BuisnessProfileId id

    @Column(name="GORFBPR_ACTIVITY_DATE", nullable= false )
    Date activityDate

    @Column(name="GORFBPR_USER_ID", nullable= false ,length = 30)
    String userId

    @Column(name="GORFBPR_SURROGATE_ID" ,length = 19)
    Long surrogateId

    @Column(name="GORFBPR_VERSION" ,length = 19)
    Long version

    @Column( name="GORFBPR_VPDI_CODE", length = 19)
    Long vpdiCode


    public static def findByProfile(String profile, String userId) {
        def profiles
        profiles = BusinessProfile.withSession {session ->
            profiles = session.getNamedQuery('getAllBusinessProfile').setString('profile', profile).setString('userId', userId).list()}
        def result = profiles?.size()>0?profiles:null
        return result
    }

    boolean equals(o) {
        if (this.is(o)) return true
        if (getClass() != o.class) return false

        BusinessProfile that = (BusinessProfile) o

        if (activityDate != that.activityDate) return false
        if (id != that.id) return false
        if (surrogateId != that.surrogateId) return false
        if (userId != that.userId) return false
        if (version != that.version) return false
        if (vpdiCode != that.vpdiCode) return false

        return true
    }

    int hashCode() {
        int result
        result = (id != null ? id.hashCode() : 0)
        result = 31 * result + (activityDate != null ? activityDate.hashCode() : 0)
        result = 31 * result + (userId != null ? userId.hashCode() : 0)
        result = 31 * result + (surrogateId != null ? surrogateId.hashCode() : 0)
        result = 31 * result + (version != null ? version.hashCode() : 0)
        result = 31 * result + (vpdiCode != null ? vpdiCode.hashCode() : 0)
        return result
    }

    @Override
    public String toString() {
        return "BusinessProfile{" +
                "id=" + id +
                ", activityDate=" + activityDate +
                ", userId='" + userId + '\'' +
                ", surrogateId=" + surrogateId +
                ", version=" + version +
                ", vpdiCode=" + vpdiCode +
                '}';
    }
}