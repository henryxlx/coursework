<@block_title "标签:${(tag.name)!'不存在'}"/>

<#include '/default/layout.ftl'/>

<#macro blockContent>

    <div class="es-row-wrap container-gap">
        <div class="row">
            <div class="col-md-12">
                <div class="page-header"><h1>标签<#if tag??>:${tag.name }</#if></h1></div>
            </div>
        </div>

        <div class="row" style="min-height:300px;">
            <div class="col-md-9">
                <#if tag??>
                    <#if courses??>
                        <@renderController path='/course/coursesBlock'  params={'courses':courses, 'view':'list'} />
                        <@web_macro.paginator paginator! />
                    <#else>
                        <div class="empty">没有搜到相关课程，请换个标签试试！</div>
                    </#if>
                <#else>
                    <div class="empty">标签不存在，请重新输入！</div>
                </#if>
            </div>
            <div class="col-md-3">
                <a href="${ctx}/tag">&raquo; 查看所有标签</a>
            </div>
        </div>

    </div>
</#macro>