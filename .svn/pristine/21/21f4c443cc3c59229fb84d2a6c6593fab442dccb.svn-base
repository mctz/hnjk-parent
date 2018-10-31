<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>学生通讯录</title>
<script type="text/javascript">
$(document).ready(function(){
		stuAddressBookQueryBegin();
	});
	//打开页面或者点击查询（即加载页面执行）
	function stuAddressBookQueryBegin() {
		var defaultValue = "${condition['branchSchool']}";
		var schoolId = "${linkageQuerySchoolId}";
		
		var classicId = "${condition['classic']}";
		
		var majorId = "${condition['major']}";
		
		var selectIdsJson = "{unitId:'stuaddressbook_branchSchool',classicId:'stuaddressbook_classicid',majorId:'stuaddressbook_majorid'}";
		cascadeQuery("begin", defaultValue, schoolId, "", classicId,"", majorId, "", selectIdsJson);
	}

	// 选择教学点
	function stuAddressBookQueryUnit() {
		var defaultValue = $("#stuaddressbook_branchSchool").val();
		var selectIdsJson = "{classicId:'stuaddressbook_classicid',majorId:'stuaddressbook_majorid'}";
		cascadeQuery("unit", defaultValue, "", "", "", "", "", "",selectIdsJson);
	}

	// 选择层次
	function stuAddressBookQueryClassic() {
		var defaultValue = $("#stuaddressbook_branchSchool").val();
		var classicId = $("#stuaddressbook_classicid").val();
		var selectIdsJson = "{majorId:'stuaddressbook_majorid'}";
		cascadeQuery("classic", defaultValue, "", "", classicId, "", "", "", selectIdsJson);
	}
