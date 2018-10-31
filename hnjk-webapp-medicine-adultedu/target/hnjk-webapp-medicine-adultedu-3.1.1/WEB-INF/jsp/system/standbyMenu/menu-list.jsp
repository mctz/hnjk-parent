<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>系统自定义功能列表</title>
<script type="text/javascript">
	$(document).ready(function() {
		authorityFilter();
	});
	//权限认证
	function authorityFilter() {
		var confirmPassword = "${confirmPassword}";
		var messageStr = "请输入使用此功能的认证密码";
		var defaultStr = "";
		//var content = window.prompt(messageStr, defaultStr);
		var content = '';
		alertMsg
				.confirm(
						"请输入使用此功能的认证密码<br><br> <input type='password' id='confirmPasswordId' />",
						{
							okCall : function() {
								content = $("#confirmPasswordId").val();
								if (content === confirmPassword) {
									//$("#confirmPassword").val(content);
									standbyMenuQueryBegin();
									return true;
								} else {
									alertMsg.error("密码错误！");
									navTab.closeCurrentTab();
									return false;
								}
							},
							cancelCall : function() {
								navTab.closeCurrentTab();
							}
						});

	}
	//打开页面或者点击查询（即加载页面执行）
	function standbyMenuQueryBegin() {
		var defaultValue = "${condition['branchSchool']}";
		var schoolId = "${linkageQuerySchoolId}";
		var gradeId = "${condition['grade']}";
		var classicId = "${condition['classic']}";
		var majorId = "${condition['major']}";
		var classesId = "${condition['classes']}";
		var selectIdsJson = "{unitId:'menu_branchSchool',gradeId:'menu_grade',classicId:'menu_classic',majorId:'menu_major',classesId:'menu_classes'}";
		cascadeQuery("begin", defaultValue, schoolId, gradeId, classicId, "",
				majorId, classesId, selectIdsJson);
	}
