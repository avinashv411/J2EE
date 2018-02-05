package com.jspiders.studentsapp.servlet.pratices;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AllStudentsViewServlet extends HttpServlet{
	
	@Override
	protected void doGet(HttpServletRequest req, 
						 HttpServletResponse resp)
	throws ServletException, IOException {
		
		String regnoVal = req.getParameter("regno");
		int fromRegno = 0;
		int toRegno = 0;
		int rows = 5;
		if(regnoVal==null)
		{
			fromRegno = 1;
			toRegno = rows;
		}
		else
		{
			fromRegno = Integer.parseInt(regnoVal);
			toRegno = fromRegno+rows-1;
		}
       
		Connection con = null;
		PreparedStatement pstmt = null;  
		Statement stmt = null;
		ResultSet rs = null;
		ResultSet rs1 = null;
		StringBuffer sbf = new StringBuffer("");
        int count = 0, pages=0;  
        
		try {
			//1.Load the Drivers
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			
			//2.Get Connection via Drivers
			String dburl = "jdbc:mysql://localhost:3306/BECM19_DB?user=j2ee&password=j2ee";
			con = DriverManager.getConnection(dburl);
			
			//3.Issue the SQL Query via connection
			String query = " select * from students_info si,guardian_info gi "
						   +" where si.regno = gi.regno and "
			               +" si.regno between ? and ? ";
			pstmt = con.prepareStatement(query);
			pstmt.setInt(1,fromRegno);
			pstmt.setInt(2, toRegno);
			rs = pstmt.executeQuery();
			
			String sqlQuery = " select count(*) from students_info ";
			stmt = con.createStatement();
			rs1 = stmt.executeQuery(sqlQuery);
			rs1.next();
			count = rs1.getInt("count(*)");
			//rs1.last();
			//last = rs1.getRow();
			rs1.close();
			System.out.println("last = "+count);
		
			if(count%rows==0) {
				pages = count/rows;
			} else {
				pages = count/rows + 1;
			}
			
			
			//4.Process the results returned by "SQL Queries"
			sbf.append("<html>");
			sbf.append("<style>");
			sbf.append("#link{ background-color: orange;");
		    sbf.append("color: white;");
		    sbf.append("padding: 2px 15px;");
		    sbf.append("text-align: center;");
	        sbf.append("text-decoration: none;");
		    sbf.append("display: inline-block;}");
		    sbf.append("table, tr,th,td {border: 1px solid black;text-align: center;}");
		    sbf.append("</style>");
			sbf.append("<body>");
			sbf.append("<table width=100%>");
			sbf.append("<tr bgcolor=\"orange\"> ");
			sbf.append("<th>Reg. No. </th>");
			sbf.append("<th>First Name</th>");
			sbf.append("<th>Middle Name</th>");
			sbf.append("<th>Last Name</th>");
			sbf.append("<th>G First Name</th>");
			sbf.append("<th>G Middle Name</th>");
			sbf.append("<th>G Last Name</th>");
			sbf.append("</tr>");
			
			while(rs.next()) {
				sbf.append("<tr>");
				sbf.append("<td>" + "<a href=\"http://localhost:8080/studentsApp/studentSearch?regno=" + rs.getInt("si.regno")+"\">" + rs.getInt("si.regno") +"</a>" + "</td>");
				sbf.append("<td>" + rs.getString("si.firstname") +"</td>");
				sbf.append("<td>" + rs.getString("si.middlename") +"</td>");
				sbf.append("<td>" + rs.getString("si.lastname") +"</td>");
				sbf.append("<td>" + rs.getString("gi.gfirstname")  +"</td> ");
				sbf.append("<td>" + rs.getString("gi.gmiddlename")+"</td>");
				sbf.append("<td>" + rs.getString("gi.glastname") +"</td>  ");
				sbf.append("</tr>");
			}
			
			sbf.append("</table>");
			sbf.append("</body>");
			sbf.append("</html>");
			
		
			
			
			
		} catch (Exception e) {
			
			e.printStackTrace();
		} finally {
			
			
			sbf.append("<BR><BR>");
			
			String nextURL = "http://localhost:8080/studentsApp/allStudentsViews?regno="+(toRegno+1);
	
			String backURL = "http://localhost:8080/studentsApp/allStudentsViews?regno="+(fromRegno-5);
			
			int page = 1;
			int j = 0;
			
			while(page<=pages)
			{
				sbf.append("<td>&nbsp;&nbsp;&nbsp;&nbsp;</td>");
				String url = "http://localhost:8080/studentsApp/allStudentsViews?regno="+(1+j*rows);
				sbf.append("<a href=\""+url+"\">"+(j+1)+"</a>");
				sbf.append("<td>&nbsp;&nbsp;&nbsp;&nbsp;</td>");
				
				j++;
				page++;
			}
			
			if(fromRegno-5>=1) {
				sbf.append("<a id=\"link\" href=\""+backURL+"\" style=\"float: left;\">"+"<----"
						+ "Previous</a>");
				sbf.append("<td>&nbsp;&nbsp;&nbsp;&nbsp;</td>");
				sbf.append("<td>&nbsp;&nbsp;&nbsp;&nbsp;</td>");
			}
			
			if(toRegno+1<=count)
			     sbf.append("<a id=\"link\" href=\""+nextURL+"\" style=\"float: right;\">"+"Next----></a>");
			
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

}//End of Class
