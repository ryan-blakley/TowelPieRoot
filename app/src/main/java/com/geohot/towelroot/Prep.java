package com.geohot.towelroot;

import android.util.Log;

public class Prep {
	
	public static void pause(long s) {
		try {
			Thread.currentThread();
			Thread.sleep(s * 1000);
		} catch(InterruptedException ex) {
	    	Thread.currentThread().interrupt();
	    	Log.e(Var.LOGTAG, "Error occured while pausing the thread. Error msg = " + ex.toString());
		}
	}
	
	public static boolean rootThisBitch() {
		if(!(Var.Modstring == 0)) {
			if(Var.Modstring == 1) {
				Var.strTR = TowelRoot.rootTheShit(Var.strModString);
			} else if (Var.Modstring == 2) {
				Var.strTR = TowelRoot.rootTheShit(Var.strModString2);
			} else if (Var.Modstring == 3) {
				Var.strTR = TowelRoot.rootTheShit(Var.strModString3);
			}
			Log.i(Var.LOGTAG, "Temp rooted thanks to TR by Geohot!");
			return true;
		} else {
			Log.e(Var.LOGTAG, "Device check didn't run");
			return false;
		}
	}

	public static boolean mSetup() {
		Log.i(Var.LOGTAG, "Setting up after TR has run");
		if(Exec.intCMD(Var.mAppCtx.getAppContext().getFilesDir() + "/busybox cp -fp " + Var.mAppCtx.getAppContext().getFilesDir() + "/busybox /data", false) !=0) {
			Log.e(Var.LOGTAG, "Access denied copying busybox to /data");
			return false;
		} else {
			Exec.CMD(Var.bbx + "rm /data/local/atvc/*", false, false);
			Exec.CMD(Var.bbx + "rm /data/local/tmp/*", false, false);
			Exec.CMD(Var.bbx + "rm -f /data/bin.img", false, false);
			Exec.CMD(Var.bbx + "rm -f /data/xbin.img", false, false);
			Exec.CMD(Var.bbx + "cp -p " + Var.mAppCtx.getAppContext().getFilesDir() + "/toolbox /data", false, false);
			Exec.CMD(Var.bbx + "cp -p " + Var.mAppCtx.getAppContext().getFilesDir() + "/mount " + Var.tmp, false, false);
			Exec.CMD(Var.bbx + "cp -p " + Var.mAppCtx.getAppContext().getFilesDir() + "/mount2 " + Var.tmp, false, false);
			Exec.CMD(Var.bbx + "cp " + Var.mAppCtx.getAppContext().getFilesDir() + "/bin.img /data", false, false);
			Exec.CMD(Var.bbx + "cp " + Var.mAppCtx.getAppContext().getFilesDir() + "/xbin.img /data", false, false);
			Exec.CMD(Var.bbx + "chmod 777 /data/bin.img", false, false);
			Exec.CMD(Var.bbx + "chmod 777 /data/xbin.img", false, false);
	    	Log.i(Var.LOGTAG, "Finished setting up");
			return true;
		}
	}
	
	public static void tmpBackup(boolean xposed) {
		if(xposed) {
			Exec.CMD(Var.bbx + "mkdir -m 777 /data/bin", false, false);
			Exec.CMD(Var.bbx + "cp -p /system/bin/* /data/bin", false, false);
			Exec.CMD(Var.bbx + "mkdir -m 777 /data/xbin", false, false);
			Exec.CMD(Var.bbx + "cp -p /system/xbin/* /data/xbin", false, false);
			Exec.CMD(Var.bbx + "rm -f /data/xbin/daemonsu; " + Var.bbx + "rm -f /data/xbin/su", false, false);
			Log.i(Var.LOGTAG, "Finished backing up /system/bin and /system/xbin");
		} else {
			Exec.CMD(Var.bbx + "mkdir -m 777 /data/xbin", false, false);
			Exec.CMD(Var.bbx + "cp -p /system/xbin/* /data/xbin", false, false);
			Exec.CMD(Var.bbx + "rm -f /data/xbin/daemonsu; " + Var.bbx + "rm -f /data/xbin/su", false, false);
			Log.i(Var.LOGTAG, "Finished backing up /system/xbin");
		}
	}
	
	public static boolean ra1nOnMoto(boolean bin) {
		Log.i(Var.LOGTAG, "Starting to ra1n on moto");
		if(bin) {
			if(Exec.intCMD(Var.bbx + "echo " + Var.tmp + "/mount2 > " + Var.kernel, false) !=0) {
				Log.e(Var.LOGTAG, "Access denied to uevent_helper");
				return false;
			} else {
				Log.i(Var.LOGTAG, "Wrote mount2 to uevent_helper");
				return true;
			}
		} else {
			if(Exec.intCMD(Var.bbx + "echo " + Var.tmp + "/mount > " + Var.kernel, false) !=0) {
				Log.e(Var.LOGTAG, "Access denied to uevent_helper");
				return false;
			} else {
				Log.i(Var.LOGTAG, "Wrote mount to uevent_helper");
				return true;
			}
		}
	}
	
