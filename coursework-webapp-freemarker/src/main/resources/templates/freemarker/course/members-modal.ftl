<#assign modalSize = 'large'/>

<#include '/bootstrap-modal-layout.ftl' />

<#macro blockTitle>课程${setting('default.user_name')!'学员'}</#macro>

<#macro blockBody>

  <ul class="media-list user-simple-list" id="course-student-list">
    <#list students! as student>
      <#assign user = users[''+student.userId] />
      <#assign progress = progresses[''+student.userId] />
      <#assign isFollowing = followingIds?seq_contains(user.id) />
      <li class="media">
        <@web_macro.user_avatar user 'pull-left'/>
        <div>

          <#if appUser.id != user.id>
            <div class="pull-right">
              <button class="btn btn-primary btn-sm follow-student-btn"
                      data-url="${ctx}/user/${user.id}/follow" <#if isFollowing> style="display:none;"</#if>>关注
              </button>
              <button class="btn btn-success btn-sm unfollow-student-btn"
                      data-url="${ctx}/user/${user.id}/unfollow" <#if !isFollowing> style="display:none;"</#if>>已关注
              </button>
              <button class="btn btn-default btn-sm send-message-btn" data-url="${ctx}/message/create/${user.id}">发私信
              </button>
            </div>
          </#if>

          <p><a target="_blank" href="${ctx}/user/${user.id}">${user.username}</a></p>

          <#if student.remark?? && canManage>
            <p class="text-muted text-sm" title="${student.remark}">(${plain_text(student.remark, 8)})</p>
          </#if>
          <#if canManage??>
            <div class="progress" title="已学${progress.number}课时">
              <div class="progress-bar" style="width: {{ progress.percent }}">
              </div>
            </div>
          </#if>
        </div>
      </li>
    <#else>
      <li>本课程暂时还没有学生</li>
    </#list>
  </ul>

  <@web_macro.paginator paginator! />

  <script>
    app.load('course/members-modal');
  </script>
</#macro>


