# terraform backend
terraform {
  backend "s3" {
    bucket  = "kudos-api-test-terraform-state"
    key     = "terraform.tfstate"
    region  = "af-south-1"
    profile = "default"
  }
}
