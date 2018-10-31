<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>审核毕业资格列表</title>
<style type="text/css">
	td,th{text-align: center;}
</style>
<script type="text/javascript">
	$(document).ready(function(){
		$(".scoreDetails").hide();
		$("#hidedetail").hide();
		if($("#toPage").val()!=1){
			//第一次打开页面
			$("#limScoreCondition").attr("checked","checked");
			$("#nesScoreCondition").attr("checked","checked");
			$("#totalScoreCondition").attr("checked","checked");
			$("#allPassCondition").attr("checked","checked");
			$("#stuFeeCondition").attr("checked","checked");
			$("#scoreCondition").attr("checked","checked");
			$("#applyCondition").attr("checked","checked");
			$("#enterSchCondition").attr("checked","checked");
			$("#graduation").attr("checked","checked");
			//$("#stuStatusCondition").attr("checked","checked");
			$("#yearCondition").attr("checked","checked");
			$("#useGraduateDate").attr("checked","checked");
            $("#practiceCourseCondition").attr("checked","checked");
            $("#practiceMaterialsCondition").attr("checked","checked");
            $("#studentPhotoCondition").attr("checked","checked");
            $("#juniorCheckCondition").attr("checked","checked");
		}else{
			if($("#lim").val()=='on'){$("#limScoreCondition").attr("checked","checked");}else{$(".scoreDetails").show();$("#showdetail").hide();}
			if($("#nes").val()=='on'){$("#nesScoreCondition").attr("checked","checked");}else{$(".scoreDetails").show();$("#showdetail").hide();}
			if($("#tot").val()=='on'){$("#totalScoreCondition").attr("checked","checked");}else{$(".scoreDetails").show();$("#showdetail").hide();}
			if($("#sco").val()=='on'){$("#scoreCondition").attr("checked","checked");}
			if($("#app").val()=='on'){$("#applyCondition").attr("checked","checked");}
			if($("#ent").val()=='on'){$("#enterSchCondition").attr("checked","checked");}
			//if($("#stu").val()=='on'){$("#stuStatusCondition").attr("checked","checked");}
		    if($("#yea").val()=='on'){$("#yearCondition").attr("checked","checked");}
		    if($("#all").val()=='on'){$("#allPassCondition").attr("checked","checked");}
		    if($("#fee").val()=='on'){$("#stuFeeCondition").attr("checked","checked");}
		    if($("#use").val()=='on'){$("#useGraduateDate").attr("checked","checked");}else{
		    	var useGraDateInFront = $("#useGraduateDate").attr("checked");
		    	if(!useGraDateInFront){
		    		$("#graduateDate").hide();
		    		$("#graduateDateEx").hide();
		    	}
		    }
            if($("#course").val()=='on'){$("#practiceCourseCondition").attr("checked","checked");}
            if($("#mate").val()=='on'){$("#practiceMaterialsCondition").attr("checked","checked");}
            if($("#photo").val()=='on'){$("#studentPhotoCondition").attr("checked","checked");}
            if($("#jun").val()=='on'){$("#juniorCheckCondition").attr("checked","checked");}
		}
		var studentStatusSet= '${stuStatusSet}';
		var statusRes= '${stuStatusRes}';
		orgStuStatus("#graduateAud #stuStatus",studentStatusSet,statusRes,"a11,b11,24,25,27");//将21修改为24 ，显示结业学生，去掉延期
		$("select[class*=flexselect]").flexselect();
		graduateAuditQueryBegin();
	});
	
	//打开页面或者点击查询（即加载页面执行）
	function graduateAuditQueryBegin() {
		var defaultValue = "${condition['branchSchool']}";
		var schoolId = "${brSchoolName}";
		var gradeId = "${condition['gradeid']}";
		var classicId = "${condition['classic']}";
		var teachingType = "${condition['schoolType']}";
		var majorId = "${condition['major']}";
		var classesId = "${condition['classesid']}";
		var selectIdsJson = "{unitId:'graduateauditbranchSchool',gradeId:'gradeid',classicId:'classic',teachingType:'teachingType',majorId:'major',classesId:'graduateaudit_classid'}";
		cascadeQuery("begin", defaultValue, schoolId, gradeId, classicId,teachingType, majorId, classesId, selectIdsJson);
	}

	// 选择教学点
	function graduateAuditQueryUnit() {
		var defaultValue = $("#graduateauditbranchSchool").val();
		var selectIdsJson = "{gradeId:'gradeid',classicId:'classic',teachingType:'teachingType',majorId:'major',classesId:'graduateaudit_classid'}";
		cascadeQuery("unit", defaultValue, "", "", "", "", "", "",selectIdsJson);
	}

	// 选择年级
	function graduateAuditQueryGrade() {
		var defaultValue = $("#graduateauditbranchSchool").val();
		var gradeId = $("#gradeid").val();
		var selectIdsJson = "{classicId:'classic',teachingType:'teachingType',majorId:'major',classesId:'graduateaudit_classid'}";
		cascadeQuery("grade", defaultValue, "", gradeId, "", "", "", "",selectIdsJson);
	}

	// 选择层次
	function graduateAuditQueryClassic() {
		var defaultValue = $("#graduateauditbranchSchool").val();
		var gradeId = $("#gradeid").val();
		var classicId = $("#classic").val();
		var selectIdsJson = "{teachingType:'teachingType',majorId:'major',classesId:'graduateaudit_classid'}";
		cascadeQuery("classic", defaultValue, "", gradeId, classicId, "", "", "", selectIdsJson);
	}

	// 选择学习形式
	function graduateAuditQueryTeachingType() {
		var defaultValue = $("#graduateauditbranchSchool").val();
		var gradeId = $("#gradeid").val();
		var classicId = $("#classic").val();
		var teachingTypeId = $("#teachingType").val();
		var selectIdsJson = "{majorId:'major',classesId:'graduateaudit_classid'}";
		cascadeQuery("teachingType", defaultValue, "", gradeId, classicId,teachingTypeId, "", "", selectIdsJson);
	}

	// 选择专业
	function graduateAuditQueryMajor() {
		var defaultValue = $("#graduateauditbranchSchool").val();
		var gradeId = $("#gradeid").val();
		var classicId = $("#classic").val();
		var teachingTypeId = $("#teachingType").val();
		var majorId = $("#major").val();
		var selectIdsJson = "{classesId:'graduateaudit_classid'}";
		cascadeQuery("major", defaultValue, "", gradeId, classicId,teachingTypeId, majorId, "", selectIdsJson);
	}
	
	 //组合学籍状态的方法(参数为:空白的select控件,原始的组合学籍状态集合,上次查询选择的值,过滤得到的学籍状态)
	function orgStuStatus(selectid,studentStatusSet,statusRes,val){
		var html = "<option value=''>请选择</option>";	
		var status= studentStatusSet.split(",");
		var filter = val.split(",");
		for(var i=0;i<(status.length-1)/2;i++){
			for(var j=0;j<filter.length;j++){
				if(filter[j]==status[2*i]){
					if(statusRes==status[2*i]){
						html += "<option selected='selected' value='"+status[2*i]+"'>"+status[2*i+1]+"</option>";
					}else{
						html += "<option value='"+status[2*i]+"'>"+status[2*i+1]+"</option>";
					}
				}
			}
		}
		$(selectid).html(html);
	} 
	function show(){
		$(".scoreDetails").fadeIn(1000);
		$("#showdetail").fadeOut(1000);
		$("#hidedetail").fadeIn(1000);
	}
	function hide(){
		$(".scoreDetails").fadeOut(1000);
		$("#showdetail").fadeIn(1000);
		$("#hidedetail").fadeOut(1000);
	}
	function checkAllScoreCon(){
		$("#totalScoreCondition").attr("checked",$("#scoreCondition").attr("checked"));
		$("#nesScoreCondition").attr("checked",$("#scoreCondition").attr("checked"));
		$("#limScoreCondition").attr("checked",$("#scoreCondition").attr("checked"));
	}
	function checkAllSwitch(){
		var switch1 = $("#totalScoreCondition").attr("checked");
		var switch2 = $("#nesScoreCondition").attr("checked");
		var switch3 = $("#limScoreCondition").attr("checked");
		if(switch1==true&&switch2==true&&switch3==true){
			$("#scoreCondition").attr("checked",true);
		}else{
			$("#scoreCondition").attr("checked",false);
		}
	}
	function changeGraduateDateEditable(){
		var useGraDateInFront = $("#useGraduateDate").attr("checked");
		if(!useGraDateInFront){
			$("#graduateDate").attr("value","");
			$("#graduateDate").fadeOut("100");
			$("#graduateDateEx").fadeOut("100");
			
		}else{
			$("#graduateDate").fadeIn("100");
			$("#graduateDateEx").fadeIn("100");
		}
	}
	function changeTPlan(){
		
	}
	/*
	function batchSetGraduateDate(val){
		//var url="${baseUrl}/edu3/schoolroll/graduate/audit/setGraduateDate.html?stus="+val;
		//$.pdialog.open(url,'RES_SCHOOL_GRADUATION_SETGRADUATEDATE','批量设置毕业时间',{height:100, width:150,mask:true});
		//改变了 一下输入方式。因为审核毕业的时候，需要设置统一的毕业时间，批量设置毕业时间，所以都统一在一个地方设置即可
		var graduateDate = $('#graduateDate').val();
		if(""==graduateDate){
			alert("您批量设置的毕业时间为空，批量设置失败。");
			return false;
		}
		var url= "${baseUrl }/edu3/schoolroll/graduate/audit/batchSetGraduateDate.html?stus="+val+"&graduateDate="+graduateDate;
		$.ajax({
				type:"post",
				url:url,
				dataType:"json",
				success:function(data){
					if(data['isUpdate']==true){
						alert("批量设置毕业时间成功。您可以在毕业生库中查到修改结果。");
						var branchSchool=$('#graduateAud #graduateauditbranchSchool').val()==undefined?"":$('#graduateAud #graduateauditbranchSchool').val();
						var major		=$('#graduateAud #major').val()==undefined?"":$('#graduateAud #major').val();
						var classic		=$('#graduateAud #classic').val()==undefined?"":$('#graduateAud #classic').val();
						var stuStatus	=$('#graduateAud #stuStatus').val()==undefined?"":$('#graduateAud #stuStatus').val();
						var name		=$('#graduateAud #name').val()==undefined?"":$('#graduateAud #name').val();
						var matriculateNoticeNo	=$('#graduateAud #matriculateNoticeNo').val()==undefined?"":$('#graduateAud #matriculateNoticeNo').val();
						var grade 		=$('#graduateAud #gradeid').val()==undefined?"":$('#graduateAud #gradeid').val();
						var url = "${baseUrl}/edu3/schoolroll/graduate/audit/list.html?branchSchool="+branchSchool+"&major="+major+"&classic="+classic+"&stuStatus="+stuStatus+"&name="+name+"&matriculateNoticeNo="+matriculateNoticeNo+"&grade="+grade;
						navTab.openTab('RES_SCHOOL_GRADUATION_AUDIT', url, '毕业资格审核');
					}else{ 
						alert("批量设置毕业时间失败。");
					}
				}
		});
	}
	*/
	//验证所选择的学生是不是均属于毕业状态。
	function validateSelectedId(){
		var stus="";
		$("input[name='studentInforesourceid']").each(function(){
			if($(this).attr("checked")){
				stus += ""+$(this).val()+",";
			}
		});
		if(""==stus){
			alertMsg.warn("请选择一条或多条记录！");return false
		}
		var url= "${baseUrl}/edu3/schoolroll/graduate/audit/validateStus.html?stus="+stus+"&studyStatus=graduate";
		$.ajax({
			type:"post",
			url:url,
			dataType:"json",
			success:function(data){
				if(data['isLegal']==false){
					alert("选择设置毕业时间的学生中有处于未毕业学籍状态的学生。");
					return false;
				}else{
					batchSetGraduateDate(stus);
				}
			}
	});
	}
	//自定义导出信息
	function exportGraduateStuToExcel(){
		var url = "${baseUrl}/edu3/schoolroll/graduate/excel/exportCustomExcelCondition.html";
		$.pdialog.open(url,'RES_SCHOOL_GRADUATESTU_CUSTOMEXPORT','自定义导出信息',{width:800,height:550});
		
	}

