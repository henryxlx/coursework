<tr>
    <td>
        ${fastLib.plainText(fill_question_stem_text(question.stem), 40)}
        <#if question.type == 'material'>
            <small class="text-muted">(${question.subCount}子题)</small>
        </#if>
        <br>
        <small class="text-muted">从属：
            <#if question.target == 'course-'+course.id>
                本课程
            <#else>
                ${targets[question.target]!'--'}.simple_name
            </#if>
        </small>
    </td>
    <td>${dict_text('questionType', question.type)}</td>
    <td>${question.score}</td>
    <td>
        <button data-url="${ctx}/course/${course.id}/manage/question/${question.id}/preview?isNew=true"
                class="btn btn-default btn-sm newWindowPreview">预览
        </button>
        <button type="button" class="btn btn-primary btn-sm" data-role="picked-item" data-replace="${replace!}"
                data-url="${ctx}/course/${course.id}/manage/testpaper/${testpaper.id}/item_picked?questionId=${question.id}"><#if replace?? && replace != ''>替换<#else>选择</#if></button>
    </td>
</tr>