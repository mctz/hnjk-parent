<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>年级教学计划</title>
<script type="text/javascript">
$(document).ready(function(){
	teachingplanQueryBegin();
});

//打开页面或者点击查询（即加载页面执行）
function teachingplanQueryBegin() {
	//var gradeId = "${condition['gradeId']}";
	var classicId = "${classicid}";
	var teachingType = "${schoolType}";
	var majorId = "${majorid}";
	var selectIdsJson = "{classicId:'teachingplan_classic',teachingType:'teachingplan_teachingType',majorId:'teachingplan_major'}";
	cascadeQuery("begin", "", "", "", classicId,teachingType, majorId, "", selectIdsJson);
}

//选择层次
function teachingplanQueryClassic() {
	//var gradeId = $("#teachingplan_grade").val();
	var classicId = $("#teachingplan_classic").val();
	var selectIdsJson = "{teachingType:'teachingplan_teachingType',majorId:'teachingplan_major'}";
	cascadeQuery("classic", defaultValue, "", "", classicId, "", "", "", selectIdsJson);
}

//选择学习形式
function teachingplanQueryTeachingType() {
	//var gradeId = $("#teachinggrade_stuGrade").val();
	var classicId = $("#teachingplan_classic").val();
	var teachingType = $("#teachingplan_teachingType").val();
	var selectIdsJson = "{majorId:'teachingplan_major'}";
	cascadeQuery("teachingType", "", "", "", classicId,teachingType, "", "", selectIdsJson);
}

function checkBrother(plan){
	$(plan).siblings().attr("checked",$(plan).attr("checked"));
}

//浏览基础教学计划
function viewTeachplan(planId){
	$.pdialog.open(baseUrl+'/edu3/teaching/teachingplan/edit.html?resourceid='+planId+'&act=view',
					'selector',
					'查看基础教学计划',
					{width:800,height:600}
					);
}