</script>
</head>
<body>

	<div class="page">
		<div class="pageHeader" style="height: 100px;">
			<form id="graduateAuditList" onsubmit="return navTabSearch(this);"
				action="${baseUrl }/edu3/schoolroll/graduate/audit/list.html"
				method="post">
				<input type="hidden" name="fromPage" value="1" /> <input
					type="hidden" id="toPage" value="${condition['fromPage']}" /> <input
					type="hidden" id="lim" value="${condition['limScoreCondition']}" />
				<input type="hidden" id="nes"
					value="${condition['nesScoreCondition']}" /> <input type="hidden"
					id="tot" value="${condition['totalScoreCondition']}" /> <input
					type="hidden" id="all" value="${condition['allPassCondition']}" />
				<input type="hidden" id="fee"
					value="${condition['stuFeeCondition']}" /> <input type="hidden"
					id="app" value="${condition['applyCondition']}" /> <input
					type="hidden" id="ent" value="${condition['enterSchCondition']}" />
				<!-- <input type="hidden" id="stu" value="${condition['stuStatusCondition']}" />-->
				<input type="hidden" id="yea" value="${condition['yearCondition']}" />
				<input type="hidden" id="sco" value="${condition['scoreCondition']}" />
				<input type="hidden" id="use" value="${condition['useGraduateDate']}" />
                <input type="hidden" id="course" value="${condition['practiceCourseCondition']}" />
                <input type="hidden" id="mate" value="${condition['practiceMaterialsCondition']}" />
                <input type="hidden" id="photo" value="${condition['studentPhotoCondition']}" />
                <input type="hidden" id="jun" value="${condition['juniorCheckCondition']}" />
				<!--当前分页数、是否刷新的标志-->
				<input type="hidden" name="pNum" id="pNum" value="${pNum}" /> <input
					type="hidden" name="isRefresh" id="isRefresh" value="${isRefresh}" />

				<div id="graduateAud" class="searchBar">
					<ul class="searchContent">
						<li class="custom-li"><label>教学站：</label> <span
							sel-id="graduateauditbranchSchool" sel-name="branchSchool"
							sel-onchange="graduateAuditQueryUnit()" sel-classs="flexselect"></span></li>
						<li><label>年级：</label> <span sel-id="gradeid"
							sel-name="gradeid" sel-onchange="graduateAuditQueryGrade()"
							sel-style="width: 120px"></span></li>
						<li><label>层次：</label> <span sel-id="classic"
							sel-name="classic" sel-onchange="graduateAuditQueryClassic()"
							sel-style="width: 120px"></span></li>
						<li><label>学习形式：</label> <span sel-id="teachingType"
							sel-name="teachingType" dictionaryCode="CodeTeachingType"
							sel-onchange="graduateAuditTeachingType()"
							sel-style="width: 120px"></span></li>

					</ul>
					<ul class="searchContent">
						<li class="custom-li"><label>专业：</label> <span sel-id="major" sel-name="major"
							sel-onchange="graduateAuditQueryMajor()" sel-classs="flexselect"></span></li>
						

						<li><label>姓名：</label><input id="name" type="text"
							name="name" value="${condition['name']}" style="width: 115px" />
						</li>
						<li><label>学号：</label><input id="matriculateNoticeNo"
							type="text" name="matriculateNoticeNo"
							value="${condition['matriculateNoticeNo']}" style="width: 115px" />
						</li>
						<li><label>学籍状态：</label> <select name="stuStatus"
							id="stuStatus" style="width: 120px">
						</select> <!--<gh:select id="stuStatus" name="stuStatus" value="${condition['stuStatus']}" dictionaryCode="CodeStudentStatus" style="width:125px" filtrationStr="11,16,21,22,27"/>-->
						</li>
					</ul>
					<%--<ul class="searchContent">
				<li>
					 &nbsp; &nbsp;<font color="red">审核类型</font>&nbsp;&nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
					 <input type="checkbox" name="graduation" id="graduation" onclick="graduationClick(1)" />毕业
				</li>
				<li>
					<input type="checkbox" name="the_graduation " id="the_graduation" onclick="graduationClick(2)"/>结业
				</li>
				
				<li>
					<input type="checkbox" name="stuStatusCondition" id="stuStatusCondition" />学籍状态要求
				</li>
				 
				<li>
					<input type="checkbox" name="enterSchCondition" id="enterSchCondition" />通过入学资格审核
				</li>
				<li>
					<input type="checkbox" name="applyCondition" id="applyCondition" />未提出延迟毕业申请
				</li>
				
			</ul> --%>
					<ul class="searchContent">
						<li class="custom-li"><label>班级：</label> <span sel-id="graduateaudit_classid"
							sel-name="classesid" sel-classs="flexselect"></span></li>
						
						<li style="width: 170px;">毕业年限： <select id="isReachGraYear"
							name="isReachGraYear" style="width: 80px">
								<option value="">请选择</option>
								<option value="1"
									<c:if test="${condition['isReachGraYear']==1}"> selected= "selected" </c:if>>达到</option>
								<option value="0"
									<c:if test="${condition['isReachGraYear']==0}"> selected= "selected" </c:if>>未达到</option>
						</select></li>
						<li style="width: 170px;">入学资格：<select id="isPassEnter"
							name="isPassEnter" style="width: 80px">
								<option value="">请选择</option>
								<option value="1"
									<c:if test="${condition['isPassEnter']==1}"> selected= "selected" </c:if>>通过</option>
								<option value="0"
									<c:if test="${condition['isPassEnter']==0}"> selected= "selected" </c:if>>未通过</option>
						</select></li>
						<li style="width: 170px;">延迟毕业： <select id="isApplyDelay"
							name="isApplyDelay" style="width: 80px">
								<option value="">请选择</option>
								<option value="1"
									<c:if test="${condition['isApplyDelay']==1}"> selected= "selected" </c:if>>申请</option>
								<option value="0"
									<c:if test="${condition['isApplyDelay']==0}"> selected= "selected" </c:if>>未申请</option>
						</select></li>
						<li style="width: 170px;">有无相片： <gh:select id="havePhoto"
								name="havePhoto" dictionaryCode="yesOrNo"
								value="${condition['havePhoto']}" choose="Y"
								style="width: 80px" /></li>
					</ul>
					<ul class="searchContent">
						<li style="width: 90%;height: auto">
							<font color="red">审核条件</font>
							<input type="checkbox" id="yearCondition" name="yearCondition"
								   <c:if test="${schoolCode eq '10571'}">onclick="return false;"</c:if> />
							<font id="yearCondition_text">1.达到毕业年限</font>
							<input type="checkbox" name="enterSchCondition" style="margin-left: 30px"
								   id="enterSchCondition" onclick="return false;" /><font
								id="enterSchCondition_text">2.学籍符合要求</font>
							<input type="checkbox" name="nesScoreCondition" style="margin-left: 30px"
								   id="nesScoreCondition" onclick="return ;" /><font
								id="nesScoreCondition_text">3.达到必修学分</font>
							<input type="checkbox" name="totalScoreCondition" style="margin-left: 30px"
								   id="totalScoreCondition" onclick="return ;" /><font
								id="totalScoreCondition_text">4.达到最低毕业学分</font>
							<c:choose>
								<c:when test="${schoolCode eq '10571'}"></c:when>
								<c:when test="${schoolCode eq '10601'}">
									<input type="checkbox" name="allPassCondition" style="margin-left: 30px"
										   id="allPassCondition" /><font id="allPassCondition_text">5.所有课程都已通过</font>
									<input type="checkbox" name="stuFeeCondition" style="margin-left: 30px"
										   id="stuFeeCondition" /><font id="stuFeeCondition_text">6.缴清所有学费</font>
									<input type="checkbox" name="practiceCourseCondition" style="margin-left: 30px"
										   id="practiceCourseCondition" /><font id="practiceCourseCondition_text">7.毕业实习通过</font>
									<input type="checkbox" name="practiceMaterialsCondition" style="margin-left: 30px"
										   id="practiceMaterialsCondition" /><font id="practiceMaterialsCondition_text">8.提交毕业实习材料</font>
									<input type="checkbox" name="studentPhotoCondition" style="margin-left: 30px"
										   id="studentPhotoCondition" /><font id="studentPhotoCondition_text">9.提交学籍相片</font>
									<input type="checkbox" name="juniorCheckCondition" style="margin-left: 30px"
										   id="juniorCheckCondition" /><font id="juniorCheckCondition_text">10.专科清查</font>
								</c:when>
								<c:otherwise>
									<input type="checkbox" name="allPassCondition" style="margin-left: 30px"
										   id="allPassCondition" /><font id="allPassCondition_text">5.所有课程都已通过</font>
									<input type="checkbox" name="stuFeeCondition" style="margin-left: 30px"
										   id="stuFeeCondition" /><font id="stuFeeCondition_text">6.缴清所有学费</font>
								</c:otherwise>
							</c:choose>
						</li>

						<%--<li>
					<input type="checkbox" name="scoreCondition" id="scoreCondition"  onclick="checkAllScoreCon()"/>审核条件    <a id="showdetail" href="#" onclick="show()">详细>>></a><a id="hidedetail" href="#" onclick="hide()">收起&lt;&lt;&lt;</a>
				</li>
				<li class="scoreDetails" >
					<input type="checkbox" name="totalScoreCondition" id="totalScoreCondition" onclick="checkAllSwitch()" />总学分要求
				</li>
				<li class="scoreDetails" >
					<input type="checkbox" name="nesScoreCondition" id="nesScoreCondition"  onclick="checkAllSwitch()" />必修课完成要求
				</li>
				<li class="scoreDetails" >
					<input type="checkbox" name="limScoreCondition" id="limScoreCondition" onclick="checkAllSwitch()" />限选课要求
				</li> --%>
						<div class="buttonActive" style="float: right">
							<div class="buttonContent">
								<button id="searchGData" type="submit">查 询</button>
							</div>
						</div>
					</ul>
					<!--  <ul>
				<li>
					<label>设置毕业时间:</label><input type="text" id="graduateDate"  name="graduateDate" class="Wdate" value="${setDate}" onfocus="WdatePicker({onpicked:function(){saveGraduateDate();},isShowWeek:true })"   />
				<font id="graduateDateEx" color="red">该毕业时间为毕业审核的毕业时间。</font><input type="checkbox" name="useGraduateDate" id="useGraduateDate" onchange="changeGraduateDateEditable()"/>启用此处设置
				</li>
			</ul>-->
				</div>
			</form>
		</div>
		<div class="pageContent">
			<gh:resAuth parentCode="RES_SCHOOL_GRADUATION_AUDIT" pageType="list"></gh:resAuth>
			<table class="table" layouth="191" id="graduateAuditTable">
				<thead>
					<tr>
						<%--onclick="trueCheckAll();" --%>
						<th width="3%"><input type="checkbox" name="checkall"
							id="check_all_audit"
							onclick="checkboxAll('#check_all_audit','studentInforesourceid','#auditBody'),defaultCheckAll()"
							<c:if test="${condition['checkAllFlag']=='1'}">checked="checked"</c:if> /></th>
						<th width="8%">学号</th>
						<th width="4%">姓名</th>
						<th width="3%">性别</th>
						<th width="4%">年级</th>
						<th width="5%">学习形式</th>
						<th width="5%">培养层次</th>
						<th width="11%">教学站</th>
						<th width="8%">专业</th>
						<th width="11%">班级</th>
						<th width="10%">身份证</th>
						<th width="4%">民族</th>
						<th width="7%">入学日期</th>
						<th width="5%">学籍状态</th>
						<th width="4%">学籍相片</th>
						<!-- 
			        <th width="6%">学习方式</th>
			        <th width="6%">账号状态</th>
			        -->
					</tr>
				</thead>
				<tbody id="auditBody">
					<c:forEach items="${graduationQualifList.result}" var="stu" varStatus="vs">
					<c:choose>
						<c:when test="${studentStatus eq '24' || studentStatus eq '27'}">
								<tr>
									<td><input type="checkbox" name="studentInforesourceid"
											   value="${stu.studentInfo.resourceid }"
											   studentstatus="${ stu.studentInfo.studentStatus}"
											   autocomplete="off"
											   <c:if test="${condition['checkAllFlag']=='1'}">checked="checked"</c:if> /></td>
									<td>${stu.studentInfo.studyNo}</td>
									<td><a href="#"
										   onclick="viewStuInfo2('${stu.studentInfo.resourceid}')"
										   title="点击查看">${stu.studentInfo.studentBaseInfo.name }</a></td>
									<td>${ghfn:dictCode2Val('CodeSex',stu.studentInfo.studentBaseInfo.gender) }</td>
									<td>${stu.studentInfo.grade.gradeName}</td>
									<td>${ghfn:dictCode2Val('CodeTeachingType',stu.studentInfo.teachingType)}</td>
									<td>${stu.studentInfo.classic.classicName }</td>
									<td style="text-align: left;">${stu.studentInfo.branchSchool}</td>
									<td style="text-align: left;">${stu.studentInfo.major.majorName }</td>
									<td style="text-align: left;">${stu.studentInfo.classes.classname}</td>
									<td>${stu.studentInfo.studentBaseInfo.certNum }</td>
									<td>${ghfn:dictCode2Val('CodeNation',stu.studentInfo.studentBaseInfo.nation) }</td>
									<td>${empty stu.studentInfo.inDate ? stu.studentInfo.grade.indate : stu.studentInfo.inDate }</td>
									<td>
											${ghfn:dictCode2Val('CodeStudentStatus',stu.studentInfo.studentStatus)}
									</td>
									<td>${empty stu.studentInfo.studentBaseInfo.photoPath ? '无' : '有' }</td>

								</tr>
						</c:when>
						<c:otherwise>
								<tr>
									<td><input type="checkbox" name="studentInforesourceid"
											   value="${stu.resourceid }" autocomplete="off"
											   studentstatus="${ stu.studentStatus}"
											   <c:if test="${condition['checkAllFlag']=='1'}">checked="checked"</c:if> /></td>
									<td>${stu.studyNo}</td>
									<td><a href="#"
										   onclick="viewStuInfo2('${stu.resourceid}')" title="点击查看">${stu.studentBaseInfo.name }</a></td>
									<td>${ghfn:dictCode2Val('CodeSex',stu.studentBaseInfo.gender) }</td>
									<td>${stu.grade.gradeName}</td>
									<td>${ghfn:dictCode2Val('CodeTeachingType',stu.teachingType)}</td>
									<td>${stu.classic.classicName }</td>
									<td>${stu.branchSchool}</td>
									<td>${stu.major.majorName }</td>
									<td>${stu.classes.classname}</td>
									<td>${stu.studentBaseInfo.certNum }</td>
									<td>${ghfn:dictCode2Val('CodeNation',stu.studentBaseInfo.nation) }</td>
									<td>${empty stu.inDate ? stu.grade.indate : stu.inDate }</td>
									<td>
										<!-- ${ghfn:dictCode2Val('CodeStudentStatus',stu.studentStatus) } -->
										<c:choose>
											<c:when
													test="${stu.studentStatus == '11' and stu.accountStatus==1}">正常注册</c:when>
											<c:when
													test="${stu.studentStatus == '11' and stu.accountStatus==0}">正常未注册</c:when>
											<c:otherwise>${ghfn:dictCode2Val('CodeStudentStatus',stu.studentStatus)}</c:otherwise>
										</c:choose>
									</td>
									<td>${empty stu.studentBaseInfo.photoPath ? '无' : '有' }</td>
									<!--
					<td>${ghfn:dictCode2Val('DegreeCondition',stu.learningStyle) }</td>

		            <td>
		            <c:choose>
		            	<c:when test="${ not empty stu.sysUser}">
		            	 <c:if test="${ stu.accountStatus == 1}">激活</c:if><font color="red"><c:if test="${ stu.accountStatus == 0}">停用</c:if></font>
		            	</c:when>
		            	<c:otherwise>
		            		<font color="red">未注册</font>
		            	</c:otherwise>
		            </c:choose>
		            </td>
		             -->
								</tr>
						</c:otherwise>
					</c:choose>
					</c:forEach>

				</tbody>
			</table>
		</div>
		<div  class="pageContent" style="position: absolute;bottom: 0px;width: 100%">
			<gh:page page="${graduationQualifList}"
				goPageUrl="${baseUrl }/edu3/schoolroll/graduate/audit/list.html"
				pageType="sys" condition="${condition}" /></div>
	</div>

