<#assign tab_nav = 'favorited'/>

<#include '/my/course/layout.ftl'/>

<#macro blockTitle>已收藏 - 我的课程 - ${blockTitleParent}</#macro>

<#macro blockMain>
    <div class="panel panel-default panel-col">
        <div class="panel-heading"><span>我的课程</span></div>
        <div class="panel-body">
            <#include '/my/course/nav-pill.ftl'/>
            <#if courses?? && courses?size gt 0>
                <@renderController path='/course/coursesBlock' params={'courses': courses, 'view': 'grid'} />
                <@web_macro.paginator paginator! />
            <#else>
                <div class="empty">暂无已收藏的课程</div>
            </#if>
        </div>
    </div>
</#macro>