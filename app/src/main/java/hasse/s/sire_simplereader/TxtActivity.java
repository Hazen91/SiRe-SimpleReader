package hasse.s.sire_simplereader;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class TxtActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_txt);

        TextView txtView = (TextView) findViewById(R.id.txtView);
        txtView.setMovementMethod(new ScrollingMovementMethod());

        String location = getIntent().getStringExtra("FileLocation");
        File file = new File(location);

        StringBuilder text = new StringBuilder();

        try
        {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            while ((line = br.readLine()) != null) {
                text.append(line);
                text.append('\n');
            }
            br.close();
        }
        catch (IOException e)
        {
            //You'll need to add proper error handling here
        }

        txtView.setText(text);
    }
}
