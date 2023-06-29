<#assign script_controller = 'course-manage-file/index'/>
<#assign side_nav = 'file'/>
<#assign tab = 'courseLesson'/>

<@block_title '文件管理'/>

<#include '/course/manage/layout.ftl'/>

<#macro blockMain>

    <style type="text/css">
        .tooltip-inner {
            max-width: 600px;
        }
    </style>

<div class="panel panel-default panel-col">

    <div class="panel-heading">

        <#if type?? && type == 'courselesson'>
            <button class="btn btn-info btn-sm pull-right"
                    data-html5-url="${ctx}/course/${course.id}/manage/batch/upload/course/file/courselesson"
                    data-normal-url="${ctx}/course/${course.id}/manage/upload/course/file/courselesson"
                    data-storage="${storageSetting.upload_mode!}"
            >
                <i class="glyphicon glyphicon-cloud-upload"></i>上传课时文件
            </button>

        <#elseif type?? && type == 'coursematerial'>
            <button class="btn btn-info btn-sm pull-right"
                    data-html5-url="${ctx}/course/${course.id}/manage/batch/upload/course/file/coursematerial"
                    data-normal-url="${ctx}/course/${course.id}/manage/upload/course/file/coursematerial"
                    data-storage="${storageSetting.upload_mode!}"
            >
                <i class="glyphicon glyphicon-cloud-upload"></i>上传备用资料文件
            </button>

        </#if>

        文件管理
    </div>


    <div class="panel-body" id="file-manage-panel">

        <ul class="nav nav-tabs mbm">
            <li <#if type?? && type== 'courselesson'> class="active" </#if>>
                <a href="${ctx}/course/${course.id}/manage/file?type=courselesson">课时文件</a></li>
            <li <#if type?? && type == 'coursematerial'> class="active" </#if>><a
                        href="${ctx}/course/${course.id}/manage/file?type=coursematerial">备用资料文件</a></li>
        </ul>

        <table class="table table-striped table-hover" id="course-lesson-table">
            <thead>
            <tr>
                <th width="5%"><input type="checkbox" data-role="batch-select"></th>
                <th>文件名</th>
                <th>类型</th>
                <th>大小</th>
                <th>最后更新</th>
            </tr>
            </thead>
            <tbody>
            <#list courseLessons! as uploadFile>
            <#include '/course/manage/file/tbody_tr.ftl' />
            <#else>
            <tr class="empty"><td colspan="20">无文件记录</td></tr>
            </#list>
            </tbody>
        </table>

        <label class="checkbox-inline mrm"><input type="checkbox" data-role="batch-select"> 全选</label>


        <div class="btn-group">
            <button class="btn btn-danger btn-sm" data-role="batch-delete" data-name="文件记录" data-url="${ctx}/course/${course.id}/delete/files/{type}', {id:course.id, type:tab}) }}">
                <i class="glyphicon glyphicon-trash"></i>
                删除</button>

        </div>

        <div class="mbl">
            <@web_macro.paginator paginator!/>
        </div>

        <#if type?? && type== 'coursematerial'>

        <div class="alert alert-info">
            <ul>
                <li>上传备用资料文件成功后，在添加课时资料时可直接选取。</li>
                <li><strong><a href="${ctx}/course/${course.id}/material/">课程资料区</a></strong>显示的是所有课时下的资料文件。</li>
            </ul>
        </div>
        </#if>



    </div>

</div>

</#macro>