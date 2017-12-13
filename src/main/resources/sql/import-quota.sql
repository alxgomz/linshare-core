-- root domain quota
INSERT INTO quota(id, uuid, creation_date, modification_date, batch_modification_date,
	current_value, last_value, domain_id,
    quota, quota_override,
    quota_warning,
    default_quota, default_quota_override,
    quota_type, current_value_for_subdomains)
VALUES (1, '2a01ac66-a279-11e5-9086-5404a683a462', NOW(), NOW(), null,
	0, 0, 1,
	10000000000000, null,
	10000000000000,
    1000000000000, true,
    'DOMAIN_QUOTA', 0);
-- quota : 10 To
-- quota_warning : 10000000000000 : 10 To
-- default_quota : 1000000000000 : 1 To (1 To per sub domain)


-- 'CONTAINER_QUOTA', 'USER' for root domain
INSERT INTO quota(id, uuid, creation_date, modification_date, batch_modification_date,
	quota_domain_id, current_value, last_value, domain_id,
    quota, quota_override,
    quota_warning,
    default_quota, default_quota_override,
    default_max_file_size, default_max_file_size_override,
    default_account_quota, default_account_quota_override,
    max_file_size, max_file_size_override,
    account_quota, account_quota_override,
    quota_type, container_type, shared)
VALUES (11, '26323798-a1a8-11e6-ad47-0800271467bb', NOW(), NOW(), null,
	1, 0, 0, 1,
	400000000000, null,
    400000000000,
    400000000000, false,
    10000000000, null,
    100000000000, null,
    100000000000, null,
    100000000000, null,
    'CONTAINER_QUOTA', 'USER', false);
-- quota : 400000000000 : 400 Go for all users
-- quota_warning : 400000000000 : 400 Go
-- default_quota : 400000000000 : 400 Go
-- default_max_file_size : 10000000000  : 10 Go
-- default_account_quota : 100000000000 : 100 Go : default value for container created inside a container of a top domain
-- max_file_size : 100000000000  : 100 Go
-- account_quota : 100000000000 : 100 Go : value for account created inside container the root domain

-- 'CONTAINER_QUOTA', 'WORK_GROUP' for root domain
INSERT INTO quota(id, uuid, creation_date, modification_date, batch_modification_date,
	quota_domain_id, current_value, last_value, domain_id,
    quota, quota_override,
    quota_warning,
    default_quota, default_quota_override,
    default_max_file_size, default_max_file_size_override,
    default_account_quota, default_account_quota_override,
    max_file_size, max_file_size_override,
    account_quota, account_quota_override,
    quota_type, container_type, shared)
VALUES (12, '63de4f14-a1a8-11e6-a369-0800271467bb', NOW(), NOW(), null,
	1, 0, 0, 1,
	400000000000, null,
    400000000000,
    400000000000, false,
    10000000000, null,
    400000000000, null,
    10000000000, null,
    400000000000, null,
    'CONTAINER_QUOTA', 'WORK_GROUP', true);
-- quota : 400000000000 : 400 Go for all workgroups
-- quota_warning : 400000000000 : 400 Go
-- default_quota : 400000000000 : 400 Go
-- default_max_file_size : 10000000000  : 10 Go
-- default_account_quota : 400000000000 : 400 Go, also 400 Go for one workgroup
-- max_file_size : 10000000000  : 10 Go
-- account_quota : 400000000000 : 400 Go, also 400 Go for one workgroup
