<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>教师管理</title>

<script type="text/javascript">

$(function() {	
    $("#uploadify_teacherCertPhoto_images").uploadify({ //初始化图片上传
        'script'         : baseUrl+'/edu3/filemanage/upload.html', //上传URL
        'auto'           : true, //自动上传               
        'multi'          : false, //多文件上传
        'scriptData'	 : {'isStoreToDatabase':'N','storePath':'common,edumanager'},
        'fileDesc'       : '支持格式:jpg/gif',  //限制文件上传格式描述
        'fileExt'        : '*.JPG;*.GIF;', //限制文件上传的类型,必须有fileDesc这个性质
        'sizeLimit'      : 1048576, //限制单个文件上传大小1M 
        'buttonImg'      : baseUrl+'/jscript/jquery.uploadify/images/icon_upload.png', //按钮图片
        onComplete: function (event, queueID, fileObj, response, data) { //上传成功回调函数
        	var result = response.split("|");                	             	
        	$('#teacherCertPhoto_images').attr('src','${rootUrl}common/edumanager/'+result[1]);   
        	$('#teacherCertPhoto').val(result[1]);
		},  
		onError: function(event, queueID, fileObj) {  //上传失败回调函数
		    alert("文件" + fileObj.name + "上传失败"); 
		    $('#uploadify').uploadifyClearQueue(); //清空上传队列
		}
	});
	var faceSrc = '${user.teacherCertPhoto}';
	if(faceSrc != ''){
			$('#teacherCertPhoto_images').attr('src','${rootUrl}common/edumanager/${user.teacherCertPhoto}');
	}
	
	$("#uploadify_teacherCertPhoto_images1").uploadify({ //初始化图片上传
        'script'         : baseUrl+'/edu3/filemanage/upload.html', //上传URL
        'auto'           : true, //自动上传               
        'multi'          : false, //多文件上传
        'scriptData'	 : {'isStoreToDatabase':'N','storePath':'common,edumanager1'},
        'fileDesc'       : '支持格式:jpg/gif',  //限制文件上传格式描述
        'fileExt'        : '*.JPG;*.GIF;', //限制文件上传的类型,必须有fileDesc这个性质
        'sizeLimit'      : 1048576, //限制单个文件上传大小1M 
        'buttonImg'      : baseUrl+'/jscript/jquery.uploadify/images/icon_upload.png', //按钮图片
        onComplete: function (event, queueID, fileObj, response, data) { //上传成功回调函数
        	var result = response.split("|");                	             	
        	$('#teacherCertPhoto_images1').attr('src','${rootUrl}common/edumanager1/'+result[1]);   
        	$('#teacherCertPhoto1').val(result[1]);
		},  
		onError: function(event, queueID, fileObj) {  //上传失败回调函数
		    alert("文件" + fileObj.name + "上传失败"); 
		    $('#uploadify').uploadifyClearQueue(); //清空上传队列
		}
	});
	var faceSrc = '${user.teacherCertPhoto1}';
	if(faceSrc != ''){
			$('#teacherCertPhoto_images1').attr('src','${rootUrl}common/edumanager1/${user.teacherCertPhoto1}');
	}
	$("#uploadify_teacherCertPhoto_images2").uploadify({ //初始化图片上传
        'script'         : baseUrl+'/edu3/filemanage/upload.html', //上传URL
        'auto'           : true, //自动上传               
        'multi'          : false, //多文件上传
        'scriptData'	 : {'isStoreToDatabase':'N','storePath':'common,edumanager2'},
        'fileDesc'       : '支持格式:jpg/gif',  //限制文件上传格式描述
        'fileExt'        : '*.JPG;*.GIF;', //限制文件上传的类型,必须有fileDesc这个性质
        'sizeLimit'      : 1048576, //限制单个文件上传大小1M 
        'buttonImg'      : baseUrl+'/jscript/jquery.uploadify/images/icon_upload.png', //按钮图片
        onComplete: function (event, queueID, fileObj, response, data) { //上传成功回调函数
        	var result = response.split("|");                	             	
        	$('#teacherCertPhoto_images2').attr('src','${rootUrl}common/edumanager2/'+result[1]);   
        	$('#teacherCertPhoto2').val(result[1]);
		},  
		onError: function(event, queueID, fileObj) {  //上传失败回调函数
		    alert("文件" + fileObj.name + "上传失败"); 
		    $('#uploadify').uploadifyClearQueue(); //清空上传队列
		}
	});
	var faceSrc = '${user.teacherCertPhoto2}';
	if(faceSrc != ''){
			$('#teacherCertPhoto_images2').attr('src','${rootUrl}common/edumanager2/${user.teacherCertPhoto2}');
	}
	var other='${user.qualification }';
	if(other!=''){
		$("#isinput").val(1);
		document.getElementById("qualification").style.backgroundColor = "white";
		$("#qualification").removeAttr("readonly"); 
	}
	
	 $("#uploadify_teacherCertPhoto_images3").uploadify({ //初始化图片上传
	        'script'         : baseUrl+'/edu3/filemanage/upload.html', //上传URL
	        'auto'           : true, //自动上传               
	        'multi'          : false, //多文件上传
	        'scriptData'	 : {'isStoreToDatabase':'N','storePath':'common,edumanager3'},
	        'fileDesc'       : '支持格式:jpg/gif',  //限制文件上传格式描述
	        'fileExt'        : '*.JPG;*.GIF;', //限制文件上传的类型,必须有fileDesc这个性质
	        'sizeLimit'      : 1048576, //限制单个文件上传大小1M 
	        'buttonImg'      : baseUrl+'/jscript/jquery.uploadify/images/icon_upload.png', //按钮图片
	        onComplete: function (event, queueID, fileObj, response, data) { //上传成功回调函数
	        	var result = response.split("|");                	             	
	        	$('#teacherCertPhoto_images3').attr('src','${rootUrl}common/edumanager3/'+result[1]);   
	        	$('#teacherCertPhoto3').val(result[1]);
			},  
			onError: function(event, queueID, fileObj) {  //上传失败回调函数
			    alert("文件" + fileObj.name + "上传失败"); 
			    $('#uploadify').uploadifyClearQueue(); //清空上传队列
			}
		});
		var faceSrc = '${user.teacherCertPhoto3}';
		if(faceSrc != ''){
				$('#teacherCertPhoto_images3').attr('src','${rootUrl}common/edumanager3/${user.teacherCertPhoto3}');
		}

	var schoolCode = "${schoolCode}";
    if (schoolCode == '11846') {//广外
        $("#unitName").attr("class","required");
        $("#major_classicCode").attr("class","required");
        $("#edu_courseClasses").attr("class","required");
        //$("#edu_teacherType").attr("classCss","required");
        //$("#edu_hireType").attr("classCss","required");
        $("#edu_workPlace").attr("class","required");
        //$("#edu_specialty").attr("classCss","required");
        $("#edu_orgUnitType").attr("class","required");
        $("#edu_graduateDate").attr("class","required");
        $("#edu_graduateSchool").attr("class","required");
    }
});

	//判断是否有其他资格证，是否让其输入
	function isInput(){
		var options=$("#isinput");
		if(options.val()=='1'){

			document.getElementById("qualification").style.backgroundColor = "white";
			$("#teacherCertPhoto2").attr("class","required");
			$("#qualification").removeAttr("readonly");
		}else{
			document.getElementById("qualification").style.backgroundColor = "gray";
			$("#qualification").val("");
			$("#qualification").attr("readonly","readonly");
			$("#teacherCertPhoto2").removeAttr("class");
		}
	}

	//校验唯一性
 	function eduValidateOnlyUser(){
	var userid = $("#eduManagerEditForm #resourceid");
	//var userName = $("#eduManagerEditForm #username");
	var teachCode = $("#eduManagerEditForm #edu_teacherCode");
	if(teachCode.val()==""){ alertMsg.warn("请输入登录账号"); teachCode.focus() ;return false; }
	var url = "${baseUrl}/edu3/framework/system/org/user/validateCode.html";
	jQuery.post(url,{username:teachCode.val(),resourceid:userid.val()},function(data){
		if(data == "exist"){ alertMsg.warn("账号已存在!"); }else{ alertMsg.correct("恭喜，此账号可用！")}
	})
 }

	//验证教师编号是否可用
	function validateOnlyTeacherCode(){
	   var teacherCode = $("#eduManagerEditForm input[name='teacherCode']");
	   if($.trim(teacherCode.val())==""){ 
		   alertMsg.warn("请输入教师编号！"); 
		   teacherCode.focus() ;
		   return false; 
		}
	   	var url = "${baseUrl}/edu3/framework/system/teachercode/validateCode.html";
	   	jQuery.post(url,{teacherCode:teacherCode.val()},function(data){
	   		if(data == "exist"){
	   			alertMsg.warn("编号已存在!"); 
	   		}else{ 
	   			alertMsg.correct("恭喜，此编号可用！");
	   		}
	   	})
   }

	//选择组织树
	 function openOrgUnitTree(){
		var defaultValues = $("#eduManagerEditForm #unitId").val();
		orgUnitSelector('unitId','unitName','radio','',defaultValues);
		setTimeout(getTeacherCodeCode,5000);
	 }

	//获取教师编号
	 function getTeacherCodeCode() {
		 var userid = $("#eduManagerEditForm #resourceid").val();
		 if (userid == "" || userid == null || userid == undefined) {
			 var url = "${baseUrl}/edu3/framework/system/getTeacherCode.html";
			 jQuery.post(url,{unitId:$("#eduManagerEditForm #unitId").val()},function(data){
				 $("#edu_teacherCode").val(data);
			 })
		 }
	 }

	 function required1(t){
		 if($(t).val()!=0){
			$("#teacherCertPhoto").attr("class","required");
		 }else{
			 $("#teacherCertPhoto").removeAttr("class");
		 }
	 }
	 function required2(t){
		 if($(t).val()!=0){
			$("#teacherCertPhoto1").attr("class","required");
		 }else{
			 $("#teacherCertPhoto1").removeAttr("class");
		 }
	 }
	 function required3(t){
		 if($(t).val()>0){
			$("#teacherCertPhoto3").attr("class","required");
		 }else{
			 $("#teacherCertPhoto3").removeAttr("class");
		 }
	 }
 
 </script>