</script>
</head>
<body>
	<div class="page">
		<div class="pageHeader">
			<form onsubmit="return navTabSearch(this);" id="standbyMenuForm"
				action="${baseUrl }/edu3/system/menu/list.html" method="post">
				<!-- <input id="confirmPassword" name="confirmPassword" type="hidden" > -->
				<div class="searchBar">
					<ul class="searchContent">
						<li style="width: 360px;"><label>教学站：</label> <span
							sel-id="menu_branchSchool" sel-name="branchSchool"
							sel-onchange="standbyMenuQueryUnit()" sel-classs="flexselect"></span></li>
						<%-- <c:if test="${isBrschool}">
							<input type="hidden" name="branchSchool" id="menu_brSchoolName"
								value="${condition['branchSchool']}" />
						</c:if> --%>
						<li><label>年级：</label> <span sel-id="menu_grade"
							sel-name="grade" sel-onchange="standbyMenuQueryGrade()"
							sel-style="width: 120px"></span></li>
						<li><label>层次：</label> <span sel-id="menu_classic"
							sel-name="classic" sel-onchange="standbyMenuQueryClassic()"
							sel-style="width: 120px"></span></li>

					</ul>
					<ul class="searchContent">
						<li style="width: 360px;"><label>专业：</label> <span
							sel-id="menu_major" sel-name="major"
							sel-onchange="standbyMenuQueryMajor()" sel-classs="flexselect"></span></li>

						<%-- <li><label>姓名：</label><input type="text" name="name"
							id="menu_name" value="${condition['name']}" style="width: 120px" /></li> --%>
						<li><label>学号：</label><input type="text" name="studyNo"
							id="menu_studyNo" value="${condition['studyNo']}"
							style="width: 120px" /></li>
						<li><label>网络课程：</label> <gh:courseAutocomplete
								name="networkCourseid" tabindex="1" id="networkCourseid"
								value="${condition['networkCourseid']}" displayType="code"
								isFilterTeacher="Y" hasResource="Y" style="width:130px" /></li>
						<li><label>面授课程：</label> <gh:courseAutocomplete
								name="faceCourseid" tabindex="1" id="faceCourseid"
								value="${condition['faceCourseid']}" displayType="code"
								isFilterTeacher="Y" hasResource="N" style="width:130px" /></li>

					</ul>
					<ul class="searchContent">
						<li style="width: 360px;"><label>班级：</label> <span
							sel-id="menu_classes" sel-name="classes" sel-classs="flexselect"></span></li>
						<li><label>考试批次：</label>
						<gh:selectModel id="menu_examSub" name="examSub"
								bindValue="resourceid" displayValue="batchName"
								style="width:130px"
								modelClass="com.hnjk.edu.teaching.model.ExamSub"
								value="${condition['examSub']}" /></li>
						<li><label>日期：</label> <input type="text" id="menu_date"
							name="date" style="width: 130px"
							value='<fmt:formatDate value="${condition['date'] }" pattern="yyyy-MM-dd"/>'
							onclick="WdatePicker({isShowWeek:true,dateFmt:'yyyy-MM-dd'})" />
						</li>
						<div class="buttonActive">
							<div class="buttonContent">
								<button type="submit">刷 新</button>
							</div>
						</div>
					</ul>
				</div>
			</form>
		</div>
		<div class="pageContent">
			<gh:resAuth parentCode="RES_SYS_STANDBY_MENU" pageType="list"></gh:resAuth>
			<div style="font-size: large; text-align: center; font-weight: bold;">
				<br>说明<br>
			</div>
			<div style="font-size: 14px; padding-left: 20px;">
				<label><br>导入二维表成绩：该功能用于后台导入未经处理的学生成绩表。<br></label>
				<label><br>学生登录（403）：一键修复学生、老师角色登录报403错误！启用在学学生账号；更改休学学生学籍状态为休学；恢复毕业生数据（之前系统bug产生的数据）。<br></label>
				<label><br>删除补考名单：一键删除所有成绩及格（大于或等于60分）或成绩已删除的补考名单。<br></label>
				<label><br>导入sql文件：页面导入的sql文件直接在后台执行，执行失败会回滚！通过数据字典【CodeSqlOperateType】设置操作权限。<br></label>
				<label style="padding-left: 75px;"><br>如果是insert语句，会判断是否已经执行过，如果已经执行自动转换为update语句！<br></label>
				<label><br>更新成绩显示：一键关联成绩表的【专业课程】；一键关联学习计划表的【成绩】；重置[查询条件]下所有成绩【学时】；重新关联[查询条件]下所有正考成绩【考试批次】和【考试信息】。<br></label>
				<label><br>重新加密成绩：后台使用sql函数加密的字符成绩在页面会报错，重新加密成绩可以一键修复这些数据。<br></label>
				<label><br>拷贝课程资源：【网络课程】和【面授课程】查询条件为必选，该功能可以将选择的网络课程的课程资源拷贝至选择的面授课程。<br></label>
				<label><br>删除成绩：该功能可以删除[查询条件]下所有的学生成绩和补考名单。删除成绩后，建议使用“立即更新学习计划成绩”来重新关联学习计划表和成绩表。<br></label>
				<label><br>撤销【删除成绩】：选择日期后恢复【删除成绩】所删除的成绩。恢复成绩后，建议使用“立即更新学习计划成绩”来重新关联学习计划表和成绩表。<br></label>
				<label><br>成绩去重：一键删除学生重复成绩（判断条件：相同课程，相同考试批次，相同综合成绩），并且优先删除未关联学习计划的成绩。<br></label>
				<label><br>执行SQL语句：执行原生的SQL查询语句（只允许查询数据及导出Excel）。<br></label>
				<label><br>分配专业：更改学生专业。可选择教学计划（自动创建年级教学计划，需要手动开课），分配班级，以及是否更改录取专业。<br></label>
				<label><br>重新计算综合成绩：重新计算所查询学生的综合成绩（成绩状态为已提交或已发布）。<br></label>
			</div>
			<!-- <table class="table" layouth="135">
				<tr><td><td></tr>
			</table> -->
		</div>
	</div>
</body>

