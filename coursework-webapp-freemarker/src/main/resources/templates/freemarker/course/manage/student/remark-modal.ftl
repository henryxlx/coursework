<#assign modalSize = 'large'/>

<#include '/bootstrap-modal-layout.ftl' />

<#macro blockTitle>${setting('default.user_name')!'学员'}备注</#macro>

<#macro blockBody>

  <form id="student-remark-form" method="post" action="${ctx}/course/${course.id}/manage/student/${user.id}/remark"
        data-user="${default.user_name!'学员'}">

    <div class="form-group">
      <div class="controls">
        <input class="form-control" id="student-remark" name="remark" value="${member.remark}">
        <div class="help-block">请填写${setting('default.user_name')!'学员'}的备注信息</div>
      </div>
    </div>

    <input type="hidden" name="_csrf_token" value="${csrf_token('site')}">

  </form>

</#macro>

<#macro blockFooter>
  <button type="submit" class="btn btn-primary pull-right" data-toggle="form-submit" data-target="#student-remark-form">
    保存
  </button>
  <button type="button" class="btn btn-link pull-right" data-dismiss="modal">取消</button>
  <script>app.load('course-manage/student-remark')</script>
</#macro>
