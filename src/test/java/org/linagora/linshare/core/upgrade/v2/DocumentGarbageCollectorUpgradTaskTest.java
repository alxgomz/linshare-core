/*
 * LinShare is an open source filesharing software, part of the LinPKI software
 * suite, developed by Linagora.
 * 
 * Copyright (C) 2017 LINAGORA
 * 
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Affero General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version, provided you comply with the Additional Terms applicable for
 * LinShare software by Linagora pursuant to Section 7 of the GNU Affero General
 * Public License, subsections (b), (c), and (e), pursuant to which you must
 * notably (i) retain the display of the “LinShare™” trademark/logo at the top
 * of the interface window, the display of the “You are using the Open Source
 * and free version of LinShare™, powered by Linagora © 2009–2017. Contribute to
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

package org.linagora.linshare.core.upgrade.v2;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.linagora.linshare.core.batches.GenericBatch;
import org.linagora.linshare.core.business.service.DocumentEntryBusinessService;
import org.linagora.linshare.core.domain.constants.LinShareTestConstants;
import org.linagora.linshare.core.domain.entities.DocumentEntry;
import org.linagora.linshare.core.domain.entities.User;
import org.linagora.linshare.core.repository.DocumentEntryRepository;
import org.linagora.linshare.core.repository.DocumentRepository;
import org.linagora.linshare.core.repository.UserRepository;
import org.linagora.linshare.core.runner.BatchRunner;
import org.linagora.linshare.core.service.DocumentEntryService;
import org.linagora.linshare.core.upgrade.v2_1.DocumentGarbageCollectorUpgradeTaskImpl;
import org.linagora.linshare.mongo.entities.DocumentGarbageCollector;
import org.linagora.linshare.mongo.repository.DocumentGarbageCollectorMongoRepository;
import org.linagora.linshare.service.LoadingServiceTestDatas;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import com.google.common.collect.Lists;

@ContextConfiguration(locations = { "classpath:springContext-datasource.xml",
		"classpath:springContext-dao.xml",
		"classpath:springContext-ldap.xml",
		"classpath:springContext-fongo.xml",
		"classpath:springContext-storage-jcloud.xml",
		"classpath:springContext-repository.xml",
		"classpath:springContext-business-service.xml",
		"classpath:springContext-rac.xml",
		"classpath:springContext-service-miscellaneous.xml",
		"classpath:springContext-service.xml",
		"classpath:springContext-batches.xml",
		"classpath:springContext-test.xml",
		"classpath:springContext-upgradeTask.xml" })
public class DocumentGarbageCollectorUpgradTaskTest extends AbstractTransactionalJUnit4SpringContextTests {

	@Autowired
	private BatchRunner batchRunner;

	@Autowired
	@Qualifier("userRepository")
	private UserRepository<User> userRepository;

	@Autowired
	@Qualifier("documentRepository")
	private DocumentRepository documentRepository;

	@Autowired
	@Qualifier("documentEntryRepository")
	private DocumentEntryRepository documentEntryRepository;

	@Autowired
	@Qualifier("documentGarbageCollectorBatch")
	private GenericBatch documentGarbageCollectorBatchImpl;

	@Autowired
	@Qualifier("documentEntryService")
	private DocumentEntryService documentEntryService;

	@Autowired
	@Qualifier("documentEntryBusinessService")
	private DocumentEntryBusinessService documentEntryBusinessService;
	
	@Autowired
	private DocumentGarbageCollectorMongoRepository documentGarbageCollectorMongoRepository;

	@Autowired
	private DocumentGarbageCollectorUpgradeTaskImpl documentGarbageCollectorUpgradeTask;

	private LoadingServiceTestDatas datas;

	private User owner;

	@Before
	public void setUp() throws Exception {
		logger.debug(LinShareTestConstants.BEGIN_SETUP);
		datas = new LoadingServiceTestDatas(userRepository);
		datas.loadUsers();
		owner = datas.getUser1();
		logger.debug(LinShareTestConstants.END_SETUP);
	}

	@After
	public void tearDown() throws Exception {
		logger.debug(LinShareTestConstants.BEGIN_TEARDOWN);
		logger.debug(LinShareTestConstants.END_TEARDOWN);
	}

	@Test
	public void documentGarbageCollectorTest() throws IOException {
		File tempFile = File.createTempFile("linshare-test-1", ".tmp");
		Calendar documentEntryExpiration = Calendar.getInstance();
		DocumentEntry documentEntry = documentEntryBusinessService.createDocumentEntry(owner, tempFile,
				tempFile.length(), "file1", null, false, null, "text/plain", documentEntryExpiration, false, null);
		Assert.assertEquals(documentRepository.findAll().size(), 1);
		Assert.assertEquals(documentEntryRepository.getRelatedDocumentEntryCount(documentEntry.getDocument()), 1);
		List<GenericBatch> batches = Lists.newArrayList();
		batches.add(documentGarbageCollectorUpgradeTask);
		batches.add(documentGarbageCollectorBatchImpl);
		Assert.assertTrue("At least one batch failed.", batchRunner.execute(batches));
		// Delete related entries
		documentEntryService.delete(owner, owner, documentEntry.getUuid());
		Assert.assertEquals(documentRepository.findAll().size(), 1);
		Assert.assertEquals(documentEntryRepository.getRelatedDocumentEntryCount(documentEntry.getDocument()), 0);
		List<DocumentGarbageCollector> documentGarbageCollectors = documentGarbageCollectorMongoRepository.findAll();
		for (DocumentGarbageCollector documentGarbageCollector : documentGarbageCollectors) {
			documentEntryExpiration.add(Calendar.DATE, -1);
			documentGarbageCollector.setCreationDate(documentEntryExpiration.getTime());
			documentGarbageCollectorMongoRepository.save(documentGarbageCollector);
		}
		Assert.assertTrue("At least one batch failed.", batchRunner.execute(batches));
		Assert.assertEquals(documentRepository.findAll().size(), 0);
	}

}
