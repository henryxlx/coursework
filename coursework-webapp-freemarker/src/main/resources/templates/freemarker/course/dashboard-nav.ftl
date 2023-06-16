<ul class="nav nav-tabs course-dashboard-tabs clearfix" id="course-tabs">
    <#if blockCourseDashboardNav??><@blockCourseDashboardNav/><#else>
        <li class="<#if nav == 'lesson'>active</#if>"><a class="js-nav" href="${ctx}/course/${course.id}">课时</a></li>
        <li class="<#if nav == 'thread'>active</#if>"><a class="js-nav"
                                                         href="${ctx}/course/${course.id}/threads">讨论区</a></li>
        <li class="<#if nav == 'material'>active</#if>"><a class="js-nav" href="${ctx}/course/${course.id}/materials">资料区</a>
        </li>
        <li class="<#if nav == 'info'>active</#if>"><a class="js-nav" href="${ctx}/course/${course.id}/info">课程信息</a>
        </li>
    </#if>
</ul>