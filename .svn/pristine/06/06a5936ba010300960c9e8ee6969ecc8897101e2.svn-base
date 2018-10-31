<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>问卷列表</title>
<script type="text/javascript">

// 	function enterEvaluate(questionnaire,studentInfoid){
// 		$.pdialog.open('${baseUrl }/edu3/teaching/quality/evaluation/stuQuestionnaire/list.html?questionnaireid='+questionnaire+"&studentInfoid="+studentInfoid,'stuQuestionnaire','课堂教学质量问卷调查表',{mask:true,width:600,height:800,close:closeDialog});
// 	}
	function stuQuestionnaireSubmit(){
		var resourceids="";
		var studentInfoid="${studentInfoid}";
		var len = "${fn:length(qbList)}";
		var startindex=0;//下标从0开始
		var incrase=4;//共有4个评分指标
		var endindex=startindex+incrase;
		for(var i=0;i<len;i++){//根据list的长度，一条记录为一组chechbox			
			var flag=false;		
			$("#stuQuestionnairebody input[type='radio']").slice(startindex,endindex).each(function(i,n){				
				if($(this).attr("checked")==true){//当前组的checkbox，找到记录，标记为true	
					flag=true;
				}
			});
			if(!flag){
				alertMsg.warn("问卷不能为空！第"+(i+1)+"条为空");
				return false;
				break;
			}
			startindex=startindex+incrase;
			endindex=endindex+incrase;
		}
		
		
		$("#stuQuestionnairebody input[type='radio']").each(function(i,n){			
			if($(this).attr("checked")==true){
				resourceids+=n.name+":"+n.value+";";
			}
		})
// 		var studentInfoid='${studentInfoid}';
// 		if($("#commentlabelid").val()==""){
// 			alertMsg.warn("教师评价不能为空");
// 			return false;
// 		}
// 		if($("#commentlabelid").val().length<=10){
// 			alertMsg.warn("评语不能少于10个字");
// 			return false;
// 		}
		var commentlabel=$("#commentlabelid").val();
		var questionnaireid='${qn.resourceid}';
		$.ajax({
			   type: "POST",
			   url: "${baseUrl }/edu3/teaching/quality/evaluation/stuQuestionnaire/submit.html",	
			   data:{resourceids:resourceids,studentInfoid:studentInfoid,commentlabel:commentlabel,questionnaireid:questionnaireid},
			   dataType: "json",	
			   error:DWZ.ajaxError,
			   success: function(data){	 
				   	if(data.statusCode == '200'){	
				   		alertMsg.correct(data.message);	
				   		$.pdialog.closeCurrent();
				   		$.pdialog.close(data.reloadDialogId);
				   		
				   		$.pdialog.open("${baseUrl }"+data.reloadUrl+"?studentInfoid="+data.studentInfoid,data.reloadDialogId,"课堂教学质量问卷调查表",{mask:true,width:600,height:450,close:closeDialog});
				   	}else{
				   		alertMsg.error(data.message);
				   		return false;
				   	}
			   }
		 });
	}
</script>
</head>
<body>
	<div class="page">
		<div class="pageHeader">
			<div class="searchBar">
				<ul class="searchContent">
					<li>年度：${qn.questionnaireBatch.yearInfo.yearName}</li>
					<li>学期：${ghfn:dictCode2Val('CodeTerm',qn.questionnaireBatch.term) }</li>
				</ul>
				<ul class="searchContent">
					<li>课程名称：${qn.course.courseName }</li>
					<li>任课老师：${qn.teacher.cnName}</li>
				</ul>
			</div>
		</div>
		<div class="pageContent">
			<form class="pageForm">			
				<table class="form">
					<thead>
						<tr>
							<th width="5%">序号</th>
							<th width="10%">调查指标</th>
							<th width="53%">指标内涵</th>
							<th width="6%">优</th>
							<th width="6%">良</th>
							<th width="6%">中</th>
							<th width="6%">差</th>
						</tr>
					</thead>
					<tbody id="stuQuestionnairebody">
						<c:forEach items="${qbList}" var="qb" varStatus="vs">
							<tr>
								<td>${qb.showOrder }</td>
								<td>${ghfn:dictCode2Val('CodeQuestionBankQuestion',qb.questionTarget) }</td>
								<td>${qb.question }</td>
								<td><input type="radio" name="${qb.resourceid }"  value="${qb.score *1 }"/></td>
								<td><input type="radio" name="${qb.resourceid }"  value="${qb.score *0.8 }" /></td>
								<td><input type="radio" name="${qb.resourceid }" value="${qb.score *0.6 }" /></td>
								<td><input type="radio" name="${qb.resourceid }" value="${qb.score *0.4 }" /></td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
				<table class="form">
					<tr>
						<td>对教师及课程教学的意见及建议（注：如认为教师不胜任教学，应指出具体表现在哪些方面）</td>
					</tr>
					<tr>
						<td colspan="3"><textarea id="commentlabelid" name="commentlabel" cols="30"
								rows="2" style="width: 600px" ></textarea></td>
					</tr>
				</table>
				<div class="formBar">
					<ul>
						<li><div class="buttonActive">
								<div class="buttonContent">
									<button type="button" onclick="stuQuestionnaireSubmit()">提交</button>
								</div>
							</div></li>
						<li><div class="button">
								<div class="buttonContent">
									<button type="button" class="close" href="#close"
										id="close_tab">取消</button>
								</div>
							</div></li>
					</ul>
				</div>
			</form>
		</div>
	</div>
</body>
</html>