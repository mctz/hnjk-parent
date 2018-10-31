<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>在线录入成绩</title>
</head>
<body>
	<script type="text/javascript">

	$(document).ready(function(){
		var msg = "${condition['msg']}";
		if(""!=msg){
			$("#examResultsInputBody").hide();
			alertMsg.warn(msg);
		}
	});
	
	//保存选择的成绩 
	function inputSave(){
		var $form = $("#inputExamResultsSaveForm");
		if (!$form.valid()) {
			alertMsg.warn("请录入合法的分数值！");

			return false; 
		}
		var sNum=0;//卷面分58,59人数
		$form.find("input[id^='writtenScore_']:not([readonly])").each(function (){
			var score = $.trim($(this).val());
			if(score=='58'||score=='59') sNum++;
		});
		var exmsg = sNum==0?"":"有<b>"+sNum+"</b>人卷面分为58或59分.<br/>";
		alertMsg.confirm(exmsg+"确认要保存成绩记录吗？", {
               okCall: function(){
               	$.ajax({
					type:'POST',
					url:$form.attr("action"),
					data:$form.serializeArray(),
					dataType:"json",
					cache: false,
					error: DWZ.ajaxError,
					success: function(json){
						if(json.statusCode == 300){
							alertMsg.warn(json.message);
					    }else{
							//navTab.reload($("#inputExamResultsSearchForm").attr("action"), $("#inputExamResultsSearchForm").serializeArray());
							var pageNum = "${page.pageNum}";
							if(pageNum==""){
								pageNum = "1";
							}
							navTabPageBreak({pageNum:pageNum});
						}
					}
				});
               }
       });   
	}
	
	//全选成绩
	function enableAllResults(obj){
		var isAbnormityInput = "${condition['isAbnormityInput']}";
		if(obj.checked){
			$("#examResultsInputBody input[name='resourceid']").each(function(obj){
				var boxObj = $(this);
				var examAb = $(this).attr("examAbnormity");
				boxObj.checked = true;
				enableThisResults(boxObj);
			});
		}else{
			$("#examResultsInputBody input[name='resourceid']").each(function(obj){
				var boxObj     = $(this);
				boxObj.checked = false;
				enableThisResults(boxObj);
			});
		}
	}
	//勾选成绩
	function enableThisResults(obj){
		var isAbnormityInput = "${condition['isAbnormityInput']}";
		var examType 		 = "${condition['examType']}";
		var isMachineExam    = "${condition['examInfo'].isMachineExam}";

		if(obj.checked ){
			if("Y"==isAbnormityInput || "Y"!=isAbnormityInput && examType!='0' && examType!='1'){
				$(obj).parent().parent().parent().find("select[name='examAbnormity']").attr("disabled","");
			}
			if("Y"!=isAbnormityInput){
				if("Y"!=isMachineExam){
					$(obj).parent().parent().parent().find("input[name='writtenScore']").attr("disabled","");
				}
				
				
				$(obj).parent().parent().parent().find("input[name='usuallyScore']").attr("disabled","");
				$(obj).parent().parent().parent().find("input[name='integratedScore']").attr("disabled","");
				$(obj).parent().parent().parent().find("input[name='writtenHandworkScore']").attr("disabled","");
			}
		}else{
			$(obj).parent().parent().parent().find("select[name='examAbnormity']").attr("disabled","disabled");
			$(obj).parent().parent().parent().find("input[name='writtenScore']").attr("disabled","disabled");
			$(obj).parent().parent().parent().find("input[name='usuallyScore']").attr("disabled","disabled");
			$(obj).parent().parent().parent().find("input[name='integratedScore']").attr("disabled","disabled");
			$(obj).parent().parent().parent().find("input[name='writtenHandworkScore']").attr("disabled","disabled");
		}
		
	}
	//设置异常成绩状态
	function abnormityStatusConfing(){
		var examSubId = "${condition['examSubId'] }";
		var url = "${baseUrl}/edu3/teaching/result/abnormity-config-list.html?examSubId="+examSubId;
		$.pdialog.open(url,"RES_TEACHING_RESULT_MANAGE_ABNORMITYSTATUSCONFIG_TRANSCRIPTS","设置异常成绩状态", {width:800, height:600});	
	}
	//当选择成绩状态异常时禁用成绩输入框
	function setInputStatus(obj){
		if($(obj).val()!="0"){
			$(obj).parent().parent().parent().find("input").attr("readonly","readonly");
		}else{
			$(obj).parent().parent().parent().find("input").attr("readonly","");
		}
	}
	function ajaxGetIntegratedScore(resourceid){
		var ws  = $("#writtenScore_"+resourceid).val();
		var whs = $("#writtenHandworkScore_"+resourceid).val();
		var uss = $("#usuallyScore_"+resourceid).val();
		if(undefined ==ws ){
			ws=0;
		}
		if(undefined ==whs ){
			whs=0;
		}
		if(undefined ==uss ){
			uss=0;
		}
		$.ajax({
			type:'POST',
			url:"${baseUrl}/edu3/teaching/result/caculateIntegratedScore.html",
			data:{ws:ws,whs:whs,uss:uss,resourceid:resourceid},
			dataType:"json",
			cache: false,
			error: DWZ.ajaxError,
			success: function(data){
				if(data['result'] == 300){
					if(undefined!=data['msg']){
						alertMsg.warn(data['msg']);
					}
			    }else{
			    	if(undefined!=data['highest']){
						$("#writtenHandworkScore_"+resourceid).val(data['highest']);
					}
			    	if(undefined!=data['highest2']){
						$("#writtenScore_"+resourceid).val(data['highest2']);
					}
			    	if(undefined!=data['msg']){
						alertMsg.warn(data['msg']);
					}
					$("#integratedScore_"+resourceid).val(data['integratedScore']);
					$("#integratedScore_"+resourceid+"td").html(data['integratedScore']);
					
				}
			}
		});
		
	}
