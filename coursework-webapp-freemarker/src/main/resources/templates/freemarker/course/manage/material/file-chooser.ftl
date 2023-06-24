<div class="file-chooser">
    <div class="file-chooser-bar" style="display:none;">
        <span data-role="placeholder"></span>
        <button class="btn btn-link btn-sm" type="button" data-role="trigger"><i class="glyphicon glyphicon-edit"></i>
            编辑
        </button>
    </div>

    <div class="file-chooser-main">
        <ul class="nav nav-pills nav-pills-mini mbs file-chooser-tabs">
            <li><a class="file-chooser-uploader-tab" href="#file-chooser-upload-pane" data-toggle="tab">上传文件</a></li>
            <#if setting('MaterialLib.enabled', '0') == '1'>
                <li><a href="#file-chooser-browser-pane" data-toggle="tab">从资料库中选择</a></li>
            </#if>
            <li><a href="#file-chooser-course-pane" data-toggle="tab">从课程文件中选择</a></li>
            <li><a class="file-chooser-link-tab" href="#file-chooser-link-pane" data-toggle="tab">网络链接</a></li>
        </ul>
        <div class="tab-content">
            <div class="tab-pane" id="file-chooser-upload-pane">

                <div class="file-chooser-uploader">
                    <div class="file-chooser-uploader-label">选择你要上传的文件：</div>
                    <div class="file-chooser-uploader-control">
                        <button
                                id="file-chooser-uploader-btn"
                                type="button"
                                class="btn btn-default btn-sm"
                                data-role="uploader-btn"
                                data-upload-url="${ctx}/uploadfile/upload?targetType=${targetType}&targetId=${targetId}"
                                data-params-url="${ctx}/uploadfile/params?targetType=${targetType}&targetId=${targetId}&storage=${storageSetting.upload_mode!}"
                                <#if storageSetting.upload_mode == 'cloud'>
                                    data-callback="${ctx}/uploadfile/cloud_callbackpath?targetType=${targetType}&targetId=${targetId}"
                                </#if>
                                <#if uploadSettings??>
                                    data-upload-settings="${json_encode(uploadSettings)}"
                                </#if>
                        >上传
                        </button>
                    </div>
                    <div class="progress" data-role="progress" style="display:none;">
                        <div class="progress-bar" role="progressbar" style="width: 0%;"></div>
                    </div>

                    <div class="alert alert-info">
                        <ul>
                            <li>支持常见文档、图片、音视频、压缩包文件格式。</li>
                            <#if storageSetting.upload_mode == 'cloud'>
                                <li>文件将上传到<strong>云服务器</strong></li>
                            <#else>
                                <li>文件将上传到<strong>网站服务器</strong>，如需使用云视频，请联系CourseWork。</li>
                            </#if>
                        </ul>
                    </div>

                </div>

            </div>
            <#if setting('MaterialLib.enabled', '0') == '1'>
                <div class="tab-pane" id="file-chooser-browser-pane">
                    <div id="file-browser" data-role="file-browser"
                         data-base-url="${ctx}/uploadfile/browser"
                         data-default-url="${ctx}/uploadfile/browser?source=upload"
                         data-my-sharing-contacts-url="path('material_lib_my_sharing_contacts')"
                         data-empty="暂无文件，请先上传。">
                        <div class="file-browser-list-container"></div>
                    </div>
                </div>
            </#if>

            <div class="tab-pane" id="file-chooser-course-pane">
                <div id="file-browser" data-role="course-file-browser"
                     data-url="${ctx}/uploadfile/browsers?targetType=${targetType}&targetId=${targetId}"
                     data-empty="暂无文件，请先上传。">
                </div>
            </div>

            <div class="tab-pane" id="file-chooser-link-pane">

                <div class="form-group" style="margin-bottom:0px;">
                    <div class="col-md-12 controls">
                        <input type="text" class="form-control" name="link" placeholder="资料链接地址">
                        <div class="help-block"></div>
                    </div>
                </div>

            </div>
        </div>
    </div>
</div>