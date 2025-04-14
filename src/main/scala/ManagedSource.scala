import zio.{IO, RIO, RLayer, Scope, Task, TaskLayer, UIO, ULayer, ZIO, ZLayer}

import java.nio.file.Paths
import scala.collection.mutable.ListBuffer
import scala.io.{BufferedSource, Source}

//final case class Reader(source: RIO[Scope, BufferedSource]) {
//
//  val getLines: RIO[Scope, Unit] =
//    for {
//      s <- source
//      lines = s.getLines().toList
//      _ <- ZIO.log(lines.mkString("\n"))
//  } yield ()
////    source.map(_.getLines().toList)
//}

//object Reader {
//
//  val layer: String => TaskLayer[Reader] =
//    path => ManagedSource.layer(path) >>> ManagedSource.readerLayer
//
//  val getLines: RIO[Reader, Unit] =
//    ZIO.scoped {
//      ZIO.serviceWithZIO[Reader](_.getLines)
//    }
//}


// 1
final case class ManagedSource(path: String) {
  
  val listBuffer = new ListBuffer[String]
  
  private val acquireSource: Task[BufferedSource] =
    ZIO.attempt(Source.fromFile(Paths.get(path).toFile))

  private val releaseSource: BufferedSource => UIO[Any] =
    s => ZIO.succeed(s.close())

  val source: RIO[Scope, BufferedSource] =
    ZIO.acquireReleaseWith(acquireSource)(releaseSource){ s =>
      val lines = s.getLines().toList
      listBuffer ++= lines
      ZIO.succeed(s)
    }
}

object ManagedSource {
  val layer: String => ULayer[ManagedSource] =
    path => ZLayer.succeed(ManagedSource(path))


//  val readerLayer: RLayer[ManagedSource, Reader] =
//    ZLayer {
//      ZIO.service[ManagedSource].map(ms => Reader(ms.source))
//    }
}


