<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>学生学习名单</title>
<style type="text/css">
td {
	text-align: center;
}
</style>
<script type="text/javascript">
function sendStudentTips(){
	if(!isChecked("resourceid","#studentOnline")){
			alertMsg.warn('请选择一条要操作记录！');
		return false;
 	}
	alertMsg.confirm("您确定要给这些学生发温馨提示?", {
			okCall: function(){		
				var res = "";
				var name = "";
				var num  = $("#studentOnline input[name='resourceid']:checked").size();
				$("#studentOnline input[name='resourceid']:checked").each(function(){
                        res+=$(this).val()+",";
                        name+=$(this).attr('rel')+",";
                 });
                
				$.pdialog.open('${baseUrl }/edu3/framework/message/dailog.html?type=batch&msgType=tips&userNames='+res+'&cnNames='+encodeURIComponent(name),'selector','温馨提示',{mask:true,width:750,height:550});
			}
	});	
}
</script>
</head>
<body>
	<div class="page">
		<div class="pageHeader">
			<form onsubmit="return navTabSearch(this);"
				action="${baseUrl}/edu3/learning/student/online-student.html"
				method="post">
				<input type="hidden" name="classesid"
					value="${condition['classesId']}" />
				<div class="searchBar">
					<ul class="searchContent">
						<li><label>姓名：</label><input type="text" name="studentName"
							value="${condition['studentName']}" /></li>
						<li><label>学号：</label><input type="text" name="studyNo"
							value="${condition['studyNo']}" /></li>
					</ul>
					<div class="subBar">
						<ul>
							<li><div class="buttonActive">
									<div class="buttonContent">
										<button type="submit">查 询</button>
									</div>
								</div></li>
						</ul>
					</div>
				</div>
			</form>
			<label> <span style="size: 20px; color: green"> <b>
						<br /> [ 班级：${onlineStudent.result[0].classes.classname} ] [
						年级：${onlineStudent.result[0].grade.gradeName} ] [
						层次：${onlineStudent.result[0].classic.classicName} ] [
						专业：${onlineStudent.result[0].major.majorName} ] [
						学习方式：${ghfn:dictCode2Val('CodeTeachingType',onlineStudent.result[0].classes.teachingType)}
						] <br />
					<br />
				</b>
			</span>
			</label>
		</div>

		<div class="pageContent">
			<gh:resAuth parentCode="RES_METARES_STUDENTADDRESSBOOK"
				pageType="list"></gh:resAuth>
			<table class="table" layouth="175">
				<thead>
					<tr>
						<th width="3%"><input type="checkbox" name="checkall"
							id="check_all_studentOnline"
							onclick="checkboxAll('#check_all_studentOnline','resourceid','#studentOnline')" /></th>
						<th width="8%">学号</th>
						<th width="8%">姓名</th>
						<th width="5%">性别</th>
						<th width="5%">学籍状态</th>
						<th width="10%">出生年月</th>
						<th width="12%">证件号码</th>
						<th width="8%">移动电话</th>
						<th width="10%">email</th>
						<th width="20%">联系地址</th>
					</tr>
				</thead>
				<tbody id="studentOnline">
					<c:forEach items="${onlineStudent.result}" var="stu" varStatus="vs">
						<tr>
							<td><input type="checkbox" name="resourceid"
								value="${stu.sysUser.username}" rel="${stu.sysUser.cnName }"
								autocomplete="off" /></td>
							<td>${stu.studyNo }</td>
							<td>${stu.studentBaseInfo.name }</td>
							<td>${ghfn:dictCode2Val('CodeSex',stu.studentBaseInfo.gender) }</td>
							<td>${ghfn:dictCode2Val('CodeStudentStatus',stu.studentStatus) }</td>
							<td>${stu.studentBaseInfo.bornDay }</td>
							<td>${stu.studentBaseInfo.certNum }</td>
							<td>${stu.studentBaseInfo.mobile }</td>
							<td>${stu.studentBaseInfo.email }</td>
							<td>${stu.studentBaseInfo.contactAddress }</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<gh:page page="${onlineStudent}"
				goPageUrl="${baseUrl }/edu3/learning/student/online-student.html?classesid=${onlineClassesid}"
				condition="${condition }" pageType="sys" />

		</div>
	</div>
</body>
</html>