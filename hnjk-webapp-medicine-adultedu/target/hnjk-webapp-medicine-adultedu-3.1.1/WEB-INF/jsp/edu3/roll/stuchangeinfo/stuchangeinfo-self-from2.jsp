<%@page contentType="text/html;charset=UTF-8"%>
<%@include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>替学生学籍异动</title>
<script type="text/javascript">	
	  jQuery(document).ready(function(){    
		  //判断显示区域
		  /*
		  停学	06
		休学	11
		复学	12
		退学	13
		转专业	23
		转学习方式	81
		转层次	83
		转年级	84
		其他	99
		转办学单位	82
		延期 100
		过期 101
		开除学籍 42
		*/
		  var changeType = '${stuChangeInfo.changeType}';
		  $("#reason_common").show();
		  if("${finished}"=='Y'){
			  $("#changeTab [name^=22],[name^=33],[name^=55],[name^=66],[name^=77],[name^=88]").hide();
			  $("#changeTab #A,#B,#C,#D,#E,#F").show();
			  if(changeType == '11'){
				  $("#changeTab #G").show();
			  }else if(changeType == '13'){
				  $("#changeTab [name^=77]").show();
				  $("#reason_common").hide();
				  $("#changeTab #A,#B,#C,#D,#E,#F,#G,#H").hide();
				  
				 /* 原逻辑
				  $("#changeTab #H").show();
				  $("#reason_common").hide(); */
			  }
		  }else{
			  if(changeType == '23'){//转专业
				  $("#changeTab [name^=33]").show();
				  $("#changeTab #A,#B,#C,#D,#E,#F,#G,#H").hide();
			  }else if(changeType == '81'){//转学习形式
				  $("#changeTab [name^=66]").show();
				  $("#changeTab #A,#B,#C,#D,#E,#F,#G,#H").hide();
			  }else if(changeType == '82'){//转办学单位
				  $("#changeTab [name^=55]").show();
				  $("#changeTab #A,#B,#C,#D,#E,#F,#G,#H").hide();
			  }else if(changeType == '11'){//休学
				  $("#changeTab [name^=88]").show();
				  $("#changeTab #A,#B,#C,#D,#E,#F,#G,#H").hide();
			  }else if(changeType == '13'){//退学
				  $("#changeTab [name^=77]").show();
				  $("#reason_common").hide();
				  $("#changeTab #A,#B,#C,#D,#E,#F,#G,#H").hide();
			  }else if(changeType == '12'){//复学
				  $("#changeTab [name^=22]").show();
				  $("#changeTab #A,#B,#C,#D,#E,#F,#G,#H").hide();
			  }
		  }
		  
		  var isManager = "${isManager}";
		  if(isManager=="false"){
			  $("#stuChangeForStu input[id='stuNum']").attr("readonly","readonly");
		  }
		  //queryStuInfo();
	  });
	function saveChange(){
		jQuery("#CM").val("0");
	}
	
	function submitChange(){
		jQuery("#CM").val("1");
	}
	
	function stuchangeinfo_submitForm(type,callback){
		var f =  document.getElementById("stuchangeinfoForm");
		f.action += "?CM="+type;	;
		return validateCallback(f,callback);
	}
	//**核心方法区
	//ajax查询学生信息
	function queryStuInfo(){
		var obj = document.getElementById('changeType');
		var stuNum = $("#stuChangeForStu input[id='stuNum']").val();
		if(stuNum!=""){
			$.ajax({
				data:"stuNum="+stuNum,
				dataType:"json",
				url:"${baseUrl}/edu3/schoolroll/awardspun/querystuinfo.html",
				success:function(data){
					if(data.error && data.error!=""){
						alertMsg.warn(data.error);
					}else{
						if(data.isLastApply=="N"){
							navTab.closeCurrentTab();
							alertMsg.warn("该学生已经有学籍异动未审核，审核完成后才能继续申请或撤销之前申请的异动，请检查学生是否已经有异动申请");							
							return;
						}
						var schoolCode =  data.schoolCode;
						$("#studentresourceid").val(data.studentid);
						
						$("#changeTab input[name='stuName']").val(data.studentName);
						$("#changeTab input[name='stuCenter']").val(data.schoolCenter);
						$("#changeTab #stuCenterName").html(data.schoolCenter);
						$("#changeTab input[name='major']").val(data.major);
						$("#changeTab #majorName").html(data.major);
						$("#changeTab input[name='majorId']").val(data.majorId);
						$("#changeTab input[name='classic']").val(data.classic);
						$("#changeTab #classicName").html(data.classic);
						$("#changeTab input[name='classicId']").val(data.classicId);
						$("#changeTab input[name='grade']").val(data.grade);
						$("#changeTab #gradeName").html(data.grade);
						$("#changeTab input[name='gradeId']").val(data.gradeId);					
						$("#changeTab input[name='changeBeforeLearingStyle']").val(data.teachingtypeid);
						$("#changeTab input[name='changeBeforeTeachingGuidePlanId']").val(data.teachingPlan);
						$("#changeTab input[name='changeBeforeBrSchoolId']").val(data.brSchool);
						$("#changeTab input[name='schoolType']").val(data.schoolType);
						$("#changeTab #classesName").html(data.classes);
						$("#changeTab #teachingtype").html(data.teachingtype);
						
						//转专业的学校名称和学习方式
						$("#changeMajor_classicName").html(data.classic);
						$("#changeMajor_gradeName").html(data.grade);
						$("#changeMajor_brschoolid").val(data.brSchool);
						$("#changeMajor_brschool").html(data.schoolCenter);
						  // 除广外以外的学校
					    if(schoolCode!="11846"){
					    	$("#changeMajor_TeachingTypeid").val(data.teachingtypeid);
							$("#changeMajor_TeachingType").html(data.teachingtype);
					    }
						$("#studentstatus").val(data.studentstatus);
						//转教学站
						$("#changeSchool_classesName").html(data.classes);
						$("#changeSchool_gradeName").html(data.grade);
					    $("#changeSchool_classicName").html(data.classic);
					    // 除广外以外的学校
					    if(schoolCode!="11846"){
					    	$("#changeSchool_TeachingType").html(data.teachingtype);
						    $("#changeSchool_TeachingTypeid").val(data.teachingtypeid);
						    $("#changeSchool_schoolCenterid").val(data.unitSelect); //教学站
					    }
					    
					    //转学习形式
					    $("#changeTeachingType_gradeName").html(data.grade);
					    $("#changeTeachingType_classicName").html(data.classic);
					    //复学
					    $("#backSchool_classicName").html(data.classic);
					    $("#backSchool_teachingType").val(data.teachingtypeid);
					    $("#backSchool_teachingTypeStr").html(data.teachingtype);
					    //复学的异动初始化 一般地,复选刚开始的异动信息与原信息应是一致的.
					    initialize_backSchool(data.teachingtypeid,data.gradeId,data.brSchool,data.majorId,data.classesId,data.classicId);
					};
					if(obj.value == '23' && schoolCode!="11846"){
						_selectMajor_new();
					}
					if(obj.value == '82' && schoolCode!="11846"){
						selectSchool_changeSchool(data.majorId);
					}
					if(obj.value == '12'){
						selectSchool_backSchool();
					}
				}
			});
		}else{
			alertMsg.warn("请输入学生学号.");
		}
	}
	//异动类型变化时的响应
	function choiceChange(){
		var obj = document.getElementById('changeType');
		var stuNum = $("#stuChangeForStu input[id='stuNum']").val();
		if($("#studentstatus").val()=='13'){//退学后的学生不能做学籍异动  2016.11.10
			alertMsg.warn("当前学生的学籍状态为退学，只有在校生才能申请学籍异动！");
			$('#changeType').val('');
			return ;
		}
		
		$("#bankName").attr("class","");
		$("#cardNo").attr("class","");
		$("#bankAddress").attr("class","");
		$("#_majorid").attr("class","");
		$("#stopStudy_reason").attr("class","");
		$("#pauseStudy_backSchoolDate").attr("class","");
		
		$("#backSchool_majorid").attr("class","");
		$("#backSchool_classesid").attr("class","");
		$("#backSchool_backSchoolDate").attr("class","");
		$("#backSchool_schoolCenterid").attr("class","");
		$("#backSchool_changeguidteachPlanId").attr("class","");
		
		$("#changeMajor_classesid").attr("class","");
		$("#changeMajor_changeguidteachPlanId").attr("class","");
		$("#changeSchool_schoolCenterid").attr("class","");
		$("#changeSchool_changeguidteachPlanId").attr("class","");
		$("#changeTeachingType_teachingType").attr("class","");
		$("#changeTeachingType_classesid").attr("class","");
		$("#changeTeachingType_changeguidteachPlanId").attr("class","");
		$("#changeTeachingType_stuCenterid").attr("class","");
		$("#changeTeachingType_majorName").attr("class","");
		$("#changeMajor_TeachingTypeid").attr("class","");
		$("#changeSchool_TeachingTypeid").attr("class","");
		
		$("#changeTab [name^=22]").hide();
		$("#changeTab [name^=33]").hide();
		$("#changeTab [name^=55]").hide();
		$("#changeTab [name^=66]").hide();
		$("#changeTab [name^=77]").hide();
		$("#changeTab [name^=88]").hide();
		if(obj.value=='23'){//转专业
			$("#changeTab [name^=33]").show();
            $("#changeTab [name^=77H]").show();
			$("#changeTab #A,#B,#C,#D,#E,#F,#G,#H").hide();
			$("#reason_common").show();
			if(""!=stuNum){
				_selectMajor_new();
			}
			
			$("#_majorid").attr("class","required");
			$("#changeMajor_classesid").attr("class","required");
			$("#changeMajor_TeachingTypeid").attr("class","required");
			$("#changeMajor_changeguidteachPlanId").attr("class","required");
			
		}else if(obj.value=='82'){//转办学单位--更改教学点
			$("#changeTab [name^=55]").show();
			$("#reason_common").show();
			$("#changeTab #A,#B,#C,#D,#E,#F,#G,#H").hide();
			// 广外先选择学习形式
			if(""!=stuNum ){
				selectSchool_changeSchool();
			}
			$("#changeSchool_schoolCenterid").attr("class","required");
			$("#changeSchool_changeguidteachPlanId").attr("class","required");
			$("#changeSchool_TeachingTypeid").attr("class","required");
			
		}else if(obj.value=='81'){//转学习形式
			$("#changeTab [name^=66]").show();
			$("#reason_common").show();
            $("#changeTab [name^=77H]").show();
			$("#changeTab #A,#B,#C,#D,#E,#F,#G,#H").hide();
			
			$("#changeTeachingType_teachingType").attr("class","required");
			$("#changeTeachingType_classesid").attr("class","required");
			$("#changeTeachingType_changeguidteachPlanId").attr("class","required");
			$("#changeTeachingType_stuCenterid").attr("class","required");
			$("#changeTeachingType_majorName").attr("class","required");
			
		}else if(obj.value=='11'){//休学
			if($("#studentstatus").val()=='13'){
				alertMsg.warn("当前学生的学籍状态为退学，只有在校生才能申请休学！");
				$('#changeType').val('');
				return ;
			}
			$("#changeTab [name^=88]").show();
			$("#changeTab [name='endDate']").html("复学日期:");
			$("#reason_common").show();
			$("#changeTab #A,#B,#C,#D,#E,#F,#G,#H").hide();
            updateBackSchoolDate();
			$("#pauseStudy_backSchoolDate").attr("class","required");
			
		}else if(obj.value=='13'){//退学
			if($("#studentstatus").val()=='13'){
				alertMsg.warn("当前学生的学籍状态已为退学，不可申请重复申请。");
				$('#changeType').val('');
				return ;
			}
			$("#changeTab [name^=77]").show();
			$("#reason_common").hide();
			$("#changeTab #A,#B,#C,#D,#E,#F,#G,#H").hide();
			
			$("#stopStudy_reason").attr("class","required");
			//$("#bankName").attr("class","required");
			//$("#cardNo").attr("class","required");
			//$("#bankAddress").attr("class","required");
		}else if(obj.value=='12'){
			if($("#studentstatus").val()=='12'){
				$("#changeTab [name^=22]").show();
				$("#reason_common").show();
				$("#changeTab #A,#B,#C,#D,#E,#F,#G,#H").hide();
				
				$("#backSchool_classesid").attr("class","required");
				$("#backSchool_changeguidteachPlanId").attr("class","required");
				$("#backSchool_schoolCenterid").attr("class","required");
				$("#backSchool_majorid").attr("class","required");
				$("#backSchool_backSchoolDate").attr("class","required");
			}else{
				alertMsg.warn("当前学生的学籍状态不为休学，不可申请复学的学籍异动。");
				$('#changeType').val('');
			}
		}else if(obj.value=='slowGraduation'){//缓毕业
			if($("#studentstatus").val()!='11'){
				alertMsg.warn("只有在校生才能申请缓毕业！");
				$('#changeType').val('');
				return ;
			}
			$("#changeTab [name^=88]").show();
			//$("#changeTab [name=88G]").hide();
			$("#changeTab td[name='endDate']").html("缓毕业期限:");
			$("#reason_common").show();
			$("#changeTab #A,#B,#C,#D,#E,#F,#G,#H").hide();
		}
	}
	
	function selfChangeApplyOnceMore(){//替学生异动
		var url = "${baseUrl}/edu3/register/stuchangeinfo/self/edit.html";
		navTab.openTab('RES_SCHOOL_SCHOOLROLL_CHANGE_EDIT', url, '申请学籍异动');
		//给异动列表页加上重载的设定
		navTab.reloadFlag("RES_SCHOOL_SCHOOLROLL_CHANGE");
	}
	//**转专业异动 方法组
	//选择专业_转专业
	function _selectMajor_new(){
		
		var gradeId   = $("#changeTab input[name='gradeId']").val();
		var classicId = $("#changeTab input[name='classicId']").val();
		
		var brSchoolId = $("#changeMajor_brschoolid").val(); 
		var teachType  = $("#changeMajor_TeachingTypeid").val();
		
		var html 	  = "<option value=''>请选择...</option>";
		$("#_majorid").html(html);
		
		if((""==brSchoolId || ""==gradeId)||(null==brSchoolId || null==gradeId)){
			alertMsg.warn("缺少异动学生的年级和办学单位信息。");
			return false;
		}
		
		if(""==teachType){
			alertMsg.warn("缺少异动学生的学习形式。");
			return false;
		}
		
		jQuery.ajax({
			data:"teachType="+teachType+"&gradeId="+gradeId+"&brSchoolId="+brSchoolId+"&classicId="+classicId,
			dataType:"json",
			url:"${baseUrl}/edu3/framework/register/stuchangeinfo/getInfosmajor.html",
			success:function(data){
				cleanhtml = "<option value=''>请选择...</option>";
				$("#changeMajor_classesid").html(cleanhtml);
				if(data.error && data.error!=""){
					alertMsg.warn(data.error);
					
				}else{				
					for(var i=0;i<data.majorList.length;i++){
						html += "<option value='"+data.majorList[i]['RESOURCEID']+"'>"+data.majorList[i]['MAJORNAME']+"</option>"
					}
					$("#_majorid").html(html);
				}
			}
		});
	}
	//选择班级_转专业
	function _selectClasses_new(obj){
		var gradeId   = $("#changeTab input[name='gradeId']").val();
		var classicId = $("#changeTab input[name='classicId']").val();
		
		var brSchoolId = $("#changeMajor_brschoolid").val(); 
		var teachType  = $("#changeMajor_TeachingTypeid").val();
		var majorId   = "";
		if(null==obj){
			majorId = $("#_majorid").val();
		}else{
			majorId = $(obj).val();
		}
		var html 	  = "<option value=''>请选择...</option>";
		$("#changeMajor_classesid").html(html);
		
		if((""==brSchoolId || ""==gradeId)||(null==brSchoolId || null==gradeId)){
			alertMsg.warn("请选择一个要异动的学生");
			return false;
		}
		if(""==teachType){
			alertMsg.warn("请选择所转专业的教学模式");
			return false;
		}
		jQuery.ajax({
			data:{teachType:teachType,gradeId:gradeId,brSchoolId:brSchoolId,classicId:classicId,majorId:majorId},
			dataType:"json",
			url:"${baseUrl}/edu3/framework/register/stuchangeinfo/getClasses.html",
			success:function(data){
				
				if(data.error && data.error!=""){
					alertMsg.warn(data.error);
					
				}else{				
					for(var i=0;i<data.classesList.length;i++){
						html += "<option value='"+data.classesList[i]['RESOURCEID']+"'>"+data.classesList[i]['CLASSESNAME']+"</option>";
					}
					$("#changeMajor_classesid").html(html);
				}
			}
		});
	}
	//选择教学计划_转专业
	function _selectTeachPlan_new(){
		var gradeId    = $("#changeTab input[name='gradeId']").val();
		var classicId  = $("#changeTab input[name='classicId']").val();
		
		var brSchoolId = $("#changeMajor_brschoolid").val(); 
		var teachType  = $("#changeMajor_TeachingTypeid").val();
		var majorId    = $("#_majorid").val();
		
		//班和教学计划目前没有关联，所以不用根据所选的班进行调整。
		var html 	  = "<option value=''>请选择</option>";
		$("#changeMajor_changeguidteachPlanId").html(html);
		if((""==brSchoolId || ""==gradeId)||(null==brSchoolId || null==gradeId)){
			alertMsg.warn("缺少异动对象的办学单位与年级数据");
			return false;
		}
		if(""==teachType){
			alertMsg.warn("缺少异动对象的教学模式数据");
			return false;
		}
		
		jQuery.ajax({
			data:"teachType="+teachType+"&gradeId="+gradeId+"&brSchoolId="+brSchoolId+"&classicId="+classicId+"&majorId="+majorId,
			dataType:"json",
			url:"${baseUrl}/edu3/framework/register/stuchangeinfo/getguidteachplan.html",
			success:function(data){
				if(data.error && data.error!=""){
					alertMsg.warn(data.error);
				}else{
					if(data.teachPlanList.length==0){
						alertMsg.warn("未找到相应的年度教学计划。");
					}
					for(var i=0;i<data.teachPlanList.length;i++){
						html += "<option value='"+data.teachPlanList[i]['RESOURCEID']+"'>"+data.teachPlanList[i]['TEACHPLANNAME']+"</option>";
					}
					$("#changeMajor_changeguidteachPlanId").html(html);
				}
			}
		});
	}
	//**复学异动方法组
	//复选异动初始化
	function initialize_backSchool(teachType,gradeId,brSchoolId,majorId,classes,classicId){
		var studentresourceid = $("#studentresourceid").val();
		var html 	  = "<option value=''>请选择...</option>";
		$("#backSchool_schoolCenterid").html(html);
		jQuery.ajax({
			data:"studentresourceid="+studentresourceid+"&teachType="+teachType,
			dataType:"json",
			url:"${baseUrl}/edu3/framework/register/stuchangeinfo/getSchool.html",
			success:function(data){
				if(data.error && data.error!=""){
					alertMsg.warn(data.error);
				}else{				
					for(var i=0;i<data.schoolList.length;i++){
						if(brSchoolId == data.schoolList[i]['RESOURCEID']){
							html += "<option value='"+data.schoolList[i]['RESOURCEID']+"' selected = 'selected' >"+data.schoolList[i]['UNITNAME']+"</option>";
						}else{
							html += "<option value='"+data.schoolList[i]['RESOURCEID']+"'>"+data.schoolList[i]['UNITNAME']+"</option>";
						}
					}
					$("#backSchool_schoolCenterid").html(html);
					$("#backSchool_gradeName option[value='"+gradeId+"']").attr("selected","selected");
					html 	  = "<option value=''>请选择...</option>";
					$("#backSchool_majorid").html(html);
					
					jQuery.ajax({
						data:"teachType="+teachType+"&gradeId="+gradeId+"&brSchoolId="+brSchoolId+"&classicId="+classicId,
						dataType:"json",
						url:"${baseUrl}/edu3/framework/register/stuchangeinfo/getInfosmajor.html",
						success:function(data){
							cleanhtml = "<option value=''>请选择...</option>";
							$("#backSchool_majorid").html(cleanhtml);
							if(data.error && data.error!=""){
								alertMsg.warn(data.error);
							}else{				
								for(var i=0;i<data.majorList.length;i++){
									if(majorId == data.majorList[i]['RESOURCEID']){
										html += "<option value='"+data.majorList[i]['RESOURCEID']+"' selected = 'selected' >"+data.majorList[i]['MAJORNAME']+"</option>";									
									}else{
										html += "<option value='"+data.majorList[i]['RESOURCEID']+"'>"+data.majorList[i]['MAJORNAME']+"</option>";
									}
									
								}
								$("#backSchool_majorid").html(html);
								html 	  = "<option value=''>请选择...</option>";
								$("#backSchool_classesid").html(html);
								jQuery.ajax({
									data:{teachType:teachType,gradeId:gradeId,brSchoolId:brSchoolId,classicId:classicId,majorId:majorId},
									dataType:"json",
									url:"${baseUrl}/edu3/framework/register/stuchangeinfo/getClasses.html",
									success:function(data){
										if(data.error && data.error!=""){
											alertMsg.warn(data.error);
										}else{				
											for(var i=0;i<data.classesList.length;i++){
												if(classes==data.classesList[i]['RESOURCEID']){
													html += "<option value='"+data.classesList[i]['RESOURCEID']+"' selected = 'selected' >"+data.classesList[i]['CLASSESNAME']+"</option>"
												}else{
													html += "<option value='"+data.classesList[i]['RESOURCEID']+"'>"+data.classesList[i]['CLASSESNAME']+"</option>"
												}
											}
											$("#backSchool_classesid").html(html);
											html 	  = "<option value=''>请选择...</option>";
											$("#backSchool_changeguidteachPlanId").html(html);
											jQuery.ajax({
												data:"teachType="+teachType+"&gradeId="+gradeId+"&brSchoolId="+brSchoolId+"&classicId="+classicId+"&majorId="+majorId,
												dataType:"json",
												url:"${baseUrl}/edu3/framework/register/stuchangeinfo/getguidteachplan.html",
												success:function(data){
													if(data.error && data.error!=""){
														alertMsg.warn(data.error);
													}else{				
														for(var i=0;i<data.teachPlanList.length;i++){
															html += "<option value='"+data.teachPlanList[i]['RESOURCEID']+"'>"+data.teachPlanList[i]['TEACHPLANNAME']+"</option>";
														}
														$("#backSchool_changeguidteachPlanId").html(html);
													}
												}
											});
										}
									}
								});
							}
						}
					});
				}
			}
		});
	}
	
	//选择教学站_复学异动
	function selectSchool_backSchool(){
		var studentresourceid = $("#studentresourceid").val();
		var teachType  = $("#backSchool_teachingType").val();
		var html 	  = "<option value=''>请选择...</option>";
		$("#backSchool_schoolCenterid").html(html);
		jQuery.ajax({
			data:"studentresourceid="+studentresourceid+"&teachType="+teachType,
			dataType:"json",
			url:"${baseUrl}/edu3/framework/register/stuchangeinfo/getSchool.html",
			success:function(data){
				if(data.error && data.error!=""){
					alertMsg.warn(data.error);
				}else{				
					for(var i=0;i<data.schoolList.length;i++){
						html += "<option value='"+data.schoolList[i]['RESOURCEID']+"'>"+data.schoolList[i]['UNITNAME']+"</option>";
					}
					$("#backSchool_schoolCenterid").html(html);
				}
			}
		});
	}
	//选择专业_复学异动
	function selectMajor_backschool(){
		var teachType  = $("#backSchool_teachingType").val();
		//复学异动连年级都可以修改
		var gradeId    = $("#backSchool_gradeName").val();
		//层次是取原来的数据
		var classicId  = $("#changeTab input[name='classicId']").val();
		var brSchoolId = $("#backSchool_schoolCenterid").val(); 
		
		var html 	  = "<option value=''>请选择...</option>";
		$("#backSchool_majorid").html(html);
		if(""==gradeId||null==gradeId){
			alertMsg.warn("请先选择一个年级.");
			return false ;
		}
		if(""==brSchoolId ||null==brSchoolId ){
			alertMsg.warn("无法获取教学站数据.");
			return false;
		}
		if(""==teachType){
			alertMsg.warn("无法获取学习形式数据.");
			return false;
		}
		jQuery.ajax({
			data:"teachType="+teachType+"&gradeId="+gradeId+"&brSchoolId="+brSchoolId+"&classicId="+classicId,
			dataType:"json",
			url:"${baseUrl}/edu3/framework/register/stuchangeinfo/getInfosmajor.html",
			success:function(data){
				cleanhtml = "<option value=''>请选择...</option>";
				$("#backSchool_majorid").html(cleanhtml);
				if(data.error && data.error!=""){
					alertMsg.warn(data.error);
				}else{				
					for(var i=0;i<data.majorList.length;i++){
						html += "<option value='"+data.majorList[i]['RESOURCEID']+"'>"+data.majorList[i]['MAJORNAME']+"</option>";
					}
					$("#backSchool_majorid").html(html);
				}
			}
		});
	}
	//选择班级_复学异动
	function _selectClasses_backschool(obj){
		var gradeId   = $("#backSchool_gradeName").val();
		var classicId = $("#changeTab input[name='classicId']").val();
		
		var brSchoolId = $("#backSchool_schoolCenterid").val(); 
		var teachType  = $("#backSchool_teachingType").val();
		var majorId   = "";
		if(null==obj){
			majorId = $("#backSchool_majorid").val();
		}else{
			majorId = $(obj).val();
		}
		var html 	  = "<option value=''>请选择...</option>";
		$("#backSchool_classesid").html(html);
		if((""==brSchoolId)||(null==brSchoolId) ){
			alertMsg.warn("无法获取办学单位数据.");
			return false;
		}
		if((""==gradeId)||(null==gradeId)){
			alertMsg.warn("无法获取年级数据.");
			return false;
		}
		if(""==teachType){
			alertMsg.warn("请选择教学模式.");
			return false;
		}
		jQuery.ajax({
			data:{teachType:teachType,gradeId:gradeId,brSchoolId:brSchoolId,classicId:classicId,majorId:majorId},
			dataType:"json",
			url:"${baseUrl}/edu3/framework/register/stuchangeinfo/getClasses.html",
			success:function(data){
				if(data.error && data.error!=""){
					alertMsg.warn(data.error);
				}else{				
					for(var i=0;i<data.classesList.length;i++){
						html += "<option value='"+data.classesList[i]['RESOURCEID']+"'>"+data.classesList[i]['CLASSESNAME']+"</option>"
					}
					$("#backSchool_classesid").html(html);
				}
			}
		});
	}
	//选择教学计划_复学异动
	function selectTeachPlan_backSchool(obj){
		var gradeId   = $("#backSchool_gradeName").val();
		var classicId = $("#changeTab input[name='classicId']").val();
		var brSchoolId = $("#backSchool_schoolCenterid").val(); 
		var teachType  = $("#backSchool_teachingType").val();
		var majorId    = $("#backSchool_majorid").val();
		//班和教学计划目前没有关联，所以不用根据所选的班进行调整。
		var html 	  = "<option value=''>请选择...</option>";
		$("#backSchool_changeguidteachPlanId").html(html);
		if((""==brSchoolId)||(null==brSchoolId) ){
			alertMsg.warn("无法获取办学单位数据.");
			return false;
		}
		if((""==gradeId)||(null==gradeId)){
			alertMsg.warn("无法获取年级数据.");
			return false;
		}
		if(""==teachType){
			alertMsg.warn("请选择教学模式.");
			return false;
		}
		jQuery.ajax({
			data:"teachType="+teachType+"&gradeId="+gradeId+"&brSchoolId="+brSchoolId+"&classicId="+classicId+"&majorId="+majorId,
			dataType:"json",
			url:"${baseUrl}/edu3/framework/register/stuchangeinfo/getguidteachplan.html",
			success:function(data){
				if(data.error && data.error!=""){
					alertMsg.warn(data.error);
				}else{				
					for(var i=0;i<data.teachPlanList.length;i++){
						html += "<option value='"+data.teachPlanList[i]['RESOURCEID']+"'>"+data.teachPlanList[i]['TEACHPLANNAME']+"</option>";
					}
					$("#backSchool_changeguidteachPlanId").html(html);
				}
			}
		});
	}
	
	
	//**更改办学单位方法组
	//选择教学站_更改办学单位
	function selectSchool_changeSchool(){
		var obj 				= 		document.getElementById('changeType'); //异动类型
		var major 				= 		$("#majorId").val(); //专业
		//学习形式
		var teachType  			= 		$("#changeSchool_TeachingTypeid").val();
		//层次
		var classicId 			= 		$("#classicId").val();
		//年级
		var gradeId 			= 		$("#gradeId").val();
		//教学站
		var schoolid 			=       $("#changeBeforeBrSchoolId").val();
		//学生id
		var studentresourceid 	= 		$("#studentresourceid").val();
		var html 	  			= 		"<option value=''>请选择...</option>";
		$("#changeSchool_schoolCenterid").html(html);
		jQuery.ajax({
			data:"studentresourceid="+studentresourceid+"&changeType="+obj.value+"&major="+major+"&teachType="+teachType+"&classicId="+classicId+"&gradeId="+gradeId+"&schoolid="+schoolid,
			dataType:"json",
			url:"${baseUrl}/edu3/framework/register/stuchangeinfo/getSchool.html",
			success:function(data){
				if(data.error && data.error!=""){
					alertMsg.warn(data.error);
				}else{				
					for(var i=0;i<data.schoolList.length;i++){
						html += "<option value='"+data.schoolList[i]['RESOURCEID']+"'>"+data.schoolList[i]['UNITNAME']+"</option>";
					}
					$("#changeSchool_schoolCenterid").html(html);
				}
			}
		});
	}
	
	//选择专业_更改办学单位
	function _selectMajor_changeSchool(){
		var gradeId   = $("#changeTab input[name='gradeId']").val();
		var classicId = $("#changeTab input[name='classicId']").val();
		var brSchoolId = $("#changeSchool_schoolCenterid").val(); 
		var teachType  = $("#changeSchool_TeachingTypeid").val();
		var changeType = $("#changeType").val();
		var major;
		if("82"==changeType){
			major = $("#majorId").val(); 
		}
		var html 	  = "<option value=''>请选择...</option>";
	//	$("#changeSchool_majorName").html(html);
		
		if((""==brSchoolId || ""==gradeId)||(null==brSchoolId || null==gradeId)){
			alertMsg.warn("缺少异动学生的年级和办学单位信息。");
			return false;
		}
		if(""==teachType){
			alertMsg.warn("缺少异动学生的学习形式。");
			return false;
		}
		var schoolCode = "${schoolCode}";
		
		jQuery.ajax({
			data:"teachType="+teachType+"&gradeId="+gradeId+"&brSchoolId="+brSchoolId+"&classicId="+classicId+"&major="+major+"&changeType="+changeType,
			dataType:"json",
			url:"${baseUrl}/edu3/framework/register/stuchangeinfo/getInfosmajor.html",
			success:function(data){
//				cleanhtml = "<option value=''>请选择...</option>";
//				$("#changeSchool_classesid").html(cleanhtml);
				if(data.error && data.error!=""){
					alertMsg.warn(data.error);
				}else{	
			/*		if(data.majorList.length==1){
						html = "<span name='changeSchool_schoolCenterid' >"+data.majorList[0]['MAJORNAME']+"</span>";
					}
					*/
					for(var i=0;i<data.majorList.length;i++){
						$('#changeSchool_majorName').val(data.majorList[0]['RESOURCEID']);
					//	html += "<option value='"+data.majorList[i]['RESOURCEID']+"'>"+data.majorList[i]['MAJORNAME']+"</option>"
					}
					
					
					if(changeType==82){
					//	document.getElementById("changeSchool_classesid").style.display = "none";
					//	$("#changeSchool_classesid").attr("class","");
					//	document.getElementById("changeSchool_classesid_f").value="请自行安排班级";
						if(schoolCode!="11846"){
							document.getElementById("changeSchool_changeguidteachPlanId").style.display = "none";
							$("#changeSchool_changeguidteachPlanId").attr("class","");
						}else{
							_selectTeachPlan_changeSchool();
						}
					}
			//		$("#changeSchool_majorName").html(html);
			//		$("#changeSchool_majorName").val(data.majorList[i]['MAJORNAME']);
				}
			}
		});
	}
	//选择班级_更改办学单位
	function _selectClasses_changeSchool(obj){
		var studentresourceid = $("#studentresourceid").val();
		var gradeId    = $("#changeTab input[name='gradeId']").val();
		var classicId  = $("#changeTab input[name='classicId']").val();
		var brSchoolId = $("#changeSchool_schoolCenterid").val(); 
		var majorId    = $(obj).val();
		var html 	  = "<option value=''>请选择...</option>";
	//	$("#changeSchool_classesid").html(html);
		if((""==brSchoolId || ""==gradeId)||(null==brSchoolId || null==gradeId)){
			alertMsg.warn("请选择一个要异动的学生");
			return false;
		}
		jQuery.ajax({
			data:{gradeId:gradeId,brSchoolId:brSchoolId,classicId:classicId,studentresourceid:studentresourceid,majorId:majorId},
			dataType:"json",
			url:"${baseUrl}/edu3/framework/register/stuchangeinfo/getClasses.html",
			success:function(data){
				if(data.error && data.error!=""){
					alertMsg.warn(data.error);
				}else{				
			//		for(var i=0;i<data.classesList.length;i++){
			//			html += "<option value='"+data.classesList[i]['RESOURCEID']+"'>"+data.classesList[i]['CLASSESNAME']+"</option>"
			//		}
			//	    $("#changeSchool_classesid").html(html);
				}
			}
		});
	}
	//选择教学计划_更改办学单位
	function _selectTeachPlan_changeSchool(){
		var studentresourceid = $("#studentresourceid").val();
		var brSchoolId = $("#changeSchool_schoolCenterid").val(); 
		var majorId    = $("#changeSchool_majorName").val();
		var teachType    = $("#changeSchool_TeachingTypeid").val();
		//班和教学计划目前没有关联，所以不用根据所选的班进行调整。
		var html 	  = "<option value=''>请选择</option>";
		$("#changeSchool_changeguidteachPlanId").html(html);
		if(""==studentresourceid||null==studentresourceid){
			alertMsg.warn("缺少异动对象");
			return false;
		}		
		jQuery.ajax({
			data:"brSchoolId="+brSchoolId+"&studentresourceid="+studentresourceid+"&majorId="+majorId+"&teachType="+teachType,
			dataType:"json",
			url:"${baseUrl}/edu3/framework/register/stuchangeinfo/getguidteachplan.html",
			success:function(data){
				if(data.error && data.error!=""){
					alertMsg.warn(data.error);
				}else{
					if(data.teachPlanList.length==0){
						alertMsg.warn("未找到相应的年度教学计划。");
					}
					for(var i=0;i<data.teachPlanList.length;i++){
						html += "<option value='"+data.teachPlanList[i]['RESOURCEID']+"'>"+data.teachPlanList[i]['TEACHPLANNAME']+"</option>";
					}
					$("#changeSchool_changeguidteachPlanId").html(html);
				}
			}
		});
	}
	
	//**转学习形式方法组
	//选择教学站_更改办学单位
	function selectSchool_changeTeachingType(){
		var studentresourceid = $("#studentresourceid").val();
		var teachType  = $("#changeTeachingType_teachingType").val();
		var html 	  = "<option value=''>请选择...</option>";
		$("#changeTeachingType_stuCenterid").html(html);
		$("#changeTeachingType_majorName").html(html);
		if(""==teachType){
			alertMsg.warn("请先选择学习形式。");
			return false;
		}
		jQuery.ajax({
			data:"studentresourceid="+studentresourceid+"&teachType="+teachType,
			dataType:"json",
			url:"${baseUrl}/edu3/framework/register/stuchangeinfo/getSchool.html",
			success:function(data){
				if(data.error && data.error!=""){
					alertMsg.warn(data.error);
				}else{				
					for(var i=0;i<data.schoolList.length;i++){
						html += "<option value='"+data.schoolList[i]['RESOURCEID']+"'>"+data.schoolList[i]['UNITNAME']+"</option>";
					}
					$("#changeTeachingType_stuCenterid").html(html);
				}
			}
		});
	}
	// 更改教学点_先选择学习形式再选择教学点（广外）
	function changeSchool_changeTeachingType(){
		var studentId = $("#studentresourceid").val();
		if(studentId==""){
			alertMsg.warn("请先填写学号，并点击查询！");
			return;
		}
		selectSchool_changeSchool();
	}
	// 转专业_先选择学习形式再选择教学点（广外）
	function changeMajor_changeTeachingType(){
		var studentId = $("#studentresourceid").val();
		if(studentId==""){
			alertMsg.warn("请先填写学号，并点击查询！");
			return;
		}
		_selectMajor_new();
	}
	
	//选择专业_转学习形式
	function _selectMajor_changeTeachingType(){
		var gradeId    = $("#changeTab input[name='gradeId']").val();
		var classicId  = $("#changeTab input[name='classicId']").val();
		var brSchoolId = $("#changeTeachingType_stuCenterid").val(); 
		var teachType  = $("#changeTeachingType_teachingType").val();
		var html 	  = "<option value=''>请选择...</option>";
		$("#changeTeachingType_majorName").html(html);
		
		if((""==brSchoolId || ""==gradeId)||(null==brSchoolId || null==gradeId)){
			alertMsg.warn("缺少异动学生的年级和办学单位信息。");
			return false;
		}
		if(""==teachType){
			alertMsg.warn("缺少异动学生的教学方式。");
			return false;
		}
		
		jQuery.ajax({
			data:"teachType="+teachType+"&brSchoolId="+brSchoolId+"&gradeId="+gradeId+"&classicId="+classicId,
			dataType:"json",
			url:"${baseUrl}/edu3/framework/register/stuchangeinfo/getInfosmajor.html",
			success:function(data){
				cleanhtml = "<option value=''>请选择...</option>";
				$("#changeTeachingType_classesid").html(cleanhtml);
				if(data.error && data.error!=""){
					alertMsg.warn(data.error);
				}else{				
					for(var i=0;i<data.majorList.length;i++){
						html += "<option value='"+data.majorList[i]['RESOURCEID']+"'>"+data.majorList[i]['MAJORNAME']+"</option>"
					}
					$("#changeTeachingType_majorName").html(html);
				}
			}
		});
	}
	
	//选择班级_转学习形式
	function _selectClasses_changeTeachingType(obj){
		var studentresourceid = $("#studentresourceid").val(); 
		var teachType  = $("#changeTeachingType_teachingType").val();
		var brSchoolId = $("#changeTeachingType_stuCenterid").val(); 
		var majorId    = $(obj).val();
		var html 	  = "<option value=''>请选择...</option>";
		$("#changeTeachingType_classesid").html(html);
		if(""==studentresourceid||null==studentresourceid){
			alertMsg.warn("缺少异动对象");
			return false;
		}
		if(""==teachType||null==teachType){
			alertMsg.warn("请先选择学习方式");
			return false;
		}
		jQuery.ajax({
			data:{studentresourceid:studentresourceid,teachType:teachType,majorId:majorId,brSchoolId:brSchoolId},
			dataType:"json",
			url:"${baseUrl}/edu3/framework/register/stuchangeinfo/getClasses.html",
			success:function(data){
				if(data.error && data.error!=""){
					alertMsg.warn(data.error);
				}else{				
					for(var i=0;i<data.classesList.length;i++){
						html += "<option value='"+data.classesList[i]['RESOURCEID']+"'>"+data.classesList[i]['CLASSESNAME']+"</option>"
					}
					$("#changeTeachingType_classesid").html(html);
				}
			}
		});
	}
	//选择教学计划_更改学习形式
	function _selectTeachPlan_changeTeachingType(){
		var studentresourceid = $("#studentresourceid").val();
		var changeTeachingType_classesid = $("#changeTeachingType_classesid").val();
		var changeTeachingType_teachingType = $("#changeTeachingType_teachingType").val();
		var brSchoolId = $("#changeTeachingType_stuCenterid").val(); 
		var majorid    = $("#changeTeachingType_majorName").val();
		var html 	  = "<option value=''>请选择</option>";
		if(""==changeTeachingType_classesid||null==changeTeachingType_classesid){
			$("#changeTeachingType_changeguidteachPlanId").html(html);
			alertMsg.warn("请先选择一个班级");
			return false ;
		}
		if(""==studentresourceid||null==studentresourceid){
			alertMsg.warn("缺少异动对象");
			return false;
		}		
		jQuery.ajax({
			data:"studentresourceid="+studentresourceid+"&teachType="+changeTeachingType_teachingType+"&majorId="+majorid+"&brSchoolId="+brSchoolId,
			dataType:"json",
			url:"${baseUrl}/edu3/framework/register/stuchangeinfo/getguidteachplan.html",
			success:function(data){
				if(data.error && data.error!=""){
					alertMsg.warn(data.error);
				}else{
					if(data.teachPlanList.length==0){
						alertMsg.warn("未找到相应的年度教学计划。");
					}
					for(var i=0;i<data.teachPlanList.length;i++){
						html += "<option value='"+data.teachPlanList[i]['RESOURCEID']+"'>"+data.teachPlanList[i]['TEACHPLANNAME']+"</option>";
					}
					$("#changeTeachingType_changeguidteachPlanId").html(html);
				}
			}
		});
	}
	
	//检查复学成绩
	function checkForReinstate(){
		var obj = document.getElementById('changeType');
		var stuNum = $("#stuChangeForStu input[id='stuNum']").val();
		var stuName = $("#changeTab input[name='stuName']").val();
		var teachGuidePlan = $("#backSchool_changeguidteachPlanId").val();
		var brSchool = $("#backSchool_schoolCenterid").val();
		var isTrue = "${schoolCode eq '12962'}";
		if(isTrue==='true'||isTrue){
			if(!$("#attachId").val()){
				alertMsg.warn("您还没有上传学籍异动申请的电子材料，请点击【上传附件】上传电子材料。");
				return false;
			}
		}
		if(obj.value=='12'){
			$.pdialog.open('${baseUrl}/edu3/register/stuchangeinfo/self/viewExamResult.html?studentNo='+stuNum+'&teachGuidePlan='+teachGuidePlan+'&brSchool='+brSchool, 'RES_SCHOOL_SCHOOLROLL_CHANGE_VIEW', '查看复学成绩详情', {width: 1100, height: 600});
		}else{
			
			$("#stuchangeinfoForm").submit();
		}
		
	}
	
	//提交
	function submit(){
		$("#stuchangeinfoForm").submit();
	}
	
	//验证银行卡号
	function validateCardNo(cardNo){
		if(cardNo!=''){
			if(!(/^([1-9]{1})(\d{14}|\d{18})$/.test(cardNo))){
				$("#validateCardNo").html("银行卡号不符合要求，请仔细核对！");
			}else{
				$.ajax({
					type:"post",
					url:baseUrl+"/edu3/register/stuchangeinfo/validateCardNo.html",
					data:{"cardNo":cardNo},
					dataType:"json",
					success:function(data){
						if(data.statusCode==200){
							var isCorrect = data.isCorrect;
							$("#getNameByCardNo").html(data.bankName);
							if(!isCorrect){
								$("#validateCardNo").html("银行卡号不正确，请仔细核对！");
							}else{
								$("#validateCardNo").html("");
							}
						}else{
							alertMsg.warn(data.message);
						}
					}
				});
			}
		}
	}

	//根据申请日期自动更新复学日期，默认加1年
	function updateBackSchoolDate() {
	    var applicationDate = $("#applicationDate_1").val();
        var year = parseInt(applicationDate.substring(0,4))+1;
        var backSchoolDate = year + applicationDate.substring(4,10);
		$("#pauseStudy_backSchoolDate").val(backSchoolDate);
    }
	</script>
