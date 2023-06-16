<#assign modalSize = 'modal-dialog-medium'/>

<#include '/bootstrap-modal-layout.ftl' />

<#macro blockBody>

    <div class="row teacher-profile-card">
        <div class="col-md-4">
            <img class="img-responsive" src="${default_path('avatar',user.largeAvatar, 'large')}">
            <#if user.id != appUser.id>
                <div class="actions">
                    <button class="btn btn-primary btn-sm btn-block follow-btn"
                            data-url="${ctx}/user/${user.id}/following" <#if isFollowing??> style="display:none;"</#if>>
                        关注
                    </button>
                    <button class="btn btn-primary btn-sm btn-block unfollow-btn"
                            data-url="${ctx}/user/${user.id}/unfollow" <#if !isFollowing??> style="display:none;"</#if>>
                        已关注
                    </button>
                    <button class="btn btn-default btn-sm btn-block" data-toggle="modal" data-target="#modal"
                            data-url="${ctx}/message/create/${user.id}" id="message_to_teacher">发私信
                    </button>
                </div>
            </#if>
        </div>

        <div class="col-md-8">
            <h4>${user.username}</h4>
            <div class="text-muted">${user.title}</div>

            <div class="mbl">
                ${(profile.about)!}
            </div>

            <p>

                <#if profile??>
                    <#if profile.weibo??>
                        <a href="${profile.weibo}" target="_blank" class="mrm"><img
                                    src="${ctx}/assets/img/user/link-weibo.png"> 微博</a>
                    </#if>
                    <#if profile.site??>
                        <a href="${profile.site}" target="_blank" class="mrm"><img
                                    src="${ctx}/assets/img/user/link-site.png"> 网站</a>
                    </#if>
                </#if>
            </p>
        </div>

    </div>

    <script>
        app.load('course/teacher-info-modal');
    </script>
</#macro>