<#assign modal_class = 'modal-lg' />

<#include '/bootstrap-modal-layout.ftl' />

<#macro blockTitle>添加资料</#macro>

<#macro blockBody>

    <form id="course-material-form" class="form-horizontal"
          action="${ctx}/course/${course.id}/manage/lesson/${lesson.id}/material/upload" enctype="multipart/form-data"
          method="post">
        <div class="form-group">
            <div class="col-md-10 col-md-offset-1  controls">
                <ul class="list-group" id="material-list" <#if !materials??>style="display:none;"</#if>>
                    <#list materials! as material>
                        <#include '/course/manage/material/list-item.ftl' />
                    </#list>
                </ul>
                <#if !materials??>
                    <span class="control-text text-warning">暂无资料，请上传。</span>
                </#if>
            </div>
        </div>

        <div class="form-group">
            <div class="col-md-10 col-md-offset-1 controls" id="material-file-chooser">
                <#include '/course/manage/material/file-chooser.ftl' />
                <input type="hidden" name="fileId">
            </div>
        </div>

        <div class="form-group">
            <div class="col-md-10 col-md-offset-1 controls">
                <textarea class="form-control" name="description" rows="2" placeholder="填写资料简介 (可选)"></textarea>
            </div>
        </div>

        <div class="form-group">
            <div class="col-md-10 col-md-offset-1 controls">
                <button class="btn btn-primary" id="material-upload-btn" type="submit">添加</button>
            </div>
        </div>

        <input type="hidden" name="_csrf_token" value="${csrf_token('site')}">

    </form>

    <script>app.load('course-manage/material-modal')</script>

</#macro>