
    <!--头部导航菜单 -->
	<table width="1003" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td width="662" height="58" align="center" valign="middle" background="${baseUrl}/style/default/portal-images-2/Rmenu2.gif">
        <span class="right"><a href="${baseUrl}/portal/index.html">首页</a> │ 
        <#list navheaderList as nav>
        	<#if nav.channelHref ? exists>	
			<a href="<#if nav.channelType != 'outlink'>${baseUrl}</#if>${nav.channelHref}">${nav.channelName}</a> 
			<#else>
			<a href="${baseUrl}/portal/site/channel/show.html?id=${nav.resourceid}" title="${nav.channelContent}">${nav.channelName}</a> 
			</#if>	
			<#if (navheaderList?size>nav_index+1)>
			|
			</#if>					
        </#list>
        </span>
        </td>
        <td width="341" align="center" valign="middle" background="${baseUrl}/style/default/portal-images-2/Rmenu1.gif" class="right">
        <table width="100%" height="22" border="0" cellpadding="0" cellspacing="0" class="left">
            <tr>
              <td width="94%" height="24" align="right"><a href="#">站内搜索</a>　　　　
                <input name="textfield" type="text" size="15" /></td><td width="6%" align="left">&nbsp;</td>
            </tr>
          </table>          
          </td>
      </tr>
    </table>    
  