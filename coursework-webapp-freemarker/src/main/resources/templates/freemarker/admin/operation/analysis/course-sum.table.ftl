<table id="user-table" class="table table-striped table-hover" data-search-form="#user-search-form">
    <thead>
    <tr>
        <th>课程</th>
        <th>创建者</th>
        <th>${setting('default.user_name')!'学员'}数</th>
        <th>价格</th>
    </tr>
    </thead>
    <tbody>
    <#if courseSumDetail??>
        {% for data in courseSumDetail %}
        <tr>
            <td><a href="{{ path('course_show', {id:data.id}) }}" target="_blank"><strong>{{ data.title
                        }}</strong></a><br><span
                        class="text-muted text-sm">分类：{{ categories[data.categoryId].name | default
                    ('--') }}</span></td>
            <td>{{ admin_macro.user_link(users[data.userId]) }}<br>{{data.createdTime | date
                ("Y-m-d H:i:s")}}
            </td>
            <td>{{data.studentNum}}</td>
            <td class="text-danger">{{data.price}}</td>
        </tr>
        {% endfor %}
    </#if>
    </tbody>
</table>
<@web_macro.paginator paginator! />