<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>考试批次</title>
</head>
<body>
	<h2 class="contentTitle">考试课程</h2>
	<div class="page">
		<div class="pageContent">
			<form method="post"
				action="${baseUrl}/edu3/teaching/exam/plan/saveSetting.html"
				class="pageForm"
				onsubmit="return validateCallback(this,navTabAjaxDone);">
				<input type="hidden" name="resourceid" value="${examSubId }" />
				<div class="pageFormContent" layoutH="97">
					<div class="tabs">
						<div class="tabsHeader">
							<div class="tabsHeaderContent">
								<ul>
									<li><a href="#"><span>考试课程信息</span></a></li>
								</ul>
							</div>
						</div>
						<div class="tabsContent" style="height: 100%;">
							<!-- 考试信息表 -->
							<div>
								<table class="form" id="settingTab">
									<tr>
										<td colspan="3"><span class="buttonActive"><div
													class="buttonContent">
													<button type="button" onclick="addExaminfoTab11()">增加考试日期</button>
												</div></span> <span class="buttonActive"><div
													class="buttonContent">
													<button type="button" onclick="delExaminfoTab()">删除</button>
												</div></span></td>
									</tr>
									<tr>
										<td style="width: 5%"><strong>选项</strong></td>
										<td style="width: 45%"><strong>考试设置名称</strong></td>
										<td style="width: 45%"><strong>考试日期:</strong></td>
									</tr>
								</table>
							</div>
						</div>

						<div class="tabsFooter">
							<div class="tabsFooterContent"></div>
						</div>
					</div>
				</div>
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
			</form>
		</div>
	</div>
	<script type="text/javascript">
	


    function addExaminfoTab11(){
    	var brsId  = "${brschoolId}"
   		var ids    = new Array();
    	$("#settingTab select[name='examSetting'] option:selected").each(function(){
       		ids.push($(this).val());
       	});
       	
       	var url    = "${baseUrl}/edu3/teaching/exam/plansetting/get-examsetting-list.html"
       	$.post(url,{exceptId:ids.toString(),brschoolId:brsId},function(data){
       		
       		if(data.length>0){
       			var trHtml = "<tr id='tr11'><td><input type='checkbox' name='c_id' value=''><input type='hidden' name='hc_id' value=''></td> ";
            	var selHtml= "<select name='examSetting'><option value=''>请选择</option>"; 
           		
            	for(var i=0;i<data.length;i++){
           			selHtml +="<option value='"+data[i].key+"'>"+data[i].value+"</option>"
           		}
            	
           		selHtml    += "</select >"; 
          		trHtml+="<td>"+selHtml+"</td>";
          	    trHtml+="<td id='ctd'><input type='text' id='date' name='date'";
          		trHtml+="style='width:50%'  value='' class='required' onclick='WdatePicker({isShowWeek:true,dateFmt:\"yyyy-MM-dd\"})' />";
          	    trHtml+="</td></tr>";
          	    
          	    jQuery("#settingTab").append(trHtml);
       		}else{
       			alertMsg.info("你已添加了所有的考试设置！");
       		}
       	},"json")
		
  	    
       
    }
    function delExaminfoTab(){
    	var delExamInfo = new Array();
    	$("#settingTab input[name='c_id']:checked").each(function(ind){
    		delExamInfo.push($(this).val());
    	});
    	if(delExamInfo.length < 1){
    		alertMsg.warn("请选择一个要删除的课程！");
    		return false;
    	}
    	$("#settingTab input[name='c_id']:checked").each(function(ind){
		  	$(this).parent().parent().remove();
		});
    }
    
  
</script>
</body>


