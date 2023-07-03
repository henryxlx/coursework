<div class="testpaper-question testpaper-question-essay" id="question${item.question.id}">
    <div class="testpaper-question-body">
        <#include '/quiz/test/testpaper-question-stem.ftl' />
    </div>
    <#assign paperResultStatus = (paperResult.status)!'' />
    <#if ['reviewing', 'finished']?seq_contains(paperResultStatus) >

        <#assign role = role!'' />
        <#if (role == 'teacher')>

            <div class="testpaper-question-footer clearfix">
                <div class="testpaper-question-result">

                    <ul class="nav nav-pills nav-mini mbm">
                        <li class="active"><a href="#studentAnswer${item.questionId}"
                                              data-toggle="tab">${setting('default.user_name')!'学员'}回答</a></li>
                        <li><a href="#teacherAnswer${item.questionId}" data-toggle="tab">参考答案</a></li>
                    </ul>
                    <div class="tab-content mbl">
                        <div class="tab-pane active"
                             id="studentAnswer${item.questionId}">${bbCode2Html((item.question.testResult.answer[0])!'<span class="text-muted">未回答</span>')}</div>
                        <div class="tab-pane"
                             id="teacherAnswer${item.questionId}">${bbCode2Html(item.question.answer[0])}</div>
                    </div>

                    <div class="form-horizontal">
                        <div class="form-group">
                            <label class="col-sm-2 control-label">得分</label>
                            <div class="col-sm-3 controls">
                                <input type="text" class="form-control" placeholder="得分" name="score_${item.questionId}"
                                       data-score="${item.score}"
                                       value="<#if (item.question.testResult.answer[0])?? && item.question.testResult.answer[0] == ''>0</#if>">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label">评语</label>
                            <div class="col-sm-9">

                                <div class="testpaper-result-essay-teacherSay">
                                    <textarea class="form-control testpaper-question-essay-teacherSay-short" rows="1"
                                              style="overflow:hidden;line-height:20px;"></textarea>

                                    <textarea id="question-teacherSay-long-${item.questionId}"
                                              class="form-control testpaper-question-essay-teacherSay-long"
                                              name="teacherSay_${item.questionId}" style="display:none;"
                                              data-image-upload-url="${ctx}/editor/upload?token=upload_token('course')"></textarea>

                                    <a class="btn btn-link btn-muted btn-sm testpaper-question-essay-teacherSay-btn"
                                       style="display:none"><span
                                                class="glyphicon glyphicon-chevron-up text-muted"></span> 收起</a>

                                </div>

                            </div>
                        </div>

                    </div>


                </div>

            </div>

        <#else>
            <div class="testpaper-question-footer clearfix">
                <div class="testpaper-question-result">
                    <div class="testpaper-question-result-suggested">
                        <div class="testpaper-question-result-title">参考答案：</div>
                        <div>${bbCode2Html(item.question.answer[0])}</div>
                    </div>
                    <div class="testpaper-question-result-your">
                        <div class="testpaper-question-result-title">你的回答：</div>
                        <div>${(item.question.testResult.answer[0])!'未回答'}</div>
                    </div>
                    <#if paperResultStatus == 'finished'>
                        <div class="testpaper-question-result-score mtl">
                            <div class="testpaper-question-result-title">得分：<strong>${(item.question.testResult.score)}
                                    分</strong></div>
                        </div>
                        <#if item.question.testResult.teacherSay != ''>
                            <div class="testpaper-question-teacherSay mtl">
                                <div class="testpaper-question-result-title">评语：</div>
                                <div>${bbCode2Html((item.question.testResult.teacherSay)!)}</div>
                            </div>
                        </#if>
                    <#else>
                        <div class="testpaper-question-score mtl">
                            <div class="testpaper-question-result-title">老师正在批阅！</div>
                        </div>
                    </#if>
                </div>

                <div class="testpaper-question-actions pull-right">
                    <#assign flags = ['favorite', 'analysis'] />
                    <#include '/quiz/test/flag.ftl'/>
                </div>
            </div>
            <div class="testpaper-question-analysis well"
                 style="display:none;">${bbCode2Html(item.question.analysis!)}</div>
        </#if>

    <#else>

        <div class="testpaper-question-footer clearfix">
            <div class="testpaper-question-essay-inputs">
                <textarea class="form-control testpaper-question-essay-input-short" rows="1"
                          style="overflow:hidden;line-height:20px;">${bbCode2Html((item.question.testResult.answer[0])!)}</textarea>

                <textarea id="question-input-long-${item.question.id}"
                          class="form-control testpaper-question-essay-input-long" data-type="essay"
                          name="${item.question.id}" style="display:none;"
                          data-image-upload-url="${ctx}/editor/upload?token=upload_token('course')">${bbCode2Html((item.question.testResult.answer[0])!)}</textarea>

                <a class="btn btn-link btn-muted btn-sm testpaper-question-essay-input-btn" style="display:none"><span
                            class="glyphicon glyphicon-chevron-up text-muted"></span> 收起</a>

            </div>
            <div class="testpaper-question-actions pull-right">
                <#assign flags = ['mark', 'favorite'] />
                <#include '/quiz/test/flag.ftl'/>
            </div>
        </div>


        <#if (questionPreview?? && questionPreview && setting('questions.testpaper_answers_show_mode', 'hide') == 'hide')  || (setting('questions.testpaper_answers_show_mode', '') == 'reviewed' && paperResultStatus == 'finished')>
            <div class="testpaper-preview-answer clearfix mtl mbl">
                <div class="testpaper-question-result">
                    <div class="testpaper-question-result-suggested">
                        <div class="testpaper-question-result-title">参考答案：</div>
                        <div>${bbCode2Html(item.question.answer[0]!)}</div>
                    </div>
                </div>
            </div>
            <div class="testpaper-question-analysis well">${bbCode2Html(item.question.analysis!'无解析')}</div>
        </#if>



    </#if>
</div>