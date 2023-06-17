<div data-role="navigation">
    <ul class="nav nav-tabs">
        <li
                <#if type == 'unPosted'>
                    class="active"
                </#if>
        ><a href="${ctx}/admin/course/question?postStatus=unPosted">未回复</a></li>
        <li
                <#if type == 'all'>
                    class="active"
                </#if>
        ><a href="${ctx}/admin/course/question?postStatus=all">所有</a></li>
    </ul>
</div>