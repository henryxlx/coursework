<#assign side_nav = 'my-teaching-courses'/>

<#include '/my/layout.ftl'/>

<#macro blockTitle>在教课程 - ${blockTitleParent}</#macro>

<#macro blockMain>
<#--<div class="panel panel-default panel-col">-->
    <div class="panel panel-default panel-col lesson-manage-panel">
        <div class="panel-heading">
    <span class="pull-right">
      <a id="create-course" href="${ctx}/course/create" class="btn btn-info btn-sm"><span
                  class="glyphicon glyphicon-plus"></span> 创建课程</a>
      <#if live_course_enabled! == '1'>
          <a id="create-course" href="${ctx}/course/create?flag=isLive" class="btn btn-info btn-sm"><span
                      class="glyphicon glyphicon-plus"></span> 创建直播课程</a>
      </#if>
      
    </span>
            在教课程
        </div>
        <div class="panel-body">

            <table class="table table-striped table-hover" id="course-table" style="word-break:break-all;">
                <thead>
                <tr>
                    <th width="50%">名称</th>
                    <th>${setting('default.user_name')!'学员'}数</th>
                    <th>价格(元)</th>
                    <th>状态</th>
                    <th>操作</th>
                </tr>
                </thead>
                <tbody>
                <#list courses! as course>
                    <tr>
                        <td>

                            <a class="pull-left mrm" href="${ctx}/course/${course.id}">
                                <img class="course-picture"
                                     src="${default_path('coursePicture',course.middlePicture, 'large')}"
                                     alt="${course.title}" width="100">
                            </a>

                            <div class="mlm">
                                <a href="${ctx}/course/${course.id}">${course.title}</a>
                                <#if course.type == 'live'>
                                    <div><span class="label label-success live-label">直播</span></div>
                                </#if>
                            </div>

                        </td>
                        <td>${course.studentNum}</td>
                        <td>${course.price}</td>
                        <td>${dict_text('courseStatus:html', course.status)}</td>
                        <td>
                            <div class="btn-group">
                                <a class="btn btn-default btn-sm" href="${ctx}/course/${course.id}/manage">管理</a>

                                <a href="#" type="button" class="btn btn-default btn-sm dropdown-toggle"
                                   data-toggle="dropdown">
                                    <span class="caret"></span>
                                </a>
                                <ul class="dropdown-menu pull-right">
                                    <li><a href="${ctx}/course/${course.id}/manage/lesson">课时管理</a></li>
                                    <li><a href="${ctx}/course/${course.id}/manage/files">文件管理</a></li>
                                    <li>
                                        <a href="${ctx}/course/${course.id}/manage/students">${setting('default.user_name')!'学员'}
                                            管理</a></li>
                                </ul>
                            </div>

                        </td>
                    </tr>
                <#else>
                    <tr>
                        <td colspan="20">暂无在教的课程</td>
                    </tr>
                </#list>
                </tbody>
            </table>

            <@web_macro.paginator paginator! />

        </div>
    </div>

</#macro>