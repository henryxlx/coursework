<#assign menu = 'course'/>
<#assign script_controller = 'course/manage'/>

<#include '/admin/course/layout.ftl'/>
<#macro blockTitle>课程管理 - ${blockTitleParent}</#macro>

<#macro blockMain>

<div class="page-header">
    <#if liveSetEnabled == '1'>
    <a href="${ctx}/course/create?flag=isLive" class="btn btn-info btn-sm pull-right mls" target="_blank">创建直播课程</a>
    </#if>
    <a href="${ctx}/course/create" class="btn btn-success btn-sm pull-right" target="_blank">创建课程</a>
    <h1>课程管理</h1>
</div>

<form id="message-search-form" class="form-inline well well-sm" action="" method="get" novalidate>
    <div class="form-group">
        <select style="max-width:150px;" class="form-control" name="categoryId">
            <@select_options categoryForCourse!{} RequestParameters['categoryId']!'' '课程分类'/>
        </select>
    </div>
    <div class="form-group">
        <select class="form-control" name="status">
            <@select_options dict['courseStatus']!{} RequestParameters['status']!'' '课程状态'/>
        </select>
    </div>
    <div class="form-group">
        <input class="form-control" type="text" placeholder="标题" name="title" value="${RequestParameters['title']!}">
    </div>
    <div class="form-group">
        <input class="form-control" type="text" placeholder="创建者" name="creator" value="${RequestParameters['creator']!}">
    </div>
    <button class="btn btn-primary">搜索</button>
</form>

<table class="table table-striped table-hover" id="course-table" style="word-break:break-all;">
    <thead>
    <tr>
        <th>编号</th>
        <th width="30%">名称</th>
        <th>连载状态</th>
        <th>${setting('default.user_name', '学员')}数</th>
        <th>状态</th>
        <th>创建者</th>
        <th>操作</th>
    </tr>
    </thead>
    <tbody>
    <#list courses! as course>
        <#assign user = users[''+course.userId]! />
        <#assign category = categories[''+course.categoryId]! />
        <#include '/admin/course/tr.ftl'/>
    <#else>
    <tr><td colspan="20"><div class="empty">暂无课程记录</div></td></tr>
    </#list>
    </tbody>
</table>

<@web_macro.paginator paginator!/>

</#macro>