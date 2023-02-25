<#assign tab_nav = 'learning'/>

<#include '/my/course/layout.ftl'/>

<#macro blockTitle>学习中 - 我的课程 - ${blockTitleParent}</#macro>

<#macro blockMain>
	<div class="panel panel-default panel-col">
		<div class="panel-heading">我的课程</span></div>
		<div class="panel-body">
			<#include '/my/course/nav-pill.ftl'/>
			<#if courses??>
				<@renderController path='/course/coursesBlock' params={'courses': courses, 'view': 'grid', 'mode': 'learn'} />
				<@web_macro.paginator paginator! />
			<#else>
				<div class="empty">暂无学习中的课程</div>
			</#if>
		</div>
	</div>

</#macro>



