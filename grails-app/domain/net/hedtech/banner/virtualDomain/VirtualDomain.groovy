package net.hedtech.banner.virtualDomain

class VirtualDomain {
    String name
    String typeOfCode="S" //SQL
    String codeGet
    String codePost
    String codePut
    String codeDelete

    static constraints = {
        name nullable: false, unique: true, maxSize: 30
        typeOfCode nullable: false, inList: ["S","G"]
        codeGet widget: 'textarea',  nullable: false, maxSize: 1000000
        codePost widget: 'textarea', nullable: true, maxSize:  1000000
        codePut widget: 'textarea', nullable: true, maxSize: 1000000
        codeDelete widget: 'textarea', nullable: true, maxSize: 1000000
    }

    /*  //uncomment first time if db object is created
    static mapping = {
        codeGet type: "clob"
        codePost type: "clob"
        codePut type: "clob"
        codeDelete type: "clob"
    }
    */
}
