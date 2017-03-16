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
package org.linagora.linshare.core.business.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.List;
import java.util.Set;

import org.linagora.linshare.core.domain.entities.Account;
import org.linagora.linshare.core.domain.entities.Document;
import org.linagora.linshare.core.domain.entities.DocumentEntry;
import org.linagora.linshare.core.domain.entities.Entry;
import org.linagora.linshare.core.domain.entities.ShareEntry;
import org.linagora.linshare.core.domain.entities.Thread;
import org.linagora.linshare.core.domain.entities.ThreadEntry;
import org.linagora.linshare.core.domain.entities.User;
import org.linagora.linshare.core.exception.BusinessException;

public interface DocumentEntryBusinessService {

	public DocumentEntry createDocumentEntry(Account owner, File myFile, Long size, String fileName, String comment, Boolean checkIfIsCiphered, String timeStampingUrl, String mimeType, Calendar expirationDate, boolean isFromCmis, String metadata) throws BusinessException;

	public DocumentEntry updateDocumentEntry(Account owner, DocumentEntry docEntry, File myFile, Long size, String fileName, Boolean checkIfIsCiphered, String timeStampingUrl, String mimeType, Calendar expirationDate) throws BusinessException ;

	public void deleteDocumentEntry(DocumentEntry documentEntry) throws BusinessException ;

	public byte[] getTimeStamp(String fileName, File tempFile, String timeStampingUrl) throws BusinessException;

	public InputStream getDocumentThumbnailStream(DocumentEntry entry) ;

	public InputStream getDocumentStream(DocumentEntry entry) ;

	public DocumentEntry find(String uuid);

	public List<DocumentEntry> findAllMyDocumentEntries(Account owner);

	public DocumentEntry renameDocumentEntry(DocumentEntry entry, String newName) throws BusinessException;

	public DocumentEntry updateFileProperties(DocumentEntry entry, String newName, String fileComment, String meta) throws BusinessException;

	public long getRelatedEntriesCount(DocumentEntry documentEntry);

	public ThreadEntry createThreadEntry(Thread owner, File myFile, Long size, String fileName, Boolean checkIfIsCiphered, String timeStampingUrl, String mimeType) throws BusinessException;

	ThreadEntry copyFromDocumentEntry(Thread thread,
			DocumentEntry documentEntry)
			throws BusinessException;

	DocumentEntry copyFromThreadEntry(Account owner,
			ThreadEntry threadEntry, Calendar expirationDate)
			throws BusinessException;

	DocumentEntry copyFromShareEntry(Account owner,
			ShareEntry shareEntry, Calendar expirationDate)
			throws BusinessException;

	public ThreadEntry findThreadEntryById(String docEntryUuid);

	public List<ThreadEntry> findAllThreadEntries(Thread owner);

	public long countThreadEntries(Thread thread);

	public InputStream getDocumentStream(ThreadEntry entry);

	public InputStream getThreadEntryThumbnailStream(ThreadEntry entry);

	public void deleteThreadEntry(ThreadEntry threadEntry) throws BusinessException;

	public void deleteSetThreadEntry(Set<Entry> setThreadEntry) throws BusinessException;

	public ThreadEntry updateFileProperties(ThreadEntry entry, String fileComment, String metaData, String newName) throws BusinessException;

	long getUsedSpace(Account owner) throws BusinessException;

	public void update(DocumentEntry docEntry) throws BusinessException;

	List<ThreadEntry> findMoreRecentByName(Thread thread) throws BusinessException;

	DocumentEntry findMoreRecentByName(Account owner, String fileName) throws BusinessException;

	public void syncUniqueDocument(Account owner, String fileName) throws BusinessException;

	List<DocumentEntry> findAllMySyncEntries(Account owner) throws BusinessException;

	String SHA256CheckSumFileStream(File file);

	String SHA1CheckSumFileStream(File file);

	String SHA256CheckSumFileStream(InputStream fs) throws IOException;

	String SHA1CheckSumFileStream(InputStream fs) throws IOException;

	List<String> findAllExpiredEntries();

	void deleteDocument(Document document)
			throws BusinessException;

	DocumentEntry create(User owner, String url, Long size, String fileName, String description,
			String mimeType, Calendar documentExpirationDate);
}
