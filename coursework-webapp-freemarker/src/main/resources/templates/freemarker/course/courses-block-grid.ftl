<style>
    .classroomPicture {

        width: 24px;
        height: 24px;
    }
</style>

<ul class="course-grids">
    <#list courses! as course>
        <li class="course-grid">
            <a href="{{ path('course_show', {id:course.id}) }}" class="grid-body">
                <img src="{{ default_path('coursePicture',course.largePicture, 'large') }}"
                     class="img-responsive thumb">
                {% if course.status == 'draft' %}
                <span class="label  label-warning course-status">未发布</span>
                {% elseif course.status == 'closed' %}
                <span class="label label-danger course-status">已关闭</span>
                {% endif %}
                {% if course.status != 'closed' %}
                {% if course.serializeMode=='serialize' %}
                <span class="label label-success series-mode-label">更新中</span>
                {% elseif course.serializeMode=='finished' %}
                <span class="label label-warning series-mode-label">已完结</span>
                {% endif %}
                {% endif %}
                {% if course.type == 'live' %}
                {% set lesson = course['lesson']|default(null) %}
                {% if lesson and "now"|date("U") >= lesson.startTime and "now"|date("U") <= lesson.endTime %}
                <span class="label label-warning series-mode-label">正在直播中</span>
                {% else %}
                <span class="label label-success series-mode-label">直播</span>
                {% endif %}
                {% endif %}

                <span class="title">{{ course.title }}</span>
                {% if mode in ['default', 'teach'] %}

                {% if course.type == 'live' %}
                {% set lesson = course['lesson']|default(null) %}
                {% if lesson %}
                <span class="live-course-lesson metas">
                <span class="text-success mrm">{{ lesson.startTime | date
                    ('n月j日 H:i') }} ~ {{ lesson.endTime | date
                    ('H:i') }}</span>
                <span class="text-muted mrm">第{{ lesson.number }}课时</span>
              </span>
                {% endif %}
                {% endif %}

                <span class="metas clearfix">
            <span class="price-col">
              <span class="meta-label">价格</span>
              {% if setting('coin.coin_enabled') and setting('coin.price_type') == 'Coin' %}
              <span class="price-num"
                    style="display:inline ">{% if course.coinPrice > 0 %}{{ course.coinPrice }}{{setting('coin.coin_name')}}{% else %}免费{% endif %}</span>
              {% else %}
              <span class="price-num">{% if course.price > 0 %}{{ course.price }}元{% else %}免费{% endif %}</span>
              {% endif %}
            </span>

            {% if setting('course.show_student_num_enabled', '1') == 1 %}
            <span class="student-col">
              <span class="meta-label">{{setting('default.user_name', '学员')}}</span>
              <span class="student-num">{{ course.studentNum }}人</span>
            </span>
            {% endif %}

            <span class="review-col">
              {% if course.ratingNum > 0 %}
                <span class="meta-label"><strong>{{ course.ratingNum }}</strong> 评价</span>
                <span class="review-rating">
                  <span class="stars-{{ (course.rating)|number_format }}">&nbsp;</span>
                </span>
              {% else %}
                <span class="meta-label">无评价</span>
                <span class="review-rating">
                  <span class="stars-0">&nbsp;</span>
                </span>
              {% endif %}
            </span>

          </span>
                {% endif %}

                {% if mode in ['default'] %}
                {% set user = users[course.teacherIds|first]|default(null) %}
                {% if user %}
                <span class="teacher clearfix">
              <img src="{{ default_path('avatar',user.smallAvatar, '') }}" class="thumb">
              <span class="nickname ellipsis">{{ user.nickname }}</span>
              <span class="user-title ellipsis">{{ user.title }}</span>
            </span>
                {% endif %}
                {% endif %}

                {% if mode in ['learn'] %}
                <div class="learn-status">

                    {% if is_plugin_installed('Classroom') and setting('classroom.enabled')|default(0) %}
                    <div class="mbm">
                        {% if course.classroomCount > 0 %}
                        {% set classroom = course.classroom %}
                        <img class="classroomPicture"
                             src="{{ default_path('classroomPicture',classroom.smallPicture, '') }}"> <span
                                class="text-muted">{{classroom.title}}
                {% if course.classroomCount > 1 %}
                等
                {% endif %}</span>
                        {% endif %}
                    </div>
                    {% endif %}


                    {% if course.memberIsLearned %}
                    <div class="progress">
                        <div class="progress-bar progress-bar-success" style="width: 100%;"></div>
                    </div>
                    学习总时长:{{course.learnTime}}
                    <div class="action clearfix"><span class="btn btn-default btn-sm pull-right">查看课程</span></div>
                    {% else %}
                    <div class="progress">
                        <div class="progress-bar progress-bar-success"
                             style="width: {{ percent(course.memberLearnedNum, course.lessonNum) }};"></div>
                    </div>
                    <div class="action"><span class="btn btn-primary btn-sm">继续学习</span></div>
                    {% endif %}
                </div>
                {% if course.status == 'draft' %}
                <span class="label  label-warning course-status">未发布</span>
                {% elseif course.status == 'closed' %}
                <span class="label label-danger course-status">已关闭</span>
                {% endif %}
                {% endif %}

                {% if mode == 'teach' %}
                {% if course.status == 'published' %}
                <span class="label label-success course-status">已发布</span>
                {% elseif course.status == 'draft' %}
                <span class="label  label-warning course-status">未发布</span>
                {% elseif course.status == 'closed' %}
                <span class="label label-danger course-status">已关闭</span>
                {% endif %}
                {% endif %}

            </a>
        </li>
    </#list>
</ul>