<li class="item-chapter <#if chapter.type == 'unit' >item-chapter-unit</#if> clearfix" id="chapter-${chapter.id}"
    style="word-break: break-all;">
    <div class="item-content">第 <span
                class="number">${chapter.number}</span> <#if chapter.type == 'unit'>${setting('default.part_name')!'节'}<#else>${setting('default.chapter_name')!'章'}</#if>
        ： ${chapter.title}</div>
    <div class="item-actions prs">
        <div class="btn-group">
            <button class="btn btn-link dropdown-toggle" title="添加" data-toggle="dropdown"><i
                        class="glyphicon glyphicon-plus-sign"></i></button>
            <#if chapter.type == 'unit'>
                <ul class="dropdown-menu" role="menu">
                    <li>
                        <a href="#" id="chapter-create-btn" data-toggle="modal" data-target="#modal"
                           data-backdrop="static" data-keyboard="false"
                           data-url="<#if course.type == 'normal'>${ctx}/course/${course.id}/manage/lesson/create?parentId=chapter-${chapter.id}<#else>${ctx}/course/{course.id}/manage/live/lesson/create?parentId=chapter-${chapter.id}</#if>"><i
                                    class="glyphicon glyphicon-plus"></i> <#if course.type == 'normal'> 添加 课时 <#else> 添加 直播课时 </#if>
                        </a>
                    </li>
                    <#if course.type == 'normal'>
                        <li>
                            <a href="#" id="chapter-create-btn" data-toggle="modal" data-target="#modal"
                               data-backdrop="static" data-keyboard="false"
                               data-url="${ctx}/course/${course.id}/manage/lesson/create/testpaper?parentId=chapter-${chapter.id}"><i
                                        class="glyphicon glyphicon-plus"></i> 添加 试卷 </a>
                        </li>
                    </#if>
                </ul>
            <#else>
                <ul class="dropdown-menu" role="menu">
                    <li>
                        <a href="#" id="chapter-create-btn" data-toggle="modal" data-target="#modal"
                           data-backdrop="static" data-keyboard="false"
                           data-url="${ctx}/course/${course.id}/manage/chapter/create?type=unit&parentId=chapter-${chapter.id}"><i
                                    class="glyphicon glyphicon-plus"></i> 添加 ${setting('default.part_name')!'节'} </a>
                    </li>
                    <li>
                        <a href="#" id="chapter-create-btn" data-toggle="modal" data-target="#modal"
                           data-backdrop="static" data-keyboard="false"
                           data-url="<#if course.type == 'normal'>${ctx}/course/${course.id}/manage/lesson/create?parentId=chapter-${chapter.id}<#else>${ctx}/course/${course.id}/manage/live/lesson/create?parentId=chapter-${chapter.id}</#if>"><i
                                    class="glyphicon glyphicon-plus"></i> <#if course.type == 'normal'> 添加 课时 <#else> 添加 直播课时 </#if>
                        </a>
                    </li>
                    <#if course.type == 'normal'>
                        <li>
                            <a href="#" id="chapter-create-btn" data-toggle="modal" data-target="#modal"
                               data-backdrop="static" data-keyboard="false"
                               data-url="${ctx}/course/${course.id}/manage/lesson/create/testpaper?parentId=chapter-${chapter.id}"><i
                                        class="glyphicon glyphicon-plus"></i> 添加 试卷 </a>
                        </li>
                    </#if>
                </ul>
            </#if>
        </div>

        <button class="btn btn-link" title="编辑" data-toggle="modal" data-target="#modal" data-keyboard="false"
                data-url="${ctx}/course/${course.id}/manage/chapter/${chapter.id}/edit"><i
                    class="glyphicon glyphicon-edit"></i></button>

        <button class="btn btn-link delete-chapter-btn" title="删除"
                data-url="${ctx}/course/${course.id}/manage/chapter/${chapter.id}/delete"
                data-chapter="${(default.chapter_name)!'章'}" data-part="${(default.part_name)!'节'}"><i
                    class="glyphicon glyphicon-trash"></i></button>

    </div>
</li>