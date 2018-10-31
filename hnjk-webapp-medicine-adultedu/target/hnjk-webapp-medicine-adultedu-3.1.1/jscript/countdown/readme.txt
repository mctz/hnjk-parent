<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html;charset=utf-8">
<title>jQuery Countdown</title>
<style type="text/css">
@import "jquery.countdown.css";

#shortly { width: 240px; height: 45px; }
</style>
<script type="text/javascript" src="jquery-1.3.2.min.js"></script>
<script type="text/javascript" src="jquery.countdown.js"></script>
<script type="text/javascript" src="jquery.countdown-zh-CN.js"></script>

<script type="text/javascript">
$(function () {
	var shortly;
	$('#shortly').countdown({until: shortly, onExpiry: liftOff, onTick: watchCountdown});

	$('#shortlyStart').click(function() { 
    shortly = new Date();
    shortly.setSeconds(shortly.getSeconds() + 1200); 
    $('#shortly').countdown('change', {until: shortly}); 
});

});

 
function liftOff() { 
    alert('时间到!'); 
} 
 
function watchCountdown(periods) { 
    $('#monitor').text('Just ' + periods[5] + ' minutes and ' + 
        periods[6] + ' seconds to go'); 
}
</script>
</head>
<body>
<h1>jQuery Countdown Basics</h1>
<button id="shortlyStart" type="button">开始</button>
<div id="shortly"></div>
</body>
</html>
