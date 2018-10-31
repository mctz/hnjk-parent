<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>网成班毕业论文成绩录入</title>
<script type="text/javascript">
	function saveNetsideStudyThesisExamResult(){
		$("#netsideStudyThesisExamResultsBody input").removeClass('error');
		var $form = $("#netsideStudyThesisExamResults_form");
		if(!isChecked('resourceid',"#netsideStudyThesisExamResultsBody")){
			alertMsg.warn('请选择一条要操作记录！');
			return false;
		}
		var isValid = true;
		var type = 1;
		$("#netsideStudyThesisExamResultsBody input[@name='resourceid']:checked").each(function(){
			/*
			var obj1 = $("#netsidestudy_thesis_firstScore"+$(this).val());				
			if(obj1.size()>0){
				var r1 = $.trim(obj1.val());
				if(r1==""||!r1.isNumber() || parseFloat(r1)>100 || parseFloat(r1)<0){
		        	isValid = false;
		        	obj1.addClass('error');
		        	return false;
		        }
			}
			var obj2 = $("#netsidestudy_thesis_secondScore"+$(this).val());		
			if(obj2.size()>0){
				var r2 = $.trim(obj2.val());
				if(r2==""||!r2.isNumber()||parseFloat(r2)>100 || parseFloat(r2)<0){
		        	isValid = false;
		        	obj2.addClass('error');
		        	return false;
		        }
			} */
			var obj3 = $("#netsidestudy_thesis_integratedScore"+$(this).val());
			if(obj3.size()>0){
				if(obj3.val()==""){
		        	isValid = false;
		        	type = 3;
		        	obj3.addClass('error');
		        	return false;
		        }
			}
	    });
	    if(!isValid){
	    	alertMsg.warn(type!=3?"请输入0-100的成绩数值":"终评成绩不能为空");
	    	return false;
	    } else {
	    	$.ajax({
				type:'POST',
				url:$form.attr("action"),
				data:$form.serializeArray(),
				dataType:"json",
				cache: false,
				success: function (json){
					DWZ.ajaxDone(json);
					if (json.statusCode == 200){
						var pageNum = "${thesisExamResultsPage.pageNum}" || "0";
						var pageSize = "${thesisExamResultsPage.pageSize}" || "20";
						navTabPageBreak({pageSize:pageSize,pageNum:pageNum});
					}
				},
				
				error: DWZ.ajaxError
			});    		
	    }
	    return false;
	}
	function _netsideStudyThesisExamResults_navTabSearch(form){
		if($("#netsideStudyThesisExamResults_batchId").val()==""){
			alertMsg.warn("请选择一个论文批次进行查询");
			return false;
		}
		if($("#netsideStudy_thiesis_graduateexamresults_branchSchool") && $("#netsideStudy_thiesis_graduateexamresults_branchSchool").val()==""){
			alertMsg.warn("请选择教学站");
			return false;
		}
		return navTabSearch(form);
	}
