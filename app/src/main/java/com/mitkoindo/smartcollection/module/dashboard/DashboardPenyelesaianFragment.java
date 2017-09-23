package com.mitkoindo.smartcollection.module.dashboard;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.mitkoindo.smartcollection.R;
import com.mitkoindo.smartcollection.objectdata.DashboardData;
import com.mitkoindo.smartcollection.utilities.NetworkConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class DashboardPenyelesaianFragment extends Fragment
{
    public DashboardPenyelesaianFragment()
    {
        // Required empty public constructor
    }

    //----------------------------------------------------------------------------------------------
    //  Data
    //----------------------------------------------------------------------------------------------
    //list of dashboard data
    private ArrayList<DashboardData> dashboardData = new ArrayList<>();

    //----------------------------------------------------------------------------------------------
    //  Views
    //----------------------------------------------------------------------------------------------
    //spinner
    private Spinner view_Spinner;

    //holder chart
    private View holder_Chart;

    //holder progress bar
    private View holder_Progress;

    //chart
    private PieChart chart_Debitur;
    private PieChart chart_Nominal;

    //alert
    private TextView view_Alert;

    //data debitur chart
    private TextView view_Debitur_Target;
    private TextView view_Debitur_Realisasi;
    private TextView view_Debitur_Outstanding;

    //data nominal chart
    private TextView view_Nominal_Target;
    private TextView view_Nominal_Realisasi;
    private TextView view_Nominal_Outstanding;

    //refresher
    private SwipeRefreshLayout view_Refresher;

    //----------------------------------------------------------------------------------------------
    //  Transaksi
    //----------------------------------------------------------------------------------------------
    //url
    private String baseURL;
    private String url_DataSP;

    //auth token
    private String authToken;

    //userID
    private String userID;

    //mode dashboard
    private String currentDashboardMode;
    private String DASHBOARDMODE_CURRENT;
    private String DASHBOARDMODE_MONTH;

    //----------------------------------------------------------------------------------------------
    //  Setup
    //----------------------------------------------------------------------------------------------
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View thisView = inflater.inflate(R.layout.fragment_dashboard_penyelesaian, container, false);
        GetViews(thisView);
        SetupViews();
        CreateGetDashboardPenyelesaianRequest();
        return thisView;
    }

    //get views
    private void GetViews(View thisView)
    {
        //get holder
        holder_Chart = thisView.findViewById(R.id.DashboardPenyelesaianFragment_ChartLayout);
        holder_Progress = thisView.findViewById(R.id.DashboardPenyelesaianFragment_ProgressLayout);

        //get spinner
        view_Spinner = thisView.findViewById(R.id.DashboardPenyelesaianFragment_Spinner);

        //get chart
        chart_Debitur = thisView.findViewById(R.id.DashboardPenyelesaianFragment_Chart_Debitur);
        chart_Nominal = thisView.findViewById(R.id.DashboardPenyelesaianFragment_Chart_Nominal);

        //get alert
        view_Alert = thisView.findViewById(R.id.DashboardPenyelesaianFragment_AlertMessage);

        //debitur chart
        view_Debitur_Target = thisView.findViewById(R.id.DashboardPenyelesaianFragment_Debitur_Target);
        view_Debitur_Realisasi = thisView.findViewById(R.id.DashboardPenyelesaianFragment_Debitur_Realisasi);
        view_Debitur_Outstanding = thisView.findViewById(R.id.DashboardPenyelesaianFragment_Debitur_Outstanding);

        //nominal chart
        view_Nominal_Target = thisView.findViewById(R.id.DashboardPenyelesaianFragment_Nominal_Target);
        view_Nominal_Realisasi = thisView.findViewById(R.id.DashboardPenyelesaianFragment_Nominal_Realisasi);
        view_Nominal_Outstanding = thisView.findViewById(R.id.DashboardPenyelesaianFragment_Nominal_Outstanding);

        //get refresher + add listener
        view_Refresher = thisView.findViewById(R.id.DashboardPenyelesaianFragment_Refresher);
        view_Refresher.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                view_Refresher.setRefreshing(false);
                CreateGetDashboardPenyelesaianRequest();
            }
        });
    }

    //setup views
    private void SetupViews()
    {
        //set spinner value
        String[] spinnerValue = new String[]
                {
                        getString(R.string.Text_HariIni),
                        getString(R.string.Text_Akumulasi)
                };
        ArrayAdapter<CharSequence> spinnerAdapter = new ArrayAdapter<>(getActivity(), R.layout.preset_spinneritem, spinnerValue);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        view_Spinner.setAdapter(spinnerAdapter);
        view_Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
                //set status ke selected status
                switch (i)
                {
                    case 0 :
                        //show data hari ini
                        currentDashboardMode = DASHBOARDMODE_CURRENT;
                        CreateGetDashboardPenyelesaianRequest();
                        break;
                    case 1 :
                        //show data monthly
                        currentDashboardMode = DASHBOARDMODE_MONTH;
                        CreateGetDashboardPenyelesaianRequest();
                    default:break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {

            }
        });
    }

    //setup transaction properties
    public void SetupTransactionProperties(String baseURL, String url_DataSP, String authToken, String userID)
    {
        this.baseURL = baseURL;
        this.url_DataSP = url_DataSP;
        this.authToken = authToken;
        this.userID = userID;
    }

    //set dashboard mode
    public void SetDashboardMode(String DASHBOARDMODE_CURRENT, String DASHBOARDMODE_MONTH)
    {
        this.DASHBOARDMODE_CURRENT = DASHBOARDMODE_CURRENT;
        this.DASHBOARDMODE_MONTH = DASHBOARDMODE_MONTH;

        //set default mode ke today
        currentDashboardMode = DASHBOARDMODE_CURRENT;
    }

    //----------------------------------------------------------------------------------------------
    //  Create request buat get data dashboard
    //----------------------------------------------------------------------------------------------
    private void CreateGetDashboardPenyelesaianRequest()
    {
        //show progress & hide chart
        holder_Progress.setVisibility(View.VISIBLE);
        holder_Chart.setVisibility(View.GONE);
        view_Alert.setVisibility(View.GONE);

        //send request
        new SendGetDashboardPenyelesaianRequest().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "");
    }

    //send request
    private class SendGetDashboardPenyelesaianRequest extends AsyncTask<String, Void, String>
    {
        @Override
        protected String doInBackground(String... strings)
        {
            //create url
            String usedURL = baseURL + url_DataSP;

            //create request object
            JSONObject requestObject = CreateGetDashboardRequestObject();

            //execute transaction
            NetworkConnection networkConnection = new NetworkConnection(authToken, "");
            networkConnection.SetRequestObject(requestObject);
            return networkConnection.SendPostRequest(usedURL);
        }

        @Override
        protected void onPostExecute(String s)
        {
            super.onPostExecute(s);
            HandleGetDashboardResult(s);
        }
    }

    //create request object
    private JSONObject CreateGetDashboardRequestObject()
    {
        //create request object
        JSONObject requestObject = new JSONObject();

        try
        {
            //create sp parameter object
            JSONObject spParameterObject = new JSONObject();
            spParameterObject.put("userID", userID);

            //populate request object
            requestObject.put("DatabaseID", "db1");
            requestObject.put("SpName", currentDashboardMode);
            requestObject.put("SpParameter", spParameterObject);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        //return request object
        return requestObject;
    }

    //handle result dari transaksi
    private void HandleGetDashboardResult(String resultString)
    {
        //pastikan response tidak kosong
        if (resultString == null || resultString.isEmpty())
        {
            //show something wrong
            String alertMessage = getString(R.string.Text_SomethingWrong);
            view_Alert.setText(alertMessage);
            view_Alert.setVisibility(View.VISIBLE);
            holder_Progress.setVisibility(View.GONE);
            return;
        }

        //cek response
        if (resultString.equals("Not Found"))
        {
            //no data
            String alertMessage = getString(R.string.Text_NoData);
            view_Alert.setText(alertMessage);
            view_Alert.setVisibility(View.VISIBLE);
            holder_Progress.setVisibility(View.GONE);
            return;

        }

        //initialize dashboard data holder
        dashboardData.clear();

        //initialize entry untuk pie chart
        ArrayList<PieEntry> pieEntries_Dabitur = new ArrayList<>();
        ArrayList<PieEntry> pieEntries_Nominal = new ArrayList<>();

        try
        {
            //parse data
            JSONArray dataArray = new JSONArray(resultString);
            for (int i = 0; i < dataArray.length(); i++)
            {
                //parse
                DashboardData currentdashboardData = new DashboardData();
                currentdashboardData.Parse(dataArray.getJSONObject(i));

                //add ke list
                dashboardData.add(currentdashboardData);

                //cek apakah ini data debitur atau nominal
                if (currentdashboardData.Kategori.equals("debitur"))
                {
                    //Add data ke pie entry debitur
                    /*PieEntry pieEntry = new PieEntry((float) Double.parseDouble(currentdashboardData.RealisasiProsentase), "Realisasi");
                    PieEntry pieEntry1 = new PieEntry((float) Double.parseDouble(currentdashboardData.OutstandingProsentase), "Outstanding");*/
                    String realisasiLabel = getString(R.string.Dashboard_Penyelesaian_SudahBayar);
                    String outstandingLabel = getString(R.string.Dashboard_Penyelesaian_BelumBayar);
                    PieEntry pieEntry = new PieEntry((float) currentdashboardData.RealisasiProsentaseValue, realisasiLabel);
                    PieEntry pieEntry1 = new PieEntry((float) currentdashboardData.OutstandingProsentaseValue, outstandingLabel);

                    //add ke list
                    pieEntries_Dabitur.add(pieEntry);
                    pieEntries_Dabitur.add(pieEntry1);

                    //create chart
                    PieDataSet pieDataSet = new PieDataSet(pieEntries_Dabitur, "Debitur");
                    pieDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
                    PieData pieData = new PieData(pieDataSet);
                    chart_Debitur.setData(pieData);
                    chart_Debitur.invalidate();

                    //set data debitur
                    view_Debitur_Target.setText("" + currentdashboardData.Target);
                    view_Debitur_Realisasi.setText("" + currentdashboardData.Realisasi);
                    view_Debitur_Outstanding.setText("" + currentdashboardData.Outstanding);
                }
                else
                {
                    //add data ke pie entry nominal
                    /*PieEntry pieEntry = new PieEntry((float) Double.parseDouble(currentdashboardData.RealisasiProsentase), "Realisasi");
                    PieEntry pieEntry1 = new PieEntry((float) Double.parseDouble(currentdashboardData.OutstandingProsentase), "Outstanding");*/
                    String realisasiLabel = getString(R.string.Dashboard_Penyelesaian_SudahBayar);
                    String outstandingLabel = getString(R.string.Dashboard_Penyelesaian_BelumBayar);
                    PieEntry pieEntry = new PieEntry((float) currentdashboardData.RealisasiProsentaseValue, realisasiLabel);
                    PieEntry pieEntry1 = new PieEntry((float) currentdashboardData.OutstandingProsentaseValue, outstandingLabel);

                    //add ke list
                    pieEntries_Nominal.add(pieEntry);
                    pieEntries_Nominal.add(pieEntry1);

                    //create chart
                    PieDataSet pieDataSet = new PieDataSet(pieEntries_Nominal, "Nominal");
                    pieDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
                    PieData pieData = new PieData(pieDataSet);
                    chart_Nominal.setData(pieData);
                    chart_Nominal.invalidate();

                    ///set data nominal
                    view_Nominal_Target.setText("" + currentdashboardData.Target);
                    view_Nominal_Realisasi.setText("" + currentdashboardData.Realisasi);
                    view_Nominal_Outstanding.setText("" + currentdashboardData.Outstanding);
                }

                //show alert
                holder_Progress.setVisibility(View.GONE);
                holder_Chart.setVisibility(View.VISIBLE);
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();

            //show something wrong
            String alertMessage = getString(R.string.Text_SomethingWrong);
            view_Alert.setText(alertMessage);
            view_Alert.setVisibility(View.VISIBLE);
            holder_Progress.setVisibility(View.GONE);
            return;
        }
    }
}
