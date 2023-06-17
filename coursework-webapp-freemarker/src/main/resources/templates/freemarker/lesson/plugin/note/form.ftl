<form id="lesson-note-plugin-form" method="post" action="${ctx}/lessonplugin/note/save">
    <div class="form-group note-content">
        <div class="controls">
			<textarea id="note_content" name="note[content]"
                      required="required" class="form-control" rows="5"
                      data-image-upload-url="${ctx}/editor/upload?token=upload_token('course')">${content!}</textarea>
        </div>
    </div>

    <div class="form-group note-actions">
        <div class="controls clearfix">
            <span class="text-muted pull-left" data-role="saved-message"></span>
            <button id="save" class="btn btn-primary btn-sm" type="submit">保存笔记</button>
        </div>
    </div>

    <input type="hidden" name="id" value="${id!}"/>
    <input type="hidden" name="courseId" value="${courseId!}"/>
    <input type="hidden" name="lessonId" value="${lessonId!}"/>
    <input type="hidden" name="_csrf_token" value="${csrf_token('site')}">

</form>