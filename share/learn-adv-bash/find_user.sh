#!/bin/bash
echo -n "Enter user name : "
read USR
cut -d: -f1 /etc/passwd | grep "$USR" > /dev/null
OUT=$?
if [ $OUT -eq 0 ];then
   echo "User account found!"
else
   echo "User account does not exists in /etc/passwd file!"
fi

