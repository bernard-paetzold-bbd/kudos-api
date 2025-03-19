# install terraform

curl -fsSL https://apt.releases.hashicorp.com/gpg | sudo apt-key add -
sudo apt-add-repository --yes "deb [arch=$(dpkg --print-architecture)] https://apt.releases.hashicorp.com $(lsb_release -cs) main"
sudo apt update
sudo apt install terraform

echo "Running Terraform..."

cd terraform

terraform init -migrate-state

privateKey=$(terraform output -raw private_key)

tempDir=$(mktemp -d)  
keyFile="$tempDir/temp-key.pem"  

echo "$privateKey" > "$keyFile"

chmod 600 "$keyFile"

ec2PublicIP=$(terraform output -raw ec2_public_ip)
dbName=$(terraform output -raw kudos_db_name)
dbEndpoint=$(terraform output -raw kudos_db_endpoint)
dbUser=$(terraform output -raw kudos_db_user)
dbPassword=$(terraform output -raw kudos_db_password)
dbURL="jdbc:postgresql://$dbEndpoint/$dbName"

jarFileName="kudos_api-0.0.1-SNAPSHOT.jar"
jarFilePath="target/$jarFileName"

cd ..

echo "Building JAR file using Maven..."
mvn clean package -DskipTests

echo "Uploading JAR file to EC2 instance..."
scp -o "StrictHostKeyChecking accept-new" -i "$keyFile" "$jarFilePath" "ec2-user@$ec2PublicIP:~/"

echo "Connecting to EC2 and restarting Spring Boot application..."

ssh -i "$keyFile" -o "StrictHostKeyChecking accept-new" ec2-user@$ec2PublicIP <<EOF
    pkill -f 'java -jar' || echo 'No running Spring Boot app to stop.'
EOF

ssh -o "StrictHostKeyChecking accept-new" -i "$keyFile" ec2-user@$ec2PublicIP <<EOF
    env DB_USERNAME='$dbUser' DB_PASSWORD='$dbPassword' DB_URL='$dbURL' nohup java -jar ~/$jarFileName > ~/app.log 2>&1 &
EOF

echo "Deployment complete."

rm -f "$keyFile"
