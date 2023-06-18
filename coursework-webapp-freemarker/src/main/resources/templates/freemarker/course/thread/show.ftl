<#assign script_controller = 'course/thread-show' />

<#include '/course/dashboard-layout.ftl' />

<#macro blockTitle>${thread.title} - ${course.title} - ${blockTitleParent}</#macro>

<#macro blockDashboardMain>

    <ul class="breadcrumb">
        <li><a href="${ctx}/course/${course.id}/thread">讨论区</a></li>
        <li class="active"><#if thread.type == 'question'>问答<#else>话题</#if></li>
    </ul>

    <div class="thread">
        <div class="thread-header">
            <@web_macro.user_avatar author  'pull-right thread-author-avatar' />
            <h2 class="thread-title">
                <#if thread.type == 'question'>
                    <span class="label label-info">问</span>
                </#if>
                ${thread.title}
            </h2>
            <div class="thread-metas">
                By <@web_macro.user_link author  'link-muted'/>
                <span class="bullet mhs">•</span>
                ${fastLib.smartTime(thread.createdTime)}
                <span class="bullet mhs">•</span>
                ${thread.hitNum} 次浏览
                <#if lesson??>
                    <span class="bullet mhs">•</span>
                    <a class="link-muted" href="{ctx}/course/${thread.courseId}/learn#lesson/${thread.lessonId}"
                       title="${lesson.title}">课时${lesson.number}</a>
                </#if>
            </div>
        </div>
        <div class="thread-body">${thread.content}</div>
        <#if isManager || appUser.id == author.id>
            <div class="thread-footer">
                <a href="${ctx}/course/${course.id}/thread/${thread.id}/edit" class="btn btn-link"><span
                            class="glyphicon glyphicon-edit"></span> 编辑</a>
                <#if isManager>
                    <a href="javascript:;" class="btn btn-link"
                       data-url="${ctx}/course/${course.id}/thread/${thread.id}/delete"
                       data-after-url="${ctx}/course/${course.id}/thread" data-role="confirm-btn"
                       data-confirm-message="您真的要删除该帖吗？"><span class="glyphicon glyphicon-remove-sign"></span> 删除</a>
                    <#if thread.isStick?? && thread.isStick == 1>
                        <a href="javascript:" class="btn btn-link"
                           data-url="${ctx}/course/${course.id}/thread/${thread.id}/unstick" data-role="confirm-btn"
                           data-confirm-message="您真的要取消置顶该帖吗？"><span class="glyphicon glyphicon-minus-sign"></span> 取消置顶</a>
                    <#else>
                        <a href="javascript:" class="btn btn-link"
                           data-url="${ctx}/course/${course.id}/thread/${thread.id}/stick" data-role="confirm-btn"
                           data-confirm-message="您真的要置顶该帖吗？"><span class="glyphicon glyphicon-circle-arrow-up"></span>
                            置顶</a>
                    </#if>

                    <#if thread.isElite?? && thread.isElite == 1>
                        <a href="javascript:" class="btn btn-link"
                           data-url="${ctx}/course/${course.id}/thread/${thread.id}/unelite" data-role="confirm-btn"
                           data-confirm-message="您真的要取消加精该帖吗？"><span class="glyphicon glyphicon-hand-right"></span> 取消加精</a>
                    <#else>
                        <a href="javascript:" class="btn btn-link"
                           data-url="${ctx}/course/${course.id}/thread/${thread.id}/elite" data-role="confirm-btn"
                           data-confirm-message="您真的要加精该帖吗？"><span class="glyphicon glyphicon-thumbs-up"></span> 加精</a>
                    </#if>
                </#if>
            </div>
        </#if>
    </div>

    <div class="thread-posts">
        <#if thread.type == 'question' && elitePosts??>
            <h3 class="thread-posts-heading"><span class="glyphicon glyphicon-share-alt"></span> 教师的答案</h3>
            <ul class="thread-post-list media-list">
                <#list elitePosts as post>
                    <#assign author = users['' + post.userId] />
                    <#include '/course/thread/post-list-item.ftl'/>
                </#list>
            </ul>
        </#if>

        <h3 class="thread-posts-heading">
            <span class="glyphicon glyphicon-share-alt"></span>
            <span id="thread-post-num">${thread.postNum}</span> <#if thread.type == 'question'>所有答案<#else>回复</#if>
        </h3>

        <ul class="thread-post-list media-list">
            <#list posts as post>
                <#assign author = users['' + post.userId] />
                <#include '/course/thread/post-list-item.ftl'/>
            <#else>
                <li class="empty">还没有<#if thread.type == 'question'>答案<#else>回复</#if>，赶快添加一个吧！</li>
            </#list>
        </ul>

        <@web_macro.paginator paginator! />

        <#if isMemberNonExpired==true>

            <h3 class="thread-posts-heading"><span class="glyphicon glyphicon-plus"></span>
                添加<#if thread.type == 'question'>答案<#else>回复</#if></h3>

            <@renderController path='/course/threadPostBlock' params={'courseId':course.id, 'id':thread.id} />

        </#if>

    </div>


</#macro>