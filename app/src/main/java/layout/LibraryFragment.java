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
import android.widget.ListView;

import java.util.ArrayList;

import hasse.s.sire_simplereader.FileOpener;
import hasse.s.sire_simplereader.R;
import hasse.s.sire_simplereader.SQLiteHelper;


public class LibraryFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    private ListView listView;
    private ArrayList<String> bookList;
    private ArrayAdapter<String> arrayAdapter;
    private SQLiteHelper sqLiteHelper;

    public LibraryFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static LibraryFragment newInstance(int sectionNumber) {
        LibraryFragment fragment = new LibraryFragment();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onStart()
    {
        super.onStart();
        bookList = sqLiteHelper.getAllBookNames();
        arrayAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,bookList);
        listView.setAdapter(arrayAdapter);
    }

    @Override
    public void onResume()
    {
        super.onResume();
        bookList = sqLiteHelper.getAllBookNames();
        arrayAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,bookList);
        listView.setAdapter(arrayAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_library, container, false);

        listView = (ListView) view.findViewById(R.id.listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                sqLiteHelper.updateRow(listView.getItemAtPosition(position).toString());
                FileOpener.openFile(getActivity(), listView, position);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {

                String pathAndName = sqLiteHelper.getPathForName(listView.getItemAtPosition(position).toString());
                //String extension = pathAndName.substring(pathAndName.lastIndexOf('.') + 1);
                String name = pathAndName.substring(pathAndName.lastIndexOf('/') + 1);

                sqLiteHelper.deleteEntry(name);

                onResume();
                return true;
            }
        });

        sqLiteHelper = new SQLiteHelper(getActivity());

        bookList = sqLiteHelper.getAllBookNames();
        arrayAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,bookList);
        listView.setAdapter(arrayAdapter);

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onLibraryFragmentInteraction(uri);
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
        void onLibraryFragmentInteraction(Uri uri);
    }
}
