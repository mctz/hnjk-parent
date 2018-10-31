<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>我的学籍卡</title>
<style type="text/css">
.pageFormContent select {
	float: none;
}
</style>
</head>
<body>
	<%-- <%@ include file="/jscript/fine-uploader/templates/default.html"%> --%>
	<script type="text/javascript">
		var msg = $("#student_card_form #msg").val();
		if (msg != null && msg != "") {
			alertMsg.warn(msg);
		}
		// 	$(function(){

		// 	})
		$(document)
				.ready(
						function() {
							// 刷新验证码
							$("#studentCard_authCodeImg").attr(
									"src",
									"${baseUrl}/imageCaptcha?now="
											+ new Date().getTime());
							// 刷新页面剩余时间归零
							remainTimeSC = 0;
							$(
									"#student_card_resum input[name=resourceid][value='']")
									.each(
											function() {
												$(this).parents("tr").find(
														"select").val("");
											});
							$(
									"#student_card_pagecontent input[name$=resourceid]")
									.click(
											function() {
												if ($(this).attr("checked") == false) {
													$(this)
															.parents("tr")
															.clone(true)
															.find("select")
															.val("")
															.end()
															.find("input")
															.val("")
															.end()
															.find(
																	"input[type='checkbox']")
															.attr("checked",
																	"checked")
															.end()
															.appendTo(
																	$(this)
																			.parents(
																					"tbody"));
													$(this).parents("tr")
															.remove();
												}
											});
							jQuery("#student_card_form #ChinaArea1")
									.jChinaArea({
										// 	 aspnet:true,
										s1 : "${homePlace_province}",//默认选中的省名
										s2 : "${homePlace_city}",//默认选中的市名
										s3 : "${homePlace_county}"//默认选中的县区名
									})
							jQuery("#student_card_form #ChinaArea2")
									.jChinaArea({
										// 	 aspnet:true,
										s1 : "${residence_province}",//默认选中的省名
										s2 : "${residence_city}",//默认选中的市名
										s3 : "${residence_county}"//默认选中的县区名
									})

							var student_certPhotoPath = '${stu.studentInfo.studentBaseInfo.certPhotoPath}';
							var student_certPhotoPathReverse = '${stu.studentInfo.studentBaseInfo.certPhotoPathReverse}';

							//初始化学生身份证照片上传
							$("#uploadify_images_certPhotoPath")
									.fineUploader(
											{
												multiple : false,
												request : {
													endpoint : baseUrl
															+ '/edu3/filemanage/uploader.html',
													params : {
														'isStoreToDatabase' : 'N',
														'storePath' : 'common,students'
													}
												},
												validation : {
													allowedExtensions : [
															'JPG', 'jpg' ],
													sizeLimit : 253600,
													acceptFiles : 'image/pjpeg,image/jpeg'
												},
												messages : {
													// typeError: "文件类型错误",  
													//  sizeError: "文件过大",
													// onLeave:'文件正在上传，如果你现在离开，上传将被取消'  
													typeError : "{file} 无效的扩展名.支持的扩展名: {extensions}.",
													sizeError : "{file} 文件太大，最大支持的文件大小 {sizeLimit}.",
													minSizeError : "{file} 太小，最小文件大小为：{minSizeLimit}.",
													emptyError : "{file} 文件为空，请重新选择",
													noFilesError : "文件不存在",
													onLeave : "文件正在上传，如果你现在离开，上传将被取消"
												},
												callbacks : {
													onCancel : function(id,
															name) {
													},
													onComplete : function(id,
															name, responseJSON) {
														if (responseJSON.success
																&& responseJSON.result == "OK") {
															var path = 'common/students/'
																	+ responseJSON.attSerName;
															$
																	.ajax({
																		type : "POST",
																		url : "${baseUrl}/edu3/filemanage/pressText.html",
																		data : {
																			path : path
																		},
																		dataType : 'json',
																		success : function(
																				data) {
																			$(
																					'#student_card_form #student_certPhotoPath')
																					.attr(
																							'src',
																							'${rootUrl}common/students/'
																									+ responseJSON.attSerName);
																			$(
																					'#student_card_form #certPhotoPathId')
																					.val(
																							responseJSON.attSerName);
																		}
																	})

														} else {
															alertMsg.error("文件"
																	+ name
																	+ "上传失败");
														}
													},
													onError : function(id,
															name, errorReason) {
														//alert("错误："+errorReason);
													}
												}
											});

							if (student_certPhotoPath != '') {
								$('#student_card_form #student_certPhotoPath')
										.attr(
												'src',
												'${rootUrl}common/students/${stu.studentInfo.studentBaseInfo.certPhotoPath}'
														+ "?" + Math.random()
														* 1000);
								$('#certPhotoPathId')
										.val(
												'${stu.studentInfo.studentBaseInfo.certPhotoPath}');
							}

							//初始化学生身份证照片上传(反面)
							$("#uploadify_images_certPhotoPath_reverse")
									.fineUploader(
											{
												multiple : false,
												request : {
													endpoint : baseUrl
															+ '/edu3/filemanage/uploader.html',
													params : {
														'isStoreToDatabase' : 'N',
														'storePath' : 'common,students'
													}
												},
												validation : {
													allowedExtensions : [
															'JPG', 'jpg' ],
													sizeLimit : 253600,
													acceptFiles : 'image/pjpeg,image/jpeg'
												},
												text : {
													failUpload : '上传失败',
													formatProgress : "{percent}% 已上传，文件大小为{total_size}",
													waitingForResponse : "处理中..."
												},
												messages : {
													//  typeError: "文件类型错误",  
													// sizeError: "文件过大",
													//  onLeave:'文件正在上传，如果你现在离开，上传将被取消'  
													typeError : "{file} 无效的扩展名.支持的扩展名: {extensions}.",
													sizeError : "{file} 文件太大，最大支持的文件大小 {sizeLimit}.",
													minSizeError : "{file} 太小，最小文件大小为：{minSizeLimit}.",
													emptyError : "{file} 文件为空，请重新选择",
													noFilesError : "文件不存在",
													onLeave : "文件正在上传，如果你现在离开，上传将被取消"
												},
												callbacks : {
													onCancel : function(id,
															name) {
													},
													onComplete : function(id,
															name, responseJSON) {
														if (responseJSON.success
																&& responseJSON.result == "OK") {
															var path = 'common/students/'
																	+ responseJSON.attSerName;
															$
																	.ajax({
																		type : "POST",
																		url : "${baseUrl}/edu3/filemanage/pressText.html",
																		data : {
																			path : path
																		},
																		dataType : 'json',
																		success : function(
																				data) {
																			$(
																					'#student_card_form #student_certPhotoPathReverse')
																					.attr(
																							'src',
																							'${rootUrl}common/students/'
																									+ responseJSON.attSerName);
																			$(
																					'#student_card_form #certPhotoPathIdReverse')
																					.val(
																							responseJSON.attSerName);
																		}
																	})

														} else {
															alertMsg.error("文件"
																	+ name
																	+ "上传失败");
														}
													},
													onError : function(id,
															name, errorReason) {
														//alert("错误："+errorReason);
													}
												}
											});

							if (student_certPhotoPathReverse != '') {
								$(
										'#student_card_form #student_certPhotoPathReverse')
										.attr(
												'src',
												'${rootUrl}common/students/${stu.studentInfo.studentBaseInfo.certPhotoPathReverse}'
														+ "?"
														+ Math.random()
														* 1000);
								$('#certPhotoPathIdReverse')
										.val(
												'${stu.studentInfo.studentBaseInfo.certPhotoPathReverse}');
							}

						});
		//提交前校验数据完整
		function submitBeforeCheck(form) {

			var $form = $(form);

			var resum_pass = true;
			var f_ralation_pass = true;
			var s_ralation_pass = true;
			var resumArray = new Array();
			var f_ralArray = new Array();
			var s_ralArray = new Array();

			var mobile = $("#student_card_form #studentCard_mobile").val();
			var email = $("#student_card_form #email");
			var homePhone = $("#student_card_form #homePhone").val();
			var homezipCode = $("#student_card_form #homezipCode");
			var officePhone = $("#student_card_form #officePhone");
			var officeZipcode = $("#student_card_form #officeZipcode");
			var marriage = $("#student_card_form #marriage");

			$(form).find("#student_card_resum tr:not(:first)").each(function() {
				var notNullArray = new Array();
				$(this).find("select").each(function() {
					if ($(this).val())
						notNullArray.push($(this).val());
				});
				$(this).find("input:not(:first)").each(function() {
					if ($(this).val())
						notNullArray.push($(this).val());
				});

				if (notNullArray.length != 0 && notNullArray.length < 6) {
					resum_pass = false;
				} else if (notNullArray.length == 6) {
					resumArray.push("0");
				}

			});
			$(form).find("#student_card_f_ralation tr:not(:first)")
					.each(
							function() {
								var notNullArray = new Array();
								$(this).find("select").each(function() {
									if ($(this).val())
										notNullArray.push($(this).val());
								});
								$(this).find("input:not(:first)").each(
										function() {
											if ($(this).val())
												notNullArray
														.push($(this).val());
										});
								if (notNullArray.length != 0
										&& notNullArray.length < 6) {
									f_ralation_pass = false;
								} else if (notNullArray.length == 6) {
									f_ralArray.push("0");
								}

							});

			// 验证是否填写主要社会关系
			$(form)
					.find("#student_card_s_ralation tr:not(:first)")
					.each(
							function() {
								var nameVal = $(this).find("td:eq(1)").find(
										"input:eq(0)").val();
								var ralationVal = $(this).find("td:eq(2)")
										.find("select:eq(0)").val();
								var workPlaceVal = $(this).find("td:eq(3)")
										.find("input:eq(0)").val();
								if (!((nameVal && ralationVal && workPlaceVal) || (!nameVal
										&& !ralationVal && !workPlaceVal))) {
									s_ralation_pass = false;
								}
							});
			/** 
			$(form).find("#student_card_s_ralation tr:not(:first)").each(function(){
				var notNullArray  = new Array();
				$(this).find("select").each(function(){
					if($(this).val()) notNullArray.push($(this).val());
				});
				$(this).find("input:not(:first)").each(function(){
					if($(this).val()) notNullArray.push($(this).val());
				});
				if(notNullArray.length!=0 && notNullArray.length<4){
					s_ralation_pass  = false;
				}else if(notNullArray.length==4){
					s_ralArray.push("0");
				}
			});
			 **/

			if (resum_pass == false) {
				alertMsg.warn("个人简历中起止年月、在何单位学习、工作、职务为必填项,不能为空!");
				return false;
			}
			if (f_ralation_pass == false) {
				alertMsg.warn("家庭主要成员中姓名、性别、关系、工作单位为必填项,不能为空!");
				return false;
			}
			if (s_ralation_pass == false) {
				alertMsg.warn("主要社会关系中姓名、关系、工作单位未填完，即如果填写一栏，这三项为必填项!");
				return false;
			}
			/** 
			if(s_ralation_pass==false){
				alertMsg.warn("主要社会关系中姓名、关系、工作单位为必填项,不能为空!");
				return false;
			}
			 **/
			if (resumArray.length < 1) {
				alertMsg.warn("个人简历至少应填定一条!");
				return false;
			}
			if (f_ralArray.length < 1) {
				alertMsg.warn("家庭主要成员至少应填定一位!");
				return false;
			}
			if ("${schoolCode eq '11078'}") {//如果是广大，则必须要上传身份证扫描件
				if ("${empty studentInfo.studentBaseInfo.certPhotoPath}") {
					var srcValue = $("#student_certPhotoPath").attr("src");
					if (srcValue == "${baseUrl}/themes/default/images/img_preview.png") {
						alertMsg.warn("请上传身份证正面图片!");
						return false;
					}
				}
				if ("${empty studentInfo.studentBaseInfo.certPhotoPathReverse}") {
					var srcValue = $("#student_certPhotoPathReverse").attr(
							"src");
					if (srcValue == '${baseUrl}/themes/default/images/img_preview.png') {
						alertMsg.warn("请上传身份证反面图片!");
						return false;
					}
				}
			}
			/* 
			if(s_ralArray.length<1){
				alertMsg.warn("主要社会关系至少应填定一个!");
				return false;
			}
			 */
			//var student_card_reWardsPuniShment= $("#student_card_reWardsPuniShment #student_card_reWardsPuniShment").val();
			//if(student_card_reWardsPuniShment.length<=0){
			//	alertMsg.warn("奖罚情况不能为空！");
			//	return false;
			//}
			//if(student_card_reWardsPuniShment.length>100){
			//	alertMsg.warn("奖罚情况字数不能大于100！");
			//	return false;
			//}
			jQuery("#student_card_form #homePlace")
					.val(
							jQuery(
									"#student_card_form #homePlace_province option:selected")
									.text()
									+ ","
									+ jQuery(
											"#student_card_form #homePlace_city option:selected")
											.text()
									+ ","
									+ jQuery(
											"#student_card_form #homePlace_county option:selected")
											.text());

			jQuery("#student_card_form #residence")
					.val(
							jQuery(
									"#student_card_form #residence_province option:selected")
									.text()
									+ ","
									+ jQuery(
											"#student_card_form #residence_city option:selected")
											.text()
									+ ","
									+ jQuery(
											"#student_card_form #residence_county option:selected")
											.text());
			return validateCallback(form);
		}
		//startYear=${startYear}&startMonth=${endYear}&endMonth=&{endMonth}&company=${company}&title=${title}
		//学籍卡打印 

		function studentInfoCardPrint() {
			var indate = $("#inDate").val();
			var url = "${baseUrl}/edu3/roll/studentCard/print-viewinstupage.html?resourceid_i=${studentInfo.resourceid}&indate="
					+ indate
					+ "&from=studentinfo&flag=print&stuChangeInfo=${stuChangeInfo}&startYear=${resum.startYear }&startMonth=${startMonth}&endYear=${resum.endYear}&endMonth=&{resum.endMonth}&company=${resum.company}&title=${resum.title}";
			$.pdialog.open(url, "RES_SCHOOL_ROLL_INFOCARD_PRINTINSTUDENTPAGE",
					"打印预览", {
						width : 800,
						height : 600
					});
		}
		//学籍卡下载
		function studentInfoCardDownload() {
			//var url          = "${baseUrl}/edu3/roll/studentCard/printAndExport.html?resourceid_i=${studentInfo.resourceid}&flag=download&from=studentinfo";
			var schoolCode = "${schoolCode}";
			var url = "";
			//每个学校的学籍卡都可能不一样，因此，不同学校调用不同的方法及打印模板 
			//广大的学籍卡，此处需要加 fromStudentRoll=1,后台已经强制写为PDF下载，不需要传入参数 isPDF
			//目前暂时只给广大增加学生的下载PDF
			switch (schoolCode) {
			case '11078':
				url = "${baseUrl}/edu3/roll/studentCardTwosided/downloadPDF.html?studentids=${studentInfo.resourceid}&fromStudentRoll=1";
				break;
			default:
				url = "${baseUrl}/edu3/roll/studentCard/printAndExport.html?resourceid_i=${studentInfo.resourceid}&flag=download&from=studentinfo";
				break;
			}
			// 		var url = "${baseUrl}/edu3/roll/studentCardTwosided/downloadPDF.html?studentids="+${studentInfo.resourceid};
			downloadFileByIframe(url,
					'rollinfocard_printAndExport_ExportIframe');
		}
		//毕业生登记表下载
		function studentRegistryFormDownload() {
			var schoolCode = "${schoolCode}";
			var url = "";
			//每个学校的毕业生登记表都可能不一样，因此，不同学校调用不同的方法及打印模板
			//目前只给桂林医的学生角色增加了下载PDF
			//管理员角色未增加功能，增加只需要调用相同的url ，传入 studentids ，以勾选的方式进行下载
			//switch (schoolCode) {
			//case '10601':
			url = "${baseUrl}/edu3/roll/studentRegistryForm/downloadPDF.html?isPdf=Y&studentids=${studentInfo.resourceid}";
			//	break;
			//default:
			//url= "${baseUrl}/edu3/roll/studentCard/printAndExport.html?resourceid_i=${studentInfo.resourceid}&flag=download&from=studentinfo";
			//	break;
			//}
			downloadFileByIframe(url, 'stuRegistryForm_pdf_downloadIframe');
		}
		//学籍卡提交
		function studentInfoCardSubmit() {
			var form = $("#student_card_form");
			var selfAssessment = $("#stucard_selfAssessment").val();
			$("#student_card_form #type").val("submit");
			//if(selfAssessment!='' && selfAssessment.length<100){
			//alertMsg.warn("自我鉴定不能少于100字！")
			//return false;
			//}else{
			alertMsg.confirm("是否要提交你的学籍表信息？提交后不允许更改学籍表的信息。", {
				okCall : function() {//执行			
					submitBeforeCheck(form);
				}
			});
			//}
		}

		// 学籍卡保存
		function studentInfoCardSave() {
			var form = $("#student_card_form");
			$("#student_card_form #type").val("save");
			submitBeforeCheck(form);
		}

		// 获取手机短信验证码
		//timer变量，控制时间
		var interValObjSC;
		var remainTimeSC;
		function getSCMsgAuthCode() {
			var validateCode = $("#studentCard_authCode").val();
			if (!validateCode) {
				alertMsg.warn("验证码不能为空！");
				return false;
			}
			var phone = $("#studentCard_mobile").val();
			if (!phone) {
				alertMsg.warn("手机号码不能为空！");
				return false;
			}
			//if( !(/^0{0,1}1([3-9])[0-9]{9}$/.test(phone))){ 
			if (!isPhoneLegal(phone)) {
				alertMsg.warn("不合法的手机号！");
				return false;
			}
			remainTimeSC = 120;// 两分钟后重发 

			$("#studentCard_getMsgAuthCode").attr("disabled", "disabled");
			interValObjSC = window.setInterval(setRemainTimeSC, 1000); // 启动计时器，1秒执行一次
			$
					.ajax({
						type : "POST",
						url : "${baseUrl}/mas/getMsgAuthCode.html",
						data : {
							phone : phone,
							authCode : validateCode
						},
						dataType : 'json',
						success : function(data) {
							if (data.statusCode == 200) {
								alertMsg.correct(phone + "短信验证码发送成功");
								// 刷新验证码
								$("#studentCard_authCodeImg").attr(
										"src",
										"${baseUrl}/imageCaptcha?now="
												+ new Date().getTime());
							} else {
								window.clearInterval(interValObjSC);// 停止计时器
								$("#studentCard_getMsgAuthCode").removeAttr(
										"disabled");// 启用按钮
								//	    			$("#studentCard_getMsgAuthCode").text("发送");
								$("#studentCard_getMsgAuthCode").val("发送");
								alertMsg.error(data.message);
								//alertMsg.error(phone+"短信验证码发送失败");
							}
						}
					});
		}

		//timer处理函数
		function setRemainTimeSC() {
			if (remainTimeSC == 0) {
				window.clearInterval(interValObjSC);// 停止计时器
				$("#studentCard_getMsgAuthCode").removeAttr("disabled");// 启用按钮
				//$("#studentCard_getMsgAuthCode").text("发送");
				$("#studentCard_getMsgAuthCode").val("发送");
			} else {
				remainTimeSC--;
				//$("#studentCard_getMsgAuthCode").text(remainTimeSC+"秒后重发");
				$("#studentCard_getMsgAuthCode").val(remainTimeSC + "秒后重发");
			}
		}
		function openTab(studentId){
			
			var url = "${baseUrl }/edu3/student/roll-card-form.html";
			var data={studentId:studentId}
			navTab.reload(url,data);
		}
	</script>
	<c:if test="${!empty listStu }">
		<div class="tabs" currentIndex="${currentIndex}" eventType="click">
			<div class="tabsHeader">
				<div class="tabsHeaderContent">
					<ul>
						<c:forEach items="${listStu }" var="stu">
							<li class="selected"><a href="javascript:void(0)" onclick="openTab('${stu.resourceid }')"><span>${stu.teachingPlan.major }-${stu.teachingPlan.classic }-${stu.grade }</span></a></li>
						</c:forEach>
					</ul>
				</div>
			</div>
		</div>
	</c:if>
	<div class="page">
		<div class="pageContent" align="center" id="student_card_pagecontent">
			<form id="student_card_form" method="post"
				action="${baseUrl}/edu3/student/roll-card-save.html"
				class="pageForm" onsubmit="return submitBeforeCheck(this);">
				<input type="hidden" value="${msg }" id="msg" name="msg"> <input
					type="hidden" value="" id="type" name="type"> <input
					type="hidden"
					value="<fmt:formatDate value="${studentInfo.inDate}" pattern="yyyy-MM-dd"/>"
					id="inDate">
				<div class="pageFormContent" layoutH="95">
					<table class="form" id="student_card_baseinfo">
						<thead>
							<tr>
								<td colspan="9"
									style="text-align: center; font-weight: bold; font-size: 16px; color: black; height: 40px">${schoolname}学生学籍表</td>
							</tr>
						</thead>
						<tr>
							<td rowspan="10"
								style="width: 5%; text-align: center; font-weight: bolder; color: black; font-size: 13px;">个</br>人</br>信</br>息
							</td>
							<td style="width: 10%; font-weight: bolder; color: black;">姓名</td>
							<td style="width: 23%;">${studentInfo.studentBaseInfo.name }</td>
							<td style="width: 10%; font-weight: bolder; color: black;">曾用名</td>
							<td style="width: 10%;">${studentInfo.studentBaseInfo.nameUsed }</td>
							<td style="width: 10%; font-weight: bolder; color: black;">性别</td>
							<td style="width: 12%;" name="gender">${ghfn:dictCode2Val("CodeSex",studentInfo.studentBaseInfo.gender) }</td>
							<td style="width: 20%" rowspan="6" align="center" colspan="2">
								<c:if test="${not empty studentInfo.studentBaseInfo.photoPath }">
									<img
										src="${rootUrl}common/students/${studentInfo.studentBaseInfo.photoPath}"
										width="90" height="126" />
								</c:if>
							</td>
						</tr>
						<tr>
							<td style="font-weight: bolder; color: black;">民族</td>
							<td>${ghfn:dictCode2Val("CodeNation",studentInfo.studentBaseInfo.nation) }</td>
							<td style="font-weight: bolder; color: black;">出生日期</td>
							<td colspan="3"><fmt:formatDate
									value="${studentInfo.studentBaseInfo.bornDay }"
									pattern="yyyy年MM月dd日" /></td>
						</tr>
						<tr>
							<td style="font-weight: bolder; color: black;">政治面目</td>
							<td><gh:select name="politics" dictionaryCode="CodePolitics"
									value="${studentInfo.studentBaseInfo.politics }"
									style="width:50%" classCss="required" /> <font color="red">*</font>

							</td>
							<td style="font-weight: bolder; color: black;">证件号码</td>
							<td colspan="3">${studentInfo.studentBaseInfo.certNum }</td>
						</tr>
						<tr>
							<td style="font-weight: bolder; color: black;">学习中心</td>
							<td>${studentInfo.branchSchool.unitName}</td>
							<td style="font-weight: bolder; color: black;">年级</td>
							<td>${studentInfo.grade.gradeName }</td>
							<td style="font-weight: bolder; color: black;">层次</td>
							<td>${studentInfo.classic.shortName }</td>
						</tr>
						<tr>
							<td style="font-weight: bolder; color: black;">专业</td>
							<td>${studentInfo.major.majorName}</td>
							<td style="font-weight: bolder; color: black;">学号</td>
							<input type="hidden" name="studyNo" value="${studentInfo.studyNo }">
							<td colspan="3">${studentInfo.studyNo }</td>

						</tr>
						<tr>
							<td style="font-weight: bolder; color: black;">籍贯</td>
							<td>
								<div id="ChinaArea1">
									<select id="homePlace_province" name="homePlace_province"
										style="width: 70px;"></select> <select id="homePlace_city"
										name="homePlace_city" style="width: 70px;"></select> <select
										id="homePlace_county" name="homePlace_county"
										style="width: 70px;">
									</select>
								</div> <input type="hidden" id="homePlace" name="homePlace"
								value="${studentInfo.studentBaseInfo.homePlace }">
							</td>
							<td style="font-weight: bolder; color: black;">户口所在地</td>
							<td colspan="3"><input type="hidden" id="residence"
								name="residence"
								value="${studentInfo.studentBaseInfo.residence }">
								<div id="ChinaArea2">
									<select id="residence_province" name="residence_province"
										style="width: 80px;"></select> <select id="residence_city"
										name="residence_city" style="width: 80px;"></select> <select
										id="residence_county" name="residence_county"
										style="width: 80px;">
									</select>
								</div></td>
							<!--<td style="font-weight: bolder;color: black;">手机号码</td>
				<td><input type="text" name="mobile" id="mobile" value="${studentInfo.studentBaseInfo.mobile }" class="mobile"/></td>
				<td style="font-weight: bolder;color: black;">已未婚</td>
				<td colspan="1">
				<gh:select name="marriage"  dictionaryCode="CodeMarriage"  value="${studentInfo.studentBaseInfo.marriage }" style="width:80%" />
				</td>	
			-->
						</tr>
						<c:choose>
							<c:when test="${phoneComfirm=='1' }">
								<tr>
									<td style="font-weight: bolder; color: black;">手机号码</td>
									<td><input type="text" name="mobile"
										id="studentCard_mobile"
										value="${studentInfo.studentBaseInfo.mobile }"
										class="required mobile" /></td>
									<td style="font-weight: bolder; color: black;">验证码</td>
									<td colspan="3"><input type="text"
										id="studentCard_authCode" name="authCode" size="10"
										style="height: 20px;" /> <img src="${baseUrl}/imageCaptcha"
										id="studentCard_authCodeImg"
										style="margin-left: 10px; cursor: pointer;"
										onclick="this.src='${baseUrl}/imageCaptcha?now=' + new Date().getTime()"
										title="换一张" align="left"> <label
										style="color: red; width: 200px">注意：验证码有效时间为
											${validTime } 分钟！</label></td>
									<td style="font-weight: bolder; color: black;">手机验证码</td>
									<td><input type="text" id="studentCard_msgAuthCode"
										name="msgAuthCode" size="15" style="height: 20px;" /> <c:if
											test="${studentInfo.rollCardStatus ne '2'}">
											<input type="button" id="studentCard_getMsgAuthCode"
												onclick="return getSCMsgAuthCode();"
												style="margin-left: 5px; cursor: pointer; height: 28px;"
												value="发送" />
											<!-- <button  id="studentCard_getMsgAuthCode" onclick="return getSCMsgAuthCode();" style="margin-leftt:5px;cursor: pointer;height: 28px;">发送</button> -->
										</c:if></td>
								</tr>
								<tr>
									<td style="font-weight: bolder; color: black;">家庭住址</td>
									<td><input type="text" name="homeAddress" id="homeAddress"
										value="${studentInfo.studentBaseInfo.homeAddress }"
										class="required" /></td>
									<td style="font-weight: bolder; color: black;">家庭邮编</td>
									<td><input type="text" name="homezipCode" id="homezipCode"
										value="${studentInfo.studentBaseInfo.homezipCode }"
										maxlength="6" class="required" /></td>
									<td style="font-weight: bolder; color: black;">家庭电话</td>
									<td colspan="3"><input type="text" name="homePhone"
										id="homePhone"
										value="${studentInfo.studentBaseInfo.homePhone }"
										class="required phone" maxlength="12" /></td>
								</tr>
								<tr>
									<td style="font-weight: bolder; color: black;">工作单位</td>
									<td><input type="text" name="officeName" id="officeName"
										value="${studentInfo.studentBaseInfo.officeName }" /></td>
									<td style="font-weight: bolder; color: black;">单位邮编</td>
									<td><input type="text" name="officeZipcode"
										id="officeZipcode"
										value="${studentInfo.studentBaseInfo.officeZipcode }"
										maxlength="6" /></td>
									<td style="font-weight: bolder; color: black;">单位电话</td>
									<td><input type="text" name="officePhone" id="officePhone"
										value="${studentInfo.studentBaseInfo.officePhone }"
										class="phone" maxlength="12" /></td>
									<td style="font-weight: bolder; color: black;">单位职务</td>
									<td><input type="text" name="officeTitle" id="officeTitle"
										value="${studentInfo.studentBaseInfo.title }" maxlength="12" /></td>
								</tr>
								<tr>
									<td style="font-weight: bolder; color: black;">电子邮件</td>
									<td><input type="text" name="email" id="email"
										value="${studentInfo.studentBaseInfo.email }" class="email" /></td>
									<td style="font-weight: bolder; color: black;">入团时间</td>
									<td><input type="text" style="width: 120px;"
										id="organizationDateid" name="organizationDate" class="Wdate"
										value="${studentInfo.organizationDate}"
										onfocus="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate:'#F{$dp.$D(\'joinPartyDateid\')}'})" /></td>
									<td style="font-weight: bolder; color: black;">入党时间</td>
									<td><input type="text" style="width: 120px;"
										id="joinPartyDateid" name="joinPartyDate" class="Wdate"
										value="${studentInfo.joinPartyDate}"
										onfocus="WdatePicker({dateFmt:'yyyy-MM-dd',minDate:'#F{$dp.$D(organizationDateid)}',maxDate:'%y-%M-%d'})" /></td>
									<td style="font-weight: bolder; color: black;">婚否</td>
									<td><gh:select name="marriage" id="marriage"
											dictionaryCode="CodeMarriage"
											value="${studentInfo.studentBaseInfo.marriage }"
											style="width:80%" /></td>
								</tr>
							</c:when>
							<c:otherwise>
								<tr>
									<td style="font-weight: bolder; color: black;">手机号码</td>
									<td><input type="text" name="mobile"
										id="studentCard_mobile"
										value="${studentInfo.studentBaseInfo.mobile }"
										class="required mobile" /></td>
									<!--<td style="font-weight: bolder;color: black;">已未婚</td>
						<td colspan="4">
						<gh:select name="marriage"  dictionaryCode="CodeMarriage"  value="${studentInfo.studentBaseInfo.marriage }" style="width:20%" />
						</td>	
						-->
									<td style="font-weight: bolder; color: black;">电子邮件</td>
									<td colspan="2"><input type="text" name="email" id="email"
										value="${studentInfo.studentBaseInfo.email }" class="email" /></td>
									<td style="font-weight: bolder; color: black;">婚否</td>
									<td colspan="2"><gh:select name="marriage" id="marriage"
											dictionaryCode="CodeMarriage"
											value="${studentInfo.studentBaseInfo.marriage }"
											style="width:80%" /></td>
								</tr>
								<tr>
									<td style="font-weight: bolder; color: black;">家庭住址</td>
									<td><input type="text" name="homeAddress" id="homeAddress"
										value="${studentInfo.studentBaseInfo.homeAddress }"
										class="required" /></td>
									<td style="font-weight: bolder; color: black;">家庭邮编</td>
									<td><input type="text" name="homezipCode" id="homezipCode"
										value="${studentInfo.studentBaseInfo.homezipCode }"
										maxlength="6" class="required" /></td>
									<td style="font-weight: bolder; color: black;">家庭电话</td>
									<td colspan="2"><input type="text" name="homePhone"
										id="homePhone"
										value="${studentInfo.studentBaseInfo.homePhone }"
										class="required phone" maxlength="12" /></td>
								</tr>
								<tr>
									<td style="font-weight: bolder; color: black;">工作单位</td>
									<td><input type="text" name="officeName" id="officeName"
										value="${studentInfo.studentBaseInfo.officeName }" /></td>
									<td style="font-weight: bolder; color: black;">单位邮编</td>
									<td><input type="text" name="officeZipcode"
										id="officeZipcode"
										value="${studentInfo.studentBaseInfo.officeZipcode }"
										maxlength="6" /></td>
									<td style="font-weight: bolder; color: black;">单位电话</td>
									<td colspan="2"><input type="text" name="officePhone"
										id="officePhone"
										value="${studentInfo.studentBaseInfo.officePhone }"
										class="phone" maxlength="12" /></td>
								</tr>
								<tr>
									<td style="font-weight: bolder; color: black;">单位职务</td>
									<td><input type="text" name=officeTitle id="officeTitle"
										value="${studentInfo.studentBaseInfo.title }" maxlength="12" /></td>
									<td style="font-weight: bolder; color: black;">入团时间</td>
									<td><input type="text" style="width: 120px;"
										id="organizationDateid" name="organizationDate" class="Wdate"
										value="${studentInfo.organizationDate}"
										onfocus="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate:'#F{$dp.$D(\'joinPartyDateid\')}'})" /></td>
									<td style="font-weight: bolder; color: black;">入党时间</td>
									<td colspan="2"><input type="text" style="width: 120px;"
										id="joinPartyDateid" name="joinPartyDate" class="Wdate"
										value="${studentInfo.joinPartyDate}"
										onfocus="WdatePicker({dateFmt:'yyyy-MM-dd',minDate:'#F{$dp.$D(organizationDateid)}',maxDate:'%y-%M-%d'})" /></td>
								</tr>
							</c:otherwise>
						</c:choose>
					</table>
					<%--个人简历--%>
					<table class="form" id="student_card_resum">
						<c:set var="addRowNum" value="${5-fn:length(resums) }"></c:set>
						<tbody>
							<tr>
								<td
									style="width: 5%; text-align: center; font-weight: bolder; color: black; font-size: 13px;"
									<c:if test="${addRowNum >0}">rowspan="${addRowNum +6}"</c:if>
									<c:if test="${addRowNum == 0}">rowspan="6"</c:if>>个</br>人</br>简</br>历
								</td>
								<td
									style="text-align: left; font-weight: bolder; color: red; font-size: 13px;"
									colspan="5">备注：个人简历从初中开始填写</td>
							</tr>
							<tr>
								<td style="width: 5%; text-align: center;"></td>
								<td
									style="width: 35%; font-weight: bolder; color: black; text-align: center;">起止年月(必填项)</td>
								<td
									style="width: 30%; font-weight: bolder; color: black; text-align: center;">在何单位学习、工作(必填项)</td>
								<td
									style="width: 15%; font-weight: bolder; color: black; text-align: center;">职务(必填项)</td>
								<!-- <td style="width:10%;font-weight: bolder;color: black;text-align: center;">证明人(必填项)</td> -->
							</tr>
							<c:forEach var="resum" items="${resums }" varStatus="vs">
								<tr>
									<td style="text-align: center;"><input type="checkbox"
										name="resourceid" value="${resum.resourceid }"
										checked="checked" /></td>
									<td style="text-align: center;"><gh:select
											name="startYear" dictionaryCode="CodeYear"
											value="${resum.startYear }" size="100" orderType="desc" />年
										<gh:select name="startMonth" dictionaryCode="CodeMonth"
											value="${resum.startMonth }" />月 至 <gh:select name="endYear"
											dictionaryCode="CodeYear" value="${resum.endYear }"
											size="100" orderType="desc" />年 <gh:select name="endMonth"
											dictionaryCode="CodeMonth" value="${resum.endMonth }" />月</td>
									<td style="text-align: center;"><input type="text"
										name="company" value="${resum.company }" /></td>
									<td style="text-align: center;"><input type="text"
										name="title" value="${resum.title }" /></td>
									<%-- <td style="text-align: center;"><input type="text" name="attestator" value="${resum.attestator }" /></td> --%>
								</tr>
							</c:forEach>
							<c:if test="${addRowNum >0}">
								<c:forEach begin="1" end="${addRowNum }" varStatus="vs">
									<tr>
										<td style="text-align: center;"><input type="checkbox"
											name="resourceid" value="" checked="checked" /></td>
										<td style="text-align: center;"><gh:select
												name="startYear" dictionaryCode="CodeYear" choose="Y"
												size="100" orderType="desc" />年 <gh:select
												name="startMonth" dictionaryCode="CodeMonth" choose="Y" />月
											至 <gh:select name="endYear" dictionaryCode="CodeYear"
												choose="Y" size="100" orderType="desc" />年 <gh:select
												name="endMonth" dictionaryCode="CodeMonth" choose="Y" />月</td>
										<td style="text-align: center;"><input type="text"
											name="company" /></td>
										<td style="text-align: center;"><input type="text"
											name="title" /></td>
										<!-- <td style="text-align: center;"><input type="text"  name="attestator"/>   </td> -->
									</tr>
								</c:forEach>
							</c:if>
						</tbody>
					</table>
					<%--个人简历--%>
					<%--家庭主要成员--%>
					<table class="form" id="student_card_f_ralation">
						<c:set var="addRowNum" value="${5-fn:length(f_rals) }"></c:set>
						<tbody>
							<tr>
								<td
									style="width: 5%; text-align: center; font-weight: bolder; color: black; font-size: 13px;"
									<c:if test="${addRowNum >0}">rowspan="${addRowNum +6}"</c:if>
									<c:if test="${addRowNum == 0}">rowspan="6"</c:if>>家</br>庭</br>主</br>要</br>成</br>员
								</td>
								<td
									style="text-align: left; font-weight: bolder; color: red; font-size: 13px;"
									colspan="7">备注：家庭主要成员至少填写一项及以上</td>
							</tr>
							<tr>
								<td style="width: 5%; text-align: center;"></td>
								<td
									style="width: 10%; font-weight: bolder; color: black; text-align: center;">姓名(必填项)</td>
								<td
									style="width: 10%; font-weight: bolder; color: black; text-align: center;">性别(必填项)</td>
								<td
									style="width: 15%; font-weight: bolder; color: black; text-align: center;">关系(必填项)</td>
								<td
									style="width: 30%; font-weight: bolder; color: black; text-align: center;">工作单位(必填项)</td>
								<td
									style="width: 10%; font-weight: bolder; color: black; text-align: center;">职务(必填项)</td>
								<td
									style="width: 20%; font-weight: bolder; color: black; text-align: center;">联系电话(必填项)</td>
							</tr>
							<c:forEach var="ral" items="${f_rals }" varStatus="vs">
								<tr>
									<td style="text-align: center;"><input type="checkbox"
										name="f_resourceid" value="${ral.resourceid }"
										checked="checked" /></td>
									<td style="text-align: center;"><input type="text"
										name="f_name" value="${ral.name }" style="width: 90%" /></td>
									<td style="text-align: center;"><gh:select name="f_gender"
											dictionaryCode="CodeSex" value="${ral.gender }"
											style="width:60%" /></td>
									<td style="text-align: center;"><gh:select
											name="f_ralation" dictionaryCode="CodeFamilyRalation"
											value="${ral.ralation }" style="width:80%" /></td>
									<td style="text-align: center;"><input type="text"
										style="width: 90%" name="f_workPlace"
										value="${ral.workPlace }" /></td>
									<td style="text-align: center;"><input type="text"
										name="f_title" value="${ral.title }" style="width: 90%" /></td>
									<td style="text-align: center;"><input type="text"
										name="f_contact" value="${ral.contact }" /></td>
								</tr>
							</c:forEach>
							<c:if test="${addRowNum >0}">
								<c:forEach begin="1" end="${addRowNum }" varStatus="vs">
									<tr>
										<td style="text-align: center;"><input type="checkbox"
											name="f_resourceid" value="" checked="checked" /></td>
										<td style="text-align: center;"><input type="text"
											name="f_name" style="width: 90%" /></td>
										<td style="text-align: center;"><gh:select
												name="f_gender" dictionaryCode="CodeSex" style="width:60%" /></td>
										<td style="text-align: center;"><gh:select
												name="f_ralation" dictionaryCode="CodeFamilyRalation"
												style="width:80%" /></td>
										<td style="text-align: center;"><input type="text"
											name="f_workPlace" style="width: 90%" /></td>
										<td style="text-align: center;"><input type="text"
											name="f_title" style="width: 90%" /></td>
										<td style="text-align: center;"><input type="text"
											name="f_contact" /></td>
									</tr>
								</c:forEach>
							</c:if>
						</tbody>
					</table>
					<%--家庭主要成员--%>
					<%--主要社会关系--%>
					<table class="form" id="student_card_s_ralation">
						<c:set var="addRowNum" value="${5-fn:length(s_rals) }"></c:set>
						<tbody>
							<tr>
								<td
									style="width: 5%; text-align: center; font-weight: bolder; color: black; font-size: 13px;"
									<c:if test="${addRowNum >0}">rowspan="${addRowNum +6}"</c:if>
									<c:if test="${addRowNum == 0}">rowspan="6"</c:if>>主</br>要</br>社</br>会</br>关</br>系
								</td>
								<td
									style="text-align: left; font-weight: bolder; color: red; font-size: 13px;"
									colspan="5">备注：社会关系至少填写一项</td>
							</tr>
							<tr>
								<td style="width: 5%; text-align: center;"></td>
								<td
									style="width: 10%; font-weight: bolder; color: black; text-align: center;">姓名</td>
								<td
									style="width: 20%; font-weight: bolder; color: black; text-align: center;">关系</td>
								<td
									style="width: 40%; font-weight: bolder; color: black; text-align: center;">工作单位</td>
								<td
									style="width: 20%; font-weight: bolder; color: black; text-align: center;">备注</td>
							</tr>
							<c:forEach var="ral" items="${s_rals }" varStatus="vs">
								<tr>
									<td style="text-align: center;"><input type="checkbox"
										name="s_resourceid" value="${ral.resourceid }"
										checked="checked" /></td>
									<td style="text-align: center;"><input type="text"
										name="s_name" value="${ral.name }" style="width: 90%" /></td>
									<td style="text-align: center;"><gh:select
											name="s_ralation" dictionaryCode="CodeSocialRalation"
											value="${ral.ralation }" style="width:60%" /></td>
									<!-- filtrationStr="other,colleague,classmate,friend,partner" -->
									<td style="text-align: center;"><input type="text"
										name="s_workPlace" value="${ral.workPlace }"
										style="width: 90%" /></td>
									<td style="text-align: center;"><input type="text"
										name="s_contact" value="${ral.contact }" /></td>
								</tr>
							</c:forEach>
							<c:if test="${addRowNum >0}">
								<c:forEach begin="1" end="${addRowNum }" varStatus="vs">
									<tr>
										<td style="text-align: center;"><input type="checkbox"
											name="s_resourceid" value="" checked="checked" /></td>
										<td style="text-align: center;"><input type="text"
											name="s_name" style="width: 90%" /></td>
										<td style="text-align: center;"><gh:select
												name="s_ralation" dictionaryCode="CodeSocialRalation"
												style="width:60%" /></td>
										<td style="text-align: center;"><input type="text"
											name="s_workPlace" style="width: 90%" /></td>
										<td style="text-align: center;"><input type="text"
											name="s_contact" /></td>
									</tr>
								</c:forEach>
							</c:if>
						</tbody>
					</table>
					<%--主要社会关系--%>
					<c:if test="${schoolCode eq '11078' }">
						<table class="form">
							<tbody>
								<tr>
									<td
										style="width: 5%; text-align: center; font-weight: bolder; color: black; font-size: 13px;"
										rowspan="3">上&nbsp;传</br>证&nbsp;件
									</td>
									<td width="25%">请上传身份证扫描件：<br /> 1、中华人民共和国第二（一）代居民身份证；<br />
										2、大小：≤150k,格式 :jpg
									</td>
									<td width="20%">
										<ul>
											<li>注意：请使用采光均匀的图片或扫描件</li>
											<li>正面：</li>
											<c:if test="${studentInfo.rollCardStatus ne '2'}">
												<li>
													<div id="uploadify_images_certPhotoPath"
														style="width: 400px; height: 200px;"></div> <!-- <input id="uploadify_images_certPhotoPath" type="file" /> -->
												</li>
											</c:if>
										</ul>
										<ul>
											<li>反面：</li>
											<c:if test="${studentInfo.rollCardStatus ne '2'}">
												<li><div id="uploadify_images_certPhotoPath_reverse"
														style="width: 400px; height: 200px;"></div></li>
											</c:if>
										</ul>
									</td>
									<td><c:if
											test="${not empty studentInfo.studentBaseInfo.certPhotoPath }">
											<img id="student_certPhotoPath"
												src="${rootUrl}common/students/${studentInfo.studentBaseInfo.certPhotoPath}"
												width="240" height="160" />
										</c:if> <c:if
											test="${empty studentInfo.studentBaseInfo.certPhotoPath }">
											<img id="student_certPhotoPath"
												src="${baseUrl}/themes/default/images/certPhoto.png"
												width="240" height="160" />
										</c:if> <c:if
											test="${not empty studentInfo.studentBaseInfo.certPhotoPathReverse }">
											<img id="student_certPhotoPathReverse"
												src="${rootUrl}common/students/${studentInfo.studentBaseInfo.certPhotoPathReverse}"
												width="240" height="160" />
										</c:if> <c:if
											test="${empty studentInfo.studentBaseInfo.certPhotoPathReverse }">
											<img id="student_certPhotoPathReverse"
												src="${baseUrl}/themes/default/images/certPhotoReverse.png"
												width="240" height="160" />
										</c:if> <input type="hidden" id="certPhotoPathId"
										name="certPhotoPath"
										value="${studentInfo.studentBaseInfo.certPhotoPath }" /> <input
										type="hidden" id="certPhotoPathIdReverse"
										name="certPhotoPathReverse"
										value="${studentInfo.studentBaseInfo.certPhotoPathReverse }" />
									</td>
								</tr>
							</tbody>
						</table>
					</c:if>
					<%--在校期间奖罚情况--%>
					<!--<table class="form" id="student_card_reWardsPuniShment">
             <tr><td colspan="2"><span style="color: red">提示:如果没有受过奖励和处分，请输入“无”，不允许为空，不大于100字</span>		</td></tr>	
             <tr>
             	<td style="width:5%;text-align:center;font-weight: bolder;color: black;font-size: 13px;" rowspan="2"></br>在</br>学</br>期</br>间</br>奖</br>罚</br>情</br>况</td>
				<td style="width:95%;"><textarea style="width: 95%;height: 200px" name = "student_card_reWardsPuniShment" id ="student_card_reWardsPuniShment" <c:if test="${studentInfo.studentStatus eq '16' }"> readonly="readonly"</c:if>  >${studentInfo.reWardsPuniShment }</textarea></td>
			</tr>	
						
		</table>
		-->
					<%--变更--%>
					<!--<table class="form" id="student_card_change">
             <tr>
             	<td style="width:5%;text-align:center;font-weight: bolder;color: black;font-size: 13px;" rowspan="3">变</br>更</td>
				<td style="width:10%;">转专业</td>
				<td style="width:85%;">
				   <c:if test="${ not empty change_1}">	
					<fmt:formatDate pattern="yyyy-MM-dd" value="${change_1.auditDate }"/> 
					从 <font color="red"> ${change_1.changeBeforeTeachingGuidePlan.teachingPlan.major.majorName } </font>  转至
					  <font color="red"> ${change_1.studentInfo.major.majorName }</font>
					</c:if>
				</td>
			</tr>	
			<tr>
				<td >转学习中心</td>
				<td >
					<c:if test="${ not empty change_2}">	
					<fmt:formatDate pattern="yyyy-MM-dd" value="${change_2.auditDate }"/> 
					从  	<font color="red">${change_2.changeBeforeBrSchool.unitName }</font>转至
						<font color="red">${change_2.changeBrschool.unitName }</font>
					</c:if>	
				</td>
			</tr>	
			<tr>
				<td >其它</td>
				<td > 
					<c:if test="${ not empty change_3}">	
						类型：<font color="red">${ghfn:dictCode2Val('CodeStudentStatusChange',change_3.changeType)}</font> 
						审核时间：<fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${change_3.auditDate }"/>
					</c:if>	
				</td>
			</tr>			
		</table>
		-->
					<table class="form" id="student_card_change">
						<tr>
							<td
								style="width: 5%; text-align: center; font-weight: bolder; color: black; font-size: 13px;"
								rowspan="3">学&nbsp;异</br>籍&nbsp;动
							</td>
							<td style="width: 95%;" name="stuChangeInfo">${stuChangeInfo}</td>
						</tr>
					</table>
					<%--变更--%>
					<%--学历--%>
					<table class="form" id="student_card_educational">
						<tr>
							<td
								style="width: 5%; text-align: center; font-weight: bolder; color: black; font-size: 13px;"
								rowspan="2">学</br>历
							</td>
							<td style="width: 6%;">信息确认签名</td>
							<td style="width: 35%;"></td>
							<td style="width: 6%;">信息确认日期</td>
							<td style="width: 40%;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;年&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;月&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;日</td>
						</tr>
						<tr>
							<td style="width: 6%;">学籍情况</td>
							<td style="width: 35%;"></td>
							<td style="width: 6%;">毕业日期</td>
							<td style="width: 40%;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;年&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;月&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;日</td>
						</tr>
						<c:choose>
							<c:when test="${schoolCode eq '10560' || schoolCode eq ''|| schoolCode eq '10601' || schoolCode eq '12962'}">
								<tr>
									<td
										style="width: 5%; text-align: center; font-weight: bolder; color: black; font-size: 13px;"
										rowspan="2">自&nbsp;我</br>鉴&nbsp;定
									</td>
									<td
										style="width: 6%; text-align: left; font-weight: bolder; color: red; font-size: 13px;">备注：建议在300-400字之间，填写完成后请点击“毕业生登记表下载”进行查看</td>
									<td style="width: 98%;" name="selfAssessment" colspan="4">
										<textarea rows="8" style="width: 95%"
											id="stucard_selfAssessment" name="selfAssessment"
											required="required" placeholder=" 请在此处输入自我鉴定，首行请自行空两格">${studentInfo.selfAssessment}</textarea>
									</td>
								</tr>
							</c:when>
							<c:otherwise>
								<%-- <textarea rows="8" style="width: 95%" id="stucard_selfAssessment" name="selfAssessment" placeholder="请在此处输入自我鉴定，首行请自行空两格" >${studentInfo.selfAssessment}</textarea>  --%>
							</c:otherwise>
						</c:choose>

					</table>
				</div>
				<%--学历--%>
				<div class="formBar">
					<ul>
						<%-- <c:if test="${studentInfo.rollCardStatus eq 'Y' }">
				<li><div class="button"><div class="buttonContent"><button type="button" onclick="studentInfoCardPrint();">打印</button></div></div></li>
				<li><div class="button"><div class="buttonContent"><button type="button" onclick="studentInfoCardDownload();">下载pdf</button></div></div></li>
				</c:if>
				<c:if test="${studentInfo.rollCardStatus eq 'N' or studentInfo.rollCardStatus eq null  }">
				<li><div class="buttonActive"><div class="buttonContent"><button type="submit" >保存</button></div></div></li>
				<li><div class="buttonActive"><div class="buttonContent"><button type="button"  onclick="studentInfoCardSubmit()">提交</button></div></div></li>
				</c:if>--%>
						<c:choose>
							<c:when test="${studentInfo.rollCardStatus eq '2'}">
								<!-- 			            	<li><div class="button"><div class="buttonContent"><button type="button" onclick="studentInfoCardPrint();">打印</button></div></div></li> -->
								<li><div class="button">
										<div class="buttonContent">
											<button type="button" onclick="studentInfoCardDownload();">下载学籍卡PDF</button>
										</div>
									</div></li>
								<c:if test="${schoolCode eq '10601' || schoolCode eq '10560' || schoolCode eq '12962'}">
									<li><div class="button">
											<div class="buttonContent">
												<button type="button"
													onclick="studentRegistryFormDownload();">毕业生登记表下载</button>
											</div>
										</div></li>
								</c:if>
							</c:when>
							<c:otherwise>
								<li><div class="buttonActive">
										<div class="buttonContent">
											<button type="button" onclick="studentInfoCardSave()">保存</button>
										</div>
									</div></li>
								<c:if test="${schoolCode eq '10601' || schoolCode eq '10560' || schoolCode eq '12962'}">
									<li><div class="button">
											<div class="buttonContent">
												<button type="button"
													onclick="studentRegistryFormDownload();">毕业生登记表下载</button>
											</div>
										</div></li>
								</c:if>
								<li><div class="button">
										<div class="buttonContent">
											<button type="button" onclick="studentInfoCardDownload();">学籍卡下载</button>
										</div>
									</div></li>
								<li><div class="buttonActive">
										<div class="buttonContent">
											<button type="button" onclick="studentInfoCardSubmit()">提交</button>
										</div>
									</div></li>
							</c:otherwise>
						</c:choose>

						<li><div class="button">
								<div class="buttonContent">
									<button type="button" class="close"
										onclick="navTab.closeCurrentTab();">取消</button>
								</div>
							</div></li>
					</ul>
				</div>
			</form>
		</div>
	</div>
</body>
</html>