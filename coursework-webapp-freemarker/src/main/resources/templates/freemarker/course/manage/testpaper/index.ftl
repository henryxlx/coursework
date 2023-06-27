<#assign script_controller = 'test-paper/index'/>
<#assign side_nav = 'testpaper'/>
<#assign  parentId= parentId! />
<#include '/course/manage/layout.ftl'/>
<#macro blockTitle>试卷管理 - ${blockTitleParent}</#macro>

<#macro blockMain>


<div class="panel panel-default panel-col">
    <div class="panel-heading">
        <a href="${ctx}/course/${course.id}/manage/testpaper/create" class="btn btn-info btn-sm pull-right mls"><span class="glyphicon glyphicon-plus"></span>创建试卷</a>
        试卷管理
    </div>

    <div class="panel-body " id="quiz-table-container">

        <table class="table table-striped table-hover" id="quiz-table">
            <@web_macro.flash_messages/>
            <thead>
            <tr>
                <th><input type="checkbox"  autocomplete="off"  data-role="batch-select"></th>
                <th width="38%">名称</th>
                <th>状态</th>
                <th>题目统计</th>
                <th>时间限制</th>
                <th>更新人/时间</th>
                <th>操作</th>
            </tr>
            </thead>
            <tbody>
            <#list testpapers! as testpaper>
                <#assign user = users[''+testpaper.updatedUserId]! />
                <#include '/course/manage/testpaper/tr.ftl'/>
            <#else>
            <tr><td colspan="20"><div class="empty">还没有试卷，请点击右上角按钮，<a href="${ctx}/course/${course.id}/manage/testpaper/create">创建一个新试卷</a></div></td></tr>
            </#list>
            </tbody>
        </table>
        <div>
            <label class="checkbox-inline"><input type="checkbox"  autocomplete="off" data-role="batch-select"> 全选</label>
            <button class="btn btn-default btn-sm mlm" data-role="batch-delete"  data-name="试卷" data-url="${ctx}/course/${course.id}/manage/testpaper/deletes">删除</button>
        </div>

        <@web_macro.paginator paginator!/>

        <div class="text-muted mtl">提示：只有已发布的试卷才能被添加到课程中去，试卷一旦发布后将不能修改。</div>


    </div>
</div>

</#macro>



