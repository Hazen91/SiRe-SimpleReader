package hasse.s.sire_simplereader;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;

import com.github.mertakdut.BookSection;
import com.github.mertakdut.CssStatus;
import com.github.mertakdut.Reader;
import com.github.mertakdut.exception.OutOfPagesException;
import com.github.mertakdut.exception.ReadingException;

import junit.framework.Assert;

import java.util.ArrayList;
import java.util.List;


public class EpubActivity extends AppCompatActivity {

    private WebView webView;
    private Button previous;
    private Button next;
    private Reader reader;

    private ProgressDialog pd;

    private static final String TAG_BODY_START = "<body";
    private static final String TAG_BODY_END = "</body>";
    private static final char TAG_CLOSING = '>';

    private String location;
    private String sectionContent = "";
    private List<String> sectionContents;
    private int currentPage = 1;
    private BookSection bookSection;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_epub);



        webView = (WebView) findViewById(R.id.webView);
        previous = (Button) findViewById(R.id.buttonprevious);
        next = (Button) findViewById(R.id.buttonnext);
        previous.setBackgroundColor(Color.TRANSPARENT);
        next.setBackgroundColor(Color.TRANSPARENT);

        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentPage--;
                setSectionContent();
                webView.loadData(sectionContent, "text/html;charset=UTF-8", null);
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentPage++;
                setSectionContent();
                webView.loadData(sectionContent, "text/html;charset=UTF-8", null);
            }
        });

        location = getIntent().getStringExtra("FileLocation");

        test();

    }

    private String getHtmlBody(String htmlContent) throws ReadingException
    {
        int startOfBody = htmlContent.lastIndexOf(TAG_BODY_START);
        int endOfBody = htmlContent.lastIndexOf(TAG_BODY_END);

        int bodyStartEndIndex = startOfBody + TAG_BODY_START.length();

        while (htmlContent.charAt(bodyStartEndIndex) != TAG_CLOSING)
        {
            bodyStartEndIndex++;
        }

        if (startOfBody != -1 && endOfBody != -1)
        {
            return htmlContent.substring(bodyStartEndIndex + 1, endOfBody);
        }
        else
        {
            throw new ReadingException("Exception while getting book section : Html body tags not found.");
        }
    }

    private void test()
    {
        CssStatus[] cssStatuses = new CssStatus[] { CssStatus.OMIT, CssStatus.INCLUDE }; // CssStatus.OMIT, CssStatus.DISTRIBUTE
        int maxContent = 800;

        reader = new Reader();
        sectionContents = new ArrayList<>();

            for (CssStatus cssStatus : cssStatuses)
            {
                        if (!sectionContents.isEmpty())
                        {
                            sectionContents.clear();
                        }

                        try
                        {
                            System.out.println("Reading file content (cssStatus: " + cssStatus + ", " + "maxContent: " + maxContent + " : " + location);

                            reader.setMaxContentPerSection(maxContent);
                            reader.setCssStatus(cssStatus);
                            reader.setIsIncludingTextContent(true);

                            try
                            {
                                reader.setFullContent(location);
                            }
                            catch (Exception e)
                            {
                                e.printStackTrace();
                            }
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                }
                setSectionContent();
                webView.loadData(sectionContent, "text/html;charset=UTF-8", null);
            }

            private void setSectionContent()
            {
                try
                {
                    reader.readSection(currentPage+1);
                    next.setClickable(true);
                } catch (OutOfPagesException e) {
                    next.setClickable(false);
                } catch (ReadingException e) {
                    e.printStackTrace();
                }
                try {
                    bookSection = reader.readSection(currentPage);
                    sectionContent = getHtmlBody(bookSection.getSectionContent());
                    Assert.assertFalse(sectionContent.endsWith("<*.") || sectionContent.startsWith("*.>"));
                } catch (ReadingException | OutOfPagesException e) {
                    e.printStackTrace();
                }
            }


}
