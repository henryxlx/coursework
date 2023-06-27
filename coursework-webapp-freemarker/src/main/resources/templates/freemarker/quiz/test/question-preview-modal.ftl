<#assign modal_class = 'modal-lg'/>

<#include '/bootstrap-modal-layout.ftl' />

<#macro blockTitle>题目预览</#macro>

<#macro blockBody>

    <#include '/quiz/test/do-test-${type}.ftl'/>

</#macro>

<#macro blockFooter>
    <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
    <script>app.load('quiz-question/preview');</script>
</#macro>