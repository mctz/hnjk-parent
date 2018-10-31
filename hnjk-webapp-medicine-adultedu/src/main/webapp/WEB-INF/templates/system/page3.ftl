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
			<span>共审核${pageInfo.recordCount}条，分为 ${pageInfo.pageCount} 页，${condition['pass']}条毕业审核通过，${condition['unpass']}条毕业审核不通过，${condition['other_pass']}条结业审核通过，${condition['other_unpass']}条结业审核不通过
			<div class="pagination" targetType="${targetType}" localArea="${localArea}" totalCount="${pageInfo.recordCount}" pageSize="${pageInfo.pageSize}" pageNumShown="${pageNumShown}" currentPage="${pageInfo.pageIndex}"></div>
			</div>		</span>
</div>