</head>
<body>
	<%/* 本页面无法实现编辑学籍异动的功能，敬请留意 */%>
	<h2 class="contentTitle">申请学籍异动</h2>
	<div class="page">
		<div class="pageContent">
			<form method="post"
				action="${baseUrl}/edu3/register/stuchangeinfo/self/save1.html"
				class="pageForm" id="stuchangeinfoForm"
				onsubmit="return validateCallback(this);">
				<input type="hidden" id="resourceid" name="resourceid"
					value="${stuChangeInfo.resourceid }" /> <input type="hidden"
					id="studentresourceid" name="studentresourceid"
					value="${stuChangeInfo.studentInfo.resourceid }" /> <input
					type=hidden name=wf_id value="${stuChangeInfo.wf_id }" /> <input
					type=hidden name=APP_WF_ID value="${stuChangeInfo.wf_id }" /> <input
					type="hidden" name="applicationDate"
					value="<fmt:formatDate value="${stuChangeInfo.applicationDate }" pattern="yyyy-MM-dd HH:mm:ss"/>" />
				<input type="hidden" id="CM" name="CM" value="">
				<div class="pageFormContent" layoutH="97">
					<div class="tabs">
						<div class="tabsHeader">
							<div class="tabsHeaderContent">
								<ul>
									<li class="selected"><a href="#"><span>异动信息</span></a></li>
								</ul>
							</div>
						</div>
						<div class="tabsContent" style="height: 100%;">
							<div>
								<table class="form" id="changeTab">
									<%-- 异动前信息 --%>
									<input type="hidden" id="changeBeforeLearingStyle"
										name="changeBeforeLearingStyle"
										value="${stuChangeInfo.changeBeforeLearingStyle }" />
									<input type="hidden" id="changeBeforeTeachingGuidePlanId"
										name="changeBeforeTeachingGuidePlanId"
										value="${stuChangeInfo.changeBeforeTeachingGuidePlan.resourceid }" />
									<input type="hidden" id="changeBeforeBrSchoolId"
										name="changeBeforeBrSchoolId"
										value="${stuChangeInfo.changeBeforeBrSchool.resourceid }" />
									<input type="hidden" id="schoolType" name="schoolType" value="" />
									<input type="hidden" id="studentstatus" name="studentstatus"
										value="" />
									<input type="hidden" id="majorId" name="majorId"
										value="${stuChangeInfo.studentInfo.major.resourceid }" />
									<input type="hidden" id="gradeId" name="gradeId"
										value="${stuChangeInfo.studentInfo.grade.resourceid }" />
									<input type="hidden" id="classicId" name="classicId"
										value="${stuChangeInfo.studentInfo.classic.resourceid }" />
									<input type="hidden" id="classesId" name="classesId"
										value="${stuChangeInfo.studentInfo.classes.resourceid }" />
									<tr>
										<td width="10%">变动类别:</td>
										<td width="22%"><gh:select id="changeType"
												name="changeType" value="${stuChangeInfo.changeType }"
												dictionaryCode="CodeStudentStatusChange"
												filtrationStr="12,81,11,13,23,82,slowGraduation"
												style="width:185px;" classCss="required"
												onchange="choiceChange();" /><font color=red>*</font></td>
										<td width="10%">学号:</td>
										<td width="25%" id="stuChangeForStu"><input type="text"
											id="stuNum" name="stuNum" style="width: 50%"
											value="${stuChangeInfo.studentInfo.studyNo }"
											class="required" /> 
											<c:if
												test="${empty stuChangeInfo.resourceid }">
												<button type="button" class="button" onclick="queryStuInfo();">查询</button>
											</c:if></td>
										<td width="10%">姓名:</td>
										<td width="22%"><input type="text" id="stuName"
											name="stuName" style="width: 50%"
											value="${stuChangeInfo.studentInfo.studentName}"
											readonly="readonly" /></td>
									</tr>

									<tr id="D">
										<td width="10%">原学习形式:</td>
										<td width="22%"><span id="teachingtype">${ghfn:dictCode2Val('CodeTeachingType',stuChangeInfo.changeBeforeLearingStyle)}</span>
										</td>
										<td width="10%">异动后学习形式:</td>
										<td width="25%"><span>${ghfn:dictCode2Val('CodeTeachingType',stuChangeInfo.changeTeachingType)}</span>
										</td>
										<td width="10%"></td>
										<td width="22%"></td>
									</tr>
									<tr style="display: none" name="22D">
										<td width="10%">原学习形式:</td>
										<td width="22%"><span id="teachingtype">${ghfn:dictCode2Val('CodeTeachingType',stuChangeInfo.changeBeforeLearingStyle)}</span>
										</td>
										<td width="10%">复学学习形式:</td>
										<td width="25%"><input type="hidden"
											id="backSchool_teachingType" name="backSchool_teachingType"
											value="${stuChangeInfo.changeTeachingType}" /> <span
											id="backSchool_teachingTypeStr">${ghfn:dictCode2Val('CodeTeachingType',stuChangeInfo.changeTeachingType)}</span>

										</td>
										<td width="10%"></td>
										<td width="22%"></td>
									</tr>
									<tr style="display: none" name="33D">
										<td width="10%">原学习形式:</td>
										<td width="22%"><span id="teachingtype">${ghfn:dictCode2Val('CodeTeachingType',stuChangeInfo.changeBeforeLearingStyle)}</span>
										</td>
										<td width="10%">转专业学习形式:</td>
										<td width="25%">
											<%/*<gh:select dictionaryCode="CodeTeachingType" name="changeMajor_TeachingType" style="float:none;" onchange="_selectMajor_new()"/>*/%>
											<c:choose>
												<c:when test="${schoolCode eq '11846' }">
													<gh:select classCss="required" id="changeMajor_TeachingTypeid"
															name="changeMajor_TeachingTypeid" filtrationStr="2,4,7" dictionaryCode="CodeTeachingType" 
															onchange="changeMajor_changeTeachingType()" style="width:260px;" /><font color=red>*</font>
												</c:when>
												<c:otherwise>
													<input type="hidden" id="changeMajor_TeachingTypeid" name="changeMajor_TeachingTypeid" value="${stuChangeInfo.changeTeachingType}" /> 
													<span id="changeMajor_TeachingType">${ghfn:dictCode2Val('CodeTeachingType',stuChangeInfo.changeTeachingType)}</span>
												</c:otherwise>
											</c:choose>
										</td>
										<td width="10%"></td>
										<td width="22%"></td>
									</tr>
									<tr style="display: none" name="55D">
										<td width="10%">原学习形式:</td>
										<td width="22%">
											<span id="teachingtype">${ghfn:dictCode2Val('CodeTeachingType',stuChangeInfo.changeBeforeLearingStyle)}</span>
										</td>
										<td width="10%">异动后学习形式:</td>
										<td width="25%">
											<c:choose>
												<c:when test="${schoolCode eq '11846' }">
													<gh:select classCss="required" id="changeSchool_TeachingTypeid"
														name="changeSchool_TeachingTypeid" filtrationStr="2,4,7" dictionaryCode="CodeTeachingType" 
														onchange="changeSchool_changeTeachingType()" style="width:260px;" /><font color=red>*</font>
												</c:when>
												<c:otherwise>
													<input type="hidden" id="changeSchool_TeachingTypeid" name="changeSchool_TeachingTypeid" value="${stuChangeInfo.changeTeachingType}" /> 
													<span id="changeSchool_TeachingType">${ghfn:dictCode2Val('CodeTeachingType',stuChangeInfo.changeTeachingType)}</span>
												</c:otherwise>
											</c:choose>
										</td>
										<td width="10%"></td>
										<td width="22%"></td>
									</tr>
									<tr style="display: none" name="66D">
										<td width="10%">原学习形式:</td>
										<td width="22%"><span id="teachingtype">${ghfn:dictCode2Val('CodeTeachingType',stuChangeInfo.changeBeforeLearingStyle)}</span>
										</td>
										<td width="10%">转学习形式:</td>
										<td width="25%"><gh:select classCss="required"
												id="changeTeachingType_teachingType"
												name="changeTeachingType_teachingType" filtrationStr="2,4,7"
												dictionaryCode="CodeTeachingType"
												value="${stuChangeInfo.changeLearningStyle }"
												onchange="selectSchool_changeTeachingType()"
												style="width:260px;" /><font color=red>*</font></td>
										<td width="10%"><font color="red">注:</font></td>

										<td width="22%"><font color="red">转学习形式异动，请先选择新的学习形式。</font>
										</td>
									</tr>
									<tr style="display: none" name="77D">
										<td width="10%">原学习形式:</td>
										<td width="22%"><span id="teachingtype">${ghfn:dictCode2Val('CodeTeachingType',stuChangeInfo.changeBeforeLearingStyle)}</span>
										</td>
										<td width="10%"></td>
										<td width="25%"></td>
										<td width="10%"></td>
										<td width="22%"></td>
									</tr>
									<tr style="display: none" name="88D">
										<td width="10%">原学习形式:</td>
										<td width="22%"><span id="teachingtype">${ghfn:dictCode2Val('CodeTeachingType',stuChangeInfo.changeBeforeLearingStyle)}</span>
										</td>
										<td width="10%"></td>
										<td width="25%"></td>
										<td width="10%"></td>
										<td width="22%"></td>
									</tr>

									<tr id="A">
										<td width="10%">原学院:</td>
										<td width="22%"><input type="hidden" id="stuCenter"
											name="stuCenter"
											value="${stuChangeInfo.changeBeforeBrSchool.resourceid }" />
											<span id="stuCenterName">${stuChangeInfo.changeBeforeBrSchool.unitName }</span>
										</td>
										<td width="10%">异动后学院:</td>
										<td width="25%"><span>${stuChangeInfo.changeBrschool.unitName}</span>
										</td>
										<td width="10%"></td>
										<td width="22%"></td>
									</tr>
									<tr style="display: none" name="22A">
										<td width="10%">原学院:</td>
										<td width="22%"><input type="hidden" id="stuCenter"
											name="stuCenter"
											value="${stuChangeInfo.changeBeforeBrSchool.resourceid}" /> <span
											id="stuCenterName">${stuChangeInfo.changeBeforeBrSchool.unitName }</span>
										</td>
										<td width="10%">复学学院:</td>
										<td width="25%"><select name="backSchool_schoolCenterid"
											id="backSchool_schoolCenterid" size="1"
											style="float: none; width: 260px;" class="required"
											onchange="selectMajor_backschool()"><option value="">请选择</option></select><font
											color=red>*</font></td>
										<td width="10%"></td>
										<td width="22%"></td>
									</tr>
									<tr style="display: none" name="33A">
										<td width="10%">原学院:</td>
										<td width="22%"><input type="hidden" id="stuCenter"
											name="stuCenter"
											value="${stuChangeInfo.changeBeforeBrSchool.resourceid }" />
											<span id="stuCenterName">${stuChangeInfo.changeBeforeBrSchool.unitName }</span>
										</td>
										<td width="10%">转专业学院:</td>
										<td width="25%">
											<%/*<gh:selectModel condition=" unitType='brSchool' " name="changeMajor_brschool" style="float:none;" id="changeMajor_brschoolid" bindValue="resourceid" displayValue="unitName" modelClass="com.hnjk.security.model.OrgUnit" onchange="_selectMajor_new()"/>*/%>
											<input type="hidden" id="changeMajor_brschoolid"
											name="changeMajor_brschoolid"
											value="${stuChangeInfo.changeBrschool.resourceid}" /> <span
											id="changeMajor_brschool">${stuChangeInfo.changeBrschool.unitName}</span>
										</td>
										<td width="10%"></td>
										<td width="22%"></td>
									</tr>
									<tr style="display: none" name="55A">
										<td width="10%">原学院:</td>
										<td width="22%"><input type="hidden" id="stuCenter"
											name="stuCenter"
											value="${stuChangeInfo.changeBeforeBrSchool.resourceid}" /> <span
											id="stuCenterName">${stuChangeInfo.changeBeforeBrSchool.unitName}</span>
										</td>
										<td width="10%">异动后学院:</td>
										<td width="25%"><select
											name="changeSchool_schoolCenterid"
											id="changeSchool_schoolCenterid" size="1"
											style="float: none; width: 260px;" class="required"
											onchange="_selectMajor_changeSchool()"><option
													value="">请选择</option></select><font color=red>*</font></td>
										<td width="10%"></td>
										<td width="22%"></td>
									</tr>
									<tr style="display: none" name="66A">
										<td width="10%">原学院:</td>
										<td width="22%"><input type="hidden" id="stuCenter"
											name="stuCenter"
											value="${stuChangeInfo.changeBeforeBrSchool.resourceid}" /> <span
											id="stuCenterName">${stuChangeInfo.changeBeforeBrSchool.unitName}</span>
										</td>
										<td width="10%">异动后学院:</td>
										<td width="25%"><select
											name="changeTeachingType_stuCenterid"
											id="changeTeachingType_stuCenterid" size="1"
											style="float: none; width: 260px;" class="required"
											onchange="_selectMajor_changeTeachingType()"><option
													value="">请选择</option></select><font color=red>*</font></td>
										<td width="10%"></td>
										<td width="22%"></td>
									</tr>
									<tr style="display: none" name="77A">
										<td width="10%">原学院:</td>
										<td width="22%"><input type="hidden" id="stuCenter"
											name="stuCenter"
											value="${stuChangeInfo.changeBeforeBrSchool.resourceid}" /> <span
											id="stuCenterName">${stuChangeInfo.changeBeforeBrSchool.unitName}</span>
										</td>
										<td width="10%"></td>
										<td width="25%"></td>
										<td width="10%"></td>
										<td width="22%"></td>
									</tr>
									<tr style="display: none" name="88A">
										<td width="10%">原学院:</td>
										<td width="22%"><input type="hidden" id="stuCenter"
											name="stuCenter"
											value="${stuChangeInfo.changeBeforeBrSchool.resourceid}" /> <span
											id="stuCenterName">${stuChangeInfo.changeBeforeBrSchool.unitName}</span>
										</td>
										<td width="10%"></td>
										<td width="25%"></td>
										<td width="10%"></td>
										<td width="22%"></td>
									</tr>
									<tr id="E">
										<td width="10%">原专业:</td>
										<td width="22%"><input type="hidden" id="major"
											name="major"
											value="${stuChangeInfo.changeBeforeTeachingGuidePlan.teachingPlan.major.majorName }" />
											<span id="majorName">${stuChangeInfo.changeBeforeTeachingGuidePlan.teachingPlan.major.majorName }</span>
										</td>
										<td width="10%">异动后专业:</td>
										<td width="25%"><span>${stuChangeInfo.changeTeachingGuidePlan.teachingPlan.major.majorName }</span>
										</td>
										<td width="10%"></td>
										<td width="22%"></td>
									</tr>
									<tr style="display: none" name="22E">
										<td width="10%">原专业:</td>
										<td width="22%"><input type="hidden" id="major"
											name="major"
											value="${stuChangeInfo.changeBeforeTeachingGuidePlan.teachingPlan.major.majorName }" />
											<span id="majorName">${stuChangeInfo.changeBeforeTeachingGuidePlan.teachingPlan.major.majorName }</span>
										</td>
										<td width="10%">复学专业:</td>
										<td width="25%"><select name="backSchool_majorid"
											id="backSchool_majorid" size="1"
											style="float: none; width: 260px;" class="required"
											onchange="_selectClasses_backschool(this)"><option
													value="">请选择</option></select><font color=red>*</font></td>
										<td width="10%"></td>
										<td width="22%"></td>
									</tr>
									<tr style="display: none" name="33E">
										<td width="10%">原专业:</td>
										<td width="22%"><input type="hidden" id="major"
											name="major"
											value="${stuChangeInfo.changeBeforeTeachingGuidePlan.teachingPlan.major.majorName }" />
											<span id="majorName">${stuChangeInfo.changeBeforeTeachingGuidePlan.teachingPlan.major.majorName }</span>
										</td>
										<td width="10%">转专业:</td>
										<td width="25%"><select name="majorid" id="_majorid"
											size="1" style="float: none; width: 260px;" class="required"
											onchange="_selectClasses_new(this)"><option value="">请选择</option></select><font
											color=red>*</font></td>
										<td width="10%"></td>
										<td width="22%"></td>
									</tr>
									<tr style="display: none" name="55E">
										<td width="10%">原专业:</td>
										<td width="22%"><input type="hidden" id="major"
											name="major"
											value="${stuChangeInfo.changeBeforeTeachingGuidePlan.teachingPlan.major.majorName }" />
											<span id="majorName">${stuChangeInfo.changeBeforeTeachingGuidePlan.teachingPlan.major.majorName }</span>
										</td>
										<td width="10%">异动后专业:</td>
										<td width="25%"><input type="hidden"
											name="changeSchool_majorName" id="changeSchool_majorName"
											value="${stuChangeInfo.changeBeforeTeachingGuidePlan.teachingPlan.major.resourceid}" />
											<span id="majorName">${stuChangeInfo.changeBeforeTeachingGuidePlan.teachingPlan.major.majorName }</span>
											<%--			<select name="changeSchool_majorName" id="changeSchool_majorName" size="1" type="hidden" disabled="true" style="float:none;width:260px;" class="required" onchange="_selectClasses_changeSchool(this)"><option value="">请选择</option></select><font color=red>*</font>--%>
										</td>
										<td width="10%"></td>
										<td width="22%"></td>
									</tr>
									<tr style="display: none" name="66E">
										<td width="10%">原专业:</td>
										<td width="22%"><input type="hidden" id="major"
											name="major"
											value="${stuChangeInfo.changeBeforeTeachingGuidePlan.teachingPlan.major.majorName }" />
											<span id="majorName">${stuChangeInfo.changeBeforeTeachingGuidePlan.teachingPlan.major.majorName }</span>
										</td>
										<td width="10%">异动后专业:</td>
										<td width="25%"><select
											name="changeTeachingType_majorName"
											id="changeTeachingType_majorName" size="1"
											style="float: none; width: 260px;" class="required"
											onchange="_selectClasses_changeTeachingType(this)"><option
													value="">请选择</option></select><font color=red>*</font></td>
										<td width="10%"></td>
										<td width="22%"></td>
									</tr>
									<tr style="display: none" name="77E">
										<td width="10%">原专业:</td>
										<td width="22%"><input type="hidden" id="major"
											name="major"
											value="${stuChangeInfo.changeBeforeTeachingGuidePlan.teachingPlan.major.majorName }" />
											<span id="majorName">${stuChangeInfo.changeBeforeTeachingGuidePlan.teachingPlan.major.majorName }</span>
										</td>
										<td width="10%"></td>
										<td width="25%"></td>
										<td width="10%"></td>
										<td width="22%"></td>
									</tr>
									<tr style="display: none" name="88E">
										<td width="10%">原专业:</td>
										<td width="22%"><input type="hidden" id="major"
											name="major"
											value="${stuChangeInfo.changeBeforeTeachingGuidePlan.teachingPlan.major.majorName }" />
											<span id="majorName">${stuChangeInfo.changeBeforeTeachingGuidePlan.teachingPlan.major.majorName }</span>
										</td>
										<td width="10%"></td>
										<td width="25%"></td>
										<td width="10%"></td>
										<td width="22%"></td>
									</tr>
									<tr id="F">
										<td width="10%">原班级:</td>
										<td width="22%"><span id="classesName">${stuChangeInfo.changeBeforeClass.classname}</span>
										</td>
										<td width="10%">异动后班级:</td>
										<td width="25%"><span>${stuChangeInfo.changeClass.classname }</span>
										</td>
										<td width="10%"></td>
										<td width="22%"></td>
									</tr>
									<tr style="display: none" name="22F">
										<td width="10%">原班级:</td>
										<td width="22%"><span id="classesName">${stuChangeInfo.changeBeforeClass.classname}</span>
										</td>
										<td width="10%">复学班级:</td>
										<td width="25%"><select name="backSchool_classesid"
											id="backSchool_classesid" size="1" class="required"
											style="float: none; width: 260px;"
											onchange="selectTeachPlan_backSchool(this)"><option
													value="">请选择</option></select><font color=red>*</font></td>
										<td width="10%">异动后教学计划</td>
										<td width="22%"><select
											name="backSchool_changeguidteachPlanId"
											id="backSchool_changeguidteachPlanId" size="1"
											class="required" style="float: none; width: 260px;"><option
													value="">请选择</option></select><font color=red>*</font></td>
									</tr>
									<tr style="display: none" name="33F">
										<td width="10%">原班级:</td>
										<td width="22%"><span id="classesName">${stuChangeInfo.changeBeforeClass.classname}</span>
										</td>
										<td width="10%">转专业班级:</td>
										<td width="25%"><select name="changeMajor_classesid"
											id="changeMajor_classesid" size="1" class="required"
											style="float: none; width: 260px;"
											onchange="_selectTeachPlan_new()"><option value="">请选择</option></select><font
											color=red>*</font></td>
										<td width="10%">转专业教学计划</td>
										<td width="22%"><select
											name="changeMajor_changeguidteachPlanId"
											id="changeMajor_changeguidteachPlanId" size="1"
											class="required" style="float: none; width: 260px;"><option
													value="">请选择</option></select><font color=red>*</font></td>
									</tr>
									<tr style="display: none" name="55F">
										<td width="10%">原班级:</td>
										<td width="22%"><span id="classesName">${stuChangeInfo.changeBeforeClass.classname}</span>
										</td>
										<td width="10%">异动后班级:</td>
										<td width="25%"><input type="hidden"
											name="changeSchool_classesid" id="changeSchool_classesid"
											value="" /> <%--    <select name="changeSchool_classesid" id="changeSchool_classesid" size="1" style="float:none;width:260px;" onchange="_selectTeachPlan_changeSchool()"><option value="">请选择</option></select> --%>
										</td>
										<td width="10%">异动后教学计划</td>
										<td width="22%"><select
											name="changeSchool_changeguidteachPlanId"
											id="changeSchool_changeguidteachPlanId" size="1"
											class="required" style="float: none; width: 260px;"><option
													value="">请选择</option></select><font
											id="changeSchool_changeguidteachPlanId_f" color=red>*</font>
										</td>
									</tr>
									<tr style="display: none" name="66F">
										<td width="10%">原班级:</td>
										<td width="22%"><span id="classesName">${stuChangeInfo.changeBeforeClass.classname}</span>
										</td>
										<td width="10%">异动后班级:</td>
										<td width="25%"><select
											name="changeTeachingType_classesid"
											id="changeTeachingType_classesid" size="1" class="required"
											style="float: none; width: 260px;"
											onchange="_selectTeachPlan_changeTeachingType()"><option
													value="">请选择</option></select><font color=red>*</font></td>
										<td width="10%">异动后教学计划</td>
										<td width="22%"><select
											name="changeTeachingType_changeguidteachPlanId"
											id="changeTeachingType_changeguidteachPlanId" size="1"
											class="required" style="float: none; width: 260px;"><option
													value="">请选择</option></select><font color=red>*</font></td>
									</tr>
									<tr style="display: none" name="77F">
										<td width="10%">原班级:</td>
										<td width="22%"><span id="classesName">${stuChangeInfo.changeBeforeClass.classname}</span>
										</td>
										<td width="10%"></td>
										<td width="25%"></td>
										<td width="10%"></td>
										<td width="22%"></td>
									</tr>
									<tr style="display: none" name="88F">
										<td width="10%">原班级:</td>
										<td width="22%"><span id="classesName">${stuChangeInfo.changeBeforeClass.classname}</span>
										</td>
										<td width="10%"></td>
										<td width="25%"></td>
										<td width="10%"></td>
										<td width="22%"></td>
									</tr>


									<tr id="B">
										<td width="10%">原年级:</td>
										<td width="22%"><input type="hidden" name="grade"
											value="${stuChangeInfo.changeBeforeTeachingGuidePlan.grade.gradeName }" />
											<span id="gradeName">${stuChangeInfo.changeBeforeTeachingGuidePlan.grade.gradeName }</span>
										</td>
										<td width="10%">异动后年级:</td>
										<td width="25%"><span>${stuChangeInfo.changeTeachingGuidePlan.grade.gradeName }</span>
										</td>
										<td width="10%"></td>
										<td width="22%"></td>
									</tr>
									<tr style="display: none" name="22B">
										<td width="10%">原年级:</td>
										<td width="22%"><input type="hidden" name="grade"
											value="${stuChangeInfo.changeBeforeTeachingGuidePlan.grade.gradeName }" />
											<span id="gradeName">${stuChangeInfo.changeBeforeTeachingGuidePlan.grade.gradeName }</span>
										</td>
										<td width="10%">复学年级:</td>
										<td width="25%"><gh:selectModel id="backSchool_gradeName"
												name="backSchool_gradeName" bindValue="resourceid"
												displayValue="gradeName"
												modelClass="com.hnjk.edu.basedata.model.Grade"
												value="${condition['stuGrade']}"
												onchange="selectMajor_backschool()"
												orderBy="yearInfo.firstYear desc" choose="Y"
												style="float:none;width: 120px" /></td>
										<td width="10%"></td>
										<td width="22%"></td>
									</tr>
									<tr style="display: none" name="33B">
										<td width="10%">原年级:</td>
										<td width="22%"><input type="hidden" name="grade"
											value="${stuChangeInfo.changeBeforeTeachingGuidePlan.grade.gradeName }" />
											<span id="gradeName">${stuChangeInfo.changeBeforeTeachingGuidePlan.grade.gradeName }</span>
										</td>
										<td width="10%">转专业年级:</td>
										<td width="25%"><span id="changeMajor_gradeName">${stuChangeInfo.changeTeachingGuidePlan.grade.gradeName}</span>
										</td>
										<td width="10%"></td>
										<td width="22%"></td>
									</tr>
									<tr style="display: none" name="55B">
										<td width="10%">原年级:</td>
										<td width="22%"><input type="hidden" name="grade"
											value="${stuChangeInfo.changeBeforeTeachingGuidePlan.grade.gradeName }" />
											<input type="hidden" name="grade_id"
											value="${stuChangeInfo.changeBeforeTeachingGuidePlan.grade.resourceid }" />
											<span id="gradeName">${stuChangeInfo.changeBeforeTeachingGuidePlan.grade.gradeName }</span>
										</td>
										<td width="10%">异动后年级:</td>
										<td width="25%"><span id="changeSchool_gradeName">${stuChangeInfo.changeTeachingGuidePlan.grade.gradeName}</span>
										</td>
										<td width="10%"></td>
										<td width="22%"></td>
									</tr>
									<tr style="display: none" name="66B">
										<td width="10%">原年级:</td>
										<td width="22%"><input type="hidden" name="grade"
											value="${stuChangeInfo.changeBeforeTeachingGuidePlan.grade.gradeName }" />
											<span id="gradeName">${stuChangeInfo.changeBeforeTeachingGuidePlan.grade.gradeName }</span>
										</td>
										<td width="10%">异动后年级:</td>
										<td width="25%"><span id="changeTeachingType_gradeName">${stuChangeInfo.changeTeachingGuidePlan.grade.gradeName}</span>
										</td>
										<td width="10%"></td>
										<td width="22%"></td>
									</tr>
									<tr style="display: none" name="77B">
										<td width="10%">原年级:</td>
										<td width="22%"><input type="hidden" name="grade"
											value="${stuChangeInfo.changeBeforeTeachingGuidePlan.grade.gradeName }" />
											<span id="gradeName">${stuChangeInfo.changeBeforeTeachingGuidePlan.grade.gradeName }</span>
										</td>
										<td width="10%"></td>
										<td width="25%"></td>
										<td width="10%"></td>
										<td width="22%"></td>
									</tr>
									<tr style="display: none" name="88B">
										<td width="10%">原年级:</td>
										<td width="22%"><input type="hidden" name="grade"
											value="${stuChangeInfo.changeBeforeTeachingGuidePlan.grade.gradeName }" />
											<span id="gradeName">${stuChangeInfo.changeBeforeTeachingGuidePlan.grade.gradeName }</span>
										</td>
										<td width="10%"></td>
										<td width="25%"></td>
										<td width="10%"></td>
										<td width="22%"></td>
									</tr>


									<tr id="C">
										<td width="10%">原层次:</td>
										<td width="22%"><input type="hidden" id="classic"
											name="classic"
											value="${stuChangeInfo.changeBeforeTeachingGuidePlan.teachingPlan.classic.classicName }" />
											<span id="classicName">${stuChangeInfo.changeBeforeTeachingGuidePlan.teachingPlan.classic.classicName }</span>
										</td>
										<td width="10%">异动后层次:</td>
										<td width="25%"><span>${stuChangeInfo.changeTeachingGuidePlan.teachingPlan.classic.classicName }</span>
										</td>
										<td width="10%"></td>
										<td width="22%"></td>
									</tr>
									<tr style="display: none" name="22C">
										<td width="10%">原层次:</td>
										<td width="22%"><input type="hidden" id="classic"
											name="classic"
											value="${stuChangeInfo.changeBeforeTeachingGuidePlan.teachingPlan.classic.classicName }" />
											<span id="classicName">${stuChangeInfo.changeBeforeTeachingGuidePlan.teachingPlan.classic.classicName }</span>
										</td>
										<td width="10%">复学层次:</td>
										<td width="25%"><span id="backSchool_classicName">${stuChangeInfo.changeTeachingGuidePlan.teachingPlan.classic.classicName}</span>
										</td>
										<td width="10%"></td>
										<td width="22%"></td>
									</tr>
									<tr style="display: none" name="33C">
										<td width="10%">原层次:</td>
										<td width="22%"><input type="hidden" id="classic"
											name="classic"
											value="${stuChangeInfo.changeBeforeTeachingGuidePlan.teachingPlan.classic.classicName }" />
											<span id="classicName">${stuChangeInfo.changeBeforeTeachingGuidePlan.teachingPlan.classic.classicName }</span>
										</td>
										<td width="10%">转专业层次:</td>
										<td width="25%"><span id="changeMajor_classicName">${stuChangeInfo.changeTeachingGuidePlan.teachingPlan.classic.classicName}</span>
										</td>
										<td width="10%"></td>
										<td width="22%"></td>
									</tr>
									<tr style="display: none" name="55C">
										<td width="10%">原层次:</td>
										<td width="22%"><input type="hidden" id="classic"
											name="classic"
											value="${stuChangeInfo.changeBeforeTeachingGuidePlan.teachingPlan.classic.classicName }" />
											<span id="classicName">${stuChangeInfo.changeBeforeTeachingGuidePlan.teachingPlan.classic.classicName }</span>
										</td>
										<td width="10%">异动后层次:</td>
										<td width="25%"><span id="changeSchool_classicName">${stuChangeInfo.changeTeachingGuidePlan.teachingPlan.classic.classicName}</span>
										</td>
										<td width="10%"></td>
										<td width="22%"></td>
									</tr>
									<tr style="display: none" name="66C">
										<td width="10%">原层次:</td>
										<td width="22%"><input type="hidden" id="classic"
											name="classic"
											value="${stuChangeInfo.changeBeforeTeachingGuidePlan.teachingPlan.classic.classicName }" />
											<span id="classicName">${stuChangeInfo.changeBeforeTeachingGuidePlan.teachingPlan.classic.classicName }</span>
										</td>
										<td width="10%">异动后层次:</td>
										<td width="25%"><span id="changeTeachingType_classicName">${stuChangeInfo.changeTeachingGuidePlan.teachingPlan.classic.classicName}</span>
										</td>
										<td width="10%"></td>
										<td width="22%"></td>
									</tr>
									<tr style="display: none" name="77C">
										<td width="10%">原层次:</td>
										<td width="22%"><input type="hidden" id="classic"
											name="classic"
											value="${stuChangeInfo.changeBeforeTeachingGuidePlan.teachingPlan.classic.classicName }" />
											<span id="classicName">${stuChangeInfo.changeBeforeTeachingGuidePlan.teachingPlan.classic.classicName }</span>
										</td>
										<td width="10%"></td>
										<td width="25%"></td>
										<td width="10%"></td>
										<td width="22%"></td>
									</tr>
									<tr style="display: none" name="88C">
										<td width="10%">原层次:</td>
										<td width="22%"><input type="hidden" id="classic"
											name="classic"
											value="${stuChangeInfo.changeBeforeTeachingGuidePlan.teachingPlan.classic.classicName }" />
											<span id="classicName">${stuChangeInfo.changeBeforeTeachingGuidePlan.teachingPlan.classic.classicName }</span>
										</td>
										<td width="10%"></td>
										<td width="25%"></td>
										<td width="10%"></td>
										<td width="22%"></td>
									</tr>

									<tr id="H" style="display: none">
										<td width="10%">退学原因:</td>
										<td width="90%" colspan="5"><c:choose>
												<c:when test="${stuChangeInfo.reason=='患病'}">患病</c:when>
												<c:when test="${stuChangeInfo.reason=='停学实践'}">停学实践</c:when>
												<c:when test="${stuChangeInfo.reason=='贫困'}">贫困</c:when>
												<c:when test="${stuChangeInfo.reason=='学习成绩不好'}">学习成绩不好</c:when>
												<c:when test="${stuChangeInfo.reason=='出国'}">出国</c:when>
												<c:when test="${stuChangeInfo.reason=='其他'}">其他</c:when>
											</c:choose></td>
									</tr>
									<tr name="77C" style="display: none">
										<td width="10%">退学原因:</td>
										<td width="90%" colspan="5"><select
											id="stopStudy_reason" name="stopStudy_reason"
											style="width: 200px;">
												<option value="">请选择</option>
												<option
													<c:if test="${stuChangeInfo.reason=='患病'}"> selected="selected"</c:if>
													value="患病">患病</option>
												<option
													<c:if test="${stuChangeInfo.reason=='停学实践'}"> selected="selected"</c:if>
													value="停学实践">停学实践</option>
												<option
													<c:if test="${stuChangeInfo.reason=='贫困'}"> selected="selected"</c:if>
													value="贫困">贫困</option>
												<option
													<c:if test="${stuChangeInfo.reason=='学习成绩不好'}"> selected="selected"</c:if>
													value="学习成绩不好">学习成绩不好</option>
												<option
													<c:if test="${stuChangeInfo.reason=='出国'}"> selected="selected"</c:if>
													value="出国">出国</option>
												<option
													<c:if test="${stuChangeInfo.reason=='其他'}"> selected="selected"</c:if>
													value="其他">其他</option>
										</select><font color=red>*</font></td>
									</tr>
									<tr name="77H" style="display: none">
										<td>所属银行：</td>
										<td><input id="bankName" name="bankName" style="width: 200px;" value="${stuChangeInfo.bankName }" ></td>
										<td>银行卡号：</td>
										<td><input id="cardNo" name="cardNo" style="width: 80%" value="${stuChangeInfo.cardNo }" onblur="validateCardNo(this.value)" ></td>
										<td colspan="2">开户行名称：<input id="bankAddress" name="bankAddress" style="width: 80%"  value="${stuChangeInfo.bankAddress }" ></td>
									</tr>
									<tr name="77H" style="display: none">
										<td id="getNameByCardNo" colspan="2" style="color: green;text-align: center;"></td>
										<td id="validateCardNo" colspan="2" style="color: red;text-align: center;"></td>
										<td colspan="2" style="color: red;text-align: center;">备注：开户行名称填写正确才能正常汇款，应详细到 **支行</td>
									</tr>
									<tr>
										<td>操作员:</td>
										<td><input type="hidden"
											name="opManId" id="opManId" value="${opManId}" /> <input
											type="hidden" name="opMan" id="opMan" value="${opMan}" /> <span>${opMan}</span>
										</td>
										<td>申请日期</td>
										<td><input type="text" id="applicationDate_1" name="applicationDate_1" size="40" style="width:50%" value="<fmt:formatDate value="${stuChangeInfo.applicationDate }" pattern="yyyy-MM-dd HH:mm:ss" />"
									 			onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowWeek:true})" onchange="updateBackSchoolDate()"/></td>
							 			<c:if test="${schoolCode eq '12962' }">
							 			<td>上传附件</td>
							 			<td>							 			
							 			<gh:upload hiddenInputName="attachId" uploadType="attach"
												baseStorePath="users,${stuChangeInfo.studentInfo.studyNo}"
												extendStorePath="attachs" formType="STUDENTCHANGEAPPLY"
												formId="${stuChangeInfo.resourceid }"
												attachList="${attachList }" />
							 			
							 			</td>
							 			</c:if>
									</tr>
									<tr id="G" style="display: none">
										<td width="10%">复学日期:</td>
										<td width="90%" colspan="5"><span><fmt:formatDate
												value="${stuChangeInfo.endDate}"
												pattern="yyyy-MM-dd" /></span></td>
									</tr>
									<tr style="display: none" name="22G">
										<td width="10%">复学日期:</td>
										<td width="90%" colspan="5">
											<input type="text"
												   id="backSchool_backSchoolDate" name="backSchool_backSchoolDate"
												   value="${stuChangeInfo.endDate}" class="Wdate"
												   onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowWeek:true})"
