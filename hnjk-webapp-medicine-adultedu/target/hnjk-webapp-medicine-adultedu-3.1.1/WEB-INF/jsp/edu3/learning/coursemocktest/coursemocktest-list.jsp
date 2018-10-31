<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>课程模拟试题管理</title>
<script type="text/javascript">
	//新增
	function addCourseMockTest(){
		navTab.openTab('courseMockTest', '${baseUrl }/edu3/learning/coursemocktest/list.html?courseId='+$("#courseMockTestCourseId").val(), '模拟试题管理');
	}
	
	//修改
	function modifyCourseMockTest(){
		var url = "${baseUrl }/edu3/learning/coursemocktest/list.html";
		if(isCheckOnlyone('resourceid','#courseMockTestBody')){
			navTab.openTab('courseMockTest', url+'?resourceid='+$("#courseMockTestBody input[@name='resourceid']:checked").val()+"&courseId="+$("#courseMockTestCourseId").val()+"&pageNum=${courseMockTestPage.pageNum}&pageSize=${courseMockTestPage.pageSize}", '模拟试题管理');
		}			
	}
		
	//删除
	function removeCourseMockTest(){	
		pageBarHandle("您确定要删除这些记录吗？","${baseUrl}/edu3/learning/coursemocktest/remove.html","#courseMockTestBody");
	}
		
</script>
</head>
<body>

	<div class="page">
		<div class="pageContent">
			<input type="hidden" id="courseMockTestCourseId"
				value="${condition['courseId']}" />
			<gh:resAuth parentCode="RES_METARES_COURSEWARE_MANAGE"
				pageType="mlist"></gh:resAuth>
			<table class="table" layouth="25%">
				<thead>
					<tr>
						<th width="10%"><input type="checkbox" name="checkall"
							id="check_all_courseMockTest"
							onclick="checkboxAll('#check_all_courseMockTest','resourceid','#courseMockTestBody')" /></th>
						<th width="30%">所属课程</th>
						<th width="60%">模拟试题</th>
					</tr>
				</thead>
				<tbody id="courseMockTestBody">
					<c:forEach items="${courseMockTestPage.result}" var="mock"
						varStatus="vs">
						<tr>
							<td><input type="checkbox" name="resourceid"
								value="${mock.resourceid }" autocomplete="off"
								${(courseMockTest.resourceid eq mock.resourceid)?'checked':'' } /></td>
							<td>${mock.course.courseName }</td>
							<td><a href="${mock.mateUrl }" target="_blank">${mock.mocktestName }</a>
							<%-- &nbsp;&nbsp;&nbsp;&nbsp;<a href="${mock.mateUrl }" style="color: green;" target="_blank">[公网入口]</a>&nbsp;&nbsp;<a href="${mock.netMeateUrl }" style="color: green;" target="_blank">[教育网入口]</a> --%></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>

			<gh:page page="${courseMockTestPage}"
				goPageUrl="${baseUrl }/edu3/learning/coursemocktest/list.html"
				pageType="sys" condition="${condition}" />
		</div>

		<h2 class="contentTitle">${(empty courseMockTest.resourceid)?'新增':'编辑' }课程模拟试题</h2>
		<div class="pageContent">
			<form method="post"
				action="${baseUrl}/edu3/learning/coursemocktest/save.html"
				class="pageForm" onsubmit="return validateMateUrl(this);">
				<div class="formBar">
					<ul>
						<li><div class="buttonActive">
								<div class="buttonContent">
									<button type="submit">提交</button>
								</div>
							</div></li>
						<li><div class="button">
								<div class="buttonContent">
									<button type="button" class="close"
										onclick="navTab.closeCurrentTab();">关闭</button>
								</div>
							</div></li>
					</ul>
				</div>
				<input type="hidden" name="resourceid"
					value="${courseMockTest.resourceid }" /> <input type="hidden"
					name="pageNum" value="${courseMockTestPage.pageNum}" /> <input
					type="hidden" name="pageSize"
					value="${courseMockTestPage.pageSize}" />
				<div class="pageFormContent" layoutH="40%">
					<table class="form">
						<tr>
							<td>所属课程:</td>
							<td><input type="text" size="30" style="width: 400px;"
								value="${courseMockTest.course.courseName }" readonly="readonly" />
								<input type="hidden" name="courseId"
								value="${courseMockTest.course.resourceid }" /></td>
						</tr>
						<tr>
							<td>资源路径:</td>
							<td>
								<table style="width: 100%;" border="0" id="mateUrlTable">
									<c:if test="${empty courseMockTest.resourceid }">
										<tr>
											<td colspan="7"><a class="button" href="javascript:;"
												onclick="addMateUrl();"><span>新增</span></a></td>
										</tr>
									</c:if>
									<tr>
										<td style="width: 10%;">名称：</td>
										<td style="width: 20%;"><input type="text"
											name="mocktestName" value="${courseMockTest.mocktestName }"
											style="width: 95%;" class="required" /></td>
										<td style="width: 10%;">公网路径：</td>
										<td style="width: 50%;"><input type="text" name="mateUrl"
											value="${courseMockTest.mateUrl }" style="width: 95%;"
											class="required url" /></td>
										<%-- 
								<td style="width: 8%;">
								教育网路径：
								</td>
								<td style="width: 25%;">
								<input type="text" name="netMeateUrl" value="${courseMockTest.netMeateUrl }" style="width: 95%;" class="required url"/>
								</td>
								 --%>
										<td style="width: 10%;">&nbsp;</td>
									</tr>

								</table>
							</td>
						</tr>
					</table>
				</div>

			</form>
			<script type="text/javascript">
		function addMateUrl(){
			//var html = "<tr><td>名称：</td><td><input type='text' style='width: 95%;' name='mocktestName' value='' class='required'/></td><td>公网路径：</td><td><input type='text' name='mateUrl' value='' style='width: 95%;' class='required url'/></td><td>教育网路径：</td><td><input type='text' name='netMeateUrl' value='' style='width: 95%;' class='required url'/></td><td><a class='button' href='javascript:;' onclick='delMateUrl(this);'><span>删除</span></a></td></tr>";
			var html = "<tr><td>名称：</td><td><input type='text' style='width: 95%;' name='mocktestName' value='' class='required'/></td><td>公网路径：</td><td><input type='text' name='mateUrl' value='' style='width: 95%;' class='required url'/></td><td><a class='button' href='javascript:;' onclick='delMateUrl(this);'><span>删除</span></a></td></tr>";
			$("#mateUrlTable").append(html);
		}
		function delMateUrl(obj){
			$(obj).parent().parent().remove();
		}
		
		function validateMateUrl(form){
			var isValidate = true;
			$("#mateUrlTable input[name='mateUrl']").each(function (){				
				if($.trim($(this).val())==""){	
					$(this).addClass("error");				
					isValidate = false;
				}
			});
			$("#mateUrlTable input[name='mocktestName']").each(function (){				
				if($.trim($(this).val())==""){	
					$(this).addClass("error");				
					isValidate = false;
				}
			});
			if(!isValidate){
				alertMsg.warn("字段不能为空！");
				return false;
			}
			return validateCallback(form);
		}
	</script>
		</div>
	</div>
</body>
</html>