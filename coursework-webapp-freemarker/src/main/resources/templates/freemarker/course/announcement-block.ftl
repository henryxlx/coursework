<#if canManage??>
    <div class="panel panel-default">
        <div class="panel-heading">
            <div class="pull-right">
                <a href="#modal" data-toggle="modal"
                   data-url="${ctx}/course/${course.id}/announcement/create" class="btn btn-link btn-xs"><i
                            class="md md-add"></i>添加公告</a>
            </div>
            <h3 class="panel-title">课程公告</h3>
        </div>
        <div class="panel-body">
            <ul class="media-list announcement-list">
                <#list announcements! as announcement>
                    <li class="media">
                        <div class="media-body">
                            <p>
                                {% if canTake %}
                                <a id="course-buy-btn" href="#modal" data-toggle="modal"
                                   data-url="{{ path('course_announcement_show', {courseId:course.id, id:announcement.id}) }}">{{ announcement.content | plain_text
                                    (40) | default
                                    ('<span class="text-warning">(请点击查看)</span>')|raw }}</a>
                                {% else %}
                                <a id="course-buy-btn"
                               {% if setting("course.buy_fill_userinfo")|default(false) %}
                            href="#modal"
                            data-toggle="modal"
                            data-url="{{ path('course_buy', {id: course.id, targetType: 'course'}) }}"
                            {% else %}
                            href="{{ path('order_show', {targetId: course.id, targetType: 'course'}) }}"
                            {% endif %}
                            >{{ announcement.content | plain_text
                            (40) | default
                            ('<span class="text-warning">(请点击查看)</span>')|raw }}</a>
                            {% endif %}
                        </p>
                        <div class="clearfix">
                            <span class="pull-right text-muted text-sm">{{ announcement.createdTime | date
                                ('Y-m-d H:i') }}</span>
                            <a class="text-muted text-sm mrm action" href="javascript:;" data-target="#modal"
                               data-toggle="modal"
                               data-url="{{ path('course_announcement_update',{courseId:course.id, id:announcement.id}) }}"><span
                                        class="glyphicon glyphicon-pencil"></span> 修改</a>
                            <a class="text-muted text-sm action" href="#" data-role="delete"
                               data-url="{{ path('course_announcement_delete',{courseId:course.id, id:announcement.id}) }}"><span
                                        class="glyphicon glyphicon-trash"></span> 删除</a>
                        </div>
                        </div>
                    </li>
                <#else>
                    <div class="empty">暂无课程公告</div>
                </#list>
            </ul>
        </div>
    </div>

<#elseif announcements??>
    <div class="panel panel-default">
        <div class="panel-heading">
            <h3 class="panel-title">课程公告</h3>
        </div>

        <div class="panel-body">
            <ul class="media-list announcement-list">
                <#list announcements! as announcement>
                    <li class="media">
                        <div class="media-body">
                            <p>
                                {% if canTake %}
                                <a id="course-buy-btn" href="#modal" data-toggle="modal"
                                   data-url="{{ path('course_announcement_show', {courseId:course.id, id:announcement.id}) }}">{{ announcement.content | plain_text
                                    (40) | default
                                    ('<span class="text-warning">(请点击查看)</span>')|raw }}</a>
                                {% else %}
                                <a id="course-buy-btn"
                               {% if setting("course.buy_fill_userinfo")|default(false) %}
                            href="#modal"
                            data-toggle="modal"
                            data-url="{{ path('course_buy', {id: course.id, targetType: 'course'}) }}"
                                {% else %}
                                href="{{ path('order_show', {targetId: course.id, targetType: 'course'}) }}"
                                {% endif %}
                                >{{ announcement.content | plain_text
                                (40) | default
                                ('<span class="text-warning">(请点击查看)</span>')|raw }}</a>
                                {% endif %}
                            </p>
                        </div>
                    </li>
                </#list>
            </ul>
        </div>
    </div>
</#if>