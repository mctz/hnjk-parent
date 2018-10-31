<#--分页 -->
<#setting number_format="#">
<form id="pagerForm" method="post" action="${goPageUrl}">	
	<input type="hidden" name="pageNum" value="${pageInfo.pageIndex}" />
	<input type="hidden" name="pageSize" value="${pageInfo.pageSize}" />	
	
	<!--动态创建查询条件-->
	<#if condition ? exists>
		<#list condition?keys as itemKey>						
			<input type="hidden" name="${itemKey}" value="${condition[itemKey]}"/>
		</#list>
	</#if>
</form>

<div class="panelBar">
			<div class="pages">		
				<#if (isShowPageSelector == "Y")>		
				<span>每页显示</span>
				<select name="pageSize" onchange="<#if (targetType =="dialog")>dialogPageBreak({pageSize:this.value})<#elseif (targetType == "navTab")>navTabPageBreak({pageSize:this.value})<#elseif (targetType == "localArea")>localAreaPageBreak('${localArea}',{pageSize:this.value})</#if>">
					<option value="20" <#if (pageInfo.pageSize ==20)> selected </#if>>20</option>
					<option value="50" <#if (pageInfo.pageSize ==50)> selected </#if>>50</option>
					<option value="100" <#if (pageInfo.pageSize ==100)> selected </#if>>100</option>				
				</select>
				<span>条，
				</#if>
				共${pageInfo.recordCount}条，分为 ${pageInfo.pageCount} 页</span>
			</div>
			
			<div class="pagination" targetType="${targetType}" postBeforeForm="${postBeforeForm}" beforeForm="${beforeForm}" localArea="${localArea}" totalCount="${pageInfo.recordCount}" pageSize="${pageInfo.pageSize}" pageNumShown="${pageNumShown}" currentPage="${pageInfo.pageIndex}"></div>
</div>
