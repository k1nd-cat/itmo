package servlet;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import model.Point;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.util.*;

@WebServlet("/AreaCheckServlet")
public class AreaCheckServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unchecked")
	@Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Point> points = (List<Point>)request.getAttribute("points");
        points.forEach(point -> {
        	point.setHitting(checkPointInArea(point.getX(), point.getY(), point.getR()));
        });

        List<Point> allPoints = (List<Point>)request.getSession().getAttribute("allPoints");
        if (allPoints == null) {
        	request.getSession().setAttribute("allPoints", allPoints = new ArrayList<>());
        }

        allPoints.addAll(0, points);
        request.getRequestDispatcher("table.jsp").forward(request, response);
    }

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getSession().setAttribute("allPoints", new ArrayList<>());
		request.getRequestDispatcher("table.jsp").forward(request, response);
		
    }

    private boolean checkPointInArea(double x, double y, double r) {
        if (((x * x + y * y <= r * r) && x >= 0 && y >= 0) ||
                (y >= x - r / 2 && x >= 0 && y <= 0) ||
                (x <= 0 && y >= 0 && x >= -1 * r / 2 && y <= r))
            return true;
        return false;
    }
}