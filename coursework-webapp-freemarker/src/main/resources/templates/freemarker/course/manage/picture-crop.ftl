<#assign side_nav = 'picture'/>
<#assign script_controller = 'course-manage/picture-crop'/>

<@block_title '课程图片'/>

<#include "/course/manage/layout.ftl"/>

<#macro blockMain>

    <div class="panel panel-default panel-col">
        <div class="panel-heading">
            课程图片
        </div>

        <div class="panel-body">
      <form id="course-picture-crop-form" method="post" enctype="multipart/form-data">
        <@web_macro.flash_messages/>

        <div class="form-group clearfix">
          <div class="col-md-offset-2 col-md-8 controls">
            <img src="${file_url(pictureUrl)}" id="course-picture-crop" width="${scaledSize.width}"
                 height="${scaledSize.height}" data-natural-width="${naturalSize.width}"
                 data-natural-height="${naturalSize.height}"/>
            <div class="help-block">提示：请选择图片裁剪区域。</div>
          </div>
        </div>

        <div class="form-group clearfix">
          <div class="col-md-offset-2 col-md-8 controls">
            <input type="hidden" name="x">
            <input type="hidden" name="y">
            <input type="hidden" name="width">
            <input type="hidden" name="height">
            <button class="btn btn-fat btn-primary" id="upload-picture-btn" type="submit">保存</button>
            <a href="javascript:;" class="go-back btn btn-link">重新选择图片</a>
          </div>
        </div>

        <input type="hidden" name="_csrf_token" value="${csrf_token('site')}">

      </form>

    </div>
  </div>

</#macro>

