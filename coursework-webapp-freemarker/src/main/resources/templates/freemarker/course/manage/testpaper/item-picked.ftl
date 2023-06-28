<table class="picked-items">
    <#include '/course/manage/testpaper/item-tr.ftl' />
    <#list subQuestions! as question>
        <#include '/course/manage/testpaper/item-tr.ftl' />
    </#list>
</table>