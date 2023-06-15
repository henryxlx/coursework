<#assign modal_class = 'modal-lg'/>

<#include '/bootstrap-modal-layout.ftl' />

<#macro blockTitle>
    <#if lesson??>编辑课时${lesson.number!}<#else>添加课时</#if>
</#macro>

<#macro blockBody>

    <form id="course-lesson-form" data-course-id="${course.id}" class="form-horizontal lesson-form" method="post"
          <#if parentId??>data-parentId="${parentId}" </#if>
          data-create-draft-url="${ctx}/course/draft/create"
            <#if lesson??>
                action="${ctx}/course/${course.id}/manage/lesson/${(lesson.id)!0}/edit" data-lesson-id="${(lesson.id)!0}"
            <#else>
                action="${ctx}/course/${course.id}/manage/lesson/create"
            </#if>
    >

        <div class="form-group" <#if lesson??>style="display:none;"</#if>>
            <div class="col-md-2 control-label"><label>类型</label></div>
            <div class="col-md-9 controls">
                <div class="radios">
                    <@radios 'type', dict['lessonType']!{}, (lesson.type)!'video' />
                </div>
            </div>
        </div>

        <div class="form-group for-text-type for-video-type for-audio-type <#if storageSetting.upload_mode == 'cloud'>for-ppt-type for-document-type for-flash-type</#if>">
            <div class="col-md-2 control-label"><label for="lesson-title-field">标题</label></div>
            <div class="col-md-9 controls">
                <div class="row">
                    <div class="col-md-10">
                        <input id="lesson-title-field" class="form-control" type="text" name="title"
                               value="${(lesson.title)!}">
                    </div>
                    <div class="col-md-2">
                        <div class="checkbox">
                            <label><input type="checkbox" name="free"
                                          value="1" <#if (lesson.free)??> checked="checked" </#if>> 免费课时</label>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <div class="form-group for-text-type for-video-type for-audio-type <#if storageSetting.upload_mode == 'cloud'>for-ppt-type for-document-type for-flash-type</#if>">
            <div class="col-md-2 control-label"><label for="lesson-summary-field">摘要</label></div>
            <div class="col-md-9 controls"><textarea class="form-control" id="lesson-summary-field"
                                                     name="summary">${(lesson.summary)!}</textarea>

            </div>
        </div>

        <div class="form-group for-text-type">


            <div class="col-md-2 control-label"><label for="lesson-content-field" class="style1">内容</label></div>
            <div class="col-md-9 controls">

                <#if draft??>
                    <a id="see-draft-btn" class="btn btn-link" data-url="${ctx}/course/draft/view">
                        <small>
                            您有一段自动保存内容，继续编辑请点击
                        </small>
                    </a>
                </#if>

                <textarea class="form-control" id="lesson-content-field" name="content"
                          data-image-upload-url="${ctx}/editor/upload?token=upload_token('course')"
                          data-flash-upload-url="${ctx}/editor/upload?token=upload_token('course', 'flash')"
                >${(lesson.content)!''}</textarea>
            </div>
        </div>

        <div class="form-group for-video-type for-audio-type <#if storageSetting.upload_mode == 'cloud'>for-ppt-type for-document-type for-flash-type</#if> ">
            <div class="col-md-2 control-label for-video-type"><label>视频</label></div>
            <div class="col-md-2 control-label for-audio-type"><label>音频</label></div>
            <div class="col-md-2 control-label for-ppt-type"><label>PPT</label></div>
            <div class="col-md-2 control-label for-document-type"><label>文档</label></div>
            <div class="col-md-2 control-label for-flash-type"><label>Flash</label></div>
            <div class="col-md-9 controls">
                <#include '/course/manage/lesson/media-choose.ftl' />
                <#assign mediaJsonText = ''/>
                <#if (lesson.media)??>
                    <#assign  mediaJsonText = json_encode(lesson.media) />
                </#if>
                <input id="lesson-media-field" type="hidden" name="media" value='${mediaJsonText}'>
            </div>
        </div>

        <div class="form-group for-none-type for-ppt-type">
            <div class="col-md-offset-2 col-md-9">
                <div class="alert alert-info">
                    <p>PPT文档需要CourseWork云转换后才能在浏览器中显示，请先开启CourseWork云。</p>
                    <p>如尚未使用CourseWork云服务，请联系<a href="http://www.coursework.net/service" target="_blank"><strong>CourseWork</strong></a>开启。
                    </p>
                </div>
            </div>
        </div>

        <div class="form-group for-none-type for-document-type">
            <div class="col-md-offset-2 col-md-9">
                <div class="alert alert-info">
                    <p>该文档类型需要CourseWork云转换后才能在浏览器中显示，请先开启CourseWork云。</p>
                    <p>如尚未使用CourseWork云服务，请联系<a href="http://www.coursework.net/service" target="_blank"><strong>CourseWork</strong></a>开启。
                    </p>
                </div>
            </div>
        </div>

        <div class="form-group for-none-type for-flash-type">
            <div class="col-md-offset-2 col-md-9">
                <div class="alert alert-info">
                    <p>Flash文件需要CourseWork云转换后才能在浏览器中显示，请先开启CourseWork云。</p>
                    <p>如尚未使用CourseWork云服务，请联系<a href="http://www.coursework.net/service" target="_blank"><strong>CourseWork</strong></a>开启。
                    </p>
                </div>
            </div>
        </div>

        <div class="form-group for-video-type for-audio-type" id="lesson-length-form-group">
            <div class="col-md-2 control-label for-video-type"><label>视频时长</label></div>
            <div class="hide"><label for="lesson-length-field">视频时长或</label></div>
            <div class="col-md-2 control-label for-audio-type"><label for="lesson-length-field">音频时长</label></div>
            <div class="col-md-9 controls">
                <input class="form-control width-input width-input-small" id="lesson-minute-field" type="text"
                       name="minute" value="${(lesson.minute)!}">分
                <input class="form-control width-input width-input-small" id="lesson-second-field" type="text"
                       name="second" value="${(lesson.second)!}">秒
                <div class="help-block">时长必须为整数。</div>
            </div>
        </div>

        <#if features?seq_contains('lesson_credit')>
            <div class="form-group for-text-type for-video-type for-audio-type for-ppt-type">
                <div class="col-md-2 control-label"><label for="lesson-give-credit-field">学分</label></div>
                <div class="col-md-9 controls">
                    <input class="form-control width-input width-input-small" id="lesson-give-credit-field" type="text"
                           name="giveCredit" value="${(lesson.giveCredit)!0}"> 分
                    <div class="help-block">学完此课时，可获得的学分</div>
                </div>
            </div>
        </#if>

        <input type="hidden" name="_csrf_token" value="${csrf_token('site')}">


    </form>


    <script>app.load('course-manage/lesson-modal')</script>


</#macro>

<#macro blockFooter>
    <button type="button" class="btn btn-link" data-dismiss="modal" id="cancel-btn">取消</button>
    <button id="course-lesson-btn" data-submiting-text="正在提交" type="submit" class="btn btn-primary"
            data-toggle="form-submit" data-target="#course-lesson-form"><#if lesson??>保存<#else>添加</#if></button>
</#macro>