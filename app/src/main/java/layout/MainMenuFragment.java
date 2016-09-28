package layout;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

import hasse.s.sire_simplereader.FileChooser;
import hasse.s.sire_simplereader.FileOpener;
import hasse.s.sire_simplereader.R;
import hasse.s.sire_simplereader.SQLiteHelper;


public class MainMenuFragment extends Fragment {


    private OnFragmentInteractionListener mListener;
    private ListView listView;
    private SQLiteHelper sqLiteHelper;
    private TextView recentDocsHeader;
    private View topDivider;
    private View lowerDivider;

    public MainMenuFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static MainMenuFragment newInstance(int sectionNumber) {
        MainMenuFragment fragment = new MainMenuFragment();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_main, container, false);



        Button openfile = (Button) view.findViewById(R.id.ButtonOpenFile);
        openfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new FileChooser(getActivity()).setFileListener(new FileChooser.FileSelectedListener() {
                    @Override
                    public void fileSelected(File file)
                    {
                        sqLiteHelper.updateRow(file.getName());
                        FileOpener.openFile(file,getActivity());
                    }
                }).showDialog();
            }
        });

        listView = (ListView) view.findViewById(R.id.recentsListView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id)
            {
                sqLiteHelper.updateRow(listView.getItemAtPosition(position).toString());
                FileOpener.openFile(getActivity(), listView, position);
            }
        });

        recentDocsHeader = (TextView) view.findViewById(R.id.recDocsHeader);
        lowerDivider = view.findViewById(R.id.lowerDivider);
        topDivider = view.findViewById(R.id.topDivider);

        // Inflate the layout for this fragment
        return view;
    }

    private void hideRecentBooksList()
    {

        recentDocsHeader.setVisibility(View.GONE);
        topDivider.setVisibility(View.GONE);
        lowerDivider.setVisibility(View.GONE);
    }

    private void showRecentBooksList()
    {
        recentDocsHeader.setVisibility(View.VISIBLE);
        topDivider.setVisibility(View.VISIBLE);
        lowerDivider.setVisibility(View.VISIBLE);
    }

    @Override
    public void onStart()
    {
        super.onStart();

        sqLiteHelper = new SQLiteHelper(getActivity());
        ArrayList<String> recentBooks = sqLiteHelper.getRecentBooks();

        if(recentBooks.isEmpty())
        {
            hideRecentBooksList();
        }
        else
        {
            showRecentBooksList();
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,recentBooks);
        listView.setAdapter(arrayAdapter);

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onMainMenuFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onMainMenuFragmentInteraction(Uri uri);
    }
}
