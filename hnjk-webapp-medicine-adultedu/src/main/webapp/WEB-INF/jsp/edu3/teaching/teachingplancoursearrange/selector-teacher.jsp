<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<script type="text/javascript">
		$(document).ready(function(){
			window.setTimeout(function(){
				var existIds = $("#${idsN}").val();
				$("#taskSearchBody input[name='resourceid']").each(function(){
					if(existIds.indexOf($(this).val())>=0){
						$(this).attr("checked",true);
					}					
				});				
			},500);			
		});
					
		function clickThisTask(obj){
			var type = $("#taskSearchForm input[name='type']").val();
			if(type=='0'){
				if(obj.checked){
					$("#taskSearchBody input[name='resourceid']").each(function(){
						if($(this).val()!=obj.value){
							$(this).attr("checked",false);
						}	
					});				
					if($("#${idsN}").val().indexOf(obj.value)<0){
						$("#${idsN}").val(obj.value);
						$("#${namesN}").val($(obj).attr('rel'));
					}
				}else{
					if($("#${idsN}").val().indexOf(obj.value)>=0){
						$("#${idsN}").val("");
						$("#${namesN}").val("");
					}
				}
			} else {
				if(obj.checked){				
					if($("#${idsN}").val().indexOf(obj.value)<0){
						$("#${idsN}").val($("#${idsN}").val()+obj.value+",");
						$("#${namesN}").val($("#${namesN}").val()+$(obj).attr('rel')+",");
					}
				}else{
					if($("#${idsN}").val().indexOf(obj.value)>=0){
						var ids = $("#${idsN}").val().replace((obj.value+","),"");
						var names = $("#${namesN}").val().replace(($(obj).attr('rel')+","),"");
						$("#${idsN}").val(ids);
						$("#${namesN}").val(names);
					}
				}
			}
		}
		
		function clickAllTask(obj){			
			$("#taskSearchBody input[name='resourceid']").each(function(){
				$(this).attr("checked",$(obj).attr("checked"));	
				clickThisTask(this);			
			});
		}	
	</script>
</head>

<body>
	<div class="page">
		<div class="pageHeader">
			<div class="pageHeader">
				<form id="taskSearchForm" onsubmit="return dialogSearch(this);"
					action="${baseUrl}/edu3/teaching/teachingplancoursetimetable/teacher.html"
					method="post">
					<input type="hidden" name="unitId" value="${unitId}" /> <input
						type="hidden" name="unitIds" value="${unitIds}" /> <input
						type="hidden" name="idsN" value="${idsN}" /> <input type="hidden"
						name="namesN" value="${namesN}" /> <input type="hidden"
						name="type" value="${condition['type']}" />
					<div class="searchBar">
						<ul class="searchContent">
							<li><label>授课模式：</label>
							<gh:select name="teachingType"
									value="${condition['teachingType']}"
									dictionaryCode="CodeTeachingType" style="width:150px" /></li>
							<li><label>姓名：</label><input type="text" name="cnName"
								value="${condition['cnName']}" /></li>
							<li><label>人员编号：</label><input type="text"
								name="teacherCode" value="${condition['teacherCode']}" /></li>
						</ul>
						<div class="subBar">
							<ul style="margin-top: 20px; margin-right: 30px">
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
			<div class="pageContent">
				<table class="table" layouth="168">
					<thead>
						<tr>
							<th width="10%"><c:if test="${condition['type'] ne '0'}">
									<input type="checkbox" onclick="clickAllTask(this)" />
								</c:if>&nbsp;</th>
							<th width="15%">人员编号</th>
							<th width="15%">姓名</th>
							<th width="10%">性别</th>
							<th width="15%">职务</th>
							<th width="10%">办公电话</th>
							<th width="15%">学习中心</th>
							<th width="10%">授课模式</th>
						</tr>
					</thead>
					<tbody id="taskSearchBody">
						<c:forEach items="${teacherlist.result}" var="t" varStatus="vs">
							<tr>
								<td><input type="checkbox" name="resourceid"
									value="${t.resourceid }" rel="${t.cnName }"
									onclick="clickThisTask(this)" /></td>
								<td>${t.teacherCode}</td>
								<td>${t.cnName }</td>
								<td>${ghfn:dictCode2Val('CodeSex',t.gender) }</td>
								<td>${ghfn:dictCode2Val('CodeDuty',t.duty) }</td>
								<td>${t.officeTel}</td>
								<td style="color: ${t.orgUnit.unitType=='brSchool'?'':'red' }">${t.orgUnit.unitName}</td>
								<td>${ghfn:dictCode2Val('CodeTeachingType',t.teachingType) }</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
				<gh:page page="${teacherlist}"
					goPageUrl="${baseUrl }/edu3/teaching/teachingplancoursetimetable/teacher.html"
					pageType="sys" targetType="dialog" condition="${condition}" />
			</div>
		</div>
</body>