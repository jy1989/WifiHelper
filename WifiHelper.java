

import java.util.List;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.WifiLock;
import android.util.Log;

public class WifiHelper {
	public interface connectWifiCallback {
		public void connectSuccess();
		public void connectFail();
	}

	public interface openWifiCallback {
		public void openSuccess();
		public void openFail();
	}

	// 定义WifiManager对象
	public WifiManager mWifiManager;
	// 定义WifiInfo对象
	private WifiInfo mWifiInfo;
	// 扫描出的网络连接列表
	private List<ScanResult> mWifiList;
	// 网络连接列表
	private List<WifiConfiguration> mWifiConfiguration;
	// 定义一个WifiLock
	WifiLock mWifiLock;

	/**
	 *  * 构造器  
	 */
	public WifiHelper(Context context) {
		// 取得WifiManager对象
		mWifiManager = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		// 取得WifiInfo对象
		mWifiInfo = mWifiManager.getConnectionInfo();
	}

	/**
	 *  * 打开WIFI  
	 */
	public void openWifi(openWifiCallback cb) {

		if (!mWifiManager.isWifiEnabled()) {
			mWifiManager.setWifiEnabled(true);
			System.out.println("wifi disabled, opening wifi");
		} else {
			System.out.println("wifi enable.");
		}

		while (mWifiManager.getWifiState() != WifiManager.WIFI_STATE_ENABLED) {
			try {

				Thread.sleep(100);

			} catch (InterruptedException ie) {
				ie.printStackTrace();
			}
		}

		System.out.println("open success,callback ");
		cb.openSuccess();

	}

	/**
	 *  * 关闭WIFI  
	 */
	public void closeWifi() {
		if (mWifiManager.isWifiEnabled()) {
			mWifiManager.setWifiEnabled(false);
		}
	}

	/**
	 *  * 检查当前WIFI状态  *   * @return  
	 */
	public int checkState() {
		return mWifiManager.getWifiState();
	}

	/**
	 *  * 锁定WifiLock  
	 */
	public void acquireWifiLock() {
		mWifiLock.acquire();
	}

	/**
	 *  * 解锁WifiLock  
	 */
	public void releaseWifiLock() {
		// 判断时候锁定
		if (mWifiLock.isHeld()) {
			mWifiLock.acquire();
		}
	}

	/**
	 *  * 创建一个WifiLock  
	 */
	public void creatWifiLock() {
		mWifiLock = mWifiManager.createWifiLock("Test");
	}

	/**
	 *  * 得到配置好的网络  *   * @return  
	 */
	public List<WifiConfiguration> getConfiguration() {
		return mWifiConfiguration;
	}

	/**
	 *  * 提供一个外部接口，传入要连接的无线网  *   * @param ssid  * @param password  * @param
	 * type  
	 */
	public void connect(String ssid, String password, int type,
			connectWifiCallback cb) {
		Thread thread = new Thread(
				new ConnectRunnable(ssid, password, type, cb));
		thread.start();
	}

	public boolean isEqualsSsid(String ssidForConnect) {
		String ssidCurrent = getSSID();
		try {
			if (!"".equals(ssidForConnect) && ssidForConnect != null) {

				if (("\"" + ssidForConnect + "\"").equals(ssidCurrent)
						|| ssidForConnect.equals(ssidCurrent)) {
					return true;
				}
			}
		} catch (Exception e) {

		}
		return false;
	}

	class ConnectRunnable implements Runnable {
		private static final String TAG = "ConnectRunnable====";

		private String ssid;

		private String password;

		private int type;
		connectWifiCallback cb;

		public ConnectRunnable(String ssid, String password, int type,
				connectWifiCallback cb) {
			System.out.printf("ssid=%s,password=%s,type=%s\r\n", ssid,
					password, type);

			this.ssid = ssid;
			this.password = password;
			this.type = type;
			this.cb = cb;
		}

