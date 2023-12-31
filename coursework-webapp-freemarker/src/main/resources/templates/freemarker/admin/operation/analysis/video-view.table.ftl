<table id="user-table" class="table table-striped table-hover" data-search-form="#user-search-form">
    <thead>
    <tr>
        <th>用户名</th>
        <th>课时</th>
        <th>观看时间</th>
        <th>视频类型</th>
    </tr>
    </thead>
    <tbody>
    <#if videoViewedDetail??>
        {% for key,val in videoViewedDetail %}

        {% if  lessons[val.lessonId].title|default(null)  %}
        <tr>
            <td>{{ users[val.userId].nickname }}</td>
            <td>{{ lessons[val.lessonId].title }}</td>
            <td>{{val.createdTime | data_format}}</td>
            <td>{{dict_text('videoStorageType', val.fileStorage)}}</td>
        </tr>
        {% else %}
        <tr class="danger">
            <td>{{ users[val.userId].nickname }}</td>
            <td>(此课时已删除)</td>
            <td>{{val.createdTime | data_format}}</td>
            <td>{{dict_text('videoStorageType', val.fileStorage)}}</td>
        </tr>
        {% endif %}
        {% endfor %}
    </#if>
    </tbody>
</table>
<@web_macro.paginator paginator! />