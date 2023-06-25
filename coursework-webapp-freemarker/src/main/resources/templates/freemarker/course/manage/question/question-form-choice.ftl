<#include '/course/manage/question/question-form-layout.ftl'/>

<#macro blockQuestionExtraFields>

    <div data-role="choices"></div>
    <div class="form-group">
        <div class="col-md-2 control-label"></div>
        <div class="col-md-8 controls">
            <button class="btn btn-success btn-sm pull-right" data-role="add-choice" type="button">新增选项</button>
        </div>
    </div>
    <div class="form-group">
        <div class="col-md-2 control-label"><label>不定项选择题</label></div>
        <div class="col-md-8 controls radios">
            <#assign radioDefaultValue><#if question.type == 'uncertain_choice'>1<#else>0</#if></#assign>
            <@radios 'uncertain' {'0':'否', '1':'是'} radioDefaultValue />
        </div>
    </div>

    <#if (question.metas.choices)??>
        <script type="text/plain" data-role="choices-data">${json_encode(question.metas.choices)}</script>
        <script type="text/plain" data-role="answers-data">${json_encode(question.answer)}</script>
    </#if>
    <script type="text/x-handlebars-template" data-role="choice-template">
        <#noparse>
            <div class="form-group" data-role="choice">
                <div class="col-sm-2 control-label"><label class="choice-label" for="item-input-{{ id }}">选项{{ code
                        }}</label></div>
                <div class="col-sm-8 controls">
                    <div class=" input-group">
                        <input class="form-control item-input col-md-8" id="item-input-{{ id }}" name="choices[]"
                               type="text" value="{{ content }}" data-display="选项内容">
                        <span class="input-group-addon choice-answer">
              <label><input type="checkbox" class="answer-checkbox" {{#if isAnswer }}checked="checked"
                            {{/if}}> 正确答案</label>
            </span>
                    </div>
                </div>
                <div class="col-sm-2" style="padding-left:0;">
                    <div id="item-upload-{{ id }}" class="btn btn-default btn-sm webuploader-container"
                         data-target="#item-input-{{ id }}"><i class="glyphicon glyphicon-picture"></i></div>
                    <div id="item-audio-upload-{{ id }}"
                         class="btn btn-sm btn-default item-audio-upload webuploader-container hide"
                         data-target="#item-input-{{ id }}"><i class="glyphicon glyphicon-volume-up"></i></div>

                    <a class="btn btn-default btn-sm delete-choice mlm" data-role="delete-choice"
                       href="javascript:void(null)"><i class="glyphicon glyphicon-trash"></i></a>
                </div>
            </div>
        </#noparse>
    </script>
</#macro>