</head>
<body>
	<h2 class="contentTitle">编辑教师</h2>
	<div class="page">
		<div class="pageContent">
			<form method="post"
				action="${baseUrl}/edu3/teaching/edumanager/save.html" class="pageForm" id="eduManagerEditForm" onsubmit="return validateCallback(this);">
				<div class="pageFormContent" layoutH="97">
					<div class="tabs">
						<div class="tabsHeader">
							<div class="tabsHeaderContent">
								<ul>
									<%-- 
	<li><a href="#" onclick="void(1)"><span>账号信息</span></a></li>
	 --%>
									<li><a href="#" onclick="void(1)"><span>教师信息</span></a></li>
								</ul>
							</div>
						</div>
						<div class="tabsContent" style="height: 100%;">
							<%-- 
	<div>
	<!-- tab1 -->
	<table class="form">
		<input type="hidden" name="resourceid" id="resourceid"	value="${user.resourceid }"/>
		<input type="hidden" name="userType" value="${user.userType }"/>
		<tr>
			<td style="width:12%">所在单位: </td>
			<td colspan="3">			
			<input type="text" id="unitName" value="${user.orgUnit.unitName }" readonly="readonly"/>
			<input type="hidden" id="unitId" name="unitId" value="${user.orgUnit.resourceid }"/>	
			<c:if test="${not isBrschool }">		
			<span	class="buttonActive" style="margin-left: 8px">
			<div class="buttonContent">
			<button type="button" onclick="openOrgUnitTree();">选择组织单位</button>
			</div>
			</span>
			</c:if>
			</td>
		</tr>
		<tr>
			<td style="width: 12%">登录账号:</td>
			<td style="width: 38%">
			  <input type="text" name="username" id="username" size="34" value="${user.username }"	class="required alphanumeric" alt="请输入字母下划线或数字的组合" /> 
			  <span	class="buttonActive" style="margin-left: 8px">
			<div class="buttonContent">
			<button type="button" onclick="validateOnlyUser();">检查唯一性</button>
			</div>
			</span></td>
			<td>姓名:</td>
			<td><input type="text" name="cnName" size="40"	value="${user.cnName }" class="required" /></td>
		</tr>
		<tr>
		<td style="width: 12%">教师密码:</td>
		<td style="width: 38%"><input type="password" name="password"	value="${user.password }" class="required"></td>
		<td style="width: 12%">教师类型:</td>
		<td style="width: 38%">
			<c:forEach items="${roleList }" var="role" varStatus="vs">
				<label><input type="checkbox" name="teacherType" value="${role.roleCode }" 
				<c:forEach items="${user.roles }" var="r">
					<c:if test="${role.roleCode eq r.roleCode }">checked</c:if>
				</c:forEach>				
				class="required"/>${role.roleName }</label>
			</c:forEach>	
		</td>
		<tr>
				<td style="width: 12%">是否可用:</td>
				<td style="width: 38%">
				<gh:select name="enabledChar" value="${user.enabledChar}" dictionaryCode="yesOrNo"/> 				
				</td>
				<c:choose>
						<c:when test="${user.isDeleted == 1 }">
							<td style="width:12%">恢复该账号:</td>
							<td style="width:38%"><input type="checkbox" name="isDeleted" value="0"/> 恢复</td>	
						</c:when>
						<c:otherwise>
							<td style="width:12%">教学模式：</td>
							<td style="width:38%">
							<gh:select name="teachingType" value="${user.teachingType}"	dictionaryCode="CodeTeachingType" class="required"/>							
							</td>
						</c:otherwise>
				</c:choose>		
		</tr>
	</tr>
	</table>
	<p style="width:100%"><font color='red'>说明：<br/>						
		禁止用户账号登录使用系统，请选择'是否可用'为'否'；
		将删除的用户恢复到正常状态，请选择'恢复该账号'
		</font>
		</p>
	</div>
	 --%>
							<div>
								<!-- tab2 -->
								<input type="hidden" name="resourceid" id="resourceid"
									value="${user.resourceid }" /> <input type="hidden"
									name="userType" value="${user.userType }" />
								<table class="form">
									<tr>
										<%--
				<input type="text" name="teacherCode"	value="${user.teacherCode}" class="required alphanumeric"/>
				<a class="button" href="javascript:;" onclick="validateOnlyTeacherCode();"><span>检查唯一性</span> </a>--%>
										<%-- 
				<td style="width: 12%">姓名拼音:</td>
				<td style="width: 38%"><input type="text" name="namePY"	value="${user.namePY}" /></td>--%>
										<td style="width: 12%">姓名:</td>
										<td style="width: 38%"><input type="text" name="cnName" size="40"
											value="${user.cnName }" class="required" /> <input
											type="hidden" name="username" id="username"
											value="${user.username }" /></td>
										<td style="width: 12%">教师编号（登录账号）:</td>
										<td style="width: 38%">
											<c:choose>
												<c:when test="${schoolCode eq '11846'}">
													<input type="text" id="edu_teacherCode" name="teacherCode"	value="${user.teacherCode}" readonly="readonly" class="required"/>
												</c:when>
												<c:otherwise>
													<input type="text" name="teacherCode" value="${user.teacherCode}" id="edu_teacherCode" class="required" />
													<span class="buttonActive" style="margin-left: 8px">
												<div class="buttonContent">
													<button type="button" onclick="eduValidateOnlyUser();">检查唯一性</button>
												</div>
											</span>
												</c:otherwise>
											</c:choose>
										</td>
									</tr>
									<tr>
										<td>出生日期:</td>
										<td><input type="text" id="edu_birthday" name="birthday"
											value="${user.birthday}" class="required Wdate"
											onFocus="WdatePicker({isShowWeek:true})" />
										</td>
										<td>性别:</td>
										<td><gh:select name="gender"
												value="${user.gender}" dictionaryCode="CodeSex"
												classCss="required" /><span style="color: red;">*</span></td>
									</tr>
									<tr>
										<td>教师选聘教学点:</td>
										<td><input type="text" id="unitName"
											value="${user.orgUnit.unitName }" readonly="readonly" /> <input
											type="hidden" id="unitId" name="unitId"
											value="${user.orgUnit.resourceid }" /> <c:if
												test="${not isBrschool }">
												<span class="buttonActive" style="margin-left: 8px">
													<div class="buttonContent">
														<button type="button" onclick="openOrgUnitTree();">选择组织单位</button>
													</div>
												</span>
											</c:if></td>
										<td>身份证号码 :</td>
										<td><c:choose>
												<c:when test="${schoolCode  eq '11078' or schoolCode eq '11846'}">
													<input type="text" name="certNum" value="${user.certNum }"
														class="required idnumber" style="width:200px;"/>
												</c:when>
												<c:otherwise>
													<input type="text" name="certNum" value="${user.certNum }" style="width:200px;"/>
												</c:otherwise>
											</c:choose></td>
									</tr>
									<tr>
										<td>职称:</td>
										<td><c:choose>
												<c:when test="${schoolCode  eq '11078' or schoolCode eq '11846'}">
													<gh:select name="titleOfTechnical"
														value="${user.titleOfTechnical}" classCss="required"
														dictionaryCode="CodeTitleOfTechnicalCode" style="width:120px;"/>
													<span style="color: red;">*</span>
												</c:when>
												<c:otherwise>
													<gh:select name="titleOfTechnical"
														value="${user.titleOfTechnical}"
														dictionaryCode="CodeTitleOfTechnicalCode" />
												</c:otherwise>
											</c:choose></td>
										<td>当前担任课程 :</td>
										<td><c:choose>
												<c:when test="${schoolCode  eq '11078' or schoolCode eq '11846'}">
													<gh:courseAutocomplete id="currentCourse" name="courseId"
														tabindex="1" classCss="required" style="width:200px;"
														value="${user.currentCourse.resourceid }" />
												</c:when>
												<c:otherwise>
													<gh:courseAutocomplete id="currentCourse" name="courseId"
														tabindex="1" style="width:200px;" value="${user.currentCourse.resourceid }" />
												</c:otherwise>
											</c:choose></td>
									</tr>
									<!-- 广外个性化编辑模版 -->
									<tr>
									<c:choose>
										<c:when test="${schoolCode eq '11846' || schoolCode eq '11910'}">
												<td>担任课程层次:</td>
												<td><gh:selectModel id="major_classicCode" name="courseClassic"
														bindValue="classicName" displayValue="classicName"
														modelClass="com.hnjk.edu.basedata.model.Classic" classCss="required"
														style="width:120px;" value="${user.courseClassic}" />
													<span style="color: red;">*</span>
									 			</td>
									 			<td>教师类型:</td><!-- （专职或兼职） -->
									 			<td><gh:select id="edu_teacherType" name="teacherType" value="${user.teacherType}" classCss="required" dictionaryCode="CodeTeacherType" style="width:120px;"/>
													<span style="color: red;">*</span>
												</td>
											</tr>
											<tr>
												<td>授课班级:</td><!-- 以文本框输入的形式 -->
												<td style="width: 50%"><input type="text" id="edu_courseClasses" name="courseClasses" style="width: 200px;" value="${user.courseClasses }" />
												</td>
												<td>聘用类型:</td><!-- 首次或续聘 -->
												<td><gh:select id="edu_hireType" name="hireType" value="${user.hireType}" classCss="required" dictionaryCode="CodeHireType" style="width:120px;" />
													<span style="color: red;">*</span>
												</td>
											</tr>
											<tr>
												<td>现工作单位:</td>
												<td style="width: 50%"><input type="text" id="edu_workPlace" name="workPlace" style="width: 200px;" value="${user.workPlace }" /></td>
												<td>所学专业学科类别:</td>
												<td><gh:select id="edu_specialty" name="specialty" value="${user.specialty}" classCss="required" dictionaryCode="CodeSpecialty" style="width:120px;"/>
													<span style="color: red;">*</span>
												</td>
										</c:when>
										<c:otherwise>
											<input type="hidden" name="teacherType" value="${user.teacherType }" />
											<td>文号:</td>
											<td><input type="text"
												name="documentcode" value="${user.documentcode }" /></td>
											<td colspan="2"></td>
										</c:otherwise>
									</c:choose>
									</tr>

									<tr>
										<td>当前担任课程属于：</td>
										<td><c:choose>
												<c:when test="${schoolCode  eq '11078' or schoolCode eq '11846'}">
													<gh:select name="currentCourseType"
														filtrationStr="1100,1500,1600,3001"
														value="${user.currentCourseType}"
														dictionaryCode="courseNature" classCss="required" style="width:120px;"/>
													<span style="color: red;">*</span>
												</c:when>
												<c:otherwise>
													<gh:select id="edu_currentCourseType" name="currentCourseType"
														filtrationStr="1100,1500,1600,3001"
														value="${user.currentCourseType}"
														dictionaryCode="courseNature" />
												</c:otherwise>
											</c:choose>
											<span style="color: red;">&nbsp;&nbsp;只可选一项，如都有担任，则选择学时数多的课程</span>
										</td>
									</tr>
									<tr>
										<td>手机:</td>
										<td><input type="text" name="mobile"
											value="${user.mobile }" class="required mobile" style="width:120px;"/></td>
										<td>E-mail:</td>
										<td><c:choose>
												<c:when test="${schoolCode  eq '11078'}">
													<input type="text" name="email" value="${user.email }"
														class="required email" style="width: 200px;"/>
												</c:when>
												<c:otherwise>
													<input type="text" name="email" value="${user.email }" style="width: 200px;"/>
												</c:otherwise>
											</c:choose></td>
										
									</tr>
									<tr>
										<td>职级:</td>
										<td><c:choose>
												<c:when test="${schoolCode  eq '11078' or schoolCode eq '11846'}">
													<gh:select name="positionLevel"
														value="${user.positionLevel}" classCss="required"
														dictionaryCode="CodeRank" style="width:120px;"/>
													<span style="color: red;">*</span>
												</c:when>
												<c:otherwise>
													<gh:select name="positionLevel"
														value="${user.positionLevel}" dictionaryCode="CodeRank" style="width:120px;"/>
												</c:otherwise>
											</c:choose></td>
										<td>首次聘用时间:</td>
										<td><c:choose>
												<c:when test="${schoolCode  eq '11078' or schoolCode eq '11846'}">
													<input type="text" name="hiringTime"
														value="${user.hiringTime }" class="required" class="Wdate"
														onFocus="WdatePicker({dateFmt:'yyyy-MM-dd'})" />
												</c:when>
												<c:otherwise>
													<input type="text" name="hiringTime"
														value="${user.hiringTime }" class="Wdate"
														onFocus="WdatePicker({dateFmt:'yyyy-MM-dd'})" />
												</c:otherwise>
											</c:choose></td>
										
									</tr>
									<tr>
										<td>是否班主任：</td>
										<td><input type="radio" name="isMaster" value="N"
											<c:if test="${user.isMaster eq 'N' or empty user.isMaster }">checked="checked"</c:if> />否
											<input type="radio" name="isMaster" value="Y"
											<c:if test="${user.isMaster eq 'Y' }">checked="checked"</c:if> />是
										</td>
										<td>是否在线教学</td>
										<td><input type="radio" name="isOnline" value="N"
											<c:if test="${user.isOnline eq 'N' or empty user.isOnline }">checked="checked"</c:if> />否
											<input type="radio" name="isOnline" value="Y"
											<c:if test="${user.isOnline eq 'Y' }">checked="checked"</c:if> />是
										</td>
									</tr>
									<tr>
										<td>最高学历 :</td>
										<td><gh:select
												name="educationalLevel" value="${user.educationalLevel}"
												onchange="required3(this)" filtrationStr="1,2,3,4,5"
												dictionaryCode="CodeHighestEducation" />
											<span style="color: red;">*</span>
										</td>
										<td colspan="2"><img id="teacherCertPhoto_images3" src="${baseUrl }/themes/default/images/img_preview.png" width="180" height="140" />
											<div id="filelist3"></div> <input name="uploadify_teacherCertPhoto_images3" id="uploadify_teacherCertPhoto_images3" type="file" />
											<span style="color: red;">文件大小不能超过1M</span>
											<input type="hidden" name="teacherCertPhoto3" id="teacherCertPhoto3" value="${user.teacherCertPhoto3 }" />
										</td>
									</tr>
									<tr>
										<td>是否有教师资格证:</td>
										<td><gh:select
												name="isTeacherCert"
												value="${empty user.isTeacherCert ?0:user.isTeacherCert}"
												classCss="required" onchange="required1(this)"
												dictionaryCode="CodeTeacherCertification"
												style="width:120px;" />
											<span style="color: red;">*</span>
										</td>
										<td colspan="2"><img id="teacherCertPhoto_images"
											src="${baseUrl }/themes/default/images/img_preview.png"
											width="180" height="140" />
											<div id="filelist"></div> <input
											name="uploadify_teacherCertPhoto_images"
											id="uploadify_teacherCertPhoto_images" type="file" /><span
											style="color: red;">文件大小不能超过1M</span> <input type="hidden"
											name="teacherCertPhoto" id="teacherCertPhoto"
											value="${user.teacherCertPhoto }" /></td>
									</tr>
									<tr>
										<td>最高获得学位：</td>
										<td><gh:select name="degree"
												value="${empty user.degree?0:user.degree}"
												onchange="required2(this)"
												dictionaryCode="CodeExternalDegree" />
											<span style="color: red;">*</span>
										</td>
										<td colspan="2"><img id="teacherCertPhoto_images1"
											src="${baseUrl }/themes/default/images/img_preview.png"
											width="180" height="140" />
											<div id="filelist1"></div> <input
											name="uploadify_teacherCertPhoto_images1"
											id="uploadify_teacherCertPhoto_images1" type="file" /> <span
											style="color: red;">文件大小不能超过1M</span> <input type="hidden"
											name="teacherCertPhoto1" id="teacherCertPhoto1"
											value="${user.teacherCertPhoto1 }" /></td>
									</tr>
									<tr>
										<td>其他职业（执业）资格:</td>
										<td><select onchange="isInput()" id="isinput"
											name="OtherPractice">
												<option value="1">有</option>
												<option value="0" selected="selected">无</option>
										</select> <input type="text" readonly="readonly"
											style="background-color: gray" value="${user.qualification }"
											name="qualification" id="qualification" />
											<span style="color: red;">*</span>
										</td>
										<td>其他职业（执业）资格证：</td>
										<td><img id="teacherCertPhoto_images2"
											src="${baseUrl }/themes/default/images/img_preview.png"
											width="180" height="140" />
											<div id="filelist2"></div> <input
											name="uploadify_teacherCertPhoto_images2"
											id="uploadify_teacherCertPhoto_images2" type="file" /> <span
											style="color: red;">文件大小不能超过1M</span> <input type="hidden"
											name="teacherCertPhoto2" id="teacherCertPhoto2"
											value="${user.teacherCertPhoto2 }" /></td>
									</tr>
									<tr>
										<td>编制:</td>
										<td><gh:select id="edu_orgUnitType" name="orgUnitType"
												value="${user.orgUnitType}"
												dictionaryCode="teacherOrgUnitType" />
											<span style="color: red;">*</span></td>
										<td>是否其它高校教师：</td>
										<td><select name="isOtherTeacher">
												<option
													<c:if test="${user.isOtherTeacher==0 }">selected="selected"</c:if>
													value="0">否</option>
												<option
													<c:if test="${user.isOtherTeacher==1 }">selected="selected"</c:if>
													value="1">是</option>
											</select>
											<span style="color: red;">*</span></td>
									</tr>
									<tr>
										<td>办公电话:</td>
										<td><input type="text" name=officeTel
											value="${user.officeTel }" /></td>
										<td>家庭电话:</td>
										<td><input type="text" name="homeTel"
											value="${user.homeTel }" /></td>
									</tr>
									<tr>
										<td>毕业时间:</td>
										<td><input type="text" id="edu_graduateDate"
											name="graduateDate" value="${user.graduateDate}"
											class="Wdate" onFocus="WdatePicker({isShowWeek:true})" /></td>
										<td>毕业学校（最高学历） :</td>
										<td><input type="text" id="edu_graduateSchool"
											name="graduateSchool" value="${user.graduateSchool }" style="width: 200px;"/></td>
									</tr>
									<tr>
										<td>邮编:</td>
										<td><input type="text" name="postCode"
											value="${user.postCode }" class="postcode" /></td>
										<td>单位通信地址:</td>
										<td><input type="text" name="homeAddress"
											value="${user.homeAddress }" style="width: 95%;" /></td>
									</tr>
									<tr>
										<td>简介:</td>
										<td><textarea rows="3" cols="" name="introduction"
												style="width: 98%">${user.introduction }</textarea></td>
										<td colspan="2"></td>
									</tr>
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
						<li>
							<div class="buttonActive">
								<div class="buttonContent">
									<button type="submit">提交</button>
								</div>
							</div>
						</li>
						<li>
							<div class="button">
								<div class="buttonContent">
									<button type="button" class="close"
										onclick="navTab.closeCurrentTab();">取消</button>
								</div>
							</div>
						</li>
					</ul>
				</div>
			</form>
		</div>
	</div>
	<script type="text/javascript">
		if('${not empty userRole}'){
			var role = "${userRole}".toString();
			var roles = role.substring(1,role.length-1).split(",");
			for(var index=0;index<roles.length;index++){
				jQuery("input[value='"+jQuery.trim(roles[index])+"']").attr("checked",true);
			}
		}
</script>
</body>
