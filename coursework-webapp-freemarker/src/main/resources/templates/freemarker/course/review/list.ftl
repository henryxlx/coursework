<#if isModal??>

    <div class="modal-dialog">
    <div class="modal-content">
    <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
        <h4 class="modal-title">评价课程</h4>
    </div>
    <div class="modal-body">
</#if>



<ul class="media-list">
    <#list reviews! as review>
        <#assign user = users['' + review.userId]!{} />
        <li class="media">
            <@web_macro.user_avatar user, 'pull-left'/>
            <div class="media-body">
                <div class="media-heading">
                    <@web_macro.user_link user/>
                    <span class="bullet">•</span>
                    <span class="text-muted">${review.createdTime?number_to_date?string('yyyy-MM-dd')}</span>
                </div>

                <div class="media-content">
                    <div><span class="stars-${review.rating!}"></span></div>

                    ${review.content!}

                </div>
            </div>
        </li>
    </#list>
</ul>

<@web_macro.paginator paginator! />

<#if isModal??>

    </div>
    <div class="modal-footer">
        <button class="btn btn-link" data-dismiss="modal">取消</button>
        <button class="btn btn-primary" data-url="${ctx}/course/${course.id}/review/create" id="back_to_create">返回
        </button>

    </div>
    </div>
    </div>

</#if>