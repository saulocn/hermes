CURRENT_DIR = $(shell pwd)
CONTAINER_NAME=hermes-pulsar
PORTS=-p 6650:6650 -p 8080:8080 
VOLUME=-v ${CURRENT_DIR}/data:/pulsar/data
IMAGE=apachepulsar/pulsar:latest bin/pulsar standalone

pulsar:	rm-pulsar
	docker run -d --name ${CONTAINER_NAME} ${PORTS} ${VOLUME}  ${IMAGE}
stop-pulsar:
	docker stop ${CONTAINER_NAME} | true
rm-pulsar: 
	docker rm -f ${CONTAINER_NAME} | true