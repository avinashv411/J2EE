package com.jspiders.studentsapp.servlets;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mysql.jdbc.Driver;


public class StudentSearchServlet extends HttpServlet{

	@Override
	protected void doGet(HttpServletRequest req, 
			             HttpServletResponse resp) 
    throws ServletException, IOException {
		
		String regNUM = req.getParameter("regno");
		
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs= null;
		try {
				//1.Load the "Driver"
				//Driver class:com.mysql.jdbc.Driver
				Driver driverRef = new Driver();
				DriverManager.registerDriver(driverRef);
				
				//2.Get the "DB Connection" via "Driver"
				String dburl = "jdbc:mysql://localhost:3306/BECM19_db?user=j2ee&password=j2ee";
				con = DriverManager.getConnection(dburl);
				
				//3.Issue "SQL Queries" via "Connection"
				String query = " select * from students_info si,guardian_info gi "
						       +" where si.regno = gi.regno "
					           +" and si.regno = ? ";
				pstmt = con.prepareStatement(query);
				pstmt.setInt(1, Integer.parseInt(regNUM));
				
				rs = pstmt.executeQuery();
				
				//4."Process the Result" returned by "SQL Queries"
				PrintWriter out=resp.getWriter();
				resp.setContentType("text/html");
				if(rs.next()){
					int regNum = rs.getInt("regno");
					String fNM = rs.getString("firstname");
					String mNM = rs.getString("middlename");
					String lNM = rs.getString("lastname");
					String gfNM = rs.getString("gfirstname");
					String gmNM = rs.getString("gmiddlename");
					String glNM = rs.getString("glastname");
					
					String htmlRes1="<html>"
								       +"<head>"
									       +"<style>"
									       +"table, th, td {"
									       +"border: 1px solid black;"
									       +"text-align: center;"
									       +"}"
									       +"th{"
									       +"background-color:orange;"
									       +"}"
									       +"</style>"
								       +"</head>"
								       +"<body>"
									       +"<table style=\"width:100%\">"
										       +"<tr>"
											       +"<th>Regno</th>"
											       +"<th>Firstname</th>"
											       +"<th>Middlename</th>"
											       +"<th>Lastname</th>"
											       +"<th>GFirstname</th>"
											       +"<th>GMiddlename</th>"
											       +"<th>GLastname</th>" 
											   +"</tr>"
											   +"<tr>"
												   +"<td>"
												   		+regNum
												   +"</td>"
												   +"<td>"
												   		+fNM
												   +"</td>"
												   +"<td>"
												   		+mNM
												   +"</td>"
												   +"<td>"
												   		+lNM
												   +"</td>"
												   +"<td>"
												   		+gfNM
												   +"</td>"
												   +"<td>"
												   		+gmNM
												   +"</td>"
												   +"<td>"
												   		+glNM
												   +"</td>"
											   +"</tr>"
										   +"</table>"
										   +"</body>"
						              +"</html>";
					out.print(htmlRes1);
					
				}else{
					String htmlRes2 = "<html>"
									+"<body>"
					                +"<h1>"
					                +"<font color=\"red\">"
					                +"Reg.No is not Found"
					                +"</font>"
					                +"</h1>"
					                +"</body>"
					                +"</html>";
					out.print(htmlRes2);
				}
				
		}catch(SQLException ie){
			ie.printStackTrace();
		}finally{
			
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
			try {
				if(rs!=null)
				{
					rs.close();
				}
			} catch (SQLException e) {
				
				e.printStackTrace();
			}
			
		}//End of try-catch


		
		
	}//End of doGet

}//End of class
