package hasse.s.sire_simplereader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

class CSVParser
{

    private String[] separateLine(String line)
    {
        String[] separatedLine;

        String otherThanQuote = " [^\"] ";
        String quotedString = String.format(" \" %s* \" ", otherThanQuote);
        String regex = String.format("(?x) "+ // enable comments, ignore white spaces
                        ",                         "+ // match a comma
                        "(?=                       "+ // start positive look ahead
                        "  (                       "+ //   start group 1
                        "    %s*                   "+ //     match 'otherThanQuote' zero or more times
                        "    %s                    "+ //     match 'quotedString'
                        "  )*                      "+ //   end group 1 and repeat it zero or more times
                        "  %s*                     "+ //   match 'otherThanQuote'
                        "  $                       "+ // match the end of the string
                        ")                         ", // stop positive look ahead
                otherThanQuote, quotedString, otherThanQuote);

        separatedLine = line.split(regex, -1);

        return  separatedLine;
    }

    public String[] convertCsvFileToStringArray(File file)
    {
        String[] result;
        ArrayList<String> resultList = new ArrayList<String>();

        int index = 0;

        try
        {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;

            while ((line = reader.readLine()) != null)
            {
                String[] RowData = separateLine(line);
                Collections.addAll(resultList, RowData);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        result = new String[resultList.size()];


        for (String value : resultList)
        {
            result[index] = value;
            index++;
        }

        return result;
    }

    public CSVParser()
    {

    }
}
