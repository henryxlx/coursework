<#assign question_stem_label = '材料'/>

<#include '/course/manage/question/question-form-layout.ftl'/>

<#macro blockQuestionButtons>
    <#if !question.id??>
        <button type="submit" data-role="submit" class="btn btn-primary" data-submission="continue_sub">保存并添加子题</button>
    <#else>
        <button type="submit" data-role="submit" class="btn btn-primary">保存</button>
    </#if>

</#macro>