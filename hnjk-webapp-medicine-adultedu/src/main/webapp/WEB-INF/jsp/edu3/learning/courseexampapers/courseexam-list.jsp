<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>题库试题选择</title>
<script type="text/javascript">
	//添加试题
	function savePapersCourseExam(){
		if(!isChecked('resourceid','#courseexampapers_courseExamBody')){
 			alertMsg.warn('请选择一条要操作记录！');
			return false;
	 	}		
		var url = "${baseUrl}/edu3/metares/courseexampapers/courseexam/save.html?paperId=${courseExamPaper.resourceid}";
		
		var isSend = true;
		$("#courseexampapers_courseexam_div input").each(function (){
			if($.trim(this.value).isNumber()){
				url += "&" + this.name + "=" + this.value;
			} else {
				isSend = false;
			}
		});		
		
		if(!isSend){
			alertMsg.warn("输入分数不合法,请重新输入!");
		} else {			
			alertMsg.confirm("您确定把这些试题加到试卷 <b>${courseExamPaper.paperName}</b> 下？", {
				okCall: function(){//执行			
					var res = "";
					var k = 0;
					var num  = $("#courseexampapers_courseExamBody input[name='resourceid']:checked").size();
					$("#courseexampapers_courseExamBody input[@name='resourceid']:checked").each(function(){
	                        res+=$(this).val();
	                        if(k != num -1 ) res += ",";
	                        k++;
	                    })	                
					$.post(url,{resourceid:res}, myDialogAjaxDone, "json");
				}
			});	
		}		
	}	
	//修改试卷试题
	function savePapersCourseExam1(){				
		if (!$("#PapersCourseExamForm1").valid()) {
			alertMsg.error("提交数据不合法，请改正后再提交!");
			return false;
		} else {
			$.ajax({
				type:'POST',
				url:$("#PapersCourseExamForm1").attr("action"),
				data:$("#PapersCourseExamForm1").serializeArray(),
				dataType:"json",
				cache: false,
				success: myDialogAjaxDone,
				
				error: DWZ.ajaxError
			});	
		}		
	}
	//删除试卷试题
	function removePapersCourseExam(){				
		if(!isChecked('resourceid','#courseexampapers_courseExamBody1')){
 			alertMsg.warn('请选择一条要操作记录！');
			return false;
	 	}		
		var url = "${baseUrl}/edu3/metares/courseexampapers/courseexam/remove.html?paperId=${courseExamPaper.resourceid}";
		alertMsg.confirm("您确定从 <b>${courseExamPaper.paperName}</b> 中删除这些试题？", {
				okCall: function(){//执行			
					var res = "";
					var k = 0;
					var num  = $("#courseexampapers_courseExamBody1 input[name='resourceid']:checked").size();
					$("#courseexampapers_courseExamBody1 input[@name='resourceid']:checked").each(function(){
	                        res+=$(this).val();
	                        if(k != num -1 ) res += ",";
	                        k++;
	                    });	               
					$.post(url,{resourceid:res}, myDialogAjaxDone, "json");
				}
		});		
	}
	function myDialogAjaxDone(json){
		DWZ.ajaxDone(json);
		if (json.statusCode == 200){
			if (json.reloadUrl){
				$.pdialog.reload(json.reloadUrl);
			}
		}
	}
	
	//function randomPapersCourseExam(){	
	//	var url = "${baseUrl}/edu3/framework/courseexampapers/random.html?paperId="+$("#dialog_courseexampapers_paperId").val();
	//	$.pdialog.open(url, "PapersCourseExamsSelector", "成卷条件", {width: 450, height: 350});
	//}	
