package org.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.ArrayList;


public class UserHistory
{
    //用户在个人主页中观看自己的评论历史
    //逻辑：找到用户id，找到评论内容，存list 里，返回list
    public List<Comment> getUserCommentHistory(int userId)
    {
        //sql查询，从 comment 里获取指定 id 所有评论
        String sql = "select * from comment where user_id = ?";

        List<Comment> commentList = new ArrayList<>();
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql))
        {
            //设置参数
            pstmt.setInt(1, userId);

            //用 rs 存储返回的结果
            ResultSet rs = pstmt.executeQuery();

            while (rs.next())
            {
                Comment commentTemp = new Comment();//创建一个comment对象，这不是 list
                commentTemp.setComment(rs.getString("content"));//set不用 add，是因为不是 list
                commentTemp.setUser(rs.getString("user_id"));
                commentTemp.setCommenDate(rs.getString("comment_date"));

                //将comment存入list
                commentList.add(commentTemp);

            }


        }

        catch (Exception e)
        {
            e.printStackTrace();
        }

        return commentList;
    }

    //用户在个人主页中观看自己的浏览历史
    //逻辑：找到用户id，找到浏览记录，存list 里，返回list
    public List<NewsItem> getUserLookHistory(int userId)
    {
        //sql查询，从 history 里获取指定 id 所有浏览记录
        String sql = "select * from history where user_id = ?";

        List<NewsItem> browsingHistory = new ArrayList<>();
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql))
        {

            //设置参数
            pstmt.setInt(1, userId);

            //用 rs 存储返回的结果
            ResultSet rs = pstmt.executeQuery();

            while (rs.next())
            {
                NewsItem newsTemp = new NewsItem();//创建一个news对象，这不是 list
                newsTemp.setTitle(rs.getString("title"));//set不用 add，是因为不是 list
                newsTemp.setAuthor(rs.getString("author"));

                //将news存入list
                browsingHistory.add(newsTemp);

            }

        }

            catch (Exception e)
            {
                e.printStackTrace();
            }

        return browsingHistory;
    }


    //(模拟)投放新闻
    //逻辑：接受新闻，然后往数据库里插入，就是往 content 里加新闻的东西，insert
    //大体逻辑：设置 sql，try 连接，设置参数，执行 sql，catch
    public static void postNews(String title, String content, String author)
    {
        //sql语句，？是占位符，防止SQL注入，？的值由后面的参数提供
        String sql = "insert into content(title, content, author) values(?, ?, ?)";

        try(Connection connection = databaseConnection.getConnection();PreparedStatement pstmt = connection.prepareStatement(sql))
        {
            //设置参数
            pstmt.setString(1, title);
            pstmt.setString(2, content);
            pstmt.setString(3, author);

            //执行sql语句
            int affect = pstmt.executeUpdate();
            if(affect>0)
            {
                System.out.println("Successfully posted news!");
            }
            else
            {
                System.out.println("Failed to post news.");
            }

        }

        catch(Exception e)
        {
            e.printStackTrace();
        }

    }

    //按照内容类型获取广告
    //逻辑：就是从 content 里找到指定类型的内容，返回 list
    //大体逻辑：设置 sql，try 连接，设置参数，执行 sql，catch
    public static String getAd(String type)
    {
        //sql
        String sql = "select * from content where text_content = ? limit 1";
        //返回的广告
        String ad = null;

        try(Connection connection = databaseConnection.getConnection(); PreparedStatement pstmt = connection.prepareStatement(sql))
        {
            //设置参数
            pstmt.setString(1, type);

            //执行sql语句
            ResultSet rs = pstmt.executeQuery();

            if(rs.next())
            {
                ad = rs.getString("text_content");
            }

        }

        catch(Exception e)
        {
            e.printStackTrace();
        }

        return ad;
    }

    //投放广告
    //逻辑：接受广告，然后就是往ads列表的 content 加东西，insert
    //大体逻辑：设置 sql，try 连接，设置参数，执行 sql，catch
    public static String postAds(String ads)
    {
        //sql
        String sql = "insert into ads(text_content) values(?)";
        String ad = null;

        try(Connection connection = databaseConnection.getConnection(); PreparedStatement pstmt = connection.prepareStatement(sql))
        {
            //设置参数
            pstmt.setString(1, ads);

            //执行sql语句
            ResultSet rs = pstmt.executeQuery();

            if(rs.next())
            {
                //返回的广告
                ad = rs.getString("text_content");
            }

            int affect = pstmt.executeUpdate();
            if(affect>0)
            {
                System.out.println("Successfully posted ads!");
            }
            else
            {
                System.out.println("Failed to post ads.");
            }

        }

        catch(Exception e)
        {
            e.printStackTrace();
        }

        return ad;

    }
}
