<#assign menu = 'category'/>
<#assign script_controller = 'course/category'/>

<@block_title '分类管理'/>

<#include '/admin/course/layout.ftl'/>

<#macro blockMain>
    <div class="page-header clearfix">
        <button class="btn btn-sm btn-info pull-right add-category" data-toggle="modal" data-target="#modal"
                data-url="${ctx}/admin/category_create', {groupId:group.id}) }}">添加分类
        </button>
        <h1 class="pull-left">{{ group.name }}管理</h1>
    </div>

    <table class="table table-hover" id="category-table">
        <thead>
        <tr>
            <th width="50%">名称</th>
            <th width="15%">编码</th>
            <th width="15%">显示序号</th>
            <th width="20%">操作</th>
        </tr>
        </thead>
        {% include 'TopxiaAdminBundle:Category:tbody.html.twig' %}
    </table>
</#macro>