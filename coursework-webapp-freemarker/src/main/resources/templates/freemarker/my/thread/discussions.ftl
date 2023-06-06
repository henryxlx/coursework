<#assign side_nav = 'my-discussions'>
<#include '/my/layout.ftl'/>

<#macro blockTitle>我的话题 -  - ${blockTitleParent}</#macro>

<#macro blockMain>

    <div class="panel panel-default panel-col">
        <div class="panel-heading">我的话题</div>

        <div class="panel-body">
            <#include '/my/thread/tab.ftl' />
            <#if threads??>
                <#include '/my/thread/thread-list.ftl' />
            <#else>
                <div class="empty">你还没有发表过话题</div>
            </#if>
        </div>
    </div>
</#macro>