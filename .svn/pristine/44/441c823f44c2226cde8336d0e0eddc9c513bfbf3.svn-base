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
			$("#classesLi").hide();
			window.setTimeout(function(){				
				var existIds = $("#${param.idsN}").val();
				$("#memberBody input[name=resourceid]").each(function(){
					if(existIds.indexOf($(this).val())>=0){
						$(this).attr("checked",true);
					}
				});
			},500);
			
		});
		
		function clickThis(obj){
			var type = $('#selectortype').val();
			if(obj.checked){
				if(type=='leader'){
					$("#memberBody input[name=resourceid]").each(function(){
						if($(this).val() != obj.value){
							$(this).attr("checked",false);
						}
					});
					if($("#${param.idsN}").val().indexOf(obj.value)<0){
						$("#${param.idsN}").val(obj.value);
						$("#${param.namesN}").val($(obj).attr('rel'));
					}
				} else {
					if($("#${param.idsN}").val().indexOf(obj.value)<0){
						$("#${param.idsN}").val($("#${param.idsN}").val()+obj.value+",");
						$("#${param.namesN}").val($("#${param.namesN}").val()+$(obj).attr('rel')+",");
					}
				}
			}else{
				if(type=='leader'){
					$("#${param.idsN}").val("");
					$("#${param.namesN}").val("");
				} else {
					if($("#${param.idsN}").val().indexOf(obj.value)>=0){
						var ids = $("#${param.idsN}").val().replace((obj.value+","),"");
						var names = $("#${param.namesN}").val().replace(($(obj).attr('rel')+","),"");
						$("#${param.idsN}").val(ids);
						$("#${param.namesN}").val(names);
					}
				}
			}			
		}	
		
		function clickAll(obj){			
			$("#memberBody input[name='resourceid']").each(function(){
				if(!$(this).attr("disabled")){
					$(this).attr("checked",$(obj).attr("checked"));	
					clickThis(this);	
				}						
			});
		}		

	</script>
</head>
<body>
	<div class="page">
		<div class="pageHeader">
			<div class="pageHeader">
				<form onsubmit="return dialogSearch(this);"
					action="${baseUrl}/edu3/framework/bbs/bbsgroup/member.html"
					method="post">
					<div class="searchBar">
						<ul class="searchContent">
							<li><label>姓名：</label><input type="text" name="name"
								value="${condition['name']}" style="width: 115px" /> <input
								type="hidden" name="courseId" id="courseId"
								value="${condition['courseId']}" /> <input type="hidden"
								id='selectortype' name="type" value="${type}" /> <input
								type="hidden" name="namesN" value="${param.namesN}" /> <input
								type="hidden" name="idsN" value="${param.idsN}" /></li>
							<li><label>学号：</label><input type="text" id="studyNo"
								name="studyNo" value="${condition['studyNo']}"
								style="width: 115px" /></li>
							<c:if test="${empty branchSchool }">
								<li><label>教学站：</label>
								<gh:selectModel name="branchSchool" bindValue="resourceid"
										displayValue="unitName" style="width:120px"
										modelClass="com.hnjk.security.model.OrgUnit"
										value="${condition['branchSchool']}"
										condition="unitType='brSchool'" /></li>
							</c:if>
							<li><label>学习层次：</label> <gh:selectModel name="classic"
									bindValue="resourceid" displayValue="classicName"
									value="${condition['classic']}"
									modelClass="com.hnjk.edu.basedata.model.Classic"
									style="width:120px" /></li>
							<li id="classesLi"><label>班级：</label> <gh:classesAutocomplete
									name="classesId" id="selector_members_classid" tabindex="1"
									displayType="code" defaultValue="${condition['classesId']}"
									exCondition="${classesCondition}"></gh:classesAutocomplete></li>
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
			<div class="pageContent">
				<table class="table" layouth="168">
					<thead>
						<tr>
							<th width="5%"><c:if test="${type ne'leader' }">
									<input type="checkbox" name="checkall" onclick="clickAll(this)" />
								</c:if></th>
							<th width="8%">姓名</th>
							<th width="14%">学号</th>
							<th width="8%">年级</th>
							<th width="8%">培养层次</th>
							<th width="15%">专业</th>
							<th width="20%">班级</th>
							<th width="15%">教学站</th>
						</tr>
					</thead>
					<tbody id="memberBody">
						<c:forEach items="${stuplanlist.result}" var="plan" varStatus="vs">
							<tr>
								<td><input type="checkbox" name="resourceid"
									value="${plan.resourceid }" rel="${plan.studentName}"
									<c:if test="${fn:contains(filterids,plan.resourceid) }">disabled="disabled"</c:if>
									onclick="clickThis(this)" /></td>
								<td>${plan.studentName}</td>
								<td>${plan.studyNo}</td>
								<td>${plan.grade.gradeName}</td>
								<td>${plan.classic.classicName }</td>
								<td>${plan.major.majorName }</td>
								<td>${plan.classes.classname }</td>
								<td>${plan.branchSchool }</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
				<gh:page page="${stuplanlist}"
					goPageUrl="${baseUrl }/edu3/framework/bbs/bbsgroup/member.html"
					pageType="sys" targetType="dialog" condition="${condition}" />
			</div>
		</div>
</body>