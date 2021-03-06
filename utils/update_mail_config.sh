#!/bin/bash
set -e

g_import_src=../src/main/resources/sql/common/import-mail.sql
g_import_new=../src/main/resources/sql/common/import-mail.sql.new
g_reset_default_emails=../src/main/resources/sql/postgresql/reset-default-emails-config.sql
g_host=127.0.0.1
g_port=5432
g_database=linshare
g_user=linshare
g_pg_dump=/usr/lib/postgresql/9.4/bin/pg_dump
g_pg_dump=/usr/bin/pg_dump
g_step="$@"
g_output=mails.sql
g_output_clean=mails.clean.sql

if [ -f update_mail_config.cfg ] ; then
    source update_mail_config.cfg
fi

echo "############ Config #########"
echo "host : $g_host"
echo "port : $g_port"
echo "database : $g_database"
echo "user : $g_user"
echo "pg_dump : $g_pg_dump"
echo "#############################"


function usage ()
{
    echo
    echo "Usage : $0 <action>"
    echo "  action = all : automatic mode."
    echo "  action = dump : only dump database."
    echo "  action = help : display all others actions."
    echo
    exit 1
}



function dump_and_clean ()
{
    echo dump and clean database : $g_host : $g_port : $g_database  : $g_user
    ${g_pg_dump} -h $g_host -p $g_port -U ${g_user} -t mail_layout -t mail_footer -t mail_content -t mail_config -t mail_content_lang -t mail_footer_lang  -a --inserts --attribute-inserts  $g_database -f ${g_output}
    grep -v "^-- " ${g_output} | grep -v "^--$" | grep -v "^SET" | sed -e '/^$/ d'>> ${g_output_clean}
echo "UPDATE domain_abstract SET mailconfig_id = 1;
UPDATE mail_footer SET readonly = true;
UPDATE mail_layout SET readonly = true;
UPDATE mail_content SET readonly = true;
UPDATE mail_config SET readonly = true;
UPDATE mail_content_lang SET readonly = true;
UPDATE mail_footer_lang SET readonly = true;
" >> ${g_output_clean}
#UPDATE mail_activation SET enable = false;

    sed -i -r -e "s/'2018-[0-9]{2}-[0-9]{2} [0-9]{2}:[0-9]{2}:[0-9]{2}\.[0-9]{2,6}'/now()/g" ${g_output_clean}

    echo generated files : ${g_output} ${g_output_clean}
}

function update_reset_script ()
{
    echo update reset default emails sql file :
    echo "
    BEGIN;
UPDATE domain_abstract SET mailconfig_id = null where mailconfig_id = 1;
DELETE FROM mail_content_lang WHERE id < 1000;
DELETE FROM mail_footer_lang WHERE id < 1000;
DELETE FROM mail_config WHERE id = 1;
DELETE FROM mail_content WHERE id < 1000;
DELETE FROM mail_footer WHERE id < 1000;
DELETE FROM mail_layout WHERE id < 1000;
" > ${g_reset_default_emails}
    cat ${g_output_clean} >> ${g_reset_default_emails}
    sed -i -e '/UPDATE domain_abstract SET mailconfig_id = 1;/ d' ${g_reset_default_emails}
    echo "UPDATE domain_abstract SET mailconfig_id = 1 where mailconfig_id is null;
COMMIT;" >> ${g_reset_default_emails}
    echo "reset default emails file updated : ${g_reset_default_emails}"
}

function update_postgresql ()
{
    cat ${g_output_clean} > ${g_import_new}
    echo update postgresql sql file :
    mv -v ${g_import_new} ${g_import_src}

}

####### MAIN

if [ -z "${g_step}" ] ; then
    usage
fi


rm -fv ${g_output}  ${g_output_clean}

if [ "${g_step}" == "help" ] ; then
    echo
    echo "available functions :"
    echo "---------------------"
    declare -F | cut -d' ' -f 3-
    echo
    exit 0
fi

if [ "${g_step}" == "dump" ] ; then
    dump_and_clean
    exit 0
fi

if [ "${g_step}" == "all" ] ; then
    dump_and_clean
    update_postgresql
    update_reset_script
    exit 0
fi

# other wise , last choice.
for func in ${g_step}
do
    echo RUNNING : $func
    $func
done
