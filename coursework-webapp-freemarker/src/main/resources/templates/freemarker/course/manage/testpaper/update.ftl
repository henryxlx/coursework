<#assign side_nav = 'testpaper'/>
<#assign script_controller = 'testpaper/testpaper-form'/>

<@block_title '编辑试卷信息'/>

<#include '/course/manage/layout.ftl'/>

<#macro blockMain>

    <div class="panel panel-default panel-col">

        <div class="panel-heading clearfix">编辑试卷信息</div>

        <div class="panel-body">

            <ol class="breadcrumb">
                <li><a href="${ctx}/course/${course.id}/manage/testpaper">试卷管理</a></li>
                <li class="active">编辑试卷信息</li>
            </ol>

            <form id="testpaper-form" class="form-horizontal" method="post" data-auto-submit="true"
                  data-have-base-fields="true">
                <@web_macro.flash_messages />

                <div class="form-group">
                    <div class="col-md-2 control-label"><label for="testpaper-name-field">试卷名称</label></div>
                    <div class="col-md-8 controls">
                        <input class="form-control" value="${testpaper.name}" name="name" id="testpaper-name-field"/>
                    </div>
                </div>

                <div class="form-group">
                    <div class="col-md-2 control-label"><label for="testpaper-description-field">试卷说明</label></div>
                    <div class="col-md-8 controls">
                        <textarea class="form-control" id="testpaper-description-field" name="description"
                                  data-image-upload-url="${ctx}/editor/upload?token=upload_token('course')">${testpaper.description}</textarea>
                    </div>
                </div>

                <div class="form-group">
                    <div class="col-md-2 control-label"><label for="testpaper-limitedTime-field">时间限制</label></div>
                    <div class="col-md-8 controls">
                        <input class="form-control width-input width-input-large" name="limitedTime"
                               id="testpaper-limitedTime-field" value="${testpaper.limitedTime}"/> 分钟
                        <div class="help-block">0分钟，表示无限制</div>
                    </div>
                </div>

                <#if (is_feature_enabled('testpaper_passed_score'))?? >
                    <div class="form-group">
                        <div class="col-md-2 control-label"><label for="testpaper-limitedTime-field">及格分数线</label></div>
                        <div class="col-md-8 controls">
                            <input class="form-control width-input width-input-large" name="passedScore"
                                   id="testpaper-passedScore-field" value="${testpaper.passedScore}"/> 分
                            <div class="help-block">总分为${testpaper.score}，最大不能超过总分；0表示不设及格分数线。</div>
                        </div>
                    </div>
                </#if>

                <div class="form-group">
                    <div class="col-md-8 col-md-offset-2 controls">
                        <button type="submit" class="btn btn-primary">保存</button>
                        <a href="${ctx}/course/${course.id}/manage/testpaper" class="btn btn-link">返回</a>
                    </div>
                </div>

                <input type="hidden" name="_csrf_token" value="${csrf_token('site')}">

            </form>

        </div>
    </div>

</#macro>



 