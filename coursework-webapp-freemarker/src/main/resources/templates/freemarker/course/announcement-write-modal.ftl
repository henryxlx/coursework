<#include '/bootstrap-modal-layout.ftl' />

<#macro blockTitle><#if announcement.id??>编辑<#else>添加</#if>公告</#macro>

<#macro blockBody>

    <form id="announcement-write-form" method="post"
            <#if announcement.id??>
                action="${ctx}/course/${course.id}/announcement/${announcement.id}/update"
            <#else>
                action="${ctx}/course/${course.id}/announcement/create"
            </#if>
    >

        <div class="form-group">
            <div class="controls">
                <textarea class="form-control" id="announcement-content-field" name="content" data-display="公告内容"
                          data-image-upload-url="${ctx}/editor/upload?token=upload_token('default')">${announcement.content!}</textarea>
            </div>
        </div>
        <#if !announcement.id??>
            <div class="checkbox">
                <label>
                    <input type="checkbox" name="notify" value="notify">
                    发送系统通知给该课程的${setting('default.user_name', '学员')}
                </label>
            </div>
        </#if>
        <input type="hidden" name="_csrf_token" value="${csrf_token('site')}">

    </form>

    <script>
        app.load('course/announcement-write');
    </script>
</#macro>

<#macro blockFooter>
    <button type="button" class="btn btn-link" data-dismiss="modal">取消</button>
    <button type="submit" class="btn btn-primary pull-right" data-toggle="form-submit"
            data-target="#announcement-write-form">保存
    </button>
</#macro>