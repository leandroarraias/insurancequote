# Descrição Geral da Aplicação

### Objetivo

Microserviço destinado a processar cotação de seguros, capaz de receber e consultar cotações interagindo com outros sistemas.

### Funcionamento

Ao receber a requisição, o microserviço consulta dados do produto e oferta informados no serviço de catálogo. Por se tratar de
uma demonstração, a representação do serviço é realizada pelo WireMock que provê endpoints para consulta de produtos e ofertas
pré cadastrados.

Os dados da requisição são então validados contra as regras explicitadas na próxima seção. Uma vez que a requisição é válida,
os dados da cotação são armazenados no banco de dados e em seguida enviados para o serviço de apólice. Também por se tratar
de uma demonstração, aqui o serviço de apólice é representado pelo ActiveMQ que, uma vez recebido os dados da cotação,
imediatamente retorna os dados para o microserviço concluir o processamento do mesmo.

Em qualquer momento o usuário pode realizar a consulta da situação atual da cotação.

### Regras de validação da requisição

- O produto e oferta existem e estão ativos
- As coberturas informadas estão dentro da lista de coberturas da oferta e abaixo dos valores máximos permitidos.
- As assistências informadas estão dentro da lista de assistências da oferta.
- O valor total do prêmio mensal está entre o máximo e mínimo definido para a oferta.
- O valor total das coberturas corresponde a somatória das coberturas informadas no corpo da requisição.
- Os campos dos dados do cliente são livres.

# Estrutura de pastas
 - app: Código fonte da aplicação
 - img: Imagens utilizadas na elaboração do README.md
 - infra: Configuração da infraestrutura necessária para execução da aplicação

# Arquitetura

### Diagrama de Integração

<img alt="Arquitetura" src="./img/arquitetura.png">

# Pré requisitos
### Java
Certifique-se de que a versão corrente do Java instalado seja a 17. Para verificar, execute o comando ``java -version``. 
O resultado, deverá ser semelhante ao exibido abaixo:

```
D:\workspace\insurancequote>java -version
openjdk version "17.0.2" 2022-01-18
OpenJDK Runtime Environment (build 17.0.2+8-86)
OpenJDK 64-Bit Server VM (build 17.0.2+8-86, mixed mode, sharing)
```

