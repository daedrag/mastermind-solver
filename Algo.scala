import scala.collection.parallel.mutable

/**
 * Created by me on 2/29/2016.
 */
class Algo(level: Int) {

  private val FULL_SOURCE: String = "0123456789abcdefghijklmnopqrstuvwxyz"
  val SOURCE_LEN = level + 9
  val SECRET_LEN = level + 3
  val SOURCE = FULL_SOURCE.substring(0, SOURCE_LEN)

  private val secret: String = generateSecret()
  private var gameOver: Boolean = false
  def getSecret(): String = { gameOver = true; secret }

  private def generateSecret(): String = {
    def generateSecret0(s: String, result: String): String =
      if (result.length == SECRET_LEN) {
        result
      } else {
        val i = scala.util.Random.nextInt(s.length)
        val c = s.charAt(i)
        generateSecret0(s.substring(0, i) + s.substring(i + 1, s.length), result + c)
      }

    generateSecret0(SOURCE, "")
  }

  val SECRET_POOL_SIZE = 500 * 1000
  var possibleSecrets: collection.mutable.Seq[String] = collection.mutable.Seq()
  var pendingPrefix: collection.mutable.Set[String] = collection.mutable.SortedSet[String]()
  var pendingSuffix: collection.mutable.Map[String, Seq[String]] = collection.mutable.Map()

  // initial prefix & suffix
  pendingPrefix += ""
  pendingSuffix += "" -> collection.mutable.Seq(SOURCE)

  private def filter(previousResults: Seq[Result]): Seq[String] = {
    possibleSecrets.filter(s => Result.of(s, secret).isMatched())
  }

  def iteratePending(): Unit = {
    if (pendingPrefix.isEmpty) return

    pendingPrefix.foreach(pre => {
      if (possibleSecrets.size > SECRET_POOL_SIZE) return

      println(pre)

      val suf: Seq[String] = pendingSuffix(pre)

      pendingPrefix -= pre
      pendingSuffix -= pre

      if (pre.length < SECRET_LEN - 1) {
        suf.foreach(s => {
          for (i <- 0 to s.length - 1) {
            val newPre = pre + s.charAt(i)
            pendingPrefix += newPre
            val newSuf: Seq[String] = pendingSuffix.getOrElse(newPre, Seq[String]()) :+ (s.substring(0, i) + s.substring(i + 1, s.length))
            pendingSuffix.put(newPre, newSuf)
          }
        })
      }
      else if (pre.length == SECRET_LEN - 1) {
//        suf.foreach(s => {
//          s.map(c => pre + c).foreach(possibleSecrets :+ _)
//        })

        possibleSecrets = possibleSecrets ++ suf.map(s => s.map(c => pre + c)).fold[Seq[String]](Seq())((s1, s2) => s1 ++ s2)

        println("Adding new set to possible secret...")
      }
    })

  }
}
