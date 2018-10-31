<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>学生作业提交情况</title>
<script type="text/javascript">
	function remindStudent(){
		if(!isChecked("resourceid","#check_all_studentExerciseStateBody")){
 			alertMsg.warn('请选择一条要操作记录！');
			return false;
	 	}
		alertMsg.confirm("您确定要给这些学生发温馨提示?", {
				okCall: function(){		
					var res = "";
					var name = "";
					var num  = $("#check_all_studentExerciseStateBody input[name='resourceid']:checked").size();
					$("#check_all_studentExerciseStateBody input[name='resourceid']:checked").each(function(){
	                        res+=$(this).val()+",";
	                        name+=$(this).attr('rel')+",";
	                 });
	                
					$.pdialog.open('${baseUrl }/edu3/framework/message/dailog.html?type=batch&msgType=tips&userNames='+res+'&cnNames='+encodeURIComponent(name),'selector','温馨提示(作业)',{mask:true,width:750,height:550});
				}
		});	
	}
	function studentexerciseReload(currentIndex){
		$.pdialog.reload("${baseUrl }/edu3/framework/exercise/student/view.html?exerciseBatchId=${condition['exerciseBatchId'] }&currentIndex="+currentIndex);
	}
</script>
</head>
<body>
	<div class="page">
		<div class="tabs" currentIndex="${condition['currentIndex'] }"
			eventtype="click">
			<div class="tabsHeader">
				<div class="tabsHeaderContent">
					<ul>
						<li><a href="javascript:void(0)"
							onclick="studentexerciseReload(0)"><span>已提交学生</span></a></li>
						<li><a href="javascript:void(0)"
							onclick="studentexerciseReload(1)"><span>未提交学生</span></a></li>
					</ul>
				</div>
			</div>
			<div class="tabsContent" style="height: 100%;">
				<!-- 1 -->
				<div>
					<div class="">
						<table class="table" layouth="96">
							<thead>
								<tr>
									<th width="5%">&nbsp;</th>
									<th width="15%">姓名</th>
									<th width="10%">性别</th>
									<th width="20%">专业</th>
									<th width="15%">学习中心</th>
									<th width="15%">移动电话</th>
									<th width="20%">Email</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach items="${studentExerciseFinished.result}"
									var="studentExercise" varStatus="vs">
									<tr>
										<td>&nbsp;</td>
										<td>${studentExercise.studentInfo.studentName}</td>
										<td>${ghfn:dictCode2Val('CodeSex',studentExercise.studentInfo.studentBaseInfo.gender)}</td>
										<td>${studentExercise.studentInfo.major.majorName }</td>
										<td>${studentExercise.studentInfo.branchSchool }</td>
										<td>${studentExercise.studentInfo.studentBaseInfo.mobile }</td>
										<td>${studentExercise.studentInfo.studentBaseInfo.email }</td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
						<gh:page page="${studentExerciseFinished}"
							goPageUrl="${baseUrl }/edu3/framework/exercise/student/view.html"
							pageType="sys" targetType="dialog" condition="${condition}" />
					</div>
				</div>
				<!-- 2 -->
				<div>
					<div class="">
						<gh:resAuth parentCode="RES_TEACHING_ESTAB_EXERCISECHECK"
							pageType="dlist"></gh:resAuth>
						<table class="table" layouth="121">
							<thead>
								<tr>
									<th width="5%"><input type="checkbox" name="checkall"
										id="check_all_studentExerciseState"
										onclick="checkboxAll('#check_all_studentExerciseState','resourceid','#check_all_studentExerciseStateBody')" /></th>
									<th width="15%">姓名</th>
									<th width="10%">性别</th>
									<th width="20%">专业</th>
									<th width="15%">学习中心</th>
									<th width="15%">移动电话</th>
									<th width="20%">Email</th>
								</tr>
							</thead>
							<tbody id="check_all_studentExerciseStateBody">
								<c:forEach items="${studentExerciseNotFinished.result}"
									var="plan" varStatus="vs">
									<tr>
										<td><input type="checkbox" name="resourceid"
											value="${plan.studentInfo.sysUser.username }"
											rel="${plan.studentInfo.sysUser.cnName }" autocomplete="off" /></td>
										<td>${plan.studentInfo.studentName}</td>
										<td>${ghfn:dictCode2Val('CodeSex',plan.studentInfo.studentBaseInfo.gender)}</td>
										<td>${plan.studentInfo.major.majorName }</td>
										<td>${plan.studentInfo.branchSchool }</td>
										<td>${plan.studentInfo.studentBaseInfo.mobile }</td>
										<td>${plan.studentInfo.studentBaseInfo.email }</td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
						<gh:page page="${studentExerciseNotFinished}"
							goPageUrl="${baseUrl }/edu3/framework/exercise/student/view.html"
							pageType="sys" targetType="dialog" condition="${condition}"
							pageNumShown="3" />
					</div>
				</div>
			</div>
			<div class="tabsFooter">
				<div class="tabsFooterContent"></div>
			</div>
		</div>
	</div>
</body>
</html>