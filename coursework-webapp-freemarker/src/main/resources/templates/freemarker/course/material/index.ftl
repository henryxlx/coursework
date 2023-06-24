<#assign script_controller = 'course/common' />

<#include '/course/dashboard-layout.ftl' />

<#macro blockDashboardMain>

    <#assign nav = 'material' />
    <#include '/course/dashboard-nav.ftl' />

    <ul class="media-list">
        <#list materials! as material>
            <#assign lesson = lessons['' + material.lessonId]/>
            <li class="media">
                <div class="media-body">
                    <div class="mbs">
                        <#if lesson?? && lesson.status != 'published'>
                            ${material.title} <span class="text-muted text-sm">(课时未发布，不能下载该资料)</span>
                        <#else>
                            <#if material.link??>
                                <a href="${material.link}" target="_blank">${material.title}</a>
                                <span class="glyphicon glyphicon-new-window text-muted text-sm" title="网络链接资料"></span>
                            <#else>
                                <a href="${ctx}/course/${course.id}/material/${material.id}/download"
                                   target="_blank">${material.title}</a>
                            </#if>
                        </#if>
                    </div>

                    <#if material.description?? && !material.link??>
                        <div class="text-muted text-sm mbs">
                            ${fastLib.plainText(material.description, 100)}
                        </div>
                    </#if>

                    <div class="text-sm">
                        <#if material.fileId gt 0>
                            <span class="text-muted">${fastLib.fileSize(material.fileSize)}</span>
                            <span class="bullet">•</span>
                        </#if>
                        <#if lesson??>
                            <a class="link-muted" href="${ctx}/course/${course.id}/learn#lesson/${lesson.id}"
                               title="${lesson.title}">课时${lesson.number}</a>
                            <span class="bullet">•</span>
                        </#if>
                        <span class="text-muted">上传于${fastLib.smartTime(material.createdTime)}</span>
                    </div>

                </div>
            </li>
        <#else>
            <li class="empty tac text-muted mvl">课程暂无资料</li>
        </#list>
    </ul>
</#macro>