<#assign modalSize = 'large'/>

<#include '/bootstrap-modal-layout.ftl'/>

<#macro blockTitle>课程复制</#macro>

<#macro blockBody>

    <form id="course-copy-form" class="form-horizontal" method="post" action="${ctx}/admin/course/${course.id}/copying">

        <div class="row form-group">
            <div class="col-md-3 control-label">
                <label for="course_title">课程新标题</label>
            </div>
            <div class="col-md-8 controls">
                <input type="text" id="course_title" name="title" class="form-control" value="${course.title}">
            </div>
        </div>

        <input type="hidden" name="_csrf_token" value="${csrf_token('site')}">

    </form>
</#macro>

<#macro blockFooter>
    <button id="course-copy-btn" data-submiting-text="正在复制" type="submit" class="btn btn-primary pull-right"
            data-toggle="form-submit" data-target="#course-copy-form">复制
    </button>
    <button type="button" class="btn btn-link pull-right" data-dismiss="modal">取消</button>
    <script>app.load('course/copy')</script>
</#macro>


