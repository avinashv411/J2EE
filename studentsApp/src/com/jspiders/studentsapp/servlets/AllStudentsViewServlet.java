package com.jspiders.studentsapp.servlets;

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

public class AllStudentsViewServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, 
			             HttpServletResponse resp) 
    throws ServletException, IOException {

		String regnoVal = req.getParameter("regno");

		resp.setContentType("text/html");
		PrintWriter out = resp.getWriter();

		int fromRegno = 0;
		int toRegno = 0;
		int rows = 5;
		
		if (regnoVal == null) {
			fromRegno = 1;
			toRegno = rows;
		} else {
			fromRegno = Integer.parseInt(regnoVal);
			toRegno = fromRegno + rows - 1;
		}

		Connection con = null;
		PreparedStatement pstmt = null;
		Statement stmt = null;
		ResultSet rs = null;
		ResultSet rs1 = null;
		int count = 0, pages = 0;

		try {
			// 1.Load the Drivers
			Class.forName("com.mysql.jdbc.Driver").newInstance();

			// 2.Get Connection via Drivers
			String dburl = "jdbc:mysql://localhost:3306/BECM19_DB?user=j2ee&password=j2ee";
			con = DriverManager.getConnection(dburl);

			// 3.Issue the SQL Query via connection
			String query = " select * from students_info si,guardian_info gi " + " where si.regno = gi.regno and "
					+ " si.regno between ? and ? ";
			pstmt = con.prepareStatement(query);
			pstmt.setInt(1, fromRegno);
			pstmt.setInt(2, toRegno);
			rs = pstmt.executeQuery();

			String sqlQuery = " select count(*) from students_info ";// To find no.of records
			stmt = con.createStatement();
			rs1 = stmt.executeQuery(sqlQuery);
			rs1.next();
			count = rs1.getInt("count(*)");
			// rs1.last();
			// last = rs1.getRow();
			rs1.close();
			// System.out.println("last = "+count);

			//find no.of pages
			if (count % rows == 0) {
				pages = count / rows;
			} else {
				pages = count / rows + 1;
			}

			// 4.Process the results returned by "SQL Queries"
			out.print("<html>");
			out.print("<style>");
			out.print("#link{ background-color: orange;");
			out.print("color: white;");
			out.print("padding: 2px 15px;");
			out.print("text-align: center;");
			out.print("display: inline-block;}");
			out.print("table, tr,th,td {border: 1px solid black;text-align: center;}");
			out.print("</style>");
			out.print("<body>");
			out.print("<table width=100%>");
			out.print("<tr bgcolor=\"orange\"> ");
			out.print("<th>Reg. No. </th>");
			out.print("<th>First Name</th>");
			out.print("<th>Middle Name</th>");
			out.print("<th>Last Name</th>");
			out.print("<th>G First Name</th>");
			out.print("<th>G Middle Name</th>");
			out.print("<th>G Last Name</th>");
			out.print("</tr>");

			while (rs.next()) {
				out.print("<tr>");
				out.print("<td>" + "<a href=\"http://localhost:8080/studentsApp/studentSearch?regno="
						+ rs.getInt("si.regno") + "\">" + rs.getInt("si.regno") + "</a>" + "</td>");
				out.print("<td>" + rs.getString("si.firstname") + "</td>");
				out.print("<td>" + rs.getString("si.middlename") + "</td>");
				out.print("<td>" + rs.getString("si.lastname") + "</td>");
				out.print("<td>" + rs.getString("gi.gfirstname") + "</td> ");
				out.print("<td>" + rs.getString("gi.gmiddlename") + "</td>");
				out.print("<td>" + rs.getString("gi.glastname") + "</td>  ");
				out.print("</tr>");
			}

			out.print("</table>");
			out.print("</body>");
			out.print("</html>");
			
			
			out.print("<BR><BR>");

			String nextURL = "http://localhost:8080/studentsApp/allStudentsView?regno=" + (toRegno + 1);

			String backURL = "http://localhost:8080/studentsApp/allStudentsView?regno=" + (fromRegno - 5);

			int page = 1;
			int pageNo = 0;
			// To display Hyper-links for Pages
			while (page <= pages) {
				
				out.print("<td>&nbsp;&nbsp;&nbsp;&nbsp;</td>");
				String url = "http://localhost:8080/studentsApp/allStudentsView?regno=" + (1 + pageNo * rows);
			
				out.print("<a href=\"" + url + "\">" + (pageNo + 1) + "</a>");
				out.print("<td>&nbsp;&nbsp;&nbsp;&nbsp;</td>");

				pageNo++;
				page++;
			}

			if (fromRegno - 5 >= 1) {
				out.print("<a id=\"link\" href=\"" + backURL + "\" style=\"float: left;\">" + "<----Previous</a>");
				out.print("<td>&nbsp;&nbsp;&nbsp;&nbsp;</td>");
				out.print("<td>&nbsp;&nbsp;&nbsp;&nbsp;</td>");
			}

			if (toRegno + 1 <= count)
				out.print("<a id=\"link\" href=\"" + nextURL + "\" style=\"float: right;\">" + "Next----></a>");

		} catch (Exception e) {

			e.printStackTrace();
		} finally {

			// 5.close JDBC object
			try {
				if (con != null) {
					con.close();
				}
			} catch (SQLException e) {

				e.printStackTrace();
			}
			try {
				if (pstmt != null) {
					pstmt.close();
				}
			} catch (SQLException e) {

				e.printStackTrace();
			}
			try {
				if (rs != null) {
					rs.close();
				}
			} catch (SQLException e) {

				e.printStackTrace();
			}
		} // End of try-catch
	}// End of doGet

}// End of Class
