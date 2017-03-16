/*
 * LinShare is an open source filesharing software, part of the LinPKI software
 * suite, developed by Linagora.
 * 
 * Copyright (C) 2016 LINAGORA
 * 
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Affero General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version, provided you comply with the Additional Terms applicable for
 * LinShare software by Linagora pursuant to Section 7 of the GNU Affero General
 * Public License, subsections (b), (c), and (e), pursuant to which you must
 * notably (i) retain the display of the “LinShare™” trademark/logo at the top
 * of the interface window, the display of the “You are using the Open Source
 * and free version of LinShare™, powered by Linagora © 2009–2015. Contribute to
 * Linshare R&D by subscribing to an Enterprise offer!” infobox and in the
 * e-mails sent with the Program, (ii) retain all hypertext links between
 * LinShare and linshare.org, between linagora.com and Linagora, and (iii)
 * refrain from infringing Linagora intellectual property rights over its
 * trademarks and commercial brands. Other Additional Terms apply, see
 * <http://www.linagora.com/licenses/> for more details.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Affero General Public License and
 * its applicable Additional Terms for LinShare along with this program. If not,
 * see <http://www.gnu.org/licenses/> for the GNU Affero General Public License
 * version 3 and <http://www.linagora.com/licenses/> for the Additional Terms
 * applicable to LinShare software.
 */

package org.linagora.linshare.core.dao.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
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
import org.apache.cxf.helpers.IOUtils;
import org.linagora.linshare.core.dao.FileDataStore;
import org.linagora.linshare.core.domain.objects.FileMetaData;
import org.linagora.linshare.core.exception.BusinessErrorCode;
import org.linagora.linshare.core.exception.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AlfrescoFileDataStoreImpl implements FileDataStore {

	protected Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private FileDataStore linshareDataStore;

	private Session session;
	
	public AlfrescoFileDataStoreImpl(FileDataStore newDataStore) {
		super();
		this.linshareDataStore = newDataStore;
		
		SessionFactory factory = SessionFactoryImpl.newInstance();
		Map<String, String> parameter = new HashMap<String, String>();

		// user credentials
		parameter.put(SessionParameter.USER, "admin");
		parameter.put(SessionParameter.PASSWORD, "admin");

		// connection settings
		parameter.put(SessionParameter.BROWSER_URL, "http://192.168.0.27:8080/alfresco/api/-default-/public/cmis/versions/1.1/browser");
		parameter.put(SessionParameter.BINDING_TYPE, BindingType.BROWSER.value());
		parameter.put(SessionParameter.REPOSITORY_ID, "-default-");

		session = factory.createSession(parameter);
		
	}

	@Override
	public void remove(FileMetaData metadata) {
		if (linshareDataStore.exists(metadata)) {
			linshareDataStore.remove(metadata);
		}
	}

	@Override
	public FileMetaData add(File file, FileMetaData metadata) {
		return linshareDataStore.add(file, metadata);
	}

	@Override
	public FileMetaData add(InputStream inputStream, FileMetaData metadata) {
		return linshareDataStore.add(inputStream, metadata);
	}

	@Override
	public InputStream get(FileMetaData metadata) {
		
		// http://192.168.0.27:8080/alfresco/api/-default-/public/cmis/versions/1.1/browser/root?cmisselector=children
		// -uadmin:admin
		// create session
				
				
		CmisObject object = session.getObject(session.createObjectId("66345be2-e6af-48e6-8880-d2b086ebdbb7"));
		Document document = (Document) object;
		String filename = document.getName();
		InputStream stream = document.getContentStream().getStream();
		File tempFile = getTempFile(stream, "connard", "dtc");
		try {
			FileInputStream fileInputStream = new FileInputStream(tempFile);
			return fileInputStream;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;	
				
//		InputStream inputStream = null;	
//		if (linshareDataStore.exists(metadata)) {
//			inputStream = linshareDataStore.get(metadata);
//		} else {
////			inputStream = oldDataStore.get(metadata);
//		}
//		return inputStream;
	}

	protected File getTempFile(InputStream theFile, String discriminator, String fileName) {
		if (discriminator == null)  {
			discriminator = "";
		}
		// Legacy code, we need to extract extension for the dirty unstable LinThumbnail Module.
		// I hope some day we get rid of it !
		String extension = null;
		if (fileName != null) {
			int splitIdx = fileName.lastIndexOf('.');
			if (splitIdx > -1) {
				extension = fileName.substring(splitIdx, fileName.length());
			}
		}
		File tempFile = null;
		try {
			tempFile = File.createTempFile("linshare-" + discriminator + "-", extension);
			tempFile.deleteOnExit();
			IOUtils.transferTo(theFile, tempFile);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
			throw new BusinessException(
					BusinessErrorCode.FILE_INVALID_INPUT_TEMP_FILE,
					"Can not generate temp file from input stream.");
		}
		return tempFile;
	}
	
	@Override
	public boolean exists(FileMetaData metadata) {
		if (linshareDataStore.exists(metadata)) {
			return true;
		} else {
			return false;
//			return oldDataStore.exists(metadata);
		}
	}

}
