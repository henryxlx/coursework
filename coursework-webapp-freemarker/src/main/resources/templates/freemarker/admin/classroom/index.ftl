<#assign menu = 'classroom'/>
<#assign script_controller = 'course/manage'/>

<#include '/admin/classroom/layout.ftl'/>
<#macro blockTitle>课程管理 - ${blockTitleParent}</#macro>

<#macro blockMain>

    <form id="message-search-form" class="form-inline well well-sm" action="" method="get" novalidate>

        <div class="form-group">
            <input class="form-control" type="text" placeholder="班级名称" name="title"
                   value="${RequestParameters['title']!}">
        </div>

        <button class="btn btn-primary">搜索</button>

    </form>
    <#if classroomInfo??>
        <table class="table table-striped table-hover" id="classroom-table">
            <thead>
            <tr>
                <th>{{'%title%编号' | trans
                    ({'%title%': setting('calssroom.title') | default('班级' | trans)
                    }) }}
                </th>
                <th width="25%">{{'%name%名称' | trans
                    ({'%name%': setting('classroom.name') | default('班级' | trans)
                    })}}
                </th>
                {% include 'OrgBundle:Org/Parts:table-thead-tr.html.twig' %}
                <th>{{'课程数' | trans}}</th>
                <th>{{'学员数' | trans}}</th>
                <th width="15%">{{'价格' | trans}}</th>
                <th>{{'状态' | trans}}</th>
                <th>{{'操作' | trans}}</th>
            </tr>
            </thead>
            <tbody>

            {% for classroom in classroomInfo %}
            {% set category = categories[classroom.categoryId]|default(null) %}
            {% include 'ClassroomBundle:ClassroomAdmin:table-tr.html.twig' with {classroom:classroom,category:category}
            %}

            {% endfor %}

            </tbody>

        </table>
    <#else>
        <div class="empty">暂无班级信息!</div>
    </#if>
    <div class="pull-right">
        <@web_macro.paginator paginator!/>
    </div>


</#macro>