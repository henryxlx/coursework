<#assign script_controller = 'quiz-question/create'/>
<#assign side_nav = 'question'/>

<@block_title "${(question.id?? && question.id > 0)?then('编辑', '添加')}题目"/>

<#include '/course/manage/layout.ftl'/>

<#macro blockMain>

    <div class="panel panel-default panel-col">
        <div class="panel-heading clearfix">
            题目管理
        </div>
        <div class="panel-body">

            <ol class="breadcrumb">
                <li><a href="${ctx}/course/${course.id}/manage/question">题目管理</a></li>
                <#if parentQuestion??>
                    <li>
                        <a href="${ctx}/course/${course.id}/manage/question?parentId=${parentQuestion.id}">${dict_text('questionType', parentQuestion.type)}</a>
                    </li>
                </#if>
                <li class="active"><#if question.id?? && question.id gt 0>编辑<#else>添加</#if>题目</li>
            </ol>

            <div id="question-creator-widget"
                 data-upload-url="${ctx}/course/${course.id}/manage/question/uploadfile/${question.type}"
                 data-media-upload-params-url="${ctx}/uploadfile/params?targetType=question&targetId=0"
                 data-media-upload-callback-url="${ctx}/uploadfile/cloud_callback?targetType=question&targetId=0"

            >
                <form id="question-create-form" data-role="question-form" class="form-horizontal quiz-question"
                      method="post"
                      action="<#if question.id?? && question.id gt 0>${ctx}/course/${course.id}/manage/question/${question.id}/update?goto=${RequestParameters['goto']!}<#else>${ctx}/course/${course.id}/manage/question/create/${question.type}?parentId=${(parentQuestion.id)!}&goto=${RequestParameters['goto']!}</#if>"
                >
                    <@web_macro.flash_messages />

                    <#if !parentQuestion??>
                        <div class="form-group">
                            <div class="col-md-2 control-label"><label>从属</label></div>
                            <div class="col-md-8 controls">
                                <select class="form-control width-input width-input-large" name="target"
                                        data-role="target">
                                    <@select_options targetsChoices question.target! />
                                </select>
                                <div class="help-block">可以针对本课程或者某个课时出题</div>
                            </div>
                        </div>
                    </#if>

                    <div class="form-group">
                        <div class="col-md-2 control-label"><label>难度</label></div>
                        <div class="col-md-8 controls radios">
                            <@radios 'difficulty' {'simple':'简单', 'normal':'一般', 'difficulty':'困难'} question.difficulty!'normal'/>
                        </div>
                    </div>

                    <div class="form-group">
                        <div class="col-md-2 control-label"><label
                                    for="question-stem-field">${question_stem_label!'题干'}</label></div>
                        <div class="col-md-8 controls">
                            <textarea class="form-control" id="question-stem-field" name="stem"
                                      style="height:180px;">${question.stem!}</textarea>
                            <#if question_stem_help??>
                                <div class="help-block">${question_stem_help!}</div></#if>
                        </div>
                        <div class="col-md-2" style="padding-left:0;">
                            <div id="question-stem-uploader" class="btn btn-sm btn-default webuploader-container"
                                 type="button" data-target="#question-stem-field"><i
                                        class="glyphicon glyphicon-picture"></i></div>
                            <#if enabledAudioQuestion?? && enabledAudioQuestion>
                                <div id="question-stem-audio-uploader"
                                     class="btn btn-sm btn-default webuploader-container" type="button"
                                     data-target="#question-stem-field"><i class="glyphicon glyphicon-volume-up"></i>
                                </div>
                            </#if>
                        </div>
                    </div>

                    <#if blockQuestionExtraFields??><@blockQuestionExtraFields/></#if>

                    <div class="form-group">
                        <div class="col-md-8 col-md-offset-2 controls ">
                            <a data-toggle="collapse" data-role="advanced-collapse" data-target="#advanced-collapse"
                               class="text-success collapsed">&raquo; 显示/隐藏 高级选项 ...</a>
                        </div>
                    </div>

                    <div id="advanced-collapse" class="advanced-collapse collapse">
                        <div class="form-group">
                            <div class="col-md-2 control-label"><label for="question-analysis-field">解析</label></div>
                            <div class="col-md-8 controls">
                                <textarea class="form-control" id="question-analysis-field"
                                          name="analysis">${question.analysis!}</textarea>
                            </div>
                            <div class="col-md-2" style="padding-left:0;">
                                <div id="question-analysis-uploader"
                                     class="btn btn-sm btn-default  webuploader-container" data-role="analysis-uploader"
                                     data-target="#question-analysis-field"><i class="glyphicon glyphicon-picture"></i>
                                </div>
                            </div>
                        </div>

                        <div class="form-group">
                            <div class="col-md-2 control-label"><label for="question-score-field">分值</label></div>
                            <div class="col-md-4 controls">
                                <input class="form-control" value="${question.score!2}" type="text"
                                       id="question-score-field" name="score"/>
                            </div>
                        </div>

                        <!-- <div class="form-group">
                          <div class="col-md-2 control-label"><label for="categoryId">类别</label></div>
                          <div class="col-md-8 controls">
                              <select class="form-control width-input width-input-large" name="categoryId" data-role="category">
                                <select_options categoryChoices question.categoryId!0  '请选择类别') />
                              </select>
                          </div>
                        </div> -->

                    </div>

                    <div class="form-group">
                        <div class="col-md-8 col-md-offset-2 controls">
                            <#if blockQuestionButtons??><@blockQuestionButtons/><#else>
                                <#if !question.id?? && question.id gt 0>
                                    <button type="submit" data-role="submit" class="btn btn-primary submit-btn"
                                            data-submission="continue">保存并继续添加
                                    </button>
                                </#if>
                                <button type="submit" data-role="submit" class="btn btn-primary submit-btn"
                                        data-submission="submit">保存
                                </button>
                            </#if>
                            <a href="${ctx}/course/${course.id}/manage/question?parentId=${(parentQuestion.id)!}"
                               class="btn btn-link">返回</a>
                        </div>
                    </div>

                    <input type="hidden" name="submission">
                    <input type="hidden" name="type" value="${question.type}">
                    <input type="hidden" name="parentId" value="${(parentQuestion.id)!}">
                    <input type="hidden" name="_csrf_token" value="${csrf_token('site')}">

                </form>

            </div>

        </div>
    </div>


</#macro>



 