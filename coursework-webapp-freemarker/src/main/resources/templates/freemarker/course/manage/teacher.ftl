<#assign script_controller = 'course-manage/teachers'/>
<#assign side_nav = 'teacher'/>

<@block_title '教师设置'/>

<#include '/course/manage/layout.ftl'/>

<#macro blockMain>

    <style>

        .ui-autocomplete {
            border: 1px solid #ccc;
            background-color: #FFFFFF;
        box-shadow: 2px 2px 3px #EEEEEE;
    }
    .ui-autocomplete-ctn{
        margin:0;
        padding:0;
    }
    .ui-autocomplete-item {
        width: 180px;
        overflow:hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
        line-height: 30px;
        padding:0 10px 0 10px;
        cursor: default;
    }
    .ui-autocomplete-item-hover {
        background:#f2f2f2;
    }
    .ui-autocomplete-item-hl {
        background:#F6FF94;
    }

</style>

<div class="panel panel-default panel-col">
    <div class="panel-heading">教师设置</div>
    <div class="panel-body">

        <form id="teachers-form" class="form-horizontal" method="post">
            <@web_macro.flash_messages/>

            <div class="form-group" id="teachers-form-group">
                <div class="col-md-2 control-label"><label>已添加教师</label></div>
                <div class="col-md-8 controls">
                    <ul class="list-group teacher-list-group sortable-list" id="teacher-list-group" data-role="list"
                        style="display:none;"></ul>
                    <div class="input-group">
                        <input class="form-control" id="teacher-input" type="text" data-role="item-input"
                               data-url="${ctx}/course/${course.id}/manage/teachersMatch?q=${query!}">
                        <span class="input-group-btn">
              <button class="btn btn-default" type="button" data-role="item-add">添加</button>
            </span>
                    </div>

                    <script type="text/plain" data-role="model">${json_encode(teachers!'')}</script>
                    <script type="text/x-handlebars-template" data-role="item-template">

                        <li class="list-group-item clearfix" data-role="item">
                            <span class="glyphicon glyphicon-resize-vertical sort-handle"></span>
                            <img src="{{ avatar }}" class="avatar-small">
                            <span class="nickname">{{ username }}</span>
                            <label class="visible-checkbox"><input type="checkbox" value="1" name="visible_{{ id }}"
                                                                   <#if isVisible??>checked="checked"</#if>> 显示</label>
                            <input type="hidden" name="ids[]" value="{{ id }}">
                            <button class="close delete-btn" data-role="item-delete" type="button" title="删除">&times;
                            </button>
                        </li>
                    </script>

                </div>
            </div>

            <div class="form-group">
                <div class="col-md-offset-2 col-md-8 controls">
                    <button type="submit" class="btn btn-fat btn-primary">保存</button>
                </div>
            </div>

            <input type="hidden" name="_csrf_token" value="${csrf_token('site')}">

        </form>
    </div>
</div>

</#macro>