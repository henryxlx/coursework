<#assign script_controller = 'analysis/video' />
<#assign href = 'admin/operation/analysis/video-view' />
<#assign menu = 'video-view' />

<#include '/admin/operation/analysis/layout.ftl'/>

<#macro blockAnalysisBody>
    <div class="col-md-12">
        <#if tab=="trend">
            <div id="line-data"></div>
            <input id="data" type="hidden" value='${data}'>
            <input type="hidden" name="minCreatedTime" value='${minCreatedTime!}'>
            </input>
        <#elseif tab=="detail">
            <#include '/admin/operation/analysis/video-view.table.ftl' />
        </#if>
    </div>
</#macro>
