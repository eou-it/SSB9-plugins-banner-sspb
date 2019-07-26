/******************************************************************************
 *  Copyright 2013-2018 Ellucian Company L.P. and its affiliates.             *
 ******************************************************************************/
package net.hedtech.banner.sspb

import grails.gorm.transactions.Rollback
import grails.testing.mixin.integration.Integration
import grails.util.GrailsWebMockUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.context.WebApplicationContext
import org.springframework.web.context.request.RequestContextHolder
import spock.lang.Specification

@Integration
@Rollback
class PageIntegrationSpec extends Specification {

    @Autowired
    WebApplicationContext ctx

    def setup() {
        GrailsWebMockUtil.bindMockWebRequest(ctx)
    }

    def cleanup() {
        RequestContextHolder.resetRequestAttributes()
    }

    void "Integration test create page and extension"() {
        given:
        Page base = new Page(constantName: "stu.base", modelView:
                '''{
                    "type": "page",
                    "name": "StudentBase",
                    "title": "Student Base",
                    "scriptingLanguage": "JavaScript",
                    "components": null
                   }''')
        Page ext1 = new Page(constantName: "stu.ext.1.1", modelView: "{}")
        base.addToExtensions(ext1)
        Page ext2 = new Page(constantName: "stu.ext.1.2", modelView:
                '''{
                    "type": "page",
                    "name": "StudentExtz12",
                    "title": "Student Base Extended 2",
                    "scriptingLanguage": "JavaScript",
                    "components": null
                   }''')
        ext1.addToExtensions(ext2)


        when: "base page created"
        then: "able to create page that is not an extension"
        base != null
        println "Created page ${base.constantName}"

        when: "extension created by adding to extensions list"
        then: "able to create page that is an extension"
        ext1 != null
        base.constantName == ext1.extendsPage.constantName
        base.extensions.size() == 1

        when: "extension of extension created by assigning extendsPage"
        then: "able to create page that is an extension of an extension"
        ext2 != null
        ext1.constantName == ext2.extendsPage.constantName

        when: "saving pages"
        base.save(flush:true,failOnError: true)

        then: "Save successful"
        base.constantName == Page.findByConstantName("stu.base").constantName
        ext1.constantName == Page.findByConstantName("stu.ext.1.1").constantName
        ext2.constantName == Page.findByConstantName("stu.ext.1.2").constantName
        println "Number of base page extensions: " + base.extensions?.size()
    }


    void "Integration test multiple extensions"() {
        given:
        Page base = new Page(constantName: "stu.base", modelView:
                '''{
                    "type": "page",
                    "name": "StudentBase",
                    "title": "Student Base",
                    "scriptingLanguage": "JavaScript",
                    "components": null
                   }''')
        Page ext1 = new Page(constantName: "stu.ext.1.1", modelView: "{}")
        base.addToExtensions(ext1)
        Page ext2 = new Page(constantName: "stu.ext.1.2", modelView:
                '''{
                    "type": "page",
                    "name": "StudentExtz12",
                    "title": "Student Base Extended 2",
                    "scriptingLanguage": "JavaScript",
                    "components": null
                   }''')
        base.addToExtensions(ext2)

        when: "Save with multiple extensions"
        base.save(flush:true,failOnError: true)

        then:
        base.extensions.size() == 2
        println "Number of base page extensions: " + base.extensions?.size()

    }

    void "Integration test update page"() {
        given:
        Page base = new Page(constantName: "stu.base", modelView:
                '''{
                    "type": "page",
                    "name": "StudentBase",
                    "title": "Student Base",
                    "scriptingLanguage": "JavaScript",
                    "components": null
                   }''')

        when:
        base.save(flush:true,failOnError: true)

        then: "Save successful"
        base.constantName == Page.findByConstantName("stu.base").constantName

        when:
        base.modelView =  '''{
                    "type": "page",
                    "name": "StudentBaseUpdate",
                    "title": "Student Base Updated",
                    "scriptingLanguage": "JavaScript",
                    "components": null
                   }'''
        base.save(flush:true,failOnError: true)

        then: "update successful"
        base.modelView == Page.findByConstantName("stu.base").modelView
        println "update successful"
    }


