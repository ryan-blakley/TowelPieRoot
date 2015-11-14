package com.geohot.towelroot;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class BootService extends IntentService {
	
	public BootService() {
		super("TowelPieRoot BootService");
	}

	@Override
    protected void onHandleIntent(Intent bootIntent) {
	    bootProcess();
	}
	
	public void bootProcess() {
		Prep.pause(3);
		Var.mAppCtx = new AppCtx();
		final SharedPreferences localSharedPreferences = PreferenceManager.getDefaultSharedPreferences(Var.mAppCtx.getAppContext());
		if (localSharedPreferences.getBoolean("install_xposed", false)) {
			Var.xposed = true;
		} else {
			Var.xposed = false;
		}
		if (localSharedPreferences.getBoolean("apply_on_boot", false)) {
		    if(Var.xposed) {
		    	rootProcess(true);
		    } else {
		    	rootProcess(false);
		    }
		}
	}
	
	public void rootProcess(boolean xposed) {
		Device.chkMN();
		if(!Prep.rootThisBitch()) {
	    	return;
	    }
		Prep.pause(1);
		if(!Prep.ra1nOnMoto(xposed)) {
		    return;
		}
		Prep.chkifRootedBoot();
	}
}
