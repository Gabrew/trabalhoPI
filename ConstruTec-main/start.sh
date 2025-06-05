#!/bin/bash
echo "Matando processo na porta 8080..."
fuser -k 8080/tcp
sleep 2
echo "Iniciando a aplicacao..."
mvn spring-boot:run 