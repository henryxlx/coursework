<#include '/my/quiz/layout.ftl'/>

<#macro blockTitle>我的考试记录 - 我的考试 - ${blockTitleParent}</#macro>

<#macro blockList>

    <#if myTestpaperResults??>
        <table class="table table-striped table-hover">
            <tbody>
            <#list myTestpaperResults as myTestpaperResult>
                <#assign myTestpaper = myTestpapers[myTestpaperResult.testId]! />
                <#if myTestpaper??>
                    <#assign course = courses[get_course_id(myTestpaper.target)] />
                </#if>
                <#include '/my/quiz/my-quiz-tr.ftl' />
            </#list>
            </tbody>
        </table>
        <@web_macro.paginator paginator! />
    <#else>
        <div class="empty">还没有参加过任何考试</div>
    </#if>

</#macro>