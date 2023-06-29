<@block_title '我的学习'/>

<#include '/layout.ftl'>

<#macro blockContent>

    <@renderController path='/my/avatarAlert' />

    <div class="row row-3-9">
        <div class="col-md-3">

            <div class="panel panel-default">
                <div class="panel-body">
                    <div class="list-group-block">

                        <#if is_granted("ROLE_TEACHER")>
                            <div class="list-group-panel">
                                <div class="list-group-heading">我的教学</div>
                                <ul class="list-group">
                                    <a class="list-group-item
               <#if side_nav == 'my-teaching-courses'> active </#if>"
                                       href="${ctx}/my/teaching/courses">在教课程</a>

                                    <#if setting('classroom.enabled')! == '1'>
                                        <a class="list-group-item
               <#if side_nav == 'my-teaching-classroom'> active </#if>"
                                           href="${ctx}/my/teaching/classrooms">所在${setting('classroom.name')!'班级'}</a>
                                    </#if>

                                    <a class="list-group-item
               <#if side_nav == 'my-teaching-questions'> active </#if>"
                                       href="${ctx}/my/teaching/threads/question">${setting('default.user_name')!'学员'}问答</a>
                                    <a class="list-group-item <#if side_nav == 'my-teaching-discussions'> active </#if>"
                                       href="${ctx}/my/teaching/threads/discussion">${setting('default.user_name')!'学员'}
                                        话题</a>

                                    <a class="list-group-item <#if side_nav == 'my-teaching-check'> active </#if>"
                                       href="${ctx}/my/teacher/reviewing/test/list">试卷批阅</a>

                                    <#if setting('Homework.enabled')! == '1'>
                                        <a class="list-group-item <#if side_nav == 'my-teaching-homework-check'> active </#if>"
                                           href="${ctx}/my/homework">作业批改</a>
                                    </#if>

                                    <#if setting('material-lib.enabled')! == '1'>
                                        <a class="list-group-item <#if side_nav == 'material-lib'> active </#if>"
                                           href="${ctx}/material-lib/browsing">教学资料库</a>
                                    </#if>
                                </ul>
                            </div>
                        </#if>

                        <div class="list-group-panel">
                            <div class="list-group-heading">我的学习</div>
                            <ul class="list-group">
                                <a class="list-group-item
               <#if side_nav == 'my-learning'> active </#if>"
                                   href="${ctx}/my/courses/learning">我的课程</a>

                                <#if setting('classroom.enabled')! == '1'>
                                    <a class="list-group-item
               <#if side_nav == 'my-classroom'> active </#if>"
                                       href="${ctx}/my/classrooms">我的${setting('classroom.name')!'班级'}</a>
                                </#if>

                                <#if setting('course.live_course_enabled')! == '1'>
                                    <a class="list-group-item
               <#if side_nav == 'my-learning-live'> active </#if>"
                                       href="${ctx}/my/livecourses/learing">我的直播课表</a>
                                </#if>
                                <a class="list-group-item
               <#if side_nav == 'my-questions'> active </#if>"
                                   href="${ctx}/my/questions">我的问答</a>
                                <a class="list-group-item
               <#if side_nav == 'my-discussions'> active </#if>"
                                   href="${ctx}/my/discussions">我的话题</a>
                                <a class="list-group-item
               <#if side_nav == 'my-notes'> active </#if>"
                                   href="${ctx}/my/notebooks">我的笔记</a>
                                <#if setting('homework.enabled')! == '1'>
                                    <a class="list-group-item <#if side_nav == 'my-homeworks'> active </#if>"
                                       href="${ctx}/my/homework">我的作业</a>
                                </#if>
                                <a class="list-group-item
               <#if side_nav == 'my-quiz'> active </#if>"
                                   href="${ctx}/my/quiz">我的考试</a>
                                <a class="list-group-item
               <#if side_nav == 'my-group'> active </#if>"
                                   href="${ctx}/my/group">我的小组</a>
                            </ul>
                        </div>

                    </div>
                </div>
            </div>

        </div>
        <div class="col-md-9"><#if blockMain??><@blockMain/></#if></div>
</div>

</#macro>