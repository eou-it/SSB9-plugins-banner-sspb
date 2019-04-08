package net.hedtech.banner.security

import javax.persistence.Column
import javax.persistence.Embeddable

@Embeddable
class ProfileKey implements Serializable{

    @Column(name="GORFBPR_FGAC_USER_ID", nullable= false ,length = 30)
    String profileUserId

    @Column(name="GORFBPR_FBPR_CODE", nullable= false ,length = 1)
    String profileCode
}
