<#assign script_controller = 'course-manage/manage'/>
<#assign side_nav = 'course_manage_data'/>

<@block_title '课程学习数据'/>

<#include '/course/manage/layout.ftl'/>

<#macro blockMain>

    <div class="panel panel-default panel-col course-data">
        <div class="panel-heading">课程学习数据</div>
        <div class="panel-body">
            <div class="table-responsive">
                <table class="table table-bordered" id="learning-data-table"
                       style="word-break:break-all;text-align:center;">
                    <tr class="active">
                        <td>课程名</td>
                        <td>课程学习人数</td>
                        <td>课程完成人数</td>
                        <td>课程平均学习时长(分)</td>
                        <td>课程提问总数</td>
                        <td>课程笔记总数</td>
                        <td>----</td>
                    </tr>
                    <tr>
                        <td><a data-toggle="modal" data-target="#modal"
                               data-url="${ctx}/course/${course.id}/detail/data"
                               href="javascript:">${course.title}</a></td>
                        <td>${course.studentNum!}</td>
                        <td>${isLearnedNum!}</td>
                        <td>${(learnTime!0/60)?floor}</td>
                        <td>${questionCount!}</td>
                        <td>${noteCount!}</td>
                        <td>----</td>
                    </tr>
                    <tr class="active">
                        <td width="35%">课时名</td>
                    <td>课时学习人数</td>
                    <td>课时完成人数</td>
                    <td>课时平均学习时长(分)</td>
                    <td>音视频时长(分)</td>
                    <td>音视频平均观看时长(分)</td>
                    <td>测试平均得分</td>
                </tr>
                    <#list lessons! as lesson>
                        <tr>
                            <td><a data-toggle="modal" data-target="#modal"
                                   data-url="${ctx}/course/${lesson.courseId}/lesson/${lesson.id}/detail/data"
                                   href="javascript:">${lesson.title}<#if lesson.type == "text">（图文）<#elseif lesson.type == 'video'>（视频）<#elseif lesson.type == 'audio'>（音频）<#elseif lesson.type == 'testpaper'>（试卷）<#elseif lesson.type == 'ppt'>（ppt）</#if></a>
                            </td>
                            </td>
                            <td>${lesson.LearnedNum}</td>
                            <td>${lesson.finishedNum}</td>
                            <td>${((lesson.learnTime)!0/60)?floor}</td>
                            <td><#if lesson.type =='audio' || lesson.type =='video'>${lesson.length}<#else>----</#if></td>
                            <td><#if lesson.mediaSource != 'self' && lesson.type == 'video'>无<#elseif lesson.type =='audio' || lesson.type =='video'>${(lesson.watchTime/60)||round(0, 'floor')}<#else>----</#if></td>
                            <td><#if lesson.type =='testpaper'>${lesson.score!}<#else>----</#if></td>
                        </tr>
                    </#list>
            </table>
            <p class="text-success">注：网络视频的观看时长无法获取</p>
        </div>
    </div>
</div>

</#macro>



