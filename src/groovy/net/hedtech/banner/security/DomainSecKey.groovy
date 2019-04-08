/*******************************************************************************
 Copyright 2019 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.security

import javax.persistence.Column
import javax.persistence.Embeddable

@Embeddable
class DomainSecKey implements Serializable{


    @Column(name="GBRVSEC_DOMAIN_ID", nullable= false ,length = 19)
    Long domainId

    @Column(name="GBRVSEC_DEVELOPER_USER_ID", nullable= false ,length = 30)
    String developerUserId
}
