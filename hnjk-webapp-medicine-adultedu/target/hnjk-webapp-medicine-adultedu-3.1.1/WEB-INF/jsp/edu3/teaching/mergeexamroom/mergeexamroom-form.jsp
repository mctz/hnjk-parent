<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>考场合并</title>
<style type="text/css">
.red {
	color: #F00;
	background-color: #FFFF00;
	bgcolor: #FFFF00;
}

.blue {
	color: #00F;
	background-color: #00FFFF;
	bgcolor: #00FFFF;
}
</style>
</head>
<body>
	<h2 class="contentTitle">编辑考场合并</h2>
	<div class="page">
		<div class="pageContent">
			<form id="mergeExamRoomForm" method="post"
				action="${baseUrl}/edu3/teaching/mergeExamRoom/save.html"
				class="pageForm" onsubmit="return checkBrschool(this)">
				<input type="hidden" name="resourceid"
					value="${mergeExamroom.resourceid }" />
				<div class="pageFormContent" layoutH="97">
					<table id="mergeExamRoomTable" class="form">
						<tr>
							<td width="12%">考试类型:</td>
							<td width="88%" colspan="3"><select name="examType"
								class="required" id="examType">
									<option value="">请选择</option>
									<option value="1"
										<c:if test="${mergeExamroom.examType == 1 }"> selected="selected" </c:if>>入学考试</option>
									<option value="2"
										<c:if test="${mergeExamroom.examType == 2 }"> selected="selected" </c:if>>期末考试</option>
							</select></td>
						</tr>
						<tr>
							<td width="12%">转出教学站:</td>
							<td width="38%"><gh:brSchoolAutocomplete
									id="mergeexamroomForm_outBrSchoolName" name="outBranchSchool"
									tabindex="1" defaultValue="${condition['branchSchool']}"
									displayType="code" style="width:55%" /></td>
							<td width="12%">接收教学站:</td>
							<td width="38%"><gh:brSchoolAutocomplete
									id="mergeexamroomForm_inBrSchoolName" name="inBranchSchool"
									tabindex="1" defaultValue="${condition['branchSchool']}"
									displayType="code" style="width:55%" /></td>
						</tr>
						<c:if test="${not empty mergeExamroom }">
							<c:choose>
								<c:when test="${mergeExamroom.examType == 1 }">
									<tr id='recruitPlanTR'>
										<td width='12%''>招生批次</td>
										<td width='38%''><gh:selectModel id='recruitPlan'
												name='recruitPlan' bindValue='resourceid'
												onchange='getExamPlanList(this)'
												displayValue='recruitPlanname'
												modelClass='com.hnjk.edu.recruit.model.RecruitPlan'
												value="${mergeExamroom.recruitPlan.resourceid}"
												orderBy='recruitPlanname desc' choose='Y'
												classCss='required' style='width:50%' /></td>
										<td width='12%''>考试场次</td>
										<td width='38%''><select name='examPlan'
											id='mergeExamRoom_examPlan'
											onchange="getRecruitExamNumStatic()">
												<c:choose>
													<c:when test="${ not empty mergeExamroom.recruitExamPlan }">
														<option
															value='${ mergeExamroom.recruitExamPlan.resourceid }'
															selected="selected">${ mergeExamroom.recruitExamPlan.examplanName}</option>
													</c:when>
													<c:otherwise>
														<option value=''>请选择</option>
													</c:otherwise>
												</c:choose>
										</select></td>
									</tr>
								</c:when>
								<c:when test="${mergeExamroom.examType == 2 }">
									<tr id='examSubTR'>
										<td width='12%''>考试批次</td>
										<td width='88%' ' colspan='3'><gh:selectModel
												name='examSub' bindValue='resourceid'
												displayValue='batchName'
												modelClass='com.hnjk.edu.teaching.model.ExamSub'
												value="${mergeExamroom.examSub.resourceid}"
												orderBy='batchName desc' choose='Y' classCss='required'
												style='width:50%' /></td>
									</tr>
								</c:when>
							</c:choose>
						</c:if>
					</table>
					<table class="table" layouth="161">
						<thead>
							<tr>
								<th width="10%">考试类型</th>
								<th width="20%">考试批次</th>
								<th width="20%">教学站编号</th>
								<th width="20%">教学站名称</th>
								<th width="20%">联系方式</th>
								<th width="10%">考试人数</th>
							</tr>
						</thead>
						<tbody id="examroomStaticBody">
							<tr>
								<td width="10%">-</td>
								<td width="20%">-</td>
								<td width="20%">-</td>
								<td width="20%">-</td>
								<td width="20%">-</td>
								<td width="10%">-</td>
							</tr>
						</tbody>
					</table>
				</div>

				<div class="formBar">
					<ul>
						<li><div class="buttonActive">
								<div class="buttonContent">
									<button type="submit">提交</button>
								</div>
							</div></li>
						<li><div class="button">
								<div class="buttonContent">
									<button type="button" class="close"
										onclick="navTab.closeCurrentTab();">取消</button>
								</div>
							</div></li>
					</ul>
				</div>
			</form>


		</div>
	</div>
	<script type="text/javascript">
	$(document).ready(function(){
		$("#mergeExamRoomForm #examType").change(function(){
			var trHTML   = "";
			var examType = $(this).val();
			$("#mergeExamRoomTable tr[id=recruitPlanTR]").html("");
			$("#mergeExamRoomTable tr[id=examSubTR]").html("");
			if(""==examType){
				alertMsg.warn("请选择一个合并考场的考试类型!");
				clearTable();
				return false;
			}else{
				if("1"==examType){
					trHTML +="<tr id='recruitPlanTR'><td width='12%''>招生批次</td><td width='38%''><gh:selectModel id='recruitPlan' name='recruitPlan' bindValue='resourceid' onchange='getExamPlanList(this)' displayValue='recruitPlanname' modelClass='com.hnjk.edu.recruit.model.RecruitPlan'  orderBy='recruitPlanname desc' choose='Y' classCss='required' style='width:50%'/> </td><td width='12%''>考试场次</td><td width='38%''><select name='examPlan' id='mergeExamRoom_examPlan'  ><option value='''>请选择</option></select></td></tr>";
					
				}else if("2" == examType ){
					trHTML +="<tr id='examSubTR'><td width='12%''>考试批次</td><td width='88%'' colspan='3'><gh:selectModel name='examSub' bindValue='resourceid'  displayValue='batchName' modelClass='com.hnjk.edu.teaching.model.ExamSub'  orderBy='batchName desc' choose='Y' classCss='required' style='width:50%' id ='mergeExamRoom_examPlan2' onchange='getExamNumStatic()' /></td></tr>";
				}
			}
			$("#mergeExamRoomTable").append(trHTML);
			clearTable();
		});
		//useless
		$("#mergeexamroomForm_outBrSchoolName #mergeexamroomForm_inBrSchoolName").change(function(){
			if("1"==examType){
				getRecruitExamNumStatic();
			}else if("2" == examType ){
				getExamNumStatic();
			}
		});
		
	})
	
	//获取选择招生批次对应的考试场次
	function getExamPlanList(obj){
		var recruitPlanId = $(obj).val();
		var url       	  = "${baseUrl}/edu3/recruit/entranceexamprint/examcard-getexamplan.html?recruitPlan="+recruitPlanId;
		$.ajax({
				type:"post",
				url:url,
				dataType:"json",
				success:function(data){
					var appendHTML = "<option value=''>请选择</option>";
					for(var i=0 ;i<data.length;i++){
						appendHTML +="<option value='"+data[i].key+"'>"+data[i].value+"</option>"
					}
					$("#mergeExamRoom_examPlan").html(appendHTML);
					clearTable();
				}
		});
		getRecruitExamNumStatic(recruitPlanId);
	}
	//清空表格
	function clearTable(){
		var clear = '<tr><td width="10%">-</td><td width="20%">-</td><td width="20%">-</td><td width="20%">-</td><td width="20%">-</td><td width="10%">-</td></tr>';
		$("#examroomStaticBody").html(clear);
	}
	//获取不同招生批次下各教学站的入学考试的人数统计
	function getRecruitExamNumStatic(recruitPlanId){
		var srcSchool = document.getElementById('mergeexamroomForm_outBrSchoolName').value;
		var desSchool = document.getElementById('mergeexamroomForm_inBrSchoolName').value ;
		var url       	  = "${baseUrl}/edu3/teaching/mergeExamRoom/statExamNumStudent.html?examTypeId=1&recruitPlanId="+recruitPlanId+"&srcSchool="+srcSchool+"&desSchool="+desSchool;
		$.ajax({
				type:"post",
				url:url,
				dataType:"json",
				success:function(list){
					var appendHTML1 ='';
					for(var i=0 ;i<list.length;i++){
						  if(srcSchool==list[i]['RESOURCEID']){
							  appendHTML1+='<tr class=\"red\" id=\">'+list[i]['RESOURCEID']+'\">';
						  }else if(desSchool==list[i]['RESOURCEID']){
							  appendHTML1+='<tr class=\"blue\"  id=\">'+list[i]['RESOURCEID']+'\">';
						  }else{
							  appendHTML1+='<tr id=\">'+list[i]['RESOURCEID']+'\">';
						  }
						  appendHTML1+='<td width="10%">入学考试</td>';
						  if(null==list[i]['EXAMPLANNAME']){
						  	appendHTML1+='<td width="20%">'+'-'+'</td>';
						  }else{
							appendHTML1+='<td width="20%">'+list[i]['EXAMPLANNAME']+'</td>'; 
						  } 
						  appendHTML1+='<td width="20%">'+list[i]['UNITCODE']+'</td>';
						  appendHTML1+='<td width="20%">'+list[i]['UNITNAME']+'</td>';
						  if(null==list[i]['CONTECTCALL']){
						    appendHTML1+='<td width="20%">'+'-'+'</td>';
						  }else{
							appendHTML1+='<td width="20%">'+list[i]['CONTECTCALL']+'</td>'; 
						  }
						  appendHTML1+='<td width="10%">'+list[i]['COUNT(I.RESOURCEID)']+'</td>';
						  appendHTML1+='</tr>';	
					}
					$("#examroomStaticBody").html(appendHTML1);
				}
		});
	}
	//获取不同考试批次下各教学站的期末考试的人数统计
	function getExamNumStatic(){
		var srcSchool = document.getElementById('mergeexamroomForm_outBrSchoolName').value ;
		var desSchool = document.getElementById('mergeexamroomForm_inBrSchoolName').value ;
		var selector2 = document.getElementById('mergeExamRoom_examPlan2');
		var examPlanId = selector2.options[selector2.selectedIndex].value;
		var url       	  = "${baseUrl}/edu3/teaching/mergeExamRoom/statExamNumStudent.html?examTypeId=2&examPlanId="+examPlanId+"&srcSchool="+srcSchool+"&desSchool="+desSchool;
		$.ajax({
				type:"post",
				url:url,
				dataType:"json",
				success:function(list){
					  var appendHTML1 ='';
					  for(var i=0 ;i<list.length;i++){
					    if(srcSchool==list[i]['RESOURCEID']){
					      appendHTML1+='<tr class=\"red\" id=\">'+list[i]['RESOURCEID']+'\">';
					    }else if(desSchool==list[i]['RESOURCEID']){
					      appendHTML1+='<tr class=\"blue\"  id=\">'+list[i]['RESOURCEID']+'\">';
					    }else{
					      appendHTML1+='<tr id=\">'+list[i]['RESOURCEID']+'\">';
					    }	
					    appendHTML1+='<td width="10%">期末考试</td>';
					    appendHTML1+='<td width="20%">'+$('#mergeExamRoom_examPlan2').find("option:selected").text()+'</td>';
					    appendHTML1+='<td width="20%">'+list[i]['UNITCODE']+'</td>';
					    appendHTML1+='<td width="20%">'+list[i]['UNITNAME']+'</td>';
					    if(null==list[i]['CONTECTCALL']){
						  appendHTML1+='<td width="20%">'+'-'+'</td>';
					    }else{
						  appendHTML1+='<td width="20%">'+list[i]['CONTECTCALL']+'</td>';
					    }
					    appendHTML1+='<td width="10%">'+list[i]['COUNT(I.RESOURCEID)']+'</td>';
					    appendHTML1+='</tr>'	;
					}
					$("#examroomStaticBody").html(appendHTML1);
				}
		});
	}
	function checkBrschool(obj){
		var resourceid  = "${mergeExamroom.resourceid }";
		var inBrschool  = $("#mergeExamRoomForm #mergeexamroomForm_inBrSchoolName").val();
		var outBrschool = $("#mergeExamRoomForm #mergeexamroomForm_outBrSchoolName").val();
		if(null== inBrschool || ""==inBrschool){
			alertMsg.warn("请选择一个接收的教学站!")
			return false;
		}
		if(null== outBrschool || ""==outBrschool){
			alertMsg.warn("请选择一个转出的教学站!")
			return false;
		}
		if(outBrschool==inBrschool){
			alertMsg.warn("转出教学站不能与接收教学站一样!")
			return false;
		}
		if(null ==resourceid || ""==resourceid){
			alertMsg.confirm("座位安排后就不允许修改、删除考场合并记录，确认要新增当前条件当的考场合并记录吗？", {
				okCall: function(){
					return validateCallback(obj);
				}	
			})	
		}else{
			return validateCallback(obj);
		}
		return false;
	}
</script>
</body>
</html>

