<#assign modal_class = 'modal-lg'/>

<#include '/bootstrap-modal-layout.ftl' />

<#macro blockTitle><span class="text-muted">免费课时预览:</span> ${lesson.title}</#macro>

<#macro blockBody>
    <#if lesson.status == 'published'>

        <#if lesson.mediaSource == 'iframe'>
            <div id="lesson-preview-iframe" data-url="${lesson.mediaUri}" style="display:none;">
            </div>
        <#elseif lesson.type == 'video'>
            <#if lesson.mediaSource == 'self'>
                <div id="lesson-preview-video-player" data-url="${ctx}/course/${course.id}/lesson/${lesson.id}/media"
                     data-hls-url="${hlsUrl!}"
                        <#if setting('storage.video_watermark', '0') == '1' && setting('storage.video_watermark_image', '0') != '0'>
                            data-watermark="${ctx}/${setting('storage.video_watermark_image', '')}"
                        </#if>
                        <#if setting('storage.video_fingerprint', '0') == '1' && appUser??>
                            data-fingerprint="${ctx}/cloud_video_fingerprint?userId=${appUser.id}"
                        </#if>

                >
                </div>
                <input type="hidden" id="videoWatermarkEmbedded" value="${hasVideoWatermarkEmbedded}">
            <#else>
                <div id="lesson-preview-swf-player" data-url="${lesson.mediaUri}"></div>
            </#if>
        <#elseif lesson.type == 'audio'>
            <audio id="lesson-preview-audio-player" width="100%" height="100%">
                <source src="${ctx}/course/${course.id}/lesson/${lesson.id}/media" type='audio/mp3'/>
            </audio>
        <#elseif lesson.type == 'ppt'>
            <div id="lesson-preview-ppt-player" data-url="${ctx}/course/${course.id}/lesson/${lesson.id}/ppt"
                 class="lesson-preview-ppt"></div>

        <#else>
            <div <#if setting('course.copy_enabled', '') != '0'> oncopy="return false;" oncut="return false;" onselectstart="return false" oncontextmenu="return false;"</#if>>
                ${lesson.content}
            </div>
        </#if>
    <#else>
        <div class="empty">课时尚未发布，无法预览！</div>
    </#if>
    <script>app.load('course/lesson-preview')</script>
</#macro>

<#macro blockFooter>
    <#if setting('classroom.enabled', '0') == '1'>
        <#if lesson.status == 'published'>
            <#if course.price=='0.00'>
                <a id="buy-btn" type="button" class="btn btn-primary"
                        <#if setting("course.buy_fill_userinfo", "0") == "1">
                            href="#modal"
                            data-toggle="modal"
                            data-url="${ctx}/course/${course.id}/buy?targetType=course"
                        <#else>
                            href="${ctx}/order/show?targetId=${course.id}&targetType=course"
                        </#if>
                >
                    <#if appUser.id gt 0>免费加入学习
                    <#else>登录后加入学习
                    </#if>
                </a>
            <#else>
                <a id="buy-btn" type="button" class="btn btn-primary"
                        <#if setting("course.buy_fill_userinfo", "0") == "1">
                            href="#modal"
                            data-toggle="modal"
                            data-url="${ctx}/course/${course.id}/buy?targetType=course"
                        <#else>
                            href="${ctx}/order/show?targetId=${course.id}&targetType=course"
                        </#if>
                >
                    <#if appUser.id gt 0>觉得不错？点击购买此课程（${course.price}元）
                    <#else>登录后购买学习完整的课程
                    </#if>
                </a>
            </#if>
        </#if>
    <#else>
        <button type="button" class="btn btn-primary" data-dismiss="modal">关闭</button>
    </#if>
</#macro>