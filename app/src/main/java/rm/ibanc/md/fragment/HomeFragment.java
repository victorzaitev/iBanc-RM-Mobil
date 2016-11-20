package rm.ibanc.md.fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.os.Bundle;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import rm.ibanc.md.activity.CursActivity;
import rm.ibanc.md.ibanc_rm.R;

/**
 * Created by PC01017745 on 27.07.2016.
 */
public class HomeFragment extends Fragment {



    public HomeFragment() {
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

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        Button button = (Button) view.findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CursActivity.class);
                startActivity(intent);

            }
        });


        ListView mListView = (ListView) view.findViewById(R.id.listView);
        String[] fakeTweets = getResources().getStringArray(R.array.cat_names);

        List<String> list = new ArrayList();


        String manufacturer = Build.MANUFACTURER;
        String brand = Build.BRAND;
        String product = Build.PRODUCT;
        String model = Build.MODEL;

        list.add(" manufacturer " + manufacturer);
        list.add(" brand " + brand);
        list.add(" product " + product);
        list.add(" model " + model);

        int REQUEST_READ_PHONE_STATE = 0;
        int permissionCheck = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_PHONE_STATE);


        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            //  int REQUEST_READ_PHONE_STATE;
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_READ_PHONE_STATE);
        } else {
            //TODO


            TelephonyManager tm = (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);

            String device_id = tm.getDeviceId();
            String deviceims = tm.getSubscriberId();
            String softwareVersion = tm.getDeviceSoftwareVersion();
            String networkOperator = tm.getNetworkOperator();
            String networkOperatorName = tm.getNetworkOperatorName();
            String subscriberId = tm.getSubscriberId();


            list.add(" device_id " + device_id);
            list.add(" deviceims " + deviceims);
            list.add(" softwareVersion " + softwareVersion);
            list.add(" networkOperator " + networkOperator);
            list.add(" networkOperatorName " + networkOperatorName);
            list.add(" subscriberId " + subscriberId);

            WifiManager wifiManager = (WifiManager) getActivity().getSystemService(Context.WIFI_SERVICE);
            WifiInfo wInfo = wifiManager.getConnectionInfo();

            String macAddress = wInfo.getMacAddress();
            String bssid = wInfo.getBSSID();
            String ssid = wInfo.getSSID();
            int ipAddress = wInfo.getIpAddress();

            list.add(" macAddress " + macAddress);
            list.add(" bssid " + bssid);
            list.add(" ssid " + ssid);
            list.add(" ipAddress " + ipAddress);

        }


            System.out.println(" folffo " + fakeTweets.length);

        final ListAdapter mAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, list);
        mListView.setAdapter(mAdapter);


        return view;
    }
}
