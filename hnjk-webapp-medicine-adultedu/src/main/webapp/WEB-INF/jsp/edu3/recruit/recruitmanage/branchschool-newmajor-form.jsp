<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>申报新专业</title>
</head>
<body>
	<script type="text/javascript">
	
	$(document).ready(function(){
		/**
		
		//版本一
		jQuery("#newMajorFromNationMajorTable select[name=classic1]").change(function (){
			var url       = "${baseUrl}/edu3/framework/get-nationmajort.html"
			var cId = $(this).val();
			jQuery.post(url,{classicId:cId,majorType:'',flag:'queryType'},function(myJSON){
				jQuery("#newMajorFromNationMajorTable select[name=majorClass1]").html("");
				jQuery("#newMajorFromNationMajorTable select[name=nationMajor1]").html("");
				var selectObj="<option value=''>请选择</option>";
				for (var i = 0; i < myJSON.length; i++) {
				  selectObj += '<option value="' + myJSON[i].key + '">' + myJSON[i].value + '</option>';    
				}
				jQuery("#newMajorFromNationMajorTable select[name=majorClass1]").html(selectObj);
			},"json");
		});
		jQuery("#newMajorFromNationMajorTable select[name=majorClass1]").change(function (){
			
			var url      	 = "${baseUrl}/edu3/framework/get-nationmajort.html"
			var cId       	 = $("#newMajorFromNationMajorTable select[name=classic1]").val();
			var majorClass	 = $(this).val();
			
			var exceptMajorId= new Array();
			$("#_addNewMajorBody input[name=nationMajor]").each(function(){
				exceptMajorId.push(jQuery(this).val());
			})
				
			jQuery.post(url,{classicId:cId,majorType:majorClass,flag:'queryMajor',exceptid:exceptMajorId.toString()},function(myJSON){
				jQuery("#newMajorFromNationMajorTable select[name=nationMajor1]").html("");
				var selectObj="<option value=''>请选择</option>";
				for (var i = 0; i < myJSON.length; i++) {
				  selectObj += '<option value="' + myJSON[i].key + '">' + myJSON[i].value + '</option>';    
				}
				jQuery("#newMajorFromNationMajorTable select[name=nationMajor1]").html(selectObj);
			},"json");
		});
		*/
		//版本二
		jQuery("#newMajorFromNationMajorTable select[name=nationmajorParentCatolog]").change(function (){
			var url       = "${baseUrl}/edu3/framework/get-nationmajort.html"
			var parCatlog = $(this).val();
			jQuery.post(url,{nationmajorParentCatolog:parCatlog,nationmajorChildCatolog:'',flag:'queryType'},function(myJSON){
				jQuery("#newMajorFromNationMajorTable select[name=nationmajorChildCatolog]").html("");
				var selectObj="<option value=''>请选择</option>";
				for (var i = 0; i < myJSON.length; i++) {
				  selectObj += '<option value="' + myJSON[i].key + '">' + myJSON[i].value + '</option>';    
				}
				jQuery("#newMajorFromNationMajorTable select[name=nationmajorChildCatolog]").html(selectObj);
			},"json");
		});
		//版本二
		jQuery("#newMajorFromNationMajorTable select[name=nationmajorChildCatolog]").change(function (){
			
			var url      				 = "${baseUrl}/edu3/framework/get-nationmajort.html"
			var nationmajorParentCatolog = $("#newMajorFromNationMajorTable select[name=nationmajorParentCatolog]").val();
			var nationmajorChildCatolog	 = $(this).val();
			
			var exceptMajorId= new Array();
			//$("#_addNewMajorBody input[name=nationMajor]").each(function(){
				//exceptMajorId.push(jQuery(this).val());
			//})
				
			jQuery.post(url,{nationmajorParentCatolog:nationmajorParentCatolog,nationmajorChildCatolog:nationmajorChildCatolog,flag:'queryMajor',exceptid:exceptMajorId.toString()},function(myJSON){
				jQuery("#newMajorFromNationMajorTable select[name=nationMajor1]").html("");
				var selectObj="<option value=''>请选择</option>";
				for (var i = 0; i < myJSON.length; i++) {
				  selectObj += '<option value="' + myJSON[i].key + '">' + myJSON[i].value + '</option>';    
				}
				jQuery("#newMajorFromNationMajorTable select[name=nationMajor1]").html(selectObj);
			},"json");
		});
		
	})
		//检查指标数的格式
	function checkLimitNum(obj){
		var limitNum = $(obj).val();
		if(!limitNum.isInteger()){
			$(obj).attr("value","");
		}
	}
	//检查下限数的格式
	function checkLowerNum(obj){
		var lowerNum = $(obj).val();	
		var limitNum = $(obj).parent().parent().parent().find(" input[name=limitNum1]").val();
		if(""==limitNum){
			$(obj).parent().parent().find(" input[name=limitNum1]").focus();
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
	function _genTRHTML(addNewMajorFormId,classicId,classicName,nationmajorParentCatolog,nationmajorParentCatologName,nationmajorChildCatolog,
						nationmajorChildCatologName,major,majorName,dicrect,scope,address,shape,memo,teachingType,teachingTypeName,limitNum ,lowerNum,studyperiod,kelei,majorDescript){
		var rows = jQuery("#newMajorListTable").get(0).rows;
		for(var i=0;i<rows.length;i++){
			var selectedTp  = "";
			var selectedCId = "";
			var selectedMId = "";
			if("addNewMajorFromBaseMajorForm"==addNewMajorFormId){
				selectedMId = $(rows[i]).parent().find("input[name=baseMajor]").val();
				//selectedCId = $(rows[i]).parent().find("input[name=classic_baseMajor]").val();
				//selectedTp  = $(rows[i]).parent().find("input[name=teachingType_baseMajor]").val();
			}else{
				selectedMId = $(rows[i]).parent().find("input[name=nationMajor]").val();
				//selectedCId = $(rows[i]).parent().find("input[name=classic_nationMajor]").val();
				//selectedTp  = $(rows[i]).parent().find("input[name=teachingType_nationMajor]").val();
			}
			if(major == selectedMId){
				alertMsg.warn("你已添加所选专业！");
				return false;
			}
		}
		var rowNum = rows.length;
		var trHTML = "<tr>";
		
		if("addNewMajorFromBaseMajorForm"==addNewMajorFormId){
			trHTML    += "<td style='width:5%'>"+rowNum+"<input type='checkbox' name='flag' value='"+major+"' autocomplete='off'/></td>";
			trHTML    += "<td style='width:8%'><input name='classic_baseMajor' type='hidden' value='"+classicId+"'/> <span>"+classicName+"</span></td>";
			//trHTML    += "<td style='width:7%'><input name='nationmajorParentCatolog' type='hidden' value='"+nationmajorParentCatolog+"'/> "+nationmajorParentCatologName+"</td>";
			//trHTML    += "<td style='width:7%'><input name='nationmajorChildCatolog' type='hidden' value='"+nationmajorChildCatolog+"'/> "+nationmajorChildCatologName+"</td>";
			trHTML    += "<td style='width:6%'><input name='shape_baseMajor' type='hidden' value='"+kelei+"'/> <span>"+kelei+"</span></td>";
			trHTML    += "<td style='width:13%'> <input name='baseMajor' type='hidden' value='"+major+"'/> <span>"+majorName+"</span></td>";
			
			trHTML    += "<td style='width:6%'><input name='teachingType_baseMajor' type='hidden' value='"+teachingType+"'/> <span>"+teachingTypeName.substring(1)+"</span></td>";
			trHTML    += "<td style='width:5%'><input name='studyperiod_baseMajor' type='hidden' value='"+studyperiod+"'/> <span>"+studyperiod+"</span></td>";
			trHTML    += "<td style='width:8%'><input name='dicrect_baseMajor' type='hidden' value='"+dicrect+"'/> <span>"+dicrect+"</span></td>";
			trHTML    += "<td style='width:8%'><input name='scope_baseMajor' type='hidden' value='"+scope+"'/> <span>"+scope+"</span></td>";
			trHTML    += "<td style='width:8%'><input name='address_baseMajor' type='hidden' value='"+address+"'/><span>"+address+"</span></td>";
			//trHTML    += "<td style='width:5%'><input name='shape_baseMajor' type='hidden' value='"+shape+"'/>"+shape+"</td>";
			trHTML    += "<td style='width:5%'><input name='limitNum_baseMajor' type='hidden' value='"+limitNum+"'/><input name='lowerNum_baseMajor' type='hidden' value='0'/><span>"+limitNum+"</span></td>";
			//trHTML    += "<td style='width:5%'><input name='lowerNum_baseMajor' type='hidden' value='"+lowerNum+"'/>"+lowerNum+"</td>";
			trHTML    += "<td style='width:8%'><input name='isPassed_baseMajor' type='hidden' value='W'/>待审核</td>";
			trHTML    += "<td style='width:10%'><textarea name='majorDescript_baseMajor'style='display: none;'>"+majorDescript+"</textarea><span>"+majorDescript+"</span></td>";
			trHTML    += "<td style='width:10%'><textarea name='memo_baseMajor'style='display: none;'>"+memo+"</textarea><span>"+memo+"</span></td>";
			trHTML    += "</tr>";
		}else{
			trHTML    += "<td style='width:5%'>"+rowNum+"<input type='checkbox' name='flag' value='"+major+"' autocomplete='off'/></td>";
			trHTML    += "<td style='width:8%'><input name='classic_nationMajor' type='hidden' value='"+classicId+"'/> "+classicName+"</td>";
			trHTML    += "<td style='width:7%'><input name='nationmajorParentCatolog' type='hidden' value='"+nationmajorParentCatolog+"'/> "+nationmajorParentCatologName+"</td>";
			trHTML    += "<td style='width:7%'><input name='nationmajorChildCatolog' type='hidden' value='"+nationmajorChildCatolog+"'/> "+nationmajorChildCatologName+"</td>";
			
			trHTML    += "<td style='width:15%'> <input name='nationMajor' type='hidden' value='"+major+"'/> "+majorName+"</td>";
			
			trHTML    += "<td style='width:8%'><input name='teachingType_nationMajor' type='hidden' value='"+teachingType+"'/> "+teachingTypeName.substring(1)+"</td>";
			trHTML    += "<td style='width:6%'><input name='dicrect_nationMajor' type='hidden' value='"+dicrect+"'/> "+dicrect+"</td>";
			trHTML    += "<td style='width:5%'><input name='scope_nationMajor' type='hidden' value='"+scope+"'/> "+scope+"</td>";
			trHTML    += "<td style='width:8%'><input name='address_nationMajor' type='hidden' value='"+address+"'/>"+address+"</td>";
			trHTML    += "<td style='width:5%'><input name='shape_nationMajor' type='hidden' value='"+shape+"'/>"+shape+"</td>";
			trHTML    += "<td style='width:8%'><input name='limitNum_nationMajor' type='hidden' value='"+limitNum+"'/><input name='lowerNum_nationMajor' type='hidden' value='0'/>"+limitNum+"</td>";
			//trHTML    += "<td style='width:5%'><input name='lowerNum_nationMajor' type='hidden' value='"+lowerNum+"'/>"+lowerNum+"</td>";
			trHTML    += "<td style='width:8%'><input name='isPassed_nationMajor' type='hidden' value='W'/>待审核</td>";			
			trHTML    += "<td style='width:10%'><textarea name='memo_nationMajor'style='display: none;'>"+memo+"</textarea>"+memo+"</td>";
			trHTML    += "</tr>";
		}
		
		
		return trHTML;
	}
	
	function _addNewMajor(obj){
	
		var classicId   	 			 = ""; //层次ID
		var classicName 	 			 = ""; //层次名
		var nationmajorParentCatolog   	 = ""; //专业大类ID
		var nationmajorParentCatologName = ""; //专业大类名
		var nationmajorChildCatolog   	 = ""; //专业类别ID
		var nationmajorChildCatologName  = ""; //专业类别名
		var major  	 			 		 = ""; //专业ID
		var majorName  					 = ""; //专业名
		var dicrect   		 			 = ""; //专业方向
		var scope   		 			 = ""; //招生范围
		var address   					 = ""; //办学地址
		var shape   		 			 = ""; //形式
		var memo   			 			 = ""; //备注
		var teachingTypeName 			 = ""; //办学模式名
		var limitNum                     = "";//指标数
		var lowerNum                     = "";//下限数
		var studyperiod                  = "";
		var kelei                        = "";
		var majorDescript                = "";
		var teachingType 				 = new Array();//办学模式ID
		var from = "";
		

		//添加国家专业库专业
		if("addNewMajorFromNationMajorForm"==obj.id){
			
			classicId   	 			 = $("#newMajorFromNationMajorTable select[name=classic1] option:selected").val();
			classicName 	 			 = $("#newMajorFromNationMajorTable select[name=classic1] option:selected").text();
			nationmajorParentCatolog   	 = $("#newMajorFromNationMajorTable select[name=nationmajorParentCatolog] option:selected").val();
			nationmajorParentCatologName = $("#newMajorFromNationMajorTable select[name=nationmajorParentCatolog] option:selected").text();
			nationmajorChildCatolog   	 = $("#newMajorFromNationMajorTable select[name=nationmajorChildCatolog] option:selected").val();
			nationmajorChildCatologName  = $("#newMajorFromNationMajorTable select[name=nationmajorChildCatolog] option:selected").text();
			major  	 					 = $("#newMajorFromNationMajorTable select[name=nationMajor1] option:selected").val();
			majorName  			 		 = $("#newMajorFromNationMajorTable select[name=nationMajor1] option:selected").text();
			dicrect   		 			 = $("#newMajorFromNationMajorTable input[name=dicrect1]").val();
			scope   		 			 = $("#newMajorFromNationMajorTable input[name=scope1]").val();
			address   		 			 = $("#newMajorFromNationMajorTable input[name=address1]").val();
			shape   					 = $("#newMajorFromNationMajorTable input[name=shape1]").val();
			memo   						 = $("#newMajorFromNationMajorTable textarea[name=memo1]").val();
			limitNum                     = $("#newMajorFromNationMajorTable input[name=limitNum1]").val();
			lowerNum 				     = $("#newMajorFromNationMajorTable input[name=lowerNum1]").val();
			memo						+= "从国家专业库中申报";	
			$("#newMajorFromNationMajorTable input[name=teachingType1]:checked").each(function(){
				teachingType.push($(this).val());
			})
			for(var i=0;i<teachingType.length;i++){
				var selectors 	  = "#newMajorFromNationMajorTable select[name=teachingType2] option[value="+teachingType[i]+"]";
				var selectVal	  =  $(selectors).text(); 
				teachingTypeName += ","+selectVal;
			}
		
		}
	
		//添加基础专业库专业
		if("addNewMajorFromBaseMajorForm"==obj.id){
			
			//classicId   	 			 = $("#addNewMajorFromBaseMajorForm select[name=classic1] option:selected").val();
			//classicName 	 			 = $("#addNewMajorFromBaseMajorForm select[name=classic1] option:selected").text();
			from                         =  $("#addNewMajorFromBaseMajorForm [name='from']").val();
			major  	 			 		 = $("#addNewMajorFromBaseMajorForm [name='baseMajor1']").val();
			var mName                    = $("#baseMajor_majorid_flexselect").val().split('-');
			classicName                  = mName[1]?$.trim(mName[1]):"";
			kelei                        = mName[2]?$.trim(mName[2]):"";
			majorName  			 		 = $.trim(mName[0]);
			dicrect   		 			 = $("#addNewMajorFromBaseMajorForm input[name=dicrect1]").val();
			scope   		 			 = $("#addNewMajorFromBaseMajorForm input[name=scope1]").val();
			address   		 			 = $("#addNewMajorFromBaseMajorForm input[name=address1]").val();
			studyperiod                  = $("#addNewMajorFromBaseMajorForm select[name=studyperiod1]").val();
			//shape   					 = $("#addNewMajorFromBaseMajorForm input[name=shape1]").val();
			memo   						 = $("#addNewMajorFromBaseMajorForm textarea[name=memo1]").val();
			majorDescript                = $("#addNewMajorFromBaseMajorForm textarea[name='majorDescript1']").val();
			limitNum                     = $("#addNewMajorFromBaseMajorForm input[name=limitNum1]").val();
			//lowerNum 				     = $("#addNewMajorFromBaseMajorForm input[name=lowerNum1]").val();
			//memo						+= "从基础专业库中申报";
			$("#addNewMajorFromBaseMajorForm input[name=teachingType1]:checked").each(function(){
				teachingType.push($(this).val());
			});
			for(var i=0;i<teachingType.length;i++){
				var selectors 	  = "#addNewMajorFromBaseMajorForm select[name=teachingType2] option[value="+teachingType[i]+"]";
				var selectVal	  =  $(selectors).text(); 
				teachingTypeName += ","+selectVal;
			}
			
			
		}
		if(classicName==""||classicName==undefined){
			alertMsg.warn("专业数据不完全!");
			return false;
		}
		if(teachingType.length<1){
			alertMsg.warn("请选择选择一个形式!");
			return false;
		}

		/*if(""==classicId  || undefined==classicId){
			alertMsg.warn("请选择一个层次!");
			return false;	
		}else */
		if(""==major || undefined==major){
			alertMsg.warn("请选择一个专业!");
			return false;
		}else if(""==limitNum || undefined==limitNum){
			alertMsg.warn("人数数不能为空!");
			return false;
		}else if(""==studyperiod || undefined==studyperiod){
			alertMsg.warn("学制不能为空!");
			return false;
		}/*else if((""==nationmajorParentCatolog  || undefined==nationmajorParentCatolog) && "addNewMajorFromNationMajorForm"==obj.id){
			alertMsg.warn("请选择一个大类!");
			return false;
		}else if((""==nationmajorChildCatolog  || undefined==nationmajorChildCatolog) && "addNewMajorFromNationMajorForm"==obj.id){
			alertMsg.warn("请选择一个专业类别!");
			return false;
		}*/
		else{
			if(from!="edit"){
				var addTr = _genTRHTML(obj.id,classicId,classicName,nationmajorParentCatolog,nationmajorParentCatologName,nationmajorChildCatolog,nationmajorChildCatologName,major,majorName,dicrect,scope,address,shape,memo,teachingType.toString(),teachingTypeName,limitNum ,lowerNum,studyperiod,kelei,majorDescript);
				$("#_addNewMajorBody").append(addTr);
			}else {
				_editNewMajorForm(classicName,major,majorName,dicrect,scope,address,memo,teachingType,teachingTypeName,limitNum,studyperiod,kelei,majorDescript);
			}			
			$.pdialog.closeCurrent();
		}
		return false;
		
	}
	function _editNewMajorForm(classicName,major,majorName,dicrect,scope,address,memo,teachingType,teachingTypeName,limitNum,studyperiod,kelei,majorDescript){
		//var tp = $("#newMajorListTable").find("#tr"+newMajorId);
		var tp = $("#_addNewMajorBody input[name='flag']:checked").parent().parent();
		//tp.find("[name='classic_baseMajor_select'] option[value='"+classicName+"']").attr("selected","selected");
		tp.find("[name='classic_baseMajor']").siblings("span").text(classicName);
		tp.find("[name='shape_baseMajor']").val(kelei).siblings("span").text(kelei);
		//tp.find("[name='baseMajor']").val(major).siblings("select").val(major);
		tp.find("[name='baseMajor']").val(major).siblings("span").text(majorName);
		tp.find("[name='teachingType_baseMajor']").val(teachingType).siblings("span").text(teachingTypeName);
		tp.find("[name='studyperiod_baseMajor']").val(studyperiod).siblings("span").text(studyperiod);
		tp.find("[name='dicrect_baseMajor']").val(dicrect).siblings("span").text(dicrect);
		tp.find("[name='scope_baseMajor']").val(scope).siblings("span").text(scope);
		tp.find("[name='address_baseMajor']").val(address).siblings("span").text(address);
		tp.find("[name='limitNum_baseMajor']").val(limitNum).siblings("span").text(limitNum);
		tp.find("[name='memo_baseMajor']").val(memo).siblings("span").text(memo);
		tp.find("[name='majorDescript_baseMajor']").val(majorDescript).siblings("span").text(majorDescript);
		
	}
	</script>
	<div class="page">
		<div class="pageContent">
			<div class="pageFormContent" layoutH="30">
				<div class="tabs">
					<div class="tabsHeader">
						<div class="tabsHeaderContent">
							<ul>
								<li class="selected"><a href="#"><span>申报基础专业库中专业</span></a></li>
								<%-- <li ><a href="#"><span>申报国家专业库中专业</span></a></li> --%>
							</ul>
						</div>
					</div>
					<div class="tabsContent" style="height: 100%;">
						<!--申报基础专业库中专业  -->
						<div>
							<form id="addNewMajorFromBaseMajorForm" method="post" action=""
								class="pageForm" onsubmit="return _addNewMajor(this);">
								<input type="hidden" name="from" value="${from }" />
								<table class="form" id="newMajorFromBaseMajorTable">
									<tr>
										<%-- 
							<td width="12%">层&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;次：</td>
							<td  width="38%">
								<gh:selectModel name="classic1" bindValue="resourceid" displayValue="classicName" modelClass="com.hnjk.edu.basedata.model.Classic"  value="${classic1 }"/>
								<font color="red">*</font> 
							</td>
							 --%>
										<td width="12%">专&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;业：</td>
										<td colspan="3" id="baseMajorTD">
											<%--<gh:selectModel id="baseMajorTD" name="baseMajor" modelClass="com.hnjk.edu.basedata.model.Major" bindValue="resourceid" displayValue="majorName" /><font color="red">*</font>  --%>
											<select id="baseMajor_majorid" name="baseMajor1"
											style="width: 75%;">
												<option value=""></option>
												<c:forEach items="${majorList }" var="m">
													<option value="${m.resourceid }"
														<c:if test="${m.resourceid eq baseMajor1 }">selected="selected"</c:if>>${m.majorName }
														- ${m.nationMajor.classic.classicName } -
														${ghfn:dictCode2Val('CodeMajorClass',m.nationMajor.nationMajorType) }</option>
												</c:forEach>
										</select> <script type="text/javascript">
								$(document).ready(function(){
									$("#baseMajor_majorid").flexselect();
							    });
								</script>
										</td>

									</tr>

									<tr>
										<td width="12%">专业方向：</td>
										<td width="38%"><input name="dicrect1" type="text"
											value="${dicrect1 }" /></td>
										<td width="12%">形式：</td>
										<td width="38%"><gh:select name="teachingType2"
												dictionaryCode="CodeTeachingType" style="display: none;"
												choose="N" /> <%-- 
								<c:choose>
									<c:when test="${not empty teachingType1 }">
										<gh:checkBox name="teachingType1" dictionaryCode="CodeTeachingType" value="face" filtrationStr="${teachingType1}" inputType="radio" />
									</c:when>
									<c:otherwise>
										<gh:checkBox name="teachingType1" dictionaryCode="CodeTeachingType" value="face" filtrationStr="${currentUser.orgUnit.schoolType}"  inputType="radio"/>
									</c:otherwise>
								</c:choose>
								 --%> <gh:checkBox name="teachingType1"
												dictionaryCode="CodeTeachingType" value="${teachingType1}"
												inputType="radio" /> <font color="red">*</font></td>
									</tr>
									<tr>
										<td width="12%">人数：</td>
										<td width="38%"><input name="limitNum1" type="text"
											value="${limitNum1 }" min='0' onblur='checkLimitNum(this)'
											class='required number' /></td>
										<td width="12%">招生范围：</td>
										<td width="38%"><input name="scope1" type="text"
											value="${scope1 }" /></td>
										<%--
							<td width="12%">下限数：</td>
							<td  width="38%">
								<input name="lowerNum1" type="text" value="${lowerNum1 }" min='0' onblur='checkLowerNum(this)' class='required number'/>
							</td>
							  --%>
									</tr>
									<tr>

										<td width="12%">办学地址：</td>
										<td width="38%"><input name="address1" type="text"
											value="${address1 }" /></td>
										<td width="12%">学&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;制：</td>
										<td width="38%">
											<%-- <input name="studyperiod1" type="text" value="${studyperiod1 }"/> --%>
											<gh:select name="studyperiod1"
												dictionaryCode="CodeMajorStudyperiod"
												value="${studyperiod1 }" style="width:120px;" /> <span
											style="color: red;">*</span>
										</td>
									</tr>
									<tr>
										<td width="12%">专业介绍：</td>
										<td width="88%" colspan="3"><textarea
												name="majorDescript1" style="width: 50%;">${majorDescript1 }</textarea>
										</td>
									</tr>
									<tr>
										<td width="12%">备&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;注：</td>
										<td width="88%" colspan="3"><textarea name="memo1"
												style="width: 50%;">${memo1 }</textarea></td>
									</tr>
								</table>

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
														onclick="$.pdialog.closeCurrent();">取消</button>
												</div>
											</div></li>
									</ul>
								</div>
							</form>
						</div>
						<!--申报基础专业库中专业  -->
						<!--申报国家专业库中专业  -->
						<%-- 
			<div >
				<form id="addNewMajorFromNationMajorForm" method="post" action="" class="pageForm" onsubmit="return _addNewMajor(this);">
					<table class="form" id="newMajorFromNationMajorTable" >
						<tr>
							<td width="12%">专业大类：</td>
							<td width="38%"><gh:select dictionaryCode="nationmajorParentCatolog" name="nationmajorParentCatolog" value="${nationmajorParentCatolog }" choose="Y"  /><font color="red">*</font> </td>
							<td width="12%">专业类别：</td>
							<td width="38%">
								<select name="nationmajorChildCatolog">
									<option value="">请选择</option>
									<c:if test="${ not empty nationmajorChildCatolog }">
										<option value="${nationmajorChildCatolog }">${nationmajorChildCatologName }</option>
									</c:if>
								</select>
								<font color="red">*</font> 
							</td>
						</tr>
						<tr>
							<td width="12%">层&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;次：</td>
							<td  width="38%">
								<gh:selectModel name="classic1" bindValue="resourceid" displayValue="classicName" modelClass="com.hnjk.edu.basedata.model.Classic"  value="${classic1 }"/><font color="red">*</font> 
							</td>
							<td width="12%">专&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;业：</td>
							<td  width="38%" id="nationMajorTD">
								<select name="nationMajor1"  >
									<c:if test="${not empty nationMajor1 }">
										<option value="${nationMajor1 }">${nationMajorName }</option>
									</c:if>
								</select><font color="red">*</font> 
							</td>
							
						</tr>
						<tr>
							<td width="12%">专业方向：</td>
							<td  width="38%">
								<input name="dicrect1" type="text" value="${dicrect1 }"/>
							</td>
							<td width="12%">办学模式：</td>
							<td  width="38%">
								<gh:select name="teachingType2" dictionaryCode="CodeTeachingType" style="display: none;" choose="N"/>
								<c:choose>
									<c:when test="${not empty teachingType1 }">
										<gh:checkBox name="teachingType1" dictionaryCode="CodeTeachingType" value="face" filtrationStr="${teachingType1}" />
									</c:when>
									<c:otherwise>
										<gh:checkBox name="teachingType1" dictionaryCode="CodeTeachingType" value="face" filtrationStr="${currentUser.orgUnit.schoolType}" />
									</c:otherwise>
								</c:choose>
								<font color="red">*</font> 
							</td>
						</tr>
						<tr>
							<td width="12%">指标数：</td>
							<td  width="38%">
								<input name="limitNum1" type="text" value="${limitNum1 }"  min='0' onblur='checkLimitNum(this)' class='required number'/>
							</td>
							<td width="12%">招生范围：</td>
							<td width="38%">
								<input name="scope1" type="text" value="${scope1 }"/>
							</td>
						</tr>
						<tr>
							
							<td width="12%">办学地址：</td>
							<td width="38%">
								<input name="address1" type="text" value="${address1 }"/>
							</td>
							<td width="12%">形&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;式：</td>
							<td width="38%">
								<input name="shape1" type="text" value="${shape1 }"/>
							</td>
						</tr>
						<tr>
							
							<td width="12%">备&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;注：</td>
							<td width="88%" colspan="3">
								<textarea name="memo1" >${memo1 }</textarea>
							</td>
						</tr>
					</table>
				
					<div class="formBar">
						<ul>
							<li><div class="buttonActive"><div class="buttonContent"><button type="submit">提交</button></div></div></li>
							<li><div class="button"><div class="buttonContent"><button type="button" class="close" onclick="$.pdialog.closeCurrent();">取消</button></div></div></li>
						</ul>
					</div>
				</form>	
			</div>
			 --%>
						<!--申报国家专业库中专业  -->
					</div>
				</div>
			</div>

		</div>
	</div>
</body>
</html>