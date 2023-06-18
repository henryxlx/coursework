<div class="panel panel-default">
    <div class="panel-heading"><h3 class="panel-title">学习进度</h3></div>
    <div class="panel-body">
        <div class="progress">
            <div class="progress-bar" style="width: ${progress.percent};"></div>
        </div>
        <div class="clearfix">
            <#if progress.percent == '100%'>
                <button class="btn btn-default btn-sm disabled pull-right">您已完成全部课时的学习</button>
            <#else>
                <#if nextLearnLesson??>
                    <a class="btn btn-primary btn-sm pull-right" id="next-learn-btn"
                       href="${ctx}/course/${course.id}/learn#lesson/${nextLearnLesson.id}"
                       title="继续学习：${nextLearnLesson.title}">继续学习</a>
                </#if>
            </#if>

            <span class="text-muted">已学: <strong
                        class="text-success">${progress.number}</strong> / <strong>${progress.total}</strong></span>

            <#if course.giveCredit gt 0>
                <span class="text-muted mlm">获得学分：<strong
                            class="text-success">${member.credit}</strong> / <strong>${course.giveCredit}</strong></span>
            </#if>

            <#if member.deadline != 0>
                <br>
                <span class="text-muted">有效期： <strong class="text-success"
                                                      title="从 ${member.createdTime?number_to_datetime?string('yyyy-MM-dd HH:mm')} 到 ${member.deadline?number_to_datetime?string('yyyy-MM-dd HH:mm')}">还有${fastLib.remainTime(member.deadline)}</strong></span>
            </#if>

        </div>
    </div>
</div>