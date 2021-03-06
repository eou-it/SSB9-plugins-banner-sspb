/*******************************************************************************
 Copyright 2019 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.security

import javax.persistence.Column
import javax.persistence.Embeddable

@Embeddable
class CssSecurityId implements Serializable{

    @Column(name="GBRCSEC_CSS_ID", nullable= false ,length = 19)
    Long cssId

    @Column(name="GBRCSEC_DEVELOPER_USER_ID", nullable= false ,length = 30)
    String developerUserId
}
