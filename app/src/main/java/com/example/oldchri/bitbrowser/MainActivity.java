package com.example.oldchri.bitbrowser;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
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
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebBackForwardList;
import android.webkit.WebHistoryItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private ImageView expand, refresh, back, forward, history, tabs, historyCancel, addTabButn;
    private ListView linkList, tabList;
    private TextView tabNum;
    private boolean panelHidden;
    private int currentTab;
    private WebView webView;
    private WebBackForwardList historyList;
    private ProgressBar progressBar;
    private Animation click;
    private LinearLayout buttonPanel, searchBar, historyView, tabView;
    private AutoCompleteTextView actv;
    private ArrayList<String> urls = new ArrayList<>();
    private ArrayList<Tab> tabArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        urls.add("https://www.google.com");
        urls.add("https://www.youtube.com");
        urls.add("https://www.facebook.com");
        urls.add("https://www.trademe.co.nz");
        urls.add("https://www.stuff.co.nz");

        click = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.click);

        actv = (AutoCompleteTextView)findViewById(R.id.actv);
        buttonPanel = (LinearLayout)findViewById(R.id.buttonPanel);
        searchBar = (LinearLayout)findViewById(R.id.searchBar);
        historyView = (LinearLayout)findViewById(R.id.historyCover);
        tabView = (LinearLayout)findViewById(R.id.tabCover);
        refresh = (ImageView)findViewById(R.id.refresh);
        expand = (ImageView)findViewById(R.id.expand);
        back = (ImageView)findViewById(R.id.back);
        forward = (ImageView)findViewById(R.id.forward);
        history = (ImageView)findViewById(R.id.history);
        tabs = (ImageView)findViewById(R.id.tabs);
        historyCancel = (ImageView)findViewById(R.id.historyCancel);
        addTabButn = (ImageView)findViewById(R.id.add_tab);
        panelHidden = false;
        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        linkList = (ListView)findViewById(R.id.historyListView);
        tabList = (ListView)findViewById(R.id.tabListView);
        tabNum = (TextView)findViewById(R.id.tab_number);
        webView = (WebView)findViewById(R.id.webView);
        webView.requestFocus();
        webView.getSettings().setJavaScriptEnabled(true);
        addTab();

        //Search Bar
        actv.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_GO) {
                    handled = true;
                    //Enter key pressed
                    launch(actv.getText().toString());

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
                launch((String)parent.getItemAtPosition(position));
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

        //Backward and forward
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

        //History
        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                history.startAnimation(click);
                historyList = webView.copyBackForwardList();
                historyView.setVisibility(View.VISIBLE);
                ArrayList<Link> links = new ArrayList<>();
                for (int i=historyList.getSize()-1; i>-1; i--) {
                    WebHistoryItem item = historyList.getItemAtIndex(i);
                    links.add(new Link(item.getTitle(), item.getUrl(), item.getFavicon()));
                }
                linkList.setAdapter(new LinkAdapter(MainActivity.this, R.layout.link_item, links));
                linkList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        launch(((TextView)view.findViewById(R.id.linkUrl)).getText().toString());
                        historyView.setVisibility(View.GONE);
                    }
                });
            }
        });
        historyCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                historyView.setVisibility(View.GONE);
            }
        });
        historyView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                historyView.setVisibility(View.GONE);
            }
        });

        //Tabs
        tabs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tabs.startAnimation(click);
                tabList.invalidateViews();
                tabView.setVisibility(View.VISIBLE);
                tabList.setAdapter(new TabAdapter(MainActivity.this, R.layout.tab_item, tabArrayList));
                tabList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                        launch(((TextView)view.findViewById(R.id.tabUrl)).getText().toString());
                        selectTab(Integer.parseInt(((TextView)view.findViewById(R.id.tabPos)).getText().toString()));
                        historyView.setVisibility(View.GONE);
                    }
                });

            }
        });
        addTabButn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTab();
            }
        });
        tabView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tabView.setVisibility(View.GONE);
            }
        });
    }

    public void launch(String url) {
        webView.loadUrl(url);
        actv.setText(url);
        if (!urls.contains(url))
            urls.add(url);
        //Update tab's url
        Tab tempTab = tabArrayList.get(currentTab);
        tempTab.setUrl(url);
        tabArrayList.set(currentTab, tempTab);
        //Hide keyboard
        InputMethodManager imm = (InputMethodManager)this.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(actv.getApplicationWindowToken(), 0);
        actv.clearFocus();
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
        if (historyView.getVisibility()==View.VISIBLE)
            historyView.setVisibility(View.GONE);
        if (tabView.getVisibility()==View.VISIBLE)
            tabView.setVisibility(View.GONE);
        else webView.goBack();
    }

    public void addTab() {
        if (tabArrayList.size()<10) {
            currentTab = tabArrayList.size();
            tabArrayList.add(new Tab(new WebViewClient() {
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    progressBar.setVisibility(View.VISIBLE);
                }
                public void onPageFinished(WebView view, String url) {
                    progressBar.setVisibility(View.GONE);
                }
            },"https://www.google.com",currentTab));
            launch("https://www.google.com");
            webView.setWebViewClient(tabArrayList.get(tabArrayList.size()-1).getWvc());
            tabNum.setText(String.valueOf((Integer.parseInt(tabNum.getText().toString()))+1));
            tabList.invalidateViews();
        }
    }

    public void selectTab(int pos) {
        currentTab = pos;
        webView.setWebViewClient(tabArrayList.get(pos).getWvc());
        tabView.setVisibility(View.GONE);
    }
}