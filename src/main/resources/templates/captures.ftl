<#assign content>
    <div class="row">
        <div class="col-md-12">
            <h3>Captures</h3>
            <div class="abount-container">
                <table class="table table-hover table-bordered">
                    <thead>
                        <tr>
                            <th>
                                URL
                            </th>
                            <th>
                                Result
                            </th>
                            <th>
                                Reason
                            </th>
                            <th>
                                CDN
                            </th>
                        </tr>
                    </thead>
                    <tbody>
                        <#list captures as capture>
                            <tr>
                                <td>${capture.url}</td>
                                <td>${capture.result}</td>
                                <td>
                                    <#if capture.error??>
                                        ${capture.error}
                                    </#if>
                                </td>
                                <td>
                                    <#if capture.storedPath??>
                                        <a href="${capture.storedPath}" target="_blank">${capture.storedPath}</a>
                                    </#if>
                                </td>
                            </tr>
                        </#list>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</#assign>

<#include "template.ftl"/>
