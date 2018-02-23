/*
 * LinShare is an open source filesharing software, part of the LinPKI software
 * suite, developed by Linagora.
 * 
 * Copyright (C) 2015-2018 LINAGORA
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
package org.linagora.linshare.core.exception;

import javax.ws.rs.core.Response.Status;

/** Exception error code.
 */
public enum BusinessErrorCode implements ErrorCode {

	UNKNOWN(1000, Status.INTERNAL_SERVER_ERROR),
	BATCH_INCOMPLETE(1001, Status.INTERNAL_SERVER_ERROR),
	INVALID_CONFIGURATION(1002, Status.METHOD_NOT_ALLOWED),

	AUTHENTICATION_ERROR(2000),
	DATABASE_INCOHERENCE_NO_ROOT_DOMAIN(2001, Status.INTERNAL_SERVER_ERROR),
	USER_NOT_FOUND(2200, Status.NOT_FOUND),
	DUPLICATE_USER_ENTRY(2201),
	CANNOT_DELETE_USER(2203, Status.FORBIDDEN),
	CANNOT_UPDATE_USER(2204, Status.FORBIDDEN),
	USER_CANNOT_CREATE_GUEST(2205, Status.FORBIDDEN),
	USER_CANNOT_DELETE_GUEST(2206, Status.FORBIDDEN),
	USER_CANNOT_UPDATE_GUEST(2207, Status.FORBIDDEN),
	USER_FORBIDDEN(2208, Status.FORBIDDEN),
	USER_ALREADY_EXISTS_IN_DOMAIN_TARGET(2209),
	MIME_NOT_FOUND(3000, Status.NOT_FOUND),
	FILE_MIME_NOT_ALLOWED(3002, Status.FORBIDDEN),
	FILE_CONTAINS_VIRUS(3003, Status.FORBIDDEN),
	FILE_MIME_WARNING(3004),
	FILE_ENCRYPTION_UNDEFINED(3005),
	FILE_TIMESTAMP_NOT_COMPUTED(3006, Status.INTERNAL_SERVER_ERROR),
	FILE_SCAN_FAILED(3007, Status.INTERNAL_SERVER_ERROR),
	FILE_TIMESTAMP_WRONG_TSA_URL(3008, Status.INTERNAL_SERVER_ERROR),
	FILE_UNREACHABLE(3009),
	INVALID_FILENAME(3010),
	FILE_INVALID_INPUT_TEMP_FILE(3011, Status.BAD_REQUEST),
	INVALID_UUID(4000),
	SHARED_DOCUMENT_NOT_FOUND(5000, Status.NOT_FOUND),
	CANNOT_SHARE_DOCUMENT(5001),
	CANNOT_DELETE_SHARED_DOCUMENT(5002),
	SHARE_NOT_FOUND(5003, Status.NOT_FOUND),
	SHARE_MISSING_RECIPIENTS(5400, Status.BAD_REQUEST),
	SHARE_WRONG_EXPIRY_DATE_AFTER(5401, Status.BAD_REQUEST),
	SHARE_WRONG_EXPIRY_DATE_BEFORE(5402, Status.BAD_REQUEST),
	SHARE_WRONG_USDA_NOTIFICATION_DATE_AFTER(5403, Status.BAD_REQUEST),
	SHARE_WRONG_USDA_NOTIFICATION_DATE_BEFORE(5404, Status.BAD_REQUEST),
	NO_SUCH_ELEMENT(6000, Status.NOT_FOUND),
	METHOD_NOT_ALLOWED(6000, Status.METHOD_NOT_ALLOWED),
	CANNOT_SIGN_DOCUMENT(9001),
	CANNOT_ENCRYPT_GENERATE_KEY(9002),
	CANNOT_ENCRYPT_DOCUMENT(9003),
	CANNOT_DECRYPT_DOCUMENT(9004),
	WRONG_URL(10000),
	SECURED_URL_IS_EXPIRED(12000),
	SECURED_URL_BAD_PASSWORD(12001),
	SECURED_URL_WRONG_DOCUMENT_ID(12002),
	DOMAIN_ID_ALREADY_EXISTS(13000),
	DOMAIN_ID_NOT_FOUND(13001, Status.NOT_FOUND),
	DOMAIN_INVALID_TYPE(13002),
	DOMAIN_POLICY_NOT_FOUND(13003, Status.NOT_FOUND),
	LDAP_CONNECTION_NOT_FOUND(13004, Status.NOT_FOUND),
	DOMAIN_PATTERN_NOT_FOUND(13005, Status.NOT_FOUND),
	DOMAIN_BASEDN_NOT_FOUND(13006),
	DOMAIN_INVALID_OPERATION(13007, Status.BAD_REQUEST),
	DOMAIN_DO_NOT_EXIST(13008, Status.NOT_FOUND),
	DOMAIN_POLICY_ALREADY_EXISTS(13009, Status.BAD_REQUEST),
	DOMAIN_ID_BAD_FORMAT(13010),
	LDAP_CONNECTION_ID_BAD_FORMAT(13011),
	DOMAIN_PATTERN_ID_BAD_FORMAT(13012),
	LDAP_CONNECTION_ID_ALREADY_EXISTS(13013),
	DOMAIN_PATTERN_ID_ALREADY_EXISTS(13014),
	LDAP_CONNECTION_CANNOT_BE_REMOVED(13015, Status.BAD_REQUEST),
	DOMAIN_PATTERN_CANNOT_BE_REMOVED(13016, Status.BAD_REQUEST),
	LDAP_CONNECTION_STILL_IN_USE(13017, Status.FORBIDDEN),
	DOMAIN_PATTERN_STILL_IN_USE(13018, Status.FORBIDDEN),
	DOMAIN_HAS_ACCESS_RULES(13019, Status.CONFLICT),
	FUNCTIONALITY_ENTITY_OUT_OF_DATE(14000),
	UNAUTHORISED_FUNCTIONALITY_UPDATE_ATTEMPT(14001),
	FUNCTIONALITY_NOT_FOUND(14004, Status.NOT_FOUND),
	RELAY_HOST_NOT_ENABLE(15000),
	XSSFILTER_SCAN_FAILED(15666),
	DIRECTORY_UNAVAILABLE(16000),

