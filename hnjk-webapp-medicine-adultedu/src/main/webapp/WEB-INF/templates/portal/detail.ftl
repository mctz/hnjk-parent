<#--显示详细内容 -->
<!-- 内容详情 -->
<div id="con_infor" style="width:810px;height:100%;border-right:1px #eee solid;border-left:1px #eee solid;">
 <table border="0" width="100%" cellpadding="0" cellspacing="0">
        <tr>
          <td><span class="detail_post"><a href="${baseUrl}/portal/index.html">首页</a> >> <a href="${baseUrl}/portal/site/channel/show.html?id=${article.channel.resourceid}">${article.channel.channelName}</a></span></td>
        </tr>
        <tr>
          <td>&nbsp;</td>
        </tr>
        <tr>
          <td height="16" align="center" valign="top" class="detail_title">${article.title}</td>
        </tr>
        <tr>
          <td height="16" align="center" class="detail_title2">${article.orgUnit.unitName}  ${article.fillinDate?string('yyyy年MM月dd日')}</td>
        </tr>
        <tr>
          <td height="12" align="center" valign="top">&nbsp;</td>
        </tr>
        <tr>
          <td height="215" valign="top" class="detail_content">
          ${article.content}
          </td>
        </tr>
         <tr>
          <td height="12" align="center" valign="top">&nbsp;</td>
        </tr>
        <tr>
          <td height="26" valign="top">
          <#if article.attachs ? exists>
          	<#list article.attachs as attach>
          		附件${attach_index+1}： <a href="${baseUrl}/portal/site/article/download.html?id=${attach.resourceid}">${attach.attName}</a><br/>
          		</#list>
			</#if>	
          </td>
        </tr>  
         <tr>
          <td height="16" align="center" class="detail_title2" style="text-align:right">来源：${article.source}</td>
        </tr>   
        <tr>
          <td height="12" align="center" valign="top">&nbsp;</td>
        </tr>  
	</table>
</div>