/*
 * LinShare is an open source filesharing software, part of the LinPKI software
 * suite, developed by Linagora.
 * 
 * Copyright (C) 2013 LINAGORA
 * 
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Affero General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version, provided you comply with the Additional Terms applicable for
 * LinShare software by Linagora pursuant to Section 7 of the GNU Affero General
 * Public License, subsections (b), (c), and (e), pursuant to which you must
 * notably (i) retain the display of the “LinShare™” trademark/logo at the top
 * of the interface window, the display of the “You are using the Open Source
 * and free version of LinShare™, powered by Linagora © 2009–2013. Contribute to
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

package org.linagora.linshare.core.domain.vo;

import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry5.beaneditor.Validate;
import org.linagora.linshare.core.domain.entities.MailingList;
import org.linagora.linshare.core.domain.entities.MailingListContact;

public class MailingListVo {

	private long persistenceId;
	private String identifier;
	private String listDescription;
	private boolean isPublic;
	private UserVo owner;
	private AbstractDomainVo domain;
	private List<MailingListContactVo> mails;

	public MailingListVo() {
	}

	public MailingListVo(MailingList list) {

		this.persistenceId = list.getPersistenceId();
		this.identifier = list.getIdentifier();
		this.listDescription = list.getDescription();
		this.isPublic = list.isPublic();
		this.owner = new UserVo(list.getOwner());
		this.domain = new AbstractDomainVo(list.getDomain());
		mails =new ArrayList<MailingListContactVo>();
		for(MailingListContact current : list.getMails()) {
			mails.add(new MailingListContactVo(current));
		}
	}

	public MailingListVo(MailingListVo list) {
		this.persistenceId = list.getPersistenceId();
		this.identifier = list.getIdentifier();
		this.listDescription = list.getListDescription();
		this.isPublic = list.isPublic();
		this.owner = list.getOwner();
		this.domain = list.getDomain();
		this.mails = list.getMails();
	}

	public MailingListVo(long id, String identifier, String description,
			boolean isPublic, UserVo owner, AbstractDomainVo domain,
			List<MailingListContactVo> mails) {
		this.persistenceId = id;
		this.identifier = identifier;
		this.listDescription = description;
		this.isPublic = isPublic;
		this.owner = owner;
		this.domain = domain;
		this.mails = mails;
	}

	public long getPersistenceId() {
		return persistenceId;
	}

	public void setPersistenceId(long persistenceId) {
		this.persistenceId = persistenceId;
	}

	@Validate("required")
	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public String getListDescription() {
		return listDescription;
	}

	public void setListDescription(String description) {
		this.listDescription = description;
	}

	public boolean isPublic() {
		return isPublic;
	}

	public void setPublic(boolean isPublic) {
		this.isPublic = isPublic;
	}

	public UserVo getOwner() {
		return owner;
	}

	public void setOwner(UserVo owner) {
		this.owner = owner;
	}

	public AbstractDomainVo getDomain() {
		return domain;
	}

	public void setDomain(AbstractDomainVo domain) {
		this.domain = domain;
	}

	public List<MailingListContactVo> getMails() {
		return mails;
	}
	
	public void addContact(MailingListContactVo contact) {
		mails.add(contact);
	}

	public void setMails(List<MailingListContactVo> mails) {
		this.mails = mails;
	}

}
