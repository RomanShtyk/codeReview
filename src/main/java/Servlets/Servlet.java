package Servlets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.*;
import java.text.*;

import com.mysql.fabric.jdbc.FabricMySQLDriver;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.sql.Date;
import java.util.Random;


public class Servlet extends HttpServlet{
    private static final String URL = "jdbc:mysql://localhost:3306/mydb";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "root";
    private static final String INS = "INSERT INTO randomdb VALUES(?, ?, ?)";
    private static final String GET_ALL = "SELECT * FROM randomdb";

    public static int answer = 0;
    public static int id = 0;
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        response.setContentType("text/html;charset=utf-8");
        if(request.getParameter("Yes")!=null) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection connection;
                Driver driver = new FabricMySQLDriver();
                DriverManager.registerDriver(driver);
                connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                Timestamp date = new Timestamp(System.currentTimeMillis());
                PreparedStatement pres = null;
                pres = connection.prepareStatement(INS);
                pres.setInt(1, id);
                id++;
                pres.setInt(2, answer);
                pres.setTimestamp(3, date);
                pres.execute();
                connection.close();
            } catch (SQLException e) {
                System.out.println(e);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            PrintWriter out = response.getWriter();
            out.write("<a href='http://localhost:8086'>Назад<a/></br>");
            try {
                Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                Statement statment = connection.createStatement();
                String query = "select * from randomdb";
                ResultSet resultSet = statment.executeQuery(query);

                while (resultSet.next()) {
                    int id = resultSet.getInt(1);
                    //System.out.print(id + " ");
                    int value = resultSet.getInt(2);
                    //System.out.print(name + " ");
                    java.util.Date date = resultSet.getTimestamp(3);
                    out.write("ID: " + id + " Random number:  " + value + " Date of create:  " + date + "<br>");
                }
            }catch (SQLException e){
                System.out.println(e);
            }
        }

    }

//    public void execute(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException {
//        DaoRand dr = new DaoRand();
//        RandomdbEntity number = new RandomdbEntity();
//        Timestamp date = new Timestamp(System.currentTimeMillis());
//        number.setDateOfCreation(date);
//        number.setRandomNum(answer);
//        dr.saveToDB(number);
//    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String upper = "";
        String lower = "";
        try {
            upper = request.getParameter("uRange");
            lower = request.getParameter("dRange");

        } catch (Exception e) {
            e.printStackTrace();
        }
        int upperInt = Integer.parseInt(upper);
        int lowerInt = Integer.parseInt(lower);
        Random random = new Random();
        answer = random.nextInt(Math.abs((upperInt - lowerInt))) + lowerInt;
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.println("<html>");
        out.println("<head><title>Result:</title></head>");
        out.println("<body>");
        out.println("<p>Your random number:"+answer+" </p>");
        out.println("<form action=\"random\" method=\"post\">" + "Put into database?<input type=\"submit\" name=\"Yes\" value=\"Yes\"/>" + "</form>");
        out.println("</body></html>");
        out.close();
    }
//            out.println("<html>");
//            out.println("<head>");
//            out.println("<title>Заголовок</title>");
//            out.println("</head>");
//            out.println("<body>");
//            out.println("Choose range:<input type=\"text\" name=\"range\"><br>");
//            out.println("");
//            out.println("</body>");
//            out.println("</html>");
//        } finally {
//            out.close();
    }