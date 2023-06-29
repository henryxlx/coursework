<li class="media">
    <div class="pull-left">
        <span class="glyphicon glyphicon-volume-down media-object"></span>
    </div>
    <div class="media-body">
        <div class="notification-body">
            <#assign data = notification.content?eval_json/>
            您已经被管理员添加为课程<a href="${ctx}/course/${data.courseId!}">《${data.courseTitle!}
                》</a>的${setting('default.user_name')!'学员'}，赶紧去学习吧！
        </div>
        <div class="notification-footer">
            ${notification.createdTime?number_to_datetime?string('yyyy-MM-dd HH:mm:ss')}
        </div>
    </div>
</li>