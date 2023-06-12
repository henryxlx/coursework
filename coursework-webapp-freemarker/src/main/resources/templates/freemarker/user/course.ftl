<#assign pageNav = type!'teach'/>

<#include '/user/layout.ftl'/>

<#macro blockMain>
    <div class="row">
        <div class="col-md-12">
            <#if courses?? && courses?size gt 0>
                <@renderController path='/course/coursesBlock' params={'courses': courses, 'view':'list'}/>
                <@web_macro.paginator paginator!/>
            <#else>
                <#if pageNav == 'teach'>
                    <div class="empty">无在教的课程</div>
                <#elseif pageNav == 'learn'>
                    <div class="empty">无在学的课程</div>
                <#elseif pageNav == 'favorited'>
                    <div class="empty">无收藏的课程</div>
                </#if>
            </#if>
        </div>
</div>
</#macro>

