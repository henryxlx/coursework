<#assign script_controller = 'course/common' />

<#include '/course/dashboard-layout.ftl' />

<#macro blockDashboardMain>

    <#assign nav = 'material' />
    <#include '/course/dashboard-nav.ftl' />

    <ul class="media-list">
        <#list materials! as material>
            {% set lesson = lessons[material.lessonId]|default(null) %}
            <li class="media">
                <div class="media-body">
                    <div class="mbs">
                        {% if lesson and lesson.status != 'published' %}
                        {{ material.title }} <span class="text-muted text-sm">(课时未发布，不能下载该资料)</span>
                        {% else %}
                        {% if material.link %}
                        <a href="{{ material.link }}" target="_blank">{{ material.title }}</a>
                        <span class="glyphicon glyphicon-new-window text-muted text-sm" title="网络链接资料"></span>
                        {% else %}
                        <a href="{{ path('course_material_download', {courseId:course.id, materialId:material.id}) }}"
                           target="_blank">{{ material.title }}</a>
                        {% endif %}
                        {% endif %}
                    </div>

                    {% if material.description and not material.link %}
                    <div class="text-muted text-sm mbs">
                        {{ material.description | plain_text
                        (100) }}
                    </div>
                    {% endif %}

                    <div class="text-sm">
                        {% if material.fileId > 0 %}
                        <span class="text-muted">{{ material.fileSize | file_size }}</span>
                        <span class="bullet">•</span>
                        {% endif %}
                        {% if lesson %}
                        <a class="link-muted" href="{{ path('course_learn', {id:course.id}) }}#lesson/{{lesson.id}}"
                           title="{{ lesson.title }}">课时{{ lesson.number }}</a>
                        <span class="bullet">•</span>
                        {% endif %}
                        <span class="text-muted">上传于{{ material.createdTime | smart_time }}</span>
                    </div>

                </div>
            </li>
        <#else>
            <li class="empty tac text-muted mvl">课程暂无资料</li>
        </#list>
    </ul>
</#macro>