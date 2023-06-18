
<table class="table table-condensed table-bordered table-striped table-hover">
    <thead>
    <tr>
        <th width="35%"></th>
        <th width="20%">今日</th>
        <th width="20%" >昨日</th>
        <#if userAcl.hasRole('ROLE_SUPER_ADMIN')>
            <th width="40%">历史</th>
        </#if>
    </tr>
    </thead>
    <tbody>
    <tr>
        <td>新注册用户数</td>
        <td><span class="pull-right">${todayRegisterNum!}</span></td>
        <td><span class="pull-right">${yesterdayRegisterNum!}</span></td>
        <#if userAcl.isAdmin()>
            <td><a href="${ctx}/admin/operation/analysis/register/trend?analysisDateType=register">趋势</a> <span
                        class="text-muted">|</span> <a
                        href="${ctx}/admin/operation/analysis/register/detail?analysisDateType=register">详情</a></td>
        </#if>
    </tr>

    <tr>
        <td>用户登录数<small>（不含重复登录）</small></td>
        <td><span class="pull-right">${todayLoginNum!}</span></td>
        <td><span class="pull-right">${yesterdayLoginNum!}</span></td>
        <#if userAcl.isAdmin()>
            <td><a href="${ctx}/admin/operation/analysis/login/trend?analysisDateType=login">趋势</a> <span
                        class="text-muted">|</span> <a
                        href="${ctx}/admin/operation/analysis/login/detail?analysisDateType=login">详情</a></td>
        </#if>
    </tr>

    <tr>
        <td>新增课程数</td>
        <td><span class="pull-right">${todayCourseNum!}</span></td>
        <td><span class="pull-right">${yesterdayCourseNum!}</span></td>
        <#if userAcl.isAdmin()>
            <td><a href="${ctx}/admin/operation/analysis/course/trend?analysisDateType=course">趋势</a> <span
                        class="text-muted">|</span> <a
                        href="${ctx}/admin/operation/analysis/course/detail?analysisDateType=course">详情</a></td>
        </#if>
    </tr>

    <tr>
        <td>新增课时数</td>
        <td><span class="pull-right">${todayLessonNum!}</span></td>
        <td><span class="pull-right">${yesterdayLessonNum!}</span></td>
        <#if userAcl.isAdmin()>
            <td><a href="${ctx}/admin/operation/analysis/lesson/trend?analysisDateType=lesson">趋势</a> <span
                        class="text-muted">|</span> <a
                        href="${ctx}/admin/operation/analysis/lesson/detail?analysisDateType=lesson">详情</a></td>
        </#if>
    </tr>

    <tr>
        <td>加入学习数</td>
        <td><span class="pull-right">${todayJoinLessonNum!}</span></td>
        <td><span class="pull-right">${yesterdayJoinLessonNum!}</span></td>
        <#if userAcl.isAdmin()>
            <td><a href="${ctx}/admin/operation/analysis/join-lesson/trend?analysisDateType=join-lesson">趋势</a> <span
                        class="text-muted">|</span> <a
                        href="${ctx}/admin/operation/analysis/join-lesson/detail?analysisDateType=join-lesson">详情</a>
            </td>
        </#if>
    </tr>

    <tr>
        <td>完成课时学习数</td>
        <td><span class="pull-right">${todayFinishedLessonNum!}</span></td>
        <td><span class="pull-right">${yesterdayFinishedLessonNum!}</span></td>
        <#if userAcl.isAdmin()>
            <td><a href="${ctx}/admin/operation/analysis/finished-lesson/trend?analysisDateType=finished-lesson">趋势</a>
                <span class="text-muted">|</span> <a
                        href="${ctx}/admin/operation/analysis/finished-lesson/detail?analysisDateType=finished-lesson">详情</a>
            </td>
        </#if>
    </tr>

    <tr>
        <td>视频观看数<small>（含重复打开）</small></td>
        <td><span class="pull-right">${todayAllVideoViewedNum!}</span></td>
        <td><span class="pull-right">${yesterdayAllVideoViewedNum!}</span></td>
        <#if userAcl.isAdmin()>
            <td><a href="${ctx}/admin/operation/analysis/video-view/trend?analysisDateType=video-viewe">趋势</a> <span
                        class="text-muted">|</span> <a
                        href="${ctx}/admin/operation/analysis/video-view/detail?analysisDateType=video-view">详情</a></td>
        </#if>
    </tr>

    <tr>
        <td>└─ 云视频观看数</td>
        <td><span class="pull-right">${todayCloudVideoViewedNum!}</span></td>
        <td><span class="pull-right">${yesterdayCloudVideoViewedNum!}</span></td>
        <#if userAcl.isAdmin()>
            <td>
                <#if (keyCheckResult.error)! == 'error'>
                    未开通或未开启云视频!
                <#else>
                    <a href="${ctx}/admin/operation/analysis/video-view-cloud/trend?analysisDateType=video-view-cloud">趋势</a>
                    <span class="text-muted">|</span> <a
                        href="${ctx}/admin/operation/analysis/video-view-cloud/detail?analysisDateType=video-view-cloud">详情</a>
                </#if>
            </td>
        </#if>
    </tr>

    <tr>
        <td>└─ 本地视频观看数</td>
        <td><span class="pull-right">${todayLocalVideoViewedNum!}</span></td>
        <td><span class="pull-right">${yesterdayLocalVideoViewedNum!}</span></td>
        <#if userAcl.hasAnyRole('ROLE_SUPER_ADMIN')>
            <td>
                <a href="${ctx}/admin/operation/analysis/video-view-local/trend?analysisDateType=video-view-local">趋势</a>
                <span class="text-muted">|</span> <a
                        href="${ctx}/admin/operation/analysis/video-view-local/detail?analysisDateType=video-view-local">详情</a>
            </td>
        </#if>
    </tr>

    <tr>
        <td>└─ 网络视频观看数</td>
        <td><span class="pull-right">${todayNetVideoViewedNum!}</span></td>
        <td><span class="pull-right">${yesterdayNetVideoViewedNum!}</span></td>
        <#if userAcl.isAdmin()>
            <td><a href="${ctx}/admin/operation/analysis/video-view-net/trend?analysisDateType=video-view-net">趋势</a>
                <span class="text-muted">|</span> <a
                        href="${ctx}/admin/operation/analysis/video-view-net/detail?analysisDateType=video-view-net">详情</a>
            </td>
        </#if>
    </tr>

    <tr>
        <td>课程总数</td>
        <td><span class="pull-right">${todayCourseSum!}</span></td>
        <td><span class="pull-right">${yesterdayCourseSum!}</span></td>
        <#if userAcl.isAdmin()>
            <td><a href="${ctx}/admin/operation/analysis/course-sum/trend?analysisDateType=course-sum">趋势</a> <span
                        class="text-muted">|</span> <a
                        href="${ctx}/admin/operation/analysis/course-sum/detail?analysisDateType=course-sum">详情</a></td>
        </#if>
    </tr>

    <tr>
        <td>用户总数</td>
        <td><span class="pull-right">${todayUserSum!}</span></td>
        <td><span class="pull-right">${yesterdayUserSum!}</span></td>
        <#if userAcl.isAdmin()>
            <td><a href="${ctx}/admin/operation/analysis/user-sum/trend?analysisDateType=user-sum">趋势</a> <span
                        class="text-muted">|</span> <a
                        href="${ctx}/admin/operation/analysis/user-sum/detail?analysisDateType=user-sum">详情</a></td>
        </#if>
    </tr>

    </tbody>
</table>
