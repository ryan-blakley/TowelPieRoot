package com.geohot.towelroot;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class Exec {
	
	public static String cmdOutput;

	public static void CMD(String cmd, boolean root, boolean returnOutput) {
		cmdOutput = null;
		String shell = "sh";
		if(root) shell = "su";
        final StringBuilder output = new StringBuilder();
        Process process;
        BufferedReader bReader = null;
        try {
        	process = Runtime.getRuntime().exec(shell);
        	DataOutputStream oStream = new DataOutputStream(process.getOutputStream());
        	oStream.writeBytes(cmd + "\n");
        	bReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        	oStream.writeBytes("exit\n");
        	if(root) oStream.writeBytes("exit\n");
        	oStream.flush();
        	String line;
        	String separator = System.getProperty("line.separator");
        	while ((line = bReader.readLine()) != null) {
        		output.append(line);
        		output.append(separator);
        	} try {
        		process.waitFor();
        	} catch (InterruptedException e) {
        		e.printStackTrace();
        	}
        } catch (IOException e) {
        	e.printStackTrace();
        }
        if(returnOutput) cmdOutput = output.toString();
	}
	
	public static int intCMD(String cmd, boolean root) {
		String shell = "sh";
		if(root) shell = "su";
		int returnVal = 5;
        Process process;
        try {
        	process = Runtime.getRuntime().exec(shell);
        	DataOutputStream oStream = new DataOutputStream(process.getOutputStream());
        	oStream.writeBytes(cmd + "\n");
        	oStream.writeBytes("exit\n");
        	if(root) oStream.writeBytes("exit\n");
        	oStream.flush();
        	try {
        		returnVal = process.waitFor();
        	} catch (InterruptedException e) {
        		e.printStackTrace();
        	}
        } catch (IOException e) {
        	e.printStackTrace();
        }
        return returnVal;
	}
}
