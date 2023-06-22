<#include '/bootstrap-modal-layout.ftl' />

<#macro blockTitle>公告</#macro>

<#macro blockBody>

    <div class="announcement-detail clearfix"><a id="show-all-announcement" class="btn btn-sm btn-link pull-right"
                                                 data-url="${ctx}/course/${announcement.courseId}/announcements">显示课程全部公告</a>
    </div>
    <div class="announcement-detail clearfix">
        <div>${announcement.content}</div>
        <div class="text-muted pull-right mtm">
            发表于 ${announcement.createdTime?number_to_datetime?string('yyyy-MM-dd HH:mm')}</div>
    </div>

    <script>
        app.load('course/announcement-modal');
    </script>

</#macro>