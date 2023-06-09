<#include '/bootstrap-modal-layout.ftl' />

<#macro blockTitle>评价课程</#macro>

<#macro blockBody>
    <form id="review-form" method="post" action="${ctx}/course/${course.id}/review/create">
        <@web_macro.flash_messages/>

        <div class="form-group clearfix">
            <a id="list_reviews" class="btn btn-sm btn-link pull-right"
               data-url="${ctx}/course/${course.id}/review/list?isModal=true">查看全部评价</a>
            <div class="controls">
                请打分： <span id="my-course-rate" data-rating="${(review.rating)!0}"
                           data-url="${ctx}/course/${course.id}/review/create" data-img-path="${ctx}/assets/img/raty"
                           class="mrm"></span>
                <input type="hidden" id="review_rating" name="review[rating]" value="">
            </div>


        </div>

        <div class="form-group">
            <div class="controls">
        <textarea id="content" name="content"
                  required="required" class="form-control" rows="8"
                  placeholder="评价详细内容" data-display="评价详细内容"></textarea>
            </div>
        </div>

        <input type="hidden" name="_csrf_token" value="${csrf_token('site')}">
    </form>
</#macro>

<#macro blockFooter>
    <button class="btn btn-link" data-dismiss="modal">取消</button>
    <button class="btn btn-primary" type="submit" data-toggle="form-submit" data-target="#review-form">保存</button>
    <script>
        app.load('course/review-modal');
    </script>
</#macro>