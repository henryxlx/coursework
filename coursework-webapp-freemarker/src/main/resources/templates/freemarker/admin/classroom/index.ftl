<#assign menu = 'classroom'/>
<#assign script_controller = 'classroom/classroom'/>
<#include '/admin/classroom/layout.ftl'/>

<#macro blockMain>

    <div class="page-header clearfix">
        <h1 class="pull-left">班级管理</h1>
    </div>

    <form id="message-search-form" class="form-inline well well-sm" action="" method="get" novalidate>
        <div class="form-group">
            <input class="form-control" type="text" placeholder="${setting('classroom.name', '班级')}名称" name="titleLike"
                   value="${RequestParameters['titleLike']!}">
        </div>

        <button class="btn btn-primary">搜索</button>
    </form>

    <p class="text-muted">
        <span class="mrl">班级：<strong class="inflow-num">${(classroomStatusNum.total)!0}</strong>个</span>
        <span class="mrl">已发布：<strong class="inflow-num">${(classroomStatusNum.published)!0}</strong>个</span>
        <span class="mrl">已关闭：<strong class="inflow-num">${(classroomStatusNum.closed)!0}</strong>个</span>
        <span class="mrl">未发布：<strong class="inflow-num">${(classroomStatusNum.draft)!0}</strong>个</span>
    </p>

    <#if classroomInfo??>
        <table class="table table-striped table-hover 111" id="classroom-table">
            <thead>
            <tr>
                <th>班级编号</th>
                <th width="22%">班级名称</th>
                {% include 'org/parts/table-thead-tr.html.twig' %}
                <th>课程数</th>
                <th>学员数</th>
                <th width="12%">价格</th>
                <th>状态</th>
                <th>操作</th>
            </tr>
            </thead>
            <tbody>

            <#list classroomInfo as classroom>
                <#assign category = categories[classroom.categoryId]! />
                <#include '/admin/classroom/table-tr.ftl' />

            </#list>

            </tbody>

        </table>
    <#else>
        <div class="empty">暂无班级信息!</div>
    </#if>
    <div class="pull-right">
        <@web_macro.paginator paginator! />
    </div>


</#macro>
