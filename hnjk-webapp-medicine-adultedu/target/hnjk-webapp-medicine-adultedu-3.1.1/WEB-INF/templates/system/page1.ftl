<#-- 分页标签 -->
<table width="100%" border="0">
<tr>
<td>
	<div id="pageBoxDivId">
		<#if !(pageInfo.recordCount==0)>
			<#if !pageInfo.isFirstPage>
				<div class="sk_pageL"><a href="javascript:goPage('1')" ></a></div>
				<div class="sk_pageup"><a href="javascript:goPage('${pageInfo.pageIndex-1}')" ></a></div> 
			<#else>
				<#--
				<div class="sk_pageL2"></div>
				<div class="sk_pageup2"></div> 
				-->
			</#if>
			<#if (pageInfo.pageIndex>5)>
				<#assign start=pageInfo.pageIndex-5>
			<#else>
				<#assign start=1>
			</#if>
			<#if (start+9)<pageInfo.pageCount>
				<#assign end=start+9>
			<#else>
				<#assign end=pageInfo.pageCount>
			</#if>
			<#list start..end as i>
				<#if i==pageInfo.pageIndex>
					<#if 1 != pageInfo.pageCount>
						<div class="sk_pagelist_now">${i}</div> 
					</#if>
				<#else>
					<div class="sk_pagelist"><a href="javascript:goPage('${i}')" >${i}</a></div> 
				</#if>
			</#list>
			<#if !pageInfo.isLastPage>
				<div class="sk_pagedown"><a href="javascript:goPage('${pageInfo.pageIndex+1}')" ></a></div>
				<div class="sk_pageR"><a href="javascript:goPage('${pageInfo.pageCount}')" ></a></div>
			<#else>
				<#--
				<div class="sk_pagedown2"></div>
				<div class="sk_pageR2"></div>
				-->
			</#if>
		</#if>
		<div class="sk_pagelist_text">共找到 ${pageInfo.recordCount} 条记录，分为 ${pageInfo.pageCount} 页</div>
	</div>
</td>
</tr>
</table>

<input type="hidden" name="pageNum" value="${pageInfo.pageIndex}" />
<#if (targetType == "localArea")>
<form id="pagerForm" method="post" action="${goPageUrl}">	
	<input type="hidden" name="pageNum" value="1" />
	<input type="hidden" name="pageSize" value="${pageInfo.pageSize}" />	
	
	<!--动态创建查询条件-->
	<#if condition ? exists>
		<#list condition?keys as itemKey>						
			<input type="hidden" name="${itemKey}" value="${condition[itemKey]}"/>
		</#list>
	</#if>
</form>
<#else>
<script>
function goPage(pageNo){	
	$("input[name=pageNum]").val(pageNo);
	$("#pageBoxDivId").parents("form").submit();//mod by Terry
}
</script>
</#if>