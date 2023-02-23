<ul class="course-wide-list">
    <#list courses! as course>
        <li class="course-item clearfix">
            <a class="course-picture-link" href="${ctx}/course/${course.id}">
                <img class="course-picture" src="${default_path('coursePicture',course.middlePicture, 'large')}"
                     alt="${course.title}">
            </a>

            <div class="course-body">

                <div style="float:right;" class="text-muted mrm mls">
                    <#assign shows=['price', 'discount']/>
                    <#include "/course/price-widget.ftl"/>
                </div>

                <h4 class="course-title"><a href="${ctx}/course/${course.id}">${course.title}</a>
                    <#if course.serializeMode=='serialize'>
                        <span class="label label-success ">更新中</span>
                    <#elseif course.serializeMode=='finished'>
                        <span class="label label-warning ">已完结</span>
                    </#if>
                    <#if course.type == 'live'>
                        <#assign lesson = course.lesson!/>
                        <#if lesson?? && .now?datetime gte lesson.startTime && .now?datetime lte lesson.endTime>
                            <span class="label label-warning series-mode-label">正在直播中</span>
                        <#else>
                            <span class="label label-success series-mode-label">直播</span>
                        </#if>
                    </#if>
                </h4>

                <#if course.type == 'live'>
                    <#assign lesson = course.lesson!/>
                    <#if lesson??>
                        <div class="live-course-lesson mbm">
                            <span class="text-success fsm mrm">${lesson.startTime?number_to_datetime?string('MM月dd日 HH:mm') }} ~ ${lesson.endTime?number_to_datetime?string('HH:mm')}</span>
                            <span class="text-muted fsm mrm">第${lesson.number}课时</span>
                        </div>
                    </#if>
                <#else>
                    <div class="course-about ellipsis">${course.subtitle}</div>
                </#if>

                <div class="course-footer clearfix">
                    <#assign teacher = users[course.teacherIds[0]!]!'null' />
                    <#if teacher?? && teacher != 'null'>
                        <div class="teacher">
                            <a href="${ctx}/user/${teacher.id}"><img
                                        src="${default_path('avatar',teacher.smallAvatar, '')}" class="teacher-avatar"></a>
                            <a class="teacher-nickname ellipsis"
                               href="${ctx}/user/${teacher.id}">${teacher.username}</a>
                            <span class="teacher-title ellipsis">${teacher.title}</span>
                        </div>
                    </#if>

                    <div class="course-metas">
                        <span class="stars-${course.rating}" number_format>&nbsp;</span>
                        <#if setting('course.show_student_num_enabled', '1') == '1'>
                            <span class="divider"></span>
                            <span class="text-muted mrm mls"><strong>${course.studentNum}</strong>${setting('default.user_name')!'学员'}</span>
                        </#if>

                    </div>
                </div>
            </div>
        </li>
    </#list>
</ul>