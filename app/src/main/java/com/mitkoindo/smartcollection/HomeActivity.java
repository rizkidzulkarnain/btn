package com.mitkoindo.smartcollection;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

import com.mitkoindo.smartcollection.adapter.HomeMenuAdapter;
import com.mitkoindo.smartcollection.helper.ItemClickListener;
import com.mitkoindo.smartcollection.helper.RealmHelper;
import com.mitkoindo.smartcollection.helper.ResourceLoader;
import com.mitkoindo.smartcollection.module.assignment.AccountAssignmentActivity;
import com.mitkoindo.smartcollection.module.berita.BeritaActivity;
import com.mitkoindo.smartcollection.module.dashboard.DashboardActivity;
import com.mitkoindo.smartcollection.module.debitur.listdebitur.ListDebiturActivity;
import com.mitkoindo.smartcollection.module.ptp_reminder.PTPReminderActivity;
import com.mitkoindo.smartcollection.network.ApiUtils;
import com.mitkoindo.smartcollection.network.RestConstants;
import com.mitkoindo.smartcollection.network.body.FormVisitDropDownBody;
import com.mitkoindo.smartcollection.network.models.DbParam;
import com.mitkoindo.smartcollection.network.models.Sort;
import com.mitkoindo.smartcollection.objectdata.DropDownAction;
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

import java.util.ArrayList;
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

public class HomeActivity extends AppCompatActivity
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

    //auth token
    private String authToken;


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
        GetViews();
        SetupViews();
        getDropdown();
        SetupTransaction();

        //ignore certificate
        HttpsTrustManager.allowAllSSL();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

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
        /*String[] menuTitle = getResources().getStringArray(R.array.HomeMenu);*/

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
    }

    //setup transaksi
    private void SetupTransaction()
    {
        //load url
        baseUrl = ResourceLoader.LoadBaseURL(this);
        url_Logout = getString(R.string.URL_Logout);

        //load auth token
        authToken = ResourceLoader.LoadAuthToken(this);
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
                break;
            case "Penugasan" :
                intent = ListDebiturActivity.instantiate(this, ListDebiturActivity.EXTRA_TYPE_PENUGASAN_VALUE);
                break;
            case "Tambah Kontak" :
                intent = ListDebiturActivity.instantiate(this, ListDebiturActivity.EXTRA_TYPE_TAMBAH_KONTAK_VALUE);
                break;
            case "Account Assignment" :
                intent = new Intent(this, AccountAssignmentActivity.class);
                break;
            case "PTP Reminder" :
                intent = new Intent(this, PTPReminderActivity.class);
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
                .subscribeWith(new DisposableObserver<List<DropDownTeleponType>>() {
                    @Override
                    public void onNext(List<DropDownTeleponType> listTeleponType) {
                        RealmHelper.deleteListDropDownTeleponType();
                        RealmHelper.storeListDropDownTeleponType(listTeleponType);
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
        dbParam.setLimit(20);
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
        dbParam.setLimit(20);
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
        dbParam.setLimit(20);
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
        dbParam.setLimit(20);
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
        dbParam.setLimit(20);
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
        dbParam.setLimit(20);
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
        dbParam.setLimit(20);
        dbParam.setSort(listSort);

        FormVisitDropDownBody formVisitDropDownBody = new FormVisitDropDownBody();
        formVisitDropDownBody.setDatabaseId(RestConstants.DATABASE_ID_VALUE);
        formVisitDropDownBody.setViewName(RestConstants.DROP_DOWN_TELEPON_TYPE_VIEW_NAME);
        formVisitDropDownBody.setdBParam(dbParam);

        return formVisitDropDownBody;
    }
}
