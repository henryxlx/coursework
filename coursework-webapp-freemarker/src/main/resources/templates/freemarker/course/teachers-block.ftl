<div class="panel panel-default">
    <div class="panel-heading">
        <h3 class="panel-title">课程教师</h3>
    </div>
    <div class="panel-body">

        <#if course.teacherIds??>
            <div class="teacher-carousel <#if course.teacherIds?size == 1>teacher-carousel-onlyone</#if>">
                <div class="carousel slide" id="teacher-carousel">
                    <div class="carousel-inner">
                        <#list course.teacherIds as teacherId>
                            <#assign user = users[teacherId] />
                            <#if profiles??>
                                <#assign profile = profiles[teacherId] />
                                <div class="item <#if teacherId?counter== 1>active</#if>" data-id="${user.id}">
                                    <a href="${ctx}/user/${user.id}"><img
                                                src="${default_path('avatar',user.largeAvatar, 'large')}"
                                                class="avatar"></a>
                                </div>
                            </#if>
                        </#list>
                    </div>
                    <a class="left carousel-control" href="#teacher-carousel" data-slide="prev">
                        <span class="icon-prev"></span>
                    </a>
                    <a class="right carousel-control" href="#teacher-carousel" data-slide="next">
                        <span class="icon-next"></span>
                    </a>
                </div>
                <div class="teacher-detail" id="teacher-detail">
                    <#list course.teacherIds as teacherId>
                        <#assign user = users[teacherId] />
                        <#if profiles??>
                            <#assign profile = profiles[teacherId] />
                            <div class="teacher-item teacher-item-${user.id} <#if teacherId?counter == 1>teacher-item-active</#if>">
                                <div class="nickname"><a href="${ctx}/user/${user.id}">${user.username}</a>
                                </div>
                                <div class="title">${user.title}</div>
                                <div class="divider"></div>
                                <div class="about">${profile.about!}</div>
                            </div>
                        </#if>
                    </#list>
                </div>
            </div>
        <#else>
            <div class="empty">该课程尚未设置教师</div>
        </#if>

    </div>
</div>