<li class="media">
    <div class="pull-left">
        <span class="glyphicon glyphicon-volume-down media-object"></span>
    </div>
    <div class="media-body">
        <div class="notification-body">
            <#assign data = notification.content?eval_json/>
            您的<#if data.threadType == 'question'>问题<#else>话题</#if>
            <a href="${ctx}/course/${data.courseId}/thread/${data.threadId}#post-${data.postId}"
               target="_blank"><strong>${data.threadTitle}</strong></a> 有了<strong>${data.postUserNickname}</strong>的新回复。
        </div>
        <div class="notification-footer">
            ${notification.createdTime?number_to_datetime?string('yyyy-MM-dd HH:mm:ss')}
        </div>
    </div>
</li>