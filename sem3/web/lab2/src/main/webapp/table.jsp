<%@page import="java.util.List"%>
<%@page import="model.Point"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Result Table</title>
<link href="css/main.css" rel="stylesheet" type="text/css" />
</head>
<body>
	<form action="index.jsp">
		<div id="goBackDiv">
	    <input id="goBackButtom" type="submit" value="Go back" />
    	</div>
	</form>

	<form action="AreaCheckServlet" method="post">
	<div id="clearDiv">
   <button type="submit" id="clear" name="button_clear" onclick="isButtonClearClicked()">Очистить</button>
   </div>
    <table class="result_table">
      <thead id="result_head">
      <tr>
      <th colspan="4">
      <div class="header-results">Таблица результатов</div>
      </th>
      </tr>
      <tr>
	      <td>X</td>
	      <td>Y</td>
	      <td>R</td>
	      <td>Попадание</td>
      </tr>
      </thead>
      <tbody id="table_out">
	<%
	    List<Point> points = (List<Point>)request.getSession().getAttribute("allPoints");
		for (Point point : points) {
	%>
      <tr>
      <td><%=point.getX()%></td>
      <td><%=point.getY()%></td>
      <td><%=point.getR()%></td>
      <td><%=point.getHitting() ? "да" : "нет"%></td>
      </tr>
	<%
		}
	%>
     </tbody>
   </table>
   </form>
	
</body>
</html>