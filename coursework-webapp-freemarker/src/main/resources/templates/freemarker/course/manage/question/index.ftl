<#assign script_controller = 'course-manage/list'/>
<#assign side_nav = 'question'/>
<#assign  parentId= '(parentQuestion.id)!0'/>

<@block_title '题目管理'/>

<#include '/course/manage/layout.ftl'/>

<#macro blockMain>

    <div class="panel panel-default panel-col">
        <div class="panel-heading">
            <div class="pull-right">
                <a href="${ctx}/course/${course.id}/manage/question/create/choice?parentId=parentId&goto=${request.requestUri}"
                   class="btn btn-info btn-sm"><span class="glyphicon glyphicon-plus"></span> 选择题</a>
                <a href="${ctx}/course/${course.id}/manage/question/create/fill?parentId=parentId&goto=${request.requestUri}"
                   class="btn btn-info btn-sm"><span class="glyphicon glyphicon-plus"></span> 填空题</a>
                <a href="${ctx}/course/${course.id}/manage/question/create/determine?parentId=parentId&goto=${request.requestUri}"
                   class="btn btn-info btn-sm"><span class="glyphicon glyphicon-plus"></span> 判断题</a>
                <a href="${ctx}/course/${course.id}/manage/question/create/essay?parentId=parentId&goto=${request.requestUri}"
                   class="btn btn-info btn-sm"><span class="glyphicon glyphicon-plus"></span> 问答题</a>
                <#if parentQuestion??>
                    <a href="${ctx}/course/${course.id}/manage/question/create/material"
                       class="btn btn-info btn-sm"><span class="glyphicon glyphicon-plus"></span> 材料题</a>
                </#if>
            </div>
            题目管理
        </div>

        <div class="panel-body " id="quiz-table-container">

            <#if parentQuestion??>
        <ol class="breadcrumb">
            <li><a href="${ctx}/course/${course.id}/manage/question/create">题目管理</a></li>
            <li class="active">材料题</li>
        </ol>
        </#if>

        <@web_macro.flash_messages/>

        <#if parentQuestion??>
        <div class="row">
            <div class="col-sm-12">
                <div class="well well-sm short-long-text">
                    <div class="short-text">{{ parentQuestion.stem||plain_text(100) }} <span class="trigger">(展开)</span></div>
                    <div class="long-text">{{ parentQuestion.stem|raw }} <span class="trigger">(收起)</span></div>
                </div>
            </div>
        </div>
        </#if>

        <#if parentQuestion??>
        <form class="form-inline well well-sm" action="" method="get" novalidate>
            <div class="form-group">
                <select class="form-control" name="type">
                    {{ select_options(dict('questionType'), app.request.query.get('type'), '--按题型--') }}
                </select>
            </div>

            <div class="form-group">
                <select class="form-control" name="target" style="width:200px;">
                    {{ select_options(targetChoices, app.request.query.get('target'), '--按从属--') }}
                </select>
            </div>

            <div class="form-group">
                <input type="text" id="keyword" name="keyword" class="form-control" value="${RequestParameters['keyword']!}" placeholder="关键词">
            </div>

            <button class="btn btn-primary btn-sm">搜索</button>

        </form>
        </#if>






        <table class="table table-striped table-hover" id="quiz-table">
            <thead>
            <tr>
                <th><input type="checkbox" autocomplete="off" data-role="batch-select"></th>
                <th width="50%">题干</th>
                <th>类型</th>
                <th>最后更新</th>
                <th>操作</th>
            </tr>
            </thead>
            <tbody>
            <#list questions! as question>
                <#include '/course/manage/question/question-tr.ftl'/>
            <#else>
                <tr>
                    <td colspan="20">
                        <div class="empty">一道题都没有，请点击右上角按钮，按不同的题型录入题目</div>
                    </td>
                </tr>
            </#list>
            </tbody>
        </table>
            <div>
                <label class="checkbox-inline"><input type="checkbox" autocomplete="off" data-role="batch-select">
                    全选</label>
                <button class="btn btn-default btn-sm mlm" data-role="batch-delete" data-name="题目"
                        data-url="${ctx}/course/${course.id}/manage/question/deletes">删除
                </button>
            <span class="pull-right text-muted">共${(paginator.getItemCount())!}题</span>
        </div>
        <@web_macro.paginator paginator!/>
    </div>
</div>

</#macro>



