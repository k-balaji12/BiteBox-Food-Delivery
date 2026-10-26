package com.app.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import com.app.DaoImp.MenuDaoImpl;
import com.app.model.Menu;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
@WebServlet("/menu")
public class MenuController extends HttpServlet{
	
	@Override 
	protected void doGet(HttpServletRequest req,HttpServletResponse res) throws ServletException,IOException {
		
		int restId=Integer.parseInt(req.getParameter("restaurantId"));
		
		MenuDaoImpl mdao=new MenuDaoImpl();
		//res.setContentType("text/html");
		List<Menu> menu=mdao.getMenusByRestaurantId(restId);
		//PrintWriter out=res.getWriter();
		
		req.setAttribute("MenuByRestaurant", menu);
		RequestDispatcher rd=req.getRequestDispatcher("menu.jsp");
		
		rd.forward(req, res);
		
//		for(Menu menus:menu) {
//			out.println("<h2>"+menus.getItemName()+"</h2>");
//			out.println("<img src="+menus.getImagePath()+">");
//		}
		
	}
	

}
