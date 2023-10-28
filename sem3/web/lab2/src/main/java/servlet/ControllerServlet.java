package servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Point;
import model.ValuesValidation;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import java.util.stream.Collectors;

@WebServlet(name = "ControllerServlet", value = "/controller-servlet")
public class ControllerServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		Map<String, String[]> parameterMap = request.getParameterMap();

		List<Point> points = new ArrayList<>();
		List<Point> pointsFromCanvas = null;
		try {
			Double r = Double.valueOf(request.getParameter("value_r"));
			String values = request.getParameter("values");
			if (values != null && !"".equals(values)) {
				pointsFromCanvas = Stream.of(values.split(";")).map(value -> {
					Double x = Double.valueOf(value.substring(0, value.indexOf('|')));
					Double y = Double.valueOf(value.substring(value.indexOf('|') + 1));
					return new Point(x, y, r);
				}).collect(Collectors.toList());
			}

			String[] xValues = parameterMap.get("value_x[]");
			Double y = Double.valueOf(request.getParameter("input_y").replace(',', '.'));
			List<Point> pointsFromInputs = Stream.of(xValues).map(Double::valueOf).map(x -> new Point(x, y, r)).collect(Collectors.toList());
			points.addAll(pointsFromInputs);
		} catch (Exception exc) {
			// exc.printStackTrace();
		}
		
		ValuesValidation validation = new ValuesValidation(points);
		validation.validate(points);
		points = validation.getPoints();

		if (pointsFromCanvas != null) {
			points.addAll(0, pointsFromCanvas);
		}
		
		if (points.isEmpty()) {
			request.setAttribute("error", "Указаны неправильные координаты");
			request.getRequestDispatcher("index.jsp").forward(request, response);
			return;
		}
		
		request.setAttribute("points", points);
		request.getRequestDispatcher("/AreaCheckServlet").forward(request, response);
	}

	@Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

}