</body>
<script type="text/javascript">
	function calculateTotalCreditHour(){
		var stus="";
		$("input[name='studentInforesourceid']").each(function(){
			if($(this).attr("checked")){
				stus += ""+$(this).val()+",";
			}
		});
		if(""==stus){
			alertMsg.warn("请选择一条或多条记录！");return false;
		}
		var url= "${baseUrl }/edu3/teaching/result/graduation_calculateTotalCreditHour.html?stus="+stus;
		$.ajax({
			type:"post",
			url:url,
			dataType:"json",
			success:function(data){
				var msg= data['msg'];
				if(data['statusCode']==200){
					alertMsg.correct(msg)
				}else if(data['statusCode']==300){
					alertMsg.info(msg);
				}else{
					alertMsg.error(msg);
				}
			}
		});
	}

//----验证所选择的学生是不是均属于在学状态。
//获得选取的学生
	function validateSelectedIdForAudit(){
		var stus="";
		$("input[name='studentInforesourceid']").each(function(){
			if($(this).attr("checked")){
				stus += ""+$(this).val()+",";
			}
		});
		if(""==stus){
			alertMsg.warn("请选择一条或多条记录！");return false;
		}
		
		//审核条件
		var yearCondition 				= 	$('#yearCondition');
		var enterSchCondition  			= 	$('#enterSchCondition');
		var nesScoreCondition  			= 	$('#nesScoreCondition');
		var totalScoreCondition  		= 	$('#totalScoreCondition');
		var allPassCondition  		= 	$('#allPassCondition');
		var stuFeeCondition  		= 	$('#stuFeeCondition');
		
		//确保4个选项都选中
		/**yearCondition.attr("checked","checked");
		enterSchCondition.attr("checked","checked");
		nesScoreCondition.attr("checked","checked");
		totalScoreCondition.attr("checked","checked");
		**/
		
		if(false == yearCondition.attr('checked') && false == enterSchCondition.attr('checked') && false == stuFeeCondition.attr('checked')
				&& false == nesScoreCondition.attr('checked') && false == totalScoreCondition.attr('checked')&& false == allPassCondition.attr('checked')){
			alertMsg.warn("请选择审核条件！");return false;
		}
		
		auditGraduate(stus);
		/*
		//因为学位审核和毕业审核放在一起了，而毕业审核和学位审核不一定同时进行，所以去除对学籍状态的限制
		var url= "${baseUrl}/edu3/schoolroll/graduate/audit/validateStus.html?stus="+stus+"&studyStatus=atSchool";
		$.ajax({
			type:"post",
			url:url,
			dataType:"json",
			success:function(data){
				if(data['isLegal']==false){
					alert("选择设置毕业时间的学生中有处于非在学学籍状态的学生。");
					return false;
				}else{
					auditGraduate(stus);
				}
			}
		});*/
	}
	function auditGraduate(val){
		//审核条件
		var yearCondition 			= $("#yearCondition").attr("checked");
		//var stuStatusCondition = $("#stuStatusCondition").attr("checked");
		var enterSchCondition 	= $("#enterSchCondition").attr("checked");
		var applyCondition 		= $("#applyCondition").attr("checked");
		var totalScoreCondition = $("#totalScoreCondition").attr("checked");
		var nesScoreCondition = $("#nesScoreCondition").attr("checked");
		var limScoreCondition 	= $("#limScoreCondition").attr("checked");
		var allPassCondition 	= $("#allPassCondition").attr("checked");
		var stuFeeCondition 	= $("#stuFeeCondition").attr("checked");

        var practiceCourseCondition 	= $("#practiceCourseCondition").attr("checked");

        var practiceMaterialsCondition 	= $("#practiceMaterialsCondition").attr("checked");
        var studentPhotoCondition 	= $("#studentPhotoCondition").attr("checked");
        var juniorCheckCondition 	= $("#juniorCheckCondition").attr("checked");
	    var graduateCon 			= "&yearCondition="+yearCondition+
	    //"&stuStatusCondition="+stuStatusCondition+
	    "&enterSchCondition="+enterSchCondition+
	    "&applyCondition="+applyCondition+
	    "&totalScoreCondition="+totalScoreCondition+
	    "&nesScoreCondition="+nesScoreCondition+
	    "&limScoreCondition="+limScoreCondition+
	    "&allPassCondition="+allPassCondition+
	    "&stuFeeCondition="+stuFeeCondition+
            "&practiceCourseCondition="+practiceCourseCondition+
            "&practiceMaterialsCondition="+practiceMaterialsCondition+
            "&studentPhotoCondition="+studentPhotoCondition+
            "&juniorCheckCondition="+juniorCheckCondition;
	    //查询条件
	    var branchSchool=$('#graduateAud #graduateauditbranchSchool').val()==undefined?"":$('#graduateAud #graduateauditbranchSchool').val();
		var major		=$('#graduateAud #major').val()==undefined?"":$('#graduateAud #major').val();
		var classic		=$('#graduateAud #classic').val()==undefined?"":$('#graduateAud #classic').val();
		var stuStatus	=$('#graduateAud #stuStatus').val()==undefined?"":$('#graduateAud #stuStatus').val();
		var name		=$('#graduateAud #name').val()==undefined?"":$('#graduateAud #name').val();
		name=encodeURIComponent(name);
		var matriculateNoticeNo	=$('#graduateAud #matriculateNoticeNo').val()==undefined?"":$('#graduateAud #matriculateNoticeNo').val();
		var grade 		=$('#graduateAud #gradeid').val()==undefined?"":$('#graduateAud #gradeid').val();
		var isReachGraYear =$('#graduateAud #isReachGraYear').val()==undefined?"":$('#graduateAud #isReachGraYear').val();//达到毕业最低年限
		var isPassEnter    = $('#graduateAud #isPassEnter').val()==undefined?"":$('#graduateAud #isPassEnter').val();//通过入学资格审核
		var isApplyDelay   =$('#graduateAud #isApplyDelay').val()==undefined?"":$('#graduateAud #isApplyDelay').val();//申请延迟毕业
		var selectCon =  "&branchSchool="+branchSchool+
	    "&major="+major+
	    "&classic="+classic+
	    "&stuStatus="+stuStatus+
	    "&name="+name+
	    "&matriculateNoticeNo="+matriculateNoticeNo+
	    "&grade="+grade+
	    "&isReachGraYear="+isReachGraYear+
	    "&isPassEnter="+isPassEnter+
	    "&isApplyDelay="+isApplyDelay;
		//是否使用全选
		var isSelectAll = $("#pagerForm input[name='checkAllFlag']").val();
		/*
		if(""==grade&&"1"==isSelectAll){
			alertMsg.warn("您使用了按查询结果审核，请您选择年级条件，以保证审核的时间。"); return false;
		} 需要改成只做提示不做限制*/
		
		var url= "${baseUrl }/edu3/schoolroll/graduation/student/getGraduateAuditNum.html?stus="+val+graduateCon+selectCon+"&isSelectAll="+isSelectAll+"&doAudit=Y";
		$.ajax({
				type:"post",
				url:url,
				dataType:"json",
				success:function(data){
					var total= data['total'];
					var info = data['info'];
					if(total!=null && total!=undefined){
						var tips = "审核条数为"+total+"条。";
						if(total<10000&&total>5000){
							tips="审核条数为"+total+"条，毕业审核时间将<font color='blue'>比较长</font>，请耐心等待。";
						} else if(total>10000&&"1"==isSelectAll){
							tips="审核条数为"+total+"条，毕业审核时间将<font color='red'>很长</font>，您确定要进行毕业审核么？";
						}
						var pNum       =$('#pNum').val()==undefined?"":$('#pNum').val();
						var postUrl="${baseUrl}/edu3/roll/graduateaudit/viaGraduate.html?stus="+val+graduateCon+selectCon+"&isSelectAll="+isSelectAll+"&doAudit=Y";
						alertMsg.confirm(tips,{
							okCall: function(){
								navTab.openTab('_blank', postUrl, '毕业/结业审核结果');
							}
						});
					}else{
						alertMsg.warn(info);
					}
		}});
		
		//获得参数组
		//以下若干参数为审核后刷新页面所需参数
		//毕业审核时所需毕业时间(根据用户定义是否填写)
		//var graduateDate = $('#graduateAud #graduateDate').val();
		//var useGraDateInFront = $("#useGraduateDate").attr("checked");
		//如果启用前台的毕业日期，就必须保证前台非空
		/*if(""==graduateDate&&useGraDateInFront){
			alert("您设定的毕业日期为空,毕业审核操作失败。");
			return false;
		}else{
		*/	
		//$.pdialog.open(postUrl, 'RES_SCHOOL_GRADUATION_AUDIT', '毕业审核结果', {max:true,maxable:true});
		//}
	}
	
	function auditGraduate_old(val){
		//获得参数组
		//以下若干参数为审核后刷新页面所需参数
		var branchSchool=$('#graduateAud #graduateauditbranchSchool').val()==undefined?"":$('#graduateAud #graduateauditbranchSchool').val();
		var major		=$('#graduateAud #major').val()==undefined?"":$('#graduateAud #major').val();
		var classic		=$('#graduateAud #classic').val()==undefined?"":$('#graduateAud #classic').val();
		var stuStatus	=$('#graduateAud #stuStatus').val()==undefined?"":$('#graduateAud #stuStatus').val();
		var name		=$('#graduateAud #name').val()==undefined?"":$('#graduateAud #name').val();
		var matriculateNoticeNo	=$('#graduateAud #matriculateNoticeNo').val()==undefined?"":$('#graduateAud #matriculateNoticeNo').val();
		var grade 		=$('#graduateAud #gradeid').val()==undefined?"":$('#graduateAud #gradeid').val();
		var pNum       =$('#pNum').val()==undefined?"":$('#pNum').val();
		//毕业审核时所需毕业时间(根据用户定义是否填写)
		var graduateDate = $('#graduateAud #graduateDate').val();
		var url = "${baseUrl}/edu3/roll/graduateaudit/auditGraduate.html?stus="+val+"&graduateDate="+graduateDate 
				+"&branchSchool="+branchSchool+"&major="+major+"&classic="+classic+"&stuStatus="+stuStatus+"&name="+name+"&matriculateNoticeNo="+matriculateNoticeNo+"&grade="+grade+"&pNum="+pNum;
		var useGraDateInFront = $("#useGraduateDate").attr("checked");
		//如果启用前台的毕业日期，就必须保证前台非空
		if(""==graduateDate&&useGraDateInFront){
			alert("您设定的毕业日期为空,毕业审核操作失败。");
			return false;
		}else{
			$.pdialog.open(url, 'RES_SCHOOL_GRADUATION_AUDIT', '毕业审核管理', {max:true,maxable:true});
		}
	/*
	alertMsg.confirm("您设定的毕业日期为空。", {
		okCall: function(){//执行			
			$.pdialog.open(url, 'RES_SCHOOL_GRADUATION_AUDIT', '毕业审核管理', {width: 800, height: 600});
		}
			
	});
	*/	
	/*var ids = new Array();
	jQuery("#auditBody input[name='resourceid']:checked").each(function(){
		ids.push(jQuery(this).val());
	});
	if(ids.length==0||ids.length>1){alertMsg.warn("请选择一条记录！");return false};*/
	}
	
	function viewStuInfo2(id){
		var url = "${baseUrl}/edu3/framework/studentinfo/view.html";
		$.pdialog.open(url+'?resourceid='+id, 'RES_SCHOOL_SCHOOLROLL_MANAGER_VIEW', '查看学籍', {width: 800, height: 600});
	}
	
	function modifyStuInfo(){
		var url = "${baseUrl}/edu3/register/studentinfo/editstu.html";
		if(isCheckOnlyone('resourceid','#infoBody')){
			navTab.openTab('RES_SCHOOL_SCHOOLROLL_MANAGER_EDIT', url+'?resourceid='+$("#infoBody input[@name='resourceid']:checked").val(), '修改学籍');
		}			
	}
	
	function disenabledStudentAccount(){//停用账号			
			pageBarHandle("您确定要停用这些学生账号吗？","${baseUrl}/edu3/register/studentinfo/enableaccount.html?type=disenable","#infoBody");		
		
	}
	
	function enabledStudentAccount(){//启用账号		
		pageBarHandle("您确定要启用这些学生账号吗？","${baseUrl}/edu3/register/studentinfo/enableaccount.html?type=enable","#infoBody");			
	}
	function trueCheckAll(){
		if($("#check_all_audit").attr("checked")==true){
			$("input[name='checkAllFlag']").val("1");
		}else{
			$("input[name='checkAllFlag']").val("0");
		}
		checkboxAll('#check_all_audit','studentInforesourceid','#auditBody');
	}
	function validateSecGraduateForAudit(){
		//选择结业状态
		var stustatus = $("#stuStatus").val();
	// 	console.info(stustatus);
		if(stustatus=="" || (stustatus!='24' && stustatus!='27')){
			alertMsg.warn("请先选择查询条件：学籍状态，并选择结业或预毕业状态的学生！");return false;
		}
		var stus="";
		
		$("input[name='studentInforesourceid']").each(function(){
			if($(this).attr("checked")){
				stus += ""+$(this).val()+",";
			}
		});
		
		if(""==stus){
			alertMsg.warn("请选择一条或多条记录！");return false;
		}
		//检查勾选的是结业状态的学生
		$("input[name='studentstatus']").each(function(){
			if($(this).val()!='24'){
				alertMsg.warn("请勾选结业状态的学生！");
				return false;
			}
		});
		//审核条件
		var yearCondition 				= 	$('#yearCondition');
		var enterSchCondition  			= 	$('#enterSchCondition');
		var nesScoreCondition  			= 	$('#nesScoreCondition');
		var totalScoreCondition  		= 	$('#totalScoreCondition');
		
		//确保4个选项都选中
		/**yearCondition.attr("checked","checked");
		enterSchCondition.attr("checked","checked");
		nesScoreCondition.attr("checked","checked");
		totalScoreCondition.attr("checked","checked");
		**/
		
		if(false == yearCondition.attr('checked') && false == enterSchCondition.attr('checked') 
				&& false == nesScoreCondition.attr('checked') && false == totalScoreCondition.attr('checked')){
			alertMsg.warn("请选择审核条件！");return false;
		}
		
		auditGraduate(stus);
		
	}
</script>
</html>