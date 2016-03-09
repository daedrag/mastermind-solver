/**
 * Created by me on 2/29/2016.
 */
object MasterMind {

  def main(args: Array[String]): Unit = {
    val level = 5
    val algo: Algo = new Algo(5)

    println(algo.SOURCE)
    println(algo.getSecret())

    val r1: Result = Result("guess1", 1, 5)
    val r2: Result = Result("guess2", 1, 5)
    println(r1 == r2)

    val secret = algo.getSecret()
    val guess = "25379cb8"
    println(Result.of(guess, secret))

    var stop = false
    while (!stop) {
      algo.iteratePending()

      if (algo.possibleSecrets.size > algo.SECRET_POOL_SIZE) {
        println("Clearing pool size.. " + algo.possibleSecrets.size)
        println("Pending prefix size.. " + algo.pendingPrefix.size)
        algo.possibleSecrets = collection.mutable.Seq.empty
      }

      if (algo.pendingPrefix.isEmpty) {
        stop = true
      }
    }

    println("Pending prefix: " + algo.pendingPrefix.size)
    println("Secret pool size: " + algo.possibleSecrets.size)
  }
}
