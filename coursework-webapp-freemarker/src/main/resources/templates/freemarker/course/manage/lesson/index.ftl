<#assign script_controller = 'course-manage/lesson'/>
<#assign side_nav = 'lesson'/>

<#include '/course/manage/layout.ftl'/>
<#macro blockTitle>课时管理 - ${blockTitleParent}</#macro>

<#macro blockMain>
<div class="panel panel-default panel-col lesson-manage-panel">
    <div class="panel-heading">
        <div class="pull-right">

            <button class="btn btn-info btn-sm" id="lesson-create-btn" data-toggle="modal" data-target="#modal"
                    data-backdrop="static" data-keyboard="false"
                    data-url="<#if course.type == 'normal'>${ctx}/course/${course.id}/manage/lesson/create<#else>${ctx}/course/${course.id}/manage/live/lesson/create</#if>">
                <i class="glyphicon glyphicon-plus"></i> <#if course.type == 'normal'>课时<#else>直播课时</#if></button>

            <#if course.type ?? && course.type== 'normal' && course.type ??  && course.type== 'live'>
                <button class="btn btn-info btn-sm" id="lesson-create-btn" data-toggle="modal" data-target="#modal"
                        data-backdrop="static" data-keyboard="false"
                        data-url="${ctx}/course/${course.id}/manage/lesson/create/testpaper"><i
                            class="glyphicon glyphicon-plus"></i> 试卷
                </button>
            </#if>

            <div class="btn-group">
                <button type="button" class="btn btn-sm btn-success dropdown-toggle" data-toggle="dropdown">
                    <i class="glyphicon glyphicon-plus"></i> ${setting('default.chapter_name')!'章'}${setting('default.part_name')!'节'}
                    <span class="caret"></span>
                </button>
                <ul class="dropdown-menu" role="menu">
                    <li>
                        <a href="#" id="chapter-create-btn" data-toggle="modal" data-target="#modal"
                           data-backdrop="static" data-keyboard="false"
                           data-url="${ctx}/course/${course.id}/manage/chapter/create"><i
                                    class="glyphicon glyphicon-plus"></i> 添加 ${setting('default.chapter_name'!)!'章'}</a>
                    </li>
                    <li>
                        <a href="#" id="chapter-create-btn" data-toggle="modal" data-target="#modal"
                           data-backdrop="static" data-keyboard="false"
                           data-url="${ctx}/course/${course.id}/manage/chapter/create?type=unit"><i
                                    class="glyphicon glyphicon-plus"></i> 添加 ${setting('default.part_name')!'节'}</a>
                    </li>
                </ul>
            </div>
        </div>

        课时管理
    </div>

    <#if items??>

    <div class="empty">暂无课时内容！</div>

    </#if>

    <div class="panel-body">
        <ul class="lesson-list sortable-list" id="course-item-list" data-sort-url="${ctx}/course/${course.id}/manage/lesson/sort">

            <#list items! as item,id>
            {% if 'chapter' in id %}
            {% include 'TopxiaWebBundle:CourseChapterManage:list-item.html.twig' with {chapter: item} %}
            {% elseif 'lesson' in id %}
            {% include 'TopxiaWebBundle:CourseLessonManage:list-item.html.twig' with {lesson: item, file: files[item.mediaId]|default(null)} %}
            {% endif %}
            </#list>

        </ul>
    </div>
</div>

</#macro>
