package net.hedtech.banner.virtualDomain

import static org.junit.Assert.*
import org.junit.*

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
    void testSaveAllToFile() {
        def vdPath = "test/testData/virtualDomain/"
        VirtualDomain.findAll().each { vd ->
            ["codeGet","codePost","codePut","codeDelete"].each {  code ->
                def text= vd[code]
                if (text )  {
                    //println text
                    def file = new File("$vdPath/${vd.serviceName}.${code}.txt")
                    file.text = vd [ code ]
                }
            }
        }
    }

    @Test
    void loadAllFromFile() {
        fail "Implement me"
    }

}
