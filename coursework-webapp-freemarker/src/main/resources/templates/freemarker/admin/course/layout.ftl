<#assign nav = 'course'/>

<#include '/admin/layout.ftl'/>

<#macro blockContent>
<div class="col-md-2">
    <#if blockSidebar??><@blockSidebar/><#else>
    <div class="list-group">
        <a href="${ctx}/admin/course" class="list-group-item <#if menu! == 'course'>active</#if>">课程管理</a>
        <#if setting('course.live_course_enabled')??>
        <a href="${ctx}/admin/live/course?status=underway" class="list-group-item <#if menu! == 'live-course'>active</#if>">直播管理</a>
        </#if>
        <a href="${ctx}/admin/course/recommend/list" class="list-group-item <#if menu! == 'course-recommend-list'>active</#if>">推荐课程列表</a>

        <a href="${ctx}/admin/review" class="list-group-item <#if menu! == 'review'>active</#if>">评价管理</a>

        <a href="${ctx}/admin/thread" class="list-group-item <#if menu! == 'thread'>active</#if>">讨论区管理</a>

        <a href="${ctx}/admin/question', {postStatus: 'unPosted'}) }}" class="list-group-item <#if menu! == 'question'>active</#if>">问答管理</a>

        <a href="${ctx}/admin/note" class="list-group-item <#if menu! == 'note'>active</#if>" >笔记管理</a>

        <a href="${ctx}/admin/course/category" class="list-group-item <#if menu! == 'category'>active</#if>">分类管理</a>

        <a href="${ctx}/admin/tag" class="list-group-item <#if menu! == 'tag'>active</#if>">标签管理</a>

        <a href="${ctx}/admin/course/disk" class="list-group-item <#if menu! == 'course-disk'>active</#if>" style="display:none;">文件管理</a>

        <a href="${ctx}/admin/course/data" class="list-group-item <#if menu! == 'course-data'>active</#if>">数据管理</a>

        <a href="${ctx}/admin/teacher" class="list-group-item <#if menu! == 'teacher'>active</#if>">教师管理</a>

    </div>
    </#if>
</div>
<div class="col-md-10">
    <#if blockMain??><@blockMain/></#if>
</div>
</#macro>