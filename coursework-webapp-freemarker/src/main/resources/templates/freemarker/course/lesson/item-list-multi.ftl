<div class="course-item-list-multi">
    <#list groupedItems! as group>
        <#if group.type == 'chapter'>
            <#assign chapter = group.data />
            <#if chapter.type == 'unit'>
                <h4>第 ${chapter.number} ${setting('default.part_name')!'节'}： <strong>${chapter.title}</strong></h4>
            <#else>
                <h3>第 ${chapter.number} ${setting('default.chapter_name')!'章'}：　<strong>${chapter.title}</strong></h3>
            </#if>
        <#else>
            <ul class="row">
                <#list group.data! as item>
                    <li class="<#if item.type == 'live'>col-md-6 live-item<#else>col-md-4</#if> clearfix item">
                        <a class="item-object item-object-${item.type}" href="#modal" data-toggle="modal"
                           data-url="${ctx}/course/${item.courseId}/lesson/${item.id}/preview"
                           data-backdrop="static" data-keyboard="false" title="${item.title}">
                            <#if item.free == 1><span class="item-free"></span></#if>
                            <#if item.type == 'video'>
                                <i class="fa fa-file-video-o"></i>
                            <#elseif item.type == 'text'>
                                <i class="fa fa-file-photo-o"></i>
                            <#elseif item.type == 'audio'>
                                <i class="fa fa-file-audio-o"></i>
                            <#elseif item.type == 'ppt'>
                                <i class="fa fa-file-powerpoint-o"></i>
                            <#elseif item.type == 'testpaper'>
                                <i class="fa fa-file-text-o"></i>
                            <#elseif item.type == 'document'>
                                <i class="fa fa-files-o"></i>
                            <#elseif item.type == 'flash'>
                                <i class="fa fa-film"></i>
                            <#elseif item.type == 'live'>
                                <i class="fa fa-video-camera"></i>
                            </#if>
                            <#if item.status == 'published'>
                                <span class="item-length">
								<#if item.type == 'text'>
                                    图文
                                <#elseif item.type == 'testpaper'>
                                    试卷
                                <#elseif item.type == 'live'>
                                    ${fastLib.duration(item.length)}
                                <#elseif item.type == 'ppt'>
                                    PPT
                                <#elseif item.type == 'document'>
                                    文档
                                <#elseif item.type == 'flash'>
                                    Flash
                                <#else>
                                    ${fastLib.duration(item.length)}
                                </#if>
							</span>
                            <#else>
                                <span class="item-length">未发布</span>
                            </#if>
                        </a>
                        <div class="item-body">
                            <div class="item-seq-name">课时${item.number}:</div>
                            <div class="item-title">
                                <a href="#modal" data-toggle="modal"
                                   data-url="${ctx}/course/${item.courseId}/lesson/${item.id}/preview"
                                   title="${item.title}">${item.title}</a>
                            </div>

                            <div class="text-muted" style="font-weight:normal;font-size:12px;color:#aaa;">

                                <#if item.type == 'live'>

                                    <#if item.startTime gt currentTime>
                                        <span>${item.startTime?number_to_date?string('MM月dd日')}
                                            <#assign weeks = {'Mon':'一','Tus':'二','Wed':'三','Thu':'四','Fri':'五','Sat':'六','Sun':'日'}/>
                                            星期${weeks[item.startTime?number_to_date?string('E')]}
                                            ${item.startTime?number_to_time?string('HH:mm')}
										</span>
                                    <#elseif item.startTime lte currentTime && item.endTime gte currentTime>
                                        <span class="text-warning">正在直播中</span>
                                    <#elseif item.endTime lt currentTime>
                                        <span>直播结束</span>
                                    </#if>

                                </#if>

                            </div>


                        </div>
                    </li>
                </#list>
            </ul>
        </#if>
    </#list>
</div>