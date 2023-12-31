<#assign hideSetupHint = true />
<#assign script_controller = 'course/learn' />
<#assign bodyClass = 'lesson-dashboard-page' />

<#if setting('homework.enabled', '0') == '1'>
    <#assign script_arguments = "{plugins: ['lesson', 'question', 'note', 'material', 'homework']}" />
<#else>
    <#assign script_arguments = "{plugins: ['lesson', 'question', 'note', 'material']}" />
</#if>

<@block_title '${course.title}'/>

<#include '/layout.ftl'>

<#macro blockStylesheetsExtra>
    <link rel="stylesheet" media="screen"
          href="${ctx}/assets/libs/jquery-plugin/perfect-scrollbar/0.4.8/perfect-scrollbar.css"/>
</#macro>

<#macro blockContent>
    <div class="container lesson-dashboard" id="lesson-dashboard"
       data-course-id="${course.id}"
       data-course-uri="${ctx}/course/${course.id}"
       data-dashboard-uri="${ctx}/course/${course.id}/learn"
       data-hide-media-lesson-learn-btn="${(is_feature_enabled['hide_media_lesson_learn_btn'])!'false'}">
    <div class="dashboard-content">
      <a class="btn btn-primary  nav-btn back-course-btn" href="${ctx}/course/${course.id}"><span
                class="glyphicon glyphicon-chevron-left"></span> 返回课程</a>
      <a class="btn btn-primary  nav-btn prev-lesson-btn" href="javascript:" data-role="prev-lesson"
         data-placement="right" title="上一课时"><span class="glyphicon glyphicon-chevron-up" title="上一课时"></span></a>
      <a class="btn btn-primary nav-btn next-lesson-btn" href="javascript:" data-role="next-lesson"
         data-placement="right" title="下一课时"><span class="glyphicon glyphicon-chevron-down"></span></a>

      <div class="dashboard-header">
        <div class="pull-left title-group">
          <span class="chapter-label">第<span
                    data-role="chapter-number"></span>${setting('default.chapter_name')!'章'}</span>
          <span class="divider">&raquo;</span>
          <span class="chapter-label">第<span data-role="unit-number"></span>${setting('default.part_name')!'节'}</span>
          <span class="divider">&raquo;</span>
          <span class="item-label">课时<span data-role="lesson-number">正在加载...</span></span>
          <span class="item-title" data-role="lesson-title"></span>
        </div>
      </div>

      <div class="dashboard-body">
        <div class="lesson-content" id="lesson-video-content" data-role="lesson-content" style="display:none;"
                <#if setting('storage.video_watermark', '') == '1' && setting('storage.video_watermark_image', '0') != '0'>
                  data-watermark="${file_url(setting('storage.video_watermark_image', ''), null, true)}"
                </#if>
                <#if setting('storage.video_fingerprint', '0') == '1' && appUser??>
                  data-fingerprint="${ctx}/cloud/video/fingerprint?userId=${appUser.id}"
                </#if>
             data-user-id="${appUser.id}"
        ></div>
        <div class="watermarkEmbedded"></div>

        <div class="lesson-content lesson-content-audio" id="lesson-audio-content" data-role="lesson-content"
             style="display:none;"></div>
        <div class="lesson-content" id="lesson-swf-content" data-role="lesson-content" style="display:none;"></div>
        <div class="lesson-content" id="lesson-iframe-content" data-role="lesson-content" style="display:none;"></div>
        <div class="lesson-content lesson-content-text" id="lesson-text-content" data-role="lesson-content"
             style="display:none;"
                <#if setting('course.copy_enabled', '0') != '0'> oncopy="return false;" oncut="return false;" onselectstart="return false" oncontextmenu="return false;"</#if>>
          <div class="lesson-content-text-body"></div>
        </div>

        <div class="lesson-content lesson-content-document" id="lesson-document-content" data-role="lesson-content"
             style="display:none;">
          <div class="lesson-content-document-body"></div>
        </div>

        <div class="lesson-content lesson-content-text" id="lesson-live-content" data-role="lesson-content"
             style="display:none;">
          <div class="lesson-content-text-body"></div>
        </div>
        <div class="lesson-content lesson-content-text" id="lesson-unpublished-content" data-role="lesson-content"
             style="display:none;">
          <div class="lesson-content-text-body">当前课时正在编辑中，暂时无法观看。</div>
        </div>

        <div class="lesson-content lesson-content-text" id="lesson-testpaper-content" data-role="lesson-content"
             style="display:none;">
          <div class="lesson-content-text-body"></div>
        </div>
        <div class="lesson-content lesson-content-text" id="lesson-ppt-content" data-role="lesson-content"
             style="display:none;">
          <div class="lesson-content-text-body"></div>
        </div>

      </div>

      <div class="dashboard-footer clearfix">

        <div class="pull-right">
          <button class="btn btn-primary finish-btn" data-role="finish-lesson" style="display:none;"><span
                    class="glyphicon glyphicon-unchecked"></span> 学过了
          </button>
        </div>

      </div>
    </div>

    <div class="toolbar toolbar-open" id="lesson-dashboard-toolbar">
      <div class="toolbar-nav">

        <ul class="toolbar-nav-stacked" id="lesson-toolbar-primary"></ul>
        <ul class="toolbar-nav-stacked" id="lesson-toolbar-secondary">
          <li class="hide-pane" style="display:none;">
            <a href="javascript:"><span class="glyphicon glyphicon-chevron-right"></span></a>
          </li>
        </ul>

      </div>
      <div class="toolbar-pane-container">
      </div>
    </div>

  </div>




  <div class="modal" id="course-learned-modal" style="display:none;">
    <div class="modal-dialog">
      <div class="modal-content">
        <div class="modal-header">
          <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
          <h4 class="modal-title">学习进度提示</h4>
        </div>
        <div class="modal-body">
          <p class="text-success">赞一个，这个课程你已经都学完啦，你可以再回顾一下或者去看看别的课程～～～</p>
        </div>
        <div class="modal-footer">
          <a href="${ctx}/course/${course.id}" class="btn btn-primary">回课程主页</a>
        </div>
      </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
  </div><!-- /.modal -->

  <div class="modal" id="mediaPlayed-control-modal" style="display:none;">
    <div class="modal-dialog">
      <div class="modal-content">
        <div class="modal-header">
          <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
          <h4 class="modal-title">媒体课时学习提示</h4>
        </div>
        <div class="modal-body">
          <p class="text-success">此课时设置了必须完整播放完整个课时才能学完～～</p>
        </div>
      </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
  </div><!-- /.modal -->

  <div class="modal" id="homeworkDone-control-modal" style="display:none;">
    <div class="modal-dialog">
      <div class="modal-content">
        <div class="modal-header">
          <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
          <h4 class="modal-title">作业未完成提示</h4>
        </div>
        <div class="modal-body">
          <p class="text-success">此课时设置了必须做完本课时作业并提交后才能学完～～</p>
        </div>
      </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
  </div><!-- /.modal -->

</#macro>
