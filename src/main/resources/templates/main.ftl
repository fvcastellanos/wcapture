<#assign content>
    <div class="row">
        <div class="col-md-12">
            <h3>Welcome!</h3>
            <p>Please enter the URL of the site you want to capture</p>
            <#if error??>
                <div class="alert alert-danger content-separator">
                    ${error}
                </div>
            </#if>
            <form method="post">
                <div class="form-group">
                    <label for="url">URL</label>
                    <input type="text" name="url" placeholder="URL" class="form-control"/>
                </div>
                <div>
                    <input type="submit" class="btn btn-primary" value="Capture URL" required/>
                    <#if capture??>
                        <a id="showCapture" target="_blank" href="${capture.storedPath}" class="btn btn-success">See Capture</a>
                    </#if>
                </div>
                <#if requestId??>
                    <input type="hidden" name="requestId" id="requestId" value="${requestId}" />
                </#if>
            </form>
        </div>
    </div>
</#assign>

<#include "template.ftl"/>
