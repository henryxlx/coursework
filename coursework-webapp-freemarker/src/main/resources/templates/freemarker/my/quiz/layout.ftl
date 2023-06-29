<#assign side_nav = 'quiz'>

<@block_title '我的考试'/>

<#include '/my/layout.ftl'/>

<#macro blockMain>
    <div class="panel panel-default panel-col">

        <div class="panel-heading">
            我的考试
        </div>
        <div class="panel-body">
            <ul class="nav nav-tabs">
                <li class="${myQuizActive!}"><a href="${ctx}/my/quiz">考试记录</a></li>
                <li class="${favoriteActive!}"><a href="${ctx}/my/favorite/question/show">收藏的题目</a></li>
            </ul>
            <br>
            <#if blockList??><@blockList/></#if>

        </div>
    </div>
</#macro>