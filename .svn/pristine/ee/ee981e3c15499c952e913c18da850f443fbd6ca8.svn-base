<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>试卷袋标签统计</title>
</head>
<body>
	<script type="text/javascript">
	//按教学站打印试卷袋标签
	function printExamPaperBag(){
		var examSubId 	  = "${condition['examSubId']}";
		var isMachineExam = $("#printPaperBagByBrschool_List_isMachineExam").val();
		var unitIds   = new Array();
		$("#examPaperBagPrintByBrSchoolListBody input[name='resourceid']:checked").each(function(){
			unitIds.push(jQuery(this).val());
		});
		if(unitIds.length==0){
			alertMsg.warn("请选择要打印的教学站！");
			return false;
		}
		alertMsg.confirm("确定打印所选教学站的试卷袋标签吗？",{
			okCall:function(){
				var url = "${baseUrl}/edu3/teaching/exam/paperbag/print-view.html?flag=printByBrschool&unitIds="+unitIds.toString()+"&examSubId="+examSubId+"&isMachineExam="+isMachineExam;
				//$.pdialog.minimize($.pdialog.getCurrent());
				$.pdialog.open(url,'RES_TEACHING_EXAM_PAPERBAG_PRINTBYBRSCHOOL_PRINT','打印预览',{height:600, width:800,mask:true});
			}
		});
	}
	
</script>
	<div class="page">
		<div class="pageHeader">
			<form id="examPaperBagPrintBySchoolSearchForm"
				onsubmit="return dialogSearch(this);"
				action="${baseUrl}/edu3/teaching/exam/paperbag/print-byschool.html?examSubId=${condition['examSubId']}"
				method="post">
				<div class="searchBar">
					<ul class="searchContent">
						<li><label>教学站：</label> <gh:brSchoolAutocomplete
								name="branchSchool" tabindex="1"
								id="printPaperBagByBrschool_List_brSchoolId" displayType="code"
								defaultValue="${condition['branchSchool']}" style="width:55%" />
						</li>
						<li><label>考试形式：</label> <select name="isMachineExam"
							id="printPaperBagByBrschool_List_isMachineExam"
							style="width: 55%">
								<option value="">请选择</option>
								<option value="Y"
									<c:if test="${condition['isMachineExam'] eq 'Y'}"> selected="selected" </c:if>>机考</option>
								<option value="N"
									<c:if test="${condition['isMachineExam'] eq 'N'}"> selected="selected" </c:if>>笔试</option>
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
		<gh:resAuth parentCode="RES_TEACHING_EXAM_PAPER_LIST"
			pageType="subList"></gh:resAuth>
		<div class="pageContent">
			<table class="table" layouth="138" width="100%">
				<thead>
					<tr>
						<th width="5%"><input type="checkbox" name="checkall"
							id="check_all_examPaperBag_print_byBrschool"
							onclick="checkboxAll('#check_all_examPaperBag_print_byBrschool','resourceid','#examPaperBagPrintByBrSchoolListBody')" /></th>
						<th width="65%">教学站</th>
						<th width="10%">预约人数</th>
						<th width="10%">试卷份数</th>
						<th width="10%">包数</th>
					</tr>
				</thead>
				<tbody id="examPaperBagPrintByBrSchoolListBody">
					<c:forEach items="${page.result}" var="examPaperBagPrintUnit"
						varStatus="vs">
						<tr>
							<td><input type="checkbox" name="resourceid"
								value="${examPaperBagPrintUnit.RESOURCEID}" autocomplete="off" /></td>
							<td>${examPaperBagPrintUnit.UNITNAME}</td>
							<td>${examPaperBagPrintUnit.ORDERNUM }</td>
							<td>${examPaperBagPrintUnit.PAPERNUM }</td>
							<td>${examPaperBagPrintUnit.BAGNUM }</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<gh:page page="${page}"
				goPageUrl="${baseUrl }/edu3/teaching/exam/paperbag/print-byschool.html?examSubId=${condition['examSubId']}"
				pageType="sys" targetType="dialog" condition="${condition}" />
		</div>
	</div>

</body>
</html>