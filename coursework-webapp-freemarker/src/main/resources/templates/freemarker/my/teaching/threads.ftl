<#if type == 'question'>
    <#assign side_nav = 'my-teaching-questions'/>
<#elseif type == 'discussion'>
    <#assign side_nav = 'my-teaching-discussions'/>
</#if>

<#assign varLocalBlockTitle>
    <#if type == 'question'>
        ${setting('default.user_name', '学员')}问答
    <#elseif type == 'discussion'>
        ${setting('default.user_name', '学员')}话题
    </#if>
</#assign>
<@block_title varLocalBlockTitle/>

<#include '/my/layout.ftl'/>

<#macro blockMain>

    <div class="panel panel-default panel-col">
        <div class="panel-heading">
            <#if type == 'question'>
                ${setting('default.user_name', '学员')}问答
            <#elseif type == 'discussion'>
                ${setting('default.user_name', '学员')}话题
            </#if>
        </div>

        <div class="panel-body">
            <#if type == 'question'>
                <#include '/my/teaching/tab.ftl' />
            </#if>
            <#if threads??>

                <#include '/my/thread/thread-list.ftl' />
            <#else>

                <#if type == 'question'>
                    <div class="empty">您的课程中还没有 ${setting('default.user_name', '学员')}提问过...</div>
                <#elseif type == 'discussion'>
                    <div class="empty">您的课程中还没有 ${setting('default.user_name', '学员')}话题...</div>
                </#if>

            </#if>
        </div>
    </div>
</#macro>