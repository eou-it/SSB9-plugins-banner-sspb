package net.hedtech.banner.sspb

import grails.test.spock.IntegrationSpec

class PageIntegrationSpec extends IntegrationSpec {

    def setup() {
    }

    def cleanup() {
    }

    void "Integration test create page and extension"() {
        given:
        Page base = new Page(constantName: "stu.base", modelView: "{\n" +
                "      \"type\": \"page\",\n" +
                "      \"name\": \"StudentBase\",\n" +
                "      \"title\": \"Student Base\",\n" +
                "      \"scriptingLanguage\": \"JavaScript\",\n" +
                "      \"components\": null\n" +
                "}")
        Page ext1 = new Page(constantName: "stu.ext.1.1", modelView: "{}")
        base.addToExtensions(ext1)
        Page ext2 = new Page(constantName: "stu.ext.1.2", modelView: "{\n" +
                "      \"type\": \"page\",\n" +
                "      \"name\": \"StudentExtz12\",\n" +
                "      \"title\": \"Student Base\",\n" +
                "      \"scriptingLanguage\": \"JavaScript\",\n" +
                "      \"components\": null\n" +
                "}")
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
        Page base = new Page(constantName: "stu.base", modelView: "{\n" +
                "      \"type\": \"page\",\n" +
                "      \"name\": \"StudentBase\",\n" +
                "      \"title\": \"Student Base\",\n" +
                "      \"scriptingLanguage\": \"JavaScript\",\n" +
                "      \"components\": null\n" +
                "}")
        Page ext1 = new Page(constantName: "stu.ext.1.1", modelView: "{}")
        base.addToExtensions(ext1)
        Page ext2 = new Page(constantName: "stu.ext.1.2", modelView: "{\n" +
                "      \"type\": \"page\",\n" +
                "      \"name\": \"StudentExtz12\",\n" +
                "      \"title\": \"Student Base\",\n" +
                "      \"scriptingLanguage\": \"JavaScript\",\n" +
                "      \"components\": null\n" +
                "}")
        base.addToExtensions(ext2)

        when: "Save with multiple extensions"
        base.save(flush:true,failOnError: true)

        then:
        base.extensions.size() == 2
        println "Number of base page extensions: " + base.extensions?.size()

    }

    void "Integration test update page"() {
        given:
        Page base = new Page(constantName: "stu.base", modelView: "{\n" +
                "      \"type\": \"page\",\n" +
                "      \"name\": \"StudentBase\",\n" +
                "      \"title\": \"Student Base\",\n" +
                "      \"scriptingLanguage\": \"JavaScript\",\n" +
                "      \"components\": null\n" +
                "}")

        when:
        base.save(flush:true,failOnError: true)

        then: "Save successful"
        base.constantName == Page.findByConstantName("stu.base").constantName

        when:
        base.modelView =  "{\n" +
                "      \"type\": \"page\",\n" +
                "      \"name\": \"StudentBaseUpdate\",\n" +
                "      \"title\": \"Student Base Updated\",\n" +
                "      \"scriptingLanguage\": \"JavaScript\",\n" +
                "      \"components\": null\n" +
                "}"
        base.save(flush:true,failOnError: true)

        then: "update successful"
        base.modelView == Page.findByConstantName("stu.base").modelView
        println "update successful"
    }


    void "Integration test update extended page"() {
        given:
        Page base = new Page(constantName: "stu.base", modelView: "{\n" +
                "      \"type\": \"page\",\n" +
                "      \"name\": \"StudentBase\",\n" +
                "      \"title\": \"Student Base\",\n" +
                "      \"scriptingLanguage\": \"JavaScript\",\n" +
                "      \"components\": null\n" +
                "}")
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
        Page baseNew = new Page(constantName: "stu.base.new", modelView: "{\n" +
                "      \"type\": \"page\",\n" +
                "      \"name\": \"StudentBaseNew\",\n" +
                "      \"title\": \"New Student Base\",\n" +
                "      \"scriptingLanguage\": \"JavaScript\",\n" +
                "      \"components\": null\n" +
                "}")
        baseNew.save(flush:true,failOnError: true)
        ext1.extendsPage = baseNew
        ext1.save(flush:true,failOnError: true)

        then: "update extends page successful"
        baseNew.constantName == Page.findByConstantName("stu.ext.1.1").extendsPage.constantName
        println "New page extended: "  + Page.findByConstantName("stu.ext.1.1").extendsPage.constantName
//        Page.findByConstantName("stu.base.new").extensions.size() == 1
//        Page.findByConstantName("stu.base").extensions.size() == 0
    }


    void "Integration test failed delete page and extension"() {
        given:
        Page base = new Page(constantName: "stu.base", modelView: "{\n" +
                "      \"type\": \"page\",\n" +
                "      \"name\": \"StudentBase\",\n" +
                "      \"title\": \"Student Base\",\n" +
                "      \"scriptingLanguage\": \"JavaScript\",\n" +
                "      \"components\": null\n" +
                "}")
        Page ext1 = new Page(constantName: "stu.ext.1.1", modelView: "{}")
        base.addToExtensions(ext1)
        Page ext2 = new Page(constantName: "stu.ext.1.2", modelView: "{\n" +
                "      \"type\": \"page\",\n" +
                "      \"name\": \"StudentExtz12\",\n" +
                "      \"title\": \"Student Base\",\n" +
                "      \"scriptingLanguage\": \"JavaScript\",\n" +
                "      \"components\": null\n" +
                "}")
        ext1.addToExtensions(ext2)
        base.save(flush:true,failOnError: true)

        when: "Try to delete base while extension 1 exists"
        base.delete()
        base.save(flush: true, failOnError: true)

        then: "error deleting parent"
        thrown(org.springframework.dao.DataIntegrityViolationException)

        when: "Try to delete ext 1 while extensions 2 and 3 exist"
        ext1.delete()
        ext1.save(flush: true, failOnError: true)

        then: "error deleting parent"
        thrown(org.springframework.dao.DataIntegrityViolationException)

    }


    void "Integration test successful delete page and extension"() {
        given:
        Page base = new Page(constantName: "stu.base", modelView: "{\n" +
                "      \"type\": \"page\",\n" +
                "      \"name\": \"StudentBase\",\n" +
                "      \"title\": \"Student Base\",\n" +
                "      \"scriptingLanguage\": \"JavaScript\",\n" +
                "      \"components\": null\n" +
                "}")
        Page ext1 = new Page(constantName: "stu.ext.1.1", modelView: "{}")
        base.addToExtensions(ext1)
        Page ext2 = new Page(constantName: "stu.ext.1.2", modelView: "{\n" +
                "      \"type\": \"page\",\n" +
                "      \"name\": \"StudentExtz12\",\n" +
                "      \"title\": \"Student Base\",\n" +
                "      \"scriptingLanguage\": \"JavaScript\",\n" +
                "      \"components\": null\n" +
                "}")
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
        base.modelView =   "{\n" +
                "      \"type\": \"page\",\n" +
                "      \"name\": \"StudentBase\",\n" +
                "      \"title\": \"Student Base\",\n" +
                "      \"scriptingLanguage\": \"JavaScript\",\n" +
                "      \"components\": null\n" +
                "}"
        base.save(flush:true,failOnError: true)

        then:
        Page.findByConstantName("stu.base").isEmptyInstance() == false
        println "Page no longer empty"

    }

}
