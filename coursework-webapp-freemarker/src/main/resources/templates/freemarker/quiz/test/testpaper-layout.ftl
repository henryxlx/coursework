<#assign script_controller = 'quiz-question/do-test'/>

<#include '/layout.ftl'/>

<#macro blockTitle>${paper.name} - ${blockTitleParent}</#macro>

<#macro blockContent>


    <div class="es-row-wrap testpaper-heading">
        <div class="row">
            <div class="col-sm-12">
                <div class="testpaper-titlebar clearfix">
                    <h1 class="testpaper-title">${paper.name} <br><small
                                class="text-sm"><#if (paperResult.status)??><#if paperResult.status == 'reviewing'>答题人：${student.username} 交卷时间：${paperResult.endTime?number_to_datetime?string('yyyy-MM-dd HH:mm')} 用时：${fastLib.duration(paperResult.usedTime)}<#elseif paperResult.status == 'finished'>答题人：${student.username} 交卷时间：${paperResult.endTime?number_to_datetime?string('yyyy-MM-dd HH:ss')} 用时：${fastLib.duration(paperResult.usedTime)}</#if></#if></small>
                    </h1>
                    <div class="testpaper-status"><#if blockTestpaperHeadingStatus??><@blockTestpaperHeadingStatus/></#if></div>
                </div>
                <#if blockTestpaperHeadingContent??><@blockTestpaperHeadingContent/></#if>
            </div>

            <#if paperResult?? &&  ['finished','reviewing']?seq_contains(paperResult.status) && userAcl.hasRole('ROLE_TEACHER') >

            <#else>
                <#local showTestpaperNavbar = showTestpaperNavbar!'on' />
                <#if showTestpaperNavbar == 'on'>
                    <div id="testpaper-navbar" class="testpaper-navbar" data-spy="affix" data-offset-top="200">
                        <ul class="nav nav navbar-nav nav-pills">
                            <#list paper.metas.question_type_seq! as type>
                                <li><a href="#testpaper-questions-${type}">${dict['questionType'][type]}</a></li>
                            </#list>
                        </ul>
                    </div>
                </#if>
            </#if>

        </div>
    </div>

    <div class="row">
        <div class="col-md-9">
            <div class="testpaper-body" oncopy="return false;" oncut="return false;" onselectstart="return false">
                <#include '/quiz/test/do-test.ftl' />
            </div>
        </div>
        <div class="col-md-3">
            <#if blockTestpaperBodySidebar??><@blockTestpaperBodySidebar/></#if>
        </div>
    </div>

</#macro>

<#macro blockTimeoutDialog>

    <div class="timeout-dialog-layout">
        <div id="timeout-dialog" class="modal in" aria-hidden="false" style="display: none;">
            <div class="modal-dialog modal-dialog-small">
                <div class="modal-content">
                    <div class="modal-header">
                        <h4 class="modal-title">考试结束</h4>
                    </div>
                    <div class="modal-body">
                        <div class="well well-lg">
                            <div class="empty">考试已结束，请点击下面的按钮查看结果~</div>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <a href="{{ path('course_manage_test_results', {id:id} ) }}" id="show_testpaper_result"
                           class="btn btn-info">查看结果</a>
                    </div>
                </div>
            </div>
        </div>
    </div>

</#macro>

<#macro blockTestpaperCheckedDialog>

    <div class="testpaper-dialog-layout">
        <div id="testpaper-checked-dialog" class="modal in" aria-hidden="false" style="display: none;">
            <div class="modal-dialog modal-dialog-small">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                        <h4 class="modal-title">完成批阅</h4>
                    </div>
                    <div class="modal-body">

                        <div class="form-group">
                            <div class="controls">
                                <textarea class="form-control" rows='4' id="testpaper-teacherSay-input"
                                          placeholder="请输入评语"></textarea>
                            </div>
                        </div>
                        <div class="form-group">
                            <div class="controls">
                                <select class="form-control" id="testpaper-teacherSay-select">
                                    <option value="">---常用评语---</option>
                                    <option value="1">继续努力，继续进步！</option>
                                    <option value="2">不错，有进步，再努力些就会更棒！</option>
                                    <option value="3">你真棒！我为你骄傲！</option>
                                    <option value="4">有点松懈了吧？继续加油吧！</option>
                                    <option value="5">用心、专注、坚持，你能做的更好的！</option>
                                </select>
                            </div>
                        </div>

                    </div>
                    <div class="modal-footer">
                        <a href="javascript:;" class="btn btn-default" data-dismiss="modal">取消</a>
                        <button type="submit" class="btn btn-info" data-toggle="form-submit"
                                data-target="#teacherCheckForm" id="testpaper-teacherSay-btn"
                                data-post-url="{{ path('course_manage_test_teacher_check', { id: id }) }}"
                                data-goto="{{ path('course_manage_list_teacher_test_reviewing') }}">完成批阅
                        </button>
                    </div>
                </div>
            </div>
        </div>
    </div>

</#macro>

<#macro blockTestpaperFinishedDialog>

    <div class="testpaper-finished-dialog-layout">
        <div id="testpaper-finished-dialog" class="modal in" aria-hidden="false" style="display: none;">
            <div class="modal-dialog modal-dialog-small">
                <div class="modal-content">
                    <div class="modal-header">
                        <h4 class="modal-title">确认交卷</h4>
                    </div>
                    <div class="modal-body">
                        <h4 class="text-primary">您真的要交卷吗？</h4>
                    </div>
                    <div class="modal-footer">
                        <a href="javascript:;" class="btn btn-link" data-dismiss="modal">取消</a>
                        <button class="btn btn-primary" id="testpaper-finish-btn" data-saving-text="正在交卷, 请稍等...">确认交卷
                        </button>
                    </div>
                </div>
            </div>
        </div>
    </div>

</#macro>