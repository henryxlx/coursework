<#--      {% set blocks = data('Blocks',{'codes':['bill_banner'] }) %}-->
${(blocks.billBanner)!}

<div class="clearfix">
    <div class="col-md-12">
        <ul class="nav nav-tabs mbl">

            <#if setting('coin.coin_enabled', '0') != '0'>
                <li class="<#if side_nav == 'my-coin'>active</#if>">
                    <a href="#url('my_coin')">我的${setting('coin.coin_name', '')}
                    </a>
                </li>
            </#if>

            <li class="<#if side_nav == 'my-bill'>active</#if>">
                <a href="#url('my_bill'">我的现金账单
                </a>
            </li>


        </ul>
    </div>
</div>