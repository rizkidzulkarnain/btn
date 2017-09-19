package com.mitkoindo.smartcollection;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.ResultReceiver;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.mitkoindo.smartcollection.adapter.HomeMenuAdapter;
import com.mitkoindo.smartcollection.backgroundservice.MyJobService;
import com.mitkoindo.smartcollection.backgroundservice.MyJobServiceTracking;
import com.mitkoindo.smartcollection.helper.ItemClickListener;
import com.mitkoindo.smartcollection.helper.RealmHelper;
import com.mitkoindo.smartcollection.helper.ResourceLoader;
import com.mitkoindo.smartcollection.module.assignment.AccountAssignmentActivity;
import com.mitkoindo.smartcollection.module.berita.BeritaActivity;
import com.mitkoindo.smartcollection.module.chat.ChatListActivity;
import com.mitkoindo.smartcollection.module.dashboard.DashboardActivity;
import com.mitkoindo.smartcollection.module.debitur.detaildebitur.DetailDebiturActivity;
import com.mitkoindo.smartcollection.module.debitur.listdebitur.ListDebiturActivity;
import com.mitkoindo.smartcollection.module.laporan.LaporanActivity;
import com.mitkoindo.smartcollection.module.ptp_reminder.PTPReminderActivity;
import com.mitkoindo.smartcollection.module.pusatnotifikasi.PusatNotifikasiActivity;
import com.mitkoindo.smartcollection.network.ApiUtils;
import com.mitkoindo.smartcollection.network.RestConstants;
import com.mitkoindo.smartcollection.network.body.CheckInBody;
import com.mitkoindo.smartcollection.network.body.FormVisitDropDownBody;
import com.mitkoindo.smartcollection.network.models.DbParam;
import com.mitkoindo.smartcollection.network.models.Sort;
import com.mitkoindo.smartcollection.network.response.CheckInResponse;
import com.mitkoindo.smartcollection.objectdata.DropDownAction;
import com.mitkoindo.smartcollection.objectdata.DropDownAddressType;
import com.mitkoindo.smartcollection.objectdata.DropDownPurpose;
import com.mitkoindo.smartcollection.objectdata.DropDownReason;
import com.mitkoindo.smartcollection.objectdata.DropDownRelationship;
import com.mitkoindo.smartcollection.objectdata.DropDownResult;
import com.mitkoindo.smartcollection.objectdata.DropDownStatusAgunan;
import com.mitkoindo.smartcollection.objectdata.DropDownTeleponType;
import com.mitkoindo.smartcollection.objectdata.HomeMenu;
import com.mitkoindo.smartcollection.utilities.GenericAlert;
import com.mitkoindo.smartcollection.utilities.HttpsTrustManager;
import com.mitkoindo.smartcollection.utilities.NetworkConnection;
import com.mitkoindo.smartcollection.utilities.NotificationChecker;
import com.mitkoindo.smartcollection.utils.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class HomeActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener
{

    private CompositeDisposable composites = new CompositeDisposable();

    //----------------------------------------------------------------------------------------------
    //  View
    //----------------------------------------------------------------------------------------------
    //Recycler view
    private RecyclerView view_HomeMenu;

    //user name holder
    private TextView view_UserName;

    //user id holder
    private TextView view_UserID;

    //user group holder
    private TextView view_UserGroup;

    //generic alert
    private GenericAlert genericAlert;

    //handler & runnable untuk update badge
    private Handler badgeUpdaterHandler;
    private Runnable badgeUpdaterRunnable;

    //----------------------------------------------------------------------------------------------
    //  Data
    //----------------------------------------------------------------------------------------------
    //home menu
    private ArrayList<HomeMenu> homeMenus;

    //adapter menu
    private HomeMenuAdapter homeMenuAdapter;

    //----------------------------------------------------------------------------------------------
    //  Transaksi
    //----------------------------------------------------------------------------------------------
    //url
    private String baseUrl;
    private String url_Logout;
    private String url_DataSP;

    //auth token
    private String authToken;

    //user id
    private String userID;

    //delay for updating badge
    private final int delay_update_badge = 5;

    private GoogleApiClient mGoogleApiClient;
    private FusedLocationProviderClient mFusedLocationClient;
    private AddressResultReceiver mResultReceiver;
    private Location mLastKnownLocation;
    private String mAddressOutput;
    private boolean mAddressRequested;


    public static Intent instantiateClearTask(Context context) {
        Intent intent = new Intent(context, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        return intent;
    }

    //----------------------------------------------------------------------------------------------
    //  Setup
    //----------------------------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        genericAlert = new GenericAlert(this);

        //setup
        SetupTransaction();
        GetViews();
        SetupViews();
        StartNotificationChecker();
        getDropdown();
        initBackgroundService();
        initGoogleService();
        initLocation();
        //ignore certificate
        HttpsTrustManager.allowAllSSL();
    }

    @Override
    protected void onStart()
    {
        super.onStart();

        //create handler & runnable to update badge
        badgeUpdaterHandler = new Handler();
        badgeUpdaterRunnable = new Runnable()
        {
            @Override
            public void run()
            {
                homeMenuAdapter.CreateGetBadgeData();
                badgeUpdaterHandler.postDelayed(badgeUpdaterRunnable, delay_update_badge * 1000);
            }
        };

        //start handler
        badgeUpdaterHandler.postDelayed(badgeUpdaterRunnable, delay_update_badge * 1000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        badgeUpdaterHandler.removeCallbacks(badgeUpdaterRunnable);

        composites.clear();
    }

    //get views
    private void GetViews()
    {
        view_HomeMenu = findViewById(R.id.HomeActivity_MenuGrid);
        view_UserName = findViewById(R.id.HomeActivity_UserName);
        view_UserID = findViewById(R.id.HomeActivity_UserID);
        view_UserGroup = findViewById(R.id.HomeActivity_UserGroup);

        //create alert
        genericAlert = new GenericAlert(this);
    }

    //setup view
    private void SetupViews()
    {
        //Setup menu
        String[] menuTitle = SetupMenuBasedOnRole();
        /*String[] menuTitle = getResources().getStringArray(R.array.HomeMenu_BC);*/

        //create menu object
        homeMenus = new ArrayList<>();
        for (int i = 0; i < menuTitle.length; i++)
        {
            HomeMenu newHomeMenu = new HomeMenu();
            newHomeMenu.Name = menuTitle[i];

            //add bitmap tergantung judul menu
            newHomeMenu.Icon = ResourceLoader.LoadMenuIcon(this, newHomeMenu.Name);

            homeMenus.add(newHomeMenu);
        }

        //create menu
        homeMenuAdapter = new HomeMenuAdapter(this, homeMenus);
        homeMenuAdapter.SetTransaction(baseUrl, getString(R.string.URL_Data_StoreProcedure), authToken, userID);

        //set click listener
        homeMenuAdapter.setClickListener(new ItemClickListener()
        {
            @Override
            public void onItemClick(View view, int position)
            {
                HandleInput_Home_SelectMenu(position);
            }
        });

        //attach adapter
        int numberOfColumns = 3;
        view_HomeMenu.setLayoutManager(new GridLayoutManager(this, numberOfColumns));
        view_HomeMenu.setAdapter(homeMenuAdapter);

        //set username
        String key_UserName = getString(R.string.SharedPreferenceKey_NamaPetugas);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String value_UserName = sharedPreferences.getString(key_UserName, "");
        view_UserName.setText(value_UserName);

        //set user ID
        String key_UserID = getString(R.string.SharedPreferenceKey_UserID);
        String value_UserID = sharedPreferences.getString(key_UserID, "");
        view_UserID.setText(value_UserID);

        //set user group
        String key_UserGroup = getString(R.string.SharedPreferenceKey_UserGroup);
        String value_UserGroup = sharedPreferences.getString(key_UserGroup, "");
        view_UserGroup.setText(value_UserGroup);

        //request badge
        homeMenuAdapter.CreateGetBadgeData();
    }

    //setup transaksi
    private void SetupTransaction()
    {
        //load url
        baseUrl = ResourceLoader.LoadBaseURL(this);
        url_Logout = getString(R.string.URL_Logout);
        url_DataSP = getString(R.string.URL_Data_StoreProcedure);

        //load auth token
        authToken = ResourceLoader.LoadAuthToken(this);

        //get user id
        userID = ResourceLoader.LoadUserID(this);
    }

    //setup menu berdasarkan role
    private String[] SetupMenuBasedOnRole()
    {
        String[] menuItems;

        //cek group ID user ini
        String userGroupID = ResourceLoader.LoadGroupID(this);

        //get user group
        final String userGroup_Staff1 = getString(R.string.UserGroup_Staff1);
        final String userGroup_Staff2 = getString(R.string.UserGroup_Staff2);
        final String userGroup_Staff3 = getString(R.string.UserGroup_Staff3);
        final String userGroup_Supervisor1 = getString(R.string.UserGroup_Supervisor1);
        final String userGroup_Supervisor2 = getString(R.string.UserGroup_Supervisor2);
        final String userGroup_BranchCoordinator = getString(R.string.UserGroup_BranchCoordinator);
        final String userGroup_BranchManager = getString(R.string.UserGroup_BranchManager);

        //cek tipe user
        if (userGroupID.equals(userGroup_Staff1) || userGroupID.equals(userGroup_Staff2) || userGroupID.equals(userGroup_Staff3))
            menuItems = getResources().getStringArray(R.array.HomeMenu_Staff);
        else if (userGroupID.equals(userGroup_Supervisor1) || userGroupID.equals(userGroup_Supervisor2))
            menuItems = getResources().getStringArray(R.array.HomeMenu_Supervisor);
        else if (userGroupID.equals(userGroup_BranchCoordinator) || userGroupID.equals(userGroup_BranchManager))
            menuItems = getResources().getStringArray(R.array.HomeMenu_BC);
        else
            menuItems = getResources().getStringArray(R.array.HomeMenu_BC);

        return menuItems;
    }

    //start notification checker
    private void StartNotificationChecker()
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.MINUTE, 1);

        AlarmManager alarmMgr = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, NotificationChecker.class);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
        alarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                300 * 1000, alarmIntent);
    }

    //----------------------------------------------------------------------------------------------
    //  Handle input
    //----------------------------------------------------------------------------------------------
    //handle input buat settings button
    public void HandleInput_Home_OpenSettings(View view)
    {
        //test open change password
        Intent intent = new Intent(this, UpdatePasswordActivity.class);
        startActivity(intent);
    }

    //handle logout
    public void HandleInput_Home_Logout(View view)
    {
        //Confirm apakah user benar2 mau logout
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.Home_Alert_Logout_Message);

        //handle input
        builder.setNegativeButton(R.string.Text_Tidak, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                //do nothing & dismiss the alertdialog
            }
        });
        builder.setPositiveButton(R.string.Text_Ya, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                //delete auth token & return to login screen
                LogoutUser();
            }
        });

        //show alert dialog
        AlertDialog confimationPopup = builder.create();
        confimationPopup.show();
    }

    //handle select menu
    private void HandleInput_Home_SelectMenu(int menuPos)
    {
        //get selected menu name
        String selectedMenuName = homeMenuAdapter.getCurrentMenu(menuPos);

        //open corresponding menu
        Intent intent = null;
        switch (selectedMenuName)
        {
            case "Dashboard" :
                intent = new Intent(this, DashboardActivity.class);
                break;
            case "Berita" :
                intent = new Intent(this, BeritaActivity.class);
                CreateReadNotificationRequest("PageNewsGroup");
                break;
            case "Penugasan" :
                intent = ListDebiturActivity.instantiate(this, ListDebiturActivity.EXTRA_TYPE_PENUGASAN_VALUE);
                CreateReadNotificationRequest("PageAssignment");
                break;
            case "Tambah Kontak" :
                intent = ListDebiturActivity.instantiate(this, ListDebiturActivity.EXTRA_TYPE_TAMBAH_KONTAK_VALUE);
                break;
            case "Assignment" :
                intent = new Intent(this, AccountAssignmentActivity.class);
                break;
            case "PTP Reminder" :
                intent = new Intent(this, PTPReminderActivity.class);
                CreateReadNotificationRequest("PagePtp");
                break;
            case "Chat" :
                intent = new Intent(this, ChatListActivity.class);
                CreateReadNotificationRequest("PageChat");
                break;
            case "Check-In":
                requestAccessLocationPermission();
                break;
            case "Laporan" :
                intent = new Intent(this, LaporanActivity.class);
                break;
            case "Pusat Notifikasi" :
                intent = new Intent(this, PusatNotifikasiActivity.class);
                break;

            default:break;
        }

        if (intent != null)
            startActivity(intent);

        /*int x = 0;
        int y = x + 1;

        //test langsung buka histori tindakan
        Intent intent = new Intent(this, HistoriTindakanActivity.class);
        startActivity(intent);*/
    }

    //----------------------------------------------------------------------------------------------
    //  Manipulate data
    //----------------------------------------------------------------------------------------------
    //handle logout
    private void LogoutUser()
    {
        CreateLogoutRequest();
    }

    //create request buat logout
    private void CreateLogoutRequest()
    {
        //show loading alert
        genericAlert.ShowLoadingAlert();

        //send logout request
        new SendLogoutRequest().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "");
    }

    //send request buat logout
    private class SendLogoutRequest extends AsyncTask<String, Void, String>
    {
        @Override
        protected String doInBackground(String... strings)
        {
            //create url
            String usedURL = baseUrl + url_Logout;

            //eksekusi request
            NetworkConnection networkConnection = new NetworkConnection(authToken, "");
            return networkConnection.SendPutRequest(usedURL);
        }

        @Override
        protected void onPostExecute(String s)
        {
            super.onPostExecute(s);
            HandleLogoutResult(s);
        }
    }

    //handle result dari logout request
    private void HandleLogoutResult(String resultString)
    {
        //cek resultstring, jika 204, maka logout sukses
        if (resultString.equals("204"))
        {
            //remove auth token & kembali ke login screen
            RemoveAuthToken();
        }
        else
        {
            //show alert bahwa gagal logout
            String alertTitle = getString(R.string.Text_MohonMaaf);
            String alertMessage = getString(R.string.Home_LogoutFailed);
            genericAlert.DisplayAlert(alertMessage, alertTitle);
        }
    }

    //remove auth token
    private void RemoveAuthToken()
    {
        //get key to auth token
        String key_AuthToken = getString(R.string.SharedPreferenceKey_AccessToken);

        //delete auth token
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key_AuthToken, "");
        editor.apply();

        //return to login screen
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    //----------------------------------------------------------------------------------------------
    //  Create request buat set semua notifikasi dengan jenis tertentu jadi "read"
    //----------------------------------------------------------------------------------------------
    private void CreateReadNotificationRequest(String pageType)
    {
        //create request object
        JSONObject requestObject = new JSONObject();

        try
        {
            //create spParameterObject
            JSONObject spParameterObject = new JSONObject();
            spParameterObject.put("userID", userID);
            spParameterObject.put("pageType", pageType);

            //setup request object
            requestObject.put("DatabaseID", "db1");
            requestObject.put("SpName", "MKI_SP_NOTIFICATION_READ_BY_TYPE");
            requestObject.put("SpParameter", spParameterObject);

            //send request
            new SendReadNotificationRequest().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, requestObject);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

    //send request
    private class SendReadNotificationRequest extends AsyncTask<JSONObject, Void, String>
    {
        @Override
        protected String doInBackground(JSONObject... jsonObjects)
        {
            //set url
            String usedURL = baseUrl + url_DataSP;

            //set requestObject
            JSONObject requestObject = jsonObjects[0];

            //send request
            NetworkConnection networkConnection = new NetworkConnection(authToken, "");
            networkConnection.SetRequestObject(requestObject);
            return networkConnection.SendPostRequest(usedURL);
        }

        @Override
        protected void onPostExecute(String s)
        {
            super.onPostExecute(s);
        }
    }

    //----------------------------------------------------------------------------------------------
    //  Get dropdown data for form visit
    //----------------------------------------------------------------------------------------------
    private void getDropdown() {
        String key_AuthToken = getString(R.string.SharedPreferenceKey_AccessToken);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String accessToken =  sharedPreferences.getString(key_AuthToken, "");

        Disposable disposable = ApiUtils.getRestServices(accessToken).getDropDownPurpose(createPurposeBody())
                .flatMap(new Function<List<DropDownPurpose>, ObservableSource<List<DropDownRelationship>>>() {
                    @Override
                    public ObservableSource<List<DropDownRelationship>> apply(List<DropDownPurpose> listPurpose) throws Exception {
                        RealmHelper.deleteListDropDownPurpose();
                        RealmHelper.storeListDropDownPurpose(listPurpose);

                        return ApiUtils.getRestServices(accessToken).getDropDownRelationship(createRelationshipBody());
                    }
                })
                .flatMap(new Function<List<DropDownRelationship>, ObservableSource<List<DropDownResult>>>() {
                    @Override
                    public ObservableSource<List<DropDownResult>> apply(List<DropDownRelationship> listRelationship) throws Exception {
                        RealmHelper.deleteListDropDownRelationship();
                        RealmHelper.storeListDropDownRelationship(listRelationship);

                        return ApiUtils.getRestServices(accessToken).getDropDownResult(createResultBody());
                    }
                })
                .flatMap(new Function<List<DropDownResult>, ObservableSource<List<DropDownReason>>>() {
                    @Override
                    public ObservableSource<List<DropDownReason>> apply(List<DropDownResult> listResult) throws Exception {
                        RealmHelper.deleteListDropDownResult();
                        RealmHelper.storeListDropDownResult(listResult);

                        return ApiUtils.getRestServices(accessToken).getDropDownReason(createReasonBody());
                    }
                })
                .flatMap(new Function<List<DropDownReason>, ObservableSource<List<DropDownAction>>>() {
                    @Override
                    public ObservableSource<List<DropDownAction>> apply(List<DropDownReason> listReason) throws Exception {
                        RealmHelper.deleteListDropDownReason();
                        RealmHelper.storeListDropDownReason(listReason);

                        return ApiUtils.getRestServices(accessToken).getDropDownAction(createActionBody());
                    }
                })
                .flatMap(new Function<List<DropDownAction>, ObservableSource<List<DropDownStatusAgunan>>>() {
                    @Override
                    public ObservableSource<List<DropDownStatusAgunan>> apply(List<DropDownAction> listAction) throws Exception {
                        RealmHelper.deleteListDropDownAction();
                        RealmHelper.storeListDropDownAction(listAction);

                        return ApiUtils.getRestServices(accessToken).getDropDownStatusAgunan(createStatusAgunanBody());
                    }
                })
                .flatMap(new Function<List<DropDownStatusAgunan>, ObservableSource<List<DropDownTeleponType>>>() {
                    @Override
                    public ObservableSource<List<DropDownTeleponType>> apply(List<DropDownStatusAgunan> listStatusAgunan) throws Exception {
                        RealmHelper.deleteListDropDownStatusAgunan();
                        RealmHelper.storeListDropDownStatusAgunan(listStatusAgunan);

                        return ApiUtils.getRestServices(accessToken).getDropDownTeleponType(createTeleponTypeBody());
                    }
                })
                .flatMap(new Function<List<DropDownTeleponType>, ObservableSource<List<DropDownAddressType>>>() {
                    @Override
                    public ObservableSource<List<DropDownAddressType>> apply(List<DropDownTeleponType> listTeleponType) throws Exception {
                        RealmHelper.deleteListDropDownTeleponType();
                        RealmHelper.storeListDropDownTeleponType(listTeleponType);

                        return ApiUtils.getRestServices(accessToken).getDropDownAddressType(createAddressTypeBody());
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        genericAlert.ShowLoadingAlert();
                    }
                })
                .doOnTerminate(new Action() {
                    @Override
                    public void run() throws Exception {
                        genericAlert.Dismiss();
                    }
                })
                .subscribeWith(new DisposableObserver<List<DropDownAddressType>>() {
                    @Override
                    public void onNext(List<DropDownAddressType> listAddressType) {
                        RealmHelper.deleteListDropDownAddressType();
                        RealmHelper.storeListDropDownAddressType(listAddressType);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("SplashActivity", e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });

        composites.add(disposable);
    }

    private FormVisitDropDownBody createPurposeBody() {
        Sort sort = new Sort();
        sort.setProperty(RestConstants.DROP_DOWN_PURPOSE_SORT_PROPERTY);
        sort.setDirection(RestConstants.ORDER_DIRECTION_ASC_VALUE);

        List<Sort> listSort = new ArrayList<>();
        listSort.add(sort);

        DbParam dbParam = new DbParam();
        dbParam.setPage(1);
        dbParam.setLimit(50);
        dbParam.setSort(listSort);

        FormVisitDropDownBody formVisitDropDownBody = new FormVisitDropDownBody();
        formVisitDropDownBody.setDatabaseId(RestConstants.DATABASE_ID_VALUE);
        formVisitDropDownBody.setViewName(RestConstants.DROP_DOWN_PURPOSE_VIEW_NAME);
        formVisitDropDownBody.setdBParam(dbParam);

        return formVisitDropDownBody;
    }

    private FormVisitDropDownBody createRelationshipBody() {
        Sort sort = new Sort();
        sort.setProperty(RestConstants.DROP_DOWN_RELATIONSHIP_SORT_PROPERTY);
        sort.setDirection(RestConstants.ORDER_DIRECTION_ASC_VALUE);

        List<Sort> listSort = new ArrayList<>();
        listSort.add(sort);

        DbParam dbParam = new DbParam();
        dbParam.setPage(1);
        dbParam.setLimit(50);
        dbParam.setSort(listSort);

        FormVisitDropDownBody formVisitDropDownBody = new FormVisitDropDownBody();
        formVisitDropDownBody.setDatabaseId(RestConstants.DATABASE_ID_VALUE);
        formVisitDropDownBody.setViewName(RestConstants.DROP_DOWN_RELATIONSHIP_VIEW_NAME);
        formVisitDropDownBody.setdBParam(dbParam);

        return formVisitDropDownBody;
    }

    private FormVisitDropDownBody createResultBody() {
        Sort sort = new Sort();
        sort.setProperty(RestConstants.DROP_DOWN_RESULT_SORT_PROPERTY);
        sort.setDirection(RestConstants.ORDER_DIRECTION_ASC_VALUE);

        List<Sort> listSort = new ArrayList<>();
        listSort.add(sort);

        DbParam dbParam = new DbParam();
        dbParam.setPage(1);
        dbParam.setLimit(50);
        dbParam.setSort(listSort);

        FormVisitDropDownBody formVisitDropDownBody = new FormVisitDropDownBody();
        formVisitDropDownBody.setDatabaseId(RestConstants.DATABASE_ID_VALUE);
        formVisitDropDownBody.setViewName(RestConstants.DROP_DOWN_RESULT_VIEW_NAME);
        formVisitDropDownBody.setdBParam(dbParam);

        return formVisitDropDownBody;
    }

    private FormVisitDropDownBody createReasonBody() {
        Sort sort = new Sort();
        sort.setProperty(RestConstants.DROP_DOWN_REASON_SORT_PROPERTY);
        sort.setDirection(RestConstants.ORDER_DIRECTION_ASC_VALUE);

        List<Sort> listSort = new ArrayList<>();
        listSort.add(sort);

        DbParam dbParam = new DbParam();
        dbParam.setPage(1);
        dbParam.setLimit(50);
        dbParam.setSort(listSort);

        FormVisitDropDownBody formVisitDropDownBody = new FormVisitDropDownBody();
        formVisitDropDownBody.setDatabaseId(RestConstants.DATABASE_ID_VALUE);
        formVisitDropDownBody.setViewName(RestConstants.DROP_DOWN_REASON_VIEW_NAME);
        formVisitDropDownBody.setdBParam(dbParam);

        return formVisitDropDownBody;
    }

    private FormVisitDropDownBody createActionBody() {
        Sort sort = new Sort();
        sort.setProperty(RestConstants.DROP_DOWN_ACTION_SORT_PROPERTY);
        sort.setDirection(RestConstants.ORDER_DIRECTION_ASC_VALUE);

        List<Sort> listSort = new ArrayList<>();
        listSort.add(sort);

        DbParam dbParam = new DbParam();
        dbParam.setPage(1);
        dbParam.setLimit(50);
        dbParam.setSort(listSort);

        FormVisitDropDownBody formVisitDropDownBody = new FormVisitDropDownBody();
        formVisitDropDownBody.setDatabaseId(RestConstants.DATABASE_ID_VALUE);
        formVisitDropDownBody.setViewName(RestConstants.DROP_DOWN_ACTION_VIEW_NAME);
        formVisitDropDownBody.setdBParam(dbParam);

        return formVisitDropDownBody;
    }

    private FormVisitDropDownBody createStatusAgunanBody() {
        Sort sort = new Sort();
        sort.setProperty(RestConstants.DROP_DOWN_STATUS_AGUNAN_SORT_PROPERTY);
        sort.setDirection(RestConstants.ORDER_DIRECTION_ASC_VALUE);

        List<Sort> listSort = new ArrayList<>();
        listSort.add(sort);

        DbParam dbParam = new DbParam();
        dbParam.setPage(1);
        dbParam.setLimit(50);
        dbParam.setSort(listSort);

        FormVisitDropDownBody formVisitDropDownBody = new FormVisitDropDownBody();
        formVisitDropDownBody.setDatabaseId(RestConstants.DATABASE_ID_VALUE);
        formVisitDropDownBody.setViewName(RestConstants.DROP_DOWN_STATUS_AGUNAN_VIEW_NAME);
        formVisitDropDownBody.setdBParam(dbParam);

        return formVisitDropDownBody;
    }

    private FormVisitDropDownBody createTeleponTypeBody() {
        Sort sort = new Sort();
        sort.setProperty(RestConstants.DROP_DOWN_TELEPON_TYPE_SORT_PROPERTY);
        sort.setDirection(RestConstants.ORDER_DIRECTION_ASC_VALUE);

        List<Sort> listSort = new ArrayList<>();
        listSort.add(sort);

        DbParam dbParam = new DbParam();
        dbParam.setPage(1);
        dbParam.setLimit(50);
        dbParam.setSort(listSort);

        FormVisitDropDownBody formVisitDropDownBody = new FormVisitDropDownBody();
        formVisitDropDownBody.setDatabaseId(RestConstants.DATABASE_ID_VALUE);
        formVisitDropDownBody.setViewName(RestConstants.DROP_DOWN_TELEPON_TYPE_VIEW_NAME);
        formVisitDropDownBody.setdBParam(dbParam);

        return formVisitDropDownBody;
    }

    private FormVisitDropDownBody createAddressTypeBody() {
        Sort sort = new Sort();
        sort.setProperty(RestConstants.DROP_DOWN_ADDRESS_TYPE_SORT_PROPERTY);
        sort.setDirection(RestConstants.ORDER_DIRECTION_ASC_VALUE);

        List<Sort> listSort = new ArrayList<>();
        listSort.add(sort);

        DbParam dbParam = new DbParam();
        dbParam.setPage(1);
        dbParam.setLimit(50);
        dbParam.setSort(listSort);

        FormVisitDropDownBody formVisitDropDownBody = new FormVisitDropDownBody();
        formVisitDropDownBody.setDatabaseId(RestConstants.DATABASE_ID_VALUE);
        formVisitDropDownBody.setViewName(RestConstants.DROP_DOWN_ADDRESS_TYPE_VIEW_NAME);
        formVisitDropDownBody.setdBParam(dbParam);

        return formVisitDropDownBody;
    }

    private void initLocation() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mResultReceiver = new AddressResultReceiver(new Handler());
        mAddressOutput = "";
        mAddressRequested = false;
    }

    // Create a GoogleApiClient instance
    private void initGoogleService() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */,
                        this /* OnConnectionFailedListener */)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private static final int REQ_CODE_LOCATION_PERMISSION = 1;
    private void requestAccessLocationPermission() {
        if (!isLocationPermissionGranted()) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQ_CODE_LOCATION_PERMISSION);
        } else {
            getLastKnownLocation();
        }
    }

    private boolean isLocationPermissionGranted() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQ_CODE_LOCATION_PERMISSION) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                requestAccessLocationPermission();
            } else {
//                showLocationAndPopup(CENTRAL_JAKATAR_LAT, CENTRAL_JAKATAR_LONG, false);
//                if not get location permission
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void getLastKnownLocation() {
        try {
            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                mLastKnownLocation = location;

//                                Get Address string
                                startIntentService(location);
                                mAddressRequested = true;
                            } else {
                                genericAlert.Dismiss();
                            }
                        }
                    });
        } catch (SecurityException e) {
            genericAlert.Dismiss();
        }
    }

    private void startIntentService(Location location) {
        // Create an intent for passing to the intent service responsible for fetching the address.
        Intent intent = new Intent(this, FetchAddressIntentService.class);

        // Pass the result receiver as an extra to the service.
        intent.putExtra(FetchAddressIntentService.Constants.RECEIVER, mResultReceiver);

        // Pass the location data as an extra to the service.
        intent.putExtra(FetchAddressIntentService.Constants.LOCATION_DATA_EXTRA, location);

        // Start the service. If the service isn't already running, it is instantiated and started
        // (creating a process for it if needed); if it is running then it remains running. The
        // service kills itself automatically once all intents are processed.
        startService(intent);
    }

    private class AddressResultReceiver extends ResultReceiver {
        AddressResultReceiver(Handler handler) {
            super(handler);
        }

        /**
         *  Receives data sent from FetchAddressIntentService and updates the UI in Activity.
         */
        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {

            // Display the address string or an error message sent from the intent service.
            mAddressOutput = resultData.getString(FetchAddressIntentService.Constants.RESULT_DATA_KEY);

            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(HomeActivity.this);
            String key_UserID = getString(R.string.SharedPreferenceKey_UserID);
            String userId = sharedPreferences.getString(key_UserID, "");

            checkIn(userId, mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude(), mAddressOutput);

            // Reset. Enable the Fetch Address button and stop showing the progress bar.
            mAddressRequested = false;
        }
    }

    public void checkIn(String userId, double latitude, double longitude, String address) {
        String key_AuthToken = getString(R.string.SharedPreferenceKey_AccessToken);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String accessToken =  sharedPreferences.getString(key_AuthToken, "");

        CheckInBody checkInBody = new CheckInBody();
        checkInBody.setDatabaseId(RestConstants.DATABASE_ID_VALUE);
        checkInBody.setSpName(RestConstants.CHECK_IN_SP_NAME);

        CheckInBody.SpParameter spParameter = new CheckInBody.SpParameter();
        spParameter.setUserID(userId);
        spParameter.setLatitude(latitude);
        spParameter.setLongitude(longitude);
        spParameter.setAddress(address);

        checkInBody.setSpParameter(spParameter);

        Disposable disposable = ApiUtils.getRestServices(accessToken).checkIn(checkInBody)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        genericAlert.ShowLoadingAlert();
                    }
                })
                .doOnTerminate(new Action() {
                    @Override
                    public void run() throws Exception {
                        genericAlert.Dismiss();
                    }
                })
                .subscribeWith(new DisposableObserver<List<CheckInResponse>>() {
                    @Override
                    public void onNext(List<CheckInResponse> listCheckInResponse) {
                        ToastUtils.toastShort(HomeActivity.this, getString(R.string.DetailDebitur_SuksesCheckIn));
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e("checkIn " + e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });

        composites.add(disposable);
    }

    private void initBackgroundService() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String key_AuthToken = getString(R.string.SharedPreferenceKey_AccessToken);
        String accessToken =  sharedPreferences.getString(key_AuthToken, "");
        String key_UserID = getString(R.string.SharedPreferenceKey_UserID);
        String userId = sharedPreferences.getString(key_UserID, "");

        Bundle extra = new Bundle();
        extra.putString(MyJobService.ACCESS_TOKEN, accessToken);
        extra.putString(MyJobService.USER_ID, userId);

        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher (new GooglePlayDriver(this));

        Job myJob = dispatcher.newJobBuilder()
                // the JobService that will be called
                .setService(MyJobService.class)
                // uniquely identifies the job
                .setTag("MyJobService")
                // recurring
                .setRecurring(true)
                // don't persist past a device reboot
                .setLifetime(Lifetime.FOREVER)
                // start between 0 and 60 seconds from now
                .setTrigger(Trigger.executionWindow(3200, 3600))
                // don't overwrite an existing job with the same tag
                .setReplaceCurrent(true)
                // retry with exponential backoff
                .setRetryStrategy(RetryStrategy.DEFAULT_EXPONENTIAL)
                // constraints that need to be satisfied for the job to run
                .setConstraints(
                        //Run this job only when the network is available.
                        Constraint.ON_ANY_NETWORK
                )
                .setExtras(extra)
                .build();

        dispatcher.mustSchedule(myJob);

        Job myJobTracking = dispatcher.newJobBuilder()
                // the JobService that will be called
                .setService(MyJobServiceTracking.class)
                // uniquely identifies the job
                .setTag("MyJobServiceTracking")
                // recurring
                .setRecurring(true)
                // don't persist past a device reboot
                .setLifetime(Lifetime.FOREVER)
                // start between 0 and 60 seconds from now
                .setTrigger(Trigger.executionWindow(800, 900))
                // don't overwrite an existing job with the same tag
                .setReplaceCurrent(true)
                // retry with exponential backoff
                .setRetryStrategy(RetryStrategy.DEFAULT_EXPONENTIAL)
                // constraints that need to be satisfied for the job to run
                .setConstraints(
                        //Run this job only when the network is available.
                        Constraint.ON_ANY_NETWORK
                )
                .setExtras(extra)
                .build();

        dispatcher.mustSchedule(myJobTracking);
    }
}
