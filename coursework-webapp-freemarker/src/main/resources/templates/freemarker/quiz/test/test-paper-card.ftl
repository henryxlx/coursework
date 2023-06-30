<div class="panel panel-default">
    <div class="panel-heading">
        <#if limitTime != 0>
            <span class="${isPreview!} testpaper-card-timer" id="time_show"
                  data-time="<#if paperResult?? && paperResult.usedTime == 0>${limitTime}<#else>${limitTime - paperResult.usedTime}</#if>">00:00</span>
            <button class="btn btn-sm btn-default pull-right"<#if id?? && id gt 0> id="suspend" data-url="{{ path('course_manage_do_test_suspend', {id:id}) }}" data-goto="{{ path('my_quiz') }}"</#if>>
                下次再做
            </button>
            <button id="pause"
                    class="btn btn-sm btn-default pull-right"<#if id?? && id gt 0> data-toggle="modal" data-backdrop="static" data-target="#modal" data-url="{{ path('course_manage_do_test_pause') }}"</#if>>
                暂停
            </button>
        <#else>
            <span class="testpaper-card-timer"><small class="text-muted" style="font-size:14px;">时间不限</small></span>
            <button class="btn btn-sm btn-default pull-right"<#if id?? && id gt 0> id="suspend" data-url="{{ path('course_manage_do_test_suspend', {id:id}) }}" data-goto="{{ path('my_quiz') }}"</#if>>
                下次再做
            </button>
        </#if>
    </div>
    <div class="panel-body">

        <#list paper.metas.question_type_seq! as type>

            <#list (items[type])! as qid, item>
                <#if item.questionType != 'material'>
                    <a href="javascript:;"
                       data-anchor="#question${item.questionId}"
                       class="btn btn-default btn-index pull-left <#if item.question.testResult??>active</#if>">${item.seq}</a>
                <#else>
                    <#list item.items as item>
                        <a href="javascript:;" data-anchor="#question${item.questionId}"
                           class="btn btn-default btn-index pull-left <#if item.question.testResult??>active</#if>">${item.seq}</a>
                    </#list>
                </#if>

            </#list>

            <div class="clearfix mtm mbm"></div>

        </#list>

    </div>
    <div class="panel-footer">
        <#if id?? && id gt 0>
            <button class="btn btn-success btn-block do-test" id="finishPaper"
                    data-ajax="{{ path('course_manage_submit_test', { id: id }) }}"
                    data-url="{{ path('course_manage_finish_test', { id: id }) }}"
                    data-goto="{{ path('course_manage_test_results', { id: id }) }}">我要交卷
            </button>
        <#else>
            <button class="btn btn-success btn-block">我要交卷</button>
        </#if>
    </div>
</div>