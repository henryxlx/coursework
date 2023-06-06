<#assign modalSize = 'large'/>

<#include '/bootstrap-modal-layout.ftl' />

<#macro blockTitle>添加${setting('default.user_name', '学员')}</#macro>
<#macro blockBody>

    <form id="student-create-form" class="form-horizontal" method="post"
          action="${ctx}/course/${course.id}/manage/student/create">
        <#if course.status == 'published'>
            <div class="row form-group">
                <div class="col-md-2 control-label">
                    <label for="student-nickname">${setting('default.user_name', '学员')}昵称</label>
                </div>
                <div class="col-md-7 controls">
                    <input type="text" id="student-nickname" name="nickname" class="form-control"
                           data-url="${ctx}/course/${course.id}/manage/username_check">
                    <div class="help-block">只能添加系统中已注册的用户</div>
                </div>
            </div>

            <div class="row form-group">
                <div class="col-md-2 control-label">
                    <label for="student-remark">备注</label>
                </div>
                <div class="col-md-7 controls">
                    <input type="text" id="student-remark" name="remark" class="form-control">
                    <div class="help-block">选填</div>
                </div>
            </div>
        <#else>
            <div class="empty">${dict_text('courseStatus', course.status)}课程不能添加${setting('default.user_name', '学员')}
                ，请课程发布后再添加。
            </div>
        </#if>

        <input type="hidden" name="_csrf_token" value="${csrf_token('site')}">

    </form>

</#macro>

<#macro blockFooter>
    <#if course.status == 'published'>
        <button id="student-create-form-submit" type="submit" class="btn btn-primary pull-right"
                data-toggle="form-submit" data-target="#student-create-form" data-submiting-text="正在保存..."
                data-user="${setting('default.user_name', '学员')}">保存
        </button>
    </#if>
    <button type="button" class="btn btn-link pull-right" data-dismiss="modal">取消</button>
    <script>app.load('course-manage/student-create')</script>
</#macro>
