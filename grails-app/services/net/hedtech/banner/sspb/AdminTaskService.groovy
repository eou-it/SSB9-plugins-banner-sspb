/******************************************************************************
 *  Copyright 2013-2016 Ellucian Company L.P. and its affiliates.             *
 ******************************************************************************/
package net.hedtech.banner.sspb
import net.hedtech.banner.tools.PBUtilServiceBase

class AdminTaskService {
    //static transactional = false

    def pageUtilService

    def create(Map content, ignore) {
        def result = [:]
        if (content.task == 'import') {
            if (content.pages) {
                def count = pageUtilService.importAllFromDir(PBUtilServiceBase.pbConfig.locations.page, PBUtilServiceBase.loadOverwriteExisting)
                result << [importedPagesCount: count]
            }
        }
        result
    }

}