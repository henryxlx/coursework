<div class="course-nav-tabs">
    <ul class="nav nav-pills mbl" id="course-nav-tabs">
        <li class="active">
            <a href="#course-about-pane" class="btn-index" data-anchor="#course-about-pane">课程介绍</a>
        </li>
        <#if course.goals??>
            <li>
                <a href="#course-goal-pane" class="btn-index" data-anchor="#course-goal-pane">课程目标</a>
            </li>
        </#if>
        <#if course.audiences??>
            <li>
                <a href="#course-audience-pane" class="btn-index" data-anchor="#course-audience-pane">适合人群</a>
            </li>
        </#if>
        <li>
            <a href="#course-list-pane" class="btn-index" data-anchor="#course-list-pane">课程列表</a>
        </li>
        <li>
            <a href="#course-review-pane" class="btn-index" data-anchor="#course-review-pane">
                课程评价
                <span class="badge">${course.ratingNum}</span>
            </a>
        </li>
    </ul>
</div>

<#assign panelId = 'course-about-pane'/>
<#assign panelHeading>
    <h3 class="panel-title">课程介绍</h3>
</#assign>
<#assign panelBody>

    <#if course.about??>
        ${course.about}
    <#else>
        <span class="text-muted">还没有课程介绍...</span>
    </#if>
    <#if tags??>
        <div class="mtm">
            <span class="text-muted">标签：</span>
            <#list tags as tag>
                <a href="${ctx}/tag/${tag.name}" class="mrs">${tag.name}</a>
            </#list>
        </div>
    </#if>
</#assign>
<#include '/bootstrap/panel.embed.ftl'/>

<#if course.goals??>
    <#assign panelId = 'course-goal-pane'/>
    <#assign panelHeading>
        <h3 class="panel-title">课程目标</h3>
    </#assign>
    <#assign panelBody>
        <ul class="media-list">
            <#list course.goals! as goal>
                <li class="media">
                    <div class="pull-left">
                        <span class="glyphicon glyphicon-flag media-object"></span>
                    </div>
                    <div class="media-body">${goal}</div>
                </li>
            </#list>
        </ul>
    </#assign>
    <#include '/bootstrap/panel.embed.ftl'/>
</#if>

<#if course.audiences??>
    <#assign panelId = 'course-audience-pane' />
    <#assign panelHeading>
        <h3 class="panel-title">适合人群</h3>
    </#assign>
    <#assign panelBody>
        <ul class="media-list">
            <#list course.audiences! as audience>
                <li class="media">
                    <div class="pull-left">
                        <span class="glyphicon glyphicon-user media-object"></span>
                    </div>
                    <div class="media-body">${audience}</div>
                </li>
            </#list>
        </ul>
    </#assign>
    <#include '/bootstrap/panel.embed.ftl'/>
</#if>

<#assign panelId = 'course-list-pane'/>
<#assign panelHeading>
    <h3 class="panel-title">课时列表</h3>
</#assign>
<#assign panelBody>

    <#assign experience = true />
    <#include '/course/lesson/item-list-multi.ftl' />

</#assign>
<#include '/bootstrap/panel.embed.ftl'/>

<#assign panelId = 'course-review-pane'/>
<#assign panelHeading>
    <h3 class="panel-title">课程评价</h3>
</#assign>
<#assign panelBody>

    <div id="course-review-pane-show" data-url="${ctx}/course/${course.id}/review/list?previewAs=previewAs">
        正在载入课程评价数据...
    </div>

</#assign>
<#include '/bootstrap/panel.embed.ftl'/>