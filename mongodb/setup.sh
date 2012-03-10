#!/bin/bash

mongodir=~/mongo/upcoming
mongoversion=2.0.0

function error_exit
{
#   ----------------------------------------------------------------
#   Function for exit due to fatal program error
#       Accepts 1 argument:
#               string containing descriptive error message
#   ----------------------------------------------------------------
    echo "FATAL: ${1:-"Unknown Error"}" 1>&2
    exit 1
}

echo Stopping mongodb
killall mongos > /dev/null 2>&1
killall mongo > /dev/null 2>&1

mkdir -p $mongodir/downloads
mkdir -p $mongodir/db/node1
mkdir -p $mongodir/db/node2
mkdir -p $mongodir/db/node3
mkdir -p $mongodir/logs

case `uname` in
	Linux)
		mongoFile=mongodb-linux-x86_64-${mongoversion}.tgz
		mongoPath=mongodb-linux-x86_64-${mongoversion}
		mongoOs="linux"
		;;
	
	Darwin)
		mongoFile=mongodb-osx-x86_64-${mongoversion}.tgz
		mongoPath=mongodb-osx-x86_64-${mongoversion}
		mongoOs="osx"
		;;
			
	*)
		echo "Only works on Linux and OSX"
		exit 1
esac
		
if [ ! -f "$mongodir/downloads/$mongoFile" ]; then 
        destMongofile=$mongodir/downloads/$mongoFile
	echo "Downloading http://fastdl.mongodb.org/$mongoOs/$mongoFile"
	wget "http://fastdl.mongodb.org/$mongoOs/$mongoFile" -O $destMongofile
	echo Extracting to $mongodir
	tar -C $mongodir -xzf $destMongofile
fi			

echo copying admin-setup `dirname "$0"`/admin-setup.js $mongodir

cp `dirname "$0"`/admin-setup.js $mongodir || error_exit "Failed to copy setup file"

cd $mongodir

echo "Creating upcoming secret key file"
echo "flexiblecontent secret key" > keyFile
chmod 0600 keyFile

echo "Shutdown current mongo nodes"


$mongodir/$mongoPath/bin/mongo localhost:27017/admin --username admin --password admin --eval "db.shutdownServer()"
$mongodir/$mongoPath/bin/mongo localhost:27018/admin --username admin --password admin --eval "db.shutdownServer()"
$mongodir/$mongoPath/bin/mongo localhost:27019/admin --username admin --password admin --eval "db.shutdownServer()"

echo ${mongodir}/${mongoPath}

echo "Starting mongodb node-1"
$mongodir/$mongoPath/bin/mongod --dbpath $mongodir/db/node1 \
	--logpath $mongodir/logs/mongodb-node1.log \
	-v \
	--keyFile $mongodir/keyFile \
	--smallfiles \
	--rest \
	--pidfilepath $mongodir/mongo.pid \
	--fork \
	--directoryperdb \
	--replSet upcoming-replica \
	--port 27017

echo "Starting mongodb node-2"
$mongodir/$mongoPath/bin/mongod --dbpath $mongodir/db/node2 \
	--logpath $mongodir/logs/mongodb-node2.log \
	-v \
	--keyFile $mongodir/keyFile \
	--smallfiles \
	--rest \
	--pidfilepath $mongodir/mongo.pid \
	--fork \
	--directoryperdb \
	--replSet upcoming-replica \
	--port 27018 


echo "Starting mongodb node-3"
$mongodir/$mongoPath/bin/mongod --dbpath $mongodir/db/node3 \
	--logpath $mongodir/logs/mongodb-node3.log \
	-v \
	--keyFile $mongodir/keyFile \
	--smallfiles \
	--rest \
	--pidfilepath $mongodir/mongo.pid \
	--fork \
	--directoryperdb \
	--replSet upcoming-replica \
	--port 27019 

echo Wait 40 secs to let the nodes start up
for i in {1..40}
do
	echo $[40 - $i]
	sleep 1
done

echo Init the replica sets

$mongodir/$mongoPath/bin/mongo admin --eval  'printjson(db.runCommand({"replSetInitiate" : {"_id" : "upcoming-replica", "members" : [ { "_id" : 1, "host" : "localhost:27017" }, {"_id" : 2, "host" :"localhost:27018" },{"_id" : 3, "host" : "localhost:27019"}]}}))' || echo "Failed to execute replica set - probably OK"

echo Wait 30 secs for the replica set
for i in {1..30}
do
	echo $[30 - $i]
	sleep 1
done


$mongodir/$mongoPath/bin/mongo admin --quiet $mongodir/admin-setup.js || echo "Failed to execute admin-setup.js"


echo DONE
	