</script>
	<div class="page">

		<div class="pageHeader">
			<form id="inputExamResultsSearchForm"
				onsubmit="return navTabSearch(this);"
				action="${baseUrl}/edu3/teaching/result/input-examresults-list.html"
				method="post">
				<input id="inputExamResultsSearchForm_examSubId" type="hidden"
					name="examSubId" value="${condition['examSubId'] }" /> <input
					id="inputExamResultsSearchForm_examInfoId" type="hidden"
					name="examInfoId" value="${condition['examInfoId'] }" /> <input
					id="inputExamResultsSearchForm_isReadOnly" type="hidden"
					name="isReadOnly" value="${condition['isReadOnly'] }" />
				<div class="searchBar">
					<ul class="searchContent">
						<li><label>教学站：</label> <c:choose>
								<c:when test="${not empty condition['isBranchSchool']}">
								${condition['branchSchoolName']}
								<input type="hidden" name="branchSchool"
										id="inputExamResults_brSchoolName"
										value="${condition['branchSchool']}" />
								</c:when>
								<c:otherwise>
									<gh:brSchoolAutocomplete name="branchSchool" tabindex="1"
										id="inputExamResults_brSchoolName"
										defaultValue="${condition['branchSchool']}" displayType="code"
										style="width:55%" />
								</c:otherwise>
							</c:choose></li>
						<li><label>试&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;室：</label><input
							type="text" name="examRoom" value="${condition['examRoom']}"
							style="width: 55%" /></li>
						<li><label>座&nbsp;&nbsp;&nbsp;位&nbsp;&nbsp;&nbsp;号：</label><input
							type="text" name="examSeatNum"
							value="${condition['examSeatNum']}" style="width: 55%" /></li>
					</ul>
					<ul class="searchContent">
						<li><label>姓&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;名：</label><input
							type="text" name="stuName" value="${condition['stuName']}"
							style="width: 55%" /></li>
						<li><label>学&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;号：</label><input
							type="text" name="stuNo" value="${condition['stuNo']}"
							style="width: 55%" /></li>
						<li><label>录入状态：</label> <select name="checkStatus"
							style="width: 55%">
								<option value="">请选择</option>
								<option value="-1"
									<c:if test="${condition['checkStatus'] eq '-1'}"> selected="selected"</c:if>>未录入</option>
								<option value="0"
									<c:if test="${condition['checkStatus'] eq '0'}"> selected="selected"</c:if>>保存</option>
								<option value="1"
									<c:if test="${condition['checkStatus'] eq '1'}"> selected="selected"</c:if>>提交</option>
								<option value="4"
									<c:if test="${condition['checkStatus'] eq '4'}"> selected="selected"</c:if>>已发布</option>
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
		<c:if test="${condition['isReadOnly'] ne '1'}">
			<gh:resAuth parentCode="RES_TEACHING_RESULT_NETWORKSTUDY_INPUT_LIST"
				pageType="netWorkStudySub"></gh:resAuth>
		</c:if>
		<div class="pageContent">

			<form id="inputExamResultsSaveForm"
				action="${baseUrl}/edu3/teaching/result/input-examresults-save.html"
				method="post">
				<input id="inputExamResultsSaveForm_examSubId" type="hidden"
					name="examSubId" value="${condition['examSubId'] }" /> <input
					id="inputExamResultsSaveForm_examInfoId" type="hidden"
					name="examInfoId" value="${condition['examInfoId'] }" />

				<table class="table" layouth="138" width="100%">
					<thead>
						<tr>
							<c:choose>
								<%--期末混合机考增加 笔考成绩 列--%>
								<c:when test="${condition['examInfo'].ismixture eq 'Y' }">
									<th width="10%">课程名称</th>
									<th width="14%">教学站</th>
									<th width="10%">专业</th>
									<th width="10%">学号</th>
									<th width="4%">试室</th>
									<th width="4%">座位号</th>
									<th width="8%">姓名</th>
									<th width="6%">选考次数</th>
									<th width="6%">平时成绩</th>
									<th width="7%">机考成绩</th>
									<th width="6%">笔考成绩</th>
									<th width="6%">综合成绩</th>
									<th width="6%">成绩异常</th>
									<th width="6%">录入状态</th>
								</c:when>
								<c:otherwise>
									<th width="10%">课程名称</th>
									<th width="14%">教学站</th>
									<th width="10%">专业</th>
									<th width="10%">学号</th>
									<th width="4%">试室</th>
									<th width="4%">座位号</th>
									<th width="8%">姓名</th>
									<th width="6%">选考次数</th>
									<th width="6%">平时成绩</th>
									<th width="6%">卷面成绩</th>
									<th width="6%">综合成绩</th>
									<th width="6%">成绩异常</th>
									<th width="6%">录入状态</th>
								</c:otherwise>
							</c:choose>
						</tr>
					</thead>
					<tbody id="examResultsInputBody">
						<c:if
							test="${fn:length(page.result)<=0 and '1' ne condition['isReadOnly'] and empty condition['examRoom'] and empty condition['examSeatNum'] }">
							<tr>
								<td colspan="9" style="color: red">所选条件下录入状态为待审核、审核通过或发布。</td>
							</tr>
						</c:if>
						<%--
			    	 判断是否允许录入卷面成绩
			   <c:set var="isAllowInputWscore" value="N"></c:set>
			   <c:if  test="${condition['isAbnormityInput'] ne 'Y' and condition['examInfo'].isMachineExam ne 'Y' }"> 
			   		<c:set var="isAllowInputWscore" value="Y"></c:set>
			   </c:if>
			   
			   		 判断是否允许录入成绩异常 
			   <c:set var="isAllowInputEscore" value="N"></c:set>
			   <c:if  test="${condition['isAbnormityInput'] eq 'Y' or (condition['isAbnormityInput'] ne 'Y' and condition['examType'] ne '0' and condition['examType'] ne '1') }"> 
			   		<c:set var="isAllowInputEscore" value="Y"></c:set>
			   </c:if>
			    --%>

						<c:forEach items="${page.result}" var="examResults" varStatus="vs">

							<c:choose>
								<%-- 已提交成绩或者开闭卷且成绩异常不为0的成绩记录不能修改 --%>
								<c:when
									test="${ examResults.checkStatus>0 || condition['isReadOnly'] eq '1' }">
									<tr style="background-color: #e4f5ff">
										<td>${examResults.course.courseName }</td>
										<td>${examResults.studentInfo.branchSchool.unitCode }-${examResults.studentInfo.branchSchool.unitShortName }</td>
										<td>${examResults.studentInfo.major.majorName}</td>
										<td>${examResults.studentInfo.studyNo}</td>
										<td>${examResults.examroom.examroomName}</td>
										<td>${examResults.examSeatNum}</td>
										<td>${examResults.studentInfo.studentName}</td>
										<td><c:choose>
												<c:when test="${not empty examResults.examCount}">${ examResults.examCount}</c:when>
												<c:otherwise>0</c:otherwise>
											</c:choose></td>
										<td>${examResults.usuallyScore}</td>
										<td><c:choose>
												<c:when test="${condition['examInfo'].ismixture eq 'Y' }">${examResults.writtenMachineScore}</c:when>
												<c:otherwise>${examResults.writtenScore}</c:otherwise>
											</c:choose></td>
										<c:if test="${condition['examInfo'].ismixture eq 'Y' }">
											<td>${examResults.writtenHandworkScore}</td>
										</c:if>
										<td>${examResults.integratedScore}</td>
										<td><gh:select name="examAbnormity"
												dictionaryCode="CodeExamAbnormity" choose="N"
												value="${examResults.examAbnormity}" disabled="disabled" /></td>
										<td style="color: green">${ghfn:dictCode2Val('CodeExamResultCheckStatus',examResults.checkStatus)}</td>
									</tr>
								</c:when>
								<c:otherwise>
									<tr>
										<td>${examResults.course.courseName }<input type="hidden"
											" name="resourceid" value="${examResults.resourceid }" />
										</td>
										<td>${examResults.studentInfo.branchSchool.unitCode }-${examResults.studentInfo.branchSchool.unitShortName }</td>
										<td>${examResults.studentInfo.major.majorName}</td>
										<td>${examResults.studentInfo.studyNo}</td>
										<td>${examResults.examroom.examroomName}</td>
										<td>${examResults.examSeatNum}</td>
										<td>${examResults.studentInfo.studentName}</td>
										<td><c:choose>
												<c:when test="${not empty examResults.examCount}">${ examResults.examCount}</c:when>
												<c:otherwise>0</c:otherwise>
											</c:choose></td>
										<td><input type="hidden"
											id="usuallyScore_${examResults.resourceid}"
											value="${examResults.usuallyScore}" />
											${examResults.usuallyScore}</td>
										<td><c:choose>
												<%--考试课程是机考课程成绩不允许修改  --%>
												<%--1.不是机考也不是混合机考 显示卷面成绩 --%>
												<c:when
													test="${condition['examInfo'].isMachineExam ne 'Y' and condition['examInfo'].ismixture ne 'Y' }">
													<input onkeyup="_writtenScoreNext(this,event);"
														id="writtenScore_${examResults.resourceid }" type="text"
														name="writtenScore"
														onchange="ajaxGetIntegratedScore('${examResults.resourceid}')"
														align="middle" class="number" style="width: 30px"
														value="${examResults.writtenScore}"
														<c:if test="${examResults.examAbnormity ne '0'}"> readonly="readonly" </c:if> />
												</c:when>
												<%--2.是机考也是混合机考  --%>
												<c:when
													test="${condition['examInfo'].isMachineExam eq 'Y' and condition['examInfo'].ismixture eq 'Y' }">
													<input type="hidden"
														id="writtenScore_${examResults.resourceid }"
														value="${examResults.writtenMachineScore}" />
					            			${examResults.writtenMachineScore}	
					            		</c:when>
												<%--3.是机考不是混合机考  --%>
												<c:otherwise>
													<input type="hidden"
														id="writtenScore_${examResults.resourceid }"
														value="${examResults.writtenScore}" />
					            			${examResults.writtenScore}
					            		</c:otherwise>
											</c:choose> <input id="integratedScore_${examResults.resourceid }"
											type="hidden" name="integratedScore" align="middle"
											class="number" style="width: 30px"
											value="${examResults.integratedScore}" /></td>
										<c:if test="${condition['examInfo'].ismixture eq 'Y' }">
											<td><input
												id="writtenHandworkScore_${examResults.resourceid }"
												type="text" name="writtenHandworkScore"
												onchange="ajaxGetIntegratedScore('${examResults.resourceid  }')"
												align="middle" class="number" style="width: 30px"
												value="${examResults.writtenHandworkScore}"
												<c:if test="${examResults.examAbnormity ne '0'}"> readonly="readonly" </c:if> />

											</td>
										</c:if>
										<td id="integratedScore_${examResults.resourceid}td">${examResults.integratedScore}</td>
										<td><gh:select name="examAbnormity"
												dictionaryCode="CodeExamAbnormity" choose="N"
												value="${examResults.examAbnormity}"
												onchange="setInputStatus(this);" /></td>

										<td
											<c:if test="${examResults.checkStatus > -1 }"> style="color:blue" </c:if>>
											${ghfn:dictCode2Val('CodeExamResultCheckStatus',examResults.checkStatus)}</td>
									</tr>
								</c:otherwise>
							</c:choose>

						</c:forEach>

					</tbody>
				</table>
				<!--防止剩下单文本框时，回车就提交了表单  -->
				<div style="display: none;">
					<input type="text" id="empty" />
				</div>
			</form>
			<c:choose>
				<c:when test="${condition['isReadOnly'] ne '1'}">
					<gh:page page="${page}" targetType="navTab"
						goPageUrl="${baseUrl }/edu3/teaching/result/input-examresults-list.html"
						pageType="sys" beforeForm="inputExamResultsSaveForm"
						postBeforeForm="${condition['isAllowInput']}"
						condition="${condition }" />
				</c:when>
				<c:otherwise>
					<gh:page page="${page}" targetType="navTab"
						goPageUrl="${baseUrl }/edu3/teaching/result/input-examresults-list.html"
						pageType="sys" postBeforeForm="${condition['isAllowInput']}"
						condition="${condition }" />
				</c:otherwise>
			</c:choose>

		</div>
	</div>
	<script type="text/javascript">
function _writtenScoreNext(obj,event){
	if(event.keyCode == 13) {//按ENTER键
		var ws = $("input[id^='writtenScore_']");
		var index = ws.index($(obj))+1;
		if(index<ws.length){
			ws.get(index).focus();
		}
		if(index == ws.length){
			ws.get(index-1).focus();
		}
	}
}
</script>
</body>
</html>