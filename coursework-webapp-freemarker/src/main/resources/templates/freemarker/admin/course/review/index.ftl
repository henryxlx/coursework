<#assign menu = 'review'/>
<#assign script_controller = 'review/list'/>

<#include '/admin/course/layout.ftl'/>
<#macro blockTitle>课程评价管理 - ${blockTitleParent}</#macro>

<#macro blockMain>
    <div class="page-header clearfix">
        <h1>评价管理</h1>
    </div>

    <form id="review-search-form" class="form-inline well well-sm" action="" method="get" novalidate>

        <div class="form-group">
            <select class="form-control" name="rating">
                <@select_options {'1':'1星', '2':'2星', '3':'3星', '4':'4星', '5':'5星'}, RequestParameters['rating']!, '评分'/>
            </select>
        </div>

        <#--
                <div class="form-group">
                    <input class="form-control" type="text" placeholder="课程编号" name="courseId" value="${RequestParameters['courseId']! }">
                </div>
        -->

        <div class="form-group">
            <input class="form-control" type="text" placeholder="课程名" name="courseTitle" value="${RequestParameters['courseTitle']! }">
        </div>

        <div class="form-group">
            <input class="form-control" type="text" placeholder="作者" name="author" value="${RequestParameters['author']! }">
        </div>

        <div class="form-group">
            <input class="form-control" type="text" placeholder="评价内容关键词" name="content" value="${RequestParameters['content']! }">
        </div>

        <button class="btn btn-primary">搜索</button>
    </form>

    <div id="review-table-container">
        <table class="table table-striped table-hover" id="review-table">
            <thead>
            <tr>
                <th width="4%"><input type="checkbox"  data-role="batch-select"></th>
                <th width="60%">评价内容</th>
                <th width="8">评分</th>
                <th width="20%">作者</th>
                <th width="8%">操作</th>
            </tr>
            </thead>
            <tbody>
            <#list reviews! as review>
                <#assign author = users['' +review.userId]!{} />
                <#assign course = courses['' + review.courseId]!{} />
                <tr id="review-table-tr-${review.id}" data-role="item">
                    <td><input type="checkbox" value="${review.id}" data-role="batch-item"></td>
                    <td>
                        <div class="short-long-text">
                            <div class="short-text">
                                ${plain_text(review.content, 60)} <span class="text-muted trigger">(展开)</span>
                            </div>
                            <div class="long-text">${review.content!''?replace("\\r", "</br>")} <span
                                        class="text-muted trigger">(收起)</span></div>
                        </div>
                        <div class="mts">
                            <#if course??>
                                <a class="text-success text-sm" href="${ctx}/course/${review.courseId}"
                                   target="_blank">${courses['' + review.courseId].title}</a>
                            <#else>
                                <span class="text-muted text-sm">课程已删除</span>
                            </#if>
                        </div>
                    </td>
                    <td>${review.rating}星</td>
                    <td>
                        <@admin_macro.user_link author! /><br>
                        <span class="text-muted">${review.createdTime?number_to_datetime?string('yyyy-MM-dd HH:mm')}</span>
                    </td>
                    <td>
                        <button class="btn btn-default btn-sm" data-role="item-delete" data-name="评价"
                                data-url="${ctx}/admin/review/${review.id}/delete">删除
                        </button>
                    </td>
                </tr>
            <#else>
                <tr>
                    <td colspan="20">
                        <div class="empty">暂无评价记录</div>
                    </td>
                </tr>
            </#list>
            </tbody>
        </table>

        <div>
            <label class="checkbox-inline"><input type="checkbox" data-role="batch-select"> 全选</label>
            <button class="btn btn-default btn-sm mlm" data-url="${ctx}/admin/review/batch_delete" data-role="batch-delete" data-name="评价">删除</button>
        </div>
    </div>

    <@web_macro.paginator paginator!/>

</#macro>
