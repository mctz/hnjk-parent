<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>试卷袋标签明细</title>
</head>
<body>

	<div class="page">
		<div class="pageContent">
			<table class="table" layouth="40" width="100%">
				<thead>
					<tr>
						<th width="25%">教学站</th>
						<th width="25%">课程名称</th>
						<th width="10%">该点包数情况</th>
						<th width="10%">每包份数</th>
						<th width="10%">占课程总包数情况</th>
						<th width="10%">考场</th>
						<th width="10%">操作</th>
					</tr>
				</thead>
				<tbody id="examPaperBagDetailsListBody">
					<c:forEach items="${bag.examPaperBags}" var="examPaperBagDetails"
						varStatus="vs">
						<tr>
							<td>${examPaperBagDetails.examPaperBag.unit.unitName }</td>
							<td>${examPaperBagDetails.examPaperBag.examInfo.course.courseName }</td>
							<td>${examPaperBagDetails.bagIndex}/${examPaperBagDetails.examPaperBag.bagNum }</td>
							<td>${examPaperBagDetails.paperNum}</td>
							<td>${examPaperBagDetails.totalBagIndex}/${examPaperBagDetails.totalBagNum }
							</td>
							<td>${examPaperBag.examRoom.examRoomName }</td>
							<td><a
								href="${baseUrl }/edu3/teaching/exam/paperbag/print-view.html?flag=printBySingleId&examPaperBagDetailsId=${examPaperBagDetails.resourceid }&examSubId=${examPaperBagDetails.examPaperBag.examInfo.examSub.resourceid}"
								target="dialog" width="800" height="600" title="打印标签">打印</a></td>
						</tr>
					</c:forEach>
					<tr>
						<td colspan="7">总体统计：该点该课程考试供 <font color="red">${bag.bagNum }</font>包，总份数
							<font color="red">${bag.paperNum }</font>份。
						</td>
					</tr>
				</tbody>
			</table>
		</div>
	</div>
	<script type="text/javascript">

	//生成试卷袋标签
	function genExamPaper(){
		var examSubId = $("#examPaperBag_List_examSub").val();
		var url = "${baseUrl}/edu3/teaching/exam/paperbag/edit.html?examSubId="+examSubId;
		if(""==examSubId){
			alertMsg.warn("请选择一个考试批次!");
			return false;
		}
		$.pdialog.open(url,"RES_TEACHING_EXAM_PAPER_GEN","生成试卷袋标签", {width:800, height:600});
	}
	//查看试卷袋标签明细
	function showExamPaperBagDetails(examPaperBagId){
		var url = "${baseUrl}/edu3/teaching/exam/paperbagdetails/list.html?examPaperBagId="+examPaperBagId;
		$.pdialog.open(url,"RES_TEACHING_EXAM_PAPER_DETAILS","查看试卷袋标签明细", {width:800, height:600});
	}
</script>
</body>
</html>