<tr id="tag-tr-{{ tag.id }}">
    <td>{{ tag.id }}</td>
    <td>{{ tag.name }}</td>
    <td>{{ tag.createdTime|date('Y-m-d H:i') }}</td>
    <td>
        <button class="btn btn-default btn-sm" data-url="{{ path('admin_tag_update', {id:tag.id}) }}" data-toggle="modal" data-target="#modal">编辑</button>
    </td>
</tr>