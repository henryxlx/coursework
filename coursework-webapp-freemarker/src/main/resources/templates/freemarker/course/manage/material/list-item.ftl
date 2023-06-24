<li class="list-group-item">
    <#if material.link??>
        <a href="${material.link}" target="_blank">${material.title}</a>
        <span class="glyphicon glyphicon-new-window text-muted text-sm" title="网络链接资料"></span>
    <#else>
        <a href="${ctx}/course/${material.courseId}/material/${material.id}/download">${material.title}</a>
    </#if>
    <button class="close delete-btn" type="button" title="删除"
            data-url="${ctx}/course/${material.courseId}/manage/lesson/${material.lessonId}/material/${material.id}/delete">
        &times;
    </button>
</li>