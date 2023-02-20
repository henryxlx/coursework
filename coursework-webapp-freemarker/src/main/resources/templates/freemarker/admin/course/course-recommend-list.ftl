<#assign menu = 'course-recommend-list'/>
<#assign script_controller = 'course/recommend-list'/>

<#include '/admin/course/layout.ftl'/>
<#macro blockTitle>推荐课程列表 - ${blockTitleParent}</#macro>

<#macro blockMain>
    <div class="page-header">
        <h1>推荐课程列表</h1>
    </div>

    <table class="table table-striped table-hover" id="course-recommend-table">
        <thead>
        <tr>
            <th>顺序号</th>
            <th width="50%">课程名称</th>
            <th>创建者</th>
            <th>推荐时间</th>
            <th>操作</th>
        </tr>
        </thead>
        <tbody>
        <#list courses! as course>
            <#assign user = users[''+course.userId]/>
            <#assign category = (categories[''+course.categoryId])!/>
            <#include '/admin/course/course-recommend-tr.ftl' />
        <#else>
            <tr>
                <td colspan="20">
                    <div class="empty">暂无推荐课程</div>
                </td>
            </tr>
        </#list>
        </tbody>
    </table>

    <@web_macro.paginator paginator!/>

</#macro>