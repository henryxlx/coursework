<#assign menu = 'course-data'/>

<@block_title '${course.title}'/>

<#include '/admin/course/layout.ftl'/>

<#macro blockMain>
    <div class="page-header">
        <h1>${course.title}</h1>
    </div>
    <div class="table-responsive">
        <table class="table table-bordered" style="word-break:break-all;text-align:center;">
            <tr class="active">
                <td>课时名</td>
                <td>课时学习人数</td>
                <td>课时完成人数</td>
                <td>课时平均学习时长(分)</td>
                <td>音视频时长(分)</td>
                <td>音视频平均观看时长(分)</td>
                <td>测试平均得分</td>
            </tr>
            <#list lessons! as lesson>
                <tr>
                    <td>${lesson.title}<#if lesson.type == "text">（图文）<#elseif lesson.type == 'video'>（视频）<#elseif lesson.type == 'audio'>（音频）<#elseif lesson.type == 'testpaper'>（试卷）<#elseif lesson.type == 'ppt'>（ppt）</#if></td>
                    <td>${lesson.LearnedNum}</td>
                    <td>${lesson.finishedNum}</td>
                    <td>${(lesson.learnTime/60)?floor}</td>
                    <td><#if lesson.type =='audio' || lesson.type =='video'>${lesson.length}<#else>----</#if></td>
                    <td><#if lesson.mediaSource != 'self' && lesson.type == 'video'>无<#elseif lesson.type =='audio' || lesson.type =='video'>${(lesson.watchTime/60)?floor}<#else>----</#if></td>
                    <td><#if lesson.type =='testpaper'>${lesson.score}<#else>----</#if></td>
                </tr>
            </#list>
        </table>

    </div>


</#macro>