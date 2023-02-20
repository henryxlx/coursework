<tr id="course-tr-${course.id}" data-sort="${course.recommendedSeq}">
    <td>${course.recommendedSeq}</td>
    <td>
        <a href="${ctx}/course/${course.id}" target="_blank"><strong>${course.title}</strong></a>
        <br>
        <span class="text-muted text-sm">分类：${category.name!'--'}</span>
    </td>
    <td>
        <@admin_macro.user_link user! />
        <br>
        <span class="text-muted text-sm">${course.createdTime?number_to_datetime?string('yyyy-MM-dd HH:mm')}</span>
    </td>
    <td>
        ${course.recommendedTime?number_to_datetime?string('yyyy-MM-dd')}
    </td>
    <td>
        <div class="btn-group">
            <a class="btn btn-default btn-sm" href="#modal" data-toggle="modal"
               data-url="${ctx}/admin/course/${course.id}/recommend?ref=recommendList">设置序号</a>

            <a href="#" type="button" class="btn btn-default btn-sm dropdown-toggle" data-toggle="dropdown">
                <span class="caret"></span>
            </a>
            <ul class="dropdown-menu pull-right">

                <li><a class="cancel-recommend-course" href="javascript:"
                       data-url="${ctx}/admin/course/${course.id}/recommend/cancel"><span
                                class="glyphicon glyphicon-hand-right"></span> 取消推荐</a></li>

            </ul>
        </div>
    </td>
</tr>