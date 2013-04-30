
#set -vx

git submodule add ssh://git@github.com/coffeebook/forms modules/forms
git submodule add ssh://git@github.com/coffeebook/coffeecup modules/coffeecup
git submodule add ssh://git@github.com/coffeebook/js2coffee modules/js2coffee
git submodule add ssh://git@github.com/coffeebook/cs2js modules/cs2js
git submodule add ssh://git@github.com/coffeebook/coffeecup-helpers modules/coffeecup-helpers
git submodule add ssh://git@github.com/coffeebook/json-template modules/json-template

git submodule update --init

#This is the equivalent of running:
#git submodule init
#git submodule update
#With version 1.6.5 of git and later, you can do this automatically by cloning the super-project with the –recursive option:
#git clone --recursive git://github.com/mysociety/whatdotheyknow.git


cd modules
cd coffeecup && git remote add upstream https://github.com/gradus/coffeecup && cd ..
cd js2coffee && git remote add upstream https://github.com/rstacruz/js2coffee && cd ..
cd cs2js && git remote add upstream https://github.com/twilson63/cs2js && cd ..
cd coffeecup-helpers && git remote add upstream https://github.com/twilson63/coffeecup-helpers && cd ..
cd json-template && git remote add upstream https://github.com/Gozala/json-template && cd ..

#echo "Too see all those with remote upstreams"
#grep -irn "remote \"upstream\"" . | cut -d '/' -f5

#git config --list|grep '^submodule'
git submodule

echo "you may wish to add to [alias] pu = !\"git fetch origin -v; git fetch upstream -v; git merge upstream/master\""

exit $?

