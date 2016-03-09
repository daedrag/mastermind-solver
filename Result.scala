/**
 * Created by me on 2/29/2016.
 */
case class Result(guess: String, gold: Int, silver: Int) {

  override def equals(o: scala.Any): Boolean = o match {
    case r: Result => this.gold == r.gold && this.silver == r.silver
    case _ => false
  }

  def isMatched(): Boolean = guess.length != 0 && guess.length == gold

}

object Result {
  def of(guess: String, secret: String): Result = {
    if (guess.length != secret.length) {
      new Result(guess, 0, 0)
    } else {
      var gold = 0
      var silver = 0
      for (i <- 0 to secret.length -1) {
        if (guess.charAt(i) == secret.charAt(i)) gold = gold + 1
        else if (secret.contains(guess.charAt(i))) silver = silver + 1
      }
      new Result(guess, gold, silver)
    }
  }

}
