<table id="user-table" class="table table-striped table-hover" data-search-form="#user-search-form">
    <thead>
    <tr>
        <th>课时</th>
        <th>课时类型</th>
        <th>${setting('default.user_name')!'学员'}</th>
    </tr>
    </thead>
    <tbody>
    <#if finishedLessonDetail??>
        {% for data in finishedLessonDetail %}
        <tr>
            <td><strong>{{lessons[data.lessonId].title | default
                    ("")}}</strong><br><span class="text-muted text-sm">属于课程:{{courses[data.courseId].title}}</span>
            </td>
            <td>{% if lessons[data.lessonId].type=="text" %}图文
                {% elseif lessons[data.lessonId].type=="video" %}视频
                {% elseif lessons[data.lessonId].type=="audio" %}音频
                {% elseif lessons[data.lessonId].type=="ppt" %}PPT
                {% elseif lessons[data.lessonId].type=="testpaper" %}试卷
                {% endif %}
            </td>
            <td>{{ admin_macro.user_link(users[data.userId]) }}<br>{{data.finishedTime | date
                ("Y-m-d H:i:s")}}
            </td>
        </tr>
        {% endfor %}
    </#if>
    </tbody>
</table>
<@web_macro.paginator paginator! />