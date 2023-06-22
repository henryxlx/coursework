<#assign modal_class= "modal-lg" />

<#include '/bootstrap-modal-layout.ftl' />

<#macro blockTitle>查看本课程的所有公告</#macro>

<#macro blockBody>

    <table class="table table-bordered">
        <thead>
        <tr>
            <th>创建者</th>
            <th>公告内容</th>
            <th>创建时间</th>
            <th>更新时间</th>
        </tr>
        </thead>
        <tbody>
        <#list announcements as announcement>
            <#local user = users[''+announcement.userId]!/>
            <tr>
                <td> ${user.username!} </td>
                <td>${announcement.content}</td>
                <td>${announcement.createdTime?number_to_datetime?string('yyyy-MM-dd HH:mm:ss')}</td>
                <#if !announcement.updatedTime?? || announcement.updatedTime == 0>
                    <td> 尚未更新</td>
                <#else>
                    <td>${announcement.updatedTime?number_to_datetime?string('yyyy-MM-dd HH:mm:ss')}</td>
                </#if>
            </tr>
        </#list>
        </tbody>
    </table>

</#macro>

<#macro blockFooter>
    <button type="button" class="btn btn-default" data-dismiss="modal">退出</button>
</#macro>