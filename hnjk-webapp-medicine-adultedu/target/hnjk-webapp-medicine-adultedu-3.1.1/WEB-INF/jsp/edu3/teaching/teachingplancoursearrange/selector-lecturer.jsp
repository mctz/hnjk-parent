<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<script type="text/javascript">
	
		$(document).ready(function(){
			// 显示已选择的记录
			var selectTeachers = $("#selectedTeachers").val();
			var selectedArray;
			if(selectTeachers){
				selectedArray = selectTeachers.split(",");
			}else {
				selectedArray = new Array();
			}
			 
			$("#teachingPlanCourseStatusBody1 input[name='resourceid']").click(function(){
				if($(this).checked){
					selectedArray.push((this).val());
				}else {
					// 删掉数组中某个字符串
					for(var i=0;i<selectedArray.length;i++){
						if(selectedArray[i]==(this).val()){
							selectedArray.splice(i, 1);
							break;
						}
					}
				}
				$("#selectedTeachers").val(selectedArray.toString());
			});
			
			// 设置任课老师
			$("#setLecturer").click(function(){
				// 处理参数
				var resids = "";
				var unitids = "";
				var guiplanids = "";
				var plancourseids = "";
				var classesids = "";
				var courseids = "";
				var isBatch = $("#isBatch").val();
				if(isBatch=="Y"){
					resids = $("#cs_selector_resIds").val();
					unitids = $("#cs_selector_unitIds").val();
					guiplanids = $("#cs_selector_gpIds").val();
					plancourseids = $("#cs_selector_pcIds").val();
					classesids = $("#cs_selector_classesIds").val();
					courseids = $("#cs_selector_courseIds").val();
				}else {
					resids = $("#selector_resIds").val();
					unitids = $("#selector_unitIds").val();
					guiplanids = $("#selector_gpIds").val();
					plancourseids = $("#selector_pcIds").val();
					classesids = $("#selector_classesIds").val();
					courseids = $("#selector_courseIds").val();
				}
				if(resids == "" || classesids == ""){
					alertMsg.warn("请您关掉弹框，重新设置任课老师！");
					return;
				}
				//var lecturerId = $("#taskSearchBody input[name='resourceid']:checked").val();
				//var setlecturerName = $("#taskSearchBody input[name='resourceid']:checked").attr("rel");
				var lecturerId = $("#lecturerPageContent #pagerForm input[name=selectedTeachers]").val();
				var setlecturerName = $("#lecturerPageContent #pagerForm input[name=selectedNames]").val();
				if(!lecturerId || !setlecturerName){
					alertMsg.warn("请选择老师！");
					return;
				}
				
				$.ajax({
					type:"post",
					url:"${baseUrl}/edu3/teaching/teachingplancoursetimetable/setLecturerSave.html",
					data:{"resids":resids,"unitids":unitids,"guiplanids":guiplanids,"plancourseids":plancourseids,
						    "classesids":classesids,"courseids":courseids,"teacherId":lecturerId,"setteacherName":setlecturerName},
					dataType:"json",
					success:function(data){
							if(data.statusCode == 200){
								if(isBatch == "N"){// 点击某条记录设置任课老师
									var targetObject = $("#teachingPlanCourseStatusBody1 input[value='"+resids+"']:first");
								    var $_tr = targetObject.parent().parent().parent();
 									var $_teacherName = $_tr.find("#setteacherid a"); 
 									if(!$_teacherName.text()){
 										$_teacherName.text(data.teacherName);
 										$_tr.find(".recordScoreTeacherId:first").val(data.teacherId); 
 									}
 									
 									$_tr.find("#setlecturerid a").text(data.lecturerName); 
								} else {// 批量设置登分老师功能
									$("#teachingPlanCourseStatusBody1 input[name='resourceid']:checked").each(function(){
										 var $_batchTr = $(this).parent().parent().parent();
										 $_batchTr.find("#setteacherid a").text(data.setteacherName);
										 $_batchTr.find(".recordScoreTeacherId:first").val(data.teacherId); 
										// $(this).parent().parent().parent().find(".recordScoreTeacher:first").text(data.setteacherName); 
									});
								}
								
								$.pdialog.closeCurrent();
								alertMsg.correct(data.message);
							} else {
								alertMsg.error(data.message);
							}
					}
				});
			});
		});
		
		function allSelectedLecturer(obj){
			var selectTeachers = $("#lecturerPageContent #pagerForm input[name=selectedTeachers]").val();
			var selectedNames = $("#lecturerPageContent #pagerForm input[name=selectedNames]").val();
			var selectedArray = new Array();
			var selectedNamesArray = new Array();
			if(selectTeachers){
				selectedArray = selectTeachers.split(",");
				selectedNamesArray = selectedNames.split(",");
			}
			
			if($(obj).attr("checked")){
				selectedArray.push($(obj).val());
				selectedNamesArray.push($(obj).attr("rel"));
			}else {
				// 删掉数组中某个字符串
				for(var i=0;i<selectedArray.length;i++){
					if(selectedArray[i]==$(obj).val()){
						selectedArray.splice(i, 1);
						selectedNamesArray.splice(i, 1);
						break;
					}
				}
			}
			$("#lecturerPageContent #pagerForm input[name=selectedTeachers]").val(selectedArray.toString());
			$("#lecturerPageContent #pagerForm input[name=selectedNames]").val(selectedNamesArray.toString());
		}
	</script>
</head>

<body>
	<div class="page">
		<div class="pageHeader">
			<div class="pageHeader">
				<form id="taskSearchForm" onsubmit="return dialogSearch(this);"
					action="${baseUrl}/edu3/teaching/teachingplancoursetimetable/lecturer.html"
					method="post">
					<input type="hidden" name="type" value="${condition['type']}" />
					<c:if test="${isBatch ne 'Y' }">
						<input type="hidden" id="selector_resIds" name="resIds"
							value="${condition['resIds']}">
						<input type="hidden" id="selector_gpIds" name="gpIds"
							value="${condition['gpIds']}">
						<input type="hidden" id="selector_pcIds" name="pcIds"
							value="${condition['pcIds']}">
						<input type="hidden" id="selector_unitIds" name="unitIds"
							value="${condition['unitIds']}">
						<input type="hidden" id="selector_classesIds" name="classesIds"
							value="${condition['classesIds']}">
						<input type="hidden" id="selector_courseIds" name="courseIds"
							value="${condition['courseIds']}">
					</c:if>
					<input type="hidden" id="isBatch" name="isBatch" value="${isBatch}">
					<div class="searchBar">
						<ul class="searchContent">
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
											<button type="button" id="setLecturer">确 定</button>
										</div>
									</div></li>
							</ul>
						</div>
					</div>
				</form>
			</div>
			<div class="pageContent" id="lecturerPageContent">
				<table class="table" layouth="168">
					<thead>
						<tr>
							<th width="10%">&nbsp;</th>
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
									value="${t.resourceid }" onclick="allSelectedLecturer(this)"
									rel="${t.cnName }"
									${ghfn:strContains(t.resourceid,condition['selectedTeachers'])?'checked':'' } /></td>
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
					goPageUrl="${baseUrl }/edu3/teaching/teachingplancoursetimetable/lecturer.html"
					pageType="sys" targetType="dialog" condition="${condition}" />
			</div>
		</div>
	</div>
</body>