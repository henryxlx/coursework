<#assign side_nav = 'my-notes'/>
<#assign script_controller = 'my/notebooks'/>
<#include '/my/layout.ftl'/>

<#macro blockTitle>我的课程 - ${blockTitleParent}</#macro>

<#macro blockMain>

    <div class="panel panel-default panel-col">
        <div class="panel-heading">我的笔记</div>
        <div class="panel-body">
            <ul class="media-list notebook-list" id="notebook-list">
                <#list courseMembers! as member>
                    <#assign course = courses[member.courseId] />
                    <div class="media">
                        <img class="pull-left media-object"
                             src="${default_path('coursePicture', course.largePicture, 'large')}">
                        <div class="media-body">
                            <h4 class="media-heading">${course.title}</h4>
                            <div class="notebook-metas">
                                <span class="notebook-number">共 ${member.noteNum} 篇笔记</span>
                            </div>
                            <div class="notebook-metas">
                                <#if member.noteLastUpdateTime gt 0>
                                    <span class="notebook-time">最后更新 ${member.noteLastUpdateTime?number_to_datetime?string('yyyy-MM-dd HH:mm:ss')}</span>
                                </#if>
                                <a class="pull-right notebook-go" href="${ctx}/my/notebook/${course.id}">查看笔记</a>
                            </div>
                        </div>
                    </div>
                <#else>
                    <li class="empty">你还没有写过笔记</li>
                </#list>
            </ul>
            <@web_macro.paginator paginator!/>
        </div>
    </div>
</#macro>