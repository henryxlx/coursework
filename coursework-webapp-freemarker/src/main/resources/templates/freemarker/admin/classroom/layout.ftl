<#assign menu = menu!'classroom'/>

<#include '/admin/layout.ftl'/>

<#macro blockContent>
    <div class="col-md-2">
        <#if blockSidebar??><@blockSidebar/><#else>
            <div class="list-group">
                <a href="${ctx}/admin/classroom" class="list-group-item <#if menu == 'classroom'>active</#if>">班级管理</a>

                <#--                <a href="${ctx}/admin/classroom/review" class="list-group-item <#if menu == 'review'>active</#if>">班级评价</a>-->

                <#--                <a href="${ctx}/admin/classroom/thread" class="list-group-item <#if menu == 'thread'>active</#if>">班级讨论区</a>-->

                <a href="${ctx}/admin/classroom/setting" class="list-group-item <#if menu == 'setting'>active</#if>">班级设置</a>

            </div>
        </#if>
    </div>
    <div class="col-md-10">
        <#if blockMain??><@blockMain/></#if>
    </div>
</#macro>