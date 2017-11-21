<#assign content>
    <div class="row">
        <div class="col-md-12">
            <h3>Welcome!</h3>
            <p>Please enter the URL of the site you want to capture</p>
            <form method="post">
                <div class="form-group">
                    <label for="url">URL</label>
                    <input type="text" name="url" placeholder="URL" class="form-control" value="" />
                </div>

                <input type="submit" class="btn btn-primary" value="Capture URL" />
            </form>

            <#if error??>
                <div class="alert alert-danger content-separator">
                    ${error}
                </div>
            </#if>

            <#if capture??>
                <div class="content-separator">
                    <h3>${capture.title}</h3>
                    <img src="${capture.source}" class="img-fluid" alt="${capture.title}"/>
                </div>
            </#if>
        </div>
    </div>
</#assign>

<#include "template.ftl"/>
