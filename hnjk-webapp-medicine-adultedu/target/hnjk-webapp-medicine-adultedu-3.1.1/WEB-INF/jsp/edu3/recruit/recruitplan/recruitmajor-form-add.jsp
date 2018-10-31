<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>招生专业表单</title>

</head>
<body>
	<script type="text/javascript">
	function  addMajor(planId){
		var rowNum = jQuery("#recruitplanMajorTable").get(0).rows.length;
		var examSubjectSet = "<select id='groupid"+rowNum+"' name='courseGroup' class='required'>"+$("#recruitMajorAddform select[id=examSubjectSet]").html()+"</select>";
		var htmlStr        = "<tr id='tr11'><td > <input type='checkbox' name='checkbox' /> <input type='hidden' id='flag"+rowNum+"' name='flag' value='"+planId+"'/></td><td><gh:select id='teachingType"+rowNum+"' name='teachingType' filtrationStr='${filtrationStr }' onchange='resetMajor(this)' dictionaryCode='CodeTeachingType'  classCss='required'  /></td>"+
							 "<td><select id='brSchoolid"+rowNum+"'  name='brSchoolid' class='required'>${brschools }</select>"+
					         "<td><gh:selectModel onchange='getMajorList(this)' id='classicid"+rowNum+"'  name='classicid' bindValue='resourceid' displayValue='classicName' modelClass='com.hnjk.edu.basedata.model.Classic'  classCss='required'/></td>"+
					         "<td><select id='majorid"+rowNum+"' onChange='Num(this)'  name='majorid' class='required'></select></td>"+
					         "<td><input type='text' id='majoridNum"+rowNum+"' name='majoridNum' class='required'></td>"+				         
							 "<span style=\"color:red;\">*</span></td>"+
					         "<td ><input id='limitNum"+rowNum+"' type='text'  name='limitNum' size='20'  min='0' onblur='checkLimitNum(this)' class='required number'/></td>"+
					         "<td ><input id='lowerNum"+rowNum+"' type='text' name='lowerNum' size='20'  min='0'  onblur='checkLowerNum(this)' class='required number'/></td>"+
					         "<td ><input id='studyperiod"+rowNum+"' type='text' name='studyperiod' size='20'  min='0'  class='required number'/></td>"+
					         "<td ><input id='tuitionFee"+rowNum+"' type='text' name='tuitionFee' size='20'  min='0'  class='number'/></td>"+
					  		 //"<td >"+examSubjectSet+"</td></tr>"
					  		 "</tr>";
		if($("#tr11").size()==0){
    		jQuery("#recruitplanMajorTable").append(htmlStr);
    	}else{
    		jQuery("tr[id='tr11']:last").after(htmlStr);    		
    	}
	}
	function Num(obj){
		var a1=$(obj).find('option:selected').val();
		
		$("#recruitMajorAddform select[name='majorid']>option:selected").each(function(){
			var a=$(this);
			var s=a.parent().attr("id");
			var str=s.substr(7,s.length-1);
			if(a1==a.val()){
				var s=$("#majoridNum"+str);	
				var value=s.attr("value");
				if(value==null){
					value="";
				}
				$(obj).parent().next().children().attr("value",value);
				
			}else{
				$(obj).parent().next().children().attr("value","");
			}
			
		});
	}
	function checkLimitNum(obj){
		var limitNum = $(obj).val();
		if(!limitNum.isInteger()){
			$(obj).attr("value","");
		}
	}
	function checkLowerNum(obj){
		var lowerNum = $(obj).val();	
		var limitNum = $(obj).parent().parent().find(" input[name=limitNum]").val();
		if(""==limitNum){
			$(obj).parent().parent().find(" input[name=limitNum]").focus();
		}
		if(lowerNum.isInteger()){
			if(parseInt(lowerNum)>=parseInt(limitNum)){
				$(obj).attr("value","").focus();
				alertMsg.warn("下限数不能大于指标数!");
			}
		}else{
			$(obj).attr("value","");
		}
		
	}
	function delMajor(){
		var idArray = new Array();
  		jQuery("#recruitplanMajorTable input[name='checkbox']:checked").each(function(index){
			if(jQuery(this).attr("checked")){
				idArray.push(index);
			}
		})
		if(idArray.length<1){alertMsg.info("请选择你要删除的记录！");return;}
		$("#recruitplanMajorTable input[name='checkbox']:checked").each(function(ind){
		  	$(this).parent().parent().remove();
		});
	}
	function resetMajor(obj){
		$(obj).parent().parent().find("select[name=majorid]").html("");
		$(obj).parent().parent().find("select[name=classicid]").val("");
	}
	function getMajorList(obj){
		var type 		 = $(obj).parent().parent().find("select[name=teachingType]").val();
		var brSchoolid 		 = $(obj).parent().parent().find("select[name=brSchoolid]").val();
		var typeName     = $(obj).parent().parent().find("select[name=teachingType] option:selected").text();
		var classicid    = $(obj).val();
		var classicName  = $(obj).children(":selected").text();
		var idArray 	 = new Array();
		
		//过滤同一模式同一层次下已选的专业
		$("#recruitMajorAddform select[name='majorid']>option:selected").each(function(){
			var selectedClassic = jQuery(this).parent().parent().parent().find(" select[name='classicid']>option:selected").val();
			var selectedTyep    = jQuery(this).parent().parent().parent().find(" select[name='teachingType']>option:selected").val();
			var selectbrSchoolid    = jQuery(this).parent().parent().parent().find(" select[name='brSchoolid']>option:selected").val();
		
			if(selectedClassic === classicid && selectedTyep ===type && brSchoolid==selectbrSchoolid){
				idArray.push(jQuery(this).val());
			}
		});
		if(""==type){
			alertMsg.warn("请选择一个办学模式!");
			return false;
		}
		if(""==classicid){
			alertMsg.warn("请选择一个层次!");
			return false;
		}
		//alert(idArray.toString());
		jQuery.ajax({
			   type:"POST",
			   url:"${baseUrl}/edu3/recruit/recruitmajorsetting/query-recruitmajorsetting.html",
			   data:{recruitPlanId:'${planid}',classic:classicid,teachingType:type,exceptMajor:idArray.toString()},
			   dataType:"json",
			   async:"false",
			   success:function(myJSON){	   
			   		var selectObj="<option value=''>请选择</option>";
			   		for (var i = 0; i < myJSON.length; i++) {
					 	 selectObj += '<option value="' + myJSON[i].key + '">' + myJSON[i].value + '</option>';    
					}
			   		$(obj).parent().parent().find("select[name=majorid]").html();
			   		$(obj).parent().parent().find("select[name=majorid]").html(selectObj);
			   		if(!myJSON.length>0){
			   			alertMsg.info("办学模式:"+typeName+"<br/>学习层次:"+classicName+"<br/>没有设置招生专业,或你已添加当前条件下的全部专业!");
			   		}
			   } 
		});	
	}
	$(document).ready(function(){
		$("#recruitMajorAddform select[name='classicid']").change(function(){
		});
	});
