<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>特色栏目</title>
<script type="text/javascript">
	//新增
	function addCourseReference(){
		navTab.openTab('courseReference', '${baseUrl }/edu3/learning/coursereference/list.html?courseId='+$("#courseReferenceCourseId").val(), '特色栏目');
	}
	
	//修改
	function modifyCourseReference(){
		var url = "${baseUrl }/edu3/learning/coursereference/list.html";
		if(isCheckOnlyone('resourceid','#courseReferenceBody')){
			navTab.openTab('courseReference', url+'?resourceid='+$("#courseReferenceBody input[@name='resourceid']:checked").val()+"&courseId="+$("#courseReferenceCourseId").val()+"&pageNum=${courseReferencePage.pageNum}&pageSize=${courseReferencePage.pageSize}", '特色栏目');
		}			
	}
		
	//删除
	function removeCourseReference(){	
		pageBarHandle("您确定要删除这些记录吗？","${baseUrl}/edu3/learning/coursereference/remove.html","#courseReferenceBody");
	}
	
	function viewCourseReference(){
		var courseId = $("#courseReferenceCourseId").val();
		var url = "${baseUrl }/edu3/learning/interactive/main.html?courseId="+courseId;	
		window.open(url,"interactive");
	}	
</script>
</head>
<body>

	<div class="page">
		<div class="pageHeader">
			<form onsubmit="return navTabSearch(this);"
				action="${baseUrl }/edu3/learning/coursereference/list.html"
				method="post">
				<div class="searchBar">
					<ul class="searchContent">
						<li><label>资料名称：</label><input
							value="${condition['referenceName']}" name="referenceName"
							style="width: 110px;" /> <input type="hidden" name="courseId"
							value="${condition['courseId']}" /></li>
						<li><label>资料类型：</label> <select name="referenceType">
								<option value="">请选择</option>
								<c:forEach items="${typesList }" var="t" varStatus="vs">
									<option value="${t.dictValue }"
										<c:if test="${t.dictValue eq condition['referenceType'] }"> selected </c:if>
										title="${t.dictName }">${t.dictName }</option>
								</c:forEach>
						</select></li>
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
		</div>
		<div class="pageContent">
			<input type="hidden" id="courseReferenceCourseId"
				value="${condition['courseId']}" />
			<gh:resAuth parentCode="RES_METARES_COURSEWARE_MANAGE"
				pageType="rlist"></gh:resAuth>
			<table class="table" layouth="25%">
				<thead>
					<tr>
						<th width="10%"><input type="checkbox" name="checkall"
							id="check_all_CourseReference"
							onclick="checkboxAll('#check_all_CourseReference','resourceid','#courseReferenceBody')" /></th>
						<th width="30%">课程</th>
						<th width="30%">资料类型</th>
						<th width="30%">资料名称</th>
					</tr>
				</thead>
				<tbody id="courseReferenceBody">
					<c:forEach items="${courseReferencePage.result}" var="courseRef"
						varStatus="vs">
						<tr>
							<td><input type="checkbox" name="resourceid"
								value="${courseRef.resourceid }" autocomplete="off"
								${(courseRef.resourceid eq courseReference.resourceid)?'checked':'' } /></td>
							<td>${courseRef.course.courseName }</td>
							<td>${ghfn:dictCode2Val('CodeReferenceType',courseRef.referenceType) }</td>
							<td><a href="${courseRef.url }" target="_blank">${courseRef.referenceName }</a></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>

			<gh:page page="${courseReferencePage}"
				goPageUrl="${baseUrl }/edu3/learning/coursereference/list.html"
				pageType="sys" condition="${condition}" />
		</div>

		<h2 class="contentTitle">${(empty courseReference.resourceid)?'新增':'编辑' }特色栏目</h2>
		<div class="pageContent">
			<form id="courseReferenceForm" method="post"
				action="${baseUrl}/edu3/learning/coursereference/save.html"
				class="pageForm" onsubmit="return validateReferenceUrl(this);">
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
										onclick="navTab.closeCurrentTab();">取消</button>
								</div>
							</div></li>
					</ul>
				</div>
				<input type="hidden" name="resourceid"
					value="${courseReference.resourceid }" /> <input type="hidden"
					name="from" value="course" />
				<div class="pageFormContent" layoutH="35%">
					<table class="form">
						<tr>
							<td>所属课程:</td>
							<td><input type="text" size="30" style="width: 400px;"
								value="${courseReference.course.courseName }"
								readonly="readonly" /> <input type="hidden" name="courseId"
								value="${courseReference.course.resourceid }" /></td>
						</tr>
						<tr>
							<td>资料类型:</td>
							<td><select name="referenceType" class="required">
									<option value="">请选择</option>
									<c:forEach items="${typesList }" var="t" varStatus="vs">
										<option value="${t.dictValue }"
											<c:if test="${t.dictValue eq courseReference.referenceType }"> selected </c:if>
											title="${t.dictName }">${t.dictName }</option>
									</c:forEach>
							</select> <span style="color: red;">*</span></td>
						</tr>
						<tr>
							<td>资料链接:</td>
							<td>
								<table style="width: 100%;" border="0" id="referenceUrlTable">
									<c:if test="${empty courseReference.resourceid }">
										<tr>
											<td colspan="5"><a class="button" href="javascript:;"
												onclick="addMateUrl();"><span>新增</span></a></td>
										</tr>
									</c:if>
									<tr>
										<td style="width: 10%;">名称：</td>
										<td style="width: 20%;"><input type="text"
											style="width: 90%;" name="referenceName"
											value="${courseReference.referenceName }" class="required" />
										</td>
										<td style="width: 10%;">链接：</td>
										<td style="width: 50%;"><input type="text" name="url"
											value="${courseReference.url }" style="width: 90%;"
											class="required url" /></td>
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
			var html = "<tr><td style='width: 10%;'>名称：</td><td style='width: 20%;'><input type='text' name='referenceName' style='width:90%;' value='' class='required'/></td><td style='width: 10%;'>链接：</td><td style='width: 50%;'><input type='text' name='url' value='' style='width: 90%;' class='required url'/></td><td><a class='button' href='javascript:;' onclick='delreferenceUrl(this);'><span>删除</span></a></td></tr>";
			$("#referenceUrlTable").append(html);
		}
		function delreferenceUrl(obj){
			$(obj).parent().parent().remove();
		}
		
		function validateReferenceUrl(form){
			var $form = $(form);
			var isValidate = true;
			if (!$form.valid()) {
				alertMsg.error(DWZ.msg["validateFormError"]);
				isValidate = false;
			}			
			$("#courseReferenceForm input[name='url']").each(function (){				
				if($.trim($(this).val())==""){	
					$(this).addClass("error");				
					isValidate = false;
				}
			});
			$("#courseReferenceForm input[name='referenceName']").each(function (){				
				if($.trim($(this).val())==""){	
					$(this).addClass("error");				
					isValidate = false;
				}
			});
			if(!isValidate){
				alertMsg.error(DWZ.msg["validateFormError"]);
				return false;
			}
			return validateCallback(form);
		}
	</script>
		</div>
	</div>
</body>
</html>