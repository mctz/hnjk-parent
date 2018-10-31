<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>选择开课的信息</title>
<script type="text/javascript">
	
		// 按某个学期给某个教学点开课或调整开课
		function openOrAdjustCourse(){
			var resourceid = $("#openOrAdjustCourse_resourceid").val();
			var guiplanid = $("#openOrAdjustCourse_guiplanid").val();
			var plancourseid = $("#openOrAdjustCourse_plancourseid").val();
			var termid = $("#openOrAdjustCourse_termid").val();
			var isOpen = $("#openOrAdjustCourse_isOpen").val();
			var term = $("#openOrAdjustCourse_term").val();
			var schoolId = $("#openOrAdjustCourse_brSchoolid").val();
			var openCourseUrl = "${baseUrl}/edu3/teaching/teachingplancoursestatus/saveOpenCourse.html"; 
			if(!schoolId && isOpen=="Y") {
				alertMsg.warn("请选择一个教学点");
				return false;
			}
			if(!term){
				alertMsg.warn("请选择开课学期！");
				return false;
			}
			
			$("#openOrAdjustCourse_open").attr("disabled","disabled");
			$("#openOrAdjustCourse_close").attr("disabled","disabled");
			$.ajax({
		          type:"POST",
		          url:openCourseUrl,
		          data:{resourceid:resourceid,guiplanid:guiplanid,plancourseid:plancourseid,termid:termid,isOpen:isOpen,term:term,schoolId:schoolId},
		          dataType:  'json',
		          cache: false,
		          success:function(data){       
		        	 $("#openOrAdjustCourse_open").removeAttr("disabled");
					 $("#openOrAdjustCourse_close").removeAttr("disabled");
	         		 if(data['statusCode'] == 200){ 
	         		 	 alertMsg.correct(data['message']);	  
	         		 	 navTabPageBreak();
	         		 }else if(data['statusCode'] == 400){
	         			alertMsg.confirm(data['message'], {
	        				okCall: function(){	
	        					$.post(openCourseUrl,{resourceid:resourceid,guiplanid:guiplanid,plancourseid:plancourseid,termid:termid,isOpen:isOpen,term:term,schoolId:schoolId,sureOpen:"Y"}, navTabAjaxDone, "json");
	        				}
	        			});
	         		 }else{
	         			alertMsg.error(data['message']);
	         		 }         
		          }            
			});
		}
	</script>
</head>
<body>
	<div align="center">
		<div style="margin-top: 40px;" id="openOrAdjustCourse-selectSchoolAndTerm">
			<input type="hidden" id="openOrAdjustCourse_resourceid" value="${resourceid }" />
			<input type="hidden" id="openOrAdjustCourse_guiplanid" value="${guiplanid }" />
			<input type="hidden" id="openOrAdjustCourse_plancourseid" value="${plancourseid }" />
			<input type="hidden" id="openOrAdjustCourse_termid" value="${termid }" />
			<input type="hidden" id="openOrAdjustCourse_isOpen" value="${isOpen }" />
			<div align="left" style="margin-left: 30px; margin-bottom: 10px;">
				<c:if test="${isOpen=='Y' }">
		    		 教学点：&nbsp;&nbsp;&nbsp; 
		    		<c:choose>
						<c:when test="${isBrschool=='Y' }">
							<input type="hidden" id="openOrAdjustCourse_brSchoolid"
								value="${schoolId }" />
							<input type="text" value="${schoolName }" disabled="disabled"
								style="width: 50%" />
						</c:when>
						<c:otherwise>
							<gh:brSchoolAutocomplete name="openOrAdjustCourse_brSchoolid"
								tabindex="1" id="openOrAdjustCourse_brSchoolid"
								displayType="code" defaultValue="${schoolId}" style="width:50%" />
						</c:otherwise>
					</c:choose>
				</c:if>
				&nbsp;
			</div>
			<div align="left" style="margin-left: 30px; margin-bottom: 10px;">
				开课学期：${openTerm}</div>
		</div>
		<div style="margin-top: 70px; margin-right: 5px;" align="right">
			<button id="openOrAdjustCourse_open" type="button"
				onclick="return openOrAdjustCourse();" style="cursor: pointer;">${isOpen=='Y'?'':'调整' }开课</button>
			<button id="openOrAdjustCourse_close" type="button" class="close"
				onclick="$.pdialog.closeCurrent();" style="cursor: pointer;">取消</button>
		</div>
	</div>
</body>
</html>