	public static void mCleanup(boolean xposed) {
		if(xposed) {
			pause(1);
		    Exec.CMD(Var.bbx + "cp -fp /data/bin/* /system/bin", false, false);
		    Exec.CMD(Var.bbx + "cp -fp /data/bin/mksh /system/xbin/sugote-mksh", false, false);
			Exec.CMD(Var.bbx + "cp -fp /data/xbin/* /system/xbin", false, false);
			Exec.CMD(Var.tbx + "chcon u:object_r:system_file:s0 /system/bin/*", false, false);
			Exec.CMD(Var.tbx + "chcon u:object_r:zygote_exec:s0 /system/bin/app_process", false, false);
			Exec.CMD(Var.tbx + "chcon u:object_r:clatd_exec:s0 /system/bin/clatd", false, false);
			Exec.CMD(Var.tbx + "chcon u:object_r:debuggerd_exec:s0 /system/bin/debuggerd", false, false);
			Exec.CMD(Var.tbx + "chcon u:object_r:dhcpcd_exec:s0 /system/bin/dhcp", false, false);
			Exec.CMD(Var.tbx + "chcon u:object_r:dnsmasq_exec:s0 /system/bin/dnsmasq", false, false);
			Exec.CMD(Var.tbx + "chcon u:object_r:drmserver_exec:s0 /system/bin/drmserver", false, false);
			Exec.CMD(Var.tbx + "chcon u:object_r:hostapd_exec:s0 /system/bin/hostapd", false, false);
			Exec.CMD(Var.tbx + "chcon u:object_r:installd_exec:s0 /system/bin/installd", false, false);
			Exec.CMD(Var.tbx + "chcon u:object_r:keystore_exec:s0 /system/bin/keystore", false, false);
			Exec.CMD(Var.tbx + "chcon u:object_r:mediaserver_exec:s0 /system/bin/mediaserver", false, false);
			Exec.CMD(Var.tbx + "chcon u:object_r:shell_exec:s0 /system/bin/mksh", false, false);
			Exec.CMD(Var.tbx + "chcon u:object_r:mtp_exec:s0 /system/bin/mtpd", false, false);
			Exec.CMD(Var.tbx + "chcon u:object_r:netd_exec:s0 /system/bin/netd", false, false);
			Exec.CMD(Var.tbx + "chcon u:object_r:ping_exec:s0 /system/bin/ping", false, false);
			Exec.CMD(Var.tbx + "chcon u:object_r:ppp_exec:s0 /system/bin/pppd", false, false);
			Exec.CMD(Var.tbx + "chcon u:object_r:racoon_exec:s0 /system/bin/racoon", false, false);
			Exec.CMD(Var.tbx + "chcon u:object_r:rild_exec:s0 /system/bin/rild", false, false);
			Exec.CMD(Var.tbx + "chcon u:object_r:runas_exec:s0 /system/bin/run-as", false, false);
			Exec.CMD(Var.tbx + "chcon u:object_r:sdcardd_exec:s0 /system/bin/sdcard", false, false);
			Exec.CMD(Var.tbx + "chcon u:object_r:servicemanager_exec:s0 /system/bin/servicemanager", false, false);
			Exec.CMD(Var.tbx + "chcon u:object_r:surfaceflinger_exec:s0 /system/bin/surfaceflinger", false, false);
			Exec.CMD(Var.tbx + "chcon u:object_r:vold_exec:s0 /system/bin/vold", false, false);
			Exec.CMD(Var.tbx + "chcon u:object_r:wpa_exec:s0 /system/bin/wpa_supplicant", false, false);
			Exec.CMD(Var.tbx + "chcon u:object_r:clatd_exec:s0 /system/bin/clatd", false, false);
			Exec.CMD(Var.tbx + "chcon u:object_r:system_file:s0 /system/xbin/*", false, false);
		    Exec.CMD(Var.tbx + "chcon u:object_r:zygote_exec:s0 /system/xbin/sugote", false, false);
			Exec.CMD(Var.bbx + "rm -r /data/bin", false, false);
			Exec.CMD(Var.bbx + "rm -r /data/xbin", false, false);
			Log.i(Var.LOGTAG, "Finished restoring backup and cleaning up");
		} else {
			Exec.CMD(Var.bbx + "cp -fp /data/xbin/* /system/xbin", false, false);
			Exec.CMD(Var.bbx + "cp -fp /system/bin/mksh /system/xbin/sugote-mksh", false, false);
			Exec.CMD(Var.tbx + "chcon u:object_r:system_file:s0 /system/xbin/*", false, false);
	    	Exec.CMD(Var.tbx + "chcon u:object_r:zygote_exec:s0 /system/xbin/sugote", false, false);
			Exec.CMD(Var.bbx + "rm -r /data/xbin", false, false);
			Log.i(Var.LOGTAG, "Finished restoring backup and cleaning up");
		}
	}
	
	public static void chkifRootedBoot() {
		pause(1);
		Exec.CMD("id", true, true);
		if (Exec.cmdOutput.contains("root")) {
			Log.i(Var.LOGTAG, "Root access granted on boot");
		} else {
			Log.e(Var.LOGTAG, "Couldn't obtain root access on boot");
		}
	}
}
