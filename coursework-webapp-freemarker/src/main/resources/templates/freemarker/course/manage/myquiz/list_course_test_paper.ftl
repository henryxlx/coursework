<#assign side_nav = 'testCheck'/>
<#assign parentId = parentId!/>
<#assign script_controller = 'testpaper/index'/>
<#include '/course/manage/layout.ftl'/>
<#macro blockTitle>试卷批阅 - ${blockTitleParent}</#macro>

<#macro blockMain>

    <div class="panel panel-default panel-col">

        <div class="panel-heading">
            试卷批阅
        </div>
        <div class="panel-body">
            <ul class="nav nav-tabs">
                <li class="<#if status?? && status == 'reviewing'>active</#if>"><a
                            href="${ctx}/course/${course.id}/check/reviewing/list">未批阅</a></li>
                <li class="<#if status ?? && status == 'finished'>}active</#if>"><a
                            href="${ctx}/course/${course.id}/check/finished/list">已批阅</a></li>
            </ul>
            <br>
            <#include '/course/manage/myquiz/list_teacher_test.ftl'/>

        </div>
    </div>

</#macro>