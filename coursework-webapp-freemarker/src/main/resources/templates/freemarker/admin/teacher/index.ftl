<#assign menu = 'teacher'/>
<#assign script_controller = 'user/teacher-list'/>

<@block_title '教师管理'/>

<#include '/admin/course/layout.ftl'/>

<#macro blockMain>
    <div class="page-header">
        <h1>教师管理</h1>
    </div>

    <table id="teacher-table" class="table table-striped table-hover" data-search-form="#user-search-form">
        <thead>
        <tr>
            <th>用户名</th>
            <th>推荐教师</th>
            <th>最近登录</th>
            <th>操作</th>
        </tr>
        </thead>
        <tbody>
        <#if users??>
            <#list  users as user>
                <#include '/admin/teacher/tr.ftl' />
            </#list>
        <#else>
            <tr><td colspan="20"><div class="empty">暂无教师记录</div></td></tr>
        </#if>
        </tbody>
    </table>
    <@web_macro.paginator paginator!/>
</#macro>