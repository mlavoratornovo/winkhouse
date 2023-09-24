package winkhouse.model.winkcloud.helpers;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import winkhouse.model.winkcloud.HTTPConnector;
import winkhouse.model.winkcloud.HttpServerConnector;
import winkhouse.model.winkcloud.restmsgs.MessageCode;
import winkhouse.model.winkcloud.restmsgs.MessageStatusCheck;
import winkhouse.model.winkcloud.restmsgs.QueryByCode;
import winkhouse.model.winkcloud.restmsgs.ResponseByCodeQueryName;
import winkhouse.model.winkcloud.restmsgs.StandardRestMessage;
import winkhouse.util.WinkhouseUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class HTTPHelper {

	private final static String CHKSTATUS_CODICE_ACTION = "wink_get_query_code_controller.php?action=checkstatus";
	private final static String NUOVO_CODICE_ACTION = "wink_get_query_code_controller.php?action=nuovo_codice";
	private final static String POST_QUERYREQUEST_ACTION = "wink_get_query_code_controller.php?action=upload_file";
	private final static String POST_DOWNLOADFILE_ACTION = "wink_get_query_code_controller.php?action=search_query_file";
	private final static String POST_RESPONSEQUERYREQUEST_ACTION = "wink_get_query_code_controller.php?action=upload_file_response";
	private final static String POST_DOWNLOAD_RESPONSEQUERY_ACTION = "wink_get_query_code_controller.php?action=download_file_response";
	private final static String POST_REST_QUERY_ACTION = "wink_get_query_code_controller.php?action=reset_search_query_file";
	private final static String POST_DELETE_QUERY_ACTION = "wink_get_query_code_controller.php?action=delete_search_query_file";
	private final static String POST_DELETE_CODE_ACTION = "wink_get_query_code_controller.php?action=delete_code";
	private final static String POST_LIST_QUERIES_BY_CODE_ACTION = "wink_get_query_code_controller.php?action=list_queries_by_code";
	private final static String POST_QUERY_FILE_EXIST_ACTION = "wink_get_query_code_controller.php?action=query_file_exist";
	private final static String POST_DOWNLOAD_FILE_BY_ID = "wink_get_query_code_controller.php?action=download_file_by_id";
	private final static String POST_LIST_RESPOSEQUERY_BY_CODE_QUERY_FILENAME = "wink_get_query_code_controller.php?action=list_response_by_code";
	
	public HTTPHelper() {
		
	}
	
	public boolean checkStatus(HttpServerConnector connector){
		
//		HttpClient client = HttpClientBuilder.create().build();
//		HttpGet request = new HttpGet((connector.getUrl().endsWith("/"))
//                					? connector.getUrl() + CHKSTATUS_CODICE_ACTION 
//                					: connector.getUrl() + "/" + CHKSTATUS_CODICE_ACTION);				
//		try {
//			HttpResponse response = client.execute(request);
//			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
//
//			StringBuffer result = new StringBuffer();
//			String line = "";
//			while ((line = rd.readLine()) != null) {
//				result.append(line);
//			}
//			
//			GsonBuilder builder = new GsonBuilder();
//	        Gson gson = builder.create();
//	        
//	        MessageStatusCheck rcm = gson.fromJson(result.toString(), MessageStatusCheck.class);
//	        if (rcm.message.startsWith("WinkCloudQuerySystem_v_1")){
//	        	return true;
//	        }else{
//	        	return false;
//	        }
//		} catch (ClientProtocolException e) {
//			return false;
//		} catch (IOException e) {
//			return false;
//		}
		return true;
	}
	
	public String getCode(HttpServerConnector connector){
		
		HttpClient client = HttpClientBuilder.create().build();
		HttpGet request = new HttpGet((connector.getUrl().endsWith("/"))
				? connector.getUrl() + NUOVO_CODICE_ACTION 
				: connector.getUrl() + "/" + NUOVO_CODICE_ACTION);				
				
		try {
			HttpResponse response = client.execute(request);
			BufferedReader rd = new BufferedReader(
									new InputStreamReader(response.getEntity().getContent()));

			StringBuffer result = new StringBuffer();
			String line = "";
			while ((line = rd.readLine()) != null) {
				result.append(line);
			}
			
			GsonBuilder builder = new GsonBuilder();
	        Gson gson = builder.create();
	        
	        try{
	        	MessageCode rcm = gson.fromJson(result.toString(), MessageCode.class);
	        	return rcm.code;
			}catch(Exception e){
				return null;
			}

		} catch (ClientProtocolException e) {
			return null;
		} catch (IOException e) {
			return null;
		}

	}
	
	public boolean uploadQueryRequest(HTTPConnector connector, File queryRequestZipFile, String code){
		
		boolean returnValue = false;
		
		CloseableHttpClient client = HttpClients.createDefault();
		HttpPost post = new HttpPost((connector.getUrl().endsWith("/"))
				 ? connector.getUrl() + POST_QUERYREQUEST_ACTION 
				 : connector.getUrl() + "/" + POST_QUERYREQUEST_ACTION);
				
		FileBody fileBody = new FileBody(queryRequestZipFile);
		StringBody sbCode = new StringBody(code, ContentType.TEXT_PLAIN);		
		StringBody sbWinkcloudid = new StringBody(WinkhouseUtils.getInstance()
																.getPreferenceStore()
																.getString("winkcloudid")
																.trim(), ContentType.TEXT_PLAIN);
		
		MultipartEntityBuilder builder = MultipartEntityBuilder.create();
		builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
		builder.addPart("qfileToUpload", fileBody);
		builder.addPart("winkcode", sbCode);
		builder.addPart("winkcloudid", sbWinkcloudid);
		
		HttpEntity entity = builder.build();

		post.setEntity(entity);
		System.out.println("content lenght : " + String.valueOf(post.getEntity().getContentLength())); 
		System.out.println("content type : " + String.valueOf(post.getEntity().getContentType()));
		System.out.println("content encoding : " + String.valueOf(post.getEntity().getContentEncoding()));
		
		HttpEntity resEntity = null;
		try {
			
			CloseableHttpResponse response = client.execute(post);
			resEntity = response.getEntity();
			System.out.println(response.getStatusLine());
            
            if (resEntity != null) {
                System.out.println("Response content length: " + resEntity.getContentLength());
            }            
            
			BufferedReader rd = new BufferedReader(new InputStreamReader(resEntity.getContent()));
			StringBuffer result = new StringBuffer();
			String line = "";
			while ((line = rd.readLine()) != null) {
				result.append(line);
			}
						
			
			GsonBuilder jsonBuilder = new GsonBuilder();
			Gson gson = jsonBuilder.create();
			try{
				StandardRestMessage muOk = gson.fromJson(result.toString(), StandardRestMessage.class);
				if (muOk == null){
					return false;
				}else{
					if (muOk.code != 200){
						return false;
					}
				}
				return true;
			}catch(Exception e){
				return false;
			}
			
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				EntityUtils.consume(resEntity);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return returnValue;
		
	}

	public boolean downloadQueryRequest(HTTPConnector connector, String queryRequestFileName, String code, File downloadFile){
		
		boolean returnValue = false;
		
		CloseableHttpClient client = HttpClients.createDefault();
		HttpPost post = new HttpPost((connector.getUrl().endsWith("/"))
				 ? connector.getUrl() + POST_DOWNLOADFILE_ACTION 
				 : connector.getUrl() + "/" + POST_DOWNLOADFILE_ACTION);
	
		StringBody sbCode = new StringBody(code, ContentType.TEXT_PLAIN);
		StringBody sbqfilename = new StringBody(queryRequestFileName, ContentType.TEXT_PLAIN);
		StringBody sbWinkcloudid = new StringBody(WinkhouseUtils.getInstance()
																.getPreferenceStore()
																.getString("winkcloudid")
																.trim(), ContentType.TEXT_PLAIN);
		
		MultipartEntityBuilder builder = MultipartEntityBuilder.create();
		builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
		builder.addPart("qfilename", sbqfilename);
		builder.addPart("winkcode", sbCode);
		builder.addPart("winkcloudid", sbWinkcloudid);
		
		HttpEntity entity = builder.build();

		post.setEntity(entity);
		System.out.println("content lenght : " + String.valueOf(post.getEntity().getContentLength())); 
		System.out.println("content type : " + String.valueOf(post.getEntity().getContentType()));
		System.out.println("content encoding : " + String.valueOf(post.getEntity().getContentEncoding()));
		
		HttpEntity resEntity = null;
				
		try {
			CloseableHttpResponse response = client.execute(post);
			resEntity = response.getEntity();
			System.out.println(response.getStatusLine());
            
            if (resEntity != null) {
                System.out.println("Response content length: " + resEntity.getContentLength());
            }            

            if (resEntity != null) {
	        	 
	        	 BufferedInputStream bInputStream = new BufferedInputStream(resEntity.getContent());
	        	 BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(downloadFile));
	        	 int inByte;
	        	 while((inByte = bInputStream.read()) != -1) bos.write(inByte);
	        	 bInputStream.close();
	        	 bos.close();
	        	 returnValue = true;	        	 
	         }
			
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();		
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return returnValue;
		
	}
	
	public boolean uploadResponse2QueryRequest(HTTPConnector connector, File queryResponseZipFile, String code, String queryFileName){
		
		boolean returnValue = false;
		
		CloseableHttpClient client = HttpClients.createDefault();
		HttpPost post = new HttpPost((connector.getUrl().endsWith("/"))
				 ? connector.getUrl() + POST_RESPONSEQUERYREQUEST_ACTION 
				 : connector.getUrl() + "/" + POST_RESPONSEQUERYREQUEST_ACTION);

		FileBody fileBody = new FileBody(queryResponseZipFile);
		StringBody sbCode = new StringBody(code, ContentType.TEXT_PLAIN);
		StringBody pqueryFileName = new StringBody(queryFileName, ContentType.TEXT_PLAIN);
		StringBody sbCloudID = new StringBody(WinkhouseUtils.getInstance()
															.getPreferenceStore()
															.getString("winkcloudid")
															.trim(), ContentType.TEXT_PLAIN);		
		
		MultipartEntityBuilder builder = MultipartEntityBuilder.create();
		builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
		builder.addPart("rfileToUpload", fileBody);
		builder.addPart("winkcode", sbCode);
		builder.addPart("qfilename", pqueryFileName);
		builder.addPart("winkcloudid", sbCloudID);
		
		HttpEntity entity = builder.build();

		post.setEntity(entity);
		System.out.println("content lenght : " + String.valueOf(post.getEntity().getContentLength())); 
		System.out.println("content type : " + String.valueOf(post.getEntity().getContentType()));
		System.out.println("content encoding : " + String.valueOf(post.getEntity().getContentEncoding()));
		
		HttpEntity resEntity = null;
		try {
			CloseableHttpResponse response = client.execute(post);
			resEntity = response.getEntity();
			System.out.println(response.getStatusLine());
            
            if (resEntity != null) {
                System.out.println("Response content length: " + resEntity.getContentLength());
            }            
            
			BufferedReader rd = new BufferedReader(new InputStreamReader(resEntity.getContent()));
			StringBuffer result = new StringBuffer();
			String line = "";
			while ((line = rd.readLine()) != null) {
				result.append(line);
			}
						
			GsonBuilder jsonBuilder = new GsonBuilder();
			Gson gson = jsonBuilder.create();
			try{
				StandardRestMessage muOk = gson.fromJson(result.toString(), StandardRestMessage.class);
				if (muOk == null){
					return false;
				}else{
					if (muOk.code != 200){
						return false;
					}
				}
				return true;
			}catch(Exception e){
				return false;
			}
			
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				EntityUtils.consume(resEntity);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return returnValue;
		
	}
	
	public boolean downloadResponse(HTTPConnector connector, String code, int idfile, File downloadFile){
		
		boolean returnValue = false;
		CloseableHttpClient client = HttpClients.createDefault();
		HttpPost post = new HttpPost((connector.getUrl().endsWith("/"))
				 ? connector.getUrl() + POST_DOWNLOAD_FILE_BY_ID 
				 : connector.getUrl() + "/" + POST_DOWNLOAD_FILE_BY_ID);		

		StringBody sbidfile = new StringBody(String.valueOf(idfile), ContentType.TEXT_PLAIN);
		StringBody sbCode = new StringBody(code, ContentType.TEXT_PLAIN);		
		
		MultipartEntityBuilder builder = MultipartEntityBuilder.create();
		builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
		builder.addPart("idfile", sbidfile);
		builder.addPart("winkcode", sbCode);
		
		HttpEntity entity = builder.build();

		post.setEntity(entity);
		HttpEntity resEntity = null;		
		try {
			CloseableHttpResponse response = client.execute(post);
			resEntity = response.getEntity();
									
	         if (resEntity != null) {
	        	 long len = resEntity.getContentLength();
	        	 if (resEntity.getContentType().getValue().equalsIgnoreCase("application/json")){
	        		 returnValue = false;
//		        	 BufferedReader rd = new BufferedReader(new InputStreamReader(resEntity.getContent()));
//		 			 StringBuffer result = new StringBuffer();
//		 			 String line = "";
//		 			 while ((line = rd.readLine()) != null) {
//		 				 result.append(line);
//		 			 }
	        	 }else{
		 			 BufferedInputStream bInputStream = new BufferedInputStream(resEntity.getContent());
		        	 BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(downloadFile));
		        	 int inByte;
		        	 while((inByte = bInputStream.read()) != -1) bos.write(inByte);
		        	 bInputStream.close();
		        	 bos.close();
		        	 returnValue = true;	        	 	        		 
	        	 }
	 			 
	         }
			
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();		
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				EntityUtils.consume(resEntity);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return returnValue;
		
	}

	public boolean resetQueryRequest(HTTPConnector connector, String queryRequestFileName, String code){
		
		boolean returnValue = false;
		HttpClient client = HttpClientBuilder.create().build();
		HttpPost post = new HttpPost((connector.getUrl().endsWith("/"))
				 ? connector.getUrl() + POST_REST_QUERY_ACTION 
				 : connector.getUrl() + "/" + POST_REST_QUERY_ACTION);
					
		List<NameValuePair> params = new ArrayList<NameValuePair>();
	    params.add(new BasicNameValuePair("winkcode", code));
	    params.add(new BasicNameValuePair("qfilename", queryRequestFileName));
		
		try {
			post.setEntity(new UrlEncodedFormEntity(params));			
			
			HttpResponse response = client.execute(post);
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			StringBuffer result = new StringBuffer();
			String line = "";
			while ((line = rd.readLine()) != null) {
				result.append(line);
			}

			GsonBuilder jsonBuilder = new GsonBuilder();
			Gson gson = jsonBuilder.create();
			try{
				StandardRestMessage muOk = gson.fromJson(result.toString(), StandardRestMessage.class);
				if (muOk == null){
					return false;
				}else{
					if (muOk.code != 200){
						return false;
					}
				}
				return true;

			}catch(Exception e){
				return false;
			}
				
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();		
		}
		return returnValue;
		
	}
	
	public boolean deleteQueryRequest(HTTPConnector connector, String code, String queryRequestFileName){
		
		boolean returnValue = false;
		HttpClient client = HttpClientBuilder.create().build();
		HttpPost post = new HttpPost((connector.getUrl().endsWith("/"))
				 ? connector.getUrl() + POST_DELETE_QUERY_ACTION 
				 : connector.getUrl() + "/" + POST_DELETE_QUERY_ACTION);
					
		List<NameValuePair> params = new ArrayList<NameValuePair>();
	    params.add(new BasicNameValuePair("winkcode", code));
	    params.add(new BasicNameValuePair("qfilename", queryRequestFileName));
		
		try {
			post.setEntity(new UrlEncodedFormEntity(params));			
			
			HttpResponse response = client.execute(post);
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			StringBuffer result = new StringBuffer();
			String line = "";
			while ((line = rd.readLine()) != null) {
				result.append(line);
			}

			GsonBuilder jsonBuilder = new GsonBuilder();
			Gson gson = jsonBuilder.create();
			try{
				StandardRestMessage muOk = gson.fromJson(result.toString(), StandardRestMessage.class);
				if (muOk == null){
					return false;
				}else{
					if (muOk.code != 200){
						return false;
					}
				}

			}catch(Exception e){
				return false;
			}
				
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();		
		}
		return returnValue;
		
	}

	public boolean deleteCode(HTTPConnector connector, String code){
		
		boolean returnValue = false;
		HttpClient client = HttpClientBuilder.create().build();
		
		HttpPost post = new HttpPost((connector.getUrl().endsWith("/"))
				                     ? connector.getUrl() + POST_DELETE_QUERY_ACTION 
				                     : connector.getUrl() + "/" + POST_DELETE_QUERY_ACTION);
		
		List<NameValuePair> params = new ArrayList<NameValuePair>();
	    params.add(new BasicNameValuePair("winkcode", code));	    
		
		try {
			post.setEntity(new UrlEncodedFormEntity(params));			
			
			HttpResponse response = client.execute(post);
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			StringBuffer result = new StringBuffer();
			String line = "";
			while ((line = rd.readLine()) != null) {
				result.append(line);
			}

			GsonBuilder jsonBuilder = new GsonBuilder();
			Gson gson = jsonBuilder.create();
			try{
				StandardRestMessage muOk = gson.fromJson(result.toString(), StandardRestMessage.class);
				if (muOk == null){
					return false;
				}else{
					if (muOk.code != 200){
						return false;
					}
				}

			}catch(Exception e){
				return false;
			}
				
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();		
		}
		return returnValue;
		
	}

	public QueryByCode[] getQueriesByCode(HTTPConnector connector, String code){
		
		QueryByCode[] returnValue = null;
		
		CloseableHttpClient client = HttpClients.createDefault();
		HttpPost post = new HttpPost((connector.getUrl().endsWith("/"))
				 					 ? connector.getUrl() + POST_LIST_QUERIES_BY_CODE_ACTION 
				 					 : connector.getUrl() + "/" + POST_LIST_QUERIES_BY_CODE_ACTION);
				
		StringBody sbCode = new StringBody(code, ContentType.TEXT_PLAIN);		
		StringBody sbWinkcloudid = new StringBody(WinkhouseUtils.getInstance()
																.getPreferenceStore()
																.getString("winkcloudid")
																.trim(), ContentType.TEXT_PLAIN);
		
		MultipartEntityBuilder builder = MultipartEntityBuilder.create();
		builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);		
		builder.addPart("winkcode", sbCode);
		builder.addPart("winkcloudid", sbWinkcloudid);
		
		HttpEntity entity = builder.build();

		post.setEntity(entity);
		
		HttpEntity resEntity = null;
		
		try {
			
			CloseableHttpResponse response = client.execute(post);
			resEntity = response.getEntity();
			System.out.println(response.getStatusLine());
            
            if (resEntity != null) {
                System.out.println("Response content length: " + resEntity.getContentLength());
            }            
            
			BufferedReader rd = new BufferedReader(new InputStreamReader(resEntity.getContent()));
			StringBuffer result = new StringBuffer();
			String line = "";
			while ((line = rd.readLine()) != null) {
				result.append(line);
			}
						
			
			GsonBuilder jsonBuilder = new GsonBuilder();
			Gson gson = jsonBuilder.create();
			try{
				returnValue = gson.fromJson(result.toString(), QueryByCode[].class);
			}catch(Exception e){
				returnValue = null;
			}
			
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				EntityUtils.consume(resEntity);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return returnValue;
		
	}
	
	public ResponseByCodeQueryName[] getQueryResposesByCodeQueryName(HTTPConnector connector, String code, String queryName){
		
		ResponseByCodeQueryName[] returnValue = null;
		
		CloseableHttpClient client = HttpClients.createDefault();
		HttpPost post = new HttpPost((connector.getUrl().endsWith("/"))
				 					 ? connector.getUrl() + POST_LIST_RESPOSEQUERY_BY_CODE_QUERY_FILENAME 
				 					 : connector.getUrl() + "/" + POST_LIST_RESPOSEQUERY_BY_CODE_QUERY_FILENAME);
				
		StringBody sbCode = new StringBody(code, ContentType.TEXT_PLAIN);		
		StringBody sbQfilename = new StringBody(queryName, ContentType.TEXT_PLAIN);
		
		MultipartEntityBuilder builder = MultipartEntityBuilder.create();
		builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);		
		builder.addPart("winkcode", sbCode);
		builder.addPart("qfilename", sbQfilename);
		
		HttpEntity entity = builder.build();

		post.setEntity(entity);
		
		HttpEntity resEntity = null;
		
		try {
			
			CloseableHttpResponse response = client.execute(post);
			resEntity = response.getEntity();
			System.out.println(response.getStatusLine());
            
            if (resEntity != null) {
                System.out.println("Response content length: " + resEntity.getContentLength());
            }            
            
			BufferedReader rd = new BufferedReader(new InputStreamReader(resEntity.getContent()));
			StringBuffer result = new StringBuffer();
			String line = "";
			
			while ((line = rd.readLine()) != null) {
				result.append(line);
			}
					
			
			GsonBuilder jsonBuilder = new GsonBuilder();
			Gson gson = jsonBuilder.create();
			try{
				returnValue = gson.fromJson(result.toString(), ResponseByCodeQueryName[].class);
			}catch(Exception e){
				returnValue = null;
			}
			
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				EntityUtils.consume(resEntity);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return returnValue;
		
	}
	
	public boolean queryFileExist(HTTPConnector connector, String queryRequestFileName, String code){
		
		boolean returnValue = false;
		HttpClient client = HttpClientBuilder.create().build();
		HttpPost post = new HttpPost((connector.getUrl().endsWith("/"))
									 ? connector.getUrl() + POST_QUERY_FILE_EXIST_ACTION 
									 : connector.getUrl() + "/" + POST_QUERY_FILE_EXIST_ACTION);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
	    params.add(new BasicNameValuePair("winkcode", code));
	    params.add(new BasicNameValuePair("qfilename", queryRequestFileName));
		
		try {
			post.setEntity(new UrlEncodedFormEntity(params));			
			
			HttpResponse response = client.execute(post);
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			StringBuffer result = new StringBuffer();
			String line = "";
			while ((line = rd.readLine()) != null) {
				result.append(line);
			}

			GsonBuilder jsonBuilder = new GsonBuilder();
			Gson gson = jsonBuilder.create();
			try{
				StandardRestMessage muOk = gson.fromJson(result.toString(), StandardRestMessage.class);
				if (muOk == null){
					return false;
				}else{
					if (muOk.code != 200){
						return false;
					}
				}
				return true;
			}catch(Exception e){
				return false;
			}
				
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();		
		}
		return returnValue;
		
	}


}
