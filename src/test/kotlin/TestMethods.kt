import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class TestMethods {
    fun testSendMessageToClient(client: Client?,
                                message: String?,
                                email: String? = null,
                                shouldBeInvoked: Boolean = false) {
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

    @Test
    fun everythingIsOk() {
        testSendMessageToClient(Client(PersonalInfo("bob@gmail.com")),
                "Hi Bob! We have an awesome proposition for you...",
                "bob@gmail.com",
                true)
    }

    @Test
    fun noMessage() {
        testSendMessageToClient(Client(PersonalInfo("bob@gmail.com")), null)
    }

    @Test
    fun noEmail() {
        testSendMessageToClient(Client(PersonalInfo(null)), "Hi Bob! We have an awesome proposition for you...")
    }

    @Test
    fun noPersonalInfo() {
        testSendMessageToClient(Client(null), "Hi Bob! We have an awesome proposition for you...")
    }

    @Test
    fun noClient() {
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

    @Test
    fun testSetOfCustomers() {
        assertEquals(customers.values.toSet(), shop.getSetOfCustomers())
    }

    @Test
    fun testCitiesCustomersAreFrom() {
        assertEquals(setOf(Canberra, Vancouver, Budapest, Ankara, Tokyo), shop.getCitiesCustomersAreFrom())
    }

    /**
     * Returns the list of the customers who live in the city 'city'
     */
    @Test
    fun testCustomersFromCity() {
        assertEquals(listOf(customers[lucas], customers[cooper]), shop.getCustomersFrom(Canberra))
    }

    @Test
    fun testCustomerIsFromCity() {
        assertTrue(customers[lucas]!!.isFrom(Canberra))
        assertFalse(customers[lucas]!!.isFrom(Budapest))
    }

    @Test
    fun testAllCustomersAreFromCity() {
        assertFalse(shop.checkAllCustomersAreFrom(Canberra))
    }

    @Test
    fun testAnyCustomerIsFromCity() {
        assertTrue(shop.hasCustomerFrom(Canberra))
    }

    @Test
    fun testCountCustomersFromCity() {
        assertEquals(2, shop.countCustomersFrom(Canberra))
    }

    @Test
    fun testAnyCustomerFromCity() {
        assertEquals(customers[lucas], shop.findAnyCustomerFrom(Canberra))
        assertEquals(null, shop.findAnyCustomerFrom(City("Chicago")))
    }

    @Test
    fun testGetOrderedProductsSet() {
        assertEquals(setOf(idea), customers[reka]!!.orderedProducts)
    }

    @Test
    fun testGetAllOrderedProducts() {
        assertEquals(orderedProducts, shop.allOrderedProducts)
    }

    @Test
    fun testCustomerWithMaximumNumberOfOrders() {
        assertEquals(customers[reka], shop.getCustomerWithMaximumNumberOfOrders())
    }

    @Test
    fun testTheMostExpensiveOrderedProduct() {
        assertEquals(rubyMine, customers[nathan]!!.getMostExpensiveOrderedProduct())
    }

    @Test
    fun testGetCustomersSortedByNumberOfOrders() {
        assertEquals(sortedCustomers, shop.getCustomersSortedByNumberOfOrders())
    }

    @Test
    fun testGetTotalOrderPrice() {
        assertEquals(148.0, customers[nathan]!!.getTotalOrderPrice())
    }

    @Test
    fun testTotalPriceForRepeatedProducts() {
        assertEquals(586.0, customers[lucas]!!.getTotalOrderPrice())
    }

    @Test
    fun testZipNameAndCustomer() {
        assertEquals(customers.toList(), shop.zipNameAndCustomer())
    }

    @Test
    fun testAssociateByNameAndCustomer() {
        assertEquals(customers, shop.associateCustomersByName())
    }

    @Test
    fun testGroupCustomersByCity() {
        assertEquals(groupedByCities, shop.groupCustomersByCity())
    }

    @Test
    fun testGetCustomersWhoHaveMoreUndeliveredOrdersThanDelivered() {
        assertEquals(setOf(customers[reka]), shop.getCustomersWithMoreUndeliveredOrdersThanDelivered())
    }

    @Test
    fun testGetProductsOrderedByAllCustomers() {
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

    @Test
    fun testMostExpensiveDeliveredProduct() {
        val testShop = shop("test shop for 'most expensive delivered product'") {
            customer(lucas, Canberra) {
                order(isDelivered = false, products = idea)
                order(reSharper)
            }
        }
        assertEquals(reSharper, testShop.customers[0].getMostExpensiveDeliveredProduct())
    }

    @Test
    fun testNumberOfTimesEachProductWasOrdered() {
        assertEquals(4, shop.getNumberOfTimesProductWasOrdered(idea))
    }

    @Test
    fun testNumberOfTimesEachProductWasOrderedForRepeatedProduct() {
        assertEquals(
                3, shop.getNumberOfTimesProductWasOrdered(reSharper), "A customer may order a product for several times")
    }

    @Test
    fun testNumberOfTimesEachProductWasOrderedForRepeatedInOrderProduct() {
        assertEquals(
                3, shop.getNumberOfTimesProductWasOrdered(phpStorm), "An order may contain a particular product more than once")
    }

    @Test
    fun testCollectionOfOneElement() {
        doTest(listOf("a"), listOf("a"))
    }

    @Test
    fun testEmptyCollection() {
        doTest(null, listOf())
    }

    @Test
    fun testSimpleCollection() {
        doTest(listOf("a", "c"), listOf("a", "bb", "c"))
    }

    @Test
    fun testCollectionWithEmptyStrings() {
        doTest(listOf("", "", "", ""), listOf("", "", "", "", "a", "bb", "ccc", "dddd"))
    }

    @Test
    fun testCollectionWithTwoGroupsOfMaximalSize() {
        doTest(listOf("a", "c"), listOf("a", "bb", "c", "dd"))
    }

    private fun doTest(expected: Collection<String>?, argument: Collection<String>) {
        assertEquals(
                expected, doSomethingStrangeWithCollection(argument), "The function 'doSomethingStrangeWithCollection' should do at least something with a collection:")
    }

    internal val drivers = (1..10).map { Driver("Driver$it") }
    internal val passengers = (1..10).map { Passenger("Passenger$it") }

    internal fun driver(i: Int) = drivers.getOrNull(i - 1) ?: throw IllegalArgumentException("Invalid driver index: $i")
    internal fun passenger(i: Int) = passengers.getOrNull(i - 1) ?: throw IllegalArgumentException("Invalid passenger index: $i")

    internal fun drivers(range: IntRange) = range.map(this::driver)
    internal fun drivers(vararg indices: Int) = indices.map(this::driver)
    internal fun passengers(range: IntRange) = range.map(this::passenger)
    internal fun passengers(vararg indices: Int) = indices.map(this::passenger)

    internal fun taxiPark(driverIndexes: IntRange, passengerIndexes: IntRange, orders: List<TaxiOrder>) =
            TaxiPark(drivers(driverIndexes), passengers(passengerIndexes), orders.toList())

    internal fun taxiPark(driverIndexes: IntRange, passengerIndexes: IntRange, vararg orders: TaxiOrder) =
            taxiPark(driverIndexes, passengerIndexes, orders.toList())

    internal fun taxiOrder(driverIndex: Int, vararg passengerIndexes: Int, duration: Int = 10, distance: Double = 10.0, discount: Double? = null) =
            TaxiOrder(driver(driverIndex), passengers(*passengerIndexes), duration, distance, discount)

    @Test
    fun testFakeDrivers() {
        val park = taxiPark(driverIndexes = 1..3, passengerIndexes = 1..2,
                orders = listOf(taxiOrder(driverIndex = 1, passengerIndexes = 1), taxiOrder(1, 2)))
        assertEquals(drivers(2, 3).toSet(), park.findFakeDrivers())
    }

    @Test
    fun testNoFakeDrivers() {
        val park = taxiPark(1..2, 1..2, taxiOrder(1, 1), taxiOrder(2, 2))
        assertTrue(park.findFakeDrivers().isEmpty())
    }

    @Test
    fun testFaithfulPassengersMoreThanMin() {
        val park = taxiPark(1..1, 1..2,
                taxiOrder(1, 1), taxiOrder(1, 2), taxiOrder(1, 2))
        assertEquals(passengers(2), park.findFaithfulPassengers(1))
    }

    @Test
    fun testNoFaithfulPassengers() {
        val park = taxiPark(1..1, 1..2,
                taxiOrder(1, 1), taxiOrder(1, 2))
        assertTrue(park.findFaithfulPassengers(1).isEmpty())
    }

    @Test
    fun testFaithfulPassenger() {
        val park = taxiPark(1..2, 1..3,
                taxiOrder(1, 2), taxiOrder(1, 2), taxiOrder(2, 2), taxiOrder(2, 2))
        assertEquals(passengers(2), park.findFaithfulPassengers(3))
    }

    @Test
    fun testFaithfulPassengers() {
        val park = taxiPark(1..3, 1..5,
                taxiOrder(1, 1), taxiOrder(2, 1), taxiOrder(1, 4), taxiOrder(3, 4), taxiOrder(1, 5), taxiOrder(2, 5), taxiOrder(2, 2))
        assertEquals(passengers(1, 4, 5), park.findFaithfulPassengers(1))
    }

    @Test
    fun testOnlyPair() {
        val park = taxiPark(1..1, 1..1, taxiOrder(1, 1), taxiOrder(1, 1))
        assertEquals(passengers(1), park.findFrequentPassengers(driver(1)))
    }

    @Test
    fun testFrequentPassengers() {
        val park = taxiPark(1..2, 1..4, taxiOrder(1, 1), taxiOrder(1, 1), taxiOrder(1, 1, 3), taxiOrder(1, 3), taxiOrder(1, 2), taxiOrder(2, 2))
        assertEquals(passengers(1, 3), park.findFrequentPassengers(driver(1)))
    }

    @Test
    fun testNoFrequentPassengers() {
        val park = taxiPark(1..2, 1..4, taxiOrder(1, 1), taxiOrder(1, 1), taxiOrder(1, 1, 3), taxiOrder(1, 3), taxiOrder(1, 2), taxiOrder(2, 2))
        assertTrue(park.findFrequentPassengers(driver(2)).isEmpty())
    }

    @Test
    fun testSmartPassengers() {
        val park = taxiPark(1..2, 1..2, taxiOrder(1, 1, discount = 0.1), taxiOrder(2, 2))
        assertEquals(passengers(1).toSet(), park.findSmartPassengers())
    }

    @Test
    fun testMoreThenMajorityDiscountTrips() {
        val park = taxiPark(1..1, 1..1, taxiOrder(1, 1, discount = 0.1), taxiOrder(1, 1, discount = 0.2), taxiOrder(1, 1))
        assertEquals(passengers(1).toSet(), park.findSmartPassengers())
    }

    @Test
    fun testLessThenMajorityDiscountTrips() {
        val park = taxiPark(1..1, 1..1, taxiOrder(1, 1), taxiOrder(1, 1), taxiOrder(1, 1, discount = 0.2), taxiOrder(1, 1))
        assertTrue(park.findSmartPassengers().isEmpty())
    }

    @Test
    fun testNoDurationInfo() {
        assertTrue(taxiPark(1..1, 1..1).findTheMostFrequentTripDurations().isEmpty())
    }

    @Test
    fun testSeveralFrequent() {
        val park = taxiPark(1..1, 1..1, taxiOrder(1, 1, duration = 11), taxiOrder(1, 1, duration = 12),
                taxiOrder(1, 1, duration = 25), taxiOrder(1, 1, duration = 26))
        assertEquals(listOf(10..19, 20..29), park.findTheMostFrequentTripDurations())
    }

    @Test
    fun testTheMostFrequentTripDuration() {
        val park = taxiPark(1..3, 1..5, taxiOrder(1, 1, duration = 10), taxiOrder(3, 4, duration = 30),
                taxiOrder(1, 2, duration = 20), taxiOrder(2, 3, duration = 30))
        assertEquals(listOf(30..39), park.findTheMostFrequentTripDurations())
    }

    @Test
    fun testParetoPrincipleSucceeds() {
        // 20% of the drivers: 1
        // the profit of the first driver: 500
        // the profit of all: 620
        // 500 >= 0.8 * 620 = 496
        val park = taxiPark(1..8, 1..8, taxiOrder(1, 1, duration = 250, distance = 250.0),
                taxiOrder(2, 2), taxiOrder(3, 3), taxiOrder(4, 4), taxiOrder(5, 5), taxiOrder(6, 6), taxiOrder(7, 7))
        assertTrue(park.checkParetoPrinciple())
    }

    @Test
    fun testRandomDriverIsTheBest() {
        // the same as before, the best driver is now #5
        val park = taxiPark(1..8, 1..8, taxiOrder(5, 1, duration = 250, distance = 250.0),
                taxiOrder(2, 2), taxiOrder(3, 3), taxiOrder(4, 4), taxiOrder(6, 6), taxiOrder(7, 7), taxiOrder(8, 8))
        assertTrue(park.checkParetoPrinciple())
    }

    @Test
    fun testSeveralBestDrivers() {
        // 20% of the drivers: 1, 10
        // the profit of 1, 10: 800
        // the profit of all: 940
        // 800 >= 0.8 * 940 = 752
        val park = taxiPark(1..10, 1..10,
                taxiOrder(1, 1, duration = 200, distance = 200.0),
                taxiOrder(10, 10, duration = 200, distance = 200.0),
                taxiOrder(2, 2), taxiOrder(3, 3), taxiOrder(4, 4), taxiOrder(5, 5), taxiOrder(6, 6), taxiOrder(7, 7), taxiOrder(8, 8))
        assertTrue(park.checkParetoPrinciple())
    }

    @Test
    fun testNotEnoughDrivers() {
        // the first driver doesn't make up 20%
        val park = taxiPark(1..4, 1..4, taxiOrder(1, 1, duration = 110, distance = 110.0),
                taxiOrder(2, 2), taxiOrder(3, 3), taxiOrder(4, 4))
        assertFalse(park.checkParetoPrinciple())
    }

    @Test
    fun testParetoPrincipleFails() {
        // 20% of the drivers: 1
        // the profit of the first driver: 220
        // the profit of all: 300
        // 220 < 0.8 * 300 = 240
        val park = taxiPark(1..5, 1..5, taxiOrder(1, 1, duration = 110, distance = 110.0),
                taxiOrder(2, 2), taxiOrder(3, 3), taxiOrder(4, 4), taxiOrder(5, 5))
        assertFalse(park.checkParetoPrinciple())
    }

    @Test
    fun testExactly80Percent() {
        // 20% of the drivers: 1
        // the profit of the first driver: 240
        // the profit of all: 300
        // 240 >= 0.8 * 300 = 240

        // tip: Use BigDecimal instead of double for comparison
        val park = taxiPark(1..5, 1..5, taxiOrder(1, 1, duration = 120, distance = 120.0),
                taxiOrder(2, 2), taxiOrder(3, 3), taxiOrder(4, 4))
        assertFalse(park.checkParetoPrinciple())
    }
}
