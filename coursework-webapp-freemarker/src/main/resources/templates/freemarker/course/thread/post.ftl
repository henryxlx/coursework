<form id="thread-post-form" class="form-vertical" method="post"
      action="${ctx}/course/${course.id}/thread/${thread.id}/post" data-auto-submit="false" novalidate>
    <div class="form-group">
        <div class="controls">
			<textarea id="post_content" name="post[content]"
                      required="required" class="form-control" rows="8"
                      data-display="内容"
                      data-image-upload-url="${ctx}/editor/upload?token=upload_token('course')"></textarea>
        </div>
    </div>
    <div class="form-group">
        <div class="controls clearfix">
            <input type="hidden" name="courseId" value="${courseId!}"/>
            <input type="hidden" name="threadId" value="${threadId!}"/>
            <button type="submit" class="btn btn-primary pull-right">
                添加<#if thread.type == 'question' >答案<#else>回复</#if></button>
        </div>
    </div>

    <input type="hidden" name="_csrf_token" value="${csrf_token('site')}">
</form>