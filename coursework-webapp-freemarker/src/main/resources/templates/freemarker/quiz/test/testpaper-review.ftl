<#assign role = 'teacher' />

<#assign showTestpaperNavbar = 'off'>

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

    <div class="text-info mbl">请完成以下题目的批阅：</div>

</#macro>

<#macro blockTestpaperBodySidebar>

    <div class="testpaper-card" data-spy="affix" data-offset-top="200">
        <div class="panel panel-default">
            <div class="panel-body">

                <#list types!(paper.metas.question_type_seq)! as type>

                    <#list items[type]! as qid, item>
                        <#if item.questionType != 'material'>
                            <a href="#question${item.questionId}"
                               class="btn btn-default btn-index ${item.question.testResult.status}">${item.seq}</a>
                        <#else>
                            <#list item.items! as item>
                                <#if item.questionType == 'essay'>
                                    <a href="#question${item.questionId}"
                                       class="btn btn-default btn-index ${item.question.testResult.status}">${item.seq}</a>
                                </#if>
                            </#list>
                        </#if>

                    </#list>

                    <div class="clearfix mtm mbm"></div>

                </#list>

            </div>
            <div class="panel-footer">
                <button class="btn btn-success btn-block" id="finishCheck">完成批阅</button>
            </div>
        </div>
    </div>

</#macro>