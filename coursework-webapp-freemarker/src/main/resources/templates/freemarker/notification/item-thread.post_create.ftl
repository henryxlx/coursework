<li class="media">
    <div class="pull-left">
        <span class="glyphicon glyphicon-volume-down media-object"></span>
    </div>
    <div class="media-body">
        <div class="notification-body">
            <#assign data = notification.content?eval/>
            <a href="${ctx}/user/${data.user.id}" target="_blank">${data.user.nickname}</a> 在话题 <a
                    href="${ctx}/thread/${data.thread.id}/post/${data.id}/jump" target="_blank">${data.thread.title}</a>
            回复了你。
            <blockquote>
                ${data.content}
            </blockquote>
        </div>
        <div class="notification-footer">
            ${notification.createdTime?number_to_datetime?string('yyyy-MM-dd HH:mm:ss')}
        </div>
    </div>
</li>