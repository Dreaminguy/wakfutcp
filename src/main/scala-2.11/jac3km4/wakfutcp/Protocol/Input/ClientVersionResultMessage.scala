package jac3km4.wakfutcp.Protocol.Input

import java.nio.ByteBuffer

import jac3km4.wakfutcp.Protocol.Domain.Version
import jac3km4.wakfutcp.Protocol.{InputMessage, InputMessageReader}

case class ClientVersionResultMessage
(
  success: Boolean,
  required: Version
  ) extends InputMessage

object ClientVersionResultMessage
  extends InputMessageReader[ClientVersionResultMessage] {

  def read(buf: ByteBuffer) =
    ClientVersionResultMessage(
      buf.get == 1,
      Version.read(buf)
    )
}