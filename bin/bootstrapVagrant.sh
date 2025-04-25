#!/bin/sh -e

# Usuário do PostgreSQL
APP_DB_USER=webpublico
# Senha do Usuário criado na linha acima
APP_DB_PASS=senha10
# Nome do Database criado
APP_DB_NAME=nfse_legado

# URL de onde o dump será baixado
REPO_URL=https://dumps.webpublico.com.br/backup_postgres
# Usuário do FTP
REPO_USER=mga
#Senha do FTP
REPO_PASS=Senha10?
# Nome do Arquivo a ser baixado, sem a adição do dia da semana
REPO_FILE=nfse

# Versão do PostgreSQL que será instalada
PG_VERSION=9.6

print_db_usage () {
  echo "Seu Banco de Dados PostgreSQL foi configurado corretamente e pode ser acessado na sua máquina local pela porta aberta (default: 5432)"
  echo " Host: localhost"
  echo " Porta: 5432"
  echo " Database: $APP_DB_NAME"
  echo " Username: $APP_DB_USER"
  echo " Password: $APP_DB_PASS"
  echo ""
  echo "Para acessar a VM com acesso administrativo:"
  echo " vagrant ssh"
  echo " sudo su - postgres"
  echo ""
  echo "Acessar o banco diretamente pelo psql:"
  echo " vagrant ssh"
  echo " sudo su - postgres"
  echo " PGUSER=$APP_DB_USER PGPASSWORD=$APP_DB_PASS psql -h localhost $APP_DB_NAME"
  echo ""
  echo "Variável de Ambiente com a URL do banco criado:"
  echo " DATABASE_URL=postgresql://$APP_DB_USER:$APP_DB_PASS@localhost:5432/$APP_DB_NAME"
  echo ""
  echo "Comando local para acessar o banco dentro da VM via psql"
  echo " PGUSER=$APP_DB_USER PGPASSWORD=$APP_DB_PASS psql -h localhost -p 5432 $APP_DB_NAME"
}

export DEBIAN_FRONTEND=noninteractive

PROVISIONED_ON=/etc/vm_provision_on_timestamp
if [ -f "$PROVISIONED_ON" ]
then
echo "VM previamente configurada em $(cat $PROVISIONED_ON)"
  echo "Para atualizar o sistema manualmente, logue via 'vagrant ssh' e execute 'apt-get update && apt-get upgrade'"
  echo ""
  print_db_usage
  exit
fi

#Adição do repositório APT oficial do PostgreSQL
PG_REPO_APT_SOURCE=/etc/apt/sources.list.d/pgdg.list
if [ ! -f "$PG_REPO_APT_SOURCE" ]
then
  # Add PG apt repo:
  echo "deb http://apt.postgresql.org/pub/repos/apt/ xenial-pgdg main" > "$PG_REPO_APT_SOURCE"

  # Add PGDG repo key:
  wget --quiet -O - http://apt.postgresql.org/pub/repos/apt/ACCC4CF8.asc | apt-key add -
fi

# Atualização de pacotes e instalação do PostgreSQL
apt-get update
apt-get -y upgrade
apt-get -y install vim
apt-get -y install "postgresql-$PG_VERSION" "postgresql-contrib-$PG_VERSION"

# Configuração do service do PostgreSQL para iniciar automaticamente ao bootar a VM
systemctl enable postgresql

# Arquivos de Configuração do PostgreSQL
PG_CONF="/etc/postgresql/$PG_VERSION/main/postgresql.conf"
PG_HBA="/etc/postgresql/$PG_VERSION/main/pg_hba.conf"
PG_DIR="/var/lib/postgresql/$PG_VERSION/main"

# Permitindo a conexão externa
sed -i "s/#listen_addresses = 'localhost'/listen_addresses = '*'/" "$PG_CONF"

# Mudando o padrão de data de mes / dia / ano para dia / mes / ano
sed -i "s/datestyle = 'iso, mdy'/datestyle = 'iso, dmy'/" "$PG_CONF"

# Permissão para todos os usuários se conectarem com login/senha
echo "host all all all md5" >> "$PG_HBA"

# Reinicialização do PostgreSQL para que as configurações acima surtam efeito
service postgresql restart

# Conectando no banco via psql para configuração de usuário e database
cat << EOF | su - postgres -c psql
-- Criação do Usuário:
CREATE USER $APP_DB_USER WITH PASSWORD '$APP_DB_PASS' SUPERUSER;

-- Criação do Database:
CREATE DATABASE $APP_DB_NAME WITH OWNER $APP_DB_USER;
EOF

# Download do Dump via FTP e restaurando o dump no database criado
echo "Buscando Base de Dados Padrão"
DOW=`date +%A -d "yesterday"`
FILE=${REPO_FILE}_$DOW.backup
wget --no-verbose --user $REPO_USER --password $REPO_PASS $REPO_URL/$FILE
sudo -u postgres pg_restore -Fc -d $APP_DB_NAME $FILE
echo "Base de Dados Padrão restaurada com Sucesso"

# Marcando a data de Configuração
date > "$PROVISIONED_ON"

echo "Máquina Virtual criada com Sucesso"
echo ""
print_db_usage
