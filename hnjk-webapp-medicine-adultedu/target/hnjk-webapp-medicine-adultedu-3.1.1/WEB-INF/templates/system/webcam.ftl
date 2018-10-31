<!-- firefox -->
	<object id="webcam" classid="java:com.hnjk.applet.webcam.WebCamApplet.class" 
		codebase="${basePath }/applets/"
		codetype="application/x-java-applet" 
		archive="hnjk-webapp-applet-3.2.10.jar,jmf.jar" 
		uploadurl = "${uploadurl}" 
		stuid = "${id}"
		storePath = "${storePath}"
		replaceName = "${replaceName}"
		studentName = "${studentName}"		
		standby="正在载入..."		
		WIDTH = "100%" 
		HEIGHT = "100%"
		name="webcam">
			<!-- 兼容IE -->
			<object id="webcam" classid="clsid:8AD9C840-044E-11D1-B3E9-00805F499D93"
			standby="正在载入..."			
			name="webcam"
			WIDTH = "100%" 
			HEIGHT = "100%"
			codebase="${basePath }/common/jinstall-6u14-windows-i586.cab#Version=6,0,0,8">
			<param name="code" value="com.hnjk.applet.webcam.WebCamApplet.class">
			<param name="archive" value="hnjk-webapp-applet-3.2.10.jar,jmf.jar">
			<param name="codebase" value="${basePath }/applets/">
			<param name="type" value="application/x-java-applet;version=1.4">
			<PARAM NAME = "uploadurl" VALUE ="${uploadurl}">
			<PARAM NAME = "stuid" VALUE ="${id}">
			<PARAM NAME = "storePath" VALUE ="${storePath}">
			<PARAM NAME = "replaceName" VALUE ="${replaceName}">
			<PARAM NAME = "studentName" VALUE ="${studentName}">
				<!--兼容chrome-->
				<applet id="webcame" classid="java:com.hnjk.applet.webcam.WebCamApplet.class" 
					code="com.hnjk.applet.webcam.WebCamApplet.class" 
					codebase="${basePath }/applets/"
					codetype="application/x-java-applet" 
					archive="hnjk-webapp-applet-3.2.10.jar,jmf.jar" 				
					standby="正在载入..." 
					WIDTH = "100%" 
					HEIGHT = "100%"
					name="webcame">	
					<PARAM NAME = "uploadurl" VALUE ="${uploadurl}">
					<PARAM NAME = "stuid" VALUE ="${id}">
					<PARAM NAME = "storePath" VALUE ="${storePath}">
					<PARAM NAME = "replaceName" VALUE ="${replaceName}">
					<PARAM NAME = "studentName" VALUE ="${studentName}">		
				</applet> 
				
			</object>
		</object>