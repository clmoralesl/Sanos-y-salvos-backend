# Pasos para desplegar RabbitMQ en Amazon EKS usando Helm

# 1. Agregar el repositorio de Helm de Bitnami
helm repo add bitnami https://charts.bitnami.com/bitnami
helm repo update

# 2. Instalar RabbitMQ en el namespace actual
# Por defecto creará un servicio llamado `rabbitmq` que estará disponible internamente en el puerto 5672
helm install rabbitmq bitnami/rabbitmq \
  --set auth.username=user \
  --set auth.password=password \
  --set replicaCount=1

# 3. Verificar el despliegue
kubectl get pods -l app.kubernetes.io/name=rabbitmq
kubectl get svc rabbitmq

# 4. Acceder al panel de control (Management UI) - Opcional
kubectl port-forward svc/rabbitmq 15672:15672
# Luego puedes ingresar desde tu navegador a http://localhost:15672 (user/password)
