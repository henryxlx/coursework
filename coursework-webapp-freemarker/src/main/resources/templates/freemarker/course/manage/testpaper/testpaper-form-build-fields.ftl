<div class="form-group">
    <div class="col-md-2 control-label"><label for="testpaper-percentage-field">生成方式</label></div>
    <div class="col-md-8 controls radios">
        <@radios 'mode', {'rand':'随机生成', 'difficulty':'按难易程度'}, 'rand'/>
    </div>
</div>

<div class="form-group difficulty-form-group hidden">
    <div class="col-md-2 control-label"><label for="testpaper-percentage-field">试卷难度</label></div>
    <div class="col-md-8 controls form-control-static">
        <div class="difficulty-percentage-slider"></div>
        <div class="help-block">
            <span class="simple-percentage-text"></span>
            <span class="normal-percentage-text"></span>
            <span class="difficulty-percentage-text"></span>
            <br><span class="text-info">如果某个难度的题目数不够，将会随机选择题目来补充。</span>
        </div>
    </div>
</div>

<div class="form-group">
    <div class="col-md-2 control-label"><label for="testpaper-range-field">出题范围</label></div>
    <div class="col-md-8 controls radios">
        <@radios 'range', {'course':'整个课程', 'lesson':'按课时范围'}, 'course'/>
        <input type="hidden" name="ranges" value="">
        <div id="testpaper-range-selects" style="display:none; margin-top:8px;">
            <select class="form-control width-input width-input-large" id="testpaper-range-start">
                <@select_options ranges />
            </select>
            <span class="text-muted mrs">到</span>
            <select class="form-control width-input width-input-large" id="testpaper-range-end">
                <@select_options ranges />
            </select>
        </div>
    </div>
</div>

<div class="form-group">
    <style>.testpaper-question-option-item {
            margin-left: -12px;
            margin-bottom: 5px;
        }</style>
    <div class="col-md-2 control-label"><label>题目设置</label></div>
    <div class="col-md-10 controls" id="testpaper-question-options">
        <#list types as type>
            <div class="testpaper-question-option-item">
                <button type="button" class="btn btn-link testpaper-question-option-item-sort-handler"><span
                            class="glyphicon glyphicon-move"></span></button>
                <span style="min-width:85px;display:inline-block;_display:inline;">${type.name}</span>

                <span class="mlm">题目数量:</span>
                <input type="text" class="form-control width-input width-input-mini input-sm item-number"
                       name="counts[${type.key}]" value="0"/>/
                <span class="text-info question-num" role="questionNum"
                      type="${type.key}">${(questionNums[type.key]["questionNum"])!0}</span>

                <span class="mlm">题目分值:</span>
                <input type="text" class="form-control width-input width-input-mini input-sm item-score"
                       name="scores[${type.key}]" value="2"/>

                <#if type.hasMissScore>
                    <span class="mlm">漏选分值:</span>
                    <input type="text" class="form-control width-input width-input-mini input-sm item-miss-score"
                           name="missScores[${type.key}]" value="0"/>
                </#if>
            </div>

        </#list>
    </div>
</div>

<input type="hidden" name="percentages[simple]">
<input type="hidden" name="percentages[normal]">
<input type="hidden" name="percentages[difficulty]">

<input type="hidden" name="_csrf_token" value="${csrf_token('site')}">