</script>
</head>
<body>
	<div class="page">
		<div class="pageHeader">
			<form
				onsubmit="return _netsideStudyThesisExamResults_navTabSearch(this);"
				action="${baseUrl }/edu3/teaching/thesisexamresults/netsidestudy/list.html"
				method="post">
				<div class="searchBar">
					<ul class="searchContent">
						<li><label>论文批次：</label> <gh:selectModel
								id="netsideStudyThesisExamResults_batchId" name="batchId"
								bindValue="resourceid" displayValue="batchName"
								style="width:55%"
								modelClass="com.hnjk.edu.teaching.model.ExamSub"
								value="${condition['batchId']}" condition="batchType='thesis'"
								orderBy="yearInfo.firstYear desc,term desc" /> <span
							style="color: red;">*</span></li>
						<c:if test="${condition['isBrSchool'] ne 'Y' }">
							<li><label>教学站：</label> <gh:brSchoolAutocomplete
									name="branchSchool" tabindex="1"
									id="netsideStudy_thiesis_graduateexamresults_branchSchool"
									defaultValue="${condition['branchSchool']}" displayType="code"
									style="width:120px" /> <span style="color: red;">*</span></li>
						</c:if>
					</ul>
					<ul class="searchContent">
						<li><label>年级：</label>
						<gh:selectModel name="grade"
								id="netsideStudyThesisExamResults__gradeId"
								bindValue="resourceid" displayValue="gradeName"
								modelClass="com.hnjk.edu.basedata.model.Grade"
								value="${condition['grade']}" orderBy="gradeName desc"
								style="width:115px;" /></li>
						<li><label>专业：</label>
						<gh:selectModel name="major"
								id="netsideStudyThesisExamResults_majorId"
								bindValue="resourceid" displayValue="majorCodeName"
								modelClass="com.hnjk.edu.basedata.model.Major"
								value="${condition['major']}" orderBy="majorCode"
								style="width:120px;" /></li>
						<li><label>层次：</label>
						<gh:selectModel name="classic"
								id="netsideStudyThesisExamResults_classicId"
								bindValue="resourceid" displayValue="classicName"
								modelClass="com.hnjk.edu.basedata.model.Classic"
								value="${condition['classic']}" style="width:115px;" /></li>
					</ul>
					<ul class="searchContent">
						<li><label>姓名：</label><input type="text" name="name"
							value="${condition['name']}" style="width: 115px" /></li>
						<li><label>学号：</label><input type="text" name="stuStudyNo"
							value="${condition['stuStudyNo']}" style="width: 115px" /></li>
						<li><label>录入状态：</label> <select name="status"
							style="width: 115px;">
								<option value="0"
									<c:if test="${condition['status'] eq '0' }">selected="selected"</c:if>>未录入</option>
								<option value="1"
									<c:if test="${condition['status'] eq '1' }">selected="selected"</c:if>>已录入</option>
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
			<gh:resAuth parentCode="RES_TEACHING_THESIS_NETSIDESTUDY_EXAMRESULT"
				pageType="list"></gh:resAuth>
			<form id="netsideStudyThesisExamResults_form"
				action="${baseUrl }/edu3/teaching/thesisexamresults/netsidestudy/save.html"
				method="post">
				<input type="hidden" name="batchId" value="${thesisSub.resourceid }" />
				<table class="table" layouth="184">
					<thead>
						<tr>
							<th width="5%"><input type="checkbox" name="checkall"
								id="check_all_netsideStudyThesisExamResults"
								onclick="checkboxAll('#check_all_netsideStudyThesisExamResults','resourceid','#netsideStudyThesisExamResultsBody')" /></th>
							<th width="10%">论文批次</th>
							<th width="13%">教学站</th>
							<th width="12%">专业</th>
							<th width="8%">层次</th>
							<th width="10%">学号</th>
							<th width="10%">姓名</th>
							<%-- 
		            <th width="8%">初评成绩</th>                
		            <th width="8%">答辩成绩</th>  --%>
							<th width="8%">终评成绩</th>
							<th width="8%">审核人</th>
							<th width="8%">审核时间</th>
							<th width="8%">审核状态</th>
						</tr>
					</thead>
					<tbody id="netsideStudyThesisExamResultsBody">
						<c:forEach items="${thesisExamResultsPage.result}" var="vo"
							varStatus="vs">
							<tr>
								<td><input type="hidden" name="status${vo.studentId }"
									value="${empty vo.checkStatus ?'0':'1'}" /> <c:choose>
										<c:when test="${vo.checkStatus eq '4' }">
											<input type="hidden" value="${vo.studentId }" />
										</c:when>
										<c:otherwise>
											<input type="checkbox" name="resourceid"
												value="${vo.studentId }" checkStatus="${vo.checkStatus}"
												autocomplete="off" />
										</c:otherwise>
									</c:choose></td>
								<td title="${thesisSub.batchName }">${thesisSub.batchName }</td>
								<td title="${vo.branchSchool }">${vo.branchSchool }</td>
								<td title="${vo.majorName }">${vo.majorName }</td>
								<td title="${vo.classicName }">${vo.classicName }</td>
								<td title="${vo.stuStudyNo }">${vo.stuStudyNo }</td>
								<td title="${vo.name }">${vo.name }</td>
								<c:choose>
									<c:when test="${vo.checkStatus eq '4' }">
										<%-- 		            	
				            <td><fmt:formatNumber value='${vo.firstScore }' pattern='###.#' /></td>
				            <td><fmt:formatNumber value='${vo.secondScore }' pattern='###.#' /></td>  --%>
										<td>${ghfn:dictCode2Val('CodeScoreChar',vo.integratedScore) }</td>
									</c:when>
									<c:otherwise>
										<%-- 			            	
			            	<td><input type="text" id="netsidestudy_thesis_firstScore${vo.studentId }" name="firstScore${vo.studentId }" value="<fmt:formatNumber value='${vo.firstScore }' pattern='###.#' />" size="5"/></td>
			            	<td>
				            	<c:choose>
				            		<c:when test="${empty vo.secondScore }"><input type="text" id="netsidestudy_thesis_secondScore${vo.studentId }" name="secondScore${vo.studentId }" value="0" size="5"/></c:when>
				            		<c:otherwise><input type="text" id="netsidestudy_thesis_secondScore${vo.studentId }" name="secondScore${vo.studentId }" value="<fmt:formatNumber value='${vo.secondScore }' pattern='###.#' />" size="5"/></c:otherwise>
				            	</c:choose>
				            </td> --%>
										<td><select name="integratedScore${vo.studentId }"
											id="netsidestudy_thesis_integratedScore${vo.studentId }"
											style="width: 90%;">
												<option value="">请选择</option>
												<option value="2515"
													<c:if test="${vo.integratedScore eq '2515' }">selected="selected"</c:if>>优</option>
												<option value="2514"
													<c:if test="${vo.integratedScore eq '2514' }">selected="selected"</c:if>>良</option>
												<option value="2513"
													<c:if test="${vo.integratedScore eq '2513' }">selected="selected"</c:if>>中</option>
												<option value="2512"
													<c:if test="${vo.integratedScore eq '2512' }">selected="selected"</c:if>>及格</option>
												<option value="2501"
													<c:if test="${vo.integratedScore eq '2501' }">selected="selected"</c:if>>不及格</option>
										</select></td>
									</c:otherwise>
								</c:choose>
								<td title="${vo.auditMan }">${vo.auditMan }</td>
								<td><fmt:formatDate value="${vo.auditDate }"
										pattern="yyyy-MM-dd HH:mm:ss" /></td>
								<td><c:choose>
										<c:when test="${vo.checkStatus eq '4' }">通过发布</c:when>
										<c:otherwise>未审核</c:otherwise>
									</c:choose></td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</form>
			<gh:page page="${thesisExamResultsPage}"
				goPageUrl="${baseUrl }/edu3/teaching/thesisexamresults/netsidestudy/list.html"
				pageType="sys" condition="${condition}" />
		</div>
	</div>
</body>
</html>