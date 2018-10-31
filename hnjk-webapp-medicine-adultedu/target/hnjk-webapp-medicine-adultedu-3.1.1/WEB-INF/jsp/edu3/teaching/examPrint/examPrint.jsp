<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>考试打印</title>


</head>
<body>
	<div class="page">
		<div class="pageHeader">
			<form id="_examPrintSearchForm" onsubmit="return navTabSearch(this);"
				action="${baseUrl}/edu3/teaching/exam/seat/examRoomSeatList.html"
				method="post">
				<div id="examPrint" class="searchBar">
					<ul class="searchContent">
						<li><label>预约批次：</label> <gh:selectModel
								id="_examPrint_examSub" name="examSub" bindValue="resourceid"
								displayValue="batchName" style="width: 55%"
								modelClass="com.hnjk.edu.teaching.model.ExamSub"
								condition="batchType='exam',examsubStatus='2'" /></li>
					</ul>
				</div>
			</form>
		</div>
		<div class="pageContent" layoutH="138">
			<gh:resAuth parentCode="RES_TEACHING_EXAM_PRINT" pageType="list"></gh:resAuth>
		</div>
	</div>
	<script type="text/javascript">

	//打印本部试卷交接表
	function partTransFerTablePrint(){
		var examSubId = $("#_examPrint_examSub").val();
		if(""==examSubId){
			alertMsg.warn("请选择一个考试批次！");
			return false;
		}
		var url = "${baseUrl}/edu3/teaching/exam/print/transferTable-view.html?flag=partTransferTable&examSubId="+examSubId+"&unitIds=''";
		$.pdialog.open(url,'RES_TEACHING_EXAM_PRINT_PARTTRANSFERTABLE','打印预览',{height:600, width:800,mask:true})
	}
	//打印巡考员试卷交接表
	function examinerTransFerTablePrint(){
		var examSubId = $("#_examPrint_examSub").val();
		if(""==examSubId){
			alertMsg.warn("请选择一个考试批次！");
			return false;
		}
		var url = "${baseUrl}/edu3/teaching/exam/examinertransferTable-condition.html?operatingType=print&flag=examinerTransferTable&examSubId="+examSubId;
		$.pdialog.open(url,'RES_TEACHING_EXAM_PRINT_EXAMINERTRANSFERTABLE_CONDITION_PRINT','学习中习选择',{height:600, width:800,mask:true});
	}
	//打印阅卷老师\复核人员交接表
	function reviewOfficerTransferTabalePrint(){
		var examSubId = $("#_examPrint_examSub").val();
		if(""==examSubId){
			alertMsg.warn("请选择一个考试批次！");
			return false;
		}
		var url = "${baseUrl}/edu3/teaching/exam/print/transferTable-view.html?flag=reviewOfficerTransferTable&examSubId="+examSubId+"&unitIds=''";
		$.pdialog.open(url,'RES_TEACHING_EXAM_PRINT_REVIEWOFFICETRANSFERTABLE','打印预览',{height:600, width:800,mask:true});
	}
	//导出本部试卷交接表
	function partTransFerTableExport(){
	
		var examSubId = $("#_examPrint_examSub").val();
		if(""==examSubId){
			alertMsg.warn("请选择一个考试批次！");
			return false;
		}
		var url ="${baseUrl}/edu3/teaching/exam/export/transferTable.html?flag=partTransferTable&examSubId="+examSubId;	
		downloadFileByIframe(url,'partTransFerTableExportIframe');
	}
	//导出巡考员试卷交接表
	function examinerTransFerTableExport(){
		var examSubId = $("#_examPrint_examSub").val();
		if(""==examSubId){
			alertMsg.warn("请选择一个考试批次！");
			return false;
		}
		var url = "${baseUrl}/edu3/teaching/exam/examinertransferTable-condition.html?operatingType=export&flag=examinerTransferTable&examSubId="+examSubId;
		$.pdialog.open(url,'RES_TEACHING_EXAM_PRINT_EXAMINERTRANSFERTABLE_CONDITION_EXPORT','学习中习选择',{height:600, width:800,mask:true});
	}
	//导出阅卷老师\复核人员交接表
	function reviewOfficerTransferTabaleExport(){
		var examSubId = $("#_examPrint_examSub").val();
		if(""==examSubId){
			alertMsg.warn("请选择一个考试批次！");
			return false;
		}
		var url ="${baseUrl}/edu3/teaching/exam/export/transferTable.html?flag=reviewOfficerTransferTable&examSubId="+examSubId;	
		downloadFileByIframe(url,'partTransFerTableExportIframe');
	}
	/*
	//打印分教点交接表[按编号]
	function studyCenterTransferListByNo(){
		var examSubId = jQuery("#examPrint #examSub").val();
		var url = "${baseUrl}/edu3/teaching/exam/examPrint/printExaminationTransferList-condition.html";
		if(""==examSubId || null == examSubId ){
			alertMsg.warn("请选择要打印的考试批次！");
			return false;
		}
		$.pdialog.open(url+"?examSubId="+examSubId+"&type=num",'RES_TEACHING_EXAM_PRINT_STUDY_CENTER_TRANSFER_LIST1','选择分教点',{height:250, width:550,mask:true});
	}
	//打印分教点交接表[按时间]
	function studyCenterTransferListByTime(){
		var examSubId = jQuery("#examPrint #examSub").val();
		var url = "${baseUrl}/edu3/teaching/exam/examPrint/printExaminationTransferList-condition.html";
		if(""==examSubId || null == examSubId ){
			alertMsg.warn("请选择要打印的考试批次！");
			return false;
		}
		$.pdialog.open(url+"?examSubId="+examSubId+"&type=time",'RES_TEACHING_EXAM_PRINT_STUDY_CENTER_TRANSFER_LIST2','选择分教点',{height:250, width:550,mask:true})
	}
	//打印考试形式
	function examPrintExamForm(){
		var examSubId = jQuery("#examPrint #examSub").val();
		var url = "${baseUrl}/edu3/teaching/exam/examPrint/printExamForm-condition.html";
		if(""==examSubId || null == examSubId ){
			alertMsg.warn("请选择要打印的考试批次！");
			return false;
		}
		alertMsg.confirm("确认要打印所选批次的考试形式吗？",{
			okCall:function(){
				$.pdialog.open(url+"?examSubId="+examSubId,'RES_TEACHING_EXAM_PRINT_EXAMFORM','选择条件',{height:100, width:150,mask:true});
			}
		})
	}
	//打印试卷袋标签
	function examPrintExamBagLabel(){
		var examSubId = jQuery("#examPrint #examSub").val();
		var url = "${baseUrl}/edu3/teaching/exam/examPrint/printBagLabel-view.html";
		if(""==examSubId || null == examSubId ){
			alertMsg.warn("请选择要打印的考试批次！");
			return false;
		}
		alertMsg.confirm("确认要打印所选批次的试卷袋标签吗？",{
			okCall:function(){
				$.pdialog.open(url+"?examSubId="+examSubId,'RES_TEACHING_EXAM_PRINT_EXAMBAGLABEL','打印预览',{height:600, width:800,mask:true});
			}
		})
	}
	
	//打印本部交接表
	function headquartersTransferList(){
		
	}
	*/
</script>
</body>
</html>