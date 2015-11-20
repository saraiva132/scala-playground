import akka.actor.{Actor,Props}
import akka.event.Logging
import scala.collection.mutable.ListBuffer

/**
 * Created by saraiva132 on 11/18/15.
 *
 * Personal playground to start learning Scala.
 * Use this file to test random stuff
 */
object Playhere {
  def main(args: Array[String]) {

    //--------------------------First and foremost! The underscore _ --------------------------
    println("---------------------------------------------------------------------------------")
    println("First and foremost! The underscore _")
    println
    //-----------------------------------------------------------------------------------------

    println("Can you name all uses of _? (Is this how to learn scala?" )
    println
    println("import scala._                      Wild card -- all of Scala is imported")
    println("import scala.{ Predef => _, _ }     Exception, everything except Predef")
    println("def f[M[_]]                         Higher kinded type parameter")
    println("def f(m: M[_])                      Existential type")
    println("_ + _                               Anonymous function placeholder parameter")
    println("m _                                 Eta expansion of method into method value")
    println("m(_)                                Partial function application")
    println("_ => 5                              Discarded parameter")
    println("case _ =>                           Wild card pattern -- matches anything")
    println("  val (a, _) = (1, 2)               same thing")
    println("  for (_ <- 1 to 10)                same thing")
    println("    f(xs: _*)                       Sequence xs is passed as multiple parameters to f(ys: T*)")
    println("case Seq(xs @ _*)                   Identifier xs is bound to the whole matched sequence")
    println("var i: Int = _                      Initialization to the default value")
    println("def abc_<>!                         An underscore must separate alphanumerics from symbols on identifiers")
    println("t._2                                Part of a method name, such as tuple getters")


    //This all looks the same but is quite quite different

    //Try to call this using sho(Array[String]("and","be","disappointed"))
    def sho(x : Array[Any]) = x.length

    //This actually works but basically you are inferring a specific type when you really do not care
    def shoo[T](x : Array[T]) = x.length

    //voila! existential types!
    // Array[_] equals  Array[T] forSome { type T}
    def shooo(x : Array[_]) = x.length

    //This looks cool. Is this enough? NO! _ is dangerous because it does not allow scoping
    //define scopes to allow a range of classes but ensure functionality (all charsequences have length)
    def shoooo(x : Array[Q] forSome { type Q <: CharSequence}) = x.foreach(y => println("Word length:" + y.length))

    println("My existential array has length: " + shooo(Array[String]("This","has","size","four")))


    shoooo(Array[String]("This","has","size","four"))

    //-----------------------Structural vs static vs duck typing-----------------------
    println("---------------------------------------------------------------------------------")
    println("Structural vs static vs duck typing")
    println
    //---------------------------------------------------------------------------------

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
    println("Up until now e had value a = <lazy>, now: " + e)

    //Moving on folks. Scala can get pretty intelligible
    //We need to specify return type Int (To use the + method: Int.+(Int))
    val f4: (Int, Int) => Int = _ + _
    //You thought it could not get worse?
    val f5: (Int, Int) => Int = (_.+(_))
    //This should be 10
    println(f4(5, 5))

    //--------------------cycles, generators and the yield keyword---------------------
    println("---------------------------------------------------------------------------------")
    println("cycles, generators and the yield keyword")
    println
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
      //gotta love how scala doesn't give a shit about parenthesis. This equals: if (x.equals(z))
      if x equals z
    } yield (x, y, z)

    println("you can call the equals method: " + le)

    le = for {
      x <- la;
      y <- lb;
      z <- lz;
      if (x != z)
    } yield (x, y, z)

    println(" == is also a method in scala: " + le)

    //------------------Optional values!? Better than Java 8 Optional?-----------------
    println("---------------------------------------------------------------------------------")
    println("Optional values!? Better than Java 8 Optional?")
    println
    //---------------------------------------------------------------------------------

    //Option can be distinguished between some and none
    def showoptionally(x: Option[String]) = x match {
      case Some(s) => s
      case None => "I got nothing m8, sorry."
    }

    //list.lift returns an option
    //Get third element
    println(showoptionally(lb lift (2)))
    //Get fifth element (Ops, out of range)
    println(showoptionally(lb lift (4)))

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

    //-------------------------What about hiding class members-------------------------
    println("---------------------------------------------------------------------------------")
    println("What about hiding class members")
    println
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

    //TODO: need more insight into covariant and contravariant patterns
    trait build[+B] {
      def startBuilding(b: String)
    }

    trait prettyprinter[-A] {
      def pprint(a: A): String
    }

    case class House(name: String)

    case class builder(name: String)

    //List[+T] is covariant so it allows a subtype List[Int] to be assigned to a supertype List[Any]
    val xs = List(1, 2, 3)
    val xz: List[Any] = xs

    //------------Playing with classes. How to pretend that i know what i am doing-------------
    println("---------------------------------------------------------------------------------")
    println("Playing with classes. How to pretend that i know what i am doing")
    println
    //---------------------------------------------------------------------------------

