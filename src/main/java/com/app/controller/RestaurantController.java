package com.app.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import com.app.DaoImp.RestaurantDaoImpl;
import com.app.model.Restaurant;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/restaurant")
public class RestaurantController extends HttpServlet {
	
	@SuppressWarnings("unchecked")
	@Override
	protected void doGet(HttpServletRequest req,HttpServletResponse res) throws ServletException, IOException {
		
		RestaurantDaoImpl rdao=new RestaurantDaoImpl();
		List<Restaurant> restaurants=rdao.getAllRestaurants();
		
		
		req.setAttribute("restaurants", restaurants);
		
		RequestDispatcher rd=req.getRequestDispatcher("restaurant.jsp");
		rd.forward(req, res);
	}

}
