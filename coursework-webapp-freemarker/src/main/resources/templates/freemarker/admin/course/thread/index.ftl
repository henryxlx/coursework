<#assign menu = 'thread'/>
<#assign script_controller = 'course/threads'/>

<#include '/admin/course/layout.ftl'/>
<#macro blockTitle>讨论区管理 - ${blockTitleParent}</#macro>

<#macro blockMain>
    <div class="page-header clearfix">
        <h1 class="pull-left">讨论区管理</h1>
    </div>

    <div class="well well-sm">
        <form class="form-inline">
            <div class="form-group">
                <select class="form-control" name="type">
                    <@select_options dict['threadType']!{},  RequestParameters['type']!'' '帖子类型'/>
                </select>
            </div>

            <span class="divider"></span>

            <div class="form-group">
                <select class="form-control" name="threadType">
                    <@select_options {'isStick':'置顶', 'isElite':'加精'}, RequestParameters['threadType']!'' '属性' />
                </select>
            </div>

            <span class="divider"></span>

            <div class="form-group">
                <select class="form-control" name="keywordType">
                    <@select_options {'title':'标题', 'content':'内容', 'courseId':'课程编号', 'courseTitle':'课程名'}, RequestParameters['keywordType']!'', ''/>
                </select>
            </div>

            <div class="form-group">
                <input class="form-control" type="text" placeholder="关键词" name="keyword"
                       value="${RequestParameters['keyword']!}">
            </div>

            <div class="form-group">
                <input class="form-control" type="text" placeholder="作者昵称" name="author"
                       value="${RequestParameters['author']!}">
            </div>

            <button class="btn btn-primary" type="submit">搜索</button>
        </form>
    </div>

    <div id="thread-table-container">
        <table class="table table-striped table-hover">
            <thead>
            <tr>
                <th width="5%"><input type="checkbox" data-role="batch-select"></th>
                <th width="50%">帖子</th>
                <th width="10%">回复/查看</th>
                <th width="10%">属性</th>
                <th width="15%">作者</th>
                <th width="10%">操作</th>
            </tr>
            </thead>
            <body>
            <#list threads! as thread>
                <#assign author = users[''+thread.userId]!{} />
                <#assign course = courses[''+thread.courseId]!{} />
                <#assign lesson = lessons[''+thread.lessonId]!{} />
                <tr data-role="item">
                    <td><input value="${thread.id}" type="checkbox" data-role="batch-item"></td>
                    <td>
                        <#if thread.type == 'question'!>
                            <span class="label label-info">问</span>
                        </#if>

                        <a href="${ctx}/course/${thread.courseId}/thread/${thread.id}"
                           target="_blank"><strong>${thread.title}</strong></a>

                        <div class="short-long-text">
                            <div class="short-text text-sm text-muted">${fastLib.plainText(thread.content, 60)} <span
                                        class="trigger">(展开)</span></div>
                            <div class="long-text">${thread.content} <span class="trigger">(收起)</span></div>
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
                    <td><span class="text-sm">${thread.postNum} / ${thread.hitNum}</span></td>
                    <td>
                        <#if course?? && course?size gt 0>
                            <a href="javascript:;" data-set-url="${ctx}/course/${course.id}/thread/${thread.id}/elite"
                               data-cancel-url="${ctx}/course/${course.id}/thread/${thread.id}/unelite"
                               class="promoted-label">
                                <span class="label <#if thread.isElite??>label-success<#else>label-default</#if>">精</span>
                            </a>

                            <a href="javascript:;" data-set-url="${ctx}/course/${course.id}/thread/${thread.id}/stick"
                               data-cancel-url="${ctx}/course/${course.id}/thread/${thread.id}/unstick"
                               class="promoted-label">
                                <span class="label <#if thread.isStick??>label-success<#else>label-default</#if>">顶</span>
                            </a>
                        </#if>
                    </td>
                    <td>
                        <@admin_macro.user_link author /> <br/>
                        <span class="text-muted text-sm">${thread.createdTime?number_to_datetime?string('yyyy-MM-dd HH:mm:ss')}</span>
                    </td>
                    <td>
                        <div class="btn-group">
                            <a href="javascript:;" data-role="item-delete"
                               data-url="${ctx}/admin/course/thread/{thread.id}/delete" class="btn btn-default btn-sm"
                               data-name="帖子">删除</a>
                        </div>
                    </td>
                </tr>
            <#else>
                <tr>
                    <td colspan="20">
                        <div class="empty">暂无帖子记录</div>
                    </td>
                </tr>
            </#list>
            </body>
        </table>

        <div class="mbm">
            <label class="checkbox-inline"><input type="checkbox" data-role="batch-select"> 全选</label>
            <button class="btn btn-default btn-sm mlm" data-role="batch-delete" data-name="帖子"
                    data-url="${ctx}/admin/course/thread/thread/batch_delete">删除
            </button>
        </div>

    </div>

    <@web_macro.paginator paginator!/>
</#macro>
