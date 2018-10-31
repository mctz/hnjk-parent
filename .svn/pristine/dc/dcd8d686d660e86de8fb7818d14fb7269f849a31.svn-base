<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
  <head>
    <title>教学计划</title>
    <meta charset="utf-8">
    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"> 
    <meta http-equiv="expires" content="0">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" type="text/css" href="${baseUrl }/css/bootstrap.min-3.3.6.css">
    <link rel="stylesheet" href="${baseUrl }/css/bootstrap-grid.min.css">
	<link rel="stylesheet" href="${baseUrl }/css/zoomify.min.css">
	<link rel="stylesheet" href="${baseUrl }/css/style.css">
    <link rel="stylesheet" type="text/css" href="${baseUrl }/css/htmleaf-demo.css">
    <link rel="stylesheet" type="text/css" href="${baseUrl }/css/plan.css">
  </head>
  <body>
  	<div class="htmleaf-container">
		<div class="demo" style="padding: 1em 0;">
		        <div class="container">
		            <div class="row">
		                <div class="col-md-offset-3 col-md-6">
		                    <div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">
		                        <c:forEach items="${majorList }" var="p" varStatus="status">
                			        <div class="panel panel-default">
			                            <div class="panel-heading" role="tab" id="heading${status.index+1 }">
			                                <h4 class="panel-title">
			                                    <a class="${status.index!=0?'collapsed':'' }" role="button" data-toggle="collapse" data-parent="#accordion" href="#collapse${status.index+1 }" aria-expanded="${status.index!=0?false:true }" aria-controls="collapse${status.index+1 }">
			                                       ${p.majorName }
			                                    </a>
			                                </h4>
			                            </div>
			                            <div id="collapse${status.index+1 }" class="panel-collapse collapse ${status.index!=0?'' :' in' }" role="tabpanel" aria-labelledby="heading${status.index+1 }">
			                                <div class="panel-body" align="center">
		                                    	<p>
			                                    	<c:choose>
			                                    		<c:when test="${not empty p.picture4course }">
			                                    			<img src="${p.picture4course }" width="300" height="400" data-duration="500" data-easing="linear" data-scale="1">
			                                    		</c:when>
			                                    		<c:otherwise>
			                                    			目前没有数据
			                                    		</c:otherwise>
			                                    	</c:choose>
		                                    	</p>
			                                </div>
			                            </div>
			                        </div>
		                        </c:forEach>
		                    </div>
		                </div>
		            </div>
		        </div>
		    </div>
	</div>
  	
  	<script type="text/javascript" src="${baseUrl }/js/jquery-1.11.0.min.js"></script>
  	<script type="text/javascript" src="${baseUrl }/js/bootstrap.min-3.3.6.js"></script>
  	<script src="${baseUrl }/js/zoomify.min.js"></script>
	<script type="text/javascript">
		$('.panel-body img').zoomify();
	</script>
  </body>
</html>
