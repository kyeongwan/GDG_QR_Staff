

 <!DOCTYPE html>
<html>
	<head>
	<meta charset="utf-8">
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.5.2/jquery.min.js"></script>
	</head>
	<body>
	<div id="result">

	</div>
	<script type="text/javascript">
			$(document).ready(function(){
				$.ajax({
						type: 'POST',
						dataType: 'html',
						url: 'https://script.google.com/macros/s/AKfycbzzkeKSDBWJOjcllgU564zpeYHKhFxRYpjnk7NE15hcIjJ53z8/exec',
						data: {"row" : <?php echo $_GET["row"]+1 ?>, "mail" : "<?php echo $_GET["mail"] ?>"},
						success: function (result) {
							var data = JSON.parse(result);
							if(data.result == "success")
								$('#result').html("확인되었습니다.");	
							else if(data.result == "NoSuchEmailError"){
								$('#result').html("잘못된 접근 방법입니다.");		
							}else{
								$('#result').html("알 수 없는 에러입니다.");		
							}
						}
				});
			});
		</script>
	</body>
</html>