#!/bin/bash
params = $@
i = 211
for((i = 211; i <= 214; i = $i + 1)) ; do
	tput setaf 2
	echo ================ s$i ================
	tput setaf 7
	ssh -4 s$i $params
done 