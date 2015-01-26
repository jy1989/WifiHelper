#Usage
``` java
protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);
 
 
        wifiHelper = new WifiHelper(this);
 
         //open wifi
        wifiHelper.openWifi(new WifiHelper.openWifiCallback() {
 
            @Override
            public void openSuccess() {
                //open success    
            }
 
            @Override
            public void openFail() {
                //open fail
 
            }
        });
 
        //scan wifi
        List<ScanResult> list = wifiHelper.startScan();
 
        for (int i = 0; i < list.size(); i++) {
            ScanResult r = list.get(i);
            String ssidName = r.SSID;
 
        }
 
        //connect wifi
        wifiHelper.connect(ssidName, apPassword, 3,
            new WifiHelper.connectWifiCallback() {
 
            @Override
            public void connectSuccess() {
                //conect success
                }
 
            @Override
            public void connectFail() {
                //connect fail
            }
        }); 
}
``` 
