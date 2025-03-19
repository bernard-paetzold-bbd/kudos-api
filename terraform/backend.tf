# terraform backend
terraform {
  backend "s3" {
    bucket  = "kudos-api-test-terraform-state-2"
    key     = "terraform.tfstate"
    region  = "af-south-1"
  }
}
