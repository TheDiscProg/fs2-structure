# FS2 Structured Main Program

## Why?
Usually, we structure the main program using Cats-Effect, mainly using the `Resource` class.
However, the drawback is that we can't use `flatMap` and, if we are using Streams, as for RabbitMQ
and Kafka, these will need to be compiled and drained.

This is an alternative, where we use FS2 Streams. See [article by Michal Matloka](https://blog.softwaremill.com/scala-application-lifecycle-from-pure-scala-to-fs2-5f2861005603)
