package com.example.oldchri.bitbrowser;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private ImageView expand, refresh, back, forward, history, tabs;
    private boolean panelHidden;
    private WebView webView;
    private ProgressBar progressBar;
    private Animation click;
    private LinearLayout buttonPanel;
    private LinearLayout searchBar;
    private AutoCompleteTextView actv;
    ArrayList<String> urls = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        click = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.click);

        actv = (AutoCompleteTextView)findViewById(R.id.actv);
        buttonPanel = (LinearLayout)findViewById(R.id.buttonPanel);
        searchBar = (LinearLayout)findViewById(R.id.searchBar);
        refresh = (ImageView)findViewById(R.id.refresh);
        expand = (ImageView)findViewById(R.id.expand);
        back = (ImageView)findViewById(R.id.back);
        forward = (ImageView)findViewById(R.id.forward);
        history = (ImageView)findViewById(R.id.history);
        tabs = (ImageView)findViewById(R.id.tabs);
        panelHidden = false;
        progressBar = (ProgressBar)findViewById(R.id.progressBar);

        webView = (WebView)findViewById(R.id.webView);
        webView.requestFocus();
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {

            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                progressBar.setVisibility(View.VISIBLE);
            }
            public void onPageFinished(WebView view, String url) {
                progressBar.setVisibility(View.GONE);
            }
        });

        //Display Google homepage
        webView.loadUrl("https://www.google.com");
//        actv.setText("https://www.google.com");
        if (!urls.contains("https://www.google.com"))
            urls.add("https://www.google.com");

        //Search Bar
        actv.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    handled = true;
                    //Enter key pressed
                    webView.loadUrl(actv.getText().toString());

                    if (!urls.contains(actv.getText().toString()))
                        urls.add(actv.getText().toString());
                }
                return handled;
            }
        });
        actv.setThreshold(1);
        actv.setAdapter(new AutoSuggestAdapter(this, android.R.layout.simple_list_item_1, urls));
        actv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                webView.loadUrl((String)parent.getItemAtPosition(position));
            }
        });
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webView.reload();
                refresh.startAnimation(click);
            }
        });

        //Button Panel
        expand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (panelHidden)
                    showUI();
                else
                    hideUI();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webView.goBack();
                back.startAnimation(click);
            }
        });
        forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webView.goForward();
                forward.startAnimation(click);
            }
        });
        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                history.startAnimation(click);
            }
        });
        tabs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tabs.startAnimation(click);
            }
        });
    }

    public void hideUI(){
        buttonPanel.animate().setDuration(300).translationY(findViewById(R.id.back).getHeight());
        searchBar.animate().setDuration(300).translationY(-1*searchBar.getHeight());
        expand.setImageResource(R.drawable.ic_expand_less_32dp);
        panelHidden = true;
    }

    public void showUI() {
        buttonPanel.animate().setDuration(300).translationY(0);
        searchBar.animate().setDuration(300).translationY(0);
        expand.setImageResource(R.drawable.ic_expand_more_32dp);
        panelHidden = false;
    }

    public void onBackPressed() {
        webView.goBack();
    }
}