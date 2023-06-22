<#assign menu = 'setting'/>
<#assign script_controller = 'classroom/set'/>
<#include '/admin/classroom/layout.ftl'/>

<#macro blockMain>

    <div class="page-header clearfix">
        <h1 class="pull-left">班级设置</h1>
    </div>

    <@web_macro.flash_messages />

    <form class="form-horizontal" method="post" id="member-zone-form" novalidate>

        <div class="row form-group">
            <div class="col-md-2 control-label">
                <label>班级称谓设置</label>
            </div>
            <div class="controls col-md-4">
                <input name="name" type="text" class="form-control" value="${setting('classroom.name')!'班级'}"/>
                <div class="help-block">用户所能看到的是这里设置的称谓</div>
            </div>
        </div>


        <div class="row form-group">
            <div class="col-md-2 control-label">
                <label>班级列表页默认排序</label>
            </div>
            <div class="controls col-md-7 radios">
                <@radios 'explore_default_orderBy', {'createdTime':'最新', 'studentNum':'最热', 'recommendedSeq':'推荐'},  (classroomSetting.explore_default_orderBy)! />
            </div>
        </div>

        <div class="row form-group">
            <div class="col-md-2 control-label"></div>
            <div class="controls col-md-8">
                <button type="submit" class="btn btn-primary">提交</button>
            </div>
        </div>
        <input type="hidden" name="_csrf_token" value="${csrf_token('site')}">
    </form>

</#macro>