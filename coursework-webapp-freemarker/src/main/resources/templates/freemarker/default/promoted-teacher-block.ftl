<#if teacher??>
    <div class="es-box">
        <div class="es-box-heading"><h2>名师风采</h2></div>
        <div class="es-box-body">
            <div class="promoted-teacher">
                <a href="${ctx}/user/${teacher.id}"><img src="${default_path('avatar',teacher.mediumAvatar, '')}"
                                                         class="avatar"></a>
                <a class="nickname" href="${ctx}/user/${teacher.id}">${teacher.username}</a>
                <div class="title">${teacherProfile.title!}</div>
                <div class="about">${fastLib.plainText(teacherProfile.about!, 100)}</div>
                <div class="more"><a href="${ctx}/user/${teacher.id}">去老师主页看看 &raquo;</a></div>
            </div>
        </div>
    </div>
</#if>