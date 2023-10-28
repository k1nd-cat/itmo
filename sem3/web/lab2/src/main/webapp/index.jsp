<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">

<head>
  <meta charset="utf-8" />
  <title>Lab1_2629</title>
  <link href="css/main.css" rel="stylesheet" type="text/css" />
</head>
<body>

<%if (request.getAttribute("error") != null) { %>
<div class="errorDiv">
<%=request.getAttribute("error")%>
</div>
<% } %>

<form action="controller-servlet" method="get">
  <input type="hidden" id="values" name="values" />
  <table class="table" border="0" cellpadding="0" cellspacing="0">
    <thead id="head">
    <tr>
      <th colspan="2" class = table>
        <div class="header-text">Трошкин Александр Евгеньевич P3216 18271</div>
      </th>
    </tr>
    </thead>

    <tbody>
    <tr id="canvas">
      <td colspan="2"><canvas id='schedule' height="270px"></canvas></td>
    </tr>

    <tr class="values">
      <td class="values"><label id="label_x">Значение X</label></td>
      <td class="values_input">
<!--        <input class="input_text" type="text" id="input_x" name="input_x"/>-->
        <label>-3<input type="checkbox" name="value_x[]" value="-3"></label>
        <label>-2<input type="checkbox" name="value_x[]" value="-2"></label>
        <label>-1<input type="checkbox" name="value_x[]" value="-1"></label>
        <label>0<input type="checkbox" name="value_x[]" value="0"></label>
        <label>1<input type="checkbox" name="value_x[]" value="1"></label>
        <label>2<input type="checkbox" name="value_x[]" value="2"></label>
        <label>3<input type="checkbox" name="value_x[]" value="3"></label>
        <label>4<input type="checkbox" name="value_x[]" value="4"></label>
        <label>5<input type="checkbox" name="value_x[]" value="5"></label>
      </td>
    </tr>

    <tr class="values">
      <td class="values"><label for="input_y" id="label_y">Значение Y: (-3 ... 3)</label></td>
      <td class="values_input">
        <input class="input_text" type="text" id="input_y" name="input_y"/>
      </td>
    </tr>

    <tr class="values">
      <td class="values"><label for="r1" id="label_r">Значение R</label></td>
      <td class="values_input">
        <label>
          <input type="radio" name="value_r" id="r1" value="1">1
        </label>
        <label>
          <input type="radio" name="value_r" id="r2" value="1.5">1.5
        </label>
        <label>
          <input type="radio" name="value_r" id="r3" value="2">2
        </label>
        <label>
          <input type="radio" name="value_r" id="r4" value="2.5">2.5
        </label>
        <label>
          <input type="radio" name="value_r" id="r5" value="3">3
        </label>
      </td>
    </tr>

    <tr>
      <td colspan="2" id="button_str">
        <button type="submit" id="check" name="button" onclick="isValidateValues()">Проверить</button>
      </td>
    </tr>

    
    </tbody>
  </table>
</form>
<script type="text/javascript" src="js/canvas.js"></script>
<script type="text/javascript" src="js/validation.js"></script>
</body>
</html>