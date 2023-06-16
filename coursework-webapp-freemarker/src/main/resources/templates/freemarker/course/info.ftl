<#assign script_controller = 'course/dashboard' />
<#assign script_arguments = '{course_uri: ${ctx}/course/${course.id}}'/>

<#include '/course/dashboard-layout.ftl' />

<#macro blockDashboardMain>
    <#assign nav = 'info' />
    <#include '/course/dashboard-nav.ftl' />

    <dl class="course-infos" style="width:100%">
        <#if !course.about?? && !category?? && !course.tags?? && !course.goals?? && !course.audiences??>
            <div class="empty">暂无课程信息</div>
        </#if>
        <#if course.about??>
            <dt>简介</dt>
            <dd>${course.about}</dd>
        </#if>
        <#if category??>
            <dt>分类</dt>
            <dd>
                <a href="${ctx}/course/explore/${category.code!category.id!}" target="_blank">${category.name}</a>
            </dd>
        </#if>

        <#if course.tags??>
            <dt>标签</dt>
            <dd>
                <ul class="list-inline">
                    <#list tags! as tag>
                        <li><a href="${ctx}/tag/${tag.id}">${tag.name}</a></li>
                    <#else>
                        <li>暂无标签</li>
                    </#list>
                </ul>
            </dd>
        </#if>

        <#if course.goals??>
            <dt>课程目标</dt>
            <dd>
                <ul>
                    <#list course.goals! as goal>
                        <li>${goal}</li>
                    </#list>
                </ul>
            </dd>
        </#if>

        <#if course.audiences??>
            <dt>适合人群</dt>
            <dd>
                <ul>
                    <#list course.audiences as audience>
                        <li>${audience}</li>
                    </#list>
                </ul>
            </dd>
        </#if>
    </dl>

</#macro>