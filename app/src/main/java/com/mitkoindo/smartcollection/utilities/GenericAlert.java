package com.mitkoindo.adsjalan.supportview;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

import com.mitkoindo.adsjalan.R;

public class GenericAlert
{
    //popup window
    private AlertDialog popup;

    //Activity data
    private Activity context;

    //Constructor
    public GenericAlert(Activity context)
    {
        this.context = context;
    }

    //display alert tanpa button
    public void ShowAlertOnly(String message)
    {
        Dismiss();

        //create pop-up window
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        //set the message
        builder.setMessage(message);
        builder.setCancelable(false);

        //create the dialogue
        popup = builder.create();
        popup.show();
    }

    //Display alert
    public void DisplayAlert(String message, String title)
    {
        Dismiss();

        //create pop-up window
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);

        //set the message
        if (title.isEmpty())
        {
            builder.setMessage(message);
        }
        else
        {
            builder.setMessage(message).setTitle(title);
        }

        //set the no button
        builder.setNegativeButton(R.string.Text_OK, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                //dismiss the dialogue

            }
        });

        //create the dialogue
        popup = builder.create();

        popup.show();
    }

    //display alert tanpa button
    public void DisplayAlertWithoutButton(String message, String title)
    {
        Dismiss();

        //create pop-up window
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);

        //set the message
        if (title.isEmpty())
        {
            builder.setMessage(message);
        }
        else
        {
            builder.setMessage(message).setTitle(title);
        }

        //create the dialogue
        popup = builder.create();

        popup.show();
    }

    //show loading alert
    public void ShowLoadingAlert()
    {
        Dismiss();

        //create pop-up window
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(R.layout.popup_loading);
        builder.setCancelable(false);

        //create the dialogue
        popup = builder.create();
        popup.show();
    }

    //display sign in prompt
    public void DisplaySignInPrompt(String message)
    {
        Dismiss();

        //create pop-up window
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        //set the message
        builder.setMessage(message).setTitle("Sorry");
        builder.setCancelable(false);

        builder.setNegativeButton(R.string.Text_OK, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                //dismiss the dialogue

            }
        });

        //create the dialogue
        popup = builder.create();


        popup.show();
    }

    //dismiss alert
    public void Dismiss()
    {
        if (popup != null && popup.isShowing())
        {
            popup.dismiss();
        }
    }
}
