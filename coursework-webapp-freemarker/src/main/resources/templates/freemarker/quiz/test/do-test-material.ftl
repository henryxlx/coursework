<div class="material">
    <div class="well testpaper-question-stem-material">${bbCode2Html(fill_question_stem_html(item.question.stem))}</div>
    <#list item.items![] as item>

        <#if ['single_choice', 'choice', 'uncertain_choice']?seq_contains(item.questionType) >

            <#include '/quiz/test/do-test-choice.ftl' />

        </#if>

        <#if item.questionType == 'determine' >

            <#include '/quiz/test/do-test-determine.ftl' />

        </#if>

        <#if item.questionType == 'fill' >

            <#include '/quiz/test/do-test-fill.ftl' />

        </#if>

        <#if item.questionType == 'essay' >

            <#include '/quiz/test/do-test-essay.ftl' />

        </#if>

    </#list>
</div>