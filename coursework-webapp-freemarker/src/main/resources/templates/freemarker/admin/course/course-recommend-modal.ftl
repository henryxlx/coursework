<#assign modalSize = 'large'/>

<#include '/bootstrap-modal-layout.ftl'/>

<#macro blockTitle>设置推荐课程</#macro>
<#macro blockBody>

    <form id="course-recommend-form" class="form-horizontal" method="post"
          action="${ctx}/admin/course/${course.id}/recommend?ref=${ref!}">


        <div class="row form-group">
            <div class="col-md-2 control-label">
                <label for="number">序号</label>
            </div>
            <div class="col-md-7 controls">
                <input type="text" id="number" name="number" class="form-control" value="" data-widget-cid="widget-1"
                       data-explain="请输入0-10000的整数">
                <div class="help-block">请输入0-10000的整数</div>
            </div>
        </div>

    </form>
</#macro>


<#macro blockFooter>
    <button id="course-recommend-btn" data-submiting-text="正在提交" class="btn btn-primary pull-right"
            data-toggle="form-submit" data-target="#course-recommend-form">保存
    </button>
    <button type="button" class="btn btn-link pull-right" data-dismiss="modal">取消</button>
    <script>app.load('course/recommend-modal')</script>
</#macro>