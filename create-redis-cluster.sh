#!/bin/bash

# Redis 노드 IP와 포트 설정
NODES=(
  "redis-node-1:6379"
  "redis-node-2:6379"
  "redis-node-3:6379"
  "redis-node-4:6379"
  "redis-node-5:6379"
  "redis-node-6:6379"
)

# 클러스터 생성 명령
CLUSTER_CREATE_CMD="docker exec -it redis-node-1 redis-cli --cluster create ${NODES[@]} --cluster-replicas 1"

# 클러스터 생성 명령 실행
$CLUSTER_CREATE_CMD

# 클러스터 상태 확인
docker exec -it redis-node-1 redis-cli -c cluster info
