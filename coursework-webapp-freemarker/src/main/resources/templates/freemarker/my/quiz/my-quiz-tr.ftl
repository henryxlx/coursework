<#if myTestpaper??>
	<tr>
		<td width="60%">
			${fastLib.plainText(myTestpaper.name, 60)}
			<div>
				<small class="text-muted">来自课程《${course.title}》</small>
				<small class="text-muted mhs">•</small>
				<small class="text-muted"
					   title="开始考试时间">${myTestpaperResult.beginTime?number_to_datetime?string('yyyy年MM月dd日 HH:mm')}</small>
			</div>
		</td>
		<td>
			<#if myTestpaperResult.status == 'reviewing'>
				<span class="text-warning">正在批阅</span>
			<#elseif myTestpaperResult.status == 'finished'>
				<span class="text-muted">得分<strong class="text-warning">${myTestpaperResult.score}</strong>分</span><br>
				<small class="text-muted">做对<strong>${myTestpaperResult.rightItemCount}</strong>题 /
					共<strong>${myTestpaper.itemCount}</strong>题</small>
			<#else>
				<span class="text-muted">未交卷</span>
			</#if>
		</td>
		<td>
			<#if ['doing', 'paused']?seq_contains(myTestpaperResult.status)>
				<a href="${ctx}/test/${myTestpaperResult.id}/show" class="btn btn-default btn-sm">继续考试</a>
			<#else>
				<a href="${ctx}/test/${myTestpaper.id}/redo" class="btn btn-default btn-sm">再做一次</a>
				<a href="${ctx}/test/${myTestpaperResult.id}/result" class="btn btn-link btn-sm">查看结果</a>
			</#if>
		</td>
	</tr>
<#else>

	<tr>
		<td colspan="3">
			<a href="javascript:;">${myTestpaperResult.paperName}</a>
			<br>
			<small>该试卷已删除</small>
		</td>
	</tr>

</#if>
