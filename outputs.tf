output "mongo_uri" {
  description = "The url to use to connect to the mongo cluster"
  value       = mongodbatlas_serverless_instance.main.connection_strings_standard_srv
}

output "service_uri" {
  description = "The public internet exposed url used to access the ECS service"
  value       = aws_lb.main.dns_name
}