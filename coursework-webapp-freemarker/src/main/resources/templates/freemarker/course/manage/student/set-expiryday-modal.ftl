<#include '/bootstrap-modal-layout.ftl' />

<#macro blockTitle>增加${setting('default.user_name')!'学员'}课程有效期</#macro>

<#macro blockBody>

  <form id="expiryday-set-form" class="form-horizontal" method="post"
        action="${ctx}/course/${course.id}/set_expiryday/${user.id}">
    <#if course.status == 'published'>
      <div class="row form-group">
        <div class="col-md-2 control-label">
          <label for="course-title">课程标题</label>
        </div>
        <div class="col-md-7 controls">
          <div style="margin-top: 7px;">
            ${course.title}
          </div>
        </div>
      </div>

      <div class="row form-group">
        <div class="col-md-2 control-label">
          <label for="student-nickname">${setting('default.user_name')!'学员'}用户名称</label>
        </div>
        <div class="col-md-7 controls">
          <div style="margin-top: 7px;">
            ${user.username}
          </div>
        </div>
      </div>

      <div class="row form-group">
        <div class="col-md-2 control-label">
          <label for="set-more-expiryday">增加天数</label>
        </div>
        <div class="col-md-7 controls">
          <input type="text" id="set-more-expiryday" name="expiryDay" value="0" class="form-control width-input">
        </div>
      </div>
    <#else>
      <div class="empty">${dict_text('courseStatus', course.status)}课程不能增加${setting('default.user_name')!'学员'}
        有效期，请课程发布后再增加。
      </div>
    </#if>

    <input type="hidden" name="_csrf_token" value="${csrf_token('site')}">

  </form>

</#macro>

<#macro blockFooter>
  <#if course.status == 'published'>
    <button type="submit" id="submit" class="btn btn-primary pull-right" data-toggle="form-submit"
            data-target="#expiryday-set-form" data-user="${default.user_name!'学员'}">保存
    </button>
  </#if>
  <button type="button" class="btn btn-link pull-right" data-dismiss="modal">取消</button>
  <script>app.load('course-manage/set-expiryday-modal')</script>
</#macro>
