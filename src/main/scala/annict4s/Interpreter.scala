package annict4s

import httpz.Action
import scalaz.~>

private[annict4s] object Interpreter extends (Command ~> Action) {
  override def apply[A](fa: Command[A]) = fa.action
}
