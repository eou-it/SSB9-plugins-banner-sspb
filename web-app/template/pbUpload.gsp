<span>
    <!--Copyright 2013-2016 Ellucian Company L.P. and its affiliates.-->
    <!--TODO referencing $parent.i18nGet, should not have this dependency-->
    <button ng-click="openUploadModal()"><g:message code="sspb.css.cssManager.upload.label" /></button>

    <!-- map editing modal body-->
    <div modal="uploadShouldBeOpen"  options="uploadModalOpts">
        <div class="modal-header">
            <h4>{{label}}</h4>
        </div>
        <div class="modal-body">
            <!-- css file upload form TODO put in a modal dialog -->
            <form id="upload" name="uploadform" ng-upload method="POST" action="{{rootWebApp + 'uploadCss?delay=yes'}}">
                <div>
                    <label><g:message code="sspb.css.cssManager.cssName.label" /></label>
                    <input name="cssName" ng-model='cssName'
                           required maxlength="60" ng-pattern="/^[a-zA-Z]+[a-zA-Z0-9\._-]*$/"/>
                    <div ng-messages="uploadform.cssName.$error" role="alert" class="fieldValidationMessage">
                        <span ng-message="pattern" ><g:message code="sspb.page.visualbuilder.name.invalid.pattern.message" /></span>
                        <span ng-message="required" > <g:message code="sspb.page.visualbuilder.name.required.message" /></span>
                    </div>
                </div>
                <div>
                    <label><g:message code="sspb.css.cssManager.description.label" /></label>
                    <input name="description" ng-model='description' maxlength="255"/>
                </div>

                <div>
                    <label><g:message code="sspb.css.cssManager.upload.file.label" />
                    <input type="file" name="file" accept=".css" />
                </div>
                <div>
                    <input upload-submit="complete(content, completed)" type="submit" class="btn btn-primary" value="Submit" />
                </div>
            </form>
            <div class="alert alert-info"><g:message code="sspb.css.cssManager.upload.server.response.label" /> {{uploadResponse}}</div>

        </div>
        <div class="modal-footer">
            <button class="btn btn-success ok" ng-click="closeUploadModal()"><g:message code="pb.template.upload.ok.label" /></button>
        </div>
    </div>
</span>
