<#assign siteNav = 'course/explore'/>
<#assign metaKeywords><#if category.id??>${category.name}</#if> ${setting('site.name')}</#assign>
<#assign metaDescription><#if category.id??>${category.name}}的</#if>课程列表，第${RequestParameters['page']!1}页。</#assign>

<#assign localVarForBlockTitle><#if category.id??>${category.name} 课程<#else>全部课程</#if> 第${RequestParameters['page']!1}页</#assign>
<@block_title localVarForBlockTitle/>

<#include '/default/layout.ftl'>

<#macro blockContent>

    <div class="es-row-wrap container-gap">
        <div class="row">
            <div class="col-md-12">
                <div class="page-header"><h1>课程</h1></div>
            </div>
        </div>

        <div class="row row-3-9">
            <div class="col-md-3">
                <ul class="nav nav-pills nav-stacked">
                    <li <#if !category.id??>class="active"</#if>><a href="${ctx}/course/explore">全部</a></li>
                    <#list categories! as cat>
                        <li <#if category.id?? && category.id == cat.id>class="active"</#if>>
                            <a href="${ctx}/course/explore/${cat.code!cat.id!}">
                                <#if cat.depth gt 1>
                                    <#list 0..(cat.depth-1) as i>&nbsp;&nbsp;</#list>
                                </#if>
                                ${cat.name}</a>
                        </li>
                    </#list>
                </ul>
            </div>
            <div class="col-md-9">
                <#if category.id?? && category.description??>
                    <div class="page">
                        ${category.description}
                    </div>
                </#if>
                <div class="category-filters">
                    <a href="${ctx}/course/explore/${category.code!category.id!}?sort=latest"
                       <#if sort == 'latest'>class="selected"</#if>>最新</a>
                    <a href="${ctx}/course/explore/${category.code!category.id!}?sort=popular"
                       <#if sort == 'popular'>class="selected"</#if>>热门</a>
                    <a href="${ctx}/course/explore/${category.code!category.id!}?sort=recommendedSeq"
                       <#if sort == 'recommendedSeq'>class="selected"</#if>>推荐</a>
                    <a href="${ctx}/course/explore/${category.code!category.id!}?sort=free"
                       <#if sort == 'free'>class="selected"</#if>>免费</a>
                </div>

                <#if courses??>
                    <@renderController path='/course/coursesBlock' params={'courses':courses, 'view':'list'}/>
                    <@web_macro.paginator paginator!/>
                <#else>
                    <div class="empty">该分类下无课程</div>
                </#if>
            </div>
        </div>

    </div>
</#macro>