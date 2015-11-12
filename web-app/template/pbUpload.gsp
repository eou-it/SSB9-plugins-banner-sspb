<%--
Copyright 2013-2016 Ellucian Company L.P. and its affiliates.
--%>
<span> <!--TODO referencing $parent.i18nGet, should not have this dependency-->
    <button ng-click="openUploadModal()"><g:message code="sspb.css.cssManager.upload.label" /></button>

    <!-- map editing modal body-->
    <div modal="uploadShouldBeOpen"  options="uploadModalOpts">
        <div class="modal-header">
            <h4>{{label}}</h4>
        </div>
        <div class="modal-body">
            <!-- css file upload form TODO put in a modal dialog -->
            <form id="upload" ng-upload method="POST" action="{{rootWebApp + 'uploadCss?delay=yes'}}">
                <div>
                    <label><g:message code="sspb.css.cssManager.cssName.label" /></label>
                    <input name="cssName" ng-model='cssName'/>
                </div>
                <div>
                    <label><g:message code="sspb.css.cssManager.description.label" /></label>
                    <input name="description" ng-model='description'/>
                </div>

                <div>
                    <label><g:message code="sspb.css.cssManager.upload.file.label" />
                    <input type="file" name="file" />
                </div>
                <div>
                    <input upload-submit="complete(content, completed)" type="submit" class="btn" value="Submit" />
                </div>
            </form>
            <div class="alert alert-info"><g:message code="sspb.css.cssManager.upload.server.response.label" /> {{uploadResponse}}</div>

        </div>
        <div class="modal-footer">
            <button class="btn btn-success ok" ng-click="closeUploadModal()"><g:message code="pb.template.upload.ok.label" /></button>
        </div>
    </div>



</span>