/**
 * Created by withus on 11/18/15.
 *
 * Personal playground to start learning Scala.
 * Use this file to test random stuff
 */
object Playhere {
  def main(args: Array[String]) {

    //define case classes to shorten the definition (automatic add: setters, getters, toStrings, toHash)
    case class a(bar: Int, bars: String)
    case class b(bar: String, bars: Int)

    //Structural typing (only wizards use this shit)
    def foo(a: {val bar: Any}) = a.bar match {
      case bar: String => bar + " foo was called " + bar
      case bar: Int => bar * 2 * bar
      case _ => "Unknown data type"
    }
    //static typing (mehh so static..)
    def fooo(arg: a): Int = arg.bar * 2 * arg.bar
    def foooo(arg: b): String = arg.bar + " foo was called " + arg.bar

    //duck typing (i hope everything goes right ~ the python way)
    //def foooo(myClass) = myClass match {
    //  case myClass : b => myClass.bar + " foo was called " +  bar
    //  case myClass : a => myClass.bar * 2 * bar
    //  case _ => "Unknown data type"
    //}

    val c = a(5, "Hello")
    val d = b("hello", 5)

    //I am not sure i am going to use this so i am doing the lazy approach
    lazy val e = a(15, "Goodbye")

    println(foo(c))
    println(foo(d))

    //anonymous functions as a parameter
    //Notice here we specify the return type (which is not necessary but recommended)
    def koo(handler: (Int, Int) => Int): Int = handler(5, 4)

    println(koo((x, y) => x + y))

    //We are using e after all
    println(e)


    //Moving on folks. Scala can get pretty intelligible
    //We need to specify return type Int (To use the + method: Int.+(Int))
    val f4: (Int, Int) => Int = _ + _
    //You thought it could not get worse?
    val f5: (Int, Int) => Int = (_.+(_))
    //This should be 10
    println(f4(5, 5))

    //---------------------------------------------------------------------------------
    //--------------------cycles, generators and the yield keyword---------------------
    //---------------------------------------------------------------------------------

    val la: List[Int] = List(1, 2, 3)
    val lb: List[String] = List("First", "Second", "Third")
    val lz: List[Int] = List(3, 2, 1)
    //zip lists is useful
    val lc = la zip lb

    //custom zipping?
    val ld = for {
      (a, b) <- la zip lb
    } yield (b, a - 1)

    println(lc)
    println(ld)

    //Lets try to get smart
    var le = for {
      x <- la
      y <- lb
      z <- lz
      if (x.equals(z))}
      yield (x, y, z)

    println("you can call the equal method: " + le)

    le = for {
      x <- la;
      y <- lb;
      z <- lz;
      if (x != z)}
      yield (x, y, z)

    println(" == is also a method in scala: " + le)

    //---------------------------------------------------------------------------------
    //------------------Optional values!? Better than Java 8 Optional?-----------------
    //---------------------------------------------------------------------------------

    //Option can be distinguished between some and none
    def showoptionally(x: Option[String]) = x match {
      case Some(s) => s
      case None => "I got nothing m8, sorry."
    }

    //list.lift returns an option
    //Get third element
    println(showoptionally(lb.lift(2)))
    //Get fifth element (Ops, out of range)
    println(showoptionally(lb.lift(4)))

    //Random operators are random
    val an = 1 :: 2 :: 3 :: Nil
    val other = 4 :: 5 :: 6 :: Nil
    val another = an ::: other

    println(another)

    //Testing option encapsulation
    case class Order(lineItem: Option[LineItem])
    case class LineItem(product: Option[Product])
    case class Product(name: String)

    val myProduct = Product("This is a sample product")
    val myLineItem = LineItem(Some(myProduct))
    val myOrder = Order(Some(myLineItem))
    val maybeOrder = Some(myOrder)

    val didThisReallyWork = for {
      order <- maybeOrder
      lineItem <- order.lineItem
      product <- lineItem.product
    } yield product.name

    println("Order has product: " + didThisReallyWork.get)

    //---------------------------------------------------------------------------------
    //-------------------------What about hiding class members-------------------------
    //---------------------------------------------------------------------------------
    class Time {

      private[this] var h = 12
      private[this] var m = 12

      def hour: Int = h

      def hour_=(x: Int) {
        //Perform internal assertions
        require(0 <= x && x < 24)
        h = x
      }

      def minute = m

      def minute_=(x: Int) {
        //Perform internal assertions
        require(0 <= x && x < 60)
        m = x
      }
    }


    //TODO: need more insight into covariant and contravariant
    trait build[+B] {
      def startBuilding(b: String )
    }

    trait prettyprinter[-A] {
      def pprint(a: A): String
    }

    case class House(name: String)

    case class builder(name: String)

  }
}

