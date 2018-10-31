<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>教师管理</title>
<script type="text/javascript">
	//function ctx357(){
	//	$.pdialog.reload("${baseUrl}/edu3/teaching/graduateNotice/chooseTeacher.html?type=${type}&cnName="+$('#cnName8').val()+"&teacherCode="+$('#teacherCode5').val());
	//}
		
	function clickThis67(obj){
		if(obj.checked){
			if(${type eq 'notice'}){
				$("#guidTeacherId").val(obj.value);
				$("#guidTeacherName").val(obj.alt);
			}else{
				$("#guidTeacherId2").val(obj.value);
				$("#guidTeacherName2").val(obj.alt);
			}
		}		
	}
	
	function clickThisRow97(tr){
		var tdEle = tr.children[0].children[0].children[0];
		tdEle.checked = true;
		clickThis67(tdEle);
	}
	
	$(document).ready(function(){
			// 修改高度
			$("#cd_right").css("height",parseInt($(".dialogContent").css("height")));
			
			window.setTimeout(function(){
				var existId = "";
				if(${type eq 'notice'})
					existId = $("#guidTeacherId").val();
				else
					existId = $("#guidTeacherId2").val();
				
				$("#tedumanagerBody input[name=resourceid]").each(function(){
					if(existId==$(this).val()){
						$(this).attr("checked",true);
					}
				});
			},500);
			
	});
</script>
</head>
<body>

	<div class="page">
		<div class="pageHeader">
			<form onsubmit="return dialogSearch(this);"
				action="${baseUrl}/edu3/teaching/graduateNotice/chooseTeacher.html?type=${type}"
				method="post">
				<div class="searchBar">
					<ul class="searchContent">
						<li><label>姓名：</label><input type="text" id="cnName8"
							name="cnName" value="${condition['cnName']}" style="width: 120px" />
						</li>
						<!--<li>
					<label>教师编号：</label><input  type="text" id="teacherCode5" name="teacherCode" value="${condition['teacherCode']}" style="width:120px"/>
				</li>				
			-->
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
										<button type="button" onclick="$.pdialog.closeCurrent();">
											确 定</button>
									</div>
								</div></li>
						</ul>
					</div>
				</div>
			</form>
		</div>
		<div class="pageContent">
			<table class="table" layouth="138">
				<thead>
					<tr>
						<th width="5%">&nbsp;</th>
						<th width="10%">教师编号</th>
						<!--<th width="10%">教师角色</th>
		            -->
						<th width="10%">姓名</th>
						<th width="10%">登录帐号</th>
						<th width="10%">性别</th>
						<th width="10%">职称</th>
						<th width="10%">手机</th>
						<th width="10%">账号状态</th>
					</tr>
				</thead>
				<tbody id="tedumanagerBody">
					<c:forEach items="${edumanagerListPage.result}" var="edumanager"
						varStatus="vs">
						<tr onclick="clickThisRow97(this)">
							<td><input type="radio" name="resourceid"
								value="${edumanager.resourceid }" onclick="clickThis67(this)"
								alt="${edumanager.cnName }" /></td>
							<td>${edumanager.teacherCode }</td>
							<!--	
			            <td>
			            	<c:forEach items="${edumanager.roles}" var="role" varStatus="vss">
			            		${role.roleName }
			            		<c:if test="${not vss.last }">,</c:if>
			            	</c:forEach>
			            </td>		           
			            -->
							<td>${edumanager.cnName }</td>
							<td>${edumanager.username }</td>
							<td>${ghfn:dictCode2Val('CodeSex',edumanager.gender ) }</td>
							<td>${ghfn:dictCode2Val('CodeTitleOfTechnicalCode',edumanager.titleOfTechnical ) }</td>
							<td>${edumanager.mobile }</td>
							<td><c:choose>
									<c:when test="${edumanager.isDeleted == 1 }">
										<font color='red'><s>删除</s></font>
									</c:when>
									<c:when test="${!edumanager.enabled }">
										<font color='red'>未注册</font>
									</c:when>
									<c:otherwise>
										<font color='green'>正常</font>
									</c:otherwise>
								</c:choose></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>

			<gh:page page="${edumanagerListPage}"
				goPageUrl="${baseUrl }/edu3/teaching/graduateNotice/chooseTeacher.html"
				pageNumShown="4" pageType="sys" targetType="dialog"
				condition="${condition}" />
		</div>
	</div>

</body>
