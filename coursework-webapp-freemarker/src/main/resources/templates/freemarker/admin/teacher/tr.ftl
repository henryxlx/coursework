<tr id="user-table-tr-${user.id}">
    <td>
        <strong><@admin_macro.user_link user/></strong>
        <#if user.locked?? && user.locked == 1>
            <label class="label label-danger">禁</label>
        </#if>
    </td>
    <td>
        <#if user.promoted?? && user.promoted == 1>
            <span class="text-success">是</span>
            <br>
            <span class="text-muted text-sm">${user.promotedTime?number_to_datetime?string('yyyy-MM-dd HH:mm')}</span>
        <#else>
            <span class="text-muted">否</span>
        </#if>
    </td>
    <td>
        <span class="text-sm"><#if user.loginTime??>${user.loginTime?number_to_datetime?string('yyyy-MM-dd HH:mm:ss')}<#else>--</#if></span>
        <br>
        <a class="text-muted text-sm" href="http://www.baidu.com/s?wd=${user.loginIp}"
           target="_blank">${user.loginIp}</a>
    </td>
    <td>
        <div class="btn-group">
            <a href="#modal" data-toggle="modal" data-url="${ctx}/admin/user/${user.id}" data-url=""
               class="btn btn-default btn-sm">查看</a>
            <a href="#" type="button" class="btn btn-default btn-sm dropdown-toggle" data-toggle="dropdown">
                <span class="caret"></span>
            </a>
            <ul class="dropdown-menu">
                <#if user.promoted?? && user.promoted == 0>
                    <li><a class="promote-user" href="javascript:" data-url="${ctx}/admin/teacher/${user.id}/promote">推荐教师</a>
                    </li>
                </#if>

                <#if user.promoted?? && user.promoted == 1>
                    <li><a class="promote-user" href="javascript:"
                           data-url="${ctx}/admin/teacher/${user.id}/promote/cancel">取消推荐教师</a></li>
                </#if>
            </ul>
        </div>
    </td>
</tr>