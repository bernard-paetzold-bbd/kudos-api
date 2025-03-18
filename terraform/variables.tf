variable "aws_region" {
  type    = string
  default = "af-south-1"
}

variable "vpc_cidr_block" {
  type    = string
  default = "10.0.0.0/16"
}

variable "subnet_cidr_block" {
  type    = string
  default = "10.0.1.0/24"
}

variable "second_subnet_cidr_block" {
  type    = string
  default = "10.0.2.0/24"
}

variable "availability_zone" {
  type    = string
  default = "af-south-1a"
}

variable "availability_zone_b" {
  type    = string
  default = "af-south-1b"
}

variable "ami_id" {
  type    = string
  default = "ami-00d6d5db7a745ff3f"
}

variable "instance_type" {
  type    = string
  default = "t3.micro"
}

variable "key_name" {
  type    = string
  default = "kudos-api-instance-key-pair"
}

variable "api_port" {
  type    = number
  default = 8080
}

variable "db_name" {
  type      = string
  sensitive = true
  default = "kudos_db"
}

variable "db_username" {
  type = string
}

variable "db_password" {
  type      = string
  sensitive = true
}

variable "db_port" {
  type    = number
  default = 5432
}
