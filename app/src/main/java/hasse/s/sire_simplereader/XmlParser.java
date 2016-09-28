package hasse.s.sire_simplereader;


import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;

class XmlParser
{
    public String parse(InputStream inputStream) throws XmlPullParserException, IOException {
        XmlPullParserFactory factory;
        XmlPullParser parser;

        factory = XmlPullParserFactory.newInstance();
        factory.setNamespaceAware(true);
        parser = factory.newPullParser();
        parser.setInput(inputStream, null);

        StringBuilder stringBuilder = new StringBuilder();

        int eventType = parser.getEventType();
        while (eventType != XmlPullParser.END_DOCUMENT)
        {
            String tagname = parser.getName();
            String prefix = parser.getPrefix();
            switch (eventType)
            {
                case XmlPullParser.START_TAG:
                    if (tagname.equalsIgnoreCase("t") && prefix.equalsIgnoreCase("w"))
                    {
                        parser.next();
                        stringBuilder.append(parser.getText());
                        String t = parser.getText();
                        Log.i("TEXT: ", t);
                    }
                    else if (tagname.equalsIgnoreCase("r") && prefix.equalsIgnoreCase("w"))
                    {
                        stringBuilder.append("\n");
                    }
                    break;

                default:
                    break;
            }
            eventType = parser.next();
        }
        return stringBuilder.toString();
    }
}
