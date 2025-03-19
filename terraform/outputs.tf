output "ec2_public_ip" {
  value = aws_instance.kudos_api_instance.public_ip
}

output "ec2_endpoint" {
  value = aws_instance.kudos_api_instance.public_dns
}

output "private_key" {
  value     = tls_private_key.instance_key.private_key_pem
  sensitive = true
}

output "kudos_db_endpoint" {
  value = aws_db_instance.kudos_db_instance.endpoint
}

output "kudos_db_address" {
  value = aws_db_instance.kudos_db_instance.address
}

output "kudos_db_port" {
  value = aws_db_instance.kudos_db_instance.port
}

output "kudos_db_name" {
  value     = var.db_name
  sensitive = true
}

output "kudos_db_user" {
  value     = aws_db_instance.kudos_db_instance.username
  sensitive = true
}

output "kudos_db_password" {
  value     = aws_db_instance.kudos_db_instance.password
  sensitive = true
}


