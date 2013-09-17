<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<script src="resources/jquery-2.0.3.min.js"></script>
</head>
<body>
<form action="submit" method="post">
	<input type="text" name="search" id="search"/>
	<list id="maches"></list>
	<input type="button" value="submit" />
</form>


<script>
$("#search").keyup(function(){
	$.post('autocomplete',{search:$(this).val()}, function(data) {
		var maches = data;
		$("#maches").empty();
		for(var mache in maches){
			$("#maches").append($("<li>").append(maches[mache]));
		}
	});
});
</script>

</body>
</html>