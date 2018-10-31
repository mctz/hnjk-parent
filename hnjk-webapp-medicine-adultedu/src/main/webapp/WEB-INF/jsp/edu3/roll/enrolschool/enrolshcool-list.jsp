<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>入学资格查询</title>
<script type="text/javascript">
	$(document).ready(function(){
		$("#enrolshcool_brSchoolName").flexselect({});
		enrolshcoolQueryBegin();
	});
	
	// 打开页面或者点击查询（即加载页面执行）
   function enrolshcoolQueryBegin() {
	   var defaultValue = "${condition['branchSchool']}";
	   var schoolId = "";
	   var brshSchool = "${condition['brshSchool']}";
	   if(brshSchool=='Y'){
		   schoolId = defaultValue;
	   }
	   var gradeId = "${condition['grade']}";;
	   var classicId = "${condition['classic']}";
	   var teachingType = "";
	 
	   var majorId = "${condition['major']}";
	
	   var selectIdsJson = "{unitId:'enrolshcool_brSchoolName',gradeId:'enrolshcool_gradeId',classicId:'enrolshcool_classicId',majorId:'enrolshcool_majorId'}";
	   cascadeQuery("begin", defaultValue, schoolId, gradeId, classicId, teachingType, majorId, "", selectIdsJson);
	   
   }
   
   // 选择教学点
   function enrolshcoolQueryUnit() {
	   var defaultValue = $("#enrolshcool_brSchoolName").val();
	   var selectIdsJson = "{gradeId:'enrolshcool_gradeId',classicId:'enrolshcool_classicId',majorId:'enrolshcool_majorId'}";
	   cascadeQuery("unit", defaultValue, "", "", "", "", "", "", selectIdsJson);
   }
	// 选择年级
   function enrolshcoolQueryGrade() {
	   var defaultValue = $("#enrolshcool_brSchoolName").val();
	   var gradeId = $("#enrolshcool_gradeId").val();
	   var selectIdsJson = "{classicId:'enrolshcool_classicId',majorId:'enrolshcool_majorId'}";
	   cascadeQuery("grade", defaultValue, "", gradeId, "", "", "", "", selectIdsJson);
   }
   
	// 选择层次
   function enrolshcoolQueryClassic() {
	   var defaultValue = $("#enrolshcool_brSchoolName").val();
	   var gradeId = $("#enrolshcool_gradeId").val();
	   var classicId = $("#enrolshcool_classicId").val();
	   var selectIdsJson = "{majorId:'enrolshcool_majorId'}";
	   cascadeQuery("classic", defaultValue, "", gradeId, classicId, "", "", "", selectIdsJson); 
   }
// 根据年级同步联动信息（招生专业）
   function syncLinkageQuery() {
   	var selectGradeUrl = "${baseUrl}/edu3/teaching/linkageQuery/selectGrade.html";
	   $.pdialog.open(selectGradeUrl, 'RES_TEACHING_LINKAGEQUERY_SELECTGRADE', '选择年级', {mask:true,width: 300, height: 200});
   }
   function FindReader_F200(){
	   var str;
	   str = SynCardOcx1.FindReader();
	   if (str > 0){
		   if(str>1000){		   			
			    alertMsg.correct("读卡器通过USB接口连接在系统上，已可进行读卡操作 ");
			    return true;
		   }
		   else{
		   		alertMsg.correct("读卡器连接在COM ");
		   		return true;
		   }
	   }else{	   		
	   		alertMsg.error("没有找到读卡器");
	   		return false;
	   }
   }
   function FindReader_DKQ(){
	   var str;
	   str = IdrOcx1.GetState();
// 	   console.info(str);
	   if (str == 0){
		   alertMsg.correct("读卡器已连接在系统上，已可进行读卡操作 ");
		   return true;		   
	   }
	   if(str<0){
		   if(str==-4){
			   alertMsg.error("未检测到设备,请先连接设备后，再尝试启用该功能");
			   return false;
		   }else {
			   alertMsg.error("未检测到设备");			   
			   return false;
		   }		   
	   }
   } 
   function ReadCard_openDialog(devicestype){//第一种设备 SynCardOcx1 F200  第二种设备 DKQ-A16D 
	   var url = "${baseUrl}/edu3/register/studentinfo/readCertNoRegister.html?devicestype="+devicestype+"&recruitPlanID=${recruitPlanID}";
	   $.pdialog.open(url,"RES_SCHOOL_REGISTER_ENROLL_READCERTNOREGISTER","读取身份证进行注册",{width:720, height:615,mask:true});
   }  
