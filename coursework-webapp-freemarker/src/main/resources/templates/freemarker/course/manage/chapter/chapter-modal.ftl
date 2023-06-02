<#include '/bootstrap-modal-layout.ftl' />

<#macro blockTitle>
    <#if chapter??>编辑<#else>添加</#if><#if type == 'unit'>${setting('default.part_name')!'节'}<#else>${setting('default.chapter_name')!'章'}</#if>
</#macro>

<#macro blockBody>

    <form id="course-chapter-form" class="form-horizontal" method="post"
          <#if parentId??>data-parentId="${parentId}" </#if>
            <#if chapter??>
                action="${ctx}/course/${course.id}/manage/chapter/${chapter.id}/edit"
            <#else>
                action="${ctx}/course/${course.id}/manage/chapter/create"
            </#if>
    >
        <div class="row form-group">
            <div class="col-md-3 control-label">
                <#if type == 'unit'>
                    <label for="chapter-title-field">${setting('default.part_name')!'节'}标题</label>
                <#else>
                    <label for="chapter-title-field">${setting('default.chapter_name')!'章'}标题</label>
                </#if>
            </div>
            <div class="col-md-8 controls"><input id="chapter-title-field" type="text" name="title"
                                                  value="${(chapter.title)!}" class="form-control"></div>
        </div>
        <input type="hidden" name="type" value="${type}">
    </form>

    <script>app.load('course-manage/chapter-modal')</script>

</#macro>

<#macro blockFooter>
    <button type="button" class="btn btn-link" data-dismiss="modal">取消</button>
    <button id="course-chapter-btn" data-submiting-text="正在提交" type="submit" class="btn btn-primary"
            data-toggle="form-submit" data-target="#course-chapter-form" data-chapter="${(default.chapter_name)!'章'}"
            data-part="${(default.part_name)!'节'}"><#if chapter??>保存<#else>添加</#if></button>
</#macro>

<#--{% set hideFooter = true %}-->
