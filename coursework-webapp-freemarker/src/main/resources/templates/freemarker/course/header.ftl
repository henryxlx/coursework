<div class="row row-12">
  <div class="col-md-12">
    <div class="panel panel-default  course-header">
      <div class="panel-body clearfix">
        <a href="${ctx}/course/${course.id}">
            <img class="picture" src="${default_path('coursePicture', course.largePicture, '')}" />
        </a>

        <h1 class="title"><a href="${ctx}/course/${course.id}">${course.title}</a>
          <#if course.status?? && course.status =='closed'>
            <span class="label label-danger ">已关闭</span>
          <#elseif course.status=='draft'>
            <span class="label label-warning ">未发布</span>
          <#elseif course.status=='published'>
            <#if course.serializeMode?? && course.serializeMode =='serialize'>
              <span class="label label-success ">更新中</span>
            <#elseif course.serializeMode=='finished'>
              <span class="label label-warning ">已完结</span>
            </#if>
          </#if>
        </h1>

        <div class="teachers">
          <#if course.teacherIds?? && course.teacherIds?is_sequence>
            教师：
            <#list course.teacherIds! as id>
              <#assign user = usersForHeader[''+id]! />
              <a href="#modal" data-toggle="modal"
                 data-url="${ctx}/course/${course.id}/teacher/${user.id}">${user.username}</a>
            </#list>
          </#if>
        </div>

        <div class="toolbar hidden-xs hidden-lt-ie8">
          <#if !manage??>
            <div class="btn-group">
              <a class="btn btn-default btn-sm" data-toggle="modal" data-url="${ctx}/course/${course.id}/review/create"
                 data-role="tooltip" title="评价" data-placement="top" href="#modal">
                <i class="esicon esicon-like"></i> ${course.ratingNum}</a>
              <#--<#if setting('course.show_student_num_enabled', '1') == 1 || ${member.role('student')} == 'teacher' || isAdmin>
                <a class="btn btn-default btn-sm" data-toggle="modal" data-url="${ctx}/course/${course.id}/members" data-role="tooltip" title="查看${setting('default.user_name')='学员'}的学习进度, 还可以发私信进行联系!" data-placement="bottom"
                href="#modal"><i class="esicon esicon-user"></i> ${course.studentNum}</a>
              </#if>-->

              <div class="btn-group" data-role="tooltip" title="分享到" data-placement="left">
                <button class="btn btn-default btn-sm dropdown-toggle" data-toggle="dropdown">
                <i class="esicon esicon-share"></i>
              </button>
                <#assign right=true/>

              <#--<#include '/course/common/share-dropdown.ftl'>-->
            </div>
            <#if canExit??>
              <#if member.joinedType?? && member.joinedType == 'course' && member.orderId??>
              <a class="btn btn-default btn-sm " title="退出学习" type="button" href="#modal" data-toggle="modal" data-url="${ctx}/target/${course.id}/order/refund?targetType=course">
              <i class="esicon esicon-exit"></i></a>
              <#else>
              <a class="btn btn-default btn-sm course-exit-btn" id="exit-course-learning" title="退出学习" type="button"  href="javascript:;" data-url="${ctx}/course/${course.id}/exit"  data-goto="${ctx}/course/${course.id}">
              <i class="esicon esicon-exit"></i></a>
            </#if>
            </#if>
            <#if canManage??>
            <a class="btn btn-default btn-sm " type="button" href="${ctx}/course/${course.id}/manage"  title="课程管理"  >
            <i class="esicon esicon-setting"></i>
            </a>
            </#if>
          </div>

          <#else>

            <#if course.status?? && course.status == 'published'>
              <div class="btn-group">
                <a class="btn btn-default btn-sm" href="${ctx}/course/${course.id}">返回课程主页</a>
              </div>
            </#if>

            <div class="btn-group">
              <button class="btn btn-default btn-sm dropdown-toggle" data-toggle="dropdown">预览 <span class="caret"></span></button>
              <ul class="dropdown-menu pull-right">
                <li><a href="${ctx}/course/${course.id}?previewAs=member" target="_blank">作为 已购买用户</a></li>
                <li><a href="${ctx}/course/${course.id}?previewAs=guest" target="_blank">作为 未购买用户</a></li>
              </ul>
            </div>

            <#if course.status?? && course.status != 'published'>
              <div class="btn-group">
                <button class="btn btn-success btn-sm course-publish-btn"
                        data-url="${ctx}/course/${course.id}/manage/publish">发布课程
                </button>
              </div>
            </#if>

          </#if>
        </div>
      </div>
    </div>

    <#if vipChecked?? && vipChecked != 'ok'>
      <div class="alert alert-danger alert-dismissable">
        <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
        <#if vipChecked?? && vipChecked == 'not_member'>
          您已经不是会员，不能学习此课程！
          <#if course.price gt 0 >
            请重新<a class="btn-link" href="${ctx}/vip/" target="_blank">开通会员</a>
          <#else>
            <a class="btn-link js-exit-course" data-url="${ctx}/course/${course.id}/exit"
               data-go="${ctx}/course/${course.id}">返回课程首页</a>
          </#if>
        <#elseif vipChecked?? && vipChecked == 'member_expired'>
          您的会员已过期，不能学习此课程，请先<a class="btn-link" href="${ctx}/vip/renew'" target="_blank">续费</a>。
        <#elseif vipChecked?? && vipChecked == 'level_low'>
          会员等级不够，不能学习此课程。请先<a class="btn-link" href="${ctx}/vip/upgrade" target="_blank">升级会员</a>。
        <#else>
          此课程已不支持会员免费学，<a class="btn-link js-exit-course" data-url="${ctx}/course/${course.id}/exit" data-go="${ctx}/course/${course.id}">返回课程首页</a>
       </#if>

      </div>

    </#if>

    <#if isNonExpired??>
       <div class="alert alert-danger alert-dismissable">
        <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
          您购买的课程已到期，无法学习课时、提问等。如有疑问，请联系老师，或点击
          <a class="btn btn-primary btn-sm "  href="${ctx}/course/${course.id}/course_rebuy">
          <#if course.price gt 0>
          重新购买
          <#else>
          重新加入
            </#if>
          </a>
      </div>

    </#if>
  </div>
</div>
