<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>学位审核管理</title>
<script type="text/javascript">
//跳转页
function pageJumpDegree(val){
	var resourceid = $('#resourceid').val();
	var url = "${baseUrl}/edu3/teaching/graduateaudit/auditdegree.html?resourceid="+resourceid+"&pageNum="+val;
	$.pdialog.open(url, 'RES_SCHOOL_GRADUATION_DGREE1', '学位审核管理', {width: 800, height: 600});
}
//学位资格审核
function auditPassDegree(){
	var stus = $('#stus').val();
	var postUrl=baseUrl+"/edu3/roll/graduateaudit/viaDegree.html?stus="+stus;
	$.ajax({
		type:"post",
		url:postUrl,
		dataType:"json",
		success:function(data){
			alert(data['message']);
			var branchSchool=$('#graduateauditbranchSchool').val()==undefined?"":$('#graduateauditbranchSchool').val();
			var major		=$('#major').val()==undefined?"":$('#major').val();
			var classic		=$('#classic').val()==undefined?"":$('#classic').val();
			var stuStatus	=$('#stuStatus').val()==undefined?"":$('#stuStatus').val();
			var name		=$('#name').val()==undefined?"":$('#name').val();
			var matriculateNoticeNo	=$('#matriculateNoticeNo').val()==undefined?"":$('#matriculateNoticeNo').val();
			var grade 		=$('#grade').val()==undefined?"":$('#grade').val();
			if(data['hasError']==false){
				$.pdialog.closeCurrent();
			}else{
				var url = "${baseUrl}/edu3/schoolroll/graduation/degree/list.html?branchSchool="+branchSchool+"&major="+major+"&classic="+classic+"&stuStatus="+stuStatus+"&name="+name+"&matriculateNoticeNo"+matriculateNoticeNo+"&grade="+grade;
				navTab.openTab('RES_SCHOOL_GRADUATION_DGREE', url, '审核毕业资格列表');
				$.pdialog.closeCurrent();
			}
		}
	});
}
</script>
</head>
<body>

	<h2 class="contentTitle">学位审核管理</h2>
	<div class="page">
		<div class="pageContent">
			<input type="hidden" id="stus" value="${stus}" /> <input
				type="hidden" id="resourceid" value="${resourceid}" />
			<div class="pageFormContent" layoutH="97">
				<!-- tabs -->
				<div class="tabs">
					<div class="tabsHeader">
						<div class="tabsHeaderContent">
							<ul>
								<li><a href="javascript:void(0)"><span>学位审核</span></a></li>
							</ul>
						</div>
					</div>
					<!-- Hidden District -->
					<input type="hidden" id="graduateauditbranchSchool"
						value="${graduateauditbranchSchool}" /> <input type="hidden"
						id="major" value="${major}" /> <input type="hidden" id="classic"
						value="${classic}" /> <input type="hidden" id="stuStatus"
						value="${stuStatus}" /> <input type="hidden" id="name"
						value="${name}" /> <input type="hidden" id="matriculateNoticeNo"
						value="${matriculateNoticeNo}" /> <input type="hidden" id="grade"
						value="${grade}" />
					<div id="via1">
						<table class="table" layouth="280" style="width: 100%;">
							<thead>
								<tr>
									<th width="25%"></th>
									<th width="25%"></th>
									<th width="20%"></th>
									<th width="25%"></th>
								</tr>
							</thead>
							<tbody id="auditStusBody">
								<c:forEach items="${graduateionDatas.result}" var="gData"
									varStatus="vs">
									<tr>
										<td width="25%">学生号：</td>
										<td width="25%">${gData.studyNo }</td>
										<td width="20%">考生号：</td>
										<td width="25%">${gData.enrolleeCode }</td>
									</tr>

									<tr>
										<td width="25%">姓名:</td>
										<td width="25%">${gData.studentBaseInfo.name }</td>
										<td width="20%">性别:</td>
										<td width="30%">${ghfn:dictCode2Val("CodeSex",gData.studentBaseInfo.gender) }</td>
									</tr>
									<tr>
										<td width="25%">进修性质:</td>
										<td width="25%">${ghfn:dictCode2Val('CodeAttendAdvancedStudies',gData.attendAdvancedStudies) }</td>
										<td width="20%">学习方式：</td>
										<td width="30%">${ghfn:dictCode2Val('CodeLearningStyle',gData.learningStyle) }</td>
									</tr>
									<tr>
										<td width="25%">教学站:</td>
										<td width="25%">${gData.branchSchool.unitName}</td>
										<td width="20%">专业：</td>
										<td width="30%">${gData.major.majorName}</td>
									</tr>
									<tr>
										<td>就读方式:</td>
										<td>${ghfn:dictCode2Val('CodeStudyInSchool',gData.studyInSchool ) }</td>
										<td>学习类别：</td>
										<td>${gData.studentKind }</td>
									</tr>
									<tr>
										<td width="25%">联系地址:</td>
										<td width="25%">${gData.studentBaseInfo.contactAddress }</td>
										<td width="20%">联系邮编：</td>
										<td width="30%">${gData.studentBaseInfo.contactZipcode }</td>
									</tr>
									<tr>
										<td width="25%">联系电话:</td>
										<td width="25%">${gData.studentBaseInfo.contactPhone }</td>
										<td width="20%">移动电话：</td>
										<td width="30%">${gData.studentBaseInfo.mobile }</td>
									</tr>
									<tr>
										<td width="25%">国籍:</td>
										<td width="25%">${ghfn:dictCode2Val("CodeCountry",gData.studentBaseInfo.country) }</td>
										<td width="20%">籍贯:</td>
										<td width="30%">${gData.studentBaseInfo.homePlace }</td>
									</tr>
									<tr>
										<td style="width: 20%">教学计划名称:</td>
										<td colspan="3" style="width: 30%">${gData.teachingPlan.major.majorname }
											- ${gData.teachingPlan.classic.classicname }
											(${gData.teachingPlan.versionNum})</td>
									</tr>
									<tr>
										<td colspan="4"><font color="red">学位审核信息</font></td>
									</tr>
									<tr>
										<td width="25%">主干课平均分:</td>
										<td width="25%"></td>
										<td width="20%">毕业论文:</td>
										<td width="30%"></td>
									</tr>
									<tr>
										<td width="25%">通过学位课程考试:</td>
										<td width="25%"></td>
										<td width="20%"></td>
										<td width="30%"></td>
									</tr>
									<tr>
										<td colspan="4">学位课程： <c:forEach items="${degreeList }"
												var="degreeCondition" varStatus="vs">
												<span style="padding-right: 20px">
													${degreeCondition.dictName}</span>
											</c:forEach>
										</td>
									</tr>

								</c:forEach>
							</tbody>
						</table>
						<c:forEach begin="0" end="${graduateionDatas.totalCount-1}"
							varStatus="status">
							<a href="#" onclick="pageJumpDegree('${status.index+1 }')"><c:choose>
									<c:when test="${pageNum==status.index+1}">
										<font color="red">${status.index+1 }</font>
									</c:when>
									<c:otherwise>${status.index+1 }</c:otherwise>
								</c:choose> </a>&nbsp;
					</c:forEach>
						<button type="button" onclick="auditPassDegree()">审核通过</button>
					</div>

					<!-- 
					<div id="via">
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
										<!--之前注释<gh:select name="learningStyle" value="${studentInfo.learningStyle }" dictionaryCode="CodeLearningStyle" style="width:50%" />
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
									-->
					<!-- 之前注释
									<td style="width:20%">办学模式:</td>
									<td style="width:30%">${ghfn:dictCode2Val('teachingType',studentInfo.teachingPlan.schoolType) }</td>
									-->
					<!-- 
								</tr>
								<tr>
									<td colspan="4">学位课程：
									<c:forEach items="${degreeList }" var="degreeCondition" varStatus="vs">									
									<span style="padding-right:20px">
									 -->
					<!--之前注释 <input type="checkbox" name="degreeRules" value="${degreeCondition.dictValue }"
									
									<c:forTokens items="${teachingPlan.degreeRules}" delims="," var="degreeRule" varStatus="status">
									<c:if test="${degreeRule eq degreeCondition.dictValue }">checked</c:if>
									
									</c:forTokens>
 									/> 
 									 -->
					<!-- 
 									${ degreeCondition.dictName}</span>
									</c:forEach>
									</td>
								</tr> -->
					<!--之前注释 
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
							 -->
					<!-- 
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
					
				</div>
				 -->

					<div class="tabsFooter">
						<div class="tabsFooterContent"></div>
					</div>
				</div>

				<!-- end tabs -->
			</div>
		</div>
	</div>



</body>

</html>