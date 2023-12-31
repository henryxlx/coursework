<#assign modal_class = 'modal-lg'/>

<#include '/bootstrap-modal-layout.ftl' />

<#macro blockTitle>
    <#if targetType == 'courselesson'> 上传课时文件
    <#elseif targetType == 'coursematerial'> 上传资料文件
    <#elseif targetType == 'materiallib'> 上传文件到教学资料库
    </#if>
</#macro>

<#macro blockBody>

    <style>
        .plupload_filelist_footer {
            height: 40px;
            line-height: 20px;
        }


    </style>

    <div id="file-uploader-container"
         class="mbl"
         data-target-type="${targetType}"
         data-upload-mode="${storageSetting.upload_mode!}"
         data-hls-encrypted="${setting('developer.hls_encrypted', '1')}"

    >

        <div
                id="file-chooser-uploader-div"
                data-role="uploader-div"
                data-upload-url="${ctx}/uploadfile/upload?targetType=${targetType}&targetId=${targetId}"
                data-params-url="${ctx}/uploadfile/params?storage=${storageSetting.upload_mode!}&targetType=${targetType}&targetId=${targetId}"
                <#if storageSetting.upload_mode?? && storageSetting.upload_mode == 'cloud'>
                    data-callback="{{ path('uploadfile_cloud_callback', {targetType:targetType, targetId:targetId, lazyConvert:1}) }}"
                </#if>
        ></div>

    </div>

    <#if targetType == 'courselesson' ||  targetType == 'materiallib' >
        <div class="alert alert-info">
            <ul>
                <#if storageSetting.upload_mode?? && storageSetting.upload_mode == 'cloud'>
                    <li>支持<strong>mp3, mp4, avi, flv, wmv, mov, ppt, pptx, pdf, doc, docx,
                            swf</strong>格式的文件上传，文件大小不能超过<strong>1 GB</strong>。
                    </li>
                    <li>视频将上传到<strong>云服务器</strong>，上传之后会对视频进行格式转换，转换过程需要一定的时间，在这个过程中视频将无法播放。</li>
                <#else>
                    <li>支持<strong>mp4, mp3</strong>格式的文件上传，且文件大小不能超过<strong>${upload_max_filesize()}</strong>。<br>MP4文件的视频编码格式，请使用AVC(H264)编码，否则浏览器无法播放。
                    </li>
                    <li>视频将上传到<strong>网站服务器</strong>，如需使用云视频，请联系CourseWork。</li>
                </#if>
            </ul>
        </div>

        <#if storageSetting.upload_mode?? && storageSetting.upload_mode == 'cloud'>
            <div class="quality-switcher">
                <div class="quality-switcher-bar">
                    视频转码类型<span class="text-muted quality-switcher-name"></span> <a href="javascript:;"
                                                                                    class="edit-btn text-info"><span
                                class="glyphicon glyphicon-cog"></span> 设置</a>
                </div>
                <div class="quality-switcher-control">
                    <div class="video-quality radios">
                        画质：
                        <@radios 'video_quality', {'low':'流畅 <span class="text-muted">(适合PPT讲解)</span>', 'normal':'标准 <span class="text-muted">(适合屏幕录制、摄像头拍摄)</span>', 'high': '精细 <span class="text-muted">(适合动态电影)</span>'}, storageSetting.video_quality!'low'/>
                    </div>

                    <div class="audio-quality radios">
                        音质：
                        <@radios 'video_audio_quality', {'low':'流畅', 'normal':'标准', 'high': '高品'}, storageSetting.video_audio_quality!'low' />
                    </div>

                    <div class="quality-actions">
                        <a href="javascript:;" class="btn btn-default btn-sm confrim-btn">确定</a>
                        <a href="javascript:;" class="btn btn-link btn-sm cancel-btn">取消</a>
                    </div>
                </div>
            </div>
        </#if>
    <#elseif targetType == 'coursematerial'>
        <div class="alert alert-info">
            <ul>
                <li>
                    支持常见文档、图片、音视频、压缩包文件格式。<#if storageSetting.upload_mode?? && storageSetting.upload_mode == 'local'>且文件大小不能超过
                    <strong>${upload_max_filesize()}</strong>。</#if></li>
                <#if storageSetting.upload_mode?? && storageSetting.upload_mode == 'cloud'>
                    <li>文件将上传到<strong>云服务器</strong>。</li>
                <#else>
                    <li>文件将上传到<strong>网站服务器</strong>，如需使用云视频，请联系CourseWork。</li>
                </#if>
            </ul>
        </div>
    </#if>

    <script>app.load('course-manage-file/upload-course-files')</script>

</#macro>

<#macro blockFooter>
    <button type="button" class="btn btn-primary" data-dismiss="modal">关闭</button>
</#macro>