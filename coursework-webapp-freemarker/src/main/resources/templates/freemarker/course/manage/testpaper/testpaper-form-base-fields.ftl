<div class="form-group">
    <div class="col-md-2 control-label"><label for="testpaper-name-field">试卷名称</label></div>
    <div class="col-md-8 controls">
        <input class="form-control" value="${(testpaper.name)!}" name="name" id="testpaper-name-field"/>
    </div>
</div>

<div class="form-group">
    <div class="col-md-2 control-label"><label for="testpaper-description-field">试卷说明</label></div>
    <div class="col-md-8 controls">
        <textarea class="form-control" id="testpaper-description-field" name="description"
                  data-image-upload-url="${ctx}/editor/upload?token=upload_token('course')">${(testpaper.description)!}</textarea>
    </div>
</div>

<div class="form-group">
    <div class="col-md-2 control-label"><label for="testpaper-limitedTime-field">时间限制</label></div>
    <div class="col-md-8 controls">
        <input class="form-control width-input width-input-large" name="limitedTime" id="testpaper-limitedTime-field"
               value="${(testpaper.limitedTime)!0}"/> 分钟
        <div class="help-block">0分钟，表示无限制</div>
    </div>
</div>