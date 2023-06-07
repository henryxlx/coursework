<#assign side_nav = 'my-group'/>
<#assign tab_nav = 'index' />
<#include '/my/layout.ftl'/>

<#macro blockTitle>我的小组 - ${blockTitleParent}</#macro>

<#macro blockMain>
  <#assign class = 'panel-col'/>
  <#include '/bootstrap/panel.ftl' />
</#macro>

<#macro blockHeading>我的小组</#macro>

<#macro blockPanelBody>
  <div class="row">

    <div class="col-md-12">

      <#--      <#include '/group/group-member-nav-pill.ftl'/>-->

      <div class="page-header">
        <h4> 我加入的小组<a class="badge pull-right" href="{{path('group_member_join',{type:'myGroup'})}}"
                      style="background-color:#3cb373;">${groupsCount!}</a></h4>
      </div>

      <#--      <#include '/group/groups-ul.html.twig' />-->

    </div>

    <div class="col-md-12">

      <div class="page-header">
        <h4>我发起的话题<a class="badge pull-right" href="{{path('group_member_threads')}}"
                     style="background-color:#3cb373;">${threadsCount!}</a></h4>
      </div>

      <#--      <#assign threads = ownThreads />-->
      <#--      <#assign groups = groupsAsOwnThreads />-->
      <#--      <#include '/group/groups-threads-ul.ftl'/>-->

    </div>

    <div class="col-md-12">

      <div class="page-header">
        <h4>回复的话题<a class="badge pull-right" href="{{path('group_member_posts')}}"
                    style="background-color:#3cb373;">${postsCount!}</a></h4>
      </div>

      <#--      <#assign lastPostMembers = postLastPostMembers />-->
      <#--      <#assign groups = groupsAsPostThreads />-->
      <#--      <#include '/group/groups-threads-ul.ftl'/>-->

    </div>

    <div class="col-md-12">

      <div class="page-header">
        <h4>收藏的话题<a class="badge pull-right" href="{{path('group_thread_collecting')}}"
                    style="background-color:#3cb373;">${collectCount!}</a></h4>
      </div>

      <#--      <#assign lastPostMembers = collectLastPostMembers />-->
      <#--      <#assign groups = groupsAsCollectThreads />-->
      <#--      <#assign threads = collectThreads />-->
      <#--      <#include '/group/groups-threads-ul.ftl'/>-->

    </div>

  </div>
</#macro>
