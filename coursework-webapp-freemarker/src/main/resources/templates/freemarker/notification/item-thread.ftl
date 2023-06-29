<li class="media">
    <div class="pull-left">
        <span class="glyphicon glyphicon-volume-down media-object"></span>
    </div>
    <div class="media-body">
        <div class="notification-body">
            <#assign data = notification.content?eval_json/>
            <a href="${ctx}/user/${data.threadUserId}" target="_blank">${data.threadUserNickname}</a>
            在课程 <a href="${ctx}/course/${data.courseId}">${data.courseTitle}</a>
            发表了<#if data.threadType == 'question'>问题<#else>话题</#if>
            <a href="${ctx}/course/${data.courseId}/thread/${data.threadId}" target="_blank">${data.threadTitle}</a>。
        </div>
        <div class="notification-footer">
            ${notification.createdTime?number_to_datetime?string('yyyy-MM-dd HH:mm:ss')}
        </div>
    </div>
</li>