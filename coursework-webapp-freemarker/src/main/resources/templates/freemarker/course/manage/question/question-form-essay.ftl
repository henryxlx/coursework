<#include '/course/manage/question/question-form-layout.ftl'/>

<#macro blockQuestionExtraFields>

    <div class="form-group">
        <div class="col-md-2 control-label"><label for="question-answer-field">答案</label></div>
        <div class="col-md-8 controls">
            <textarea class="form-control" id="question-answer-field"
                      name="answer[]">${(question.answer[0])!}</textarea>
        </div>
        <div class="col-md-2" style="padding-left:0;">
            <div class="btn btn-sm btn-default " data-role="answer-uploader" data-target="#question-answer-field"
                 id="answer-stem-uploader"><i class="glyphicon glyphicon-picture"></i></div>
        </div>
    </div>


</#macro>