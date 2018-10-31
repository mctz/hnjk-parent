<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>招生批次表单</title>
<style>
#_recruitPlan_nostyle td {
	height: 4px
}
</style>
</head>
<body>
	<h2 class="contentTitle">设置招生批次</h2>
	<div class="page">
		<div class="pageContent">
			<form id="planForm" method="post"
				action="${baseUrl}/edu3/recruit/recruitplan/saveplan.html"
				class="pageForm" onsubmit="return validateCallback(this);">
				<div layoutH="77">
					<table id="planTable" class="form">
						<input type="hidden" id="gradeid" name="gradeid"
							value="${plan.grade.resourceid }" />
						<input type="hidden" name="resourceid" value="${plan.resourceid }" />
						<tr>
							<td style="width: 12%">招生批次名称:</td>
							<td style="width: 38%" colspan="3"><input type="text"
								name="recruitPlanname" size="40"
								value="${plan.recruitPlanname }" maxlength="100"
								class="required" /></td>

						</tr>
						<tr>
							<td style="width: 12%">招生批次发布日期:</td>
							<td style="width: 38%"><input type="text" name="publishDate"
								size="40"
								value="<fmt:formatDate value="${plan.publishDate }" pattern="yyyy-MM-dd" />"
								class="required date1" onFocus="WdatePicker({isShowWeek:true})" />
							</td>
							<td style="width: 12%">教学站申报结束时间:</td>
							<td style="width: 38%"><input type="text"
								name="brSchoolApplyCloseTime" size="40"
								value="<fmt:formatDate value="${plan.brSchoolApplyCloseTime }" pattern="yyyy-MM-dd" />"
								class="required date1" onFocus="WdatePicker({isShowWeek:true})" />
							</td>
						</tr>
						<tr>
							<td style="width: 12%">报名开始时间:</td>
							<td style="width: 38%"><input type="text" name="startDate"
								size="40"
								value="<fmt:formatDate value="${plan.startDate }" pattern="yyyy-MM-dd" />"
								class="required date1" onFocus="WdatePicker({isShowWeek:true})" />
							</td>
							<td style="width: 12%">报名截止时间:</td>
							<td style="width: 38%"><input type="text" name="endDate"
								size="40"
								value="<fmt:formatDate value="${plan.endDate }" pattern="yyyy-MM-dd" />"
								class="required date1" onFocus="WdatePicker({isShowWeek:true})" />
							</td>
						</tr>
						<%-- 
					<tr>
						<td style="width:12%">录取查询开放时间:</td>
						<td style="width:38%">
							<input type="text" id="da3" name="enrollStartTime" size="40" value="${plan.enrollStartTime }" class="required"  
							 onFocus="WdatePicker({isShowWeek:true})"/>
						</td>
						<td style="width:12%">录取查询截止时间:</td>
						<td style="width:38%">
							<input type="text" id="da4" name="enrollEndTime" size="40" value="${plan.enrollEndTime }" class="required" 
							onFocus="WdatePicker({isShowWeek:true})"/>
						</td>
					</tr>
					--%>
						<tr>
							<td style="width: 12%">年度:</td>
							<td style="width: 38%"><gh:selectModel id="yearInfo"
									name="yearInfo" bindValue="resourceid" displayValue="yearName"
									modelClass="com.hnjk.edu.basedata.model.YearInfo"
									value="${plan.yearInfo.resourceid}" classCss="required"
									style="width:150px" orderBy="firstYear desc" /> <font
								color="red">*</font></td>
							<td style="width: 12%">学期:</td>
							<td style="width: 38%"><gh:select id="term" name="term"
									dictionaryCode="CodeTerm" value="${plan.term }"
									classCss="required" style="width:150px" /> <font color="red">*</font>
							</td>
						</tr>
						<tr>
							<td style="width: 12%">是否特批:</td>
							<td style="width: 38%"><input type="radio" name="isSpecial"
								value="Y" classCss="required"
								<c:if test="${plan.isSpecial eq 'Y'}"> checked="checked" </c:if>>是&nbsp;&nbsp;&nbsp;
								<input type="radio" name="isSpecial" value="N"
								classCss="required"
								<c:if test="${plan.isSpecial eq 'N' || plan.isSpecial ==null  || plan.isSpecial eq '' }"> checked="checked" </c:if>>否
							</td>
							<td style="width: 12%">批次状态:</td>
							<td style="width: 38%"><input type="radio"
								name="isPublished" value="Y"
								<c:if test="${plan.isPublished eq 'Y' }" >  checked="checked" </c:if> />发布&nbsp;&nbsp;&nbsp;
								<input type="radio" name="isPublished" value="N"
								<c:if test="${plan.isPublished eq 'N' || plan.isPublished eq '' || plan.isPublished ==null}"> checked="checked" </c:if> />关闭
							</td>
						</tr>
						<tr id="teachingTypeTR">
							<td style="width: 12%">办学模式:</td>
							<td style="width: 38%" colspan="3">
								<%-- 第一个版本:特殊批次只允许网络成人直属班的办学模式
							<c:choose>
								<c:when test="${plan.isSpecial eq 'Y'}"><input type="checkbox" name="teachingType" checked="checked" value="${plan.teachingType}"> 网络成人直属班</c:when>
								<c:otherwise>
									<gh:checkBox name="teachingType"  dictionaryCode="CodeTeachingType" value="${plan.teachingType}"  />
								</c:otherwise>
							</c:choose>
							--%> <gh:checkBox name="teachingType"
									dictionaryCode="CodeTeachingType" value="${plan.teachingType}" />
							</td>
						</tr>

						<tr id="brSchoolTR"
							<c:if test="${plan.isSpecial != 'Y'  }"> style="display: none;" </c:if>>
							<td style="width: 10%">允许申报的教学站:<br />
							<font color="red">（如果选择此项，则该批次只允许选定的教学站申报）</font></td>
							<td style="width: 90%" colspan="3"><select id="brSchool"
								name="brSchool" multiple='multiple' size="10">
									<c:forEach items="${brSchoolList }" var="brSchool">
										<c:forEach items="${selectedBrSchool }" var="selectedBrSchool">
											<c:if test="${selectedBrSchool eq brSchool.resourceid  }">
												<option value="${brSchool.resourceid }" selected="selected">${brSchool.unitName }</option>
											</c:if>
										</c:forEach>
										<option value="${brSchool.resourceid }">${brSchool.unitName }</option>
									</c:forEach>
							</select></td>
						</tr>
						<c:if test="${not empty plan.resourceid }">
							<tr>
								<td style="width: 10%">附件：</td>
								<td style="width: 90%" colspan="3"><gh:upload
										hiddenInputName="uploadfileid"
										baseStorePath="users,${storeDir},attachs" fileSize="10485760"
										uploadType="attach" attachList="${attachList}"
										fileExt="doc|xls" formType="entranceMemoAttach"
										formId="${plan.resourceid}" /></td>
							</tr>
						</c:if>
						<tr id="_recruitPlan_nostyle">
							<td style="width: 10%">入学须知:</td>
							<td style="width: 90%" colspan="3"><textarea
									id="_recruitPlan_webregMemo" name="webregMemo"
									style="width: 80%; height: 200px; visibility: hidden;">${plan.webregMemo }</textarea>
							</td>
						</tr>
					</table>

				</div>
				<div class="formBar">
					<ul>
						<li>
							<div class="buttonActive">
								<div class="buttonContent">
									<button id="plansubmit" type="submit">提交</button>
								</div>
							</div>
						</li>
						<li>
							<div class="button">
								<div class="buttonContent">
									<button type="button" class="close"
										onclick="navTab.closeCurrentTab();">取消</button>
								</div>
							</div>
						</li>
					</ul>
				</div>
			</form>
		</div>
	</div>
	<script type="text/javascript">
	//时间设置必须为前一个设置完成后才能设置后一个
	function showMoreTime(obj,id){
		var curValue = $(obj).val();
		if(null!= curValue && ""!=curValue){
			var els = "#planTable input[id="+id+"]";
			$(els).show();
			$(els).focus();
		}
	}
	
   $(document).ready(function(){
	    $('#brSchool').multiselect2side({
			selectedPosition: 'right',
			moveOptions: false,
			labelsx: '',
			labeldx: ''
		});
	    
	    KE.init({//初始化编辑器
	        id : '_recruitPlan_webregMemo',     
	        items : [
	  				'fontname', 'fontsize', '|','subscript','superscript', 'textcolor', 'bgcolor', 'bold', 'italic', 'underline',
	  				'removeformat', '|', 'justifyleft', 'justifycenter', 'justifyright', 'insertorderedlist',
	  				'insertunorderedlist', '|', 'link'],

	  	      afterCreate : function(id) {
	  				KE.util.focus(id);
	  			}	      
	  	});
	    
	    $("#planForm input[name=isSpecial]").click(function(){
			
	    	if("Y"==$(this).val()){
				
				$("#planForm input[name=teachingType]").each(function(){
					if("face"==$(this).val()){
						$(this).attr("checked","checked");
					}	
					//}else{
						//$(this).attr("disabled","disabled");
					//}
				})
				$("#planTable tr[id=brSchoolTR]").show();
			}else{
				$("#planForm input[name=teachingType]").each(function(){
					$(this).attr("disabled","");
				})
				$("#planTable tr[id=brSchoolTR]").hide();
			}
	    	
		})
	    
		jQuery("#term").change(function(){
			var yearInfo = $("#yearInfo").val();
			var term = $("#term").val();
			var url = "${baseUrl}/edu3/recruit/recruitplan/getGrade.html?yearInfo="+yearInfo+"&term="+term;
			if(null==yearInfo || ""==yearInfo){
				 $("#term").attr("value","");
				alertMsg.info('请选择年度！');
			}else{
				$.ajax({
					type:"post",
					url:url,
					dataType:"json",
					success:function(data){
						if(data['status']==true){
							$("#planTable #gradeid").attr("value",data['gradeid']);
							$("#plansubmit").removeAttr("disabled");
						}else{
							$("#plansubmit").attr({disabled:"disabled"});
							alertMsg.warn(data['msg']);
							
						}
					}
				});
			}
		});		
		
	});
   
   function createKE(){
		KE.create('_recruitPlan_webregMemo');
	}
	setTimeout(createKE,1000);
</script>
</body>

</html>