<li class="media">
    <div class="pull-left">
        <span class="glyphicon glyphicon-volume-down media-object"></span>
    </div>
    <div class="media-body">
        <div class="notification-body">
            <#assign data = notification.content?eval/>
            <a href="{{ path('user_show', {id:data.user.id}) }}" target="_blank">{{ data.user.nickname }}</a> 在话题 <a
                    href="{{ path('thread_jump', {threadId:data.id}) }}" target="_blank">{{ data.title }}</a> 中提到了你。
            <blockquote>
                {{ data.content }}
            </blockquote>
        </div>
        <div class="notification-footer">
            ${notification.createdTime?number_to_datetime?string('yyyy-MM-dd HH:mm:ss')}
        </div>
    </div>
</li>