    void "Integration test update extended page"() {
        given:
        Page base = new Page(constantName: "stu.base", modelView:
                '''{
                    "type": "page",
                    "name": "StudentBase",
                    "title": "Student Base",
                    "scriptingLanguage": "JavaScript",
                    "components": null
                   }''')
        Page ext1 = new Page(constantName: "stu.ext.1.1", modelView: "{}",extendsPage: base)

        when:
        base.save(flush:true,failOnError: true)
        ext1.save(flush: true, failOnError: true)

        then: "Save successful"
        base.constantName == Page.findByConstantName("stu.base").constantName
        ext1.constantName == Page.findByConstantName("stu.ext.1.1").constantName
        Page.findByConstantName("stu.ext.1.1").extendsPage.constantName == Page.findByConstantName("stu.base").constantName
        println "Original page extended: " + Page.findByConstantName("stu.ext.1.1").extendsPage.constantName

        when:
        Page baseNew = new Page(constantName: "stu.base.new", modelView:
                '''{
                    "type": "page",
                    "name": "StudentBaseNew",
                    "title": "New Student Base",
                    "scriptingLanguage": "JavaScript",
                    "components": null
                   }''')
        baseNew.save(flush:true,failOnError: true)
        ext1.extendsPage = baseNew
        ext1.save(flush:true,failOnError: true)

        then: "update extends page successful"
        baseNew.constantName == Page.findByConstantName("stu.ext.1.1").extendsPage.constantName
        println "New page extended: "  + Page.findByConstantName("stu.ext.1.1").extendsPage.constantName
//        Page.findByConstantName("stu.base.new").extensions.size() == 1
//        Page.findByConstantName("stu.base").extensions.size() == 0
    }


//    void "Integration test failed delete page and extension"() {
//        given:
//        Page base = new Page(constantName: "stu.base", modelView:
//                '''{
//                    "type": "page",
//                    "name": "StudentBase",
//                    "title": "Student Base",
//                    "scriptingLanguage": "JavaScript",
//                    "components": null
//                   }''')
//        Page ext1 = new Page(constantName: "stu.ext.1.1", modelView: "{}")
//        base.addToExtensions(ext1)
//        Page ext2 = new Page(constantName: "stu.ext.1.2", modelView:
//                '''{
//                    "type": "page",
//                    "name": "StudentExtz12",
//                    "title": "Student Base Extended 2",
//                    "scriptingLanguage": "JavaScript",
//                    "components": null
//                   }''')
//        ext1.addToExtensions(ext2)
//        base.save(flush:true,failOnError: true)
//
//        when: "Try to delete base while extension 1 exists"
//        base.delete()
//        boolean correctErrorRaised = false
//        try {
//            base.save(flush: true)
//        } catch (org.springframework.dao.DataIntegrityViolationException dve){
//            correctErrorRaised = true
//        }
//
//        then: "error deleting parent"
//        correctErrorRaised
//        println "Correct error raised on parent deletion"
//
//        when: "Try to delete ext 1 while extensions 2 and 3 exist"
//        ext1.delete()
//        correctErrorRaised = false
//        try {
//            ext1.save(flush: true, failOnError: true)
//        } catch (org.springframework.dao.DataIntegrityViolationException dve){
//            correctErrorRaised = true
//        }
//
//        then: "error deleting parent"
//        correctErrorRaised
//        println "Correct error raised on multi extension parent deletion"
//
//    }


    void "Integration test successful delete page and extension"() {
        given:
        Page base = new Page(constantName: "stu.base", modelView:
                '''{
                    "type": "page",
                    "name": "StudentBase",
                    "title": "Student Base",
                    "scriptingLanguage": "JavaScript",
                    "components": null
                   }''')
        Page ext1 = new Page(constantName: "stu.ext.1.1", modelView: "{}")
        base.addToExtensions(ext1)
        Page ext2 = new Page(constantName: "stu.ext.1.2", modelView:
                '''{
                    "type": "page",
                    "name": "StudentBase",
                    "title": "Student Base Extended 2",
                    "scriptingLanguage": "JavaScript",
                    "components": null
                   }''')
        ext1.addToExtensions(ext2)
        base.save(flush:true,failOnError: true)

        when: "Removed second extension can delete first"
        ext1.removeFromExtensions(ext2)
        ext1.save(flush:true,failOnError: true)
        base.removeFromExtensions(ext1)
        base.save(flush:true,failOnError: true)
        ext1.delete(flush: true)
        ext1 = Page.findByConstantName("stu.ext.1.1")

        then:
        ext1 == null
        println "extension page successfully deleted"

        when: "Remove base page now allowed"
        base.delete(flush: true)
        base = Page.findByConstantName("stu.base")

        then:
        base == null
        println "base page successfully deleted"

    }


    void "Integration test empty Page"() {
        given:
        Page base = new Page(constantName: "stu.base", modelView: "{}")

        when: "Save empty page"
        base.save(flush:true,failOnError: true)

        then:
        Page.findByConstantName("stu.base").isEmptyInstance() == true
        println "Empty instance page created"

        when: "Save non-empty page"
        base.modelView =   '''{
                    "type": "page",
                    "name": "StudentBase",
                    "title": "Student Base",
                    "scriptingLanguage": "JavaScript",
                    "components": null
                   }'''
        base.save(flush:true,failOnError: true)

        then:
        Page.findByConstantName("stu.base").isEmptyInstance() == false
        println "Page no longer empty"

    }

}
