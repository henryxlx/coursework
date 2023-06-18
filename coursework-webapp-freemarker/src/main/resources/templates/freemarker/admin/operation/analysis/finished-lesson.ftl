<#assign script_controller = 'analysis/finished-lesson' />
<#assign href = 'admin/operation/analysis/finished-lesson' />
<#assign menu = 'finished-lesson' />

<#include '/admin/operation/analysis/layout.ftl'/>

<#macro blockAnalysisBody>

    <div class="col-md-12">

        <#if tab=="trend">
            <div id="line-data"></div>
            <input id="data" type="hidden" value='${data!}'>
            </input> <input id="finishedLessonStartDate" type="hidden" value='${finishedLessonStartDate!}'>
        </input>
        <#elseif tab=="detail">
            <#include '/admin/operation/analysis/finished-lesson.table.ftl' />
        </#if>
    </div>
</#macro>
