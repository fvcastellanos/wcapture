<#assign content>
    <div class="row">
        <div class="col-md-12">
            <h3>Capture History</h3>
            <div class="container">
                <table class="table table-hover">
                    <thead>
                        <tr>
                            <th>Created Date</th>
                            <th>Url</th>
                        </tr>
                    </thead>
                    <tbody>
                    <#list captures as captureHistory>
                        <tr>
                            <td>${captureHistory.createdDate?string('yyyy/MM/dd HH:mm:ss')}</td>
                            <td>${captureHistory.url}</td>
                        </tr>
                    </#list>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</#assign>

<#include "template.ftl"/>
