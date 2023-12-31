<#if sortedCourses??>
<table class="table table-condensed table-striped table-hover" style="word-break: break-all;">
    <thead>
    <tr>
        <th>课程名称</th>
        <th>新增学生人数</th>
        <th>总学生人数</th>
        <th>新增收入</th>
    </tr>
    </thead>
    <tbody>
    {% for sortedcourse in sortedCourses %}
    <tr>
        <td>
            <a href="{{ path('course_show', {id: sortedcourse.courseId}) }}" target="_blank">{{ sortedcourse.title }}</a>
        </td>
        <td>{{ sortedcourse['addedStudentNum'] }} 人 </td>
        <td>{{ sortedcourse['studentNum'] }} 人</td>
        <td class="money-text">{{sortedcourse['addedMoney'] }} 元 </td>
    </tr>
    {% endfor %}
    </tbody>
</table>
<#else>
<div class="empty">暂无受欢迎课程</div>
</#if>