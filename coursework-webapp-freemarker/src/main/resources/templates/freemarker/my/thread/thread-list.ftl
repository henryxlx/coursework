<ul class="media-list">
    <#list threads! as thread>
        <#if courses??>
            <#assign course = courses['' + thread.courseId]! />
            <li class="media">
                <div class="media-body">
                    <#if thread.postNum gt 0>
                        <span class="badge pull-right" style="margin-top:15px;">${thread.postNum}</span>
                    </#if>
                    <div class="mbm">
                        <#if thread.type == 'question'>
                            <span class="label label-info" title="问答帖">问</span>
                        </#if>

                        <a href="${ctx}/course/${thread.courseId}/thread/${thread.id}"><strong>${thread.title}</strong></a>

                        <#if thread.lessonId??>
                            <span class="label" title="课时${thread.lessonId}">L${thread.lessonId}</span>
                        </#if>

                        <#if thread.isElite?? && thread.isElite == 1>
                            <span class="label label-warning" title="精华帖">精</span>
                        </#if>

                    </div>
                    <div class="text-muted text-normal">
                        <#if course??>
                            <span>发表于<a href="${ctx}/course/${course.id}"
                                        class="link-muted">${course.title}</a></span>
                        <#else>
                            <span>课程已删除</span>
                        </#if>
                        <span class="bullet">•</span>
                        <#if thread.postNum gt 0 >
                            <#assign poster = users['' + thread.latestPostUserId] />
                            <span>最后回复 <@web_macro.user_link poster  'link-muted'/></span>
                            ,
                            <span>${fastLib.smartTime(thread.latestPostTime)}</span>
                            <span class="bullet">•</span>
                        </#if>
                        <span>${thread.hitNum}浏览</span>
                    </div>
                </div>
            </li>
        <#else>
            <#if type == 'question'>
                <li class="empty">你还没提过问题</li>
            <#else>
                <li class="empty">你还没发表过话题</li>
            </#if>

        </#if>

    </#list>

</ul>

<@web_macro.paginator paginator! />