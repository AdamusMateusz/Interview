# CDQ Interview Task


## Decision made
1. Job api always returns response when task exists. When `pattern` is not matching `input` the response clearly states that no match was found.
2. When job with given id does not exist, `404 Not Found` is returned
3. If `input text` or `pattern` is missing - request is rejected

## Caution

Application is simulating errors. Every 20th message is retried and every 100th message is rejected.
Exceptions like the one below are normal behavior to show how the application handles exceptions.
```text
2023-05-09T13:44:55.332Z ERROR 1 --- [onPool-worker-2] c.c.i.t.e.receiver.TasksQueueListener    : Error occurred: 
2023-05-09T13:44:55.333855802Z 
2023-05-09T13:44:55.333858532Z com.cdq.interview.task.execution.mapper.RetryMessageException: Simulated exception. This is expected behaviour
2023-05-09T13:44:55.333860922Z 	at com.cdq.interview.task.execution.receiver.impl.RetryingCommandProcessor.process(RetryingCommandProcessor.java:37) ~[classes!/:1.0.0-SNAPSHOT]
2023-05-09T13:44:55.333863452Z 	Suppressed: reactor.core.publisher.FluxOnAssembly$OnAssemblyException: 
2023-05-09T13:44:55.333865592Z Assembly trace from producer [reactor.core.publisher.MonoIgnoreThen] :
2023-05-09T13:44:55.333867852Z 	reactor.core.publisher.Mono.then
2023-05-09T13:44:55.333869882Z 	com.cdq.interview.task.execution.receiver.impl.RetryingCommandProcessor.process(RetryingCommandProcessor.java:38)
2023-05-09T13:44:55.333871992Z Error has been observed at the following site(s):
2023-05-09T13:44:55.333874002Z 	*__Mono.then ⇢ at com.cdq.interview.task.execution.receiver.impl.RetryingCommandProcessor.process(RetryingCommandProcessor.java:38)
2023-05-09T13:44:55.333877002Z 	*__Mono.then ⇢ at com.cdq.interview.task.execution.receiver.impl.PercentageCountingCommandProcessor.process(PercentageCountingCommandProcessor.java:35)
2023-05-09T13:44:55.333879202Z 	*__Mono.then ⇢ at com.cdq.interview.task.execution.receiver.impl.PercentageCountingCommandProcessor.process(PercentageCountingCommandProcessor.java:35)
2023-05-09T13:44:55.333881372Z 	|_           ⇢ at com.cdq.interview.task.execution.receiver.impl.LoggingTaskCommandProcessor.process(LoggingTaskCommandProcessor.java:17)
2023-05-09T13:44:55.333883502Z Original Stack Trace:
2023-05-09T13:44:55.333885472Z 		at com.cdq.interview.task.execution.receiver.impl.RetryingCommandProcessor.process(RetryingCommandProcessor.java:37) ~[classes!/:1.0.0-SNAPSHOT]
2023-05-09T13:44:55.333898542Z 		at com.cdq.interview.task.execution.receiver.impl.PercentageCountingCommandProcessor.process(PercentageCountingCommandProcessor.java:35) ~[classes!/:1.0.0-SNAPSHOT]
2023-05-09T13:44:55.333900612Z 		at com.cdq.interview.task.execution.receiver.impl.RejectingCommandProcessor.process(RejectingCommandProcessor.java:41) ~[classes!/:1.0.0-SNAPSHOT]
2023-05-09T13:44:55.333902442Z 		at com.cdq.interview.task.execution.receiver.impl.PercentageCountingCommandProcessor.process(PercentageCountingCommandProcessor.java:35) ~[classes!/:1.0.0-SNAPSHOT]
2023-05-09T13:44:55.333905642Z 		at com.cdq.interview.task.execution.receiver.impl.LoggingTaskCommandProcessor.process(LoggingTaskCommandProcessor.java:17) ~[classes!/:1.0.0-SNAPSHOT]
2023-05-09T13:44:55.333908312Z 		at com.cdq.interview.task.execution.receiver.TasksQueueListener.executeCommand(TasksQueueListener.java:46) ~[classes!/:1.0.0-SNAPSHOT]
2023-05-09T13:44:55.333910882Z 		at com.cdq.interview.task.execution.receiver.TasksQueueListener.lambda$connect$0(TasksQueueListener.java:37) ~[classes!/:1.0.0-SNAPSHOT]
2023-05-09T13:44:55.333914522Z 		at reactor.core.publisher.FluxFlatMap$FlatMapMain.onNext(FluxFlatMap.java:386) ~[reactor-core-3.5.5.jar!/:3.5.5]
2023-05-09T13:44:55.333916512Z 		at reactor.core.publisher.FluxCreate$BufferAsyncSink.drain(FluxCreate.java:814) ~[reactor-core-3.5.5.jar!/:3.5.5]
2023-05-09T13:44:55.333918272Z 		at reactor.core.publisher.FluxCreate$BufferAsyncSink.next(FluxCreate.java:739) ~[reactor-core-3.5.5.jar!/:3.5.5]
2023-05-09T13:44:55.333920022Z 		at reactor.core.publisher.FluxCreate$SerializedFluxSink.next(FluxCreate.java:161) ~[reactor-core-3.5.5.jar!/:3.5.5]
2023-05-09T13:44:55.333921782Z 		at reactor.rabbitmq.Receiver.lambda$null$9(Receiver.java:216) ~[reactor-rabbitmq-1.5.6.jar!/:1.5.6]
2023-05-09T13:44:55.333923532Z 		at com.rabbitmq.client.impl.recovery.AutorecoveringChannel$2.handleDelivery(AutorecoveringChannel.java:588) ~[amqp-client-5.16.0.jar!/:5.16.0]
2023-05-09T13:44:55.333925342Z 		at com.rabbitmq.client.impl.ConsumerDispatcher$5.run(ConsumerDispatcher.java:149) ~[amqp-client-5.16.0.jar!/:5.16.0]
2023-05-09T13:44:55.333927112Z 		at com.rabbitmq.client.impl.ConsumerWorkService$WorkPoolRunnable.run(ConsumerWorkService.java:111) ~[amqp-client-5.16.0.jar!/:5.16.0]
2023-05-09T13:44:55.333928892Z 		at java.base/java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1136) ~[na:na]
2023-05-09T13:44:55.333930612Z 		at java.base/java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:635) ~[na:na]
2023-05-09T13:44:55.333932352Z 		at java.base/java.lang.Thread.run(Thread.java:833) ~[na:na]
```


## Commands

`docker-compose up --build`