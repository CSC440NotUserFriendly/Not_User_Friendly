package csc440.nuf.cache;

import java.io.*;
import java.net.*;

import android.content.Context;
import android.os.Environment;
import csc440.nuf.*;

import java.util.Enumeration;
import java.util.zip.*;

import org.apache.commons.io.FileUtils;
import org.apache.http.*;
import org.apache.http.Header;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.*;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.impl.client.DefaultHttpClient;

public class Blob {

	private final Context activity;
	private String address;
	private String workingDir;
	private static String key;

	public Blob(Context activity) {
		this.activity = activity;
		address = Util.getBaseUrl(this.activity);
		workingDir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/SMIL";
		
		//String filename = "/SMIL2";
		//key = sendBlob(filename, key);
		//if(key != null)
			//System.out.println(key);
		//getBlob("JNnOIQUad6Lbq5KfWpCqJg", "SMIL2");
	}
	//Name of the message, key is null if it's being created
	public String sendBlob(String filename, String key) {

		try {
			HttpClient httpclient = new DefaultHttpClient();

			String first = getHTML(address + "/upload");

			System.err.println(first);

			// Delete this before deploying
			//This is because my dev machine has a dns running, shouldn't be needed otherwise
			System.out.println("Address Pre:" + first);
			String temp3 = first.replace("NKUVETS.org", "10.206.2.56");
			System.out.println("Address: " + temp3);
			
			
			HttpPost httppost = new HttpPost(temp3);
			
			System.out.println(workingDir); //Working Dir
			
			File f = new File(workingDir + "/" + filename);
			
			/* Remove this if all goes well
			if (!f.exists()){
				f.mkdir();
				//Populate the new directory with the default template
				FileUtils.copyDirectory(new File(workingDir + "/SMILBlank"), f);
			}*/
			
			//if unziped
			if (f.isDirectory()){
				ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(workingDir + "/" + filename + ".zip"));
				zip(f, f, zos);
				zos.close();
			}
			File blobFile = new File(workingDir + "/" + filename + ".zip");
			
			System.out.println(blobFile.getAbsolutePath());
			
			httppost.setHeader("blob-key", key);
			httppost.setHeader("name", filename);
			MultipartEntity entity = new MultipartEntity(
					HttpMultipartMode.BROWSER_COMPATIBLE);
			entity.addPart(filename,
					new ByteArrayBody(FileUtils.readFileToByteArray(blobFile),
							"application/zip", filename));
			httppost.setEntity(entity);
			HttpResponse response = httpclient.execute(httppost);
			Header [] h = response.getHeaders("blob-key");
			
			System.out.println("Return Headers: ");
			for(int i=0; i<h.length; i++){
				System.out.println(h[i].getName() + " : " + h[i].getValue());
				if(h[i].getName().equals("blob-key"))
					return h[i].getValue();
			}

		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Bad URL: " + address);
		}
		return null;
	}
	//Gets Blob, opens objects
	public boolean getBlob(String blobkey, String name){
		boolean successful = true;
		
		try {
			URL url = new URL(address + "/blobserve?" + "blob-key=" + blobkey);
			URLConnection connection = url.openConnection();
			connection.connect();
			
			if(connection.getContentType().equals("application/zip")){
				String path = workingDir + "/" + name + ".zip";
				
				FileOutputStream fos = new FileOutputStream(path);
				InputStream is = connection.getInputStream();
				byte[] buffer = new byte[1024];
				int ByteRead;
				
				while ((ByteRead = is.read(buffer)) != -1) {
		            fos.write(buffer, 0, ByteRead);
		        }
				is.close();
				fos.close();
				
				File out = new File(workingDir + "/" + name);
				if(!out.exists())
					out.mkdir();
				unzip(new File(path), out);
			}
			else{
				successful = false;
			}
			
		} catch (Exception e) {
			System.err.println("Get Blob Failure: " + e);
			successful = false;
		}
		return successful;
	}

	private void zip(File directory, File base, ZipOutputStream zos) {
		try {
			File[] files = directory.listFiles();
			byte[] buffer = new byte[8192];
			int read = 0;
			for (int i = 0, n = files.length; i < n; i++) {
				if (files[i].isDirectory()) {
					zip(files[i], base, zos);
				} else {
					FileInputStream in = new FileInputStream(files[i]);
					ZipEntry entry = new ZipEntry(files[i].getPath().substring(
							base.getPath().length() + 1));
					zos.putNextEntry(entry);
					while (-1 != (read = in.read(buffer))) {
						zos.write(buffer, 0, read);
					}
					in.close();
				}
			}
		} catch (Exception e) {
			System.err.println("Zip Error: " + e);
		}
	}

	public void unzip(File zip, File extractTo) {
		try {
			ZipFile archive = new ZipFile(zip);
			Enumeration<? extends ZipEntry> e = archive.entries();
			while (e.hasMoreElements()) {
				ZipEntry entry = (ZipEntry) e.nextElement();
				File file = new File(extractTo, entry.getName());
				if (entry.isDirectory() && !file.exists()) {
					file.mkdirs();
				} else {
					if (!file.getParentFile().exists()) {
						file.getParentFile().mkdirs();
					}

					InputStream in = archive.getInputStream(entry);
					BufferedOutputStream out = new BufferedOutputStream(
							new FileOutputStream(file));

					byte[] buffer = new byte[8192];
					int read;

					while (-1 != (read = in.read(buffer))) {
						out.write(buffer, 0, read);
					}

					in.close();
					out.close();
				}
			}
		} catch (Exception e) {
			System.err.println("UnZip Error: " + e);
		}
	}

	public String getHTML(String urlToRead) {
		URL url;
		HttpURLConnection conn;
		BufferedReader rd;
		String line;
		String result = "";
		try {
			url = new URL(urlToRead);
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			rd = new BufferedReader(
					new InputStreamReader(conn.getInputStream()));
			while ((line = rd.readLine()) != null) {
				result += line;
			}
			rd.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

}
