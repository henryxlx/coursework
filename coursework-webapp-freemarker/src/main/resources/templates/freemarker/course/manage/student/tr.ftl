<tr id="student-${user.id}-tr">
    <td class="media">
        <@web_macro.user_avatar user 'pull-left' />
        <a target="_blank" href="${ctx}/user/${user.id}">${user.username}</a>
        <#if student.remark??>
            <span class="text-muted text-sm" title="${student.remark}">(${student.remark})</span>
        </#if>
        <div class="text-muted text-sm">加入时间：${student.createdTime?number_to_datetime?string('yyyy-MM-dd HH:mm')}</div>
        <#if course.expiryDay gt 0 && student.deadline gt 0 >
            <div class="text-muted text-sm">有效期至：${student.deadline?number_to_datetime?string('yyyy-MM-dd HH:mm')}
                (${fastLib.remainTime(student.deadline)})
            </div>
        </#if>
    </td>

    <td>
        <div class="progress" title="已学${progress.number}课时">
            <div class="progress-bar" style="width: ${progress.percent}">
            </div>
        </div>
    </td>

    <td>


        <div class="btn-group">
            <#if appUser.id != user.id>
            <button class="btn btn-default btn-sm" data-toggle="modal" data-target="#modal" data-url="${ctx}/message/create/${user.id}">发私信</button>
            </#if>
            <#if userAcl.hasRole('ROLE_ADMIN')>
                <button class="btn btn-default btn-sm" data-toggle="modal" data-target="#modal"
                        data-url="${ctx}/course/${course.id}/manage/student/${user.id}/show">查看资料
                </button>
            <#elseif setting("course.buy_fill_userinfo")??>
                <button class="btn btn-default btn-sm" data-toggle="modal" data-target="#modal"
                        data-url="${ctx}/course/${course.id}/manage/student/${user.id}/defined_show">查看资料
                </button>
            </#if>
            <a href="#" type="button" class="btn btn-default btn-sm dropdown-toggle" data-toggle="dropdown">管理
                <span class="caret"></span>
            </a>
            <ul class="dropdown-menu pull-right">
                <li><a class="" href="" data-toggle="modal" data-target="#modal" data-url="${ctx}/course/${course.id}/manage/student/${user.id}/remark">备注</a></li>
                <#if appUser.id != user.id >
                <li>
                    <a class="follow-student-btn" href="javascript:;" data-url="${ctx}/user/${user.id}/following" <#if isFollowing??> style="display:none;"</#if>>关注</a>
                    <a class="btn-success unfollow-student-btn" href="javascript:;" data-url="${ctx}/user/${user.id}/unfollow" <#if isFollowing!> style="display:none;"</#if>>已关注</a>
                </li>
                </#if>
                <#if userAcl.hasRole('ROLE_ADMIN') ||  userAcl.hasRole('ROLE_TEACHER') &&  isTeacherAuthManageStudent == 1 && course.expiryDay gt 0 >
                <li><a class="" href="" data-toggle="modal" data-target="#modal" data-url="${ctx}/course/${course.id}/set_expiryday/${user.id}">增加有效期</a></li>
                </#if>

                <#if userAcl.hasRole('ROLE_ADMIN') ||  userAcl.hasRole('ROLE_TEACHER') &&  isTeacherAuthManageStudent == 1 >
                    <li><a class="student-remove" href="javascript:;"
                           data-url="${ctx}/course/${course.id}/manage/student/${user.id}/remove"
                           data-user="${default.user_name!'学员'}">移除</a></li>
                </#if>
            </ul>
        </div>
    </td>
</tr>