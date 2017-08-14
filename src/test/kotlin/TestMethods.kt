import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class TestMethods {
    fun testSendMessageToClient(
            client: Client?,
            message: String?,
            email: String? = null,
            shouldBeInvoked: Boolean = false
    ) {
        var invoked = false
        sendMessageToClient(client, message, object : Mailer {
            override fun sendMessage(actualEmail: String, actualMessage: String) {
                invoked = true
                assertEquals(
                        message, actualMessage, "The message is not as expected:")
                assertEquals(
                        email, actualEmail, "The email is not as expected:")
            }
        })
        assertEquals(shouldBeInvoked, invoked, "The function 'sendMessage' should${if (shouldBeInvoked) "" else "n't"} be invoked")
    }

    @Test fun everythingIsOk() {
        testSendMessageToClient(Client(PersonalInfo("bob@gmail.com")),
                "Hi Bob! We have an awesome proposition for you...",
                "bob@gmail.com",
                true)
    }

    @Test fun noMessage() {
        testSendMessageToClient(Client(PersonalInfo("bob@gmail.com")), null)
    }

    @Test fun noEmail() {
        testSendMessageToClient(Client(PersonalInfo(null)), "Hi Bob! We have an awesome proposition for you...")
    }

    @Test fun noPersonalInfo() {
        testSendMessageToClient(Client(null), "Hi Bob! We have an awesome proposition for you...")
    }

    @Test fun noClient() {
        testSendMessageToClient(null, "Hi Bob! We have an awesome proposition for you...")
    }

    class ShopBuilder(val name: String) {
        val customers = arrayListOf<Customer>()
        fun build(): Shop = Shop(name, customers)
    }

    fun shop(name: String, init: ShopBuilder.() -> Unit): Shop = ShopBuilder(name).apply(init).build()

    fun ShopBuilder.customer(name: String, city: City, init: CustomerBuilder.() -> Unit) {
        val customer = CustomerBuilder(name, city)
        customer.init()
        customers.add(customer.build())
    }

    class CustomerBuilder(val name: String, val city: City) {
        val orders = arrayListOf<Order>()

        fun build(): Customer = Customer(name, city, orders)
    }

    fun CustomerBuilder.order(isDelivered: Boolean, vararg products: Product) {
        orders.add(Order(products.toList(), isDelivered))
    }

    fun CustomerBuilder.order(vararg products: Product) = order(true, *products)

    //products
    val idea = Product("IntelliJ IDEA Ultimate", 199.0)
    val reSharper = Product("ReSharper", 149.0)
    val dotTrace = Product("DotTrace", 159.0)
    val dotMemory = Product("DotTrace", 129.0)
    val dotCover = Product("DotCover", 99.0)
    val appCode = Product("AppCode", 99.0)
    val phpStorm = Product("PhpStorm", 99.0)
    val pyCharm = Product("PyCharm", 99.0)
    val rubyMine = Product("RubyMine", 99.0)
    val webStorm = Product("WebStorm", 49.0)
    val teamCity = Product("TeamCity", 299.0)
    val youTrack = Product("YouTrack", 500.0)

    //customers
    val lucas = "Lucas"
    val cooper = "Cooper"
    val nathan = "Nathan"
    val reka = "Reka"
    val bajram = "Bajram"
    val asuka = "Asuka"
    val riku = "Riku"

    //cities
    val Canberra = City("Canberra")
    val Vancouver = City("Vancouver")
    val Budapest = City("Budapest")
    val Ankara = City("Ankara")
    val Tokyo = City("Tokyo")

    val shopT = Shop("name", listOf(Customer("name", Ankara, listOf(Order(listOf(reSharper), true)))))

    val shop = shop("test shop") {
        customer(lucas, Canberra) {
            order(reSharper)
            order(reSharper, dotMemory, dotTrace)
        }
        customer(cooper, Canberra) {}
        customer(nathan, Vancouver) {
            order(rubyMine, webStorm)
        }
        customer(reka, Budapest) {
            order(isDelivered = false, products = idea)
            order(isDelivered = false, products = idea)
            order(idea)
        }
        customer(bajram, Ankara) {
            order(reSharper)
        }
        customer(asuka, Tokyo) {
            order(idea)
        }
        customer(riku, Tokyo) {
            order(phpStorm, phpStorm)
            order(phpStorm)
        }
    }

    val customers: Map<String, Customer> = shop.customers.associateBy(Customer::name)

    val orderedProducts = setOf(idea, reSharper, dotTrace, dotMemory, rubyMine, webStorm, phpStorm)

    val sortedCustomers = listOf(cooper, nathan, bajram, asuka, lucas, riku, reka).map { customers[it] }

    val groupedByCities = mapOf(
            Canberra to listOf(lucas, cooper),
            Vancouver to listOf(nathan),
            Budapest to listOf(reka),
            Ankara to listOf(bajram),
            Tokyo to listOf(asuka, riku)
    ).mapValues { it.value.map { name -> customers[name] } }

    @Test fun testSetOfCustomers() {
        assertEquals(customers.values.toSet(), shop.getSetOfCustomers())
    }

    @Test fun testCitiesCustomersAreFrom() {
        assertEquals(setOf(Canberra, Vancouver, Budapest, Ankara, Tokyo), shop.getCitiesCustomersAreFrom())
    }

    /**
     * Returns the list of the customers who live in the city 'city'
     */
    @Test fun testCustomersFromCity() {
        assertEquals(listOf(customers[lucas], customers[cooper]), shop.getCustomersFrom(Canberra))
    }

    @Test fun testCustomerIsFromCity() {
        assertTrue(customers[lucas]!!.isFrom(Canberra))
        assertFalse(customers[lucas]!!.isFrom(Budapest))
    }

    @Test fun testAllCustomersAreFromCity() {
        assertFalse(shop.checkAllCustomersAreFrom(Canberra))
    }

    @Test fun testAnyCustomerIsFromCity() {
        assertTrue(shop.hasCustomerFrom(Canberra))
    }

    @Test fun testCountCustomersFromCity() {
        assertEquals(2, shop.countCustomersFrom(Canberra))
    }

    @Test fun testAnyCustomerFromCity() {
        assertEquals(customers[lucas], shop.findAnyCustomerFrom(Canberra))
        assertEquals(null, shop.findAnyCustomerFrom(City("Chicago")))
    }

    @Test fun testGetOrderedProductsSet() {
        assertEquals(setOf(idea), customers[reka]!!.orderedProducts)
    }

    @Test fun testGetAllOrderedProducts() {
        assertEquals(orderedProducts, shop.allOrderedProducts)
    }

    @Test fun testCustomerWithMaximumNumberOfOrders() {
        assertEquals(customers[reka], shop.getCustomerWithMaximumNumberOfOrders())
    }

    @Test fun testTheMostExpensiveOrderedProduct() {
        assertEquals(rubyMine, customers[nathan]!!.getMostExpensiveOrderedProduct())
    }

    @Test fun testGetCustomersSortedByNumberOfOrders() {
        assertEquals(sortedCustomers, shop.getCustomersSortedByNumberOfOrders())
    }

    @Test fun testGetTotalOrderPrice() {
        assertEquals(148.0, customers[nathan]!!.getTotalOrderPrice())
    }

    @Test fun testTotalPriceForRepeatedProducts() {
        assertEquals(586.0, customers[lucas]!!.getTotalOrderPrice())
    }

    @Test fun testZipNameAndCustomer() {
        assertEquals(customers.toList(), shop.zipNameAndCustomer())
    }

    @Test fun testAssociateByNameAndCustomer() {
        assertEquals(customers, shop.associateCustomersByName())
    }

    @Test fun testGroupCustomersByCity() {
        assertEquals(groupedByCities, shop.groupCustomersByCity())
    }

    @Test fun testGetCustomersWhoHaveMoreUndeliveredOrdersThanDelivered() {
        assertEquals(setOf(customers[reka]), shop.getCustomersWithMoreUndeliveredOrdersThanDelivered())
    }

    @Test fun testGetProductsOrderedByAllCustomers() {
        val testShop = shop("test shop for 'fold'") {
            customer(lucas, Canberra) {
                order(idea)
                order(webStorm)
            }
            customer(reka, Budapest) {
                order(idea)
                order(youTrack)
            }
        }
        assertEquals(setOf(idea), testShop.getSetOfProductsOrderedByEveryCustomer())
    }

    @Test fun testMostExpensiveDeliveredProduct() {
        val testShop = shop("test shop for 'most expensive delivered product'") {
            customer(lucas, Canberra) {
                order(isDelivered = false, products = idea)
                order(reSharper)
            }
        }
        assertEquals(reSharper, testShop.customers[0].getMostExpensiveDeliveredProduct())
    }

    @Test fun testNumberOfTimesEachProductWasOrdered() {
        assertEquals(4, shop.getNumberOfTimesProductWasOrdered(idea))
    }

    @Test fun testNumberOfTimesEachProductWasOrderedForRepeatedProduct() {
        assertEquals(
                3, shop.getNumberOfTimesProductWasOrdered(reSharper), "A customer may order a product for several times")
    }

    @Test fun testNumberOfTimesEachProductWasOrderedForRepeatedInOrderProduct() {
        assertEquals(
                3, shop.getNumberOfTimesProductWasOrdered(phpStorm), "An order may contain a particular product more than once")
    }

    @Test fun testCollectionOfOneElement() {
        doTest(listOf("a"), listOf("a"))
    }

    @Test fun testEmptyCollection() {
        doTest(null, listOf())
    }

    @Test fun testSimpleCollection() {
        doTest(listOf("a", "c"), listOf("a", "bb", "c"))
    }

    @Test fun testCollectionWithEmptyStrings() {
        doTest(listOf("", "", "", ""), listOf("", "", "", "", "a", "bb", "ccc", "dddd"))
    }

    @Test fun testCollectionWithTwoGroupsOfMaximalSize() {
        doTest(listOf("a", "c"), listOf("a", "bb", "c", "dd"))
    }

    private fun doTest(expected: Collection<String>?, argument: Collection<String>) {
        assertEquals(
                expected, doSomethingStrangeWithCollection(argument), "The function 'doSomethingStrangeWithCollection' should do at least something with a collection:")
    }
}