	MAILCONFIG_IN_USE(16666),
	MAILCONFIG_NOT_FOUND(16667, Status.NOT_FOUND),
	MAILCONFIG_FORBIDDEN(166678, Status.FORBIDDEN),

	MAILCONTENT_IN_USE(17666),
	MAILCONTENT_NOT_FOUND(17667),
	MAILCONTENTLANG_NOT_FOUND(17668),
	MAILCONTENTLANG_DUPLICATE(17669),
	MAILCONTENT_FORBIDDEN(17670, Status.FORBIDDEN),
	MAILCONTENTLANG_FORBIDDEN(17671, Status.FORBIDDEN),

	MAILFOOTER_IN_USE(18666),
	MAILFOOTER_NOT_FOUND(18667),
	MAILFOOTERLANG_NOT_FOUND(18668),
	MAILFOOTERLANG_DUPLICATE(18669),
	MAILFOOTER_FORBIDDEN(18670, Status.FORBIDDEN),
	MAILFOOTERLANG_FORBIDDEN(18671, Status.FORBIDDEN),

	MAILLAYOUT_IN_USE(19666),
	MAILLAYOUT_NOT_FOUND(19667),
	MAILLAYOUT_FORBIDDEN(19668, Status.FORBIDDEN),
	MAILLAYOUT_DO_NOT_REMOVE_COPYRIGHT_FOOTER(19669, Status.BAD_REQUEST),

	FORBIDDEN(17000, Status.FORBIDDEN),
	UPDATE_FORBIDDEN(17001, Status.FORBIDDEN),
	BAD_REQUEST(17400, Status.BAD_REQUEST),

