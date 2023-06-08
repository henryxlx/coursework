<#assign script_controller = 'course/show'/>
<#assign metaKeywords>${(category.name)!}<#list tags as tag>${tag.name}</#list>${course.title}${setting('site.name')}</#assign>
<#assign metaDescription = course.about!''?substring(0, 150) />

<#include '/layout.ftl'>

<#macro blockTitle>课程存档 - ${blockTitleParent}</#macro>

<#macro blockContent>

  <#if member?? && member.locked?? && member.locked == 1>
    <div class="row">
      <div class="col-md-12">
        <div class="mtl alert alert-warning">
          您的退款申请已提交，请等待管理员的处理，退款期间将不能学习课程。
          <button class="btn btn-warning btn-sm cancel-refund"
                  data-url="${ctx}/target/${course.id}/order/cancel_refund?targetType=course">取消退款，继续学习！
          </button>
        </div>
      </div>
    </div>
  </#if>

  <#include '/course/course-detail.ftl' />

</#macro>

<#macro blockBottom>
  <div id="course-modal" class="modal"></div>
  <div id="course-edit-modal" class="modal"></div>
</#macro>

