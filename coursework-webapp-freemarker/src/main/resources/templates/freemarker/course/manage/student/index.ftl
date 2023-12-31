<#assign script_controller = 'course-manage/students'/>
<#assign side_nav = 'student'/>

<@block_title "${setting('default.user_name', '学员')}管理"/>

<#include '/course/manage/layout.ftl'/>

<#macro blockMain>

    <div class="panel panel-default panel-col">
        <div class="panel-heading">${setting('default.user_name', '学员')}管理

            <#if userAcl.isAdmin() || setting('course.teacher_export_student', 'false') == 'true' >
                <a class="btn btn-info btn-sm pull-right mhs" href="${ctx}/course/${course.id}/manage/student/export"><i
                            class="glyphicon glyphicon-export"></i> 导出${setting('default.user_name', '学员')}</a>
            </#if>
            <#if userAcl.isAdmin() ||  isTeacherAuthManageStudent == 1>
                <button class="btn btn-info btn-sm pull-right mhs" id="student-add-btn" data-toggle="modal"
                        data-target="#modal" data-url="${ctx}/course/${course.id}/manage/student/create"><i
                            class="glyphicon glyphicon-plus"></i> 添加${setting('default.user_name', '学员')}</button>
            </#if>

        </div>

        <div class="panel-body">
            <form class="form-inline well well-sm " action="" method="get" novalidate>

                <div class="form-group">
                    <input class="form-control " type="text" placeholder="请输入用户名" name="nickName"
                           value="${RequestParameters['nickName']!}">

                    <button class="btn btn-primary">搜索</button>
                </div>
                <div class="clearfix"></div>


            </form>
            <table class="table table-striped" id="course-student-list">
                <thead>
                <tr>
                    <th width="40%">${setting('default.user_name', '学员')}</th>
                    <th width="30%">学习进度</th>
                    <th width="30%">操作</th>
                </tr>
                </thead>
                <tbody>
                <#list students! as student>
                    <#assign user = users[''+student.userId]! />
                    <#assign progress = progresses[''+student.userId]! />
                    <#assign isFollowing = followingIds?seq_contains(user.id)/>

                    <#include '/course/manage/student/tr.ftl'/>
                <#else>
                    <tr class="empty">
                        <td colspan="20">无${setting('default.user_name', '学员')}记录</td>
                    </tr>
                </#list>
                </tbody>
            </table>

            <@web_macro.paginator paginator!/>

        </div>

    </div>


</#macro>