	WEBSERVICE_FAULT(20000, Status.INTERNAL_SERVER_ERROR),
	WEBSERVICE_FORBIDDEN(20001, Status.FORBIDDEN),
	WEBSERVICE_NOT_FOUND(20002, Status.NOT_FOUND),
	WEBSERVICE_BAD_REQUEST(20003, Status.BAD_REQUEST),

	LIST_DO_NOT_EXIST(25000, Status.NOT_FOUND),
	LIST_ALDREADY_EXISTS(25001),
	CONTACT_LIST_DO_NOT_EXIST(25002, Status.NOT_FOUND),

	THREAD_NOT_FOUND(26000, Status.NOT_FOUND),
	THREAD_MEMBER_NOT_FOUND(26001, Status.NOT_FOUND),
	WORK_GROUP_DOCUMENT_NOT_FOUND(26002, Status.NOT_FOUND),
	THREAD_FORBIDDEN(26403, Status.FORBIDDEN),
	THREAD_MEMBER_FORBIDDEN(26443, Status.FORBIDDEN),
	WORK_GROUP_DOCUMENT_FORBIDDEN(26444, Status.FORBIDDEN),
	WORK_GROUP_DOCUMENT_ALREADY_EXISTS(26445, Status.BAD_REQUEST),
	THREAD_MEMBER_ALREADY_EXISTS(26446, Status.FORBIDDEN),

	WORK_GROUP_FOLDER_NOT_FOUND(26003, Status.NOT_FOUND),
	WORK_GROUP_FOLDER_FORBIDDEN(26004, Status.FORBIDDEN),
	WORK_GROUP_FOLDER_FORBIDDEN_NOT_EMPTY(26006, Status.BAD_REQUEST),
	WORK_GROUP_FOLDER_ALREADY_EXISTS(28005, Status.BAD_REQUEST),
	WORK_GROUP_OPERATION_UNSUPPORTED(28006, Status.BAD_REQUEST),
	WORK_GROUP_NODE_NOT_FOUND(26007, Status.NOT_FOUND),

	GUEST_NOT_FOUND(28000, Status.NOT_FOUND),
	GUEST_ALREADY_EXISTS(28001, Status.BAD_REQUEST),
	GUEST_FORBIDDEN(28403, Status.FORBIDDEN),
	GUEST_INVALID_INPUT(28405, Status.BAD_REQUEST),
	GUEST_INVALID_SEARCH_INPUT(28406, Status.BAD_REQUEST),
	GUEST_EXPIRY_DATE_INVALID(38400, Status.BAD_REQUEST),
	RESET_GUEST_PASSWORD_EXPIRED_TOKEN(28407, Status.BAD_REQUEST),
	RESET_GUEST_PASSWORD_ALREADY_USED_TOKEN(28408, Status.BAD_REQUEST),
	RESET_GUEST_PASSWORD_NOT_FOUND(28409, Status.NOT_FOUND),

	TECHNICAL_ACCOUNT_NOT_FOUND(29000, Status.NOT_FOUND),

