<#if paperResult?? &&  ['finished','reviewing']?seq_contains(paperResult.status) && userAcl.hasRole('ROLE_TEACHER') >

<#else>

    <#if showTestpaperNavbar?? && showTestpaperNavbar == 'off' >
        <form id='teacherCheckForm' autocomplete="off">
    </#if>
    <#if !types??>
        <#assign types = paper.metas.question_type_seq/>
    </#if>

    <#list types! as type>

        <#if (items[type])?? && items[type]?size gt 0>

            <div class="panel panel-default testpaper-question-block" id="testpaper-questions-${type}">
                <div class="panel-heading"><strong class="">${dict['questionType'][type]}</strong>
                    <small class="text-muted">共${total[type].number}题，共${(total[type].score)!}
                        分<#if total[type].missScore gt 0>，漏选得${total[type].missScore}分</#if></small>
                </div>
                <div class="panel-body">

                    <#list items[type] as qid, item>

                        <#if item.question.isDeleted??>
                            <div class="testpaper-question testpaper-question-choice"
                                 id="question${item.questionId}">
                                <div class="testpaper-question-body">
                                    <#include '/quiz/test/testpaper-question-stem.ftl' />
                                </div>
                            </div>
                        <#else>

                            <#if ['single_choice', 'choice', 'uncertain_choice']?seq_contains(type) >
                                <#include '/quiz/test/do-test-choice.ftl' />

                            <#else>

                                <#include '/quiz/test/do-test-${type}.ftl' />

                            </#if>

                        </#if>
                    </#list>
                </div>
            </div>

        </#if>

    </#list>

    <#if showTestpaperNavbar?? && showTestpaperNavbar == 'off' >
        <textarea name="teacherSay" id="teacherSay" style="display:none"></textarea>
    </#if>


    </form>

</#if>