package com.geohot.towelroot;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

public class TowelRoot extends Activity {

	private SharedPreferences localSharedPreferences;
	private CheckBox chkApplyBoot;
	private Button btnRoot;
	private TextView lblStatus;
	private ProgressDialog rootProgress;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Var.mAppCtx = new AppCtx();
		localSharedPreferences = PreferenceManager.getDefaultSharedPreferences(Var.mAppCtx.getAppContext());
		btnRoot = (Button) findViewById(R.id.btnRoot);
		lblStatus = (TextView) findViewById(R.id.lblStatus);
		chkApplyBoot = (CheckBox) findViewById(R.id.chkbAOB);
        chkApplyBoot.setChecked(localSharedPreferences.getBoolean("apply_on_boot", false));
        
        chkApplyBoot.setOnCheckedChangeListener(new OnCheckedChangeListener() {
        	@Override
        	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        		if(isChecked) {
        			SharedPreferences.Editor editor = localSharedPreferences.edit();
        			editor.putBoolean("apply_on_boot", true);
        			editor.commit();
        			Exec.CMD("chmod 777 " + getApplicationInfo().dataDir + "/shared_prefs", false, false);
        			Exec.CMD("chmod 666 " + getApplicationInfo().dataDir + "/shared_prefs/*", false, false);
        			mBootPrompt();
        		} else if(!isChecked) {
        			SharedPreferences.Editor editor = localSharedPreferences.edit();
        			editor.putBoolean("apply_on_boot", false);
        			editor.commit();
        			Exec.CMD("chmod 777 " + getApplicationInfo().dataDir + "/shared_prefs", false, false);
        			Exec.CMD("chmod 666 " + getApplicationInfo().dataDir + "/shared_prefs/*", false, false);
        		}
        	}
        });
        mCompatibility();
        btnRoot.setOnClickListener(
        	new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                	mXposedPrompt();
                }
        });
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		switch (id) {
	    case R.id.reboot:
	      mRebootPrompt();
	      return true;
	    case R.id.uninstall:
	    	mUninstallPrompt();
	      return true;
	    default:
	      return super.onOptionsItemSelected(item);
		}
	}
	
	static {
	  System.loadLibrary("exploit");
	}
	
	public static native String rootTheShit(String modString);

	private void Assets() {
		Var.version = localSharedPreferences.getInt("version", 0);
		if(Var.version !=2) {
			Assets unpack = new Assets();
			unpack.apkPath= getPackageCodePath();
			unpack.mAppRoot = getFilesDir().toString();
			unpack.unpackAsset("/busybox");
			unpack.unpackAsset("/mount");
			unpack.unpackAsset("/mount2");
			unpack.unpackAsset("/toolbox");
			unpack.unpackAsset("/bin.img");
			unpack.unpackAsset("/xbin.img");
			unpack = null;
			Log.i(Var.LOGTAG, "Finished unpacking assests");
		} else {
			Log.i(Var.LOGTAG, "Already unpacked skipping unpacking");
		}
	}
	
	private void mCompatibility() {
		Var.Modstring = Device.chkMN();
		if(Var.Modstring == 99) {
			updateStatus("Status: Device not supported!");
			btnRoot.setVisibility(View.INVISIBLE);
			chkApplyBoot.setVisibility(View.INVISIBLE);
		} else {
			Assets();
			chkifRooted2();
		}
	}
	
	private void updateStatus(final String status) {
		TowelRoot.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
            	lblStatus.setText(status);
            }
        });
	}
	
	private void updateProgress(final String msg) {
		TowelRoot.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
            	rootProgress.setMessage(msg);
            }
        });
	}
	
	private void mBootPrompt() {
		AlertDialog.Builder prompt = new AlertDialog.Builder(this);
		prompt.setTitle("Root on Boot?");
		prompt.setMessage("The root on boot feature can be buggy for certain phones, are you sure you want to enable this feature?");
		prompt.setCancelable(false);
		prompt.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
	    	@Override
	        public void onClick(DialogInterface dialog, int which) {
	            dialog.dismiss();
	        }
	    });
		prompt.setNegativeButton("No", new DialogInterface.OnClickListener() {
	        @Override
	        public void onClick(DialogInterface dialog, int which) {
	            dialog.dismiss();
	            chkApplyBoot.setChecked(false);
	            SharedPreferences.Editor editor = localSharedPreferences.edit();
	            editor.putBoolean("apply_on_boot", false);
	            editor.commit();
	            Exec.CMD("chmod 777 " + getApplicationInfo().dataDir + "/shared_prefs", false, false);
	            Exec.CMD("chmod 666 " + getApplicationInfo().dataDir + "/shared_prefs/*", false, false);
	        }
	    });
	    AlertDialog alert = prompt.create();
	    alert.show();
	}
	
	private void mXposedPrompt() {
		AlertDialog.Builder prompt = new AlertDialog.Builder(this);
		prompt.setTitle("Xposed?");
		prompt.setMessage("Do you plan on installing Xposed?");
		prompt.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
	    	@Override
	        public void onClick(DialogInterface dialog, int which) {
	            dialog.dismiss();
	            rootProgressDialog(true);
	            SharedPreferences.Editor editor = localSharedPreferences.edit();
				editor.putBoolean("install_xposed", true);
				editor.commit();
				Exec.CMD("chmod 777 " + getApplicationInfo().dataDir + "/shared_prefs", false, false);
				Exec.CMD("chmod 666 " + getApplicationInfo().dataDir + "/shared_prefs/*", false, false);
	        }
	    });
		prompt.setNegativeButton("No", new DialogInterface.OnClickListener() {
	        @Override
	        public void onClick(DialogInterface dialog, int which) {
	            dialog.dismiss();
	            rootProgressDialog(false);
	            SharedPreferences.Editor editor = localSharedPreferences.edit();
				editor.putBoolean("install_xposed", false);
				editor.commit();
				Exec.CMD("chmod 777 " + getApplicationInfo().dataDir + "/shared_prefs", false, false);
				Exec.CMD("chmod 666 " + getApplicationInfo().dataDir + "/shared_prefs/*", false, false);
	        }
	    });
	    AlertDialog alert = prompt.create();
	    alert.show();
	}
	
	private void rootProgressDialog(final boolean xposed) {
		rootProgress = new ProgressDialog(this);
		rootProgress.setTitle("Rooting in Progress.....");
		rootProgress.setMessage("Starting...");
		rootProgress.setIndeterminate(true);
		rootProgress.setCancelable(true);
		rootProgress.setCanceledOnTouchOutside(false);
		rootProgress.show();
		new Thread(new Runnable() {
			@Override
		    public void run() {
				if(!Prep.rootThisBitch()) {
					updateStatus("Status: Device check didn't run");
			    	return;
			    } else {
			    	updateProgress("Escalate to root complete...");
			    	Prep.pause(1);
				    if(Var.version !=2) {
				    	if(!Prep.mSetup()) {
				    		updateStatus("Status: Failed to Setup");
				    		return;
				    	} else {
				    		updateProgress("Setup complete...");
				    	}
				    	Prep.tmpBackup(xposed);
				    	updateProgress("Backup complete...");
				    } else {
				    	Log.i(Var.LOGTAG, "Already setup time to echo to uevent_helper");
				    	updateProgress("Already setup...");
				    }
				    	
				    if(!Prep.ra1nOnMoto(xposed)) {
				    	updateStatus("Status: Failed to Write to Kernel");
				    	return;
				    } else updateProgress("Mounting complete...");
				    	
				    if(Var.version !=2) {
				    	Prep.mCleanup(xposed);
				    	updateProgress("Cleanup complete...");
				    	Prep.pause(1);
				    	updateProgress("Checking for Root...");
				    } else {
				    	Log.i(Var.LOGTAG, "Already cleaned up time to check if rooted");
				    	updateProgress("Already cleaned up...");
				    	Prep.pause(1);
				    	updateProgress("Checking for Root...");
				    }
				    SharedPreferences.Editor editor = localSharedPreferences.edit();
				    editor.putInt("version", 2);
		    		editor.commit();
		    		Exec.CMD("chmod 777 " + getApplicationInfo().dataDir + "/shared_prefs", false, false);
		    		Exec.CMD("chmod 666 " + getApplicationInfo().dataDir + "/shared_prefs/*", false, false);
				    chkifRooted();
			    }
				rootProgress.dismiss();
			}
		}).start();
	}
	
	private void chkifRooted() {
		TowelRoot.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
            	Prep.pause(1);
        		Exec.CMD("id", true, true);
        		if (Exec.cmdOutput.contains("root")) {
        			Log.i(Var.LOGTAG, "Root access granted");
        			lblStatus.setText("Status: Root Achieved Enjoy");
        			Var.rooted = true;
        			chkApplyBoot.setVisibility(View.VISIBLE);
        		} else {
        			Log.e(Var.LOGTAG, "Can't get root access");
        			lblStatus.setText("Status: Failed to Achieve Root");
        			Var.rooted = false;
        			chkApplyBoot.setVisibility(View.INVISIBLE);
        		}
            }
        });
	}
	
	private void chkifRooted2() {
		Exec.CMD(Var.mAppCtx.getAppContext().getFilesDir() + "/busybox ps -A", false, true);
		if (Exec.cmdOutput.contains("daemonsu")) {
			Log.i(Var.LOGTAG, "Root access granted on create");
			lblStatus.setText("Status: Rooted Already");
			Var.rooted = true;
			chkApplyBoot.setVisibility(View.VISIBLE);
		} else {
			Log.w(Var.LOGTAG, "The device isn't currently rooted");
			lblStatus.setText("Status: Not Rooted");
			Var.rooted = false;
			chkApplyBoot.setVisibility(View.INVISIBLE);
		}
	}
	
	private void mRebootPrompt() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Full Reboot?");
		builder.setMessage("Are you sure you want to fully reboot?");
	    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
	    	@Override
	        public void onClick(DialogInterface dialog, int which) {
	            dialog.dismiss();
	    		reboot();
	        }
	    });
	    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
	        @Override
	        public void onClick(DialogInterface dialog, int which) {
	            dialog.dismiss();
	        }
	    });
	    AlertDialog alert = builder.create();
	    alert.show();
	}
	
	private void reboot() {
		if(!Var.rooted) {
			Log.e(Var.LOGTAG, "Can't reboot because the device isn't rooted");
			Toast.makeText(Var.mAppCtx.getAppContext(), "Can't reboot because the device isn't rooted!", Toast.LENGTH_SHORT).show();
			return;
		} else {
			Log.i(Var.LOGTAG, "Rebooting normally");
			Exec.CMD("reboot", true, false);
		}
	}
	
	private void mUninstallPrompt() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
	    builder.setTitle("Uninstall?");
	    builder.setMessage("Are you sure you want to uninstall TowelPieRoot, this will reboot your phone?");
	    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
	    	@Override
	        public void onClick(DialogInterface dialog, int which) {
	            dialog.dismiss();
	            uninstall();
	        }
	    });
	    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
	        @Override
	        public void onClick(DialogInterface dialog, int which) {
	            dialog.dismiss();
	        }
	    });
	    AlertDialog alert = builder.create();
	    alert.show();
	}
	
	private void uninstall() {
		Log.i(Var.LOGTAG, "Starting the uninstall process");
		if(!Var.rooted) {
			Log.e(Var.LOGTAG, "Can't uninstall because the device isn't rooted");
			Toast.makeText(Var.mAppCtx.getAppContext(), "Can't uninstall because the device isn't rooted!", Toast.LENGTH_SHORT).show();
			return;
		} else {
			Toast.makeText(Var.mAppCtx.getAppContext(), "Uninstalling Towel Pie Root!", Toast.LENGTH_SHORT).show();
			Exec.CMD(Var.mAppCtx.getAppContext().getFilesDir() + "/busybox rm /data/local/tmp/*", true, false);
			Exec.CMD(Var.mAppCtx.getAppContext().getFilesDir() + "/busybox rm /data/local/atvc/*", true, false);
			Exec.CMD(Var.mAppCtx.getAppContext().getFilesDir() + "/busybox rm -r /data/bin", true, false);
			Exec.CMD(Var.mAppCtx.getAppContext().getFilesDir() + "/busybox rm -r /data/xbin", true, false);
			Exec.CMD(Var.mAppCtx.getAppContext().getFilesDir() + "/busybox rm /data/toolbox", true, false);
			Exec.CMD(Var.mAppCtx.getAppContext().getFilesDir() + "/busybox rm /data/bin.img", true, false);
			Exec.CMD(Var.mAppCtx.getAppContext().getFilesDir() + "/busybox rm /data/xbin.img", true, false);
			Exec.CMD(Var.mAppCtx.getAppContext().getFilesDir() + "/busybox rm /data/busybox", true, false);
			Exec.CMD("/system/bin/pm uninstall com.geohot.towelroot; /system/bin/reboot", true, false);
			Log.i(Var.LOGTAG, "Uninstall Complete!");
		}
	}
}
