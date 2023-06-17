<#if type == 'unPosted'>

    <div class="btn-group">
        <a class="btn btn-default btn-sm remind-teachers" title="提醒教师"
           data-url="${ctx}/course/${question.courseId}/question/${question.id}/remindTeachers">
            <span class="glyphicon glyphicon-bell"></span>提醒教师
        </a>
        <a href="#" type="button" class="btn btn-default btn-sm dropdown-toggle" data-toggle="dropdown">
            <span class="caret"></span>
        </a>
        <ul class="dropdown-menu pull-right">
            <li>
                <a class="btn" data-role="item-delete" data-name="删除问答"
                   data-url="${ctx}/admin/course/thread/${question.id}/delete">
                    <span class="glyphicon glyphicon-trash"></span> 删除问答</a>
            </li>
        </ul>

    </div>

<#elseif type == "all">

    <button class="btn btn-default btn-sm" data-role="item-delete" data-name="删除问答"
            data-url="${ctx}/admin/course/thread/${question.id}/delete">删除
    </button>

</#if>