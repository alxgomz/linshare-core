/*
 *    This file is part of Linshare.
 *
 *   Linshare is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU Affero General Public License as
 *   published by the Free Software Foundation, either version 3 of
 *   the License, or (at your option) any later version.
 *
 *   Linshare is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Affero General Public License for more details.
 *
 *   You should have received a copy of the GNU Affero General Public
 *   License along with Foobar.  If not, see
 *                                    <http://www.gnu.org/licenses/>.
 *
 *   (c) 2008 Groupe Linagora - http://linagora.org
 *
*/
package org.linagora.linshare.core.Facade.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.linagora.linshare.core.Facade.SecuredUrlFacade;
import org.linagora.linshare.core.domain.entities.Contact;
import org.linagora.linshare.core.domain.entities.Document;
import org.linagora.linshare.core.domain.entities.MailContainer;
import org.linagora.linshare.core.domain.entities.SecuredUrl;
import org.linagora.linshare.core.domain.entities.User;
import org.linagora.linshare.core.domain.transformers.impl.DocumentAdapter;
import org.linagora.linshare.core.domain.vo.DocumentVo;
import org.linagora.linshare.core.domain.vo.UserVo;
import org.linagora.linshare.core.exception.BusinessException;
import org.linagora.linshare.core.exception.LinShareNotSuchElementException;
import org.linagora.linshare.core.repository.DocumentRepository;
import org.linagora.linshare.core.repository.UserRepository;
import org.linagora.linshare.core.service.MailContentBuildingService;
import org.linagora.linshare.core.service.NotifierService;
import org.linagora.linshare.core.service.SecuredUrlService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SecuredUrlFacadeImpl implements SecuredUrlFacade {

	private static final Logger logger = LoggerFactory.getLogger(SecuredUrlFacadeImpl.class);
	
	private final SecuredUrlService securedUrlService;
	
	private final DocumentAdapter documentAdapter;
	
	private final NotifierService notifierService;
	
	private final UserRepository<User> userRepository;

	private final DocumentRepository documentRepository;
    
    private final MailContentBuildingService mailElementsFactory;
	
	public SecuredUrlFacadeImpl(SecuredUrlService securedUrlService,DocumentAdapter documentAdapter,
			final NotifierService notifierService,
			final UserRepository<User> userRepository,
			final DocumentRepository documentRepository,
    		final MailContentBuildingService mailElementsFactory) {
		this.securedUrlService=securedUrlService;
		this.documentAdapter=documentAdapter;
		this.notifierService = notifierService;
		this.userRepository = userRepository;
		this.documentRepository = documentRepository;
		this.mailElementsFactory = mailElementsFactory;
	}

	@Override
	public DocumentVo getDocument(String alea, String urlPath,
			Integer documentId) throws BusinessException {
		return  documentAdapter.disassemble(securedUrlService.getDocument(alea, urlPath, documentId));
	}

	@Override
	public DocumentVo getDocument(String alea, String urlPath, String password,
			Integer documentId) throws BusinessException {
		return documentAdapter.disassemble(securedUrlService.getDocument(alea, urlPath, password, documentId));
	}

	@Override
	public List<DocumentVo> getDocuments(String alea, String urlPath)
			throws BusinessException {
		return documentAdapter.disassembleDocList(securedUrlService.getDocuments(alea, urlPath));
	}

	@Override
	public List<DocumentVo> getDocuments(String alea, String urlPath,
			String password) throws BusinessException {
		return documentAdapter.disassembleDocList(securedUrlService.getDocuments(alea, urlPath, password));
	}

	@Override
	public boolean isPasswordProtected(String alea, String urlPath) throws LinShareNotSuchElementException {
		return securedUrlService.isProtectedByPassword(alea, urlPath);
	}

	@Override
	public boolean isValid(String alea, String urlPath) {
		return securedUrlService.isValid(alea, urlPath);
	}

	@Override
	public boolean isValid(String alea, String urlPath, String password) {
		return securedUrlService.isValid(alea, urlPath, password);
	}

	@Override
	public boolean exists(String alea, String urlPath) {
		return securedUrlService.exists(alea,urlPath);
	}
	
	
	@Override
	public void logDownloadedDocument(String alea, String urlPath, String password,
			Integer documentId, String email) {
		securedUrlService.logDownloadedDocument(alea, urlPath, password, documentId, email) ;
	}

	@Override
	public void sendEmailNotification(String alea, String urlPath, MailContainer mailContainer, List<DocumentVo> docs, String email) throws BusinessException {
		
		User owner = securedUrlService.getSecuredUrlOwner(alea, urlPath);
		List<Document> docList = new ArrayList<Document>();
		for (DocumentVo documentVo : docs) {
			docList.add(documentRepository.findByUuid(documentVo.getIdentifier()));
		}
		
		//send a notification by mail to the owner
		notifierService.sendAllNotifications(mailElementsFactory.buildMailAnonymousDownloadWithOneRecipient(owner, mailContainer, docList, email, owner));
	}
	
}
