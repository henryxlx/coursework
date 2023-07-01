<#assign modal_class= "modal-lg" />

<#include '/bootstrap-modal-layout.ftl' />

<#macro blockTitle>${course.title}课程的数据</#macro>

<#macro blockBody>
  <div class="table-responsive">
    <table class="table table-bordered" style="word-break:break-all;text-align:center;">
      <tr class="active">
        <td>用户名</td>
        <td>加入课程时间</td>
        <td>完成课程时间</td>
        <td>课程学习天数</td>
        <td>课程学习时长（分）</td>
        <td>提问数</td>
        <td>笔记数</td>
      </tr>

      <#list students! as student>
        <tr>
          <td>${student.username}</td>
          <td>${student.createdTime?number_to_datetime?string('yyyy-MM-dd HH:mm:ss')}</td>
          <td><#if student.fininshTime??>${student.fininshTime?number_to_datetime?string('yyyy-MM-dd HH:mm:ss')}<#else>----</#if></td>
          <td>${student.fininshDay}</td>
          <td>${(student.learnTime!0/60.0)?floor}</td>
          <td>${student.questionCount}</td>
          <td>${student.noteNum}</td>
        </tr>
      </#list>


    </table>
    <@web_macro.paginator paginator! />
  </div>


</#macro>
