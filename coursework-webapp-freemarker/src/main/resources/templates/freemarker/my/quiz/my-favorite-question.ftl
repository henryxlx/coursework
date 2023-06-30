<#assign script_controller = 'quiz-question/my-favorite' />

<@block_title '我的考试记录'/>

<#include '/my/quiz/layout.ftl'/>

<#macro blockList>

    <#if favoriteQuestions?? && favoriteQuestions?size gt 0>
        <table class="table table-striped table-hover">
            <tbody>
            <#list favoriteQuestions as qid, favoriteQuestion>
                <#assign paper = testpapers[''+targets[favoriteQuestion.target].id]! />
                <#assign question = questions[''+favoriteQuestion.questionId]! />

                <#include '/my/quiz/my-favorite-question-tr.ftl' />

            </#list>
            </tbody>
        </table>
    <#else>
        <div class="empty">还没有收藏的题目</div>
    </#if>

    <@web_macro.paginator paginator! />
</#macro>