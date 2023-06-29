<#assign script_controller = 'testpaper/testpaper-form'/>
<#assign side_nav = 'testpaper'/>

<@block_title '创建试卷'/>

<#include '/course/manage/layout.ftl'/>

<#macro blockMain>

    <div class="panel panel-default panel-col test-creator">
        <div class="panel-heading clearfix">创建试卷</div>

        <div class="panel-body">

            <ol class="breadcrumb">
                <li><a href="${ctx}/course/${course.id}/manage/testpaper">试卷管理</a></li>
                <li class="active">创建试卷</li>
            </ol>

            <form id="testpaper-form" class="form-horizontal" method="post"
                  data-build-check-url="${ctx}/course/${course.id}/manage/testpaper/build_check"
                  data-auto-submit="false" data-have-base-fields="true" data-have-build-fields="true"
                  data-course-id="${course.id}">
                <@web_macro.flash_messages />

                <#include '/course/manage/testpaper/testpaper-form-base-fields.ftl' />
                <#include '/course/manage/testpaper/testpaper-form-build-fields.ftl' />

                <div class="form-group">
                    <div class="col-md-8 col-md-offset-2 controls">
                        <button id="testpaper-create-btn" data-submiting-text="正在提交" type="submit"
                                class="btn btn-primary">保存，下一步
                        </button>
                        <a href="${ctx}/course/${course.id}/manage/testpaper" class="btn btn-link  ">返回</a>
                    </div>
                </div>

            </form>

        </div>
    </div>
</#macro>



 