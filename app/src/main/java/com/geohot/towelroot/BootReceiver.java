package com.geohot.towelroot;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context ctx, Intent bootIntent) {	
		if (Intent.ACTION_BOOT_COMPLETED.equals(bootIntent.getAction())) {
            Intent bootService = new Intent(ctx, BootService.class);
            ctx.startService(bootService);
        }
	}
}
