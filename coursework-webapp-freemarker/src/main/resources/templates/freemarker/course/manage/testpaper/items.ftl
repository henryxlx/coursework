<#assign script_controller = 'testpaper/item-manager'/>
<#assign side_nav = 'testpaper'/>

<@block_title '试卷题目管理'/>

<#include '/course/manage/layout.ftl'/>

<#macro blockMain>

    <style>
        tr.placeholder {
            display: block;
            background: red;
            position: relative;
            margin: 0;
            padding: 0;
            border: none;
        }

        tr.placeholder:before {
            content: "";
            position: absolute;
            width: 0;
            height: 0;
            border: 5px solid transparent;
            border-left-color: red;
            margin-top: -5px;
            left: -5px;
            border-right: none;
        }
    </style>

    <div class="panel panel-default panel-col" id="testpaper-items-manager">
        <div class="panel-heading">
            ${testpaper.name}
        </div>

        <div class="panel-body clearfix">

            <ol class="breadcrumb">
                <li><a href="${ctx}/course/${course.id}/manage/testpaper">试卷管理</a></li>
                <li class="active">试卷题目管理</li>
            </ol>

            <div class="clearfix mbm">
                <button data-url="${ctx}/course/${course.id}/manage/testpaper/${testpaper.id}/item_picker"
                        class="btn btn-info btn-sm pull-right" data-role="pick-item"><span
                            class="glyphicon glyphicon-plus"></span> 新增试题
                </button>
                <ul class="nav nav-pills nav-mini">
                    <#list testpaper['metas']['question_type_seq']! as type>
                        <li><a href="javascript:" data-type="${type}" data-name="${dict_text('questionType', type)}"
                               class="testpaper-nav-link">${dict_text('questionType', type)}</a></li>
                    </#list>
                </ul>
            </div>

            <form method="post" id="testpaper-items-form" class="form-horizontal">
                <div id="testpaper-stats" class="text-success"></div>
                <table class="table table-striped table-hover tab-content" id="testpaper-table">
                    <thead>
                    <tr>
                        <th></th>
                        <th><input type="checkbox" data-role="batch-select"></th>
                        <th>题号</th>
                        <th width="40%">题干</th>
                        <th>类型</th>
                        <th>难度</th>
                        <th width="8%">分值</th>
                        <th>操作</th>
                    </tr>
                    </thead>

                    <#list items?keys as type>
                        <#assign typeItems = items[type] />
                        <tbody data-type="${type}" id="testpaper-items-${type}" class="hide testpaper-table-tbody">
                        <#list typeItems as item>
                            <#assign question = questions[''+item.questionId]! />
                            <#include '/course/manage/testpaper/item-tr.ftl' />
                            <#list subItems[''+item.questionId]! as item>
                                <#assign question = questions[''+item.questionId] />
                                <#include '/course/manage/testpaper/item-tr.ftl' />
                            </#list>
                        </#list>
                        </tbody>
                    </#list>

                </table>
                <div>
                    <label class="checkbox-inline"><input type="checkbox" data-role="batch-select"> 全选</label>
                    <button type="button" class="btn btn-default btn-sm mlm" data-role="batch-delete" data-name="题目">
                        删除
                    </button>
                </div>

                <button type="button" class="btn btn-primary pull-right mlm request-save">保存</button>
                <div>
                    <a href="${ctx}/course/${course.id}/manage/testpaper" class="btn btn-link  pull-right">返回</a>
                </div>

                <input type="hidden" name="_csrf_token" value="${csrf_token('site')}">
            </form>
        </div>

        <div id="testpaper-confirm-modal" class="modal fade" tabindex="-1" role="dialog" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                        <h4 class="modal-title">确认试卷题目信息</h4>
                    </div>
                    <div class="modal-body">
                        <table class="table table-bordered">
                            <thead>
                            <tr>
                                <th>题目类型</th>
                                <th>题目数量</th>
                                <th>总分值</th>
                            </tr>
                            </thead>
                            <tbody class="detail-tbody"></tbody>
                        </table>
                    </div>
                    <div class="modal-footer">
                        <button class="btn btn-link" data-dismiss="modal" type="button">关闭</button>
                        <button type="button" class="btn btn-primary confirm-submit" data-saving-text="正在保存...">确定
                        </button>
                    </div>
                </div>
            </div>
        </div>

        <script type="text/x-handlebars-template" data-role="confirm-tr-template">
            <#noparse>
                <tr>
                    <td>{{name}}</td>
                    <td>{{count}}题</td>
                    <td>{{score}}分</td>
                </tr>
            </#noparse>
        </script>

    </div>

</#macro>