<#include '/bootstrap-modal-layout.ftl' />

<#macro blockTitle>个人详细信息</#macro>

<#macro blockBody>

    <table class="table table-striped table-condenseda table-bordered">
        <tr>
            <th width="25%">用户名</th>
            <td width="75%">
                <a class="pull-right" href="${ctx}/user/${user.id}" target="_blank">个人主页</a>
                ${user.username}
            </td>
        </tr>

        <tr>
            <th>Email</th>
            <td>${user.email}</td>
        </tr>

        <tr>
            <th>姓名</th>
            <td>${profile.truename!'暂无'}</td>
        </tr>

        <tr>
            <th>性别</th>
            <td>
                <#if profile.gender == 'mail'>
                    男
                <#elseif profile.gender == 'femail'>
                    女
                </#if>
            </td>
        </tr>

        <tr>
            <th>公司</th>
            <td>${profile.company!'暂无'}</td>
        </tr>

        <tr>
            <th>职业</th>
            <td>${profile.job!'暂无'}</td>
        </tr>

        <tr>
            <th>头衔</th>
            <td>${profile.title!'暂无'}</td>
        </tr>

        <tr>
            <th>电话</th>
            <td>${profile.mobile!'暂无'}</td>
        </tr>

        <tr>
            <th>个人签名</th>
            <td>${profile.signature!'暂无'}</td>
        </tr>

        <tr>
            <th>自我介绍</th>
            <td>${profile.about!'暂无'}</td>
        </tr>

        <tr>
            <th>个人网站</th>
            <td>${profile.site!'暂无'}</td>
        </tr>

        <tr>
            <th>微博</th>
            <td>${profile.weibo!'暂无'}</td>
        </tr>

        <tr>
            <th>微信</th>
            <td>${profile.weixin!'暂无'}</td>
        </tr>

        <tr>
            <th>QQ</th>
            <td>${profile.qq!'暂无'}</td>
        </tr>
        <#list userFields as userField>
            <tr>
                <th width="25%">${userField.title}</th>
                <td>
                    <#if profile[userField.fieldName]??>
                        <#if userField.type=="date">
                            ${profile[userField.fieldName]?number_to_date?string('yyyy-MM-dd')}
                        <#else>
                            ${profile[userField.fieldName]}
                        </#if>
                    <#else>
                        暂无
                    </#if>
                </td>
            </tr>
        </#list>
    </table>
    </table>

</#macro>

<#macro blockFooter>
    <button type="button" class="btn btn-primary" data-dismiss="modal">关闭</button>
</#macro>