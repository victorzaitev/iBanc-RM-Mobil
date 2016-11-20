package rm.ibanc.md.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;

import rm.ibanc.md.ibanc_rm.R;

/**
 * Created by PC01017745 on 27.07.2016.
 */
public class ServiciiFragment extends Fragment {

    private WebView mWebView;

    public ServiciiFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_service, container, false);

        mWebView = (WebView) view.findViewById(R.id.webView);
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setLoadsImagesAutomatically(true);
        mWebView.loadUrl("http://172.18.111.101:8089/ClickCard/showpage.do?pageName=home");




        return view;
    }


}
