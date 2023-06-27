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
                    <ul>
                        <#list item.question.answer![] as answer>
                            <#assign userAnswer = item.question.testResult.answer[answer?index]! />
                            <li>
                                填空(${answer?counter})： 正确答案 <strong class="text-success">{{ answer | join
                                    (' 或 ') }}</strong>
                                <#if userAnswer == ''>
                                    。你未回答
                                <#elseif item.question.testResult.status == "right">
                                    ，你的答案　<strong class="text-success">${userAnswer}</strong>。
                                <#elseif item.question.testResult.status == "partRight">
                                    ，你的答案　<strong class="text-warning">${userAnswer}</strong>。
                                <#else>
                                    ，你的答案　<strong class="text-danger">${userAnswer}</strong>。
                                </#if>
                            </li>
                        </#list>
                    </ul>

                    <#if item.question.testResult.status == "right">
                        <p class="text-success">回答正确。</p>
                    <#elseif item.question.testResult.status == "partRight">
                        <p class="text-warning">回答部分正确，本题得分：${item.question.testResult.score}分。</p>
                    <#else>
                        <p class="text-danger">回答错误。</p>
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
                <div class="testpaper-question-fill-inputs">
                    <#list item.question.answer![] as answer>
                        <input class="form-control " type="text" data-type="fill" name="${item.question.id}"
                               placeholder="填空(${answer?counter})答案，请填在这里"
                               <#if (item.question.testResult.answer[answer?index])??>value="${item.question.testResult.answer[answer?index]}"</#if> />
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
                        <ul>
                            <#list item.question.answer as answer>
                                <li>
                                    填空(${answer?counter})： 正确答案 <strong
                                            class="text-success">${answer?join(' 或 ')}</strong>
                                </li>
                            </#list>
                        </ul>
                    </div>
                </div>
                <div class="testpaper-question-analysis well">${bbCode2Html(item.question.analysis!'无解析')}</div>
            </#if>

        </#if>
    </div>

</#if>