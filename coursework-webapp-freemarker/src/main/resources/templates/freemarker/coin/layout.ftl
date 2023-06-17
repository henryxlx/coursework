<#include '/layout.ftl'>

<#macro blockTitle>账户中心 - ${blockTitleParent}</#macro>

<#macro blockContent>

    <div class="row">
        <div class="col-md-3">
            <#if blockSide??><@blockSide/><#else>
                <div class="panel panel-default">
                    <div class="panel-body">
                        <div class="list-group-block">

                            <div class="list-group-panel">


                                <div class="list-group-heading">账户中心</div>

                                <ul class="list-group">
                                    <a class="list-group-item
                     <#if side_nav == 'my-bill' || side_nav == 'my-coin'> active </#if> "
                                       href="
                     <#if setting('coin.coin_enabled', '0') != '0'>
                      ${ctx}/my/bill
                     <#else>
                      ${ctx}/my/bill
                     </#if>
                     "
                                    >我的账户</a>
                                    <a class="list-group-item
                     <#if side_nav == 'my-orders' > active </#if> "
                                       href="#path('my_orders')">我的订单</a>

                                </ul>
                            </div>


                        </div><!-- /list-group-block -->
                    </div>

                </div>
            </#if>
        </div>
        <div class="col-md-9">
            <#if blockMain??><@blockMain/></#if>
        </div>
    </div>
</#macro>