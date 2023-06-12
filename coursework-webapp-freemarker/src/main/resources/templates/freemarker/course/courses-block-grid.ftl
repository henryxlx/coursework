<style>
    .classroomPicture {

        width: 24px;
        height: 24px;
    }
</style>

<ul class="course-grids">
    <#list courses! as course>
        <li class="course-grid">
            <a href="${ctx}/course/${course.id}" class="grid-body">
                <img src="${default_path('coursePicture',course.largePicture, 'large')}"
                     class="img-responsive thumb">
                <#if course.status == 'draft'>
                    <span class="label  label-warning course-status">未发布</span>
                <#elseif course.status == 'closed'>
                    <span class="label label-danger course-status">已关闭</span>
                </#if>
                <#if course.status != 'closed'>
                    <#if course.serializeMode=='serialize'>
                        <span class="label label-success series-mode-label">更新中</span>
                    <#elseif course.serializeMode=='finished'>
                        <span class="label label-warning series-mode-label">已完结</span>
                    </#if>
                </#if>
                <#if course.type == 'live'>
                    <#assign lesson = course['lesson']!'N/A' />
                    <#if lesson != 'N/A' && .now gte lesson.startTime && .now lte lesson.endTime>
                        <span class="label label-warning series-mode-label">正在直播中</span>
                    <#else>
                        <span class="label label-success series-mode-label">直播</span>
                    </#if>
                </#if>

                <span class="title">${course.title}</span>

                <#if ['default', 'teach']?seq_contains(mode)>

                    <#if course.type == 'live'>
                        <#assign lesson = course['lesson']!'N/A' />
                        <#if lesson != 'N/A'>
                            <span class="live-course-lesson metas">
                <span class="text-success mrm">${lesson.startTime?number_to_datetime?string('MM月dd日 HH:mm')} ~
                ${lesson.endTime?number_to_datetime?string('HH:mm')}</span>
                <span class="text-muted mrm">第${lesson.number}课时</span>
              </span>
                        </#if>
                    </#if>

                    <span class="metas clearfix">
            <span class="price-col">
              <span class="meta-label">价格</span>
              <#if setting('coin.coin_enabled', '0') == '1' && setting('coin.price_type', '') == 'Coin'>
                  <span class="price-num"
                        style="display:inline "><#if course.coinPrice gt 0>${course.coinPrice}${setting('coin.coin_name', '')}<#else>免费</#if></span>
              <#else>
                  <span class="price-num"><#if course.price gt 0>${course.price}元<#else>免费</#if></span>
              </#if>
            </span>

            <#if setting('course.show_student_num_enabled', '1') == '1'>
                <span class="student-col">
              <span class="meta-label">${setting('default.user_name', '学员')}</span>
              <span class="student-num">${course.studentNum}人</span>
            </span>
            </#if>

            <span class="review-col">
              <#if course.ratingNum gt 0>
                  <span class="meta-label"><strong>${course.ratingNum}</strong> 评价</span>
                  <span class="review-rating">
                  <span class="stars-${course.rating!}">&nbsp;</span>
                </span>
              <#else>
                  <span class="meta-label">无评价</span>
                  <span class="review-rating">
                  <span class="stars-0">&nbsp;</span>
                </span>
              </#if>
            </span>

          </span>
                </#if>

                <#if mode == 'default'>
                    <#assign user = users[course.teacherIds[0]]! />
                    <#if user??>
                        <span class="teacher clearfix">
              <img src="${default_path('avatar',user.smallAvatar, '')}" class="thumb">
              <span class="nickname ellipsis">${user.username}</span>
              <span class="user-title ellipsis">${user.title}</span>
            </span>
                    </#if>
                </#if>

                <#if mode == 'learn' >
                    <div class="learn-status">

                        <#if setting('classroom.enabled', '0') == '1'>
                            <div class="mbm">
                                <#if course.classroomCount gt 0>
                                    <#assign classroom = course.classroom />
                                    <img class="classroomPicture"
                                         src="${default_path('classroomPicture',classroom.smallPicture, '')}"> <span
                                        class="text-muted">${classroom.title}
                                    <#if course.classroomCount gt 1 >等</#if></span>
                                </#if>
                            </div>
                        </#if>


                        <#if course.memberIsLearned?? && course.memberIsLearned gt 0>
                            <div class="progress">
                                <div class="progress-bar progress-bar-success" style="width: 100%;"></div>
                            </div>
                            学习总时长:${course.learnTime!0}
                            <div class="action clearfix"><span class="btn btn-default btn-sm pull-right">查看课程</span>
                            </div>
                        <#else>
                            <div class="progress">
                                <div class="progress-bar progress-bar-success"
                                     style="width: ${fastLib.percent(course.memberLearnedNum!, course.lessonNum!)};"></div>
                            </div>
                            <div class="action"><span class="btn btn-primary btn-sm">继续学习</span></div>
                        </#if>
                    </div>
                    <#if course.status == 'draft'>
                        <span class="label  label-warning course-status">未发布</span>
                    <#elseif course.status == 'closed'>
                        <span class="label label-danger course-status">已关闭</span>
                    </#if>
                </#if>

                <#if mode == 'teach'>
                    <#if course.status == 'published'>
                        <span class="label label-success course-status">已发布</span>
                    <#elseif course.status == 'draft'>
                        <span class="label  label-warning course-status">未发布</span>
                    <#elseif course.status == 'closed'>
                        <span class="label label-danger course-status">已关闭</span>
                    </#if>
                </#if>

            </a>
        </li>
    </#list>
</ul>