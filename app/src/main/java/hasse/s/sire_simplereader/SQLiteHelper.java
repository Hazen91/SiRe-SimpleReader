package hasse.s.sire_simplereader;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;

public class SQLiteHelper extends SQLiteOpenHelper
{
    private static final String DATABASE_NAME = "bookDB";
    private static final String DATABASE_TABLE = "bookTable";
    private static final int DATABASE_VERSION = 1;
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.ENGLISH);

    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDB)
    {
        String createDB = "CREATE TABLE IF NOT EXISTS " + DATABASE_TABLE + "(NAME TEXT, PATH TEXT PRIMARY KEY, DATETIME TEXT)";
        sqLiteDB.execSQL(createDB);
    }


    public ArrayList<String> getRecentBooks()
    {
        ArrayList<String> recentBookNames = new ArrayList<String>();
        ArrayList<BooknameWithDatetime> recentBooks = new ArrayList<BooknameWithDatetime>();
        String selectQuery = "SELECT * FROM " + DATABASE_TABLE;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        try{
            if (cursor.moveToFirst())
            {
                do
                {
                    recentBooks.add(new BooknameWithDatetime(cursor.getString(0), cursor.getString(2)));
                }
                while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
            Collections.sort(recentBooks, new Comparator<BooknameWithDatetime>()
            {
                public int compare(BooknameWithDatetime book1, BooknameWithDatetime book2)
                {
                    return book2.getDateTime().compareTo(book1.getDateTime());
                }
            });

            for (int i = 0; i < 4; i++)
            {
                try
                {
                    Log.i("Adding: ", recentBooks.get(i).getName());
                    recentBookNames.add(recentBooks.get(i).getName());
                } catch (IndexOutOfBoundsException e)
                {
                    e.printStackTrace();
                }
            }
        }
        catch (Exception e)
        {e.printStackTrace();}
        return recentBookNames;
    }

    public void updateRow(String name)
    {
        try
        {
            SQLiteDatabase sqLiteDB = this.getWritableDatabase();

            Date date = new Date();
            String dateTime = dateFormat.format(date);

            String statementString = "UPDATE " + DATABASE_TABLE + " SET DATETIME = '" + dateTime + "' WHERE NAME = '" + name + "'";
            SQLiteStatement statement = sqLiteDB.compileStatement(statementString);
            statement.executeUpdateDelete();

            sqLiteDB.close();
        }
        catch (Exception e)
        {e.printStackTrace();}
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDB, int oldVersion, int newVersion)
    {
        sqLiteDB.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
        onCreate(sqLiteDB);
    }

    public void addBook(String name, String path)
    {
        SQLiteDatabase sqLiteDB = this.getWritableDatabase();

        Date date = new Date();
        String dateTime = dateFormat.format(date);

        String statementString = "INSERT INTO "+DATABASE_TABLE+ " (NAME, PATH, DATETIME) VALUES ('"+name+"', '"+path+"', +'"+dateTime+"')";
        SQLiteStatement statement = sqLiteDB.compileStatement(statementString);
        statement.executeInsert();

        sqLiteDB.close();
    }

    public ArrayList<String> getAllBookNames()
    {
        ArrayList<String> bookNames = new ArrayList<String>();

        String selectQuery = "SELECT * FROM " + DATABASE_TABLE;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do
            {
                bookNames.add(cursor.getString(0));
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return bookNames;
    }

    public String getPathForName(String name)
    {
        String path = "";

        String selectQuery = "SELECT * FROM " + DATABASE_TABLE + " WHERE TRIM(NAME) = '"+name.trim()+"'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do
            {
                { path = cursor.getString(1); }
            }
            while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return path;
    }

    public boolean bookExistsInDatabase(String name)
    {
        String selectQuery = "SELECT * FROM " + DATABASE_TABLE;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do
            {
                if(name.equalsIgnoreCase(cursor.getString(0)))
                {
                    cursor.close();
                    db.close();
                    return true;
                }
            }
            while (cursor.moveToNext());
        }
        return false;
    }

    public void deleteEntry(String name)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DATABASE_TABLE, "NAME='" + name + "'", null);
        db.close();
    }

    public void deleteAllEntries()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DATABASE_TABLE,null,null);
        db.close();
    }
}
