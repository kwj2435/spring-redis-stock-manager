#!/bin/bash

# Redis 노드 이름 설정
NODE_NAMES=(
  "redis-node-1"
  "redis-node-2"
  "redis-node-3"
  "redis-node-4"
  "redis-node-5"
  "redis-node-6"
)

# Redis 노드 IP와 포트 동적 구성
NODES=()
for NODE in "${NODE_NAMES[@]}"; do
  # Docker 네트워크에서 IP를 가져옵니다.
  IP=$(docker inspect -f '{{range .NetworkSettings.Networks}}{{.IPAddress}}{{end}}' "$NODE")
  if [ -n "$IP" ]; then
    NODES+=("${IP}:6379")
  else
    echo "Error: Could not retrieve IP for $NODE"
    exit 1
  fi
done

# 노드 확인 출력
echo "Detected Redis nodes:"
printf '%s\n' "${NODES[@]}"

# 클러스터 생성 명령
CLUSTER_CREATE_CMD="docker exec -it redis-node-1 redis-cli --cluster create ${NODES[@]} --cluster-replicas 1"

# 클러스터 생성 명령 실행
echo "Creating Redis cluster..."
eval "$CLUSTER_CREATE_CMD"

# 클러스터 상태 확인
echo "Checking cluster info..."
docker exec -it redis-node-1 redis-cli -c cluster info
