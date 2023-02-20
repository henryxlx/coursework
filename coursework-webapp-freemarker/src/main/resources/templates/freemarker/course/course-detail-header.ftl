<div class="row course-cover-heading">
    <div class="col-sm-5 picture">

        <#if course.type != 'live' && setting('course.picturePreview_enabled', '0') != '0'  && freeLesson?? >
            <a href="#modal" data-toggle="modal"
               data-url="${ctx}/course/${freeLesson.courseId}/lesson/${freeLesson.id}/preview">
                <span class="glyphicon glyphicon-play-circle"
                      style="font-size:80px;position:absolute;top:30%;right:40%;"></span>
                <img src="${default_path('coursePicture', course.largePicture, '')}" class="img-responsive"/>
                <#if course.serializeMode == 'serialize'>
                    <span class="tag-serial"></span>
                </#if>
            </a>
        <#else>
            <img src="${default_path('coursePicture', course.largePicture, '')}" class="img-responsive"/>
            <#if course.serializeMode == 'serialize'>
                <span class="tag-serial"></span>
            </#if>
        </#if>

    </div>
    <div class="col-sm-7 info">

        <#if is_granted('ROLE_ADMIN') >
            <div class="btn-group pull-right">
                <a class="btn btn-default btn-sm" type="button" href="${ctx}/course/${course.id}/manage" title="课程管理">
                    <i class="esicon esicon-setting"></i>
                </a>
            </div>
        </#if>

        <h1 class="title">${course.title}
            <#if course.type == 'live'>
                <span class="label label-success">直播</span>
            </#if>
        </h1>
        <div class="subtitle">${course.subtitle}</div>
        <#include '/course/course-detail-price.ftl' />

        <div class="stats">
      <span class="stat-item">
        <span class="stat-item-label">评价</span>
        <span class="stars-${course.rating}">&nbsp;</span>
        <span>(${course.ratingNum}人)</span>
      </span>

            <#if setting('course.show_student_num_enabled', '1') == '1'>
                <span class="stat-divider">|</span>
                <span class="stat-item">
          <span class="stat-item-label">${setting('default.user_name', '学员')}</span>
          <span class="member-num">${course.studentNum}人</span>
          <#if course.type == 'live'>
              <#if course.studentNum == course.maxStudentNum>
                  <span class="member-text hidden-sm hidden-xs">(名额已满)</span>
            <#else>
                  <span class="member-text hidden-sm hidden-xs"> (${course.maxStudentNum}名额)</span>
              </#if>
          </#if>
        </span>
            </#if>

            <#if course.expiryDay != 0 >
                <span class="stat-divider">|</span>
                <span class="stat-item">
          <span class="stat-item-label">有效期</span>
          <span class="member-num">${course.expiryDay} 天</span>
        </span>
            </#if>
        </div>


        <div class="actions clearfix">
            <div class="pull-left">
                <a class="btn btn-primary btn-fat " id="course-buy-btn"
                        <#if setting("course.buy_fill_userinfo", "false") != "false">
                            href="#modal"
                            data-toggle="modal"
                            data-url="${ctx}/course/${course.id}/buy?targetType=course"
                        <#else>
                            href="${ctx}/order/show?targetId=${course.id}&targetType=course"
                        </#if>
                        <#if course.status != 'published' || (course.type == 'live' && course.studentNum >= course.maxStudentNum)>
                            disabled="disabled"
                        </#if>
                >
                    <#if setting('coin.coin_enabled')?? && setting('coin.price_type', '') == 'Coin'>
                    <#if course.coinPrice gt 0>购买课程<#else>加入学习</#if></a>
                <#else>
                    <#if course.price gt 0>购买课程<#else>加入学习</#if></a>
                </#if>
                </a>

                <#if courseMemberLevel??>
                    <#if checkMemberLevelResult == 'not_login' >
                        <a href="${ctx}/login" class="btn btn-link btn-link-warning">${courseMemberLevel.name}，免费学</a>
                    <#elseif checkMemberLevelResult == 'not_member' || checkMemberLevelResult == 'member_expired' >
                        <a href="${ctx}/vip/buy" class="btn btn-link btn-link-warning">${courseMemberLevel.name}，免费学</a>
                    <#elseif checkMemberLevelResult == 'level_low' >
                        <a href="${ctx}/vip/upgrade?level=${courseMemberLevel.id}"
                           class="btn btn-link btn-link-warning">${courseMemberLevel.name}，免费学</a>
                    <#elseif checkMemberLevelResult == 'ok' >
                        <a href="javascript:;" data-url="${ctx}/course/${course.id}/become_use_member"
                           class="btn btn-link btn-link-warning become-use-member-btn">${courseMemberLevel.name}，免费学</a>
                    </#if>
                </#if>
            </div>

            <div class="dropdown pull-left">
                <a href="javascript:;" class="btn btn-link" data-toggle="dropdown"><span
                            class="glyphicon glyphicon-share"></span> 分享到</a>
                <#assign type = 'course'/>
                <#include '/common/share-dropdown.ftl' />
            </div>


            <div class="pull-left hidden-xs">
                <a class="btn btn-link" href="#" style="display:none;"> <i class="glyphicon glyphicon-play-circle"></i>
                    免费体验
                </a>
                <a class="btn btn-link" id="favorite-btn" href="javascript:"
                   data-url="${ctx}/course/${course.id}/favorite" <#if hasFavorited??>style="display:none;"</#if>>
                    <i class="glyphicon glyphicon-bookmark"></i>
                    收藏课程
                </a>
                <a class="btn btn-link" id="unfavorite-btn" href="javascript:"
                   data-url="${ctx}/course/${course.id}/unfavorite"
                   <#if hasFavorited??><#else>style="display:none;"</#if>>
                    <i class="glyphicon glyphicon-bookmark"></i>
                    已收藏
                </a>
            </div>

        </div>

    </div>
</div>