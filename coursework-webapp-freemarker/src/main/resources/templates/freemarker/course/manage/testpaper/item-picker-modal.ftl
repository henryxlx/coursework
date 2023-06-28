<#assign modal_class = 'modal-lg'/>

<#include '/bootstrap-modal-layout.ftl' />

<#macro blockTitle>试卷<#if replaceFor??>替换<#else>添加</#if>题目</#macro>

<#macro blockBody>

    <form id="testpaper-item-picker-form" class="form-inline well well-sm"
          action="${ctx}/course/${course.id}/manage/testpaper/${testpaper.id}/item_picker" novalidate>
        <div class="form-group">
            <select class="form-control" name="target" style="width:200px;height:32px;">
                <@select_options targetChoices, RequestParameters['target']!'', '--按从属--' />
            </select>
        </div>

        <div class="form-group">
            <input type="text" id="keyword" name="keyword" class="form-control" value="${RequestParameters['keyword']!}"
                   placeholder="关键词">
        </div>

        <input type="hidden" name="excludeIds" value="${conditions.excludeIds!?join(',')}">
        <input type="hidden" name="type" value="${conditions.type!}">
        <input type="hidden" name="replace" value="${replace!}">

        <button class="btn btn-primary btn-sm">搜索</button>

    </form>

    <table class="table table-condensed" id="testpaper-item-picker-table">
        <thead>
        <tr>
            <th width="45%">题干</th>
            <th>类型</th>
            <th>分值</th>
            <th>操作</th>
        </tr>
        </thead>
        <tbody>
        <#list questions as question>
            <#include '/course/manage/testpaper/item-pick-tr.ftl' />
        <#else>
            <tr>
                <td colspan="20">
                    <div class="empty">无题目记录</div>
                </td>
            </tr>
        </#list>
        </tbody>
    </table>
    <@web_macro.paginator paginator! />

    <script>app.load('testpaper/item-picker')</script>

</#macro>

<#macro blockFooter>
    <button type="button" class="btn btn-default pull-right" data-dismiss="modal" data-toggle="form-submit"
            data-target="#block-form">关闭
    </button>
</#macro>