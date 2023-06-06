<div id="media-choosers">

    <div class="file-chooser" id="video-chooser" style="display:none;"
         data-params-url="${ctx}/uploadfile/params?storage=storageSetting.upload_mode&targetType=${targetType}&targetId=${targetId}"
         data-hls-encrypted="${setting('developer.hls_encrypted', '1')}"
         data-targetType="${targetType}"
         data-targetId="${targetId}"
    >
        <div class="file-chooser-bar" style="display:none;">
            <span data-role="placeholder"></span>
            <button class="btn btn-link btn-sm" type="button" data-role="trigger"><i
                        class="glyphicon glyphicon-edit"></i> 编辑
            </button>
            <div class="alert alert-warning" data-role="waiting-tip" style="display:none;">
                正在转换视频格式，转换需要一定的时间，期间将不能播放该视频。<br/>转换完成后将以站内消息通知您。
            </div>
        </div>

        <div class="file-chooser-main">
            <ul class="nav nav-pills nav-pills-mini mbs file-chooser-tabs">
                <li class="active"><a class="file-chooser-uploader-tab" href="#video-chooser-upload-pane"
                                      data-toggle="tab">上传视频</a></li>
                <li>
                    <a href="#video-chooser-disk-pane" data-toggle="tab">
                        从资料库中选择
                    </a>
                </li>

                <li>
                    <a href="#video-chooser-course-file" data-toggle="tab">
                        从课程文件中选择
                    </a>
                </li>

                <li><a href="#video-chooser-import-pane" data-toggle="tab">导入网络视频</a></li>
            </ul>
            <div class="tab-content">
                <div class="tab-pane active" id="video-chooser-upload-pane">

                    <div class="file-chooser-uploader">


                        <div class="file-chooser-uploader-label">选择你要上传的视频文件：</div>
                        <div class="file-chooser-uploader-control">
              <span id="video-choose-uploader-btn"
                    data-role="uploader-btn"
                    data-filetypes="*.mp4"
                    data-button-image="${ctx}/assets/img/common/swfupload-btn.png"
                    data-callback=""
                    data-progressbar="#video-chooser-progress"
                    data-storage-type="${(storageSetting.upload_mode)!}"
                    data-get-media-info="${ctx}/uploadfile/get_media_info/video"
              >
                <a class="uploadBtn btn btn-default btn-lg">
                  上传
                </a>
                <div style="display:none">
                  <input data-role="fileSelected" class="filePrew" type="file"/>
                </div>
              </span>
                        </div>
                        <div class="progress" id="video-chooser-progress" style="display:none;">
                            <div class="progress-bar" role="progressbar" aria-valuenow="0" aria-valuemin="0"
                                 aria-valuemax="100" style="width: 0%;">
                            </div>
                        </div>

                        <div class="alert alert-info">
                            <ul>
                                <li>支持<strong>mp4</strong>格式的视频文件上传，文件大小不能超过
                                    <strong>${upload_max_filesize()}</strong>。
                                    MP4文件的视频编码格式，请使用AVC(H264)编码，否则浏览器无法播放。
                                </li>
                                <li>视频将上传到<strong>网站服务器</strong>，如需使用云视频，请联系CourseWork官方购买。使用云视频，将获得更好的播放体验。</li>
                            </ul>
                        </div>

                    </div>

                </div>
                <div class="tab-pane" id="video-chooser-disk-pane">
                    <div id="file-browser-video" data-role="file-browser"
                         data-base-url="${ctx}/uploadfile/browser?type=video"
                         data-default-url="${ctx}/uploadfile/browser?type=video&source=upload"
                         data-my-sharing-contacts-url="${ctx}/material/lib/my_sharing_contacts"
                         data-empty="暂无视频文件，请先上传。">
                        <div class="file-browser-list-container"></div>
                    </div>
                </div>

                <div class="tab-pane" id="video-chooser-course-file">
                    <div id="file-browser-video" data-role="course-file-browser"
                         data-url="${ctx}/uploadfile/browser?targetType=${targetType}&targetId=${targetId}&type=video"
                         data-empty="暂无视频文件，请先上传。">
                    </div>
                </div>

                <div class="tab-pane" id="video-chooser-import-pane">
                    <div class="input-group">
                        <input class="form-control" type="text" placeholder="支持优酷、土豆、网易公开课的视频页面地址导入"
                               data-role="import-url">
                        <span class="input-group-btn">
              <button type="button" class="btn btn-default" data-role="import"
                      data-url="${ctx}/course/${course.id}/manage/media/import" data-loading-text="正在导入，请稍等">导入</button>
            </span>
                    </div>
                </div>
            </div>
        </div>

    </div>


    <div class="file-chooser" id="audio-chooser" style="display:none;"
         data-params-url="${ctx}/uploadfile/params?storage=storageSetting.upload_mode&targetType=${targetType}&targetId=${targetId}"
         data-targetType="${targetType}"
         data-targetId="${targetId}"
    >
        <div class="file-chooser-bar" style="display:none;">
            <span data-role="placeholder"></span>
            <button class="btn btn-link btn-sm" type="button" data-role="trigger"><i
                        class="glyphicon glyphicon-edit"></i> 编辑
            </button>
        </div>

        <div class="file-chooser-main">
            <ul class="nav nav-pills nav-pills-mini mbs file-chooser-tabs">
                <li class="active"><a class="file-chooser-uploader-tab" href="#audio-chooser-upload-pane"
                                      data-toggle="tab">上传音频</a></li>
                <li><a href="#audio-chooser-disk-pane" data-toggle="tab">
                        从资料库中选择
                    </a></li>

                <li>
                    <a href="#audio-chooser-course-file" data-toggle="tab">
                        从课程文件中选择
                    </a>
                </li>

            </ul>
            <div class="tab-content">
                <div class="tab-pane active" id="audio-chooser-upload-pane">

                    <div class="file-chooser-uploader">
                        <div class="file-chooser-uploader-label">选择你要上传的音频文件：</div>
                        <div class="file-chooser-uploader-control">
              <span id="audio-choose-uploader-btn"
                    data-role="uploader-btn"
                    data-button-image="${ctx}/assets/img/common/swfupload-btn.png"
                    data-callback=""
                    data-progressbar="#audio-chooser-progress"
                    data-storage-type="${(storageSetting.upload_mode)!}"
                    data-get-media-info="${ctx}/uploadfile/get_media_info/audio"
              >
                <a class="uploadBtn btn btn-default btn-lg">
                  上传
                </a>
                <div style="display:none">
                  <input data-role="fileSelected" class="filePrew" type="file"/>
                </div>
              </span>
                        </div>
                        <div class="progress" id="audio-chooser-progress" style="display:none;">
                            <div class="progress-bar" role="progressbar" aria-valuenow="0" aria-valuemin="0"
                                 aria-valuemax="100" style="width: 0%;">
                            </div>
                        </div>

                        <div class="alert alert-info">
                            <ul>
                                <li>支持<strong>mp3</strong>格式的音频文件上传，且文件大小不能超过<strong>
                                        ${upload_max_filesize()}
                                    </strong>。
                                </li>
                                <li>音频将上传到<strong>网站服务器</strong></li>
                            </ul>
                        </div>
                    </div>

                </div>
                <div class="tab-pane" id="audio-chooser-disk-pane">
                    <div id="file-browser-audio" data-role="file-browser"
                         data-base-url="${ctx}/uploadfile/browser?type=audio"
                         data-default-url="${ctx}/uploadfile/browser?type=audio&source=upload"
                         data-my-sharing-contacts-url="{ctx}/material/lib/my_sharing_contacts"
                         data-empty="暂无音频文件，请先上传。">
                        <div class="file-browser-list-container"></div>
                    </div>
                </div>

                <div class="tab-pane" id="audio-chooser-course-file">
                    <div id="file-browser-audio" data-role="course-file-browser"
                         data-url="${ctx}/uploadfile/browser?targetType=${targetType}&targetId=${targetId}&type=audio"
                         data-empty="暂无音频文件，请先上传。"></div>
                </div>

            </div>
        </div>

    </div>


    <div class="file-chooser" id="ppt-chooser" style="display:none;"
         data-params-url="${ctx}/uploadfile/params?storage=storageSetting.upload_mode&targetType=${targetType}&targetId=${targetId}&convertor=ppt&lazyConvert=1"
         data-targetType="${targetType}"
         data-targetId="${targetId}"
    >
        <div class="file-chooser-bar" style="display:none;">
            <span data-role="placeholder"></span>
            <button class="btn btn-link btn-sm" type="button" data-role="trigger"><i
                        class="glyphicon glyphicon-edit"></i> 编辑
            </button>
        </div>

        <div class="file-chooser-main">
            <ul class="nav nav-pills nav-pills-mini mbs file-chooser-tabs">
                <li class="active"><a class="file-chooser-uploader-tab" href="#ppt-chooser-upload-pane"
                                      data-toggle="tab">上传PPT</a></li>
                <li><a href="#ppt-chooser-disk-pane" data-toggle="tab">
                        从资料库中选择
                    </a></li>

                <li>
                    <a href="#ppt-chooser-course-file" data-toggle="tab">
                        从课程文件中选择
                    </a>
                </li>

            </ul>
            <div class="tab-content">
                <div class="tab-pane active" id="ppt-chooser-upload-pane">

                    <div class="file-chooser-uploader">
                        <div class="file-chooser-uploader-label">选择你要上传的PPT文件：</div>
                        <div class="file-chooser-uploader-control">
              <span id="ppt-choose-uploader-btn"
                    data-role="uploader-btn"
                    data-button-image="${ctx}/assets/img/common/swfupload-btn.png"
                    data-callback=""
                    data-storage-type="${(storageSetting.upload_mode)!}"
                    data-progressbar="#ppt-chooser-progress">
                <a class="uploadBtn btn btn-default btn-lg">
                  上传
                </a>
                <div style="display:none">
                  <input data-role="fileSelected" class="filePrew" type="file"/>
                </div>
              </span>
                        </div>
                        <div class="progress" id="ppt-chooser-progress" style="display:none;">
                            <div class="progress-bar" role="progressbar" aria-valuenow="0" aria-valuemin="0"
                                 aria-valuemax="100" style="width: 0%;">
                            </div>
                        </div>

                        <div class="alert alert-info">
                            <ul>
                                <li>支持<strong>ppt, pptx</strong>格式的PPT文件上传，且文件大小不能超过<strong>100 MB</strong>。</li>
                                <li>PPT将上传到<strong>云服务器</strong></li>
                            </ul>
                        </div>
                    </div>

                </div>
                <div class="tab-pane" id="ppt-chooser-disk-pane">
                    <div id="file-browser-ppt" data-role="file-browser"
                         data-base-url="${ctx}/uploadfile/browser?type=ppt"
                         data-default-url="${ctx}/uploadfile/browser?type=ppt&source=upload"
                         data-my-sharing-contacts-url="${ctx}/material/lib/my_sharing_contacts"
                         data-empty="暂无PPT文件，请先上传。">
                        <div class="file-browser-list-container"></div>
                    </div>
                </div>

                <div class="tab-pane" id="ppt-chooser-course-file">
                    <div id="file-browser-ppt" data-role="course-file-browser"
                         data-url="${ctx}/uploadfile/browser?targetType=${targetType}&targetId=${targetId}&type=ppt"
                         data-empty="暂无PPT文件，请先上传。"></div>
                </div>

            </div>
        </div>

    </div>


    <div class="file-chooser" id="document-chooser" style="display:none;"
         data-params-url="${ctx}/uploadfile/params?storage=storageSetting.upload_mode&targetType=${targetType}&targetId=${targetId}&convertor=document"
         data-targetType="${targetType}"
         data-targetId="${targetId}"
    >
        <div class="file-chooser-bar" style="display:none;">
            <span data-role="placeholder"></span>
            <button class="btn btn-link btn-sm" type="button" data-role="trigger"><i
                        class="glyphicon glyphicon-edit"></i> 编辑
            </button>
        </div>

        <div class="file-chooser-main">
            <ul class="nav nav-pills nav-pills-mini mbs file-chooser-tabs">
                <li class="active"><a class="file-chooser-uploader-tab" href="#document-chooser-upload-pane"
                                      data-toggle="tab">上传文档</a></li>
                <li><a href="#document-chooser-disk-pane" data-toggle="tab">
                        从资料库中选择
                    </a></li>

                <li>
                    <a href="#document-chooser-course-file" data-toggle="tab">
                        从课程文件中选择
                    </a>
                </li>

            </ul>
            <div class="tab-content">
                <div class="tab-pane active" id="document-chooser-upload-pane">

                    <div class="file-chooser-uploader">
                        <div class="file-chooser-uploader-label">选择你要上传的文档：</div>
                        <div class="file-chooser-uploader-control">
              <span id="document-choose-uploader-btn"
                    data-role="uploader-btn"
                    data-button-image="${ctx}/assets/img/common/swfupload-btn.png"
                    data-callback=""
                    data-storage-type="${(storageSetting.upload_mode)!}"
                    data-progressbar="#document-chooser-progress">
                <a class="uploadBtn btn btn-default btn-lg">
                  上传
                </a>
                <div style="display:none">
                  <input data-role="fileSelected" class="filePrew" type="file"/>
                </div>
              </span>
                        </div>
                        <div class="progress" id="document-chooser-progress" style="display:none;">
                            <div class="progress-bar" role="progressbar" aria-valuenow="0" aria-valuemin="0"
                                 aria-valuemax="100" style="width: 0%;">
                            </div>
                        </div>

                        <div class="alert alert-info">
                            <ul>
                                <li>支持<strong>pdf,doc,docx</strong>格式的文档上传，且文件大小不能超过<strong>100 MB</strong>。</li>
                                <li>文档将上传到<strong>云服务器</strong></li>
                            </ul>
                        </div>
                    </div>

                </div>
                <div class="tab-pane" id="document-chooser-disk-pane">
                    <div id="file-browser-document" data-role="file-browser"
                         data-base-url="${ctx}/uploadfile/browser?type=document"
                         data-default-url="${ctx}/uploadfile/browser?type=document&source=upload"
                         data-my-sharing-contacts-url="${ctx}/material/lib/my_sharing_contacts"
                         data-empty="暂无文档，请先上传。">
                        <div class="file-browser-list-container"></div>
                    </div>
                </div>

                <div class="tab-pane" id="document-chooser-course-file">
                    <div id="file-browser-document" data-role="course-file-browser"
                         data-url="${ctx}/uploadfile/browser?targetType=${targetType}&targetId=${targetId}&type=document"
                         data-empty="暂无文档，请先上传。"></div>
                </div>

            </div>
        </div>

    </div>


    <div class="file-chooser" id="flash-chooser" style="display:none;"
         data-params-url="${ctx}/uploadfile/params?storage=storageSetting.upload_mode&targetType=${targetType}&targetId=${targetId}&convertor="
         data-targetType="${targetType}"
         data-targetId="${targetId}"
    >
        <div class="file-chooser-bar" style="display:none;">
            <span data-role="placeholder"></span>
            <button class="btn btn-link btn-sm" type="button" data-role="trigger"><i
                        class="glyphicon glyphicon-edit"></i> 编辑
            </button>
        </div>

        <div class="file-chooser-main">
            <ul class="nav nav-pills nav-pills-mini mbs file-chooser-tabs">
                <li class="active"><a class="file-chooser-uploader-tab" href="#flash-chooser-upload-pane"
                                      data-toggle="tab">上传Flash</a></li>
                <li><a href="#flash-chooser-disk-pane" data-toggle="tab">
                        从资料库中选择
                    </a></li>

                <li>
                    <a href="#flash-chooser-course-file" data-toggle="tab">
                        从课程文件中选择
                    </a>
                </li>

            </ul>
            <div class="tab-content">
                <div class="tab-pane active" id="flash-chooser-upload-pane">

                    <div class="file-chooser-uploader">
                        <div class="file-chooser-uploader-label">选择你要上传的Flash：</div>
                        <div class="file-chooser-uploader-control">
              <span id="flash-choose-uploader-btn"
                    data-role="uploader-btn"
                    data-button-image="${ctx}/assets/img/common/swfupload-btn.png"
                    data-callback=""
                    data-storage-type="${(storageSetting.upload_mode)!}"
                    data-progressbar="#flash-chooser-progress">
                <a class="uploadBtn btn btn-default btn-lg">
                  上传
                </a>
                <div style="display:none">
                  <input data-role="fileSelected" class="filePrew" type="file"/>
                </div>
              </span>
                        </div>
                        <div class="progress" id="flash-chooser-progress" style="display:none;">
                            <div class="progress-bar" role="progressbar" aria-valuenow="0" aria-valuemin="0"
                                 aria-valuemax="100" style="width: 0%;">
                            </div>
                        </div>

                        <div class="alert alert-info">
                            <ul>
                                <li>支持<strong>swf</strong>格式的文件上传，且文件大小不能超过<strong>100 MB</strong>。</li>
                                <li>Flash将上传到<strong>云服务器</strong></li>
                            </ul>
                        </div>
                    </div>

                </div>
                <div class="tab-pane" id="flash-chooser-disk-pane">
                    <div id="file-browser-flash" data-role="file-browser"
                         data-base-url="${ctx}/uploadfile/browser?type=flash"
                         data-default-url="${ctx}/uploadfile/browser?type=flash&source=upload"
                         data-my-sharing-contacts-url="${ctx}/material/lib/my_sharing_contacts"
                         data-empty="暂无Flash，请先上传。">
                        <div class="file-browser-list-container"></div>
                    </div>
                </div>

                <div class="tab-pane" id="flash-chooser-course-file">
                    <div id="file-browser-flash" data-role="course-file-browser"
                         data-url="${ctx}/uploadfile/browser?targetType=${targetType}&targetId=${targetId}&type=flash"
                         data-empty="暂无Flash，请先上传。"></div>
                </div>

            </div>
        </div>

    </div>


</div>