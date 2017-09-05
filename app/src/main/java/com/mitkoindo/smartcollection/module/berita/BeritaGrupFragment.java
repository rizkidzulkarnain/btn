package com.mitkoindo.smartcollection.module.berita;


import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mitkoindo.smartcollection.R;
import com.mitkoindo.smartcollection.adapter.BeritaAdapter;
import com.mitkoindo.smartcollection.helper.ItemClickListener;
import com.mitkoindo.smartcollection.objectdata.MobileNews;
import com.mitkoindo.smartcollection.utilities.NetworkConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class BeritaGrupFragment extends Fragment
{


    public BeritaGrupFragment()
    {
        // Required empty public constructor
    }

    //----------------------------------------------------------------------------------------------
    //  Views
    //----------------------------------------------------------------------------------------------
    //view data
    private RecyclerView view_ListBerita;

    //progress bar
    private ProgressBar view_ProgressBar;

    //message
    private TextView view_Message;

    //layout buat refresh
    private SwipeRefreshLayout view_SwipeRefresher;

    //progress bar buat indicator load page baru
    private ProgressBar view_ProgressBar_PageIndicator;

    //search form
    private EditText view_SearchForm;

    //search button
    private ImageView view_SearchButton;

    //clear button
    private ImageView view_ClearButton;

    //----------------------------------------------------------------------------------------------
    //  Data
    //----------------------------------------------------------------------------------------------
    //adapter for berita
    private BeritaAdapter beritaAdapter;

    //search query
    private String searchQuery;

    //----------------------------------------------------------------------------------------------
    //  Transaksi
    //----------------------------------------------------------------------------------------------
    //base url dan url yang digunakan
    private String baseURL;
    private String url_GetNews;

    //auth token
    private String authToken;

    //user ID
    private String userID;

    //----------------------------------------------------------------------------------------------
    //  Setup
    //----------------------------------------------------------------------------------------------
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View thisView = inflater.inflate(R.layout.fragment_berita_grup, container, false);
        GetViews(thisView);
        CreateGetNewsRequest();
        return thisView;
    }

    //get views
    private void GetViews(View thisView)
    {
        view_ListBerita = thisView.findViewById(R.id.BeritaGroupFragment_ListBerita);
        view_ProgressBar = thisView.findViewById(R.id.BeritaGroupFragment_ProgressBar);
        view_Message = thisView.findViewById(R.id.BeritaGroupFragment_Message);
        view_ProgressBar_PageIndicator = thisView.findViewById(R.id.BeritaGrupFragment_PageLoadingIndicator);
        view_SwipeRefresher = thisView.findViewById(R.id.BeritaGroupFragment_SwipeRefresh);
        view_SearchForm = thisView.findViewById(R.id.BeritaGroupFragment_SearchForm);
        view_SearchButton = thisView.findViewById(R.id.BeritaGroupFragment_SearchButton);
        view_ClearButton = thisView.findViewById(R.id.BeritaGroupFragment_ClearButton);

        //set listener
        view_SwipeRefresher.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                CreateGetNewsRequest();
            }
        });

        //set listener pada list
        view_ListBerita.addOnScrollListener(new RecyclerView.OnScrollListener()
        {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy)
            {
                super.onScrolled(recyclerView, dx, dy);

                //pastikan adapter tidak null
                if (beritaAdapter == null)
                    return;

                //load new page
                beritaAdapter.CreateLoadNewPageRequest();
            }
        });

        //add listener to search form
        view_SearchForm.setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent)
            {
                if ((keyEvent.getAction() == KeyEvent.ACTION_DOWN) && (i == KeyEvent.KEYCODE_ENTER))
                {
                    //Handle search
                    CreateSearchRequest();

                    return true;
                }

                return false;
            }
        });

        //add listener pada search button
        view_SearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HandleInput_SearchButton();
            }
        });

        //add listener pada clear button
        view_ClearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HandleInput_ClearButton();
            }
        });
    }

    //set transaction data
    public void SetTransactionData(String baseURL, String url_GetNews, String authToken, String userID)
    {
        this.baseURL = baseURL;
        this.url_GetNews = url_GetNews;
        this.authToken = authToken;
        this.userID = userID;
    }

    //----------------------------------------------------------------------------------------------
    //  Start transaction buat load berita
    //----------------------------------------------------------------------------------------------
    private void CreateGetNewsRequest()
    {
        //show progress bar & hide recycler view
        view_ProgressBar.setVisibility(View.VISIBLE);
        view_ListBerita.setVisibility(View.GONE);
        view_Message.setVisibility(View.GONE);
        view_SwipeRefresher.setRefreshing(false);

        //send request
        new SendGetNewsRequest().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "");
    }

    //send get news request
    private class SendGetNewsRequest extends AsyncTask<String, Void, String>
    {
        @Override
        protected String doInBackground(String... strings)
        {
            //create used url
            String usedURL = baseURL + url_GetNews;

            //create request object
            JSONObject requestObject = CreateGetNewsRequestObject();

            //execute transaction
            NetworkConnection networkConnection = new NetworkConnection(authToken, "");
            networkConnection.SetRequestObject(requestObject);
            return networkConnection.SendPostRequest(usedURL);
        }

        @Override
        protected void onPostExecute(String s)
        {
             super.onPostExecute(s);
            HandleGetGroupNewsResult(s);
        }
    }

    //create request object
    private JSONObject CreateGetNewsRequestObject()
    {
        //create empty object
        JSONObject requestObject = new JSONObject();

        //populate request object
        try
        {
            //create sorting object
            JSONObject sortingObject = new JSONObject();
            sortingObject.put("Property", "CreatedDate");
            sortingObject.put("Direction", "DESC");

            //create sorting array
            JSONArray sortingArray = new JSONArray();
            sortingArray.put(sortingObject);

            //create dbParam
            JSONObject dbParam = new JSONObject();
            dbParam.put("Page", 1);
            dbParam.put("Limit", 10);
            dbParam.put("Sort", sortingArray);

            //create filter object
            JSONObject filterObject = new JSONObject();
            filterObject.put("Property", "ToUserID");
            filterObject.put("Operator", "eq");
            filterObject.put("Value", "'" + userID + "'");

            //create filter array
            JSONArray filterArray = new JSONArray();
            filterArray.put(filterObject);

            //create search object jika searchquery tidak kosong
            if (searchQuery != null && !searchQuery.isEmpty())
            {
                JSONObject searchObject = new JSONObject();
                searchObject.put("Property", "Title");
                searchObject.put("Operator", "like");
                searchObject.put("Value", searchQuery);
                filterArray.put(searchObject);
            }

            //create request object
            requestObject.put("DatabaseID", "db1");
            requestObject.put("ViewName", "MKI_VW_NEWS_GROUP");
            requestObject.put("DBParam", dbParam);
            requestObject.put("Filter", filterArray);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        //return object
        return requestObject;
    }

    //handle get global news
    private void HandleGetGroupNewsResult(String resultString)
    {
        try
        {
            //parse data
            JSONArray dataArray = new JSONArray(resultString);

            //initialize array
            ArrayList<MobileNews> mobileNews = new ArrayList<>();

            //extract data
            for (int i = 0; i < dataArray.length(); i++)
            {
                //extract data dari json
                MobileNews newMobileNews = new MobileNews();
                newMobileNews.ParseData(dataArray.getJSONObject(i));
                mobileNews.add(newMobileNews);
            }

            //create mobile news adapter
            beritaAdapter = new BeritaAdapter(getActivity(), mobileNews);
            beritaAdapter.SetView_ProgressBar(view_ProgressBar_PageIndicator);
            beritaAdapter.SetupTransaction(baseURL, url_GetNews, authToken, userID);
            beritaAdapter.SetSearchQuery(searchQuery);

            //set click listener ke adapter
            beritaAdapter.setClickListener(new ItemClickListener()
            {
                @Override
                public void onItemClick(View view, int position)
                {
                    OpenAttachedFile(position);
                }
            });

            //attach adapter ke list view
            AttachNewsData();
        }
        catch (JSONException e)
        {
            e.printStackTrace();

            //show message bahwa something wrong
            String somethingWrongMessage = getString(R.string.Text_SomethingWrong);
            view_Message.setText(somethingWrongMessage);

            //hide other views
            view_Message.setVisibility(View.VISIBLE);
            view_ProgressBar.setVisibility(View.GONE);
            view_ListBerita.setVisibility(View.GONE);
        }
    }

    //Attach news data
    private void AttachNewsData()
    {
        //attach adapter to recyclerview
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        view_ListBerita.setLayoutManager(layoutManager);
        view_ListBerita.setItemAnimator(new DefaultItemAnimator());
        Drawable dividerDrawable = ContextCompat.getDrawable(getActivity(), R.drawable.divider_vertical_10dp);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(dividerDrawable);
        view_ListBerita.addItemDecoration(dividerItemDecoration);
        view_ListBerita.setAdapter(beritaAdapter);

        //show list
        view_ListBerita.setVisibility(View.VISIBLE);
        view_Message.setVisibility(View.GONE);
        view_ProgressBar.setVisibility(View.GONE);
    }

    //----------------------------------------------------------------------------------------------
    //  Create request buat search
    //----------------------------------------------------------------------------------------------
    private void CreateSearchRequest()
    {
        //set search query
        searchQuery = view_SearchForm.getText().toString();

        //jika search query tidak kosong, ubah tombol search jadi tombol clear
        if (!searchQuery.isEmpty())
        {
            view_ClearButton.setVisibility(View.VISIBLE);
            view_SearchButton.setVisibility(View.GONE);
        }
        else
        {
            //tapi kalo kosong, tampilkan kembali search button
            view_ClearButton.setVisibility(View.GONE);
            view_SearchButton.setVisibility(View.VISIBLE);
        }

        //send request
        CreateGetNewsRequest();
    }

    //----------------------------------------------------------------------------------------------
    //  Handle input
    //----------------------------------------------------------------------------------------------
    //open attachment
    private void OpenAttachedFile(int position)
    {
        //get current berita
        MobileNews newsItem = beritaAdapter.GetCurrentNews(position);

        //pastikan item tidak null
        if (newsItem == null)
            return;

        //buka attachment
        String attachmentURL = newsItem.Attachment;
        char attachmentURL_FirstCharacter = attachmentURL.charAt(0);
        if (attachmentURL_FirstCharacter == '/')
        {
            attachmentURL = attachmentURL.substring(1);
        }

        String intentURL = baseURL + attachmentURL;

        //open intent
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(intentURL));
        startActivity(i);
    }

    //Handle input pada search button
    private void HandleInput_SearchButton()
    {
        CreateSearchRequest();
    }

    //Handle input pada clear button
    private void HandleInput_ClearButton()
    {
        //jika search form tidak kosong, clear textnya
        if (!view_SearchForm.getText().toString().isEmpty())
        {
            view_SearchForm.setText("");
        }
        else
        {
            //jika kosong, show search button & hide clear button
            view_ClearButton.setVisibility(View.GONE);
            view_SearchForm.setVisibility(View.VISIBLE);

            //dan refresh data
            CreateSearchRequest();
        }
    }
}
