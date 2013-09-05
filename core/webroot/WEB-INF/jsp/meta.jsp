
<!-- HTTP 1.1 -->
<meta http-equiv="Cache-Control" content="no-store" />
<!-- HTTP 1.0 -->
<meta http-equiv="Pragma" content="no-cache" />
<meta http-equiv="Expires" content="0" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="icon" href="<c:url value="/images/favicon.ico"/>" /> 
<link href="<c:url value="/template/css/taxpayer.css"/>" rel="stylesheet" type="text/css"> 
<script src="<c:url value="/template/js/jquery-1.8.2.min.js"/>" type="text/javascript"></script>
<script type="text/javascript">
	$(function() {
		$('#chooseAll').click(function() {
			var list = $('[name=ids]').length;
			if ($('#chooseAll').attr("checked") == 'checked') {
				for ( var i = 0; i < list; i++) {
					$('[name=ids]').attr("checked", 'true');
				}
			} else {
				$('[name=ids]').click();
			}
		});
	});
</script>