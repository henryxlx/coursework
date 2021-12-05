<tr id="{{ block.id }}">
    <td>
        {{ block.title }}
        <br>
        <span class="text-muted text-sm">编码：{{ block.code }}</span>
    </td>
    <td>
        {{ admin_macro.user_link(latestUpdateUser|default(null)) }}
        <br>
        <span class="text-muted text-sm">{{ block.updateTime|date('Y-n-d H:i:s') }}</span>
    </td>
    <td>
        <button class="btn btn-sm btn-primary update-btn" data-url="{{ path('admin_block_update', {block:block.id}) }}" data-toggle="modal" data-target="#modal">编辑内容</button>
        {% if setting('developer.debug') %}
        <button class="btn btn-sm btn-default edit-btn" data-url="{{ path('admin_block_edit', {block:block.id}) }}" data-toggle="modal" data-target="#modal" >设置</button>
        <button class="btn btn-sm btn-default delete-btn" data-url="{{ path('admin_block_delete', {id:block.id}) }}" data-target="{{ block.id }}">删除</button>
        {% endif %}
    </td>
</tr>