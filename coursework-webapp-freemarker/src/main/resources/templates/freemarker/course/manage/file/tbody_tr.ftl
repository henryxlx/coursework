<#assign  quality = ""/>

<tr id="upload-file-tr-${uploadFile.id}" data-role="item" data-convertHash="${uploadFile.convertHash}">

    <td><input value="${uploadFile.id}" type="checkbox" data-role="batch-item"></td>

    <td>
        <#if uploadFile.storage?? && uploadFile.storage == 'cloud'>
            <span class="glyphicon glyphicon-cloud text-muted" title="云文件"></span>
        </#if>

        <#if uploadFile.convertStatus?? && uploadFile.convertStatus == 'waiting'>
            <a rel="tooltip" title
               data-original-title="{{ uploadFile.filename }}">{{ uploadFile.filename || sub_text(30) }}</a>
            <br><span class="text-warning text-sm">正在文件格式转换</span>
            <#if ( now - uploadFile.updatedTime ) gt 28800>
            <a href="javascript:;" data-url="${ctx}/course/${course.id}/manage/file/${uploadFile.id}/convert" class="convert-file-btn text-danger">重新转换</a>
            </#if>
        <#elseif uploadFile.convertStatus?? && uploadFile.convertStatus == 'doing'>
            <a rel="tooltip" title data-original-title="{{ uploadFile.filename }}"  >{{ uploadFile.filename||sub_text(30) }}</a>
            <br><span class="text-info text-sm">正在文件格式转换</span>
            <#if uploadFile.type == 'ppt' && ( now - uploadFile.updatedTime ) gt 1800>
            <a href="javascript:;" data-url="${ctx}/course/${course.id}/manage/file/${uploadFile.id}/convert" class="convert-file-btn text-danger">重新转换</a>
            </#if>
        <#elseif uploadFile.convertStatus == 'error'>
            <a rel="tooltip" title data-original-title="{{ uploadFile.filename }}"  >{{ uploadFile.filename||sub_text(30) }}</a>
            <br><span class="text-danger text-sm">文件格式转换失败，<a href="javascript:;" data-url="${ctx}/course/${course.id}/manage/file/${uploadFile.id}/convert" class="convert-file-btn">重新转换</a></span>
        <#else>
            <#if ['courselesson', 'coursematerial']?seq_contains(uploadFile.targetType) >
                <a href="${ctx}/course/${course.id}/manage/file/${uploadFile.id}" target="_blank" rel="tooltip" title
                   data-original-title="${uploadFile.filename}">${fastLib.subText(uploadFile.filename, 30)}</a>
                <#if uploadFile.type == 'ppt' && !(uploadFile.metas2.length)?? && uploadFile.targetType == 'courselesson'>
                    <br><a href="javascript:;"
                           data-url="${ctx}/course/${course.id}/manage/file/${uploadFile.id}/convert"
                           class="convert-file-btn text-danger">重新转换</a>
                </#if>
            <#else>
                <a rel="tooltip" title
                   data-original-title="${uploadFile.filename}">${fastLib.subText(uploadFile.filename, 30)}</a>
            </#if>
        </#if>

        <#if setting('developer.debug')?? && (uploadFile.convertParams.convertor)??>
            <a href="javascript:;" data-url="${ctx}/course/${course.id}/manage/file/${uploadFile.id}/convert"
               class="convert-file-btn text-danger">重新转换(调试模式)</a>
        </#if>
        <#if uploadFile.useNum == 0>
        <br>
        <span class="label label-danger tip">未使用</span>
        <#else>
        <br>
        </#if>
        <#if uploadFile.convertStatus == 'none' && uploadFile.type != 'flash'>
        <span class="label label-default tip">未转码</span>
        </#if>
        <#if uploadFile.convertStatus == 'success'>

        <span class="label label-success tip">已转码</span>
        </#if>

        <#if uploadFile.type == "video" && uploadFile.metas2??>
            <#if uploadFile.convertParams.videoQuality == "low">
                <#assign quality = "流畅画质"/>
            <#elseif uploadFile.convertParams.videoQuality == "normal">
                <#assign quality = "标准画质"/>
            <#elseif uploadFile.convertParams.videoQuality == "high">
                <#assign quality = "精细画质"/>
            </#if>
            <#if uploadFile.metas2.shd?? >
                <#if uploadFile.useNum != 0 ><br></#if>
                <span class="label label-info  tip" data-toggle="tooltip" title="{{quality}}">超清</span>
            <#elseif uploadFile.metas2.hd??>
                <#if uploadFile.useNum != 0 ><br></#if>
                <span class="label label-info  tip" data-toggle="tooltip" title="{{quality}}">高清</span>
            <#elseif uploadFile.metas2.sd??>
                <#if uploadFile.useNum != 0 ><br></#if>
                <span class="label label-info  tip" data-toggle="tooltip" title="{{quality}}">标清</span>
            </#if>
        </#if>

    </td>
    <td>
        ${dict_text('fileType', uploadFile.type)}
    </td>
    <td><@web_macro.bytesToSize uploadFile.size /></td>
    <td>
        <span class="text-sm"><@web_macro.user_link users[''+uploadFile.updatedUserId]! /></span>
        <br>
        <span class="text-muted text-sm">${uploadFile.updatedTime?number_to_datetime?string('yyyy-MM-dd HH:mm')}</span>
    </td>

</tr>