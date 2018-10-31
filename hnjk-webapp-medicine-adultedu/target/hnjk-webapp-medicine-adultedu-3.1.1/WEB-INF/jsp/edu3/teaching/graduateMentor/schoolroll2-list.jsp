<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>增加学生</title>
</head>
<body>

	<div class="page">
		<div class="pageHeader">
			<form onsubmit="return dialogSearch(this);"
				action="${baseUrl}/edu3/teaching/graduateMentor/chooseStudent.html"
				method="post">
				<div class="searchBar">
					<ul class="searchContent">
						<li><label>批次：</label>
						<gh:selectModel id="batchId6" name="batchId"
								bindValue="resourceid" displayValue="batchName"
								value="${condition['batchId']}"
								modelClass="com.hnjk.edu.teaching.model.ExamSub"
								style="width:120px" condition="batchType='thesis'" /></li>
						<c:if test="${showCenter eq 'show'}">
							<li><label>教学站：</label> <gh:selectModel id="branchSchool6"
									name="branchSchool" bindValue="resourceid"
									displayValue="unitName" style="width:120px"
									modelClass="com.hnjk.security.model.OrgUnit"
									value="${condition['branchSchool']}"
									condition="unitType='brSchool'" /></li>
						</c:if>
					</ul>
					<ul class="searchContent">
						<li><label>专业：</label>
						<gh:selectModel id="major6" name="major" bindValue="resourceid"
								displayValue="majorName" value="${condition['major']}"
								modelClass="com.hnjk.edu.basedata.model.Major"
								style="width:120px" /></li>
						<li><label>层次：</label>
						<gh:selectModel id="classic6" name="classic"
								bindValue="resourceid" displayValue="classicName"
								value="${condition['classic']}"
								modelClass="com.hnjk.edu.basedata.model.Classic"
								style="width:120px" /></li>
					</ul>
					<ul class="searchContent">
						<li><label>年级：</label>
						<gh:selectModel id="grade6" name="grade" bindValue="resourceid"
								displayValue="gradeName" style="width:120px"
								modelClass="com.hnjk.edu.basedata.model.Grade"
								orderBy="gradeName desc" value="${condition['grade']}" /></li>
						<li><label>姓名：</label><input type="text" id="name6"
							name="name" value="${condition['name']}" style="width: 115px" />
						</li>
					</ul>
					<div class="subBar">
						<ul>
							<li><div class="buttonActive">
									<div class="buttonContent">
										<button type="submit">查 询</button>
									</div>
								</div></li>
							<li><div class="button">
									<div class="buttonContent">
										<button type="button" onclick="clickThis547();">确 定</button>
									</div>
								</div></li>
						</ul>
					</div>
				</div>

			</form>
		</div>
		<div class="pageContent">
			<table class="table" layouth="168">
				<thead>
					<tr>
						<th width="5%"><input type="checkbox" name="checkall"
							id="check_allschoolRoll2in_info"
							onclick="checkboxAll('#check_allschoolRoll2in_info','resourceid','#schoolRoll2infoBody111')" /></th>
						<th width="10%">姓名</th>
						<th width="10%">学号</th>
						<th width="5%">性别</th>
						<th width="10%">身份证</th>
						<th width="10%">年级</th>
						<th width="10%">层次</th>
						<th width="10%">专业</th>
						<th width="10%">教学站</th>
						<th width="10%">学籍状态</th>
						<th width="10%">账号状态</th>
					</tr>
				</thead>
				<tbody id="schoolRoll2infoBody111">
					<c:forEach items="${ordercList.result}" var="stu" varStatus="vs">
						<tr>
							<td><input type="checkbox" name="resourceid"
								value="${stu.studentInfo.resourceid }" autocomplete="off" /></td>
							<td>${stu.studentInfo.studentName }</td>
							<td>${stu.studentInfo.studyNo}</td>
							<td>${ghfn:dictCode2Val('CodeSex',stu.studentInfo.studentBaseInfo.gender) }</td>
							<td>${stu.studentInfo.studentBaseInfo.certNum }</td>
							<td>${stu.studentInfo.grade.gradeName}</td>
							<td>${stu.studentInfo.classic.classicName }</td>
							<td>${stu.studentInfo.major.majorName }</td>
							<td>${stu.studentInfo.branchSchool}</td>
							<td>${ghfn:dictCode2Val('CodeStudentStatus',stu.studentInfo.learingStatus) }</td>
							<td><c:if test="${ stu.studentInfo.accountStatus == 1}">激活</c:if><font
								color="red"><c:if
										test="${ stu.studentInfo.accountStatus == 0}">停用</c:if></font></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<gh:page page="${ordercList}" pageNumShown="4"
				goPageUrl="${baseUrl }/edu3/teaching/graduateMentor/chooseStudent.html"
				pageType="sys" targetType="dialog" condition="${condition}" />
		</div>
	</div>
	<script type="text/javascript">
	//function ctx357(){
	//	$.pdialog.reload("${baseUrl}/edu3/teaching/graduateMentor/chooseStudent.html?branchSchool="+$('#branchSchool6').val()+"&major="+$('#major6').val()+"&classic="+$('#classic6').val()+"&batchId="+$('#batchId6').val()+"&name="+$('#name6').val()+"&grade="+$('#grade6').val());
	//}
	
	$(document).ready(function(){
			// 修改高度
			$("#cd_right").css("height",parseInt($(".dialogContent").css("height")));
			
			window.setTimeout(function(){
				$("input[name='stuId7']").each(function(){
					$("#infoBody input[value='"+$(this).val()+"']").attr("checked",true);
				});
			},500);
			
	});
	
	/*
	function checkboxAll3(checkallId,checkboxItemName,bodyName){
		$(bodyName+" INPUT[name='"+checkboxItemName+"']").each(function(ind){
			if($(checkallId).attr("checked")){
				$(this).attr("checked",true);
				$("li[id='"+$(this).attr("value")+"']").remove();
				$("#displayName5").append("<li id='"+$(this).attr("value")+"'><input name='stuId7' type='hidden' value='"+$(this).attr("value")+"'/>&nbsp;&nbsp;"+$(this).attr("alt")+"&nbsp;&nbsp;<img src='"+baseUrl+"/jscript/jquery.uploadify/images/cancel.png' onclick='deleteStu6(\""+$(this).attr("value")+"\",\"\");' style='cursor: pointer; height: 10px;'></li>");
			}else{
				$(this).attr("checked",false);
				$("li[id='"+$(this).attr("value")+"']").remove();
			}
		});
	}
	*/
	function deleteStu6(id){
		$("li[id='"+id+"']").remove();
	}

	//OK	
	function clickThis547(){
		
		//if(obj.checked){
		//	$("#displayName5").append("<li id='"+obj.value+"'><input name='stuId7' type='hidden' value='"+obj.value+"'/>&nbsp;&nbsp;"+obj.alt+"&nbsp;&nbsp;<img src='"+baseUrl+"/jscript/jquery.uploadify/images/cancel.png' onclick='deleteStu6(\""+obj.value+"\",\"\");' style='cursor: pointer; height: 10px;'></li>");
		//}else{
		//	$("li[id='"+obj.value+"']").remove();
		//}
		
		alertMsg.confirm("确定要添加这些学生吗？", {	
			okCall: function(){//执行
			var id = $("#_graduateMentorForm input[name='resourceid']").val();
		
			var res = "";
			var k = 0;
			var num  = $("#schoolRoll2infoBody111 input[name='resourceid']:checked").size();
			
			$("#schoolRoll2infoBody111 input[@name='resourceid']:checked").each(function(){
                    res+=$(this).val();
                    if(k != num -1 ) res += ",";
                    k++;
                });	 
			
			$.post("${baseUrl}/edu3/framework/teaching/graduateMentor/stu/add.html", { detailId: res ,gid:id}, function(data){
				DWZ.ajaxDone(data);
				if (data.statusCode == 200){
					if (data.navTabId){
						navTab.reload(data.reloadTabUrl, {}, data.navTabId);
					}	
				}
				$.pdialog.closeCurrent();
			},"json");			
			}
		});	
		
		
	}
</script>
</body>
</html>