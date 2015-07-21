import akka.actor._
import com.github.wakfutcp.Protocol.Domain._
import com.github.wakfutcp.Protocol.Input._
import com.github.wakfutcp.Protocol.Output._
import com.github.wakfutcp._

object MarketSniffer {
  def props = Props[MarketSniffer]
}

class MarketSniffer extends Actor with Stash with Messenger {

  import context._

  def receive = {
    case ServerList(servers, _) =>
      sender() ! ServerChoice(servers.find(_.name == "Nox").get)
    case CharacterList(characters) =>
      sender() ! CharacterChoice(characters.find(_.name == "Derp").get)
    case ConnectedToWorld() =>
      sender() ! wrap(InteractiveElementActionMessage(12224, 12))
    case MarketConsultResultMessage(sales, count) =>
      sender() ! wrap(MarketConsultRequestMessage(Array.empty, -1, -1, -1, -1, 10.toShort, lowestMode = false))
      become(receiveAndAsk(sales.toList, 10))
  }

  def receiveAndAsk(entries: List[MarketEntry], start: Int): Receive = {
    case MarketConsultResultMessage(sales, count) =>
      val next = start + 10
      if (next >= count)
        handleData(entries ::: sales.toList)
      else {
        sender() ! wrap(MarketConsultRequestMessage(Array.empty, -1, -1, -1, -1, next.toShort, lowestMode = false))
        become(receiveAndAsk(entries ::: sales.toList, next))
      }
  }

  def handleData(data: List[MarketEntry]) = {
    // stuff
    stop(self)
  }
}