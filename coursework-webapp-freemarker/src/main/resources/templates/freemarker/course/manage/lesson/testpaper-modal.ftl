<#include '/bootstrap-modal-layout.ftl' />

<#macro blockTitle>
    <#if lesson??>编辑试卷课时${lesson.number!}<#else>添加试卷课时</#if>
</#macro>

<#macro blockBody>
    <#if paperOptions??>
        <form id="course-lesson-form" class="form-horizontal lesson-form" method="post"
              <#if parentId??>data-parentId="${parentId}" </#if>
                <#if lesson??>
                    action="${ctx}/course/${course.id}/manage/lesson/${lesson.id}/edit/testpaper"
                <#else>
                    action="${ctx}/course/${course.id}/manage/lesson/create/testpaper"
                </#if>
        >

            <div class="form-group">
                <div class="col-md-2 control-label"><label for="lesson-mediaId-field">试卷</label></div>
                <div class="col-md-9 controls">
                    <select id="lesson-mediaId-field" class="form-control" name="mediaId">
                        <@select_options paperOptions, (lesson.mediaId)!'0', '请选择试卷'/>
                    </select>
                    <div class="help-block">如果找不到试卷，请先确定该试卷已经发布</div>
                </div>
            </div>

            <div class="form-group">
                <div class="col-md-2 control-label"><label for="lesson-title-field">标题</label></div>
                <div class="col-md-9 controls">
                    <input id="lesson-title-field" class="form-control" type="text" name="title"
                           value="${(lesson.title)!''}">
                </div>
            </div>

            <#if features?? && features?seq_contains('lesson_credit')>
                <div class="form-group">
                    <div class="col-md-2 control-label"><label for="lesson-title-field">限制学分</label></div>
                    <div class="col-md-9 controls">
                        <input class="form-control width-input width-input-small" type="text" name="requireCredit"
                               value="${(lesson.requireCredit)!0}"> 分
                        <div class="help-block">参加此次考试所需的学分，０分则不限制。</div>
                    </div>
                </div>
            </#if>


            <input type="hidden" name="_csrf_token" value="${csrf_token('site')}">
        </form>
        <script>app.load('course-manage/lesson-testpaper-modal')</script>
    <#else>
        <div class="empty">尚未创建试卷，请先<a href="${ctx}/course/${course.id}/manage/testpaper">创建试卷</a>。</div>
    </#if>

</#macro>

<#macro blockFooter>
    <#if paperOptions??>
        <button type="button" class="btn btn-link" data-dismiss="modal">取消</button>
        <button id="course-testpaper-btn" data-submiting-text="正在提交" type="submit" class="btn btn-primary"
                data-toggle="form-submit" data-target="#course-lesson-form">保存
        </button>
    <#else>
        <button type="button" class="btn btn-link" data-dismiss="modal">取消</button>
    </#if>
</#macro>