		@Override
		public void run() {

			// System.out.println("getSSID() " + getSSID());
			// System.out.println(isEqualsSsid(ssid,getSSID()));
			if (isEqualsSsid(ssid)) {
				System.out.println(" connected");
				cb.connectSuccess();
			} else {

				boolean enabled = false;
				boolean connected = false;

				WifiConfiguration wifiConfig = CreateWifiInfo(ssid, password,
						type);
				//
				if (wifiConfig == null) {
					Log.d(TAG, "wifiConfig is null!");
					return;
				}

				WifiConfiguration tempConfig = IsExsits(ssid);

				if (tempConfig != null) {
					mWifiManager.removeNetwork(tempConfig.networkId);
				}

				int netID = mWifiManager.addNetwork(wifiConfig);
				enabled = mWifiManager.enableNetwork(netID, true);
				Log.d(TAG, "enableNetwork status enable=" + enabled);
				connected = mWifiManager.reconnect();
				Log.d(TAG, "enableNetwork connected=" + connected);

				while (mWifiManager.getWifiState() != WifiManager.WIFI_STATE_ENABLED
						|| !isEqualsSsid(ssid)) {
					try {
						System.out
								.println("wait for wifi connect,"
										+ (mWifiManager.getWifiState() != WifiManager.WIFI_STATE_ENABLED)
										+ "," + !isEqualsSsid(ssid) + " ,"
										+ ssid + "," + getSSID());
						Thread.sleep(500);
					} catch (InterruptedException ie) {
						ie.printStackTrace();
					}
				}
				System.out
						.println("wifi connected,"
								+ (mWifiManager.getWifiState() != WifiManager.WIFI_STATE_ENABLED)
								+ "," + !isEqualsSsid(ssid) + " ," + ssid + ","
								+ getSSID());

				System.out.printf("enabled=%s,connected=%s\r\n", enabled,
						connected);

				if (enabled && connected) {
					System.out.println("connect success");
					cb.connectSuccess();
				} else {
					System.out.println("connect fail");
					cb.connectFail();
				}

			}

		}
	}

	/**
	 *  * 指定配置好的网络进行连接  *   * @param index  
	 */
	public void connectConfiguration(int index) {
		// 索引大于配置好的网络索引返回
		if (index > mWifiConfiguration.size()) {
			return;
		}
		// 连接配置好的指定ID的网络
		mWifiManager.enableNetwork(mWifiConfiguration.get(index).networkId,
				true);
	}

	public List<ScanResult> startScan() {

		mWifiManager.startScan();
		// 得到扫描结果
		mWifiList = mWifiManager.getScanResults();
		// 得到配置好的网络连接
		mWifiConfiguration = mWifiManager.getConfiguredNetworks();
		return mWifiList;
	}

	/**
	 *  * 得到网络列表  
	 */
	public List<ScanResult> getWifiList() {
		return mWifiList;
	}

	/**
	 *  * 查看扫描结果  *   * @return  
	 */
	public StringBuilder lookUpScan() {
		StringBuilder stringBuilder = new StringBuilder();
		for (int i = 0; i < mWifiList.size(); i++) {
			stringBuilder
					.append("Index_" + new Integer(i + 1).toString() + ":");
			// 将ScanResult信息转换成一个字符串包
			// 其中把包括：BSSID、SSID、capabilities、frequency、level
			stringBuilder.append((mWifiList.get(i)).toString());
			stringBuilder.append("/n");
		}
		return stringBuilder;
	}

	/**
	 *  * 得到MAC地址  *   * @return  
	 */
	public String getMacAddress() {
		return (mWifiInfo == null) ? "NULL" : mWifiInfo.getMacAddress();
	}

	/**
	 *  * 得到接入点的BSSID  *   * @return  
	 */
	public String getBSSID() {
		return (mWifiInfo == null) ? "NULL" : mWifiInfo.getBSSID();
	}

	/**
	 *  * 得到IP地址  *   * @return  
	 */
	public int getIPAddress() {
		return (mWifiInfo == null) ? 0 : mWifiInfo.getIpAddress();
	}

