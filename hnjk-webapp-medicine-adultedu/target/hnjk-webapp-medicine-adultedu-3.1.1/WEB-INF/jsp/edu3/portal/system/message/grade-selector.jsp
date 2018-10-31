<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<style>
* {
	font-size: 12px;
}

.type {
	text-align: center;
	line-height: 28px;
}
</style>
<script type="text/javascript">
		$(document).ready(function(){
			window.setTimeout(function(){				
				var existIds = $("#${condition['codesN']}").val();
				$("#check_all_selector_gradeBody input[name=resourceid]").each(function(){
					if($(this).val()!="" && existIds.indexOf($(this).val())>=0){
						$(this).attr("checked",true);
					}
				});
			},500);
			
		});
		
		function msgGradeClickThis(obj){
			if(obj.value!=""){
				if(obj.checked){				
					if($("#${condition['codesN']}").val().indexOf(obj.value)<0){
						$("#${condition['codesN']}").val($("#${condition['codesN']}").val()+obj.value+",");
						$("#${condition['namesN']}").val($("#${condition['namesN']}").val()+$(obj).attr('rel')+",");
					}
				}else{
					if($("#${condition['codesN']}").val().indexOf(obj.value)>=0){
						var ids = $("#${condition['codesN']}").val().replace((obj.value+","),"");
						var names = $("#${condition['namesN']}").val().replace(($(obj).attr('rel')+","),"");
						$("#${condition['codesN']}").val(ids);
						$("#${condition['namesN']}").val(names);
					}
				}
			}			
		}	
		
		function msgGradeCheckboxAll(obj){			
			$("#check_all_selector_gradeBody input[name='resourceid']").each(function(){
				$(this).attr("checked",$(obj).attr("checked"));	
				msgGradeClickThis(this);		
			});
		}		

	</script>
</head>
<body>
	<div class="page">
		<div class="pageHeader">
			<form onsubmit="return dialogSearch(this);"
				action="${baseUrl}/edu3/portal/message/grade.html" method="post">
				<div class="searchBar">
					<ul class="searchContent">
						<li><label>年级名称：</label><input type="text" name="gradeName"
							value="${condition['gradeName']}" /> <input type="hidden"
							name="codesN" value="${condition['codesN']}" /> <input
							type="hidden" name="namesN" value="${condition['namesN']}" /></li>
					</ul>
					<div class="subBar">
						<ul>
							<li><div class="buttonActive">
									<div class="buttonContent">
										<button type="submit">查 询</button>
									</div>
								</div></li>
							<li><div class="button">
									<div class="buttonContent">
										<button type="button" class="close">确 定</button>
									</div>
								</div></li>
						</ul>
					</div>
				</div>
			</form>
		</div>
		<div class="check_all_selector_gradeBody">
			<table id="gradeTab" class="table" layouth="150">
				<thead>
					<tr>
						<th width="5%"><input type="checkbox" name="checkall"
							id="check_all_selector_grade" onclick="msgGradeCheckboxAll(this)" /></th>
						<th width="25%">年级名称</th>
						<th width="25%">年度名称</th>
					</tr>
				</thead>
				<tbody id="gradeBody">
					<c:forEach items="${gradeList.result}" var="grade" varStatus="vs">
						<tr>
							<td><input type="checkbox" name="resourceid"
								value="${grade.resourceid }" rel="${grade.gradeName }"
								onclick="msgGradeClickThis(this)" /></td>
							<td>${grade.gradeName }</td>
							<td>${grade.yearInfo }</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<gh:page page="${gradeList}"
				goPageUrl="${baseUrl }/edu3/portal/message/grade.html"
				pageType="sys" targetType="dialog" condition="${condition}" />
		</div>
	</div>
</body>
</html>