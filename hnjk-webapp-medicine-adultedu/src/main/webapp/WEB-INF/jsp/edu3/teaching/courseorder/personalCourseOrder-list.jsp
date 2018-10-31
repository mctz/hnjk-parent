<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>个人预约学习\考试权限设置</title>
<script type="text/javascript">
$(document).ready(function(){
		personalCourseOrderQueryBegin();
	});
//打开页面或者点击查询（即加载页面执行）
function personalCourseOrderQueryBegin() {
	var defaultValue = "${condition['branchSchool']}";
	var schoolId = "${linkageQuerySchoolId}";
	var gradeId = "${condition['gradeid']}";
	var classicId = "${condition['classic']}";
	var teachingType = "${condition['name~teachingType']}";
	var majorId = "${condition['major']}";
	var classesId = "${condition['name~classes']}";
	var selectIdsJson = "{unitId:'personal_orderConfig_brSchoolName',gradeId:'gradeid',classicId:'classicid',majorId:'majorid'}";
	cascadeQuery("begin", defaultValue, schoolId, gradeId, classicId,teachingType, majorId, classesId, selectIdsJson);
}

// 选择教学点
	function personalCourseOrderQueryUnit() {
		var defaultValue = $("#personal_orderConfig_brSchoolName").val();
		var selectIdsJson = "{gradeId:'gradeid',classicId:'classicid',majorId:'majorid'}";
		cascadeQuery("unit", defaultValue, "", "", "", "", "", "",selectIdsJson);
	}

	// 选择年级
	function personalCourseOrderQueryGrade() {
		var defaultValue = $("#personal_orderConfig_brSchoolName").val();
		var gradeId = $("#gradeid").val();
		var selectIdsJson = "{classicId:'classicid',majorId:'majorid'}";
		cascadeQuery("grade", defaultValue, "", gradeId, "", "", "", "",selectIdsJson);
	}

	// 选择层次
	function personalCourseOrderQueryClassic() {
		var defaultValue = $("#personal_orderConfig_brSchoolName").val();
		var gradeId = $("#gradeid").val();
		var classicId = $("#classicid").val();
		var selectIdsJson = "{majorId:'majorid'}";
		cascadeQuery("classic", defaultValue, "", gradeId, classicId, "", "", "", selectIdsJson);
	}
