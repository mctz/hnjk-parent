<!-- firefox -->
	<object id="print" classid="java:com.hnjk.applet.printer.EmbeddedViewerApplet.class" 
		codebase="${basePath }/applets/"
		codetype="application/x-java-applet" 
		archive="hnjk-webapp-applet-3.2.10.jar,commons-logging-1.0.4.jar,commons-digester-1.7.jar,commons-collections-2.1.1.jar,jasperreports-applet-3.7.4.jar,jasperreports-3.7.4.jar" 
		REPORT_URL = "${reportUrl}"
		SID="${SID}" 
		standby="正在载入打印程序..." 
		WIDTH = "${width}" 
		HEIGHT = "${height}"
		name="print">
			<!-- 兼容IE -->
			<object id="print1" classid="clsid:8AD9C840-044E-11D1-B3E9-00805F499D93"
			standby="正在载入打印程序..."  
			WIDTH = "${width}" 
			HEIGHT = "${height}"
			name="print1"
			codebase="${basePath }/common/jinstall-6u14-windows-i586.cab#Version=6,0,0,8">
			<param name="code" value="com.hnjk.applet.printer.EmbeddedViewerApplet.class">
			<param name="archive" value="hnjk-webapp-applet-3.2.10.jar,commons-logging-1.0.4.jar,commons-digester-1.7.jar,commons-collections-2.1.1.jar,jasperreports-applet-3.7.4.jar,jasperreports-3.7.4.jar">
			<param name="codebase" value="${basePath }/applets/">
			<param name="type" value="application/x-java-applet;version=1.4">
			<PARAM NAME = "REPORT_URL" VALUE ="${reportUrl}">
			<PARAM NAME = "SID" VALUE ="${SID}">		
				<!--兼容chrome-->				
				<applet id="print2" classid="java:com.hnjk.applet.printer.EmbeddedViewerApplet.class" 
					code="com.hnjk.applet.printer.EmbeddedViewerApplet.class" 
					codebase="${basePath }/applets/"
					codetype="application/x-java-applet" 
					archive="hnjk-webapp-applet-3.2.10.jar,commons-logging-1.0.4.jar,commons-digester-1.7.jar,commons-collections-2.1.1.jar,jasperreports-applet-3.7.4.jar,jasperreports-3.7.4.jar" 
					standby="正在载入打印程序..." 
					WIDTH = "${width}" 
					HEIGHT = "${height}"
					name="print2">	
					<PARAM NAME = "REPORT_URL" VALUE ="${reportUrl}">
					<PARAM NAME = "SID" VALUE ="${SID}">		
				</applet> 			
			</object>
		</object>