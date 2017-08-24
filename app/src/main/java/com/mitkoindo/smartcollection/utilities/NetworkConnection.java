package com.mitkoindo.smartcollection.utilities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class NetworkConnection
{
    //property koneksi
    private String accessToken, phoneSpec;

    //request object
    private JSONObject requestObject;

    //set koneksi
    public NetworkConnection(String accessToken, String phonespec)
    {
        this.accessToken = accessToken;
        this.phoneSpec = phonespec;
    }

    //set request object
    public void SetRequestObject(JSONObject requestObject)
    {
        this.requestObject = requestObject;
    }

    //Send post request
    public String SendPostRequest(String url_String)
    {
        StringBuilder sb = new StringBuilder();

        String serverResponse = "";

        try
        {
            URL url = null;
            url = new URL(url_String);
            if (url.openConnection() instanceof HttpsURLConnection)
            {
                HttpsURLConnection httpURLConnection = (HttpsURLConnection) url.openConnection();
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setReadTimeout(60000);
                httpURLConnection.setRequestProperty("Content-Type","application/json");
                if (accessToken != null && !accessToken.isEmpty())
                {
                    if (accessToken.contains("Bearer"))
                    {
                        httpURLConnection.setRequestProperty("Authorization", accessToken);
                    }
                    else
                    {
                        httpURLConnection.setRequestProperty("Authorization", "Bearer " + accessToken);
                    }
                }
                httpURLConnection.setRequestProperty("PhoneSpec", phoneSpec);
                httpURLConnection.connect();

                OutputStreamWriter out = new OutputStreamWriter(httpURLConnection.getOutputStream());
                out.write(requestObject.toString());
                out.close();

                int HttpResult = httpURLConnection.getResponseCode();

                //cek jika resultcode nggak 200
                switch (HttpResult)
                {
                    case HttpsURLConnection.HTTP_BAD_REQUEST : //400
                        return "Bad Request";
                    case HttpsURLConnection.HTTP_UNAUTHORIZED : //401
                        return "Unauthorized";
                    case HttpsURLConnection.HTTP_FORBIDDEN : //403
                        return "Forbidden";
                    case HttpsURLConnection.HTTP_NOT_FOUND : //404
                        return "Not Found";
                    case HttpsURLConnection.HTTP_INTERNAL_ERROR : //500
                        return "Internal Server Error";
                    default:break;
                }

                //cek resultcode, jika 503, return maintenance
                if (HttpResult == HttpURLConnection.HTTP_UNAVAILABLE)
                    return "Maintenance";

                if (HttpResult == HttpURLConnection.HTTP_OK)
                {
                    BufferedReader br = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(),"utf-8"));
                    String line = null;
                    while ((line = br.readLine()) != null)
                    {
                        sb.append(line + "\n");
                    }
                    br.close();

                    serverResponse = ""+sb.toString();
                }
            }
            else if (url.openConnection() instanceof HttpURLConnection)
            {
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setReadTimeout(60000);
                httpURLConnection.setRequestProperty("Content-Type","application/json");
                if (accessToken != null && !accessToken.isEmpty())
                {
                    if (accessToken.contains("Bearer"))
                    {
                        httpURLConnection.setRequestProperty("Authorization", accessToken);
                    }
                    else
                    {
                        httpURLConnection.setRequestProperty("Authorization", "Bearer " + accessToken);
                    }
                }
                httpURLConnection.setRequestProperty("PhoneSpec", phoneSpec);
                httpURLConnection.connect();

                OutputStreamWriter out = new OutputStreamWriter(httpURLConnection.getOutputStream());
                out.write(requestObject.toString());
                out.close();

                int HttpResult = httpURLConnection.getResponseCode();

                //cek jika resultcode nggak 200
                switch (HttpResult)
                {
                    case HttpsURLConnection.HTTP_BAD_REQUEST : //400
                        return "Bad Request";
                    case HttpsURLConnection.HTTP_UNAUTHORIZED : //401
                        return "Unauthorized";
                    case HttpsURLConnection.HTTP_FORBIDDEN : //403
                        return "Forbidden";
                    case HttpsURLConnection.HTTP_NOT_FOUND : //404
                        return "Not Found";
                    case HttpsURLConnection.HTTP_INTERNAL_ERROR : //500
                        return "Internal Server Error";
                    default:break;
                }

                //cek resultcode, jika 503, return maintenance
                if (HttpResult == HttpURLConnection.HTTP_UNAVAILABLE)
                    return "Maintenance";

                if (HttpResult == HttpURLConnection.HTTP_OK)
                {
                    BufferedReader br = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(),"utf-8"));
                    String line = null;
                    while ((line = br.readLine()) != null)
                    {
                        sb.append(line + "\n");
                    }
                    br.close();

                    serverResponse = ""+sb.toString();
                }
            }

        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return serverResponse;
    }

    //send get request
    public String SendGetRequest(String url_String)
    {
        StringBuilder sb = new StringBuilder();

        String serverResponse = "";

        try
        {
            URL url = null;
            url = new URL(url_String);
            HttpsURLConnection httpURLConnection = (HttpsURLConnection) url.openConnection();
            httpURLConnection.setDoOutput(false);
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setConnectTimeout(5000);
            httpURLConnection.setReadTimeout(60000);
            httpURLConnection.setRequestProperty("Content-Type","application/json");
            if (accessToken != null && !accessToken.isEmpty())
                httpURLConnection.setRequestProperty("Authorization", "Bearer " + accessToken);
            httpURLConnection.setRequestProperty("PhoneSpec", phoneSpec);
            httpURLConnection.connect();

            int HttpResult = httpURLConnection.getResponseCode();

            //cek jika resultcode nggak 200
            switch (HttpResult)
            {
                case HttpsURLConnection.HTTP_BAD_REQUEST : //400
                    return "Bad Request";
                case HttpsURLConnection.HTTP_UNAUTHORIZED : //401
                    return "Unauthorized";
                case HttpsURLConnection.HTTP_FORBIDDEN : //403
                    return "Forbidden";
                case HttpsURLConnection.HTTP_NOT_FOUND : //404
                    return "Not Found";
                case HttpsURLConnection.HTTP_INTERNAL_ERROR : //500
                    return "Internal Server Error";
                default:break;
            }

            //cek resultcode, jika 503, return maintenance
            if (HttpResult == HttpURLConnection.HTTP_UNAVAILABLE)
                return "Maintenance";

            if (HttpResult == HttpURLConnection.HTTP_OK)
            {
                BufferedReader br = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(),"utf-8"));
                String line = null;
                while ((line = br.readLine()) != null)
                {
                    sb.append(line + "\n");
                }
                br.close();

                serverResponse = ""+sb.toString();
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return serverResponse;
    }

    //send request buat download image
    public Bitmap SendDownloadImageRequest(String imageUrl)
    {
        Bitmap resultImage = null;

        try
        {
            URL url = new URL(imageUrl);
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.connect();
            int responseCode = connection.getResponseCode();

            if (responseCode == 200)
            {
                //give image data
                resultImage = BitmapFactory.decodeStream(connection.getInputStream());
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return resultImage;
    }

    //Send put request
    public String SendPutRequest(String url_String)
    {
        StringBuilder sb = new StringBuilder();

        String serverResponse = "";

        try
        {
            URL url = null;
            url = new URL(url_String);
            if (url.openConnection() instanceof  HttpsURLConnection)
            {
                HttpsURLConnection httpURLConnection = (HttpsURLConnection) url.openConnection();
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setRequestMethod("PUT");
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setReadTimeout(60000);
                httpURLConnection.setRequestProperty("Content-Type","application/json");
                if (accessToken != null && !accessToken.isEmpty())
                {
                    if (accessToken.contains("Bearer"))
                    {
                        httpURLConnection.setRequestProperty("Authorization", accessToken);
                    }
                    else
                    {
                        httpURLConnection.setRequestProperty("Authorization", "Bearer " + accessToken);
                    }
                }
                httpURLConnection.setRequestProperty("PhoneSpec", phoneSpec);
                httpURLConnection.connect();

                OutputStreamWriter out = new OutputStreamWriter(httpURLConnection.getOutputStream());
                out.write(requestObject.toString());
                out.close();

                int HttpResult = httpURLConnection.getResponseCode();

                //cek jika resultcode nggak 200
                switch (HttpResult)
                {
                    case HttpsURLConnection.HTTP_NO_CONTENT : //204
                        return "204"; //Sukses, tapi balesannya emang no content
                    case HttpsURLConnection.HTTP_BAD_REQUEST : //400
                        return "Bad Request";
                    case HttpsURLConnection.HTTP_UNAUTHORIZED : //401
                        return "Unauthorized";
                    case HttpsURLConnection.HTTP_FORBIDDEN : //403
                        return "Forbidden";
                    /*case HttpsURLConnection.HTTP_NOT_FOUND : //404
                        return "Not Found";*/
                    case HttpsURLConnection.HTTP_INTERNAL_ERROR : //500
                        return "Internal Server Error";
                    default:break;
                }

                //the only way to reach this line is if the result is 404
                BufferedReader br = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(),"utf-8"));
                String line = null;
                while ((line = br.readLine()) != null)
                {
                    sb.append(line + "\n");
                }
                br.close();

                serverResponse = ""+sb.toString();

                //cek resultcode, jika 503, return maintenance
                /*if (HttpResult == HttpURLConnection.HTTP_UNAVAILABLE)
                    return "Maintenance";

                if (HttpResult == HttpURLConnection.HTTP_OK)
                {
                    BufferedReader br = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(),"utf-8"));
                    String line = null;
                    while ((line = br.readLine()) != null)
                    {
                        sb.append(line + "\n");
                    }
                    br.close();

                    serverResponse = ""+sb.toString();
                }*/
            }
            else if (url.openConnection() instanceof HttpURLConnection)
            {
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setRequestMethod("PUT");
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setReadTimeout(60000);
                httpURLConnection.setRequestProperty("Content-Type","application/json");
                if (accessToken != null && !accessToken.isEmpty())
                {
                    if (accessToken.contains("Bearer"))
                    {
                        httpURLConnection.setRequestProperty("Authorization", accessToken);
                    }
                    else
                    {
                        httpURLConnection.setRequestProperty("Authorization", "Bearer " + accessToken);
                    }
                }
                httpURLConnection.setRequestProperty("PhoneSpec", phoneSpec);
                httpURLConnection.connect();

                OutputStreamWriter out = new OutputStreamWriter(httpURLConnection.getOutputStream());
                out.write(requestObject.toString());
                out.close();

                int HttpResult = httpURLConnection.getResponseCode();

                /*serverResponse = Integer.toString(HttpResult);*/

                //cek jika resultcode nggak 200
                switch (HttpResult)
                {
                    case HttpURLConnection.HTTP_NO_CONTENT : //204
                        return "204";
                    case HttpURLConnection.HTTP_BAD_REQUEST : //400
                        return "Bad Request";
                    case HttpURLConnection.HTTP_UNAUTHORIZED : //401
                        return "Unauthorized";
                    case HttpURLConnection.HTTP_FORBIDDEN : //403
                        return "Forbidden";
                    /*case HttpURLConnection.HTTP_NOT_FOUND : //404
                        return "Not Found";*/
                    case HttpURLConnection.HTTP_INTERNAL_ERROR : //500
                        return "Internal Server Error";
                    default:break;
                }

                //the only way to reach this line is if the result is 404
                BufferedReader br = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(),"utf-8"));
                String line = null;
                while ((line = br.readLine()) != null)
                {
                    sb.append(line + "\n");
                }
                br.close();

                serverResponse = ""+sb.toString();

                //cek resultcode, jika 503, return maintenance
                /*if (HttpResult == HttpURLConnection.HTTP_UNAVAILABLE)
                    return "Maintenance";*/

                /*if (HttpResult == HttpURLConnection.HTTP_OK)
                {
                    BufferedReader br = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(),"utf-8"));
                    String line = null;
                    while ((line = br.readLine()) != null)
                    {
                        sb.append(line + "\n");
                    }
                    br.close();

                    serverResponse = ""+sb.toString();
                }*/

                /*
                int x = 0;
                int z = x + 1;
                */
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return serverResponse;
    }
}
