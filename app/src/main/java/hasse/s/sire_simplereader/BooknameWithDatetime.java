package hasse.s.sire_simplereader;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class BooknameWithDatetime implements Comparable<BooknameWithDatetime>
{
    String name = "";
    String datetime = "";
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.ENGLISH);

    public String getName()
    {
        return name;
    }

    public String getDatetimeString()
    {
        return datetime;
    }

    public BooknameWithDatetime(String name, String datetime)
    {
        this.name = name;
        this.datetime=datetime;
    }

    public Date getDateTime() {
        Date date = null;
        try
        {
            date = dateFormat.parse(datetime);
        } catch (ParseException e)
        {
            e.printStackTrace();
        }
        return date;
    }

    @Override
    public int compareTo(BooknameWithDatetime book)
    {
        return book.getDateTime().compareTo(getDateTime());
    }
}
