package org.example;

public class NewsItem
{
    private String title;
    private String author;
    private String releaseDate;

    private String content;

    public NewsItem()
    {
        this.title = title;
        this.author = author;
        this.releaseDate = releaseDate;
        this.content = content;
    }


    public String getTitle()
    {
        return title;
    }

    public String getAuthor()
    {
        return author;
    }

    public String getReleaseDate()
    {
        return releaseDate;
    }

    public String getContent()
    {
        return content;
    }


    // setter
    public void setTitle(String title)
    {
        this.title = title;
    }

    public void setAuthor(String author)
    {
        this.author = author;
    }

    public void setReleaseDate(String releaseDate)
    {
        this.releaseDate = releaseDate;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

}
