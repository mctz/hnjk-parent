<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>考试批次</title>
<script type="text/javascript">

$(document).ready(function(){
	$("#mergeexamroomSearchDIV #examType").change(function(){
		var examType = $(this).val();
		if("1"==examType){
			
			$("#recruitPlanUL select[name='recruitPlan']").attr("disabled","")
			$("#recruitPlanUL").show();
			$("#examSubUL").hide();
			$("#examSubUL select[name='examSub']").attr("disabled","disabled")
			
		}else if("2" == examType ){
			
			$("#examSubUL select[name='examSub']").attr("disabled","")
			$("#examSubUL").show();
			$("#recruitPlanUL").hide();
			$("#recruitPlanUL select[name='recruitPlan']").attr("disabled","disabled")
		}
	});
})
	//新增
	function addMergeExamRoom(){
		navTab.openTab('RES_TEACHING_EXAM_MERGEEXAMROOM_FORM', '${baseUrl}/edu3/teaching/mergeExamRoom/form.html', '新增考场合并');
	}
	
	//修改
	function editMergeExamRoom(){
		var url = "${baseUrl}/edu3/teaching/mergeExamRoom/form.html";
		if(isCheckOnlyone('resourceid','#mergeexamroomBody')){
			navTab.openTab('RES_TEACHING_EXAM_MERGEEXAMROOM_EIDT', url+'?resourceid='+$("#mergeexamroomBody input[@name='resourceid']:checked").val(), '编辑考场合并');
		}			
	}
		
	//删除
	function delMergeExamRoom(){	
		pageBarHandle("您确定要删除选中记录吗？","${baseUrl}/edu3/teaching/mergeExamRoom/del.html","#mergeexamroomBody");
	}
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
					$("#mergeexamroomSearch_examPlan").html(appendHTML);
				}
		});
	}
</script>
</head>
<body>

	<div class="page">
		<div class="pageHeader">
			<form onsubmit="return navTabSearch(this);"
				action="${baseUrl }/edu3/teaching/mergeExamRoom/list.html"
				method="post">
				<div class="searchBar" id="mergeexamroomSearchDIV">
					<ul class="searchContent">
						<li><label>考试类型：</label> <select name="examType"
							id="examType" style='width: 50%'>
								<option value="1"
									<c:if test="${condition['examType'] eq '1' }"> selected="selected" </c:if>>入学考试</option>
								<option value="2"
									<c:if test="${condition['examType'] eq '2' }"> selected="selected" </c:if>>期末考试</option>
						</select></li>
						<li><label>转出的教学站：</label> <gh:brSchoolAutocomplete
								id="mergeexamroom_outBrSchoolName" name="outBranchSchool"
								tabindex="1" defaultValue="${condition['outBranchSchool']}"
								displayType="code" /></li>
						<li><label>转入的教学站：</label> <gh:brSchoolAutocomplete
								id="mergeexamroom_inBrSchoolName" name="inBranchSchool"
								tabindex="1" defaultValue="${condition['inBranchSchool']}"
								displayType="code" /></li>
					</ul>

					<ul class="searchContent" id="recruitPlanUL"
						<c:if test="${not empty condition['examSub'] or condition['examType'] eq '2' }"> style="display: none;" </c:if>>
						<li><label>招生批次:</label> <gh:selectModel name='recruitPlan'
								bindValue='resourceid' onchange='getExamPlanList(this)'
								displayValue='recruitPlanname'
								value="${condition['recruitPlan']}"
								modelClass='com.hnjk.edu.recruit.model.RecruitPlan'
								orderBy='recruitPlanname desc' choose='Y' classCss='required'
								style='width:50%' /></li>
					</ul>

					<ul id="examSubUL" class="searchContent"
						<c:if test="${ empty condition['examType'] or condition['examType'] eq '1' }"> style="display: none;"  </c:if>>
						<li><label>考试批次</label> <gh:selectModel name='examSub'
								bindValue='resourceid' displayValue='batchName'
								value="${condition['examSub']}"
								modelClass='com.hnjk.edu.teaching.model.ExamSub'
								orderBy='batchName desc' choose='Y' classCss='required'
								style='width:50%' /></li>
					</ul>
					<div class="subBar">
						<ul>
							<li><div class="buttonActive">
									<div class="buttonContent">
										<button type="submit">查询</button>
									</div>
								</div></li>
						</ul>
						</ul>
					</div>
			</form>
		</div>
		<div class="pageContent">
			<gh:resAuth parentCode="RES_TEACHING_EXAM_MERGEEXAMROOM"
				pageType="list"></gh:resAuth>
			<table class="table" layouth="161">
				<thead>
					<tr>
						<th width="5%"><input type="checkbox" name="checkall"
							id="check_all_mergeexamroom"
							onclick="checkboxAll('#check_all_mergeexamroom','resourceid','#mergeexamroomBody')" /></th>
						<th width="10%">考试类型</th>
						<th width="30%">转出的教学站</th>
						<th width="30%">接收的教学站</th>
						<th width="25%">考试批次/场次</th>
					</tr>
				</thead>
				<tbody id="mergeexamroomBody">
					<c:forEach items="${page.result}" var="mergeExamRoom"
						varStatus="vs">
						<tr>
							<td><input type="checkbox" name="resourceid"
								value="${mergeExamRoom.resourceid }" autocomplete="off" /></td>
							<td><c:choose>
									<c:when test="${mergeExamRoom.examType ==1 }">入学考试</c:when>
									<c:when test="${mergeExamRoom.examType ==2 }">期末考试</c:when>
								</c:choose></td>
							<td>${mergeExamRoom.outBrSchool.unitName }</td>
							<td>${mergeExamRoom.inBrSchool.unitName}</td>
							<td><c:choose>
									<c:when test="${mergeExamRoom.examType ==1 }">
			            			${mergeExamRoom.recruitPlan.recruitPlanname}
			            			<c:if
											test="${not empty mergeExamRoom.recruitExamPlan.resourceid}">
			            				-----> 考试场次：${mergeExamRoom.recruitExamPlan.examplanName}
			            			</c:if>
									</c:when>
									<c:when test="${mergeExamRoom.examType ==2 }">
			            			${mergeExamRoom.examSub.batchName}
								</c:when>
								</c:choose></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<gh:page page="${page}"
				goPageUrl="${baseUrl }/edu3/teaching/mergeExamRoom/list.html"
				pageType="sys" condition="${condition}" />
		</div>
	</div>

</body>
