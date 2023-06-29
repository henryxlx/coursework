<#assign tab_nav = 'learned'/>

<@block_title '已学完'/>

<#include '/my/course/layout.ftl'/>

<#macro blockMain>
    <div class="panel panel-default panel-col">
        <div class="panel-heading"><span>我的课程</span></div>
        <div class="panel-body">
            <#include '/my/course/nav-pill.ftl'/>
            <#if courses?? && courses?size gt 0>
                <@renderController path='/course/coursesBlock' params={'courses': courses, 'view': 'grid', 'mode': 'learn'} />
                <@web_macro.paginator paginator! />
            <#else>
                <div class="empty">暂无已学完的课程</div>
            </#if>
        </div>
    </div>
</#macro>