
<#if paperResults??>

    <table class="table table-striped table-hover">
        <tbody>
        <#list paperResults! as paperResult>

            <#assign testpaper = testpapers[''+paperResult.testId]/>
            <#assign student = users[''+paperResult.userId]/>
            {% if courses|default(null) %}
            {% set course = courses[testpaper.target|get_course_id] %}
            {% endif %}
            {% if teachers|default(null) %}
            {% set teacher = teachers[paperResult.checkTeacherId]|default(null) %}
            {% endif %}
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