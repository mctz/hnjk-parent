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
		jQuery(document).ready(function(){
			 //初始化学生照片上传			
		      //alert("rootUrl:"+'${rootUrl}');
			 $("#changeStu #uploadify_images_photoPath").uploadify({ 
		            'script'         : baseUrl+'/edu3/filemanage/upload.html',
		            'auto'           : true, //自动上传               
		            'multi'          : false, //多文件上传
		            'scriptData'	 : {'isStoreToDatabase':'N','storePath':'common,students,${storeDir}'},//按学生报名日期创建目录
		            'fileDesc'       : '支持格式:jpg',  //限制文件上传格式描述
		            'fileExt'        : '*.JPG;', //限制文件上传的类型,必须有fileDesc这个性质
		            'sizeLimit'      : 102400, //限制单个文件上传大小40K 
		            'buttonImg'      : baseUrl+'/jscript/jquery.uploadify/images/icon_upload.png', //按钮图片
		            'onInit': function () {       
			        	//载入时触发，将flash设置到最小             
			        	$("#fileQueue").hide();
			        },			        
		            onComplete: function (event, queueID, fileObj, response, data) { //上传成功回调函数
		             	var result = response.split("|");
		              	$('#changeStu #student_photoPath').attr('src','${rootUrl}common/students/${storeDir}/'+result[1]);
		               	$('#changeStu #photoPath').val('${storeDir}/'+result[1]);
					},  
					onError: function(event, queueID, fileObj) {  //上传失败回调函数
					    alert("文件" + fileObj.name + "上传失败"); 
					    $('#changeStu #uploadify').uploadifyClearQueue(); //清空上传队列
					}
	     	 });
	      	 var student_photoPath = '${studentInfo.studentBaseInfo.photoPath}';
	      	 if(student_photoPath != ''){	   
	    		$('#student_photoPath').attr('src','${rootUrl}common/students/${studentInfo.studentBaseInfo.photoPath}'+"?"+Math.random()*1000);	    		
	     	 }	   
	      	 //初始化学生身份证照片上传
	     	 $("#changeStu #uploadify_images_certPhotoPath").uploadify({ 
		            'script'         : baseUrl+'/edu3/filemanage/upload.html', //上传URL
		            'auto'           : true, //自动上传               
		            'multi'          : false, //多文件上传
		            'scriptData'	 : {'isStoreToDatabase':'N','storePath':'common,students,${storeDir}'},//按学生报名日期创建目录
		            'fileDesc'       : '支持格式:jpg',  //限制文件上传格式描述
		            'fileExt'        : '*.JPG;', //限制文件上传的类型,必须有fileDesc这个性质
		            'sizeLimit'      : 102400, //限制单个文件上传大小60K 
		            'buttonImg'      : baseUrl+'/jscript/jquery.uploadify/images/icon_upload.png', //按钮图片
		            onComplete: function (event, queueID, fileObj, response, data) { //上传成功回调函数
		             	var result = response.split("|");                	             	
		              	$('#changeStu #student_certPhotoPath').attr('src','${rootUrl}common/students/${storeDir}/'+result[1]);
		               	$('#changeStu #certPhotoPath').val('${storeDir}/'+result[1]);
					},  
					onError: function(event, queueID, fileObj) {  //上传失败回调函数
					    alert("文件" + fileObj.name + "上传失败"); 
					    $('#changeStu #uploadify').uploadifyClearQueue(); //清空上传队列
					}
	      	 });
	      	 var student_certPhotoPath = '${studentInfo.studentBaseInfo.certPhotoPath}';
	     	 if(student_certPhotoPath != ''){
	    		$('#changeStu #student_certPhotoPath').attr('src','${rootUrl}common/students/${studentInfo.studentBaseInfo.certPhotoPath}'+"?"+Math.random()*1000);
	     	 }
	     	 	 
	     	 //初始化学生其他证件照片上传
	     	 $("#changeStu #uploadify_images_eduPhotoPath").uploadify({ 
		            'script'         : baseUrl+'/edu3/filemanage/upload.html', //上传URL
		            'auto'           : true, //自动上传               
		            'multi'          : false, //多文件上传
		            'scriptData'	 : {'isStoreToDatabase':'N','storePath':'common,students,${storeDir}'},//按学生报名日期创建目录
		            'fileDesc'       : '支持格式:jpg',  //限制文件上传格式描述
		            'fileExt'        : '*.JPG;', //限制文件上传的类型,必须有fileDesc这个性质
		            'sizeLimit'      : 307200, //限制单个文件上传大小60K 
		            'buttonImg'      : baseUrl+'/jscript/jquery.uploadify/images/icon_upload.png', //按钮图片
		            onComplete: function (event, queueID, fileObj, response, data) { //上传成功回调函数
		             	var result = response.split("|");                	             	
		              	$('#changeStu #student_eduPhotoPath').attr('src','${rootUrl}common/students/${storeDir}/'+result[1]);
		               	$('#changeStu #eduPhotoPath').val('${storeDir}/'+result[1]);
					},  
					onError: function(event, queueID, fileObj) {  //上传失败回调函数
					    alert("文件" + fileObj.name + "上传失败"); 
					    $('#changeStu #uploadify').uploadifyClearQueue(); //清空上传队列
					}
	     	 });
	     	 var student_eduPhotoPath = '${studentInfo.studentBaseInfo.eduPhotoPath}';
	      	 if(student_eduPhotoPath != ''){
	    		$('#changeStu #student_eduPhotoPath').attr('src','${rootUrl}common/students/${studentInfo.studentBaseInfo.eduPhotoPath}'+"?"+Math.random()*1000);
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
</script>
	<div class="page">
		<div class="pageContent">
			<form id="changeStuAudit" method="post"
				action="${baseUrl}/edu3/register/studentinfo/auditResults.html"
				onsubmit="return saveChangeForm(this);" class="pageForm"
				enctype="multipart/form-data">
				<div class="pageFormContent" layouth="21">
					<!-- tabs -->
					<div class="tabs">
						<div class="tabsContent" style="height: 100%;">
							<!-- tabs -->
							<div class="tabs">
								<div class="tabsHeader">
									<div class="tabsHeaderContent">
										<ul>
											<li><a href="javascript:void(0)"><span>基本信息</span></a></li>
										</ul>
									</div>
								</div>
								<div class="tabsContent" style="height: 100%;">
									<!-- 1 -->
									<div>
										<div class="panelBar">
											<ul>
												<li><label>审核结果：</label> <select name="auditResults"
													style="width: 130px">
														<option value="">请选择</option>
														<c:choose>
															<c:when test="${auditResults eq 'Y'}">
																<option value="0">撤销</option>
																<option value="2">不通过</option>
															</c:when>
															<c:otherwise>
																<option value="1">通过</option>
																<option value="2">不通过</option>
															</c:otherwise>
														</c:choose>
												</select></li>
											</ul>
										</div>
										<table id="student_base_info_table" class="form">
											<input type="hidden" name="resourceid"
												value="${studentInfo.resourceid }">
											<input type="hidden" name="name"
												value="${studentInfo.studentBaseInfo.name  }">
											<input type="hidden" name="gender"
												value="${studentInfo.studentBaseInfo.gender}">
											<input type="hidden" name="certType"
												value="${studentInfo.studentBaseInfo.certType}">
											<input type="hidden" name="certNum"
												value="${studentInfo.studentBaseInfo.certNum}">
											<input type="hidden" name="photoPath"
												value="${studentInfo.studentBaseInfo.photoPath}">
											<tr>
												<td style="width: 20%">姓名：</td>
												<td style="width: 30%">${studentInfo.studentBaseInfo.name }</td>
												<!--<td style="width:20%" rowspan="4">个人照片：</td>
											<td style="width:30%" rowspan="4">
												<c:if test="${not empty studentInfo.studentBaseInfo.photoPath }">
													<img src="${rootUrl}common/students/${studentInfo.studentBaseInfo.photoPath}" width="90" height="126"/>
												</c:if>
											 </td>-->
												<td>性别：</td>
												<td>${ghfn:dictCode2Val("CodeSex",studentInfo.studentBaseInfo.gender) }</td>
											</tr>
											<tr>
												<td>证件类别：</td>
												<td>${ghfn:dictCode2Val("CodeCertType",studentInfo.studentBaseInfo.certType) }</td>
												<td>证件号码：</td>
												<td>${studentInfo.studentBaseInfo.certNum }</td>
											</tr>
											<tr>
												<td>联系地址:</td>
												<td>${studentInfo.studentBaseInfo.contactAddress }</td>
												<td>联系邮编：</td>
												<td>${studentInfo.studentBaseInfo.contactZipcode }</td>
											</tr>
											<tr>
												<td>联系电话：</td>
												<td>${studentInfo.studentBaseInfo.contactPhone }</td>
												<td>移动电话：</td>
												<td>${studentInfo.studentBaseInfo.mobile }</td>
											</tr>
											<tr>
												<td>邮件：</td>
												<td>${studentInfo.studentBaseInfo.email }</td>
												<td>个人主页：</td>
												<td>${studentInfo.studentBaseInfo.homePage }</td>
											</tr>
											<tr>
												<td>身高：</td>
												<td>${studentInfo.studentBaseInfo.height }</td>
												<td>血型：</td>
												<td>${ghfn:dictCode2Val("CodeBloodStyle",studentInfo.studentBaseInfo.bloodType) }</td>
											</tr>
											<tr>
												<td>出生日期：</td>
												<td><fmt:formatDate
														value="${studentInfo.studentBaseInfo.bornDay }"
														pattern="yyyy-MM-dd" /></td>
												</td>
												<td>出生地：</td>
												<td>${studentInfo.studentBaseInfo.bornAddress }</td>
											</tr>
											<tr>
												<td>国籍：</td>
												<td>${ghfn:dictCode2Val("CodeCountry",studentInfo.studentBaseInfo.country) }</td>
												<td>籍贯：</td>
												<td>${studentInfo.studentBaseInfo.homePlace }</td>
											</tr>
											<tr>
												<td>港澳侨代码：</td>
												<td>${ghfn:dictCode2Val("CodeGAQ",studentInfo.studentBaseInfo.gaqCode) }</td>
												<td>民族：</td>
												<td>${ghfn:dictCode2Val("CodeNation",studentInfo.studentBaseInfo.nation) }</td>
											</tr>
											<tr>
												<td>身体健康状态：</td>
												<td>${ghfn:dictCode2Val("CodeHealth",studentInfo.studentBaseInfo.health ) }</td>
												<td>婚姻状况：</td>
												<td>${ghfn:dictCode2Val("CodeMarriage",studentInfo.studentBaseInfo.marriage ) }</td>
											</tr>
											<tr>
												<td>政治面目：</td>
												<td>${ghfn:dictCode2Val("CodePolitics",studentInfo.studentBaseInfo.politics ) }</td>
												<td>宗教信仰：</td>
												<td>${studentInfo.studentBaseInfo.faith }</td>
											</tr>
											<tr>
												<td>户口性质：</td>
												<td>${ghfn:dictCode2Val("CodeRegisteredResidenceKind",studentInfo.studentBaseInfo.residenceKind  ) }</td>

												<td>户口所在地：</td>
												<td><input name="residence"
													value="${studentInfo.studentBaseInfo.residence }"
													style="width: 48%" readonly="readonly"></td>
											</tr>
											<tr>
												<td>现住址：</td>
												<td>${studentInfo.studentBaseInfo.currenAddress }</td>
												<td>家庭住址：</td>
												<td>${studentInfo.studentBaseInfo.homeAddress }</td>
											</tr>
											<tr>
												<td>家庭住址邮编：</td>
												<td>${studentInfo.studentBaseInfo.homezipCode }</td>
												<td>家庭电话：</td>
												<td>${studentInfo.studentBaseInfo.homePhone }</td>
											</tr>
											<tr>
												<td>公司名称：</td>
												<td>${studentInfo.studentBaseInfo.officeName }</td>
												<td>公司电话：</td>
												<td>${studentInfo.studentBaseInfo.officePhone }</td>
											</tr>
											<tr>
												<td>职务职称：</td>
												<td>${studentInfo.studentBaseInfo.title }</td>
												<td>特长：</td>
												<td>${studentInfo.studentBaseInfo.specialization }</td>
											</tr>
											<tr>
												<td>备注：</td>
												<td colspan="3">${studentInfo.studentBaseInfo.memo }</td>
											</tr>
											<tr>
												<td>照片</td>
												<td colspan="3"><c:if
														test="${not empty studentInfo.studentBaseInfo.photoPath }">
														<img
															src="${rootUrl}common/students/${studentInfo.studentBaseInfo.photoPath}"
															width="90" height="126" />
													</c:if></td>
											</tr>
											<tr>
												<td>身份证复印件</td>
												<td colspan="3">
													<c:if test="${not empty studentInfo.studentBaseInfo.certPhotoPath}">
														<img
															src="${rootUrl}common/students/${studentInfo.studentBaseInfo.certPhotoPath}"
															width="150" height="100" />
													</c:if>
													 <c:if test="${not empty studentInfo.studentBaseInfo.certPhotoPathReverse }">
														<img style="margin-left: 20px;"
															src="${rootUrl}common/students/${studentInfo.studentBaseInfo.certPhotoPathReverse}"
															width="150" height="100" />
													</c:if>
												</td>
											</tr>
											<tr>
												<td>证书复印件</td>
												<td colspan="3"><c:if
														test="${not empty studentInfo.studentBaseInfo.eduPhotoPath}">
														<img
															src="${rootUrl}common/students/${studentInfo.studentBaseInfo.eduPhotoPath}"
															width="150" height="100" />
													</c:if></td>
											</tr>
											<tr>
												<td>户口册证件</td>
												<td colspan="3"><c:if
														test="${not empty studentInfo.bookletPhotoPath}">
														<img
															src="${rootUrl}common/students/${studentInfo.bookletPhotoPath}"
															width="150" height="100" />
													</c:if></td>
											</tr>
											<tr>
												<td>其他证件</td>
												<td colspan="3"><c:if
														test="${not empty studentInfo.otherPhotoPath}">
														<img
															src="${rootUrl}common/students/${studentInfo.otherPhotoPath}"
															width="150" height="100" />
													</c:if></td>
											</tr>

										</table>
									</div>
								</div>
								<div class="tabsFooter">
									<div class="tabsFooterContent"></div>
								</div>
							</div>
							<!-- end tabs -->
						</div>
						<div class="tabsFooter">
							<div class="tabsFooterContent"></div>
						</div>
					</div>
					<!-- end tabs -->
					<div class="formBar">
						<ul>
							<li><div class="buttonActive">
									<div class="buttonContent">
										<button type="submit">保存</button>
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
				</div>
			</form>
		</div>
	</div>
</body>
</html>