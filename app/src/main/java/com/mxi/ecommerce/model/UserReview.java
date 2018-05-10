package com.mxi.ecommerce.model;

/**
 * Created by aksahy on 25/1/18.
 */

public class UserReview
{
    private String author;

    private String text;

    private String date_added;

    private String rating;

    public String getAuthor ()
    {
        return author;
    }

    public void setAuthor (String author)
    {
        this.author = author;
    }

    public String getText ()
    {
        return text;
    }

    public void setText (String text)
    {
        this.text = text;
    }

    public String getDate_added ()
    {
        return date_added;
    }

    public void setDate_added (String date_added)
    {
        this.date_added = date_added;
    }

    public String getRating ()
    {
        return rating;
    }

    public void setRating (String rating)
    {
        this.rating = rating;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [author = "+author+", text = "+text+", date_added = "+date_added+", rating = "+rating+"]";
    }
}