</script>
</head>
<body>
	<div class="page">
		<div class="pageHeader">
			<form id="personalOrderConfigSearchForm"
				onsubmit="return navTabSearch(this);"
				action="${baseUrl }/edu3/teaching/courseorder/personallearnbook-list.html"
				method="post">
				<input name="configStatus" value="${condition['configStatus']}"
					type="hidden" " />
				<div class="searchBar">
					<ul class="searchContent">
						<li style="width: 360px;"><label>教学站：</label> <span
							sel-id="personal_orderConfig_brSchoolName"
							sel-name="branchSchool"
							sel-onchange="personalCourseOrderQueryUnit()"
							sel-classs="flexselect"></span></li>
						<li><label>年级：</label> <span sel-id="gradeid"
							sel-name="gradeid" sel-onchange="personalCourseOrderQueryGrade()"
							sel-style="width: 120px"></span></li>
						<li><label>层次：</label> <span sel-id="classicid"
							sel-name="classic"
							sel-onchange="personalCourseOrderQueryClassic()"
							sel-style="width: 120px"></span></li>
						

					</ul>
					<ul class="searchContent">
						<li style="width: 360px;"><label>专业：</label> <span sel-id="majorid"
							sel-name="major" sel-classs="flexselect"></span>
						</li>
						<li><label>姓名：</label><input type="text" name="name"
							value="${condition['name']}" style="width: 55%" /></li>
						<li><label>学号：</label><input type="text"
							name="matriculateNoticeNo"
							value="${condition['matriculateNoticeNo']}" style="width: 55%" />
						</li>
						<li><label> <c:choose>
									<c:when test="${condition['configStatus'] eq '1'}">预约学习状态</c:when>
									<c:when test="${condition['configStatus'] eq '2'}">预约考试状态</c:when>
								</c:choose>
						</label> <select name="orderCourseStatus">
								<option value="">请选择</option>
								<option value="0"
									<c:if test="${condition['orderCourseStatus'] eq '0'}"> selected="selected"</c:if>>屏蔽</option>
								<option value="1"
									<c:if test="${condition['orderCourseStatus'] eq '1'}"> selected="selected"</c:if>>开放</option>
						</select></li>
						<div class="buttonActive" style="float: right">
							<div class="buttonContent">
								<button type="submit">查 询</button>
							</div>
						</div>
					</ul>

				</div>
			</form>
		</div>
		<div class="pageContent">
			<c:choose>
				<c:when test="${condition['configStatus'] eq '1'}">
					<gh:resAuth parentCode="RES_TEACHING_BOOKING_PERSONAL_CONFIG"
						pageType="list"></gh:resAuth>
				</c:when>
				<c:when test="${condition['configStatus'] eq '2'}">
					<gh:resAuth parentCode="RES_TEACHING_EXAM_BOOKING_PERSONAL_CONFIG"
						pageType="list"></gh:resAuth>
				</c:when>
			</c:choose>
			<table class="table" layouth="138">
				<thead>
					<tr>
						<th width="3%"><input type="checkbox" name="checkall"
							id="check_all_stdentinfo_order_config"
							onclick="checkboxAll('#check_all_stdentinfo_order_config','resourceid','#personalOrderConfigBody')" /></th>
						<th width="8%">姓名</th>
						<th width="12%">学号</th>
						<th width="3%">性别</th>
						<th width="10%">身份证</th>
						<th width="5%">民族</th>
						<th width="8%">年级</th>
						<th width="10%">培养层次</th>
						<th width="10%">专业</th>
						<th width="15%">教学站</th>
						<th width="6%"><c:choose>
								<c:when test="${condition['configStatus'] eq '1'}">预约学习状态</c:when>
								<c:when test="${condition['configStatus'] eq '2'}">预约考试状态</c:when>
							</c:choose></th>
						<th width="5%">账号状态</th>
						<th width="5%">交费状态</th>
					</tr>
				</thead>
				<tbody id="personalOrderConfigBody">
					<c:forEach items="${stulist.result}" var="stu" varStatus="vs">
						<tr>
							<td><input type="checkbox" name="resourceid"
								value="${stu.resourceid }" autocomplete="off" /></td>
							<td><a href="#" onclick="viewStuInfo2('${stu.resourceid}')"
								title="点击查看">${stu.studentBaseInfo.name }</a></td>
							<td>${stu.studyNo}</td>
							<td>${ghfn:dictCode2Val('CodeSex',stu.studentBaseInfo.gender) }</td>
							<td>${stu.studentBaseInfo.certNum }</td>
							<td>${ghfn:dictCode2Val('CodeNation',stu.studentBaseInfo.nation) }</td>
							<td>${stu.grade.gradeName}</td>
							<td>${stu.classic.classicName }</td>
							<td>${stu.major.majorName }</td>
							<td>${stu.branchSchool}</td>
							<td><c:choose>
									<c:when
										test="${condition['configStatus'] eq '1' and stu.orderCourseStatus==1}">开放</c:when>
									<c:when
										test="${condition['configStatus'] eq '1' and stu.orderCourseStatus==0}">屏蔽</c:when>
									<c:when
										test="${condition['configStatus'] eq '2' and stu.examOrderStatus==1}">开放</c:when>
									<c:when
										test="${condition['configStatus'] eq '2' and stu.examOrderStatus==0}">屏蔽</c:when>
								</c:choose></td>
							<c:if test="${ stu.accountStatus == 1}">
								<td>激活</td>
							</c:if>
							<c:if test="${ stu.accountStatus == 0}">
								<td style="color: red">停用</td>
							</c:if>
							<td><a href="javascript:void(0)"
								onclick="checkFeeStatus('${stu.studyNo}')">查看</a></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<gh:page page="${stulist}"
				goPageUrl="${baseUrl }/edu3/teaching/courseorder/personallearnbook-list.html"
				pageType="sys" condition="${condition}" />
		</div>

	</div>
	<script type="text/javascript">
	function viewStuInfo2(id){
		var url = "${baseUrl}/edu3/framework/studentinfo/view.html";
		$.pdialog.open(url+'?act=view&resourceid='+id, 'RES_SCHOOL_SCHOOLROLL_MANAGER_VIEW', '查看学籍', {width: 800, height: 600});
	}
	function checkFeeStatus(studentId){
		$.ajax({
			type:"post",
			url:"${baseUrl}/edu3/teaching/courseorder/checkFeeStatus.html",
			data:({matriculateNoticeNo:studentId}),
			dataType: 'json',
			success:function(date){          	   		
         		 if(date.isFee === true){         		 	
         		 	 alertMsg.Info(date.msg);
         		 }else{
         		 	 alertMsg.warn(date.msg);
         		 }         
          	}      
			
		},"json");
	}
	//预约学习\考试屏蔽
	function personalBookDisenable(){
		var idArray = new Array();
		var configStatus = "${condition['configStatus']}";
		var ids = "";
		jQuery("#personalOrderConfigBody input[name='resourceid']:checkbox").each(function(index){
			if(jQuery(this).attr("checked")){
				ids = ids + ","+jQuery(this).val() ;
				idArray.push(index);
			}
		})
		
		if(""==ids){
			alertMsg.info('请选择要屏蔽权限的学生!')
			return ;
		}else{
			var url = "${baseUrl}/edu3/teaching/courseorder/bookingStatus.html?orderCourseStatusConfigType=personal&configStatus="+configStatus+"&orderCourseStatus=0&sturesourceId="+ids.substring(1);
			$.ajax({
				type:"post",
				url:url,
				success:function(msg){          	   		
	         		 alertMsg.correct(msg);
	         		 navTab.reload("${baseUrl}/edu3/teaching/courseorder/personallearnbook-list.html?configStatus="+configStatus,$("#searchForm").serializeArray());
	          	}      
			});
		}
	}
	//预约学习\考试开放
	function personalBookEnable(){
		var idArray = new Array();
		var configStatus = "${condition['configStatus']}";
		var ids = "";
		jQuery("#personalOrderConfigBody input[name='resourceid']:checkbox").each(function(index){
			if(jQuery(this).attr("checked")){
				ids = ids + ","+jQuery(this).val() ;
				idArray.push(index);
			}
		})
		
		if(""==ids){
			alertMsg.info('请选择要屏蔽权限的学生!')
			return ;
		}else{
			var url = "${baseUrl}/edu3/teaching/courseorder/bookingStatus.html?orderCourseStatusConfigType=personal&configStatus="+configStatus+"&orderCourseStatus=1&sturesourceId="+ids.substring(1);
			$.ajax({
				type:"post",
				url:url,
				success:function(msg){          	   		
	         		 alertMsg.correct(msg);
	         		 navTab.reload("${baseUrl}/edu3/teaching/courseorder/personallearnbook-list.html?configStatus="+configStatus,$("#searchForm").serializeArray());
	          	}      
			});
		}
	}
</script>
</body>
</html>