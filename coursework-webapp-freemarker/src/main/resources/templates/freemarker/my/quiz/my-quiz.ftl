<#include '/my/quiz/layout.ftl'/>

<#macro blockTitle>我的考试记录 - 我的考试 - ${blockTitleParent}</#macro>

<#macro blockList>

    <#if myTestpaperResults??>
        <table class="table table-striped table-hover">
            <tbody>
            {% for myTestpaperResult in myTestpaperResults %}
            {% set myTestpaper = myTestpapers[myTestpaperResult.testId]|default(null) %}
            {% if myTestpaper %}
            {% set course = courses[myTestpaper.target|get_course_id] %}
            {% endif %}
            {% include 'TopxiaWebBundle:MyQuiz:my-quiz-tr.html.twig' %}
            {% endfor %}
            </tbody>
        </table>
        {{ web_macro.paginator(paginator) }}
    <#else>
        <div class="empty">还没有参加过任何考试</div>
    </#if>

</#macro>