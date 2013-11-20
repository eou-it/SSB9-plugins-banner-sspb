package net.hedtech.banner.tools

class PBUtilServiceBase {
    def final static propertyDataDir = 'SSPB_DATA_DIR'
    def final static loadOverwriteExisting=0
    def final static loadSkipExisting=1
    def final static loadRenameExisting =2
    def final static loadIfNew=3

    def static pbConfig = grails.util.Holders.getConfig().pageBuilder
    def nowAsIsoInFileName = {
        new Date().format("yyyy-MM-dd_HH_mm_ss")
    }

    def nowAsIsoTime = {
        new Date().format("HH:mm:ss.SSS")
    }

    def bootMsg = { msg ->
        def tempMsg = "***** ${nowAsIsoTime()} - Bootstrap: $msg "
        tempMsg=tempMsg.padRight(120,"*")
        println tempMsg
    }

}
