<#assign menu = 'announcement'/>
<#assign script_controller = 'course/announcement'/>

<@block_title '公告管理'/>

<#include '/admin/course/layout.ftl'/>

<#macro blockMain>

    <div class="page-header clearfix">
        <h1 class="pull-left">公告管理</h1>
    </div>

    <div class="well well-sm">
        <form class="form-inline">

            <div class="form-group">
                <select class="form-control" name="keywordType">
                    <@select_options {'content':'内容', 'courseId':'课程编号', 'courseTitle':'课程名' }, RequestParameters['keywordType']!'', ''/>
                </select>
            </div>

            <div class="form-group">
                <input class="form-control" type="text" name="keyword" value="${RequestParameters['keyword']! }"
                       placeholder="关键词">
            </div>

            <span class="divider"></span>

            <div class="form-group">
                <input class="form-control" type="text" name="author" value="${RequestParameters['author']! }"
                       placeholder="作者昵称">
            </div>

            <button class="btn btn-primary" type="submit">搜索</button>
        </form>
    </div>

    <div id="announcement-table-container">

        <table class="table table-striped table-hover" id="announcement-table">

            <thead>
            <tr>
                <th width="3%"><input type="checkbox" data-role="batch-select"></th>
                <th width="75%">内容</th>
                <th width="15%">作者</th>
                <th width="7%">操作</th>
            </tr>
            </thead>

            <tbody>

            <#list announcements! as announcement >
                <#assign course = courses['' + announcement.courseId]! />
                <tr data-role="item">
                    <td><input value="${announcement.id}" type="checkbox" data-role="batch-item"></td>
                    <td>
                        <div class="short-long-text">
                            <div class="short-text">${fastLib.plainText(announcement.content, 100)} <span
                                        class="trigger">(展开)</span></div>
                            <div class="long-text">${announcement.content} <span class="trigger">(收起)</span></div>
                        </div>

                        <div class="text-sm mts">
                            <#if course?? && course?size gt 0>
                                <a href="${ctx}/course/${course.id}" class="text-success"
                                   target="_blank">${course.title}</a>
                            </#if>
                        </div>
                    </td>
                    <td>
                        <@admin_macro.user_link users['' + announcement.userId] />
                        <br>
                        <span class="text-muted text-sm">${announcement.createdTime?number_to_datetime?string('yyyy-MM-dd HH:mm')}</span>
                    </td>
                    <td>
                        <button class="btn btn-default btn-sm" data-role="item-delete" data-name="公告"
                                data-url="${ctx}/admin/course/announcement/${announcement.id}/delete">删除
                        </button>
                    </td>
                </tr>
            <#else>
                <tr>
                    <td colspan="20">
                        <div class="empty">暂无课程公告记录</div>
                    </td>
                </tr>
            </#list>
            </tbody>
        </table>

        <div>
            <label class="checkbox-inline"><input type="checkbox" data-role="batch-select"> 全选</label>
            <button class="btn btn-default btn-sm mlm" data-role="batch-delete" data-name="公告"
                    data-url="${ctx}/admin/course/announcement/batch_delete">删除
            </button>
        </div>

    </div>

    <div>
        <@web_macro.paginator paginator!/>
    </div>
</#macro>