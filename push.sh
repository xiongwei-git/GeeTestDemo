time=`date "+%Y-%m-%d_%H-%M-%S"`
who=`whoami`
git add --all
git commit -m "${who} push @ ${time}"
git push 
echo "Finished Push"
