<tr data-role='item'>
    <td><input value="${testpaper.id}" type="checkbox" data-role="batch-item"></td>
    <td>
        <a href="${ctx}/test/${testpaper.id}/preview" target="_blank">${fastLib.plainText(testpaper.name, 40)}</a>
    </td>
    <td>
        <#if testpaper.status == 'draft'>草稿</#if>
        <#if testpaper.status == 'open'>已发布</#if>
        <#if testpaper.status == 'closed'>已关闭</#if>
    </td>
    <td>
        ${testpaper.itemCount}题 <span class="text-muted">/</span> ${testpaper.score}分
        <#if testpaper.passedScore gt 0>
            <div class="text-muted"><small>及格：${testpaper.passedScore}分</small></div>
        </#if>
    </td>
    <td>
        <#if testpaper.limitedTime gt 0>${testpaper.limitedTime}分钟<#else>无限制</#if>
    </td>
    <td>
        <@web_macro.user_link user />
        <br/>
        <span class="text-muted text-sm">${testpaper.updatedTime?number_to_datetime?string('yyyy-MM-dd HH:mm:ss')}</span>
    </td>
    <td>
        <div class="btn-group">
            <a href="${ctx}/test/${testpaper.id}/preview" class="btn btn-default btn-sm" target="_blank">预览</a>

            <a href="#" type="button" class="btn btn-default btn-sm dropdown-toggle" data-toggle="dropdown">
                <span class="caret"></span>
            </a>

            <ul class="dropdown-menu pull-right">
                <#if ['draft', 'closed']?seq_contains(testpaper.status)>
                    <li><a class="open-testpaper" href="javascript:" title="发布试卷${testpaper.name}"
                           data-url="${ctx}/course/${course.id}/manage/testpaper/${testpaper.id}/publish"><span
                                    class="glyphicon glyphicon-ok"></span> 发布试卷</a></li>
                </#if>
                <#if testpaper.status == 'open'>
                    <li><a class="close-testpaper" href="javascript:" title="关闭试卷${testpaper.name}"
                           data-url="${ctx}/course/${course.id}/manage/testpaper/${testpaper.id}/close"><span
                                    class="glyphicon glyphicon-list"></span> 关闭试卷</a></li>
                </#if>

                <#if testpaper.status == 'draft'>
                    <li><a href="${ctx}/course/${course.id}/manage/testpaper/${testpaper.id}/update"><span
                                    class="glyphicon glyphicon-edit"></span> 编辑试卷信息</a></li>
                    <li><a href="${ctx}/course/${course.id}/manage/testpaper/${testpaper.id}/items"><span
                                    class="glyphicon glyphicon-list"></span> 管理题目</a></li>
                    <li><a href="${ctx}/course/${course.id}/manage/testpaper/${testpaper.id}/items_reset"><span
                                    class="glyphicon glyphicon-repeat"></span> 重新生成题目</a></li>
                    <li><a href="javascript:" data-name='试卷' data-role='item-delete'
                           data-url="${ctx}/course/${course.id}/manage/testpaper/${testpaper.id}/delete"><span
                                    class="glyphicon glyphicon-remove-circle"></span> 删除试卷</a></li>
                </#if>
            </ul>
        </div>
    </td>
</tr>