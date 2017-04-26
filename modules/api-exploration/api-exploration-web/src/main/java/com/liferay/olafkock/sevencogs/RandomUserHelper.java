package com.liferay.olafkock.sevencogs;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Locale;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.liferay.counter.kernel.service.CounterLocalServiceUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Contact;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.CompanyLocalServiceUtil;
import com.liferay.portal.kernel.service.ContactLocalServiceUtil;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.util.StringUtil;

// largely taken from https://github.com/adelcastillo/addUsers (MIT license)
// requires gson to be deployed to Liferay! https://github.com/google/gson

public class RandomUserHelper {
	public static User createRandomUser(UserLocalService userLocalService, long companyId) {
		String randomUserURL = "https://api.randomuser.me/?nat=gb,es"; // just a string
		JsonObject rootobj = null;
		// Connect to the URL using java's native library
		URL url;
		User user = null;
		try {
			url = new URL(randomUserURL);
			
			HttpURLConnection request = (HttpURLConnection) url.openConnection();
			request.connect();

			// Convert to a JSON object to print data
			JsonParser jp = new JsonParser(); // from gson
			JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent()));
			rootobj = root.getAsJsonObject(); // May be an array, may be an object.
			JsonElement results = rootobj.get("results");
			JsonArray array = results.getAsJsonArray();
			rootobj = array.get(0).getAsJsonObject();
			String gender = rootobj.get("gender").getAsString();
		    
		    String firstname = rootobj.get("name").getAsJsonObject().get("first").getAsString();
		    String lastname = rootobj.get("name").getAsJsonObject().get("last").getAsString();
		    firstname = StringUtil.upperCaseFirstLetter(firstname);
		    lastname = StringUtil.upperCaseFirstLetter(lastname);
		    String email =  rootobj.get("email").getAsString();
		    String username = rootobj.get("login").getAsJsonObject().get("username").getAsString();
		    String picture = rootobj.get("picture").getAsJsonObject().get("medium").getAsString();
		
		    Contact newContact = ContactLocalServiceUtil.createContact(CounterLocalServiceUtil.increment(Contact.class.getName()));
		    boolean male = false;
		    if (gender.equals("male"))
				male = true;
		    long[] groupIds = {}; 
		    long[] organizationIds= {};
			long[] roleIds= {};
			long[] userGroupIds= {};
			ContactLocalServiceUtil.updateContact(newContact); 
		    user = userLocalService.addUser(
		    		CompanyLocalServiceUtil.getCompanyById(companyId).getDefaultUser().getUserId(),
		    		companyId, false,
		    		"test", "test", false,
					username, email, (long) 0, "",
					new Locale("en_US"), firstname, "",
					lastname, (long) 0, (long) 0, male,
					1, 1, 1980,
					"", groupIds, organizationIds,
					roleIds, userGroupIds, false,
					null);
			_log.info("Added user: ["+user.getUserId()+"] "+username+" ("+email+")");
			
			
			byte[] portraitBytes = downloadUrl(new URL(picture));
			userLocalService.updatePortrait(user.getUserId(), portraitBytes);
			_log.info("Added Profile pic for: ["+user.getUserId()+"] "+username+" ("+email+")");	
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return user;
	}
	
	private static byte[] downloadUrl(URL toDownload) {
	    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

	    try {
	        byte[] chunk = new byte[4096];
	        int bytesRead;
	        InputStream stream = toDownload.openStream();

	        while ((bytesRead = stream.read(chunk)) > 0) {
	            outputStream.write(chunk, 0, bytesRead);
	        }

	    } catch (IOException e) {
	        e.printStackTrace();
	        return null;
	    }

	    return outputStream.toByteArray();
}
	
	private static Log _log = LogFactoryUtil.getLog(RandomUserHelper.class.getName());
}
