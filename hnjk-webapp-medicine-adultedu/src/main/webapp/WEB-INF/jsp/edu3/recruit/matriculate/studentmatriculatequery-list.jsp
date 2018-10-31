<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<c:if test="${ empty fromFlag }">
	<gh:loadCom components="jquery,framework" />
	<link href="${baseUrl }/themes/css/luqu.css" rel="stylesheet" type="text/css" />
</c:if>
<title>${schoolCode eq '10571'?'分点查询':'录取查询' }</title>
</head>
<style>
.queryLable{font-size: 19px;color: black;font-weight: bold;height: 30px};
</style>
<script type="text/javascript">	
	//获取手机短信验证码
	//timer变量，控制时间
	var interValObj;
	var remainTime;
	var interValObj4simdown;
	var remainTime4simdown;
	var buttonValue;
	$(function(){
		//$("#matriculatequery_from input[name=firstOpen]").attr("type","hidden");
		//remainTime4simdown = 0;
		//$("#simdown").attr("disabled","disabled");
		//interValObj4simdown = window.setInterval(setRemainTime4simdown, 1000); // 启动计时器，1秒执行一次
		var school=$("#school").val();
		var schoolCode = "${schoolCode}";
		if(schoolCode == '11078'){
			buttonValue = "下载入学须知";
		}else{
			buttonValue = "下载录取通知书及入学须知";
		}
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
	
	//timer处理函数
	function setRemainTime() {
		if (remainTime == 0) {                
			window.clearInterval(interValObj);// 停止计时器
			$("#matriculatequery_getMsgAuthCode").removeAttr("disabled");// 启用按钮
			$("#matriculatequery_getMsgAuthCode").text("发送");
		}else {
			remainTime--;
			$("#matriculatequery_getMsgAuthCode").text(remainTime+"秒后重发");
		}
	}
	function setRemainTime4simdown(){
		if (remainTime4simdown == 0) {                
			window.clearInterval(interValObj4simdown);// 停止计时器
			$("#simdown").removeAttr("disabled");// 启用按钮
			$("#simdown").text(buttonValue);
		}else {
			remainTime4simdown--;
			$("#simdown").text(remainTime4simdown+1+"秒后可下载");
		}
	}
</script>

<body style="color: black;">
	<c:choose>
		<c:when test="${empty fromFlag }">
			<div id="query_main">
				<div class="leftside" style="margin-left: 10px; height: 1090px;">
					<div class="list01">
						<form id="matriculatequery_from" onsubmit="" action="${baseUrl }/cx.html" method="post">
							<input type="hidden" name="firstOpen" value="Y" >
							<table style="width: 500px;margin-left: 265px;">
								<tr>
									<td class="queryLable">姓名：</td>
									<td><input type="text" id="studentName" name="studentName"
										value="${condition['studentName']}" /> <span
										style="color: red;">*</span></td>
								</tr>
								<%-- 下次注释掉--%>
								<!-- <tr>
									<td colspan="2" style="color: red; font-weight: bold;">注：身份证号码中的英文字母要大写，如：x
										-> X</td>
								</tr> -->
								<tr>
									<td class="queryLable">身份证号码：</td>
									<td><input type="text" id="certNum" name="certNum"
										value="${condition['certNum']}" /> <span style="color: red;">*</span>
									</td>
								</tr>
								<c:if test="${schoolCode ne '10571'}">
									<tr>
										<td class="queryLable">考生号：</td>
										<td><input type="text" id="enrolleeCode"
											name="enrolleeCode" value="${condition['enrolleeCode']}" /> <input
											type="hidden" id="school" value="${school }"></td>
	
									</tr>
								</c:if>
								<tr>
									<td colspan="2" >
										<button type="submit" onclick="return mariclateValidate()"
											style="cursor: pointer;font-size: 16px;">查 询</button>
									</td>
								</tr>
							</table>
						</form>
						<table width="900" >
							<tr>
								<td colspan="4" style="text-align: center;height: 100px"><strong> <font color="black" size="6">${message}</font>
								</strong></td>
							</tr>
							<c:if test="${ei!=null}">
								<tr>
									<td width="">姓&nbsp;&nbsp;&nbsp;名:</td>
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
										<c:when test="${schoolCode eq '10598' }">
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
										<c:if test="${ei.branchSchool.unitName eq '未分配'}"> style="color:black;font: 25;font-weight: bolder;"</c:if>>
										<!-- 不显示教学点 -->
										<c:if test="${schoolCode ne '10600' && schoolCode ne '10602'}">教学站:</c:if></td>
									</td>
									<td colspan="3"><c:if
											test="${fn:contains(ei.branchSchool.unitName,'N')}">
											${fn:replace(ei.branchSchool.unitName,"N","")}
										</c:if> <c:if test="${fn:contains(ei.branchSchool.unitName,'W')}">
											${fn:replace(ei.branchSchool.unitName,"W","")}
										</c:if>
										<c:choose>
											<c:when test="${schoolCode eq '10600' || schoolCode eq '10602'}"></c:when>
											<c:otherwise>
												<c:if test="${ei.branchSchool.unitName eq '未分配'}">
													<c:choose>
														<c:when test="${schoolCode eq '10602'}"></c:when>
														<c:when test="${schoolCode ne '11078' && schoolCode ne '10598' }">
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
														</c:when>
														<c:otherwise>
															${ei.branchSchool.unitName}
														</c:otherwise>
													</c:choose>
												</c:if>
												<c:if test="${ei.branchSchool.unitName ne '未分配'}">
													${ei.branchSchool.unitName}
												</c:if>
											</c:otherwise>
										</c:choose>
										 <%--  <c:choose>
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
								<tr>
									<c:choose>
										<c:when test="${schoolCode eq '10600' || schoolCode eq '10571'}">
											<td>教学点联系电话:</td>
											<td>${ei.branchSchool.contectCall }</td>
										</c:when>
										<c:when test="${schoolCode eq '10602' }">
											<td>联系人:</td>
											<td>${ei.branchSchool.linkman }</td>
											<td>教学点联系电话:</td>
											<td>${ei.branchSchool.contectCall }</td>
											</tr><tr>
										</c:when>
										<c:when test="${phoneComfirm!='1' }">
											<td>联系电话:</td>
											<td><input type="text"
											id="matriculatequery_contactPhone"
											value="${ei.studentBaseInfo.contactPhone }"
											name="contactPhone" size="10"
											style="border: 1px; border-bottom-style: none; border-top-style: none; border-left-style: none; border-right-style: none; text-decoration: underline;" />
											<a href="javascript:void(0)"
											onclick="return editContact('contactPhone');">修改</a></td>
										</c:when>
										<c:otherwise>
										</c:otherwise>
									</c:choose>
											
									<c:choose>
										<c:when test="${schoolCode eq '10600' || schoolCode eq '10571'}">
											<td>报到时间:</td>
											<td>${ei.branchSchool.reportTime }</td>
										</c:when>
										<c:when test="${schoolCode eq '10602' }">
												<td>报到时间:</td>
												<td>${ei.branchSchool.reportTime }</td>
												<td>报到地点:</td>
												<td>${ei.branchSchool.reportSite }</td>
										</c:when>
										<c:when test="${phoneComfirm=='1' }">
											<td width="">手&nbsp;&nbsp;&nbsp;&nbsp;机:</td>
											<td width="" colspan="3"><input type="text"
												id="matriculatequery_mobile"
												value="${ei.studentBaseInfo.mobile }" name="mobile"
												size="10" class="phone" style="text-decoration: underline;" />
												验证码:<input type="text" id="matriculatequery_authCode"
												name="authCode" size="4" /> <img
												src="${baseUrl}/imageCaptcha"
												id="matriculatequery_authCodeImg"
												style="margin-bottom: -6px; padding-left: 3px; cursor: pointer;"
												onclick="this.src='${baseUrl}/imageCaptcha?now=' + new Date().getTime()"
												title="换一张"> 手机验证码:<input type="text"
												id="matriculatequery_msgAuthCode" name="msgAuthCode"
												size="5" />
												<button id="matriculatequery_getMsgAuthCode"
													onclick="return getMsgAuthCode();" style="cursor: pointer;">发送</button>
												<button onclick="return editContact('mobile');"
													style="cursor: pointer;">提交</button>（本人手机号码如有变动，请及时修改）</td>
										</c:when>
										<c:when test="${phoneComfirm!='1' }">
											<td width="">手&nbsp;&nbsp;&nbsp;&nbsp;机:</td>
											<td width=""><input type="text"
												id="matriculatequery_mobile"
												value="${ei.studentBaseInfo.mobile }" name="mobile"
												size="10" class="phone"
												style="border: 1px; border-bottom-style: none; border-top-style: none; border-left-style: none; border-right-style: none; text-decoration: underline;" />
												<a href="javascript:void(0)"
												onclick="return editContact('mobile');">修改</a>（本人手机号码如有变动，请及时修改）
											</td>
										</c:when>
										<c:otherwise>
										</c:otherwise>
									</c:choose>
								</tr>
								<c:if test="${schoolCode eq '10602' &&  ei.branchSchool.unitName eq '未分配'}">
									<tr><td colspan="4">备注：已录取为未分配状态的考生请持本人身份证到广西师范大学继续教育学院招生办确定归属学院或教学点事宜。</td></tr>
								</c:if>
							</c:if>
						</table>
						<br/>
						<c:choose>
							<c:when
								test="${schoolCode  eq '10598' or schoolCode eq '11078' or schoolCode eq '10599' or schoolCode eq '10600' or schoolCode eq '10602'}">
								<c:if test="${not empty ei.matriculateNoticeNo}">
									<button onclick="simdown();" id="simdown" 
										style="height: 35px; font-size: 16px; font-style: inherit; cursor: pointer; margin: auto;">${schoolCode eq '11078'?'下载入学须知':'下载录取通知书及入学须知'}</button>
									<c:if test="${schoolCode eq '10600' }">
										<button onclick="exit();"
										style="height: 35px; font-size: 16px; font-style: inherit; cursor: pointer; margin-right:50px;color: red;font-weight: bold;">退出</button>
									</c:if>
									<br />
									<br />
								</c:if>
								<c:if test="${not empty ei.matriculateNoticeNo}">
									<img
										src="${basePath }/TheAdmissionNotice/${noticeSubDir}/${ei.matriculateNoticeNo}.jpg">
								</c:if>
							</c:when>
							<c:when test="${schoolCode  eq '10366' or schoolCode eq '11846'}">
								<c:if test="${not empty ei.matriculateNoticeNo}">
									<img
										src="${basePath }/TheAdmissionNotice/${noticeSubDir}/${ei.matriculateNoticeNo}.jpg">
								</c:if>
								<c:if test="${not empty firstOpen }">
									<div style="color: red; font-weight: bold; margin-top: 10px; margin-bottom: 10px;">请务必查看和下载以下材料：</div>
								</c:if>
								<c:forEach items="${dictionaryList}" var="dict" varStatus="vs">
									<a href="${baseUrl}/portal/site/article/download1.html?id=${dict.resourceid}&noticeNo=${ei.matriculateNoticeNo}&subdir=${noticeSubDir}"
										style="color: blue;">${dict.dictValue }</a>
									<br/>
								</c:forEach>
								<label></label>
							</c:when>
							<c:otherwise>
							</c:otherwise>
						</c:choose>
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
<script type="text/javascript">	
	
	//修改联系方式 
	function editContact(editFlag){
		var enrolleeCode 	  =  "${condition['enrolleeCode']}";
		var certNum           =  "${condition['certNum']}"; 
		var editResult        =  $("#matriculatequery_"+editFlag).val();
		// 判断是否输入短信验证码
		var phoneComfirm = "${phoneComfirm}";
		var msgAuthCode = $("#matriculatequery_msgAuthCode").val();
		if(phoneComfirm=='1'){
			if(!msgAuthCode){
				alert("短信验证码不能为空！");
				return false;
			}
		}
		
		if(editFlag =='mobile' &&  !isPhoneLegal(editResult)){
			alert("不合法的手机号！");
			return false;
		}
		if(editFlag =='contactPhone' && !(/(^[0-9]{3,4}\-[0-9]{3,8}$)|(^[0-9]{3,8}$)|(^\([0-9]{3,4}\)[0-9]{3,8}$)|(^[0-9]{3,4}[0-9]{3,8}$)|(^0{0,1}13[0-9]{9}$)/.test(editResult))){
			alert("不合法的电话号码!");
			return false;
		}
		$.ajax({
	          type:"POST",
	          url:"${baseUrl}/edu3/recruit/matriculate/studentmatriculatequery-edit.html",
	          data:{editFlag:editFlag,editResult:editResult,enrolleeCode:enrolleeCode,certNum:certNum,msgAuthCode:msgAuthCode},
	          dataType:'json',       
	          success:function(date){          	   		
	         		 if(date.success == 'Y'){         		 	
	         			$("#matriculatequery_from").submit();
	         		 }
	         		 alert(date.msg);
	         		        
	          }            
		});
	}
	//修改联系地址
	function editAddress(){
		var address = $("#contactAddress").val();
		var certNum           =  "${condition['certNum']}"; 
		
		$.ajax({
	          type:"POST",
	          url:"${baseUrl}/edu3/recruit/matriculate/studentmatriculatequery-editAddress.html",
	          data:{address:address,certNum:certNum},
	          dataType:'json',       
	          success:function(date){          	   		
	         		 if(date.success == 'Y'){         		 	
	         			$("#matriculatequery_from").submit();
	         		 }
	         		 alert(date.msg);
	          }            
		});
	}
	function editQQ(){
		alert("该功能暂未开放！");
		return false;
		  var reg=/^\d{5,12}$/;    
			   
		var qq = $("#contactqq").val();
		if(!reg.test(qq)){   
		    alert("请输入你正确的QQ号");   
		    return false;   
		  }  
		var certNum           =  "${condition['certNum']}"; 
		
		$.ajax({
	          type:"POST",
	          url:"${baseUrl}/edu3/recruit/matriculate/studentmatriculatequery-editQq.html",
	          data:{qq:qq,certNum:certNum},
	          dataType:'json',       
	          success:function(date){          	   		
	         		 if(date.success == 'Y'){         		 	
	         			$("#matriculatequery_from").submit();
	         		 }
	         		 alert(date.msg);
	          }            
		});
	}
	
	function updateBr(){
		var ids=$("#ids").attr("value");

		var brSchoolid=$("#brSchoolid").val();
		var recruitPlanId = "${ei.recruitMajor.recruitPlan.resourceid}";
		$.ajax({
	          type:"POST",
	          url:"${baseUrl}/edu3/recruit/exameeinfo/updateunit1.html",
	          data:{ids:ids,brSchoolid:brSchoolid,recruitPlanId:recruitPlanId},
	          dataType:'json',       
	          success:function(date){  
	        	  
	         		 if(date == 'Y'){         		 	
	         			$("#matriculatequery_from").submit();
	         		 }
	         		 alert("保存办学单位成功！");
	         		        
	          }            
		});
	}
	//附件下载
	function downloadAttachFile(attid){
		
		var url = '${baseUrl}/edu3/framework/filemanage/public/download.html?id='+attid;
		var elemIF = document.createElement("iframe");  
		elemIF.src = url;
		elemIF.style.display = "none"; 
		document.body.appendChild(elemIF);

	}
	
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
	
	function simdown(){
		var shoolCode = "${schoolCode}";
		var subDir = "${noticeSubDir}";
		var url = '${baseUrl }/edu3/framework/filemanage/public/simpleDownload.html';
		var name = "${zipFile}" ;
		var resourceid = "${ei.resourceid}";
		if(shoolCode=="10598" || shoolCode=="10600" || shoolCode=="10602"){
		   url += '?name='+encodeURI(name)+'&subPath='+subDir;
		}else if(shoolCode=="11078"){
			url += '?name=入学须知.zip&subPath=downloads'; 
		}
		url += '&resourceid='+resourceid;
		downloadFileByIframe(url, "downloadNoticeZip");
	}
	
	function getMsgAuthCode(){
		var validateCode = $("#matriculatequery_authCode").val();
		if(!validateCode){
			alert("验证码不能为空！");
			return false;
		}
		var phone = $("#matriculatequery_mobile").val();
		if(!phone){
			alert("手机号码不能为空！");
			return false;
		}
		if( !isPhoneLegal(phone)){
			alert("不合法的手机号！");
			return false;
		}
		remainTime = 120;// 两分钟后重发 
		$("#matriculatequery_getMsgAuthCode").attr("disabled","disabled");
		interValObj = window.setInterval(setRemainTime("matriculatequery_getMsgAuthCode"), 1000); // 启动计时器，1秒执行一次
		$.ajax({
	        type:"POST",
	        url:"${baseUrl}/mas/getMsgAuthCode.html",
	        data:{phone:phone,authCode:validateCode},
	        dataType:'json',       
	        success:function(data){  
	       		 if(data.statusCode == 200){         	
	       		   // 刷新验证码
			      $("#matriculatequery_authCodeImg").attr("src","${baseUrl}/imageCaptcha?now="+ new Date().getTime());
	       		 }else{
	       			window.clearInterval(interValObj);// 停止计时器
	    			$("#matriculatequery_getMsgAuthCode").removeAttr("disabled");// 启用按钮
	    			$("#matriculatequery_getMsgAuthCode").text("发送");
	       			alert(data.message);
	       		 }
	        }            
		});
	}

	function exit(){
		//window.close();
		window.location.replace("http://cjy.gxtcmu.edu.cn/"); 
	}
	
</script>
</html>