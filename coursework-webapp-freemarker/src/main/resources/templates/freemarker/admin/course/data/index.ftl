<#assign menu = 'course-data'/>
<#assign script_controller = 'Course/data'/>

<#include '/admin/course/layout.ftl'/>
<#macro blockTitle>数据管理 - ${blockTitleParent}</#macro>

<#macro blockMain>

    <div class="page-header clearfix">
        <h1 class="pull-left">数据管理</h1>
    </div>
    <form id="message-search-form" class="form-inline well well-sm" action="${ctx}/admin/course_data" method="get" novalidate>
        <div class="form-group">
            <select class="form-control" name="categoryId">
<#--                <@select_options category_choices('course') RequestParameters['categoryId']!'' '课程分类'/>-->
            </select>
        </div>
        <div class="form-group">
            <input class="form-control" type="text" placeholder="标题" name="title" value="${RequestParameters['title']!}">
        </div>
        <div class="form-group">
            <input class="form-control" type="text" placeholder="创建者" name="creator" value="${RequestParameters['creator']!}">
        </div>
        <button class="btn btn-primary">搜索</button>
    </form>
    <div class="table-responsive">
        <table class="table table-bordered" style="word-break:break-all;text-align:center;">
            <tr class="active">
                <td width="30%">课程名</td>
                <td>课时数</td>
                <td><#if setting('default.user_name')?? >{{setting('default.user_name')||default('学员')}}<#else>学员</#if>人数</td>
                <td>完成课程人数</td>
                <td>课程学习时长(分)</td>
                <td>课程收入(元)</td>
                <td>操作</td>
            </tr>

            <#if courses??>
                <#list courses as course>
                    <tr>
                        <td><a data-toggle="modal" data-target="#modal" data-url="${ctx} course_detail_data',{id:course.id})}}" href="javascript:">{{course.title}}</a></td>
                        <td>{{course.lessonCount}}</td>
                        <td>{{course.studentNum}}</td>
                        <td>{{course.isLearnedNum}}</td>
                        <td>{{(course.learnTime/60)||round(0, 'floor')}}</td>
                        <td>{{course.income}}</td>
                        <td><a href="${ctx}admin_course_lesson_data,{id:course.id})}}">查看课时数据</a></td>
                    </tr>
                </#list>
            </#if>


        </table>
        <@web_macro.paginator paginator!/>
    </div>


</#macro>