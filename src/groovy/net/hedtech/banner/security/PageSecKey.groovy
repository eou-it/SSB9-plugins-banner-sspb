/*******************************************************************************
 Copyright 2019 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.security

import javax.persistence.Column
import javax.persistence.Embeddable

@Embeddable
class PageSecKey implements Serializable{

    @Column(name="GBRPSEC_PAGE_ID", nullable= false ,length = 19)
    Long pageId

    @Column(name="GBRPSEC_DEVELOPER_USER_ID", nullable= false ,length = 30)
    String developerUserId
}