<script type="text/javascript">
	// 选择教学点
	function standbyMenuQueryUnit() {
		var defaultValue = $("#menu_branchSchool").val();
		var selectIdsJson = "{gradeId:'menu_grade',classicId:'menu_classic',majorId:'menu_major',classesId:'menu_classes'}";
		cascadeQuery("unit", defaultValue, "", "", "", "", "", "",
				selectIdsJson);
	}

	// 选择年级
	function standbyMenuQueryGrade() {
		var defaultValue = $("#menu_branchSchool").val();
		var gradeId = $("#menu_grade").val();
		var selectIdsJson = "{classicId:'menu_classic',majorId:'menu_major',classesId:'menu_classes'}";
		cascadeQuery("grade", defaultValue, "", gradeId, "", "", "", "",
				selectIdsJson);
	}

	// 选择层次
	function standbyMenuQueryClassic() {
		var defaultValue = $("#menu_branchSchool").val();
		var gradeId = $("#menu_grade").val();
		var classicId = $("#menu_classic").val();
		var selectIdsJson = "{majorId:'menu_major',classesId:'menu_classes'}";
		cascadeQuery("classic", defaultValue, "", gradeId, classicId, "", "",
				"", selectIdsJson);
	}

	//选择专业
	function standbyMenuQueryMajor() {
		var defaultValue = $("#menu_branchSchool").val();
		var gradeId = $("#menu_grade").val();
		var classicId = $("#menu_classic").val();
		var majorId = $("#menu_major").val();
		var selectIdsJson = "{classesId:'menu_classes'}";
		cascadeQuery("major", defaultValue, "", gradeId, classicId, "",
				majorId, "", selectIdsJson);
	}
	//导入二维成绩表
	function importExamResult4Bivariate() {
		$.pdialog.open(baseUrl
				+ "/edu3/teaching/bivariateExamResult/inputScore.html",
				'导入二维成绩表', {
					width : 600,
					height : 360
				});
	}
	//修复学生登录报403
	function studentLogin403() {
		$.ajax({
			type : "post",
			url : baseUrl + "/edu3/system/standby/studentLogin.html",
			data : {},
			dataType : "json",
			success : function(data) {
				if (data.statusCode == 200) {
					alertMsg.correct(data.message);
				} else {
					alertMsg.warn(data.message);
				}
			}
		});
	}
	//删除无效补考名单
	function deleteMakeupList() {
		if (checkAuthority()) {
			$.ajax({
				type : "post",
				url : baseUrl + "/edu3/system/standby/deleteMakeupList.html",
				data : {},
				dataType : "json",
				success : function(data) {
					if (data.statusCode == 200) {
						alertMsg.correct(data.message);
					} else {
						alertMsg.warn(data.message);
					}
				}
			});
		}
	}
	//导入sql文件（用于恢复和修改数据）
	function importSql() {
		if (checkAuthority()) {
			$.pdialog.open(baseUrl + "/edu3/system/standby/inputSql.html",
					'导入sql文件', {
						width : 600,
						height : 360
					});
		}
	}

	//更新成绩显示（关联教学计划课程和学习计划）
	function updateExamShow() {
		if (checkAuthority()) {
			var courseid = "";
			var branchSchool = $("#menu_branchSchool").val();
			var grade = $("#menu_grade").val();
			var classic = $("#menu_classic").val();
			var major = $("#menu_major").val();
			var classes = $("#menu_classes").val();

			var studyNo = $("#menu_studyNo").val();
			var examSub = $("#menu_examSub").val();
			var faceCourseid = $("#faceCourseid").val();
			var networkCourseid = $("#networkCourseid").val();
			if (faceCourseid != "" && networkCourseid != "") {
				courseid = faceCourseid + "','" + networkCourseid;
			} else if (faceCourseid != "") {
				courseid = faceCourseid;
			} else if (networkCourseid != "") {
				courseid = networkCourseid;
			}
			$.ajax({
				type : "post",
				url : baseUrl + "/edu3/system/standby/updateExamShow.html",
				data : {
					branchSchool : branchSchool,
					grade : grade,
					classic : classic,
					major : major,
					classes : classes,
					studyNo : studyNo,
					examSub : examSub,
					courseid : courseid
				},
				dataType : "json",
				success : function(data) {
					if (data.statusCode == 200) {
						alertMsg.correct(data.message);
					} else {
						alertMsg.warn(data.message);
					}
				}
			});
		}
	}

	//重新加密成绩
	function encryptScore() {
		if (checkAuthority()) {
			$.ajax({
				type : "post",
				url : baseUrl + "/edu3/system/standby/encryptScore.html",
				data : {},
				dataType : "json",
				success : function(data) {
					if (data.statusCode == 200) {
						alertMsg.correct(data.message);
					} else {
						alertMsg.warn(data.message);
					}
				}
			});
		}
	}
	//拷贝课程资源
	function copyOnlineCourse() {
		if (checkAuthority()) {
			var faceCourseid = $("#faceCourseid").val();
			var networkCourseid = $("#networkCourseid").val();
			if (networkCourseid == "") {
				alertMsg.warn("请在【网络课程】查询条件选项栏，选择在线课程！");
				return false;
			}
			if (faceCourseid == "") {
				alertMsg.warn("请在【面授课程】查询条件选项栏，选择面授课程！");
				return false;
			}
			$.ajax({
				type : "post",
				url : baseUrl + "/edu3/system/standby/copyOnlineCourse.html",
				data : {
					networkCourseid : networkCourseid,
					faceCourseid : faceCourseid
				},
				dataType : "json",
				success : function(data) {
					if (data.statusCode == 200) {
						alertMsg.correct(data.message);
						navTab.reload("${baseUrl}/edu3/system/menu/list.html",
								$("#standbyMenuForm").serializeArray(),
								"RES_SYS_STANDBY_MENU");
					} else {
						alertMsg.warn(data.message);
					}
				}
			});
		}
	}
	//删除成绩
	function deleteExamResults() {
		if (checkAuthority()) {
			var msg = "是否要删除查询条件下";
			var param = "";
			var courseid = "";
			var branchSchool = $("#menu_branchSchool").val();
			var grade = $("#menu_grade").val();
			var classic = $("#menu_classic").val();
			var major = $("#menu_major").val();
			var classes = $("#menu_classes").val();

			var studyNo = $("#menu_studyNo").val();
			var examSub = $("#menu_examSub").val();
			var faceCourseid = $("#faceCourseid").val();
			var networkCourseid = $("#networkCourseid").val();
			if ((branchSchool == "" || grade == "") && studyNo == "") {
				alertMsg.warn("请选择教学点、年级！");
				return false;
			}
			if (networkCourseid == "" && faceCourseid == "") {
				alertMsg.warn("请选择一门课程！");
				return false;
			} else if (networkCourseid != "" && faceCourseid != "") {
				alertMsg.warn("只能选择一门课程！");
				return false;
			}
			if (faceCourseid != "") {
				courseid = faceCourseid;
			} else {
				courseid = networkCourseid;
			}
			if (classes == "" && studyNo == "") {
				msg += "【<lable style='color: red;'>所有班级</lable>】，"
			} else if (studyNo != "") {
				msg += "【<lable style='color: red;'>学号：" + studyNo
						+ "</lable>】，";
			} else {
				msg += "该班级，";
			}
			msg += "该门课程"
			if (examSub == "") {
				msg += "【<lable style='color: red;'>所有考试批次</lable>】的成绩？";
			} else {
				msg += "，该考试批次的成绩？";
			}
			alertMsg
					.confirm(
							msg,
							{
								okCall : function() {
									$
											.ajax({
												type : "post",
												url : baseUrl
														+ "/edu3/system/standby/deleteExamResults.html",
												data : {
													branchSchool : branchSchool,
													grade : grade,
													classic : classic,
													major : major,
													classes : classes,
													studyNo : studyNo,
													examSub : examSub,
													courseid : courseid
												},
												dataType : "json",
												success : function(data) {
													if (data.statusCode == 200) {
														alertMsg
																.confirm(
																		data.message
																				+ "<br>是否立即更新学习计划成绩？",
																		{
																			okCall : function() {
																				updateExamShow();
																			}
																		});
													} else {
														alertMsg
																.warn(data.message);
													}
												}
											});
								}
							});
		}
	}
	//撤销删除成绩
	function OOPS() {
		if (checkAuthority()) {
			var courseid = "";
			var branchSchool = $("#menu_branchSchool").val();
			var grade = $("#menu_grade").val();
			var classic = $("#menu_classic").val();
			var major = $("#menu_major").val();
			var classes = $("#menu_classes").val();

			var studyNo = $("#menu_studyNo").val();
			var examSub = $("#menu_examSub").val();
			var faceCourseid = $("#faceCourseid").val();
			var networkCourseid = $("#networkCourseid").val();
			var date = $("#menu_date").val();
			if (date == "") {
				alertMsg.warn("请选择删除成绩的日期！");
				return false;
			}
			if (networkCourseid == "" && faceCourseid == "") {
				alertMsg.warn("请选择一门课程！");
				return false;
			} else if (networkCourseid != "" && faceCourseid != "") {
				alertMsg.warn("只能选择一门课程！");
				return false;
			}
			if (faceCourseid != "") {
				courseid = faceCourseid;
			} else {
				courseid = networkCourseid;
			}
			alertMsg
					.confirm(
							"是否要恢复【" + date + "】使用【删除成绩】删除的成绩？",
							{
								okCall : function() {
									$
											.ajax({
												type : "post",
												url : baseUrl
														+ "/edu3/system/standby/deleteExamResults.html?opt=OOPS",
												data : {
													branchSchool : branchSchool,
													grade : grade,
													classic : classic,
													major : major,
													classes : classes,
													studyNo : studyNo,
													examSub : examSub,
													courseid : courseid,
													date : date
												},
												dataType : "json",
												success : function(data) {
													if (data.statusCode == 200) {
														alertMsg
																.confirm(
																		data.message
																				+ "<br>是否立即更新学习计划成绩？",
																		{
																			okCall : function() {
																				updateExamShow();
																			}
																		});
													} else {
														alertMsg
																.warn(data.message);
													}
												}
											});
								}
							});
		}
	}
	//删除重复成绩
	function deleteRepeatExam() {
		if (checkAuthority()) {
			$.ajax({
				type : "post",
				url : baseUrl + "/edu3/system/standby/deleteRepeatExam.html",
				data : {},
				dataType : "json",
				success : function(data) {
					if (data.statusCode == 200) {
						alertMsg.correct(data.message);
					} else {
						alertMsg.warn(data.message);
					}
				}
			});
		}
	}

	//执行SQL语句
	function executeSQL() {
		if (checkAuthority()) {
			navTab.openTab('navTab_SQL',
					'${baseUrl}/edu3/system/standby/executeSQL.html',
					'执行SQL查询语句');
		}
	}
	//查看sql文件导入记录
	function getSqlFiles() {
		if (checkAuthority()) {
			navTab.openTab("RES_SYS_STANDBY_MENU_SQLFILE", "", "sql执行记录");
		}
	}
	//分配专业
	function RTCW() {
		if (checkAuthority()) {
			var branchSchool = $("#menu_branchSchool").val();
			var grade = $("#menu_grade").val();
			var classic = $("#menu_classic").val();
			var major = $("#menu_major").val();
			var classes = $("#menu_classes").val();
			var studyNo = $("#menu_studyNo").val();
			var param = "";
			if (studyNo == "") {
				if (branchSchool == "" || grade == "" || major == "") {
					alertMsg.warn("请选择教学点、年级、专业！");
					return false;
				}
				param = "branchSchoolId=" + branchSchool + "&gradeId=" + grade
						+ "&classicId=" + classic + "&majorId=" + major;
			} else {
				param = "studyNo=" + studyNo;
			}
			navTab.openTab('RES_SYS_STANDBY_RTCW',
					'${baseUrl}/edu3/system/standby/RTCW-list.html?' + param,
					'分配专业');
		}
	}

	//重新计算综合成绩
	function calculateScore() {
		if (checkAuthority()) {
			var msg = "是否要删除查询条件下";
			var param = "";
			var courseid = "";
			var branchSchool = $("#menu_branchSchool").val();
			var grade = $("#menu_grade").val();
			var classic = $("#menu_classic").val();
			var major = $("#menu_major").val();
			var classes = $("#menu_classes").val();

			var studyNo = $("#menu_studyNo").val();
			var examSub = $("#menu_examSub").val();
			var faceCourseid = $("#faceCourseid").val();
			var networkCourseid = $("#networkCourseid").val();
			if (branchSchool == "" && studyNo == "") {
				alertMsg.warn("请选择教学点！");
				return false;
			}
			if (faceCourseid != "") {
				courseid = faceCourseid;
			} else {
				courseid = networkCourseid;
			}
			if (classes == "" && studyNo == "") {
				msg += "【<lable style='color: red;'>所有班级</lable>】，"
			} else if (studyNo != "") {
				msg += "【<lable style='color: red;'>学号：" + studyNo
						+ "</lable>】，";
			} else {
				msg += "该班级，";
			}
			if (faceCourseid = "") {
				msg += "【<lable style='color: red;'>所有课程</lable>】，";
			} else {
				msg += "该门课程";
			}
			if (examSub == "") {
				msg += "【<lable style='color: red;'>所有考试批次</lable>】的成绩？";
			} else {
				msg += "，该考试批次的成绩？";
			}
			alertMsg.confirm(msg, {
				okCall : function() {
					$.ajax({
						type : "post",
						url : baseUrl
								+ "/edu3/system/standby/calculateScore.html",
						data : {
							branchSchool : branchSchool,
							grade : grade,
							classic : classic,
							major : major,
							classes : classes,
							studyNo : studyNo,
							examSub : examSub,
							courseid : courseid
						},
						dataType : "json",
						success : function(data) {
							if (data.statusCode == 200) {
								alertMsg.correct(data.message);
							} else {
								alertMsg.warn(data.message);
							}
						}
					});
				}
			});
		}
	}

	//用户验证
	function checkAuthority() {
		var username = "${username}";
		if (username == "administrator" || username == "msl") {
			return true;
		} else {
			alertMsg.warn("抱歉，当前用户(" + username + ")不是超级管理员，没有该操作权限！");
			return false;
		}
	}
</script>
</html>