<%-- 												   onfocus="WdatePicker({minDate:'${stuChangeInfo.applicationDate}'})"  --%>
												   /></td>
									</tr>
									<tr style="display: none" name="88G">
										<td width="10%" name="endDate">复学日期:</td>
										<td width="90%" colspan="5">
											<input type="text"
											   id="pauseStudy_backSchoolDate" name="pauseStudy_backSchoolDate"
											   value="${stuChangeInfo.endDate}" class="Wdate"
											   onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowWeek:true})"
<%-- 											   onfocus="WdatePicker({minDate:'${stuChangeInfo.applicationDate}'})"  --%>
											   /></td>
									</tr>
									<tr id="reason_common" style="display: none;">
										<td width="10%">变动原因:</td>
										<td width="90%" colspan="5"><textarea rows="5" cols="70%"
												name="reason">${stuChangeInfo.reason}</textarea></td>
									</tr>
									<tr>
										<td width="10%">备注:</td>
										<td width="90%" colspan="5"><textarea rows="5" cols="70%"
												name="memo">${stuChangeInfo.memo }</textarea></td>
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
						<c:if test="${ empty stuChangeInfo.resourceid }">
							<li><div class="buttonActive">
									<div class="buttonContent">
										<button type="button" onclick="checkForReinstate()">保存</button>
									</div>
								</div></li>
						</c:if>
						<c:if test="${not empty stuChangeInfo.resourceid }">
							<li><div class="buttonActive">
									<div class="buttonContent">
										<button type="button" onclick="selfChangeApplyOnceMore()">继续申请异动</button>
									</div>
								</div></li>
						</c:if>
						<li><div class="button">
								<div class="buttonContent">
									<button type="button" class="close"
										onclick="navTab.closeCurrentTab();">关闭</button>
								</div>
							</div></li>
					</ul>
				</div>

			</form>
		</div>
	</div>
</body>
</html>