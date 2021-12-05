<#if setting('copyright.owned')??>
<#if setting('copyright.name')??>
Powered by <a href="${ctx}" target="_blank">${setting('copyright.name')}</a>
</#if>
<#else>
Powered by <a href="#" target="_blank">EduNext v${setting('app.version', '5.3.2')}</a>
©2014-2015 <a href="#" target="_blank"> 第二课程工作网</a>
</#if>