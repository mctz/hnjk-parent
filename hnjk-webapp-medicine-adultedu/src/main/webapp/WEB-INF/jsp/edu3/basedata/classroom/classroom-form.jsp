<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>教室管理</title>
</head>
<body>
	<h2 class="contentTitle">${(empty classroom.resourceid)?'新增':'编辑' }教室</h2>
	<div class="page">
		<div class="pageContent">
			<form method="post"
				action="${baseUrl}/edu3/sysmanager/exclassroom/save.html"
				class="pageForm" onsubmit="return validateCallback(this);">
				<input type="hidden" name="resourceid" value="${classroom.resourceid }" /> 
				<input type="hidden" name="buildingId" value="${classroom.building.resourceid }" />
				<div class="pageFormContent" layoutH="97">
					<table class="form">
						<tr>
							<td width="20%">教学站:</td>
							<td width="30%">
								<c:choose>
									<c:when
										test="${empty classroom.resourceid and not isBrschool }">
										<gh:brSchoolAutocomplete name="brSchoolid" tabindex="1" id="classroom_form_brschoolid" defaultValue=""
											displayType="code" style="width:80%;"></gh:brSchoolAutocomplete>
									</c:when>
									<c:otherwise>
										${classroom.building.branchSchool.unitName }
										<input type="hidden" name="brSchoolid" value="${classroom.building.branchSchool.resourceid }"/>
									</c:otherwise>
								</c:choose>
							</td>
						</tr>
				<tr>
					<td width="20%">教室名称:</td>
					<td width="30%"><input type="text" name="classroomName" style="width:50%" value="${classroom.classroomName }" class="required"/></td>
					<td width="20%">教室类型:</td>
					<td width="30%"><gh:select name="classroomType" value="${classroom.classroomType }" dictionaryCode="CodeClassRoomStyle" style="width:100px;" classCss="required" /><font color="red">*</font></td>
				</tr>
				<tr>
					<td>楼号：</td>
					<td><input type="text" name="layerNo" value="${classroom.layerNo }" class="required number"/></td>
					<td>课室号</td>
					<td><input type="text" name="roomCode" style="width:50px" value="${classroom.roomCode }" class="required"/></td>
				</tr>
				<tr>
					<td>所在房号:</td>
					<td><input type="text" name="unitNo" style="width:50px" value="${classroom.unitNo }" class="required number"/></td>
					<td>座位数:</td>
					<td><input type="text" name="seatNum" style="width:50px" value="${classroom.seatNum }" class="required number"/></td>
				</tr>
				<tr>
					<td>双座位数:</td>
					<td><input type="text" name="doubleSeatNum" style="width:50px" value="${classroom.doubleSeatNum }" class="required number"/></td>
					<td>单座位数:</td>
					<td><input type="text" name="singleSeatNum" style="width:50px" value="${classroom.singleSeatNum }" class="required number"/></td>
				</tr>
				<c:if test="${isUseArrange}">
					<tr>
						<td>排课可用:</td>
						<td><gh:select name="isUseCourse" value="${classroom.isUseCourse }" dictionaryCode="yesOrNo" style="width:100px;" classCss="required"/></td>
						<td>排考可用:</td>
						<td><gh:select name="isUseExam" value="${classroom.isUseExam }" dictionaryCode="yesOrNo" style="width:100px;" classCss="required"/></td>
					</tr>
					<tr>
						<td>可用开始时间：</td>
						<td><input type="text" name="startDate" size="40" style="width:50%" value="<fmt:formatDate value="${classroom.startDate }" pattern="yyyy-MM-dd" />"
								   onFocus="WdatePicker({isShowWeek:true})"/>
						<td>可用结束时间：</td>
						<td><input type="text" name="endDate" size="40" style="width:50%" value="<fmt:formatDate value="${classroom.endDate }" pattern="yyyy-MM-dd" />"
								   onFocus="WdatePicker({isShowWeek:true})"/>
						</td>
					</tr>
					<tr>
						<td>有无空调:</td>
						<td><gh:select name="hasAir" value="${classroom.hasAir }" dictionaryCode="yesOrNo" style="width:100px;" /></td>
						<td>状态:</td>
						<td><gh:select name="status" value="${classroom.status }" dictionaryCode="CodeClassroomStatus" style="width:100px;" /></td>
					</tr>
				</c:if>
				<tr>
					<td>是否直播室:</td>
					<td><gh:select name="isLiving" value="${classroom.isLiving }" dictionaryCode="yesOrNo" style="width:100px;" /></td>
					<td>排序号:</td>
					<td><input type="text" name="showOrder" style="width:50px" value="${classroom.showOrder }"/></td>
				</tr>
				<tr>
					<td>备注:</td>
					<td colspan="3"><textarea name="memo" style="width:50%" cols="" rows="3">${classroom.memo }</textarea></td>
				</tr>
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
	$(function (){
		if($("#classroom_form_brschoolid_flexselect").length>0){
			$("#classroom_form_brschoolid_flexselect").addClass("required");
		}
	});
	</script>
</body>
</html>