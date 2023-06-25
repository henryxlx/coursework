<tr data-role='item'>
    <td><input value="${question.id}" type="checkbox" autocomplete="off" data-role="batch-item"></td>
    <td>
        <a href="#modal" data-toggle="modal"
           data-url="${ctx}/course/${course.id}/manage/question/${question.id}/preview">
            ${fastLib.plainText(fastLib.fillQuestionStemText(question.stem), 40)}</a>
        <div>
            <#assign target = targets[question.target]! />
            <#if (target.type)?? && target.type != 'course'>
                <small class="text-muted">从属于 ${(targets[question.target].simple_name)!''}</small>
            <#else>
                <small class="text-muted">从属于 本课程</small>
            </#if>
            <#if question.type == 'material'>
                <#if question.subCount == 0>
                    <span class="label label-danger">未完成</span>
                </#if>
            </#if>
        </div>

    </td>
    <td>
        ${dict_text('questionType', question.type)}
        <#if question.type == 'material'><br><small class="text-muted">(${question.subCount}子题)</small></#if>
    </td>
    <td>
        <@web_macro.user_link users[''+question.userId]! />
        <br/>
        <span class="text-muted text-sm">${question.updatedTime?number_to_datetime?string('yyyy-MM-dd HH:mm:ss')}</span>
    </td>
    <td>
        <div class="btn-group">
            <a class="btn btn-default btn-sm" data-toggle="modal" data-target="#modal"
               data-url="${ctx}/course/${course.id}/manage/question/${question.id}/preview">预览</a>
            <a href="#" type="button" class="btn btn-default btn-sm dropdown-toggle" data-toggle="dropdown"><span
                        class="caret"></span></a>
            <ul class="dropdown-menu pull-right">
                <#if question.type == 'material'>
                    <li><a href="${ctx}/course/${course.id}/manage/question?parentId=${question.id}"><span
                                    class="glyphicon glyphicon-list"></span> 管理子题</a></li>
                </#if>
                <li>
                    <a href="${ctx}/course/${course.id}/manage/question/${question.id}/update?goto=${request.requestUri}"><span
                                class="glyphicon glyphicon-edit"></span> 编辑</a></li>
                <li><a href="javascript:" data-name='题目<#if question.type == ' material'>(含子题)</#if>'
                       data-role='item-delete'
                       data-url="${ctx}/course/${course.id}/manage/question/delete/${question.id}">
                        <span class="glyphicon glyphicon-remove-circle"></span> 删除</a></li>
            </ul>
        </div>
    </td>
</tr>