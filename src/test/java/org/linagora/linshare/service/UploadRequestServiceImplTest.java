/*
 * LinShare is an open source filesharing software, part of the LinPKI software
 * suite, developed by Linagora.
 * 
 * Copyright (C) 2015 LINAGORA
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

package org.linagora.linshare.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

import org.apache.cxf.helpers.IOUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.linagora.linshare.core.dao.FileDataStore;
import org.linagora.linshare.core.domain.constants.FileMetaDataKind;
import org.linagora.linshare.core.domain.constants.LinShareTestConstants;
import org.linagora.linshare.core.domain.constants.UploadRequestStatus;
import org.linagora.linshare.core.domain.entities.AbstractDomain;
import org.linagora.linshare.core.domain.entities.Account;
import org.linagora.linshare.core.domain.entities.Contact;
import org.linagora.linshare.core.domain.entities.Document;
import org.linagora.linshare.core.domain.entities.UploadRequest;
import org.linagora.linshare.core.domain.entities.UploadRequestEntry;
import org.linagora.linshare.core.domain.entities.UploadRequestGroup;
import org.linagora.linshare.core.domain.entities.User;
import org.linagora.linshare.core.domain.objects.FileMetaData;
import org.linagora.linshare.core.exception.BusinessException;
import org.linagora.linshare.core.repository.AbstractDomainRepository;
import org.linagora.linshare.core.repository.ContactRepository;
import org.linagora.linshare.core.repository.DocumentRepository;
import org.linagora.linshare.core.repository.UploadRequestEntryRepository;
import org.linagora.linshare.core.repository.UserRepository;
import org.linagora.linshare.core.service.UploadRequestEntryService;
import org.linagora.linshare.core.service.UploadRequestGroupService;
import org.linagora.linshare.core.service.UploadRequestService;
import org.linagora.linshare.utils.LinShareWiser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import com.google.common.collect.Lists;

@ContextConfiguration(locations = { "classpath:springContext-datasource.xml",
		"classpath:springContext-repository.xml",
		"classpath:springContext-dao.xml",
		"classpath:springContext-ldap.xml",
		"classpath:springContext-business-service.xml",
		"classpath:springContext-service-miscellaneous.xml",
		"classpath:springContext-service.xml",
		"classpath:springContext-rac.xml",
		"classpath:springContext-fongo.xml",
		"classpath:springContext-storage-jcloud.xml",
		"classpath:springContext-test.xml", })
public class UploadRequestServiceImplTest extends AbstractTransactionalJUnit4SpringContextTests {
	private static Logger logger = LoggerFactory.getLogger(UploadRequestServiceImplTest.class);

	@Qualifier("userRepository")
	@Autowired
	private UserRepository<User> userRepository;

	@Autowired
	private UploadRequestGroupService uploadRequestGroupService;
	
	@Autowired
	private ContactRepository repository;

	@Autowired
	private UploadRequestService service;

	@Autowired
	private AbstractDomainRepository abstractDomainRepository;

	@Autowired
	private UploadRequestEntryService uploadRequestEntryService;

	@Autowired
	private UploadRequestEntryRepository uploadRequestEntryRepository;

	@Qualifier("jcloudFileDataStore")
	@Autowired
	private FileDataStore fileDataStore;

	@Autowired
	private DocumentRepository documentRepository;

	private UploadRequestEntry uploadRequestEntry;

	private UploadRequest ure = new UploadRequest();

	private LoadingServiceTestDatas datas;

	private UploadRequest eJohn;

	private UploadRequest eJane;

	private User john;

	private User jane;

	private final InputStream stream = Thread.currentThread().getContextClassLoader().getResourceAsStream("linshare-default.properties");

	private final String fileName = "linshare-default.properties";

	private final String comment = "file description";

	private Contact yoda;

	private LinShareWiser wiser;

	public UploadRequestServiceImplTest() {
		super();
		wiser = new LinShareWiser(2525);
	}

	@Before
	public void init() throws Exception {
		logger.debug(LinShareTestConstants.BEGIN_SETUP);
		this.executeSqlScript("import-tests-default-domain-quotas.sql", false);
		this.executeSqlScript("import-tests-quota-other.sql", false);
		this.executeSqlScript("import-tests-upload-request.sql", false);
		wiser.start();
		datas = new LoadingServiceTestDatas(userRepository);
		datas.loadUsers();
		john = datas.getUser1();
		jane = datas.getUser2();
		AbstractDomain subDomain = abstractDomainRepository.findById(LoadingServiceTestDatas.sqlSubDomain);
		yoda = repository.findByMail("yoda@linshare.org");
		john.setDomain(subDomain);
//		UPLOAD REQUEST CREATE
		ure.setCanClose(true);
		ure.setMaxDepositSize((long) 100);
		ure.setMaxFileCount(new Integer(3));
		ure.setMaxFileSize((long) 50);
		ure.setStatus(UploadRequestStatus.CREATED);
		ure.setExpiryDate(new Date());
		ure.setSecured(false);
		ure.setCanEditExpiryDate(true);
		ure.setCanDelete(true);
		ure.setLocale("en");
		ure.setActivationDate(new Date());
		UploadRequestGroup uploadRequestGroupJohn = uploadRequestGroupService.create(john, john, ure, Lists.newArrayList(yoda), "This is a subject",
				"This is a body", false);
		eJohn = uploadRequestGroupJohn.getUploadRequests().iterator().next();
		UploadRequestGroup uploadRequestGroupJane = uploadRequestGroupService.create(jane, jane, ure, Lists.newArrayList(yoda), "This is a subject",
				"This is a body", false);
		eJane = uploadRequestGroupJane.getUploadRequests().iterator().next();
//		END OF UPLOAD REQUEST CREATE
		Assert.assertEquals(john, (User) eJohn.getUploadRequestGroup().getOwner());
		Assert.assertEquals(eJohn.getStatus(), UploadRequestStatus.ENABLED);
		logger.debug(LinShareTestConstants.END_SETUP);
	}

	@After
	public void tearDown() throws Exception {
		logger.debug(LinShareTestConstants.BEGIN_TEARDOWN);
		wiser.stop();
		logger.debug(LinShareTestConstants.END_TEARDOWN);
	}

	@Test
	public void findRequest() throws BusinessException {
		logger.info(LinShareTestConstants.BEGIN_TEST);
		UploadRequest tmp = service.find(john, john, eJohn.getUuid());
		Assert.assertEquals(tmp.getStatus(), eJohn.getStatus());
		Assert.assertEquals(tmp.getUploadRequestGroup().getOwner(), eJohn.getUploadRequestGroup().getOwner());
		Assert.assertEquals(tmp.getStatus(), eJohn.getStatus());
		Assert.assertEquals(tmp.getMaxDepositSize(), eJohn.getMaxDepositSize());
		Assert.assertEquals(tmp.getMaxFileCount(), eJohn.getMaxFileCount());
		Assert.assertEquals(tmp.getMaxFileSize(), eJohn.getMaxFileSize());
		Assert.assertEquals(tmp.isSecured(), eJohn.isSecured());
		Assert.assertEquals(tmp.isCanClose(), eJohn.isCanClose());
		Assert.assertEquals(tmp.isCanDelete(), eJohn.isCanDelete());
		Assert.assertEquals(tmp.getUploadRequestGroup().getAbstractDomain(), eJohn.getUploadRequestGroup().getAbstractDomain());
		logger.debug(LinShareTestConstants.END_TEST);
	}

	@Test
	public void update() throws BusinessException {
		logger.info(LinShareTestConstants.BEGIN_TEST);
		UploadRequest tmp = service.find(john, john, eJohn.getUuid());
		tmp.setCanClose(false);
		tmp.setCanDelete(false);
		tmp.setCanEditExpiryDate(false);
		tmp.setMaxFileCount(new Integer(2));
		tmp = service.update(john, john, tmp.getUuid(), tmp);
		Assert.assertEquals(tmp.isCanClose(), false);
		Assert.assertEquals(tmp.isCanDelete(), false);
		Assert.assertEquals(tmp.isCanEditExpiryDate(), false);
		Assert.assertEquals(tmp.getMaxFileCount(), new Integer(2));
		wiser.checkGeneratedMessages();
		logger.debug(LinShareTestConstants.END_TEST);
	}

	@Test
	public void UpdateUploadRequestStatus() throws BusinessException {
		logger.info(LinShareTestConstants.BEGIN_TEST);
		UploadRequest tmp = eJohn.clone();
		tmp = service.updateStatus(john, john, tmp.getUuid(), UploadRequestStatus.CLOSED, false);
		Assert.assertEquals(tmp.getStatus(), UploadRequestStatus.CLOSED);
		Assert.assertEquals(john, (User) eJohn.getUploadRequestGroup().getOwner());
		// Status ARCHIVED
		tmp = service.updateStatus(john, john, tmp.getUuid(), UploadRequestStatus.ARCHIVED, true);
		Assert.assertEquals(tmp.getStatus(), UploadRequestStatus.ARCHIVED);
		Assert.assertEquals(john, (User) eJohn.getUploadRequestGroup().getOwner());
		// STATUS DELETED
		tmp = service.updateStatus(john, john, tmp.getUuid(), UploadRequestStatus.DELETED, false);
		Assert.assertEquals(tmp.getStatus(), UploadRequestStatus.DELETED);
		Assert.assertEquals(john, (User) eJohn.getUploadRequestGroup().getOwner());
		wiser.checkGeneratedMessages();
		logger.debug(LinShareTestConstants.END_TEST);
	}

	@Test
	public void closeRequestByRecipient() throws BusinessException {
		logger.info(LinShareTestConstants.BEGIN_TEST);
		UploadRequest tmp = eJohn.clone();
		tmp = service.closeRequestByRecipient(eJohn.getUploadRequestURLs().iterator().next());
		Assert.assertEquals(tmp.getStatus(), UploadRequestStatus.CLOSED);
		Assert.assertEquals(john, (User) eJohn.getUploadRequestGroup().getOwner());
		wiser.checkGeneratedMessages();
		logger.debug(LinShareTestConstants.END_TEST);
	}

	@Test
	public void updateStatusWithError() throws BusinessException {
		logger.info(LinShareTestConstants.BEGIN_TEST);
		try {
			eJohn.updateStatus(UploadRequestStatus.CLOSED);
			UploadRequest tmp = service.updateRequest(john, john, eJohn);
			tmp = service.find(john, john, eJohn.getUuid());
			tmp.updateStatus(UploadRequestStatus.ENABLED);
			tmp = service.updateRequest(john, john, tmp);
		} catch (BusinessException ex) {
			Assert.assertEquals("Cannot transition from CLOSED to ENABLED.", ex.getMessage());
		}
		wiser.checkGeneratedMessages();
		logger.debug(LinShareTestConstants.END_TEST);
	}

	@Test
	public void testUploadRequestCreateDocumentEntry() throws BusinessException, IOException {
		logger.info(LinShareTestConstants.BEGIN_TEST);
		Account actor = jane;
		File tempFile = File.createTempFile("linshare-test-", ".tmp");
		IOUtils.transferTo(stream, tempFile);
		uploadRequestEntry = uploadRequestEntryService.create(actor, actor, tempFile, fileName, comment, false, null,
				eJohn.getUploadRequestURLs().iterator().next());
		Assert.assertTrue(uploadRequestEntryRepository.findByUuid(uploadRequestEntry.getUuid()) != null);
		Document aDocument = uploadRequestEntry.getDocument();
		uploadRequestEntryRepository.delete(uploadRequestEntry);
		jane.getEntries().clear();
		userRepository.update(jane);
		FileMetaData metadata = new FileMetaData(FileMetaDataKind.THUMBNAIL_SMALL, aDocument, "image/png");
		metadata.setUuid(aDocument.getUuid());
		fileDataStore.remove(metadata);
		documentRepository.delete(aDocument);
		logger.debug(LinShareTestConstants.END_TEST);
	}

	@Test
	public void testFindAllUploadRequestEntries()
			throws BusinessException, IOException {
		logger.info(LinShareTestConstants.BEGIN_TEST);
		Account actor = jane;
		File tempFile = File.createTempFile("linshare-test-", ".tmp");
		IOUtils.transferTo(stream, tempFile);
		uploadRequestEntry = uploadRequestEntryService.create(actor, actor, tempFile, fileName, comment, false, null,
				eJane.getUploadRequestURLs().iterator().next());
		Assert.assertTrue(uploadRequestEntryRepository.findByUuid(uploadRequestEntry.getUuid()) != null);

		List<UploadRequestEntry> entries = service.findAllEntries(actor, actor, eJane.getUuid());
		Assert.assertNotNull(entries);
		logger.debug(LinShareTestConstants.END_TEST);
	}
}
