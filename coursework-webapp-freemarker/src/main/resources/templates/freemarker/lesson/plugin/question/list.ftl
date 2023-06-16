<ul class="media-list thread-list-small" data-role="list">
    <#list threads! as thread>
        <#assign user = users['' + thread.userId] />
        <#include '/lesson/plugin/question/item.ftl' />
    <#else>
        <li class="empty-item">此课时还没有问题</li>
    </#list>
</ul>