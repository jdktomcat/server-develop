#!/bin/bash
echo "commit hash is $1"
echo "registry is $2"
commit_hash=$1
docker_registry=$2
#tag使用今天的日期 + commit hash 前六位
tag=$(date +%Y%m%d)_${commit_hash:0:6}
rm -rf ./buildContext
mkdir ./buildContext
cp Dockerfile ./buildContext/
cp ./build/libs/*.jar ./buildContext/api.jar
docker build -t ${docker_registry}:${tag} ./buildContext
docker push ${docker_registry}:${tag}
