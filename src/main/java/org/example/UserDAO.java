package org.example;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/*所有关于用户的操作*/
public class UserDAO
{
    //通过邮箱创建账户
    public static void createAccountByEmail(String email, String nickname, String password)
    {
        //SQL语句, ？是占位符, 防止SQL注入, ？的值由后面的参数提供
        String sql = "INSERT INTO Users (email, nickname, password) VALUES (?, ?, ?)";

        try
                (Connection connection = databaseConnection.getConnection();
                 PreparedStatement pstmt = connection.prepareStatement(sql)
                )//prepareStatement()方法会将SQL语句预编译到数据库中，一次编译，多次执行
        {
            //设置参数
            pstmt.setString(1, email);
            pstmt.setString(2, nickname);
            pstmt.setString(3, password);

            //执行SQL语句
            //executeUpdate()方法用于执行 INSERT、UPDATE 或 DELETE 语句以及 SQL DDL（数据定义语言）语句
            //executeUpdate()方法返回受影响的行数&&&
            //int 存储受影响的行数
            int affectedRows = pstmt.executeUpdate();

            //检查是否成功插入
            if(affectedRows>0)
            {
                System.out.println("Successfully created account!");
            }
            else
            {
                System.out.println("Failed to create account.");
            }

        }

        //cat
        catch (SQLException e)//SQLException是一个异常类，用于处理数据库相关的异常
        {

            e.printStackTrace();//打印异常信息
        }

    }

    //用户编辑自己的昵称等账号信息
    public static void updateUserInfor(String email, String nickname, String password)
    {
        //SQL语句, ？是占位符, 防止SQL注入, ？的值由后面的参数提供
        String sql = "UPDATE Users SET nickname = ?, password = ? WHERE email = ?";

        try
                (   Connection connection = databaseConnection.getConnection();
                    PreparedStatement pstmt = connection.prepareStatement(sql)
                )//prepareStatement()方法会将SQL语句预编译到数据库中，一次编译，多次执行
        {
            //设置参数
            pstmt.setString(1, nickname);
            pstmt.setString(2, password);
            pstmt.setString(3, email);

            //执行SQL语句
            //executeUpdate()方法用于执行 INSERT、UPDATE 或 DELETE 语句以及 SQL DDL（数据定义语言）语句
            //executeUpdate()方法返回受影响的行
            int affectedRows = pstmt.executeUpdate();

            //检查是否成功更改
            if(affectedRows>0)
            {
                System.out.println("Successfully updated account!");
            }
            else
            {
                System.out.println("Failed to update account.");
            }


        }

        //catch
        catch (SQLException e)//SQLException是一个异常类，用于处理数据库相关的异常
        {

            e.printStackTrace();//打印异常信息
        }
    }


    //用户登录
    //逻辑：验证账号（登录邮箱）密码是否与数据库匹配
    public static boolean login(String email, String password)
    {
        //SQL语句, ？是占位符, 防止SQL注入, ？的值由后面的参数提供
        String sql = "SELECT * FROM Users WHERE email = ? AND password = ?";

        try
                (   Connection connection = databaseConnection.getConnection();
                    PreparedStatement pstmt = connection.prepareStatement(sql)
                )//prepareStatement()方法会将SQL语句预编译到数据库中，一次编译，多次执行
        {
            //设置参数
            pstmt.setString(1, email);
            pstmt.setString(2, password);

            //执行SQL语句
            //executeUpdate()方法用于执行 INSERT、UPDATE 或 DELETE 语句以及 SQL DDL（数据定义语言）语句
            //executeUpdate()方法返回受影响的行
            int affectedRows = pstmt.executeUpdate();

            //pstmt.executeQuery()返回一个ResultSet对象，该对象包含了所有符合条件的行
            //用 rs 存储返回的结果
            ResultSet rs = pstmt.executeQuery();

            if(rs.next())//如果rs有下一行，说明账号密码匹配
            {
                return true;//如果return  T，那么代码执行到这里就结束了，后面的代码不会执行
            }



        }

        //catch
        catch (SQLException e)//SQLException是一个异常类，用于处理数据库相关的异常
        {

            e.printStackTrace();//打印异常信息
        }

        return false;
    }




}
