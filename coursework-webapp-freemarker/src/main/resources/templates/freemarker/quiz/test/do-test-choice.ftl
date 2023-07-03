<#assign role = role!'' />
<#if !(role == 'teacher')>

    <#assign paperResultStatus = (paperResult.status)!'' />
    <div class="testpaper-question testpaper-question-choice" id="question${item.questionId}">

        <#assign keys = [] />
        <#assign keys_answer = [] />
        <div class="testpaper-question-body">
            <#include '/quiz/test/testpaper-question-stem.ftl' />

            <ul class="testpaper-question-choices">
                <#list item.question.metas.choices as choice>
                    <#assign key = choice?index />

                    <#assign itemClass = '' />
                    <#if ['reviewing', 'finished']?seq_contains(paperResultStatus) && item.question.answer?seq_contains(key)>
                        <#assign itemClass = 'testpaper-question-choice-right'/>
                    </#if>
                    <#assign choiceIndex = (65+key) />

                    <li class="${itemClass}"><span
                                class="testpaper-question-choice-index">${chr(choiceIndex)}.</span> ${bbCode2Html(choice)}
                    </li>
                    <#if (item.question.answer)?? && item.question.answer?seq_contains(key?c) >
                        <#assign keys = keys + [chr(choiceIndex)] />
                    </#if>
                    <#if (item.question.testResult.answer)?? && item.question.testResult.answer?seq_contains(''+key) >
                        <#assign keys_answer = keys_answer + [chr(choiceIndex)] />
                    </#if>
                </#list>
            </ul>
        </div>

        <#if (['reviewing', 'finished']?seq_contains(paperResultStatus) && setting('questions.testpaper_answers_show_mode','submitted') == 'submitted') || (setting('questions.testpaper_answers_show_mode','submitted') == 'reviewed'  && paperResultStatus == 'finished') >
            <div class="testpaper-question-footer clearfix">
                <div class="testpaper-question-result">
                    <#if item.question.testResult.status == 'right'>
                        正确答案是 <strong class="text-success">${keys?join(',')}</strong>，回答正确
                    <#elseif item.question.testResult.status == 'partRight'>
                        正确答案是 <strong class="text-success">${keys?join(',')}</strong>，你的答案是 <strong
                            class="text-danger">${keys_answer?join(',')}</strong>。回答部分正确。得分：${item.question.testResult.score}分
                    <#elseif item.question.testResult.status == 'wrong'>
                        正确答案是 <strong class="text-success">${keys?join(',')}</strong>，你的答案是 <strong
                            class="text-danger">${keys_answer?join(',')}</strong>。回答错误
                    <#elseif item.question.testResult.status == 'noAnswer'>
                        正确答案是 <strong class="text-success">${keys?join(',')}</strong>，你未答题
                    </#if>
                </div>

                <div class="testpaper-question-actions pull-right">
                    <#assign flags = ['favorite', 'analysis'] />
                    <#include '/quiz/test/flag.ftl'/>
                </div>

            </div>
            <div class="testpaper-question-analysis well"
                 style="display:none;">${bbCode2Html(item.question.analysis!)}</div>
        <#else>
            <div class="testpaper-question-footer clearfix">

                <div class="testpaper-question-choice-inputs">
                    <#list item.question.metas.choices as choice>
                        <#assign key = choice?index />
                        <#assign choiceIndex = (65+key) />

                        <label class="<#if item.questionType == 'single_choice'>radio<#else>checkbox</#if>-inline <#if keys_answer?seq_contains(choiceIndex)>active</#if>">
                            <input type="<#if item.questionType == 'single_choice'>radio<#else>checkbox</#if>"
                                   data-type="choice" name="${item.questionId}" value="${key}"
                                   <#if keys_answer?seq_contains(choiceIndex)>checked</#if> >
                            ${chr(choiceIndex)}
                        </label>
                    </#list>
                </div>
                <div class="testpaper-question-actions pull-right">
                    <#assign flags = ['mark', 'favorite'] />
                    <#include '/quiz/test/flag.ftl'/>
                </div>
            </div>

            <#if questionPreview?? && questionPreview>
                <div class="testpaper-preview-answer clearfix mtl mbl">
                    <div class="testpaper-question-result">
                        正确答案是 <strong class="text-success">${keys?join(',')}</strong>
                    </div>
                </div>

                <div class="testpaper-question-analysis well">${bbCode2Html(item.question.analysis!'无解析')}</div>
            </#if>


        </#if>
    </div>

</#if>