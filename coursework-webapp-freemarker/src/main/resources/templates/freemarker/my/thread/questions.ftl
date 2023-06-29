<#assign side_nav = 'my-questions'>

<@block_title '我的问答'/>

<#include '/my/layout.ftl'/>

<#macro blockMain>


    <div class="panel panel-default panel-col">


        <div class="panel-heading">我的问答
        </div>

        <div class="panel-body">

            <#if threads??>
                <#include '/my/thread/thread-list.ftl'/>
            <#else>
                <div class="empty">暂无提问的记录</div>
            </#if>

        </div>

    </div>

</#macro>