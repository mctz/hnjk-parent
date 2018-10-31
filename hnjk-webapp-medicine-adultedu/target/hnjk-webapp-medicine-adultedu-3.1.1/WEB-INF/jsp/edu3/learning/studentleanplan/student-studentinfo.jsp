<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>我的学籍信息</title>
<style type="text/css">
#my_studentInfo_stuFactFee td {
	text-align: center;
}

#my_studentInfo_stuFactFee th {
	text-align: center;
}
</style>
</head>
<body>
	<script type="text/javascript">
function Canceldisabled(){
		$("#changeStu select[name='politics']").attr("disabled","");
		$("#changeStu select[name='nation']").attr("disabled","");
	}
	//导出学位审批表
		function exportDegreeAuditTable(studentId){
			var url = "${baseUrl }/edu3/student/studentinfo/degreeAuditTable.html?operatingFlag=download&studentId="+studentId;
			alertMsg.confirm("确定要导出学位审批表吗？",{
				okCall:function(){
					downloadFileByIframe(url,'degreeAuditTableExportIframe');
				}
			});
		}
		jQuery(document).ready(function(){
			//如果当前用户不是admin就不给修改部分信息
			 var current=$("#current").attr("value");				
				if(current!='administrator'){
					$("#changeStu input[name='name']").attr("readonly","readonly");
					$("#changeStu input[name='gender']").attr("readonly","readonly");
					$("#changeStu input[name='certNum']").attr("readonly","readonly");
					$("#changeStu select[name='politics']").attr("disabled","disabled");
					$("#changeStu select[name='nation']").attr("disabled","disabled");
					
				}
			 //初始化学生照片上传			
		      //alert("rootUrl:"+'${rootUrl}');
			 $("#changeStu #uploadify_images_photoPath").uploadify({ 
		            'script'         : baseUrl+'/edu3/filemanage/upload.html',
		            'auto'           : true, //自动上传               
		            'multi'          : false, //多文件上传
		            'scriptData'	 : {'isStoreToDatabase':'N','storePath':'common,students,${storeDir}'},//按学生报名日期创建目录
		            'fileDesc'       : '支持格式:jpg',  //限制文件上传格式描述
		            'fileExt'        : '*.JPG;', //限制文件上传的类型,必须有fileDesc这个性质
		            'sizeLimit'      : 40960 , //限制单个文件上传大小40K 
		            'buttonImg'      : baseUrl+'/jscript/jquery.uploadify/images/icon_upload.png', //按钮图片
		            'onInit': function () {       
			        	//载入时触发，将flash设置到最小             
			        	$("#fileQueue").hide();
			        },			        
		            onComplete: function (event, queueID, fileObj, response, data) { //上传成功回调函数
		             	var result = response.split("|");
		              	$('#changeStu #student_photoPath').attr('src','${rootUrl}common/students/${storeDir}/'+result[1]);
		               	$('#changeStu #photoPathId').val('${storeDir}/'+result[1]);
					},  
					onError: function(event, queueID, fileObj) {  //上传失败回调函数
					    alert("文件" + fileObj.name + "上传失败"); 
					    $('#changeStu #uploadify').uploadifyClearQueue(); //清空上传队列
					}
	     	 });
	      	 var student_photoPath = '${stu.studentInfo.studentBaseInfo.photoPath}';
	      	 if(student_photoPath != ''){	   
	    		$('#student_photoPath').attr('src','${rootUrl}common/students/${stu.studentInfo.studentBaseInfo.photoPath}'+"?"+Math.random()*1000);
	    		$('#photoPathId').val('${stu.studentInfo.studentBaseInfo.photoPath}');
	     	 }	   
	      	 //初始化学生身份证照片上传
	     	 $("#changeStu #uploadify_images_certPhotoPath").uploadify({ 
		            'script'         : baseUrl+'/edu3/filemanage/upload.html', //上传URL
		            'auto'           : true, //自动上传               
		            'multi'          : false, //多文件上传
		            'scriptData'	 : {'isStoreToDatabase':'N','storePath':'common,students,${storeDir}'},//按学生报名日期创建目录
		            'fileDesc'       : '支持格式:jpg',  //限制文件上传格式描述
		            'fileExt'        : '*.JPG;', //限制文件上传的类型,必须有fileDesc这个性质
		            'sizeLimit'      : 153600,//61440, //限制单个文件上传大小60K 
		            'buttonImg'      : baseUrl+'/jscript/jquery.uploadify/images/icon_upload.png', //按钮图片
		            onComplete: function (event, queueID, fileObj, response, data) { //上传成功回调函数
		             	var result = response.split("|");                	             	
		              	$('#changeStu #student_certPhotoPath').attr('src','${rootUrl}common/students/${storeDir}/'+result[1]);
		               	$('#changeStu #certPhotoPathId').val('${storeDir}/'+result[1]);
// 		               	$('#student_certPhotoPath').bind("load", function(){
// 		            		if(this.width!=492 || this.height!=640){
// 		            			alertMsg.error("图片"+this.width+"*"+this.height+"尺寸错误,上传的图片的尺寸必须为492*640,否则无法保存");
// 		            		}else{
// 		            			$('#certPhotoPathId').val('covers/${storePath}/'+result[1]);
// 		            		}
// 		            	});	 
					},  
					onError: function(event, queueID, fileObj) {  //上传失败回调函数
					    alert("文件" + fileObj.name + "上传失败"); 
					    $('#changeStu #uploadify').uploadifyClearQueue(); //清空上传队列
					}
	      	 });
	      	 var student_certPhotoPath = '${stu.studentInfo.studentBaseInfo.certPhotoPath}';
	     	 if(student_certPhotoPath != ''){
	    		$('#changeStu #student_certPhotoPath').attr('src','${rootUrl}common/students/${stu.studentInfo.studentBaseInfo.certPhotoPath}'+"?"+Math.random()*1000);
	    		$('#certPhotoPathId').val('${stu.studentInfo.studentBaseInfo.certPhotoPath}');
	     	 }
	     	 //初始化学生身份证照片上传(反面)
	    	 $("#changeStu #uploadify_images_certPhotoPath_reverse").uploadify({ 
		            'script'         : baseUrl+'/edu3/filemanage/upload.html', //上传URL
		            'auto'           : true, //自动上传               
		            'multi'          : false, //多文件上传
		            'scriptData'	 : {'isStoreToDatabase':'N','storePath':'common,students,${storeDir}'},//按学生报名日期创建目录
		            'fileDesc'       : '支持格式:jpg',  //限制文件上传格式描述
		            'fileExt'        : '*.JPG;', //限制文件上传的类型,必须有fileDesc这个性质
		            'sizeLimit'      : 153600,//61440, //限制单个文件上传大小60K 
		            'buttonImg'      : baseUrl+'/jscript/jquery.uploadify/images/icon_upload.png', //按钮图片
		            onComplete: function (event, queueID, fileObj, response, data) { //上传成功回调函数
		             	var result = response.split("|");                	             	
		            	$('#changeStu #student_certPhotoPathReverse').attr('src','${rootUrl}common/students/${storeDir}/'+result[1]);
		               	$('#changeStu #certPhotoPathIdReverse').val('${storeDir}/'+result[1]);
					},  
					onError: function(event, queueID, fileObj) {  //上传失败回调函数
					    alert("文件" + fileObj.name + "上传失败"); 
					    $('#changeStu #uploadify').uploadifyClearQueue(); //清空上传队列
					}
	      	 });
	      	 var student_certPhotoPathReverse = '${stu.studentInfo.studentBaseInfo.certPhotoPathReverse}';
	     	 if(student_certPhotoPathReverse != ''){
	    		$('#changeStu #student_certPhotoPathReverse').attr('src','${rootUrl}common/students/${stu.studentInfo.studentBaseInfo.certPhotoPathReverse}'+"?"+Math.random()*1000);
	    		$('#certPhotoPathIdReverse').val('${stu.studentInfo.studentBaseInfo.certPhotoPathReverse}');
	     	 }
	     	 	 
	     	 //初始化学生其他证件照片上传
	     	 $("#changeStu #uploadify_images_eduPhotoPath").uploadify({ 
		            'script'         : baseUrl+'/edu3/filemanage/upload.html', //上传URL
		            'auto'           : true, //自动上传               
		            'multi'          : false, //多文件上传
		            'scriptData'	 : {'isStoreToDatabase':'N','storePath':'common,students,${storeDir}'},//按学生报名日期创建目录
		            'fileDesc'       : '支持格式:jpg',  //限制文件上传格式描述
		            'fileExt'        : '*.JPG;', //限制文件上传的类型,必须有fileDesc这个性质
		            'sizeLimit'      : 102400, //限制单个文件上传大小100K 
		            'buttonImg'      : baseUrl+'/jscript/jquery.uploadify/images/icon_upload.png', //按钮图片
		            onComplete: function (event, queueID, fileObj, response, data) { //上传成功回调函数
		             	var result = response.split("|");                	             	
		              	$('#changeStu #student_eduPhotoPath').attr('src','${rootUrl}common/students/${storeDir}/'+result[1]);
		               	$('#changeStu #eduPhotoPathId').val('${storeDir}/'+result[1]);
					},  
					onError: function(event, queueID, fileObj) {  //上传失败回调函数
					    alert("文件" + fileObj.name + "上传失败"); 
					    $('#changeStu #uploadify').uploadifyClearQueue(); //清空上传队列
					}
	     	 });
	     	 
	    	 
	     	//户口册上传
	     	 $("#changeStu #uploadify_images_bookletPhotoPath").uploadify({ 
		            'script'         : baseUrl+'/edu3/filemanage/upload.html', //上传URL
		            'auto'           : true, //自动上传               
		            'multi'          : false, //多文件上传
		            'scriptData'	 : {'isStoreToDatabase':'N','storePath':'common,students,${storeDir}'},//按学生报名日期创建目录
		            'fileDesc'       : '支持格式:jpg',  //限制文件上传格式描述
		            'fileExt'        : '*.JPG;', //限制文件上传的类型,必须有fileDesc这个性质
		            'sizeLimit'      : 102400, //限制单个文件上传大小100K 
		            'buttonImg'      : baseUrl+'/jscript/jquery.uploadify/images/icon_upload.png', //按钮图片
		            onComplete: function (event, queueID, fileObj, response, data) { //上传成功回调函数
		             	var result = response.split("|");                	             	
		              	$('#changeStu #student_bookletPhotoPath').attr('src','${rootUrl}common/students/${storeDir}/'+result[1]);
		               	$('#changeStu #bookletPhotoPathId').val('${storeDir}/'+result[1]);
					},  
					onError: function(event, queueID, fileObj) {  //上传失败回调函数
					    alert("文件" + fileObj.name + "上传失败"); 
					    $('#changeStu #uploadify').uploadifyClearQueue(); //清空上传队列
					}
	     	 });
	     	
	     	//其他图片上传
	     	 $("#changeStu #uploadify_images_otherPhotoPath").uploadify({ 
		            'script'         : baseUrl+'/edu3/filemanage/upload.html', //上传URL
		            'auto'           : true, //自动上传               
		            'multi'          : false, //多文件上传
		            'scriptData'	 : {'isStoreToDatabase':'N','storePath':'common,students,${storeDir}'},//按学生报名日期创建目录
		            'fileDesc'       : '支持格式:jpg',  //限制文件上传格式描述
		            'fileExt'        : '*.JPG;', //限制文件上传的类型,必须有fileDesc这个性质
		            'sizeLimit'      : 102400, //限制单个文件上传大小100K 
		            'buttonImg'      : baseUrl+'/jscript/jquery.uploadify/images/icon_upload.png', //按钮图片
		            onComplete: function (event, queueID, fileObj, response, data) { //上传成功回调函数
		             	var result = response.split("|");                	             	
		              	$('#changeStu #student_otherPhotoPath').attr('src','${rootUrl}common/students/${storeDir}/'+result[1]);
		               	$('#changeStu #otherPhotoPathId').val('${storeDir}/'+result[1]);
					},  
					onError: function(event, queueID, fileObj) {  //上传失败回调函数
					    alert("文件" + fileObj.name + "上传失败"); 
					    $('#changeStu #uploadify').uploadifyClearQueue(); //清空上传队列
					}
	     	 });
	     	
	     	 
	     	 //var student_eduPhotoPath = jQuery("#changeStu input[id='eduPhotoPath']").val();
	     	 var student_eduPhotoPath = '${stu.studentInfo.studentBaseInfo.eduPhotoPath}';
	      	 if(student_eduPhotoPath != ''){
	    		$('#changeStu #student_eduPhotoPath').attr('src','${rootUrl}common/students/${stu.studentInfo.studentBaseInfo.eduPhotoPath}'+"?"+Math.random()*1000);
	    		$('#eduPhotoPathId').val('${stu.studentInfo.studentBaseInfo.eduPhotoPath}');
	      	 }  
			//籍贯
			jQuery("#changeStu #ChinaArea1").jChinaArea({
					 //aspnet:true,
					 s1:"${homePlaceProvince}",//默认选中的省名
					 s2:"${homePlaceCity}",//默认选中的市名
					 s3:"${homePlaceDistrict}"//默认选中的县区名
				 });
			//户口
			jQuery("#changeStu #ChinaArea2").jChinaArea({
					 //aspnet:true,
					 s1:"${HouseholdRegisterationProvince}",//默认选中的省名
					 s2:"${HouseholdRegisterationCity}",//默认选中的市名
					 s3:"${HouseholdRegisterationDistrict}"//默认选中的县区名
			 });		 		 		
		});
		function saveChangeForm(form){//保存回调 
			jQuery("#changeStu #homePlace").val(jQuery("#changeStu #homePlaceprovince option:selected").text()+","
		  			 +jQuery("#changeStu #homePlacecity option:selected").text()+","
		   			 +jQuery("#changeStu #homePlacecounty option:selected").text());
			//alert(jQuery("#changeStu input[id='homePlace']").val());return false;
			jQuery("#changeStu #residence").val(jQuery("#changeStu #HouseholdRegisterationprovince option:selected").text()+","
		 			 +jQuery("#changeStu #HouseholdRegisterationcity option:selected").text()+","
		  			 +jQuery("#changeStu #HouseholdRegisterationcounty option:selected").text());		
			return validateCallback(form,navTabAjaxDone);
		}
		
		function navTabAjaxDone(json){
			if(json.statusCode == "200"){
				alertMsg.correct(json.message);
			}else{
				alertMsg.error(json.message);
			}
		}

		// 申请学位
		function applyDegree(stuid,obj){
			alertMsg.confirm("您确定要申请学位吗？",{
				okCall:function(){
					$(obj).attr("disabled","disabled");
					$.ajax({  
				        url : '${baseUrl}/edu3/schoolroll/graduation/applyDegree.html',  
				        async : false, 
				        type : "POST",  
				        dataType : "json",
				        data:{studentId:stuid},
				        success : function(data) { 
				        	if(data.statusCode==200){
				        		$(obj).parent(".degreeStatus").text("待审核");
				        		alertMsg.correct(data.message);
				        	}else{
				        		alertMsg.error(data.message);
				        		$(obj).removeAttr("disabled");
				        	}
				        }  
				    });
				}
			});
		}
		
