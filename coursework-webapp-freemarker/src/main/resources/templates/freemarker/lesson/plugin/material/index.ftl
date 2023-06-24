<#if lesson.type != 'live' >
    <h5>课时简介</h5>
    <div class="lesson-about">
        <#if lesson.summary??>
            ${lesson.summary}
        <#else>
            此课时无简介。
        </#if>
    </div>
</#if>

<#if ['video', 'audio']?seq_contains(lesson.type) && setting('course.student_download_media', '0') != '0' && file??>
    <h5>课时音视频文件下载</h5>
    <div class="lesson-about">
        <a href="${ctx}/course/${lesson.courseId}/lesson/${lesson.id}/media_download">${fastLib.fileSize(file.size)}
            (点击下载)</a>
    </div>
</#if>


<h5>课时资料</h5>

<#if lesson.status == 'published'>
    <ul class="media-list toolbar-pane-list">
        <#list materials! as material>
            <li class="media list-item">
                <div class="media-body list-item-body">
                    <#if material.link?? && material.link != ''>
                        <div class="title">
                            <a href="${material.link}" target="_blank">${material.title}</a>
                            <span class="glyphicon glyphicon-new-window text-muted text-sm" title="网络链接资料"></span>
                        </div>
                    <#else>
                        <div class="title">
                            <a href="${ctx}/course/${course.id}/material/${material.id}/download"
                               target="_blank">${material.title}</a>
                            <span class="meta">(${fastLib.fileSize(material.fileSize)})</span>
                        </div>
                        <div class="summary">${material.description}</div>
                    </#if>
                </div>
            </li>
        <#else>
            <li class="empty-item" style="text-align:left;margin-left:10px;">此课时无资料</li>
        </#list>
    </ul>
<#else>
    <div class="lesson-about">此课时尚未发布，不能下载课时资料</div>
</#if>