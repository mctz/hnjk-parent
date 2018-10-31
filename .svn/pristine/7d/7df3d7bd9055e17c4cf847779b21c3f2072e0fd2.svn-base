<table width="100%" height="560" border="0" cellpadding="0" cellspacing="0">
      <tr>
        <td height="30" valign="top">
        <table width="816" height="30" border="0" cellpadding="0" cellspacing="0">
          <tr id="myTab1">
            <th class="news_title_curr" onclick="nTabs(this,0);"><span style="margin-left:8px">网院通知</span></th>
            <td width="4px"></td>
            <th class="news_title_old" onclick="nTabs(this,1);"><span style="margin-left:8px">网院新闻</span></th>
             <td width="4px"></td>
             <th class="news_title_old" onclick="nTabs(this,2);"><span style="margin-left:8px">网教动态</span></th>
             <td width="4px"></td>
            <td width="358" height="30"><img src="${baseUrl}/style/default/portal-images-2/Rnews_04.gif" width="358" height="30" alt="" /></td>
          </tr>
        </table></td>
      </tr>
      <tr>
        <td height="181" align="center" valign="top">
        <table width="98%" border="0" cellspacing="0" cellpadding="0">
          <tr>
            <td width="20%" rowspan="2">
			<!--循环播放-->
			<div class="container" id="idContainer2">
				<table id="idSlider2" border="0" cellpadding="0" cellspacing="0">
					<tr>
					<td id="loopedslider">					
						<div class="container1">
					        <div class="slides">
					        	<#if photoArticleList?exists>
									<#list photoArticleList as photo>
					               <div>
					               <img src="${rootUrl}portal/images/${photo.imgPath}" title="${photo.title}" width="157" height="141" onclick="window.location.href ='${baseUrl}/portal/site/article/show.html?id=${photo.resourceid}'"/>
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
            <td width="1%" rowspan="2">&nbsp;</td>
            <!--新闻通知条目-->
            <td width="79%" valign="top">
            <!--通知-->
            <table  border="0" cellpadding="0" cellspacing="0" class="STYLE2" id="myTab1_Content0" style="display:">
            <#if noticeArticleList?exists>
            	<#list noticeArticleList as notice>	
	                <tr>
	                  <td align="left" background="${baseUrl}/style/default/portal-images-2/Rdi.gif"> 
	                  	<a href="${baseUrl }/portal/site/article/show.html?id=${notice.resourceid }" title="${notice.title}"> 					
						<#if notice.title?length lt 41 >
						- ${notice.title}
						<#else>
						- ${notice.title[0..40]}...
						</#if>								
						</a> 
						${notice.fillinDate?string('yyyy.MM.dd')}						
						<#if notice.isNew = 'Y'>
						<img src="${baseUrl}/style/default/portal-images-2/news.gif"/>
						</#if>
	                  </td>
	                </tr>  
                </#list>
             <#else>
             	<tr>
                  <td align="left" background="${baseUrl}/style/default/portal-images-2/Rdi.gif"> 
                  - 暂时无相关通知...
                  </td>
                </tr>
             </#if>	           
            </table>  
             <!--新闻-->
               <table  border="0" cellpadding="0" cellspacing="0" class="STYLE2" id="myTab1_Content1" style="display:none">
                 <#if newsArticleList?exists>  
        			 <#list newsArticleList as news>
		                <tr>
		                  <td align="left" background="${baseUrl}/style/default/portal-images-2/Rdi.gif"> 
		                  <a href="${baseUrl}/portal/site/article/show.html?id=${news.resourceid}" title="${news.title}">
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
            <!--动态-->
              <table  border="0" cellpadding="0" cellspacing="0" class="STYLE2" id="myTab1_Content2" style="display:none">
                 <#if dynamicArticleList?exists>	
      			<#list dynamicArticleList as dynamic>
	                <tr>
	                  <td align="left" background="${baseUrl}/style/default/portal-images-2/Rdi.gif"> 
	                  	<a href="${baseUrl}/portal/site/article/show.html?id=${dynamic.resourceid}" title="${dynamic.title}">
	                  	<#if dynamic.title?length lt 41 >
									- ${dynamic.title}
									<#else>
									- ${dynamic.title[0..40]}...
									</#if>   
								</a>   		
				      		${dynamic.fillinDate?string('yyyy.MM.dd')}
				      	<#if dynamic.isNew = 'Y'>
							<img src="${baseUrl}/style/default/portal-images-2/news.gif"/>
						</#if>
	                  </td>
	                </tr>
	               </#list>	                   
	              <#else>
	               <tr>
                 	 <td align="left" background="${baseUrl}/style/default/portal-images-2/Rdi.gif"> 
                  	- 暂时无相关动态...
                 	 </td>
                	</tr>
	              </#if>         
            </table>                       
            </td>
          </tr>
          <tr>          
            <td width="79%" align="right"><span class="STYLE2" id="news_more"><a href="${baseUrl}/portal/site/channel/show.html?id=${noticeChannelId}">查看更多网院通知&gt;&gt;</a></span></td>
          </tr>
        </table></td>
      </tr>
      <tr>
        <td height="125" align="center" valign="top">
        <!--门户banner 循环-->
        <script type=text/javascript>
		
		var focus_width=805
		var focus_height=125
		var text_height=0
		var pics ='';
		var links ='';
		var texts ='';
		var swf_height = focus_height+text_height
		 <#if linkList?exists>	
			<#list linkList as link>
			 pics += '${rootUrl}portal/link/${link.imgPath}';			 
			 links += '${link.linkHref}';
			 texts += '${link.linkName}';
			 <#if (linkList?size >link_index+1)>
			 pics += '|';
			 links += '|';
			 texts += '|';
			 </#if>
			</#list>	
		</#if>		
		
		document.write('<object ID="focus_flash" classid="clsid:d27cdb6e-ae6d-11cf-96b8-444553540000" codebase="http://fpdownload.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=6,0,0,0" width="'+ focus_width +'" height="'+ swf_height +'">');
		document.write('<param name="allowScriptAccess" value="sameDomain"><param name="movie" value="${baseUrl}/common/banner.swf"><param name="quality" value="high"><param name="bgcolor" value="#E7E7E7">');
		document.write('<param name="menu" value="false"><param name=wmode value="transparent">');
		document.write('<param name="FlashVars" value="pics='+pics+'&links='+links+'&texts='+texts+'&borderwidth='+focus_width+'&borderheight='+focus_height+'&textheight='+text_height+'">');
		document.write('<embed ID="focus_flash" src="${baseUrl}/common/banner.swf" wmode="transparent" FlashVars="pics='+pics+'&links='+links+'&texts='+texts+'&borderwidth='+focus_width+'&borderheight='+focus_height+'&textheight='+text_height+'" menu="false" bgcolor="#E7E7E7" quality="high" width="'+ focus_width +'" height="'+ focus_height +'" allowScriptAccess="sameDomain" type="application/x-shockwave-flash" pluginspage="http://www.macromedia.com/go/getflashplayer"/>');		
		document.write('</object>');
	     
	     </script>
     
        </td>
      </tr>
      <tr>
        <td height="13" align="center" valign="top">&nbsp;</td>
      </tr>
      
   <script type="text/javascript">		
	//动态tab
	function nTabs(thisObj,Num){
		if(thisObj.className == "news_title_curr")return;
		var tabObj = thisObj.parentNode.id;
		var tabList = document.getElementById(tabObj).getElementsByTagName("th");	
		var showMore = document.getElementById('news_more');	
		for(i=0; i <tabList.length; i++){
			if (i == Num){
	   			thisObj.className = "news_title_curr"; 
	      		document.getElementById(tabObj+"_Content"+i).style.display = "";
			}else{
	   			tabList[i].className = "news_title_old"; 
	   			document.getElementById(tabObj+"_Content"+i).style.display = "none";
			}
			if(Num == 0) showMore.innerHTML = "<a href=\"${baseUrl}/portal/site/channel/show.html?id=${noticeChannelId}\">查看更多网院通知&gt;&gt;</a>";
			if(Num == 1) showMore.innerHTML = "<a href=\"${baseUrl}/portal/site/channel/show.html?id=${newsChannelId}\">查看更多网院新闻&gt;&gt;</a>";
			if(Num == 2) showMore.innerHTML = "<a href=\"${baseUrl}/portal/site/channel/show.html?id=${dynamicChannelId}\">查看更多网教动态&gt;&gt;</a>";
		} 
	}
	$(function(){
			$('#loopedslider').loopedSlider({
				addPagination: true,
				container: ".container1",
				autoStart: 5000,
				containerClick: false				
			});
	});	