//    function showStuInfo(){
// 	   alertMsg.confirm("读卡成功<br>"+	
// 	   	   		"姓名:"+SynCardOcx1.NameA +"<br>"+
// 	   	   		"性别:"+SynCardOcx1.Sex +"<br>"+
// 	   	   		"民族:"+SynCardOcx1.Nation +"<br>"+
// 	   	   		"出生日期:"+SynCardOcx1.Born +"<br>"+
// 	   	   		"地址:"+SynCardOcx1.Address +"<br>"+
// 	   	   		"身份证号:"+SynCardOcx1.CardNo +"<br>"+
// 	   	   		"有效期开始:"+SynCardOcx1.UserLifeB +"<br>"+
// 	   	   		"有效期结束:"+SynCardOcx1.UserLifeE +"<br>"+
// 	   	   		"发证机关:"+SynCardOcx1.Police +"<br>"+
// 	   	   		"照片文件名:"+SynCardOcx1.PhotoName +"<br>");
//    }
//    function showStuInfo4NewDevices(){
// 	   alertMsg.correct("读卡成功<br>"+	
// 	   	   		"姓名:"+IdrOcx1.NameL +"<br>"+
// 	   	   		"性别:"+IdrOcx1.SexL +"<br>"+
// 	   	   		"民族:"+IdrOcx1.NationL +"<br>"+
// 	   	   		"出生日期:"+IdrOcx1.BornL +"<br>"+
// 	   	   		"地址:"+IdrOcx1.Address +"<br>"+
// 	   	   		"身份证号:"+IdrOcx1.CardNo +"<br>"+
// 	   	   		"有效期开始:"+IdrOcx1.ActivityLFrom +"<br>"+
// 	   	   		"有效期结束:"+IdrOcx1.UserLifeE +"<br>"+
// 	   	   		"发证机关:"+IdrOcx1.Police +"<br>"+
// 	   	   		"照片文件名:"+IdrOcx1.PhotoPath +"<br>");
//    }

	/**
	由于开发的第一种设备 新中新 F200 的SDK开发包中，没有循环读卡的功能，因此需要在JS方法中写循环调用读卡的方法
	*/
   function startPersonalIDReader_F200(){
	   var isInTime=isRecruitTimeInRange();
	   if(isInTime=='N'){
		   return false;
	   }
	   var isBrschool="";
	   readCertNum=0;
	   isBrschool="${condition['brshSchool']}";
	   if(isBrschool!='Y'){
		   alertMsg.warn("该功能只允许教学点使用");
		   return false;
	   }
	   if(!(window.ActiveXObject || "ActiveXObject" in window)){   
		   alertMsg.warn("该功能只能在IE浏览器使用！");
   		   return false; 
	   }
// 	   var userAgent = navigator.userAgent; //取得浏览器的userAgent字符串
// 	   var isIE = userAgent.indexOf("MSIE") > -1; //判断是否IE浏览器
// 	   var browser=navigator.appName;
// 	   if(!isIE){
// 		   alertMsg.warn("该功能无法使用其他浏览器，请使用 IE8.0 以上版本的浏览器");
// 		   return false;
// 	   }else{
// 		    var reIE = new RegExp("MSIE (\\d+\\.\\d+);");
// 	        reIE.test(userAgent);
// 	        var fIEVersion = parseFloat(RegExp["$1"]);
// 	        if(fIEVersion<8.0) {
// 	        	alertMsg.warn("该功能无法使用其他浏览器，请使用 IE8.0 以上版本的浏览器");
// 	        	return false;
// 	        }else{//浏览器符合要求
	        	var flag= FindReader_F200();
	      	  	if(flag){//连接成功
	      	  		ReadCard_openDialog("F200");
	      	 	}else{//连接不成功
	      	 		//上一步已经给出了提示语，因此这里不需要处理
	      	 	}
// 	        }
// 	   }
	  
   }
   function isRecruitTimeInRange(){
	   var isTimeInRange = "${isTimeInRange}";
	   var startTime="${startTime}";
	   var endTime="${endTime}";
	   var planName = "${planName}";
	   if(isTimeInRange!='Y'){
		   alertMsg.warn("当前时间不在报到时间范围内：<br>"+planName+"<br> 报到开始时间："+startTime+"<br> 报到结束时间："+endTime);
		   return 'N';
	   }else{
		   return 'Y';
	   }
   }
   function startPersonalIDReader_DKQ(){
	   var isInTime=isRecruitTimeInRange();
	    if(isInTime=='N'){
		    return false;
	    }else{
		   var isBrschool="";
		   readCertNum=0;
		   isBrschool="${condition['brshSchool']}";
		   if(isBrschool!='Y'){
			   alertMsg.warn("该功能只允许教学点使用");
			   return false;
		   }
		   if(!(window.ActiveXObject || "ActiveXObject" in window)){   
			   alertMsg.warn("该功能只能在IE浏览器使用！");
	   		   return false; 
		   }
	// 	   var userAgent = navigator.userAgent; //取得浏览器的userAgent字符串
	// 	   var isIE = userAgent.indexOf("MSIE") > -1; //判断是否IE浏览器
	// 	   var browser=navigator.appName;
	// 	   if(!isIE){
	// 		   alertMsg.warn("该功能无法使用其他浏览器，请使用 IE8.0 以上版本的浏览器");
	// 		   return false;
	// 	   }else{
	// 		    var reIE = new RegExp("MSIE (\\d+\\.\\d+);");
	// 	        reIE.test(userAgent);
	// 	        var fIEVersion = parseFloat(RegExp["$1"]);
	// 	        if(fIEVersion<8.0) {
	// 	        	alertMsg.warn("该功能无法使用其他浏览器，请使用 IE8.0 以上版本的浏览器");
	// 	        	return false;
	// 	        }else{//浏览器符合要求
		        	var flag= FindReader_DKQ();
		      	  	if(flag){//连接成功
		      	  		ReadCard_openDialog("DKQ-A16D");
		      	 	}else{//连接不成功
		      	 		
		      	 	}
	// 	        }
	// 	   }
	    }
   }
	</script>
