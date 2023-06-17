<#assign side_nav = 'my-bill'/>

<#include '/coin/layout.ftl' />

<#macro blockMain>
    <div class="panel panel-default panel-col">
        <#include '/coin/bill-bar.ftl' />

        <div class="panel-body">

            <form id="user-search-form" class="form-inline well well-sm" action="" method="get" novalidate>
                <div class="form-group">
                    <select class="form-control" name="lastHowManyMonths" onchange="submit();">
                        <#assign options = {'':'全部记录','oneWeek':'最近一周','twoWeeks':'最近两周','oneMonth':'最近一个月','twoMonths':'最近两个月','threeMonths':'最近三个月'} />
                        <@select_options options, RequestParameters['lastHowManyMonths'] />
                    </select>
                </div>

                <div class="control-label pull-right" style="padding-top: 8px;">
                    收入：<span style="color:#1bb974;">${amountInflow!0}</span>&nbsp;元&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    支出：<span style="color:#ff7b0e;">${amountOutflow!0}</span>&nbsp;元
                </div>
            </form>

            <br>
            <div class="table-responsive">
                <table class="table table-striped">
                    <#if cashes??>
                        <tr>
                            <th><span class="text-sm">流水号</span></th>
                            <th><span class="text-sm">名称</span></th>
                            <th><span class="text-sm">成交时间</span></th>
                            <th class="text-right" style="padding-right: 60px;"><span class="text-sm">收支</span></th>

                            <th><span class="text-sm">支付方式</span></th>
                        </tr>
                        <#list cashes as cash>
                            <tr>
                                <td><span class="text-sm">${cash.sn}</span></td>
                                <td><span class="text-sm">${cash.name}</span><br>
                                    <span class="text-muted text-sm">订单号：${cash.orderSn}</span></td>
                                <td>
                                    <span class="text-sm">${cash.createdTime?number_to_datetime?string('yyyy-MM-dd HH:mm')}</span>
                                </td>

                                <#if cash.type =="inflow">
                                    <td class="text-right" style="color:#1bb974;padding-right: 50px;">
                                        ${cash.amount}
                                    </td>
                                </#if>
                                <#if cash.type =="outflow">
                                    <td class="text-right" style="color:#ff7b0e;padding-right: 50px;">
                                        -&nbsp;${cash.amount}
                                    </td>
                                </#if>

                                <td>
              <span class="text-sm">
                <#if cash.type =="inflow">
                    支付宝
                <#else>
                    网校支付
                </#if>
              </span>
                                </td>
                            </tr>
                        </#list>
                    <#else>
                        <div class="empty">暂无记录</div>
                    </#if>
                </table>
            </div>
            <@web_macro.paginator paginator! />
        </div>
    </div>

</#macro>