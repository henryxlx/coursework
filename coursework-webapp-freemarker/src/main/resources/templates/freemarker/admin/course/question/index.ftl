<#assign menu = 'question'/>
<#assign script_controller = 'course/questions'/>

<@block_title '问答管理'/>

<#include '/admin/course/layout.ftl'/>

<#macro blockMain>
    <div class="page-header clearfix">
        <h1 class="pull-left">问答管理</h1>
    </div>

    <#include '/admin/course/question/tab.ftl'/>
    <br>

<div class="well well-sm">
    <form class="form-inline">

        <div class="form-group">
            <select class="form-control" name="keywordType">
                <@select_options {'title':'标题', 'content': '内容', 'courseId':'课程编号', 'courseTitle':'课程名' }, RequestParameters['keywordType']!'', ''/>
            </select>
        </div>

        <div class="form-group">
            <input class="form-control" type="text" placeholder="关键词" name="keyword" value="${RequestParameters['keyword']!}">
        </div>

        <span class="divider"></span>

        <div class="form-group">
            <input class="form-control" type="text" placeholder="作者昵称" name="author" value="${RequestParameters['author']!}">
        </div>


        <button class="btn btn-primary" type="submit">搜索</button>
    </form>
</div>

<div id="question-table-container">
    <table id="question-table" class="table table-striped table-hover">
        <thead>
        <tr>
            <th width="5%"><input type="checkbox" data-role="batch-select"></th>
            <th width="50%">问答</th>
            <#if type?? && type == 'unPosted'>
            <th width="10%">查看</th>
            <#elseif type?? && type == 'all'>
            <th width="10%">回复/查看</th>
            </#if>
            <th width="10%">作者</th>
            <th width="10%">创建时间</th>
            <th width="15%">操作</th>
        </tr>
        </thead>
        <tbody class="tbody">
        <#list questions! as question>

            <#assign author = users[''+question.userId]! />
            <#assign course = courses[''+question.courseId]! />
            <#assign lesson = lessons[''+question.lessonId]! />
            <tr data-role="item">
                <td><input value="${question.id}" type="checkbox" data-role="batch-item"></td>
                <td>
                    <a href="${ctx}/course/${question.courseId}/thread/${question.id}"
                       target="_blank"><strong>${question.title}</strong></a>

                    <div class="short-long-text">
                        <div class="short-text text-sm text-muted">${fastLib.plainText(question.content, 60)} <span
                                    class="trigger">(展开)</span></div>
                        <div class="long-text">${question.content} <span class="trigger">(收起)</span></div>
                    </div>

                    <div class="text-sm mts">
                        <#if course?? && course?size gt 0>
                            <a href="${ctx}/course/${course.id}" class="text-success"
                               target="_blank">${course.title}</a>
                            <#if lesson?? && lesson?size gt 0>
                                <span class="text-muted mhs">&raquo;</span>
                                <a class="text-success"
                                   href="${ctx}/course/${lesson.courseId}/learn#lesson/${lesson.id}"
                                   target="_blank">课时${lesson.number}：${lesson.title}</a>
                            </#if>
                        </#if>
                    </div>
                </td>
                <td>
            <span class="text-sm">
              <#if type == 'unPosted'>
                  ${question.hitNum}
              <#elseif type == 'all'>
                  ${question.postNum} / ${question.hitNum}
              </#if>
            </span>
                </td>
                <td>
                    <@admin_macro.user_link author /> <br/>
                </td>
                <td>
                    ${question.createdTime?number_to_datetime?string('yyyy-MM-dd HH:mm:ss')}
                </td>
                <td>
                    <#include '/admin/course/question/td-operations.ftl' />
                </td>
            </tr>
        <#else>
            <tr>
                <td colspan="20">
                    <div class="empty">暂无问答记录</div>
                </td>
            </tr>
        </#list>
        </tbody>
    </table>

    <div class="mbm">
        <label class="checkbox-inline"><input type="checkbox" data-role="batch-select"> 全选</label>
        <button class="btn btn-default btn-sm mlm" data-role="batch-delete" data-name="问答"
                data-url="${ctx}/admin/course/thread/batch_delete">删除
        </button>
    </div>
</div>

    <@web_macro.paginator paginator!/>
</#macro>
