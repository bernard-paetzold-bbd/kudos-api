

# create private key
resource "tls_private_key" "instance_key" {
  algorithm = "RSA"
  rsa_bits  = 2048
}

# create key pair
resource "aws_key_pair" "instance_key_pair" {
  key_name   = var.key_name
  public_key = tls_private_key.instance_key.public_key_openssh

  tags = {
    Name = "instance-key-pair"
  }
}

# create instance
resource "aws_instance" "kudos_api_instance" {
  ami           = var.ami_id
  instance_type = var.instance_type

  subnet_id                   = aws_subnet.default_subnet.id
  vpc_security_group_ids      = [aws_security_group.instance_security_group.id]
  associate_public_ip_address = true

  key_name = aws_key_pair.instance_key_pair.key_name

  user_data = file("data/instance_init_data.sh")

  tags = {
    Name = "kudos-api-instance"
  }
}
