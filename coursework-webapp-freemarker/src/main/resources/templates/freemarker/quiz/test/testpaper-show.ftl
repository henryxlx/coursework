<#include '/quiz/test/testpaper-layout.ftl'/>

<#macro blockTestpaperHeadingStatus>
    <#if isPreview??>
        <div class="label label-warning">试卷预览中</div>
    <#else>
        <#if paperResult ?? && paperResult.status == 'doing'>
            <div class="label label-primary testpaper-status-doing">答题中</div>
        </#if>
    </#if>
</#macro>

<#macro blockTestpaperHeadingContent>
    <div class="testpaper-description">${bbCode2Html(paper.description)}</div>
    <div class="testpaper-metas">
        共 ${paper.itemCount} 题，总分 ${paper.score} 分
        <#if paper.passedScore gt 0>，及格为 ${paper.passedScore} 分</#if>
        <#if paper.limitedTime gt 0>，请在 ${paper.limitedTime} 分钟内作答</#if>
        。
    </div>
</#macro>

<#macro blockTestpaperBodySidebar>
    <div class="testpaper-card" data-spy="affix" data-offset-top="200">
        <#include '/quiz/test/test-paper-card.ftl' />
    </div>
</#macro>