/*
 * Copyright © 2014 - 2016 | Wurst-Imperium | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.updater;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.swing.UIManager;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

public class Main
{
	public static File currentDirectory;
	public static File wurstJar;
	public static File newWurstJar;
	public static File wurstJSON;
	public static File newWurstJSON;
	public static File tmp;
	private static ProgressDialog progress;
	
	public static void main(final String[] args)
	{
		Thread thread = new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				try
				{
					UIManager.setLookAndFeel(
						UIManager.getSystemLookAndFeelClassName());
				}catch(Exception e)
				{
					e.printStackTrace();
				}
				try
				{
					if(args != null && args.length == 4
						&& args[0].equals("update"))
					{
						currentDirectory =
							new File(args[2].replace("%20", " "));
						String releaseName = currentDirectory.getName();
						wurstJar =
							new File(currentDirectory, releaseName + ".jar");
						newWurstJar =
							new File(currentDirectory, "Wurst-update.jar");
						wurstJSON =
							new File(currentDirectory, releaseName + ".json");
						newWurstJSON =
							new File(currentDirectory, "Wurst-update.json");
						tmp = new File(currentDirectory, "Wurst-update.tmp");
						progress = new ProgressDialog();
						Thread thread = new Thread(new Runnable()
						{
							@Override
							public void run()
							{
								// Thread needed because setVisible() freezes
								progress.setVisible(true);
							}
						});
						thread.start();
						download(args[1], args[3]);
						extract(releaseName);
						install();
						System.exit(0);
					}else
						System.err.println("Syntax:\n"
							+ "    update <release_id> <path> <repository>");
				}catch(Exception e)
				{
					e.printStackTrace();
				}
			}
		});
		thread.setPriority(Thread.MIN_PRIORITY);
		thread.start();
	}
	
	private static void download(String id, String repository) throws Exception
	{
		JsonArray json = new JsonParser()
			.parse(new InputStreamReader(new URL("https://api.github.com/repos/"
				+ repository + "/releases/" + id + "/assets").openStream()))
			.getAsJsonArray();
		URL downloadUrl = new URL(json.get(0).getAsJsonObject()
			.get("browser_download_url").getAsString());
		long bytesTotal = downloadUrl.openConnection().getContentLengthLong();
		InputStream input = downloadUrl.openStream();
		FileOutputStream output = new FileOutputStream(tmp);
		byte[] buffer = new byte[8192];
		long bytesDownloaded = 0;
		for(int length; (length = input.read(buffer)) != -1;)
		{
			bytesDownloaded += length;
			if(bytesDownloaded > 0)
			{
				String percent = ((float)(short)((float)bytesDownloaded
					/ (float)bytesTotal * 1000F) / 10F) + "%";
				String data =
					((float)(int)((float)bytesDownloaded * 1000F / 1048576F)
						/ 1000F) + " / "
						+ ((float)(int)((float)bytesTotal * 1000F / 1048576F)
							/ 1000F)
						+ " MB";
				progress.updateProgress("Downloading Update: " + percent, data);
				System.out.println(
					"Downloading Update: " + percent + " (" + data + ")");
			}
			output.write(buffer, 0, length);
		}
		input.close();
		output.close();
	}
	
	private static void extract(String releaseName) throws Exception
	{
		progress.updateProgress("Extracting update...", "");
		System.out.println("Extracting update...");
		ZipInputStream input = new ZipInputStream(new FileInputStream(tmp));
		byte[] buffer = new byte[8192];
		for(ZipEntry entry; (entry = input.getNextEntry()) != null;)
		{
			if(entry.isDirectory())
				continue;
			File file;
			if(entry.getName().equals(releaseName + "/" + releaseName + ".jar"))
				file = newWurstJar;
			else if(entry.getName()
				.equals(releaseName + "/" + releaseName + ".json"))
				file = newWurstJSON;
			else
				file = new File(currentDirectory, entry.getName());
			FileOutputStream output = new FileOutputStream(file);
			for(int length; (length = input.read(buffer)) != -1;)
			{
				output.write(buffer, 0, length);
			}
			output.close();
		}
		input.close();
		tmp.delete();
	}
	
	private static void install() throws Exception
	{
		while((wurstJar.exists() && !wurstJar.delete())
			|| !newWurstJar.renameTo(wurstJar)
			|| (wurstJSON.exists() && !wurstJSON.delete())
			|| !newWurstJSON.renameTo(wurstJSON))
		{
			progress.updateProgress("Update ready",
				"Restart Minecraft to install it.");
			System.out
				.println("Update ready - Restart Minecraft to install it.");
			Thread.sleep(500);
		}
		System.out.println("Done.");
		progress.dispose();
	}
}
