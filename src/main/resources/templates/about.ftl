<#assign content>
    <div class="row">
        <div class="col-md-12">
            <h3>About</h3>
            <div class="abount-container">
                <table class="table table-hover">
                    <tbody>
                    <tr>
                        <th scope="row">Git Branch</th>
                        <td>${gitBranch}</td>
                    </tr>
                    <tr>
                        <th scope="row">Git Build Host</th>
                        <td>${gitBuildHost}</td>
                    </tr>
                    <tr>
                        <th scope="row">Git Build Time</th>
                        <td>${gitBuildTime}</td>
                    </tr>
                    <tr>
                        <th scope="row">Git Build Version</th>
                        <td>${gitBuildVersion}</td>
                    </tr>
                    <tr>
                        <th scope="row">Git Commit Id</th>
                        <td>${gitCommitId}</td>
                    </tr>
                    <tr>
                        <th scope="row">Git Commit Id Abbreviation</th>
                        <td>${gitCommitIdAbbrev}</td>
                    </tr>
                    <tr>
                        <th scope="row">Git Commit Time</th>
                        <td>${gitCommitTime}</td>
                    </tr>
                    <tr>
                        <th scope="row">Git Tags</th>
                        <td>${gitTags}</td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</#assign>

<#include "template.ftl"/>
