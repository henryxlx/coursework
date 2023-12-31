<table id="user-table" class="table table-striped table-hover" data-search-form="#user-search-form"
       style="word-break:break-all;">
    <thead>
    <tr>
        <th>课时</th>
        <th>课时类型</th>
        <th>创建者</th>
    </tr>
    </thead>
    <tbody>
    <#if lessonDetail??>
        {% for data in lessonDetail %}
        <tr>
            <td style="width:50%"><strong>{{ data.title }}</strong><br><span
                        class="text-muted text-sm">属于课程:{{courses[data.courseId].title}}</span></td>
            <td>{% if data.type=="text" %}图文
                {% elseif data.type=="video" %}视频
                {% elseif data.type=="audio" %}音频
                {% elseif data.type=="ppt" %}PPT
                {% elseif data.type=="testpaper" %}试卷
                {% elseif data.type=="live" %}直播
                {% elseif data.type=="flash" %}Flash
                {% elseif data.type=="document" %}文档
                {% endif %}
            </td>
            <td>{{ admin_macro.user_link(users[courses[data.courseId].userId]) }}<br>{{data.createdTime | date
                ("Y-m-d H:i:s")}}
            </td>
        </tr>
        {% endfor %}
    </#if>
    </tbody>
</table>
<@web_macro.paginator paginator! />