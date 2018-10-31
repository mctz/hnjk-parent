<#-- BBS分页标签 -->
<DIV class=mainbar0 style="MARGIN-TOP: 2px; HEIGHT: 32px">
		<DIV style="FLOAT: right">
			<TABLE class=tableborder5 style="MARGIN: 0px" cellSpacing=4 cellPadding=1>
				<TBODY>
					<TR align=middle>
						<#assign firstNum=pageInfo.pageIndex-pageInfo.pageIndex%10+1 >
						<#if pageInfo.pageIndex%10==0 >
							<#assign firstNum=firstNum-10 >
						</#if>
						<#assign lastNum=firstNum+9 >
						<#if (lastNum>pageInfo.pageCount) >
							<#if (pageInfo.pageCount==0) >
								<#assign lastNum=1 >
							<#else>
								<#assign lastNum=pageInfo.pageCount >
							</#if>
						</#if>
						<#if condition ? exists>
							<#assign querys="" >
							<#list condition?keys as itemKey>	
								<#if querys!="">
									<#assign querys=querys+"&"+itemKey+"="+condition[itemKey] >	
								<#else>
									<#assign querys=itemKey+"="+condition[itemKey] >	
								</#if>								
							</#list>
						</#if>	
						<#if firstNum!=1 >
							<td class="page">
								&nbsp;
								<a title="第一页"
									href="${goPageUrl}?${querys}&pageNum=1">
									<img border="0" src="${baseUrl }/style/default/images/page_first.png">
								</a>&nbsp;
							</td>						
							<td class="page">
								&nbsp;
								<a title="上十页"
									href="${goPageUrl}?${querys}&pageNum=${firstNum-1 }">
									<img border="0" src="${baseUrl }/style/default/images/page_prev.png">
								</a>&nbsp;
							</td>
						</#if>
						<#list firstNum..lastNum as i>	
							<#if pageInfo.pageIndex==i >
								<TD class=page2> &nbsp; <B>${i }</B> </TD>
							<#else>
								<TD class="page">							
									<A href="${goPageUrl}?${querys}&pageNum=${i }"><SPAN>${i }</SPAN></A>
								</TD>
							</#if>	
						</#list>
						<#if (lastNum < pageInfo.pageCount) >
							<TD class=page>
								&nbsp;
								<A title=下十页 href="${goPageUrl}?${querys}&pageNum=${lastNum+1 }">
								<IMG src="${baseUrl }/style/default/images/page_next.gif" border=0>
								</A>&nbsp;
							</TD>
							<TD class=page>
								&nbsp;
								<A title=尾页 href="${goPageUrl}?${querys}&pageNum=${pageInfo.pageCount }">
								<IMG src="${baseUrl }/style/default/images/page_last.gif" border=0>
								</A>&nbsp;
							</TD>
						</#if>
						<TD class="page">
							<INPUT class="PageInput"
								style="BORDER-TOP-WIDTH: 0px; BORDER-LEFT-WIDTH: 0px; BORDER-BOTTOM-WIDTH: 0px; BORDER-RIGHT-WIDTH: 0px"
								size="2" value="${pageInfo.pageIndex }" id="bbsPageInput" onkeydown="if(event.keyCode==13) {goPage();return false;}"/>
						</TD>
						<TD class=page>
							<INPUT class=button
								style="BORDER-TOP-WIDTH: 0px; BORDER-LEFT-WIDTH: 0px; BORDER-BOTTOM-WIDTH: 0px; HEIGHT: 16px; BORDER-RIGHT-WIDTH: 0px"
								type=submit value=GO onclick="goPage();">
						</TD>
						<TD class=tabletitle1 title=总数>
							<DIV class=page_line>
								&nbsp;${pageInfo.recordCount }&nbsp;
							</DIV>
							&nbsp;${pageInfo.pageIndex }/${pageInfo.pageCount }&nbsp;页&nbsp;
						</TD>
					</TR>					
				</TBODY>
			</TABLE>
		</DIV>
<script type="text/javascript">
	function goPage(){		
		var pageNum = $('#bbsPageInput').val();
		if(pageNum.isNumber()){
			pageNum = pageNum>${pageInfo.pageCount }?${pageInfo.pageCount }:pageNum;
			pageNum = pageNum<1?1:pageNum;
			location.href="${goPageUrl}?${querys}&pageNum="+pageNum;
		}
	}
</script>