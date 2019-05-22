#!/bin/bash
if[[ $# -lt 1 ]] ; then echo no params ; exit ; fi
p = $1
#echo p = $p
dir = 'dirname $p'
#echo dir = $dir
filename = 'basename $p'
#echo finelname = $filename
cd $dir
fullpath = 'pwd -P .'
#echo fullpath = $fullpath
user = 'whoami'
for((i = 211; i <= 214; i = $i + 1)) ; do 
	echo ================ s$i ================
	rsync -lr $p ${user}@s$i:$fullpath
done ;