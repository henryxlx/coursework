<#assign side_nav = 'testCheck'/>
<#assign parentId = parentId!/>
<#assign script_controller = 'testpaper/index'/>

<@block_title '试卷批阅'/>

<#include '/course/manage/layout.ftl'/>

<#macro blockMain>

    <div class="panel panel-default panel-col">

        <div class="panel-heading">
            试卷批阅
        </div>
        <div class="panel-body">
            <ul class="nav nav-tabs">
                <li class="<#if status?? && status == 'reviewing'>active</#if>"><a
                            href="${ctx}/course/${course.id}/check/reviewing/list">未批阅</a></li>
                <li class="<#if status ?? && status == 'finished'>active</#if>"><a
                            href="${ctx}/course/${course.id}/check/finished/list">已批阅</a></li>
            </ul>
            <br>
            <#include '/my/quiz/list-teacher-test.ftl'/>

        </div>
    </div>

</#macro>