<li id="post-${post.id}" class="thread-post media clearfix">
    <@web_macro.user_avatar author 'pull-left media-object'/>

    <#if isManager?? || appUser.id == author.id>
        <div class="thread-post-dropdown">
            <a href="javascript:" class="dropdown-toggle text-muted" data-toggle="dropdown"><i
                        class="glyphicon glyphicon-collapse-down"></i></a>
            <ul class="dropdown-menu pull-right">
                <li><a href="${ctx}/course/${course.id}/thread/${thread.id}/post/${post.id}/edit"><i
                                class="glyphicon glyphicon-edit"></i> 编辑</a></li>
                <#if isManager>
                    <li><a href="javascript:" data-action="post-delete"
                           data-url="${ctx}/course/${course.id}/thread/${thread.id}/post/${post.id}/delete"
                           data-for="#post-${post.id}"><i class="glyphicon glyphicon-remove"></i> 删除</a></li>
                </#if>
            </ul>
        </div>
    </#if>

    <div class="thread-post-body media-body" style="word-break:break-all;">
        <div class="media-heading">
            <a href="javascript:">${author.nickname}</a>
            <span class="bullet">•</span>
            <span class="text-muted">${fastLib.smartTime(post.createdTime)}</span>


        </div>
        <div class="thread-post-content">${post.content}</div>
        <#if appUser.id != author.id>
            <div class="thread-post-action" data-user="${author.username}"><a href="#thread-post-form">回复</a></div>
        </#if>
    </div>
</li>