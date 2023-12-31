<table id="user-table" class="table table-striped table-hover" data-search-form="#user-search-form">
    <thead>
    <tr>
        <th>用户名</th>
        <th>加入课程</th>
        <th>加入时间</th>
        <th>课程价格</th>
    </tr>
    </thead>
    <tbody>
    <#if JoinLessonDetail??>
        {% for data in JoinLessonDetail %}
        {% if courses[data.targetId].title|default(null) %}
        <tr>
            <td> {{ admin_macro.user_link(users[data.userId]) }}</td>
            <td><a href="{{ path('course_show', {id:data.targetId}) }}"
                   target="_blank"><strong>{{ courses[data.targetId].title | default
                        ("") }}</strong></a><br></td>
            <td>{{data.createdTime | date
                ("Y-m-d H:i:s")}}
            </td>
            <td class="text-danger">{% if data.amount==0 %}免费{% else %}{{data.amount}}{% endif %}</td>
        </tr>
        {% else %}
        <tr>
            <td> {{ admin_macro.user_link(users[data.userId]) }}</td>
            <td><strong>此课程已不存在</strong><br></td>
            <td>{{data.createdTime | date
                ("Y-m-d H:i:s")}}
            </td>
            <td class="text-danger">{% if data.amount==0 %}免费{% else %}{{data.amount}}{% endif %}</td>
        </tr>
        {% endif %}
        {% endfor %}
    </#if>
    </tbody>
</table>
<@web_macro.paginator paginator! />