</head>
<body>

	<div class="page">
		<div class="pageHeader">
			<object classid="clsid:46E4B248-8A41-45C5-B896-738ED44C1587"
				id="SynCardOcx1"
				codeBase="${baseUrl}/ocx/SynCardOcx.CAB#version=1,0,0,1" width="0"
				height="0"> </object>
			<object Name="GT2ICROCX" id="IdrOcx1" width="0" height="0"
				CLASSID="CLSID:220C3AD1-5E9D-4B06-870F-E34662E2DFEA"
				CODEBASE="${baseUrl}/ocx/IdrOcx.cab#version=1,0,1,3"> </object>
			<form onsubmit="return navTabSearch(this);"
				action="${baseUrl }/edu3/register/studentinfo/enrolshcool-list.html"
				method="post">
				<div class="searchBar">
					<ul class="searchContent">
						<%-- <c:if test="${condition['brshSchool'] ne 'Y'}"> --%>
							<li class="custom-li"><label>教学站：</label> <span
								sel-id="enrolshcool_brSchoolName" sel-name="branchSchool"
								sel-onchange="enrolshcoolQueryUnit()" sel-classs="flexselect"></span></li>
						<%-- </c:if> --%>

						<li><label>年级：</label> <span sel-id="enrolshcool_gradeId"
							sel-name="grade" sel-onchange="enrolshcoolQueryGrade()"
							sel-style="width: 120px"></span></li>

						<li><label>层次：</label> <span sel-id="enrolshcool_classicId"
							sel-name="classic" sel-onchange="enrolshcoolQueryClassic()"
							sel-style="width: 120px"></span></li>

					</ul>
					<ul class="searchContent">
						<li class="custom-li"><label>专业:</label> <span sel-id="enrolshcool_majorId"
							sel-name="major" sel-classs="flexselect" ></span>
						</li>
						<li><label>入学前最高学历层次：</label>
						<gh:select name="highClassic"
								dictionaryCode="CodeEducationalLevel"
								value="${condition['highClassic']}" validate="Require"
								mes="入学前国民教育最高学历层次" style="width:120px" /></li>
						<li><label>姓名：</label><input type="text" name="stuName"
							value="${condition['stuName']}" style="width: 115px" /></li>
						<li><label>学号：</label><input type="text"
							name="matriculateNoticeNo"
							value="${condition['matriculateNoticeNo']}" style="width: 115px" />
						</li>
					</ul>
					<ul class="searchContent">
						<li><label>是否跟读:</label> <gh:select name="isStudyFollow"
								id="isStudyFollow" dictionaryCode="yesOrNo"
								value="${condition['isStudyFollow'] }" style="width:125px" /></li>
						<li><label>是否已分配:</label> <gh:select name="isDistribution"
								id="isDistribution" dictionaryCode="yesOrNo"
								value="${condition['isDistribution'] }" style="width:125px" /></li>
					</ul>
					<div class="buttonActive" style="float: right">
						<div class="buttonContent">
							<button type="submit">查 询</button>
						</div>
					</div>
				</div>
			</form>
		</div>
		<div class="pageContent">
			<gh:resAuth parentCode="RES_SCHOOL_REGISTER_ENROLL" pageType="list"></gh:resAuth>
			<table class="table" layouth="198">
				<thead>
					<tr class="head_bg">
						<th width="3%"><input type="checkbox" name="checkall"
							id="check_all_enroll"
							onclick="checkboxAll('#check_all_enroll','resourceid','#enroll_body')" /></th>
						<th width="12%">教学站</th>
						<th width="4%">年级</th>
						<th width="4%">姓名</th>
						<th width="8%">身份证号</th>
						<th width="8%">考生号</th>
						<th width="8%">学号</th>
						<th width="4%">报名层次</th>
						<th width="8%">报名专业</th>
						<th width="10%">入学前国民教育最高学历层次</th>
						<th width="6%">入学前学校名称</th>
						<th width="6%">入学前学历毕业年份</th>
						<th width="8%">未报到原因</th>
						<th width="3%">是否跟读</th>
						<th width="4%">入学前学历证书编号</th>
						<th width="4%">备注</th>

					</tr>
				</thead>
				<tbody id="enroll_body">
					<c:forEach items="${eilist.result}" var="e" varStatus="vs">
						<tr>
							<td><input type="checkbox" name="resourceid"
								value="${e.resourceid }" autocomplete="off"
								unitName="${e.branchSchool.unitName }" /></td>
							<td>${e.branchSchool.unitName }</td>
							<td>${e.recruitMajor.recruitPlan.grade }</td>
							<td><a href="#" onclick="viewStuInfo('${e.resourceid}')">${e.studentBaseInfo.name }</a></td>
							<td>${e.studentBaseInfo.certNum }</td>
							<td><c:if
									test="${ghfn:getSysConfigurationValue('exameeInfo.uniqueId','server') eq '0' }">${e.enrolleeCode }</c:if>
								<c:if
									test="${ghfn:getSysConfigurationValue('exameeInfo.uniqueId','server') eq '1' }">${e.examCertificateNo }</c:if>

							</td>
							<td>${e.matriculateNoticeNo }</td>
							<td>${e.recruitMajor.classic }</td>

							<td>${e.recruitMajor.recruitMajorName }</td>
							<td>${ghfn:dictCode2Val('CodeEducationalLevel',e.educationalLevel) }</td>
							<td>${e.graduateSchool }</td>
							<td>${e.graduateDate }</td>
							<td>${e.noReportReason }</td>
							<td>${ghfn:dictCode2Val('yesOrNo',e.isStudyFollow) }</td>
							<td>${e.graduateId }</td>
							<td>${e.memo }</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<%-- <gh:page page="${eilist}"
				goPageUrl="${baseUrl }/edu3/register/studentinfo/enrolshcool-list.html"
				pageType="sys" condition="${condition }" /> --%>
		</div>
		<div  class="pageContent" style="position: absolute;bottom: 0px;width: 100%">
			<gh:page page="${eilist}"
				goPageUrl="${baseUrl }/edu3/register/studentinfo/enrolshcool-list.html"
				pageType="sys" condition="${condition}" /></div>
	</div>
	<script type="text/javascript">
	function registerStu(){
		var isInTime=isRecruitTimeInRange();
	    if(isInTime=='N'){
		    return false;
	    }else{
			var reg = true;
			$("input[name='resourceid']").each(function(){
				if ($(this).attr("checked") && $(this).attr("unitName") == "未分配") {
					alertMsg.warn("所选学生未分配教学站，不允许注册！");
					reg = false;
					return false;
				}
			});
				/*  pageBarHandle("您确定要注册这些学生吗？","${baseUrl}/edu3/register/studentinfo/registering.html","#enroll_body");  */
			if (reg) {
				var num;
				var msg;
				var isAll=false;
				if(!isChecked('resourceid',"#enroll_body")){
				//筛选出来的全部信息
					num="${eilist.totalCount}"; 
					if(num<=0){
						alertMsg.warn("未检测到学生信息");
						return false;
					}
				
				msg="您确定要注册根据以上查询条件导出的<font color='red'>"+num+"</font>名学生吗？";
				isAll=true;
	 			}else{
	 				num=$("#enroll_body input[name='resourceid']:checked").size();
					msg="您确定要注册所选的<font color='red'>"+num+"</font>名学生吗？";
	 			}
				alertMsg.confirm(msg, {
				okCall: function(){//执行			
					var res = "";
					var k = 0;
					/* var num  = $(bodyname+" input[name='resourceid']:checked").size(); */
					
					if(isAll){
						var branchSchool =  "${condition['branchSchool']}";
						var classic = "${condition['classic']}";
						var grade = "${condition['grade']}";
						var major = "${condition['major']}";
						var highClassic = "${condition['highClassic']}";
						var stuName = "${condition['stuName']}";
						var matriculateNoticeNo = "${condition['matriculateNoticeNo']}";
						var isStudyFollow = $("#isStudyFollow").val();
						var params = "?branchSchool="+branchSchool+"&classic="+classic+"&grade="+grade+"&highClassic="+highClassic+"&stuName="+stuName+"&matriculateNoticeNo="+matriculateNoticeNo+"&major="+major+"&isStudyFollow="+isStudyFollow+"&numTotal="+num;;
						//var requestString="?branchSchool="+$("#enrolshcool_brSchoolName").val()+"&classic="+$("#enrolshcool_classicId").val()+"&grade="+$("#enrolshcool_gradeId").val()+"&major="+$("#enrolshcool_majorId").val()+"&highClassic="+$("#highClassic").val()+"&stuName="+$("#studentNameId").val()+"&matriculateNoticeNo="+$("#matriculateNoticeNoId").val()+"&isStudyFollow="+$("#isStudyFollow").val()+"&isDistribution="+$("#isDistribution").val()+"&numTotal="+num;
						$.post("${baseUrl}/edu3/register/studentinfo/registeringAll.html"+params,navTabAjaxDone, "json");
					}else{
						$("#enroll_body input[name='resourceid']:checked").each(function(){
	                        res+=$(this).val();
	                        if(k != num -1 ) res += ",";
	                        k++;
	                    })
					$.post("${baseUrl}/edu3/register/studentinfo/registering.html",{resourceid:res}, navTabAjaxDone, "json");
	              	}
					}
				});			
			}
	    }
	}
	
	function aotuRegisterStu(){//自动注册
		var isInTime=isRecruitTimeInRange();
	    if(isInTime=='N'){
		    return false;
	    }else{
		//alertMsg.warn("该功能暂时未开放！");
			alertMsg.confirm("在使用自动注册，请注意：<br/><font color='red'>1)自动注册只会给新生注册账号，请确定已同步新生缴费记录;<br/>2)如果新学期的教学计划没有制定，则新生首学期的预约可能失败.<br/></font>确定要自动注册码？", {
	   			okCall: function(){
	   				$.ajax({
	   				   type: "GET",
	   				   url: "${baseUrl }/edu3/register/studentinfo/autoregister.html?act=count",	
	   				   dataType: "json",	    
	   				   success: function(data){	 
	   				   	if(data.statusCode == 200){	
	   				   		if(data.registerNum  > 0){
	   				   			alertMsg.confirm("符合自动注册条件的人数为：<b><font color='red'>"+data.registerNum+"</font></b> 人.<br/>确实要给这批人注册学籍吗?", {
	   					   			okCall: function(){
	   					   				_autoRegister();
	   					   			}
	   					   		});
	   				   		}else{
	   				   			alertMsg.warn("没有符合注册条件的学生！");
	   				   			return false;
	   				   		}
	   				   		
	   				   	}else{
	   				   		alertMsg.error(data.message);
	   				   		return false;
	   				   	}
	   				   }
	   				 });
	   			}
	   		});
	    }
	}
	
	function _autoRegister(){//自动注册回调
		$.ajax({
			   type: "GET",
			   url: "${baseUrl }/edu3/register/studentinfo/autoregister.html?act=register",	
			   dataType: "json",	    
			   success: function(data){	 
			   	if(data.statusCode == 200){	
			   		alertMsg.correct(data.message);			   		
			   	}else{
			   		alertMsg.error(data.message);
			   		return false;
			   	}
			   }
		 });
	}
	
	//查看
	function viewStuInfo(id){
		var url = "${baseUrl}/edu3/register/studentinfo/edit.html";		
		$.pdialog.open(url+'?resourceid='+id, 'RES_SCHOOL_REGISTER_ENROLL_VIEW', '查看详细', {width: 800, height: 600});
	}
	
	//导出未注册
	function exportUnRegList(){
		var branchSchool =  "${condition['branchSchool']}";
		var classic = "${condition['classic']}";
		var grade = "${condition['grade']}";
		var major = "${condition['major']}";
		var highClassic = "${condition['highClassic']}";
		var stuName = "${condition['stuName']}";
		var matriculateNoticeNo = "${condition['matriculateNoticeNo']}";
		var isStudyFollow = $("#isStudyFollow").val();
		var params = "?branchSchool="+branchSchool+"&classic="+classic+"&grade="+grade+"&highClassic="+highClassic+"&stuName="+stuName+"&matriculateNoticeNo="+matriculateNoticeNo+"&major="+major+"&isStudyFollow="+isStudyFollow;
		var url = "${baseUrl }/edu3/register/studentinfo/downloadUnRegList.html"+params;
		alertMsg.confirm("您确定按查询条件导出未注册信息吗？", {
			okCall:function(){
				downloadFileByIframe(url,'RES_SCHOOL_REGISTER_ENROLL_EXPORTUNREG');
			}
		})
	}
	//导入批量注册
	function importToReg(){
		var tmp = isRecruitTimeInRange();
		if(tmp=='N'){			
			return false;
		}else{
			var url = "${baseUrl}/edu3/register/studentinfo/uploadToReg.html";
			$.pdialog.open(url,"RES_SCHOOL_REGISTER_ENROLL_IMPORTUNREG","导入批量注册",{width:480, height:320});		
		}	
	}
	
	// 编辑
	function editEnrolleeInfo() {
		if(isCheckOnlyone('resourceid','#enroll_body')){
			$("#enroll_body input[name='resourceid']:checked").each(function(){
				var enrolleeId = $(this).val();
				var url = "${baseUrl}/edu3/recruit/enroll/edit.html";
				$.pdialog.open(url+'?resourceid='+enrolleeId, 'RES_SCHOOL_REGISTER_ENROLL_EDIT', '编辑信息', {width: 800, height: 600});
			});
		}
	}
	
</script>
</body>
</html>