package com.mitkoindo.smartcollection;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.mitkoindo.smartcollection.helper.RealmHelper;
import com.mitkoindo.smartcollection.helper.ResourceLoader;
import com.mitkoindo.smartcollection.module.misc.ChangeBaseURLActivity;
import com.mitkoindo.smartcollection.network.ApiUtils;
import com.mitkoindo.smartcollection.network.RestConstants;
import com.mitkoindo.smartcollection.network.body.LoginBody;
import com.mitkoindo.smartcollection.network.response.LoginResponse;
import com.mitkoindo.smartcollection.network.response.OfflineBundleResponse;
import com.mitkoindo.smartcollection.objectdata.DebiturItem;
import com.mitkoindo.smartcollection.objectdata.DetailDebitur;
import com.mitkoindo.smartcollection.objectdata.DropDownAddress;
import com.mitkoindo.smartcollection.objectdata.DropDownAddressDb;
import com.mitkoindo.smartcollection.objectdata.PhoneNumber;
import com.mitkoindo.smartcollection.objectdata.databasemodel.DebiturItemDb;
import com.mitkoindo.smartcollection.objectdata.databasemodel.DetailDebiturDb;
import com.mitkoindo.smartcollection.objectdata.databasemodel.PhoneNumberDb;
import com.mitkoindo.smartcollection.utilities.GenericAlert;
import com.mitkoindo.smartcollection.utilities.HttpsTrustManager;
import com.mitkoindo.smartcollection.utilities.LoginEncryption;
import com.mitkoindo.smartcollection.utilities.NetworkConnection;
import com.mitkoindo.smartcollection.utils.ProfileUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class LoginActivity extends AppCompatActivity
{
    private CompositeDisposable mComposites = new CompositeDisposable();

    //----------------------------------------------------------------------------------------------
    //  Views
    //----------------------------------------------------------------------------------------------
    //checkbox buat remember password
    /*private Switch view_RememberMe;*/

    //username & password field
    private EditText form_Username;
    private EditText form_Password;

    //text dari form username & password
    private String text_Username;
    private String text_Password;

    //generic alert dialog
    private GenericAlert genericAlert;

    //----------------------------------------------------------------------------------------------
    //  Transaksi
    //----------------------------------------------------------------------------------------------
    //base url
    private String baseURL;
    private String url_Login;

    //request code buat ganti base url
    private static final int REQUESTCODE_CHANGEURL = 850;

    //----------------------------------------------------------------------------------------------
    //  Setup
    //----------------------------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        GetViews();
        SetupURL();

        //ignore certificate
        HttpsTrustManager.allowAllSSL();
    }

    @Override
    protected void onDestroy() {
        mComposites.clear();

        super.onDestroy();
    }

    //get views
    private void GetViews()
    {
        /*view_RememberMe = findViewById(R.id.LoginActivity_RememberMe);*/

        //get form username & password
        form_Username = findViewById(R.id.LoginActivity_Username);
        form_Password = findViewById(R.id.LoginActivity_Password);

        //create alert dialog
        genericAlert = new GenericAlert(this);
    }

    //setup URL
    private void SetupURL()
    {
        baseURL = ResourceLoader.LoadBaseURL(this);
        url_Login = getString(R.string.URL_Login);
    }

    //----------------------------------------------------------------------------------------------
    //  Handle input
    //----------------------------------------------------------------------------------------------
    //handle input di login button
    public void HandleInput_Login_LoginButton(View view)
    {
        //test start main activity
        /*Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();*/

        //test start update password
        /*Intent intent = new Intent(this, UpdatePasswordActivity.class);
        startActivity(intent);
        finish();*/

        //test home screen
        /*Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();*/

        //test histori tindakan
        /*Intent intent = new Intent(this, HistoriTindakanActivity.class);
        startActivity(intent);
        finish();*/

        //get valuenya username & password
        text_Username = form_Username.getText().toString();
        text_Password = form_Password.getText().toString();

        //cek apakah value username / password ada yang kosong
        if (text_Username.isEmpty() || text_Password.isEmpty())
        {
            //show alert bahwa username / password belum diisi
            String alertMessage = getString(R.string.Login_Alert_UsernameOrPasswordEmpty);
            genericAlert.DisplayAlert(alertMessage, "");
            return;
        }

        //encrypt username & password
        LoginEncryption loginEncryption = new LoginEncryption();
        try
        {
            text_Username = loginEncryption.encryptWithDefaultKey(text_Username);
            text_Password = loginEncryption.encryptWithDefaultKey(text_Password);
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        catch (InvalidKeyException e)
        {
            e.printStackTrace();
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        catch (NoSuchPaddingException e)
        {
            e.printStackTrace();
        }
        catch (InvalidAlgorithmParameterException e)
        {
            e.printStackTrace();
        }
        catch (IllegalBlockSizeException e)
        {
            e.printStackTrace();
        }
        catch (BadPaddingException e)
        {
            e.printStackTrace();
        }

        //show loading alert
        genericAlert.ShowLoadingAlert();

        //execute request
        new SendLoginRequest().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "");
//        login(text_Username, text_Password);
    }

    //toggle switch button
    public void HandleInput_Login_ToggleSwitch(View view)
    {
        /*//get switch state
        if (view_RememberMe.isChecked())
            view_RememberMe.setChecked(false);
        else
            view_RememberMe.setChecked(true);*/
    }

    //secret feature, change base url
    public void HandleInput_Login_ChangeBaseURL(View view)
    {
        //open change base url activity
        Intent intent = new Intent(this, ChangeBaseURLActivity.class);
        startActivityForResult(intent, REQUESTCODE_CHANGEURL);
    }

    //----------------------------------------------------------------------------------------------
    //  Create login request
    //----------------------------------------------------------------------------------------------
    //create request
    private class SendLoginRequest extends AsyncTask<String, Void, String>
    {
        @Override
        protected String doInBackground(String... strings)
        {
            //create request object
            JSONObject requestObject = CreateLoginRequestObject();

            //set url
            String usedURL = baseURL + url_Login;

            //create network connection
            NetworkConnection networkConnection = new NetworkConnection("", "");
            networkConnection.SetRequestObject(requestObject);
            /*return networkConnection.SendPostRequest(usedURL);*/
            return networkConnection.SendPlainPostRequest(usedURL);
        }

        @Override
        protected void onPostExecute(String s)
        {
            super.onPostExecute(s);
            HandleLoginResult(s);
        }
    }

    //create login request
    private JSONObject CreateLoginRequestObject()
    {
        JSONObject requestObject = new JSONObject();
        try
        {
            requestObject.put("UserID", text_Username);
            requestObject.put("Password", text_Password);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        return requestObject;
    }

    //handle login result
    private void HandleLoginResult(String resultString)
    {
        //dismiss loading alert
        genericAlert.Dismiss();

        try
        {
            //get access token
            JSONObject resultObject = new JSONObject(resultString);

            //get token & user properties
            String userID = resultObject.getString("USERID");
            String accessToken = resultObject.getString("AccessToken");
            String userName = resultObject.getString("SU_FULLNAME");
            String userGroup = resultObject.getString("SG_GRPNAME");
            String userGroupID = resultObject.getString("GROUPID");

            //cek apakah user group ini berhak menggunakan app atau tidak
            if (!AllowAccessToApp(userGroupID))
            {
                //send request untuk melogout user ini
                CreateLogoutRequest(accessToken);

                //show alert bahwa user group ini tidak memiliki akses ke dalam app ini
                String alertTitle = getString(R.string.Text_MohonMaaf);
                String alertMessage = getString(R.string.Login_Alert_UserCannotAccess);
                genericAlert.DisplayAlert(alertMessage, alertTitle);
                return;
            }

            //simpan access token di shared preference
            SaveToken(accessToken);
            SaveUserID(userID);
            SaveUserNameAndGroup(userName, userGroup, userGroupID);

            // Get Data Bundle
            getBundle();
        }
        catch (JSONException e)
        {
            e.printStackTrace();

            //show alert
            genericAlert.DisplayAlert(resultString, "");
        }
    }

    //----------------------------------------------------------------------------------------------
    //  Save property
    //----------------------------------------------------------------------------------------------
    private void SaveToken(String authToken)
    {
        //get preference name & key
        String authTokenKey = getString(R.string.SharedPreferenceKey_AccessToken);

        //save access token
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(authTokenKey, authToken);
        editor.apply();
    }

    //save user ID
    private void SaveUserID(String userID)
    {
        //get user id key
        String userIDKey = getString(R.string.SharedPreferenceKey_UserID);

        //save user ID
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(userIDKey, userID);
        editor.apply();
    }

    //save user name
    private void SaveUserNameAndGroup(String userName, String userGroup, String userGroupID)
    {
        //get user id key & group key
        String userNameKey = getString(R.string.SharedPreferenceKey_NamaPetugas);
        String userGroupKey = getString(R.string.SharedPreferenceKey_UserGroup);
        String userGroupIDKey = getString(R.string.SharedPreferenceKey_UserGroupID);

        //save user ID
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(userNameKey, userName);
        editor.putString(userGroupKey, userGroup);
        editor.putString(userGroupIDKey, userGroupID);
        editor.apply();
    }

    //----------------------------------------------------------------------------------------------
    //  Cek apakah group user ini boleh menggunakan aplikasi ini atau tidak
    //----------------------------------------------------------------------------------------------
    private boolean AllowAccessToApp(String currentUserGroup)
    {
        final String userGroup_Staff1 = getString(R.string.UserGroup_Staff1);
        final String userGroup_Staff2 = getString(R.string.UserGroup_Staff2);
        final String userGroup_Staff3 = getString(R.string.UserGroup_Staff3);
        final String userGroup_Supervisor1 = getString(R.string.UserGroup_Supervisor1);
        final String userGroup_Supervisor2 = getString(R.string.UserGroup_Supervisor2);
        final String userGroup_BranchCoordinator = getString(R.string.UserGroup_BranchCoordinator);
        final String userGroup_BranchManager = getString(R.string.UserGroup_BranchManager);
        final String userGroup_Admin = getString(R.string.UserGroup_Admin);
        final String userGroup_SystemAdmin = getString(R.string.UserGroup_SystemAdmin);
        final String userGroup_CollectionManager = getString(R.string.UserGroup_CollectionManager);

        if (currentUserGroup.equals(userGroup_Staff1) || currentUserGroup.equals(userGroup_Staff2) || currentUserGroup.equals(userGroup_Staff3))
            return true;
        else if (currentUserGroup.equals(userGroup_Supervisor1) || currentUserGroup.equals(userGroup_Supervisor2))
            return true;
        else if (currentUserGroup.equals(userGroup_BranchCoordinator) || currentUserGroup.equals(userGroup_BranchManager) ||
                currentUserGroup.equals(userGroup_SystemAdmin) || currentUserGroup.equals(userGroup_CollectionManager))
            return true;
        else if (currentUserGroup.toLowerCase().equals(userGroup_Admin))
            return true;
        else
            return false;
    }

    //----------------------------------------------------------------------------------------------
    //  Open activity
    //----------------------------------------------------------------------------------------------
    //open home activity
    private void OpenHomeActivity()
    {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    //----------------------------------------------------------------------------------------------
    //  Logout user jika nggak ada akses
    //----------------------------------------------------------------------------------------------
    private String url_Logout;

    //create request buat logout
    private void CreateLogoutRequest(String authToken)
    {
        url_Logout = getString(R.string.URL_Logout);

        //send logout request
        new SendLogoutRequest().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, authToken);
    }

    //send request buat logout
    private class SendLogoutRequest extends AsyncTask<String, Void, String>
    {
        @Override
        protected String doInBackground(String... strings)
        {
            //get token
            String authToken = strings[0];

            //create url
            String usedURL = baseURL + url_Logout;

            //eksekusi request
            NetworkConnection networkConnection = new NetworkConnection(authToken, "");
            return networkConnection.SendPutRequest(usedURL);
        }

        @Override
        protected void onPostExecute(String s)
        {
            super.onPostExecute(s);
        }
    }

    //----------------------------------------------------------------------------------------------
    //  Handle ganti URL
    //----------------------------------------------------------------------------------------------

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode)
        {
            case REQUESTCODE_CHANGEURL :
                if (resultCode == RESULT_OK)
                    //reload data base url
                    baseURL = ResourceLoader.LoadBaseURL(this);
                break;
            default:break;
        }

        int x = 0;
        int z = x + 1;
    }

    private void login(String userName, String password) {
        String key_AuthToken = getString(R.string.SharedPreferenceKey_AccessToken);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String accessToken =  sharedPreferences.getString(key_AuthToken, "");

        LoginBody loginBody = new LoginBody();
        loginBody.setUserId(userName);
        loginBody.setPassword(password);

        ApiUtils.getRestServices(accessToken).login(loginBody)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<LoginResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(LoginResponse loginResponse) {
                        genericAlert.Dismiss();
                        SaveToken(loginResponse.getAccessToken());
                        OpenHomeActivity();
                    }

                    @Override
                    public void onError(Throwable e) {
                        genericAlert.Dismiss();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void getBundle() {

        Disposable disposable = ApiUtils.getRestServices(ProfileUtils.getAccessToken(this)).getBundle(RestConstants.DATABASE_ID_VALUE, "20")
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
                .subscribeWith(new DisposableObserver<OfflineBundleResponse>() {
                    @Override
                    public void onNext(OfflineBundleResponse offlineBundleResponse) {
                        Timber.i("___getBundle success");

                        List<DebiturItemDb> debiturItemDbList = new ArrayList<DebiturItemDb>();
                        for (DebiturItem debiturItem : offlineBundleResponse.getDebiturList()) {
                            debiturItemDbList.add(new DebiturItemDb(debiturItem));
                        }
                        RealmHelper.deleteListDebiturItem();
                        RealmHelper.storeListDebiturItem(debiturItemDbList);

                        List<DetailDebiturDb> detailDebiturDbList = new ArrayList<DetailDebiturDb>();
                        for (DetailDebitur detailDebitur : offlineBundleResponse.getDebiturDetailList()) {
                            detailDebiturDbList.add(new DetailDebiturDb(detailDebitur));
                        }
                        RealmHelper.deleteListDetailDebitur();
                        RealmHelper.storeListDetailDebitur(detailDebiturDbList);

                        List<PhoneNumberDb> phoneNumberDbList = new ArrayList<PhoneNumberDb>();
                        for (PhoneNumber phoneNumber : offlineBundleResponse.getDebiturPhoneList()) {
                            phoneNumberDbList.add(new PhoneNumberDb(phoneNumber));
                        }
                        RealmHelper.deleteListPhoneNumber();
                        RealmHelper.storeListPhoneNumber(phoneNumberDbList);

                        List<DropDownAddressDb> dropDownAddressDbList = new ArrayList<DropDownAddressDb>();
                        for (DropDownAddress downAddress : offlineBundleResponse.getDebiturAddressList()) {
                            dropDownAddressDbList.add(new DropDownAddressDb(downAddress));
                        }
                        RealmHelper.deleteListDropDownAddress();
                        RealmHelper.storeListAddress(dropDownAddressDbList);

                        //open home activity
                        OpenHomeActivity();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e("___getBundle Error");

                        //open home activity
                        OpenHomeActivity();
                    }

                    @Override
                    public void onComplete() {

                    }
                });

        mComposites.add(disposable);
    }
}
