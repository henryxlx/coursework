<#assign script_controller = 'analysis/join-lesson' />
<#assign href = 'admin/operation/analysis/join-lesson' />
<#assign menu = 'join-lesson' />

<#include '/admin/operation/analysis/layout.ftl'/>

<#macro blockAnalysisBody>

    <div class="col-md-12">

        <#if tab=="trend">
            <div id="line-data"></div>
            <input id="data" type="hidden" value='${data}'>
            </input><input id="joinLessonStartDate" type="hidden" value='${joinLessonStartDate!}'>
        </input>
        <#elseif tab=="detail">
            <#include '/admin/operation/analysis/join-lesson.table.ftl' />
        </#if>
    </div>
</#macro>