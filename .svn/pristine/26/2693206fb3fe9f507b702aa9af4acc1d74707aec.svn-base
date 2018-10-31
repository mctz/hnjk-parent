<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>参考资料管理</title>
<script type="text/javascript">    
  //新增参考资料
	function addCourseReference(){
		var syllabusId = "${condition['syllabusId']}";
		var courseId = "${condition['courseId']}";
		mateAddHandle(syllabusId,courseId,"3",'${resultList.pageNum}','${resultList.pageSize}');
	}
	
	//修改参考资料
	function modifyCourseReference(){
		var syllabusId = "${condition['syllabusId']}";
		var courseId = "${condition['courseId']}";
		mateModifyHandle(syllabusId,courseId,"#courseReferenceBody1","3",'${resultList.pageNum}','${resultList.pageSize}');			
	}
		
	//删除参考资料
	function removeCourseReference(){
		var syllabusId = "${condition['syllabusId']}";
		var courseId = "${condition['courseId']}";
		var postUrl = "${baseUrl}/edu3/learning/coursereference/remove.html?syllabusId="+syllabusId+"&courseId="+courseId;
		mateRemoveHandle("您确定要删除这些记录吗？",postUrl,"#courseReferenceBody1",myMateAjaxDone);		
	}
	//预览
	function viewCourseReference(){
		var courseId = "${condition['courseId']}";
		var url = "${baseUrl }/edu3/learning/interactive/main.html?courseId="+courseId;	
		var isQualityResource = "${course.isQualityResource}";
		if(isQualityResource=='Y'){
			url = "${baseUrl }/resource/course/index.html?courseId="+courseId;	
		} 
		window.open(url,"interactive");
	}
	
	
</script>
</head>
<body>
	<div class="page">
		<div class="pageContent">
			<gh:resAuth parentCode="RES_METARES_COURSEWARE_MANAGE"
				pageType="rlist"></gh:resAuth>
			<table class="table" layouth="25%" id="courseReferenceTable">
				<thead>
					<tr>
						<th width="10%"><input type="checkbox" name="checkall"
							id="check_all_CourseReference1"
							onclick="checkboxAll('#check_all_CourseReference1','resourceid','#courseReferenceBody1')" /></th>
						<th width="15%">知识节点</th>
						<th width="15%">资料类型</th>
						<th width="30%">参考资料名称</th>
						<th width="30%">参考资料内容</th>
					</tr>
				</thead>
				<tbody id="courseReferenceBody1">
					<c:forEach items="${resultList.result}" var="courseRef"
						varStatus="vs">
						<tr>
							<td><input type="checkbox" name="resourceid"
								value="${courseRef.resourceid }" autocomplete="off"
								${(courseRef.resourceid eq courseReference.resourceid)?'checked':'' } /></td>
							<td>${courseRef.syllabus.syllabusName }</td>
							<td>${ghfn:dictCode2Val('CodeReferenceType',courseRef.referenceType) }</td>
							<td><a href="${courseRef.url }" target="_blank">${courseRef.referenceName }</a></td>
							<td>${courseRef.url }</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<gh:page page="${resultList}"
				goPageUrl="${baseUrl}/edu3/framework/metares/list.html"
				targetType="localArea"
				localArea="mateTabContent${condition['currentIndex'] }"
				condition="${condition }" pageType="sys" />
		</div>

		<c:if
			test="${(not empty condition['syllabusId'])or (not empty courseReference.resourceid)}">
			<div class="pageContent">
				<form id="courseReferenceForm1" method="post"
					action="${baseUrl}/edu3/learning/coursereference/save.html"
					class="pageForm"
					onsubmit="return validateReferenceUrl1(this,myMateAjaxDone);">
					<div class="formBar">
						<span style="font-weight: bold;">${(empty courseReference.resourceid)?'新增':'编辑' }课程参考资料</span>
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
						name="courseId" value="${courseReference.course.resourceid }" />
					<input type="hidden" name="parentSyllabusId"
						value="${condition['syllabusId'] }" />
					<div class="pageFormContent" layoutH="35%">
						<table class="form">
							<tr>
								<td style="width: 12%">所属知识节点:</td>
								<td style="width: 88%" colspan="3"><select
									name="syllabusId" class="required">
										<c:forEach items="${syllabusList}" var="syb" varStatus="vs">
											<option value="${syb.resourceid }"
												<c:if test="${syb.resourceid eq courseReference.syllabus.resourceid}"> selected </c:if>>
												<c:forEach var="seconds" begin="1"
													end="${syb.syllabusLevel}" step="1">&nbsp;&nbsp;</c:forEach>
												${syb.syllabusName }
											</option>
										</c:forEach>
								</select> <span style="color: red;">*</span> <span style="color: green;">(课程的参考资料请加到根节点)</span>
								</td>
							</tr>
							<tr>
								<td>资料类型:</td>
								<td><select name="referenceType" class="required">
										<option value="">请选择</option>
										<option value="reference_doc"
											${(courseReference.referenceType eq 'reference_doc')?'selected':'' }>参考文献</option>
										<option value="reference_web"
											${(courseReference.referenceType eq 'reference_web')?'selected':'' }>参考网站</option>
								</select> <span style="color: red;">*</span></td>
							</tr>
							<tr>
								<td>资料链接:</td>
								<td>
									<table style="width: 100%;" border="0" id="referenceUrlTable1">
										<c:if test="${empty courseReference.resourceid }">
											<tr>
												<td colspan="5"><a class="button" href="javascript:;"
													onclick="addMateUrl1();"><span>新增</span></a></td>
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
			</div>
		</c:if>
		<script type="text/javascript">
		//增加链接
		function addMateUrl1(){
			var html = "<tr><td style='width: 10%;'>名称：</td><td style='width: 20%;'><input type='text' name='referenceName' style='width:90%;' value='' class='required'/></td><td style='width: 10%;'>链接：</td><td style='width: 50%;'><input type='text' name='url' value='' style='width: 90%;' class='required url'/></td><td><a class='button' href='javascript:;' onclick='delreferenceUrl1(this);'><span>删除</span></a></td></tr>";
			$("#referenceUrlTable1").append(html);
		}
		//删除链接项
		function delreferenceUrl1(obj){
			$(obj).parent().parent().remove();
		}
		
		function validateReferenceUrl1(form,getNewResult21){
			var $form = $(form);
			var isValidate = true;
			if (!$form.valid()) {
				alertMsg.error(DWZ.msg["validateFormError"]);
				isValidate = false;
			}
			$("#courseReferenceForm1 input[name='url']").each(function (){				
				if($.trim($(this).val())==""){	
					$(this).addClass("error");				
					isValidate = false;
				}
			});
			$("#courseReferenceForm1 input[name='referenceName']").each(function (){				
				if($.trim($(this).val())==""){	
					$(this).addClass("error");				
					isValidate = false;
				}
			});
			if(!isValidate){
				alertMsg.error(DWZ.msg["validateFormError"]);
				return false;
			}
			return validateCallback(form,myMateAjaxDone);
		}		
		 
	</script>
	</div>
</body>
</html>