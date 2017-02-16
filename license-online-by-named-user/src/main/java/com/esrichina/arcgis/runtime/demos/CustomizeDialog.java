package com.esrichina.arcgis.runtime.demos;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.LayoutInflaterFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by Administrator on 2017/2/16.
 */

public class CustomizeDialog extends Dialog {
    private Activity context;
    private View.OnClickListener mClickListener;
    private EditText etPortalURL;
    private EditText etUsername;
    private EditText etPassword;
    private Button btnContinue;
    private Button btnCancel;

    public CustomizeDialog(Activity context) {
        super(context);
        this.context = context;
    }
    public CustomizeDialog(Activity context, View.OnClickListener clickListener) {
        super(context);
        this.context = context;
        mClickListener = clickListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.dialog_custom);
        etPortalURL = (EditText) findViewById(R.id.et_portal_url);
        etUsername = (EditText) findViewById(R.id.et_nameduser_name);
        etPassword = (EditText) findViewById(R.id.et_nameduser_password);
        btnContinue = (Button)findViewById(R.id.btn_continue);
        btnCancel = (Button)findViewById(R.id.btn_cancel);

        btnContinue.setOnClickListener(mClickListener);
        btnCancel.setOnClickListener(mClickListener);

        this.setCancelable(true);

    }

    public View getEditTextView(String strEditView){
        if (strEditView.equalsIgnoreCase("portalURL")){
            return etPortalURL;
        }else if (strEditView.equalsIgnoreCase("username")){
            return etUsername;
        }else if (strEditView.equalsIgnoreCase("password")){
            return etPassword;
        }else{
            return null;
        }
    }
}
