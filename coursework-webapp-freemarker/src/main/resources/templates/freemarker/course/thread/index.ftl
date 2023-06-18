<#assign script_controller = 'course/common' />

<#include '/course/dashboard-layout.ftl' />

<#macro blockDashboardMain>

    <#assign nav = 'thread' />
    <#include '/course/dashboard-nav.ftl' />

    <div class="pull-right">
        <a class="btn btn-sm btn-info"
           href="${ctx}/course/${course.id}/thread/create?type=discussion">发话题</a>
        <a class="btn btn-sm btn-info"
           href="${ctx}/course/${course.id}/thread/create?type=question">提问题</a>
    </div>

    <div class="thread-filters clearfix">
        <ul class="nav nav-pills nav-mini pull-left">
            <#assign threadFilterQueryStr = ''/>
            <#list filters!as k, v>
                <#assign threadFilterQueryStr = threadFilterQueryStr + '&' + k + '=' + v />
            </#list>
            <li
                    <#if filters.type == 'all'> class="active"</#if>><a class="js-nav" data-target="#thread-pane"
                                                                        href="${ctx}/course/${course.id}/thread?type=all${threadFilterQueryStr}">全部</a>
            </li>
            <li
                    <#if filters.type == 'question'> class="active"</#if>><a class="js-nav" data-target="#thread-pane"
                                                                             href="${ctx}/course/${course.id}/thread?type=question${threadFilterQueryStr}">问答</a>
            </li>
            <li
                    <#if filters.type == 'elite'> class="active"</#if>><a class="js-nav" data-target="#thread-pane"
                                                                          href="${ctx}/course/${course.id}/thread?type=elite${threadFilterQueryStr}">精华</a>
            </li>

            <li><span class="mhm text-muted">|</span></li>

            <li class="dropdown">
                <a class="dropdown-toggle" data-toggle="dropdown" href="#">
                    <span class="text-muted">排序：</span>
                    <#if filters.sort == 'posted'>
                        最后回复
                    <#elseif filters.sort == 'created'>
                        最新发帖
                    </#if>
                    <span class="caret"></span>
                </a>
                <ul class="dropdown-menu">
                    <li class="<#if filters.sort == 'posted'>active</#if>"><a class="js-nav"
                                                                              href="${ctx}/course/${course.id}/thread?sort=posted${threadFilterQueryStr}">最后回复</a>
                    </li>
                    <li class="<#if filters.sort == 'created'>active</#if>"><a class="js-nav"
                                                                               href="${ctx}/course/${course.id}/thread?sort=created${threadFilterQueryStr}">最新发帖</a>
                    </li>
                </ul>
            </li>


        </ul>


    </div>

    <ul class="media-list">
        <#list threads! as thread>
            <#assign author = users['' + thread.userId] />
            <li class="media">
                <@web_macro.user_avatar author 'pull-left media-object media-object-small'/>
                <div class="media-body">
                    <#if thread.postNum gt 0>
                        <span class="badge pull-right" style="margin-top:15px;">${thread.postNum}</span>
                    </#if>
                    <div class="mbm">
                        <#if thread.isStick?? && thread.isStick == 1>
                            <span class="label label-danger" title="置顶帖">置顶</span>
                        </#if>
                        <#if thread.type == 'question'>
                            <span class="label label-info" title="问答帖">问</span>
                        </#if>
                        <a class="js-nav"
                           href="${ctx}/course/${course.id}/thread/${thread.id}"><strong>${thread.title}</strong></a>

                        <#if thread.isElite?? && thread.isElite == 1>
                            <span class="label label-warning" title="精华帖">精</span>
                        </#if>

                    </div>

                    <div class="text-muted text-normal">
                        by <@web_macro.user_link author 'link-muted'/>

                        <#if thread.postNum gt 0>
                            <#assign poster = users['' + thread.latestPostUserId] >
                            <span class="bullet">•</span>
                            <span>最后回复 <@web_macro.user_link poster 'link-muted' /></span>
                        </#if>
                        <span class="bullet">•</span>
                        <span>${thread.latestPostTime?number_to_datetime?string('yyyy-MM-dd HH:mm')}</span>
                        <span class="bullet">•</span>
                        <span>${thread.hitNum}浏览</span>
                        <#if thread.lessonId gt 0>
                            <#assign lesson = lessons[thread.lessonId]!'null'/>
                        </#if>
                        <#if lesson?? && lesson != 'null'>
                            <span class="bullet">•</span>
                            <a class="link-muted"
                               href="${ctx}/course/${thread.courseId}/learn#lesson/${thread.lessonId}"
                               title="${lesson.title}">课时${lesson.number}</a>
                        </#if>

                    </div>
                </div>
            </li>
        <#else>
            <li class="mvl tac text-muted">课程暂无话题</li>
        </#list>
    </ul>

    <@web_macro.paginator paginator! />



</#macro>