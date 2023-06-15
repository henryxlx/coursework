<#assign experience = experience!'false' />
<div class="course-item-list-wrap">
    <ul class="course-item-list ${class!} " id="course-item-list">
        <#list items as item>
            <#assign id = item['itemType']!'' />
            <#if id?contains('chapter') >
                <#if item.type == 'unit'>
                    <li class="item chapter-item chapter-item-unit clearfix">
                        <span class="name">第 ${item.number } ${setting('default.part_name')!'节'}</span>
                        <span class="title">${item.title}</span>
                    </li>
                <#else>
                    <li class="item chapter-item clearfix">
                        <span class="name">第 ${item.number} ${setting('default.chapter_name')!'章'}</span>
                        <span class="title">${item.title}</span>
                    </li>
                </#if>
            <#elseif id?contains('lesson') >
                <#assign lessonUrl = ctx + '/course/' + course.id + '/learn#lesson/' + item.id />
                <li class="item lesson-item <#if learnStatuses?? && learnStatuses[item.id]??>lesson-item-${learnStatuses[item.id]}</#if> lesson-item-${item.id} hover-item clearfix "
                    data-id="${item.id}" data-num="${item?index}">
                    <#if item.status == 'published'>
                        <#if item.type == 'video'>
                            <span class="type" title="视频时长${fastLib.duration(item.length)}">
				    		<span>${fastLib.duration(item.length)}</span>
				    		<i class="fa fa-file-video-o"></i>
				    	</span>
                        <#elseif item.type == 'audio'>
                            <span class="type" title="音频时长${fastLib.duration(item.length)}">
			    		<span>${fastLib.duration(item.length)}</span>
			    		<i class="fa fa-file-audio-o"></i>
				      </span>
                        <#elseif item.type == 'text'>
                            <span class="type">图文 <i class="fa fa-file-photo-o"></i></span>
                        <#elseif item.type == 'testpaper'>
                            <span class="type">试卷 <i class="fa fa-file-text-o"></i></span>
                        <#elseif item.type == 'ppt'>
                            <span class="type">PPT <i class="fa fa-file-powerpoint-o"></i></span>
                        <#elseif item.type == 'document'>
                            <span class="type">文档 <i class="fa fa-files-o"></i></span>
                        <#elseif item.type == 'flash'>
                            <span class="type">Flash <i class="fa fa-film"></i></span>
                        <#elseif item.type == 'live'>
                            <span class="type">&nbsp;<i class="fa fa-video-camera"></i></span>
                            <#if item.startTime gt currentTime>
                                <small class="type">${item.startTime?number_to_date?string('MM月dd日')}
                                    <#assign weeks = {'Mon':'一','Tus':'二','Wed':'三','Thu':'四','Fri':'五','Sat':'六','Sun':'日'}/>
                                    星期${weeks[item.startTime?number_to_date?string('E')]}
                                    ${item.startTime?number_to_time?string('HH:mm')}
                                </small>
                            <#elseif item.startTime lte currentTime && item.endTime gte currentTime>
                                <small class="type">正在直播中</small>
                            <#elseif item.endTime lt currentTime >

                                <small class="type">
                                    <#if item.replayStatus == 'generated'>
                                        <span class="type">回放</span>
                                        <span class="glyphicon
                                glyphicon-play-circle"></span>
                                    <#else>
                                        <span class="type">直播结束</span>
                                    </#if>
                                </small>

                            </#if>
                        </#if>
                    <#else>
                        <span class="status">未发布</span>
                    </#if>

                    <span class="name ">课时${item.number}</span>
                    <span class="pie ">&nbsp;</span>
                    <a class=" title" href="${lessonUrl}" title="${item.title}" data-id="${item.id}"
                       data-experience="${experience}"
                       style="white-space:nowrap; overflow:hidden; text-overflow: ellipsis;">
                        ${item.title}
                        <#if item.type != 'text' && item.type != 'live' && item.type != 'testpaper'>
                            <#if item.mediaId != 0 && (files?? && files[item.mediaId]??) && show??>
                                <span class="label label-danger fileDeletedLesson" title="课时文件已删除">无效课时</span>
                            </#if>
                        </#if>

                        <#if (homeworkLessonIds ?? && homeworkLessonIds?seq_contains(item.id)) || (exercisesLessonIds ?? && exercisesLessonIds?seq_contains(item.id)) >
                            <span class="glyphicon  glyphicon-list-alt" title="作业、练习"></span>
                        </#if>

                        <#if item.materialNum gt 0 >
                            <span class="glyphicon  glyphicon-download" title="资料"></span>
                        </#if>

                        <#if item.giveCredit gt 0 >
                            <small class="text-muted">(${item.giveCredit}学分)</small>
                        </#if>

                        <#if item.requireCredit gt 0>
                            <small class="text-muted">(需${item.requireCredit}学分)</small>
                        </#if>

                    </a>
                    <button class="btn btn-success btn-mini action">开始学习</button>
                </li>
            </#if>
        </#list>
    </ul>
</div>