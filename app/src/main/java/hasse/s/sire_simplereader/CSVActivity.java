package hasse.s.sire_simplereader;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.GridView;

import java.io.File;

public class CSVActivity extends AppCompatActivity {

    private GridView gridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_csv);

        gridView = (GridView) findViewById(R.id.gridView);

        String location = getIntent().getStringExtra("FileLocation");
        File file = new File(location);

        CSVParser csvParser = new CSVParser();
        String[] csvArray = csvParser.convertCsvFileToStringArray(file);

        CSVAdapter csvAdapter = new CSVAdapter(this, R.id.gridView, csvArray);

        gridView.setAdapter(csvAdapter);
    }
}
