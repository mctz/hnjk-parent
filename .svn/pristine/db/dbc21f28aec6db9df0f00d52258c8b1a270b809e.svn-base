<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>${school}${schoolConnectName}平台--常用下载</title>

</head>
<script type="text/javascript">

function simdown(n){
    var name = n ;
    var url = '${baseUrl }/edu3/framework/filemanage/public/simpleDownload.html?name='+name+'&subPath=downloads';
    window.open(url,"download");
}

</script>
<body>
	<div>
		<div>${school}常用下载</div>
		<ul>
			<ul>
				<c:forEach items="${fileList}" var="file" varStatus="vs">
					<li><a>${file.name}</a> <a href="##"
						onclick="simdown('${file.name}');">点击下载</a></li>
				</c:forEach>
			</ul>
		</ul>
	</div>
</body>
</html>


