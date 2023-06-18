<#assign script_controller = 'analysis/lesson' />
<#assign href = 'admin/operation/analysis/lesson' />
<#assign menu = 'lesson' />

<#include '/admin/operation/analysis/layout.ftl'/>

<#macro blockAnalysisBody>
	<div class="col-md-12">

		<#if tab=="trend">
			<div id="line-data"></div>
			<input id="data" type="hidden" value='${data!}'>
			</input> <input id="lessonStartDate" type="hidden" value='${lessonStartDate!}'>
		</input>
		<#elseif tab=="detail">
			<#include '/admin/operation/analysis/lesson.table.ftl'/>
		</#if>
	</div>
</#macro>
