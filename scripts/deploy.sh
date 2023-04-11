# 가동중인 awsstudy 도커 중단 및 삭제
sudo docker ps -a -q --filter "name=keyword_searcher_alarm" | grep -q . && docker stop keyword_searcher_alarm && docker rm keyword_searcher_alarm | true

# 기존 이미지 삭제
sudo docker rmi yeomin1024/keyword_searcher_alarm

# 도커허브 이미지 pull
sudo docker pull yeomin1024/keyword_searcher_alarm

# 도커 run
docker run -d -p 8081:8081 -v /home/ec2-user:/config --name keyword_searcher_alarm yeomin1024/keyword_searcher_alarm

# 사용하지 않는 불필요한 이미지 삭제 -> 현재 컨테이너가 물고 있는 이미지는 삭제 안됨
docker rmi -f $(docker images -f "dangling=true" -q) || true
