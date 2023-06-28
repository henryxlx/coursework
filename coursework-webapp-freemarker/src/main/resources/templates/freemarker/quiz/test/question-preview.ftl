<#include '/layout.ftl'/>

<#macro blockStylesheetsExtra>
    <style>
        body {
            background-color: #fff;
        }
    </style>
</#macro>

<#macro blockBody>
    <div style="padding:20px;">
        <#include '/quiz/test/do-test-${type}.ftl' />
    </div>
</#macro>