function saveGuiplan(){
	var gradeid = $("#teachinggrade_gradeid").val();
	if(gradeid==''){
		alertMsg.warn("请选择年级！");
		return;
	}
	if(isChecked('teachingPlanId','#_planlistBody')){
		
		$.ajax({
			type:"post",
			url:"${baseUrl}/edu3/teaching/teachinggrade/save.html",
			data: $("#planlistForm").serialize(),
			dataType:"json",
			cache: false,
			error: DWZ.ajaxError,
			success:function(data){
				if(data.statusCode==200){
					alertMsg.confirm(data.message);
				} else if(data.statusCode==400) {
					alertMsg.info(data.message);
				} else {
					alertMsg.error(data.message);
				}
			}
		});
	}else{
		alertMsg.warn("请选择教学计划！");
		return;
	}
	
}
</script>
</head>
<body>
	<!-- <h2 class="contentTitle">编辑年级教学计划</h2> -->
	<div class="page">
		<div class="pageHeader">

			<form method="post"
				action="${baseUrl}/edu3/teaching/teachinggrade/edit.html"
				class="pageForm" id="planlistForm"
				onsubmit="return navTabSearch(this);">
				<!-- <input type="hidden" name="resourceid" value="${teachingGuidePlan.resourceid }"/>  -->
				<div class="searchBar">
					<ul class="searchContent">
						<li><label>层次：</label> <span sel-id="teachingplan_classic"
							sel-name="classicid" sel-onchange="teachingplanQueryClassic()"
							sel-style="width: 120px"></span></li>
						<li><label>教学模式：</label> <span
							sel-id="teachingplan_teachingType" sel-name="teachingType"
							sel-onchange="teachingplanQueryTeachingType"
							dictionaryCode="CodeTeachingType" sel-style="width: 120px"></span>
						</li>
						<li><label>专业：</label> <span sel-id="teachingplan_major"
							sel-name="majorid" sel-classs="flexselect"
							sel-style="width: 120px"></span></li>
						<li><label>版本：</label><input type="text" name="version"
							value="${version}" style="width: 120px;" /></li>
					</ul>
					<div class="subBar">
						<ul style="margin-top: 20px; margin-right: 30px">
							<li><div class="buttonActive">
									<div class="buttonContent">
										<button type="submit">查 询</button>
									</div>
								</div></li>
						</ul>
					</div>
				</div>

				<div class="pageFormContent" layoutH="120">
					<table class="form">
						<tr>
							<td width="15%">年级:</td>
							<td width="85%"><gh:selectModel id="teachinggrade_gradeid"
									name="gradeid" bindValue="resourceid" displayValue="gradeName"
									modelClass="com.hnjk.edu.basedata.model.Grade"
									value="${gradeid}" orderBy="yearInfo.firstYear desc" /></td>
							<!-- 
							<td width="20%">是否发布:</td>
							<td width="30%">
								<gh:select name="ispublished" dictionaryCode="yesOrNo" value="${teachingGuidePlan.ispublished}" classCss="required" style="width:50%"/>
							</td>
							 -->
						</tr>
						<tr>
							<td>教学计划:</td>
							<td>
								<table class="list" width="100%" id="_table_planlist">
									<tr>
										<th width="5%">
										<input type="checkbox" name="checkall" id="check_all_planlist"
											onclick="checkboxAll('#check_all_planlist','teachingPlanId','#_planlistBody')" />
										</th>
										<th width="5%">版本</th>
										<th width="15%">层次</th>
										<th width="20%">专业</th>
										<th width="40%">教学计划名称</th>
										<th width="15%">教学模式</th>
									</tr>
									<tbody id="_planlistBody">
										<c:forEach items="${planList }" var="plan" varStatus="vs">
											<tr>
												<td><input type="checkbox" name="teachingPlanId"
													value="${plan.resourceid }"
													<c:forEach items="${selPlanList }" var="selplan">
									 				  <c:if test="${selplan.teachingPlan.resourceid eq plan.resourceid}">checked</c:if>
													 </c:forEach>
													onclick="checkBrother(this)" /> <input type="checkbox"
													name="majorId" value="${plan.majorId }"
													style="display: none"
													<c:forEach items="${selPlanList }" var="selplan">
									 				  <c:if test="${selplan.teachingPlan.resourceid eq plan.resourceid}">checked</c:if>
													 </c:forEach> />
													<input type="checkbox" name="classicId"
													value="${plan.classicId }" style="display: none"
													<c:forEach items="${selPlanList }" var="selplan">
									 				  <c:if test="${selplan.teachingPlan.resourceid eq plan.resourceid}">checked</c:if>
													 </c:forEach> />
													<input type="checkbox" name="schoolType"
													value="${plan.schoolType }" style="display: none"
													<c:forEach items="${selPlanList }" var="selplan">
									 				  <c:if test="${selplan.teachingPlan.resourceid eq plan.resourceid}">checked</c:if>
													 </c:forEach> />
												</td>
												<td>${plan.versionNum}</td>
												<td>${plan.classic.classicName }</td>
												<td>${plan.major.majorName }</td>
												<td onclick="viewTeachplan('${plan.resourceid }');"
													style="cursor: pointer; font-weight: bolder;"
													title="查看教学计划"><c:choose>
														<c:when test="${not empty plan.planName }">
									            			${plan.planName }
									            		</c:when>
														<c:when test="${not empty plan.unitShortName }">
									            			${plan.majorName } - ${plan.classicName } (${plan.unitShortName})
									            		</c:when>
														<c:otherwise>
									            			${plan.majorName } - ${plan.classicName } (${plan.versionNum})
									            		</c:otherwise>
													</c:choose></td>
												<td>${ghfn:dictCode2Val('CodeTeachingType',plan.schoolType) }</td>
											</tr>
										</c:forEach>
									</tbody>
								</table>
							</td>
						</tr>
					</table>
				</div>
				<div class="formBar">
					<ul>
						<li><div class="buttonActive">
								<div class="buttonContent">
									<button onclick="saveGuiplan()">提交</button>
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
</body>
</html>