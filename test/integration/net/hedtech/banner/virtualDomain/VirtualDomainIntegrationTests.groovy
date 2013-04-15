package net.hedtech.banner.virtualDomain

import static org.junit.Assert.*
import org.junit.*
import grails.converters.JSON

class VirtualDomainIntegrationTests {

    @Before
    void setUp() {
        // Setup logic here
    }

    @After
    void tearDown() {
        // Tear down logic here
    }
    @Test
    void saveAllToFile() {
        def vdPath = "test/testData/virtualDomain/"
        VirtualDomain.findAll().each { vd ->
            def file = new File("$vdPath/${vd.serviceName}.json")
            def json =  new JSON(vd)
            def jsonString = json.toString(true)
            println "Exported $vd.serviceName"
            file.text = jsonString
        }
    }

    @Test
    void loadAllFromFile() {
        def vdPath = "test/testData/virtualDomain/"
        new File(vdPath).eachFileMatch(~/.*.json/) {   file ->
            def jsonString = file.getText()
            def json = JSON.parse(jsonString)
            def vd = new VirtualDomain(json)
            if (VirtualDomain.findByServiceName(vd.serviceName)) {
                vd.serviceName=vd.serviceName+".imp.dup"
                vd = vd.save(flush: true)
                println "WARN: service already exists. Imported as ${vd.serviceName}."
            } else {
                vd = vd.save(flush: true)
                println "Imported ${vd.serviceName} "
            }
        }

    }

}
