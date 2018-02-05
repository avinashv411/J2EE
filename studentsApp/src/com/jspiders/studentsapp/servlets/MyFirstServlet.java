package com.jspiders.studentsapp.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



public class MyFirstServlet extends HttpServlet {
	
	@Override
	protected void doGet(HttpServletRequest req,
			             HttpServletResponse resp) 
    throws ServletException, IOException {
		
		//1. Java Code to Generate CurrentDate & Time
		Date dateRef = new Date();
		String currDate = dateRef.toString();
		
		//2.Generate HTML Response
		String htmlRes = "<html>"
						+"<head>"
						+"<meta http-eqiv=\"refresh\" content=\"1\">"
		                +"</head>" 
						+"<body>"
		                +"<h1>"
						+"Current Date & Time is : "
		                +"<font color=\"red\">"
		                +currDate
		                +"</font>"
		                +"</h1>"
		                +"</body>"
		                +"</html>";
		
		//3.Send HTML Response to Browser via Web Server 
		resp.setHeader("Refresh", "1");
		resp.setContentType("text/html");
		PrintWriter out=resp.getWriter();
		out.print(htmlRes);
		
	}//End of doGet

}//End of class
