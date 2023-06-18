<#assign script_controller = 'course/thread-form' />

<#include '/course/dashboard-layout.ftl' />

<#macro blockTitle><#if thread??>编辑话题<#else>发表话题</#if> - ${course.title} - ${blockTitleParent}</#macro>

<#macro blockDashboardMain>

  <ul class="breadcrumb">
    <li><a href="${ctx}/course/${course.id}/thread">讨论区</a></li>
    <#if thread??>
      <li><a href="${ctx}/course/${thread.courseId}/thread/${thread.id}" title="${thread.title}">${thread.title}
          |plain_text(10)</a></li>
      <li class="active"><#if type == 'question'>编辑问题<#else>编辑话题</#if></li>
    <#else>
      <li class="active"><#if type == 'question'>提问题<#else>发表话题</#if></li>
    </#if>
  </ul>


  <form id="thread-form" class="form-vertical" method="post"
          <#if thread??>
            action="${ctx}/course/${course.id}/thread/${thread.id}/edit"
          <#else>
            action="${ctx}/course/${course.id}/thread/create"
          </#if>
  >

    <div class="form-group">
      <div class="controls">
        <#if type == 'question'>
          <#assign placeholder = '标题，用一句话说清你的问题'/>
        <#else>
          <#assign placeholder = '标题'/>
        </#if>
        <input type="text" id="thread_title" name="thread[title]"
               required="required" class="form-control expand-form-trigger" placeholder="我要提问"
               data-display="标题" value="${(thread.title)!}"/>
      </div>
    </div>

    <div class="form-group">
      <div class="controls">
      <textarea id="thread_content" name="thread[content]"
                required="required" class="form-control" rows="15"
                data-image-upload-url="${ctx}/editor/upload?token=upload_token('course')"
                placeholder="详细描述你的问题" data-display="内容">${(thread.content)!}</textarea>
      </div>
    </div>

    <div class="form-group clearfix">
      <div class="controls pull-right">
        <input type="hidden" name="type" value="${type}"/>
        <input type="hidden" name="courseId" value="${courseId}"/>
        <#if thread??>
          <a href="${ctx}/course/${thread.courseId}/thread/${thread.id}" class="btn btn-link">取消</a>
          <button type="submit" class="btn btn-primary btn-fat">保存</button>
        <#else>
          <a href="${ctx}/course/${course.id}/thread" class="btn btn-link">取消</a>
          <button type="submit" class="btn btn-primary">发表</button>
        </#if>
      </div>
    </div>

    <input type="hidden" name="_csrf_token" value="${csrf_token('site')}">
  </form>

</#macro>
