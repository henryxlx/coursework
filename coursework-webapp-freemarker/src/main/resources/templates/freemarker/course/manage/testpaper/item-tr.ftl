<tr id="testpaper-item-${question.id}" data-id="${question.id}" data-type="${question.type}"
    <#if question.parentId gt 0 >data-parent-id="${question.parentId}"</#if>
    class="<#if question.subCount gt 0> have-sub-questions</#if><#if question.parentId gt 0> is-sub-question<#else> is-question</#if>">
    <td><span class="glyphicon glyphicon-resize-vertical sort-handle"></span></td>
    <td>
        <input <#if question.parentId != 0 > class="hidden" </#if> class="notMoveHandle" type="checkbox"
                                                                   value="${question.id}" data-role="batch-item">
        <input type="hidden" name="questionId[]" value="${question.id}">
    </td>
    <td class="seq"><#if question.subCount gt 0><span class="text-muted">~</span><#else>${(item.seq)!' '}</#if></td>
    <td>
        ${fastLib.plainText(question['stem'], 40)}
        <div class="text-muted text-sm">
            从属：
            <#if question.target == 'course-' + course.id>
                本课程
            <#else>
                ${targets[question.target]!'--'}.simple_name
            </#if>
        </div>
    </td>
    <td>${dict_text('questionType',question.type )}</td>
    <td>${dict_text('difficulty', question.difficulty)}</td>
    <td>
        <input name="scores[]" class="notMoveHandle form-control input-sm"
               <#if question.subCount gt 0>type="hidden" <#else>type="text"</#if>
               value="${(item.score)!question.score!}"
               data-miss-score="${testpaper.metas.missScore!0}">
    </td>

    <td>
        <div class="btn-group">
            <#if question.parentId == 0>
                <a href="#modal" data-toggle="modal"
                   data-url="${ctx}/course/${course.id}/manage/question/${question.id}/preview"
                   class="notMoveHandle btn btn-default btn-sm">预览</a>
                <a href="javascript:" class="notMoveHandle btn btn-default btn-sm item-delete-btn">删除</a>
                <a href="javascript:" class="notMoveHandle btn btn-default btn-sm" data-role="pick-item"
                   data-url="${ctx}/course/${course.id}/manage/testpaper/${testpaper.id}/item_picker?replace=${question.id}">替换</a>
            </#if>
        </div>
    </td>
</tr>