</script>
	<div class="page">
		<div class="pageContent">
			<form id="changeStu" method="post"
				action="${baseUrl}/edu3/register/studentinfo/changeBaseInfo.html"
				onsubmit="return saveChangeForm(this);" class="pageForm"
				enctype="multipart/form-data">
				<div class="pageFormContent" layouth="21">
					<!-- tabs -->
					<div class="tabs">
						<div class="tabsHeader">
							<div class="tabsHeaderContent">
								<ul>
									<c:forEach items="${stuList }" var="stu">
										<li
											<c:if test="${stu.isDefaultStudentId eq 'Y' }">class="selected"</c:if>>
											<a href="javascript:void(0)"><span>${stu.studentInfo.teachingPlan.major.majorName }-${stu.studentInfo.teachingPlan.classic.classicName }</span></a>
										</li>
									</c:forEach>
								</ul>
							</div>
						</div>
						<div class="tabsContent" style="height: 100%;">
							<c:forEach items="${stuList }" var="stu">
								<!-- tabs -->
								<div class="tabs">
									<div class="tabsHeader">
										<div class="tabsHeaderContent">
											<ul>
												<li><a href="javascript:void(0)"><span>学籍信息</span></a></li>
												<li><a href="javascript:void(0)"><span>基本信息</span></a></li>
												<%--<c:if test="${stuts eq '2' }"> --%>
												<li><a href="javascript:void(0)"><span>上传证件信息</span></a></li>
												<%--</c:if> --%>
												<li><a href="javascript:void(0)"><span>学分信息</span></a></li>
												<li><a href="javascript:void(0)"><span>学费信息</span></a></li>
											</ul>
										</div>
									</div>
									<div class="tabsContent" style="height: 100%;">
										<!-- 1 学籍信息 -->
										<div>
											<table class="form">
												<tr>
													<td style="width: 20%">学生号：</td>
													<td style="width: 30%">${stu.studentInfo.studyNo }</td>
													<td style="width: 20%">考生号：</td>
													<td style="width: 30%">${stu.studentInfo.enrolleeCode }</td>
												</tr>
												<tr>
													<td>学生姓名：</td>
													<td>${stu.studentInfo.studentName }</td>
													<td>注册号：</td>
													<td>${stu.studentInfo.registorNo }</td>
												</tr>
												<tr>
													<td>年级：</td>
													<td>${stu.studentInfo.grade.gradeName }</td>
													<td>层次：</td>
													<td>${stu.studentInfo.classic.classicName }</td>
												</tr>
												<tr>
													<td>进修性质：</td>
													<td>${ghfn:dictCode2Val('CodeAttendAdvancedStudies',stu.studentInfo.attendAdvancedStudies) }</td>
													<td>学习方式：</td>
													<td>${ghfn:dictCode2Val('CodeLearningStyle',stu.studentInfo.learningStyle) }</td>
												</tr>
												<tr>
													<td>教学站：</td>
													<td>${stu.studentInfo.branchSchool.unitName}</td>
													<td>专业：</td>
													<td>${stu.studentInfo.major.majorName}</td>
												</tr>
												<tr>
													<td>就读方式：</td>
													<td>${ghfn:dictCode2Val('CodeStudyInSchool',stu.studentInfo.studyInSchool) }</td>
													<td>学习类别：</td>
													<td>${stu.studentInfo.studentKind }</td>
												</tr>
												<tr>
													<td>学籍状态：</td>
													<td>${ghfn:dictCode2Val('CodeStudentStatus',stu.studentInfo.studentStatus) }</td>
													<td>在学状态：</td>
													<td>${ghfn:dictCode2Val('CodeLearingStatus',stu.studentInfo.learingStatus) }</td>
												</tr>
												<tr>
													<td>入学资格审核：</td>
													<td><c:choose>
															<c:when test="${stu.studentInfo.enterAuditStatus=='N'}">
																<table>
																	<tr>
																		<td
																			style="font-size: 13px; font-weight: bold; color: red">入学资格复查未通过（专科资格待查），请立即对专科证书验国证。教育部规定，专科资格待查的，不能视为在校生，更不能发放毕业证书以及注册学历！</td>
																	</tr>
																</table>
																<%--<font color="red">
												<b></b>
												 ${ghfn:dictCode2Val('CodeAuditStatus',stu.studentInfo.enterAuditStatus)}，请立即对专科证书验国证。
												</font>
												--%>
															</c:when>
															<c:otherwise>
												${ghfn:dictCode2Val('CodeAuditStatus',stu.studentInfo.enterAuditStatus)}
											</c:otherwise>
														</c:choose></td>
													<td>备注：</td>
													<td colspan="3">${stu.studentInfo.memo }</td>
												</tr>

												<%-- <c:if test="${stu.studentInfo.classic.endPoint eq '本科' }">
													<tr>
														<td>下载打印：</td>
														<td colspan="3"><a href="javaScript:void(0)"
															onclick="exportDegreeAuditTable('${stu.studentInfo.resourceid}');">下载</a>
															<a
															href="${baseUrl }/edu3/student/studentinfo/degreeAuditTable-view.html?operatingFlag=print&studentId=${stu.studentInfo.resourceid}"
															target="dialog" title="打印学位审批表" width="800" height="600">
																打印</a></td>
													</tr>
												</c:if> --%>
												
												<!-- 目前只有广外使用 -->
												<c:if test="${stu.degreeStatus eq 'Y' and schoolCode eq '11846'}">
													<tr>
														<td  style="color: red;font-weight: bolder;" >学位情况：</td>
														<td colspan="3"  style="color: red;font-weight: bolder;" class="degreeStatus">
														    <c:choose>
														    	<c:when test="${empty stu.degreeApplyStatus }">
														    		<input type="button" style="cursor:pointer; " title="申请学位"  onclick="applyDegree('${stu.studentInfo.resourceid}',this);" value="申请学位" />
														    		<%-- <a style="cursor:pointer; " href="javaScript:void(0)"  title="申请学位"  onclick="applyDegree('${stu.studentInfo.resourceid}',this);" >申请学位</a> --%>
														    	</c:when>
														    	<c:otherwise>
														    		  ${ghfn:dictCode2Val('CodeDegreeApplyStatus',stu.degreeApplyStatus)}
														    	</c:otherwise>
														    </c:choose>
														</td>
													</tr>
												</c:if>
											</table>
										</div>
										<!-- 2 基本信息 -->
										<c:choose>
											<%--已提交的学籍信息不能编辑这些信息了 --%>
											<c:when test="${stu.studentInfo.rollCardStatus eq '2' || open ne 'Y'}">
												<div>
													<table id="student_base_info_table" class="form">
														<input type="hidden" name="resourceid"
															value="${stu.studentInfo.resourceid }">
														<input type="hidden" name="name"
															value="${stu.studentInfo.studentBaseInfo.name  }">
														<input type="hidden" name="gender"
															value="${stu.studentInfo.studentBaseInfo.gender}">
														<input type="hidden" name="certType"
															value="${stu.studentInfo.studentBaseInfo.certType}">
														<input type="hidden" name="certNum"
															value="${stu.studentInfo.studentBaseInfo.certNum}">
														<input type="hidden" name="isSubmit" value="Y">
														<!--  
									<input type="hidden" name="photoPath"  value="${stu.studentInfo.studentBaseInfo.photoPath}">
									<input type="hidden" name="certPhotoPath"  value="${stu.studentInfo.studentBaseInfo.certPhotoPath}">	
									<input type="hidden" name="eduPhotoPath"  value="${stu.studentInfo.studentBaseInfo.eduPhotoPath}">
									-->
														<tr>
															<td style="width: 20%">姓名：</td>
															<td style="width: 30%">${stu.studentInfo.studentBaseInfo.name }</td>
															<!--<td style="width:20%" rowspan="4">个人照片：</td>
											<td style="width:30%" rowspan="4">
												<c:if test="${not empty stu.studentInfo.studentBaseInfo.photoPath }">
													<img src="${rootUrl}common/students/${stu.studentInfo.studentBaseInfo.photoPath}" width="90" height="126"/>
												</c:if>
											 </td>-->
															<td>性别：</td>
															<td>${ghfn:dictCode2Val("CodeSex",stu.studentInfo.studentBaseInfo.gender) }</td>
														</tr>
														<tr>
															<td>证件类别：</td>
															<td>${ghfn:dictCode2Val("CodeCertType",stu.studentInfo.studentBaseInfo.certType) }</td>
															<td>证件号码：</td>
															<td>${stu.studentInfo.studentBaseInfo.certNum }</td>
														</tr>
														<tr>
															<td>联系地址:</td>
															<td>${stu.studentInfo.studentBaseInfo.contactAddress }</td>
															<td>联系邮编：</td>
															<td>${stu.studentInfo.studentBaseInfo.contactZipcode }</td>
														</tr>
														<tr>
															<td>联系电话：</td>
															<td>${stu.studentInfo.studentBaseInfo.contactPhone }</td>
															<td>移动电话：</td>
															<td>${stu.studentInfo.studentBaseInfo.mobile }</td>
														</tr>
														<tr>
															<td>邮件：</td>
															<td>${stu.studentInfo.studentBaseInfo.email }</td>
															<td>个人主页：</td>
															<td>${stu.studentInfo.studentBaseInfo.homePage }</td>
														</tr>
														<tr>
															<td>身高：</td>
															<td>${stu.studentInfo.studentBaseInfo.height }</td>
															<td>血型：</td>
															<td>${ghfn:dictCode2Val("CodeBloodStyle",stu.studentInfo.studentBaseInfo.bloodType) }</td>
														</tr>
														<tr>
															<td>出生日期：</td>
															<td><fmt:formatDate
																	value="${stu.studentInfo.studentBaseInfo.bornDay }"
																	pattern="yyyy-MM-dd" /> <input type="hidden"
																name="bornDay" id="bornDay"
																value="<fmt:formatDate value="${stu.studentInfo.studentBaseInfo.bornDay }" pattern="yyyy-MM-dd" />" /></td>
															</td>
															<td>出生地：</td>
															<td>${stu.studentInfo.studentBaseInfo.bornAddress }</td>
														</tr>
														<tr>
															<td>国籍：</td>
															<td>${ghfn:dictCode2Val("CodeCountry",stu.studentInfo.studentBaseInfo.country) }</td>
															<td>籍贯：</td>
															<td>${stu.studentInfo.studentBaseInfo.homePlace }</td>
														</tr>
														<tr>
															<td>港澳侨代码：</td>
															<td>${ghfn:dictCode2Val("CodeGAQ",stu.studentInfo.studentBaseInfo.gaqCode) }</td>
															<td>民族：</td>
															<td>${ghfn:dictCode2Val("CodeNation",stu.studentInfo.studentBaseInfo.nation) }</td>
														</tr>
														<tr>
															<td>身体健康状态：</td>
															<td>${ghfn:dictCode2Val("CodeHealth",stu.studentInfo.studentBaseInfo.health ) }</td>
															<td>婚姻状况：</td>
															<td>${ghfn:dictCode2Val("CodeMarriage",stu.studentInfo.studentBaseInfo.marriage ) }</td>
														</tr>
														<tr>
															<td>政治面目：</td>
															<td>${ghfn:dictCode2Val("CodePolitics",stu.studentInfo.studentBaseInfo.politics ) }</td>
															<td>宗教信仰：</td>
															<td>${stu.studentInfo.studentBaseInfo.faith }</td>
														</tr>
														<tr>
															<td>户口性质：11</td>
															<td>${ghfn:dictCode2Val("CodeRegisteredResidenceKind",stu.studentInfo.studentBaseInfo.residenceKind  ) }</td>

															<td>户口所在地：</td>
															<td>${stu.studentInfo.studentBaseInfo.residence } <!--<input name="residence" value="${stu.studentInfo.studentBaseInfo.residence }" style="width:48%" readonly="readonly">
											--></td>
														</tr>
														<tr>
															<td>现住址：</td>
															<td>${stu.studentInfo.studentBaseInfo.currenAddress }</td>
															<td>家庭住址：</td>
															<td>${stu.studentInfo.studentBaseInfo.homeAddress }</td>
														</tr>
														<tr>
															<td>家庭住址邮编：</td>
															<td>${stu.studentInfo.studentBaseInfo.homezipCode }</td>
															<td>家庭电话：</td>
															<td>${stu.studentInfo.studentBaseInfo.homePhone }</td>
														</tr>
														<tr>
															<td>公司名称：</td>
															<td>${stu.studentInfo.studentBaseInfo.officeName }</td>
															<td>公司电话：</td>
															<td>${stu.studentInfo.studentBaseInfo.officePhone }</td>
														</tr>
														<tr>
															<td>职务职称：</td>
															<td>${stu.studentInfo.studentBaseInfo.title }</td>
															<td>特长：</td>
															<td>${stu.studentInfo.studentBaseInfo.specialization }</td>
														</tr>
														<tr>
															<td>备注：</td>
															<td colspan="3">${stu.studentInfo.studentBaseInfo.memo }</td>
														</tr>
													</table>
												</div>
											</c:when>
											<c:otherwise>
												<div>
													<table id="student_base_info_table" class="form">
														<input type="hidden" name="resourceid"
															value="${stu.studentInfo.resourceid }">
														<input type="hidden" name="name"
															value="${stu.studentInfo.studentBaseInfo.name  }">
														<input type="hidden" name="gender"
															value="${stu.studentInfo.studentBaseInfo.gender}">
														<input type="hidden" name="certType"
															value="${stu.studentInfo.studentBaseInfo.certType}">
														<input type="hidden" name="certNum"
															value="${stu.studentInfo.studentBaseInfo.certNum}">
														<tr>
															<td style="width: 20%">姓名：</td>
															<td style="width: 30%">${stu.studentInfo.studentBaseInfo.name }</td>
															<!--<td style="width:20%" rowspan="4">个人照片：</td>
											<td style="width:30%" rowspan="4">
												<c:if test="${not empty stu.studentInfo.studentBaseInfo.photoPath }">
													<img src="${rootUrl}common/students/${stu.studentInfo.studentBaseInfo.photoPath}" width="90" height="126"/>
												</c:if>
											 </td>-->
															<td>性别：</td>
															<td>${ghfn:dictCode2Val("CodeSex",stu.studentInfo.studentBaseInfo.gender) }</td>
														</tr>
														<tr>
															<td>证件类别：</td>
															<td>${ghfn:dictCode2Val("CodeCertType",stu.studentInfo.studentBaseInfo.certType) }</td>
															<td>证件号码：</td>
															<td>${stu.studentInfo.studentBaseInfo.certNum }</td>
														</tr>
														<tr>
															<td>联系地址:</td>
															<!--<td>${stu.studentInfo.studentBaseInfo.contactAddress }</td>-->
															<td><input name="contactAddress"
																value="${stu.studentInfo.studentBaseInfo.contactAddress }"
																style="width: 85%" class="required"></td>
															<td>联系邮编：</td>
															<!--<td>${stu.studentInfo.studentBaseInfo.contactZipcode }</td>	-->
															<td><input name="contactZipcode"
																value="${stu.studentInfo.studentBaseInfo.contactZipcode }"
																style="width: 50%" class="postcode required"></td>
														</tr>
														<tr>
															<td>联系电话：</td>
															<!--<td>${stu.studentInfo.studentBaseInfo.contactPhone }</td>-->
															<td><input name="contactPhone"
																value="${stu.studentInfo.studentBaseInfo.contactPhone }"
																style="width: 50%" class="phone"></td>
															<td>移动电话：</td>
															<td><input name="mobile"
																value="${stu.studentInfo.studentBaseInfo.mobile }"
																style="width: 50%" class="mobile required"></td>
															<!--<td>${stu.studentInfo.studentBaseInfo.mobile }</td>	-->
														</tr>
														<tr>
															<td>邮件：</td>
															<!--<td>${stu.studentInfo.studentBaseInfo.email }</td>-->
															<td><input name="email"
																value="${stu.studentInfo.studentBaseInfo.email }"
																style="width: 50%" class="email"></td>
															<td>个人主页：</td>
															<!--<td>${stu.studentInfo.studentBaseInfo.homePage }</td>-->
															<td><input name="homePage"
																value="${stu.studentInfo.studentBaseInfo.homePage }"
																style="width: 50%" class="url"></td>
														</tr>
														<tr>
															<td>身高：</td>
															<!--<td>${stu.studentInfo.studentBaseInfo.height }</td>-->
															<td><input name="height" type="text"
																value="${stu.studentInfo.studentBaseInfo.height }"
																style="width: 50%"
																onkeyup="value=value.replace(/[^\d]/g,'') "
																onbeforepaste="clipboardData.setData('text',clipboardData.getData('text').replace(/[^\d]/g,''))">
																<span class="tips">单位：cm,请填写整数</span></td>
															<td>血型：</td>
															<!--<td>${ghfn:dictCode2Val("CodeBloodStyle",stu.studentInfo.studentBaseInfo.bloodType) }</td>-->
															<td><gh:select name="bloodType"
																	value="${stu.studentInfo.studentBaseInfo.bloodType }"
																	dictionaryCode="CodeBloodStyle" style="width:50%" /></td>
														</tr>
														<tr>
															<td>出生日期：</td>
															<!--<td><fmt:formatDate value="${stu.studentInfo.studentBaseInfo.bornDay }" pattern="yyyy-MM-dd"/></td>-->
															<td>
																<%--<input style="width:48%" type="text" name="bornDay" id="bornDay"  value="<fmt:formatDate value="${stu.studentInfo.studentBaseInfo.bornDay }" pattern="yyyy-MM-dd" />" /> --%>
																<input type="hidden" name="bornDay" id="bornDay"
																value="<fmt:formatDate value="${stu.studentInfo.studentBaseInfo.bornDay }" pattern="yyyy-MM-dd" />" />
																<fmt:formatDate
																	value="${stu.studentInfo.studentBaseInfo.bornDay }"
																	pattern="yyyy-MM-dd" /> <%--<span class="tips">格式：如1990-10-02</span>--%>
															</td>
															<td>出生地：</td>
															<!--<td>${stu.studentInfo.studentBaseInfo.bornAddress }</td>-->
															<td><input name="bornAddress"
																value="${stu.studentInfo.studentBaseInfo.bornAddress }"
																style="width: 50%"></td>
														</tr>
														<tr>
															<td>国籍：</td>
															<!--<td>${ghfn:dictCode2Val("CodeCountry",stu.studentInfo.studentBaseInfo.country) }</td>-->
															<td><gh:select name="country"
																	value="${stu.studentInfo.studentBaseInfo.country }"
																	dictionaryCode="CodeCountry" style="width:50%" /></td>
															<td>籍贯：</td>
															<!--<td>${stu.studentInfo.studentBaseInfo.homePlace }</td>
											-->
															<!--<td><input name="homePlace" value="${stu.studentInfo.studentBaseInfo.homePlace }" style="width:50%" ></td>-->
															<td>
																<div id="ChinaArea1">
																	<select id="homePlaceprovince" name="homePlaceprovince"
																		tyle="width: 80px;"></select> <select
																		id="homePlacecity" name="homePlacecity"
																		style="width: 100px;"></select> <select
																		id="homePlacecounty" name="homePlacecounty"
																		style="width: 80px;"></select>
																</div> <input type="hidden" name="homePlace" id="homePlace"
																value="${stu.studentInfo.studentBaseInfo.homePlace }">
															</td>
														</tr>
														<tr>
															<td>港澳侨代码：</td>
															<!--<td>${ghfn:dictCode2Val("CodeGAQ",stu.studentInfo.studentBaseInfo.gaqCode) }</td>
											-->
															<td><gh:select name="gaqCode"
																	value="${stu.studentInfo.studentBaseInfo.gaqCode }"
																	dictionaryCode="CodeGAQ" style="width:50%" /></td>
															<td>民族：</td>
															<!--<td>${ghfn:dictCode2Val("CodeNation",stu.studentInfo.studentBaseInfo.nation) }</td>-->
															<td><gh:select name="nation"
																	value="${stu.studentInfo.studentBaseInfo.nation }"
																	dictionaryCode="CodeNation" style="width:50%"
																	classCss="required" /><font color='red'>* </font></td>
														</tr>
														<tr>
															<td>身体健康状态：</td>
															<!--<td>${ghfn:dictCode2Val("CodeHealth",stu.studentInfo.studentBaseInfo.health ) }</td>-->
															<td><gh:select name="health"
																	value="${stu.studentInfo.studentBaseInfo.health }"
																	dictionaryCode="CodeHealth" style="width:50%" /></td>
															<td>婚姻状况：</td>
															<!--<td>${ghfn:dictCode2Val("CodeMarriage",stu.studentInfo.studentBaseInfo.marriage ) }</td>-->
															<td><gh:select name="marriage"
																	value="${stu.studentInfo.studentBaseInfo.marriage }"
																	dictionaryCode="CodeMarriage" style="width:50%"
																	classCss="required" /><font color='red'>* </font></td>
														</tr>
														<tr>
															<td>政治面目：</td>
															<!--<td>${ghfn:dictCode2Val("CodePolitics",stu.studentInfo.studentBaseInfo.politics ) }-->
															<td><gh:select name="politics"
																	value="${stu.studentInfo.studentBaseInfo.politics }"
																	dictionaryCode="CodePolitics" style="width:50%"
																	classCss="required" /><font color='red'>* </font></td>
															</td>
															<td>宗教信仰：</td>
															<!--<td>${stu.studentInfo.studentBaseInfo.faith }</td>-->
															<td><input name="faith"
																value="${stu.studentInfo.studentBaseInfo.faith }"
																style="width: 48%"></td>
														</tr>
														<tr>
															<td>户口性质：</td>
															<!--<td>${ghfn:dictCode2Val("CodeRegisteredResidenceKind",stu.studentInfo.studentBaseInfo.residenceKind  ) }
											</td>
											-->
															<td><gh:select name="residenceKind"
																	value="${stu.studentInfo.studentBaseInfo.residenceKind }"
																	dictionaryCode="CodeRegisteredResidenceKind"
																	style="width:50%" /></td>
															<td>户口所在地：</td>
															<!--<td><input name="residence" value="${stu.studentInfo.studentBaseInfo.residence }" style="width:48%" ></td>
											-->
															<!--<td>${stu.studentInfo.studentBaseInfo.residence }</td>
											-->
															<td>
																<div id="ChinaArea2">
																	<select id="HouseholdRegisterationprovince"
																		name="HouseholdRegisterationprovince"
																		tyle="width: 80px;"></select> <select
																		id="HouseholdRegisterationcity"
																		name="HouseholdRegisterationcity"
																		style="width: 100px;"></select> <select
																		id="HouseholdRegisterationcounty"
																		name="HouseholdRegisterationcounty"
																		style="width: 80px;">
																	</select>
																</div> <input type="hidden" name="residence" id="residence"
																value="${stu.studentInfo.studentBaseInfo.residence }">
															</td>
														</tr>
														<tr>
															<td>现住址：</td>
															<!--<td>${stu.studentInfo.studentBaseInfo.currenAddress }</td>
											-->
															<td><input name="currenAddress"
																value="${stu.studentInfo.studentBaseInfo.currenAddress }"
																style="width: 48%"></td>
															<td>家庭住址：</td>
															<!--<td>${stu.studentInfo.studentBaseInfo.homeAddress }</td>
											-->
															<td><input name="homeAddress"
																value="${stu.studentInfo.studentBaseInfo.homeAddress }"
																style="width: 48%"></td>
														</tr>
														<tr>
															<td>家庭住址邮编：</td>
															<!--<td>${stu.studentInfo.studentBaseInfo.homezipCode }</td>
											-->
															<td><input name="homezipCode"
																value="${stu.studentInfo.studentBaseInfo.homezipCode }"
																style="width: 48%"></td>
															<td>家庭电话：</td>
															<!--<td>${stu.studentInfo.studentBaseInfo.homePhone }</td>
											-->
															<td><input name="homePhone"
																value="${stu.studentInfo.studentBaseInfo.homePhone }"
																style="width: 48%" class="phone"></td>
														</tr>
														<tr>
															<td>公司名称：</td>
															<!--<td>${stu.studentInfo.studentBaseInfo.officeName }</td>
											-->
															<td><input name="officeName"
																value="${stu.studentInfo.studentBaseInfo.officeName }"
																style="width: 48%"></td>
															<td>公司电话：</td>
															<!--<td>${stu.studentInfo.studentBaseInfo.officePhone }</td>
											-->
															<td><input name="officePhone"
																value="${stu.studentInfo.studentBaseInfo.officePhone }"
																style="width: 48%" class="phone"></td>
														</tr>
														<tr>
															<td>职务职称：</td>
															<!--<td>${stu.studentInfo.studentBaseInfo.title }</td>
											-->
															<td><input name="title"
																value="${stu.studentInfo.studentBaseInfo.title }"
																style="width: 48%"></td>
															<td>特长：</td>
															<!--<td>${stu.studentInfo.studentBaseInfo.specialization }</td>
											-->
															<td><input name="specialization"
																value="${stu.studentInfo.studentBaseInfo.specialization }"
																style="width: 48%"></td>
														</tr>
														<tr>
															<td>备注：</td>
															<!--<td colspan="3">${stu.studentInfo.studentBaseInfo.memo }</td>
											-->
															<td colspan="3"><textarea rows="3" cols=""
																	name="memo" style="width: 50%">${stu.studentInfo.studentBaseInfo.memo }</textarea>
															</td>
														</tr>
													</table>
												</div>
											</c:otherwise>
										</c:choose>
										<!-- 3 上传证件信息-->
										<%-- <c:if test="${stuts eq '2' }">--%>
										<div>
											<table class="form">
												<h5 class="tips" style="width: 96%">
													<c:if test="${stu.studentInfo.auditResults eq '0'}">
											图片暂未审核
										</c:if>
													<c:if test="${stu.studentInfo.auditResults eq '3'}">
											你未提交图片
										</c:if>
													<c:if test="${stu.studentInfo.auditResults eq '1'}">
											图片已审核通过
										</c:if>
													<c:if test="${stu.studentInfo.auditResults eq '2'}">
											图片审核不通过
										</c:if>
												</h5>
												<tr>
													<td width="100px">我的录取相片</td>
													<td width="100px"><c:choose>
											<c:when
												test="${not empty studentInfo.studentBaseInfo.recruitPhotoPath }">
												<img
													src="${rootUrl}common/students/${studentInfo.studentBaseInfo.recruitPhotoPath}"
													width="90" height="126" />
											</c:when>
											<c:otherwise>
												<img src="${baseUrl}/themes/default/images/img_preview.png"
													width="90" height="126" />
											</c:otherwise>
										</c:choose></td>
												</tr>
												<tr>
													<td width="30%">选择文件</td>
													<td width="20%">选择文件</td>
													<td>图片预览</td>
												</tr>
												<tr>
													<td width="30%">选择照片：<br /> 1、背景要求：统一为蓝色；<br />
														2、服装：白色或浅色系；<br /> 3、图片尺寸（像素）宽：150、高：210；<br />
														4、大小：≤40K、格式：jpg
													</td>
													<td width="20%"><c:if test="${open eq 'Y'  and stuts eq 2}">
															<input id="uploadify_images_photoPath" type="file" />
														</c:if></td>
													<td><c:if
															test="${not empty stu.studentInfo.studentBaseInfo.photoPath }">
															<img id="student_photoPath"
																src="${rootUrl}common/students/${stu.studentInfo.studentBaseInfo.photoPath}"
																width="90" height="126" />
														</c:if> <c:if
															test="${empty stu.studentInfo.studentBaseInfo.photoPath }">
															<img id="student_photoPath"
																src="${baseUrl}/themes/default/images/img_preview.png"
																width="90" height="126" />
														</c:if> <input type="hidden" name="photoPath" id="photoPathId"
														value="${stu.studentInfo.studentBaseInfo.photoPath}" /></td>
												</tr>
												<tr>
													<td width="30%">选择身份证扫描件：<br /> 1、中华人民共和国第二（一）代居民身份证；<br />
														2、大小：≤150k,格式 :jpg
													</td>
													<td width="20%"><c:if test="${open eq 'Y'  and stuts eq 2}">
															<!-- <input id = "uploadify_images_certPhotoPath" type="file"/> -->
															<ul>
																<li>注意：请使用采光均匀的图片或扫描件</li>
																<li>正面</li>
																<li><input id="uploadify_images_certPhotoPath"
																	type="file" /></li>
															</ul>
															<ul>
																<li>反面</li>
																<li><input
																	id="uploadify_images_certPhotoPath_reverse" type="file" /></li>
															</ul>
														</c:if></td>
													<td><c:if
															test="${not empty stu.studentInfo.studentBaseInfo.certPhotoPath }">
															<img id="student_certPhotoPath"
																src="${rootUrl}common/students/${stu.studentInfo.studentBaseInfo.certPhotoPath}"
																width="240" height="160" />
														</c:if> <c:if
															test="${empty stu.studentInfo.studentBaseInfo.certPhotoPath }">
															<img id="student_certPhotoPath"
																src="${baseUrl}/themes/default/images/certPhoto.png"
																width="240" height="160" />
														</c:if> <c:if
															test="${not empty stu.studentInfo.studentBaseInfo.certPhotoPathReverse }">
															<img id="student_certPhotoPathReverse"
																src="${rootUrl}common/students/${stu.studentInfo.studentBaseInfo.certPhotoPathReverse}"
																width="240" height="160" />
														</c:if> <c:if
															test="${empty stu.studentInfo.studentBaseInfo.certPhotoPathReverse }">
															<img id="student_certPhotoPathReverse"
																src="${baseUrl}/themes/default/images/certPhotoReverse.png"
																width="240" height="160" />
														</c:if> <input type="hidden" id="certPhotoPathId"
														name="certPhotoPath"
														value="${stu.studentInfo.studentBaseInfo.certPhotoPath }" />
														<input type="hidden" id="certPhotoPathIdReverse"
														name="certPhotoPathReverse"
														value="${stu.studentInfo.studentBaseInfo.certPhotoPathReverse }" />
													</td>
												</tr>
												<tr>
													<td width="30%">选择毕业证复印扫描件：<br /> 1、普通大中专院校证件；<br />
														2、大小：≤100k,格式 :jpg
													</td>
													<td width="20%"><c:if
															test="${open eq 'Y'  and stuts eq 2}">
															<input id="uploadify_images_eduPhotoPath" type="file" />
														</c:if></td>
													<td><c:if
															test="${not empty stu.studentInfo.studentBaseInfo.eduPhotoPath }">
															<img id="student_eduPhotoPath"
																src="${rootUrl}common/students/${stu.studentInfo.studentBaseInfo.eduPhotoPath}"
																width="150" height="100" />
														</c:if> <c:if
															test="${empty stu.studentInfo.studentBaseInfo.eduPhotoPath }">
															<img id="student_eduPhotoPath"
																src="${baseUrl }/themes/default/images/img_preview.png"
																width="150" height="100" />
														</c:if> <input type="hidden" name="eduPhotoPath"
														id="eduPhotoPathId"
														value="${stu.studentInfo.studentBaseInfo.eduPhotoPath }" />
													</td>
												</tr>
												<tr>
													<td width="30%">选择户口簿复印扫描件：<br /> 1、户口簿证件；<br />
														2、大小：≤100k,格式 :jpg
													</td>
													<td width="20%"><c:if
															test="${open eq 'Y'  and stuts eq 2}">
															<input id="uploadify_images_bookletPhotoPath" type="file" />
														</c:if></td>
													<td><c:if
															test="${not empty stu.studentInfo.bookletPhotoPath }">
															<img id="student_bookletPhotoPath"
																src="${rootUrl}common/students/${stu.studentInfo.bookletPhotoPath}"
																width="150" height="100" />
														</c:if> <c:if test="${empty stu.studentInfo.bookletPhotoPath }">
															<img id="student_bookletPhotoPath"
																src="${baseUrl }/themes/default/images/img_preview.png"
																width="150" height="100" />
														</c:if> <input type="hidden" name="bookletPhotoPath"
														id="bookletPhotoPathId"
														value="${stu.studentInfo.bookletPhotoPath }" /></td>
												</tr>
												<tr>
													<td width="30%">其他：<br /> 1、其他证件；<br /> 2、大小：≤100k,格式
														:jpg
													</td>
													<td width="20%"><c:if
															test="${open eq 'Y'  and stuts eq 2}">
															<input id="uploadify_images_otherPhotoPath" type="file" />
														</c:if></td>
													<td><c:if
															test="${not empty stu.studentInfo.otherPhotoPath }">
															<img id="student_otherPhotoPath"
																src="${rootUrl}common/students/${stu.studentInfo.otherPhotoPath}"
																width="150" height="100" />
														</c:if> <c:if test="${empty stu.studentInfo.otherPhotoPath }">
															<img id="student_otherPhotoPath"
																src="${baseUrl }/themes/default/images/img_preview.png"
																width="150" height="100" />
														</c:if> <input type="hidden" name="otherPhotoPath"
														id="otherPhotoPathId"
														value="${stu.studentInfo.otherPhotoPath }" /></td>
												</tr>
											</table>
											<span><font color="red">说明：点击上传附件后，需要保存表单.</font> </span>
										</div>
										<%--</c:if> --%>
										<!-- 4 学分信息 -->
										<div>
											<table class="form">
												<tr>
													<td style="width: 20%">教学计划名称：</td>
													<td style="width: 30%">${stu.studentInfo.teachingPlan.major }
														- ${stu.studentInfo.teachingPlan.classic }
														(${stu.studentInfo.teachingPlan.versionNum})</td>
													<td style="width: 20%">办学模式：</td>
													<td style="width: 30%">${ghfn:dictCode2Val('CodeTeachingType',stu.studentInfo.teachingPlan.schoolType) }</td>
												</tr>
												<tr>
													<td width="15%">毕业论文申请最低学分：</td>
													<td width="35%">${stu.studentInfo.teachingPlan.applyPaperMinResult }</td>
													<td width="15%">毕业最低学分：</td>
													<td width="35%">${stu.studentInfo.teachingPlan.minResult }</td>
												</tr>
												<tr>
													<td width="15%">选修课修读门数：</td>
													<td width="35%">${stu.studentInfo.teachingPlan.optionalCourseNum }</td>
													<td width="15%">学位授予：</td>
													<td width="35%">${ghfn:dictCode2Val('CodeDegree',stu.studentInfo.teachingPlan.degreeName ) }</td>
												</tr>
											</table>
											<div
												style="padding-top: 2px; padding-bottom: 2px; font-weight: bold">修读情况：</div>
											<table class="list" width="100%;">
												<tr>
													<th style="width: 5%">序号</th>
													<th style="width: 10%">课程类型</th>
													<th style="width: 17%">课程名称</th>
													<th style="width: 15%">课程类别</th>
													<th style="width: 10%">学时</th>
													<th style="width: 10%">学分</th>
													<th style="width: 10%">成绩</th>
													<th style="width: 12%">主干课</th>
													<th style="width: 11%">建议学期</th>
												</tr>
												<c:forEach
													items="${stu.studentInfo.teachingPlan.teachingPlanCourses }"
													var="c" varStatus="vs">
													<tr>
														<td>${vs.index +1}</td>
														<td>${ghfn:dictCode2Val('CodeCourseType',c.courseType) }</td>
														<td>${c.course.courseName }</td>
														<td>${ghfn:dictCode2Val('courseNature',c.courseNature) }</td>
														<td>${c.stydyHour }</td>
														<td>${c.creditHour }</td>
														<td><c:forEach items="${stu.examResults }"
																var="result">
																<c:if
																	test="${result.courseId eq c.course.resourceid and (result.isDegreeUnitExam eq 'Y' or result.isStateExamResults eq 'Y' or result.isNoExam eq 'Y' or result.checkStatusCode eq '4')}">
																	<c:choose>
																		<c:when test="${result.isNoExam eq 'Y' }">${result.checkStatus }</c:when>
																		<c:when test="${not empty result.examResultsChs}">
																			<c:choose>
																				<c:when test="${result.examResultsChs eq '不及格' }">
																					<span style="color: red;">${result.examResultsChs }</span>
																				</c:when>
																				<c:otherwise>${result.examResultsChs }</c:otherwise>
																			</c:choose>
																		</c:when>
																		<c:otherwise>
										            			${result.integratedScore}
										            		</c:otherwise>
																	</c:choose>
																	<br />
																</c:if>
															</c:forEach></td>
														<td>${ghfn:dictCode2Val('yesOrNo',c.isMainCourse) }</td>
														<td>${ghfn:dictCode2Val('CodeTermType',c.term) }</td>
													</tr>
												</c:forEach>
											</table>
										</div>
										<!-- 5 学费信息 -->
										<div>
											<c:choose>
												<c:when test="${ not empty stu.stuFactFees}">
													<table class="list" width="100%;"
														id="my_studentInfo_stuFactFee">
														<tr>
															<th style="width: 10%">序号</th>
															<c:if test="${schoolCode ne '11078'}">
																<th style="width: 15%">应缴金额</th>
																<th style="width: 15%">实缴金额</th>
																<th style="width: 15%">欠费金额</th>
																<th style="width: 15%">减免金额</th>
															</c:if>
															
															<th style="width: 10%">缴费情况</th>
															<th style="width: 20%">缴费截止日期</th>
														</tr>
														<c:set var="totalFactFee" value="0.0" />
														<c:forEach items="${stu.stuFactFees }" var="fee"
															varStatus="vs">
															<tr>
																<td>${vs.index +1}</td>
																<c:if test="${schoolCode ne '11078'}">
																	<td>${fee.recpayFee }</td>
																	<td>${fee.facepayFee }</td>
																	<td>${fee.unpaidFee }</td>
																	<td>${fee.derateFee }</td>
																</c:if>
																<td>${ghfn:dictCode2Val('CodeChargeStatus',fee.chargeStatus) }</td>
																<td><fmt:formatDate value="${fee.chargeEndDate }"
																		pattern="yyyy-MM-dd" /></td>
															</tr>
														</c:forEach>
														<!-- 
											<tr>													
												<td colspan="4">
												<div style="text-align: right;margin-right: 50px;">
												合计：  ${totalFactFee }元
												</div>
												</td>	
												<td colspan="2"></td>												
											</tr>	
											 -->
													</table>
												</c:when>
												<c:otherwise>
													<font color="red"><b>没有缴费信息！</b></font>
												</c:otherwise>
											</c:choose>
											<c:choose>
												<c:when test="${schoolCode eq '11078'}"><!-- 广大 -->
													<font color="red">说明：应缴金额原则上包含学费及教材费，独立结算教材费的教学点，应缴金额中未包含教材费。附（教材费缴费标准：第一年400元，第二年300元。教材费为预收款，毕业前结算，多退少补）</font>
												</c:when>
												<c:otherwise>
												</c:otherwise>
											</c:choose>
										</div>
									</div>
									
									<div class="tabsFooter">
										<div class="tabsFooterContent"></div>
									</div>
								</div>
								<!-- end tabs -->
							</c:forEach>
						</div>
						<div class="tabsFooter">
							<div class="tabsFooterContent"></div>
						</div>
					</div>
					<!-- end tabs -->
					<%-- <c:if test="${stuts eq '2' }">--%>
					<c:if test="${open eq 'Y' }">
						<div class="formBar">
							<ul>
								<li><div class="buttonActive">
										<div class="buttonContent">
											<button type="submit" onclick="Canceldisabled()">保存</button>
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
					</c:if>
					<%-- </c:if>--%>
				</div>
			</form>
		</div>
	</div>
</body>
</html>