/*
 * LinShare is an open source filesharing software, part of the LinPKI software
 * suite, developed by Linagora.
 * 
 * Copyright (C) 2018 LINAGORA
 * 
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Affero General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version, provided you comply with the Additional Terms applicable for
 * LinShare software by Linagora pursuant to Section 7 of the GNU Affero General
 * Public License, subsections (b), (c), and (e), pursuant to which you must
 * notably (i) retain the display of the “LinShare™” trademark/logo at the top
 * of the interface window, the display of the “You are using the Open Source
 * and free version of LinShare™, powered by Linagora © 2009–2018. Contribute to
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

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.linagora.linshare.core.domain.constants.LinShareTestConstants;
import org.linagora.linshare.core.domain.entities.Account;
import org.linagora.linshare.core.domain.entities.Guest;
import org.linagora.linshare.core.domain.entities.User;
import org.linagora.linshare.core.repository.UserRepository;
import org.linagora.linshare.core.service.GuestService;
import org.linagora.linshare.core.service.JwtLongTimeService;
import org.linagora.linshare.core.service.impl.JwtServiceImpl;
import org.linagora.linshare.mongo.entities.PermanentToken;
import org.linagora.linshare.utils.LinShareWiser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import io.jsonwebtoken.Claims;

@ContextConfiguration(locations = { 
		"classpath:springContext-datasource.xml",
		"classpath:springContext-repository.xml",
		"classpath:springContext-dao.xml",
		"classpath:springContext-service.xml",
		"classpath:springContext-business-service.xml",
		"classpath:springContext-rac.xml",
		"classpath:springContext-fongo.xml",
		"classpath:springContext-storage-jcloud.xml",
		"classpath:springContext-service-miscellaneous.xml",
		"classpath:springContext-test.xml",
		"classpath:springContext-ldap.xml" })
public class JwtLongTimeServiceImplTest extends AbstractTransactionalJUnit4SpringContextTests {

	private final String TOKEN_LABEL = "token label";

	private final String TOKEN_DESC = "token description";

	@Autowired
	private JwtServiceImpl jwtService;

	@Autowired
	private JwtLongTimeService jwtLongTimeService;

	private LinShareWiser wiser;

	@Autowired
	@Qualifier("userRepository")
	private UserRepository<User> userRepository;

	LoadingServiceTestDatas datas;

	private User john;

	private User jane;

	private Account root;

	@Autowired
	private GuestService guestService;

	public JwtLongTimeServiceImplTest() {
		super();
		wiser = new LinShareWiser(2525);
	}

	@Before
	public void setUp() {
		logger.debug(LinShareTestConstants.BEGIN_SETUP);
		this.executeSqlScript("import-tests-jwt-long-time-functionnality.sql", false);
		this.executeSqlScript("import-tests-default-domain-quotas.sql", false);
		wiser.start();
		datas = new LoadingServiceTestDatas(userRepository);
		datas.loadUsers();
		john = datas.getUser1();
		jane = datas.getUser2();
		root = datas.getRoot();
		logger.debug(LinShareTestConstants.END_SETUP);
	}

	@After
	public void tearDown() throws Exception {
		logger.debug(LinShareTestConstants.BEGIN_TEARDOWN);
		wiser.stop();
		logger.debug(LinShareTestConstants.END_TEARDOWN);
	}

	@Test
	public void createTokenTest() {
		logger.info(LinShareTestConstants.BEGIN_TEST);
		PermanentToken johnToken = new PermanentToken(TOKEN_LABEL, TOKEN_DESC);
		PermanentToken token = jwtLongTimeService.create(john, john, johnToken);
		Claims decode = jwtService.decode(token.getToken());
		logger.debug("Token:" + decode.toString());
		assertEquals(john.getMail(), decode.getSubject());
		assertEquals(null, decode.getExpiration());
		assertEquals(token.getLabel(), TOKEN_LABEL);
		assertEquals(token.getDescription(), TOKEN_DESC);
		logger.info(LinShareTestConstants.END_TEST);
	}

	@Test
	public void findAllByActorTest() {
		logger.info(LinShareTestConstants.BEGIN_TEST);
		PermanentToken janeToken = new PermanentToken(TOKEN_LABEL, null);
		PermanentToken johnToken = new PermanentToken(TOKEN_LABEL, null);
		for (int i = 0; i < 5; i++) {
			jwtLongTimeService.create(jane, jane, janeToken);
			jwtLongTimeService.create(john, john, johnToken);
		}
		List<PermanentToken> mongoEntities = jwtLongTimeService.findAll(jane, jane);
		assertEquals(5, mongoEntities.size());
		for (PermanentToken entity : mongoEntities) {
			assertEquals(jane.getMail(), entity.getSubject());
			assertEquals(entity.getDescription(), null);
		}
		logger.info(LinShareTestConstants.END_TEST);
	}

	@Test
	public void findTest() {
		logger.info(LinShareTestConstants.BEGIN_TEST);
		PermanentToken janeToken = new PermanentToken(TOKEN_LABEL, null);
		janeToken = jwtLongTimeService.create(jane, jane, janeToken);
		PermanentToken found = jwtLongTimeService.find(jane, jane, janeToken.getUuid());
		assertEquals(jane.getMail(), found.getSubject());
		jwtLongTimeService.delete(jane, jane, found);
		logger.info(LinShareTestConstants.END_TEST);
	}

	@Test
	public void findAllByDomainTest() {
		logger.info(LinShareTestConstants.BEGIN_TEST);
		PermanentToken johnToken = new PermanentToken(TOKEN_LABEL, TOKEN_DESC);
		int initSize = jwtLongTimeService.findAllByDomain(root, john.getDomain(), false).size();
		for (int i = 0; i < 5; i++) {
			jwtLongTimeService.create(john, john, johnToken);
		}
		List<PermanentToken> mongoEntities = jwtLongTimeService.findAllByDomain(root, john.getDomain(), false);
		assertEquals(initSize + 5, mongoEntities.size());
		Guest guest = new Guest("Guest", "Doe", "guest1@linshare.org");
		guest.setCmisLocale("en");
		guest = guestService.create(john, john, guest, null);
		PermanentToken guestToken = new PermanentToken(TOKEN_LABEL, TOKEN_DESC);
		int initSizeGuest = jwtLongTimeService.findAllByDomain(root, guest.getDomain(), true).size();
		for (int i = 0; i < 5; i++) {
			jwtLongTimeService.create(root, guest, guestToken);
		}
		List<PermanentToken> mongoEntitiesRecursive = jwtLongTimeService.findAllByDomain(root, john.getDomain(), true);
		assertEquals(initSize + initSizeGuest + 10, mongoEntitiesRecursive.size());
		logger.info(LinShareTestConstants.END_TEST);
	}

	@Test
	public void updateTokenTest() {
		logger.info(LinShareTestConstants.BEGIN_TEST);
		PermanentToken johnToken = new PermanentToken(TOKEN_LABEL, TOKEN_DESC);
		PermanentToken token = jwtLongTimeService.create(john, john, johnToken);
		assertEquals(token.getLabel(), TOKEN_LABEL);
		assertEquals(token.getDescription(), TOKEN_DESC);
		token.setLabel("New label");
		token.setDescription("New description");
		PermanentToken updated = jwtLongTimeService.update(john, john, token.getUuid(), token);
		assertEquals(updated.getUuid(), token.getUuid());
		assertEquals(updated.getLabel(), "New label");
		assertEquals(updated.getDescription(), "New description");
		logger.info(LinShareTestConstants.END_TEST);
	}

	@Test
	public void deleteTokenTest() {
		logger.info(LinShareTestConstants.BEGIN_TEST);
		PermanentToken johnToken = new PermanentToken(TOKEN_LABEL, TOKEN_DESC);
		PermanentToken token = jwtLongTimeService.create(john, john, johnToken);
		PermanentToken deleted = jwtLongTimeService.delete(john, john, token);
		assertEquals(token.getUuid(), deleted.getUuid());
		assertEquals(token.getLabel(), deleted.getLabel());
		assertEquals(deleted.getActor().getUuid(), john.getLsUuid());
		logger.info(LinShareTestConstants.END_TEST);
	}

	@Test
	public void deleteTokenByAdminTest() {
		logger.info(LinShareTestConstants.BEGIN_TEST);
		PermanentToken johnToken = new PermanentToken(TOKEN_LABEL, null);
		PermanentToken token = jwtLongTimeService.create(john, john, johnToken);
		PermanentToken deleted = jwtLongTimeService.delete(root, john, token);
		assertEquals(token.getUuid(), deleted.getUuid());
		assertEquals(token.getLabel(), deleted.getLabel());
		assertEquals(deleted.getActor().getUuid(), john.getLsUuid());
		wiser.checkGeneratedMessages();
		logger.info(LinShareTestConstants.END_TEST);
	}

	@Test
	public void sendMailOnCreateByAdminTest() {
		logger.info(LinShareTestConstants.BEGIN_TEST);
		PermanentToken johnToken = new PermanentToken(TOKEN_LABEL, TOKEN_DESC);
		jwtLongTimeService.create(root, john, johnToken);
		wiser.checkGeneratedMessages();
		logger.info(LinShareTestConstants.END_TEST);
	}

}
