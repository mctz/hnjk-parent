<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>按课程批量座位安排列表</title>


</head>
<body>
	<div class="page">
		<div class="pageHeader">
			<form id="batchExamSeatSearchForm"
				onsubmit="return _checkBatchexamSeatFormPost(this);"
				action="${baseUrl}/edu3/teaching/exam/seat/listbycourse.html"
				method="post">
				<input type="hidden" id="batchExamSeatSearchForm_chooseExamSubId"
					value="${condition['examSub']}" /> <input type="hidden"
					id="batchExamSeatSearchForm_branchSchoolId"
					value="${condition['branchSchool']}" /> <input type="hidden"
					id="batchExamSeatSearchForm_examSub_examTimeSegment"
					value="${condition['examSub_examTimeSegment'] }" />
				<div class="searchBar">
					<ul class="searchContent">

						<li><label>考试批次：</label> <gh:selectModel
								id="batchExamSeatSearchExamSub" name="examSub"
								bindValue="resourceid" displayValue="batchName"
								style="width:55%"
								modelClass="com.hnjk.edu.teaching.model.ExamSub"
								value="${condition['examSubId']}"
								condition="batchType='exam',examsubStatus='2'"
								orderBy="batchName desc" /><font color="red">*</font></li>
						<li><label>课程：</label> <select id="courseId" name="courseId"
							style="width: 55%">
								<option value="">::::::请选择::::::</option>
								<c:forEach items="${courseList }" var="course">
									<option value="${course.resourceid }"
										<c:if test="${course.resourceid eq  condition['courseId']}"> selected="selected"</c:if>>
										${course.courseCode}-${course.courseName }</option>
								</c:forEach>
						</select><font color="red">*</font></li>
						<c:if test="${!isBrschool }">
							<li><label>考试地点：</label> <select id="examplace"
								name="examplace">
									<option value="">请选择</option>
									<option value="0"
										<c:if test="${condition['examPlace'] eq '0' }" >selected="selected"</c:if>>校内</option>
									<option value="1"
										<c:if test="${condition['examPlace'] eq '1' }" >selected="selected"</c:if>>校外</option>
							</select></li>
							<li><label>教学站：</label> <gh:brSchoolAutocomplete
									name="branchSchool" tabindex="1"
									id="batchExamSeat_eiinfo_brSchoolName"
									defaultValue="${condition['branchSchool']}" displayType="code"
									style="width:53%" /></li>
						</c:if>
					</ul>

					<ul class="searchContent">
						<li style="width: 400px"><c:if test="${ !isBrschool }">
								<span class="tips">提示：考试批次、课程为必选条件</span>
							</c:if></li>
						<div class="buttonActive" style="float: right">
							<div class="buttonContent">
								<button type="submit">查 询</button>
							</div>
						</div>
					</ul>
				</div>
			</form>
		</div>
		<gh:resAuth parentCode="RES_TEACHING_EXAM_ARRANGE_SEAT"
			pageType="list"></gh:resAuth>
		<div class="pageContent" layoutH="100">
			<div
				style="float: left; display: block; width: 60%; margin-bottom: 16px; padding: 5px 0px 5px 15px; line-height: 21px;">
				<strong>请选择要安排座位的学生 </strong>
				<!-- <span id="notSingleCourseInRommText" style="float:right;">同一考场有多门课程考试(座位号分别从1开始)</span><input type="checkbox" id="notSingleCourseInRomm" style="float: right;margin-top: -1px" /> -->
				<div>
					<table class="list" width="100%">
						<thead>
							<tr>
								<th width="5%"><input type="checkbox" name="checkall"
									id="examInfo_list_check_all"
									onclick="checkboxAll('#examInfo_list_check_all','resourceid','#_batchExamSeatExamInfoListBody'),tipMoreOptions();" /></th>
								<th width="28%">考试时间</th>
								<th width="10%">姓名</th>
								<th width="25%">学号</th>
								<th width="32%">教学点</th>

							</tr>
						</thead>
						<tbody id="_batchExamSeatExamInfoListBody">
							<c:forEach items="${objPage.result }" var="info">
								<tr>
									<td><input type="checkbox" name="resourceid"
										value="${info.reId}" autocomplete="off"
										onclick="tipMoreOptions();" title="${info.COURSENAME }" /></td>
									<td><fmt:formatDate value="${info.EXAMSTARTTIME}"
											pattern="yyyy-MM-dd HH:mm" />-<fmt:formatDate
											value="${info.EXAMENDTIME}" pattern="HH:mm" /></td>

									<td>${info.studentname }</td>
									<td>${info.studyNo }</td>
									<td>${info.unitname }</td>

								</tr>
							</c:forEach>
						</tbody>
					</table>
					<gh:page page="${objPage}"
						goPageUrl="${baseUrl }/edu3/teaching/exam/seat/listbycourse.html"
						pageType="sys" condition="${condition}" pageNumShown="2" />
				</div>
			</div>
			<div
				style="float: right; display: block; width: 34%; margin-bottom: 16px; padding: 5px 15px 5px 0px; line-height: 21px;">

				<h1>请选择要安排座位的考场</h1>
				<div>
					<table class="list" width="100%">
						<thead>
							<tr>
								<th width="5%"><input type="checkbox" name="checkall"
									id="examroom_list_check_all"
									onclick="checkboxAll('#examroom_list_check_all','resourceid','#_batchExamSeatExamRoomListBody')" /></th>
								<th width="30%">试室</th>
								<th width="10%">机房</th>
								<th width="15%">双隔位</th>
								<th width="15%">已安排数</th>
								<th width="15%">剩余容量</th>
							</tr>
						</thead>
						<tbody id="_batchExamSeatExamRoomListBody">
							<c:forEach items="${examRoomList }" var="examRoom">
								<tr>
									<td><input type="checkbox" name="resourceid"
										value="${examRoom.RESOURCEID }" autocomplete="off"
										title="${examRoom.EXAMROOMNAME }" /></td>
									<td>${examRoom.EXAMROOMNAME }</td>
									<td>${ghfn:dictCode2Val('yesOrNo',examRoom.ISCOMPUTERROOM)}</td>
									<td>${examRoom.DOUBLESEATNUM}</td>
									<td><a href="javascript:void(0)"
										onclick="viewAssignedExamStuInfo('room','${examRoom.RESOURCEID}','${condition['examSubId']}','','${condition['branchSchool']}','${condition['examSub_examTimeSegment'] }')">${examRoom.ASSIGNED }</a></td>
									<td><c:choose>
											<c:when
												test="${(examRoom.DOUBLESEATNUM - examRoom.ASSIGNED)>0 }">
												<font color="red">${examRoom.DOUBLESEATNUM - examRoom.ASSIGNED }</font>
											</c:when>
											<c:otherwise>
										${examRoom.DOUBLESEATNUM - examRoom.ASSIGNED }
									</c:otherwise>
										</c:choose></td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>
	<script type="text/javascript">
	//当选择多个考试课程时，提示同一考场有多门课程考试(座位号分别从1开始)
	function tipMoreOptions(){
		if($("#_batchExamSeatExamInfoListBody input[name='resourceid']:checked").size()>1){
			$("#notSingleCourseInRomm").focus();
			$("#notSingleCourseInRommText").css("color","red")
		}else{
			$("#notSingleCourseInRommText").css("color","black")
		}
	}
	//获取考试时间段
	function getExamCourse(obj){alert(0);
		var examSubId = $(obj).val();
		if(""!=examSubId){
			jQuery.ajax({
				type:"post",
				url :"${baseUrl}/edu3/teaching/exam/seat/getExameCourse.html",
				data:{examSubId:examSubId},
				dataType:"json",
				success:function(resultData){alert(resultData);
				if(resultData!=""){
					var optionStr  = "<option value=''>::::::请选择::::::</option>";
					for(var i=0 ;i<resultData.length;i++){
						optionStr += "<option value='"+resultData[i].resourceid+"'>"+resultData[i].courseCode+"-"+resultData[i].courseName+"</option>";
					}
					$("#courseId").html(optionStr);
				}
					
				}
			});
		}
	}
	//点击查询时的条件检查
	function _checkBatchexamSeatFormPost(obj){
		
		if($('#batchExamSeatSearchForm').find('#batchExamSeatSearchExamSub').val()==''){
			alertMsg.warn("请选择要分配座位的考试批次，点击查询!");		
			return false;
		}
		if($('#batchExamSeatSearchForm').find('#courseId').val()==''){
			alertMsg.warn("请选考试课程，点击查询!");		
			return false;
		}
		
		return navTabSearch(obj);
	}
	//分配座位
	function assignSeat(){
		
		var results        = new Array(); 
		var roomIds          = new Array(); 
		var examSubId        = jQuery("#batchExamSeatSearchExamSub").val(); 
		var branchSchool     = jQuery("#batchExamSeat_eiinfo_brSchoolName").val(); 
		var courseId      = jQuery("#courseId").val();
		var reloadURL        = "${baseUrl}/edu3/teaching/exam/seat/listbycourse.html";
		var notSingleCourseInRomm = "false";
		if(jQuery("#notSingleCourseInRomm").attr("checked")==true){
			notSingleCourseInRomm = "true";
		}

		jQuery("#_batchExamSeatExamInfoListBody input[name='resourceid']:checked").each(function(){
			results.push(jQuery(this).val());
		})
		jQuery("#_batchExamSeatExamRoomListBody input[name='resourceid']:checked").each(function(){
			roomIds.push(jQuery(this).val());
		})
	
		if(results.length==0){
			alertMsg.warn("请选择要安排座位的学生!");
			jQuery("#examInfo_list_check_all").focus();
			return false;
		}
		if( roomIds.length==0){
			alertMsg.warn("请选择要安排座位的考场!");
			jQuery("#examroom_list_check_all").focus();
			return false;
		}
		
		
		alertMsg.confirm("确认要为所选的课程分配座位吗？",{
				okCall:function(){
					var url = "${baseUrl}/edu3/teaching/exam/seat/assignbycourse.html"
					jQuery.ajax({
						type:"post",
						url :url,
						data:{results:results.toString(),examRoomIds:roomIds.toString(),examSubId:examSubId,reset:notSingleCourseInRomm,branchSchool:branchSchool,courseId:courseId},
						dataType:"json",
						success:function(resultDate){
							if(resultDate['isAssignSuccess']==true){
								navTab.reload(reloadURL,$("#batchExamSeatSearchForm").serializeArray());
								alertMsg.info(resultDate['msg']);
							}else{
								alertMsg.warn(resultDate['msg']);
							}
						}
					});
				}
			});
	}
	//清空座位
	function clearnExamSeat(){
		
		var examInfos         = new Array(); 
		var roomIds           = new Array();
		var msg               = new Array();
		var reloadURL         = "${baseUrl}/edu3/teaching/exam/seat/list.html";
		var url 			  = "${baseUrl}/edu3/teaching/exam/seat/clearnExamSeat.html";
		
		var examTimeSegment   = $("#batchExamSeatSearchForm_examSub_examTimeSegment").val();
		var examSubId         = $("#batchExamSeatSearchForm_chooseExamSubId").val();
		var branchSchool      = $("#batchExamSeatSearchForm_branchSchoolId").val();
		var examTimeSegmentStr= $("#examSub_examTimeSegment option[value='"+examTimeSegment+"']").text();
		
		jQuery("#_batchExamSeatExamInfoListBody input[name='resourceid']:checked").each(function(){
			examInfos.push(jQuery(this).val());
			msg.push("<font color='red'>"+jQuery(this).attr("title")+"</font>");
			
		})
		jQuery("#_batchExamSeatExamRoomListBody input[name='resourceid']:checked").each(function(){
			roomIds.push(jQuery(this).val());
			msg.push("<font color='red'>"+jQuery(this).attr("title")+"</font>");
		})
		if(""==examSubId){
			alertMsg.warn("请选择要分配座位的考试批次，点击查询!");
			return false;
		}
		if(""==branchSchool){
			alertMsg.warn("请选择要分配座位的教学站，点击查询!");
			return false;
		}
		if(""==examTimeSegment){
			alertMsg.warn("请选考试时间，点击查询!");
			return false;
		}
		
		alertMsg.confirm("确定要清空 </br>"+msg.toString()+"</br>"+examTimeSegmentStr+"</br>的座位吗？",{
			okCall:function(){
				jQuery.ajax({
					type:"post",
					url:url,
					data:{examTimeSegment:examTimeSegment,examSubId:examSubId,examRoomId:roomIds.toString(),examInfoId:examInfos.toString(),branchSchool:branchSchool},
					dataType:"json",
					success:function(resultData){
						var isSuccess = resultData['isSuccess'];
						var msg       = resultData['msg'];
						if(isSuccess==true){
							navTab.reload(reloadURL,$("#batchExamSeatSearchForm").serializeArray());
						}else{
							alertMsg.warn(msg);
						}
					}
				});
			}
		});
	}
	//查看座位安排汇总情况
	function examSeatAssignedInfo(){
		var examTimeSegment   = $("#examSub_examTimeSegment").val();
		var examSubId         = $("#batchExamSeatSearchExamSub").val();
		var branchSchool      = $("#batchExamSeat_eiinfo_brSchoolName").val();

		if(""==examSubId){
			alertMsg.warn("请选择要分配座位的考试批次!");
			return false;
		}
		if(""==examTimeSegment){
			alertMsg.warn("请选考试时间!");
			return false;
		}
		var url 			  = "${baseUrl}/edu3/teaching/exam/seat/examSeatAssignInfo.html?examSubId="+examSubId+"&branchSchool="+branchSchool+"&examTimeSegment="+examTimeSegment; 
		$.pdialog.open(url,"RES_TEACHING_VIEW_EXAM_SEAT_ASSIGN","查看座位安排汇总情况", {width:800, height:600});
	}
	/**
	
	//清空考场
	function clearnExamRoomSeat(){
		var roomIds          = new Array(); 
		var examSubId        = jQuery("#chooseExamSubId").val(); 
		 
		jQuery("#batchExamSeatExamRoomListBody input[name='resourceid']:checked").each(function(){
			roomIds.push(jQuery(this).val());
		})
		if(""==examSubId){
			alertMsg.warn("请选择考试批次，点击查询!");
			jQuery("#examSub").focus();
			return;
		}
		if( roomIds.length==0){
			alertMsg.warn("请选择要清空座位的考场");
			jQuery("#examroom_list_check_all").focus();
			return;
		}
		var url = "${baseUrl}/edu3/teaching/exam/seat/clearnRommSeat.html";
		alertMsg.confirm("确定要清空所选考场的座位吗？",{
			okCall:function(){
				
				jQuery.ajax({
					type:"post",
					url:url,
					data:"examRoomId="+roomIds.toString()+"&examSubId="+examSubId,
					dataType:"json",
					success:function(resultData){
						if(resultData==true){
							alertMsg.info("操作成功！");
							var branchSchool = jQuery("#branchSchool").val();
							var gradeid      = jQuery("#gradeid").val();
							var major        = jQuery("#major").val();
							var classic      = jQuery("#classic").val();
							var examSub      = jQuery("#examSub").val();
							var courseId     = jQuery("#courseId").val();
							var gradeid      = jQuery("#gradeid").val();
							var reloadURL    = "${baseUrl}/edu3/teaching/exam/seat/list.html?branchSchool="+branchSchool
								             + "&gradeid="+gradeid+"&major="+major+"&classic="+classic+"&examSub="+examSub
								             +"&courseId="+courseId+"&gradeid="+gradeid;
							navTab.reload(reloadURL,$("#batchExamSeatSearchForm").serializeArray());
						}else{
							alertMsg.warn("操作失败！");
						}
					}
				});
			}
		});
		
	}
	//根据课程取消座位安排
	function cleanSeatByCourse(){
		var courseId         = jQuery("#chooseCourseId").val(); 
		var examSubId        = jQuery("#chooseExamSubId").val(); 
		
		jQuery("#batchExamSeatExamRoomListBody input[name='resourceid']:checked").each(function(){
			roomIds.push(jQuery(this).val());
		})
		
		if(""==courseId){
			alertMsg.warn("请选择考试课程，点击查询！");
			jQuery("#courseId").focus();
			return;
		}
		if(""==examSubId){
			alertMsg.warn("请选择考试批次，点击查询!");
			jQuery("#examSub").focus();
			return;
		}
		var url = "${baseUrl}/edu3/teaching/exam/seat/clearnRommSeatByCourse.html";
		alertMsg.confirm("确定要清空所选课程的座位吗？",{
			okCall:function(){
				
				jQuery.ajax({
					type:"post",
					url:url,
					data:$("#batchExamSeatSearchForm").serializeArray(),
					dataType:"json",
					success:function(resultData){
						if(resultData==true){
							alertMsg.info("操作成功！");
							var branchSchool = jQuery("#branchSchool").val();
							var gradeid      = jQuery("#gradeid").val();
							var major        = jQuery("#major").val();
							var classic      = jQuery("#classic").val();
							var examSub      = jQuery("#examSub").val();
							var courseId     = jQuery("#courseId").val();
							var gradeid      = jQuery("#gradeid").val();
							var reloadURL    = "${baseUrl}/edu3/teaching/exam/seat/list.html";
							navTab.reload(reloadURL,$("#batchExamSeatSearchForm").serializeArray());
						}else{
							alertMsg.warn("操作失败！");
						}
					}
				});
			}
		});
		
	}
	*/
	//查看考场分配的考生
	function viewAssignedExamStuInfo(flag,roomId,examsubId,examInfoId,branchSchool,examTimeSegment){
		var url ="${baseUrl}/edu3/teaching/exam/seat/viewExmaRoomResult.html?flag="+flag+"&examRoomId="+roomId+"&examSubId="+examsubId+"&examInfoId="+examInfoId+"&branchSchool="+branchSchool+"&examSub_examTimeSegment="+examTimeSegment;
		$.pdialog.open(url,'RES_TEACHING_EXAM_SEAT_LIST', '座位列表', {width: 800, height: 400});
	}
	//试室安排总表
	function exportExamRoomPlanExcelCondition(){
		var url ="${baseUrl}/edu3/teaching/exam/seat/exportExamRoomPlanExcelCondition.html";
		$.pdialog.open(url,'RES_TEACHING_EXAM_EXPORTROOMPLAN', '试室安排总表', {width: 800, height: 400});
	}
</script>
</body>
</html>