README for nfse
==========================

<h3>O projeto utiliza o mongodb para armazenar algumas informações em cache.</h3>
<ul>
    <li>Você pode alterar as configurações de conexão com o MongoDB no application-dev.yml</li>
    <li>Caso queira subir um MongoDB local em docker, execute o seguinte comando: </li>
    <li>sudo docker run -d -p 27017:27017 --name mongodb mongo:latest</li>
</ul>

<h3>Para comunicação com outros serviço (Ex: Integração Nota Nacional) o projeto utiliza o RabbitMQ</h3>
<ul>
    <li>Você pode alterar as configurações de conexão com o RabbitMQ no application-dev.yml</li>
    <li>Caso queira subir um RabbitMQ local em docker, execute o seguinte comando: </li>
    <li>sudo docker run -p 5672:5672 -p 15672:15672 -d --name rabbitmq -e RABBITMQ_DEFAULT_USER=user -e RABBITMQ_DEFAULT_PASS=senha10 rabbitmq:3.8-management</li>
</ul>