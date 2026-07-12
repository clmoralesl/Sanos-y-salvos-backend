locals {
  repositories = [
    "sanos-salvos-frontend",
    "sanos-salvos-api-gateway",
    "sanos-salvos-bff",
    "sanos-salvos-ms-mascotas",
    "sanos-salvos-ms-geo",
    "sanos-salvos-ms-coincidencias"
  ]
}

resource "aws_ecr_repository" "repos" {
  for_each             = toset(local.repositories)
  name                 = each.key
  image_tag_mutability = "MUTABLE"

  image_scanning_configuration {
    scan_on_push = false
  }

  force_delete = true 
}
