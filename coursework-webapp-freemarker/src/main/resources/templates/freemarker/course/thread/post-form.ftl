<#assign script_controller = 'course/thread-post-form' />

<@block_title "${(post??)?then('编辑帖子', '回复帖子')} - ${course.title}"/>

<#include '/course/dashboard-layout.ftl' />

<#macro blockDashboardMain>

    <ul class="breadcrumb">
        <li><a href="${ctx}/course/${course.id}/thread">讨论区</a></li>
        <li><a href="${ctx}/course/${course.id}/thread/${thread.id}">${fastLib.plainText(thread.title, 10)}</a></li>
        <li class="active">编辑帖子</li>
    </ul>

  <form id="thread-post-form" method="post"
          <#if post??>
            action="${ctx}/course/${course.id}/thread/${thread.id}/post/${post.id}/edit"
          <#else>
            action="${ctx}/course/${course.id}/thread/${post.id}/post"
          </#if>
  >
    <div class="form-group">
      <div class="controls">
      <textarea id="post_content" name="post[content]"
                required="required" class="form-control" rows="15"
                data-image-upload-url="${ctx}/editor/upload?token=upload_token('course')"
                placeholder="内容" data-display="内容">${post.content!}</textarea>
      </div>
    </div>
    <div class="form-group">
      <div class="controls clearfix">
        <input type="hidden" name="courseId" value="${courseId}"/>
        <input type="hidden" name="threadId" value="${threadId}"/>
        <button type="submit" class="btn btn-primary pull-right">发表</button>
        <a href="${ctx}/course/${course.id}/thread" class="btn btn-link pull-right">取消</a>
      </div>
    </div>

    <input type="hidden" name="_csrf_token" value="${csrf_token('site')}">
  </form>

</#macro>
