<!--校外学习中心首页中部-->
<table width="93%" height="360" border="0" cellpadding="0" cellspacing="0">
      <tr>
        <td height="30" colspan="2" valign="top">
        <table  width="741" height="30" border="0" cellpadding="0" cellspacing="0">
          <tr>
            <td height="19" width="80%" style="color:#fff;background:#fff url(${baseUrl}/style/default/portal-images-2/Rnews2_01.png)  left repeat-x;">
            <span style="padding-left:6px"><b>学习中心新闻</b></span>            
            </td>            
            <td width="10%" class="title_more">
            <#if newsChannelId?exists>
             <a href="${baseUrl}/portal/site/channel/show.html?id=${newsChannelId}&uid=${schoolId}">更多...</a>
             </#if>
            </td>
            </tr>
        </table></td>
      </tr>
      <tr>
        <td height="154" colspan="2" align="center" valign="top"><table width="98%" border="0" cellspacing="0" cellpadding="0">
          <tr>
            <td width="20%" rowspan="2">
            <!--滚动图片新闻-->
            <div class="container" id="idContainer2">
				<table id="idSlider2" border="0" cellpadding="0" cellspacing="0">
					<tr>
					<td id="loopedslider">					
						<div class="container1">
					        <div class="slides">
					        	<#if photoArticleList?exists>
									<#list photoArticleList as photo>
					               <div>
					               <img src="${rootUrl}portal/images/${photo.imgPath}" title="${photo.title}" width="157" height="141" onclick="window.location.href ='${baseUrl}/portal/site/article/show.html?id=${photo.resourceid}&uid=${photo.orgUnit.resourceid}'"/>
					               </div>
					             	</#list>
								</#if>		 
					        </div>
						</div>											 
					</td>		
					</tr>
				</table>	
			</div>
            </td>
            <td width="1%">&nbsp;</td>
            <td width="79%" valign="top">
            <!--学习中心新闻-->
            <table width="99%" border="0" cellpadding="0" cellspacing="0" class="STYLE2">
                 <#if newsArticleList?exists>  
        			 <#list newsArticleList as news>
		                <tr>
		                  <td align="left" background="${baseUrl}/style/default/portal-images-2/Rdi.gif"> 
		                  <a href="${baseUrl}/portal/site/article/show.html?id=${news.resourceid}&uid=${news.orgUnit.resourceid}" title="${news.title}">
				      		<#if news.title?length lt 41 >
									- ${news.title}
									<#else>
									- ${news.title[0..40]}...
									</#if>   
								</a>   		
				      		${news.fillinDate?string('yyyy.MM.dd')}
				      		<#if news.isNew = 'Y'>
							<img src="${baseUrl}/style/default/portal-images-2/news.gif"/>
							</#if>
		                  </td>
		                </tr>  
		               </#list>    
		         <#else>
                <tr>
                  <td align="left" background="${baseUrl}/style/default/portal-images-2/Rdi.gif"> 
                  - 暂时无相关新闻...
                  </td>
                </tr>
		         </#if> 
            </table>
            </td>
          </tr>
          
        </table></td>
      </tr>
      <tr>
        <td height="12" colspan="2" align="center" valign="top">&nbsp;</td>
      </tr>
      <tr>
        <td height="164" align="center" valign="top">
        <table border="0" cellpadding="0" cellspacing="0" class="STYLE2" style="width:95%">
          <tr>
            <td width="30%" align="left" bgcolor="#474747">　<span class="STYLE3">公告栏</span></td>
            <td width="60%" align="right" bgcolor="#D2D2D2">
            <#if dynamicChannelId?exists>
            <a href="${baseUrl}/portal/site/channel/show.html?id=${dynamicChannelId}&uid=${schoolId}">更多...</a>
            </#if>
            </td>
          </tr>
            <#if dynamicArticleList?exists>  
        			 <#list dynamicArticleList as news>
		                <tr>
		                  <td colspan="2"  align="left" background="${baseUrl}/style/default/portal-images-2/Rdi.gif"> 
		                  <a href="${baseUrl}/portal/site/article/show.html?id=${news.resourceid}&uid=${news.orgUnit.resourceid}" title="${news.title}">
				      		<#if news.title?length lt 16 >
									 ${news.title}
									<#else>
									 ${news.title[0..15]}...
									</#if>   
								</a>   		
				      		${news.fillinDate?string('yyyy.MM.dd')}
				      		<#if news.isNew = 'Y'>
							<img src="${baseUrl}/style/default/portal-images-2/news.gif"/>
							</#if>
		                  </td>
		                </tr>  
		               </#list>    
		         <#else>
                <tr>
                  <td colspan="2"  align="left" background="${baseUrl}/style/default/portal-images-2/Rdi.gif"> 
                  - 暂时无相关公告...
                  </td>
                </tr>
		        </#if>         
          
        </table>
        </td>
        <td align="center" valign="top">
        <table border="0" cellpadding="0" cellspacing="0" class="STYLE2" style="width:95%">
          <tr>
            <td width="30%" align="left" bgcolor="#474747">　<span class="STYLE3">通知栏</span></td>
            <td width="70%" align="right" bgcolor="#D2D2D2">
            <#if noticeChannelId?exists>  
            <a href="${baseUrl}/portal/site/channel/show.html?id=${noticeChannelId}&uid=${schoolId}">更多...</a>
            </#if>
            </td>
          </tr>
            <#if noticeArticleList?exists>  
        			 <#list noticeArticleList as news>
		                <tr>
		                  <td colspan="2"  align="left" background="${baseUrl}/style/default/portal-images-2/Rdi.gif"> 
		                  <a href="${baseUrl}/portal/site/article/show.html?id=${news.resourceid}&uid=${news.orgUnit.resourceid}" title="${news.title}">
				      		<#if news.title?length lt 16 >
									 ${news.title}
									<#else>
									 ${news.title[0..15]}...
									</#if>   
								</a>   		
				      		${news.fillinDate?string('yyyy.MM.dd')}
				      		<#if news.isNew = 'Y'>
							<img src="${baseUrl}/style/default/portal-images-2/news.gif"/>
							</#if>
		                  </td>
		                </tr>  
		               </#list>    
		         <#else>
                <tr>
                  <td colspan="2"  align="left" background="${baseUrl}/style/default/portal-images-2/Rdi.gif"> 
                  - 暂时无相关公告...
                  </td>
                </tr>
		        </#if>           
        </table>
        </td>
      </tr>
    </table>
    <script type="text/javascript">	
	$(function(){
			$('#loopedslider').loopedSlider({
				addPagination: true,
				container: ".container1",
				autoStart: 5000,
				containerClick: false				
			});
	});	
	</script>