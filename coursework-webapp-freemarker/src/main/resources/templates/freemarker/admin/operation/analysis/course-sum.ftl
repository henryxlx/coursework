<#assign script_controller = 'analysis/course-sum' />
<#assign href = 'admin/operation/analysis/course-sum' />
<#assign menu = 'course-sum' />

<#include '/admin/operation/analysis/layout.ftl'/>

<#macro blockAnalysisBody>
    <div class="col-md-12">

        <#if tab=="trend">
            <div id="line-data"></div>
            <input id="data" type="hidden" value='${data!}'>
            </input> <input id="courseSumStartDate" type="hidden" value='${courseSumStartDate!}'>
        </input>
        <#elseif tab=="detail" >
            <#include '/admin/operation/analysis/course-sum.table.ftl' />
        </#if>
    </div>
</#macro>
