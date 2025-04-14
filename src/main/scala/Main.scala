import zio.{ZIO, ZIOAppDefault}

object MyApp extends ZIOAppDefault {

  def run = myAppLogic

  val myAppLogic =
    for {
      env    <- Reader.layer("src/main/resources/input.txt").build
      lines     <- env.get[Reader].getLines
      _ <- ZIO.log(lines.mkString("\n")) // working
//      _ <- ZIO.succeed(lines.map(list => ZIO.log(list.mkString("\n"))))

    } yield ()
}

