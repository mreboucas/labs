#!/bin/bash

#não precisa sudo - pq a máquina já roda como sudo

#atualizar os pacotes do yum
yum update -y

#adicionando repositórios extras - pq a máquina aponta para os repositórios extra da amazon
amazon-linux-extras install -y lamp-mariadb10.2-php7.2 php7.2
#instalando o apache e mariadb
yum install -y httpd mariadb-server

#iniciando o apache
systemctl start httpd
#habilitar o apache quando a instância ec2 for reiniciada
systemctl enable httpd

#iniciando o mariadb
systemctl start mariadb
#habilitar o mariadb-server quando a instância ec2 for reiniciada
systemctl enable mariadb

#alterando permissões
# adicionando o usuário no grupo do apache
usermod -a -G apache ec2-usermod
#ajustar as permissões do /var para o usuário e grupo
chown -R ec2-user:apache /var/www