package org.linagora.linshare.core.dao.impl;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.chemistry.opencmis.client.api.CmisObject;
import org.apache.chemistry.opencmis.client.api.Document;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.client.api.SessionFactory;
import org.apache.chemistry.opencmis.client.runtime.SessionFactoryImpl;
import org.apache.chemistry.opencmis.commons.SessionParameter;
import org.apache.chemistry.opencmis.commons.enums.BindingType;

public class TestAlex {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// default factory implementation
		SessionFactory factory = SessionFactoryImpl.newInstance();
		Map<String, String> parameter = new HashMap<String, String>();

		// user credentials
		parameter.put(SessionParameter.USER, "admin");
		parameter.put(SessionParameter.PASSWORD, "admin");

		// connection settings
		parameter.put(SessionParameter.BROWSER_URL, "http://192.168.0.27:8080/alfresco/api/-default-/public/cmis/versions/1.1/browser");
		parameter.put(SessionParameter.BINDING_TYPE, BindingType.BROWSER.value());
		parameter.put(SessionParameter.REPOSITORY_ID, "-default-");

		// create session
		Session session = factory.createSession(parameter);
		
		CmisObject object = session.getObject(session.createObjectId("66345be2-e6af-48e6-8880-d2b086ebdbb7"));
		Document document = (Document) object;
		String filename = document.getName();
		InputStream stream = document.getContentStream().getStream();
		
		
		
		
		// http://192.168.0.27:8080/alfresco/api/-default-/public/cmis/versions/1.1/browser/root?cmisselector=children
				// -uadmin:admin
		//doclib.ftl: http://192.168.0.27:8080/alfresco/api/-default-/public/cmis/versions/1.1/browser/root?objectId=66345be2-e6af-48e6-8880-d2b086ebdbb7
	}

}
