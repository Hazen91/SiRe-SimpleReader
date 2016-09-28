package hasse.s.sire_simplereader;


import android.content.Context;
import android.content.Intent;
import android.widget.ListView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class FileOpener
{



    public static void openFile(File file, Context context)
    {
        SQLiteHelper sqLiteHelper = new SQLiteHelper(context);
        String pathAndName = file.getAbsolutePath();
        String fileName = file.getName();
        String fileExtension = fileName.substring(fileName.lastIndexOf('.') + 1);

        if(isValidFile(file) && !sqLiteHelper.bookExistsInDatabase(fileName))
        {
            sqLiteHelper.addBook(fileName, pathAndName);
        }

        startActivity(pathAndName,fileExtension,fileName,context);

    }

    public static void openFile(Context context, ListView listView, int position)
    {
        SQLiteHelper sqLiteHelper = new SQLiteHelper(context);
        String pathAndName = sqLiteHelper.getPathForName(listView.getItemAtPosition(position).toString());
        String fileExtension = pathAndName.substring(pathAndName.lastIndexOf('.') + 1);
        String fileName = pathAndName.substring(pathAndName.lastIndexOf('/') + 1);

        if(!sqLiteHelper.bookExistsInDatabase(fileName))
        {
            sqLiteHelper.addBook(fileName, pathAndName);
        }

        startActivity(pathAndName,fileExtension,fileName,context);
    }

    private static void startActivity(String pathAndName, String fileExtension, String fileName, Context context)
    {
        if (fileExtension.equalsIgnoreCase("pdf"))
        {

            Intent i = new Intent(context, PDFActivity.class);
            i.putExtra("FileLocation", pathAndName);
            context.startActivity(i);
        }
        else if (fileExtension.equalsIgnoreCase("epub"))
        {

            Intent i = new Intent(context, EpubActivity.class);
            i.putExtra("FileLocation", pathAndName);
            context.startActivity(i);
        }
        else if (fileExtension.equalsIgnoreCase("txt") || fileExtension.equalsIgnoreCase("xml"))
        {

            Intent i = new Intent(context, TxtActivity.class);
            i.putExtra("FileLocation", pathAndName);
            context.startActivity(i);
        }
        else if (fileExtension.equalsIgnoreCase("csv"))
        {

            Intent i = new Intent(context, CSVActivity.class);
            i.putExtra("FileLocation", pathAndName);
            context.startActivity(i);
        }
        else if(fileExtension.equalsIgnoreCase("html"))
        {
            Intent i = new Intent(context, HtmlActivity.class);
            i.putExtra("FileLocation", pathAndName);
            context.startActivity(i);
        }
        else if(fileExtension.equalsIgnoreCase("docx"))
        {
            File finalPath  = new File(context.getCacheDir() + File.separator + fileName);
            if(!finalPath.exists())
            {   finalPath.mkdir();   }
            try
            {
                Unzipper.unzip(pathAndName, finalPath );
                XmlParser xmlParser = new XmlParser();
                File f  = new File(context.getCacheDir() + File.separator + fileName + File.separator + "word/"+"document.xml");
                Intent i = new Intent(context, DocxActivity.class);
                i.putExtra("content", xmlParser.parse(new FileInputStream(f)));
                i.putExtra("Location", context.getCacheDir() + File.separator + fileName);
                context.startActivity(i);

            } catch (IOException | XmlPullParserException e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            Toast.makeText(context, "File format not supported. Sorry :-/", Toast.LENGTH_LONG).show();
        }
    }

    private static boolean isValidFile(File file)
    {
        String[] validExtensions = {"pdf", "epub", "txt", "csv", "docx", "html", "xml"};
        String fileName = file.getName();
        String fileExtension = fileName.substring(fileName.lastIndexOf('.') + 1);
        for (String extension:validExtensions)
        {
            if (fileExtension.equalsIgnoreCase(extension))
                return true;
        }
        return false;
    }


}
