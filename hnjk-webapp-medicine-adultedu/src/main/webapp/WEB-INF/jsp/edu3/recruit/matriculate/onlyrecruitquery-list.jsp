<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<c:if test="${ empty fromFlag }">
	<gh:loadCom components="jquery" />
	<link href="${baseUrl }/themes/css/luqu.css" rel="stylesheet"
		type="text/css" />
</c:if>
<title>录取查询</title>
<script type="text/javascript">	
	function mariclateValidate(){
		var studentName = $("#studentName").val();
		var cerNum = $("#certNum").val();
		
		if($.trim(studentName)==""){
			alert("请填写姓名！");
			return false;
		}
		if ($.trim(cerNum)==""){
			alert("请填写身份证号！");
			return false;
		}
		return true;
	}
	
			
	$(function(){
		var school=$("#school").val();
		//if(school=='广西医科大学'){
			var divcss = {
					background: "url(${baseUrl}/themes/default/images/login_new/loginbg123.jpg) center top no-repeat" ,
					width:"1030px", 
					height:"1750px",
					margin:"0 auto", 
					padding:0
				};
			$("#query_main").css(divcss);
			$(".footer").remove();
		//}
	});
	
</script>
</head>
<body>
	<c:choose>
		<c:when test="${empty fromFlag }">
			<div id="query_main" style="height: 890px">
				<div class="leftside" style="margin-left: 10px; height: 750px;">
					<div class="list01">
						<form id="matriculatequery_from"
							action="${baseUrl }/recruitQuery.html" method="post">
							<table style="width: 100%">
								<tr>
									<td>姓名：</td>
									<td><input type="text" id="studentName" name="studentName"
										value="${condition['studentName']}" /> <span
										style="color: red;">*</span></td>
								</tr>
								<tr>
									<td width="">身份证号码：</td>
									<td><input type="text" id="certNum" name="certNum"
										value="${condition['certNum']}" /> <span style="color: red;">*</span>
									</td>
								</tr>
								<tr>
									<td colspan="2">
										<button type="submit" onclick="return mariclateValidate()"
											style="cursor: pointer;">查 询</button>
									</td>
								</tr>
							</table>
						</form>
						<table width="700">
							<tr>
								<td colspan="4"><strong> <font color="red">${message}</font>
								</strong></td>
							</tr>
							<c:if test="${ei!=null}">
								<tr>
									<td width="">姓&nbsp;&nbsp;&nbsp;&nbsp;名:</td>
									<td width="">${ei.studentBaseInfo.name  }</td>
									<td width="">性&nbsp;&nbsp;&nbsp;&nbsp;别:</td>
									<td width="">${ghfn:dictCode2Val('CodeSex',ei.studentBaseInfo.gender)}</td>
								</tr>
								<tr>
									<td width="">考&nbsp;生&nbsp;号:</td>
									<td width="">${Isreplce eq 'Y' ? ei.examCertificateNo : ei.enrolleeCode }</td>
									<c:choose>
										<c:when test="${schoolCode eq '11078' }">
											<td width="">准考证号:<input
												id="matriculatequery_from_matriculateNoticeNo" type="hidden"
												value="${ei.matriculateNoticeNo }" /></td>
											<td width="">${Isreplce eq 'Y' ? ei.enrolleeCode : ei.examCertificateNo }</td>
										</c:when>
										<c:otherwise>
											<td width="">学&nbsp;&nbsp;&nbsp;&nbsp;号:</td>
											<td width="">${ei.matriculateNoticeNo }</td>
										</c:otherwise>
									</c:choose>
								</tr>
								<tr>
									<td width="">身份证号码:</td>
									<td width="">${ei.studentBaseInfo.certNum }</td>
									<td width="">形&nbsp;&nbsp;&nbsp;&nbsp;式:</td>
									<td width="">${ghfn:dictCode2Val('CodeTeachingType',ei.teachingType)}</td>
								</tr>
								<tr>
									<td width="">录取专业:</td>
									<td width="">${ei.recruitMajor.major.majorName }</td>
									<td width="">层&nbsp;&nbsp;&nbsp;&nbsp;次:</td>
									<td width="">${ei.recruitMajor.classic.classicName }</td>
								</tr>
								<tr>
									<td width=""
										<c:if test="${ei.branchSchool.unitName eq '未分配'}"> style="color:black;font: 25;font-weight: bolder;"</c:if>>教学站:</td>
									<td colspan="3"><c:if
											test="${fn:contains(ei.branchSchool.unitName,'N')}">
											${fn:replace(ei.branchSchool.unitName,"N","")}
										</c:if> <c:if test="${fn:contains(ei.branchSchool.unitName,'W')}">
											${fn:replace(ei.branchSchool.unitName,"W","")}
										</c:if> <c:if
											test="${ei.branchSchool.unitName eq '未分配' && schoolCode ne '11078' && schoolCode ne '10598'}">
											<form method="post" id="_imputDialogForm"
												action="${baseUrl}/edu3/recruit/exameeinfo/updateunit.html">
												<select class="flexselect" id="brSchoolid" name="brSchoolid"
													tabindex=1
													style="width: 250px; font: 25; font-weight: bolder; border-color: red;">${unitOption}</select>
												<input type="hidden" id="ids" name="ids"
													value="${fee1['RESOURCEID'] }"> <input
													type="button" value="提交" onclick="updateBr()" name="submit"
													style="font: 25; font-weight: bolder;" /> <span
													style="color: red; font: 25; font-weight: bolder;">（本着就近、自愿原则选择教学点，选择后无法更改，请谨慎操作）</span>
											</form>
										</c:if> <c:if
											test="${ei.branchSchool.unitName ne '未分配' || schoolCode eq '11078' || schoolCode eq '10598'}">
											${ei.branchSchool.unitName}
										</c:if> <%--  <c:choose>
											<c:when test="${ei.branchSchool.unitName eq '未分配' or schoolCode eq '11078'} ">
												<form method="post" id="_imputDialogForm" action="${baseUrl}/edu3/recruit/exameeinfo/updateunit.html">
													<select class="flexselect" id="brSchoolid" name="brSchoolid" tabindex=1 style="width:250px;font: 25;font-weight: bolder;border-color: red;">${unitOption}</select>
													<input type="hidden" id="ids" name="ids" value="${fee1['RESOURCEID'] }">
													<input type="button" value="提交" onclick="updateBr()" name="submit" style="font: 25;font-weight: bolder;" />
													<span style="color: red;font: 25;font-weight: bolder;">（本着就近、自愿原则选择教学点，选择后无法更改，请谨慎操作）</span>
												 </form>	
											</c:when>
											<c:otherwise>
												${ei.branchSchool.unitName}
											</c:otherwise>
										</c:choose> 
									</td> --%> <!--									<td > （本着就近、自愿原则选择教学点，选择后无法更改，请谨慎操作）</td>-->
								</tr>
							</c:if>

						</table>
						<br />
						<%-- 					<c:if test="${schoolCode  eq '10598' }"> --%>
						<%-- 						<c:if test="${not empty ei.matriculateNoticeNo}"> --%>
						<!-- 			 				<button onclick="simdown();" style=" height: 45px;font-size: 16px;font-style: inherit;cursor: pointer;margin:auto;">下载录取通知书及入学须知</button> -->
						<!-- 			 				<br/><br/> -->
						<%-- 				 		</c:if> --%>
						<%-- 				 	</c:if> --%>


						<%-- 					<c:if test="${not empty ei.matriculateNoticeNo}"> --%>
						<%-- 						 <img src="${basePath }/TheAdmissionNotice/${noticeSubDir}/${ei.matriculateNoticeNo}.jpg"> --%>
						<%-- 					 </c:if> --%>
						<%-- 					<c:if test="${AdmissionDocument4 eq 'Y' or AdmissionDocument4 eq 'y' }"> --%>
						<!-- 				  		<div style="color: red;font-weight:bold; margin-top:10px;margin-bottom: 10px;">请务必查看和下载以下材料：</div> -->
						<%-- 					</c:if> --%>
						<%-- 					 <c:if test="${(AdmissionDocument1 eq 'Y' or AdmissionDocument1 eq 'y') and classicId eq '5a402f0339764b8e013976677c820001' }"> --%>
						<%-- 						<a href="${baseUrl}/portal/site/article/download1.html?id=1" style="color:blue;">安徽医科大学成人教育学院2016级专升本新生入学须知</a><br/> --%>
						<%-- 					</c:if> --%>
						<%-- 					<c:if test="${(AdmissionDocument5 eq 'Y' or AdmissionDocument5 eq 'y') and classicId eq '5a402f0339764b8e013976687cfb0002'  }"> --%>
						<%-- 						<a href="${baseUrl}/portal/site/article/download1.html?id=5" style="color:blue;">安徽医科大学成人教育学院2016级专科新生报到须知</a><br/> --%>
						<%-- 					</c:if> --%>
						<%-- 					<c:if test="${AdmissionDocument2 eq 'Y' or AdmissionDocument2 eq 'y' }"> --%>
						<%-- 						 <a href="${baseUrl}/portal/site/article/download1.html?id=2" style="color:blue;">新生登记表</a><br/> --%>
						<%-- 					</c:if> --%>
						<%-- 					<c:if test="${(AdmissionDocument3 eq 'Y' or AdmissionDocument3 eq 'y') and classicId eq '5a402f0339764b8e013976677c820001' }"> --%>
						<%-- 						<a href="${baseUrl}/portal/site/article/download1.html?id=3" style="color:blue;">专升本学生有效毕业文凭保证书</a><br/> --%>
						<%-- 					</c:if> --%>
						<%-- 					<c:if test="${AdmissionDocument4 eq 'Y' or AdmissionDocument4 eq 'y' }"> --%>
						<%-- 						 <a href="${baseUrl}/portal/site/article/download1.html?id=${ei.matriculateNoticeNo}&subdir=${noticeSubDir}" style="color:blue;">安徽医科大学成人高等教育新生录取通知书</a><br/> --%>
						<%-- 					</c:if> --%>
					</div>
				</div>

				<!--		<div class="rightside" style="border-left: 2px dotted #eee;">-->
				<!--				<table width="100%" style="margin-left: 10px">-->
				<!--					<c:if test="${not empty fee }">-->
				<!--					<tr><td colspan="2" > <B>学费 :</B></td></tr>-->
				<!--					<tr><td colspan="2">本年学费为<span style="color:red;">${fee['recpayfee'] }元</span></td></tr>-->
				<!--					<tr><td colspan="2" > </td></tr>-->
				<!--					<tr><td colspan="2" > </td></tr>-->
				<!--					</c:if>-->

				<!--					<c:if test="${fn:length(attachList)>0 }">-->
				<!--						<tr><td colspan="2"> <B>入学须知 :</B></td></tr>-->
				<!--						<tr>-->
				<!--						<td colspan="2" >-->
				<!--							<c:forEach items="${attachList }" var="att">-->
				<!--								<p><a  onclick="downloadAttachFile('${att.resourceid}')" href="#">${att.attName}</a></p>-->
				<!--							</c:forEach>-->
				<!--						</td>-->
				<!--						</tr>-->
				<!--					</c:if>-->
				<!--				</table>	-->
				<!--			</div>-->
				<div class="footer">
					Copyright&nbsp;&copy;&nbsp;2001-2013 ${school}${schoolConnectName}
					All Rights Reserved
					<gh:version />
				</div>
			</div>
		</c:when>
		<c:otherwise>
			<!--		<div class="page">-->
			<!--			<div class="pageHeader">-->
			<!--				<form  id="matriculatequery_from" onsubmit="return navTabSearch(this);" action="${baseUrl }/edu3/recruit/matriculate/studentmatriculatequery-list.html" method="post">-->
			<!--				<div class="searchBar">-->
			<!--					<ul class="searchContent">-->
			<!--			            <li>  -->
			<!--			                 	考生号：<input  type="text" id="enrolleeCode" name="enrolleeCode"   value="${condition['enrolleeCode']}"/>-->
			<!--						</li>				-->
			<!--			            <li>	-->
			<!--			             	          证件号码：<input  type="text" name="certNum" value="${condition['certNum']}"  />-->
			<!--					     </li>-->
			<!--						<li>-->
			<!--							<button type="submit">查 询</button>-->
			<!--						</li>-->
			<!--					</ul>-->
			<!--				</div>-->
			<!--				</form>-->
			<!--			</div>-->
			<!--			<div class="pageContent">-->
			<!--				<table boder='0' layouth="138">-->
			<!--					<tr>-->
			<!--						<td colspan="2" >-->
			<!--							<c:if test="${currentUser.orgUnit.unitCode != 'DEPT_RECRUIT' }">-->
			<!--								<strong> <font color="red">${message}</font> </strong>-->
			<!--							</c:if>-->
			<!--						</td>-->
			<!--					</tr>-->
			<!--				 		<c:if test="${ei!=null}">-->
			<!--	-->
			<!--						<tr>-->
			<!--							<td width="13%">姓&nbsp;&nbsp;&nbsp;&nbsp;名:</td>-->
			<!--							<td>${ei.studentBaseInfo.name  }</td>-->
			<!--						</tr>-->
			<!--						<tr>-->
			<!--							<td width="13%">性&nbsp;&nbsp;&nbsp;&nbsp;别:</td>-->
			<!--							<td>${ghfn:dictCode2Val('CodeSex',ei.studentBaseInfo.gender)}</td>-->
			<!--						</tr>-->
			<!--						-->
			<!--						<tr>-->
			<!--							<td width="13%">考&nbsp;生&nbsp;号:</td>-->
			<!--							<td>${ei.enrolleeCode }</td>-->
			<!--						</tr>-->
			<!--						<tr>-->
			<!--							<td width="13%">学&nbsp;&nbsp;&nbsp;&nbsp;号:</td>-->
			<!--							<td>${ei.matriculateNoticeNo }</td>-->
			<!--						</tr>-->
			<!--						<tr>-->
			<!--							<td width="13%">证件号码:</td>-->
			<!--							<td>${ei.studentBaseInfo.certNum }</td>-->
			<!--						</tr>-->
			<!--						<tr>-->
			<!--							<td width="13%">形&nbsp;&nbsp;&nbsp;&nbsp;式:</td>-->
			<!--							<td>${ghfn:dictCode2Val('CodeTeachingType',ei.teachingType)}</td>-->
			<!--						</tr>-->
			<!--						<tr>-->
			<!--							<td width="13%">录取专业:</td>-->
			<!--							<td>${ei.recruitMajor.major.majorName }</td>-->
			<!--						</tr>-->
			<!--						<tr>-->
			<!--							<td width="13%">层&nbsp;&nbsp;&nbsp;&nbsp;次:</td>-->
			<!--							<td>${ei.recruitMajor.classic.classicName }</td>-->
			<!--						</tr>-->
			<!--						-->
			<!--						<tr>-->
			<!--							<td width="20%">联系电话:</td>-->
			<!--							<td><input type="text" id="matriculatequery_contactPhone" value="${ei.studentBaseInfo.contactPhone }" name="contactPhone" >-->
			<!--								<a href="javascript:void(0)" onclick="return editContact('contactPhone');">修改</a>-->
			<!--							</td>-->
			<!--						</tr>-->
			<!--						<tr>-->
			<!--							<td width="20%">移动电话:</td>-->
			<!--							<td><input type="text" id="matriculatequery_mobile" value="${ei.studentBaseInfo.mobile }" name="mobile" />-->
			<!--								<a href="javascript:void(0)" onclick="return editContact('mobile');">修改</a>-->
			<!--							</td>-->
			<!--						</tr>-->
			<!--						</c:if>-->
			<!--			</table>-->
			<!--			</div>	-->
			<!--		</div>-->
		</c:otherwise>
	</c:choose>
</body>
</html>
