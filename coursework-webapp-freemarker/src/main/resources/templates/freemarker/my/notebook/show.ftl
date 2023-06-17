<#assign side_nav = 'notes'/>
<#assign script_controller = 'my/notebook-show'/>
<#include '/my/layout.ftl'/>

<#macro blockTitle>我的课程 - ${blockTitleParent}</#macro>

<#macro blockMain>
    <style>
        .notebook-body img {
            width: 100%;
        }
    </style>
    <div class="panel panel-default panel-col">
        <div class="panel-heading">我的笔记</div>
        <div class="panel-body">
            <div class="notebook" id="notebook">
                <div class="notebook-heading">
                    <a class="btn btn-default btn-sm pull-right notebook-back-btn" href="${ctx}/my/notebooks">返回</a>
                    <img class="notebook-icon" src="${default_path('coursePicture',course.largePicture, 'large')}"/>
                    《${course.title}》的笔记
                </div>
                <div class="notebook-body">
                    <#list notes as note>
                        <#assign lesson = lessons['' + note.lessonId]! />
                        <div class="notebook-note notebook-note-collapsed">
                            <div class="notebook-note-heading">
                                <#if lesson??>
                                    <a href="${ctx}/course/${note.courseId}/learn#lesson/${lesson.id}" target="_blank">课时${lesson.number}
                                        ：${lesson.title}</a>
                                <#else>
                                    <span>该课时已删除</span>
                                </#if>
                                <span class="pull-right notebook-note-length">共${note.length}字</span>
                            </div>
                            <div class="notebook-note-summary">${fastLib.plainText(note.content, 97)}</div>
                            <div class="notebook-note-body">
                                ${note.content}

                                <div class="notebook-note-actions clearfix">
                                    <a href="javascript:" data-url="${ctx}/my/note/${note.id}/delete"
                                       class="pull-right notebook-note-delete" title="删除笔记"><span
                                                class="glyphicon glyphicon-trash"></span></a>
                                </div>
                                <div class="notebook-note-collapse-bar"><span
                                            class="glyphicon glyphicon-chevron-up"></span></div>
                            </div>
                        </div>
                    </#list>
                </div>
            </div>
        </div>
    </div>
</#macro>