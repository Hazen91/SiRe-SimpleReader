package hasse.s.sire_simplereader;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

import java.io.File;

public class DocxActivity extends AppCompatActivity {

    private String location = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_docx);

        TextView textView = (TextView) findViewById(R.id.docxTextView);
        textView.setMovementMethod(new ScrollingMovementMethod());

        location = getIntent().getStringExtra("Location");

        String text = getIntent().getStringExtra("content");
        textView.setText(text);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        deleteRecursive(new File(location));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        deleteRecursive(new File(location));
    }

    private void deleteRecursive(File fileOrDirectory) {
        if (fileOrDirectory.isDirectory())
            for (File child : fileOrDirectory.listFiles())
                deleteRecursive(child);

        fileOrDirectory.delete();
    }

}
