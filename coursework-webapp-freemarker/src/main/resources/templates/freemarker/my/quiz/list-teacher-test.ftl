
<#if paperResults?? && paperResults?size gt 0>

    <table class="table table-striped table-hover">
        <tbody>
        <#list paperResults! as tid, paperResult>

            <#assign testpaper = testpapers[''+paperResult.testId]/>
            <#assign student = users[''+paperResult.userId]/>
            <#if courses??>
                <#assign course = courses[get_course_id(testpaper.target)]! />
            </#if>
            <#if teachers??>
                <#assign teacher = teachers[''+paperResult.checkTeacherId]! />
            </#if>
            <#include '/my/quiz/list-teacher-test-tr.ftl'/>

        </#list>
        </tbody>
    </table>
    <@web_macro.paginator paginator!/>

<#else>
    <#if status?? && status == 'reviewing'>
        <div class="empty">还没有等待批阅的试卷</div>
    <#elseif status?? && status == 'finished'>
        <div class="empty">还没有已经批阅的试卷</div>
    </#if>
</#if>