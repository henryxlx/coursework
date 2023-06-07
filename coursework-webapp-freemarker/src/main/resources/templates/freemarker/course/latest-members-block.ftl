<#if students??>
    <div class="panel panel-default">
        <div class="panel-heading">
            <h3 class="panel-title">最新${setting('default.user_name', '学员')}</h3>
        </div>
        <div class="panel-body">
            <ul class="user-grids">
                <#list students as student>
                    <#assign user = users[student.userId]>
                    <li>
                        <@web_macro.user_avatar(user) />
                        <p><@web_macro.user_link(user) /></p>
                    </li>
                </#list>
            </ul>
        </div>
    </div>
</#if>