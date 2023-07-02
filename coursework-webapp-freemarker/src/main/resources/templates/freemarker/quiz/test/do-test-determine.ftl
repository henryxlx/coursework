<#assign role = role!'' />
<#if !(role == 'teacher')>

    <div class="testpaper-question testpaper-question-fill" id="question${item.question.id}">
        <div class="testpaper-question-body">
            <#include '/quiz/test/testpaper-question-stem.ftl' />
        </div>

        <#assign paperResultStatus = (paperResult.status)!'' />
        <#if (['reviewing', 'finished']?seq_contains(paperResultStatus) && setting('questions.testpaper_answers_show_mode','submitted') == 'submitted') || (setting('questions.testpaper_answers_show_mode','submitted') == 'reviewed'  && paperResultStatus == 'finished') >
            <div class="testpaper-question-footer clearfix">
                <div class="testpaper-question-result">
                    <#if item.question.testResult.status == 'right'>
                        正确答案是 <strong
                            class="text-success"><#if item.question.answer[0] == '1'> 正确 <#else> 错误 </#if></strong class="text-success">，你答对了
                    <#elseif item.question.testResult.status == 'wrong'>
                        正确答案是 <strong
                            class="text-success"><#if item.question.answer[0] == '1'> 正确 <#else> 错误 </#if></strong>， 你
                        <strong class="text-danger">答错</strong>了
                    <#elseif item.question.testResult.status == 'noAnswer'>
                        正确答案： <strong
                            class="text-success"><#if item.question.answer[0] == '1'> 正确 <#else> 错误 </#if></strong>，你未答题
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
                <div class="testpaper-question-determine-inputs">
                    <#assign itemQuestionTestResultAnswerFirst = (item.question.testResult.answer[0])!'' />
                    <label class="radio-inline <#if itemQuestionTestResultAnswerFirst == '1'> active</#if>"><input
                                type="radio" data-type="determine" name="${item.question.id}" value="1"
                                <#if itemQuestionTestResultAnswerFirst == '1'>checked</#if>> 正确</label>
                    <label class="radio-inline <#if itemQuestionTestResultAnswerFirst == '0'> active</#if>"><input
                                type="radio" data-type="determine" name="${item.question.id}" value="0"
                                <#if itemQuestionTestResultAnswerFirst == '0'>checked</#if>>错误</label>
                </div>

                <div class="testpaper-question-actions pull-right">
                    <#assign flags = ['mark', 'favorite'] />
                    <#include '/quiz/test/flag.ftl'/>
                </div>
            </div>

            <#if questionPreview?? && questionPreview>
                <div class="testpaper-preview-answer clearfix mtl mbl">
                    <div class="testpaper-question-result">
                        正确答案是 <strong
                                class="text-success"><#if item.question.answer[0] == '1'> 正确 <#else> 错误 </#if></strong class="text-success">
                    </div>
                </div>
                <div class="testpaper-question-analysis well">${bbCode2Html(item.question.analysis!'无解析')}</div>
            </#if>

        </#if>
    </div>

</#if>