	/**
	 *  * 得到连接的ID  *   * @return  
	 */
	public int getNetworkId() {
		return (mWifiInfo == null) ? 0 : mWifiInfo.getNetworkId();
	}

	/**
	 *  * 得到WifiInfo的所有信息包  *   * @return  
	 */
	public String getWifiInfo() {
		return (mWifiInfo == null) ? "NULL" : mWifiInfo.toString();
	}

	/**
	 *  * 添加一个网络并连接  *   * @param wcg  
	 */
	public void addNetwork(WifiConfiguration wcg) {
		int wcgID = mWifiManager.addNetwork(wcg);
		boolean b = mWifiManager.enableNetwork(wcgID, true);
		System.out.println("a--" + wcgID);
		System.out.println("b--" + b);
	}

	/**
	 *  * 断开指定ID的网络  *   * @param netId  
	 */
	public void disconnectWifi(int netId) {
		mWifiManager.disableNetwork(netId);
		mWifiManager.disconnect();
	}

	/**
	 *  * 获取SSID（网络名称）  *   * @return  
	 */
	public String getSSID() {

		// 取得WifiInfo对象
		mWifiInfo = mWifiManager.getConnectionInfo();
		return (mWifiInfo == null) ? null : mWifiInfo.getSSID();
	}

	/**
	 *  * 然后是一个实际应用方法，只验证过没有密码的情况：  *   * @param SSID  * @param Password  * @param
	 * Type  * @return  
	 */
	// 分为三种情况：1没有密码2用wep加密3用wpa加密
	public WifiConfiguration CreateWifiInfo(String SSID, String Password,
			int Type) {
		WifiConfiguration config = new WifiConfiguration();
		config.allowedAuthAlgorithms.clear();
		config.allowedGroupCiphers.clear();
		config.allowedKeyManagement.clear();
		config.allowedPairwiseCiphers.clear();
		config.allowedProtocols.clear();
		config.SSID = "\"" + SSID + "\"";

		/*
		 *  * WifiConfiguration tempConfig = this.IsExsits(SSID); if (tempConfig
		 * !=  * null) { mWifiManager.removeNetwork(tempConfig.networkId); }  
		 */

		if (Type == 1) // WIFICIPHER_NOPASS
		{

			
			config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
		
		}
		if (Type == 2) // WIFICIPHER_WEP
		{
			config.hiddenSSID = true;
			config.wepKeys[0] = "\"" + Password + "\"";
			config.allowedAuthAlgorithms
					.set(WifiConfiguration.AuthAlgorithm.SHARED);
			config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
			config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
			config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
			config.allowedGroupCiphers
					.set(WifiConfiguration.GroupCipher.WEP104);
			config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
			config.wepTxKeyIndex = 0;
		}
		if (Type == 3) // WIFICIPHER_WPA
		{
			config.preSharedKey = "\"" + Password + "\"";
			config.hiddenSSID = true;
			config.allowedAuthAlgorithms
					.set(WifiConfiguration.AuthAlgorithm.OPEN);
			config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
			config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
			config.allowedPairwiseCiphers
					.set(WifiConfiguration.PairwiseCipher.TKIP);
			// config.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
			config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
			config.allowedPairwiseCiphers
					.set(WifiConfiguration.PairwiseCipher.CCMP);
			config.status = WifiConfiguration.Status.ENABLED;
		}
		return config;
	}

	/**
	 *  * 查看以前是否也配置过这个网络  *   * @param SSID  * @return  
	 */
	private WifiConfiguration IsExsits(String SSID) {
		List<WifiConfiguration> existingConfigs = mWifiManager
				.getConfiguredNetworks();
		if (existingConfigs == null) {
			return null;
		} else {
			// Log.e("", "existingConfigs==" + existingConfigs);

			for (WifiConfiguration existingConfig : existingConfigs) {
				if (("\"" + SSID + "\"").equals(existingConfig.SSID)) {
					return existingConfig;
				}
			}
		}
		return null;
	}

}