#!/bin/bash

sudo apt-get install postgresql-client

curl -fsSL https://apt.releases.hashicorp.com/gpg | sudo apt-key add -
sudo apt-add-repository --yes "deb [arch=$(dpkg --print-architecture)] https://apt.releases.hashicorp.com $(lsb_release -cs) main"
sudo apt update
sudo apt install terraform

cd terraform

terraform init

echo "Reading deployment state..."

pgHost=$(terraform output -raw kudos_db_address)
dbName=$(terraform output -raw kudos_db_name)
pgUser=$(terraform output -raw kudos_db_user)
pgPassword=$(terraform output -raw kudos_db_password)

cd ..

echo "Checking database..."

export PGPASSWORD="$pgPassword"

databaseExists=$(psql -h "$pgHost" -U "$pgUser" -d postgres -t -c "SELECT 1 FROM pg_database WHERE datname = '$dbName';")

if [ -z "$databaseExists" ]; then
    echo "Database does not exist. Creating database..."
    psql -h "$pgHost" -U "$pgUser" -d postgres -c "CREATE DATABASE $dbName;"
else
    echo "Database '$dbName' already exists."
fi

unset PGPASSWORD