</script>
</head>
<body>
	<div class="page">
		<div class="tabs" currentIndex="${condition['currentIndex'] }"
			eventtype="click">
			<div class="tabsHeader">
				<div class="tabsHeaderContent">
					<ul>
						<li><a href="javascript:void(0)"><span>可用试题</span></a></li>
						<li><a href="javascript:void(0)"><span>试卷试题</span></a></li>
					</ul>
				</div>
			</div>
			<div class="tabsContent" style="height: 100%;">
				<!-- 1 -->
				<div>
					<div class="pageHeader">
						<form onsubmit="return dialogSearch(this);"
							action="${baseUrl }/edu3/metares/courseexampapers/courseexam/list.html"
							method="post">
							<input id="dialog_courseexampapers_paperId" type="hidden"
								name="paperId" value="${courseExamPaper.resourceid }" />
							<div class="searchBar">
								<ul class="searchContent">
									<c:if test="${condition['isEnrolExam'] ne 'Y'}">
										<li><label>题型：</label>
										<gh:select name="examType" value="${condition['examType']}"
												dictionaryCode="CodeExamType" /></li>
									</c:if>
									<li><label>难易度：</label>
									<gh:select name="difficult" value="${condition['difficult']}"
											dictionaryCode="CodeExamDifficult" /></li>
									<li><label>关键字：</label><input type="text" name="keywords"
										value="${condition['keywords'] }" /></li>
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
					<div class="">
						<gh:resAuth parentCode="RES_METARES_COURSEEXAMPAPERS"
							pageType="saveexam"></gh:resAuth>
						<div id="courseexampapers_courseexam_div">
							设置默认分值： <label>单选题： </label><input name='score1' value='5'
								size="5" /> <label>多选题： </label><input name='score2' value='5'
								size="5" /> <label>判断题： </label><input name='score3' value='5'
								size="5" /> <label>材料题： </label><input name='score6' value='10'
								size="5" />
							<c:if test="${condition['isEnrolExam'] ne 'Y'}">
								<label>填空题： </label>
								<input name='score4' value='5' size="5" />
								<label>论述题： </label>
								<input name='score5' value='10' size="5" />
							</c:if>
						</div>
						<table class="table" layouth="188">
							<thead>
								<tr>
									<th width="10%"><input type="checkbox" name="checkall"
										id="check_all_courseexampapers_courseexam"
										onclick="checkboxAll('#check_all_courseexampapers_courseexam','resourceid','#courseexampapers_courseExamBody')" /></th>
									<th width="15%">课程</th>
									<th width="15%">题型</th>
									<th width="15%">类别</th>
									<th width="15%">难度</th>
									<th width="20%">关键字</th>
									<th width="10%">&nbsp;</th>
								</tr>
							</thead>
							<tbody id="courseexampapers_courseExamBody">
								<c:forEach items="${courseExamList.result }" var="exam"
									varStatus="vs">
									<tr>
										<td><input type="checkbox" name="resourceid"
											value="${exam.resourceid }" autocomplete="off"
											rel="${exam.examType}" /></td>
										<td><c:choose>
												<c:when test="${exam.isEnrolExam eq 'Y' }">${ghfn:dictCode2Val('CodeEntranceExam',exam.courseName)}</c:when>
												<c:otherwise>${exam.course.courseName }</c:otherwise>
											</c:choose></td>
										<td>${ghfn:dictCode2Val('CodeExamType',exam.examType)}</td>
										<td>${ghfn:dictCode2Val('CodeExamNodeType',exam.examNodeType)}</td>
										<td>${ghfn:dictCode2Val('CodeExamDifficult',exam.difficult)}</td>
										<td>${exam.keywords }</td>
										<td><c:choose>
												<c:when test="${exam.isEnrolExam eq 'Y' }">
													<a href="javascript:;"
														onclick="javascript:$.pdialog.open('${baseUrl }/edu3/metares/entrance/courseexam/view.html?courseExamId=${exam.resourceid }','selector','查看入学考试试题',{width:600,height:400});">查看</a>
												</c:when>
												<c:otherwise>
													<a href="javascript:;"
														onclick="javascript:$.pdialog.open('${baseUrl }/edu3/metares/courseexam/view.html?courseExamId=${exam.resourceid }','selector','查看试题',{width:600,height:400});">查看</a>
												</c:otherwise>
											</c:choose></td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
						<gh:page page="${courseExamList}"
							goPageUrl="${baseUrl}/edu3/metares/courseexampapers/courseexam/list.html"
							condition="${condition }" targetType="dialog" pageType="sys" />
					</div>
				</div>
				<!-- 2 -->
				<div>
					<div>
						<gh:resAuth parentCode="RES_METARES_COURSEEXAMPAPERS"
							pageType="sublist"></gh:resAuth>
						<form id="PapersCourseExamForm1" method="post"
							action="${baseUrl}/edu3/metares/courseexampapers/courseexam/save.html"
							class="pageForm">
							<input type="hidden" name="paperId"
								value="${courseExamPaper.resourceid }" /> <input type="hidden"
								name="from" value="save" />
							<table class="table" layouth="96">
								<thead>
									<tr>
										<th width="10%"><input type="checkbox" name="checkall"
											id="check_all_courseexampapers_courseexam1"
											onclick="checkboxAll('#check_all_courseexampapers_courseexam1','resourceid','#courseexampapers_courseExamBody1')" /></th>
										<th width="15%">序号</th>
										<th width="15%">分值</th>
										<th width="15%">课程</th>
										<th width="15%">题型</th>
										<th width="20%">关键字</th>
										<th width="10%">&nbsp;</th>
									</tr>
								</thead>
								<tbody id="courseexampapers_courseExamBody1">
									<c:forEach items="${courseExamPaper.courseExamPaperDetails }"
										var="detail" varStatus="vss">
										<tr>
											<td><c:if test="${not empty detail.courseExam.parent }">&nbsp;&nbsp;&nbsp;&nbsp;</c:if><input
												type="checkbox" name="resourceid"
												value="${detail.resourceid }" autocomplete="off" /><input
												type="hidden" name="paperExamId"
												value="${detail.resourceid }" /></td>
											<td><input type="text" name="showOrder" size="5"
												class="required digits" value="${detail.showOrder }" /></td>
											<td><input type="text" name="score" size="5"
												class="required number" value="${detail.score }" /></td>
											<td><c:choose>
													<c:when test="${detail.courseExam.isEnrolExam eq 'Y' }">${ghfn:dictCode2Val('CodeEntranceExam',detail.courseExam.courseName)}</c:when>
													<c:otherwise>${detail.courseExam.course.courseName }</c:otherwise>
												</c:choose></td>
											<td>${ghfn:dictCode2Val('CodeExamType',detail.courseExam.examType)}</td>
											<td>${detail.courseExam.keywords }</td>
											<td><c:choose>
													<c:when test="${detail.courseExam.isEnrolExam eq 'Y'}">
														<a href="javascript:;"
															onclick="javascript:$.pdialog.open('${baseUrl }/edu3/metares/entrance/courseexam/view.html?courseExamId=${detail.courseExam.resourceid }','selector','查看入学考试试题',{width:600,height:400});">查看</a>
													</c:when>
													<c:otherwise>
														<a href="javascript:;"
															onclick="javascript:$.pdialog.open('${baseUrl }/edu3/metares/courseexam/view.html?courseExamId=${detail.courseExam.resourceid }','selector','查看试题',{width:600,height:400});">查看</a>
													</c:otherwise>
												</c:choose></td>
										</tr>
									</c:forEach>
								</tbody>
							</table>
						</form>
					</div>
				</div>
			</div>
			<div class="tabsFooter">
				<div class="tabsFooterContent"></div>
			</div>
		</div>
	</div>
</body>
</html>