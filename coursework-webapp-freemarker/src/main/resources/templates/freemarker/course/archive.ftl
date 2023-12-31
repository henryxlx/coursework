<@block_title '课程存档'/>

<#include '/default/layout.ftl'>

<#macro blockContent>

    <div class="es-row-wrap container-gap">
        <div class="row">
            <div class="col-md-12">
                <div class="page-header"><h1>课程存档</h1></div>
            </div>

            <div class="col-md-12">
                <#if courses??>
                    <ul class="media-list">
                        {% for course in courses %}
                        {% set teacher = users[course.teacherIds|first]|default(null) %}
                        {% set tags = course.tags %}
                        <li class="media">
                            <h4><a href="{{ path('course_archive_show', {id:course.id}) }}">{{ course.title }}</a></h4>
                            <div class="fsn text-muted">
                                {% if teacher %}
                                教师： <a class="teacher-nickname mrl" href="{{ path('user_show', {id:teacher.id}) }}">{{ teacher.nickname }}</a>
                                {% endif %}
                                {% if tags %}
                                <span class="text-muted">标签：</span>
                                {% for tag in tags %}
                                <a href="{{ path('tag_show', {name:tag.name}) }}" class="mrs">{{ tag.name }}</a>
                                {% endfor %}
                                {% endif %}
                            </div>
                        </li>
                        {% endfor %}
                    </ul>
                <#else>
                    <div class="empty">目前暂时无课程</div>
                </#if>
                <@web_macro.paginator paginator!/>
            </div>
        </div>

    </div>
</#macro>