<#assign side_nav = 'base'/>
<#assign script_controller = 'course-manage/base'/>
<#assign script_arguments = "{'categoryUrl': '/category/all','tagMatchUrl': '/tag/match_jsonp', 'locationUrl': '/location/all'}"/>
<#include '/course/manage/layout.ftl'/>

<#macro blockTitle>基本信息 - 课程管理 - ${blockTitleParent}</#macro>

<#macro  blockMain>

<div class="panel panel-default panel-col">
    <div class="panel-heading">基本信息</div>
    <div class="panel-body">
        <form class="form-horizontal" id="course-form" method="post">

            <@web_macro.flash_messages/>

            <div class="form-group">
                <label class="col-md-2 control-label">标题</label>
                <div class="col-md-8 controls">
                    <input type="text" id="course_title" name="title" required="required" class="form-control" value="${course.title!''}">
                    <div class="help-block" style="display:none;"></div>
                </div>
            </div>

            <div class="form-group">
                <label class="col-md-2 control-label">副标题</label>
                <div class="col-md-8 controls">
                    <textarea id="course_subtitle" name="subtitle" required="required" class="form-control">${course.subtitle!''}</textarea>
                    <div class="help-block" style="display:none;"></div>
                </div>
            </div>

            <div class="form-group">
                <label class="col-md-2 control-label">标签</label>
                <div class="col-md-8 controls">
                    <input type="text" id="course_tags" name="tags" required="required" class="width-full select2-offscreen" tabindex="-1" value="${tags?join(', ')}" data-explain="将会应用于按标签搜索课程、相关课程的提取等">
                    <div class="help-block">将会应用于按标签搜索课程、相关课程的提取等</div>
                </div>
            </div>

            <#if course.type?? && course.type == 'live'>

            <div class="form-group">
                <label class="col-md-2 control-label" for="maxStudentNum-field">直播最大${setting('default.user_name')!'学员'}数</label>
                <div class="col-md-8 controls">
                    <input type="text" id="maxStudentNum-field" name="maxStudentNum" class="form-control width-input width-input-large" value="${course.maxStudentNum!20}" data-live-capacity="${liveCapacity!}"> 人
                    <div class="help-block" style="display:none;"></div>
                </div>
            </div>

            </#if>

            <div class="form-group">
                <label class="col-md-2 control-label" for="course_expiryDay">${setting('default.user_name')!'学员'}有效期</label>
                <div class="col-md-8 controls">
                    <input type="text" id="course_expiryDay" name="expiryDay" required="required" class="form-control width-input width-input-large" value="${course.expiryDay!}" > 天
                    <div class="help-block">设置该值后，${setting('default.user_name')!'学员'}只能在有效期内进行课程的相关操作，系统默认会在到期前10天提醒${setting('default.user_name')!'学员'}。该值为0，则不做此限制。</div>
                </div>
            </div>

            <#if course.type?? && course.type == 'normal'>
            <div class="form-group">
                <label class="col-md-2 control-label">连载状态</label>
                <div class="col-md-8 controls radios">
                    <@radios 'serializeMode' {'none':'非连载课程', 'serialize':'更新中', 'finished':'已完结'} course.serializeMode!'none' />
                </div>
            </div>
            </#if>

            <div class="form-group">
                <label class="col-md-2 control-label">分类</label>
                <div class="col-md-8 controls">
                    <select id="course_categoryId" name="categoryId" required="required" class="form-control width-input width-input-large">
                        <@select_options categoryForCourse!{} course.categoryId + '' '分类' />
                    </select>
                    <div class="help-block" style="display:none;"></div>
                </div>
            </div>
            <#if course.type?? && course.type == 'live'>
            <div class="form-group">
                <div class="col-md-offset-2 col-md-8 controls">
                    <a href="http://www.edusoho.com/files/liveplugin/live_desktop_${liveProvider!}.rar" target="_blank">直播“桌面分享”插件下载</a>
                </div>
            </div>
            </#if>
            <div class="form-group">
                <div class="col-md-offset-2 col-md-8 controls">
                    <button class="btn btn-fat btn-primary" id="course-create-btn" type="submit">保存</button>
                </div>
            </div>

            <input type="hidden" name="_csrf_token" value="{{ csrf_token('site') }}">

        </form>
    </div>
</div>

</#macro>



