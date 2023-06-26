<#include '/course/manage/question/question-form-layout.ftl'/>

<#macro blockQuestionExtraFields>

    <div class="form-group">
        <div class="col-md-2 control-label"><label>答案</label></div>
        <div class="col-md-8 controls radios ">
            <@radios 'answer[]' {'1':'正确', '0':'错误'} question.answer[0]! />
        </div>
    </div>

</#macro>