</script>
</head>
<body>
	<div class="page">
		<div class="pageHeader">
			<form onsubmit="return navTabSearch(this);"
				action="${baseUrl }/edu3/student/info/addressbook.html"
				method="post">
				<div class="searchBar">
					<ul class="searchContent">
						<c:if test="${empty brSchool }">
							<li><label>教学站：</label> <span
								sel-id="stuaddressbook_branchSchool" sel-name="branchSchool"
								sel-onchange="stuAddressBookQueryUnit()" sel-classs="flexselect"
								sel-style="width: 120px"></span></li>
						</c:if>
						<li><label>层次：</label> <span
							sel-id="stuaddressbook_classicid" sel-name="classic"
							sel-onchange="stuAddressBookQueryClassic()"
							sel-style="width: 120px"></span></li>
						<li><label>专业：</label> <span sel-id="stuaddressbook_majorid"
							sel-name="major" sel-onchange="stuAddressBookQueryMajor()"
							sel-classs="flexselect" sel-style="width: 120px"></span></li>

					</ul>
					<ul class="searchContent">
						<li><label>学籍状态：</label>
						<gh:select name="stuStatus" value="${condition['stuStatus']}"
								dictionaryCode="CodeStudentStatus" style="width:120px" /></li>
						<li><label>姓名：</label><input type="text" name="name"
							value="${condition['name']}" style="width: 115px" /></li>
						<li><label>学号：</label><input type="text"
							name="matriculateNoticeNo"
							value="${condition['matriculateNoticeNo']}" style="width: 115px" />
						</li>
					</ul>
					<div class="subBar">
						<ul>
							<li><span class="tips">提示：更多查询条件请点击高级查询 &nbsp;&nbsp;</span></li>
							<li><div class="buttonActive">
									<div class="buttonContent">
										<button type="submit">查 询</button>
									</div>
								</div></li>
							<li><a class="button"
								href="${baseUrl }/edu3/student/info/addressbook.html?con=advance&branchSchool=${condition['branchSchool']}&major=${condition['major']}&classic=${condition['classic']}&stuStatus=${condition['stuStatus']}&name=${condition['name']}&matriculateNoticeNo=${condition['matriculateNoticeNo']}"
								target="dialog" rel="RES_STUDENT_ADDRESSBOOK" title="学生通讯信息查询"
								height="380" width="500"><span>高级查询</span></a></li>
						</ul>
					</div>
				</div>
			</form>
		</div>
		<div class="pageContent">
			<gh:resAuth parentCode="RES_STUDENT_ADDRESSBOOK" pageType="list"></gh:resAuth>
			<table class="table" layouth="158">
				<thead>
					<tr>
						<th width="5%"><input type="checkbox" name="checkall"
							id="check_all_stuaddressbook"
							onclick="checkboxAll('#check_all_stuaddressbook','resourceid','#stuaddressbookBody')" /></th>
						<th width="4%">年级</th>
						<th width="8%">专业</th>
						<th width="6%">培养层次</th>
						<th width="6%">教学站</th>
						<th width="4%">办学模式</th>
						<th width="4%">学籍状态</th>
						<th width="7%">学号</th>
						<th width="5%">姓名</th>
						<th width="4%">性别</th>
						<th width="8%">身份证</th>
						<th width="7%">电话</th>
						<th width="7%">手机</th>
						<th width="25%">操作</th>
					</tr>
				</thead>
				<tbody id="stuaddressbookBody">
					<c:forEach items="${stulist.result}" var="stu" varStatus="vs">
						<tr>
							<td><input type="checkbox" name="resourceid"
								value="${stu.sysUser.username }" autocomplete="off"
								rel="${stu.sysUser.cnName }" /></td>
							<td title="${stu.grade.gradeName}">${stu.grade.gradeName}</td>
							<td title="${stu.major.majorName }">${stu.major.majorName }</td>
							<td title="${stu.classic.classicName }">${stu.classic.classicName }</td>
							<td title="${stu.branchSchool}">${stu.branchSchool}</td>
							<td
								title="${ghfn:dictCode2Val('CodeTeachingType',stu.teachingPlan.schoolType) }">${ghfn:dictCode2Val('CodeTeachingType',stu.teachingPlan.schoolType) }</td>
							<td
								title="${ghfn:dictCode2Val('CodeStudentStatus',stu.studentStatus) }">${ghfn:dictCode2Val('CodeStudentStatus',stu.studentStatus) }</td>
							<td title="${stu.studyNo}">${stu.studyNo}</td>
							<td title="${stu.studentBaseInfo.name }"><a href="#"
								onclick="viewStuInfo3('${stu.resourceid}','${stu.studentBaseInfo.name }')"
								title="点击查看">${stu.studentBaseInfo.name }</a></td>
							<td
								title="${ghfn:dictCode2Val('CodeSex',stu.studentBaseInfo.gender) }">${ghfn:dictCode2Val('CodeSex',stu.studentBaseInfo.gender) }</td>
							<td title="${stu.studentBaseInfo.certNum }">${stu.studentBaseInfo.certNum }</td>
							<td title="${stu.studentBaseInfo.contactPhone }">${stu.studentBaseInfo.contactPhone }</td>
							<td title="${stu.studentBaseInfo.mobile }">${stu.studentBaseInfo.mobile }</td>
							<td><a href="###"
								onclick="sendMsg(0,'${stu.sysUser.username }');" title="发消息">发消息</a>
								<!-- <a href="###" onclick="sendMsg(1,'${stu.studentBaseInfo.email }');" title="发邮件">发邮件</a> 
			            <a href="###" onclick="sendMsg(2,'${stu.studentBaseInfo.mobile }');" title="发短信">发短信</a> -->
								<a href="###"
								onclick="sendMsg(3,'${stu.branchSchool.resourceid }');"
								title="通知所在教学站">通知所在教学站</a></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<gh:page page="${stulist}"
				goPageUrl="${baseUrl }/edu3/student/info/addressbook.html"
				pageType="sys" condition="${condition}" />
		</div>
	</div>
	<script type="text/javascript">
	function sendMsg(type,touser){
		if(type==0){
			$.pdialog.open('${baseUrl }/edu3/framework/message/dailog.html?touser='+touser,'selector','发送消息',{mask:true,width:750,height:550});
		} else if(type==3) {
			$.pdialog.open('${baseUrl }/edu3/framework/message/dailog.html?tounit='+touser,'selector','发送消息',{mask:true,width:750,height:550});
		} else{
			alertMsg.warn("该功能暂时未开通!");
		}
	}
	function viewStuInfo3(id,name){
		var url = "${baseUrl}/edu3/framework/studentinfo/base/view.html";
		$.pdialog.open(url+'?resourceid='+id, 'RES_SCHOOL_SCHOOLROLL_MANAGER_VIEW', name+'的信息', {width: 600, height: 400});
	}
	//群发消息
	function batchSendMessage(){
		if(!isChecked("resourceid","#stuaddressbookBody")){
 			alertMsg.warn('请选择一条要操作记录！');
			return false;
	 	}
		alertMsg.confirm("您确定要给这些学生发消息?", {
				okCall: function(){		
					var res = "";
					var name = "";
					var num  = $("#stuaddressbookBody input[name='resourceid']:checked").size();
					$("#stuaddressbookBody input[name='resourceid']:checked").each(function(){
	                        res+=$(this).val()+",";
	                        name+=$(this).attr('rel')+",";
	                 });
	                
					$.pdialog.open('${baseUrl }/edu3/framework/message/dailog.html?type=batch&userNames='+res+'&cnNames='+encodeURIComponent(name),'selector','发送消息',{mask:true,width:750,height:550});
				}
		});			
	}
	
	
	
</script>
</body>
</html>