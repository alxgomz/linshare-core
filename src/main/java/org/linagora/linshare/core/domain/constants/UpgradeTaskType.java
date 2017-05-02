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
package org.linagora.linshare.core.domain.constants;

import org.apache.commons.lang.StringUtils;
import org.linagora.linshare.core.exception.TechnicalErrorCode;
import org.linagora.linshare.core.exception.TechnicalException;

public enum UpgradeTaskType {

	// uuid generation for domains instead of identifier/label
	UPGRADE_2_0_DOMAIN_UUID,

	// uuid generation for domains instead of identifier/label
	UPGRADE_2_0_DOMAIN_POLICIES_UUID,

	/*
	 * initialization quota structure (domain quota and container quota) for all
	 * existing domains
	 */
	UPGRADE_2_0_DOMAIN_QUOTA,

	/*
	 * initialization quota structure for all existing accounts (users and
	 * workgroups)
	 */
	UPGRADE_2_0_ACCOUNT_QUOTA,

	/*
	 * build a operation history with sum of all documents size in order to
	 * initialization default used space.
	 */
	UPGRADE_2_0_SUM_OPERATION_HISTORY,

	/*
	 * Uuid generation for all restricted contacts. Property did not exist in
	 * v1.
	 */
	UPGRADE_2_0_RESTRICTED_CONTACT,

	/*
	 * Trigger the migration of all documents from the old datastore to the new
	 * datastore.
	 */
	UPGRADE_2_0_STORAGE;

	public static UpgradeTaskType fromString(String s) {
		try {
			return UpgradeTaskType.valueOf(s.toUpperCase());
		} catch (RuntimeException e) {
			throw new TechnicalException(TechnicalErrorCode.DATABASE_INCOHERENCE,
					StringUtils.isEmpty(s) ? "null or empty" : s);
		}
	}
}
