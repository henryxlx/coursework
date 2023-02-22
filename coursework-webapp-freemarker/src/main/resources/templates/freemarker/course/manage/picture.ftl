<#assign side_nav = 'picture'/>
<#assign script_controller = 'course-manage/picture'/>
<#include "/course/manage/layout.ftl"/>
<#macro blockTitle>课程图片 - ${blockTitleParent}</#macro>
<#macro blockMain>

<div class="panel panel-default panel-col">
    <div class="panel-heading">
        课程图片
    </div>

    <div class="panel-body">
        <form id="course-picture-form" method="post" action="${ctx}/course/${course.id}/manage/picture" enctype="multipart/form-data">
            <@web_macro.flash_messages/>

            <div class="form-group clearfix">
                <div class="col-md-offset-2 col-md-8 controls">
                    <img src="${default_path('coursePicture', course.largePicture!, '')}" />
                </div>
            </div>

            <div class="form-group clearfix">
                <div class="col-md-offset-2 col-md-8 controls">
                    <input id="course-picture-field" type="file" name="picture" accept="image/gif,image/jpeg,image/png">

                    <p class="help-block">你可以上传jpg, gif, png格式的文件, 图片建议尺寸至少为480x270。<br>文件大小不能超过<strong>2M</strong>。</p>

                </div>
            </div>

            <div class="form-group clearfix">
                <div class="col-md-offset-2 col-md-8 controls">
                    <button class="btn btn-fat btn-primary" id="upload-picture-btn" type="submit">上传</button>
                </div>
            </div>

            <input type="hidden" name="_csrf_token" value="${csrf_token('site')}">

        </form>

    </div>
</div>

</#macro>