</script>

      <tr>
        <td height="75" align="center" valign="top">
        <table width="100%" border="0" cellspacing="0" cellpadding="0">
        	<table  border="0" cellspacing="0" cellpadding="0" width="100%">
				<tr>
					<td width="100%">
					 <#list navheaderList as nav>
					 	<!--定义每列输出的单元格个数-->
					      <#assign columnCount = 4>
						   <#assign childsize = nav.children?size>
						     <#if childsize % columnCount == 0>
						  	  		<#assign rowCount = (childsize / columnCount) - 1 >
								<#else>
						    		<#assign rowCount = ( childsize / columnCount) >
							</#if>
						<div style="width:49%;float:left;text-align:left">
							<!--栏目1-->
							<table width="100%" border="0" cellspacing="0" cellpadding="0">
							  <tr>
								<td>
								<!--栏目头部-->
								<table width="100%" border="0" cellspacing="0" cellpadding="0">
								  <tr>
									<td width="88%" class="rtex"><span style="padding-left:8px">${nav.channelName}</span></td>
									<td width="12%" align="left">
									<a href="${baseUrl}/portal/site/channel/show.html?id=${nav.resourceid}"><img src="${baseUrl}/style/default/portal-images-2/Rmore.gif" width="37" height="19" border="0"/></a>
									</td>							   
								  </tr>
								</table>							
								</td>
							  </tr>						 
							  <tr>
								<td height="44" align="left" valign="top">
								<!--栏目内容-->
								<table width="96%" border="0" cellpadding="0" cellspacing="0" class="STYLE2">
								<tr>
								<td width="100%">													
									<table border='0' cellSpacing="0" align="center" width="100%">
									<#list 0..rowCount as row >
									    <tr>
									        <#-- 内层循环输出表格的 td  -->
									        <#list 0..columnCount - 1 as cell >
									            <td align="left" height="23" width='${100 / columnCount}%'>
									            <#-- 判断是否存在当前对象：存在就输出；不存在就输出空格 -->
									            <#if  nav.children[row * columnCount + cell]?exists>                        
									                <#assign child =  nav.children[row * columnCount + cell]> 
									                <img src="${baseUrl}/style/default/portal-images-2/title.png"/> 
									                <#if child.channelHref ? exists>                                     
									                  <a href="<#if child.channelType != 'outlink'>${baseUrl}</#if>${child.channelHref}" title="${child.channelContent}">${child.channelName}</a>
									            	 <#else>
									            	  <a href="${baseUrl}/portal/site/channel/show.html?id=${child.resourceid}" title="${child.channelContent}">${child.channelName}</a>
									            	 </#if>
									            <#else>
									                 
									            </#if>
									            </td>
									        </#list>
									    </tr>
									</#list>
									</table>								
								</td>
								</tr>								
								</table>								
								</td>            
								</tr>
							</table>
							<!--栏目1结束-->
						</div>
						
					</#list>
					</td>					
				</tr>
			</table>
        </td>
      </tr>
      
      <tr>
        <td height="12" align="center" valign="top">&nbsp;</td>
      </tr>
      <tr>
        <td height="13" align="left" valign="top">  
