<form id="lesson-question-plugin-form" method="post" action="${ctx}/lessonplugin/question/create">

    <div class="form-group">
        <div class="controls">
            <input type="text" id="question_title" name="question[title]"
                   required="required" class="form-control expand-form-trigger" placeholder="我要提问"
                   data-auto-url="${ctx}/following/bynickname/match_jsonp"
                   data-url="${ctx}/message/check/receiver" data-display="标题"/>
        </div>
    </div>

    <div class="form-group detail-form-group hide">
        <div class="controls">
			<textarea id="question_content" name="question[content]"
                      required="required" class="form-control" rows="5"
                      data-image-upload-url="${ctx}/editor/upload?token=upload_token('course')"
                      placeholder="详细描述你的问题" data-display="详细描述你的问题"></textarea>
        </div>
    </div>

    <div class="form-group detail-form-group hide">
        <div class="controls clearfix">
            <button class="btn btn-primary btn-sm pull-right" type="submit">提问</button>
            <button class="btn btn-link btn-sm collapse-form-btn pull-right" type="button">取消</button>
        </div>
    </div>

    <input type="hidden" name="courseId" value="${courseId!}"/>
    <input type="hidden" name="lessonId" value="${lessonId!}"/>
    <input type="hidden" name="threadId" value="${threadId!}"/>
    <input type="hidden" name="_csrf_token" value="${csrf_token('site')}">
</form>