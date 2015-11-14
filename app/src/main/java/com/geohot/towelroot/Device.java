package com.geohot.towelroot;

import android.util.Log;
//import android.widget.Toast;

public class Device {

	public static int chkMN() {
		Log.i(Var.LOGTAG, "Checking device model");
    	Exec.CMD("getprop ro.product.model", false, true);
    	String strModelNum = Exec.cmdOutput;
    	if (strModelNum.contains("XT1060") || strModelNum.contains("XT1058") || strModelNum.contains("XT1056") || strModelNum.contains("XT1055") ||
    			strModelNum.contains("XT1054") || strModelNum.contains("XT1053") || strModelNum.contains("XT1052") || strModelNum.contains("XT1051") ||
    			strModelNum.contains("XT1050") || strModelNum.contains("XT1049") || strModelNum.contains("XT1080") || strModelNum.contains("XT1080m")) { 
    		return 1;
    	} else if (strModelNum.contains("XT1034") || strModelNum.contains("XT1033") || strModelNum.contains("XT1031") || strModelNum.contains("XT1028") || 
    			strModelNum.contains("XT939G") || strModelNum.contains("XT1045") || strModelNum.contains("XT1040") || strModelNum.contains("XT1039") 
    			/*|| strModelNum.contains("XT1060")*/) {	
    		//Toast.makeText(Var.mAppCtx.getAppContext(), "Test successfully found the device model", Toast.LENGTH_SHORT).show();
    		return 2;
    	} else if (strModelNum.contains("XT1032")) {
    		return 3;
    	} else return 99;
	}
}
