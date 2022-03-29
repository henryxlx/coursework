<tr>
    <td>
        <a href="<#if status?? && status == 'reviewing'>{${ctx}/test/${paperResult.id}/teacher/check}</#if><#if status?? && status == 'finished'>{${ctx}/test/${paperResult.id}/result}</#if>">{{ testpaper.name||plain_text(60) }}</a>
        <br>
        <small class="text-muted">来自课程《${course.title}》</small>
        <small class="text-muted mhs">•</small>
        <small class="text-muted">共${testpaper.score}分 / ${testpaper.itemCount}道题	</small>
    </td>
    <td>
        <@web_macro.user_link student!/> <br>
        <small class="text-muted">{{ paperResult.endTime||date("Y年n月d日 H:i") }} 交卷</small>
    </td>
    <td>
        <#if status?? && status == 'reviewing'>
        <a href="{${ctx}/test/${paperResult.id}/teacher/check" class="btn btn-default btn-sm pull-right" target="_blank">批阅</a>
        </#if>

        <#if status?? && status == 'finished'>
        <div>
            <div class="text-warning">总分 <strong>${paperResult.score}</strong> 分</div>
            <div class="text-muted"><small>客观题${paperResult.objectiveScore}分 / 主观题${paperResult.subjectiveScore}分</small></div>
            <div class="text-success"><small>${teacher.nickname||default('系统')} 批于${paperResult.checkedTime||date("Y年n月d日 H:i")}</small></div>
        </div>
       </#if>
    </td>
</tr>