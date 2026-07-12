output "cluster_endpoint" {
  description = "Endpoint for EKS control plane"
  value       = aws_eks_cluster.this.endpoint
}

output "cluster_name" {
  description = "Kubernetes Cluster Name"
  value       = aws_eks_cluster.this.name
}

output "ecr_repository_urls" {
  description = "URLs of the ECR repositories"
  value       = { for repo in aws_ecr_repository.repos : repo.name => repo.repository_url }
}
