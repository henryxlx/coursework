<#assign script_controller = 'course/dashboard' />
<#assign script_arguments = '{course_uri: ${ctx}/course/${course.id}}'/>

<#include '/course/dashboard-layout.ftl' />

<#macro blockDashboardMain>
    <#assign nav = 'lesson' />
    <#include '/course/dashboard-nav.ftl' />

    <#if items??>
        <#assign show = true />
        <#include '/course/lesson/item-list.ftl'/>
    <#else>
        <ul class="media-list">
            <li class="mvl tac text-muted">课程暂无课时内容</li>
        </ul>
    </#if>

</#macro>

<#macro blockDashboardRelatedCoursesBlock>
    <#if setting('course.relatedCourses', '0') == '1'>
        <@renderController path='/course/relatedCoursesBlock' params={'course':course}/>
    </#if>
</#macro>