</script>
	<h2 class="contentTitle">设置招生专业-(${planName})</h2>
	<div class="page">
		<div class="pageContent">
			<form method="post" id="recruitMajorAddform"
				action="${baseUrl}/edu3/recruit/recruitplan/savemajor.html"
				class="pageForm" onsubmit="return validateCallback(this);">
				<div class="pageFormContent" layoutH="97">
					<span class="buttonActive"><div class="buttonContent">
							<button type="button" onclick="addMajor('${planid}')">增加专业</button>
						</div></span> <span class="buttonActive"><div class="buttonContent">
							<button type="button" onclick="delMajor()">删除专业</button>
						</div></span> <select style="display: none;" id="examSubjectSet">
						<option value="">请选择入学考试科目组</option>
						<c:forEach items="${examSubjectSet }" var="set">
							<option value="${set.resourceid }">${ghfn:dictCode2Val('CodeEntranceExaminationType',set.courseGroupName)}</option>
						</c:forEach>
					</select>
					<table id="recruitplanMajorTable" class="form">
						<input type="hidden" id="planid" name="planid" value="${planid}" />
						<tr>
							<td align="center" width="3%"><strong>选择</strong></td>
							<td align="center" width="10%"><strong>办学模式</strong></td>
							<td align="center" width="10%"><strong>教学站</strong></td>
							<td align="center" width="10%"><strong>层次</strong></td>
							<td align="center" width="10%"><strong>专业</strong></td>
							<td align="center" width="5%"><strong>编码</strong></td>
							<td align="center" width="10%"><strong>指标数</strong></td>
							<td align="center" width="10%"><strong>下限人数</strong></td>
							<td align="center" width="5%"><strong>学制</strong></td>
							<td align="center" width="10%"><strong>总学费</strong></td>
							<!-- <td align="center" width="9%"><strong>入学考试科目</strong></td> -->

						</tr>
					</table>
				</div>
				<div class="formBar">
					<ul>
						<li>
							<div class="buttonActive">
								<div class="buttonContent">
									<button type="submit">提交</button>
								</div>
							</div>
						</li>
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
</body>

</html>