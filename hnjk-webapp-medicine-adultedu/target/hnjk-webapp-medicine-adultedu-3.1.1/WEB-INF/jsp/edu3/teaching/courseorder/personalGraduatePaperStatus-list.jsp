<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>个人预约学习权限设置</title>


</head>
<body>
	<div class="page">
		<div class="pageHeader">
			<form id="personalGraduatePaperStatusSearchForm"
				onsubmit="return navTabSearch(this);"
				action="${baseUrl }/edu3/teaching/courseorder/personal-graduatepaper-bookstatus-list.html"
				method="post">
				<div class="searchBar">
					<ul class="searchContent">
						<li><label>教学站：</label> <gh:brSchoolAutocomplete
								name="branchSchool" tabindex="1"
								id="graduateMentor_brSchoolName"
								defaultValue="${condition['branchSchool']}" style="width:55%"
								displayType="code" /> <%-- 	<gh:selectModel name="branchSchool" bindValue="resourceid" displayValue="unitName" style="width:120px" 
							modelClass="com.hnjk.security.model.OrgUnit" value="${condition['branchSchool']}"  condition="unitType='brSchool'"/>--%>
						</li>

						<li><label>层次：</label>
						<gh:selectModel name="classic" bindValue="resourceid"
								displayValue="classicName" value="${condition['classic']}"
								modelClass="com.hnjk.edu.basedata.model.Classic"
								style="width:55%" /></li>
						<li><label>年级：</label>
						<gh:selectModel name="gradeid" bindValue="resourceid"
								displayValue="gradeName" style="width:55%"
								modelClass="com.hnjk.edu.basedata.model.Grade"
								orderBy="gradeName desc" value="${condition['gradeid']}" /></li>



						<li><label>专业：</label>
						<gh:selectModel name="major" bindValue="resourceid"
								displayValue="majorCodeName" value="${condition['major']}"
								modelClass="com.hnjk.edu.basedata.model.Major" style="width:55%"
								orderBy="majorCode" /></li>

					</ul>
					<ul class="searchContent">
						<li><label>姓名：</label><input type="text" name="name"
							value="${condition['name']}" style="width: 55%" /></li>
						<li><label>学号：</label><input type="text"
							name="matriculateNoticeNo"
							value="${condition['matriculateNoticeNo']}" style="width: 55%" />
						</li>
						<li><label>论文预约状态</label> <select name="isAbleOrderSubject"
							style="width: 55%">
								<option value="">请选择</option>
								<option value="0"
									<c:if test="${condition['isAbleOrderSubject'] eq '0'}"> selected="selected"</c:if>>屏蔽</option>
								<option value="1"
									<c:if test="${condition['isAbleOrderSubject'] eq '1'}"> selected="selected"</c:if>>开放</option>
						</select></li>
						<div class="buttonActive" style="float: right">
							<div class="buttonContent">
								<button type="submit">查 询</button>
							</div>
						</div>
					</ul>

				</div>
			</form>
		</div>
		<div class="pageContent">
			<gh:resAuth parentCode="RES_TEACHING_THESIS_GRADUATEPAPERSTATUS_LIST"
				pageType="list"></gh:resAuth>
			<table class="table" layouth="138">
				<thead>
					<tr>
						<th width="3%"><input type="checkbox" name="checkall"
							id="check_all_graduatepaper_info"
							onclick="checkboxAll('#check_all_graduatepaper_info','resourceid','#graduatePaperInfoBody')" /></th>
						<th width="8%">姓名</th>
						<th width="12%">学号</th>
						<th width="3%">性别</th>
						<th width="10%">身份证</th>
						<th width="5%">民族</th>
						<th width="8%">年级</th>
						<!--   <th width="10%">入学日期</th>-->
						<th width="10%">培养层次</th>
						<th width="10%">专业</th>
						<th width="15%">教学站</th>
						<th width="6%">论文预约权限</th>
						<th width="5%">账号状态</th>
						<th width="5%">交费状态</th>
					</tr>
				</thead>
				<tbody id="graduatePaperInfoBody">
					<c:forEach items="${stulist.result}" var="stu" varStatus="vs">
						<tr>
							<td><input type="checkbox" name="resourceid"
								value="${stu.resourceid }" autocomplete="off" /></td>
							<td><a href="#" onclick="viewStuInfo2('${stu.resourceid}')"
								title="点击查看">${stu.studentBaseInfo.name }</a></td>
							<td>${stu.studyNo}</td>
							<td>${ghfn:dictCode2Val('CodeSex',stu.studentBaseInfo.gender) }</td>
							<td>${stu.studentBaseInfo.certNum }</td>
							<td>${ghfn:dictCode2Val('CodeNation',stu.studentBaseInfo.nation) }</td>
							<td>${stu.grade.gradeName}</td>
							<!--   <td ></td>-->
							<td>${stu.classic.classicName }</td>
							<td>${stu.major.majorName }</td>
							<td>${stu.branchSchool}</td>
							<c:if
								test="${stu.isAbleOrderSubject==1 or  empty stu.isAbleOrderSubject}">
								<td>开放</td>
							</c:if>
							<c:if test="${stu.isAbleOrderSubject==0 }">
								<td>屏蔽</td>
							</c:if>
							<c:if test="${ stu.accountStatus == 1}">
								<td>激活</td>
							</c:if>
							<c:if test="${ stu.accountStatus == 0}">
								<td style="color: red">停用</td>
							</c:if>
							<td><a href="javascript:void(0)"
								onclick="checkFeeStatus('${stu.studyNo}')">查看</a></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<gh:page page="${stulist}"
				goPageUrl="${baseUrl }/edu3/teaching/courseorder/personal-graduatepaper-bookstatus-list.html"
				pageType="sys" condition="${condition}" />
		</div>

	</div>
	<script type="text/javascript">
	function viewStuInfo2(id){
		var url = "${baseUrl}/edu3/framework/studentinfo/view.html";
		$.pdialog.open(url+'?act=view&resourceid='+id, 'RES_SCHOOL_SCHOOLROLL_MANAGER_VIEW', '查看学籍', {width: 800, height: 600});　
	}
	function checkFeeStatus(studentId){
		$.ajax({
			type:"post",
			url:"${baseUrl}/edu3/teaching/courseorder/checkFeeStatus.html",
			data:({matriculateNoticeNo:studentId}),
			dataType: 'json',
			success:function(date){          	   		
         		 if(date.isFee === true){         		 	
         		 	 alertMsg.correct(date.msg);
         		 }else{
         		 	 alertMsg.warn(date.msg);
         		 }         
          	}      
			
		},"json");
	}
	function personalGraduatePaperBookDisenable(){
		
		var idArray = new Array();
		var ids = "";
		jQuery("input[name='resourceid']:checkbox").each(function(index){
			if(jQuery(this).attr("checked")){
				ids = ids + ","+jQuery(this).val() ;
				idArray.push(index);
			}
		})
		
		if(""==ids){
			alertMsg.info('请选择要屏蔽权限的学生!')
			return ;
		}else{
			var url = "${baseUrl}/edu3/teaching/courseorder/bookingStatus.html?orderCourseStatusConfigType=graduatePaper&orderCourseStatus=0&sturesourceId="+ids.substring(1);
			$.ajax({
				type:"post",
				url:url,
				success:function(msg){          	   		
	         		 alertMsg.correct(msg);
	         		 navTab.reload("${baseUrl}/edu3/teaching/courseorder/personal-graduatepaper-bookstatus-list.html",$("#searchForm").serializeArray());
	          	}      
			});
		}
	}
	function personalGraduatePaperBookEnable(){
		var idArray = new Array();
		var ids = "";
		jQuery("input[name='resourceid']:checkbox").each(function(index){
			if(jQuery(this).attr("checked")){
				ids = ids + ","+jQuery(this).val() ;
				idArray.push(index);
			}
		})
		
		if(""==ids){
			alertMsg.info('请选择要屏蔽权限的学生!')
			return ;
		}else{
			var url = "${baseUrl}/edu3/teaching/courseorder/bookingStatus.html?orderCourseStatusConfigType=graduatePaper&orderCourseStatus=1&sturesourceId="+ids.substring(1);
			$.ajax({
				type:"post",
				url:url,
				success:function(msg){          	   		
	         		 alertMsg.correct(msg);
	         		 navTab.reload("${baseUrl}/edu3/teaching/courseorder/personal-graduatepaper-bookstatus-list.html",$("#searchForm").serializeArray());
	          	}      
			});
		}
	}
</script>
</body>
</html>