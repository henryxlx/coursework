<#assign script_controller = 'analysis/course' />
<#assign href = 'admin/operation/analysis/course' />
<#assign menu = 'course' />

<#include '/admin/operation/analysis/layout.ftl'/>

<#macro blockAnalysisBody>

	<div class="col-md-12">

		<#if tab=="trend">
			<div id="line-data"></div>
			<input id="data" type="hidden" value='${data!}'>
			</input><input id="courseStartDate" type="hidden" value='$courseStartDate!}'>
		</input>
		<#elseif tab=="detail">
			<#include '/admin/operation/analysis/course.table.ftl' />
		</#if>
	</div>
</#macro>
