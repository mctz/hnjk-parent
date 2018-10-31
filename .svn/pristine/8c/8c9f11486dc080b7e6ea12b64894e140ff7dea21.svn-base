<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<title>课程检索</title>
<style type="text/css">
.highlight {
	color: red;
	background-color: yellow;
}
</style>
<script type="text/javascript">
		 (function($){ 
			$.fn.extend({
				highlight: function(strings) {		 			
		 			function findText(node, string) {
					  	if (node.nodeType == 3)
					   		 return searchText(node, string);		
					  	else if (node.nodeType == 1 && node.childNodes && !(/(script|style)/i.test(node.tagName))) {
					   		for (var i = 0; i < node.childNodes.length; ++i) {
					    		i += findText(node.childNodes[i], string);
					   		}
					  	}
					  	return 0;		  	
		 			}
		 
				   	function searchText(node, string){
				  		var position = node.data.toUpperCase().indexOf(string);
				   		if (position >= 0)
				    		return highlight(node, position, string);
				    	else
				    		return 0;
				  	}
		  	
				  	 function highlight(node, position, string){
				 		var spannode = document.createElement('span');
				    	spannode.className = 'highlight';
				    	var middlebit = node.splitText(position);
				    	var endbit = middlebit.splitText(string.length);
				    	var middleclone = middlebit.cloneNode(true);
				    	spannode.appendChild(middleclone);
				    	middlebit.parentNode.replaceChild(spannode, middlebit);
				 		return 1;
				 	}
		 
					 return this.each(function() {
					 	if(typeof strings == 'string')
					 		findText(this, strings.toUpperCase());	
					 	else
					 		for (var i = 0; i < strings.length; ++i) findText(this, strings[i].toUpperCase());	
					 });
		        }
		    }); 
		})(jQuery);
	 	$(function (){			
			var kwd = "${keyword}";	
			if(kwd != ""){
				$(".syllabus").highlight(kwd);
			}			
		});
	</script>
</head>
<body>
	<div id="main">
		<div id="left">
			<div class="studyLearn"></div>
			<div id="left_menu">
				<ul>
					<li><a href="#">课程检索</a></li>
				</ul>
				<p>&nbsp;</p>
			</div>
			<!--end menu-->
		</div>
		<!--end left-->
		<div id="right">
			<div class="position">当前位置：课程检索</div>
			<br />
			<div>
				<div id="content">
					<table class="list" width="100%">
						<tbody>
							<c:forEach items="${searchSyllabusVoList }" var="vo"
								varStatus="vs">
								<tr>
									<td width="100%">
										<div class="syllabus" style="line-height: 18px;">
											<a
												href="${baseUrl }/resource/course/materesource.html?syllabusid=${vo.resourceid}"><span>${vo.syllabusName }</span></a>
										</div>
										<div>
											<a class="button"
												href="${baseUrl }/resource/course/materesource.html?resType=0&syllabusid=${vo.resourceid}"><span>讲义</span></a>
											<a class="button"
												href="${baseUrl }/resource/course/materesource.html?resType=1&syllabusid=${vo.resourceid}"><span>授课</span></a>
											<a class="button"
												href="${baseUrl }/resource/course/materesource.html?resType=2&syllabusid=${vo.resourceid}"><span>习题</span></a>
										</div>
									</td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>
</body>
</html>
