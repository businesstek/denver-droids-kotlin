/**
 * SECTION 1
 *
 * Elvis Operator and Nullability
 */

class Client(val personalInfo: PersonalInfo?)

class PersonalInfo(val email: String?)

interface Mailer {
    fun sendMessage(email: String, message: String)
}

// Rewrite Java method 'SampleJavaMethods.sendMessageToClient' in Kotlin
fun sendMessageToClient(client: Client?, message: String?, mailer: Mailer) {
    TODO()
}


/**
 * SECTION 2
 *
 * Let's Get Functional
 */

data class Shop(val name: String, val customers: List<Customer>)

data class Customer(val name: String, val city: City, val orders: List<Order>)

data class Order(val products: List<Product>, val isDelivered: Boolean)

data class Product(val name: String, val price: Double)

data class City(val name: String)

// Return a set containing all the customers of this shop
// Hint: List 'to' a Set
fun Shop.getSetOfCustomers(): Set<Customer> {
    TODO()
//    return this.customers
}

// Return the set of cities the customers are from
// Hint: Filter map
fun Shop.getCitiesCustomersAreFrom(): Set<City> {
    TODO()
}

// Return a list of the customers who live in the given city
// Hint: Filter map
fun Shop.getCustomersFrom(city: City): List<Customer> {
    TODO()
}

// Return true if the customer is from the given city
// Hint: Predicates
fun Customer.isFrom(city: City): Boolean {
    TODO()
}

// Return true if all customers are from the given city
// Hint: Predicates
fun Shop.checkAllCustomersAreFrom(city: City): Boolean {
    TODO()
}

// Return true if there is at least one customer from the given city
// Hint: Predicates
fun Shop.hasCustomerFrom(city: City): Boolean {
    TODO()
}

// Return the number of customers from the given city
// Hint: Predicates
fun Shop.countCustomersFrom(city: City): Int {
    TODO()
}

// Return a customer who lives in the given city, or null if there is none
// Hint: Predicates
fun Shop.findAnyCustomerFrom(city: City): Customer? {
    TODO()
}

// Return all products that were ordered by customer
// Hint: Flat map
val Customer.orderedProducts: Set<Product> get() {
    TODO()
}

// Return all products that were ordered by at least one customer
// Hint: Flat map
val Shop.allOrderedProducts: Set<Product> get() {
    TODO()
}

// Return a customer whose order count is the highest among all customers
// Hint: Max/Min
fun Shop.getCustomerWithMaximumNumberOfOrders(): Customer? {
    TODO()
}

// Return the most expensive product which has been ordered
// Hint: Max/Min
fun Customer.getMostExpensiveOrderedProduct(): Product? {
    TODO()
}

// Return a list of customers, sorted by the ascending number of orders they made
// Hint: Sort
fun Shop.getCustomersSortedByNumberOfOrders(): List<Customer> {
    TODO()
}

// Return the sum of prices of all products that a customer has ordered.
// Note: a customer may order the same product for several times.
// Hint: Sum
fun Customer.getTotalOrderPrice(): Double {
    TODO()
}

// Find the correspondence between customers and their names (as a list or pairs)
// Hint: Zip
fun Shop.zipNameAndCustomer(): List<Pair<String, Customer>> {
    TODO()
}

// Find the correspondence between customers and their names (as a map)
// Hint: AssociateBy
fun Shop.associateCustomersByName(): Map<String, Customer> {
    TODO()
}

// Return a map of the customers living in each city
// Hint: GroupBy
fun Shop.groupCustomersByCity(): Map<City, List<Customer>> {
    TODO()
}

// Return customers who have more undelivered orders than delivered
// Hint: Partition
fun Shop.getCustomersWithMoreUndeliveredOrdersThanDelivered(): Set<Customer> {
    TODO()
}

// Return the set of products ordered by every customer
// Hint: Fold
fun Shop.getSetOfProductsOrderedByEveryCustomer(): Set<Product> {
    return customers.fold(allOrderedProducts, {
        orderedByAll, customer ->
        TODO()
    })
}

// Return the most expensive product among all delivered products
// (use the Order.isDelivered flag)
// Hint: CompoundTasks
fun Customer.getMostExpensiveDeliveredProduct(): Product? {
    TODO()
}

// Return the number of times the given product was ordered.
// Note: a customer may order the same product for several times.
// Hint: CompoundTasks
fun Shop.getNumberOfTimesProductWasOrdered(product: Product): Int {
    TODO()
}

// Rewrite SampleJavaMethods.doSomethingStrangeWithCollection function to Kotlin.
fun doSomethingStrangeWithCollection(collection: Collection<String>): Collection<String>? {

    val groupsByLength = collection.groupBy { s -> TODO() }

    return groupsByLength.values.maxBy { group -> TODO() }
}

/**
 * SECTION 3
 *
 * No More Hints!
 *
 * Hint: Don't confuse Order from above with TaxiOrder here
 *
 * That's your last hint!
 */
class TaxiPark(
        val allDrivers: List<Driver>,
        val allPassengers: List<Passenger>,
        val orders: List<TaxiOrder>)

data class Driver(val name: String)
data class Passenger(val name: String)

data class TaxiOrder(
        val driver: Driver,
        val passengers: Collection<Passenger>,
        val duration: Int,
        val distance: Double,
        val discount: Double? = null
) {
    val cost: Double
        get() = (1 - (discount ?: 0.0)) * (duration + distance)
}

// Find all the drivers that completed no orders
fun TaxiPark.findFakeDrivers(): Set<Driver> =
        TODO()

// Find all passengers having more than 'minTrips' number of trips
fun TaxiPark.findFaithfulPassengers(minTrips: Int): List<Passenger> =
        TODO()

// Find all passengers that were taken by this driver more than once
fun TaxiPark.findFrequentPassengers(driver: Driver): List<Passenger> =
        TODO()

// Find passengers that used discounts for majority of the trips
fun TaxiPark.findSmartPassengers(): Set<Passenger> =
        TODO()

// Find the most frequent trip interval duration (or several if there are many)
// among 0-9 minutes, 10-19 minutes, 20-29 minutes etc.
// If there's no duration info, return empty list.
fun TaxiPark.findTheMostFrequentTripDurations(): List<IntRange> {
    TODO()
}

// Check whether 20% of the drivers make 80% of the profit
fun TaxiPark.checkParetoPrinciple(): Boolean {
    TODO()
}

/**
 * Section 4
 *
 * There is no Section 4.
 *
 * If you want to continue, clone this git repo and follow the lessons here "student/advanced/src/main..."
 *
 * https://github.com/JetBrains/kotlin-workshop
 */