<tbody data-update-seqs-url="${ctx}/admin/navigation/seqs-update">
<#if navigations??>
{% for navigation in navigations %}
<tr class="{% if navigation.parentId == 0 %} has-subItems {% else %} child {% endif %}" id="navigations-tr-{{ navigation.id }}" data-id="{{ navigation.id }}" data-parent-id="{{ navigation.parentId }}">
    <td style="vertical-align: middle;">
        {% if navigation.parentId == 0 %}
        <span class="glyphicon glyphicon-resize-vertical"></span>
        {% endif %}
        {% if navigation.parentId > 0 %}<span class="indentation">&nbsp;&nbsp;&nbsp;&nbsp; └─</span>{% endif %}
        <a href="{{navigation.url|navigation_url}}" target="_blank"> {{navigation.name}} </a>
    </td>
    <td>
        {% if navigation.isNewWin == 0 %}否{% else %}是{% endif %}
    </td>
    <td>
        {% if navigation.isOpen == 1 %}开启{% else %}关闭{% endif %}
    </td>
    <td>
        {% if navigation.type == 'top' and navigation.parentId == 0 %}
        <button class="btn btn-sm btn-default edit-btn" data-url="{{ path('admin_navigation_create', {type: navigation.type, parentId: navigation.id}) }}" data-toggle="modal" data-target="#modal">添加二级导航</button>
        {% endif %}
        <button class="btn btn-sm btn-default edit-btn" data-url="{{ path('admin_navigation_update', {id:navigation.id}) }}" data-toggle="modal" data-target="#modal">编辑</button>
        <button class="btn btn-sm btn-default delete-btn" data-url="{{ path('admin_navigation_delete', {id:navigation.id}) }}" data-target="{{ navigation.id }}">删除</button>
    </td>
</tr>
<#else>
<tr><td colspan="20"><div class="empty">暂无导航记录</div></td></tr>
</#if>

</tbody>