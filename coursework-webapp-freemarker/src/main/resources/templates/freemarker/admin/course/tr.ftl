<tr id="course-tr-${course.id}">
    <td>${course.id}</td>
    <td>
        <a href="${ctx}/course/${course.id}"
           target="_blank"><strong>${course.title}</strong></a><#if course.type == 'live'><span
            class="label label-success live-label mls">直播</span></#if>
        <br>
        <span class="text-muted text-sm">分类：${(category.name)!'--'}</span>
        <#if course.recommended??>
            <span class="label label-default">荐:${course.recommendedTime?number_to_datetime?string('yyyy-MM-dd HH:mm:ss')} / 序号:${course.recommendedSeq}</span>
        </#if>
    </td>
    <td><#if course.serializeMode == 'none'><span
                class="text-info">非连载课程</span><#elseif course.serializeMode == 'serialize'><span class="text-success">连载中</span><#elseif course.serializeMode == 'finished'>
            <span class="text-danger">已完结</span></#if></td>
    <td>${course.studentNum}</td>
    <td><span class="money-text">${course.income}</span></td>
    <td>${dict_text('courseStatus4html', course.status)}</td>
    <td>
        <@admin_macro.user_link user />
        <br>
        <span class="text-muted text-sm">${course.createdTime?number_to_datetime?string('yyyy-MM-dd HH:mm:ss')}</span>
    </td>
    <td>
        <div class="btn-group">
            <a class="btn btn-default btn-sm" href="${ctx}/course/${course.id}/manage" target="_blank">管理</a>

            <a href="#" type="button" class="btn btn-default btn-sm dropdown-toggle" data-toggle="dropdown">
                <span class="caret"></span>
            </a>
            <ul class="dropdown-menu pull-right">

                <#if course.recommended?? && course.recommended == 0>
                    <li><a class="recommend-course" href="#modal" data-toggle="modal"
                           data-url="${ctx}/admin/course/${course.id}/recommend?ref=courseList"><span
                                    class="glyphicon glyphicon-hand-up"></span> 推荐课程</a></li>
                </#if>

                <#if course.recommended?? && course.recommended != 0>
                    <li><a class="cancel-recommend-course" href="javascript:"
                           data-url="${ctx}/admin/course/${course.id}/recommend/cancel"><span
                                    class="glyphicon glyphicon-hand-right"></span> 取消推荐</a></li>
                </#if>

                <li ><a class="copy-course" id="copy-course" href="javascript:"
                        <#if course.type =="live">
                    title="直播课程不支持复制"
                    style="color:#909090"
                        </#if>
                    data-toggle="modal"
                    data-target="#modal"
                    data-url="${ctx}/admin/course/${course.id}/copy"
                    data-type="${course.type}"
                    ><span class="glyphicon glyphicon-plus-sign"></span> 复制课程</a></li>

                <li><a href="${ctx}/course/${course.id}?previewAs=guest" target="_blank"><span class="glyphicon glyphicon-eye-open"></span> 预览：作为未购买用户</a></li>
                <li><a href="${ctx}/course/${course.id}?previewAs=member" target="_blank"><span class="glyphicon glyphicon-eye-open"></span> 预览：作为已购买用户</a></li>

                <li class="divider"></li>

                <#if course.status == 'published'>
                <li><a class="close-course" href="javascript:" data-url="${ctx}/admin/course/${course.id}/close" data-user="${default.user_name!'学员'}"><span class="glyphicon glyphicon-ban-circle"></span> 关闭课程</a></li>
                </#if>

                <#if course.status != 'published'>
                <li><a class="publish-course" href="javascript:" data-url="${ctx}/admin/course/${course.id}/publish"><span class="glyphicon glyphicon-ok-circle"></span> 发布课程</a></li>
                </#if>

                <#if course.status == 'draft'>
                <li class="divider"></li>

                <li><a class="delete-course" href="javascript:" data-url="${ctx}/admin/course/${course.id}/delete"
                       data-chapter="${default.chapter_name!'章'}" data-part="${default.part_name!'节'}" data-user="${default.user_name!'学员'}"><span class="glyphicon glyphicon-trash"></span> 删除课程</a></li>
                </#if>
            </ul>
        </div>
    </td>
</tr>
