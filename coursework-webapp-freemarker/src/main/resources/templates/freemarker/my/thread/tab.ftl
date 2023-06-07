<#if setting('classroom.enabled', '0') == '1'>
    <ul class="nav nav-tabs">
        <li class="<#if threadType == 'course'>active</#if>"><a href="${ctx}/my/discussions">课程话题</a></li>
        <li class="<#if threadType == 'classroom'>active</#if>"><a
                    href="${ctx}/my/classroom/discussions">${setting('classroom.name', '班级')}话题</a></li>
    </ul>
    <br>
</#if>