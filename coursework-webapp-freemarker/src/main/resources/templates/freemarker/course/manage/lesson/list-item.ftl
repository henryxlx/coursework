<li class="item-lesson clearfix" id="lesson-${lesson.id}" style="word-break: break-all;">
    <div class="item-line"></div>
    <div class="item-content">
        <#assign mediaStatus = lesson.mediaStatus!>

        <#if lesson.type == 'video'>
            <#if mediaStatus == 'waiting'>
                <i class="fa fa-file-video-o text-warning"></i>
            <#elseif mediaStatus == 'doing'>
                <i class="fa fa-file-video-o text-info"></i>
            <#elseif mediaStatus == 'error'>
                <i class="fa fa-file-video-o text-danger"></i>
            <#else>
                <i class="fa fa-file-video-o text-success"></i>
            </#if>
        <#elseif lesson.type == 'live'>
            <#if mediaStatus == 'waiting'>
                <i class="fa fa-video-camera text-warning"></i>
            <#elseif mediaStatus == 'doing'>
                <i class="fa fa-video-camera text-info"></i>
            <#elseif mediaStatus == 'error'>
                <i class="fa fa-video-camera text-danger"></i>
            <#else>
                <i class="fa fa-video-camera text-success"></i>
            </#if>
        <#elseif lesson.type == 'audio'>
            <i class="fa fa-file-audio-o text-success"></i>
        <#elseif lesson.type == 'testpaper'>
            <i class="fa fa-file-text-o text-success"></i>
        <#elseif lesson.type == 'text'>
            <i class="fa fa-file-photo-o text-success"></i>
        <#elseif lesson.type == 'document'>
            <i class="fa fa-files-o text-success"></i>
        <#elseif lesson.type == 'flash'>
            <i class="fa fa-film text-success"></i>
        <#else>
            <i class="fa fa-file-powerpoint-o text-success"></i>
        </#if>
        课时 <span class="number">${lesson.number}</span>：<#if lesson.free == 1><span
            class="label label-danger">免费</span></#if>

        <#if lesson.type != 'text' && lesson.type != 'live' && lesson.type != 'testpaper' && lesson.mediaId != 0 && file??>
            <span class="label label-danger fileDeletedLesson" title="课时文件已删除">无效课时</span>
        </#if>
        ${lesson.title}

        <#if ['video', 'audio']?seq_contains(lesson.type)>
            <span class="text-muted">${lesson.length}</span>
        </#if>

        <#if lesson.type == 'live'>
            <span class="text-muted">${lesson.startTime?number_to_date?string('yyyy-MM-dd HH:mm:ss')}</span>
        </#if>

        <#if lesson.giveCredit gt 0>
            <small class="text-muted">(${lesson.giveCredit}学分)</small>
        </#if>

        <#if lesson.requireCredit gt 0>
            <small class="text-muted">(需${lesson.requireCredit}学分)</small>
        </#if>

        <#if lesson.type != 'testpaper'>
            <#if lesson.type == 'video'>
                <#assign mediaTypeName = '视频' />
            <#elseif lesson.type == 'audio'>
                <#assign mediaTypeName = '视频' />
            <#else>
                <#assign mediaTypeName = '文件' />
            </#if>
            <#if mediaStatus == 'waiting'>
                <span class="text-warning">(正在等待${mediaTypeName}格式转换)</span>
            <#elseif mediaStatus == 'doing'>
                <span class="text-info">(正在转换${mediaTypeName}格式)</span>
            <#elseif mediaStatus == 'error'>
                <span class="text-danger">(${mediaTypeName}格式转换失败)</span>
            </#if>
        </#if>

        <#if lesson.status == 'unpublished'>
            <span class="unpublish-warning text-warning">(未发布)</span>
        </#if>
    </div>

    <div class="item-actions">
        <a class="btn btn-link" data-toggle="modal" data-target="#modal" data-backdrop="static" data-keyboard="false"
                <#if lesson.type == 'testpaper'>
                    data-url="${ctx}/course/${course.id}/manage/lesson/${lesson.id}/edit/testpaper"
                <#elseif lesson.type == 'live'>
                    data-url="${ctx}/course/${course.id}/manage/live/lesson/${lesson.id}/edit"
                <#else>
                    data-url="${ctx}/course/${course.id}/manage/lesson/${lesson.id}/edit"
                </#if>
        ><span class="glyphicon glyphicon-edit prs"></span>编辑</a>
        <#if lesson.type == 'testpaper'>
            <a class="btn btn-link" href="${ctx}/test/${lesson.mediaId}/preview" target="_blank"><span
                        class="glyphicon glyphicon-eye-open prs"></span>预览</a>
        <#else>
            <a class="btn btn-link" href="${ctx}/course/${course.id}/learn?preview=1#lesson/${lesson.id}"
               target="_blank"><span class="glyphicon glyphicon-eye-open prs"></span>预览</a>
        </#if>
        <span class="dropdown">
			<a class=" dropdown dropdown-toggle dropdown-more" id="dropdown-more" data-toggle="dropdown" href="#"><span
                        class="glyphicon glyphicon-chevron-down" style="top:4px;margin-right:3px" aria-haspopup="true"
                        aria-expanded="false"></span>更多</a>

			<ul class="dropdown-menu pull-right dropdown-menu-more" role="menu" style="margin-top:12px;min-width:144px"
                aria-labelledby="dLabel" style="display:none;">

			  <#if setting('homework.enabled', '0') == '1' && lesson.type != 'testpaper'>
                  <#assign homework =  homeworks[lesson.id]! />
                  <#assign  homeworkId =  homework.id />
                  <#if homework??>
                      <li><a href="${ctx}/course/${course.id}/manage/lesson/${lesson.id}/homework/${homeworkId}/edit"><span
                                      class="glyphicon glyphicon-list-alt prs"></span>编辑作业</a></li>
                      <li><a href="${ctx}${ctx}/course/${course.id}/homework/${homeworkId}/preview"
                             class="preview-homework-btn"><span class="glyphicon glyphicon-eye-open prs"></span>预览作业</a></li>
                      <li><a href="javascript:;" class="delete-homework-btn"
                             data-url="${ctx}/course/${course.id}/manage/live/lesson/${lesson.id}/homework/${homeworkId}/remove"><span
                                      class="glyphicon glyphicon-trash prs"></span>移除作业</a></li>
					<#else>
                      <li><a href="${ctx}/course/${course.id}/manage/lesson/${lesson.id}/homework/${homeworkId}/create"><span
                                      class="glyphicon glyphicon-list-alt prs"></span>布置作业</a></li>
                  </#if>

                  <li class="divider"></li>
    			<#assign  lessonId = lesson.id />
    			<#assign  exercise = exercises[lessonId]! />
    			<#if exercise??>
                  <li><a href="javascript:;" class="delete-exercise-btn"
                         data-url="${ctx}/course/${course.id}/manage/lesson/${lesson.id}/exercise/${exercise.id}/delete"><span
                                  class="glyphicon glyphicon-trash prs"></span>移除练习</a></li>
					<#else>
                  <li><a href="${ctx}/course/${course.id}/manage/lesson/${lesson.id}/exercise/create"><span
                                  class="glyphicon glyphicon-list-alt prs"></span>新增练习</a></li>
              </#if>

                  <li class="divider"></li>
              </#if>

                <#if lesson.type != 'testpaper'>
                    <li><a href="javascript:;" data-toggle="modal" data-target="#modal"
                           data-url="${ctx}/course/${course.id}/manage/lesson/${lesson.id}/material"><span
                                    class="glyphicon glyphicon-paperclip prs"></span>添加资料</a></li>
                    <li class="divider"></li>
                </#if>

  				<li class="<#if lesson.status == 'published'>hidden </#if>">
  					<a href="javascript:;" class="publish-lesson-btn"
                       data-url="${ctx}/course/${course.id}/manage/lesson/${lesson.id}/publish"><span
                                class="glyphicon glyphicon-ok-circle prs"></span>发布
  					</a>
  				</li>

          		<li class="<#if lesson.status == 'unpublished'>hidden </#if>">
          			<a href="javascript:;" class="unpublish-lesson-btn"
                       data-url="${ctx}/course/${course.id}/manage/lesson/${lesson.id}/unpublish"><span
                                class="glyphicon glyphicon-remove-circle prs"></span>取消发布
          			</a>
          		</li>
				<li><a href="javascript:;" class="delete-lesson-btn"
                       data-url="${ctx}/course/${course.id}/manage/lesson/${lesson.id}/delete"><span
                                class="glyphicon glyphicon-trash prs"></span>删除</a></li>
        </ul>
		</span>
    </div>
</li>