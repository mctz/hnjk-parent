<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>考场座位列表</title>


</head>
<body>
	<div class="page">
		<div class="pageHeader">
			<form id="examRoomSeatSearchForm"
				onsubmit="return _checkPrintroomFormPost(this);"
				action="${baseUrl}/edu3/teaching/exam/seat/examRoomSeatList.html"
				method="post">
				<input type="hidden" id="_examRoomSeat_chooseExamSubId"
					value="${condition['examSub']}" /> <input type="hidden"
					id="_examRoomSeat_chooseBranchSchoolId"
					value="${condition['branchSchool']}" />
				<div class="searchBar">
					<ul class="searchContent">
						<c:if test="${!isBrschool}">
							<li><label>教学站：</label> <gh:brSchoolAutocomplete
									name="branchSchool" tabindex="1"
									id="batchExamSeat_eiinfo_brSchoolName"
									defaultValue="${condition['branchSchool']}" displayType="code"
									style="width:55%" /></li>
						</c:if>
						<li><label>预约批次：</label> <gh:selectModel id="examSub"
								name="examSub" bindValue="resourceid" displayValue="batchName"
								style="width:55%" orderBy="batchName desc "
								modelClass="com.hnjk.edu.teaching.model.ExamSub"
								value="${condition['examSub']}"
								condition="batchType='exam',examsubStatus='2'" /></li>
						<c:if test="${!isBrschool}">
							<span class="tips">提示：教学站、预约批次为必选条件</span>
						</c:if>
					</ul>
					<div class="buttonActive" style="float: right">
						<div class="buttonContent">
							<button type="submit">查 询</button>
						</div>
					</div>
				</div>
			</form>
		</div>
		<gh:resAuth parentCode="RES_TEACHING_EXAM_ROOM_SEAT_LIST"
			pageType="list"></gh:resAuth>
		<div class="pageContent" layoutH="98">

			<div
				style="float: right; display: block; width: 98%; margin-bottom: 16px; padding: 5px 15px 5px 0px; line-height: 21px;">
				<div>
					<table class="list" width="100%">
						<thead>
							<tr>
								<th width="10%"><input type="checkbox" name="checkall"
									id="examroom_seatlist_check_all"
									onclick="checkboxAll('#examroom_seatlist_check_all','resourceid','#examRoomSeatListBody')" /></th>
								<th width="30%">试室</th>
								<th width="30%">容量</th>
								<th width="30%">已按排座位数</th>
							</tr>
						</thead>
						<tbody id="examRoomSeatListBody">
							<c:forEach items="${examRoomList }" var="examRoom">
								<tr>
									<td><input type="checkbox" name="resourceid"
										value="${examRoom.RESOURCEID }" autocomplete="off" /></td>
									<td>${examRoom.EXAMROOMNAME }</td>
									<td>${examRoom.EXAMROOMSIZE }</td>
									<td><a href="javascript:void(0)"
										onclick="viewExamStuInfo('${examRoom.RESOURCEID}','${condition['examSub']}','${condition['branchSchool']}')">${examRoom.ASSIGNED }</a></td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>

			</div>

		</div>

	</div>
	<script type="text/javascript">
	
	function _checkPrintroomFormPost(obj){
		if($('#examRoomSeatSearchForm #batchExamSeat_eiinfo_brSchoolName').val() ==''){
			alertMsg.warn("请选择要打印座位表的教学站，点击查询！");			
			return false;
		}
		if($('#examRoomSeatSearchForm #examSub').val() == ''){
			alertMsg.warn("请选择要打印座位表的考试批次，点击查询!");		
			return false;
		}
		return navTabSearch(obj);
	}
	
	function printRoomSeatList(){
		
		var roomIds          = new Array(); 
		var examSubId        = jQuery("#_examRoomSeat_chooseExamSubId").val(); 
		var branchSchool     = jQuery("#_examRoomSeat_chooseBranchSchoolId").val(); 
		jQuery("#examRoomSeatListBody input[name='resourceid']:checked").each(function(){
			roomIds.push(jQuery(this).val());
		})
		
		if( roomIds.length==0){
			alertMsg.warn("请选择要打印座位表的考场!");
			jQuery("#examroom_list_check_all").focus();
			return;
		}
		if(""==branchSchool){
			alertMsg.warn("请选择要打印座位表的教学站，点击查询！");			
			return false;
		}
		alertMsg.confirm("确定要打印所选考场的座位表吗？",{
			okCall:function(){
				var url = "${baseUrl}/edu3/teaching/exam/seat/printRoomSeatList-view.html";
				    url += "?examRoomIds="+roomIds.toString()+"&examSubId="+examSubId+"&branchSchool="+branchSchool;
				$.pdialog.open(url,'RES_TEACHING_EXAM_ROOM_SEAT_LIST','打印预览',{height:600, width:800});
			}
		})
	}
	//查看考场分配的考生
	function viewExamStuInfo(roomId,examsubId,brSchool){
		var url ="${baseUrl}/edu3/teaching/exam/seat/viewExmaRoomResult.html?examRoomId="+roomId+"&examSubId="+examsubId+"&branchSchool="+brSchool;
		$.pdialog.open(url,'RES_TEACHING_EXAM_SEAT_LIST', '座位列表', {width: 800, height: 400});
	}
	//导出期末考试座位表
	function exportRoomSeatList(){
		var roomIds          = new Array(); 
		var examSubId        = jQuery("#_examRoomSeat_chooseExamSubId").val(); 
		var branchSchool     = jQuery("#_examRoomSeat_chooseBranchSchoolId").val(); 
		jQuery("#examRoomSeatListBody input[name='resourceid']:checked").each(function(){
			roomIds.push(jQuery(this).val());
		})
		
		if( roomIds.length==0){
			alertMsg.warn("请选择要导出座位表的考场!");
			jQuery("#examroom_list_check_all").focus();
			return;
		}
		if(""==branchSchool){
			alertMsg.warn("请选择要导出座位表的教学站，点击查询！");			
			return false;
		}
		alertMsg.confirm("确定要导出所选考场的座位表吗？",{
			okCall:function(){
				var url = "${baseUrl}/edu3/teaching/exam/seat/exportRoomSeatList.html";
				    url += "?examRoomIds="+roomIds.toString()+"&examSubId="+examSubId+"&branchSchool="+branchSchool;
				    $('#frame_exportRecruitExamSeat').remove();
					var iframe = document.createElement("iframe");
					iframe.id = "frame_exportRecruitExamSeat";
					iframe.src = url;
					iframe.style.display = "none";
					//创建完成之后，添加到body中
					document.body.appendChild(iframe);
			}
		})
	}

</script>
</body>
</html>