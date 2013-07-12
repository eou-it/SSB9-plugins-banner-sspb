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
            if (vd.serviceName.endsWith(".imp.dup"))
                println "Skipped exporting duplicate ( service name ending with .imp.dup)"
            else {
                def file = new File("$vdPath/${vd.serviceName}.json")
                JSON.use("deep")
                def json =  new JSON(vd)
                def jsonString = json.toString(true)
                println "Exported $vd.serviceName"
                file.text = jsonString
            }
        }
    }

    @Test
    void loadAllFromFile() {
        def vdPath = "test/testData/virtualDomain/"
        new File(vdPath).eachFileMatch(~/.*.json/) {   file ->
            def msg
            def jsonString = file.getText()
            JSON.use("deep")
            def json = JSON.parse(jsonString)
            def vd = new VirtualDomain()
            vd.serviceName=json.serviceName
            def vd1=VirtualDomain.findByServiceName(vd.serviceName)
            if (vd1) {
                vd.serviceName=vd.serviceName+".imp.dup"
                vd1=VirtualDomain.findByServiceName(vd.serviceName)
                if (vd1) //if we have already saved a duplicate, get rid of it.
                    vd1.delete(flush: true)
                msg= "WARN: service already exists. Imported as ${vd.serviceName}."
            } else {
                msg= "Imported ${vd.serviceName} "
            }
            //default marshaling fails on nested roles so have to do 'manually'
            vd.properties[ 'typeOfCode', 'dataSource', 'codeGet', 'codePost', 'codePut', 'codeDelete'] = json
            json.virtualDomainRoles.each {
                vd.addToVirtualDomainRoles(new VirtualDomainRole(it))
            }
            vd = vd.save(flush: true)
            println msg
        }
    }

}
