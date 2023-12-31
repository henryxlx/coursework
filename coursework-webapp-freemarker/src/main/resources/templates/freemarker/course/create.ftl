<#assign script_controller = 'course/create' />

<@block_title "创建${(type == 'normal')?then('课程', '直播课程')}"/>

<#include '/layout.ftl'/>

<#macro blockContent>
    <div class="row">
        <div class="col-md-offset-2 col-md-8">
            <div class="panel panel-default panel-page">
                <div class="panel-heading"><h2>创建<#if type == 'normal'>课程<#else>直播课程</#if></h2></div>

                <#if !appUser.largeAvatar?? || !appUser.title?? || !userProfile.aboutme??>
            <div class="alert alert-info">
                <h5><strong>请先完成以下设置，才能创建课程：</strong></h5>
                <ol>
                    <li>设置头像。
                    <#if appUser.largeAvatar?? && appUser.largeAvatar != ''>
                        <span class="text-success"><span class="glyphicon glyphicon-ok-circle"></span> 已完成</span></li>
                    <#else>
                        <span class="text-danger"><span class="glyphicon glyphicon-remove-circle"></span> 未完成</span>，<a
                            href="${ctx}/settings/avatar?fromCourse=true" class="alert-link">去设置</a>
                    </#if>
                    <li>设置头衔 、自我介绍。
                    <#if appUser.title?? && userProfile.aboutme??>
                        <span class="text-success"><span class="glyphicon glyphicon-ok-circle"></span> 已完成</span> </li>
                    <#else>
                        <span class="text-danger"><span class="glyphicon glyphicon-remove-circle"></span> 未完成</span>，<a href="${ctx}/settings?fromCourse=true" class="alert-link">去设置</a>
                    </#if>
                    </li>
                </ol>
            </div>
            <#else>
            <form id="course-create-form" class="form-horizontal" method="post">

                <@web_macro.flash_messages />

                <div class="form-group">
                    <div class="col-md-2 control-label"><label for="course_title" class="required">标题</label></div>
                    <div class="col-md-8 controls">
                        <input type="text" id="course_title" name="title" required="required" class="form-control" data-widget-cid="widget-1" data-explain="">
                        <div class="help-block" style="display:none;"></div>
                    </div>
                </div>

                <div class="form-group">
                    <div class="col-md-offset-2 col-md-8 controls">
                        <input id="course-create-btn" data-submiting-text="正在创建" class="btn btn-fat btn-primary" type="submit" value="创建">
                    </div>
                </div>

                <#if !setting('copyright.owned')??>
                <div class="form-group">
                    <div class="col-md-offset-2 col-md-8 controls">
                        <p class="mtl"><a href="http://www.qiqiuyu.com/course/22" class="text-muted" target="_blank">如何创建课程？</a></p>
                    </div>
                </div>
                </#if>

                <input type="hidden" name="_csrf_token" value="${csrf_token('site')}">
                <input type="hidden" name="type" value="${type}">

            </form>

            </#if>
        </div>

    </div>

</div>
</#macro>