import zio.{ZIO, ZIOAppDefault}

object MyApp extends ZIOAppDefault {

  def run = myAppLogic

  val myAppLogic =
    for {
      env <- ManagedSource.layer("src/main/resources/input.txt").build
      _ <- env.get[ManagedSource].source
      lines = env.get[ManagedSource].listBuffer.toList
      _ <-  ZIO.log(lines.mkString("\n")) 
      
//      env    <- Reader.layer("src/main/resources/input.txt").build
//      lines     <- env.get[Reader].getLines
//      _ <- ZIO.log(lines.mkString("\n")) // working
//      _ <- ZIO.succeed(lines.map(list => ZIO.log(list.mkString("\n"))))

    } yield ()
}

