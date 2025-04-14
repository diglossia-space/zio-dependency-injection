import zio.{IO, RIO, RLayer, Scope, Task, TaskLayer, UIO, ULayer, ZIO, ZLayer}

import java.nio.file.Paths
import scala.io.{BufferedSource, Source}

final case class Reader(source: RIO[Scope, BufferedSource]) {

  val getLines: RIO[Scope, List[String]] =
    source.map(_.getLines().toList)
}

object Reader {

  val layer: String => TaskLayer[Reader] =
    path => ManagedSource.layer(path) >>> ManagedSource.readerLayer

  val getLines: RIO[Reader, List[String]] =
    ZIO.scoped {
      ZIO.serviceWithZIO[Reader](_.getLines)
    }
}


// 1
final case class ManagedSource(path: String) {
  private val acquireSource: Task[BufferedSource] =
    ZIO.attempt(Source.fromFile(Paths.get(path).toFile))

  private val releaseSource: BufferedSource => UIO[Any] =
    s => ZIO.succeed(s.close())

  val source: RIO[Scope, BufferedSource] =
    ZIO.acquireRelease(acquireSource)(releaseSource)
}

object ManagedSource {
  val layer: String => ULayer[ManagedSource] =
    path => ZLayer.succeed(ManagedSource(path))


  val readerLayer: RLayer[ManagedSource, Reader] =
    ZLayer {
      ZIO.service[ManagedSource].map(ms => Reader(ms.source))
    }
}


