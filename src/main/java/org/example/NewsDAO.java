package org.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/*所有关于新闻的操作*/
public class NewsDAO
{
    //用户获取主页新闻列表
    //逻辑，创建 另一个class 代表新闻信息，下面调用
    //整个逻辑：从数据库中获取新闻信息，创建一个NewsItem对象，将新闻信息存储到NewsItem对象中，将NewsItem对象存储到List中，返回List
    public static List<NewsItem> getHomepageNews()
    {

        //创建一个List，用于存储NewsItem
        List<NewsItem> newsItems = new ArrayList<>();

        //SQL语句, ？是占位符, 防止SQL注入, ？的值由后面的参数提供
        String sql = "SELECT title, author FROM contents ORDER BY release_date DESC LIMIT 5";

        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql))
        {
            ResultSet rs = pstmt.executeQuery();

            while (rs.next())
            {
                String title = rs.getString("title");
                String author = rs.getString("author");
                String releaseDate = rs.getString("release_date");

                NewsItem newsItem = new NewsItem(); //想调用NewsItem，必须先创建一个NewsItem对象
                newsItem.setTitle(title);
                newsItem.setAuthor(author);
                newsItem.setReleaseDate(releaseDate);

                //开始创的空，放进去上面信息
                newsItems.add(newsItem);
            }
        }

        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return newsItems;
    }

    //用户按分区浏览主页新闻列表
    //主逻辑，创空，用 sql 句子找同类，下一行就是真，就放新信息

    public static List<NewsItem> getNewsByCategory(String categoryName)
    {

        // 创建一个List，用于存储NewsItem
        List<NewsItem> newsItems = new ArrayList<>();

        // SQL语句, ？是占位符, 防止SQL注入, ？的值由后面的参数提供
        String sql = "SELECT title, author, release_date FROM contents WHERE category = ? ORDER BY release_date DESC LIMIT 5";

        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql))
        {
            pstmt.setString(1, categoryName); // 设置分类名称参数

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String title = rs.getString("title");
                String author = rs.getString("author");
                String releaseDate = rs.getString("release_date");

                NewsItem newsItem = new NewsItem(); //想调用NewsItem，必须先创建一个NewsItem对象
                newsItem.setTitle(title);
                newsItem.setAuthor(author);
                newsItem.setReleaseDate(releaseDate);

                //开始创的空，放进去上面信息
                newsItems.add(newsItem);
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return newsItems;
    }

    // 用户按标题和内容搜索新闻列表
    public static List<NewsItem> searchNewsByTitleAndContent(String search)
    {

            // 创建一个List，用于存储NewsItem
            List<NewsItem> newsItems = new ArrayList<>();

            // SQL语句, ？是占位符, 防止SQL注入, ？的值由后面的参数提供
            String sql = "SELECT title, news_body FROM contents WHERE title LIKE ? OR content LIKE ? ORDER BY release_date DESC LIMIT 5";

            try (Connection connection = databaseConnection.getConnection();
                PreparedStatement pstmt = connection.prepareStatement(sql))
            {
                // 设置搜索文本,%表示任意字符
                pstmt.setString(1, "%" + search + "%"); // 设置标题参数
                pstmt.setString(2, "%" + search + "%"); // 设置内容参数

                ResultSet rs = pstmt.executeQuery();

                while (rs.next())
                {
                    String title = rs.getString("title");
                    String newsBody = rs.getString("news_body");

                    NewsItem newsItem = new NewsItem(); //想调用NewsItem，必须先创建一个NewsItem对象
                    newsItem.setTitle(title);
                    newsItem.setContent(newsBody);

                    //开始创的空，放进去上面信息
                    newsItems.add(newsItem);

                }
            }

            catch (SQLException e)
            {
                e.printStackTrace();
            }

            return newsItems;
    }

    //用户获取新闻，并加载新闻下的评论
    //先获取新闻
    public static NewsItem getNews(int newsId)
    {
        NewsItem newsItem = new NewsItem();
        String sql = "SELECT * FROM contents WHERE id = ?";

        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql))
        {
            pstmt.setInt(1, newsId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next())
            {
                String title = rs.getString("title");
                String author = rs.getString("author");
                String releaseDate = rs.getString("release_date");
                String newsBody = rs.getString("news_body");

                newsItem.setTitle(title);
                newsItem.setAuthor(author);
                newsItem.setReleaseDate(releaseDate);
                newsItem.setContent(newsBody);
            }
        }

        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return newsItem;
    }

    //再获取评论
    public static List<Comment> getNewsComment(int newsId)
    {
        List<Comment> comments = new ArrayList<>();
        String sql = "SELECT * FROM comments WHERE news_id = ?";

        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql))
        {
            pstmt.setInt(1, newsId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next())
            {
                String user = rs.getString("user_id");
                String commentText = rs.getString("content");
                String commentDate = rs.getString("comment_date");

                Comment comment = new Comment();
                comment.setUser(user);
                comment.setComment(commentText);
                comment.setCommenDate(commentDate);

                comments.add(comment);

            }
        }

        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return comments;

    }

    //用户给新闻收藏
    //逻辑：将新闻id和用户id存储到相对应的数据库表中（favorites）
    public static void favoriteNews(int userId, int newsId)
    {
        String sql = "INSERT INTO NewsFavorite (user_id, news_id) VALUES (?, ?)";

        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql))
        {
            pstmt.setInt(1, userId);
            pstmt.setInt(2, newsId);

            int affect = pstmt.executeUpdate();

            //判断是否收藏成功
            if (affect > 0)
            {
                System.out.println("favorite successfully");
            }

            else
            {
                System.out.println("favorite failed");
            }
        }

            catch (SQLException e)
            {
                e.printStackTrace();
            }
    }

    //用户分享新闻
    //逻辑：将新闻id和用户id存储到相对应的数据库表中（NewsShares）
    public static void shareNews(int userId, int newsId)
    {
        String sql = "INSERT INTO NewsShares (user_id, news_id) VALUES (?, ?)";

        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql))
        {
            pstmt.setInt(1, userId);
            pstmt.setInt(2, newsId);

            int affect = pstmt.executeUpdate();

            //判断是否分享成功
            if (affect > 0)
            {
                System.out.println("share successfully");
            }

            else
            {
                System.out.println("share failed");
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    //用户评论新闻
    //逻辑：找到新闻id，用户id，评论内容，存储到数据库中（comments）
    public static void addComment(int userId, int newsId, String comment)
    {
        String sql = "INSERT INTO comments (user_id, news_id, content) VALUES (?, ?, ?)";

        try(Connection connection = databaseConnection.getConnection(); //先连接数据库
            PreparedStatement pstmt = connection.prepareStatement(sql))////prepareStatement()方法会将SQL语句预编译到数据库中，一次编译，多次执行
        {
            //设置参数
            pstmt.setInt(1, userId);
            pstmt.setInt(2, newsId);
            pstmt.setString(3, comment);

            int affect = pstmt.executeUpdate();//executeUpdate返回受影响的行数

            //判断是否评论成功
            if (affect > 0)
            {
                System.out.println("comment successfully");
            }

            else
            {
                System.out.println("comment failed");
            }


        }

        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    //用户对评论进行点赞,踩
    //逻辑：找到评论id，用户id，存储到数据库中（upvote）
    public static void upvoteComment(int userId, int commentId)
    {
        String sql = "INSERT INTO upvote (user_id, comment_id) VALUES (?, ?)";

        try (Connection connection = databaseConnection.getConnection(); //先连接数据库
             PreparedStatement pstmt = connection.prepareStatement(sql))////prepareStatement()方法会将SQL语句预编译到数据库中，一次编译，多次执行
        {
            //设置参数
            pstmt.setInt(1, userId);
            pstmt.setInt(2, commentId);

            int affect = pstmt.executeUpdate();//executeUpdate返回受影响的行数

            //判断是否点赞成功
            if (affect > 0)
            {
                System.out.println("upvote successfully");
            }

            else
            {
                System.out.println("upvote failed");
            }


        }

        catch (Exception e)
        {
            e.printStackTrace();
        }



    }

    //用户对评论进行点踩
    //逻辑：找到评论id，用户id，存储到数据库中（downvotes）
    public static void downvoteComment(int userId, int commentId)
    {
        String sql = "INSERT INTO downvotes (user_id, comment_id) VALUES (?, ?)";

        try (Connection connection = databaseConnection.getConnection(); //先连接数据库
             PreparedStatement pstmt = connection.prepareStatement(sql))////prepareStatement()方法会将SQL语句预编译到数据库中，一次编译，多次执行
        {
            //设置参数
            pstmt.setInt(1, userId);
            pstmt.setInt(2, commentId);

            int affect = pstmt.executeUpdate();//executeUpdate返回受影响的行数

            //判断是否点赞成功
            if (affect > 0)
            {
                System.out.println("downvote successfully");
            }

            else
            {
                System.out.println("downvote failed");
            }
        }

        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    //用户删除自己的评论
    //逻辑：找到评论id，用户id，删除数据库中（comments）
    public static void deleteComment(int userId, int commentId)
    {
        String sql = "DELETE FROM comments WHERE user_id = ? AND id = ?";

        try (Connection connection = databaseConnection.getConnection(); //先连接数据库
             PreparedStatement pstmt = connection.prepareStatement(sql))////prepareStatement()方法会将SQL语句预编译到数据库中，一次编译，多次执行
        {
            //设置参数
            pstmt.setInt(1, userId);
            pstmt.setInt(2, commentId);

            int affect = pstmt.executeUpdate();//executeUpdate返回受影响的行数

            //判断是否删除成功
            if (affect > 0)
            {
                System.out.println("delete successfully");
            }

            else
            {
                System.out.println("delete failed");
            }
        }

        catch (Exception e)
        {
            e.printStackTrace();
        }
    }



}
