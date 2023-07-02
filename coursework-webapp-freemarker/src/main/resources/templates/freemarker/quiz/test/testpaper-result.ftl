<#include '/quiz/test/testpaper-layout.ftl'/>

<#macro blockTestpaperHeadingStatus>
    <#if paperResult.status == 'finished'>
        <div class="label label-success">批阅完成</div>
    <#elseif paperResult.status == 'reviewing'>
        <div class="label label-info">批阅中</div>
    </#if>
</#macro>

<#macro blockTestpaperHeadingContent>

    <#include '/quiz/test/testpaper-result-objective.ftl' />

    <#if paperResult.status == 'reviewing'>
        <div class="alert alert-warning">老师正在批阅试卷，批阅完成后会以站内私信通知您批阅结果，请稍等。</div>
    <#elseif paperResult.status == 'finished'>
        <#if paperResult.teacherSay??>
            <div class="alert alert-success">
                <div class=""><strong>评语：</strong></div>
                <div class="mtm">${bbCode2Html(paperResult.teacherSay)}</div>
            </div>
        </#if>

        <#if paperResult.passedStatus == 'unpassed'>
            <div class="alert alert-danger">
                您未通过考试，请<a href="${ctx}/test/${paperResult.testId}/redo">重新参加考试</a>，或者重新学习课程。
            </div>
        <#elseif paperResult.passedStatus == 'passed'>
            <div class="alert alert-success">恭喜您已通过本次考试。</div>
        </#if>
    </#if>

</#macro>

<#macro blockTestpaperBodySidebar>

    <#if (['finished','reviewing']?seq_contains(paperResult.status) && setting('questions.testpaper_answers_show_mode','submitted') == 'submitted' ) || ( setting('questions.testpaper_answers_show_mode','submitted') == 'reviewed' ) && paperResult.status == 'finished'>

        <div class="testpaper-card" data-spy="affix" data-offset-top="200">
            <div class="panel panel-default">
                <div class="panel-body">
                    <div class="clearfix mbl">
                        <#list paper.metas.question_type_seq! as type>

                            <#list items[type]! as qid, item>
                                <#if item.questionType != 'material'>
                                    <a href="javascript:;" data-anchor="#question${item.questionId}"
                                       class="btn btn-default btn-index pull-left <#if paperResult.status == 'reviewing' && item.questionType == 'essay'>checking<#elseif item.question.testResult.status == 'partRight'>wrong<#else>${item.question.testResult.status!}</#if>">${item.seq}</a>
                                <#else>
                                    <#list item.items! as item>
                                        <a href="javascript:;" data-anchor="#question${item.questionId}"
                                           class="btn btn-default btn-index pull-left <#if paperResult.status == 'reviewing' && item.questionType == 'essay'>checking<#elseif item.question.testResult.status == 'partRight'>wrong<#else>${item.question.testResult.status!}</#if>">${item.seq}</a>
                                    </#list>
                                </#if>
                            </#list>

                            <div class="clearfix mtm mbm"></div>

                        </#list>

                        <div class="testpaper-card-explain clearfix">
                            <a href="javascript:;" class="btn btn-success btn-index"></a><small
                                    class="text-muted">正确</small>
                            <a href="javascript:;" class="btn btn-danger btn-index"></a><small
                                    class="text-muted">错误</small>
                            <a href="javascript:;" class="btn btn-warning btn-index"></a><small
                                    class="text-muted">待批阅</small>
                            <a href="javascript:;" class="btn btn-default btn-index"></a><small
                                    class="text-muted">未做</small>
                        </div>
                    </div>

                    <div class="panel-footer">
                        <div class="btn-group btn-group-justified btn-group-sm">
                            <label class="checkbox">
                                <span class="text-info">只看错题</span>
                                <input type="checkbox" id="showWrong"/>
                            </label>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </#if>


</#macro>