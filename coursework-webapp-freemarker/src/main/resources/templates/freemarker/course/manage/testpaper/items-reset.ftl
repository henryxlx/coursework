<#assign side_nav = 'testpaper'/>
<#assign script_controller = 'testpaper/testpaper-form'/>

<@block_title '重新生成试卷题目'/>

<#include '/course/manage/layout.ftl'/>

<#macro blockMain>

    <div class="panel panel-default panel-col test-creator">
        <div class="panel-heading clearfix">重新生成试卷题目</div>

        <div class="panel-body">

            <ol class="breadcrumb">
                <li><a href="${ctx}/course/${course.id}/manage/testpaper">试卷管理</a></li>
                <li class="active">重新生成试卷题目</li>
            </ol>

            <form id="testpaper-form" class="form-horizontal" method="post" data-auto-submit="false"
                  data-build-check-url="${ctx}/course/${course.id}/manage/testpaper/build_check"
                  data-have-build-fields="true">

                <#include '/course/manage/testpaper/testpaper-form-build-fields.ftl' />

                <div class="form-group">
                    <div class="col-md-8 col-md-offset-2">
                        <button type="submit" class="btn btn-primary">保存，下一步</button>
                        <a href="${ctx}/course/${course.id}/manage/testpaper" class="btn btn-link  ">返回</a>
                    </div>
                </div>

            </form>

        </div>
    </div>

</#macro>



 