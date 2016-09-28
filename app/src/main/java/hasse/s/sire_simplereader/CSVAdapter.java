package hasse.s.sire_simplereader;


import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


class CSVAdapter extends ArrayAdapter<String>
{
    private final Context mContext;

    public CSVAdapter(Context context, int resource, String[] objects) {

        super(context, resource, objects);
        this.mContext = context;
        values = objects;
    }

    @Override
    public int getCount() {
        return values.length;
    }

    @Override
    public String getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        TextView textView;
        if(view == null)
        {
            textView = new TextView(mContext);
        }
        else
        {
            textView = (TextView) view;
        }
        textView.setText(values[i]);
        textView.setTextColor(ContextCompat.getColor(getContext(), R.color.textGray));
        textView.setGravity(1);
        return textView;
    }

    private final String[] values;
}
