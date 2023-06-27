<#assign favorites = favorites![] />

<#if flags?seq_contains('mark') && id?? >
    <a class="btn btn-link btn-muted btn-sm marking text-muted"><span
                class="glyphicon glyphicon-bookmark text-muted"></span> 标记</a>
    <a class="btn btn-link btn-sm unMarking" style="display:none"><span class="glyphicon glyphicon-bookmark"></span>
        取消标记</a>
</#if>

<#if flags?seq_contains('favorite') && id?? >
    <button data-url="${ctx}/question/${item.question.id}/favorite?targetType=testpaper&targetId=${(paper.id)!}"
            class="btn btn-link btn-muted btn-sm btn-default favorite-btn"
            <#if favorites?seq_contains(item.question.id)>style="display:none"</#if>><span
                class="glyphicon glyphicon-star-empty text-muted"></span> 收藏
    </button>
    <button data-url="${ctx}/question/${item.question.id}/unfavorite?targetType=testpaper&targetId=${(paper.id)!}"
            class="btn btn-link btn-sm unfavorite-btn"
            <#if !favorites?seq_contains(item.question.id)>style="display:none"</#if>><span
                class="glyphicon glyphicon-star"></span> 取消收藏
    </button>
</#if>

<#if flags?seq_contains('analysis') && id?? >
    <#if !(item.question.analysis == '') >
        <a class="btn btn-sm btn-link analysis-btn"><span class="glyphicon glyphicon-chevron-down"></span> 展开解析</a>
        <a class="btn btn-sm btn-link unanalysis-btn" style="display:none"><span
                    class="glyphicon glyphicon-chevron-up"></span> 收起解析</a>
    </#if>
</#if>