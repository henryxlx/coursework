<#assign nav = 'index'/>
<#assign bodyClass = 'index'/>

<@block_title '个人中心'/>

<#include '/layout.ftl'/>

<#macro blockContent>

    <div class="row">
        <#include '/my/my-nav.ftl'/>
        <div class="col-md-9">
            <div class="panel panel-default panel-col">
                <div class="panel-heading">
<#--                <#include '/mycourse/my-courses-nav.ftl'/>-->
            </div>
            <div class="panel-body">
                <#if learningCourses?? && learningCourses?size gt 0>
<#--                    <#assign users = users!/>-->
<#--                    <#include courses = learningCourses!/>-->
<#--                    <#include '/mycourse/my-courses-list.ftl'/>-->
                <#else>
                    <h3>你还没有学习任何课程，<a href="${ctx}/course/explore">来探索更多课程吧！ </a>
                </#if>
            </div>
        </div>
        <@web_macro.paginator paginator!/>
    </div>
</div>

</#macro>