	UPLOAD_REQUEST_NOT_FOUND(30404, Status.NOT_FOUND),
	UPLOAD_REQUEST_TOO_MANY_FILES(30000, Status.BAD_REQUEST),
	UPLOAD_REQUEST_NOT_ENABLE_YET(30001, Status.BAD_REQUEST),
	UPLOAD_REQUEST_EXPIRED(30002, Status.BAD_REQUEST),
	UPLOAD_REQUEST_TOTAL_DEPOSIT_SIZE_TOO_LARGE(30003, Status.BAD_REQUEST),
	UPLOAD_REQUEST_FILE_TOO_LARGE(30004, Status.BAD_REQUEST),
	UPLOAD_REQUEST_READONLY_MODE(30005, Status.FORBIDDEN),
	UPLOAD_REQUEST_FORBIDDEN(30406, Status.FORBIDDEN),
	UPLOAD_REQUEST_GROUP_STATUS(30407, Status.FORBIDDEN),
	UPLOAD_REQUEST_ENTRY_NOT_FOUND(31404, Status.NOT_FOUND),
	UPLOAD_REQUEST_ENTRY_FILE_UNREACHABLE(31405, Status.NOT_FOUND),
	UPLOAD_REQUEST_DELETE_LAST_RECIPIENT(31500, Status.FORBIDDEN),
	UPLOAD_REQUEST_DELETE_RECIPIENT_FROM_RESTRICTED_REQUEST(31501, Status.FORBIDDEN),
	UPLOAD_REQUEST_NOT_UPDATABLE_GROUP_MODE(31406, Status.BAD_REQUEST),
	UPLOAD_REQUEST_ENTRY_FORBIDDEN(31406, Status.FORBIDDEN),
	UPLOAD_REQUEST_ENTRY_FILE_CANNOT_DELETED(31407, Status.FORBIDDEN),
	UPLOAD_REQUEST_ENTRY_FILE_CANNOT_BE_COPIED(31408, Status.FORBIDDEN),

	UPLOAD_REQUEST_URL_FORBIDDEN(32401, Status.UNAUTHORIZED),
	UPLOAD_REQUEST_ENTRY_URL_NOT_FOUND(32404, Status.NOT_FOUND),
	UPLOAD_REQUEST_ENTRY_URL_EXPIRED(32002, Status.FORBIDDEN),
	UPLOAD_REQUEST_ENTRY_URL_EXISTS(32300, Status.FORBIDDEN),

	DOCUMENT_ENTRY_FORBIDDEN(33403, Status.FORBIDDEN),
	DOCUMENT_ENTRY_NOT_FOUND(33404, Status.NOT_FOUND),

	ANONYMOUS_URL_FORBIDDEN(33403, Status.FORBIDDEN),
	ANONYMOUS_URL_NOT_FOUND(33404, Status.NOT_FOUND),
	ANONYMOUS_SHARE_ENTRY_FORBIDDEN(33403, Status.FORBIDDEN),
	ANONYMOUS_SHARE_ENTRY_NOT_FOUND(33404, Status.NOT_FOUND),

	SHARE_ENTRY_FORBIDDEN(34403, Status.FORBIDDEN),
	SHARE_ENTRY_NOT_FOUND(34404, Status.NOT_FOUND),

	UPLOAD_PROPOSITION_FILTER_NOT_FOUND(35004, Status.NOT_FOUND),

	LDAP_ATTRIBUTE_CONTAINS_NULL(37001, Status.BAD_REQUEST),

	WELCOME_MESSAGES_ALREADY_EXISTS(36001, Status.BAD_REQUEST),
	WELCOME_MESSAGES_FORBIDDEN(36003, Status.FORBIDDEN),
	WELCOME_MESSAGES_NOT_FOUND(36004, Status.NOT_FOUND),
	WELCOME_MESSAGES_ILLEGAL_KEY(36005),

	NO_UPLOAD_RIGHTS_FOR_ACTOR(38001, Status.BAD_REQUEST),
	USER_PROVIDER_NOT_FOUND(37000,Status.NOT_FOUND),

	INVALID_INPUT_FOR_X509_CERTIFICATE(39000),

	MODE_MAINTENANCE_ENABLED(39001, Status.UNSUPPORTED_MEDIA_TYPE),
	// https://github.com/flowjs/flow.js
	// 200, 201, 202: The chunk was accepted and correct. No need to re-upload.
	// 404, 415. 500, 501: The file for which the chunk was uploaded is not supported, cancel the entire upload.
	// Anything else: Something went wrong, but try reuploading the file.

	ASYNC_TASK_NOT_FOUND(40404, Status.NOT_FOUND),
	ASYNC_TASK_FORBIDDEN(40403, Status.FORBIDDEN),

	SHARE_ENTRY_GROUP_NOT_FOUND(41404, Status.NOT_FOUND),
	SHARE_ENTRY_GROUP_FORBIDDEN(41403, Status.FORBIDDEN),

