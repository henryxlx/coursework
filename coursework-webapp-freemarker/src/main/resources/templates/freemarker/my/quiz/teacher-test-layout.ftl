<#assign side_nav = 'my-teaching-check'>
<#assign status = status!'reviewing'/>
<#include '/my/layout.ftl'/>

<#macro blockTitle>我的批阅 - ${blockTitleParent}</#macro>

<#macro blockMain>
    <div class="panel panel-default panel-col">

        <div class="panel-heading">
            我的批阅
        </div>
        <div class="panel-body">
            <ul class="nav nav-tabs">
                <li class="<#if status == 'reviewing'>active</#if>"><a
                            href="${ctx}/my/teacher/reviewing/test/list">未批阅</a></li>
                <li class="<#if status == 'finished'>active</#if>"><a
                            href="${ctx}/my/teacher/finished/test/list">已批阅</a></li>
            </ul>
            <br>
            <#include '/my/quiz/list-teacher-test.ftl' />

        </div>
    </div>
</#macro>