<#assign bodyClass = 'course-dashboard-page'/>

<#assign script_arguments>{"course_uri": "${ctx}/course/${course.id}"}</#assign>

<@block_title '${course.title}'/>

<#include '/layout.ftl'>

<#macro blockContent>

    <div class="container-gap course-dashboard-container">

        <@renderController path='/course/header' params={'course':course,'courseId':course.id} />

        <div class="row row-8-4">
            <div class="col-md-8 course-dashboard-main">
                <div class="panel panel-default">
                    <div class="panel-body" style="overflow: hidden;">
                        <#if blockDashboardMain??><@blockDashboardMain/></#if>
                    </div>
                </div>
                <div>
                    <#if blockDashboardRelatedCoursesBlock??><@blockDashboardRelatedCoursesBlock/></#if>
                </div>
            </div>
            <div class="col-md-4 course-dashboard-side">

                <#if setting('classroom.enabled', '0') != '0' && isLearnInClassrooms?? >
                    <div class="panel panel-default go-class">
                        <div class="panel-body ">
                            <p>您正在以下${setting('classroom.name')!'班级'}学习此课程</p>
                            <#list isLearnInClassrooms as classroom>
                                <div class="pull-right go-class-link">
                                    <a href="${ctx}/classroom/${classroom.id}">去${classroom.title} > </a>
                                </div>
                            </#list>
                        </div>
                    </div>
                </#if>

                <@renderController path='/course/progressBlock' params={'course':course, 'courseId': course.id, 'userId':appUser.id}/>
                <@renderController path='/course/announcementBlock' params={'courseId': course.id, 'userId':appUser.id}/>
                <@renderController path='/course/thread/questionBlock' params={'course': course}/>


            </div>
        </div>

    </div>

</#macro>

<#macro blockBottom>
    <div id="course-modal" class="modal"></div>
</#macro>