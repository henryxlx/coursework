<li class="media thread-item" data-id="${thread.id}">
    <div class="media-body thread-item-body">
        <div class="title"><a class="show-question-item" target="_blank" data-id="${thread.id}">${thread.title}</a>
        </div>
        <div class="metas">
            <@web_macro.user_link user! /> 发表于 ${thread.createdTime?number_to_datetime?string('yyyy-MM-dd HH:mm:ss')}
            <span class="bullet">•</span>
            <a href="#">${thread.postNum}回答</a>
        </div>
    </div>
</li>