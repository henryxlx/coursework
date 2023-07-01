<#assign modal_class= "modal-lg" />

<#include '/bootstrap-modal-layout.ftl' />

<#macro blockTitle>${lesson.title}课时的数据</#macro>

<#macro blockBody>
  <div class="table-responsive">
    <table class="table table-bordered" style="word-break:break-all;text-align:center;">
      <tr class="active">
        <td>用户名</td>
        <td>加入课时时间</td>
        <td>完成课时时间</td>
        <td width="12%">课时学习时长（分）</td>
        <td width="12%">音视频时长（分）</td>
        <td width="12%">音视频学习时长（分）</td>
        <td>最近考试得分</td>
      </tr>

      <#list students! as student>
        <tr>
          <td>${student.username}</td>
          <td>${student.startTime?number_to_datetime?string('yyyy-MM-dd HH:mm:ss')}</td>
          <td><#if student.finishedTime?? && student.finishedTime gt 3600>${student.finishedTime?number_to_datetime?string('yyyy-MM-dd HH:mm:ss')}<#else>----</#if></td>
          <td>${(student.learnTime!0/60.0)?floor}</td>
          <td>
            <#if lesson.type == 'live'>
              ----
            <#else>
              ${lesson.length!'----'}
            </#if>
          </td>
          <td><#if lesson.type == "video" || lesson.type == "audio">${(student.watchTime/60.0)?floor}<#else>----</#if></td>
          <td><#if lesson.type == "testpaper">${student.result!}<#else>----</#if></td>
        </tr>
      </#list>


    </table>
    <@web_macro.paginator paginator! />
  </div>


</#macro>
