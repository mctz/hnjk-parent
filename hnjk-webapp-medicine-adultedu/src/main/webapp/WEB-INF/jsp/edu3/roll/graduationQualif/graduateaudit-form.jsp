<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>毕业<c:if test="${isApplyGraduate}">、学位 </c:if>审核管理
</title>
<script type="text/javascript">
	// 跳转页(暂废弃)
	/*
	function pageJump(val){
		var stus = $('#stus').val();
		var graduateDate = $('#graduateDate').val();
		var url = "${baseUrl}/edu3/roll/graduateaudit/auditGraduate.html?stus="+stus+"&pageNum="+val+"&graduateDateL="+graduateDate;
		$.pdialog.open(url, 'RES_SCHOOL_GRADUATION_AUDIT', '毕业审核管理', {width: 800, height: 600});
	}
	*/
	//批量毕业资格审核（新）
	function auditPass(){
		var stus = $('#stus').val();
		var graduateDate = $('#graduateDateInform').val();
		if(""==graduateDate){
			alertMsg.warn("您设定的毕业日期为空,毕业审核操作失败。");
			return false;
		}
		var postUrl=baseUrl+"/edu3/roll/graduateaudit/viaGraduate.html?stus="+stus+"&graduateDate="+graduateDate;
		
		$.ajax({
			type:"post",
			url:postUrl,
			dataType:"json",
			success:function(data){
				alertMsg.warn(data['message']);
				var branchSchool=$('#graduateAudForm #branchSchool').val()==undefined?"":$('#graduateAudForm #branchSchool').val();
				var major		=$('#graduateAudForm #major').val()==undefined?"":$('#graduateAudForm #major').val();
				var classic		=$('#graduateAudForm #classic').val()==undefined?"":$('#graduateAudForm #classic').val();
				var stuStatus	=$('#graduateAudForm #stuStatus').val()==undefined?"":$('#graduateAudForm #stuStatus').val();
				var name		=$('#graduateAudForm #name').val()==undefined?"":$('#graduateAudForm #name').val();
				var matriculateNoticeNo	=$('#graduateAudForm #matriculateNoticeNo').val()==undefined?"":$('#graduateAudForm #matriculateNoticeNo').val();
				var grade 		=$('#graduateAudForm #grade').val()==undefined?"":$('#graduateAudForm #grade').val();
				
				if(data['hasError']==true){
					
				}else{
					//全局设置的毕业时间
					var graduateDateG = $('#graduateAudForm #graduateDate').val();
					//毕业审核名单的页数
					var pNum                 = $('#graduateAudForm #pNum').val();
					var url = "${baseUrl}/edu3/schoolroll/graduate/audit/list.html?branchSchool="+branchSchool+"&major="+major+"&classic="+classic+"&stuStatus="+stuStatus+"&name="+name+"&matriculateNoticeNo="+matriculateNoticeNo+"&grade="+grade+"&graduateDate="+graduateDateG+"&pNum="+pNum+"&isRefresh=1";
					navTab.openTab('RES_SCHOOL_GRADUATION_AUDIT', url, '毕业资格审核');
					$("#graduateStatus").attr("color","blue");
					$("#graduateStatus").html("毕业已审核");
					$("#singleGPass").hide();
				}
				
			}
		});
	}
	//单个毕业资格审核（新）
	function auditSinglePass(){
		var studentId = $('#studentId').val();
		var graduateDate = $('#graduateDateInform').val();
		var graduateStatus = $('#graduateStatus').text();
		if(""==graduateDate){
			alertMsg.warn("您设定的毕业日期为空,毕业审核操作失败。");
			return false;
		}
		if("毕业已审核" == graduateStatus){
			alertMsg.warn("该学生已经通过毕业审核，目前不予进行毕业审核。");
			return false;
		}
		var postUrl=baseUrl+"/edu3/roll/graduateaudit/viaGraduateBySingle.html?studentId="+studentId+"&graduateDate="+graduateDate;
		$.ajax({
			type:"post",
			url:postUrl,
			dataType:"json",
			success:function(data){
				alertMsg.warn(data['message']);
				var branchSchool=$('#graduateAudForm #branchSchool').val()==undefined?"":$('#graduateAudForm #branchSchool').val();
				var major		=$('#graduateAudForm #major').val()==undefined?"":$('#graduateAudForm #major').val();
				var classic		=$('#graduateAudForm #classic').val()==undefined?"":$('#graduateAudForm #classic').val();
				var stuStatus	=$('#graduateAudForm #stuStatus').val()==undefined?"":$('#graduateAudForm #stuStatus').val();
				var name		=$('#graduateAudForm #name').val()==undefined?"":$('#graduateAudForm #name').val();
				var matriculateNoticeNo	=$('#graduateAudForm #matriculateNoticeNo').val()==undefined?"":$('#graduateAudForm #matriculateNoticeNo').val();
				var grade 		=$('#graduateAudForm #grade').val()==undefined?"":$('#graduateAudForm #grade').val();
			
				if(data['hasError']==true){//如果有错误信息，不修改页面的毕业审核状态
					
				}else{
					//全局设置的毕业时间
					var graduateDateG = $('#graduateAudForm #graduateDate').val();
					//毕业审核名单的页数
					var pNum                 = $('#graduateAudForm #pNum').val();
					var url = "${baseUrl}/edu3/schoolroll/graduate/audit/list.html?branchSchool="+branchSchool+"&major="+major+"&classic="+classic+"&stuStatus="+stuStatus+"&name="+name+"&matriculateNoticeNo="+matriculateNoticeNo+"&grade="+grade+"&graduateDate="+graduateDateG+"&pNum="+pNum+"&isRefresh=1";
					navTab.openTab('RES_SCHOOL_GRADUATION_AUDIT', url, '毕业资格审核');
					$("#graduateStatus").attr("color","blue");
					$("#graduateStatus").html("毕业已审核");
					$('#singleGPass').hide();
				}		
			}
		});
		
	}
	
	//批量学生学位审核（新）
	function auditDegreePass(){
		var stus = $('#stus').val();
		var mainC = $("#mainC").attr("checked"); 
		var graduateC = $("#graduateC").attr("checked");
		var majorBaseC = $("#majorBaseC").attr("checked");
		var majorC = $("#majorC").attr("checked");
		var flC = $("#flC").attr("checked");
		var studentId = $('#studentId').val();            //当前页的学生id
		var postUrl=baseUrl+"/edu3/roll/graduateaudit/viaDegree.html?stus="+stus+"&studentId="+studentId
				+"&mainC="+mainC+"&graduateC="+graduateC+"&majorBaseC="+majorBaseC+"&majorC="+majorC+"&flC="+flC;
		$.ajax({
			type:"post",
			url:postUrl,
			dataType:"json",
			success:function(data){
				alertMsg.warn(data['message']);
				var branchSchool=$('#graduateAudForm #branchSchool').val()==undefined?"":$('#graduateAudForm #branchSchool').val();
				var major		=$('#graduateAudForm #major').val()==undefined?"":$('#graduateAudForm #major').val();
				var classic		=$('#graduateAudForm #classic').val()==undefined?"":$('#graduateAudForm #classic').val();
				var stuStatus	=$('#graduateAudForm #stuStatus').val()==undefined?"":$('#graduateAudForm #stuStatus').val();
				var name		=$('#graduateAudForm #name').val()==undefined?"":$('#graduateAudForm #name').val();
				var matriculateNoticeNo	=$('#graduateAudForm #matriculateNoticeNo').val()==undefined?"":$('#graduateAudForm #matriculateNoticeNo').val();
				var grade 		=$('#graduateAudForm #grade').val()==undefined?"":$('#graduateAudForm #grade').val();

				if(data['hasError']==true){
					
				}else{
					//全局设置的毕业时间
					var graduateDateG = $('#graduateAudForm #graduateDate').val();
					//毕业审核名单的页数
					var pNum                 = $('#graduateAudForm #pNum').val();
					var url = "${baseUrl}/edu3/schoolroll/graduate/audit/list.html?branchSchool="+branchSchool+"&major="+major+"&classic="+classic+"&stuStatus="+stuStatus+"&name="+name+"&matriculateNoticeNo="+matriculateNoticeNo+"&grade="+grade+"&graduateDate="+graduateDateG+"&pNum="+pNum+"&isRefresh=1";
					navTab.openTab('RES_SCHOOL_GRADUATION_AUDIT', url, '毕业资格审核');
					if(data['hasFail']!=true){
						$("#degreeStatus").attr("color","blue");
						$("#degreeStatus").html("学位已审核");
						$('#singleDPass').hide();
					}
					
				}
			}
		});
	}
	//单个学生学位审核（新）
	function auditSingleDegreePass(){
		var mainC = $("#mainC").attr("checked"); 
		var graduateC = $("#graduateC").attr("checked");
		var majorBaseC = $("#majorBaseC").attr("checked");
		var majorC = $("#majorC").attr("checked");
		var flC = $("#flC").attr("checked");
		var studentId = $('#studentId').val();
		var graduateStatus = $('#graduateStatus').text();
		var degreeStatus   = $("#degreeStatus").text();
		var mainCourseAvg = $("#mainCourseAvg").val();
		var isPass = $("#isPass").val();
		var gdPass = $("#gdPass").val();
		var majorPass = $("#majorPass").val();
		if("毕业已审核" != graduateStatus){
			alertMsg.warn("该学生并未通过毕业审核，目前不能进行学位审核。");
			return false;
		}
		if("学位已审核"==degreeStatus){
			alertMsg.warn("该学生已经通过学位审核，目前不予进行学位审核。");
			return false;
		}
		var postUrl=baseUrl+"/edu3/roll/graduateaudit/viaDegreeBySingle.html?studentId="+studentId
				+"&mainCourseAvg="+mainCourseAvg
				+"&isPass="+isPass
				+"&gdPass="+gdPass
				+"&majorPass="+majorPass
				+"&mainC="+mainC+"&graduateC="+graduateC+"&majorBaseC="+majorBaseC+"&majorC="+majorC+"&flC="+flC;
		$.ajax({
			type:"post",
			url:postUrl,
			dataType:"json",
			success:function(data){
				alertMsg.warn(data['message']);
				var branchSchool=$('#graduateAudForm #branchSchool').val()==undefined?"":$('#graduateAudForm #branchSchool').val();
				var major		=$('#graduateAudForm #major').val()==undefined?"":$('#graduateAudForm #major').val();
				var classic		=$('#graduateAudForm #classic').val()==undefined?"":$('#graduateAudForm #classic').val();
				var stuStatus	=$('#graduateAudForm #stuStatus').val()==undefined?"":$('#graduateAudForm #stuStatus').val();
				var name		=$('#graduateAudForm #name').val()==undefined?"":$('#graduateAudForm #name').val();
				var matriculateNoticeNo	=$('#graduateAudForm #matriculateNoticeNo').val()==undefined?"":$('#graduateAudForm #matriculateNoticeNo').val();
				var grade 		=$('#graduateAudForm #grade').val()==undefined?"":$('#graduateAudForm #grade').val();

				if(data['hasError']==true){
					
				}else{
					//全局设置的毕业时间
					var graduateDateG = $('#graduateAudForm #graduateDate').val();
					//毕业审核名单的页数
					var pNum                 = $('#graduateAudForm #pNum').val();
					var url = "${baseUrl}/edu3/schoolroll/graduate/audit/list.html?branchSchool="+branchSchool+"&major="+major+"&classic="+classic+"&stuStatus="+stuStatus+"&name="+name+"&matriculateNoticeNo="+matriculateNoticeNo+"&grade="+grade+"&graduateDate="+graduateDateG+"&pNum="+pNum+"&isRefresh=1";
					navTab.openTab('RES_SCHOOL_GRADUATION_AUDIT', url, '毕业资格审核');
					$("#degreeStatus").attr("color","blue");
					$("#degreeStatus").html("学位已审核");
					$('#singleDPass')
				}
				
			}
		});
	}
	function saveGraduateDateL(){
		$("#isRefreshForm").val("1");
		$("#updateGData").click();	
	}
