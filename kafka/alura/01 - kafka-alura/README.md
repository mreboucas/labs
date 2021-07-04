CLasse principal para gerar Orders (Produtor/Dispatcher):

```
NewOrderMain
```

Classe secundárias para ler as informações/gerar outras informações (Consumidores/Service):

```
FraudDetectorService
EmailService
LogService (pega as infos de logs de todos os logs) - Atua como  Consumer(Service)/Producer(Dispatcher)
```

Kafka Dektop manager: <b>Conduktor</b>