Caso não possua, realizar o download e instalação conforme [site oficial](https://docs.oracle.com/en/java/javase/17/install/overview-jdk-installation.html#GUID-8677A77F-231A-40F7-98B9-1FD0B48C346A)

### Maven

A aplicação possui o maven wrapper não sendo obrigatória sua instalação na máquina local. Mas caso já o tenha instalado,
certifique-se de que a versão corrente seja a 3.X.X. Para verificar, execute o comando ``mvn -version``. O resultado, 
deverá ser semelhante ao exibido abaixo:

```
D:\workspace\insurancequote>mvn -version
Apache Maven 3.8.1 (05c21c65bdfed0f71a2f2ada8b84da59348c4c5d)
Maven home: D:\Tecnologia\apache-maven-3.8.1\bin\..
Java version: 17.0.2, vendor: Oracle Corporation, runtime: D:\Tecnologia\java\openjdk-17.0.2
Default locale: pt_BR, platform encoding: Cp1252
OS name: "windows 11", version: "10.0", arch: "amd64", family: "windows"
```

Caso seja necessário nova instalação, realizar o download e configuração conforme [site oficial](https://maven.apache.org/users/index.html)

### Docker

Os sistemas de observability estão configurados para serem utilizados como containers Docker. Portanto, certifique-se de 
que o Docker esteja instalado, executando o comando ``docker --version``. O resultado, deverá ser semelhante ao exibido 
abaixo:

```
D:\workspace\insurancequote>docker --version
Docker version 20.10.17, build 100c701
```

Caso não possua, realizar o download e instalação conforme [site oficial](https://docs.docker.com/engine/install/)

# Startup da aplicação
1. Realizar o download da aplicação em sua pasta de preferência.
2. Na pasta ``infra`` executar o comando ``docker compose -p insurancequote up`` para inicializar os containers de observability.
3. Na pasta ``app`` executar o comando ``mvnw spring-boot:run`` para inicializar a aplicação.

# Execução

### Contrato da API

O contrato da API no formato OpenAPI 3 atualiado para geração de clients está disponível para download [aqui](http://localhost:8080/api-docs.yaml), ou 
ser visualizado no browser [aqui](http://localhost:8080/api-docs).

A página de documentação da API no formato Swagger pode ser acessada [aqui](http://localhost:8080/api-docs/swagger.html).

### Execução da API

Abaixo segue um exemplo das requisiçãos HTTP para a aplicação realizadas no Insomnia. Para maiores detalhes, acessar a página Swagger da aplicação.

#### Solicitação de cotação
```
> POST /insurances-quotes HTTP/1.1
> Host: localhost:8080
> Content-Type: application/json

| {
| 	"productId": "7e8a8a99-80ba-4f40-aab5-6608ef9dafed",
| 	"offerId": "2de095f4-5f5a-4525-b1ed-42a0f859f815",
| 	"category": "HOME",
| 	"totalMonthlyPremiumAmount": 100.74,
| 	"totalCoverageAmount": 100000.12,
| 	"coverages": [
| 		{
| 			"type": "NATURAL_DISASTERS",
| 			"value": "100000.12"
| 		}
| 	],
| 	"assistances": ["TWENTY_FOUR_KEYCHAIN"],
| 	"customer": {
| 		"phoneNumber": "111",
| 		"gender": "M",
| 		"dateOfBirth": "1990-01-01",
| 		"name": "João",
| 		"email": "joao@gmail.com",
| 		"documentNumber": "1234",
| 		"type": "A"
| 	}
| }

< HTTP/1.1 201 
< spanId: a4592f09c77b2d22
< traceId: 57f54ac90d863098aa2d4ecbf01d6516
< Content-Type: application/json

| {
| 	"id": "aec58fbe-5597-495c-9526-25df4b3db5ac",
| 	"insurancePolicyId": null,
| 	"productId": "7e8a8a99-80ba-4f40-aab5-6608ef9dafed",
| 	"offerId": "8452c5f3-f508-413e-b146-5e34cf1badaa",
| 	"category": "HOME",
| 	"totalMonthlyPremiumAmount": 100.74,
| 	"totalCoverageAmount": 100000.12,
| 	"coverages": [
| 		{
| 			"type": "NATURAL_DISASTERS",
| 			"value": 100000.12
| 		}
| 	],
| 	"assistances": [
| 		"TWENTY_FOUR_KEYCHAIN"
| 	],
| 	"customer": {
| 		"documentNumber": "1234",
| 		"name": "João",
| 		"type": "A",
| 		"gender": "M",
| 		"dateOfBirth": "1990-01-01",
| 		"email": "joao@gmail.com",
| 		"phoneNumber": "111"
| 	},
| 	"createdAt": "2024-08-11T17:05:30.8881233",
| 	"updatedAt": null
| }
```

#### Consulta da cotação
```
> GET /insurances-quotes/aec58fbe-5597-495c-9526-25df4b3db5ac HTTP/1.1
> Host: localhost:8080

< spanId: 1b7b4d01e5cc3c37
< traceId: ccaa8c6f7046d1d694c2dd75329afa39
< Content-Type: application/json

| {
| 	"id": "aec58fbe-5597-495c-9526-25df4b3db5ac",
| 	"insurancePolicyId": "1634474c-f60a-4cae-b159-546e81bea4ac",
| 	"productId": "7e8a8a99-80ba-4f40-aab5-6608ef9dafed",
| 	"offerId": "8452c5f3-f508-413e-b146-5e34cf1badaa",
| 	"category": "HOME",
| 	"totalMonthlyPremiumAmount": 100.74,
| 	"totalCoverageAmount": 100000.12,
| 	"coverages": [
| 		{
| 			"type": "NATURAL_DISASTERS",
| 			"value": 100000.12
| 		}
| 	],
| 	"assistances": [
| 		"TWENTY_FOUR_KEYCHAIN"
| 	],
| 	"customer": {
| 		"documentNumber": "1234",
| 		"name": "João",
| 		"type": "A",
| 		"gender": "M",
| 		"dateOfBirth": "1990-01-01",
| 		"email": "joao@gmail.com",
| 		"phoneNumber": "111"
| 	},
| 	"createdAt": "2024-08-11T17:05:30.888123",
| 	"updatedAt": "2024-08-11T17:05:31.084887"
| }
```

# Observability

As seguintes soluções e recursos foram utilizados para prover observabilidade à aplicação:

### Spring Boot Actuator

O Spring Boot Actuator é um conjunto de recursos adicionais oferecidos pelo Spring Boot para facilitar a monitorização e
gestão de uma aplicação Spring Boot em execução. Ele fornece endpoints que expõem informações sobre a aplicação,
como métricas, informações de saúde, informações sobre propriedades de configuração, entre outros.

### Micrometer

O Micrometer fornece um facade para os sistemas de observability mais populares, permitindo a instrumentação do
código da aplicação baseada em JVM.

Assim como o Spring Boot Actuator, trata-se de um conjunto de dependências adicionadas ao pom.xml da aplicação.

### Opentelemetry collector

O OpenTelemetry Collector (otel collector) oferece uma implementação independente de fornecedor sobre como receber,
processar e exportar dados de telemetria. Ele reduz a necessidade de executar, operar e manter vários agentes/coletores.
Sendo assim, a aplicação é configurada para enviar dados de telemtria apenas para o otel collector, que por sua vez,
às envia para os sistemas de telemetria selecionados.

A configuração dos receivers e exporters estão definidos no arquivo ./infra/cfg/otel-collector.yml. Nele, as seguintes
soluções de telemetria foram configuradas:
- Métricas: Prometheus
- Tracing: Zipkin e Tempo
- Logs: Loki

### Prometheus

O Prometheus é um conjunto de ferramentas de monitoramento de sistemas e alerta de código aberto. Ele coleta e armazena
métricas como dados de séries temporais, ou seja, as informações de métricas são armazenadas com o carimbo de data e
hora no qual foram registradas, juntamente com pares chave-valor opcionais chamados rótulos.

Devido algumas métricas fornecidas pelo Opentelemetry collector divergirem das oferecidas pela biblioteca do Micrometer,
optou-se nessa solução pelo uso de ambas na alimentação de métricas ao Prometheus.

Para acessar o Prometheus: [http://localhost:9090/](http://localhost:9090/)

Home:

<img alt="Prometheus Home" src="./img/prometheus_home.png">

### Zipkin

O Zipkin é um sistema de tracing distribuído. Ele auxilia na coleta de dados temporais necessários para solucionar
problemas de latência em arquiteturas de serviços. Na arquitetura de solução proposta da aplicação, ele é alimentado
pelo Opentelemetry collector.

Aqui, ele foi selecionado em paralelo ao ``Tempo`` devido sua interface mais amigável, enquanto o ``Tempo`` oferece
uma integração mais natural com o Grafana.

Para acessar o Zipkin: [http://localhost:9411/zipkin/](http://localhost:9411/zipkin/)

Home:

<img alt="Zipkin Home" src="./img/zipkin_home.png">

Consulta geral realizada:

<img alt="Zipkin Run Query" src="./img/zipkin_run_query.png">

Tracing selecionado:

<img alt="Zipkin Show" src="./img/zipkin_show.png">

### Loki

Loki é um sistema de agregação de logs escalável, altamente disponível e multi-tenant.

Na solução adotada nessa aplicação, ele é alimentado pelo Opentelemetry collector e os logs propriamente dito podem
ser acessados no Grafana:

Para consultar os logs no Loki:

1. Acessar a opção ``Explore`` no Grafana:

<img alt="Grafana Menu Explore" src="./img/grafana_menu_explore.png">

2. Na página de ``Explore``, selecionar o Loki como datasource e a aplicação no job:

<img alt="Loki Datasource" src="./img/grafana_loki_filter.png">

3. Exemplo de resultado:

<img alt="Loki Result" src="./img/grafana_loki_result.png">

### Tempo

O Grafana Tempo é um sistema de tracing distribuído.

Aqui, ele é utilizado em paralelo ao Zipkin devido sua fácil integraçaõ com o Grafana e Loki, permitindo em um único
sistema a integração entre dashboards, traces e logs. É alimentado pelo Opentelemetry collector e os traces podem
ser acessados no Grafana:

Para consultar os traces no Tempo:

1. Acessar a opção ``Explore`` no Grafana:

<img alt="Grafana Menu Explore" src="./img/grafana_menu_explore.png">

2. (A) Na página de ``Explore``, na aba ``Search``, selecionar o Tempo como datasource e a aplicação no Service Name:

<img alt="Tempo Datasource" src="./img/grafana_tempo_filter.png">

2. (B) Na página de ``Explore``, na aba ``TraceQL``, selecionar o Tempo como datasource e informar um traceId:

<img alt="Tempo Datasource 2" src="./img/grafana_tempo_filter_2.png">

3. Exemplo de resultado:

<img alt="Tempo Result" src="./img/grafana_tempo_result.png">

4. No resultado, é possível identificar os logs associados ao tracing clicando na opção ``Logs for this span``:

<img alt="Tempo Loki Integration" src="./img/grafana_tempo_loki_integration.png">

### Grafana

Grafana é uma plataforma de análise e monitoramento de código aberto que integra dados de diversas fontes em uma
interface visual personalizável.

Nessa solução, utilizamos o Grafana para centralizar as soluções de observability: Loki para logs, Tempo para tracing e
Prometheus para métricas.

Adicionalmente, foram criados 2 dashboards: JVM Micrometer com os dados básicos da saúde da JVM e Requisições HTTP para
métricas de processamento da API.

Para acessar o Grafana: [http://localhost:3000](http://localhost:3000).

1. Dashboard JVM Micrometer:

<img alt="JVM Micrometer" src="./img/grafana_dash_jvm_micrometer.png">

2. Dashboard Requisições HTTP:

<img alt="Requisições HTTP" src="./img/grafana_dash_http.png">
