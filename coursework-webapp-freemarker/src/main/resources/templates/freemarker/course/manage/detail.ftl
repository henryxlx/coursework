<#assign script_controller = 'course-manage/detail'/>
<#assign side_nav = 'detail'/>

<@block_title '详细信息'/>

<#include '/course/manage/layout.ftl'/>

<#macro blockMain>

    <div class="panel panel-default panel-col">
        <div class="panel-heading">详细信息</div>
        <div class="panel-body">
            <form id="course-base-form" class="form-horizontal" method="post">
                <@web_macro.flash_messages/>

            <div class="form-group">
                <div class="col-md-2 control-label"><label>课程简介</label></div>
                <div class="col-md-8 controls">
                    <textarea name="about" rows="10" id="course-about-field" class="form-control" data-image-upload-url="${ctx}/editor_upload?token:upload_token=course" >${(course.about)!}</textarea>
                </div>
            </div>

            <div class="form-group dynamic-collection" id="course-goals-form-group">
                <div class="col-md-2 control-label"><label>课程目标</label></div>
                <div class="col-md-8 controls">
                    <ul class="list-group sortable-list" data-role="list" style="margin-bottom:10px;display:none;"></ul>
                    <div class="input-group">
                        <input id="teacher-input" type="text" data-role="item-input" class="form-control">
                        <span class="input-group-btn">
              <button class="btn btn-default" type="button" data-role="item-add">添加</button>
            </span>
                    </div>

                    <script type="text/plain" data-role="model">${json_encode(course.goals!)}</script>
                    <script type="text/x-handlebars-template" data-role="item-template">
                        <#noparse>
                            <li class="list-group-item clearfix" data-role="item">
                                <span class="glyphicon glyphicon-resize-vertical sort-handle"></span>
                                {{ this }}
                                <input type="hidden" name="goals[]" value="{{ this }}">
                                <button class="close delete-btn" data-role="item-delete" type="button" title="删除">
                                    &times;
                                </button>
                            </li>
                        </#noparse>
                    </script>

                </div>
            </div>

            <div class="form-group" id="course-audiences-form-group">
                <div class="col-md-2 control-label"><label>适应人群</label></div>
                <div class="col-md-8 controls">

                    <ul class="list-group sortable-list  dynamic-collection" data-role="list"
                        style="margin-bottom:10px;display:none;"></ul>
                    <div class="input-group">
                        <input id="teacher-input" type="text" data-role="item-input" class="form-control">
                        <span class="input-group-btn">
              <button class="btn btn-default" type="button" data-role="item-add">添加</button>
            </span>
                    </div>

                    <script type="text/plain" data-role="model">${json_encode(course.audiences!)}</script>
                    <script type="text/x-handlebars-template" data-role="item-template">
                        <#noparse>
                            <li class="list-group-item clearfix" data-role="item">
                                <span class="glyphicon glyphicon-resize-vertical sort-handle"></span>
                                {{ this }}
                                <input type="hidden" name="audiences[]" value="{{ this }}">
                                <button class="close delete-btn" data-role="item-delete" type="button" title="删除">
                                    &times;
                                </button>
                            </li>
                        </#noparse>
                    </script>

                </div>
            </div>

            <div class="form-group">
                <div class="col-md-8 col-md-offset-2 controls">
                    <button class="btn btn-fat btn-primary" id="course-create-btn" type="submit">保存</button>
                </div>
            </div>

            <input type="hidden" name="_csrf_token" value="${csrf_token('site')}">

        </form>
    </div>
</div>

</#macro>


