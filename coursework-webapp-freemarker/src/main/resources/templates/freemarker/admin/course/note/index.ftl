<#assign menu = 'note'/>
<#assign script_controller = 'course/note'/>

<#include '/admin/course/layout.ftl'/>
<#macro blockTitle>笔记管理 - ${blockTitleParent}</#macro>

<#macro blockMain>

<div class="page-header clearfix">
    <h1 class="pull-left">笔记管理</h1>
</div>

<div class="well well-sm">
    <form class="form-inline">

        <div class="form-group">
            <select class="form-control" name="keywordType">
                <@select_options {'content':'内容', 'courseId':'课程编号', 'courseTitle':'课程名' }, RequestParameters['keywordType']/>
            </select>
        </div>

        <div class="form-group">
            <input class="form-control" type="text" name="keyword" value="${RequestParameters['keyword']! }" placeholder="关键词">
        </div>

        <span class="divider"></span>

        <div class="form-group">
            <input class="form-control" type="text" name="author" value="${RequestParameters['author']! }" placeholder="作者昵称">
        </div>

        <button class="btn btn-primary" type="submit">搜索</button>
    </form>
</div>

<div id="note-table-container">

    <table class="table table-striped table-hover" id="note-table">

        <thead>
        <tr>
            <th width="3%"><input type="checkbox" data-role="batch-select"></th>
            <th width="75%">内容</th>
            <th width="15%">作者</th>
            <th width="7%">操作</th>
        </tr>
        </thead>

        <tbody>

        <#list notes! as note >
            <#assign course = courses['' + note.courseId]! />
            <#assign lesson = lessons['' + note.lessonId]! />
            <tr data-role="item">
                <td><input value="${note.id}" type="checkbox" data-role="batch-item"></td>
                <td>
                    <div class="short-long-text">
                        <div class="short-text">${fastLib.plainText(note.content, 100)} <span
                                    class="trigger">(展开)</span></div>
                        <div class="long-text">${note.content} <span class="trigger">(收起)</span></div>
                    </div>

                    <div class="text-sm mts">
                        <#if course??>
                            <a href="${ctx}/course/${course.id}" class="text-success"
                               target="_blank">${course.title}</a>
                            <#if lesson??>
                                <span class="text-muted mhs">&raquo;</span>
                                <a class="text-success"
                                   href="${ctx}/course/${lesson.courseId}learn#lesson/${lesson.id}}"
                                   target="_blank">课时${lesson.number}：${lesson.title}</a>

                            </#if>
                        </#if>
                    </div>
                </td>
                <td>
                    <@admin_macro.user_link users['' + note.userId] />
                    <br>
                    <span class="text-muted text-sm">${note.createdTime?number_to_datetime?string('yyyy-MM-dd HH:mm')}</span>
                </td>
                <td>
                    <button class="btn btn-default btn-sm" data-role="item-delete" data-name="笔记"
                            data-url="${ctx}/admin/course/note/${note.id}/delete">删除
                    </button>
                </td>
            </tr>
        <#else>
            <tr>
                <td colspan="20">
                    <div class="empty">暂无笔记记录</div>
                </td>
            </tr>
        </#list>
        </tbody>
    </table>

    <div>
        <label class="checkbox-inline"><input type="checkbox" data-role="batch-select"> 全选</label>
        <button class="btn btn-default btn-sm mlm" data-role="batch-delete" data-name="笔记"
                data-url="${ctx}/admin/course/note/batch_delete">删除
        </button>
    </div>

</div>

    <div>
        <@web_macro.paginator paginator!/>
    </div>
</#macro>