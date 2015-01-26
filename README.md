<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
  <meta http-equiv="Content-Style-Type" content="text/css">
  <title></title>
  <meta name="Generator" content="Cocoa HTML Writer">
  <meta name="CocoaVersion" content="1343.16">
  <style type="text/css">
    p.p2 {margin: 0.0px 0.0px 0.0px 0.0px; line-height: 16.0px; font: 11.0px Monaco; color: #737373; -webkit-text-stroke: #737373; background-color: #ffffff}
    p.p3 {margin: 0.0px 0.0px 0.0px 0.0px; line-height: 16.0px; font: 11.0px Monaco; color: #737373; -webkit-text-stroke: #737373; background-color: #ffffff; min-height: 15.0px}
    p.p4 {margin: 0.0px 0.0px 0.0px 0.0px; line-height: 16.0px; font: 11.0px Monaco; color: #737373; -webkit-text-stroke: #737373}
    p.p5 {margin: 0.0px 0.0px 9.0px 0.0px; line-height: 18.0px; font: 13.0px 'Helvetica Neue'; color: #737373; -webkit-text-stroke: #737373; min-height: 15.0px}
    span.s1 {font-kerning: none}
    span.s2 {font-kerning: none; background-color: #ffffff}
  </style>
</head>
<body>
<h2 style="margin: 0.0px 0.0px 0.0px 0.0px; line-height: 36.0px; font: 24.0px 'Helvetica Neue'; color: #404040; -webkit-text-stroke: #404040"><span class="s1"><b>Usage</b></span></h2>
<p class="p2"><span class="s1"><span class="Apple-converted-space">  </span>protected void onCreate(Bundle savedInstanceState) {</span></p>
<p class="p2"><span class="s1"><span class="Apple-converted-space">        </span>// TODO Auto-generated method stub</span></p>
<p class="p2"><span class="s1"><span class="Apple-converted-space">        </span>super.onCreate(savedInstanceState);</span></p>
<p class="p2"><span class="s1"><span class="Apple-converted-space">        </span>setContentView(R.layout.activity_config);</span></p>
<p class="p3"><span class="s1"></span><br></p>
<p class="p3"><span class="s1"></span><br></p>
<p class="p2"><span class="s1"><span class="Apple-converted-space">        </span>wifiHelper = new WifiHelper(this);</span></p>
<p class="p3"><span class="s1"></span><br></p>
<p class="p2"><span class="s1"><span class="Apple-converted-space">         </span>//open wifi</span></p>
<p class="p2"><span class="s1"><span class="Apple-converted-space">        </span>wifiHelper.openWifi(new WifiHelper.openWifiCallback() {</span></p>
<p class="p3"><span class="s1"></span><br></p>
<p class="p2"><span class="s1"><span class="Apple-converted-space">            </span>@Override</span></p>
<p class="p2"><span class="s1"><span class="Apple-converted-space">            </span>public void openSuccess() {</span></p>
<p class="p2"><span class="s1"><span class="Apple-converted-space">                </span>//open success <span class="Apple-converted-space">   </span></span></p>
<p class="p2"><span class="s1"><span class="Apple-converted-space">            </span>}</span></p>
<p class="p3"><span class="s1"></span><br></p>
<p class="p2"><span class="s1"><span class="Apple-converted-space">            </span>@Override</span></p>
<p class="p2"><span class="s1"><span class="Apple-converted-space">            </span>public void openFail() {</span></p>
<p class="p2"><span class="s1"><span class="Apple-converted-space">                </span>//open fail</span></p>
<p class="p3"><span class="s1"></span><br></p>
<p class="p2"><span class="s1"><span class="Apple-converted-space">            </span>}</span></p>
<p class="p2"><span class="s1"><span class="Apple-converted-space">        </span>});</span></p>
<p class="p3"><span class="s1"></span><br></p>
<p class="p2"><span class="s1"><span class="Apple-converted-space">        </span>//scan wifi</span></p>
<p class="p2"><span class="s1"><span class="Apple-converted-space">        </span>List&lt;ScanResult&gt; list = wifiHelper.startScan();</span></p>
<p class="p3"><span class="s1"></span><br></p>
<p class="p2"><span class="s1"><span class="Apple-converted-space">        </span>for (int i = 0; i &lt; list.size(); i++) {</span></p>
<p class="p2"><span class="s1"><span class="Apple-converted-space">            </span>ScanResult r = list.get(i);</span></p>
<p class="p2"><span class="s1"><span class="Apple-converted-space">            </span>String ssidName = r.SSID;</span></p>
<p class="p3"><span class="s1"></span><br></p>
<p class="p2"><span class="s1"><span class="Apple-converted-space">        </span>}</span></p>
<p class="p3"><span class="s1"></span><br></p>
<p class="p2"><span class="s1"><span class="Apple-converted-space">        </span>//connect wifi</span></p>
<p class="p2"><span class="s1"><span class="Apple-converted-space">        </span>wifiHelper.connect(ssidName, apPassword, 3,</span></p>
<p class="p2"><span class="s1"><span class="Apple-converted-space">            </span>new WifiHelper.connectWifiCallback() {</span></p>
<p class="p3"><span class="s1"></span><br></p>
<p class="p2"><span class="s1"><span class="Apple-converted-space">            </span>@Override</span></p>
<p class="p2"><span class="s1"><span class="Apple-converted-space">            </span>public void connectSuccess() {</span></p>
<p class="p2"><span class="s1"><span class="Apple-converted-space">                </span>//conect success</span></p>
<p class="p2"><span class="s1"><span class="Apple-converted-space">                </span>}</span></p>
<p class="p3"><span class="s1"></span><br></p>
<p class="p2"><span class="s1"><span class="Apple-converted-space">            </span>@Override</span></p>
<p class="p2"><span class="s1"><span class="Apple-converted-space">            </span>public void connectFail() {</span></p>
<p class="p2"><span class="s1"><span class="Apple-converted-space">                </span>//connect fail</span></p>
<p class="p2"><span class="s1"><span class="Apple-converted-space">            </span>}</span></p>
<p class="p2"><span class="s1"><span class="Apple-converted-space">        </span>});</span></p>
<p class="p3"><span class="s1"></span><br></p>
<p class="p3"><span class="s1"></span><br></p>
<p class="p3"><span class="s1"></span><br></p>
<p class="p4"><span class="s2"><span class="Apple-converted-space">    </span>}</span></p>
<p class="p5"><span class="s1"></span><br></p>
</body>
</html>
