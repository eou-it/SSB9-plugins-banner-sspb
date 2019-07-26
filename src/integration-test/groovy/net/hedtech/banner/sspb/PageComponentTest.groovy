/*******************************************************************************
 * Copyright 2019 Ellucian Company L.P. and its affiliates.
 ******************************************************************************/

package net.hedtech.banner.sspb

import spock.lang.Specification

class PageComponentTest extends Specification{

    def pageComponent
    def tb

    def setup(){
        pageComponent = new PageComponent()
        tb = new PageComponent(type:PageComponent.COMP_TYPE_TEXT, name: "GD1", model: "someField", label:"MyLabel" ,
                                value: "this is a test field")
        def root = new PageComponent(type:PageComponent.COMP_TYPE_TEXT, name: "base", model: "someField", label:"MyLabel" ,
                value: "this is a test field")
        List pbList = new ArrayList();
        pbList.add(tb);
        pageComponent.components = pbList
        pageComponent.root = root
    }

    void "testforStringType" (){
        when:
        boolean type = PageComponent.isStringType("test")
        then:
        type
    }

    void "test for initnewRecord" (){
        when:
        def res = pageComponent.initNewRecordJS()
        then:
        res.length()>0
    }
}
