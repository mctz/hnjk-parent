<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>详细信息</title>

</head>
<body>
	<h2 class="contentTitle">详细信息</h2>
	<div class="page">
		<div class="pageContent">
			<form id="inputForm1" method="post"
				action="${baseUrl}/edu3/register/studentinfo/registering.html"
				class="pageForm">
				<input type="hidden" name="resourceid"
					value="${enrollInfo.resourceid }">
				<div class="pageFormContent" layoutH="97">
					<div class="tabs">
						<div class="tabsHeader">
							<div class="tabsHeaderContent">
								<ul>
									<li class="selected"><a href="#"><span>个人信息</span></a></li>
									<li><a href="#"><span>缴费信息</span></a></li>
								</ul>
							</div>
						</div>
						<div class="tabsContent" style="height: 100%;">
							<div>
								<table class="form">
									<tr>
										<td style="width: 20%">姓名:</td>
										<td style="width: 30%">${enrollInfo.studentBaseInfo.name }</td>
										<td style="width: 20%">性别:</td>
										<td style="width: 30%">${ghfn:dictCode2Val("CodeSex",enrollInfo.studentBaseInfo.gender) }</td>
									</tr>
									<tr>
										<td>证件类别:</td>
										<td>${ghfn:dictCode2Val("CodeCertType",enrollInfo.studentBaseInfo.certType) }</td>
										<td>证件号码：</td>
										<td>${enrollInfo.studentBaseInfo.certNum }</td>
									</tr>
									<tr>
										<td>出生日期:</td>
										<td>${enrollInfo.studentBaseInfo.bornDay }</td>
										<td>籍贯:</td>
										<td>${enrollInfo.studentBaseInfo.homePlace }</td>
									</tr>
									<tr>
										<td>民族:</td>
										<td>${ghfn:dictCode2Val("CodeNation",enrollInfo.studentBaseInfo.nation) }</td>
										<td>政治面目:</td>
										<td>${ghfn:dictCode2Val("CodePolitics",enrollInfo.studentBaseInfo.politics) }</td>
									</tr>
									<tr>
										<td>固定电话:</td>
										<td>${enrollInfo.studentBaseInfo.contactPhone }</td>
										<td>本人地址：</td>
										<td>${enrollInfo.studentBaseInfo.contactAddress }</td>
									</tr>
									<tr>
										<td>教学站:</td>
										<td>${enrollInfo.branchSchool }</td>
										<td>招生专业：</td>
										<td>${enrollInfo.recruitMajor.recruitMajorName }</td>
									</tr>
									<tr>
										<td>准考证号:</td>
										<td>${enrollInfo.examCertificateNo }</td>
										<td>入学前国民教育最高学历层次：</td>
										<td>${ghfn:dictCode2Val("CodeEducationalLevel",enrollInfo.educationalLevel) }</td>
									</tr>
									<tr>
										<td>入学前学历学校名称:</td>
										<td>${enrollInfo.graduateSchool }</td>
										<td>入学前学历学校代码：</td>
										<td>${enrollInfo.graduateSchoolCode }</td>
									</tr>
									<tr>
										<td>入学前学历证书编号:</td>
										<td>${enrollInfo.graduateId }</td>
										<td>入学前最高学历毕业日期：</td>
										<td>${enrollInfo.graduateDate }</td>
									</tr>
								</table>
							</div>
							<div>
								<table class="form" id="studentResume">
									<thead>
										<tr>
											<td width="10%">学号</td>
											<td width="9%">姓名</td>
											<td width="5%">性别</td>
											<td width="9%">专业</td>
											<td width="9%">培养层次</td>
											<td width="9%">学习方式</td>
											<td width="12%">教学站</td>
											<td width="13%">班级</td>
											<td width="6%">年度</td>
											<td width="9%">学费金额</td>
											<td width="9%">缴费金额</td>
										</tr>
									</thead>
									<tbody>
										<c:forEach items="${feeList}" var="fee" varStatus="vs">
											<tr>
												<td>${fee['StudentID'] }</td>
												<td>${fee['StudentName'] }</td>
												<td>${ghfn:dictCode2Val('CodeSex',fee['Sex']) }</td>
												<td>${fee['ProfessionName'] }</td>
												<td>${fee['LevelName'] }</td>
												<td>${fee['FormalName'] }</td>
												<td>${fee['CollegeName'] }</td>
												<td>${fee['ClassName'] }</td>
												<td>${fee['ChargeYear'] }</td>
												<td>${fee['TuitionFee'] }</td>
												<td>${fee['OccurAmt'] }</td>
											</tr>
										</c:forEach>
									</tbody>
								</table>
							</div>
						</div>
						<div class="tabsFooter">
							<div class="tabsFooterContent"></div>
						</div>
					</div>
				</div>
				<div class="formBar">
					<ul>
						<c:if
							test="${ghfn:hasAuth('RES_SCHOOL_REGISTER_ENROLL_REGISTER') }">
							<li><div class="buttonActive">
									<div class="buttonContent">
										<button type="button"
											onclick="submitRegister('${enrollInfo.resourceid}')">注册</button>
									</div>
								</div></li>
						</c:if>
						<!--  <li><div class="button"><div class="buttonContent"><button type="button" class="close" onclick="navTab.closeCurrentTab();">取消</button></div></div></li>-->
						<li><div class="button">
								<div class="buttonContent">
									<button type="button" class="close" href="#close"
										id="close_tab">取消</button>
								</div>
							</div></li>

					</ul>
				</div>
			</form>
		</div>
	</div>

</body>
<
<script type="text/javascript">
	function submitRegister(id){
		jQuery.ajax({
			data:"resourceid="+id,
			url:"${baseUrl}/edu3/register/studentinfo/registering.html",
			success:function(){
				//navTab.closeCurrentTab();
				$("#close_tab").click();
			}
		})
	}
</script>
</html>