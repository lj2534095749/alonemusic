package com.example.alonemusic.ui.contacts;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.alonemusic.R;
import com.example.alonemusic.activity.ContactsItemActivity;
import com.example.alonemusic.adapter.TextImageAdapter;
import com.example.alonemusic.bean.Contacts;
import com.example.alonemusic.dao.ContactsDao;

import java.util.ArrayList;
import java.util.List;

public class ContactsFragment extends Fragment {

    TextView toolbarTitle;
    List<Contacts> contactsList = new ArrayList<>();
    ListView mListView = null;
    LinearLayout progressBarLayout;
    ProgressBar pb;
    ContactsDao contactsDao;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             final ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contacts, container, false);
        toolbarTitle = getActivity().findViewById(R.id.toolbar_title);
        toolbarTitle.setText("Contacts");

        contactsDao = new ContactsDao(getActivity());

        progressBarLayout = view.findViewById(R.id.progressBarLayout);
        pb = view.findViewById(R.id.pb);
        mListView = view.findViewById(R.id.list_image);
        initContactsList();
        TextImageAdapter adapter = new TextImageAdapter(getActivity(), contactsList);
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("contactsList", contactsList.get(position));
                Intent intent = new Intent(getContext(), ContactsItemActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
                //Toast.makeText(getActivity(),"您选择了" + contactsList.get(position).getName(), Toast.LENGTH_SHORT).show();
            }
        });
        new HandlerAsyncTask().execute(1);
        return view;
    }

    private void initContactsList(){
        contactsList = contactsDao.queryAllContacts(getActivity().getContentResolver());
        Log.d("ContactsFragment", "initContactsList: " + contactsList);
    }

    private class HandlerAsyncTask extends AsyncTask<Integer, Integer, String> {

        @Override
        protected void onPreExecute() {
            //第一个执行方法
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Integer... params) {
            //第二个执行方法,onPreExecute()执行完后执行
            for(int i = 0;i <= 100; i++){
                publishProgress(i);
                try {
                    Thread.sleep(params[0]);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return "执行完毕";
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);
            pb.setProgress(progress[0]);
            try {
                if(progress[0] == 100){
                    progressBarLayout.removeView(pb);
//                    TextView pbFinishText = new TextView(getActivity());
//                    pbFinishText.setGravity(Gravity.CENTER);
//                    pbFinishText.setText("Finish");
//                    pbFinishText.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorBlue));
//                    progressBarLayout.addView(pbFinishText);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        @Override
        protected void onPostExecute(String result) {
//            setTitle(result);
            super.onPostExecute(result);
        }

    }

}