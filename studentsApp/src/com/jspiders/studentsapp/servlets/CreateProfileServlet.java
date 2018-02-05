package com.jspiders.studentsapp.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CreateProfileServlet extends HttpServlet{
	
	
	@Override
	protected void doPost(HttpServletRequest req, 
						  HttpServletResponse resp) 
	throws ServletException, IOException {
		
		String regNoVal = req.getParameter("regno");
		String fNameVal = req.getParameter("fName");
		String mNameVal = req.getParameter("mName");
		String lNameVal = req.getParameter("lName");
		String gfNameVal = req.getParameter("gfName");
		String gmNameVal = req.getParameter("gmName");
		String glNameVal = req.getParameter("glName");
		String passVal = req.getParameter("pass");
		String isAdminVal = req.getParameter("isAdmin");
		
		Connection con = null;
		PreparedStatement pstmt= null;
        StringBuffer sbf = new StringBuffer("<html><body>");
		
		try {
			
			//1. Load the Drivers
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			
			//2.Get DB connection via Drivers
			String dburl = "jdbc:mysql://localhost:3306/BECM19_db?user=j2ee&password=j2ee";
			con = DriverManager.getConnection(dburl);
			
			//3.Issue the SQL Queries via Connection
			String query1 = " insert into students_info "
			        		+ " values(?,?,?,?) ";
			pstmt = con.prepareStatement(query1);
			pstmt.setInt(1,Integer.parseInt(regNoVal));
			pstmt.setString(2, fNameVal);
			pstmt.setString(3, mNameVal);
			pstmt.setString(4, lNameVal);
			pstmt.executeUpdate();
			pstmt.close();
			
			String query2 = " insert into guardian_info "
					        + " values(?,?,?,?) ";		   
			pstmt = con.prepareStatement(query2);
			pstmt.setInt(1,Integer.parseInt(regNoVal));
			pstmt.setString(2, gfNameVal);
			pstmt.setString(3, gmNameVal);
			pstmt.setString(4, glNameVal);
			pstmt.executeUpdate();
			pstmt.close();
			
			String query3 = " insert into students_otherinfo "
							+ " values(?,?,?) ";		   
			pstmt = con.prepareStatement(query3);
			pstmt.setInt(1,Integer.parseInt(regNoVal));
			pstmt.setString(2,isAdminVal);
			pstmt.setString(3,passVal);
			pstmt.executeUpdate();
			pstmt.close();
			
			//4.Process the Result return by SQL Queries
			 sbf.append("<h1><font color=\"green\"> Successfully profile Created </font></h1>");
			
			
			
		} catch (Exception e) {
			sbf.append("<h1><font color=\"red\">  Profile Not Created </font></h1>");
			e.printStackTrace();
		} finally {
			sbf.append("</body></html>");
			resp.setContentType("text/html");
			PrintWriter out=resp.getWriter();
			out.print(sbf.toString());
			//5.close JDBC object
			try {
				if(con!=null)
				{
					con.close();
				}
			} catch (SQLException e) {
			
				e.printStackTrace();
			}
			try {
				if(pstmt!=null)
				{
					pstmt.close();
				}
			} catch (SQLException e) {
				
				e.printStackTrace();
			}
		}
		
		
		
	}

}
