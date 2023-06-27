<div class="testpaper-question-stem-wrap clearfix">
    <div class="testpaper-question-seq-wrap">
        <div class="testpaper-question-seq">${item.seq!}</div>
        <div class="testpaper-question-score">${item.score!item.question.score}分</div>
    </div>
    <#if item.questionType == 'fill'>
        <div class="testpaper-question-stem">${bbCode2Html(fill_question_stem_html(item.question.stem))}</div>
    <#else>
        <div class="testpaper-question-stem">${bbCode2Html(item.question.stem)}</div>
    </#if>
</div>