<#assign nav = 'classroom'/>

<#include '/admin/layout.ftl'/>

<#macro blockContent>
    <div class="col-md-2">
        <#if blockSidebar??><@blockSidebar/><#else>
            <div class="list-group">
                <a href="${ctx}/admin/course" class="list-group-item <#if menu! == 'classroom'>active</#if>">班级管理</a>

                <a href="${ctx}/admin/course/recommend/list"
                   class="list-group-item <#if menu! == 'course-recommend-list'>active</#if>">推荐班级列表</a>

                <a href="${ctx}/admin/course/review"
                   class="list-group-item <#if menu! == 'review'>active</#if>">班级评价</a>

                <a href="${ctx}/admin/course/thread"
                   class="list-group-item <#if menu! == 'thread'>active</#if>">班级话题</a>

                <a href="${ctx}/admin/course/question?postStatus=unPosted"
                   class="list-group-item <#if menu! == 'question'>active</#if>">问答管理</a>

                <a href="${ctx}/admin/course/note" class="list-group-item <#if menu! == 'note'>active</#if>">笔记管理</a>

                <a href="${ctx}/admin/category?group=course"
                   class="list-group-item <#if menu! == 'category'>active</#if>">分类管理</a>

            </div>
        </#if>
    </div>
    <div class="col-md-10">
        <#if blockMain??><@blockMain/></#if>
    </div>
</#macro>