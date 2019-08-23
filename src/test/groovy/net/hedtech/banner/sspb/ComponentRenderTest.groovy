/*******************************************************************************
 * Copyright 2013-2016 Ellucian Company L.P. and its affiliates.
 ******************************************************************************/

package net.hedtech.banner.sspb
import spock.lang.Specification
class ComponentRenderTest extends Specification {

    void "test detail + Text item render"(){
        given:
        def detail  = new PageComponent(type:PageComponent.COMP_TYPE_DETAIL,name: "detail1")
        def tb = new PageComponent(type:PageComponent.COMP_TYPE_TEXT, name: "TB1", model: "someField", label:"MyLabel")
        tb.parent = detail
        tb.root = detail
        detail.components = [tb]
        detail.root = detail
        def html = detail.compileComponent("")

        expect:
        html != null
        println html
    }


}
