package hasse.s.sire_simplereader;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.pdf.PdfRenderer;
import android.os.ParcelFileDescriptor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;

import uk.co.senab.photoview.PhotoViewAttacher;


public class PDFActivity extends AppCompatActivity {

   // private static final String STATE_CURRENT_PAGE_INDEX = "current_page_index";

    private ParcelFileDescriptor mFileDescriptor;
    private PdfRenderer mPdfRenderer;
    private PdfRenderer.Page mCurrentPage;

    private ImageView mImageView;
    private PhotoViewAttacher mAttacher;
    private Button mButtonPrevious;
    private Button mButtonNext;
    private MenuItem keepZoomItem;

    String location = "";
    String fileName = "";
    private int currentPage = 0;
    private int pageCount = 0;
    private float zoomLevel = 1.0f;

    private boolean start = true;

    private int jumpNumber = 1;
    private SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf);

        mImageView = (ImageView) findViewById(R.id.image);
        mButtonPrevious = (Button) findViewById(R.id.previous);
        mButtonNext = (Button) findViewById(R.id.buttonnext);
        sharedPreferences = this.getSharedPreferences("PageMemory", Context.MODE_PRIVATE);

        mButtonPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentPage > 0)
                {
                    currentPage--;
                    saveLastViewedPage();
                    if(keepZoomItem.isChecked())
                    {zoomLevel = mAttacher.getScale();}
                }
                showPage();
            }
        });
        mButtonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(currentPage < pageCount)
                {
                    currentPage++;
                    saveLastViewedPage();
                    if(keepZoomItem.isChecked())
                    {zoomLevel = mAttacher.getScale();}
                }
                showPage();
            }
        });
        mButtonNext.setBackgroundColor(Color.TRANSPARENT);
        mButtonPrevious.setBackgroundColor(Color.TRANSPARENT);
        mButtonPrevious.bringToFront();
        mButtonNext.bringToFront();

        location = getIntent().getStringExtra("FileLocation");
        fileName = location.substring(location.lastIndexOf('/') + 1);
        fileName = fileName.substring(0,fileName.length() - 4);

        openRenderer();

        showPage();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_pdf, menu);
        keepZoomItem = menu.findItem(R.id.action_keepZoom);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id)
        {
            case R.id.action_settings:
                getFragmentManager().beginTransaction()
                        .replace(android.R.id.content, new SettingsFragment())
                        .addToBackStack("preferences")
                        .commit();
                break;

            case R.id.action_jumppage:
                makeJumpDialog();
                break;

            case R.id.action_restorePage:
                SharedPreferences sharedPreferences = this.getSharedPreferences(getString(R.string.pageMemory), Context.MODE_PRIVATE);
                currentPage = sharedPreferences.getInt(getString(R.string.savedPageNumber),currentPage);
                showPage();
                break;

            case R.id.action_keepZoom:
                if(keepZoomItem.isChecked())
                    keepZoomItem.setChecked(false);
                else
                    keepZoomItem.setChecked(true);
                break;

            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed()
    {
        closeRenderer();
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

    private void showPage()
    {
        try
        {
            if (mCurrentPage != null)
            {
                mCurrentPage.close();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        mCurrentPage = mPdfRenderer.openPage(currentPage);
        Bitmap bitmap = Bitmap.createBitmap(mCurrentPage.getWidth(), mCurrentPage.getHeight(), Bitmap.Config.ARGB_8888);
        mCurrentPage.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);
        mImageView.setImageBitmap(bitmap);


        if(start)
        {
            mAttacher = new PhotoViewAttacher(mImageView);
            start = false;
        }
        else if(!start)
        {   mAttacher.update();
            if(keepZoomItem.isChecked())
            {mAttacher.setScale(zoomLevel);}
        }

        setTitle((currentPage+1)+"/"+(pageCount+1)+" - " + fileName);
    }

    private void openRenderer()
    {
        File file = new File(location);
        try {
            mFileDescriptor = ParcelFileDescriptor.open(file,ParcelFileDescriptor.MODE_READ_ONLY);
            mPdfRenderer = new PdfRenderer(mFileDescriptor);
            pageCount = mPdfRenderer.getPageCount() - 1;
            Log.i("PAGECOUNT:   ", ""+mPdfRenderer.getPageCount());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void closeRenderer()
    {
        try
        {
            if (mCurrentPage != null)
            {
                mCurrentPage.close();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        mPdfRenderer.close();
        try {
            mFileDescriptor.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveLastViewedPage()
    {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(getString(R.string.savedPageNumber),currentPage);
        editor.apply();
    }

    @Override
    public void onDestroy()
    {
        closeRenderer();
        super.onDestroy();
    }

    private void makeJumpDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Jump to Page");
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        builder.setView(input);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(!input.getText().toString().equals(""))
                {jumpNumber = Integer.parseInt(input.getText().toString());}
                if(jumpNumber <= pageCount && jumpNumber > 0)
                {
                    currentPage = --jumpNumber;
                    showPage();
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

}