	BASE64_INPUTSTREAM_ENCODE_ERROR(42000),

	USER_PREFERENCE_FORBIDDEN(44403, Status.FORBIDDEN),
	USER_PREFERENCE_NOT_FOUND(44404, Status.NOT_FOUND),

	UPLOAD_REQUEST_TEMPLATE_FORBIDDEN(43401, Status.FORBIDDEN),

	MAILING_LIST_CONTACT_ALREADY_EXISTS(45400, Status.BAD_REQUEST),

	QUOTA_FORBIDDEN(46403, Status.FORBIDDEN),

	ACCOUNT_QUOTA_NOT_FOUND(46001, Status.NOT_FOUND),
	CONTAINER_QUOTA_NOT_FOUND(46002, Status.NOT_FOUND),
	DOMAIN_QUOTA_NOT_FOUND(46003, Status.NOT_FOUND),
	PLATFORM_QUOTA_NOT_FOUND(46004, Status.NOT_FOUND),

	QUOTA_FILE_FORBIDDEN_FILE_SIZE(46010, Status.FORBIDDEN),
	QUOTA_ACCOUNT_FORBIDDEN_NO_MORE_SPACE_AVALAIBLE(46011, Status.FORBIDDEN),
	QUOTA_CONTAINER_FORBIDDEN_NO_MORE_SPACE_AVALAIBLE(46012, Status.FORBIDDEN),
	QUOTA_DOMAIN_FORBIDDEN_NO_MORE_SPACE_AVALAIBLE(46013, Status.FORBIDDEN),
	QUOTA_GLOBAL_FORBIDDEN_NO_MORE_SPACE_AVALAIBLE(46014, Status.FORBIDDEN),
	QUOTA_PLATFORM_UNAUTHORIZED(46015, Status.FORBIDDEN),

	STATISTIC_FORBIDDEN(47403, Status.FORBIDDEN),

	TEMPLATE_PARSING_ERROR(48000, Status.BAD_REQUEST),
	TEMPLATE_PARSING_ERROR_TEMPLATE_INPUT_EXCEPTION(48001, Status.BAD_REQUEST),
	TEMPLATE_PARSING_ERROR_TEXT_PARSE_EXCEPTION(48002, Status.BAD_REQUEST),
	TEMPLATE_PARSING_ERROR_NO_SUCH_PROPERTY_EXCEPTION(48003, Status.BAD_REQUEST),
	TEMPLATE_PARSING_ERROR_TEMPLATE_PROCESSING_EXCEPTION(48004, Status.BAD_REQUEST),
	TEMPLATE_PROCESSING_ERROR_INVALID_CONTEXT(48005, Status.INTERNAL_SERVER_ERROR),
	TEMPLATE_MISSING_TEMPLATE_BUILDER(48006, Status.BAD_REQUEST),

	UPGRADE_TASK_NOT_FOUND(40404, Status.NOT_FOUND),
	UPGRADE_TASK_FORBIDDEN(40403, Status.FORBIDDEN),

	SAFE_DETAIL_CAN_NOT_CREATE(50000,Status.FORBIDDEN),
	SAFE_DETAIL_CAN_NOT_DELETE(50001,Status.FORBIDDEN),
	SAFE_DETAIL_CAN_NOT_READ(50002,Status.FORBIDDEN),
	SAFE_DETAIL_CAN_NOT_LIST(50003,Status.FORBIDDEN),
	SAFE_DETAIL_ALREADY_EXIST(50004,Status.BAD_REQUEST),
	SAFE_DETAIL_NOT_FOUND(50005,Status.BAD_REQUEST);

	private final int code;

	private final Status status;

	private BusinessErrorCode(int code) {
		this.code = code;
		this.status = Status.BAD_REQUEST;
	}

	private BusinessErrorCode(int code, Status status) {
		this.code = code;
		this.status = status;
	}

	public int getCode() {
		return code;
	}

	public Status getStatus() {
		return status;
	}
}
