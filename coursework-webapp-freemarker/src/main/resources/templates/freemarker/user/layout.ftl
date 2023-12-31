<#assign bodyClass = 'userpage'/>
<#assign script_controller = 'user/user'/>

<@block_title "${(user.username)!'佚名'}的公共主页"/>

<#include '/layout.ftl'/>

<#macro blockContent>

    <@renderController path='/user/headerBlock' params={'userId':user.id}/>

    <div class="es-row-wrap container-gap userpage-body">
        <ul class="nav nav-pills userpage-nav clearfix">
            <#if (user.roles)?? && user.roles?contains('ROLE_TEACHER')>
                <li <#if pageNav == 'teach'>class="active"</#if>><a href="${ctx}/user/${(user.id)!}/teach">在教课程</a>
                </li>
            </#if>
            <li <#if pageNav == 'learn'>class="active"</#if>><a href="${ctx}/user/${(user.id)!}/learn">在学课程</a></li>
            <li <#if pageNav == 'favorited'>class="active"</#if>><a
                        href="${ctx}/user/${(user.id)!}/favorited">收藏的课程</a></li>
            <li <#if pageNav == 'group'>class="active"</#if>><a href="${ctx}/user/${(user.id)!}/group">加入的小组</a></li>
            <li <#if pageNav == 'friend'>class="active"</#if>><a href="${ctx}/user/${(user.id)!}/following">关注/粉丝</a>
            </li>

            <#if setting('classroom.enabled')??>
                <#if (user.roles)?? && user.roles?contains('ROLE_TEACHER')>
                    <li <#if pageNav! == 'teaching'>class="active"</#if>><a
                                href="${ctx}/user/teaching/classrooms?id=${(user.id)!}">在教${setting('classroom.name', '班级')}</a>
                    </li>
                </#if>

                <li <#if pageNav! == 'learning'>class="active"</#if>><a
                            href="${ctx}/user/learning/classrooms?id=${(user.id)!}">在学${setting('classroom.name', '班级')}</a>
                </li>
            </#if>
            <li <#if pageNav! == 'about'>class="active"</#if>><a href="${ctx}/user/${(user.id)!}/about">个人介绍</a></li>
        </ul>

        <#if blockMain??><@blockMain/></#if>

    </div>

</#macro>