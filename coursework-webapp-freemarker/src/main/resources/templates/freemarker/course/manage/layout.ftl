<#include '/layout.ftl'/>

<#macro blockContent>
<@renderController path='/course/header' params={'course':course, 'manage': true} />

<div class="row">
    <div class="col-md-3">
        <#if blockSide??><@blockSide/><#else>
        <#assign side_nav = side_nav!''/>
        <div class="panel panel-default">
            <div class="panel-body">
                <div class="list-group-panel">
                    <div class="list-group-heading">课程信息</div>
                    <div class="list-group">
                        <a class="list-group-item <#if side_nav == 'base'>active</#if>" href="${ctx}/course/${course.id}/manage/base">基本信息</a>
                        <a class="list-group-item <#if side_nav == 'detail'>active</#if>" href="${ctx}/course/${course.id}/manage/detail">详细信息</a>
                        <a class="list-group-item <#if side_nav == 'picture'>active</#if>" href="${ctx}/course/${course.id}/manage/picture">课程图片</a>

                        <a class="list-group-item <#if side_nav == 'lesson'>active</#if>" href="${ctx}/course/${course.id}/manage/lesson">课时管理</a>
                        <#if course.type?? && course.type == 'live'>
                        <a class="list-group-item <#if side_nav == 'replay'>active</#if>" href="${ctx}/livecourse/${course.id}/manage/replay">录播管理</a>
                        </#if>
                        <a class="list-group-item <#if side_nav == 'files'>active</#if>" href="${ctx}/course/${course.id}/manage/files">文件管理</a>
                    </div>
                </div><!-- /list-group-block -->

                <div class="list-group-panel">
                    <div class="list-group-heading">课程设置</div>
                    <div class="list-group">
                        <a class="list-group-item <#if side_nav == 'teachers'>active</#if>" href="${ctx}/course/${course.id}/manage/teachers">教师设置</a>
                        <a class="list-group-item <#if side_nav == 'students'>active</#if>" href="${ctx}/course/${course.id}/manage/students">${setting('default.user_name')!'学员'}管理</a>
                    </div>
                </div>

                <div class="list-group-panel">
                    <div class="list-group-heading">题库</div>
                    <div class="list-group">
                        <a class="list-group-item <#if side_nav == 'question'>active</#if>" href="${ctx}/course/${course.id}/manage/question">题目管理</a>

                        <a class="list-group-item <#if side_nav == 'question_plumber'>active</#if>" href="${ctx}course_question_plumber', {courseId:course.id, type:'import'}) }}">题目导入/导出</a>

                        <!-- <a class="list-group-item <#if side_nav == 'question_category' >active</#if>" href="${ctx}course_manage_question_category', {courseId:course.id}) }}">题目类别管理</a> -->
                        <a class="list-group-item <#if side_nav == 'testpaper'>active</#if>" href="${ctx}/course/${course.id}/manage/testpaper">试卷管理</a>
                        <a class="list-group-item <#if side_nav == 'testCheck'>active</#if>" href="${ctx}course_manage_test_check', {id:course.id, status:'reviewing'}) }}">试卷批阅</a>
                        <a class="list-group-item <#if side_nav == 'homeworkCheck'>active</#if>" href="${ctx}course_homework_check_list', {courseId:course.id,status:'reviewing'}) }}">作业批阅</a>
                    </div>
                </div>

                <div class="list-group-panel">
                    <div class="list-group-heading">课程运营</div>
                    <div class="list-group">
                        <a class="list-group-item <#if side_nav == 'course_manage_data'>active</#if>" href="${ctx}/course/${course.id}/manage/data">课程学习数据</a>
                    </div>
                </div>
            </div>

        </div>
        </#if>
    </div>
    <div class="col-md-9">
        <#if blockMain??><@blockMain/></#if>
    </div>
</div>
</#macro>