    //define an abstract class and some base traits
    trait Food {
      def amIfood = println("I am food!")
    }
    trait Edible {
      def amIedible = println("I am also currently edible! Eat me fast please!")
    }
    abstract class Animal {
      type CanEat <: Food

      def eat(food: CanEat) = food.amIfood
    }

    //define concrete types of food
    class Grass extends Food
    class Meat extends Food with Edible {
      //super.amIfood gets called first
      override def amIfood = amIedible;
      super.amIfood
    }

    //animal implementation defines what is food and what is not
    //Cows eat grass, Dogs eat meat
    class Cow extends Animal {
      type CanEat = Grass

      override def eat(food: Grass) = food.amIfood
    }
    class Dog extends Animal {
      type CanEat = Meat

      override def eat(food: Meat) = food.amIfood
    }

    val betty = new Cow
    betty.eat(new Grass)

    val jimmy = new Dog
    //Since scala methods are also operators and vice-versa. You can do this
    jimmy eat (new Meat)

    //Explicit Animal declaration will make porkie expect a CanEat type and not Grass awww
    val porkie: Animal = new Cow

    //This will return a compile error :( porkie can't eat :(
    //porkie.eat(new Grass)


    //Class scoping
    class Outer {

      class Inner

      //Each Inner class instance is different from the other
      def f(innie: Inner) = println("Got my own innie!")

      //To accept any Inner instance use # operator!
      def g(innie: Outer#Inner) = println("Got some innie. It works!")
    }

    val oi = new Outer
    val bye = new Outer

    oi.f(new oi.Inner)
    oi.g(new bye.Inner)

    //------------------------The implicit keyword. Magic some might say----------------------
    println("---------------------------------------------------------------------------------")
    println("The implicit keyword. Magic some might say")
    println
    //----------------------------------------------------------------------------------------

    //implicit is similar to extension methods (C#, kotlin) which also allow to add new methods to existing classes

    //So one implicit purpose would be to add new behaviour (subtyping) to objects without necessarily exposing it

    implicit def doubleToInt(x: Double) = x.toInt

    //this now works! amazing
    val x: Int = 3.5

    println("My implicit conversion converts 3.5 double to " + x + " Int")

    //OK. SHIT CAN NOT GET MORE COMPLICATED. I MEAN. WHAT IS THERE NOW? VIEW BOUNDS? CONTEXT BOUNDS? PFT PLEASE. OH..


    //A view bound was a mechanism introduced in Scala to enable the use of some type A as if it were some type B.

    //We know Grass can call Food method. What if Grass did not extend Food?
    def f0[Grass <% Food](a: Grass) = a.amIfood

    //Simple example, we can call an ordered class which implements <(other : Grass) : Boolean
    //implicit conversion allows Scala to automatically infer calls without forcing the developer to call with Ordered[Type]
    def f1[Grass <% Ordered[Grass]](a: Grass, b: Grass) = if (a < b) a else b

    //While a view bound can be used with simple types (for example, A <% String), a context bound requires
    // a parameterized type, such as Ordered[A] above, but unlike String.

    //A context bound describes an implicit value instead of view bound's implicit conversion
    //Here, we are obtaining the implicit value(Grass) to give to Ordering when comparing a and b
    def f2[Grass: Ordering](a: Grass, b: Grass) = implicitly[Ordering[Grass]].compare(a, b)


    //---------------------------More stuff. Listbuffer, extractors----------------------------
    println("---------------------------------------------------------------------------------")
    println("More stuff. Listbuffer, extractors")
    println
    //-----------------------------------------------------------------------------------------

    //user Listbuffer to obtain better performance
    val buf = new ListBuffer[Int]
    for (x <- xs) buf += x + 1
    buf.toList

    //extractors have the reserve purpose of injectors

    //apply -> inject object from fields

    //unapply -> extract fields from object


    @volatile
    var hel = 1

    @transient
    var nope = 2

    @deprecated
    var nopenope = 3

    //@serializable... @unchecked

    //---------------------------Scala concurrency: Actors -> Akka ----------------------------
    println("---------------------------------------------------------------------------------")
    println("Scala concurrency: Actors -> Akka")
    println
    //-----------------------------------------------------------------------------------------

    //Stopped for reading: formal languages and context-free languages in Scala (Learning how to do parsers)
    //Link: http://www.artima.com/pins1ed/combinator-parsing.html

    //Stopped for reading:
    //Actor Systems: http://doc.akka.io/docs/akka/snapshot/general/actor-systems.html#actor-systems
    //Supervision and monitoring: http://doc.akka.io/docs/akka/snapshot/general/supervision.html#supervision


    class DumbActor extends Actor {
      val log = Logging(context.system, this)

      def receive = {
        case "test" => log.info("received test")
        case _ => log.info("received unknown message")
      }
    }

    //val props1 = Props[DumbActor]


  }
}

