<div class="media testpaper-result">
    <div class="testpaper-result-total">
        <div class="well">
            <div class="testpaper-result-total-score"><#if paperResult.status == 'finished'>${paperResult.score}<#else>?</#if>
                <small>分</small></div>
            <small class="text-muted">总分 ${paper.score} 分</small>
        </div>
    </div>
    <div class="media-body">
        <div class="table-responsive">
            <table class="table table-bordered table-condensed testpaper-result-table">
                <thead>
                <th></th>
                <#list paper.metas.question_type_seq! as type>
                    <th>${dict['questionType'][type]} <small class="text-muted">(${accuracy[type].all}道)</small></th>
                </#list>
                </thead>
                <tbody>
                <tr>
                    <th>答对</th>
                    <#list paper.metas.question_type_seq! as type>
                        <#if type == 'essay'>
                            <#if paperResult.status == 'finished'>
                                <td><span class="text-success">${accuracy[type].right} <small>道</small></span></td>
                            <#else>
                                <td rowspan="4" style="vertical-align:middle"><span class="text-success"
                                                                                    style="font-size:40px">?</span></td>
                            </#if>
                        <#else>
                            <#if paperResult.status == 'finished' || !(accuracy[type].hasEssay?? && accuracy[type].hasEssay)>
                                <td><span class="text-success">${accuracy[type].right} <small>道</small></span></td>
                            <#else>
                                <td rowspan="4" style="vertical-align:middle"><span class="text-success"
                                                                                    style="font-size:40px">?</span></td>
                            </#if>
                        </#if>
                    </#list>
                </tr>
                <tr>
                    <th>答错</th>
                    <#list paper.metas.question_type_seq! as type>
                        <#if type == 'essay'>
                            <#if paperResult.status == 'finished'>
                                <td><span class="text-danger">${accuracy[type].wrong + accuracy[type].partRight} <small>道</small></span>
                                </td>
                            </#if>
                        <#else>
                            <#if paperResult.status == 'finished' || !(accuracy[type].hasEssay?? && accuracy[type].hasEssay)>
                                <td><span class="text-danger">${accuracy[type].wrong + accuracy[type].partRight} <small>道<#if type == 'choice' && accuracy[type].partRight != 0 || type == 'uncertain_choice' && accuracy[type].partRight != 0>（其中有${accuracy[type].partRight}道漏选）</#if></small></span>
                                </td>
                            </#if>
                        </#if>
                    </#list>
                </tr>
                <tr>
                    <th>未答</th>
                    <#list paper.metas.question_type_seq! as type>
                        <#if type == 'essay'>
                            <#if paperResult.status == 'finished'>
                                <td><span class="text-muted">${accuracy[type].noAnswer} <small>道</small></span></td>
                            </#if>
                        <#else>
                            <#if paperResult.status == 'finished' || !(accuracy[type].hasEssay?? && accuracy[type].hasEssay)>
                                <td><span class="text-muted">${accuracy[type].noAnswer} <small>道</small></span></td>
                            </#if>
                        </#if>
                    </#list>
                </tr>
                <tr>
                    <th>得分</th>
                    <#list paper.metas.question_type_seq! as type>
                        <#if type == 'essay'>
                            <#if paperResult.status == 'finished'>
                                <td><span class="text-score">${accuracy[type].score} <small>分</small></span></td>
                            </#if>
                        <#else>
                            <#if paperResult.status == 'finished' || !(accuracy[type].hasEssay?? && accuracy[type].hasEssay)>
                                <td><span class="text-score">${accuracy[type].score} <small>分</small></span></td>
                            </#if>
                        </#if>
                    </#list>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
</div>