</script>
</head>
<body>

	<h2 class="contentTitle">
		毕业
		<c:if test="${auditDegree=='1'}">、学位 </c:if>
		审核管理
	</h2>
	<div class="page">
		<div class="pageContent">
			<div class="pageFormContent" layoutH="97">
				<!-- tabs -->
				<div class="tabs">
					<div class="tabsHeader">
						<div class="tabsHeaderContent">
							<ul>
								<li><a href="javascript:void(0)"><span>毕业审核</span></a></li>
							</ul>
						</div>
					</div>

					<!-- 审核信息 -->
					<div id="main">
						<table class="table" layouth="200" style="width: 100%;">
							<thead>
								<tr>
									<th width="25%"></th>
									<th width="25%"></th>
									<th width="25%"></th>
									<th width="25%"></th>
								</tr>
							</thead>
							<tbody id="auditStusBody">
								<c:forEach items="${studentInfos.result}" var="studentInfo"
									varStatus="vs">
									<input type="hidden" id="studentId"
										value="${studentInfo.resourceid}">
									<tr>
										<td colspan="2"><font color="red">审核状态</font></td>
										<td colspan="2"><c:choose>
												<c:when
													test="${studentInfo.studentStatus=='22'||studentInfo.studentStatus=='16'}">
													<font id="graduateStatus" color="blue">毕业已审核</font>
												</c:when>
												<c:otherwise>
													<font id="graduateStatus" color="red">毕业未审核</font>
												</c:otherwise>
											</c:choose> <c:choose>
												<c:when test="${studentInfo.degreeStatus=='Y'}">
													<font id="degreeStatus" color="blue">学位已审核</font>
												</c:when>
												<c:otherwise>
													<font id="degreeStatus" color="red">学位未审核</font>
												</c:otherwise>
											</c:choose></td>
									</tr>
									<tr>
										<td width="25%">学生号：</td>
										<td width="25%">${studentInfo.studyNo}</td>
										<td width="20%">考生号：</td>
										<td width="25%">${studentInfo.enrolleeCode}</td>
									</tr>

									<tr>
										<td width="25%">姓名:</td>
										<td width="25%">${studentInfo.studentBaseInfo.name }</td>
										<td width="20%">性别:</td>
										<td width="30%">${ghfn:dictCode2Val("CodeSex",studentInfo.studentBaseInfo.gender) }</td>
									</tr>
									<tr>
										<td width="25%">进修性质:</td>
										<td width="25%">${ghfn:dictCode2Val('CodeAttendAdvancedStudies',studentInfo.attendAdvancedStudies) }</td>
										<td width="20%">学习方式：</td>
										<td width="30%">${ghfn:dictCode2Val('CodeLearningStyle',studentInfo.learningStyle) }
										</td>
									</tr>
									<tr>
										<td width="25%">教学站:</td>
										<td width="25%">${studentInfo.branchSchool.unitName}</td>
										<td width="20%">专业：</td>
										<td width="30%">${ studentInfo.major.majorName}</td>
									</tr>
									<tr>
										<td>就读方式:</td>
										<td>${ghfn:dictCode2Val('CodeStudyInSchool',studentInfo.studyInSchool ) }</td>
										<td>学习类别：</td>
										<td>${studentInfo.studentKind }</td>
									</tr>
									<tr>
										<td width="25%">联系地址:</td>
										<td width="25%">${studentInfo.studentBaseInfo.contactAddress }</td>
										<td width="20%">联系邮编：</td>
										<td width="30%">${studentInfo.studentBaseInfo.contactZipcode }</td>
									</tr>
									<tr>
										<td width="25%">联系电话:</td>
										<td width="25%">${studentInfo.studentBaseInfo.contactPhone }</td>
										<td width="20%">移动电话：</td>
										<td width="30%">${studentInfo.studentBaseInfo.mobile }</td>
									</tr>
									<tr>
										<td width="25%">国籍:</td>
										<td width="25%">${ghfn:dictCode2Val("CodeCountry",studentInfo.studentBaseInfo.country) }</td>
										<td width="20%">籍贯:</td>
										<td width="30%">${studentInfo.studentBaseInfo.homePlace }</td>
									</tr>

									<tr>
										<td style="width: 20%">教学计划名称:</td>
										<td colspan="3" style="width: 30%">${studentInfo.teachingPlan.major }
											- ${studentInfo.teachingPlan.classic }
											(${studentInfo.teachingPlan.versionNum})</td>
									</tr>
									<tr>
										<td colspan="4"><font color="red">毕业资格审核信息</font></td>
									</tr>
									<tr>
										<td width="25%">年级:</td>
										<td width="25%">${studentInfo.grade.gradeName }</td>
										<td width="20%">层次:</td>
										<td width="30%">${studentInfo.classic.shortName}</td>
									</tr>
									<tr>
										<td width="25%">毕业年限:</td>
										<td width="25%">
											<!-- 2008秋以前 --> <c:if
												test="${studentInfo.grade.yearInfo.firstYear<2008}">
												<c:choose>
													<c:when test="${studentInfo.classic.shortName=='高升专'}">2年</c:when>
													<c:when test="${studentInfo.classic.shortName=='专升本'}">2年</c:when>
													<c:when test="${studentInfo.classic.shortName=='高起本'}">4年</c:when>
												</c:choose>
											</c:if> <!-- 2008秋以后--> <c:if
												test="${studentInfo.grade.yearInfo.firstYear>=2008}">
												<c:choose>
													<c:when test="${studentInfo.classic.shortName=='高升专'}">2.5年</c:when>
													<c:when test="${studentInfo.classic.shortName=='专升本'}">2.5年</c:when>
													<c:when test="${studentInfo.classic.shortName=='高起本'}">5年</c:when>
												</c:choose>
											</c:if>
										</td>
										<td width="20%"></td>
										<td width="30%"></td>
									</tr>
									<tr>
										<td width="25%">毕业最低学分:</td>
										<td width="25%">${studentInfo.teachingPlan.minResult }</td>
										<td width="20%">已经取得的总学分:</td>
										<td width="30%">${studentInfo.finishedCreditHour}</td>
									</tr>
									<tr>
										<td width="25%">毕业必修最低学分:</td>
										<td width="25%">${studentInfo.teachingPlan.mustCourseTotalCreditHour }</td>
										<td width="20%">学生必修学分</td>
										<td width="30%">${studentInfo.finishedNecessCreditHour}</td>
									</tr>
									<tr>
										<td width="25%">毕业最低限选课修读门数:</td>
										<td width="25%"><c:if
												test="${studentInfo.teachingPlan.optionalCourseNum== null}">0</c:if>
											<c:if
												test="${studentInfo.teachingPlan.optionalCourseNum!= null}">${studentInfo.teachingPlan.optionalCourseNum }</c:if>
										</td>
										<td width="20%">已修读限选课门数:</td>
										<td width="30%"><c:if
												test="${studentInfo.finishedOptionalCourseNum== null}">0</c:if>
											<c:if test="${studentInfo.finishedOptionalCourseNum!= null}">${studentInfo.finishedOptionalCourseNum }</c:if>
										</td>
									</tr>
									<!-- 
							<tr>
								<td width="25%">毕业论文申请最低学分:</td>
								<td width="25%">${studentInfo.teachingPlan.applyPaperMinResult }</td>
								<td width="20%">已取得的毕业论文分数:</td>
								<td width="30%"></td>
							</tr>
							 -->
									<tr>
										<td width="25%">入学资格审查状态:</td>
										<td width="25%"><c:choose>
												<c:when test="${studentInfo.enterAuditStatus=='Y'}">通过</c:when>
												<c:when test="${studentInfo.enterAuditStatus=='N'}">
													<font color="red">未通过</font>
												</c:when>
												<c:otherwise>
													<font color="red">待审核</font>
												</c:otherwise>
											</c:choose></td>
										<td width="20%">是否申请延迟毕业:</td>
										<td width="30%"><c:choose>
												<c:when test="${studentInfo.isApplyGraduate=='W'}">
													<font color="red">是</font>
												</c:when>
												<c:otherwise>否</c:otherwise>
											</c:choose>
									</tr>
									<c:if test="${auditDegree=='1'}">
										<tr>
											<td colspan="4"><font color="red">学位审核信息</font></td>
										</tr>
										<input type="hidden" id="mainCourseAvg"
											value="${mainCourseAvg}" />
										<input type="hidden" id="isPass" value="${isPass}" />
										<input type="hidden" id="gdPass" value="${gdPass}" />
										<input type="hidden" id="majorPass" value="${majorPass}" />
										<tr>
											<td width="25%">主干课程平均成绩:</td>
											<td width="25%">${mainCourseAvg}</td>
											<td width="20%">主干课程修读情况:</td>
											<td width="30%">${isPass}</td>
										</tr>
										<tr>
											<td width="25%">毕业设计(论文):</td>
											<td width="25%">${gdPass}</td>
											<td width="20%">一门外语课、一门专业基础、两门专业课:</td>
											<td width="30%">${majorPass}</td>
										</tr>
										<tr>
											<td width="25%">学位审核条件:</td>
											<td colspan="3">主干课<input type="checkbox" id="mainC"
												value="on" checked="checked" />&nbsp;&nbsp;&nbsp;&nbsp;
												毕业论文<input type="checkbox" id="graduateC" value="on"
												checked="checked" />&nbsp;&nbsp;&nbsp;&nbsp; 专业基础课<input
												type="checkbox" id="majorBaseC" value="on" checked="checked" />&nbsp;&nbsp;&nbsp;&nbsp;
												专业课<input type="checkbox" id="majorC" value="on"
												checked="checked" />&nbsp;&nbsp;&nbsp;&nbsp; 外国语课<input
												type="checkbox" id="flC" value="on" checked="checked" />

											</td>
										</tr>
									</c:if>
									<!-- 
							<tr>
								<td width="25%">学位授予:</td>
								<td width="25%">${ghfn:dictCode2Val('CodeDegree',studentInfo.teachingPlan.degreeName ) }</td>
							</tr>
							 -->

								</c:forEach>
							</tbody>
						</table>
						<div class="panelBar">
							<gh:page page="${studentInfos}" targetType="dialog"
								isShowPageSelector="N"
								goPageUrl="${baseUrl}/edu3/roll/graduateaudit/auditGraduate.html"
								pageType="sp_graduate" condition="${condition}" />
							<form id="graduateAuditInfoList"
								onsubmit="return dialogSearch(this);"
								action="${baseUrl }/edu3/roll/graduateaudit/auditGraduate.html"
								method="post">
								<!-- 与审查结束后重新加载两个页面相关的参数隐藏域 -->
								<div id="graduateAudForm">
									<input type="hidden" name="stus" id="stus" value="${stus}" />
									<!-- 以下若干个参数与审核无关，审核之后需要更新审查名单页面，是该页面上的条件 -->
									<input type="hidden" name="branchSchool" id="branchSchool"
										value="${condition['branchSchool']}" /> <input type="hidden"
										name="major" id="major" value="${condition['major']}" /> <input
										type="hidden" name="classic" id="classic"
										value="${condition['classic']}" /> <input type="hidden"
										name="stuStatus" id="stuStatus"
										value="${condition['stuStatus']}" /> <input type="hidden"
										name="name" id="name" value="${condition['name']}" /> <input
										type="hidden" name="matriculateNoticeNo"
										id="matriculateNoticeNo"
										value="${condition['matriculateNoticeNo']}" /> <input
										type="hidden" name="grade" id="grade"
										value="${condition['grade']}" /> <input type="hidden"
										name="graduateDate" id="graduateDate"
										value="${condition['graduateDate']}" /> <input type="hidden"
										name="pNum" id="pNum" value="${condition['pNum']}" />
									<!-- 刷新本审核界面时需要的当前页的页数 -->
									<input type="hidden" name="currentPageNumber"
										id="currentPageNumber" value="${pageNum}"> <input
										type="hidden" name="isRefreshForm" id="isRefreshForm"
										value="0" />
								</div>
								<label>设置毕业时间:</label><input type="text" id="graduateDateInform"
									name="graduateDateL" class="Wdate"
									value="${condition['graduateDateL']}"
									onfocus="WdatePicker({onpicked:function(){saveGraduateDateL();},isShowWeek:true})" />
								<button id="updateGData" type="submit" hidden="true">更新局部毕业日期</button>
							</form>
							<!-- 废弃的分页
				<c:forEach begin="0" end="${studentInfos.totalCount-1}" varStatus="status">
					<a href="#" onclick="pageJump('${status.index+1 }')"><c:choose>
											<c:when test="${pageNum==status.index+1}"><font color="red">${status.index+1 }</font></c:when>
											<c:otherwise>${status.index+1 }</c:otherwise>
											</c:choose>
											</a>&nbsp;
				</c:forEach>
				-->
							<c:forEach items="${studentInfos.result}" var="studentInfo"
								varStatus="vs">
								<c:if
									test="${studentInfo.studentStatus!='22'&&studentInfo.studentStatus!='16'}">
									<button id="singleGPass" type="button"
										onclick="auditSinglePass()">毕业审核单个通过</button>
								</c:if>
								<c:if test="${studentInfo.degreeStatus!='Y'}">
									<c:if test="${auditDegree=='1'}">
										<button id="singleDPass" type="button"
											onclick="auditSingleDegreePass()">学位审核单个通过</button>
									</c:if>
								</c:if>
								<button id="allGPass" type="button" onclick="auditPass()">毕业审核全部通过</button>
								<button id="allDPass" type="button" onclick="auditDegreePass()">学位审核全部通过</button>
							</c:forEach>
						</div>
						<!-- <input type="text" name="graduateDateInAudit" id="graduateDateInAudit" class="Wdate" value="${setDate}" onfocus="WdatePicker({isShowWeek:true})" /> -->
					</div>
					<!--<form  action="" method="post">
					<input type="hidden" name="resourceid" value="${studentInfo.resourceid }">
					</form>
						<table class="form" width="100%" highet="%100">
								<tr>
									<td width="25%">学生号：</td>
									<td width="25%">${studentInfo.studyNo }</td>
									<td width="20%">考生号：</td>
									<td width="25%">${studentInfo.enrolleeCode }</td>
								</tr>
								<tr>
									<td width="25%">姓名:</td>
									<td width="25%">${studentInfo.studentBaseInfo.name }</td>
									<td width="20%">性别:</td>
									<td width="30%">${ghfn:dictCode2Val("CodeSex",studentInfo.studentBaseInfo.gender) }</td>
								</tr>
								<tr>
									<td width="25%">进修性质:</td>
									<td width="25%">${ghfn:dictCode2Val('CodeAttendAdvancedStudies',studentInfo.attendAdvancedStudies) }</td>
									<td width="20%">学习方式：</td>
									<td width="30%">${ghfn:dictCode2Val('CodeLearningStyle',studentInfo.learningStyle) }
										<!--<gh:select name="learningStyle" value="${studentInfo.learningStyle }" dictionaryCode="CodeLearningStyle" style="width:50%" />
									-->
					<!--</td>				
								</tr>
								<tr>
									<td width="25%">教学站:</td>
									<td width="25%">${studentInfo.branchSchool.unitName}</td>
									<td width="20%">专业：</td>
									<td width="30%">${ studentInfo.major.majorName}</td>				
								</tr>
								<tr>
									<td>就读方式:</td>
									<td>${ghfn:dictCode2Val('CodeStudyInSchool',studentInfo.studyInSchool ) }</td>
									<td>学习类别：</td>
									<td>${studentInfo.studentKind }</td>				
								</tr>
								<tr>
									<td width="25%">联系地址:</td>
									<td width="25%">${studentInfo.studentBaseInfo.contactAddress }</td>
									<td width="20%">联系邮编：</td>
									<td width="30%">${studentInfo.studentBaseInfo.contactZipcode }</td>				
								</tr>
								<tr>
									<td width="25%">联系电话:</td>
									<td width="25%">${studentInfo.studentBaseInfo.contactPhone }</td>
									<td width="20%">移动电话：</td>
									<td width="30%">${studentInfo.studentBaseInfo.mobile }</td>				
								</tr>
								<tr>
									<td width="25%">国籍:</td>
									<td width="25%">${ghfn:dictCode2Val("CodeCountry",studentInfo.studentBaseInfo.country) }</td>
									<td width="20%">籍贯:</td>
									<td width="30%">${studentInfo.studentBaseInfo.homePlace }</td>
								</tr>
								<tr>
									<td style="width:20%">教学计划名称:</td>
									<td  colspan="3" style="width:30%">${studentInfo.teachingPlan.major } - ${studentInfo.teachingPlan.classic } (${studentInfo.teachingPlan.versionNum})</td>
									<!-- 
									<td style="width:20%">办学模式:</td>
									<td style="width:30%">${ghfn:dictCode2Val('teachingType',studentInfo.teachingPlan.schoolType) }</td>
									-->
					<!--
								</tr>
								<tr>
									<td width="25%">毕业最低学分:</td>
									<td width="25%">${studentInfo.teachingPlan.minResult }</td>
									<td width="20%">毕业论文申请最低学分:</td>
									<td width="30%">${studentInfo.teachingPlan.applyPaperMinResult }</td>
								</tr>
								<tr>
									<td width="25%">已经取得的总学分:</td>
									<td width="25%">${realityIntegratedScore }</td>
									<td width="20%">已经取得的毕业论文分数:</td>
									<td width="30%">${realityThesisScore }</td>
								</tr>
							<tr>
								<td width="25%">选修课修读门数：:</td>
								<td width="25%">${studentInfo.teachingPlan.optionalCourseNum }</td>
								<td width="20%">学位授予:</td>
								<td width="30%">${ghfn:dictCode2Val('CodeDegree',studentInfo.teachingPlan.degreeName ) }</td>
							</tr>
							<tr>
								<td colspan="4">&nbsp;</td>
							</tr>
							<tr>
								<td colspan="4">
								
								<div   align="center" whidth="%100" hight="40">
									<INPUT TYPE="button" VALUE="审核通过" ONCLICK="viaGraduate('确定该学员审核通过吗？','16','${studentInfo.resourceid }')">&nbsp;&nbsp;
									<INPUT TYPE="button" VALUE="审核不通过" ONCLICK="viaGraduate('确定该学员审核不通过吗？','11','${studentInfo.resourceid }')">&nbsp;&nbsp;
									<INPUT TYPE="button" VALUE="关闭" ONCLICK="$.pdialog.closeCurrent();">
								
								</div>
								</td>
							</tr>
						</table>
						<div style="padding-top:2px;padding-bottom:2px;font-weight:bold"></div>
					</div>
				<!-- 2 这是曾经的一种做法 将学位和毕业的审核放在了一个也页面中进行-->
					<!-- 
				<c:if test="${isApplyGraduate}">
				<div id="via2">
					<form  action="" method="post">
					<input type="hidden" name="resourceid" value="${studentInfo.resourceid }">
					</form>
					
						<table class="form" width="100%" highet="%100">
								<tr>
									<td width="25%">学生号：</td>
									<td width="25%">${studentInfo.studyNo }</td>
									<td width="20%">考生号：</td>
									<td width="25%">${studentInfo.enrolleeCode }</td>
								</tr>
								<tr>
									<td width="25%">姓名:</td>
									<td width="25%">${studentInfo.studentBaseInfo.name }</td>
									<td width="20%">性别:</td>
									<td width="30%">${ghfn:dictCode2Val("CodeSex",studentInfo.studentBaseInfo.gender) }</td>
								</tr>
								<tr>
									<td width="25%">进修性质:</td>
									<td width="25%">${ghfn:dictCode2Val('CodeAttendAdvancedStudies',studentInfo.attendAdvancedStudies) }</td>
									<td width="20%">学习方式：</td>
									<td width="30%">${ghfn:dictCode2Val('CodeLearningStyle',studentInfo.learningStyle) }
										<!--<gh:select name="learningStyle" value="${studentInfo.learningStyle }" dictionaryCode="CodeLearningStyle" style="width:50%" />
									-->
					<!-- </td>				
								</tr>
								<tr>
									<td width="25%">教学站:</td>
									<td width="25%">${studentInfo.branchSchool.unitName}</td>
									<td width="20%">专业：</td>
									<td width="30%">${ studentInfo.major.majorName}</td>				
								</tr>
								<tr>
									<td>就读方式:</td>
									<td>${ghfn:dictCode2Val('CodeStudyInSchool',studentInfo.studyInSchool ) }</td>
									<td>学习类别：</td>
									<td>${studentInfo.studentKind }</td>				
								</tr>
								<tr>
									<td width="25%">联系地址:</td>
									<td width="25%">${studentInfo.studentBaseInfo.contactAddress }</td>
									<td width="20%">联系邮编：</td>
									<td width="30%">${studentInfo.studentBaseInfo.contactZipcode }</td>				
								</tr>
								<tr>
									<td width="25%">联系电话:</td>
									<td width="25%">${studentInfo.studentBaseInfo.contactPhone }</td>
									<td width="20%">移动电话：</td>
									<td width="30%">${studentInfo.studentBaseInfo.mobile }</td>				
								</tr>
								<tr>
									<td width="25%">国籍:</td>
									<td width="25%">${ghfn:dictCode2Val("CodeCountry",studentInfo.studentBaseInfo.country) }</td>
									<td width="20%">籍贯:</td>
									<td width="30%">${studentInfo.studentBaseInfo.homePlace }</td>
								</tr>
								
								
								<tr>
									<td style="width:20%">教学计划名称:</td>
									<td  colspan="3" style="width:30%">${studentInfo.teachingPlan.major } - ${studentInfo.teachingPlan.classic } (${studentInfo.teachingPlan.versionNum})</td>
									<!-- 
									<td style="width:20%">办学模式:</td>
									<td style="width:30%">${ghfn:dictCode2Val('teachingType',studentInfo.teachingPlan.schoolType) }</td>
									-->
					<!--  
								</tr>
								<tr>
									<td colspan="4">学位课程：
									<c:forEach items="${degreeList }" var="degreeCondition" varStatus="vs">									
									<span style="padding-right:20px">
									<!-- <input type="checkbox" name="degreeRules" value="${degreeCondition.dictValue }"
									
									<c:forTokens items="${teachingPlan.degreeRules}" delims="," var="degreeRule" varStatus="status">
									<c:if test="${degreeRule eq degreeCondition.dictValue }">checked</c:if>
									
									</c:forTokens>
 									/> 
 									 -->
					<!-- 
 									${ degreeCondition.dictName}</span>
									</c:forEach>
									</td>
								</tr>
							<tr>
								<td colspan="4">&nbsp;</td>
							</tr>
							<tr>
								<td colspan="4">
								
								<div   align="center" whidth="%100" hight="40">
									<INPUT TYPE="button" VALUE="审核通过" ONCLICK="viaDegree('确定该学员审核通过吗？','16','${studentInfo.resourceid }')">&nbsp;&nbsp;
									<INPUT TYPE="button" VALUE="审核不通过" ONCLICK="viaDegree('确定该学员审核不通过吗？','11','${studentInfo.resourceid }')">&nbsp;&nbsp;
									<INPUT TYPE="button" VALUE="关闭" ONCLICK="$.pdialog.closeCurrent();">
								
								</div>
								</td>
							</tr>
						</table>
						<div style="padding-top:2px;padding-bottom:2px;font-weight:bold"></div>
					</div>
				</c:if>
				</div>
				-->
					<!-- end tabs -->
				</div>
			</div>
		</div>
		<SCRIPT LANGUAGE="JavaScript">
	
	//毕业资格审核(旧)
	/*
		function viaGraduate(msg,via,id){
			var postUrl=baseUrl+"/edu3/roll/graduateaudit/viaGraduate.html?via="+via;
			alertMsg.confirm(msg, {
				okCall: function(){//执行			
                $.post(postUrl,{resourceid:id}, navTabAjaxDone, "json");
				}
		});	
		}
	*/
		//学位资格审核	(现在学位审核定在graduationdegree-form.jsp中执行)
		/*
		function viaDegree(msg,via,id){
			var postUrl=baseUrl+"/edu3/roll/graduateaudit/viaDegree.html?via="+via;
			alertMsg.confirm(msg, {
				okCall: function(){//执行			
                $.post(postUrl,{resourceid:id}, navTabAjaxDone, "json");
				}
		});	
		}
		*/
	</SCRIPT>
</body>

</html>